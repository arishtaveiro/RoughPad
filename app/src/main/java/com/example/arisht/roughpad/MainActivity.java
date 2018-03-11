package com.example.arisht.roughpad;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;


public class MainActivity extends AppCompatActivity {


    SeekBar seekBar;
    static int BRUSH;
    static int COLOR = Color.BLACK;


    //Intent intent;
    private static int IMG_RESULT = 1;
    String ImageDecode;
    ImageView imageViewLoad;
    Button LoadImage;
    Intent intent;
    String[] FILE;

    static Bitmap mBitmap;

    PaintView paintView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        paintView = (PaintView) findViewById(R.id.paintView);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        paintView.init(metrics);
        seekBar = (SeekBar)findViewById(R.id.seekBar);

        seekBar.setMax(100);
        seekBar.setProgress(5);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //PaintView pv = new PaintView(this);
                seekBar.setEnabled(true);

                //new PaintView(getBaseContext(), progress);
                //strokeWidth = progress;
                BRUSH = progress;
               // Toast.makeText(MainActivity.this, ""+progress, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
             //   seekBar.

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.save) {
            Toast.makeText(this, "working on it...", Toast.LENGTH_LONG).show();

            //paintView.save();
            return true;
        }

        if (id == R.id.share) {
            Toast.makeText(this, "Working on it...", Toast.LENGTH_LONG).show();
            return true;
        }

        if (id == R.id.delete) {
            Toast.makeText(this, "delete", Toast.LENGTH_SHORT).show();
            paintView.clear();
            return true;
        }

        if (id == R.id.undo) {
            paintView.undo();
            return true;
        }

        if (id == R.id.redo) {
            paintView.redo();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    //set color of stroke
    public void col1(View view){

        COLOR = Color.BLACK;
    }
    public void col2(View view){

        COLOR = Color.parseColor("#f20606");

    }
    public void col3(View view){

        COLOR = Color.parseColor("#e48d29");
    }
    public void col4(View view){

        COLOR = Color.parseColor("#d9e413");
    }
    public void col5(View view){

        COLOR = Color.parseColor("#85de0a");

    }
    public void col6(View view){

        COLOR = Color.parseColor("#0cea10");
    }
    public void col7(View view){

        COLOR = Color.parseColor("#12e8ec");

    }
    public void col8(View view){

        COLOR = Color.parseColor("#0a4aec");
    }
    public void col9(View view){

        COLOR = Color.parseColor("#9609e7");
    }
    public void col10(View view){

        COLOR = Color.parseColor("#e205b2");

    }
    public void col11(View view){

        COLOR = Color.WHITE;

    }
/*
    public void  save(){
        View content = (PaintView) findViewById(R.id.paintView);
        content.setDrawingCacheEnabled(true);
        content.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = content.getDrawingCache();
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file = new File(path+"/image.png");
        FileOutputStream ostream;
        try {
            file.createNewFile();
            ostream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
            ostream.flush();
            ostream.close();
            Toast.makeText(getApplicationContext(), "image saved", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
        }
    }*/
}
