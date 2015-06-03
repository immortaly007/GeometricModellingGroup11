package workshop.Assignment1;

import jv.geom.PgElementSet;
import jv.project.PgGeometry;
import jv.vecmath.PdVector;
import jvx.numeric.PnMassMatrix;
import jvx.numeric.PnSparseMatrix;
import jvx.numeric.PnStiffDiriConforming;
import jvx.project.PjWorkshop;

/**
 * Created by Immortaly007 on 20-5-2015.
 */
public class Task3MeanCurvatureFlow extends PjWorkshop {
    PgElementSet m_geom;
    PgElementSet m_geomSave;

    public Task3MeanCurvatureFlow() {
        super("Task 3");
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

    public void apply(double tau)
    {
        // Triangulate the geometry
        PgElementSet.triangulate(m_geom);

        // Calculate the inverse lumped mass matrix (M^{-1}):
        PdVector v_MInv =  PnMassMatrix.getInvLumpedMassMatrix(m_geom, null);
        PnSparseMatrix MInv = new PnSparseMatrix(m_geom.getNumVertices(), m_geom.getNumVertices());
        MInv.addDiagonal(v_MInv);

        // Calculate the stiffness matrix (S)
        PnStiffDiriConforming S = new PnStiffDiriConforming(m_geom);

        // Calculate the laplace matrix: L = M^{-1} S
        PnSparseMatrix L =  PnSparseMatrix.multMatrices(MInv, S, null);

        // Multiply the laplace matrix by the stepwidth (tau)
        L.multScalar(tau);

        // Calculate the movement for each direction
        PdVector[] res = new PdVector[m_geom.getNumVertices()];
        for (int i = 0; i < m_geom.getNumVertices(); i++) res[i] = new PdVector(3);

        for (int axis = 0; axis < 3; axis++) {
            // Create vector x (containing the current axis coordinate for each vertex)
            PdVector x = new PdVector(m_geom.getNumVertices());
            for (int i = 0; i < m_geom.getNumVertices(); i++) {
                x.setEntry(i, m_geom.getVertex(i).getEntry(axis));
            }
            // Set x = x - \tau L x
            x.sub(L.leftMultMatrix(null, x));

            // Update res to contain these entries
            for (int i = 0; i < m_geom.getNumVertices(); i++)
                res[i].setEntry(axis, x.getEntry(i));
        }

        m_geom.setVertices(res);


    }
}
