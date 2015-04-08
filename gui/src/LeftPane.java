import com.sun.javaws.progress.Progress;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by ZHANGZUOHAO on 2015/3/30.
 */
public class LeftPane extends JTabbedPane{
    private  PanelOne panelone = new PanelOne();
    private  PanelTwo paneltwo = new PanelTwo();
    public LeftPane(){
        this.setFont(new Font("SansSerif",Font.ITALIC,22));
        this.addTab("panelone",panelone);
        this.setEnabledAt(0,true);
        this.setTitleAt(0,"File Conversion");

        this.addTab("paneltwo",paneltwo);
        this.setEnabledAt(1,true);
        this.setTitleAt(1,"Direct Text Conversion");
        this.setTabPlacement(JTabbedPane.TOP);
        this.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
    }
}
