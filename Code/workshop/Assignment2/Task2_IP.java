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
        setTitle("Task 2 (Mesh editing)");
    }

    public String getNotice() {
        return "This tool applies the given (3x3) matrix to the gradients of the embeddings of all elements in the set. " +
                "The updated gradients are then used to solve a linear system such that the gradients of the embedding of the " +
                "updated mesh are as close as possible (in the least-squares sense) to the updated gradients.";
    }

    public void setParent(PsUpdateIf parent) {
        super.setParent(parent);
        m_ws = (Task2)parent;

        addSubTitle("Mesh editing");

        String[] columns = { "x", "y", "z" };
        Object[][] data = {
                { 1, 0, 0 },
                { 0, 1, 0 },
                { 0, 0, 1 }
        };
        m_tAnalysis = new JTable(data, columns);
        add(m_tAnalysis);

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
            m_ws.m_geom.update(m_ws.m_geom);
            return;
        }
    }
}
