import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class BracketPanel extends JPanel implements ActionListener{

	/**
	 * 
	 */
	
	private JLayeredPane layeredPane = new JLayeredPane();
	
	private static final long serialVersionUID = 1L;

	public BracketPanel(String arg){
		super();
		if (BrackEm.debug){
			System.out.println(arg + " created.");

			JButton btn = new JButton(arg);
			btn.setActionCommand(arg);

			add(btn);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}


}

