package util;

import jv.geom.PgElementSet;
import jv.vecmath.PiVector;
import jvx.geom.PgVertexStar;

/**
* Class containing static methods that can be helpful for the assignments. 
*/
public class Util {
	/**
	* Computes the vertex star (one-ring neighborhood information) for every vertex.
	*/
	public static PgVertexStar[] getVertexStars(PgElementSet geom){
		int nov = geom.getNumVertices();
		PgVertexStar[] vs = new PgVertexStar[nov];
		//generating the vertex star of a vertex is faster when one triangle adjacent 
		//to the vertex is known. Therefore, we first generate such a list.
		PiVector elemPerVert = PgVertexStar.getElementPerVertex(geom);
		
		for(int i=0; i<nov; i++){
			vs[i] = new PgVertexStar();
			vs[i].makeVertexStar(geom, i, elemPerVert.m_data[i]);
		}
		
		return vs;
	}
	
}