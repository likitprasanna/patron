package com.ngo_request.ngo_request;

/**
 * Created by USER on 22-10-2015.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

/**
 * Created by Harrison on 5/23/2015.
 */
public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ListRowViewHolder> {

    private List<ListItems> listItemsList;
    private Context mContext;
    private ImageLoader mImageLoader;

    private int focusedItem = 0;
    private int previousPosition=0;

    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    public MyRecyclerAdapter(Context context, List<ListItems> listItemsList) {
        this.listItemsList = listItemsList;
        this.mContext = context;
    }

    @Override
    public ListRowViewHolder onCreateViewHolder(ViewGroup viewGroup, final int position) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_ngo, null);
        ListRowViewHolder holder = new ListRowViewHolder(v, mContext);


        return holder;
    }


    @Override
    public void onBindViewHolder(final ListRowViewHolder listRowViewHolder, int position) {
        ListItems listItems = listItemsList.get(position);
        listRowViewHolder.itemView.setSelected(focusedItem == position);

        listRowViewHolder.getLayoutPosition();
        listRowViewHolder.title.setText(Html.fromHtml(listItems.getName()));

        previousPosition=position;


    }

    public void clearAdapter() {
        listItemsList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (null != listItemsList ? listItemsList.size() : 0);
    }


    public class ListRowViewHolder extends RecyclerView.ViewHolder {
        protected NetworkImageView thumbnail;
        protected TextView title,time;
        protected RelativeLayout recLayout;
        private FrameLayout frameLayout;
        protected LayoutInflater inflater;


        public ListRowViewHolder(View view, Context context) {
            super(view);
            inflater = LayoutInflater.from(mContext);
            this.title = (TextView) view.findViewById(R.id.name);


        }
    }
}
