package wang.julis.jwbase.ImageLoader;

import android.graphics.Bitmap;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Julis on 2019/02/10 21:11
 * <p>
 * description:
 */
public class NetworkUtil {
    private NetworkUtil() {
    }

    public static Bitmap getBitmap(String url,int reqWidth,int reqHeight) throws IOException {
        URL realUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
        try (InputStream in = connection.getInputStream()) {

            Bitmap bitmap = BitmapUtil.getBitmap(in,reqWidth,reqHeight);
            return bitmap;
        } finally {
            connection.disconnect();
        }
    }
}