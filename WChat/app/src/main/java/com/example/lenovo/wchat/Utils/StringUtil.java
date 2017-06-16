package com.example.lenovo.wchat.Utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yinm_pc on 2017/6/6.
 */

public class StringUtil {
    static Map<String, Integer> emojiMap = new HashMap<>();

    public static void list2Map() {
        emojiMap.putAll(FaceList.getMapInstance());
    }

    /**
     * 得到一个SpanableString对象，通过传入的字符串,并进行正则判断
     *
     * @param context
     * @param str
     * @return
     */
    public static SpannableString getExpressionString(Context context, String str) {

        SpannableString spannableString = new SpannableString(str);

        // 正则表达式比配字符串里是否含有表情
        String zhengze = "(\\[)\\w{3}(\\])";
        // 通过传入的正则表达式来生成一个pattern
        Pattern sinaPatten = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);
        try {
            dealExpression(context, spannableString, sinaPatten, 0);
        } catch (Exception e) {
            Log.e("dealExpression", e.getMessage());
        }
        return spannableString;
    }

    /**
     * 对spanableString进行正则判断，如果符合要求，则以表情图片代替
     *
     * @param context
     * @param spannableString
     * @param patten
     * @param start
     * @throws Exception
     */
    private static void dealExpression(Context context,
                                       SpannableString spannableString, Pattern patten, int start)
            throws Exception {

        Matcher matcher = patten.matcher(spannableString);

        while (matcher.find()) {
            String key = matcher.group();
            // 返回第一个字符的索引的文本匹配整个正则表达式,ture 则继续递归
            if (matcher.start() < start) {
                continue;
            }
            Log.e("map", String.valueOf(emojiMap.get(key)));
            int resId = emojiMap.get(key);
            if (TextUtils.isEmpty(resId + "")) {
                continue;
            }
            if (resId != 0) {
                // 通过图片资源id来得到bitmap，用一个ImageSpan来包装
                Drawable d = context.getResources().getDrawable(resId);
                d.setBounds(0, 0, d.getIntrinsicWidth() / 2, d.getIntrinsicHeight() / 2);
                ImageSpan imageSpan = new ImageSpan(d);
                // 计算该图片名字的长度，也就是要替换的字符串的长度
                int end = matcher.start() + key.length();
                // 将该图片替换字符串中规定的位置中
                spannableString.setSpan(imageSpan, matcher.start(), end,
                        Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                if (end < spannableString.length()) {
                    // 如果整个字符串还未验证完，则继续。。
                    dealExpression(context, spannableString, patten, end);
                }
                break;
            }
        }

    }
}
