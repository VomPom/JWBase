package wang.julis.jwbase.basecompact.baseList;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import wang.julis.jwbase.R;


/*******************************************************
 *
 * Created by julis.wang on 2019/09/24 14:16
 *
 * Description :
 * History   :
 *
 *******************************************************/

public class ListHolder extends RecyclerView.ViewHolder {
    private Context mContext;
    private TextView tvDesc;


    public ListHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.mContext = context;
        initView();
    }

    private void initView() {
        tvDesc = itemView.findViewById(R.id.tv_desc);

    }

    public void updateView(final ListModel data, int position) {
        tvDesc.setText(data.getActivityName());
        itemView.setOnClickListener(v -> {
            if (data.getOnClick() != null) {
                data.getOnClick().invoke();
            } else {
                mContext.startActivity(new Intent(mContext, data.getActivityClass()));
            }
        });
    }
}
