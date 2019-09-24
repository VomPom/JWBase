package wang.julis.jwbase.Request;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;


/**
 * Created by Julis on 2019/02/03 19:54
 * <p>
 * description:
 */
public class BaseOuterRequest extends StringRequest{


    public BaseOuterRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
    }
    public BaseOuterRequest(String url, final RequestListener requestListener) {
        this(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                requestListener.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                requestListener.onError(error);
            }
        });

    }

}
