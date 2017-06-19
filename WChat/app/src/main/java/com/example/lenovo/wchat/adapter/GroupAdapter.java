package com.example.lenovo.wchat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lenovo.wchat.R;
import com.example.lenovo.wchat.callback.IGroupItemClick;
import com.hyphenate.chat.EMGroup;

import java.util.List;

/**
 * Created by Lenovo on 2017/6/19.
 */

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.MyViewHolder> {

    private Context context;
    private List<EMGroup> list;
    private IGroupItemClick iGroupItemClick;

    public void setiGroupItemClick(IGroupItemClick iGroupItemClick) {
        this.iGroupItemClick = iGroupItemClick;
    }

    public GroupAdapter(Context context, List<EMGroup> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recycler_group_list,parent,false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.groupName.setText(list.get(position).getGroupName());
        holder.groupLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iGroupItemClick.groupItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView groupName;
        private View groupLl;

        public MyViewHolder(View itemView) {
            super(itemView);
            groupName = (TextView) itemView.findViewById(R.id.item_group_name);
            groupLl = itemView.findViewById(R.id.item_group_ll);
        }
    }
}
