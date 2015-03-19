package THREE;

/**
 * Created by cfy on 14-11-29.
 */
public class EditableSurface extends Mesh{
    private float h;
    private float w;
    private int ws;
    private int hs;
    float[] vertices=null;
    float[] normals=null;
    short[] indices=null;
    public EditableSurface(){
        this(1,1,1,1);
    }
    public EditableSurface(float width,float height){
        this(width,height,1,1);
    }
    public EditableSurface(float width,float height,int widthSegments,int heightSegments){
        this.h=height;
        this.w=width;
        this.ws=widthSegments;
        this.hs=heightSegments;
        init(this.w,this.h,this.ws,this.hs);
    }
    private void init(float width,float height,int widthSegments,int heightSegments){
        vertices=new float[(widthSegments+1)*(heightSegments+1)*3];
        normals=new float[(widthSegments+1)*(heightSegments+1)*3];
        indices=new short[(widthSegments+1)*(heightSegments+1)*6];
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
    public void setCoordinate(int x,int y,float z) throws ArrayIndexOutOfBoundsException{
        if(x>=0&&y>=0&&x<=this.ws&&y<=this.hs){
            int index=(y*(this.ws+1)+x)*3+2;
            this.vertices[index]=z;
            setVertices(this.vertices);
        }
        else{
            throw new ArrayIndexOutOfBoundsException("height or width index is out of the plane.");
        }
    }
    public void setVertex(float[] v) throws VerticesLengthNotMatchException{
        if(this.vertices.length==v.length) {
            this.vertices=v;
            setVertices(this.vertices);
            this.calculateNormals();
        }
        else throw new VerticesLengthNotMatchException("The two vertices arrays have different lengths.");
    }
    public void calculateNormals(){
        float dx=this.w/this.ws;
        float dy=this.h/this.hs;
        for(int i=1;i<=this.hs-1;i++){
            for(int j=1;j<=this.ws-1;j++){
                int index=(i*(this.ws+1)+j)*3;
                float l1x=(this.vertices[index+3+0]-this.vertices[index-3+0])/2;
                float l1y=(this.vertices[index+3+1]-this.vertices[index-3+1])/2;
                float l1z=(this.vertices[index+3+2]-this.vertices[index-3+2])/2;
                float l2x=(this.vertices[index+(this.ws+1)*3+0]-this.vertices[index-(this.ws+1)*3+0])/2;
                float l2y=(this.vertices[index+(this.ws+1)*3+1]-this.vertices[index-(this.ws+1)*3+1])/2;
                float l2z=(this.vertices[index+(this.ws+1)*3+2]-this.vertices[index-(this.ws+1)*3+2])/2;

//                float nx=-(this.vertices[index+5]-this.vertices[index-1])/2;
//                float ny=-(this.vertices[index+(this.ws+1)*3+2]-this.vertices[index-(this.ws+1)*3+2])/2;
//                float nz=1;

                float nx=l1y*l2z-l1z*l2y;
                float ny=l1z*l2x-l1x*l2z;
                float nz=l1x*l2y-l1y*l2x;
                float a=(float)Math.sqrt(nx*nx+ny*ny+nz*nz);
                this.normals[index]=nx/a;
                this.normals[index+1]=ny/a;
                this.normals[index+2]=nz/a;
            }
        }
        for(int j=1;j<=this.ws-1;j++){
            int i=0;
            int index=(i*(this.ws+1)+j)*3;
            this.normals[index]=this.normals[index+(this.ws+1)*3];
            this.normals[index+1]=this.normals[index+(this.ws+1)*3+1];
            this.normals[index+2]=this.normals[index+(this.ws+1)*3+2];
            i=this.hs;
            index=(i*(this.ws+1)+j)*3;
            this.normals[index]=this.normals[index-(this.ws+1)*3];
            this.normals[index+1]=this.normals[index-(this.ws+1)*3+1];
            this.normals[index+2]=this.normals[index-(this.ws+1)*3+2];
        }
        for(int i=0;i<=this.hs;i++){
            int j=0;
            int index=(i*(this.ws+1)+j)*3;
            this.normals[index]=this.normals[index+3];
            this.normals[index+1]=this.normals[index+3+1];
            this.normals[index+2]=this.normals[index+3+2];

            j=this.ws;
            index=(i*(this.ws+1)+j)*3;
            this.normals[index]=this.normals[index-3];
            this.normals[index+1]=this.normals[index-3+1];
            this.normals[index+2]=this.normals[index-3+2];
        }
        setNormals(this.normals);
    }
}
