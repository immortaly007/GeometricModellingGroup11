package workshop.Assignment2;

import jv.geom.PgElementSet;
import jv.project.PgGeometry;
import jv.vecmath.PdVector;
import jv.vecmath.PiVector;
import jvx.geom.PgVertexStar;
import jvx.project.PjWorkshop;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import util.Util;
import util.EverythingHelper;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Immortaly007 on 16-5-2015.
 */
public class Task2 extends PjWorkshop{
    PgElementSet m_geom;

    public Task2() {
        super("Task 2");
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

    public void calculate() {
        throw new NotImplementedException();
        // TODO: write this
    }
}
