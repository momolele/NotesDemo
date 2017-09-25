package com.momo.notesdemo;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_text,btn_video,btn_img;
    private ListView lv;
    private Intent intent;
    private MyAdapter adapter;
    private NotesDB notesDB;
    private SQLiteDatabase dbRead;
    private Cursor mCursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        notesDB = new NotesDB(this);
        dbRead = notesDB.getReadableDatabase();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SelectDB();
    }

    private void initView() {
        btn_text = (Button)findViewById(R.id.btn_text);
        btn_video= (Button)findViewById(R.id.btn_video);
        btn_img= (Button)findViewById(R.id.btn_img);
        lv = (ListView)findViewById(R.id.ls_list);
        btn_text.setOnClickListener(this);
        btn_video.setOnClickListener(this);
        btn_img.setOnClickListener(this);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mCursor.moveToPosition(i);
                Intent mIntent = new Intent(MainActivity.this,SelectActActivity.class);
                mIntent.putExtra(NotesDB.ID,mCursor.getInt(mCursor.getColumnIndex(NotesDB.ID)));
                mIntent.putExtra(NotesDB.CONTENT,mCursor.getString(mCursor.getColumnIndex(NotesDB.CONTENT)));
                mIntent.putExtra(notesDB.TIME,mCursor.getString(mCursor.getColumnIndex(NotesDB.TIME)));
                mIntent.putExtra(NotesDB.PATH,mCursor.getString(mCursor.getColumnIndex(NotesDB.PATH)));
                mIntent.putExtra(NotesDB.VIDEO,mCursor.getString(mCursor.getColumnIndex(NotesDB.VIDEO)));
                startActivity(mIntent);
            }
        });
    }


    @Override
    public void onClick(View view) {
        intent = new Intent(this,AddContextActivity.class);
        switch (view.getId()){
            case R.id.btn_text:
                intent.putExtra("flag","1");
                startActivity(intent);
                break;
            case R.id.btn_img:
                intent.putExtra("flag","2");
                startActivity(intent);
                break;
            case R.id.btn_video:
                intent.putExtra("flag","3");
                startActivity(intent);
                break;
        }
    }

    public  void SelectDB(){
        mCursor = dbRead.query(NotesDB.TABLE_NAME,null,null,null,null,null,null);
        adapter = new MyAdapter(this,mCursor);
        lv.setAdapter(adapter);
    }
}
