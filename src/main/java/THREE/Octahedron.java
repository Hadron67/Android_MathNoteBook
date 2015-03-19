package THREE;

public class Octahedron extends Mesh{
	private short[] indices={0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23};
	private float p=(float) (1/Math.sqrt(3));
	public Octahedron(float a){
		a/=2;
		float[] vertices={
				a,0,0,
				0,a,0,
				0,0,a,
				
				a,0,0,
				0,-a,0,
				0,0,a,
				
				a,0,0,
				0,-a,0,
				0,0,-a,
				
				a,0,0,
				0,a,0,
				0,0,-a,
				
				-a,0,0,
				0,a,0,
				0,0,a,
				
				-a,0,0,
				0,a,0,
				0,0,-a,
				
				-a,0,0,
				0,-a,0,
				0,0,-a,
				
				-a,0,0,
				0,-a,0,
				0,0,a
		};
		float[] normals={
				p,p,p,
				p,p,p,
				p,p,p,
				
				p,-p,p,
				p,-p,p,
				p,-p,p,
				
				p,-p,-p,
				p,-p,-p,
				p,-p,-p,
				
				p,p,-p,
				p,p,-p,
				p,p,-p,
				
				-p,p,p,
				-p,p,p,
				-p,p,p,
				
				-p,p,-p,
				-p,p,-p,
				-p,p,-p,
				
				-p,-p,-p,
				-p,-p,-p,
				-p,-p,-p,
				
				-p,-p,p,
				-p,-p,p,
				-p,-p,p,
		};
		setVertices(vertices);
		setIndices(indices);
		setNormals(normals);
	}
}
