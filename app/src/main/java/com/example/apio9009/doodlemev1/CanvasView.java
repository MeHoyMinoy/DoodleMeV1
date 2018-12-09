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
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.Color.RED;


public class CanvasView extends View {

    public int width;
    public int height;
    public Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Drawing drawing;
    private Paint mPaint;
    private float mX, mY;
    private static final float TOLERANCE = 5;
    List<Pair<Path, Integer>> path_color_list = new ArrayList<Pair<Path,Integer>>();
    Context context;

    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        mPath = new Path();
        mCanvas = new Canvas();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(20f);

        //mPaint.setStrokeWidth(4f);
        //mBitmap = Bitmap.createBitmap(mCanvas.getWidth(), mCanvas.getHeight(), Bitmap.Config.ARGB_8888)
    }

    protected void onCreate(Bundle savedInstanceState) {

    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        if(mBitmap != null)
        canvas.drawBitmap(mBitmap, 0, 0, mPaint);
        //mCanvas.setBitmap(mBitmap);
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
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        if (mBitmap == null) {
            mBitmap = Bitmap.createBitmap(w, h, conf);
        }
        mCanvas.setBitmap(mBitmap);
    }

    private void startTouch(float x, float y){
        mCanvas.setBitmap(mBitmap);
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void moveTouch(float x, float y){
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        mCanvas.setBitmap(mBitmap);
        if(dx >= TOLERANCE || dy >= TOLERANCE){
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    public void setColor(int color){
        //
        mCanvas.setBitmap(mBitmap);
        int tempColor = mPaint.getColor();
        mPaint.setColor(color);
        path_color_list.add( new Pair(mPath, tempColor));
        mPath = new Path();
    }

    public Bitmap getDoodle(){
        return mBitmap;
    }

    public void setDoodle(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    public Canvas getCanvas(){
        return mCanvas;
    }

    public void setCanvas(Bitmap bMap){
        Canvas temp = new Canvas(bMap);
        CanvasView view = this;
        view.draw(temp);
        mBitmap = bMap;
    }

    private void upTouch(){
        mCanvas.setBitmap(mBitmap);
        mPath.lineTo(mX, mY);
        }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        float x = event.getX();
        float y = event.getY();
        mCanvas.setBitmap(mBitmap);

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
