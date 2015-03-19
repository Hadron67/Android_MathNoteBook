package com.example.cfy.polandtest;

import android.content.Context;
import android.graphics.Canvas;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import javax.microedition.khronos.opengles.GL10;

import THREE.OpenGLRenderer;

/**
 * Created by cfy on 14-11-22.
 */
public class MyGLView extends GLSurfaceView {
    private OpenGLRenderer renderer;
    public MyGLView(Context context, AttributeSet attrs){
        super(context,attrs);

    }
    public MyGLView(Context context,OpenGLRenderer renderer,AttributeSet attrs){
        super(context,attrs);
        this.renderer=renderer;
        super.setRenderer(renderer);
    }
    public MyGLView(Context context,OpenGLRenderer renderer){
        super(context);
        this.renderer=renderer;
        super.setRenderer(renderer);
        Canvas canvas=new Canvas();
        this.setFocusable(true);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        super.dispatchTouchEvent(event);
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getHistorySize()>0) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    float x1 = event.getHistoricalX(0);
                    float y1 = event.getHistoricalY(0);
                    float x2 = event.getX();
                    float y2 = event.getY();
                    renderer.scene.ry += (x2 - x1) / 2;
                    renderer.scene.rx += (y2 - y1) / 2;
//                    Matrix.rotateM(renderer.scene.viewMatrix,0,(x2-x1)/10,0,1,0);
//                    Matrix.rotateM(renderer.scene.viewMatrix,0,(y2-y1)/10,1,0,0);
                    event.getHistorySize();
                    break;
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_UP:
                    break;
                default:
                    break;
            }
        }
        return super.onTouchEvent(event);
    }
}
