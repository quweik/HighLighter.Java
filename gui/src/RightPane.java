import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created by ZHANGZUOHAO on 2015/3/31.
 */
public class RightPane extends JPanel{
    private  JPanel optionPane = new JPanel();
    private  JPanel buttonPane = new JPanel();
    private  JLabel type = new JLabel("Style");
    private  static JComboBox styleChoice ;
    private  JLabel theme = new JLabel("  Theme");
    private  JComboBox chooseTheme ;
    private  JCheckBox lineCheckBox = new JCheckBox("Show line number",false);
    private  JCheckBox hanceCheckBox = new JCheckBox("EnhanceMent",false);
    private  JButton buttonExit = new JButton("Exit");
    static  String syname = "Java";
    static String thename = "default";
    static boolean islinechecked = false;
    static boolean ishancementchecked = false;
    public RightPane(){
        GridBagLayout layout = new GridBagLayout();
        optionPane.setLayout(layout);
        String [] styleName = {"Java","C/C++","Python","Haskell","Javascript"};
        JLabel label = new JLabel("Setting options");
        styleChoice = new JComboBox(styleName);
        String [] themeName = {"default","desert","molokai","GRB256","solarized_light","solarized_dark"};
        chooseTheme = new JComboBox(themeName);
        // make the size of the font
        type.setFont(new Font("Courier", Font.BOLD, 20));
        styleChoice.setFont(new Font("Courier", Font.BOLD, 20));
        theme.setFont(new Font("Courier", Font.BOLD, 20));
        chooseTheme.setFont(new Font("Courier", Font.BOLD, 20));
        lineCheckBox.setFont(new Font("Courier", Font.BOLD, 20));
        hanceCheckBox.setFont(new Font("Courier", Font.BOLD, 20));
        label.setFont(new Font("Courier", Font.BOLD, 26));

        // set init the comboBox
        styleChoice.setEditable(false);
        chooseTheme.setEditable(false);
        styleChoice.setSelectedIndex(0);
        chooseTheme.setSelectedIndex(0);


        optionPane.setBorder(new TitledBorder(label.getText()));
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.NONE;
        constraints.ipady = 1;
        constraints.ipadx = 5;
        constraints.insets.set(10,5,5,5);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        layout.setConstraints(type, constraints);
        optionPane.add(type);

        constraints.ipadx = 42;
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        layout.setConstraints(styleChoice, constraints);
        optionPane.add(styleChoice);

        constraints.ipadx = 5;
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        layout.setConstraints(theme, constraints);
        optionPane.add(theme);

        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        layout.setConstraints(chooseTheme, constraints);
        optionPane.add(chooseTheme);

        constraints.ipadx = 90;
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 3;
        constraints.gridheight = 1;
        layout.setConstraints(hanceCheckBox, constraints);
        optionPane.add(hanceCheckBox);

        constraints.ipadx = 52;
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 3;
        constraints.gridheight = 1;
        layout.setConstraints(lineCheckBox, constraints);
        optionPane.add(lineCheckBox);

        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPane.add(buttonExit);

        JPanel panel = new JPanel();
        JPanel panel2 = new JPanel();
        panel.setLayout(new GridLayout(2, 1));
        panel.add(optionPane);
        panel.add(panel2);
        this.setLayout(new BorderLayout());
        this.add(panel, BorderLayout.CENTER);
        this.add(buttonPane,BorderLayout.SOUTH);
        HandleEvent();
    }

    public void HandleEvent(){
        buttonExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        styleChoice.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                syname = (String)styleChoice.getSelectedItem();
                PanelTwo.style = syname;
                PanelOne.style = syname;
            }
        });
        chooseTheme.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                thename = (String)chooseTheme.getSelectedItem();
                PanelTwo.theme = thename;
                PanelOne.theme = thename;
            }
        });
        lineCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (lineCheckBox.isSelected()) {
                    islinechecked = true;
                } else {
                    islinechecked = false;
                }
                PanelTwo.linechecked = islinechecked;
                PanelOne.linechecked = islinechecked;
            }
        });
        hanceCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (hanceCheckBox.isSelected()) {
                    ishancementchecked = true;
                } else {
                    ishancementchecked = false;
                }
                PanelTwo.hanechecked = ishancementchecked;
                PanelOne.hanechecked = ishancementchecked;
            }
        });
    }
    public   static  void setStyleChoice(String styleChoice1){
        if(styleChoice1.equals("Java")){
            styleChoice.setSelectedItem("Java");
        }
       else if(styleChoice1.equals("C/C++")){
            styleChoice.setSelectedItem("C/C++");
        }
        else if(styleChoice1.equals("Haskell")){
            styleChoice.setSelectedItem("Haskell");
        }
        else if(styleChoice1.equals("Python")){
            styleChoice.setSelectedItem("Python");
        }
        else {
            styleChoice.setSelectedItem("Javascript");
        }
    }
}
