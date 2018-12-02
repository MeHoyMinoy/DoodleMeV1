package com.example.apio9009.doodlemev1;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.Bitmap;
import java.util.ArrayList;
import java.util.List;


public class CanvasView extends View {

    public int width;
    public int height;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mPaint;
    private float mX, mY;
    private static final float TOLERANCE = 5;
    List<Pair<Path, Integer>> path_color_list = new ArrayList<Pair<Path,Integer>>();
    Context context;

    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        mPath = new Path();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        //mPaint.setStrokeWidth(4f);
        mPaint.setStrokeWidth(20f);
    }

    protected void onCreate(Bundle savedInstanceState) {
    
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        int tempC = mPaint.getColor();
        for (Pair<Path,Integer> path_clr : path_color_list ){
        mPaint.setColor(path_clr.second);
        canvas.drawPath( path_clr.first, mPaint);
        mPaint.setColor(tempC);
        canvas.drawPath(mPath, mPaint);
    }
    }

    //not using
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        super.onSizeChanged(w, h, oldw, oldh);

        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    private void startTouch(float x, float y){
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void moveTouch(float x, float y){
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if(dx >= TOLERANCE || dy >= TOLERANCE){
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    public void setColor(int color){
        //
        int tempColor = mPaint.getColor();
        mPaint.setColor(color);
        path_color_list.add( new Pair(mPath, tempColor));
        mPath = new Path();
    }

    public Bitmap getDoodle(){
        return mBitmap;
    }

    public void setDoodle(Bitmap image){
        mCanvas.setBitmap(image);
    }

    private void upTouch(){
    mPath.lineTo(mX, mY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                startTouch(x, y);
                invalidate();
                break;

            case MotionEvent.ACTION_MOVE:
                moveTouch(x, y);
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                upTouch();
                invalidate();
                break;
        }

        return true;
    }
}
