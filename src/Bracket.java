import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

public class Bracket extends JLabel implements MouseListener{

	/**
	 * 
	 */
	
	String ID;
	String placement;
	String displayName = "empty";
	int maxCharDisp = 15;
	Font labelFont;
	int labelWidth;
	int labelHeight;
	boolean center = false;
	boolean upper = true;
	
	Border raisedbevel = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
	
	private static final long serialVersionUID = 1L;

	Bracket(int x, int y, boolean individual, boolean pairingtype) {
		super();
		this.setText(displayName);
		
		// Format
		labelWidth = x;
		labelHeight = y;
		center = individual;
		upper = pairingtype;
		
		setMaximumSize(new Dimension(labelWidth,labelHeight));
		setPreferredSize(new Dimension(labelWidth,labelHeight));
		setMinimumSize(new Dimension(labelWidth,labelHeight));
		setHorizontalAlignment(JLabel.CENTER);
		
		addMouseListener(this);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		this.setBorder(raisedbevel);
		repaint();
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		this.setBorder(null);
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
    	String result = JOptionPane.showInputDialog(BrackEm.frame,
    			"Edit player",
    			"Edit player",
    			JOptionPane.OK_CANCEL_OPTION
    			);
    	
    	if (result != null) {
    		ID = result;
    		if (ID.length() > maxCharDisp){
    			displayName = ID.substring(0,15) + "...";
    		} else {
    			displayName = ID;
    		}
    		
    		this.setText(displayName);
    	}
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	int getStringWidth(){
		return this.getFontMetrics(this.getFont()).stringWidth(displayName);
	}
	
	int[] getStringX(){
		int stringWidth = getStringWidth();
		int x1 = (labelWidth/2) - (stringWidth/2);
		int x2 = (labelWidth/2) + (stringWidth/2);
		int[] arrayOut = {x1,x2};
		return arrayOut;
	}
	
	public void paintComponent(Graphics g){
	    super.paintComponent(g);
	    Graphics2D g2 = (Graphics2D) g;
	    g2.setStroke(new BasicStroke(3));
	    
	    g2.drawLine(0, labelHeight/2, getStringX()[0]-2, labelHeight/2); // Line from left to text
	    g2.drawLine(getStringX()[1]+2, labelHeight/2, labelWidth, labelHeight/2); // Line from text to right
	    
	    if (center){
	    } else {
	    	if (upper){
	    		g2.drawLine(labelWidth-2,labelHeight/2,labelWidth-2,labelHeight); // Line moving downwards
	    	} else {
	    		g2.drawLine(labelWidth-2,labelHeight/2,labelWidth-2,0); // Line moving upwards
	    	}
	    }
	}
	
	
}
