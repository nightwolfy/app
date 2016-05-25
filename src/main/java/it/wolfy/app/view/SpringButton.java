package it.wolfy.app.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;

public class SpringButton extends Button implements View.OnTouchListener, SpringListener
{
    private static double TENSION = 40;
    private static double FRICTION = 5; //friction

    private SpringSystem mSpringSystem;
    private Spring mSpring;

    public SpringButton(Context context)
    {
        super(context);
        initSpring();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SpringButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        initSpring();
    }

    public SpringButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initSpring();
    }

    public SpringButton(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initSpring();
    }

    void initSpring()
    {
        mSpringSystem = SpringSystem.create();

        mSpring = mSpringSystem.createSpring();
        mSpring.addListener(this);

        SpringConfig config = new SpringConfig(TENSION, FRICTION);
        mSpring.setSpringConfig(config);
        this.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                Log.e("TAG", "down");
                mSpring.setEndValue(1f);
                return false;
            case MotionEvent.ACTION_UP:
                Log.e("TAG", "up");
                mSpring.setEndValue(0f);
                return false;
        }

        return false;
    }

    @Override
    public void onSpringUpdate(Spring spring)
    {
        float value = (float) spring.getCurrentValue();
        float scale = 1f - (value * 0.5f);
        this.setScaleX(scale);
        this.setScaleY(scale);
//        Log.e("TAG","onSpringUpdate");
    }

    @Override
    public void onSpringAtRest(Spring spring)
    {
        Log.e("TAG", "onSpringAtRest");
    }

    @Override
    public void onSpringActivate(Spring spring)
    {
        Log.e("TAG", "onSpringActivate");

    }

    @Override
    public void onSpringEndStateChange(Spring spring)
    {
        Log.e("TAG", "onSpringEndStateChange");
    }
}
