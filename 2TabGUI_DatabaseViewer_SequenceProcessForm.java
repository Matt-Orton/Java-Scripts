
/*
 * Authored by Matthew Orton
 * This program will be a two tabbed GUI. One tab will contain the sequence process form
 * developed in the SequenceProcessGUI program whereas the other will possess database integration with my own mysql
 * account bif724_161a17. The user will then be able to select any of the tables present
 * in this database by selecting the appropriate radiobutton and a table displaying the table
 * will be shown on this tab once they click submit.
*/


/*

I declare that the attached assignment is my own work in accordance
with Seneca Academic Policy. No part of this assignment has been
copied manually or electronically from any other source (including web
sites) or distributed to other students.
Name : Matthew Orton
Student ID: 030739155
*/

//The first part of this assignment is my work from Assignment 2 so much of the code remains the same
//Also note the tables I use are just example tables, not real datasets


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;


class MatthewA3 extends JFrame implements ActionListener 
{
	private static final long serialVersionUID = 1L;
	
	/*
	 * First defining a Jtabbed pane and 2 panels - one for the sequence panel GUI
	 * and one for the database table viewer GUI
	 */
	
	private	JTabbedPane tabbedPane;
	private	JPanel panel1;
	private	JPanel panel2;
	
	//Declaring variables for the various text areas used and sequence parameter variables of tab 1
	JTextArea inputSequence;
	JTextArea processSequence;
	JTextArea statsSequence;
	JCheckBox tenSpaceCheck;
	JRadioButton lowercaseRadio;
	JRadioButton uppercaseRadio;
	JComboBox comboBox;
	JButton processButton;
	JScrollPane scrollPane;
	JScrollPane scrollPane2;
	
	//Tab 2 components
	//Array list for the table names
	ArrayList<String> names = new ArrayList<String>(); 
	//Array size variable
	int size;
	//JRadioButtons
	JRadioButton[] buttons;
	//metadata for resultSet
	DatabaseMetaData md;
	//defining a variable for our result set which will be our table names and table data
	ResultSet resultSet = null;
	//Defining a new button group for tab2
	//Using a buttongroup will ensure multiple radiobuttons for our tables cannot be selected at once
	ButtonGroup buttonGroup2 = new ButtonGroup();
	//Connection variable
	java.sql.Connection connection;
	//Statement variable
	Statement statement;
	//second result set
	ResultSet rs2;
	//JTable variable
	JTable table;
	//Variable for the button name
	String buttonName;
					
