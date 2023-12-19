package schd;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import java.awt.*;

public class Form extends JFrame{
    private JPanel panel1;
    private JScrollPane pane;
    private JTable table1;
    private JTextArea textArea1;
    private JTextField textField1;
    public JButton button1;
    private JPanel bottomPanel;
    private JLabel cmdLbl;
    private JTextPane textPane1;
    private ButtonPress buttonPress;
    private Style style;
    public Form(){
        setVisible(true);
        style = textPane1.addStyle("Style", null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        DefaultTableModel model = TableCreate.createModel(Main.itinerary, "");
        table1.setModel(model);
        setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height));
        bottomPanel.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width - 5, Toolkit.getDefaultToolkit().getScreenSize().height / 2));
        textPane1.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width / 4, Toolkit.getDefaultToolkit().getScreenSize().height / 16));
        textField1.setPreferredSize(new Dimension(10, 5));
        button1.setPreferredSize(new Dimension(100, 50));
        setContentPane(panel1);
        pack();
        setLocationRelativeTo(null);
        button1.addActionListener(ActionEvent ->{
            if(buttonPress != null){
                buttonPress.buttonPressed(Main.curriculum, Main.professors, Main.schedule, Main.courseMap, Main.itinerary, Form.this);
            }
        });

    }

    public void updateTable(DefaultTableModel model){
        this.table1.setModel(model);
    }
    public JTextPane getTextPane(){
        return this.textPane1;
    }


    public String getTextField(){
        return this.textField1.getText();
    }

    public void setPressed(ButtonPress press){
        this.buttonPress = press;
    }

    public JLabel getLabel(){
        return this.cmdLbl;
    }
    public void setStyle(Color color){
        StyleConstants.setForeground(this.style, color);
    }
    public Style getStyle(){
        return this.style;
    }

}
