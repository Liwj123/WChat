package com.example.lenovo.wchat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.lenovo.wchat.R;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * 发送图片时弹出的图片选择器，帧布局
 * <p>
 * RecyclerView，checkBox配合使用时有乱序问题
 * 使用boolean类型数组解决，记录每一个checkBox的状态
 * <p>
 * Created by Lenovo on 2017/5/23.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<String> list;
    private HashSet<String> paths = new HashSet<>();
    private boolean[] flag;

    public ImageAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
        flag = new boolean[list.size()];
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_fragment_recycler_image, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        Glide.with(context)
                .load(list.get(position))
                .override(500, 700)
                .into(holder.img);
        holder.check.setOnCheckedChangeListener(null);
        //将数组中的值设置给checkBox
        holder.check.setChecked(flag[position]);
        holder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //使用数组来记录当前的选择框状态
                flag[position] = isChecked;
                if (isChecked) {
                    paths.add(list.get(position));
                } else {
                    holder.check.setChecked(false);
                    paths.remove(list.get(position));
                }
            }
        });
    }

    public HashSet<String> getPaths() {
        return paths;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView img;
        private CheckBox check;

        public MyViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.item_image_img);
            check = (CheckBox) itemView.findViewById(R.id.item_image_check);
        }
    }
    public void refresh(){
        for(int i = 0;i < flag.length;i++){
            flag[i] = false;
        }
        notifyDataSetChanged();
    }
}
