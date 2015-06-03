package util;

import jv.vecmath.PdMatrix;
import jv.vecmath.PdVector;

/**
 * Created by immortaly007 on 3-6-15.
 */
public class SimpleTriangle {
    private PdVector p1;
    private PdVector p2;
    private PdVector p3;

    public SimpleTriangle(PdVector p1, PdVector p2, PdVector p3)
    {
        this.p1 = p1; this.p2 = p2; this.p3 = p3;
    }

    /**
     * Returns the value of a vertex in the triangle
     * @param i The index of the vertex in the triangle
     * @return The value/coordinate of vertex i.
     */
    public PdVector get(int i) {
        switch(i)
        {
            case 0: return p1;
            case 1: return p2;
            case 2: return p3;
            default: throw new IndexOutOfBoundsException("Triangle indices should be in range [0, 2].");
        }
    }

    /**
     * Set a vertex with a certain index to the given value.
     * @param i The index of the vertex to update
     * @param value The value to which this vertex should be changed
     */
    public void set(int i, PdVector value)
    {
        switch(i)
        {
            case 0: p1 = value; break;
            case 1: p2 = value; break;
            case 2: p3 = value; break;
            default: throw new IndexOutOfBoundsException("Triangle indices should be in range [0, 2].");
        }
    }

    /**
     * Returns the area of the triangle
     * @return
     */
    public double area()
    {
        return PdVector.area(p1, p2, p3);
    }

    /**
     * Gets the angle at a certain vertex in the triangle
     * @param i
     * @return
     */
    public double angle(int i)
    {
        return PdVector.angle(get(i), get((i + 1) % 3), get((i + 2) % 3)) * (Math.PI / 180);
    }

    /**
     * Returns the angles at all three vertices in the triangle. The order is equal to the vertex order
     * @return
     */
    public double[] angles()
    {
        double[] res = new double[3];
        PdVector.angle(res, p1, p2, p3);
        return res;
    }
     

    /**
     * Returns the three edges in the triangle. The indexes are such that edge i is opposite to vertex i.
     * @param i The index of the edge
     * @return The edge
     */
    public SimpleEdge edge(int i)
    {
        return new SimpleEdge(get(i), get((i + 1) % 3));
    }

    public SimpleEdge[] edges()
    {
        return new SimpleEdge[]
                {
                        edge(0),
                        edge(1),
                        edge(2)
                };
    }

    public PdVector edgeDescriptor(int i)
    {
        return PdVector.subNew(get((i + 1) % 3), get(i));
    }

    public PdVector[] edgeDescriptors()
    {
        return new PdVector[]
                {
                        edgeDescriptor(0),
                        edgeDescriptor(1),
                        edgeDescriptor(2)
                };
    }

    public PdMatrix gradientMatrix()
    {
        // Get the angles at the triangles
        double[] a = angles();

        PdVector[] eCotA = edgeDescriptors();
        for (int i = 0; i < 3; i++) { eCotA[i].multScalar(1.0 / Math.tan(a[i])); }


        PdVector[] R90e = new PdVector[3];
        for (int i =0; i < 3; i++) {
            int v1 = (i + 1) % 3;
            int v2 = (i + 2) % 3;
            R90e[i] = PdVector.subNew(eCotA[v1], eCotA[v2]);
        }
        PdMatrix res = new PdMatrix(3, 3);
        res.setRows(R90e);
        res.multScalar(1.0 / (2.0 * area()));
        return res;
    }

}
