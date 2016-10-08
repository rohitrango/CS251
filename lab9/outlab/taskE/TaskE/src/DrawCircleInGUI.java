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
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
       // JPanel thepanel2 = new JPanel();
        thepanel = new JPanel();
        
        // Set the layout as GridBagLayout and specify properties

        
        thepanel.setLayout(new BorderLayout());
        paintPanel = new GPanel();
        
        textPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        // c.fill = GridBagConstraints.HORIZONTAL;
        // set properties
        thepanel.add(paintPanel);

        // set properties
        thepanel.add(textPanel,BorderLayout.SOUTH);

        add(thepanel);
        //add(thepanel2);        
        //thepanel.setLayout(new BorderLayout());
        button1 = new JButton("Draw Circle");

        button1.setToolTipText("Draws a circle");
        x = new JTextField("0", 3);
        y = new JTextField("0", 3);
        r = new JTextField("0", 3);
        
        JLabel xlabel = new JLabel("X coordinate:");
        JLabel ylabel = new JLabel("Y coordinate:");
        JLabel rlabel = new JLabel("Radius:");
        
        DrawNow lbutton = new DrawNow();
        
        button1.addActionListener(lbutton);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx=0; c.gridy=0;
        textPanel.add(xlabel,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx=1; c.gridy=0;
        textPanel.add(x,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx=2; c.gridy=0;
        textPanel.add(ylabel,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx=3; c.gridy=0;
        textPanel.add(y,c);
    
    	c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx=4; c.gridy=0;
		textPanel.add(rlabel,c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx=5; c.gridy=0;
        textPanel.add(r,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx=0; c.gridy=1; c.gridwidth=6;
        textPanel.add(button1,c);

        lab = new JLabel("The circle will be out of draw area. Try different values.");

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx=0; c.gridy=2; c.gridwidth=6;
		textPanel.add(lab,c);
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
				try {
					xd = Integer.parseInt(x.getText());
					yd = Integer.parseInt(y.getText());
					rd = Integer.parseInt(r.getText());
					
					if((xd - rd) < 0 || (xd + rd) > paintPanel.getSize().width || (yd - rd) < 0 || (yd + rd) > paintPanel.getSize().height || rd < 0)
					{	
						lab.setVisible(true);
					}
					else
					{
						lab.setVisible(false);
						repaint();
					}
				}
				catch(Exception eeee) {
					System.out.println("\n"+eeee.getMessage());
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
			// setBackground(Color.gray);
		}
		
		
		@Override
		public void paintComponent(Graphics g)
	    {
	        super.paintComponent(g);	        
	        if((xd - rd) < 0 || (xd + rd) > paintPanel.getSize().width || (yd - rd) < 0 || (yd + rd) > paintPanel.getSize().height || rd < 0) {
	        	// System.out.println("Invalid values!");
	        }
	        else {
	        	drawCircleByCenter(g, xd,yd,rd);
	        }

	    }
		
		void drawCircleByCenter(Graphics g, int x, int y, int radius){
			Graphics2D g2 = (Graphics2D)g;
			g2.setStroke(new BasicStroke(3));
	        g2.drawOval(x-radius, y-radius, 2*radius, 2*radius);
	    }
	    
	}
}