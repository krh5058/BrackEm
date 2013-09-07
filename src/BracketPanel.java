import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class BracketPanel extends JPanel implements ActionListener{

	/**
	 * 
	 */
	
	protected int panelWidth;
	protected int panelHeight;
	protected int labelWidth;
	protected int labelHeight;

	ArrayList<HashMap<Bracket, Integer>> hashList = new ArrayList<HashMap<Bracket, Integer>>();
	
	private static final long serialVersionUID = 1L;

	public BracketPanel(String arg){
		super();
		
		if (BrackEm.debug){
			System.out.println(arg + " created.");
		}
        
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
			System.out.println("BracketPanel (populateWinPanel): winPanel width: " + panelWidth);
			System.out.println("BracketPanel (populateWinPanel): winPanel height: " + panelHeight);
			System.out.println("BracketPanel (populateWinPanel): Rounds: " + BrackEm.bracketData.getTotalRoundsW());
			System.out.println("BracketPanel (populateWinPanel): Label width: " + labelWidth);
			System.out.println("BracketPanel (populateWinPanel): Start labels: " + startingLabels);
		}

		for(int i=0;i<=BrackEm.bracketData.getTotalRoundsW();i++) {  // Include final winner (<=)
			
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

			int numOfLabels = (int) (startingLabels/(Math.pow(2, i)));
			labelHeight = panelHeight/numOfLabels;
			
			if (BrackEm.debug){
				System.out.println("BracketPanel (populateWinPanel): Number of labels: " + numOfLabels);				
				System.out.println("BracketPanel (populateWinPanel): Label height: " + labelHeight);
			}

			boolean upper = true;
			boolean individual = false;
			if (i==BrackEm.bracketData.getTotalRoundsW()){ // Last panel is individual
				individual = true;
			}
			
			HashMap<Bracket, Integer> tempHashMap = new HashMap<Bracket, Integer>();
			
			for(int ii=1;ii<=numOfLabels;ii++) {

				if ( (ii % 2) == 0){ // Check even/odd for upper/lower
					upper = false;
				} else {
					upper = true;
				}
				
				if (i==0){ // First round Bys 
					if (ii<=BrackEm.bracketData.getFirstPlayersW()){ // Temporary placement formula (Top-most)

						Bracket bracket = new Bracket(labelWidth,labelHeight,individual,upper,ii);
						tempHashMap.put(bracket,ii);
						panel.add(bracket);
					} else {
						panel.add(Box.createRigidArea(new Dimension(0,labelHeight)));
					}
				} else {
					Bracket bracket = new Bracket(labelWidth,labelHeight,individual,upper,ii);
					tempHashMap.put(bracket,ii);
					panel.add(bracket);
				}
			}
			
			hashList.add(tempHashMap);
			this.add(panel);

		}
	}
	
	void populateLosePanel(){
		
		int i = 0;
		int startingLabels = 0;
		int [] numOfLabels = new int[BrackEm.bracketData.getTotalRoundsL()+1]; // Initialize and include final winner (+1)
		boolean [] byEntries = new boolean[BrackEm.bracketData.getTotalRoundsL()+1]; // Boolean if By entries or not
		
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        
		panelWidth = getWidth();
		panelHeight = getHeight();
		
		labelWidth = panelWidth/(BrackEm.bracketData.getTotalRoundsL()+1); // Include final winner (+1)
		
		// 1 or 2 Initial Rounds
		if (BrackEm.bracketData.getInitialRounds()==1){
			startingLabels = BrackEm.bracketData.getSecondPlayersL();	
			numOfLabels[i] = startingLabels;
			byEntries[i] = false;
			i++;
		} else if (BrackEm.bracketData.getInitialRounds()==2){
			startingLabels = 2*(BrackEm.bracketData.getSecondPlayersL());
			numOfLabels[i] = startingLabels;
			byEntries[i] = false;
			i++;
			numOfLabels[i] = BrackEm.bracketData.getSecondPlayersL();
			byEntries[i] = false;
			i++;
		}
		
		// 1/0 Incoming By Entries
		if (BrackEm.bracketData.getFirstIncoming()==1){
			numOfLabels[i] = numOfLabels[i-1];
			byEntries[i] = true;
			i++;
		}
		
		labelHeight = panelHeight/startingLabels; // All label heights (based on first round label heights)
		int smallestHeight = labelHeight; // Smallest label height, used for by entries and rigid areas
		
		if (BrackEm.debug){
			System.out.println("BracketPanel (populateLosePanel): losePanel width: " + panelWidth);
			System.out.println("BracketPanel (populateLosePanel): losePanel height: " + panelHeight);
			System.out.println("BracketPanel (populateLosePanel): Rounds: " + BrackEm.bracketData.getTotalRoundsL());
			System.out.println("BracketPanel (populateLosePanel): Label width: " + labelWidth);
			System.out.println("BracketPanel (populateLosePanel): Start labels: " + startingLabels);
		}
		
		// Remaining rounds - Format: (Non-By, By)*(addRoundsLessOne)
		while(i<=BrackEm.bracketData.getTotalRoundsL()) {  // Include final winner (<=)
			
			numOfLabels[i]=numOfLabels[i-1]/2;
			byEntries[i] = false;
			i++;
			if (i!=BrackEm.bracketData.getTotalRoundsL()+1){
				numOfLabels[i]=numOfLabels[i-1];
				byEntries[i] = true;
				i++;
			}
		}
		
		for(int j=0;j<numOfLabels.length;j++) {

			HashMap<Bracket, Integer> tempHashMap = new HashMap<Bracket, Integer>();

			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

			if (BrackEm.debug){
				System.out.println("BracketPanel (populateLosePanel): Number of labels: " + numOfLabels[j]);
			}

			boolean upper = true;
			boolean individual = true;
//			if (i==BrackEm.bracketData.getTotalRoundsW()){ // Last panel is individual
//				individual = true;
//			}
			
			for(int jj=1;jj<=numOfLabels[j];jj++) {
//
//				if ( (ii % 2) == 0){ // Check even/odd for upper/lower
//					upper = false;
//				} else {
//					upper = true;
//				}
				
//				if (BrackEm.bracketData.getInitialRounds()==2){ // Remove first round Bys if 2 initial rounds 
//					if (jj<=BrackEm.bracketData.getFirstPlayersL()){ // Temporary placement formula (Top-most)
//						Bracket bracket = new Bracket(labelWidth,smallestHeight,individual,upper,jj);
//						tempHashMap.put(bracket,jj);
//						panel.add(bracket);
//					} else {
//						panel.add(Box.createRigidArea(new Dimension(0,smallestHeight)));
//					}
//				} else { // If only 1 additional round, fill normally (Base2 first round)
//					Bracket bracket = new Bracket(labelWidth,smallestHeight,individual,upper,jj);
//					tempHashMap.put(bracket,jj);
//					panel.add(bracket);
//				}
//				
//				if (j==0){ // First round
//
//				} else if(j==1) { // Second round and non-by, fill normally
//					if(j==1 && byEntries[j] == false){ // Second round and non-by, fill normally
//						Bracket bracket = new Bracket(labelWidth,labelHeight,individual,upper,jj);
//						tempHashMap.put(bracket,jj);
//						panel.add(bracket);
//					}
//				}
			}
//			
			hashList.add(tempHashMap);
			this.add(panel);

		}
		
		
		
	}

}

