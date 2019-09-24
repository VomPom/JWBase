package wang.julis.jwbase.Utils;


import android.util.JsonReader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

/**
 * Created by Julis on 2019/01/31 16:09
 * <p>
 * description:
 */
public class JsonUtils {

    private static Gson mGson;

    private static Gson getGson() {
        if (null == mGson) {
            mGson = new GsonBuilder().serializeNulls().disableHtmlEscaping().create();
        }
        return mGson;
    }

    public static String toJson(Object object) {
        return new Gson().toJson(object);
    }

    public static String toJson(Object object, Type typeOfT) {
        return new Gson().toJson(object, typeOfT);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return getGson().fromJson(json, clazz);
    }

    public static <T> T fromJson(String json, Type typeOfT) {
        return getGson().fromJson(json, typeOfT);
    }

}
