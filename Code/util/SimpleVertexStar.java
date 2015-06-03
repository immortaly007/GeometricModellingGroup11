package workshop;

import jv.geom.PgElementSet;
import jv.vecmath.PdVector;

import java.util.ArrayList;

/**
 * Created by Immortaly007 on 21-5-2015.
 */
public class SimpleVertexStar {
    int centerVertex;
    ArrayList<Integer> neighborVertexIds;
    PgElementSet geom;

    public  SimpleVertexStar(int center, PgElementSet geom)
    {
        this.centerVertex = center;
        this.geom = geom;
        this.neighborVertexIds = new ArrayList<Integer>();
    }

    public int getCenterVertex() {
        return centerVertex;
    }
    public PdVector getCenter() { return geom.getVertex(centerVertex); }

    public void setCenterVertex(int centerVertex) {
        this.centerVertex = centerVertex;
    }

    public Integer[] getNeighborVertexIds() {
        Integer[] res = new Integer[neighborVertexIds.size()];
        neighborVertexIds.toArray(res);
        return res;
    }

    public int getNeighborCount() {
        return neighborVertexIds.size();
    }

    public PgElementSet getGeom() {
        return geom;
    }

    public void setGeom(PgElementSet base) {
        this.geom = base;
    }

    public boolean containsNeighbor(int neighbor)
    {
        for (int cur : neighborVertexIds)
            if (cur == neighbor) return true;
        return false;
    }

    public void addNeighbor(int neighbor) {
        if (!this.containsNeighbor(neighbor)) neighborVertexIds.add(neighbor);
    }

    public PdVector[] getNeighborVertices()
    {
        PdVector[] res = new PdVector[neighborVertexIds.size()];
        int i = 0;
        for (int cur : neighborVertexIds) {
            res[i++] = geom.getVertex(cur);
        }
        return res;
    }


}
