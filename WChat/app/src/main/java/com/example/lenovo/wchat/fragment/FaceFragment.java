package com.example.lenovo.wchat.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.DynamicDrawableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lenovo.wchat.R;
import com.example.lenovo.wchat.Utils.FaceList;
import com.example.lenovo.wchat.Utils.StringUtil;
import com.example.lenovo.wchat.act.GroupMessageActivity;
import com.example.lenovo.wchat.act.MainActivity;
import com.example.lenovo.wchat.act.MessageActivity;
import com.example.lenovo.wchat.adapter.FaceAdapter;
import com.example.lenovo.wchat.callback.IFaceItemClick;
import com.example.lenovo.wchat.model.FaceBean;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Lenovo on 2017/5/26.
 */

public class FaceFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private ArrayList<FaceBean> list = FaceList.getListInstance();
    private EditText et;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_face, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.face_recycler);
        FaceAdapter adapter = new FaceAdapter(getActivity(), list);
        GridLayoutManager glm = new GridLayoutManager(getActivity(), 7, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(glm);
        recyclerView.setAdapter(adapter);

        if(getActivity() instanceof MessageActivity){
            et = ((MessageActivity)getActivity()).et_message;
        }else if(getActivity() instanceof GroupMessageActivity){
            et = ((GroupMessageActivity)getActivity()).et_message;
        }
        adapter.setonFaceItemClick(new IFaceItemClick() {
            @Override
            public void faceItemClick(int position) {
                et.append(StringUtil.getExpressionString(getActivity(),list.get(position).getName()));
            }
        });
    }
}
