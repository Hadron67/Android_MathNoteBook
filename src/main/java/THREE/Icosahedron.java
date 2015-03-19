package THREE;

import android.util.Log;

public class Icosahedron extends Mesh{
	private float phi=(float) ((1+Math.sqrt(5))/2);
	private float[] normals=new float[180];
	private short[] indices=new short[60];
	private float[] vertices={
			phi,1,0,
			1,0,phi,
			0,phi,1,
			
			1,0,phi,
			phi,-1,0,
			phi,1,0,
			
			1,0,phi,
			-1,0,phi,
			0,phi,1,
			
			0,phi,1,
			0,phi,-1,
			phi,1,0,//
			
			phi,-1,0,
			phi,1,0,
			1,0,-phi,
			
			phi,1,0,
			1,0,-phi,
			0,phi,-1,
			
			0,phi,-1,
			0,phi,1,
			-phi,1,0,
			
			-1,0,phi,
			0,phi,1,
			-phi,1,0,
			
			1,0,phi,
			-1,0,phi,
			0,-phi,1,
			
			phi,-1,0,
			1,0,phi,
			0,-phi,1,//
			
			-phi,-1,0,
			-1,0,phi,
			0,-phi,1,
			
			-phi,-1,0,
			0,-phi,-1,
			0,-phi,1,
			
			0,-phi,1,
			0,-phi,-1,
			phi,-1,0,
			
			0,-phi,-1,
			phi,-1,0,
			1,0,-phi,
			
			0,-phi,-1,
			-1,0,-phi,
			1,0,-phi,
			
			-1,0,-phi,
			1,0,-phi,
			0,phi,-1,//
			
			-1,0,-phi,
			0,phi,-1,
			-phi,1,0,
			
			-1,0,-phi,
			-phi,1,0,
			-phi,-1,0,
			
			-phi,1,0,
			-phi,-1,0,
			-1,0,phi,
			
			0,-phi,-1,
			-1,0,-phi,
			-phi,-1,0
	};
	public Icosahedron(float a){
		for(int i=0;i<vertices.length;i++){
			vertices[i]*=a/2;
			if(i<indices.length) indices[i]=(short) i;
		}
		Log.d("lenth",vertices.length+"");
		for(int i=0;i<=vertices.length-9;i+=9){
			float[] nor=getNormal(vertices[i],vertices[i+1],vertices[i+2],vertices[i+3],vertices[i+4],vertices[i+5],vertices[i+6],vertices[i+7],vertices[i+8]);
			normals[i]=nor[0];
			normals[i+1]=nor[1];
			normals[i+2]=nor[2];
			normals[i+3]=nor[0];
			normals[i+4]=nor[1];
			normals[i+5]=nor[2];
			normals[i+6]=nor[0];
			normals[i+7]=nor[1];
			normals[i+8]=nor[2];
			Log.d("opengl",i+"");
		}
		setVertices(vertices);
		setNormals(normals);
		setIndices(indices);
	}
	private float[] getNormal(float x1,float y1,float z1,float x2,float y2,float z2,float x3,float y3,float z3){
		float[] normal=new float[3];
		float modul;
		float x=(x1+x2+x3)/3;
		float y=(y1+y2+y3)/3;
		float z=(z1+z2+z3)/3;
		modul=(float) Math.sqrt(x*x+y*y+z*z);
		normal[0]=x/modul;
		normal[1]=y/modul;
		normal[2]=z/modul;
		return normal;
	}
}
