package wang.julis.jwbase.Request;

import android.text.TextUtils;

import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import wang.julis.jwbase.utils.JsonUtils;
import wang.julis.jwbase.utils.ToastUtils;


public abstract class BaseApiRequest<T> {

    protected Map<String, Object> mUrlParams;
    protected Map<String, String> mEntityParams;
    private RequestListener<T> requestListener;

    private int requestMethod = Request.Method.GET;

    public BaseApiRequest() {
        mUrlParams = new HashMap<>();
        mEntityParams = new HashMap<>();
    }

    public void setRequestMethodPost() {
        requestMethod = Request.Method.POST;
    }

    public void setRequestListener(RequestListener<T> requestListener) {
        this.requestListener = requestListener;
    }

    protected StringRequest getStringRequest() {
        return new StringRequest(requestMethod, getRequestUrl(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        BaseApiRequest.this.parseJson(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (requestListener != null) {
                            requestListener.onError(error);
                        }
                    }
                }) {
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
            Iterator iterator = mUrlParams.entrySet().iterator();
            if (url.indexOf("?") < 0) {
                url.append("?");
            }
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
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

            if (!jsonObject.has("data")) { //走普通解析
                parseResponse(jsonObject.toString());
                return;
            }

            String resultString = jsonObject.getString("data");

            if (jsonObject.has("code")) {
                responseCode = jsonObject.getInt("code");
            }
            if (jsonObject.has("error")) {
                errorCode = jsonObject.getInt("error");
            }

            if (responseCode == 200 || errorCode == 0) {
                if (!TextUtils.isEmpty(response)) {
                    parseResponse(resultString);
                    return;
                }
                ToastUtils.showToast("Data is empty!");
            }
            ToastUtils.showToast("Response code is error.");
            if (requestListener != null) {
                requestListener.onError(new ParseError());
            }

        } catch (JSONException e) {
            ToastUtils.showToast(e.toString());
            e.printStackTrace();
        }
    }
    private void parseResponse(String dataString) {
        if (requestListener != null) {
            Type type = getTType(requestListener.getClass());
            //泛型是实体或者List等类型
            T t = JsonUtils.fromJson(dataString, type);
            requestListener.onSuccess(t);
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

    public void putParams(String key, Object value) {
        mUrlParams.put(key,value);
        mEntityParams.put(key, String.valueOf(value));
    }

}