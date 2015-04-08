import javax.swing.*;
import java.awt.*;

public class MainFrame{
    public MainFrame(){

    }
    public  static  void main(String[] args){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200,800);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(screenSize.width / 2 - frame.getWidth() / 2, screenSize.height / 2 - frame.getHeight() / 2);
        JPanel topPane = new PicturePane();
        LeftPane leftPane = new LeftPane();
        RightPane RightPane = new RightPane();    // make three panel

        JSplitPane splitPane = new JSplitPane();
        splitPane.setOneTouchExpandable(true);
        splitPane.setContinuousLayout(true);
        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT); //add the top and bottom panel
        splitPane.setLeftComponent(topPane);
        splitPane.setDividerLocation(100);
        splitPane.setDividerSize(3);

        JSplitPane splitPaneLR = new JSplitPane();
        splitPaneLR.setOneTouchExpandable(true);
        splitPaneLR.setContinuousLayout(true);
        splitPaneLR.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        splitPaneLR.setLeftComponent(leftPane);                   // add the left and right panel
        splitPaneLR.setRightComponent(RightPane);
        splitPaneLR.setDividerLocation(frame.getWidth()-320);
        splitPaneLR.setDividerSize(10);

        splitPane.setRightComponent(splitPaneLR);   // put the splitPane
        frame.setContentPane(splitPane);
        frame.setVisible(true);
    }
}