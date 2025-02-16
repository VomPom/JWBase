package wang.julis.jwbase.skeleton;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/*******************************************************
 *
 * Created by julis.wang on 2019/07/08 11:35
 *
 * Description :
 * History   :
 *
 *******************************************************/

public class SkeletonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int mItemCount;
    private int mLayoutReference;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new RecyclerView.ViewHolder(inflater.inflate(mLayoutReference, parent, false)) {};
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mItemCount;
    }

    public void setLayoutReference(int layoutReference) {
        this.mLayoutReference = layoutReference;
    }

    public void setItemCount(int itemCount) {
        this.mItemCount = itemCount;
    }

}
