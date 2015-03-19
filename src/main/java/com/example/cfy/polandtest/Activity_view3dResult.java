package com.example.cfy.polandtest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import THREE.OpenGLRenderer;

/**
 * Created by cfy on 14-11-30.
 */
public class Activity_view3dResult extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        String key=(String)intent.getExtras().get("aaa");
        OpenGLRenderer r=(OpenGLRenderer) CentralStation.map.get(key);
        MyGLView view=new MyGLView(this,r);
        this.getActionBar().setBackgroundDrawable(new Drawable() {
            @Override
            public void draw(Canvas canvas) {
                canvas.drawColor(Color.parseColor("#8fb5e8"));
            }

            @Override
            public void setAlpha(int alpha) {

            }

            @Override
            public void setColorFilter(ColorFilter cf) {

            }

            @Override
            public int getOpacity() {
                return 0;
            }
        });
        setContentView(view);
    }
}
