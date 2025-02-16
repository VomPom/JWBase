package wang.julis.jwbase.ImageLoader;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by julis.wang on 2018/12/05 11:34
 * <p>
 * Description:
 */
public class CImageLoader {
    private static volatile CImageLoader mSingleton;
    private final Context mContext;
    private static final int MAX_THREAD_NUM = 3;
    private final ExecutorService mExecutorService;
    static final Handler HANDLER = new Handler(Looper.getMainLooper());

    private CImageLoader(Context context) {
        mContext = context.getApplicationContext();
        mExecutorService = Executors.newFixedThreadPool(MAX_THREAD_NUM);
    }

    public static CImageLoader with(Context context) {
        if (mSingleton == null) {
            synchronized (CImageLoader.class) {
                if (mSingleton == null) {
                    mSingleton = new CImageLoader(context);

                }
            }
        }
        return mSingleton;
    }

    public Dispatcher load(String url) {
        return new Dispatcher(url, mExecutorService);
    }




}
