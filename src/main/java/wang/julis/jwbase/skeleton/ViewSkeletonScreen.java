package wang.julis.jwbase.skeleton;

import androidx.annotation.LayoutRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

/*******************************************************
 *
 * Created by julis.wang on 2019/07/08 11:38
 *
 * Description :
 * History   :
 *
 *******************************************************/

public class ViewSkeletonScreen implements SkeletonScreen {
    private static final String TAG = ViewSkeletonScreen.class.getName();
    private final ViewReplacer mViewReplacer;
    private final View mActualView;
    private final int mSkeletonResID;

    private ViewSkeletonScreen(Builder builder) {
        mActualView = builder.mView;
        mSkeletonResID = builder.mSkeletonLayoutResID;
        mViewReplacer = new ViewReplacer(builder.mView);
    }

    private View generateSkeletonLoadingView() {
        ViewParent viewParent = mActualView.getParent();
        if (viewParent == null) {
            Log.e(TAG, "the source view have not attach to any view");
            return null;
        }
        ViewGroup parentView = (ViewGroup) viewParent;
        return LayoutInflater.from(mActualView.getContext()).inflate(mSkeletonResID, parentView, false);
    }

    @Override
    public void show() {
        View skeletonLoadingView = generateSkeletonLoadingView();
        if (skeletonLoadingView != null) {
            mViewReplacer.replace(skeletonLoadingView);
        }
    }

    @Override
    public void hide() {
        mViewReplacer.restore();
    }

    public static class Builder {
        private final View mView;
        private int mSkeletonLayoutResID;

        public Builder(View view) {
            this.mView = view;
        }

        /**
         * @param skeletonLayoutResID the loading skeleton layoutResID
         */
        public Builder load(@LayoutRes int skeletonLayoutResID) {
            this.mSkeletonLayoutResID = skeletonLayoutResID;
            return this;
        }

        public ViewSkeletonScreen show() {
            ViewSkeletonScreen skeletonScreen = new ViewSkeletonScreen(this);
            skeletonScreen.show();
            return skeletonScreen;
        }

    }
}

