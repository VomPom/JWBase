package wang.julis.jwbase.basecompact;

import android.app.Application;
import android.content.Context;

import java.lang.reflect.Method;


/**
 * Created by julis.wang on 2018/12/07 09:34
 * <p>
 * Description:
 */
public class NaApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

    }

    public static Context getApp() {
        return context;
    }
}
