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
    protected Button m_bMakeRandomElementColors;
    protected Button m_bMakeRandomVertexColors;
    protected PuDouble m_xOff;

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

        addSubTitle("Example of a subtitle");

        m_bMakeRandomElementColors = new Button("Random Element Colors");
        m_bMakeRandomElementColors.addActionListener(this);
        m_bMakeRandomVertexColors = new Button("Random Vertex Colors");
        m_bMakeRandomVertexColors.addActionListener(this);
        Panel panel1 = new Panel(new FlowLayout(FlowLayout.CENTER));
        panel1.add(m_bMakeRandomElementColors);
        panel1.add(m_bMakeRandomVertexColors);
        add(panel1);

        m_xOff = new PuDouble("X Offset");
        m_xOff.setDefBounds(-10,10,0.1,1);
        m_xOff.addUpdateListener(this);
        m_xOff.init();
        add(m_xOff.getInfoPanel());

        validate();
    }

    @Override
    public boolean update(Object event) {
        if (event == m_xOff) {
            m_ws.setXOff(m_xOff.getValue());
            m_ws.m_geom.update(m_ws.m_geom);
            return true;
        } else
            return super.update(event);
    }

    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if (source == m_bMakeRandomElementColors) {
            m_ws.makeRandomElementColors();
            m_ws.m_geom.update(m_ws.m_geom);
            return;
        }
        else if (source == m_bMakeRandomVertexColors) {
            m_ws.makeRandomVertexColors();
            m_ws.m_geom.update(m_ws.m_geom);
            return;
        }
    }
}
