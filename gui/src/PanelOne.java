import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Enumeration;

/**
 * Created by ZHANGZUOHAO on 2015/4/7.
 */
public class PanelOne extends JPanel{
    private  MakeButton conButton = new MakeButton("Convert");
    MakeButton scanButton = new MakeButton("Scan the file");
    static  String srcfilepath = "";
    static  String style = RightPane.syname;
    static  String theme = RightPane.thename;
    static  boolean linechecked = RightPane.islinechecked;
    static  boolean hanechecked = RightPane.ishancementchecked;
    public static String path = "";
    public static String  cmdline = "";
    String srcFilename = "";
    JTree tree = new JTree();
    public PanelOne(){
        JPanel picturePane = new JPanel();
        picturePane.setBorder(BorderFactory.createEmptyBorder());
        setLayout(new BorderLayout());
        add(picturePane,BorderLayout.CENTER);
        getTree();
        JScrollPane scrollPane = new JScrollPane();
        tree.setBackground(new Color(238,238,238));
        tree.setForeground(new Color(238,238,238));
        scrollPane.setViewportView(tree);
        scrollPane.setBackground(new Color(238,238,238));
        picturePane.setLayout(new BorderLayout());
        picturePane.add(scrollPane,BorderLayout.CENTER);
        JLabel label = new JLabel("        Please select the file that you want to convert");
        label.setFont(new Font("SansSerif", Font.ITALIC, 18));
        picturePane.add(new JLabel("       "), BorderLayout.WEST);
        picturePane.add(label,BorderLayout.NORTH);
        JPanel panel = new JPanel();
        panel.setBackground(new Color(43,43,43));
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(conButton);
        JPanel panel2 = new JPanel();
        panel2.setBackground(new Color(43,43,43));
        panel2.setLayout(new FlowLayout(FlowLayout.RIGHT));
        panel2.add(scanButton);
        JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayout(1,2,0,0));
        panel3.add(panel);
        panel3.add(panel2);
        add(panel3, BorderLayout.SOUTH);
        dealEvent();
    }

    public void getTree(){
        DefaultMutableTreeNode top = new DefaultMutableTreeNode();
        DefaultMutableTreeNode node ;
        FileSystemView sys = FileSystemView.getFileSystemView();//获得跟文件系统有关的资源
        final File[] files = File.listRoots();                // 获得本机系统的所有盘，包括光驱，U盘
        for(int i = 0; i < files.length; i++) {
            // 利用if语句剔除光驱
            if(!sys.getSystemTypeDescription(files[i]).contains("驱")){
                node = new DefaultMutableTreeNode(files[i]);
                top.add(node);
            }
        }

        Font font = new Font("Dialog",Font.PLAIN,12);
        Enumeration keys = UIManager.getLookAndFeelDefaults().keys();
        while(keys.hasMoreElements()){
            Object key = keys.nextElement();
            if(UIManager.get(key) instanceof  Font){
                UIManager.put(key,font);
            }
            try{
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        tree = new JTree(top);
        DefaultTreeCellRenderer myCellRenderer = new MyTreeCellRender();
        //设置叶子节点的图标
        tree.setCellRenderer(myCellRenderer);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setEditable(false);
        tree.setRootVisible(false);
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                String string = e.getPath().toString();
                int n = string.length();
                string = string.substring(1,n-1);
                string = string.replace(", ", "\\");
                string = string +"\\";
                path = string;
                srcfilepath = path;
                System.out.println("srcfilepath"+srcfilepath);
                if(srcfilepath.charAt(0)=='\\'){
                    srcfilepath = srcfilepath.substring(1, srcfilepath.length());
                }
                System.out.println(srcfilepath);
                if(srcfilepath.charAt(srcfilepath.length()-1)=='\\'){
                    srcfilepath = srcfilepath.substring(0,srcfilepath.length()-1);
                }
                System.out.println(srcfilepath);
                srcfilepath.replace("\\\\", "\\");
                System.out.println(srcfilepath);
                cmdline = "java -jar cli.jar ";
                File   file = new File(srcfilepath);

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
                    }
                     else if(file.getName().contains(".js")){
                        System.out.println("file"+ file.getName());
                        cmdline = cmdline + "-t "+ "js";
                        RightPane.setStyleChoice("Javascript");
                    }
                    else if (file.getName().contains(".py")) {
                        cmdline = cmdline + "-t "+ "py";
                        RightPane.setStyleChoice("Python");
                    }
                    else {
                    }
                }
                DefaultMutableTreeNode node1 = (DefaultMutableTreeNode)e.getPath().getLastPathComponent();
                if(string.contains("."))return;
                File temp = new File(string);
                if(temp==null)
                    return;
                File [] files1 = temp.listFiles();
                DefaultMutableTreeNode node2;

                for(int i = 0; i<files1.length;i++){
                    node2 = new DefaultMutableTreeNode(files1[i].getName());
                    node1.add(node2);
                }
            }
        });
    }
    public void dealEvent(){
        conButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!srcfilepath.contains(".")){
                    JOptionPane.showMessageDialog(null,"Please select the correctly file\nThe file style   *.*","Information",JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                cmdline = cmdline + " -s "+srcfilepath;
                int i = srcfilepath.indexOf('.');
                srcFilename = srcfilepath.substring(0,i);

                    int option = JOptionPane.showConfirmDialog(null,"Do you want to use the default path to save the dst file ?");
                    if (option == JOptionPane.YES_OPTION){
                        srcFilename = srcFilename + ".html";
                    }
                    else if(option == JOptionPane.NO_OPTION){
                        JFileChooser jFileChooser = new JFileChooser();
                        jFileChooser.setDialogTitle("Select the path to save the dst file");// set the title
                        jFileChooser.setFont(new Font("SansSerif", Font.ITALIC, 28));
                        jFileChooser.setMultiSelectionEnabled(false);
                        FileSystemView fsv = FileSystemView.getFileSystemView();
                        //  System.out.println(fsv.getHomeDirectory());                //得到桌面路径
                        jFileChooser.setCurrentDirectory(fsv.getHomeDirectory());  // 设置默认路径

                        if (jFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                            srcFilename = jFileChooser.getSelectedFile().getAbsolutePath();
                            if(!srcFilename.contains(".")){
                                srcFilename = srcFilename + ".html";
                            }
                            System.out.println("srcFilename"+srcFilename);
                        }
                        else {
                            srcFilename = srcFilename + ".html";
                        }
                    }
                    else if(option == JOptionPane.CANCEL_OPTION){
                        return;
                    }
                cmdline = cmdline + " -o "+srcFilename;
                cmdline = cmdline + " -c "+theme;

                if(linechecked){
                    cmdline = cmdline + " -l ";
                }
                if(hanechecked){
                    cmdline = cmdline + " -e ";
                }

                    try {
                        Process pr = Runtime.getRuntime().exec(cmdline);
                        int exitcode = pr.waitFor();
                        //  JOptionPane.showMessageDialog(null,"Y","t",JOptionPane.CANCEL_OPTION);
                        //   System.out.println(cmdline);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        System.exit(0);
                    }
            }
        });
        scanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            JFileChooser jFileChooser = new JFileChooser(srcFilename);
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
class MyTreeCellRender extends DefaultTreeCellRenderer {
    //定义图标和要显示的字符串
    ImageIcon icon = null;
    String str = null;
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
    {
        // TODO Auto-generated method stub

        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
                row, hasFocus);
        DefaultMutableTreeNode   node=(DefaultMutableTreeNode)value;
        String str =PanelOne.path  +"\\"+value.toString();
        // String str = "E:\\Photos\\cp.png";
      //  System.out.println("str:" + str);
        //  System.out.println("Strng :" + );
        File file = new File(str);
        FileSystemView fileSystemView = FileSystemView.getFileSystemView();
        ImageIcon imageIcon = (ImageIcon)fileSystemView.getSystemIcon(file);
        //  if (node.getNextNode().)
        setIcon(imageIcon);
        return this;
    }
}