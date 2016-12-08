package com.cst2335_group_final;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class AutoMenuListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_menu_list);
        Log.i(ACTIVITY_NAME, "In onCreate()");
        if(findViewById(R.id.option_detail_container)!= null){
            twoPane = true;
        }

        listView = (ListView)findViewById(R.id.menuListView);
        options = new ArrayList<String>();
        adapter = new DbAdapter(this);
        adapter.open();
        adapter.add();
        cursor = adapter.getRows();
        listView.setAdapter(new AutoMenuCursorAdapter(this, cursor, 0));
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
        if(cursor != null){
            cursor.close();
        }
        if(adapter != null){
            adapter.close();
        }
    }

    protected static final String ACTIVITY_NAME = "AutoMenuListActivity";
    private ListView listView;
    private DbAdapter adapter;
    private Cursor cursor;
    private boolean twoPane;

    public class AutoMenuCursorAdapter extends CursorAdapter {

        public AutoMenuCursorAdapter(Context context, Cursor cursor, int flags){
            super(context, cursor, flags);
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            Log.i(ACTIVITY_NAME, "In newView()");
            View view = inflater.inflate(R.layout.option_layout, null);
            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                }
            });
            return view ;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            Log.i(ACTIVITY_NAME, "In getView()");
            if(convertView == null){
                convertView = inflater.inflate(R.layout.option_layout, parent, false);
            }
            convertView.setTag(position);
            return super.getView(position, convertView, parent);
        }

        @Override
        public void bindView(View view, final Context context, final Cursor cursor) {
            Log.i(ACTIVITY_NAME, "In bindView()");
            String item = cursor.getString(cursor.getColumnIndex(DbAdapter.OPTION));
            options.add(item);
            TextView menuViewOpton = (TextView)view.findViewById(R.id.menuViewOption);
            menuViewOpton.setText(item);
            item = cursor.getString(cursor.getColumnIndex(DbAdapter.DESCRIPTION));
            TextView menuViewDescription = (TextView)view.findViewById(R.id.menuViewDescription);
            menuViewDescription.setText(item);
            int resID = cursor.getInt(cursor.getColumnIndex(DbAdapter.IMAGE));
            ImageView menuImage = (ImageView)view.findViewById(R.id.menuImage);
            menuImage.setImageResource(resID);

            final ListView listView = (ListView)findViewById(R.id.menuListView);

           listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
               @Override
               public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                   Cursor item = (Cursor)listView.getItemAtPosition(position);
                   String stuff = item.getString(item.getColumnIndex(DbAdapter.OPTION));
                   if(stuff.equals("GPS Navigation")){
                       Uri uri = Uri.parse("geo:45.4215,-75.6972");
                       Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                       intent.setPackage("com.google.android.apps.maps");
                       startActivity(intent);
                   }else if(twoPane){
                       Bundle bundle = new Bundle();
                       bundle.putString(MenuOptionDetailFragment.ITEM_ID,stuff );
                       MenuOptionDetailFragment fragment = new MenuOptionDetailFragment();
                       fragment.setArguments(bundle);
                       getSupportFragmentManager().beginTransaction()
                               .replace(R.id.option_detail_container, fragment)
                               .commit();
                   }else{
                       Context context = view.getContext();
                       Intent intent = new Intent(context, MenuOptionDetailActivity.class);
                       intent.putExtra(MenuOptionDetailFragment.ITEM_ID, stuff);
                       context.startActivity(intent);
                   }
               }
           });
        }
        protected static final String ACTIVITY_NAME = "AutoMenuCursorAdapter";
    }

    private LayoutInflater inflater;
    private ArrayList<String> options;

}
