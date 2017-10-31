package com.example.android_razrab.contactslist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by android_razrab on 30/10/2017.
 */


    class PhoneNumberAdapter extends BaseAdapter {

        Context context;

    // списки имен и телефонов соответственно
        List names ,phones;

        private static LayoutInflater inflater = null;

        public PhoneNumberAdapter (Context context, List<String> phones , List <String> names) {
            // TODO Auto-generated constructor stub
            this. context = context;
            this. names = names;

            this .phones = phones ;


            inflater = (LayoutInflater) context
                    .getSystemService (Context. LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return phones.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return phones.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }


        // Инициализируем вьюху
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View vi = convertView;
            if (vi == null)
                vi = inflater .inflate (R.layout. item_list_contacts, null);

            // ставим данные
            TextView tv_name = (TextView) vi.findViewById (R.id.tv_name);
            TextView tv_phone_number = (TextView) vi. findViewById( R.id. tv_phone_number);
            tv_phone_number .setText (phones .get (position) .toString());

            tv_name .setText( names . get( position ) .toString() );

            return vi;
        }
    }


