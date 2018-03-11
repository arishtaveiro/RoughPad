package com.example.arisht.roughpad;

/**
 * Created by ARISHT on 19-01-2018.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


import static com.example.arisht.roughpad.MainActivity.BRUSH;
import static com.example.arisht.roughpad.MainActivity.COLOR;


public class PaintView extends View  {

    public  int BRUSH_SIZE =10;
    //public static final int DEFAULT_COLOR = Color.BLACK;
    public static final int DEFAULT_BG_COLOR = Color.WHITE;
    private static final float TOUCH_TOLERANCE = 4;
    private float mX, mY;
    private Path mPath;
    private Paint mPaint;
    private ArrayList<FingerPath> paths = new ArrayList<>();
    private int currentColor;
    private int backgroundColor = DEFAULT_BG_COLOR;
    private int strokeWidth;
    private boolean emboss;
    private boolean blur;
    private MaskFilter mEmboss;
    private MaskFilter mBlur;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mBitmapPaint = new Paint(Paint.DITHER_FLAG);
    private ArrayList<FingerPath> undonePaths = new ArrayList<>();

    //Context c;


    public PaintView(Context context) {
        this(context, null);
    }


    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        //stroke color
        mPaint.setColor(COLOR);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setXfermode(null);
        mPaint.setAlpha(0xff);

        mEmboss = new EmbossMaskFilter(new float[] {1, 1, 1}, 0.4f, 6, 3.5f);
        mBlur = new BlurMaskFilter(5, BlurMaskFilter.Blur.NORMAL);
    }




    public void init(DisplayMetrics metrics) {
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;

        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        mCanvas = new Canvas(mBitmap);

        currentColor = COLOR;
        Log.i("imp", ""+currentColor);

    }

    /*
    public void normal() {
        emboss = false;
        blur = false;
    }

    public void emboss() {
        emboss = true;
        blur = false;
    }

    public void blur() {
        emboss = false;
        blur = true;
    }
*/
    public void clear() {
        backgroundColor = DEFAULT_BG_COLOR;
        paths.clear();
      //  normal();
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        mCanvas.drawColor(backgroundColor);

        for (FingerPath fp : paths) {
            //mPaint.setColor(fp.color);
            mPaint.setColor(fp.color);
            if(BRUSH != 0){
                strokeWidth = BRUSH;
                mPaint.setStrokeWidth(fp.strokeWidth);
            }else{
                strokeWidth = BRUSH_SIZE;
                mPaint.setStrokeWidth(fp.strokeWidth);
            }

            mPaint.setMaskFilter(null);

            if (fp.emboss)
                mPaint.setMaskFilter(mEmboss);
            else if (fp.blur)
                mPaint.setMaskFilter(mBlur);

            mCanvas.drawPath(fp.path, mPaint);

        }
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);

        canvas.restore();
    }

    private void touchStart(float x, float y) {
        mPath = new Path();

        if(strokeWidth!= 0) {
            FingerPath fp = new FingerPath(COLOR, emboss, blur, strokeWidth, mPath);
            paths.add(fp);
        }
       else{
           // SeekBar seekbar = (SeekBar) findViewById(R.id.seekBar);
            //Log.i("imp", "herejjfghj");
            //strokeWidth = seekbar.getKeyProgressIncrement();
            FingerPath fp = new FingerPath(COLOR, emboss, blur, 10, mPath);
            paths.add(fp);
        }

        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touchMove(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);

        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    private void touchUp() {
        mPath.lineTo(mX, mY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN :
                touchStart(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE :
                touchMove(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP :
                touchUp();
                invalidate();
                break;
        }

        return true;
    }


    public void undo () {
        if (paths.size()>0)
        {
            undonePaths.add(paths.remove(paths.size()-1));
            invalidate();
        }
        else
        {

        }
        //toast the user
    }

    public void redo () {
        if (undonePaths.size() > 0) {
            paths.add(undonePaths.remove(undonePaths.size() - 1));
            invalidate();
        } else {

        }
    }

    public void  save(){
        View content = findViewById(R.id.paintView);
        content.setDrawingCacheEnabled(true);
        content.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = content.getDrawingCache();

        Date date = new Date();
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file = new File(path+"/roughpad"+date.getTime()+".png");
        FileOutputStream ostream;
        try {
            file.createNewFile();
            ostream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
            ostream.flush();
            ostream.close();
           // Toast.makeText(getApplicationContext(), "image saved", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("imp", "nope");
           // Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
        }
    }
/*
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }


*/



}