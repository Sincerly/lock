package com.ysxsoft.lock.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.ysxsoft.common_base.utils.DisplayUtils;
import com.ysxsoft.lock.R;

public class TouchView extends View {
    private float touchX;
    private float touchY;
    private float downX;
    private float downY;
    private int direction;//0左 1上 2右 3下
    private boolean isHalfed;//是否滑动到一半的距离
    private float percent;//滑动百分比
    private Paint paint;
    private int width;
    private int height;

    private int color1,color2,color3;
    private int directionCount=4;//方向数
    private int circleCount=3;//圆的数量
    private int pointRadius=14;//指示点半径
    private int pointDistance=60;//指示点与圆的距离
    private float radius;
    private float distance=40;//圆与圆之间的距离
    private Bitmap bitmap;
    private boolean isPressed=false;//是否按下

    private float indicatorRadius;

    public TouchView(Context context) {
        this(context,null);
    }

    public TouchView(Context context, @Nullable AttributeSet attrs) {
        this(context,null,0);
    }

    public TouchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        //1.初始化3种颜色  3个圆的大小 圆点的大小
        //2.初始化画笔
        paint=new Paint();
        paint.setColor(Color.WHITE);
        paint.setDither(true);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        color1=Color.parseColor("#3062C0B3");
        color2=Color.parseColor("#4062C0B3");
        color3=Color.parseColor("#9062C0B3");
        bitmap= BitmapFactory.decodeResource(getResources(), R.mipmap.icon_setting);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width=getWidth();
        height=getHeight();
        radius=width/4;
        drawCircle(canvas);
    }

    private void drawCircle(Canvas canvas){
        //1.画3个圆
        if(isPressed){
            paint.setColor(color3);
            int startX= (int) (touchX-radius);
            int startY=(int)(touchY-radius);
            Shader shader = new LinearGradient(startX, startY, startX+(radius-distance*2), startY+radius, Color.parseColor("#2AA9E0"),Color.parseColor("#62C0B3"), Shader.TileMode.CLAMP);
            paint.setShader(shader);
            canvas.drawCircle((float)width/2,(float)height/2,radius-distance*2,paint);
            //3.画图标
            canvas.drawBitmap(bitmap,startX-bitmap.getWidth()/2,startY-bitmap.getHeight()/2,paint);
        }else{
            paint.setColor(color1);
            canvas.drawCircle((float) width/2,(float)height/2,radius,paint);
            paint.setColor(color2);
            canvas.drawCircle((float)width/2,(float)height/2,radius-distance,paint);
            paint.setColor(color3);
            int startX= (int) (width/2-radius+distance*6);
            int startY=height/2;
            Shader shader = new LinearGradient(startX, startY, startX+(radius-distance*2), height/2, Color.parseColor("#2AA9E0"),Color.parseColor("#62C0B3"), Shader.TileMode.CLAMP);
            paint.setShader(shader);
            canvas.drawCircle((float)width/2,(float)height/2,radius-distance*2,paint);
            //3.画图标
            canvas.drawBitmap(bitmap,getWidth()/2-bitmap.getWidth()/2,getHeight()/2-bitmap.getHeight()/2,paint);
        }
        //2.画圆点
        canvas.translate(width/2,height/2);
        float start=radius+pointDistance;
        for (int i = 0; i <directionCount; i++) {
            paint.setColor(color3);
            canvas.drawCircle(0,start,pointRadius,paint);
            paint.setColor(color2);
            canvas.drawCircle(0,start+distance,pointRadius,paint);
            paint.setColor(color1);
            canvas.drawCircle(0,start+distance*2,pointRadius,paint);
            canvas.rotate(90);
        }
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                isPressed=true;
                downX=event.getX();
                downY=event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                touchX=event.getX();
                touchY=event.getY();
                float offsetX=touchX-downX;
                float offsetY=touchY-downY;
                if(offsetX<0&&offsetY<0){
                    Log.e("tag","向下");
                }else if(offsetX<0&&offsetY>0){
                    Log.e("tag","向上");
                }else if(offsetX>0&&offsetY<0){

                }else if(offsetX>0&&offsetY>0){

                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isPressed=false;
                touchX=0;
                touchY=0;
                downX=width/2;
                downY=height/2;
                break;
        }
        postInvalidate();
        return true;
    }

    public interface OnViewChangedListener{
        void end(int direction);
        void moveHalf();
    }
}