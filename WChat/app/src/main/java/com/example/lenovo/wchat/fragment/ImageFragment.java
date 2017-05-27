package com.example.lenovo.wchat.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.lenovo.wchat.R;
import com.example.lenovo.wchat.act.MessageActivity;
import com.example.lenovo.wchat.adapter.ImageAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by Lenovo on 2017/5/23.
 */

public class ImageFragment extends BaseFragment implements View.OnClickListener {

    private RecyclerView recycler;
    private Button send;
    private ArrayList<String> list = new ArrayList<>();
    private ImageAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        recycler = (RecyclerView) view.findViewById(R.id.image_recycler);
        send = (Button) view.findViewById(R.id.image_send);
        send.setOnClickListener(this);
        //根据内容提供者得到图片的路径
        getImagePath();

        adapter = new ImageAdapter(getActivity(), list);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler.setLayoutManager(llm);
        recycler.setAdapter(adapter);
    }

    private void getImagePath() {
        //清空图片数据源
        list.clear();
        //使用内容提供者查询得到sd卡中所存在的图片
        Cursor datas = getActivity().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,//通过uri查询图片
                null,//想要查询的字段
                null,//查询的条件
                null,//查询的值
                null);//排序
        //判断查询结果是否为空
        if (datas != null) {
            //遍历游标
            while (datas.moveToNext()) {
                //获取当前游标中图片路径字段的值
                String path = datas.getString(datas.getColumnIndex(MediaStore.Images.Media.DATA));
                //获取当前游标中图片宽 高 字段的值
//                int w = datas.getInt(datas.getColumnIndex(MediaStore.Images.Media.WIDTH));
//                int h = datas.getInt(datas.getColumnIndex(MediaStore.Images.Media.HEIGHT));
                //将查询出的图片路径添加到集合中
                list.add(path);
            }
            //关闭游标
            datas.close();
        }
    }

    /**
     * 按钮的点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        //拿到容器activity的对象
        MessageActivity ma = (MessageActivity) getActivity();
        //获取选中图片的数据
        HashSet<String> paths = adapter.getPaths();
        //遍历set集合
        Iterator<String> iterator = paths.iterator();
        //遍历集合，将以存入的图片路径传入发送图片的方法中
        while (iterator.hasNext()) {
            String path = iterator.next();
            ma.createImage(path);
        }
        adapter.refresh();
    }
}
