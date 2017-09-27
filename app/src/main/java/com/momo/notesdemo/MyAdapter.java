package com.momo.notesdemo;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by 沫沫 on 2017/9/25.
 */

public class MyAdapter extends BaseAdapter {

    private Context mContext;
    private Cursor mCursor;
    private LinearLayout linearLayout;

    public MyAdapter(Context context,Cursor cursor){
        this.mContext = context;
        this.mCursor = cursor;
    }
    @Override
    public int getCount() {
        return mCursor.getCount();
    }

    @Override
    public Object getItem(int i) {
        return mCursor.getPosition();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if(view == null){
            holder = new ViewHolder();
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            view = layoutInflater.inflate(R.layout.cell,null);
            holder.contexttv = view.findViewById(R.id.list_content);
            holder.timeTv = view.findViewById(R.id.list_time);
            holder.iv_img = view.findViewById(R.id.list_img);
            holder.iv_video = view.findViewById(R.id.list_video);
            view.setTag(holder);
        }else {
            holder = (ViewHolder)view.getTag();
        }
        mCursor.moveToPosition(i);
        String context = mCursor.getString(mCursor.getColumnIndex(NotesDB.CONTENT));
        String time = mCursor.getString(mCursor.getColumnIndex(NotesDB.TIME));
        String url = mCursor.getString(mCursor.getColumnIndex(NotesDB.PATH));
        String video = mCursor.getString(mCursor.getColumnIndex(NotesDB.VIDEO));
        holder.contexttv.setText(context);
        holder.timeTv.setText(time);
        holder.iv_video.setImageBitmap(getVideoThumbnail(video,200,200, MediaStore.Images.Thumbnails.MICRO_KIND));
        holder.iv_img.setImageBitmap(getImageThumbnail(url,200,200));
        return linearLayout;
    }

    private class ViewHolder{
        private TextView contexttv;
        private TextView timeTv;
        private ImageView iv_img;
        private ImageView iv_video;
    }

    public Bitmap getImageThumbnail(String uri,int width,int height){
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        bitmap = BitmapFactory.decodeFile(uri,options);
        options.inJustDecodeBounds = false;
        int beWidth = options.outWidth/width;
        int beHeight = options.outHeight/height;
        int be = 1;
        if (beHeight >beWidth){
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0){
            be =1;
        }
        options.inSampleSize = be;
        bitmap = BitmapFactory.decodeFile(uri,options);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap,width,height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }



    private Bitmap getVideoThumbnail(String uri,int width,int height,int kind){
        Bitmap bitmap = null;
        bitmap = ThumbnailUtils.createVideoThumbnail(uri,kind);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap,width,height,ThumbnailUtils.OPTIONS_RECYCLE_INPUT);

        return bitmap;
    }
}
