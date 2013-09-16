package BrackEm
;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

public class Bracket extends JLabel implements MouseListener{

	/**
	 * 
	 */

	// Organization flags
	boolean blocked =  true; // Applicable only to loser's panel, T means filled by advancement from last round, F means filled by loss incoming from winner's bracket
	boolean editable = false; // Can edit player, True only for first round winners and or if bracket contains an ID
	boolean advancable = false; // Can advance player to next round, True only if editable is True, ID and paired ID exists, and bracket has not been decided yet
	boolean decided = false; // This bracket, and subsequently the match, result has been decided.  True only if this or paired bracket has been advanced.
	
	// Formatting
	String ID = "";
	int bracketIndex;
	int bracketRound;
	String displayName = "";
	int maxCharDisp = 15;
	int labelWidth;
	int labelHeight;
	boolean center = false;
	boolean upper = true;
	
	// Accessories
	JPopupMenu menu = new JPopupMenu("Popup");
	JMenuItem editItem;
	JMenuItem advItem;
	Border raisedbevel = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
	
	private static final long serialVersionUID = 1L;

	Bracket(int x, int y, boolean individual, boolean pairingtype, int placement, int round) {
		super();
		this.setText(displayName);
		
		// Format
		labelWidth = x;
		labelHeight = y;
		center = individual;
		upper = pairingtype;
		bracketIndex = placement;
		bracketRound = round;
		
//		AdvanceButton advanceButton = new AdvanceButton();
//		add(advanceButton);
		
	    editItem = new JMenuItem("Edit Player");
	    editItem.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	  		String result = (String) JOptionPane.showInputDialog(BrackEm.frame,
	  				"Edit player",
	  				"Edit player",
	  				JOptionPane.OK_CANCEL_OPTION,
	  				createImageIcon("images/blank.png"),
	  				null,
	  				getID());
	  
	  		if (result != null) {
	  			setID(result);
	  		}
	      }
	    });
	    editItem.setEnabled(false);
	    menu.add(editItem);
	    
	    advItem = new JMenuItem("Advance");
	    advItem.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	    	  if (BrackEm.debug){
	    		  System.out.println("JMenuItem (actionPerformed): Advanced");
	    	  }
	    	  requestUpdate();
	      }
	    });
	    advItem.setEnabled(false);
	    menu.add(advItem);
	    
		this.setFont(new Font(this.getName(), Font.PLAIN, this.getFont().getSize()*7/4));
		
		setMaximumSize(new Dimension(labelWidth,labelHeight));
		setPreferredSize(new Dimension(labelWidth,labelHeight));
		setMinimumSize(new Dimension(labelWidth,labelHeight));
		setHorizontalAlignment(JLabel.CENTER);
		
		addMouseListener(this);
	}

	void requestUpdate(){
		if (BrackEm.debug){
			System.out.println("Bracket (requestUpdate): Update request");
		}
        BrackEm.bracketUpdate(bracketRound,bracketIndex,SwingUtilities.getAncestorOfClass(BracketPanel.class, this).getName());
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		this.setBorder(raisedbevel);
		repaint();
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		this.setBorder(null);
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		menu.show(arg0.getComponent(), arg0.getX(), arg0.getY());
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
	
	/** Returns an ImageIcon, or null if the path was invalid. */
	protected ImageIcon createImageIcon(String path) {
	    java.net.URL imgURL = getClass().getClassLoader().getResource(path);
	    if (imgURL != null) {
	        return new ImageIcon(imgURL);
	    } else {
	        System.err.println("Couldn't find file: " + path);
	        return null;
	    }
	}
	
	void setID(String arg){
		ID = arg;
		setDisplayName();
		if(ID.isEmpty()!=true && getDecided()!=true){
			setAdvancable(true);
		}
	}
	
	void setDisplayName(){
		if (getID().length() > maxCharDisp){
			displayName = getID().substring(0,15) + "...";
		} else {
			displayName = getID();
		}
		this.setText("<html><font color='blue'>" + displayName + "</font></html>");
	}
	
	String getID(){
		return this.ID;
	}
	
	int getPlacement(){
		return bracketIndex;
	}
	
	void setState(boolean state){
		blocked = state;
	}
	
	boolean getState(){
		return blocked;
	}
	
	void setEditable(boolean state){
		editable = state;
		if (getEditable()){ // Set menu item enable
			editItem.setEnabled(true);
		} else {
			editItem.setEnabled(false);
		}
	}
	
	boolean getEditable(){
		return editable;
	}
	
	void setAdvancable(boolean state){
		advancable = state;
		if (getAdvancable()){ // Set menu item enable
			advItem.setEnabled(true);
		} else {
			advItem.setEnabled(false);
		}
	}
	
	boolean getAdvancable(){
		return advancable;
	}
	
	void setDecided(boolean state){
		decided = state;
	}
	
	boolean getDecided(){
		return decided;
	}
	
	int getStringWidth(){
		return this.getFontMetrics(this.getFont()).stringWidth(displayName);
	}
	int[] getStringXY(){
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
	    
	    g2.drawLine(0, labelHeight/2, getStringXY()[0]-2, labelHeight/2); // Line from left to text
	    g2.drawLine(getStringXY()[1]+2, labelHeight/2, labelWidth, labelHeight/2); // Line from text to right
	    
	    if (center){
	    } else {
	    	if (upper){
	    		g2.drawLine(labelWidth-2,labelHeight/2,labelWidth-2,labelHeight); // Line moving downwards
	    	} else {
	    		g2.drawLine(labelWidth-2,labelHeight/2,labelWidth-2,0); // Line moving upwards
	    	}
	    }
	}
	
//	class AdvanceButton extends JButton implements ActionListener {
//		/**
//		 * 
//		 */
//		private static final long serialVersionUID = 1L;
//
//		AdvanceButton() {
//			super(createImageIcon("images/circle_green.png")); 
//			addActionListener(this);
//			setMaximumSize(new Dimension(labelWidth,lineLabelHeight));
//			setPreferredSize(new Dimension(labelWidth,lineLabelHeight));
//			setMinimumSize(new Dimension(labelWidth,lineLabelHeight));
//		}
//		
//		public void paintComponent(Graphics g){
//		    super.paintComponent(g);
//		    Graphics2D g2 = (Graphics2D) g;
//		    g2.setStroke(new BasicStroke(7)); // Why larger than 3?
//		    g2.drawLine(getWidth(),0,getWidth(),lineLabelHeight); // Line on right side
//		}

//		public void actionPerformed(ActionEvent arg0) {
//			// TODO Auto-generated method stub
//			
//		}
//	}
//	
}
