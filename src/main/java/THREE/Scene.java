package THREE;

import android.opengl.Matrix;

import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

public class Scene extends Mesh{
	private Vector<Mesh> children=new Vector<Mesh>();
	private float r=0;
	private float g=0;
	private float b=0;
	private float a=(float) 0.5;

    public float[] viewMatrix=new float[]{1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1};
	public void draw(GL10 gl,boolean castShadow,float[] lightPosition){
		gl.glClearColor(r, g, b, a);
        gl.glMultMatrixf(viewMatrix,0);
        gl.glTranslatef(x,y,z);
        gl.glRotatef(rx,1,0,0);
        gl.glRotatef(ry,0,1,0);
        gl.glRotatef(rz,0,0,1);
		int size=children.size();
		for(int i=0;i<size;i++){
			gl.glEnable(GL10.GL_DEPTH_TEST);
			gl.glEnable(GL10.GL_LIGHTING);
			if(children.get(i) instanceof Plane) {
				children.get(i).draw(gl,false);
			}
		}
		if(castShadow){
			gl.glPushMatrix();
			gl.glDisable(GL10.GL_DEPTH_TEST);
			gl.glDisable(GL10.GL_LIGHTING);
			for(int i=0;i<size;i++){
				if(children.get(i) instanceof Plane){
					gl.glMultMatrixf(MakeShadowMap(children.get(i).getEq(),lightPosition), 0);
					for(int j=0;j<size;j++){
						if(i!=j){
							children.get(j).draw(gl,true);
						}
					}
				}
				
			}
		}
		for(int i=0;i<size;i++){
			gl.glPopMatrix();
			gl.glEnable(GL10.GL_DEPTH_TEST);
			gl.glEnable(GL10.GL_LIGHTING);
			if(!(children.get(i) instanceof Plane)){ 
				children.get(i).draw(gl,false);
			}
		}
        gl.glRotatef(-rz,0,0,1);
        gl.glRotatef(-ry,0,1,0);
        gl.glRotatef(-rx,1,0,0);
        gl.glTranslatef(-x,-y,-z);
	}
	
	public void add(Mesh object){
		children.add(children.size(), object);
	}
	public void add(int location,Mesh object){
		children.add(location, object);
	}
	public void clear(){
		children.clear();
	}
	public Mesh get(int location){
		return children.get(location);
	}
	public Mesh remove(int location){
	
		return children.remove(location);
	}
	public boolean remove(Object object){
		return children.remove(object);
	}
	public int size(){
		return children.size();
	}
	public void setClearColor(float r,float g,float b,float a){
		this.r=r;
		this.g=g;
		this.b=b;
		this.a=a;
	}
	protected float[] MakeShadowMap(float[] planeEq,float[] lightPos){
		float[] proj=new float[16];
		float a = planeEq[0];
		float b = planeEq[1];
		float c = planeEq[2];
		float d = planeEq[3];
		float dx = lightPos[0];
		float dy = lightPos[1];
		float dz = lightPos[2];
		proj[0] = b * dy + c * dz;
		proj[1] = -a * dy;
		proj[2] = -a * dz;
		proj[3] = (float) 0.0;
		proj[4] = -b * dx;
		proj[5] = a * dx + c * dz;
		proj[6] = -b * dz;
		proj[7] = (float) 0.0;
		proj[8] = -c * dx;
		proj[9] = -c * dy;
		proj[10] = a * dx + b * dy;
		proj[11] = (float) 0.0;
		proj[12] = -d * dx;
		proj[13] = -d * dy;
		proj[14] = -d * dz;
		proj[15] = a * dx + b * dy + c * dz;
		return proj;
	}
}
