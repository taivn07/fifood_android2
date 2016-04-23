package Adapter;

import Object.Comment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import Constant.Constant;
import Constant.ImageLoaderConfig;
import paditech.com.fifood_android.PhotoViewPagerActivity;
import paditech.com.fifood_android.R;
import Constant.FormatValue;

/**
 * Created by USER on 15/4/2016.
 */
public class ListCommentAdapter extends BaseAdapter implements Constant {
    private ArrayList<Comment> listComment;

    private Context context;

    public ListCommentAdapter(ArrayList<Comment> listComment, Context context) {
        this.listComment = listComment;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listComment.size();
    }

    @Override
    public Object getItem(int position) {
        return listComment.get(position);

    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final ViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_list_comment, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.tvNickname = (TextView) convertView.findViewById(R.id.tvNickname);
            viewHolder.tvComment = (TextView) convertView.findViewById(R.id.tvComment);
            viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
            viewHolder.imgAvartar = (ImageView) convertView.findViewById(R.id.imgAvartar);
            viewHolder.img = (ImageView) convertView.findViewById(R.id.img);
            viewHolder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Comment comment = listComment.get(position);
        viewHolder.tvTime.setText(FormatValue.getTimeComment(comment.getTime(), comment.getDateCreated()));
        viewHolder.tvComment.setText(comment.getContent());
        viewHolder.tvNickname.setText(comment.getNickname());
        ImageLoader.getInstance().displayImage(comment.getUserProfifeImage(), viewHolder.imgAvartar, ImageLoaderConfig.options);
        if(comment.getImgUrl()!=null) {
            ImageLoaderConfig.imageLoader.displayImage(comment.getImgUrl(), viewHolder.img, ImageLoaderConfig.options, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {
                    viewHolder.progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {
                    viewHolder.progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    viewHolder.progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingCancelled(String s, View view) {
                    viewHolder.progressBar.setVisibility(View.GONE);
                }
            });
            viewHolder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PhotoViewPagerActivity.class);
                    intent.putExtra(URL, comment.getImgUrl());
                    context.startActivity(intent);
                }
            });
        }
        return convertView;
    }

    private class ViewHolder {

        TextView tvNickname, tvComment, tvTime;
        ImageView imgAvartar;
        ImageView img;
        ProgressBar progressBar;
    }
}
