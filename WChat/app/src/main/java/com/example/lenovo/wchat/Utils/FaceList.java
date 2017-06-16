package com.example.lenovo.wchat.Utils;

import com.example.lenovo.wchat.R;
import com.example.lenovo.wchat.model.FaceBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lenovo on 2017/6/5.
 */

public class FaceList {

    private static Map<String, Integer> map;
    private static ArrayList<FaceBean> list;
    private static int[] faceRes = {R.drawable.mxq, R.drawable.mxp, R.drawable.mxo, R.drawable.mxn, R.drawable.mxm, R.drawable.mxl
            , R.drawable.mxk, R.drawable.mxj, R.drawable.mxi, R.drawable.mxh, R.drawable.mxg, R.drawable.mxd
            , R.drawable.eft, R.drawable.eff, R.drawable.efb, R.drawable.efa, R.drawable.eez, R.drawable.eex
            , R.drawable.eew, R.drawable.eet, R.drawable.ees, R.drawable.eer, R.drawable.eeq, R.drawable.eep
            , R.drawable.eeo, R.drawable.eef, R.drawable.eed, R.drawable.eec, R.drawable.edw, R.drawable.edv
            , R.drawable.eds, R.drawable.edr, R.drawable.edq, R.drawable.edp, R.drawable.edo, R.drawable.edn
            , R.drawable.edj, R.drawable.edi, R.drawable.ede, R.drawable.edd, R.drawable.edb, R.drawable.eda
            , R.drawable.ecz, R.drawable.ecy, R.drawable.ecx, R.drawable.ecw, R.drawable.ecv, R.drawable.ect
            , R.drawable.ecs, R.drawable.ecr, R.drawable.ecq, R.drawable.ecp, R.drawable.eco, R.drawable.ecn
            , R.drawable.ecm, R.drawable.ecl, R.drawable.eck, R.drawable.ecj, R.drawable.eci, R.drawable.ech
            , R.drawable.ecg, R.drawable.ecf, R.drawable.ece, R.drawable.ecd, R.drawable.ecc, R.drawable.ecb
            , R.drawable.eca, R.drawable.ebz, R.drawable.ebx, R.drawable.ebw, R.drawable.ebv, R.drawable.ebu
            , R.drawable.ebt, R.drawable.ebs, R.drawable.ebr, R.drawable.ebq, R.drawable.ebp, R.drawable.ebo
            , R.drawable.ebl, R.drawable.ebj, R.drawable.ebh, R.drawable.ebg};
    private static String[] faceName = {"[mxq]", "[mxp]", "[mxo]", "[mxn]", "[mxm]", "[mxl]"
            , "[mxk]", "[mxj]", "[mxi]", "[mxh]", "[mxg]", "[mxd]"
            , "[eft]", "[eff]",  "[efb]", "[efa]", "[eez]", "[eex]"
            , "[eew]", "[eet]", "[ees]", "[eer]", "[eeq]", "[eep]"
            , "[eeo]", "[eef]", "[eed]", "[eec]", "[edw]", "[edv]"
            , "[eds]", "[edr]", "[edq]", "[edp]", "[edo]", "[edn]"
            , "[edj]", "[edi]", "[ede]", "[edd]", "[edb]", "[eda]"
            , "[ecz]", "[ecy]", "[ecx]", "[ecw]", "[ecv]", "[ect]"
            , "[ecs]", "[ecr]", "[ecq]", "[ecp]", "[eco]", "[ecn]"
            , "[ecm]", "[ecl]", "[eck]", "[ecj]", "[eci]", "[ech]"
            , "[ecg]", "[ecf]", "[ece]", "[ecd]", "[ecc]", "[ecb]"
            , "[eca]", "[ebz]", "[ebx]", "[ebw]", "[ebv]", "[ebu]"
            , "[ebt]", "[ebs]", "[ebr]", "[ebq]", "[ebp]", "[ebo]"
            , "[ebl]", "[ebj]", "[ebh]", "[ebg]"};

    public static synchronized ArrayList<FaceBean> getListInstance(){
        if (list == null){
            list = new ArrayList<>();
            for (int i = 0;i < faceRes.length;i++){
                list.add(new FaceBean(faceName[i], faceRes[i]));
            }
        }
        return list;
    }

    public static synchronized Map<String, Integer> getMapInstance(){
        if (map == null){
            map = new HashMap<>();
            for (int i = 0;i < faceRes.length;i++){
                map.put(faceName[i], faceRes[i]);
            }
        }
        return map;
    }

}
