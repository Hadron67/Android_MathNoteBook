package THREE;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;

public class Mesh implements Cloneable{
	private FloatBuffer TextureBuffer=null;
	private FloatBuffer verticesBuffer=null;
	private ShortBuffer indicesBuffer=null;
	private FloatBuffer normalBuffer=null;
	 //��������
	private FloatBuffer materialAmbientBuffer=null;
	private FloatBuffer materialDiffuseBuffer=null;
	private FloatBuffer materialSpecularBuffer=null;
	private float shinness=64.0f;
	//����
	private int type=GL10.GL_TRIANGLES;
	private int numOfIndices=-1;
	private int TextureId=-1;
	private float rgba[]={1f,1f,1f,1f};
	private FloatBuffer colorBuffer=null;
	private Bitmap mBitmap;
	public float x=0;
	public float y=0;
	public float z=0;
	public float rx=0;
	public float ry=0;
	public float rz=0;
	public void draw(GL10 gl,boolean castShadow){
		gl.glFrontFace(GL10.GL_CCW);
		//gl.glEnable(GL10.GL_CULL_FACE);
		gl.glCullFace(GL10.GL_BACK);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
		if(normalBuffer!=null){
			gl.glNormalPointer(GL10.GL_FLOAT, 0,normalBuffer);
		}
		gl.glVertexPointer(3,GL10.GL_FLOAT,0,verticesBuffer);
		gl.glColor4f(rgba[0],rgba[1],rgba[2],rgba[3]);
		if(colorBuffer!=null){
			gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
			gl.glColorPointer(4,GL10.GL_FLOAT,0,colorBuffer);
		}
		else{
			gl.glEnable(GL10.GL_COLOR_MATERIAL);
		}
		if(castShadow){
			gl.glColor4f(0, 0, 0,rgba[3]);
		}
		gl.glTranslatef(x,y,z);
		gl.glRotatef(rx, 1, 0, 0);
		gl.glRotatef(ry, 0, 1, 0);
		gl.glRotatef(rz, 0, 0, 1);
		if(materialAmbientBuffer!=null&&materialDiffuseBuffer!=null&materialSpecularBuffer!=null){
			gl.glMaterialfv(GL10.GL_FRONT_AND_BACK,GL10.GL_AMBIENT, materialAmbientBuffer);     
			gl.glMaterialfv(GL10.GL_FRONT_AND_BACK,GL10.GL_DIFFUSE, materialDiffuseBuffer);     
			gl.glMaterialfv(GL10.GL_FRONT_AND_BACK,GL10.GL_SPECULAR, materialSpecularBuffer);     
			gl.glMaterialf(GL10.GL_FRONT_AND_BACK,GL10.GL_SHININESS, shinness);
		}
		gl.glDrawElements(type,numOfIndices,GL10.GL_UNSIGNED_SHORT,indicesBuffer);
		gl.glRotatef(-rz, 0, 0, 1);
		gl.glRotatef(-ry, 0, 1, 0);
		gl.glRotatef(-rx, 1, 0, 0);
		gl.glTranslatef(-x,-y,-z);
		gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisable(GL10.GL_CULL_FACE);
		gl.glDisable(GL10.GL_COLOR_MATERIAL);
	}
	protected void setVertices(float[] vertices){
		ByteBuffer vbb=ByteBuffer.allocateDirect(vertices.length*4);
		vbb.order(ByteOrder.nativeOrder());
		verticesBuffer=vbb.asFloatBuffer();
		verticesBuffer.put(vertices);
		verticesBuffer.position(0);
	}
	protected void setIndices(short[] indices){
		ByteBuffer ibb=ByteBuffer.allocateDirect(indices.length*2);
		ibb.order(ByteOrder.nativeOrder());
		indicesBuffer=ibb.asShortBuffer();
		indicesBuffer.put(indices);
		indicesBuffer.position(0);
		numOfIndices=indices.length;
	}
	protected void setNormals(float[] normal){
		ByteBuffer nbb=ByteBuffer.allocateDirect(normal.length*4);
		nbb.order(ByteOrder.nativeOrder());
		normalBuffer=nbb.asFloatBuffer();
		normalBuffer.put(normal);
		normalBuffer.position(0);
	}
	protected void setColor(float r,float g,float b,float a){
		rgba[0]=r;
		rgba[1]=g;
		rgba[2]=b;
		rgba[3]=a;
	}
	public void setColors(float[] colors){
		ByteBuffer cbb=ByteBuffer.allocateDirect(colors.length*4);
		cbb.order(ByteOrder.nativeOrder());
		rgba[3]=colors[3];
		colorBuffer = cbb.asFloatBuffer();
		colorBuffer.put(colors);
		colorBuffer.position(0);
	}
	protected void setTextureCoordinates(float[] textureCoords){
		ByteBuffer byteBuf=ByteBuffer.allocateDirect(textureCoords.length*4);
		byteBuf.order(ByteOrder.nativeOrder());
		TextureBuffer=byteBuf.asFloatBuffer();
		TextureBuffer.put(textureCoords);
		TextureBuffer.position(0);
	}
	public void setMaterialAmbient(float[] MAmbient){
		ByteBuffer mbb=ByteBuffer.allocateDirect(MAmbient.length*4);
		mbb.order(ByteOrder.nativeOrder());
		materialAmbientBuffer = mbb.asFloatBuffer();
		materialAmbientBuffer.put(MAmbient);
		materialAmbientBuffer.position(0);
	}
    public void setMaterialDiffuse(float[] MDiffuse){
		ByteBuffer mbb=ByteBuffer.allocateDirect(MDiffuse.length*4);
		mbb.order(ByteOrder.nativeOrder());
		materialDiffuseBuffer = mbb.asFloatBuffer();
		materialDiffuseBuffer.put(MDiffuse);
		materialDiffuseBuffer.position(0);
	}
    public void setMaterialSpecular(float[] MSpecular){
		ByteBuffer mbb=ByteBuffer.allocateDirect(MSpecular.length*4);
		mbb.order(ByteOrder.nativeOrder());
		materialSpecularBuffer = mbb.asFloatBuffer();
		materialSpecularBuffer.put(MSpecular);
		materialSpecularBuffer.position(0);
	}
	protected void setShinness(float shinness){
		this.shinness=shinness;
	}
	protected void setMaterialColors(float r,float g,float b,float a){
		rgba[3]=a;
		this.setMaterialAmbient(new float[]{r,g,b,a});
		this.setMaterialDiffuse(new float[]{r,g,b,a});
		this.setMaterialSpecular(new float[]{r,g,b,a});
	}
	protected float[] getEq(){
		float[] Eq=new float[4];
		float r1=(float) (rx*Math.PI/180);
		float r2=(float) (ry*Math.PI/180);
		float r3=(float) (rz*Math.PI/180);
		Eq[0]=(float) (Math.cos(r1)*Math.sin(r2)*Math.cos(r3)+Math.sin(r1)*Math.sin(r3));
		Eq[1]=(float) (Math.cos(r1)*Math.sin(r2)*Math.sin(r3)-Math.sin(r1)*Math.cos(r3));
		Eq[2]=(float) (Math.cos(r1)*Math.cos(r2));
		Eq[3]=-Eq[0]*x-Eq[1]*y-Eq[2]*z;
		return Eq;
	}
	public void invertNormals(){
		float[] normals=new float[normalBuffer.limit()];
		normalBuffer.get(normals, 0, normals.length);
		for(int i=0;i<normals.length;i++){
			normals[i]=-normals[i];
		}
		this.setNormals(normals);
	}
	public Object clone(){
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return 0;
		}
	}
	public void setType(String t){
		if(t.equals("triangles")){
			this.type=GL10.GL_TRIANGLES;
		}
		else if(t.equals("lines")){
			this.type=GL10.GL_LINES;
		}
		else if(t.equals("points")){
			this.type=GL10.GL_POINTS;
		}
		else if(t.equals("line_strip")){
			this.type=GL10.GL_LINE_STRIP;
		}
		else if(t.equals("line_loop")){
			this.type=GL10.GL_LINE_LOOP;
		}
		else if(t.equals("triangle_strip")){
			this.type=GL10.GL_TRIANGLE_STRIP;
		}
		else if(t.equals("triangle_fan")){
			this.type=GL10.GL_TRIANGLE_FAN;
		}
	}
}