	public MatthewA3()
	{	//Title and size of the GUI
		setTitle( "SequenceProcessForm and TableSelectionForm" );
		setSize( 1000, 400 );
		setBackground( Color.gray );
		
		//Setting a JPanel for the GUI
		JPanel topPanel = new JPanel();
		topPanel.setLayout( new BorderLayout() );
		getContentPane().add( topPanel );

		// Creating each page/tab of the GUI
		createPage1();
		createPage2();

		// Create a tabbed pane
		tabbedPane = new JTabbedPane();
		//Naming each tab and specifying the location of the tabs
		tabbedPane.addTab( "SequenceProcessForm", panel1 );
		tabbedPane.addTab( "TableSelectionForm", panel2 );
		topPanel.add( tabbedPane, BorderLayout.CENTER );
	}
	//First tab (Assignment2)
	public void createPage1()
	{
		panel1 = new JPanel();
		panel1.setLayout( null );
		
		//Defining various options for the Jcombobox in charactersPerLine String
		String[] charactersPerLine = {"40", "50", "60" ,"70"}; 
					
		//Defining the input sequence text area, setting font to a fixed width font as monospaced, also setting linewrap and wrapstyle word 
		//Jscroll pane was also added to add a vertical scrollbar as needed 
		inputSequence = new JTextArea();
		inputSequence.setFont(new Font("monospaced",Font.PLAIN,12));
		inputSequence.setBounds(10, 29, 495, 56);
		inputSequence.setColumns(10);
		inputSequence.setLineWrap(true);
		inputSequence.setWrapStyleWord(true);
		panel1.add(inputSequence);
		scrollPane = new JScrollPane(inputSequence);
		scrollPane.setBounds(10, 29, 495, 56);
		panel1.add(scrollPane);
					
		//Defining the process sequence text area, setting font to a fixed width font as monospaced, also setting linewrap and wrapstyle word 
		//Jscroll pane was also added to add a vertical scrollbar as needed 
		//also expanded text area to make larger compared to sequence input text area, set to a width of 70 characters before submission
		processSequence = new JTextArea();
		processSequence.setFont(new Font("monospaced",Font.PLAIN,12));
		processSequence.setBounds(10, 139, 495, 70);
		processSequence.setColumns(10);
		processSequence.setLineWrap(true);
		processSequence.setWrapStyleWord(true);
		panel1.add(processSequence);
		scrollPane2 = new JScrollPane(processSequence);
		scrollPane2.setBounds(10, 139, 495, 100);
		panel1.add(scrollPane2);
		
		//Defining the stats sequence text area, also setting linewrap and wrapstyle word 
		statsSequence = new JTextArea();
		statsSequence.setBounds(570, 139, 265, 80);
		statsSequence.setColumns(10);
		statsSequence.setLineWrap(true);
		statsSequence.setWrapStyleWord(true);
		panel1.add(statsSequence);
		
		//Process button added with the action listener and command enabled
		processButton = new JButton("Process Sequence");
		processButton.setBounds(10, 88, 190, 23);
		processButton.addActionListener(this);
		processButton.setActionCommand("process button was clicked");
		panel1.add(processButton);
		
		//resetButton added with the action listener and command enabled
		JButton resetButton = new JButton("Clear and Reset to Default");
		resetButton.setBounds(10, 243, 190, 23);
		resetButton.addActionListener(this);
		resetButton.setActionCommand("reset button was clicked");
		panel1.add(resetButton);
		
		//Jcheckbox added for adding a space every 10 sequences with action listener and command enabled
		//Deafult is unchecked as specified
		tenSpaceCheck = new JCheckBox("Space every 10 bases");
		tenSpaceCheck.setBounds(570, 55, 155, 14);
		panel1.add(tenSpaceCheck);
		
		//creating a button group for the radiobuttons
		//this will prevent both buttons from being used at the same time
		ButtonGroup buttonGroup = new ButtonGroup();
		
		//JRadiobutton for switching to uppercase characters
		//default for this is unselected as specified
		buttonGroup.add(uppercaseRadio = new JRadioButton("Uppercase", false));
		uppercaseRadio.setBounds(570, 29, 92, 23);
		uppercaseRadio.setSelected(false);
		panel1.add(uppercaseRadio);
		
		//JRadiobutton for switching to lowercase characters
		//default for this is selected as specified
		buttonGroup.add(lowercaseRadio = new JRadioButton("Lowercase", true));
		//Making sure that only one Radiobutton can be selected at one time
		lowercaseRadio.setBounds(670, 29, 100, 20);
		panel1.add(lowercaseRadio);
		
		//Jcombobox for choosing between the four various options
		//set to a default of 60 characters per line as specified
		comboBox = new JComboBox(charactersPerLine);
		comboBox.setSelectedIndex(2);
		comboBox.setBounds(660, 76, 50, 20);
		panel1.add(comboBox);
		
		//Defining a textfield above the input sequence text area
		JLabel rawSeq = new JLabel("Enter a Raw DNA Sequence:");
		rawSeq.setFont(new Font("Tahoma", Font.BOLD, 13));
		rawSeq.setBounds(10, 11, 190, 14);
		panel1.add(rawSeq);
		
		//Defining a textfield above the process sequence text area
		JLabel testFieldTitle2 = new JLabel("Processed Sequence:");
		testFieldTitle2.setFont(new Font("Tahoma", Font.BOLD, 13));
		testFieldTitle2.setBounds(10, 122, 150, 14);
		panel1.add(testFieldTitle2);
		
		//Defining a textfield above the sequence parameter section
		JLabel SequenceParameters = new JLabel("Sequence Parameters:");
		SequenceParameters.setFont(new Font("Tahoma", Font.BOLD, 13));
		SequenceParameters.setBounds(570, 11, 150, 14);
		panel1.add(SequenceParameters);
		
		//Bases per line text for the combobox
		JLabel CharactersPerLine = new JLabel("Bases per line:");
		CharactersPerLine.setBounds(570, 76, 100, 14);
		panel1.add(CharactersPerLine);
		
		//Defining a textfield above the sequence stats section
		JLabel SequenceStats = new JLabel("Processed Sequence Stats:");
		SequenceStats.setFont(new Font("Tahoma", Font.BOLD, 13));
		SequenceStats.setBounds(570, 122, 185, 14);
		panel1.add(SequenceStats);
	}
	
