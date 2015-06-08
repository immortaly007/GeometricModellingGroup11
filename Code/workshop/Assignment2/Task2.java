package workshop.Assignment2;

import dev6.numeric.PnMumpsSolver;
import jv.geom.PgElementSet;
import jv.object.PsDebug;
import jv.object.PsObject;
import jv.project.PgGeometry;
import jv.vecmath.PdMatrix;
import jv.vecmath.PdVector;
import jv.vecmath.PiVector;
import jvx.geom.PgVertexStar;
import jvx.numeric.PnBiconjugateGradient;
import jvx.numeric.PnMassMatrix;
import jvx.numeric.PnSparseMatrix;
import jvx.project.PjWorkshop;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import util.Util;
import util.EverythingHelper;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Immortaly007 on 16-5-2015.
 */
public class Task2 extends PjWorkshop{
    PgElementSet m_geom;
    PgElementSet m_geom_original;

    public Task2() {
        super("Task 2");
        init();
    }

    @Override
    public void setGeometry(PgGeometry geom) {
        super.setGeometry(geom);
        m_geom = (PgElementSet) super.getGeometry();
        PgElementSet.triangulate(m_geom);
        m_geom_original = (PgElementSet) m_geom.clone();
    }

    public void reset() {
        m_geom.copy(m_geom_original);
    }

    public void init() {
        super.init();
    }

    public void calculate(JTable data) {
        PnSparseMatrix gradientMatrix = EverythingHelper.GetGradientMatrix(m_geom);

        // get x y z coords
        PdVector xcoords = new PdVector(m_geom.getNumVertices());
        PdVector ycoords = new PdVector(m_geom.getNumVertices());
        PdVector zcoords = new PdVector(m_geom.getNumVertices());
        for (int i = 0; i < xcoords.getSize(); i++) {
            xcoords.setEntry(i, m_geom.getVertex(i).getEntry(0));
            ycoords.setEntry(i, m_geom.getVertex(i).getEntry(1));
            zcoords.setEntry(i, m_geom.getVertex(i).getEntry(2));
        }

        // get gradient vectors
        PdVector gradx = PnSparseMatrix.rightMultVector(gradientMatrix, xcoords, null);
        PdVector grady = PnSparseMatrix.rightMultVector(gradientMatrix, ycoords, null);
        PdVector gradz = PnSparseMatrix.rightMultVector(gradientMatrix, zcoords, null);

        // get user input
        PdMatrix input = new PdMatrix();
        for (int c = 0; c < 3; c++){
            for (int r = 0; r < 3; r++){
                input.setEntry(r, c, Double.parseDouble(data.getValueAt(r, c).toString()));
            }
        }

        // apply input to selected elements
        for (int e = 0; e < m_geom.getNumElements(); e++){
            PiVector element = m_geom.getElement(e);
            if (element.hasTag(PsObject.IS_SELECTED)) {
                PdVector gradx_element = new PdVector(gradx.getEntry(e * 3), gradx.getEntry(e * 3 + 1), gradx.getEntry(e * 3 + 2));
                PdVector grady_element = new PdVector(grady.getEntry(e * 3), grady.getEntry(e * 3 + 1), grady.getEntry(e * 3 + 2));
                PdVector gradz_element = new PdVector(gradz.getEntry(e * 3), gradz.getEntry(e * 3 + 1), gradz.getEntry(e * 3 + 2));

                gradx_element.rightMultMatrix(input);
                grady_element.rightMultMatrix(input);
                gradz_element.rightMultMatrix(input);

                for (int i = 0; i < 3; i++) gradx.setEntry(e * 3 + i, gradx_element.getEntry(i));
                for (int i = 0; i < 3; i++) grady.setEntry(e * 3 + i, grady_element.getEntry(i));
                for (int i = 0; i < 3; i++) gradz.setEntry(e * 3 + i, gradz_element.getEntry(i));
            }
        }

        // get the prerequisites
        // get the diagonal mass matrix
        PnSparseMatrix mass = new PnSparseMatrix(m_geom.getNumElements() * 3, m_geom.getNumElements() * 3);
        for (int i = 0; i < m_geom.getNumElements(); i++)
        {
            double area = EverythingHelper.getTriangle(m_geom, i).area();
            mass.setEntry(i * 3, i * 3, area);
            mass.setEntry(i * 3 + 1, i * 3 + 1, area);
            mass.setEntry(i * 3 + 2, i * 3 + 2, area);
        }

        // get the transposed gradient matrix
        PnSparseMatrix gradientMatrixTransposed = gradientMatrix.transposeNew();

        // get A, from Ax = b
        PnSparseMatrix GtMv = PnSparseMatrix.multMatrices(gradientMatrixTransposed, mass, new PnSparseMatrix());
        PnSparseMatrix GtMvG = PnSparseMatrix.multMatrices(GtMv, gradientMatrix, new PnSparseMatrix());

        // get x, from Ax = b
        PdVector newx = new PdVector(m_geom.getNumVertices());
        PdVector newy = new PdVector(m_geom.getNumVertices());
        PdVector newz = new PdVector(m_geom.getNumVertices());

        // get b, from Ax = b
        PdVector GtMvgx = PnSparseMatrix.rightMultVector(GtMv, gradx, null);
        PdVector GtMvgy = PnSparseMatrix.rightMultVector(GtMv, grady, null);
        PdVector GtMvgz = PnSparseMatrix.rightMultVector(GtMv, gradz, null);

        try {
            jvx.numeric.PnBiconjugateGradient hmm = new PnBiconjugateGradient();
            hmm.solve(GtMvG, newx, GtMvgx);
            hmm.solve(GtMvG, newy, GtMvgy);
            hmm.solve(GtMvG, newz, GtMvgz);
        } catch (Exception e) {
            PsDebug.message(e.toString());
        }

        for (int i = 0; i < newx.getSize(); i++) {
            m_geom.setVertex(i, newx.getEntry(i), newy.getEntry(i), newz.getEntry(i));
        }

    }
}
