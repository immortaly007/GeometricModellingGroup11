package workshop;

import jv.geom.PgElementSet;
import jv.object.PsDebug;
import jv.project.PgGeometry;
import jv.vecmath.PdVector;
import jv.vecmath.PiVector;
import jvx.geom.PgVertexStar;
import jvx.project.PjWorkshop;
import util.Util;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.*;
import javax.swing.table.*;
/**
 * Created by Immortaly007 on 16-5-2015.
 */
public class Task1 extends PjWorkshop{
    PgElementSet m_geom;

    public Task1() {
        super("Task 1");
        init();
    }

    @Override
    public void setGeometry(PgGeometry geom) {
        super.setGeometry(geom);
        m_geom = (PgElementSet) super.getGeometry();
        PgElementSet.triangulate(m_geom);
    }

    public void init() {
        super.init();
    }

    public void calculate(JTable table) {
        // Calculate everything
        Collection<Double> shapeRegularities = calculateShapeRegularities(m_geom);
        Collection<Integer> valences = calculateValences(m_geom);
		Collection<Double> angles = calculateTriangleAngles(m_geom);
		Collection<Double> edgeLengths = calculateEdgeLengths(m_geom);

        // Put the results in the table
        TableModel model = table.getModel();

        model.setValueAt(EverythingHelper.min(shapeRegularities), 0, 1);
        model.setValueAt(EverythingHelper.max(shapeRegularities), 0, 2);
        model.setValueAt(EverythingHelper.mean(shapeRegularities), 0, 3);
        model.setValueAt(EverythingHelper.std(shapeRegularities), 0, 4);

        model.setValueAt(EverythingHelper.min(valences), 1, 1);
        model.setValueAt(EverythingHelper.max(valences), 1, 2);
        model.setValueAt(EverythingHelper.mean(valences), 1, 3);
        model.setValueAt(EverythingHelper.std(valences), 1, 4);

        model.setValueAt(EverythingHelper.min(angles), 2, 1);
        model.setValueAt(EverythingHelper.max(angles), 2, 2);
        model.setValueAt(EverythingHelper.mean(angles), 2, 3);
        model.setValueAt(EverythingHelper.std(angles), 2, 4);

        model.setValueAt(EverythingHelper.min(edgeLengths), 3, 1);
        model.setValueAt(EverythingHelper.max(edgeLengths), 3, 2);
        model.setValueAt(EverythingHelper.mean(edgeLengths), 3, 3);
        model.setValueAt(EverythingHelper.std(edgeLengths), 3, 4);
    }

    public void color() {
    	m_geom.assureElementColors();
    	int n = m_geom.getNumElements();
    	Color[] colors = new Color[n];

        for (int i = 0; i < n; i++) {
            PiVector e = m_geom.getElement(i);
            PdVector v1 = m_geom.getVertex(e.getEntry(0));
            PdVector v2 = m_geom.getVertex(e.getEntry(1));
            PdVector v3 = m_geom.getVertex(e.getEntry(2));
            float sr = (float) calculateShapeRegularity(v1, v2, v3);
            colors[i] = Color.getHSBColor(0, 1 - 2*sr, 1);
        }

        m_geom.setElementColors(colors);
        m_geom.showElementColors(true);
        m_geom.update(m_geom);

    }

    /**
     * Calculates the shape regularities for all triangles in the geometry
     * @param set Geometry (should be triangulated)
     * @return Collection of douvles representing the shape regularities of all triangles
     */
    private static Collection<Double> calculateShapeRegularities(PgElementSet set)
    {
        ArrayList<Double> shapeRegularities = new ArrayList<Double>();

        int n = set.getNumElements();
        for (int i = 0; i < n; i++) {
            // Extract the triangle
            PiVector e = set.getElement(i);

            PdVector v1 = set.getVertex(e.getEntry(0));
            PdVector v2 = set.getVertex(e.getEntry(1));
            PdVector v3 = set.getVertex(e.getEntry(2));
            // Calculate it's shape regularity
            double sr = calculateShapeRegularity(v1, v2, v3);
            shapeRegularities.add(sr);
        }
        EverythingHelper.filterNaN(shapeRegularities);
        return shapeRegularities;
    }

    /**
     * Calculates the shape regularity of a triangle by dividing the diameter of the inscribed circle by the of the
     * circumscribed circle.
     *
     * @param v1 First vertex of the triangle
     * @param v2 Second vertex of the triangle
     * @param v3 Third vertex of the triangle
     * @return Shape regularity
     */
    private static double calculateShapeRegularity(PdVector v1, PdVector v2, PdVector v3) {
		PdVector cache = new PdVector();
		cache.sub(v2, v1); double a = cache.length();
		cache.sub(v3, v2); double b = cache.length();
		cache.sub(v1, v3); double c = cache.length();
		double s = (a + b + c)/2.0;
		return 4.0*(s - a)*(s - b)*(s - c)/(a*b*c);
    }

    /**
     * Calculate the valences
     * @param set The geometry (should be triangulated)
     * @return
     */
    private static Collection<Integer> calculateValences(PgElementSet set)
    {
        ArrayList<Integer> valences = new ArrayList<Integer>();
        PgVertexStar[] edgeStars = Util.getVertexStars(set);
        for (PgVertexStar star : edgeStars)
        {
            // The valence is equal to the number of edges minus 1 (assuming the geometry is closed)
            valences.add(star.getLink().getSize());
        }
        return valences;
    }

    /**
     * Calculates the angles (in radians) of all edges in the geometry
     * @param set Geometry (should be triangulated)
     * @return A collections of all angles (3 * m)
     */
    private static Collection<Double> calculateTriangleAngles(PgElementSet set)
    {
        ArrayList<Double> angles = new ArrayList<Double>();

        int n = set.getNumElements();
        double[] res = new double[3];
        for (int i = 0; i < n; i++) {
            // Extract the triangle vertices
            PiVector e = set.getElement(i);

            PdVector v1 = set.getVertex(e.getEntry(0));
            PdVector v2 = set.getVertex(e.getEntry(1));
            PdVector v3 = set.getVertex(e.getEntry(2));

            // Calculate the angles and add them to the angle collection
            PdVector.angle(res, v1, v2, v3);
            for (double r : res)
                angles.add(r);
        }
        EverythingHelper.filterNaN(angles);
        return angles;
    }

    /**
     * Calculates the edge length of all edges in the geometry (requires the geometry to be triangulated)
     * @param set The geometry to process (should be traingulated)
     * @return A collection containing the edge length of all edges
     */
    private static Collection<Double> calculateEdgeLengths(PgElementSet set)
    {
        ArrayList<Double> edgeLengths = new ArrayList<Double>();

        int n = set.getNumElements();
        double[] res = new double[3];
        // Go through all elements
        for (int i = 0; i < n; i++) {
            // Extract the vertices of the triangle
            PiVector e = set.getElement(i);
            PdVector v1 = set.getVertex(e.getEntry(0));
            PdVector v2 = set.getVertex(e.getEntry(1));
            PdVector v3 = set.getVertex(e.getEntry(2));
            // Calculate the edge length of all edges in the triangle and add them to the results
            edgeLengths.add(PdVector.subNew(v2, v1).length());
            edgeLengths.add(PdVector.subNew(v3, v2).length());
            edgeLengths.add(PdVector.subNew(v1, v3).length());
        }
        EverythingHelper.filterNaN(edgeLengths);
        return edgeLengths;
    }
}
