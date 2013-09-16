package BrackEm;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class BracketPanel extends JPanel{

	/**
	 * 
	 */
	
	protected int panelWidth;
	protected int panelHeight;
	protected int labelWidth;
	protected int labelHeight;
	protected int smallestHeight;
	protected int fontSize;
	protected String panelName;

	ArrayList<HashMap<Bracket, Integer>> hashList = new ArrayList<HashMap<Bracket, Integer>>();
	
	private static final long serialVersionUID = 1L;

	public BracketPanel(String arg){
		super();
		
		setName(arg);
		
		if (BrackEm.debug){
			System.out.println(arg + " created.");
		}
        
	}
	
	public void setName(String name){
		panelName = name;
	}
	
	public String getName(){
		return panelName;
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
			
			for(int ii=1;ii<=numOfLabels;ii++) { // Placement index, Where the label is placed on the panel (vs. Mapping index)

				if ( (ii % 2) == 0){ // Check even/odd for upper/lower
					upper = false;
				} else {
					upper = true;
				}
				
				if (i==0){ // First round placement 
					if (BrackEm.bracketData.getFirstRoundByW()!=0) { // First round Bys
						int[] mapVals = BracketCalc.getFirstOrder(numOfLabels); // Ordered values to enter in hashmap
						if (mapVals[ii-1]<=BrackEm.bracketData.getFirstPlayersW()){ // Placement value is less than the number of first round players
							//						Bracket bracket = new Bracket(labelWidth,labelHeight,individual,upper,mapVals[ii-1]);
							Bracket bracket = new Bracket(labelWidth,labelHeight,individual,upper,ii,i); // Retain placement index
							bracket.setEditable(true);
							tempHashMap.put(bracket,mapVals[ii-1]); // Follow mapping index
							panel.add(bracket);
						} else {
							panel.add(Box.createRigidArea(new Dimension(0,labelHeight)));
						} 
					} else {
						Bracket bracket = new Bracket(labelWidth,labelHeight,individual,upper,ii,i);
						bracket.setEditable(true);
						tempHashMap.put(bracket,ii);
						panel.add(bracket);
					}
				} else {
					Bracket bracket = new Bracket(labelWidth,labelHeight,individual,upper,ii,i);
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
		smallestHeight = labelHeight; // Smallest label height, used for by entries and rigid areas
		
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
		
		if (BrackEm.debug){
			System.out.println("BracketPanel (populateLosePanel): Initial rounds (1/2): " + BrackEm.bracketData.getInitialRounds());
			System.out.println("BracketPanel (populateLosePanel): Incoming losers (0/1): " + BrackEm.bracketData.getFirstIncoming());
			System.out.println("BracketPanel (populateLosePanel): Additional rounds multiplier (1/2): " + BrackEm.bracketData.getAddRoundMultiplier());
		}
		
		// Panel iteration schematic
		int [] iterationSchematic = null;
		if (BrackEm.bracketData.getInitialRounds()==1){
			if (BrackEm.bracketData.getAddRoundMultiplier()==1){ // 3 rounds?, [1 0 1]
				iterationSchematic = getIterationSchematic(1, numOfLabels.length);
			} else if (BrackEm.bracketData.getAddRoundMultiplier()==2){ // Loser's base2, [1 0 2]
				iterationSchematic = getIterationSchematic(2, numOfLabels.length);
			}
		} else if (BrackEm.bracketData.getInitialRounds()==2){ 
			if (BrackEm.bracketData.getFirstIncoming()==1){ // Assume multiplier is 2, [2 1 2]
				iterationSchematic = getIterationSchematic(3, numOfLabels.length);
			} else if (BrackEm.bracketData.getFirstIncoming()==0){
				if(BrackEm.bracketData.getAddRoundMultiplier()==1){ // 4 rounds?, [2 0 1]
					iterationSchematic = getIterationSchematic(4, numOfLabels.length);
				} else if (BrackEm.bracketData.getAddRoundMultiplier()==2){ // [2 0 2]
					iterationSchematic = getIterationSchematic(5, numOfLabels.length);
				}
			}
		}
		
		for(int j=0;j<numOfLabels.length;j++) {
			
			if (BrackEm.debug){
				System.out.println("BracketPanel (populateLosePanel): Number of labels: " + numOfLabels[j]);
			}
			
			JPanel jPanel = new JPanel();
			
			switch (iterationSchematic[j]){
			case 1: // Normal
				if (BrackEm.debug){
					System.out.println("BracketPanel (populateLosePanel): Normal schematic");
				}
				jPanel = normalLosePanel(numOfLabels[j],j);
				this.add(jPanel);
				break;
			case 2: // Limited
				if (BrackEm.debug){
					System.out.println("BracketPanel (populateLosePanel): Limited schematic");
				}
				jPanel = limitedLosePanel(numOfLabels[j],j);
				this.add(jPanel);
				break;
			case 3: // By
				if (BrackEm.debug){
					System.out.println("BracketPanel (populateLosePanel): By schematic");
				}
				jPanel = byLosePanel(numOfLabels[j],j);
				this.add(jPanel);
				break;
			case 4: // Non-by

				if (j==numOfLabels.length-1){
					if (BrackEm.debug){
						System.out.println("BracketPanel (populateLosePanel): Final Non-By schematic");
					}

					jPanel = nonByLosePanel(numOfLabels[j],j,true);
					this.add(jPanel);
				} else {
					if (BrackEm.debug){
						System.out.println("BracketPanel (populateLosePanel): Non-By schematic");
					}

					jPanel = nonByLosePanel(numOfLabels[j],j,false);
					this.add(jPanel);
				}
				
				break;
			}

		}

	}	
	
	void populateFinalPanel(){
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        
		panelWidth = getWidth();
		panelHeight = getHeight();
		
		int startingLabels = 3;
		
		labelWidth = panelWidth/startingLabels; 
		labelHeight = panelHeight/3;
		
		if (BrackEm.debug){
			System.out.println("BracketPanel (populateFinalPanel): finalPanel width: " + panelWidth);
			System.out.println("BracketPanel (populateFinalPanel): finalPanel height: " + panelHeight);
			System.out.println("BracketPanel (populateFinalPanel): Rounds: " + 3);
			System.out.println("BracketPanel (populateFinalPanel): Label width: " + labelWidth);
			System.out.println("BracketPanel (populateFinalPanel): Start labels: " + startingLabels);
		}
		
		HashMap<Bracket, Integer> tempHashMap1 = new HashMap<Bracket, Integer>();
		
		JPanel panel1 = new JPanel();
		panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));

		Bracket bracket1_1 = new Bracket(labelWidth,labelHeight,false,true,1,0);
		tempHashMap1.put(bracket1_1,1);
		panel1.add(bracket1_1);
		
		Bracket bracket1_2 = new Bracket(labelWidth,labelHeight,false,false,2,0);
		tempHashMap1.put(bracket1_2,2);
		panel1.add(bracket1_2);

		panel1.add(Box.createRigidArea(new Dimension(0,labelHeight)));
		hashList.add(tempHashMap1);
		this.add(panel1);

		HashMap<Bracket, Integer> tempHashMap2 = new HashMap<Bracket, Integer>();
		
		JPanel panel2 = new JPanel();
		panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));		
		
		Bracket bracket2_1 = new Bracket(labelWidth,labelHeight*2,false,true,1,1);
		tempHashMap2.put(bracket2_1,1);
		panel2.add(bracket2_1);
		
		Bracket bracket2_2 = new Bracket(labelWidth,labelHeight,false,false,2,1);
		tempHashMap2.put(bracket2_2,2);
		panel2.add(bracket2_2);
		
		hashList.add(tempHashMap2);
		this.add(panel2);
		
		HashMap<Bracket, Integer> tempHashMap3 = new HashMap<Bracket, Integer>();
		
		JPanel panel3 = new JPanel();
		panel3.setLayout(new BoxLayout(panel3, BoxLayout.Y_AXIS));		
		
		Bracket bracket3_1 = new Bracket(labelWidth,panelHeight,true,true,1,2);
		tempHashMap2.put(bracket3_1,1);
		panel3.add(bracket3_1);
		
		hashList.add(tempHashMap3);
		this.add(panel3);
	}
	
	int[] getIterationSchematic(int format, int numOfRounds){
		int [] iterationSchematic = new int [numOfRounds];
		// format:
		// 1 = [1 0 1]
		// 2 = [1 0 2]
		// 3 = [2 1 2]
		// 4 = [2 0 1]
		// 5 = [2 0 2]
		
		// Panel command:
		// 1 = Normal
		// 2 = Limited
		// 3 = By
		// 4 = Non-By
		
		switch (format){
		case 1:
			for(int i1=0;i1<numOfRounds;i1++){
				iterationSchematic[i1] = 1;
			}
			break;
		case 2:
			int i2 = 0;
			while(i2<numOfRounds){
				if (i2==0|i2==1){
					iterationSchematic[i2] = 1;
				} else {
					if ( (i2 % 2) == 0){ // If even
						iterationSchematic[i2] = 3;
					} else {
						iterationSchematic[i2] = 4;
					}
				}
				i2++;
			}
			break;
		case 3:
			int i3 = 0;
			while(i3<numOfRounds){
				if (i3==0){
					iterationSchematic[i3] = 2;
				} else if(i3==1){
					iterationSchematic[i3] = 1;
				} else {
					if ( (i3 % 2) == 0){ // If even
						iterationSchematic[i3] = 3;
					} else {
						iterationSchematic[i3] = 4;
					}
				}
				i3++;
			}
			break;
		case 4:
			int i4 = 0;
			while(i4<numOfRounds){
				if (i4==0){
					iterationSchematic[i4] = 2;
				} else {
					iterationSchematic[i4] = 1;
				}
				i4++;
			}
			break;
		case 5:
			int i5 = 0;
			while(i5<numOfRounds){
				if (i5==0){
					iterationSchematic[i5] = 2;
				} else if(i5==1|i5==2){
					iterationSchematic[i5] = 1;
				} else {
					if ( (i5 % 2) == 0){ // If even
						iterationSchematic[i5] = 4;
					} else {
						iterationSchematic[i5] = 3;
					}
				}
				i5++;
			}
			break;
		}

		return iterationSchematic;
	}
	
	int getNormalLabelHeight(int numOfLabels){
		int normLabelHeight = panelHeight/numOfLabels;
		if (BrackEm.debug){
			System.out.println("BracketPanel (populateLosePanel): Normal Label height: " + normLabelHeight);
		}
		
		return normLabelHeight;
	}
	
	int getSmallestLabelHeight(){
		if (BrackEm.debug){
			System.out.println("BracketPanel (populateLosePanel): Smallest Label height: " + smallestHeight);
		}
		return smallestHeight;
	}
	
	int getByLabelHeight(int numOfRounds){
		int numOfSmallest = numOfRounds/2;
		int byLabelHeight = (panelHeight-(numOfSmallest*smallestHeight))/numOfSmallest;
		
		if (BrackEm.debug){
			System.out.println("BracketPanel (populateLosePanel): By Label height: " + byLabelHeight);
		}
		
		return byLabelHeight;
	}
	
	JPanel normalLosePanel(int numOfLabels,int round){
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		HashMap<Bracket, Integer> tempHashMap = new HashMap<Bracket, Integer>();

		boolean upper = true;
		boolean individual = false;

		int currentHeight = getNormalLabelHeight(numOfLabels);
		
		for(int n=1;n<=numOfLabels;n++) {
			if ( (n % 2) == 0){ // Check even/odd for upper/lower
				upper = false;
			} else {
				upper = true;
			}

			Bracket bracket = new Bracket(labelWidth,currentHeight,individual,upper,n,round);
			
			if (hashList.isEmpty()){ // If first panel
				bracket.setState(false);
				if (BrackEm.debug){
					System.out.println("BracketPanel (normalLosePanel): Bracket placed at " + bracket.getPlacement() + ", state set to false");
				}
			} else { // Otherwise
				boolean breakFlag = false;
				int placement = bracket.getPlacement();
				HashMap<Bracket, Integer> lastHashMap = hashList.get(hashList.size()-1);
				for (Entry<Bracket, Integer> mapEntry : lastHashMap.entrySet()){
					Bracket key = mapEntry.getKey();
					int keyPlacement = key.getPlacement();
					int nextPlacement = ((byte)(keyPlacement - 1) >> 1);
					if (nextPlacement==(placement-1)){
						breakFlag = true;
						break;
					}
				}
				
				if (breakFlag==false){ // If no previous brackets were found, then set state to false
					bracket.setState(false);
					if (BrackEm.debug){
						System.out.println("BracketPanel (normalLosePanel): Bracket placed at " + placement + ", state set to false");
					}
				}
			}
			
			tempHashMap.put(bracket,n);
			panel.add(bracket);

		}
		
		hashList.add(tempHashMap);
	
		return panel;
		
	}
	
	JPanel limitedLosePanel(int numOfLabels,int round){
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		HashMap<Bracket, Integer> tempHashMap = new HashMap<Bracket, Integer>();

		boolean upper = true;
		boolean individual = false;
		int currentHeight = getSmallestLabelHeight();
		
		int[] mapVals = BracketCalc.getFirstOrder(numOfLabels); // Ordered values to enter in hashmap
		
		for(int n=1;n<=numOfLabels;n++) {

			if ( (n % 2) == 0){ // Check even/odd for upper/lower
				upper = false;
			} else {
				upper = true;
			}
			
			if (mapVals[n-1]<=BrackEm.bracketData.getFirstPlayersL()){
				Bracket bracket = new Bracket(labelWidth,currentHeight,individual,upper,n,round); // Retain placement index
				bracket.setState(false);
				if (BrackEm.debug){
					System.out.println("BracketPanel (limitedLosePanel): Bracket placed at " + bracket.getPlacement() + ", state set to false");
				}
				tempHashMap.put(bracket,mapVals[n-1]); // Use map index
				panel.add(bracket);
			} else {
				panel.add(Box.createRigidArea(new Dimension(0,currentHeight)));
			}

		}
		
		hashList.add(tempHashMap);
		
		return panel;
		
	}
	
	JPanel byLosePanel(int numOfLabels,int round){
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		HashMap<Bracket, Integer> tempHashMap = new HashMap<Bracket, Integer>();

		boolean upper = true;
		boolean individual = false;
		int currentHeight1 = getByLabelHeight(numOfLabels);
		int currentHeight2 = getSmallestLabelHeight();

		for(int n=1;n<=numOfLabels;n++) {
			int n2 = (int) Math.ceil(((double)n/2));
			if (n2 % 2 == 0) { // if ceil(half) even
				if ( (n % 2) == 0){ // If even
					upper = false;
					Bracket bracket = new Bracket(labelWidth,currentHeight2,individual,upper,n,round);
					bracket.setState(false);
					if (BrackEm.debug){
						System.out.println("BracketPanel (byLosePanel): Bracket placed at " + bracket.getPlacement() + ", state set to false");
					}
					tempHashMap.put(bracket,n);
					panel.add(bracket);
				} else {
					upper = true;
					Bracket bracket = new Bracket(labelWidth,currentHeight1,individual,upper,n,round);
					bracket.setState(true);
					tempHashMap.put(bracket,n);
					panel.add(bracket);
				}
			} else {
				if ( (n % 2) == 0){ // If even
					upper = false;
					Bracket bracket = new Bracket(labelWidth,currentHeight1,individual,upper,n,round);
					tempHashMap.put(bracket,n);
					bracket.setState(true);
					panel.add(bracket);
				} else {
					upper = true;
					Bracket bracket = new Bracket(labelWidth,currentHeight2,individual,upper,n,round);
					bracket.setState(false);
					if (BrackEm.debug){
						System.out.println("BracketPanel (byLosePanel): Bracket placed at " + bracket.getPlacement() + ", state set to false");
					}
					tempHashMap.put(bracket,n);
					panel.add(bracket);
				}
			}
		}
		
		hashList.add(tempHashMap);
	
		return panel;
		
	}
	
	JPanel nonByLosePanel(int numOfLabels, int round, boolean last){
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		HashMap<Bracket, Integer> tempHashMap = new HashMap<Bracket, Integer>();

		boolean upper = true;
		boolean individual;
		if(last){
			individual = true;
		} else {
			individual = false;
		}
		
		int lastHeight = getByLabelHeight(numOfLabels*2);
		int currentHeight1 = lastHeight/2;
		int currentHeight2 = getSmallestLabelHeight();
		
		for(int n=1;n<=numOfLabels;n++) {
			
			if ( (n % 2) == 0){ // If even
				upper = false;
				Bracket bracket = new Bracket(labelWidth,currentHeight1,individual,upper,n,round);
				tempHashMap.put(bracket,n);
				panel.add(bracket);

				panel.add(Box.createRigidArea(new Dimension(0,currentHeight2)));
			} else {
				upper = true;
				panel.add(Box.createRigidArea(new Dimension(0,currentHeight2)));

				Bracket bracket = new Bracket(labelWidth,currentHeight1,individual,upper,n,round);
				tempHashMap.put(bracket,n);
				panel.add(bracket);

				if (last) {
					panel.add(Box.createRigidArea(new Dimension(0,lastHeight)));
				} else {
					panel.add(new BlankLineLabel(lastHeight));
				}
			}
		}
		
		hashList.add(tempHashMap);
	
		return panel;
		
	}
	
	class BlankLineLabel extends JLabel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		int lineLabelHeight;
		
		BlankLineLabel(int y) {
			super(); 
			lineLabelHeight = y;
			
			setMaximumSize(new Dimension(labelWidth,lineLabelHeight));
			setPreferredSize(new Dimension(labelWidth,lineLabelHeight));
			setMinimumSize(new Dimension(labelWidth,lineLabelHeight));
		}
		
		public void paintComponent(Graphics g){
		    super.paintComponent(g);
		    Graphics2D g2 = (Graphics2D) g;
		    g2.setStroke(new BasicStroke(7)); // Why larger than 3?
		    g2.drawLine(getWidth(),0,getWidth(),lineLabelHeight); // Line on right side
		}
	}
	
	
}

