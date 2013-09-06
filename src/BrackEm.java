import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.JFrame;
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
	protected static Container cp;
	protected JTabbedPane tabbedPane;
	protected BracketPanel winPanel;
	protected BracketPanel losePanel; 
	protected BracketPanel finalPanel;
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
        
        printMI = new JMenuItem("Print");
        printMI.setActionCommand("Print");
        printMI.addActionListener(this);
        
        exitMI = new JMenuItem("Exit");
        exitMI.setActionCommand("Exit");
        exitMI.addActionListener(this);
        
        fileMenu = new JMenu("File");
        fileMenu.add(newMI);
        fileMenu.add(printMI);
        fileMenu.add(exitMI);
        
        

        // Add menu Bar
        menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        
    	if (debug){
    		JMenu debugMenu = new JMenu("Debug");
            JMenuItem debugMI = new JMenuItem("Debug");
            debugMI.setActionCommand("Debug");
            debugMI.addActionListener(this);
            debugMenu.add(debugMI);
            menuBar.add(debugMenu,-1);
    	}
        
        setJMenuBar(menuBar);

        // Content pane set-up
		cp=getContentPane();
		genPanels();
		
		// Frame initialization
		setResizable(false);
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		setAlwaysOnTop(true);
		setMinimumSize(new Dimension(fWidth,fHeight));
		setVisible( true );
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("New"))
        {
        	if (debug){
        		System.out.println("New");
        	}
        	
        	String result = JOptionPane.showInputDialog(frame,
        			"How many players?",
        			"Create a bracket",
        			JOptionPane.OK_CANCEL_OPTION
        			);
        	
        	if (result != null) {
        		try {
        			if (Integer.parseInt(result) < 3 || Integer.parseInt(result) > 31 ){
        				invalidNumDiag();
        			} else {
            			int totalPlayers = Integer.parseInt(result);
            			
            			bracketData = new BracketCalc(totalPlayers);
            			
        				popPanels();
        			}

        		} catch(java.lang.NumberFormatException e1) { 
        			invalidDiag();
        		}
        	}
        }
        
        if (e.getActionCommand().equals("Print"))
        {
        	if (debug){
        		System.out.println("Print");
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
        	System.out.println("Debug-------------");
        	System.out.println("Debug----------------");
        	System.out.println("Debug-------------------");
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
        			System.out.println("Integer: " + value);
//        			System.out.println("Converted Integer: " + Integer.toBinaryString(value));
        			System.out.println("getPlacement(): " + placement);
        			System.out.println("getID(): " + name);
        			System.out.println("Next round placement (Win): " + (1+nextplacement)); // Convert so "1" starts index
        			
        			if (key.getID().equals(search)){
        				System.out.println("Match*******");
        				key.setID(replace);
        			}
        		}
        }

        //        if (e.getActionCommand().equals("Loser's"))
//        {
//        	if (debug){
//        		System.out.println("Loser's");
//        	}
//        	
//        	BrackEm.cardLayout.show(BrackEm.cards, "LPanel");
//        }
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
		
		cp.add(tabbedPane, BorderLayout.CENTER);
		
	}
		
	private void popPanels(){
		if (panelExist){
			BrackEm.cp.remove(tabbedPane);
			winPanel = null;
			losePanel = null;
			finalPanel = null;
			genPanels();
			refreshFrame();
		}

		winPanel.populateWinPanel();
		panelExist = true;
	}
	
	private void refreshFrame(){
		// Frame initialization
		frame.setResizable(false);
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.setAlwaysOnTop(true);
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
				"Only 3 to 31 players are allowed.",
				"Create a bracket",
				JOptionPane.OK_CANCEL_OPTION
				);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BrackEm frame = new BrackEm();
		BrackEm.frame = frame;
	}

}
