package workshop;

import jv.object.PsUpdateIf;
import jvx.project.PjWorkshop_IP;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Immortaly007 on 20-5-2015.
 */
public class Task3IterativeAveraging_IP extends PjWorkshop_IP implements ActionListener {
    protected Button m_apply;

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
