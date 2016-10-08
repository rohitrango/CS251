import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class DrawCircleInGUI extends JFrame {

    /**
	 * 
	 */
	JTextField x;
	JTextField y;
	JTextField r;
	int xd, yd, rd;
	JButton button1;
	
	JPanel thepanel;
	GPanel paintPanel;
	JPanel textPanel;
	JLabel lab;
	
	private static final long serialVersionUID = 1L;

	public DrawCircleInGUI() {

        initUI();
        
    }

    private void initUI() {
        
        setTitle("TaskE");
        setSize(400, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
       // JPanel thepanel2 = new JPanel();
        thepanel = new JPanel();
        thepanel.setLayout(new BoxLayout(thepanel, BoxLayout.Y_AXIS));
        add(thepanel);
        
        paintPanel = new GPanel();
        textPanel = new JPanel();
        
        thepanel.add(paintPanel);
        thepanel.add(textPanel);
        //add(thepanel2);
        
        
        //thepanel.setLayout(new BorderLayout());
        button1 = new JButton("Draw Circle");

        button1.setToolTipText("Draws a circle");
        x = new JTextField("0", 2);
        y = new JTextField("0", 2);
        r = new JTextField("0", 2);
        
        JLabel xlabel = new JLabel("X coordinate:");
        JLabel ylabel = new JLabel("Y coordinate:");
        JLabel rlabel = new JLabel("Radius:");
        
        DrawNow lbutton = new DrawNow();
        
        button1.addActionListener(lbutton);
        
        textPanel.add(xlabel);
        textPanel.add(x);
        textPanel.add(ylabel);
        textPanel.add(y);
        textPanel.add(rlabel);
        textPanel.add(r);
        textPanel.add(button1);
        lab = new JLabel("The circle will be out of draw area. Try different values.");
		textPanel.add(lab);
		lab.setVisible(false);
    }
	    
    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            DrawCircleInGUI ex = new DrawCircleInGUI();
            ex.setVisible(true);
        });
    }

	private class DrawNow implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			if(e.getSource() == button1)
			{
				xd = Integer.parseInt(x.getText());
				yd = Integer.parseInt(y.getText());
				rd = Integer.parseInt(r.getText());
				
				if(xd - rd < 0 || xd + rd > thepanel.getSize().width || yd - rd < 0 || yd + rd > thepanel.getSize().height)
				{	
					lab.setVisible(true);
				}
				else
				{
					lab.setVisible(false);
					repaint();
				}
			}
			
		}

	}
	
	private class GPanel extends JPanel
	{
	    /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		public GPanel()
		{
			xd = 0;
			yd = 0;
			rd = 0;
		}
		
		
		@Override
		public void paintComponent(Graphics g)
	    {
	        super.paintComponent(g);
	        //rectangle originated at 10,10 and end at 240,240
//	        g.drawRect(10, 10, 240, 240);
//	                //filled Rectangle with rounded corners.    
//	        g.fillRoundRect(50, 50, 100, 100, 80, 80);
	        
	        drawCircleByCenter(g, xd,yd,rd);
	    }
		
		void drawCircleByCenter(Graphics g, int x, int y, int radius){
	        g.drawOval(x-radius, y-radius, 2*radius, 2*radius);
	    }
	    
	}
}