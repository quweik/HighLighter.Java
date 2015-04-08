import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.Parser;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.Properties;
import java.util.Scanner;

public class PanelTwo extends JPanel {
    private  JLabel namelabel = new JLabel("        Destination Filename");
    private  JTextField textname = new JTextField(10);
    private  JLabel textlabel = new JLabel("Source code Please");
    private  JTextArea  textcode = new JTextArea();
    private  JLabel convlabel = new JLabel("Dst converted code");
    private  JButton conv = new JButton("convert");
    private  JTextArea convtext = new JTextArea();
    static  String style = RightPane.syname;
    static  String theme = RightPane.thename;
    static  boolean linechecked = RightPane.islinechecked;
    static  boolean hanechecked = RightPane.ishancementchecked;
    public static  String  outname = "";
    public PanelTwo(){

        namelabel.setFont(new Font("Courier", Font.BOLD, 20));
        textlabel.setFont(new Font("Courier",Font.BOLD,20));    // make the size of the  font
        convlabel.setFont(new Font("Courier",Font.BOLD,20));
        conv.setFont(new Font("Courier",Font.BOLD,20));
        textname.setText(null);
        textname.setFont(new Font("SansSerif", Font.ITALIC, 18));
        textname.setForeground(new Color(82,39,123));
        textcode.setText(null);
        textcode.setLineWrap(true);
        textcode.setWrapStyleWord(true);
        textcode.setFont(new Font("SansSerif", Font.ITALIC, 18));
        textcode.setForeground(new Color(145,13,0));
        convtext.setLineWrap(true);
        convtext.setEditable(false);
        convtext.setWrapStyleWord(true);
        convtext.setFont(new Font("SansSerif",Font.ITALIC,18));
        convtext.setForeground(new Color(22, 154, 218));


        JScrollPane scrollPaneSrc = new JScrollPane(textcode);
        scrollPaneSrc.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);  // make the bar
        scrollPaneSrc.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        JScrollPane scrollPaneDst = new JScrollPane(convtext);
        scrollPaneDst.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPaneDst.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        // set the tooltiptext
        textname.setToolTipText("name the file that you want to save the converted code");
        // textcode.setToolTipText("Write down or paste the code that you want to convert");
        // convtext.setToolTipText("the converted code");
        JPanel panel  = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.X_AXIS)); // the layoutManger of the panel
        panel.add(namelabel);                                    // panel to hold the component
        panel.add(Box.createHorizontalStrut(10));
        panel.add(textname);

        JPanel panel2  = new JPanel();
        panel2.setLayout(new BoxLayout(panel2,BoxLayout.Y_AXIS)); // BoxLayout
        panel2.add(textlabel);
        panel2.add(Box.createVerticalStrut(5));
        panel2.add(scrollPaneSrc);
        panel2.add(Box.createVerticalStrut(5));

        JPanel panel3  = new JPanel();
        panel3.setLayout(new BoxLayout(panel3,BoxLayout.Y_AXIS));
        panel3.add(convlabel);
        panel3.add(Box.createVerticalStrut(5));
        panel3.add(scrollPaneDst);
        panel3.add(Box.createVerticalStrut(5));

        JPanel pane4 = new JPanel();
        JPanel pane5 = new JPanel();
        pane5.setLayout(new FlowLayout(FlowLayout.LEFT));
        pane5.add(conv);
        pane4.setLayout(new BorderLayout());        // BorderLayout
        pane4.add(panel, BorderLayout.NORTH);       // pane4 to hold the panel and panel2
        pane4.add(panel2,BorderLayout.CENTER);
        pane4.add(pane5,BorderLayout.SOUTH);
        this.setLayout(new GridLayout(2, 1, 10, 10));   // GridLayout
        this.add(pane4);
        this.add(panel3);
        dealAction();
    }

    public  void  dealAction(){
        conv.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (textname.getText().length() == 0) {
                    textname.requestFocus();
                    JOptionPane.showMessageDialog(null, "Please named the file that you want to save",
                            "Information", JOptionPane.INFORMATION_MESSAGE);
                    return;
                } else {
                    outname = textname.getText();
                   int  i = outname.indexOf('.');
                    if(i ==-1){
                        System.out.print("yes");
                        outname = outname +".html";
                        System.out.println(outname);
                    }
                }
                if(textcode.getText().length()==0){
                    textcode.requestFocus();
                    JOptionPane.showMessageDialog(null,"Please write or paste your code firstly",
                            "Information",JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                convtext.setText(null);
                convtext.setEditable(true);
                String cmdline = "java -jar cli.jar ";
                int i = outname.indexOf('.');
                String sourceName = outname.substring(0,i+1);
                if (style.equals("Java")) {
                    sourceName = sourceName + "java";
                }
                else if(style.equals("Python")){
                    sourceName = sourceName + "py";
                }
                else if(style.equals("Haskell")){
                    sourceName = sourceName + "hs";
                }
                else if(style.equals("Javascript")){
                    sourceName = sourceName + "js";
                }
                else{
                    sourceName = sourceName + "c";
                }
                cmdline = cmdline + "-s " + sourceName + " -o " + outname + " -c " + theme + " -t " + style;
                if (linechecked) {
                    cmdline = cmdline + " -l";
                }
                if (hanechecked) {
                    cmdline = cmdline + " -e";
                }
                try {
                    SaveSourceFile(sourceName);
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, "failed to save the source file", "Information", JOptionPane.INFORMATION_MESSAGE);
                }
                try {
                    //  System.out.println(cmdline);
                    Process proc = Runtime.getRuntime().exec(cmdline);
                    int exitVal = proc.waitFor();
                    System.out.println(exitVal);

                } catch (Exception e1) {
                    System.exit(0);
                }
                Properties properties = System.getProperties();
                String cmd = "cmd.exe /c start ";
                System.out.println(cmd);
                cmd = cmd + properties.getProperty("user.dir");
                System.out.println();
                cmd = cmd + "\\" + outname;
                System.out.println(cmd);
                String filePath = properties.getProperty("user.dir")+ "\\" + outname;
                readHtml(filePath);
                try {
                    Runtime.getRuntime().exec(cmd);
                } catch (Exception e1) {
                    System.exit(0);
                }
            }
        });

    }
    public  void SaveSourceFile(String  filename)throws  Exception{
        java.io.File file = new java.io.File(filename);
        if(file.exists()){
            JOptionPane.showMessageDialog(null,"file had exited,rewrite the file context",
                    "Information",JOptionPane.INFORMATION_MESSAGE);
        }
        java.io.PrintWriter output = new java.io.PrintWriter(file);
        String [] lines = textcode.getText().toString().split("\n");
        for (int i = 0 ;i<lines.length;i++){
            output.print(lines[i]+"\n");
        }
          output.close();
      }

    public void readHtml(String filePath) {
        try{
            File file = new File(filePath);
            InputStreamReader  reader = new InputStreamReader(new FileInputStream(file),"GB2312");
            BufferedReader in = new BufferedReader(reader);
            String string = null;
            while ( (string =in.readLine().toString()).length()!=0){
                convtext.append(string);
                convtext.append("\n");
            }
            in.close();
        }
        catch (Exception e){
           e.printStackTrace();
        }
    }
}