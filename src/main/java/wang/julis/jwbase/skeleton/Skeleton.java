package wang.julis.jwbase.skeleton;


import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/*******************************************************
 *
 * Created by julis.wang on 2019/07/08 11:34
 *
 * Description :
 * History   :
 *
 *******************************************************/

public class Skeleton {

    public static RecyclerViewSkeletonScreen.Builder bind(RecyclerView recyclerView, RecyclerView.Adapter actualAdapter) {
        return new RecyclerViewSkeletonScreen.Builder(recyclerView, actualAdapter);
    }

    public static ViewSkeletonScreen.Builder bind(View view) {
        return new ViewSkeletonScreen.Builder(view);
    }

}
