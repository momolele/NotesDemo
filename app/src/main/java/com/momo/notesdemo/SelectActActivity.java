package com.momo.notesdemo;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

public class SelectActActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_delete,btn_return;
    private ImageView iv_img;
    private TextView tv_text;
    private VideoView vv_video;
    private NotesDB notesDB;
    private SQLiteDatabase dbWrite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_act);
        btn_delete = (Button)findViewById(R.id.btn_delete);
        btn_return = (Button)findViewById(R.id.btn_return);
        iv_img = (ImageView)findViewById(R.id.s_img);
        tv_text = (TextView)findViewById(R.id.s_tv);
        vv_video = (VideoView)findViewById(R.id.s_video);
        btn_delete.setOnClickListener(this);
        btn_return.setOnClickListener(this);
        notesDB = new NotesDB(this);
        dbWrite = notesDB.getWritableDatabase();
        if (getIntent().getStringExtra(NotesDB.PATH).equals("null")){
            iv_img.setVisibility(View.GONE);
        }else {
            iv_img.setVisibility(View.VISIBLE);
        }

        if (getIntent().getStringExtra(NotesDB.VIDEO).equals("null")){
            vv_video.setVisibility(View.GONE);
        }else {
            vv_video.setVisibility(View.VISIBLE);
        }
        tv_text.setText(getIntent().getStringExtra(NotesDB.CONTENT)+getIntent().getStringExtra(NotesDB.TIME));
        Bitmap bitmap = BitmapFactory.decodeFile(getIntent().getStringExtra(NotesDB.PATH));
        iv_img.setImageBitmap(bitmap);
        vv_video.setVideoURI(Uri.parse(getIntent().getStringExtra(NotesDB.VIDEO)));
        vv_video.start();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_delete:
                deleteData();
                finish();
                break;
            case R.id.btn_return:
                finish();
                break;
        }
    }
    public void deleteData(){
        dbWrite.delete(NotesDB.TABLE_NAME,"_id="+getIntent().getIntExtra(NotesDB.ID,0),null);
    }
}
