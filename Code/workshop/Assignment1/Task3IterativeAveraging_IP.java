package workshop.Assignment1;

import jv.number.PuDouble;
import jv.object.PsUpdateIf;
import jvx.project.PjWorkshop_IP;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Immortaly007 on 20-5-2015.
 */
public class Task3IterativeAveraging_IP extends PjWorkshop_IP implements ActionListener {
    protected Button m_bApply;
    protected PuDouble m_xStepwidth;

    Task3IterativeAveraging m_ws;

    public Task3IterativeAveraging_IP() {
        super();
        if(getClass() == Task3IterativeAveraging_IP.class)
            init();
    }

    public void init() {
        super.init();
        setTitle("Task 3 (Iterative Averaging)");
    }

    public String getNotice() {
        return "Tool for mesh smoothing using iterative averaging.";
    }

    public void setParent(PsUpdateIf parent) {
        super.setParent(parent);
        m_ws = (Task3IterativeAveraging)parent;

        addSubTitle("Iterative Averaging");

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
