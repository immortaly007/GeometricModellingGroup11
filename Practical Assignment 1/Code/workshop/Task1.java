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
    PgElementSet m_geomSave;
    PgElementSet m_geomTrian;

    public Task1() {
        super("Task 1");
        init();
    }

    @Override
    public void setGeometry(PgGeometry geom) {
        super.setGeometry(geom);
        m_geom 		= (PgElementSet)super.m_geom;
        m_geomSave 	= (PgElementSet)super.m_geomSave;
        m_geomTrian = (PgElementSet)m_geom.clone();
        PgElementSet.triangulate(m_geomTrian);
    }

    public void init() {
        super.init();
    }

    public void calculate(JTable table) {
        // Calculate all shape regularities
        Collection<Double> shapeRegularities = calculateShapeRegularities(m_geomTrian);
        Collection<Integer> valences = calculateValences(m_geomTrian);
		Collection<Double> angles = calculateTriangleAngles(m_geomTrian);
		Collection<Double> edgeLengths = calculateEdgeLengths(m_geomTrian);

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

    private static Collection<Double> calculateShapeRegularities(PgElementSet set)
    {
        ArrayList<Double> shapeRegularities = new ArrayList<Double>();

        int n = set.getNumElements();
        for (int i = 0; i < n; i++) {
            PiVector e = set.getElement(i);

            PdVector v1 = set.getVertex(e.getEntry(0));
            PdVector v2 = set.getVertex(e.getEntry(1));
            PdVector v3 = set.getVertex(e.getEntry(2));
            double sr = calculateShapeRegularity(v1, v2, v3);
            shapeRegularities.add(sr);
        }
        EverythingHelper.filterNaN(shapeRegularities);
        return shapeRegularities;
    }

    private static double calculateShapeRegularity(PdVector v1, PdVector v2, PdVector v3) {
		PdVector cache = new PdVector();
		cache.sub(v2, v1); double a = cache.length();
		cache.sub(v3, v2); double b = cache.length();
		cache.sub(v1, v3); double c = cache.length();
		double s = (a + b + c)/2.0;
		return 4.0*(s - a)*(s - b)*(s - c)/(a*b*c);
    }

    private static Collection<Integer> calculateValences(PgElementSet set)
    {
        ArrayList<Integer> valences = new ArrayList<Integer>();
        PgVertexStar[] edgeStars = Util.getVertexStars(set);
        for (PgVertexStar star : edgeStars)
        {
            valences.add(star.getSize() - 1);
        }
        return valences;
    }

    private static Collection<Double> calculateTriangleAngles(PgElementSet set)
    {
        ArrayList<Double> angles = new ArrayList<Double>();

        int n = set.getNumElements();
        double[] res = new double[3];
        for (int i = 0; i < n; i++) {
            PiVector e = set.getElement(i);

            PdVector v1 = set.getVertex(e.getEntry(0));
            PdVector v2 = set.getVertex(e.getEntry(1));
            PdVector v3 = set.getVertex(e.getEntry(2));
            PdVector.angle(res, v1, v2, v3);
            for (double r : res)
                angles.add(r);
        }
        EverythingHelper.filterNaN(angles);
        return angles;
    }

    private static Collection<Double> calculateEdgeLengths(PgElementSet set)
    {
        ArrayList<Double> edgeLengths = new ArrayList<Double>();

        int n = set.getNumElements();
        double[] res = new double[3];
        for (int i = 0; i < n; i++) {
            PiVector e = set.getElement(i);
            PdVector v1 = set.getVertex(e.getEntry(0));
            PdVector v2 = set.getVertex(e.getEntry(1));
            PdVector v3 = set.getVertex(e.getEntry(2));
            edgeLengths.add(PdVector.subNew(v2, v1).length());
            edgeLengths.add(PdVector.subNew(v3, v2).length());
            edgeLengths.add(PdVector.subNew(v1, v3).length());
        }
        EverythingHelper.filterNaN(edgeLengths);
        return edgeLengths;
    }
}
