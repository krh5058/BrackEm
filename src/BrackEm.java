import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class BrackEm extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @param args
	 */
	
	// General
	static BrackEm frame; // Static frame for easy reference
	protected Container cp;
	protected static CardLayout cardLayout;
	protected static JPanel cards; // Panel that uses CardLayout
	protected BracketPanel winPanel;
	protected BracketPanel losePanel; 
	protected BracketPanel finalPanel; 

	// Coordinate info
	static int fWidth;
	static int fHeight;
	
	// Create UI items
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem newMI;
	private JMenuItem printMI;
	private JMenuItem exitMI;
	private JMenu viewMenu;
	private JMenuItem winMI;
	private JMenuItem loseMI;
	private JMenuItem finalMI;
	
	public BrackEm(){
		super("BrackEm");
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		fWidth = screenSize.width;
		fHeight = screenSize.height;
		
		// Panel set-up
		cp=getContentPane();
		cp.setLayout(new BoxLayout(cp, BoxLayout.Y_AXIS));

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
        
        // Add "View"
        winMI = new JMenuItem("Winner's");
        winMI.setActionCommand("Winner's");
        winMI.setEnabled(false);
        winMI.addActionListener(this);
        
        loseMI = new JMenuItem("Loser's");
        loseMI.setActionCommand("Loser's");
        loseMI.setEnabled(false);
        loseMI.addActionListener(this);
        
        finalMI = new JMenuItem("Finals");
        finalMI.setActionCommand("Finals");
        finalMI.setEnabled(false);
        finalMI.addActionListener(this);
        
        viewMenu = new JMenu("View");
        viewMenu.add(winMI);
        viewMenu.add(loseMI);
        viewMenu.add(finalMI);
        
        // Add menu Bar
        menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        setJMenuBar(menuBar);
		
		// Frame initialization
		setResizable(false);
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		setAlwaysOnTop(true);
		setMinimumSize(new Dimension(fWidth,fHeight));
		setVisible( true );
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
//		void select(String event){
//			if (event == "mainMenu")
//			{  
//				RaceGame.cp.remove(RaceGame.cards);
//				cards = null;
//				trackPanel = null;
//				RaceGame.mapIndex = 0;
//				frame.setVisible( false ); // Clear old
//				frame.dispose(); // Clear old
//				String [] input = {"New"};
//				main(input); // Restart
//			}
        if (e.getActionCommand().equals("New"))//new game on the menu bar
        {
        	System.out.println("New");
        	
        	String result = JOptionPane.showInputDialog(frame,
        			"How many players?",
        			"Create a bracket",
        			JOptionPane.OK_CANCEL_OPTION
        			);
        	try {
        		System.out.println(Integer.parseInt(result));
        		
        		
        		genPanels();
        		
        	} catch(java.lang.NumberFormatException e1) { 
        		JOptionPane.showMessageDialog(frame,
        				"Invalid entry!",
        				"Create a bracket",
        				JOptionPane.OK_CANCEL_OPTION
        				);
        	}
        }
        
        if (e.getActionCommand().equals("Print"))//new game on the menu bar
        {
        	System.out.println("Print");
        }
        
        if (e.getActionCommand().equals("Exit"))//new game on the menu bar
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
        
        if (e.getActionCommand().equals("Winner's"))//new game on the menu bar
        {
        	System.out.println("Winner's");
        	
            BrackEm.cardLayout.show(BrackEm.cards, "WPanel");
        }
        
        if (e.getActionCommand().equals("Loser's"))//new game on the menu bar
        {
        	System.out.println("Loser's");
        	
        	BrackEm.cardLayout.show(BrackEm.cards, "LPanel");
        }
        
        if (e.getActionCommand().equals("Finals"))//new game on the menu bar
        {
        	System.out.println("Finals");
        	
        	BrackEm.cardLayout.show(BrackEm.cards, "FPanel");
        }
	}
	
	private void genPanels(){
		winPanel = new BracketPanel("WPanel");
		losePanel = new BracketPanel("LPanel");
		finalPanel = new BracketPanel("FPanel");
		cardLayout = new CardLayout();
		cards = new JPanel(cardLayout);
        cards.add(winPanel, "WPanel");
        cards.add(losePanel, "LPanel");
        cards.add(finalPanel, "FPanel");
        cp.add(cards);
        
        winMI.setEnabled(true);
        loseMI.setEnabled(true);
        finalMI.setEnabled(true);
        
        BrackEm.cardLayout.show(BrackEm.cards, "LPanel");
        BrackEm.cardLayout.show(BrackEm.cards, "WPanel");
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BrackEm frame = new BrackEm();
		BrackEm.frame = frame;
	}

}
