package Adapter;

import Object.Comment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import Constant.Constant;
import Constant.ImageLoaderConfig;
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

        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_list_comment, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.tvNickname = (TextView) convertView.findViewById(R.id.tvNickname);
            viewHolder.tvComment = (TextView) convertView.findViewById(R.id.tvComment);
            viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
            viewHolder.imgAvartar = (ImageView) convertView.findViewById(R.id.imgAvartar);
            viewHolder.img = (ImageView) convertView.findViewById(R.id.img);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Comment comment = listComment.get(position);
        viewHolder.tvTime.setText(FormatValue.getTimeComment(comment.getTime(), comment.getDateCreated()));
        viewHolder.tvComment.setText(comment.getContent());
        viewHolder.tvNickname.setText(comment.getNickname());
        ImageLoader.getInstance().displayImage(comment.getUserProfifeImage(), viewHolder.imgAvartar, ImageLoaderConfig.options);
        if(comment.getImgUrl()!=null)
            ImageLoaderConfig.imageLoader.displayImage(comment.getImgUrl(), viewHolder.img, ImageLoaderConfig.options);


        return convertView;
    }

    private class ViewHolder {

        TextView tvNickname, tvComment, tvTime;
        ImageView imgAvartar;
        ImageView img;
    }
}
