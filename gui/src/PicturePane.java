import javax.swing.*;
import java.awt.*;

/**
 * Created by ZHANGZUOHAO on 2015/3/30.
 */
public class PicturePane extends JPanel{
    Image image;
    public PicturePane(){
        ImageIcon icon =new ImageIcon("cp.png");
        image = icon.getImage();
    }

    protected void paintComponent(Graphics g){       // paint the backGround of the pane
        super.paintComponent(g);
        Graphics2D gd = (Graphics2D)g;
        gd.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);              // 出去锯齿操作，2D绘图可以做平滑处理
        gd.drawImage(image, 0, 0,this.getWidth(), this.getHeight(), this);
        gd.setColor(Color.BLUE);
        gd.setFont(new Font("SansSerif",Font.ITALIC,30));
        FontMetrics fm = gd.getFontMetrics();                   // get the statue of gd
        int stringWidth = fm.stringWidth("C O D I N G     C O N V E R T E R");
        int stringAscent = fm.getAscent();
        int xCoordinate = this.getWidth()/2 - stringWidth/2;
        int yCoordinate = this.getHeight()/2+stringAscent/2;
        gd.drawString("C O D I N G     C O N V E R T E R",xCoordinate,yCoordinate);
        gd.dispose();
        g.dispose();
    }
}
