package wang.julis.jwbase.ImageLoader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;

/**
 * Created by julis.wang on 2019/02/20 11:53
 * <p>
 * Description:
 */
public class BitmapUtil {


    /**
     * 计算采样率
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    protected static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height == 0 || width == 0) { //如果没有传任何参数则表示原图
            return inSampleSize;
        }
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    protected static Bitmap getBitmap(InputStream in,int reqWidth,int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = BitmapUtil.calculateInSampleSize(options,reqWidth,reqHeight);;
        Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);
        options.inJustDecodeBounds = true;
        return bitmap;
    }

}
