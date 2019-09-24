package wang.julis.jwbase.BannerView;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;


import java.util.ArrayList;
import java.util.List;

import wang.julis.jwbase.R;


/**
 * Created by Julis on 2019/01/31 20:26
 * <p>
 * description:
 */
public class CBannerView extends LinearLayout {
    private final int LOOP_TIME = 5000;
    private ViewPager viewPager;
    private Context context;
    private List<View> mDots = new ArrayList<>();
    private int previousPosition;
    private List<ImageView> views = new ArrayList<>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        }
    };


    public CBannerView(@NonNull Context context) {
        super(context);
        this.context = context;
    }
    public CBannerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }
    public void setImages(List<ImageView> views) {
        this.views = views;
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.sdk_banner_loop,this);
        viewPager = view.findViewById(R.id.vp_banner);
        //添加轮播点
        LinearLayout linearLayoutDots = view.findViewById(R.id.lineLayout_dot);
        mDots = addDots(linearLayoutDots,getResources().getDrawable(R.drawable.ic_loop),views.size(),view);//其中
        viewPager.setAdapter(new CBannerAdapter(views,viewPager));
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                //伪无限循环，滑到最后一张图片又从新进入第一张图片
                int newPosition = position % views.size();
                //设置轮播点
                mDots.get(newPosition).setBackground(getResources().getDrawable(R.drawable.ic_loop_black));
                mDots.get(previousPosition).setBackground(getResources().getDrawable(R.drawable.ic_loop));
                // 把当前的索引赋值给前一个索引变量, 方便下一次再切换.
                previousPosition = newPosition;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        autoPlay();
    }
    //自动播放图片
    public void autoPlay() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){

                    SystemClock.sleep(LOOP_TIME);
                    handler.sendEmptyMessage(100);
                }
            }
        }).start();
    }
    /**
     * 添加多个轮播小点到横向线性布局
     * @param linearLayout
     * @param backgount
     * @param number
     * @return
     */
    private List<View> addDots(final LinearLayout linearLayout, Drawable backgount, int number,View view){
        List<View> dots = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            int dotId = addDot(linearLayout,backgount);
            dots.add(view.findViewById(dotId));
        }
        return dots;
    }
    /**
     * 动态添加一个点
     * @param linearLayout 添加到LinearLayout布局
     * @param backgoung 设置图标
     * @return
     */
    private int addDot(final LinearLayout linearLayout, Drawable backgoung) {
        final View dot = new View(context);
        LayoutParams dotParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        dotParams.width = 16;
        dotParams.height = 16;
        dotParams.setMargins(8,0,8,0);
        dot.setLayoutParams(dotParams);
        dot.setBackground(backgoung);
        dot.setId(View.generateViewId());
        linearLayout.addView(dot);
        return dot.getId();
    }
}
