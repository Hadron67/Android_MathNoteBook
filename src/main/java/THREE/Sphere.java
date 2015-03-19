package THREE;

public class Sphere extends Mesh{
	private float radius;
	private int ws;
	private int hs;
	private float ps;
	private float pe;
	private float ts;
	private float te;
	public Sphere(float R,int widthSegments,int heightSegments,float phiStart,float phiEnd,float thetaStart,float thetaEnd){
		radius=R;
		ws=widthSegments;
		hs=heightSegments;
		ps=phiStart;
		pe=phiEnd;
		ts=thetaStart;
		te=thetaEnd;
		init(radius,ws,hs,ps,pe,ts,te);
	}
	private void init(float R,int widthSegments,int heightSegments,float phiStart,float phiEnd,float thetaStart,float thetaEnd){
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
				float x=(float) (R*Math.sin(theta)*Math.cos(phi));
				float y=(float) (R*Math.sin(theta)*Math.sin(phi));
				float z=(float) (R*Math.cos(theta));
				vertices[currentVertex]=x;
				vertices[currentVertex+1]=y;
				vertices[currentVertex+2]=z;
				normals[currentVertex]=x/R;
				normals[currentVertex+1]=y/R;
				normals[currentVertex+2]=z/R;
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
	public Sphere(float R,int widthSegments,int heightSegments){
		this(R,widthSegments,heightSegments,0,(float)Math.PI*2,0,(float)Math.PI);
		
	}
	public Sphere(float R){
		this(R,25,50);
	}
	public void setRadius(float R){
		this.radius=R;
		init(this.radius,this.ws,this.hs,this.ps,this.pe,this.ts,this.te);
	}
	public void setWidthSegments(int widthsegments){
		this.ws=widthsegments;
		init(this.radius,this.ws,this.hs,this.ps,this.pe,this.ts,this.te);
	}
	public void setHeightSegments(int heightsegments){
		this.hs=heightsegments;
		init(this.radius,this.ws,this.hs,this.ps,this.pe,this.ts,this.te);
	}
	public void setPhiStart(float pstart){
		this.ps=pstart;
		init(this.radius,this.ws,this.hs,this.ps,this.pe,this.ts,this.te);
	}
	public void setPhiEnd(float psend){
		this.pe=psend;
		init(this.radius,this.ws,this.hs,this.ps,this.pe,this.ts,this.te);
	}
	public void setThetaStart(float thetastart){
		this.ts=thetastart;
		init(this.radius,this.ws,this.hs,this.ps,this.pe,this.ts,this.te);
	}
	public void setThetaEnd(float thetaend){
		this.te=thetaend;
		init(this.radius,this.ws,this.hs,this.ps,this.pe,this.ts,this.te);
	}
	public float getRadius(){
		return this.radius;
	}
	public int getWidthSegments(){
		return this.ws;
	}
	public int getHeightSegments(){
		return this.hs;
	}
	public float getPhiStart(){
		return this.ps;
	}
	public float getPhiEnd(){
		return this.pe;
	}
	public float getThetaStart(){
		return this.ts;
	}
	public float getThetaEnd(){
		return this.te;
	}
}
