package wang.julis.jwbase.toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import wang.julis.jwbase.R;

/*******************************************************
 *
 * Created by julis.wang@beibei.com on 2019/10/08 10:05
 *
 * Description :
 * History   :
 *
 *******************************************************/

public class JWToolbar extends Toolbar implements View.OnClickListener {
    private Context context;
    private Toolbar toolbar;
    private ImageView ivRightIcon;
    private TextView tvRightText;

    private String leftTitle;
    private String rightTitle;
    private Drawable rightIconDrawable;


    public JWToolbar(Context context) {
        this(context, null, -1);
    }

    public JWToolbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public JWToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.toolbarTheme);
        rightTitle = ta.getString(R.styleable.toolbarTheme_right_text);
        rightIconDrawable = ta.getDrawable(R.styleable.toolbarTheme_right_icon);
        leftTitle = ta.getString(R.styleable.toolbarTheme_left_text);
        initView();
    }

    public void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.sdk_toolbar, this);
        toolbar = view.findViewById(R.id.tool_bar);
        ivRightIcon = view.findViewById(R.id.iv_right_icon);
        tvRightText = view.findViewById(R.id.tv_right_title);
        TextView tvLeftText = view.findViewById(R.id.tv_left_title);
        ImageView ivBack = view.findViewById(R.id.iv_left_icon);

        ivBack.setOnClickListener(this);
        tvLeftText.setOnClickListener(this);

        if (!TextUtils.isEmpty(rightTitle)) {
            tvRightText.setVisibility(VISIBLE);
            tvRightText.setText(rightTitle);
        }

        if (!TextUtils.isEmpty(leftTitle)) {
            tvLeftText.setVisibility(VISIBLE);
            tvLeftText.setText(leftTitle);
        }

        if (rightIconDrawable != null) {
            ivRightIcon.setVisibility(VISIBLE);
            ivRightIcon.setImageDrawable(rightIconDrawable);
        }
    }

    public void setTitle(String title) {
        toolbar.setTitle(title);
    }

    public void setRightTitle(String title) {
        tvRightText.setVisibility(VISIBLE);
        tvRightText.setText(title);
    }

    public void setRightIcon(Drawable drawable) {
        ivRightIcon.setVisibility(VISIBLE);
        ivRightIcon.setImageDrawable(drawable);
    }

    public void setRightOnclickListener(OnClickListener listener) {
        tvRightText.setOnClickListener(listener);
        ivRightIcon.setOnClickListener(listener);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_left_icon || i == R.id.tv_left_title) {
            Activity activity = (Activity) context;
            activity.finish();
        }
    }
}
