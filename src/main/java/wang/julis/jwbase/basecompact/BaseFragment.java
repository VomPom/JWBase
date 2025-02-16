package wang.julis.jwbase.basecompact;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import wang.julis.jwbase.Request.BaseApiRequest;
import wang.julis.jwbase.Request.RequestQueueUtils;

/*******************************************************
 *
 * Created by julis.wang on 2019/09/27 11:41
 *
 * Description :
 * History   :
 *
 *******************************************************/

public abstract class BaseFragment extends Fragment {
    private View mContentView;
    private Context mContext;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(setLayoutResourceID(),container,false);
        mContext = getContext();
        init();
        initView();
        initData();
        return mContentView;
    }

    /**
     * 此方法用于返回Fragment设置ContentView的布局文件资源ID
     *
     * @return 布局文件资源ID
     */
    protected abstract int setLayoutResourceID();

    /**
     * 一些View的相关操作
     */
    protected abstract void initView();

    /**
     * 一些Data的相关操作
     */
    protected abstract void initData();

    /**
     * 此方法用于初始化成员变量及获取Intent传递过来的数据
     * 注意：这个方法中不能调用所有的View，因为View还没有被初始化，要使用View在initView方法中调用
     */
    protected void init() {

    }

    public View getContentView() {
        return mContentView;
    }

    public Context getMContext() {
        return mContext;
    }

    protected void addRequestToQueue(BaseApiRequest request) {
        RequestQueueUtils.getInstance().addRequestToQueue(request);
    }
}
