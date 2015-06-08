package workshop.Assignment2;

import jv.object.PsUpdateIf;
import jvx.project.PjWorkshop_IP;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Immortaly007 on 16-5-2015.
 */
public class Task2_IP extends PjWorkshop_IP implements ActionListener {
    protected Button m_bCalculate;
    protected JTable m_tAnalysis;
    Task2 m_ws;

    public Task2_IP() {
        super();
        if(getClass() == Task2_IP.class)
            init();
    }

    public void init() {
        super.init();
        setTitle("Task 1 (Gradients)");
    }

    public String getNotice() {
        return "A tool that can calculate the gradients of a linear function on some mesh";
    }

    public void setParent(PsUpdateIf parent) {
        super.setParent(parent);
        m_ws = (Task2)parent;

        addSubTitle("Mesh Analysis");

        String[] columns = { "c1", "c2", "c3" };
        Object[][] data = {
                { 0, 0, 0 },
                { 0, 0, 0 },
                { 0, 0, 0 }
        };
        m_tAnalysis = new JTable(data, columns);
        //m_tAnalysis.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(m_tAnalysis);
        add(scrollPane);

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
            m_ws.calculate(m_tAnalysis);
            return;
        }
    }
}
