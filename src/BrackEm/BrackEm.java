package BrackEm;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

public class BrackEm extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @param args
	 */

	// General
	static boolean debug = true;
	static BrackEm frame; // Static frame for easy reference
	protected JTabbedPane tabbedPane;
	static protected BracketPanel winPanel;
	static protected BracketPanel losePanel; 
	static protected BracketPanel finalPanel;
	protected boolean panelExist = false;

	// Data
	static BracketCalc bracketData;

	// Coordinate info
	static int fWidth;
	static int fHeight;

	// Create UI items
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem newMI;
	private JMenuItem printMI;
	private JMenuItem exitMI;
	private JMenu aboutMenu;
	private JMenuItem helpMI;
	private JLabel splashLabel;

	public BrackEm(){
		super("BrackEm");

		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		fWidth = screenSize.width;
		fHeight = screenSize.height;

		// Add "File"
		newMI = new JMenuItem("New");
		newMI.setActionCommand("New");
		newMI.addActionListener(this);

		printMI = new JMenuItem("Save");
		printMI.setActionCommand("Save");
		printMI.setEnabled(false);
		printMI.addActionListener(this);

		exitMI = new JMenuItem("Exit");
		exitMI.setActionCommand("Exit");
		exitMI.addActionListener(this);

		fileMenu = new JMenu("File");
		fileMenu.add(newMI);
		fileMenu.add(printMI);
		fileMenu.add(exitMI);

		helpMI = new JMenuItem("Help");
		helpMI.setActionCommand("Help");
		helpMI.addActionListener(this);

		aboutMenu = new JMenu("About");        
		aboutMenu.add(helpMI);

		// Add menu Bar
		menuBar = new JMenuBar();
		menuBar.add(fileMenu);
		menuBar.add(aboutMenu);

		if (debug){
			JMenu debugMenu = new JMenu("Debug");
			JMenuItem debugMI = new JMenuItem("Debug");
			debugMI.setActionCommand("Debug");
			debugMI.addActionListener(this);
			debugMenu.add(debugMI);
			menuBar.add(debugMenu,-1);
		}

		setJMenuBar(menuBar);

		// Splash
		splashLabel = new JLabel(createImageIcon("images/splash.jpg"));
		add(splashLabel);
		
//		Image icon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB_PRE);
		ImageIcon icon = createImageIcon("images/bracket_icon.jpg");
		setIconImage(icon.getImage());
		
		// Frame initialization
		setResizable(true);
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		setAlwaysOnTop(false);
		setMinimumSize(new Dimension(fWidth,fHeight));
		setVisible( true );
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("New"))
		{
			if (debug){
				System.out.println("BrackEm (actionPerformed): New");
			}

			int startnew = JOptionPane.YES_OPTION;

			if (panelExist){
				// Prompt
				startnew = JOptionPane.showConfirmDialog(
						frame,
						"A current bracket is open.  Start over?",
						"New bracket",
						JOptionPane.YES_NO_OPTION);
			}

			if (startnew == JOptionPane.YES_OPTION){

				String result = JOptionPane.showInputDialog(frame,
						"How many players?",
						"Create a bracket",
						JOptionPane.OK_CANCEL_OPTION
						);

				if (result != null) {
					try {
						if (Integer.parseInt(result) < 3 || Integer.parseInt(result) > 32 ){
							invalidNumDiag();
						} else {
							int totalPlayers = Integer.parseInt(result);

							bracketData = new BracketCalc(totalPlayers);

							genPanels();
							popPanels();
							bracketData.loserPlacementCalc();
									
						}

					} catch(java.lang.NumberFormatException e1) { 
						invalidDiag();
					}
				}
			}
		}

		if (e.getActionCommand().equals("Save"))
		{
			if (debug){
				System.out.println("BrackEm (actionPerformed): Save");
			}

			BufferedImage winImg = ScreenImage.createImage(winPanel);
			BufferedImage loseImg = ScreenImage.createImage(losePanel);
			BufferedImage finalImg = ScreenImage.createImage(finalPanel);
			try {
					JFileChooser fc = new JFileChooser();
				    
				    fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				    fc.setDialogTitle("Save Brackets");
					fc.setApproveButtonText("Save Brackets");
					fc.setAcceptAllFileFilterUsed(false);
					int returnVal = fc.showOpenDialog(this); 
					if (returnVal == JFileChooser.APPROVE_OPTION) { 
						String filePath = fc.getSelectedFile().getPath();

						File winfile = new File(filePath + "/Winners.png");
						File losefile = new File(filePath + "/Losers.png");
						File finalfile = new File(filePath + "/Finals.png");
						ImageIO.write(winImg, "png", winfile);
						ImageIO.write(loseImg, "png", losefile);
						ImageIO.write(finalImg, "png", finalfile);
					}
			} catch (IOException e1) {
			}
		}
		if (e.getActionCommand().equals("Exit"))
		{
			// Prompt
			int result = JOptionPane.showConfirmDialog(
					frame,
					"Are you sure you want to exit the application?",
					"Exit Application",
					JOptionPane.YES_NO_OPTION);

			if (result == JOptionPane.YES_OPTION){
				frame.setVisible( false ); // Clear old
				frame.dispose(); // Clear old
			}
		}

		if (e.getActionCommand().equals("Debug"))//Debug on the menu bar
		{
			System.out.println("BrackEm (actionPerformed): Debug-------------");
			System.out.println("BrackEm (actionPerformed): Debug----------------");
			System.out.println("BrackEm (actionPerformed): Debug-------------------");
			System.out.println("BrackEm (actionPerformed): Winner's-------------");
			String search = "Bob";
			String replace = "Bill";
			for (HashMap<Bracket, Integer> map : winPanel.hashList)
				for (Entry<Bracket, Integer> mapEntry : map.entrySet())
				{
					Bracket key = mapEntry.getKey();
					Integer value = mapEntry.getValue();
					int placement = key.getPlacement();
					byte nextplacement =  (byte) ((byte)(placement - 1) >> 1);
					String name = key.getID();
					System.out.println("BrackEm (actionPerformed): HashMap Value: " + value);
					//        			System.out.println("Converted Integer: " + Integer.toBinaryString(value));
					System.out.println("BrackEm (actionPerformed): getPlacement(): " + placement);
					System.out.println("BrackEm (actionPerformed): getID(): " + name);
					System.out.println("BrackEm (actionPerformed): Next round placement (Win): " + (1+nextplacement)); // Convert so "1" starts index

					if (key.getID().equals(search)){
						System.out.println("BrackEm (actionPerformed): Match*******");
						key.setID(replace);
					}
				}
			System.out.println("BrackEm (actionPerformed): Debug-------------");
			System.out.println("BrackEm (actionPerformed): Debug----------------");
			System.out.println("BrackEm (actionPerformed): Debug-------------------");
			System.out.println("BrackEm (actionPerformed): Loser's-------------");
//			String search = "Bob";
//			String replace = "Bill";
			for (HashMap<Bracket, Integer> map : losePanel.hashList)
				for (Entry<Bracket, Integer> mapEntry : map.entrySet())
				{
					Bracket key = mapEntry.getKey();
					Integer value = mapEntry.getValue();
					int placement = key.getPlacement();
					byte nextplacement =  (byte) ((byte)(placement - 1) >> 1);
					String name = key.getID();
					System.out.println("BrackEm (actionPerformed): HashMap Value: " + value);
					//        			System.out.println("Converted Integer: " + Integer.toBinaryString(value));
					System.out.println("BrackEm (actionPerformed): getPlacement(): " + placement);
					System.out.println("BrackEm (actionPerformed): getID(): " + name);
					System.out.println("BrackEm (actionPerformed): Next round placement (Win): " + (1+nextplacement)); // Convert so "1" starts index

					if (key.getID().equals(search)){
						System.out.println("BrackEm (actionPerformed): Match*******");
						key.setID(replace);
					}
				}
		}

		if (e.getActionCommand().equals("Help"))
		{
			if (debug){
				System.out.println("About");
			}

			String infoString = "v1.0\nLast updated Sept. 15th, 2013\nAuthor: Ken Hwang\nDesign: Shane Walker\nDouble elimination bracket generator";
			JOptionPane.showMessageDialog(frame,infoString,"About BrackEm",JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private void genPanels(){

		// Content pane set-up
		tabbedPane = new JTabbedPane();
		winPanel = new BracketPanel("Winners");
		losePanel = new BracketPanel("Losers");
		finalPanel = new BracketPanel("Finals");
		tabbedPane.addTab("Winners Bracket", winPanel);
		tabbedPane.addTab("Losers Bracket", losePanel);
		tabbedPane.addTab("Finals Bracket", finalPanel);
		
		frame.setContentPane(tabbedPane);
		refreshFrame();

	}

	private void popPanels(){

		winPanel.populateWinPanel();
		losePanel.populateLosePanel();
		finalPanel.populateFinalPanel();
		printMI.setEnabled(true);
		panelExist = true;
	}

	private void refreshFrame(){
		// Frame initialization
		frame.setResizable(true);
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.setAlwaysOnTop(false);
		frame.setMinimumSize(new Dimension(fWidth,fHeight));
		frame.setVisible( true );
	}

	private void invalidDiag(){
		JOptionPane.showMessageDialog(frame,
				"Invalid entry!",
				"Create a bracket",
				JOptionPane.OK_CANCEL_OPTION
				);
	}

	private void invalidNumDiag(){
		JOptionPane.showMessageDialog(frame,
				"Only 3 to 32 players are allowed.",
				"Create a bracket",
				JOptionPane.OK_CANCEL_OPTION
				);
	}

	protected ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = getClass().getClassLoader().getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	protected static void bracketUpdate(int round, int placement, String name){
		if (debug){
			System.out.println("BrackEm (bracketUpdate): Bracket changed -- Round: " + round + ", Placement: " + placement + ", Name: " + name);
		}
		
//		HashMap<Bracket, Integer> searchList = null; // The HashMap for the specified round
//		int effectedPanels;
		
		switch (name){
		case "Winners":
//			searchList = winPanel.hashList.get(round);
			
			int[] samePanelEffectedBrackets = bracketData.getSamePanelEffectedBrackets(placement);
			int[] loserPanelEffectedBracketAndRound = bracketData.getLoserPanelEffectedRounds(placement, placementToValue(winPanel.hashList.get(round),placement),round);

			if (debug){
				System.out.println("BrackEm (bracketUpdate): Paired bracket effected: " + samePanelEffectedBrackets[0]);
				System.out.println("BrackEm (bracketUpdate): Next round bracket effected: " + samePanelEffectedBrackets[1]);
				System.out.println("BrackEm (bracketUpdate): Loser panel round effected: " + loserPanelEffectedBracketAndRound[0]);
				System.out.println("BrackEm (bracketUpdate): Loser panel bracket effected: " + loserPanelEffectedBracketAndRound[1]);
			}
			
			if (round==(bracketData.getTotalRoundsW()-1)){ // Include finals
				// TODO Include final panel updating
			}
			
			break;
		case "Losers":
//			int[] samePlanelEffectedBrackets = bracketData.getSamePanelEffectedBrackets(placement);
			if (round==(bracketData.getTotalRoundsL()-1)){ // Include finals
				// TODO Include final panel updating
			}
			break;
		case "Finals":
//			int[] samePanelEffectedBrackets = bracketData.getSamePanelEffectedBrackets(placement);
			break;
		}
		
//		for (Entry<Bracket, Integer> entry: searchList.entrySet())
//		{
//			Bracket key = mapEntry.getKey();
//			Integer value = mapEntry.getValue();
//			int entryPlacement = ((Bracket) entry.getKey()).getPlacement();
//			byte nextplacement =  (byte) ((byte)(placement - 1) >> 1);
//			String name = key.getID();
//			boolean decided = key.getDecided();
//			System.out.println("BrackEm (actionPerformed): HashMap Value: " + value);
			//        			System.out.println("Converted Integer: " + Integer.toBinaryString(value));
//			System.out.println("BrackEm (actionPerformed): getPlacement(): " + placement);
//			System.out.println("BrackEm (actionPerformed): getID(): " + name);
//			System.out.println("BrackEm (actionPerformed): getDecided(): " + decided);
//			System.out.println("BrackEm (actionPerformed): Next round placement (Win): " + (1+nextplacement)); // Convert so "1" starts index

//			if (decided){
//				System.out.println("BrackEm (actionPerformed): ********Decided*******");
//			}
			
//			if (entryPlacement==placement){
//				System.out.println("BrackEm (actionPerformed): getPlacement() matched: " + placement);
//				System.out.println("BrackEm (actionPerformed): HashMap Value "  + entry.getValue());
//				break;
//			}
			
//		}

//		bracketData.getEffectedBrackets();
		
//		for (HashMap<Bracket, Integer> map : winPanel.hashList)
//			for (Entry<Bracket, Integer> mapEntry : map.entrySet())
//			{
//				Bracket key = mapEntry.getKey();
//				Integer value = mapEntry.getValue();
//				int placement = key.getPlacement();
//				byte nextplacement =  (byte) ((byte)(placement - 1) >> 1);
//				String name = key.getID();
//				boolean decided = key.getDecided();
//				System.out.println("BrackEm (actionPerformed): HashMap Value: " + value);
//				//        			System.out.println("Converted Integer: " + Integer.toBinaryString(value));
//				System.out.println("BrackEm (actionPerformed): getPlacement(): " + placement);
//				System.out.println("BrackEm (actionPerformed): getID(): " + name);
//				System.out.println("BrackEm (actionPerformed): getDecided(): " + decided);
//				System.out.println("BrackEm (actionPerformed): Next round placement (Win): " + (1+nextplacement)); // Convert so "1" starts index
//
//				if (decided){
//					System.out.println("BrackEm (actionPerformed): ********Decided*******");
//				}
//			}
	}
	
	static int placementToValue(HashMap<Bracket, Integer> hashMap, int placement){
		
		int out = 0;
		
		for (Entry<Bracket, Integer> entry: hashMap.entrySet())
		{
			Bracket key = entry.getKey();
			Integer value = entry.getValue();
			int entryPlacement = key.getPlacement();
			
			if (entryPlacement==placement){
				System.out.println("BrackEm (placementToValue): getPlacement() matched: " + placement);
				System.out.println("BrackEm (placementToValue): HashMap Value: "  + value);
				out = value;
				break;
			}
			
		}
		
		return out;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BrackEm frame = new BrackEm();
		BrackEm.frame = frame;
	}

}
