package com.example.android_razrab.contactslist;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;

public class MainActivity extends AppCompatActivity {

    List <String> names ;
    List <String> numbers ;

    ListView listview ;


    static int PERMISSION_REQUEST_CODE = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        getSupportActionBar() .setTitle( "Контакты" );


        requestPerms();


        // Запрашиваем список контактов и имен
      getContactList();


        listview = (ListView) findViewById(R.id.list_view);
        listview.setAdapter(new PhoneNumberAdapter(this, numbers , names));



        // листенер для кликов на айтемы
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                // TODO: 30/10/2017 ставить имя пользователя 
                sendSms(numbers.get(position).toString(), "Name" );

            }
        });
    }




    void sendSms (String number ,String name) {

        // устанавливаем номер телефона адресата
        Uri smsNumber = Uri. parse( "sms:" +number);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent. setData (smsNumber);
        // формируем текст нашего сообщения
        String textMessage = "Пользователь " + name + " приглашает Вас в чудную" +
                " программу ";
      // устанавливаем его . Это сообщение появится внизу , готове к отправке
        intent. putExtra("sms_body", textMessage);
        // запускаем активность
        startActivity (intent);

    }








  // получаем контакты ,которыу
    private void getContactList() {

        names = new ArrayList<>();
        numbers = new ArrayList <>();

        ContentResolver cr = getContentResolver();
        Cursor cur = cr .query( ContactsContract.Contacts .CONTENT_URI,
                null, null, null, null);

        if ( (cur != null ? cur. getCount() : 0) > 0) {
            while (cur != null && cur. moveToNext()) {
                String id = cur. getString (
                        cur. getColumnIndex( ContactsContract. Contacts ._ID));
                String name = cur. getString (cur. getColumnIndex(
                        ContactsContract. Contacts.DISPLAY_NAME));

                if (cur. getInt (cur. getColumnIndex(
                        ContactsContract. Contacts. HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr. query(
                            ContactsContract. CommonDataKinds. Phone. CONTENT_URI,
                            null,
                            ContactsContract. CommonDataKinds. Phone. CONTACT_ID + " = ?",
                            new String[ ] { id }, null);
                    while ( pCur.moveToNext()) {
                        String phoneNo = pCur .getString (pCur. getColumnIndex(
                                ContactsContract. CommonDataKinds. Phone. NUMBER));


                        names . add ( name );
                        numbers .add ( phoneNo );

                        Log.i("TAG", "Name: " + name);
                        Log.i("TAG", "Phone Number: " + phoneNo);
                    }
                    pCur.close();
                }
            }
        }
        if (cur != null) {
            cur .close();
        }

    }

















    /**разрешения всем скопом проверяем*/
    public boolean hasPermissions() {
        int res = 0;
        //string array of permissions,
        String[] perms = new String[]{Manifest.permission.SEND_SMS
                , Manifest.permission.READ_CONTACTS};

        for (String permsn : perms) {
            res = checkCallingOrSelfPermission(permsn);
            if (!(res == PackageManager.PERMISSION_GRANTED)) {
                return false;
            }
        }
        return true;
    }




    /** запрашиваем разрешения если их нет*/
    public void requestPerms(){

        String[] perms = new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_CONTACTS};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(perms,PERMISSION_REQUEST_CODE);
        }

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 1 :
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();

                    Cursor cur =  getContentResolver().query(contactData, null, null, null, null);
                    if (cur.getCount() > 0) {// thats mean some resutl has been found
                        if(cur.moveToNext()) {
                            String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                            String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                            Log.e("Names", name);

                            if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                            {

                                Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,null, null);
                                while (phones.moveToNext()) {
                                    String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                    Log.e("Number", phoneNumber);
                                }
                                phones.close();
                            }

                        }
                    }
                    cur.close();
                }
                break;
        }

    }
}
