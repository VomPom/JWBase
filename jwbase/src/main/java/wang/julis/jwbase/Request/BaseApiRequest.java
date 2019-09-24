package wang.julis.jwbase.Request;

import android.text.TextUtils;


import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import wang.julis.jwbase.Utils.JsonUtils;
import wang.julis.jwbase.Utils.ToastUtils;



public abstract class BaseApiRequest<T> {

    protected Map<String, String> mUrlParams;
    protected Map<String, String> mEntityParams;
    private RequestListener<T> requestListener;

    private int requestMethod = Request.Method.GET;

    public BaseApiRequest(RequestListener<T> requestListener) {
        this.requestListener = requestListener;
        mUrlParams = new HashMap<>();
        mEntityParams = new HashMap<>();
    }

    public void setRequestMethodPost() {
        requestMethod = Request.Method.POST;
    }

    protected StringRequest getStringRequest() {
        return new StringRequest(requestMethod, getRequestUrl(),
                response -> parseJson(response),
                error -> requestListener.onError(error)) {
            @Override
            protected Map<String, String> getParams() {
                return mEntityParams;
            }
        };
    }

    /**
     * 获取get 或者 post请求的基本url
     *
     * @return
     */
    public abstract String getBaseUrl();

    /**
     * 为get请求构造参数
     *
     * @return
     */
    public String getRequestUrl() {
        StringBuilder url = new StringBuilder(getBaseUrl());
        if (requestMethod == Request.Method.GET) {
            Iterator iter = mUrlParams.entrySet().iterator();
            if (url.indexOf("?") < 0) {
                url.append("?");
            }
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                Object val = entry.getValue();

                url.append("&")
                        .append(String.valueOf(key))
                        .append("=")
                        .append(String.valueOf(val));
            }
        }
        return url.toString();
    }

    /**
     * 解析返回的数据
     *
     * @param response
     */
    private void parseJson(String response) {
        int responseCode = 0;
        int errorCode = 400;
        try {
            JSONObject jsonObject = new JSONObject(response);

            String resultString = jsonObject.getString("data");

            if (jsonObject.has("code")) {
                responseCode = jsonObject.getInt("code");
            }
            if (jsonObject.has("error")) {
                errorCode = jsonObject.getInt("error");
            }

            if (responseCode == 200 || errorCode == 0) {
                if (!TextUtils.isEmpty(response)) {
                    Type type = getTType(requestListener.getClass());
                    //泛型是实体或者List等类型
                    T t = JsonUtils.fromJson(resultString, type);
                    requestListener.onSuccess(t);
                    return;
                }
                ToastUtils.showToast("Data is empty!");
            }
            ToastUtils.showToast("Response code is error.");
            requestListener.onError(new ParseError());
        } catch (JSONException e) {
            ToastUtils.showToast(e.toString());
            e.printStackTrace();
        }
    }

    /**
     * 获取回调接口中 T 的具体类型
     *
     * @param clazz
     * @return
     */
    public static Type getTType(Class<?> clazz) {
        //以Type的形式返回本类直接实现的接口.
        Type[] types = clazz.getGenericInterfaces();
        clazz.getInterfaces();
        if (types.length > 0) {
            //返回表示此类型实际类型参数的 Type 对象的数组
            Type[] interfacesTypes = ((ParameterizedType) types[0]).getActualTypeArguments();
            return interfacesTypes[0];
        }
        return null;
    }



}