	//Second tab (Assignment3)
	public void createPage2()
	{
		panel2 = new JPanel();
		panel2.setLayout( null );
		
		//Defining a Jlabel above the radiobuttons to indicate the database
		JLabel topHeader = new JLabel("Select one table from the bif724_161a17 database:");
		topHeader.setFont(new Font("Tahoma", Font.BOLD, 13));
		topHeader.setBounds(10, 11, 500, 14);
		panel2.add(topHeader);
		
		//Defining a Jlabel above the Jtable 
		JLabel topHeaderTable = new JLabel("Table View:");
		topHeaderTable.setFont(new Font("Tahoma", Font.BOLD, 13));
		topHeaderTable.setBounds(400, 11, 150, 14);
		panel2.add(topHeaderTable);
		
		//Defining a Jbutton for selection of one of the tables presented in the database
		JButton submitButton = new JButton("Submit Table Selection");
		submitButton.setBounds(10, 150, 250, 23);
		submitButton.addActionListener(this);
		submitButton.setActionCommand("submit button was clicked");
		panel2.add(submitButton);
		
		//Defining an empty scrollpane before any radiobutton is selected
		JScrollPane pane = new JScrollPane();
	    pane.setBounds(400, 40, 550, 200);
	    panel2.add(pane);
		
		//using the com.mysql.jdbc.Driver
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("The Jar file for the connection may not be included.");

		}
		//Defining the url for the mysql account 
		//Note that I am using the mysql zenit account since this already a few tables I can show for the GUI
		String databaseURI = "jdbc:mysql://zenit.senecac.on.ca/bif724_161a17";
		
		//setting connection, statement all to null right now
		connection = null;
		statement = null;
		
		//setting my username and password, also setting connection as false for right now
		String username = "bif724_161a17";
		String password = "cpAE6674";
		boolean connected = false;
		
