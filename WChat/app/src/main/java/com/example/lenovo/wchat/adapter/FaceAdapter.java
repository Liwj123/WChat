package com.example.lenovo.wchat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.lenovo.wchat.R;
import com.example.lenovo.wchat.callback.IFaceItemClick;
import com.example.lenovo.wchat.model.FaceBean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Created by Lenovo on 2017/5/26.
 */

public class FaceAdapter extends RecyclerView.Adapter<FaceAdapter.MyAdapter> {

    private IFaceItemClick onFaceItemClick;
    private Context context;
    private ArrayList<FaceBean> list;

    public void setonFaceItemClick(IFaceItemClick faceItemClick) {
        this.onFaceItemClick = faceItemClick;
    }

    public FaceAdapter(Context context, ArrayList<FaceBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MyAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyAdapter(LayoutInflater.from(context).inflate(R.layout.item_fragment_recycler_face,parent,false));
    }

    @Override
    public void onBindViewHolder(MyAdapter holder, final int position) {
        holder.face.setImageResource(list.get(position).getRes());
        holder.face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFaceItemClick.faceItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyAdapter extends RecyclerView.ViewHolder{

        private ImageView face;

        public MyAdapter(View itemView) {
            super(itemView);
            face = (ImageView) itemView.findViewById(R.id.item_face_img);
        }
    }
}
