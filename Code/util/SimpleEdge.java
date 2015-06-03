package util;

import jv.vecmath.PdVector;

/**
 * Created by immortaly007 on 3-6-15.
 */
public class SimpleEdge {
    PdVector p1;
    PdVector p2;

    public SimpleEdge(PdVector p1, PdVector p2)
    {
        this.p1 = p1;
        this.p2 = p2;
    }

    public PdVector get(int i)
    {
        switch(i)
        {
            case 0: return p1;
            case 1: return p2;
            default: throw new IndexOutOfBoundsException("Edges indices should be either 0 or 1");
        }
    }

    public void set(int i, PdVector value)
    {
        switch(i)
        {
            case 0: p1 = value; break;
            case 1: p2 = value; break;
            default: throw new IndexOutOfBoundsException("Edges indices should be either 0 or 1");
        }
    }

    /**
     * returns a vector pointing from p1 to p2. The length and direction are equal to that of the edge.
     * @return A vector pointing from p1 to p2 (e.g. length * direction)
     */
    public PdVector descriptor()
    {
        return PdVector.subNew(p2, p1);
    }

    public double length()
    {
        return descriptor().length();
    }

    public PdVector direction()
    {
        PdVector res = descriptor();
        res.normalize();
        return res;
    }
}
