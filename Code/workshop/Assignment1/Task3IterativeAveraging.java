package workshop.Assignment1;

import jv.geom.PgElementSet;
import jv.project.PgGeometry;
import jv.vecmath.PdVector;
import jvx.project.PjWorkshop;
import workshop.EverythingHelper;
import workshop.SimpleVertexStar;

import java.util.ArrayList;

/**
 * Created by Immortaly007 on 20-5-2015.
 */
public class Task3IterativeAveraging extends PjWorkshop {
    PgElementSet m_geom;
    PgElementSet m_geomSave;

    public Task3IterativeAveraging() {
        super("Task 3 (Iterative Averaging)");
        init();
    }

    @Override
    public void setGeometry(PgGeometry geom) {
        super.setGeometry(geom);
        m_geom 		= (PgElementSet)super.m_geom;
        m_geomSave 	= (PgElementSet)super.m_geomSave;
    }

    public void init() {
        super.init();
    }

    public void apply(double stepwidth)
    {
        // For each vertex, calculate the average position of it's neighbors
        // Then subtract this average from the current vertex position
        // This gives a vector that moves the current vertex towards the average of it's neighbors.
        // This vector is then multiplied by the stepwidth and added to the current position

        // Calculate the vertex stars (for easy access to neighbors)
        SimpleVertexStar[] stars =  EverythingHelper.makeVertexStars(m_geom);
        PdVector[] vertexOffsets = new PdVector[m_geom.getVertices().length];
        for (SimpleVertexStar star : stars)
        {
            PdVector center = star.getCenter();
            // Calculate the average position of all neighbors
            ArrayList<Double> xCoords = new ArrayList<Double>();
            ArrayList<Double> yCoords = new ArrayList<Double>();
            ArrayList<Double> zCoords = new ArrayList<Double>();
            for (PdVector vertex : star.getNeighborVertices()) {
                xCoords.add(vertex.getEntry(0));
                yCoords.add(vertex.getEntry(1));
                zCoords.add(vertex.getEntry(2));
            }
            PdVector mean = new PdVector(
                    EverythingHelper.mean(xCoords).doubleValue(),
                    EverythingHelper.mean(yCoords).doubleValue(),
                    EverythingHelper.mean(zCoords).doubleValue()
            );
            // Subtract the current vertex position from this mean.
            vertexOffsets[star.getCenterVertex()] = PdVector.subNew(mean, center);
        }

        // Move all vertices towards the average position
        for (int i = 0; i < m_geom.getVertices().length; i++) {
            PdVector scaledOffset = vertexOffsets[i];
            if (scaledOffset == null) continue;
            scaledOffset.multScalar(stepwidth);
            m_geom.setVertex(i, PdVector.addNew(m_geom.getVertex(i), scaledOffset));
        }

    }
}
