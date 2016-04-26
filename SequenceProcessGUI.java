/*
Authored by Matthew Orton

This java program will output a GUI that take raw DNA sequence data and process the sequence data from lowercase to uppercase, inserting spaces
every 10 chars as well as adjusting the number of chars per line.
*/

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class SequenceProcessGUI extends JPanel implements ActionListener{

	private static final long serialVersionUID = 5541128329654819823L;
	
		//Declaring variables for the various text areas used and sequence parameter variables
		private JTextArea inputSequence;
		private JTextArea processSequence;
		private JTextArea statsSequence;
		private JCheckBox tenSpaceCheck;
		private JRadioButton lowercaseRadio;
		private JRadioButton uppercaseRadio;
		private JComboBox comboBox;
		private JButton processButton;
		private JScrollPane scrollPane;
		private JScrollPane scrollPane2;
		
		//Defining various options for the Jcombobox in charactersPerLine String
		String[] charactersPerLine = {"40", "50", "60" ,"70"}; 
		
		//Constructor for the GUI layout and functionality
		public SequenceProcessGUI() {
			setLayout(null);
			//Setting overall dimensions of the GUI
			setPreferredSize(new Dimension(900,350));
			
			//Defining the input sequence text area, setting font to a fixed width font as monospaced, also setting linewrap and wrapstyle word 
			//Jscroll pane was also added to add a vertical scrollbar as needed 
			inputSequence = new JTextArea();
			inputSequence.setFont(new Font("monospaced",Font.PLAIN,12));
			inputSequence.setBounds(10, 29, 495, 56);
			inputSequence.setColumns(10);
			inputSequence.setLineWrap(true);
			inputSequence.setWrapStyleWord(true);
			add(inputSequence);
			scrollPane = new JScrollPane(inputSequence);
			scrollPane.setBounds(10, 29, 495, 56);
			add(scrollPane);
			
			//Defining the process sequence text area, setting font to a fixed width font as monospaced, also setting linewrap and wrapstyle word 
			//Jscroll pane was also added to add a vertical scrollbar as needed 
			//also expanded text area to make larger compared to sequence input text area, set to a width of 70 characters before submission
			processSequence = new JTextArea();
			processSequence.setFont(new Font("monospaced",Font.PLAIN,12));
			processSequence.setBounds(10, 139, 495, 70);
			processSequence.setColumns(10);
			processSequence.setLineWrap(true);
			processSequence.setWrapStyleWord(true);
			add(processSequence);
			scrollPane2 = new JScrollPane(processSequence);
			scrollPane2.setBounds(10, 139, 495, 100);
			add(scrollPane2);
			
			//Defining the stats sequence text area, also setting linewrap and wrapstyle word 
			statsSequence = new JTextArea();
			statsSequence.setBounds(570, 139, 265, 80);
			statsSequence.setColumns(10);
			statsSequence.setLineWrap(true);
			statsSequence.setWrapStyleWord(true);
			add(statsSequence);
			
			//Process button added with the action listener and command enabled
			processButton = new JButton("Process Sequence");
			processButton.setBounds(10, 88, 190, 23);
			processButton.addActionListener(this);
			processButton.setActionCommand("process button was clicked");
			add(processButton);
			
			//resetButton added with the action listener and command enabled
			JButton resetButton = new JButton("Clear and Reset to Default");
			resetButton.setBounds(10, 243, 190, 23);
			resetButton.addActionListener(this);
			resetButton.setActionCommand("reset button was clicked");
			add(resetButton);
			
			//Jcheckbox added for adding a space every 10 sequences with action listener and command enabled
			//Deafult is unchecked as specified
			tenSpaceCheck = new JCheckBox("Space every 10 bases");
			tenSpaceCheck.setBounds(570, 55, 155, 14);
			add(tenSpaceCheck);
			
			//creating a button group for the radiobuttons
			//this will prevent both buttons from being used at the same time
			ButtonGroup buttonGroup = new ButtonGroup();
			
			//JRadiobutton for switching to uppercase characters
			//default for this is unselected as specified
			buttonGroup.add(uppercaseRadio = new JRadioButton("Uppercase", false));
			uppercaseRadio.setBounds(570, 29, 92, 23);
			uppercaseRadio.setSelected(false);
			add(uppercaseRadio);
			
			//JRadiobutton for switching to lowercase characters
			//default for this is selected as specified
			buttonGroup.add(lowercaseRadio = new JRadioButton("Lowercase", true));
			//Making sure that only one Radiobutton can be selected at one time
			lowercaseRadio.setBounds(670, 29, 100, 20);
			add(lowercaseRadio);
			
			//Jcombobox for choosing between the four various options
			//set to a default of 60 characters per line as specified
			comboBox = new JComboBox(charactersPerLine);
			comboBox.setSelectedIndex(2);
			comboBox.setBounds(660, 76, 50, 20);
			add(comboBox);
			
			//Defining a textfield above the input sequence text area
			JLabel rawSeq = new JLabel("Enter a Raw DNA Sequence:");
			rawSeq.setFont(new Font("Tahoma", Font.BOLD, 13));
			rawSeq.setBounds(10, 11, 190, 14);
			add(rawSeq);
			
			//Defining a textfield above the process sequence text area
			JLabel testFieldTitle2 = new JLabel("Processed Sequence:");
			testFieldTitle2.setFont(new Font("Tahoma", Font.BOLD, 13));
			testFieldTitle2.setBounds(10, 122, 150, 14);
			add(testFieldTitle2);
			
			//Defining a textfield above the sequence parameter section
			JLabel SequenceParameters = new JLabel("Sequence Parameters:");
			SequenceParameters.setFont(new Font("Tahoma", Font.BOLD, 13));
			SequenceParameters.setBounds(570, 11, 150, 14);
			add(SequenceParameters);
			
			//Bases per line text for the combobox
			JLabel CharactersPerLine = new JLabel("Bases per line:");
			CharactersPerLine.setBounds(570, 76, 100, 14);
			add(CharactersPerLine);
			
			//Defining a textfield above the sequence stats section
			JLabel SequenceStats = new JLabel("Processed Sequence Stats:");
			SequenceStats.setFont(new Font("Tahoma", Font.BOLD, 13));
			SequenceStats.setBounds(570, 122, 185, 14);
			add(SequenceStats);
		}
	@Override
	public void actionPerformed(ActionEvent ae) {
		//Defining the action command for the process button
		if(ae.getActionCommand() == "process button was clicked"){
			//Defining a string pattern for validation, will only validate for nucleotide characters
			String seqInput = inputSequence.getText();
			String pattern = "[^actgATCG]";
			Pattern r = Pattern.compile(pattern);
			Matcher m = r.matcher(seqInput);
			//If an invalid sequence is entered, a message dialog box will pop up asking the user to insert a valid sequence
			if (m.find( )) {
				JOptionPane.showMessageDialog(null, "Sequence is invalid, please only insert nucleotide characters in either uppercase or lowercase.", "Error!", JOptionPane.ERROR_MESSAGE);	
			} else {
				//If sequence is valid, another pattern is used to identify if the sequence is Uppercase and will convert to lowercase if it is, this is for the base calculations in the stats section 
				String pattern2 = "[ATCG]";
				Pattern r2 = Pattern.compile(pattern2);
				Matcher m2 = r2.matcher(seqInput);
				if (m2.find( )) {
					//Conversion to lowercase nucleotides
					seqInput = seqInput.toLowerCase();
				}
				//Calculation of sequence length
				int seqLength = seqInput.length();
				//Calculation of each of the various base counts
				int countA = seqInput.length() - seqInput.replace("a", "").length();
				int countG = seqInput.length() - seqInput.replace("g", "").length();
				int countT = seqInput.length() - seqInput.replace("t", "").length();
				int countC = seqInput.length() - seqInput.replace("c", "").length();
				//Calculation of the percentage of each base and converting to float, also rounding the decimal for each percentage
				float perA = Math.round((countA * 100f) / seqLength);
				float perG = Math.round((countG * 100f) / seqLength);
				float perC = Math.round((countC * 100f) / seqLength);
				float perT = Math.round((countT * 100f) / seqLength);
				
				//For the combo box selection, will first identify the index for which combo box option was chosen
				int combo = comboBox.getSelectedIndex();
				//then depending on which option was chosen, will reset the bounds of the process sequence text area to represent the number of characters per line that was chosen
				//will also change depending on whether the sequence has spaces or not
				//index 0 = 40 characters per line
				if (combo == 0){
					processSequence.setBounds(10, 139, 285, 70);
					scrollPane2.setBounds(10, 139, 285, 100);
					if(tenSpaceCheck.isSelected()){
					processSequence.setBounds(10, 139, 315, 70);
					scrollPane2.setBounds(10, 139, 315, 100);
					}
				}
				//index 1 = 50 characters per line, modify bounds accordingly
				if (combo == 1){
					processSequence.setBounds(10, 139, 355, 70);
					scrollPane2.setBounds(10, 139, 355, 100);
					if(tenSpaceCheck.isSelected()){
					processSequence.setBounds(10, 139, 390, 70);
					scrollPane2.setBounds(10, 139, 390, 100);
					}
				}
				//index 2 = 60 characters per line, modify bounds accordingly
				if (combo == 2){
					processSequence.setBounds(10, 139, 425, 70);
					scrollPane2.setBounds(10, 139, 425, 100);
					if(tenSpaceCheck.isSelected()){
					processSequence.setBounds(10, 139, 465, 70);
					scrollPane2.setBounds(10, 139, 465, 100);
					}
				}
				//index 3 = 70 characters per line, modify bounds accordingly
				if (combo == 3){
					processSequence.setBounds(10, 139, 495, 70);
					scrollPane2.setBounds(10, 139, 495, 100);
					if(tenSpaceCheck.isSelected()){
					processSequence.setBounds(10, 139, 550, 70);
					scrollPane2.setBounds(10, 139, 550, 100);
					}
				}
				//If uppercase radio button is selected than will change to uppercase since the sequence would already be in lowercase according to the default
				if(uppercaseRadio.isSelected()){
					String pattern3 = "[actg]";
					Pattern r3 = Pattern.compile(pattern3);
					Matcher m3 = r3.matcher(seqInput);
					if (m3.find( )) {
						//Conversion to Uppercase nucleotides if Uppercase is selected
						seqInput = seqInput.toUpperCase();
					}
				}
				//If tenspace is checked, then the sequence will split every ten characters using string builder
				//stringbuilder will insert a space every 10 characters
				String seqSpace;
				if(tenSpaceCheck.isSelected()){
				int interval = 10;
				String separator = " ";

				StringBuilder sb = new StringBuilder(seqInput);

				for(int i = 0; i < seqInput.length() / interval; i++) {
					 sb.insert(((i + 1) * interval) + i, separator);
				}
				seqSpace = sb.toString();
				//will print the sequence with the spaces if checked else will print the raw sequence without spaces
				processSequence.setText(seqSpace);
				} else {
					processSequence.setText(seqInput);
				}
				
				//Setting text in sequence stats text area for the length,counts and percentages of the sequence and bases
				//will always print unless an invalid sequence is entered
				statsSequence.setText("Sequence length is: " + Integer.toString(seqLength) + 
				System.lineSeparator() + "Number of A's: " + Integer.toString(countA) + ", Percentage of A's: " + Float.toString(perA) +
				System.lineSeparator() + "Number of G's: " + Integer.toString(countG) + ", Percentage of G's: " + Float.toString(perG) +
				System.lineSeparator() + "Number of T's: " + Integer.toString(countT) + ", Percentage of T's: " + Float.toString(perT) +
				System.lineSeparator() + "Number of C's: " + Integer.toString(countC) + ", Percentage of C's: " + Float.toString(perC));
			}
		}
		//Defining another action command for the reset button
		if(ae.getActionCommand() == "reset button was clicked"){
			//Defining a new JOptionPane
			//Dialog box will pop up if reset button is clicked giving users three options, Yes, no and cancel
			int Response = JOptionPane.showConfirmDialog(null, "Are you sure you want to clear and reset all fields?", "Reset Dialog",JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
			//If Yes option is selected, clear all text areas and reset to defaults
			if (Response == JOptionPane.YES_OPTION){
				statsSequence.setText("");
				processSequence.setText("");
				inputSequence.setText("");
				lowercaseRadio.setSelected(true);
				uppercaseRadio.setSelected(false);
				comboBox.setSelectedIndex(2);
				tenSpaceCheck.setSelected(false);
			//else if no or cancel options are selected, all text and settings will remain
			} else {
			}
	}
	}
	
}
