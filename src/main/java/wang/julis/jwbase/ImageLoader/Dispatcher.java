package wang.julis.jwbase.ImageLoader;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

/**
 * Created by Julis on 2019/02/10 21:10
 * <p>
 * description:
 */
public class Dispatcher {
    private final String mUrl;
    private int reqWidth,reqHeight;


    private final ExecutorService mExecutorService;

    public Dispatcher(String url, ExecutorService executorService) {
        mUrl = url;
        mExecutorService = executorService;
    }

    public Bitmap get() throws IOException {
        return NetworkUtil.getBitmap(mUrl,reqWidth,reqHeight);
    }

    public Dispatcher size(int reqWidth,int reqHeight) {
        this.reqWidth = reqWidth;
        this.reqHeight = reqHeight;
        return this;
    }
    public void into(final ImageView imageView) {

        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    final Bitmap bitmap = get();


                    CImageLoader.HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(bitmap);
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}