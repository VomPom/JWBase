package wang.julis.jwbase.Utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/*******************************************************
 *
 * Created by julis.wang@beibei.com on 2019/09/23 15:24
 *
 * Description :
 * History   :
 *
 *******************************************************/

public class ImageUtils {
    /**
     * 保存图片到 gallery
     *
     * @param context Context
     * @param bmp     要保存的 bitmap
     * @return 是否保存成功
     */
    public static boolean saveImageToGallery(Context context, Bitmap bmp) {
        return saveImageToGallery(context, bmp, new StringBuilder().append(System.currentTimeMillis()).append(".jpg").toString());
    }

    /**
     * 保存图片到 gallery
     *
     * @param context  Context
     * @param bmp      要保存的 bitmap
     * @param fileName 要保存的名称
     * @return 是否保存成功
     */
    public static boolean saveImageToGallery(final Context context, Bitmap bmp, String fileName) {
        if (context == null || bmp == null) {
            return false;
        }
        boolean isSuccess = false;
        if (!fileName.endsWith(".jpg") && !fileName.endsWith(".png")) {
            fileName = new StringBuilder().append(fileName).append(".jpg").toString();
        }
        File file = new File(getCameraFile(), fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            //保存图片后发送广播通知更新数据库
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(file)));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtil.closeSilently(fos);
        }
        if (isSuccess) {
            return true;
        }
        Uri result = null;
        try {
            String title = "julis_wang_" + System.currentTimeMillis();
            result = InsertImageUtils.insertImage(context.getContentResolver(), bmp, title);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (result != null) {
            return true;
        }

        return false;
    }

    public static File getCameraFile() {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath(), "Camera");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    private static class InsertImageUtils {
        private static Bitmap storeThumbnail(ContentResolver contentResolver, Bitmap source,
                                             long id, float width, float height, int kind) {
            if (source == null) {
                return null;
            }
            // create the matrix to scale it
            Matrix matrix = new Matrix();

            float scaleX = width / source.getWidth();
            float scaleY = height / source.getHeight();

            matrix.setScale(scaleX, scaleY);

            Bitmap thumb = Bitmap.createBitmap(source, 0, 0,
                    source.getWidth(),
                    source.getHeight(), matrix,
                    true);

            ContentValues values = new ContentValues(4);
            values.put(MediaStore.Images.Thumbnails.KIND, kind);
            values.put(MediaStore.Images.Thumbnails.IMAGE_ID, (int) id);
            values.put(MediaStore.Images.Thumbnails.HEIGHT, thumb.getHeight());
            values.put(MediaStore.Images.Thumbnails.WIDTH, thumb.getWidth());

            Uri url = contentResolver.insert(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, values);

            if (url == null) {
                return null;
            }

            OutputStream thumbOut = null;
            try {
                thumbOut = contentResolver.openOutputStream(url);
                thumb.compress(Bitmap.CompressFormat.JPEG, 100, thumbOut);
                return thumb;
            } catch (FileNotFoundException ex) {
                return null;
            } finally {
                IOUtil.closeSilently(thumbOut);
            }
        }

        public static Uri insertImage(ContentResolver contentResolver, Bitmap source, String title) {

            if (source == null) {
                return null;
            }
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, title);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

            // Add the date meta data to ensure the image is added at the front of the gallery
            values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
            values.put(MediaStore.Images.Media.DATE_MODIFIED, System.currentTimeMillis() / 1000);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                values.put(MediaStore.Images.Media.HEIGHT, source.getHeight());
                values.put(MediaStore.Images.Media.WIDTH, source.getWidth());
            }

            Uri uri = null;
            OutputStream imageOut = null;
            try {
                uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                imageOut = contentResolver.openOutputStream(uri);
                source.compress(Bitmap.CompressFormat.JPEG, 100, imageOut);
                long id = ContentUris.parseId(uri);
                // Create MINI_KIND thumbnail
                Bitmap miniThumb = MediaStore.Images.Thumbnails.getThumbnail(contentResolver, id, MediaStore.Images.Thumbnails.MINI_KIND, null);
                // Use MINI_KIND thumbnail to create MICRO_KIND thumbnail
                storeThumbnail(contentResolver, miniThumb, id, 50F, 50F, MediaStore.Images.Thumbnails.MICRO_KIND);
            } catch (Exception e) {
                if (uri != null) {
                    contentResolver.delete(uri, null, null);
                    uri = null;
                }
            } finally {
                IOUtil.closeSilently(imageOut);
            }
            return uri;
        }
    }

    private static class IOUtil {

        public static final int DEFAULT_BUFFER_SIZE = 12 * 1024; // 12 KB


        public static void copyfile(InputStream is, File to) throws IOException {
            File tempFile = new File(to.getAbsolutePath() + "_" + System.currentTimeMillis() + "_temp");
            FileOutputStream fos = new FileOutputStream(tempFile);
            copyStream(is, fos);
            closeSilently(fos);
            closeSilently(is);
            tempFile.renameTo(to);
        }

        public static boolean copyStream(InputStream is, OutputStream os) throws IOException {

            final byte[] bytes = new byte[DEFAULT_BUFFER_SIZE];
            int count;
            while ((count = is.read(bytes, 0, DEFAULT_BUFFER_SIZE)) != -1) {
                os.write(bytes, 0, count);
            }
            os.flush();
            return true;
        }

        public static void closeSilently(Closeable closeable) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (Exception ignored) {
                }
            }
        }
    }

}
