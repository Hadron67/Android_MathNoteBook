package THREE;

public class Cylinder extends Mesh{
	private float R;
	private float r;
	private float h;
	private int hs;
	private int ws;
	private float ps;
	private float pe;
	public Cylinder(float R,float r,float height,int widthSegments,int heightSegments,float phiStart,float phiEnd){
		this.R=R;
		this.r=r;
		this.h=height;
		this.hs=heightSegments;
		this.ws=widthSegments;
		this.ps=phiStart;
		this.pe=phiEnd;
		init(this.R,this.r,this.h,this.ws,this.hs,this.ps,this.pe);
	}
	private void init(float R,float r,float height,int widthSegments,int heightSegments,float phiStart,float phiEnd){
		float[] vertices=new float[(heightSegments+3)*(widthSegments+1)*3+6];
		float[] normals=new float[(heightSegments+3)*(widthSegments+1)*3+6];
		short[] indices=new short[(heightSegments+1)*(widthSegments+1)*6+(widthSegments-1)*6];
		float dphi=(phiEnd-phiStart)/widthSegments;
		float dheight=height/heightSegments;
		float modul=(float) Math.sqrt(height*height+(R-r)*(R-r));
		int currentVertex=3;
		int currentIndex=0;
		vertices[0]=0;
		vertices[1]=0;
		vertices[2]=height/2;
		normals[0]=0;
		normals[1]=0;
		normals[2]=1;
		
		vertices[vertices.length-3]=0;
		vertices[vertices.length-2]=0;
		vertices[vertices.length-1]=-height/2;
		normals[normals.length-3]=0;
		normals[normals.length-2]=0;
		normals[normals.length-1]=-1;
		for(int i=0;i<=widthSegments;i++){
			float phi=phiStart+dphi*i;
			vertices[currentVertex]=(float) (r*Math.cos(phi));
			vertices[currentVertex+1]=(float) (r*Math.sin(phi));
			vertices[currentVertex+2]=height/2;
			normals[currentVertex]=0;
			normals[currentVertex+1]=0;
			normals[currentVertex+2]=1;
			currentVertex+=3;
			if(i>0){
				indices[currentIndex]=0;
				indices[currentIndex+1]=(short) (i);
				indices[currentIndex+2]=(short) (i+1);
				currentIndex+=3;
			}
		}
		int Index2=widthSegments+2;
		for(int i=0;i<=heightSegments;i++){
			for(int j=0;j<=widthSegments;j++){
				float phi=phiStart+dphi*j;
				float h=height/2-dheight*i;
				float radius=r+(R-r)*i/heightSegments;
				vertices[currentVertex]=(float) (radius*Math.cos(phi));
				vertices[currentVertex+1]=(float) (radius*Math.sin(phi));
				vertices[currentVertex+2]=h;
				normals[currentVertex]=(float) (height*Math.cos(phi)/modul);
				normals[currentVertex+1]=(float) (height*Math.sin(phi)/modul);
				normals[currentVertex+2]=(R-r)/modul;
				currentVertex+=3;
				int n=i*(widthSegments+1)+j+Index2;
				if(i<heightSegments&&j<widthSegments){
					indices[currentIndex]=(short) n;
					indices[currentIndex+1]=(short) (n+1);
					indices[currentIndex+2]=(short) (n+1+widthSegments+1);
					indices[currentIndex+3]=(short) n;
					indices[currentIndex+4]=(short) (n+1+widthSegments);
					indices[currentIndex+5]=(short) (n+1+widthSegments+1);
					currentIndex+=6;
				}
			}
		}
		int Index3=widthSegments+1+(heightSegments+1)*(widthSegments+1)+1;
		for(int i=0;i<=widthSegments;i++){
			float phi=phiStart+dphi*i;
			vertices[currentVertex]=(float) (R*Math.cos(phi));
			vertices[currentVertex+1]=(float) (R*Math.sin(phi));
			vertices[currentVertex+2]=-height/2;
			normals[currentVertex]=0;
			normals[currentVertex+1]=0;
			normals[currentVertex+2]=-1;
			currentVertex+=3;
			if(i>0){
				indices[currentIndex]=(short) (vertices.length/3-1);
				indices[currentIndex+1]=(short) (i-1+Index3);
				indices[currentIndex+2]=(short) (i+Index3);
				currentIndex+=3;
			}
		}
		setIndices(indices);
		setVertices(vertices);
		setNormals(normals);
	}
	public Cylinder(float R,float r,float height,int widthSegments,int heightSegments){
		this(R,r,height,widthSegments,heightSegments,0,(float) (2*Math.PI));
	}
	public Cylinder(float R,float r,float height){
		this(R,r,height,50,100);
	}
	public void setRadius1(float R){
		this.R=R;
		init(this.R,this.r,this.h,this.ws,this.hs,this.ps,this.pe);
	}
	public void setRadius2(float r){
		this.r=r;
		init(this.R,this.r,this.h,this.ws,this.hs,this.ps,this.pe);
	}
	public void setHeight(float h){
		this.h=h;
		init(this.R,this.r,this.h,this.ws,this.hs,this.ps,this.pe);
	}
	public void setPhiStart(float phi){
		this.ps=phi;
		init(this.R,this.r,this.h,this.ws,this.hs,this.ps,this.pe);
	}
	public void setPhiEnd(float phi){
		this.pe=phi;
		init(this.R,this.r,this.h,this.ws,this.hs,this.ps,this.pe);
	}
	public void setHeightSegments(int hs){
		this.hs=hs;
		init(this.R,this.r,this.h,this.ws,this.hs,this.ps,this.pe);
	}
	public void setWidthSegments(int ws){
		this.ws=ws;
		init(this.R,this.r,this.h,this.ws,this.hs,this.ps,this.pe);
	}
}
