package com.momo.notesdemo;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.VideoView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddContextActivity extends AppCompatActivity implements View.OnClickListener {

    private String val;

    private Button  btn_save,btn_cancle;
    private EditText et_text;
    private ImageView c_img;
    private VideoView c_video;

    private NotesDB notesDB;
    private SQLiteDatabase db;
    private File phoneFile,videoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_context);
        Intent mIntent = getIntent();
        val = mIntent.getStringExtra("flag");
        btn_save = (Button)findViewById(R.id.btn_save);
        btn_cancle = (Button)findViewById(R.id.btn_cancle);
        et_text = (EditText) findViewById(R.id.et_text);
        c_img = (ImageView)findViewById(R.id.c_img);
        c_video = (VideoView) findViewById(R.id.c_video);
        btn_save.setOnClickListener(this);
        btn_cancle.setOnClickListener(this);
        notesDB = new NotesDB(this);
        db = notesDB.getWritableDatabase();
        initView();
    }

    public void initView(){
        if (val.equals("1")){
            c_img.setVisibility(View.GONE);
            c_video.setVisibility(View.GONE);
        }else if (val.equals("2")){
            c_img.setVisibility(View.VISIBLE);
            c_video.setVisibility(View.GONE);
            Intent mIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            phoneFile = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()
                    +"/"+getTime()+".jpg");
            mIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(phoneFile));
            startActivityForResult(mIntent,1);
        }else if (val.equals("3")){
            c_img.setVisibility(View.GONE);
            c_video.setVisibility(View.VISIBLE);
            Intent mIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            videoFile = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()
                    +"/"+getTime()+".mp4");
            mIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(videoFile));
            startActivityForResult(mIntent,2);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_save:
                addDB();
                finish();
                break;
            case R.id.btn_cancle:
                finish();
                break;
        }
    }

    public void addDB(){
        ContentValues cv = new ContentValues();
        cv.put(NotesDB.CONTENT,et_text.getText().toString());
        cv.put(NotesDB.TIME,getTime());
        cv.put(NotesDB.PATH,phoneFile+"");
        cv.put(NotesDB.VIDEO,videoFile+"");
        db.insert(NotesDB.TABLE_NAME,"",cv);

    }

    private String getTime(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date = new Date();
        String time = format.format(date);
        return time;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==1){
            Bitmap bitmap = BitmapFactory.decodeFile(phoneFile.getAbsolutePath());
            c_img.setImageBitmap(bitmap);
        }

        if (requestCode == 2){
            c_video.setVideoURI(Uri.fromFile(videoFile));
            c_video.start();
        }
    }
}
