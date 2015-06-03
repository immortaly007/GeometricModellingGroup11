package workshop.Assignment1;

import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import jv.object.PsUpdateIf;
import jvx.project.PjWorkshop_IP;

public class Task2_IP extends PjWorkshop_IP implements ActionListener {

    protected Button m_calc;

    Task2 m_t2;

    public Task2_IP() {
        super();
        if(getClass() == Task2_IP.class)
            init();
    }

    public void init() {
        super.init();
        setTitle("Task 2");
    }

    public String getNotice() {
        return "a tool for surface analysis that computes, \n" +
                "- the genus of the surface,\n" +
                "- the area of the surface, and\n" +
                "- the absolute value of the mean curvature (length of the mean curvature vector) for every vertex.";
    }

    public void setParent(PsUpdateIf parent) {
        super.setParent(parent);
        m_t2 = (Task2)parent;

        addSubTitle("Surface Analysis");

        m_calc = new Button("Calculate");
        m_calc.addActionListener(this);
        add(m_calc);

        validate();
    }

    @Override
    public boolean update(Object event) {
        return super.update(event);
    }

    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if (source == m_calc) {
            m_t2.calculate();
            m_t2.m_geom.update(m_t2.m_geom);
            return;
        }
    }

}
