package wang.julis.jwbase.Request;

/**
 * Created by julis.wang on 2018/12/04 09:47
 * <p>
 * Description:
 */
public interface RequestListener<T> {
    void onSuccess(T result);
    void onError(Exception e);
}
