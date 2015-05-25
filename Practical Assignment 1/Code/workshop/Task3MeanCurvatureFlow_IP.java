package workshop;

import jv.object.PsUpdateIf;
import jvx.project.PjWorkshop_IP;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Immortaly007 on 20-5-2015.
 */
public class Task3MeanCurvatureFlow_IP extends PjWorkshop_IP implements ActionListener {
    protected Button m_apply;

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

        m_apply = new Button("Apply");
        m_apply.addActionListener(this);
        add(m_apply);

        validate();
    }

    @Override
    public boolean update(Object event) {
        return super.update(event);
    }

    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if (source == m_apply) {
            return;
        }
    }
}