		try {
			//now will actually try to connect to the mysql database
			connection = DriverManager.getConnection(databaseURI, username, password);
			statement = connection.createStatement();
			connected = true;
		} catch (SQLException e) {
			e.printStackTrace();
			connected = false;
		}
		if(connected == true){
			/*
			 * If connection is good, then we can grab the metadata for the database
			 * including the table names
			*/
			md = null;
			
			try {
				//grabbing metadata for the specified database
				md = connection.getMetaData();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				//grabbing each table name from the database with md.getTables
				resultSet = md.getTables(null, null, "%", null);
				//Adding names to an array so we can iterate through each name
				while (resultSet.next()){
					names.add(resultSet.getString(3));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			//iterate over the names array for each table entry in the database
			//in this for loop we can add a new radiobutton for each additional table
			//each radiobutton then gets added to the button group and the tabbed panel itself
			//default being that no radiobutton is selected initially
			//Defining size of the array first
			size = names.size(); 
			//Creating as many radiobuttons as there are array items the database names array
			buttons = new JRadioButton[size];
			for (int i = 0; i < size; i++) {
				buttons[i] = new JRadioButton(names.get(i));
			    buttonGroup2.add(buttons[i]);
			    //Default of no selection
			    buttons[i].setSelected(false);
			    //Setting bounds of radiobutton so each one appears underneath the one before it
			    buttons[i].setBounds(10, 30+(i*20), 92, 23);
			    //Adding each one to the panel
			    panel2.add(buttons[i]);
			}
			
		}
	
	}
	//Action commands for both tabs 
	//Tab 1 action commands
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
					processSequence.setBounds(10, 139, 330, 70);
					scrollPane2.setBounds(10, 139, 330, 100);
					}
				}
				//index 1 = 50 characters per line, modify bounds accordingly
				if (combo == 1){
					processSequence.setBounds(10, 139, 355, 70);
					scrollPane2.setBounds(10, 139, 355, 100);
					if(tenSpaceCheck.isSelected()){
					processSequence.setBounds(10, 139, 405, 70);
					scrollPane2.setBounds(10, 139, 405, 100);
					}
				}
				//index 2 = 60 characters per line, modify bounds accordingly
				if (combo == 2){
					processSequence.setBounds(10, 139, 425, 70);
					scrollPane2.setBounds(10, 139, 425, 100);
					if(tenSpaceCheck.isSelected()){
					processSequence.setBounds(10, 139, 480, 70);
					scrollPane2.setBounds(10, 139, 480, 100);
					}
				}
				//index 3 = 70 characters per line, modify bounds accordingly
				if (combo == 3){
					processSequence.setBounds(10, 139, 495, 70);
					scrollPane2.setBounds(10, 139, 495, 100);
					if(tenSpaceCheck.isSelected()){
					processSequence.setBounds(10, 139, 555, 70);
					scrollPane2.setBounds(10, 139, 555, 100);
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
		//Tab 2 action command
		//Defining another action command for the submit button on tab 2
		if(ae.getActionCommand() == "submit button was clicked"){
			for(int i = 0; i < size; i++) {
				if(buttons[i].isSelected()){
					//Determining which table the selected radiobutton is associated with
					buttonName = buttons[i].getText();
					try {
						//preparing an sql statement with the name of the button that was
						//chosen since this will represent the name of the table we want
						String sql = "select * from " + buttonName;
						rs2 = statement.executeQuery(sql);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				    try {
				    	//using the buildTableModel to construct a table from resultset2
						table = new JTable(buildTableModel(rs2));
					} catch (SQLException e) {
						e.printStackTrace();
					}
				    //Adding the table to a new scrollpane and adding to the second tab of the gui
				    JScrollPane pane = new JScrollPane(table);
				    pane.setBounds(400, 40, 550, 200);
				    panel2.add(pane);
				} 
			}
		}
	}
	//this method will be used to actually construct the table based on the query generated
	//from resultset2
	public TableModel buildTableModel(ResultSet rs2) throws SQLException {
		java.sql.ResultSetMetaData metaData = rs2.getMetaData();

	    //Grabbing the names of each of the columns from the table
		//colNames are defined as type Vector<String>
	    Vector<String> colNames = new Vector<String>();
	    int columnCounter = metaData.getColumnCount();
	    //Iterating through columns of the of the table
	    for (int col = 1; col <= columnCounter; col++) {
	        colNames.add(metaData.getColumnName(col));
	    }
	    //Grabbing the actual data from the table using the resultset2 query
	    //data is defined as type Vector<Object>
	    Vector<Vector<Object>> tableData = new Vector<Vector<Object>>();
	    while (rs2.next()) {
	        Vector<Object> tableVector = new Vector<Object>();
	        //iterating through column indices
	        for (int columnInd = 1; columnInd <= columnCounter; columnInd++) {
	            tableVector.add(rs2.getObject(columnInd));
	        }
	        tableData.add(tableVector);
	    }
	    //Finally returning this query back to the table variable above 
	    //where it can be inserted into a JScrollPane
	    return new DefaultTableModel(tableData, colNames);
	}
	
	//Main for actually running the GUI
	public static void main( String args[] )
	{
		MatthewA3 mainFrame = new MatthewA3();
		mainFrame.setVisible( true );
	}
}

