package Adapter;

import Object.Comment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Constant.Constant;
import Constant.ImageLoaderConfig;
import paditech.com.fifood.R;
import Constant.ExpandableHeightListView;

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
        double time = comment.getTime();
        viewHolder.tvTime.setText(comment.getTime() + "");
        viewHolder.tvComment.setText(comment.getContent());
        viewHolder.tvNickname.setText(comment.getNickname());
        ImageLoaderConfig.imageLoader.displayImage(comment.getUserProfifeImage(), viewHolder.imgAvartar, ImageLoaderConfig.options);
        ImageLoaderConfig.imageLoader.displayImage(comment.getImgUrl(), viewHolder.img, ImageLoaderConfig.options);


        return convertView;
    }

    private class ViewHolder {

        TextView tvNickname, tvComment, tvTime;
        ImageView imgAvartar;
        ImageView img;
    }
}
