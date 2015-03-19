package THREE;

public class Torus extends Mesh{
	private float R;
	private float r;
	private int ws;
	private int hs;
	private float ps;
	private float pe;
	private float ts;
	private float te;
	public Torus(float R,float r,int widthSegments,int heightSegments,float phiStart,float phiEnd,float thetaStart,float thetaEnd){
		this.R=R;
		this.r=r;
		this.ws=widthSegments;
		this.hs=heightSegments;
		this.ps=phiStart;
		this.pe=phiEnd;
		this.ts=thetaStart;
		this.te=thetaEnd;
		init(this.R,this.r,this.ws,this.hs,this.ps,this.pe,this.ts,this.te);
	}
	private void init(float R,float r,int widthSegments,int heightSegments,float phiStart,float phiEnd,float thetaStart,float thetaEnd){
		float[] vertices=new float[(widthSegments+1)*(heightSegments+1)*3];
		float[] normals=new float[(widthSegments+1)*(heightSegments+1)*3];
		short[] indices=new short[(widthSegments+1)*(heightSegments+1)*6];
		float dphi=(phiEnd-phiStart)/heightSegments;
		float dtheta=(thetaEnd-thetaStart)/widthSegments;
		int currentVertex=0;
		int currentIndex=0;
		short w=(short) (heightSegments+1);
		for(int i=0;i<=widthSegments;i++){
			for(int j=0;j<=heightSegments;j++){
				float theta=thetaStart+dtheta*i;
				float phi=phiStart+dphi*j;
				float x=(float) ((R+r*Math.cos(phi))*Math.cos(theta));
				float y=(float) ((R+r*Math.cos(phi))*Math.sin(theta));
				float z=(float) (r*Math.sin(phi));
				vertices[currentVertex]=x;
				vertices[currentVertex+1]=y;
				vertices[currentVertex+2]=z;
				normals[currentVertex]=(float) (Math.cos(phi)*Math.cos(theta));
				normals[currentVertex+1]=(float) (Math.cos(phi)*Math.sin(theta));
				normals[currentVertex+2]=(float) (Math.sin(phi));
				currentVertex+=3;
				int n=(heightSegments+1)*i+j;
				if(i<widthSegments&&j<heightSegments){
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
	public Torus(float R,float r,int widthSegments,int heightSegments){
		this(R,r,widthSegments,heightSegments,0,(float)Math.PI*2,0,(float)Math.PI*2);
	}
	public Torus(float R,float r){
		this(R,r,50,25);
	}
	public void setRadius1(float R){
		this.R=R;
		init(this.R,this.r,this.ws,this.hs,this.ps,this.pe,this.ts,this.te);
	}
	public void setRadius2(float r){
		this.r=r;
		init(this.R,this.r,this.ws,this.hs,this.ps,this.pe,this.ts,this.te);
	}
	public void setHeightSegments(int hs){
		this.hs=hs;
		init(this.R,this.r,this.ws,this.hs,this.ps,this.pe,this.ts,this.te);
	}
	public void setWidthSegments(int ws){
		this.ws=ws;
		init(this.R,this.r,this.ws,this.hs,this.ps,this.pe,this.ts,this.te);
	}
	public void setPhiStart(float ps){
		this.ps=ps;
		init(this.R,this.r,this.ws,this.hs,this.ps,this.pe,this.ts,this.te);
	}
	public void setPhiEnd(float pe){
		this.pe=pe;
		init(this.R,this.r,this.ws,this.hs,this.ps,this.pe,this.ts,this.te);
	}
	public void setThetaStart(float ts){
		this.ts=ts;
		init(this.R,this.r,this.ws,this.hs,this.ps,this.pe,this.ts,this.te);
	}
	public void setThetaEnd(float te){
		this.te=te;
		init(this.R,this.r,this.ws,this.hs,this.ps,this.pe,this.ts,this.te);
	}
}
