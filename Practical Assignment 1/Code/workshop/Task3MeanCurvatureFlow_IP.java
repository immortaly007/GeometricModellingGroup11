package workshop;

import jv.number.PuDouble;
import jv.object.PsUpdateIf;
import jvx.project.PjWorkshop_IP;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Immortaly007 on 20-5-2015.
 */
public class Task3MeanCurvatureFlow_IP extends PjWorkshop_IP implements ActionListener {
    protected Button m_bApply;
    protected PuDouble m_xStepwidth;

    Task3MeanCurvatureFlow m_ws;

    public Task3MeanCurvatureFlow_IP() {
        super();
        if(getClass() == Task3MeanCurvatureFlow_IP.class)
            init();
    }

    public void init() {
        super.init();
        setTitle("Task 3 (Mean Curvature Flow)");
    }

    public String getNotice() {
        return "A tool for mesh smoothing using mean curvature flow.";
    }

    public void setParent(PsUpdateIf parent) {
        super.setParent(parent);
        m_ws = (Task3MeanCurvatureFlow)parent;

        addSubTitle("Mean Curvature Flow");

        m_xStepwidth = new PuDouble("Stepwidth (tau)");
        m_xStepwidth.setDefBounds(0, 1.0, 0.01, 0.1);
        m_xStepwidth.addUpdateListener(this);
        m_xStepwidth.init();
        add(m_xStepwidth.getInfoPanel());

        m_bApply = new Button("Apply");
        m_bApply.addActionListener(this);
        add(m_bApply);

        validate();
    }

    @Override
    public boolean update(Object event) {
        return super.update(event);
    }

    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if (source == m_bApply) {
            m_ws.apply(m_xStepwidth.getValue());
            m_ws.m_geom.update(m_ws.m_geom);
            return;
        }
    }
}
