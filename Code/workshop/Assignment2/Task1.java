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

    }

    public String[] getVectorFieldNames()
    {
        String[] vectorFieldNames = new String[m_geom.getNumVectorFields()];
        for (int i = 0; i < m_geom.getNumVectorFields(); i++)
            vectorFieldNames[i] = m_geom.getVectorField(i).getName();
        return vectorFieldNames;
    }

    public void calculate(String valuesName, String checkName, String resultName) {
        // If it doesn't yet exist, build the gradient matrices
        if (m_gradientMatrix == null) {
            m_gradientMatrix = EverythingHelper.GetGradientMatrix(m_geom);
        }

        PgVectorField functionValuesVectorField = m_geom.getVectorField(valuesName);
//        if (functionValuesVectorField.getDimOfVectors() != 1) throw new Exception("The function values should be 1-dimensional");
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
