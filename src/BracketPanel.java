import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
//import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class BracketPanel extends JPanel implements ActionListener{

	/**
	 * 
	 */
	
//	private JLayeredPane layeredPane = new JLayeredPane();
	protected int panelWidth;
	protected int panelHeight;
	protected int labelWidth;
	protected int labelHeight;
	
	private static final long serialVersionUID = 1L;

	public BracketPanel(String arg){
		super();
		
		if (BrackEm.debug){
			System.out.println(arg + " created.");
		}
        
//		layeredPane.setLayout(new GridLayout(2,3));
//		layeredPane.add(new Bracket());
//		add(layeredPane);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	void populateWinPanel(){

        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        
		panelWidth = getWidth();
		panelHeight = getHeight();
		
		labelWidth = panelWidth/(BrackEm.bracketData.getTotalRoundsW() + 1); // Include final winner (+1)
		int startingLabels = 2*(BrackEm.bracketData.getSecondPlayersW());
		
		if (BrackEm.debug){
			System.out.println("winPanel width: " + panelWidth);
			System.out.println("winPanel height: " + panelHeight);
			System.out.println("Rounds: " + BrackEm.bracketData.getTotalRoundsW());
			System.out.println("Label width: " + labelWidth);
			System.out.println("Start labels: " + startingLabels);
		}

		for(int i=0;i<=BrackEm.bracketData.getTotalRoundsW();i++) {  // Include final winner (<=)

			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

			int numOfLabels = (int) (startingLabels/(Math.pow(2, i)));
			labelHeight = panelHeight/numOfLabels;
			
			if (BrackEm.debug){
				System.out.println("Label height: " + labelHeight);
			}

			boolean upper = true;
			boolean individual = false;
			if (i==BrackEm.bracketData.getTotalRoundsW()){ // Last panel is individual
				individual = true;
			}
			
			for(int ii=1;ii<=numOfLabels;ii++) {

				if ( (ii % 2) == 0){ // Check even/odd for upper/lower
					upper = false;
				} else {
					upper = true;
				}
				
				if (i==0){ // First round Bys 
					if (ii<=BrackEm.bracketData.getFirstPlayersW()){
						panel.add(new Bracket(labelWidth,labelHeight,individual,upper));
					} else {
						panel.add(Box.createRigidArea(new Dimension(0,labelHeight)));
					}
				} else {

					panel.add(new Bracket(labelWidth,labelHeight,individual,upper));
				}
			}
			
			this.add(panel);

		}
	}
	

}

