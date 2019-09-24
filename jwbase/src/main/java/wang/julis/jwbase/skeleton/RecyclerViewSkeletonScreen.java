package wang.julis.jwbase.skeleton;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;

import wang.julis.jwbase.R;


/*******************************************************
 *
 * Created by julis.wang on 2019/07/08 11:34
 *
 * Description :
 * History   :
 *
 *******************************************************/

public class RecyclerViewSkeletonScreen implements SkeletonScreen {
    private static final int DEFULT_RECYCLERVIEW_COUNT = 10;
    private final RecyclerView mRecyclerView;
    private final RecyclerView.Adapter mActualAdapter;
    private final SkeletonAdapter mSkeletonAdapter;
    private final boolean mRecyclerViewFrozen;

    private RecyclerViewSkeletonScreen(Builder builder) {
        mRecyclerView = builder.mRecyclerView;
        mActualAdapter = builder.mActualAdapter;
        mSkeletonAdapter = new SkeletonAdapter();
        mSkeletonAdapter.setItemCount(builder.mItemCount);
        mSkeletonAdapter.setLayoutReference(builder.mItemResID);
        mRecyclerViewFrozen = builder.mFrozen;
    }

    @Override
    public void show() {
        mRecyclerView.setAdapter(mSkeletonAdapter);
        if (!mRecyclerView.isComputingLayout() && mRecyclerViewFrozen) {
            mRecyclerView.setLayoutFrozen(true);
        }
    }

    @Override
    public void hide() {
        mRecyclerView.setAdapter(mActualAdapter);
    }

    public static class Builder {
        private RecyclerView.Adapter mActualAdapter;
        private final RecyclerView mRecyclerView;
        private int mItemCount = DEFULT_RECYCLERVIEW_COUNT;
        private int mItemResID = R.layout.sdk_layout_default_item_skeleton;
        private boolean mFrozen = true;

        public Builder(RecyclerView recyclerView,RecyclerView.Adapter actualAdapter) {
            this.mRecyclerView = recyclerView;
            this.mActualAdapter = actualAdapter;
        }

        /**
         * @param itemCount 展示recyclerview_item数量
         */
        public Builder count(int itemCount) {
            this.mItemCount = itemCount;
            return this;
        }

        /**
         * @param skeletonLayoutResID 需要加载的骨架layout_id
         */
        public Builder load(@LayoutRes int skeletonLayoutResID) {
            this.mItemResID = skeletonLayoutResID;
            return this;
        }

        /**
         * @param frozen 加载过程中RecyclerView是否可以滚动
         * @return
         */
        public Builder frozen(boolean frozen) {
            this.mFrozen = frozen;
            return this;
        }

        public RecyclerViewSkeletonScreen show() {
            RecyclerViewSkeletonScreen recyclerViewSkeleton = new RecyclerViewSkeletonScreen(this);
            recyclerViewSkeleton.show();
            return recyclerViewSkeleton;
        }
    }
}
