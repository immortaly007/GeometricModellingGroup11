package workshop.Assignment2;

import jv.geom.PgElementSet;
import jv.geom.PgVectorField;
import jv.object.PsDebug;
import jv.project.PgGeometry;
import jv.vecmath.PdMatrix;
import jv.vecmath.PdVector;
import jv.vecmath.PiVector;
import jvx.geom.PgVertexStar;
import jvx.numeric.PnSparseMatrix;
import jvx.project.PjWorkshop;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import util.SimpleTriangle;
import util.Util;
import util.EverythingHelper;

import javax.management.openmbean.InvalidOpenTypeException;

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

    public void calculate(int vectorFieldId) {
        // If it doesn't yet exist, build the gradient matrices
        if (m_gradientMatrix == null) {
            m_gradientMatrix = new PnSparseMatrix(m_geom.getNumElements() * 3, m_geom.getNumVertices(), 3);
            for (int i = 0; i < m_geom.getNumElements(); i++)
            {
                // Get the triangle and calculate it's 3x3 gradient matrix
                SimpleTriangle triangle = EverythingHelper.getTriangle(m_geom, i);
                PdMatrix mat = triangle.gradientMatrix();
                // Put the values into the sparse matrix:
                for (int v = 0; v < 3; v ++)
                {
                    int col = m_geom.getElement(i).getEntry(v); // Get the vertex index
                    for (int row = 0; row < 3; row++)
                        m_gradientMatrix.setEntry(i * 3 + row, col, mat.getEntry(row, v));
                }
            }
        }

        PgVectorField functionValuesVectorField = m_geom.getVectorField(vectorFieldId);
//        if (functionValuesVectorField.getDimOfVectors() != 1) throw new Exception("The function values should be 1-dimensional");
        PdVector values = new PdVector(m_geom.getNumVertices());
        for (int i = 0; i < values.getSize(); i++)
            values.setEntry(i, functionValuesVectorField.getVector(i).getEntry(0));

        PdVector grad = PnSparseMatrix.rightMultVector(m_gradientMatrix, values, null);

        // Hack to check the solution
        PgVectorField answers = m_geom.getVectorField(0);
        for (int i = 0; i < grad.getSize(); i++)
        {
            PsDebug.message("Our answer: " + grad.getEntry(i) + ", expected: " + answers.getVector(i / 3).getEntry(i % 3));
        }



        throw new NotImplementedException();
        // TODO: write this
    }

    public PdVector getEdge(PiVector elem, int a, int b) {
        return PdVector.subNew(m_geom.getVertex(elem.getEntry(b)), m_geom.getVertex(elem.getEntry(a)));
    }


}
