package wang.julis.jwbase.Request;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import wang.julis.jwbase.basecompact.NaApplication;


/**
 * Created by julis.wang on 2019/02/19 10:21
 * <p>
 * Description:
 */
public class RequestQueueUtils {
    private static RequestQueue mQueue;
    private static RequestQueueUtils mRequest;

    private RequestQueueUtils() {
        mQueue = Volley.newRequestQueue(NaApplication.getApp());
    }

    public static RequestQueueUtils getInstance() {
        if (mRequest == null) {
            synchronized (RequestQueueUtils.class) {
                if (mRequest == null) {
                    mRequest = new RequestQueueUtils();
                }
            }
        }
        return mRequest;
    }

    public void addRequestToQueue(BaseApiRequest request) {
        mQueue.add(request.getStringRequest());
    }

    public void addRequestToQueue(StringRequest request) {
        mQueue.add(request);
    }
}
