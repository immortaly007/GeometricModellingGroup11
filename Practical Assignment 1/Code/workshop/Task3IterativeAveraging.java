package workshop;

import jv.geom.PgElementSet;
import jv.object.PsDebug;
import jv.project.PgGeometry;
import jv.vecmath.PdVector;
import jv.vecmath.PiVector;
import jvx.geom.PgVertexStar;
import jvx.project.PjWorkshop;
import util.Util;

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
        PgVertexStar[] stars =  Util.getVertexStars(m_geom);
        PdVector[] vertexOffsets = new PdVector[m_geom.getVertices().length];
        for (PgVertexStar star : stars)
        {
            if (star.getSize() == 1) {
                vertexOffsets[star.getFirstElemInd()] = new PdVector(0, 0, 0);
                continue;
            }
            PiVector starElement = star.getElement();
            PdVector center = m_geom.getVertex(star.getFirstElemInd());
            ArrayList<Double> xCoords = new ArrayList<Double>();
            ArrayList<Double> yCoords = new ArrayList<Double>();
            ArrayList<Double> zCoords = new ArrayList<Double>();
            for (int i = 0; i < star.getElement().getSize(); i++) {
                if (i == star.getFirstElemInd()) continue;
                PdVector vertex = m_geom.getVertex(starElement.getEntry(i));
                xCoords.add(vertex.getEntry(0));
                yCoords.add(vertex.getEntry(1));
                zCoords.add(vertex.getEntry(2));
            }
            PdVector mean = new PdVector(
                    EverythingHelper.mean(xCoords).doubleValue(),
                    EverythingHelper.mean(yCoords).doubleValue(),
                    EverythingHelper.mean(zCoords).doubleValue()
            );
            vertexOffsets[star.getElement().getEntry(star.getFirstElemInd())] = PdVector.subNew(mean, center);
        }

        for (int i = 0; i < m_geom.getVertices().length; i++) {
            PdVector scaledOffset = vertexOffsets[i];
            if (scaledOffset == null) continue;
            scaledOffset.multScalar(stepwidth);
            m_geom.setVertex(i, PdVector.addNew(m_geom.getVertex(i), scaledOffset));
        }

    }
}
