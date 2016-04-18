package Adapter;

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

import Constant.Constant;
import Constant.ImageLoaderConfig;
import paditech.com.fifood.R;
import Constant.ExpandableHeightListView;

/**
 * Created by USER on 15/4/2016.
 */
public class ListCommentAdapter extends BaseAdapter implements Constant {
    private JSONArray listComment;

    private Context context;

    public ListCommentAdapter(JSONArray listComment, Context context) {
        this.listComment = listComment;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listComment.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return listComment.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
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
            viewHolder.lvPhoto = (ExpandableHeightListView) convertView.findViewById(R.id.listPhoto);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        try {
            JSONObject comment = listComment.getJSONObject(position);
            double time = comment.getDouble(TIME);
            viewHolder.tvTime.setText(comment.getString(TIME));
            viewHolder.tvComment.setText(comment.getString(CONTENT));
            viewHolder.tvNickname.setText(comment.getString(NICKNAME));

            JSONArray imgs = comment.getJSONArray(FILES);
            if (imgs.length() > 0) {
                viewHolder.lvPhoto.setAdapter(new ListPhotoAdapter(imgs, context));
                viewHolder.lvPhoto.setExpanded(true);
            }

            ImageLoaderConfig.imageLoader.displayImage(comment.getString(USER_PROFILE_IMAGE), viewHolder.imgAvartar, ImageLoaderConfig.options);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    private class ViewHolder {

        TextView tvNickname, tvComment, tvTime;
        ImageView imgAvartar;
        ExpandableHeightListView lvPhoto;
    }
}
