package wang.julis.jwbase.basecompact.baseList;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import wang.julis.jwbase.R;
import wang.julis.jwbase.basecompact.BaseActivity;

/*******************************************************
 *
 * Created by julis.wang on 2021/07/08 13:49
 *
 * Description :
 *
 * History   :
 *
 *******************************************************/

public abstract class BaseListActivity extends BaseActivity {
    protected ListAdapter mAdapter;
    protected final List<ListModel> mDataList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        submitActivityList();
    }

    @Override
    protected void initView() {
        RecyclerView rvList = findViewById(R.id.rv_list);
        mAdapter = new ListAdapter(this);
        rvList.setAdapter(mAdapter);
        rvList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void initData() {

    }

    protected void addItem(String tipName, Class<?> activityClass) {
        mDataList.add(new ListModel(tipName, activityClass));
    }

    protected void addItem(String tipName, Function0<Unit> function) {
        mDataList.add(new ListModel(tipName, null, function));
    }

    protected void submitActivityList() {
        mAdapter.updateData(mDataList);
    }


    @Override
    protected int getContentView() {
        return R.layout.activity_list;
    }
}
