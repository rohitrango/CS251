import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;


public class FitCircleInGUI {


	/* This is the frame that will contain panels and buttons */
	public static class CircleGUI extends JFrame{


		// handles events like mouse press, release
		public class CanvasMouseListener implements MouseListener {
			
			public void mouseDragged(MouseEvent e) {
				//
			}

			public void mousePressed(MouseEvent e) {
				//
			}

			public void mouseClicked(MouseEvent e) {
				//
			}

			public void mouseReleased(MouseEvent e) {
				//		
			}

			public void mouseEntered(MouseEvent e) {
				//
			}

			public void mouseExited(MouseEvent e) {
				//
			}
		}

		// handles draw events
		public class DrawOnCanvas implements MouseMotionListener {

			public DrawOnCanvas() {
				canvasPanel.addMouseMotionListener(this);
				addMouseMotionListener(this);
			}

			public void mouseMoved(MouseEvent e) {
				String posMouse = "Coordinates: (" + e.getX() + ", " + e.getY() + ")";
				if(canvasPanel.fitRadius > -1) {
					String rad = "Radius: " + canvasPanel.fitRadius + ", Center: (" + (int)canvasPanel.fitCenter.x + ", " + (int)canvasPanel.fitCenter.y + ")";
					radiusMsg.setText(rad);
				}
				else {
					radiusMsg.setText("");
				}
				message.setText(posMouse);
		    }

		    public void mouseDragged(MouseEvent e) {
		    	// System.out.println("Mouse moved at (" + e.getX() + ", " + e.getY() + ")" );	
		    	String posMouse = "Coordinates: (" + e.getX() + ", " + e.getY() + ")";
		    	message.setText(posMouse);

		    	Point location = new Point(e.getX(),e.getY());
		    	try {
		    		pts.add(location);
		    		repaint();			// repaint the canvas
		    	}	
		    	catch(Exception eee) {
		    		System.out.println(eee);
		    	}
		    }
		}


		public class MyCanvas extends JPanel {

			public boolean circleDraw;
			public int fitRadius;
			public Point2D.Double fitCenter;

			public MyCanvas() {
				super();
				circleDraw = false;
				fitRadius = -1;
			}

			public void paintComponent(Graphics g) {
		        super.paintComponent(g);

		        g.setColor(Color.red);
		        if(pts.size() > 1) {
		        	for(int i=0;i<pts.size()-1;i++) {
		        		g.drawLine(pts.get(i).x, pts.get(i).y, pts.get(i+1).x, pts.get(i+1).y);
		        	}
		        }
		        g.setColor(Color.black);

		        if(circleDraw == true) {
		        	DrawCircleFromPoints(g);
		        }

		        if(fitRadius > -1) {
		        	DrawCircle(g);
		        }

	   	 	}


	   	 	public void DrawCircleFromPoints(Graphics g) {
	   	 		
	   	 		if(pts.size() > 1) {

	   	 			Point2D.Double[] circlePoints = new Point2D.Double[pts.size()];
	   	 			for(int i=0;i<pts.size();i++)
	   	 				circlePoints[i] = new Point2D.Double( (double)pts.get(i).x , (double)pts.get(i).y );
	   	 			CircleFitter fitC = new CircleFitter(circlePoints);
	   	 			fitC.run();

	   	 			fitCenter = fitC.getCenter();
	   	 			fitRadius = (int)fitC.getRadius();

	   	 			DrawCircle(g);
	   	 			
	   	 		}
	   	 		circleDraw = false;

	   	 	}

	   	 	public void DrawCircle(Graphics g) {
	   	 		g.setColor(Color.green.darker());
	   	 		g.drawOval((int)fitCenter.x - fitRadius, (int)fitCenter.y - fitRadius, 2*fitRadius, 2*fitRadius);
	   	 		g.setColor(Color.black);
	   	 	}

		}
		
		private Toolkit mTools;	// toolkit
		private Dimension size;	// dimensions
		private JPanel btnPanel, mainPanel, btnSuperPanel;
		private MyCanvas canvasPanel;
		private JButton fitCircleBtn, clearBtn;
		public static ArrayList<Point> pts;
		private JLabel message, radiusMsg;
		// Default constructor 
		public CircleGUI() {

			pts = new ArrayList<Point>();
			mTools = getToolkit();
			setSize(600,500);
			size = mTools.getScreenSize();
			setLocation((size.width-getWidth())/2,(size.height - getHeight())/2);
			setTitle("Extra Credit: Draw a Circle");
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			// btnPanel = new JPanel();
			// canvasPanel = new JPanel();
			message = new JLabel("");
			radiusMsg = new JLabel("");
			mainPanel = new JPanel(new BorderLayout());
			btnSuperPanel = new JPanel(new GridLayout(3,1,0,0));
			btnPanel = new JPanel(new GridLayout(1,2,0,0));
			canvasPanel = new MyCanvas();

			mainPanel.add(canvasPanel);
			mainPanel.add(btnSuperPanel,BorderLayout.SOUTH);

			btnSuperPanel.add(btnPanel);
			btnSuperPanel.add(message);
			btnSuperPanel.add(radiusMsg);

			fitCircleBtn = new JButton("Fit the Circle now");
			clearBtn = new JButton("Clear Screen");

			add(mainPanel);

			btnPanel.add(fitCircleBtn);
			btnPanel.add(clearBtn);
			// fit Circle button sets the circle variable to true and repaints 
			fitCircleBtn.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent e) {

					// System.out.println("Fitting circle!");
					canvasPanel.circleDraw = true;
					repaint();
				}

			});

			// clear button removes the list and repaints
			clearBtn.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					
					// System.out.println("Clearing screen!");
					pts.clear();
					repaint();
					canvasPanel.fitRadius = -1;
				}
			});
			
			canvasPanel.addMouseMotionListener(new DrawOnCanvas());
			canvasPanel.addMouseListener(new CanvasMouseListener());
			// Everything has been added, now time for adding listeners

		}
	}

	public static void main(String[] args) {
		CircleGUI frame = new CircleGUI();
		frame.setVisible(true);
	}
}