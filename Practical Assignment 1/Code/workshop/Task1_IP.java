package workshop;

import jv.number.PuDouble;
import jv.object.PsUpdateIf;
import jvx.project.PjWorkshop_IP;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Immortaly007 on 16-5-2015.
 */
public class Task1_IP extends PjWorkshop_IP implements ActionListener {
    protected Button m_bCalculate;
    Task1 m_ws;

    public Task1_IP() {
        super();
        if(getClass() == Task1_IP.class)
            init();
    }

    public void init() {
        super.init();
        setTitle("Task 1");
    }

    public String getNotice() {
        return "This workshop tool calculates some mesh statistics";
    }

    public void setParent(PsUpdateIf parent) {
        super.setParent(parent);
        m_ws = (Task1)parent;

        addSubTitle("Mesh Analysis");

        m_bCalculate = new Button("Calculate...");
        m_bCalculate.addActionListener(this);
        add(m_bCalculate);



        validate();
    }

    @Override
    public boolean update(Object event) {
        return super.update(event);
    }

    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if (source == m_bCalculate) {
            m_ws.calculate();
            return;
        }
    }
}
