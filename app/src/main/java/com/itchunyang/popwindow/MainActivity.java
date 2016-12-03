package com.itchunyang.popwindow;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button longBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        longBtn = (Button) findViewById(R.id.longBtn);
        longBtn.setOnLongClickListener(listener);
    }

    public void popWindow(View view) {
        View v = getLayoutInflater().inflate(R.layout.layout_pop,null);
        PopupWindow popupWindow = new PopupWindow(v, 300, ViewGroup.LayoutParams.WRAP_CONTENT);

        //这两行可以使popWindow点击外部消失
        popupWindow.setOutsideTouchable(true);
        //如果不设置PopupWindow的背景，有些版本就会出现一个问题：无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(new ColorDrawable());

        popupWindow.showAsDropDown(view,0,0);
    }

    public void popWindow1(View view) {
        View v = getLayoutInflater().inflate(R.layout.layout_pop1,null);

        PopupWindow window = new PopupWindow(this);

        //不设置宽高 不显示
        window.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        window.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        window.setContentView(v);
        window.setOutsideTouchable(true);
        window.setBackgroundDrawable(new ColorDrawable());
//        window.showAsDropDown(view,50,0);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
                layoutParams.alpha = 1.0f;
                getWindow().setAttributes(layoutParams);
                Toast.makeText(MainActivity.this,"popWindow dismiss",Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * 为什么要设置焦点?
         * 如果设置了setFocusable,弹出后，所有的触屏和物理按键都有PopupWindows处理。
         * 比如这样一个PopupWindow出现的时候，按back键首先是让PopupWindow消失，第二次按才是退出activity。
         * 而且如果popWindow里面有EditText的话,如果不设置setFocusable,那么点击不会出现键盘!
         */
        window.setFocusable(true);

        window.showAtLocation(findViewById(R.id.activity_main), Gravity.BOTTOM,0,5);
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.alpha = 0.5f;
        getWindow().setAttributes(layoutParams);
    }

    /**
     * 如果创建PopupWindow的时候没有指定高宽，那么showAsDropDown默认只会向下弹出显示.最明显的缺点就是：弹窗口可能被屏幕截断，显示不全
     * 所以需要使用showAtLocation,这个的坐标是相对于整个屏幕的,我们自己计算位置.
     * 可以看看Drawable目录里的图片(pop pop1 pop2)
     */
    public void popWindow2(View view) {
        View v = getLayoutInflater().inflate(R.layout.layout_pop2,null);
        PopupWindow window = new PopupWindow(v, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int pos[] = calcPos(view,v);

        //可以自己设置一些偏移
        int xOff = 20;
        pos[0] -= xOff;
        window.setOutsideTouchable(true);
        window.setBackgroundDrawable(new ColorDrawable());
        window.showAtLocation(findViewById(R.id.activity_main),Gravity.TOP|Gravity.START,pos[0],pos[1]);
    }

    private int[] calcPos(View view, View windowView) {
        int triggerViewPos[] = new int[2];
        // 获取锚点View在屏幕上的左上角坐标位置
        view.getLocationOnScreen(triggerViewPos);
        int triggerViewHeight = view.getHeight();

        // 获取屏幕的高宽
        final int screenHeight = getResources().getDisplayMetrics().heightPixels;
        final int screenWidth = getResources().getDisplayMetrics().widthPixels;

        // 计算contentView的高宽
        windowView.measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED);

        int windowHeight = windowView.getMeasuredHeight();
        int windowWidth = windowView.getMeasuredWidth();
        System.out.println("width="+windowWidth+" height="+windowHeight);

        int realPos[] = new int[2];
        // 判断需要向上弹出还是向下弹出显示
        boolean isNeedShowUp = ((screenHeight - triggerViewPos[1] - triggerViewHeight) < windowHeight);
        if(isNeedShowUp){
            realPos[0] = screenWidth - windowWidth;
            realPos[1] = triggerViewPos[1] - windowHeight;
            System.out.println(""+triggerViewPos[0]+" " + triggerViewPos[1]);
        }else{
            realPos[0] = screenWidth - windowWidth;
            realPos[1] = triggerViewPos[1] +triggerViewHeight;
        }
        return realPos;
    }

    public void popAnimation(View view) {
        View v = getLayoutInflater().inflate(R.layout.layout_pop_animation,null);

        PopupWindow window = new PopupWindow(this);

        window.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        window.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
                layoutParams.alpha = 1.0f;
                getWindow().setAttributes(layoutParams);
                Toast.makeText(MainActivity.this,"popWindow dismiss",Toast.LENGTH_SHORT).show();
            }
        });

        window.setContentView(v);
        window.setOutsideTouchable(true);
        window.setBackgroundDrawable(new ColorDrawable());

        //PopupWindow的动画显示效果是通过setAnimationStyle(int id)方法设置的
        window.setAnimationStyle(R.style.pop_anim);
        window.showAtLocation(findViewById(R.id.activity_main), Gravity.BOTTOM,0,5);
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.alpha = 0.5f;
        getWindow().setAttributes(layoutParams);

    }

    //长按弹出
    private View.OnLongClickListener listener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            View view = getLayoutInflater().inflate(R.layout.layout_pop3,null);
            int screenWidth = getResources().getDisplayMetrics().widthPixels;
//            int screenHeight = getResources().getDisplayMetrics().heightPixels;

            PopupWindow window = new PopupWindow(view,2*screenWidth/3, ViewGroup.LayoutParams.WRAP_CONTENT,true);
            window.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
                    layoutParams.alpha = 1.0f;
                    getWindow().setAttributes(layoutParams);
                    Toast.makeText(MainActivity.this,"popWindow dismiss",Toast.LENGTH_SHORT).show();
                }
            });

            WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
            layoutParams.alpha = 0.5f;
            getWindow().setAttributes(layoutParams);
            window.setBackgroundDrawable(new ColorDrawable());
            window.setOutsideTouchable(true);
            window.setAnimationStyle(R.style.pop_anim1);
            window.showAtLocation(findViewById(R.id.activity_main),Gravity.CENTER,0,0);
            return true;
        }
    };
}
