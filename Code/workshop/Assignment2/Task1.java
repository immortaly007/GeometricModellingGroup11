package workshop.Assignment2;

import jv.geom.PgElementSet;
import jv.geom.PgVectorField;
import jv.object.PsDebug;
import jv.project.PgGeometry;
import jv.vecmath.PdMatrix;
import jv.vecmath.PdVector;
import jv.vecmath.PiVector;
import jvx.geom.PgVertexStar;
import jvx.numeric.PnMatrix;
import jvx.numeric.PnSparseMatrix;
import jvx.project.PjWorkshop;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import util.SimpleTriangle;
import util.Util;
import util.EverythingHelper;

import java.util.ArrayList;

/**
 * Created by Immortaly007 on 16-5-2015.
 */
public class Task1 extends PjWorkshop{
    PgElementSet m_geom;
    PnSparseMatrix m_gradientMatrix;
    public Task1() {
        super("Task 1");
        init();
    }

    @Override
    public void setGeometry(PgGeometry geom) {
        super.setGeometry(geom);
        m_geom = (PgElementSet) super.getGeometry();
        // Make sure we're working with triangles
        PgElementSet.triangulate(m_geom);
        m_gradientMatrix = null;
    }

    public void init() {
        super.init();
    }

    public void reset()
    {
        m_geom.copy(m_geomSave);
        m_geom.update(m_geom);
        // Make sure we're working with triangles
        PgElementSet.triangulate(m_geom);
        m_gradientMatrix = null;
    }

    public String[] getValidFunctionVectorFieldNames() {
        ArrayList<String> vectorFieldNames = new ArrayList<>();
        for (int i = 0; i < m_geom.getNumVectorFields(); i++)
            if (m_geom.getVectorField(i).getDimOfVectors() == 1 && m_geom.getVectorField(i).getBasedOn() == PgVectorField.VERTEX_BASED)
                vectorFieldNames.add(m_geom.getVectorField(i).getName());
        String[] res = new String[vectorFieldNames.size()];
        vectorFieldNames.toArray(res);
        return res;
    }

    public String[] getValidGradientVectorFieldNames() {
        ArrayList<String> vectorFieldNames = new ArrayList<>();
        for (int i = 0; i < m_geom.getNumVectorFields(); i++)
            if (m_geom.getVectorField(i).getDimOfVectors() == 3 && m_geom.getVectorField(i).getBasedOn() == PgVectorField.ELEMENT_BASED)
                vectorFieldNames.add(m_geom.getVectorField(i).getName());
        String[] res = new String[vectorFieldNames.size()];
        vectorFieldNames.toArray(res);
        return res;
    }

    public void calculate(String valuesName, String checkName, String resultName) {
        // If it doesn't yet exist, build the gradient matrices
        if (m_gradientMatrix == null) {
            m_gradientMatrix = EverythingHelper.GetGradientMatrix(m_geom);
        }

        PgVectorField functionValuesVectorField = m_geom.getVectorField(valuesName);
        PdVector values = new PdVector(m_geom.getNumVertices());
        for (int i = 0; i < values.getSize(); i++)
            values.setEntry(i, functionValuesVectorField.getVector(i).getEntry(0));

        PdVector grads = PnSparseMatrix.rightMultVector(m_gradientMatrix, values, null);

        // If a check vector is given, use it to check our solution:
        if (checkName != "") {
            PgVectorField answers = m_geom.getVectorField(checkName);
            for (int i = 0; i < grads.getSize(); i++) {
                PsDebug.message("Our answer: " + grads.getEntry(i) + ", expected: " + answers.getVector(i / 3).getEntry(i % 3));
            }
        }

        if (resultName != "") {
            PgVectorField result = new PgVectorField(3, PgVectorField.ELEMENT_BASED);
            result.setNumVectors(m_geom.getNumElements());
            result.setName(resultName);
            result.showVectorArrows(true);
            for (int i = 0; i < m_geom.getNumElements(); i++)
                result.setVector(i, grads.getEntry(i * 3), grads.getEntry(i * 3 + 1), grads.getEntry(i * 3 + 2));
            m_geom.addVectorField(result);
        }
        m_geom.update(m_geom);
    }

    void showVectorField(String name) {
        m_geom.selectVectorField(m_geom.getVectorField(name));
    }


}
