/*
Authored by Matthew Orton

This class represents the main to go with the SequenceProcessGUI.java
*/
		
import javax.swing.JComponent;
import javax.swing.JFrame;

//Instantiate our Jpanel class and place in a JFrame
public class SequenceGUIMain {

	public static void main(String[] args) {
		
		javax.swing.SwingUtilities.invokeLater( new Runnable(){
				//Indicate that we want to call our SequenceGUI Method
				public void run(){
					SequenceGUI();
				}
		});
	}
	//Method to instantiate a JPanel for the Sequence Process Form GUI
	private static void SequenceGUI(){
		//New JFrame for the sequence process form GUI
		JFrame frame = new JFrame("Sequence Process Form");
		//set exit on close to JFrame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//New J component panel for the Sequence Process GUI
		JComponent Panel = new SequenceProcessGUI();
		//make pane visible 
		Panel.setOpaque(true);
		//associate the Panel with the JFrame
		frame.setContentPane(Panel);
		//Will actually show the sequence process form GUI itself
		frame.pack();
		frame.setVisible(true);
		
	}
}

