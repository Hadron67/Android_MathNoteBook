package THREE;



public class EditableMesh extends Mesh{
	private float[] vertices;
	private float[] normals;
	private short[] indices;
	public EditableMesh(){
		vertices=new float[]{0,0,0};
		normals=new float[]{0,1,0};
		indices=new short[]{0};
		init();
	}
	private void init(){
		setIndices(indices);
		setVertices(vertices);
		setNormals(normals);
	}
	public void setvertices(float[] vertex){
		this.vertices=vertex;
		init();
	}
	public void setindices(short[] indices){
		this.indices=indices;
		init();
	}
	public void setnormals(float[] normals){
		this.normals=normals;
		init();
	}
	public void pushvertex(float x,float y,float z){
		float[] temp=new float[this.vertices.length+3];
		for(int i=0;i<vertices.length;i++){
			temp[i]=vertices[i];
		}
		temp[this.vertices.length]=x;
		temp[this.vertices.length+1]=y;
		temp[this.vertices.length+2]=z;
		this.vertices=temp;
	}
	public void pushindex(int n){
		short[] temp=new short[this.indices.length+1];
		for(int i=0;i<indices.length;i++){
			temp[i]=indices[i];
		}
		temp[this.indices.length]=(short) n;
		this.indices=temp;
	}
	public void pushnormal(float x,float y,float z){
		float[] temp=new float[this.normals.length+3];
		for(int i=0;i<normals.length;i++){
			temp[i]=normals[i];
		}
		temp[this.normals.length]=x;
		temp[this.normals.length+1]=y;
		temp[this.normals.length+2]=z;
		this.normals=temp;
	}
	public void AutoNormal(){
		normals=new float[vertices.length];
		indices=new short[vertices.length/3];
		for(int i=0;i<normals.length;i+=9){
			float x1=vertices[i];
			float y1=vertices[i+1];
			float z1=vertices[i+2];
			float x2=vertices[i+3];
			float y2=vertices[i+4];
			float z2=vertices[i+5];
			float x3=vertices[i+6];
			float y3=vertices[i+7];
			float z3=vertices[i+8];
			float x=(y1-y2)*(z1-z3)-(z1-z2)*(y1-y3);
			float y=(z1-z2)*(x1-x3)-(x1-x2)*(z1-z3);
			float z=(x1-x2)*(y1-y3)-(y1-y2)*(x1-x3);
			float s=(float) Math.sqrt(x*x+y*y+z*z);
			x/=s;
			y/=s;
			z/=s;
			normals[i]=x;
			normals[i+1]=y;
			normals[i+2]=z;
			normals[i+3]=x;
			normals[i+4]=y;
			normals[i+5]=z;
			normals[i+6]=x;
			normals[i+7]=y;
			normals[i+8]=z;
		}
		for(int i=0;i<vertices.length/3;i++){
			indices[i]=(short) i;
		}
		init();
	}
	public void setX(int index,float x){
		vertices[index*3]=x;
		init();
	}
	public void setY(int index,float y){
		vertices[index*3+1]=y;
	}
	public void setZ(int index,float z){
		vertices[index*3+2]=z;
		init();
	}
	public void setNX(int index,float x){
		normals[index*3]=x;
		init();
	}
	public void setNY(int index,float y){
		normals[index*3+1]=y;
		init();
	}
	public void setNZ(int index,float z){
		normals[index*3+2]=z;
		init();
	}
	public float getX(int index){
		return vertices[index*3];
	}
	public float getY(int index){
		return vertices[index*3+1];
	}
	public float getZ(int index){
		return vertices[index*3+2];
	}
	public float getNX(int index){
		return normals[index*3];
	}
	public float getNY(int index){
		return normals[index*3+1];
	}
	public float getNZ(int index){
		return normals[index*3+2];
	}
}
