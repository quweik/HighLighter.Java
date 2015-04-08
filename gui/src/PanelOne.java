import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by ZHANGZUOHAO on 2015/4/7.
 */
public class PanelOne extends JPanel{
    private  JButton btnChoose = new JButton("Choose the file");
    private  JButton btnScan = new JButton("Scan the dst");
    JPanel picturePane = new JPanel();
    static  String srcfilepath = "";
    static  String style = RightPane.syname;
    static  String theme = RightPane.thename;
    static  boolean linechecked = RightPane.islinechecked;
    static  boolean hanechecked = RightPane.ishancementchecked;
    public PanelOne(){
        setLayout(new BorderLayout());
        add(picturePane,BorderLayout.CENTER);
        GridBagLayout layout = new GridBagLayout();
        picturePane.setLayout(layout);

        btnChoose.setFont(new Font("SansSerif", Font.ITALIC, 20));
        btnScan.setFont(new Font("SansSerif", Font.ITALIC, 20));
        btnChoose.setBackground(new Color(189, 243, 253));
        btnScan.setBackground(new Color(189,243,253));
        btnChoose.setForeground(new Color(12,154,218));
        btnScan.setForeground(new Color(12,154,218));
        btnChoose.setBorder(BorderFactory.createLineBorder(new Color(189,243,253)));
        btnScan.setBorder(BorderFactory.createLineBorder(new Color(189,243,253)));
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.NONE;
        constraints.ipady = 15;
        constraints.ipadx = 0;
        constraints.insets.set(25,5,25,5);
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = 5;
        constraints.gridheight = 2;
        layout.setConstraints(btnChoose,constraints);
        picturePane.add(btnChoose);

        constraints.ipadx = 25;
        constraints.gridx = 1;
        constraints.gridy = 11;
        constraints.gridwidth = 5;
        constraints.gridheight = 2;
        layout.setConstraints(btnScan,constraints);
        picturePane.add(btnScan);
        dealEvent();
    }

    public void dealEvent(){
        btnChoose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setDialogTitle("Select the source file");// set the title
                jFileChooser.setFont(new Font("SansSerif", Font.ITALIC, 28));
                jFileChooser.setMultiSelectionEnabled(false);
                FileSystemView fsv = FileSystemView.getFileSystemView();
              //  System.out.println(fsv.getHomeDirectory());                //得到桌面路径
                jFileChooser.setCurrentDirectory(fsv.getHomeDirectory());  // 设置默认路径
                String srcFilename = "";
                String  cmdline = "java -jar cli.jar ";
                File   file = null;
                        if (jFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                            file = jFileChooser.getSelectedFile();
                            //     srcFilename = file.getName();     //get the file name
                            srcfilepath = jFileChooser.getSelectedFile().getAbsolutePath();
                        }
                if(file!= null) {
                    if (file.getName().contains(".java")) {
                        cmdline = cmdline + "-t "+ "Java";
                        RightPane.setStyleChoice("Java");
                    } else if (file.getName().contains(".c")) {

                        System.out.println(file.getName());

                        cmdline = cmdline + "-t "+ "c";
                        RightPane.setStyleChoice("C/C++");
                    } else if (file.getName().contains(".hs")) {
                        cmdline = cmdline + "-t "+ "hs";
                        RightPane.setStyleChoice("Haskell");
                    } else if (file.getName().contains("py")) {
                        cmdline = cmdline + "-t "+ "py";
                        RightPane.setStyleChoice("Python");
                    } else if (file.getName().equals("js")){
                        cmdline = cmdline + "-t "+ "js";
                        RightPane.setStyleChoice("Javascript");
                    }
                }

                cmdline = cmdline + " -s "+srcfilepath;
                int i = srcfilepath.indexOf('.');
                srcFilename = srcfilepath.substring(0,i+1);
                srcFilename = srcFilename + "html";
                cmdline = cmdline + " -o "+srcFilename;
                cmdline = cmdline + " -c "+theme;

                if(linechecked){
                    cmdline = cmdline + " -l ";
                }
                if(hanechecked){
                    cmdline = cmdline + " -e ";
                }
                try{
                    Process pr = Runtime.getRuntime().exec(cmdline);
                    int exitcode = pr.waitFor();
                    JOptionPane.showMessageDialog(null,"Y","t",JOptionPane.CANCEL_OPTION);
                    System.out.println(cmdline);
                }
                catch (Exception e1){
                    e1.printStackTrace();
                    System.exit(0);
                }
            }
        });
        btnScan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            JFileChooser jFileChooser = new JFileChooser(srcfilepath);
                if(jFileChooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION){
                    String cmdline = "cmd.exe /c start ";
                     cmdline = cmdline +jFileChooser.getSelectedFile().getAbsolutePath();
                   try{
                       Runtime.getRuntime().exec(cmdline);
                   }
                   catch (Exception e1){
                       System.exit(0);
                   }
                }
            }
        });
    }
}
