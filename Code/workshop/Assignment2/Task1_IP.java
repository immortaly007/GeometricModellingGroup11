package workshop.Assignment2;

import jv.object.PsDialog;
import jv.object.PsUpdateIf;
import jvx.project.PjWorkshop_IP;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Immortaly007 on 16-5-2015.
 */
public class Task1_IP extends PjWorkshop_IP implements ActionListener {
    protected Panel m_pSettings;
    protected GridLayout m_gSettings;
    protected Label m_lSelectFunctionVectorField;
    protected Choice m_cSelectFunctionVectorField;
    protected Label m_lSelectCheckVectorField;
    protected Choice m_cSelectCheckVectorField;
    protected Label m_lResultVectorFieldName;
    protected  TextField m_tResultVectorFieldName;
    protected Button m_bCalculate;
    protected Label m_lLog;
    Task1 m_ws;

    public Task1_IP() {
        super();
        if(getClass() == Task1_IP.class)
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
        m_ws = (Task1)parent;

        addSubTitle("Gradient calculation");

        m_pSettings = new Panel();
        m_gSettings = new GridLayout(3, 2);
        m_pSettings.setLayout(m_gSettings);

        m_lSelectFunctionVectorField = new Label("Function:");

        m_cSelectFunctionVectorField = new Choice();
        String[] vectorFieldNames = m_ws.getVectorFieldNames();
        for (int i = 0; i < vectorFieldNames.length; i++)
            m_cSelectFunctionVectorField.add(vectorFieldNames[i]);
        m_cSelectFunctionVectorField.select(0);

        m_lSelectCheckVectorField = new Label("Debug check:");
        m_cSelectCheckVectorField = new Choice();
        m_cSelectCheckVectorField.add("");
        for (int i = 0; i < vectorFieldNames.length; i++)
            m_cSelectCheckVectorField.add(vectorFieldNames[i]);
        m_cSelectCheckVectorField.select(0);

        m_lResultVectorFieldName = new Label("Result name:");
        m_tResultVectorFieldName = new TextField("gradients");

        m_pSettings.add(m_lSelectFunctionVectorField);
        m_pSettings.add(m_cSelectFunctionVectorField);
        m_pSettings.add(m_lSelectCheckVectorField);
        m_pSettings.add(m_cSelectCheckVectorField);
        m_pSettings.add(m_lResultVectorFieldName);
        m_pSettings.add(m_tResultVectorFieldName);

        add(m_pSettings);
        m_bCalculate = new Button("Calculate...");
        m_bCalculate.addActionListener(this);
        add(m_bCalculate);

        m_lLog = new Label();
        add(m_lLog);



        validate();
    }

    @Override
    public boolean update(Object event) {
        return super.update(event);
    }

    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if (source == m_bCalculate) {
            String function = m_cSelectFunctionVectorField.getSelectedItem();
            String check = m_cSelectCheckVectorField.getSelectedItem();
            String result = m_tResultVectorFieldName.getText();
            m_ws.calculate(function, check, result);
            m_lLog.setText("Gradients calculated and stored in vector field with name: " + result);
            m_lLog.validate();
            m_ws.showVectorField(result);
            return;
        }

        if (event.getID() == PsDialog.BUTTON_RESET)
        {
            m_ws.reset();
        }
    }
}
