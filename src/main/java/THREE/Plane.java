package THREE;

public class Plane extends Mesh{
	private float h;
	private float w;
	private int ws;
	private int hs;
	public Plane(){
		this(1,1,1,1);
	}
	public Plane(float width,float height){
		this(width,height,1,1);
	}
	public Plane(float width,float height,int widthSegments,int heightSegments){
		this.h=height;
		this.w=width;
		this.ws=widthSegments;
		this.hs=heightSegments;
		init(this.w,this.h,this.ws,this.hs);
	}
	private void init(float width,float height,int widthSegments,int heightSegments){
		float[] vertices=new float[(widthSegments+1)*(heightSegments+1)*3];
		float[] normals=new float[(widthSegments+1)*(heightSegments+1)*3];
		short[] indices=new short[(widthSegments+1)*(heightSegments+1)*6];
		float xOffset=-width/2;
		float yOffset=-height/2;
		float xWidth=width/(widthSegments);
		float yHeight=height/(heightSegments);
		int currentVertex=0;
		int currentIndex=0;
		short w=(short) (widthSegments+1);
		for(int y=0;y<=heightSegments;y++){
			for(int x=0;x<=widthSegments;x++){
				vertices[currentVertex]=xOffset+x*xWidth;
				vertices[currentVertex+1]=yOffset+y*yHeight;
				vertices[currentVertex+2]=0;
				normals[currentVertex]=0;
				normals[currentVertex+1]=0;
				normals[currentVertex+2]=1;
				currentVertex+=3;
				int n=y*(widthSegments+1)+x;
				if(y<heightSegments&&x<widthSegments){
					indices[currentIndex]=(short) n;
					indices[currentIndex+1]=(short) (n+1);
					indices[currentIndex+2]=(short) (n+w);
					
					indices[currentIndex+3]=(short) (n+1);
					indices[currentIndex+4]=(short) (n+1+w);
					indices[currentIndex+5]=(short) (n+w);
					
					currentIndex+=6;
				}
			}
		}
		setIndices(indices);
		setVertices(vertices);
		setNormals(normals);
	}
	public void setWidth(float w){
		this.w=w;
		init(this.w,this.h,this.ws,this.hs);
	}
	public void setHeight(float h){
		this.h=h;
		init(this.w,this.h,this.ws,this.hs);
	}
	public void setHeightSegments(int hs){
		this.hs=hs;
		init(this.w,this.h,this.ws,this.hs);
	}
	public void setWidthSegments(int ws){
		this.ws=ws;
		init(this.w,this.h,this.ws,this.hs);
	}
}
