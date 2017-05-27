package com.example.lenovo.wchat.Utils;

import android.util.Size;

/**
 * Created by Lenovo on 2017/5/23.
 */

public class ImageUtil {
    private static int minWidth = 150, minHeight = 150, maxWidth = 400, maxHeight = 400;

    public static Size getWidthAndHeight(int width, int height) {
        int cWidth = 0, cHeight = 0;
        int t = 0;
        if (width < height) {
            t = width;
            width = height;
            height = t;
        }
        if (width > maxWidth) {
            double shang = width / (double) maxWidth;
            cWidth = maxWidth;
            cHeight = (int) (height / shang);
            cHeight = cHeight > minHeight ? cHeight : minHeight;
        } else if (width < maxWidth) {
            if (width < minWidth) {
                int shang = minWidth / width;
                cWidth = minWidth;
                cHeight = cHeight > minHeight ? cHeight : minHeight;
            } else {
                cWidth = height > minHeight ? height : minHeight;
            }
        }
        if (t > 0) {
            t = cWidth;
            cWidth = cHeight;
            cHeight = t;
        }
        return new Size(cHeight, cWidth);
    }
}
