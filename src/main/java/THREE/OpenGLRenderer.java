package THREE;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

import android.graphics.Canvas;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.os.Parcelable;

import java.io.Serializable;
import java.net.InterfaceAddress;

public class OpenGLRenderer implements Renderer,Cloneable{
	public Scene scene=new Scene();
	private boolean castShadow=false;
    private Drawer d;
	public static boolean allow=true;
	public GLlight light0=new GLlight();
	public float cameraX=0;
	public float cameraY=0;
	public float cameraZ=10;
	public float LookAtX=0;
	public float LookAtY=0;
	public float LookAtZ=0;
	public float cameraupX=0;
	public float cameraupY=1;
	public float cameraupZ=0;
    public interface Drawer{
        public void draw(GL10 gl);
    }
    public OpenGLRenderer(Drawer d){
        this.d=d;
    }
	public void LookAt(float upX,float upY,float upZ){
		LookAtX=upX;
		LookAtY=upY;
		LookAtZ=upZ;
	}
	public void setCamera(float x,float y,float z){
		cameraX=x;
		cameraY=y;
		cameraZ=z;
	}
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		//gl.glClearColor(0f, 0f, 0f, 0.5f);
		gl.glShadeModel(GL10.GL_SMOOTH);

		//gl.glClear(GL10.GL_COLOR_BUFFER_BIT|GL10.GL_DEPTH_BUFFER_BIT);
		gl.glClear(GL10.GL_DEPTH_BITS);
		gl.glClearDepthf(1.0f);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glEnable(GL10.GL_NORMALIZE);
		gl.glDepthMask(true);
		gl.glDepthFunc(GL10.GL_LEQUAL);
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,GL10.GL_NICEST);
        gl.glEnable(GL10.GL_POINT_SMOOTH);
        gl.glEnable(GL10.GL_LINE_SMOOTH);
        gl.glHint(GL10.GL_LINE_SMOOTH_HINT,GL10.GL_NICEST);//消锯齿
        GLlight.init();
        gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		 // ����͸����ʾ
		gl.glEnable(GL10.GL_ALPHA_TEST);
		gl.glAlphaFunc(GL10.GL_GREATER,0.1f);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		gl.glViewport(0,0,width,height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		GLU.gluPerspective(gl,45.0f,(float)width/(float)height,0.1f,200.0f);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT|GL10.GL_DEPTH_BUFFER_BIT);
		gl.glColor4f(1, 1, 1, 1);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glEnable(GL10.GL_LIGHTING);
		light0.enable(gl);
		scene.draw(gl,castShadow,light0.getPosition());
		gl.glLoadIdentity();
		GLU.gluLookAt(gl, cameraX,cameraY, cameraZ, LookAtX, LookAtY, LookAtZ, cameraupX, cameraupY, cameraupZ);
        if(this.d!=null) this.d.draw(gl);
	}
	public void enableShadow(){
		castShadow=true;
	}
	public void disableShadow(){
		castShadow=false;
	}

    @Override
    public Object clone() {
        try {
            return super.clone();
        }
        catch(CloneNotSupportedException e){
            return null;
        }
    }
}
