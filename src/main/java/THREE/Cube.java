package THREE;

public class Cube extends Mesh{
	private float a;
	private float b;
	private float c;
	public Cube(float a,float b,float c){
		this.a=a;
		this.b=b;
		this.c=c;
		init(this.a,this.b,this.c);
	}
	public void setA(float a){
		this.a=a;
		init(this.a,this.b,this.c);
	}
	public void setB(float b){
		this.b=b;
		init(this.a,this.b,this.c);
	}
	public void setC(float c){
		this.c=c;
		init(this.a,this.b,this.c);
	}
	private void init(float a,float b,float c){
		a/=2;
		b/=2;
		c/=2;
		float vertices[]={
				-a,-b,c,
				a,-b,c,
				a,b,c,
				-a,b,c,//up
				
				-a,-b,-c,
				a,-b,-c,
				a,b,-c,
				-a,b,-c,//down
				
				a,b,c,
				a,b,-c,
				a,-b,-c,
				a,-b,c,//front
				
				-a,b,c,
				-a,b,-c,
				-a,-b,-c,
				-a,-b,c,//back
				
				a,b,c,
				-a,b,c,
				-a,b,-c,
				a,b,-c,//right
				
				a,-b,c,
				-a,-b,c,
				-a,-b,-c,
				a,-b,-c,//left
				};
		short indices[]={
				0,1,2,0,2,3,
				4,5,6,4,7,6,
				8,9,10,8,11,10,
				12,13,14,12,15,14,
				16,17,18,16,19,18,
				20,21,22,20,23,22
		};
		float normals[]={
				0,0,1,
				0,0,1,
				0,0,1,
				0,0,1,
				
				0,0,-1,
				0,0,-1,
				0,0,-1,
				0,0,-1,
				
				1,0,0,
				1,0,0,
				1,0,0,
				1,0,0,
				
				-1,0,0,
				-1,0,0,
				-1,0,0,
				-1,0,0,
				
				0,1,0,
				0,1,0,
				0,1,0,
				0,1,0,
				
				0,-1,0,
				0,-1,0,
				0,-1,0,
				0,-1,0,
		};
		setIndices(indices);
		setNormals(normals);
		setVertices(vertices);
	}
}
