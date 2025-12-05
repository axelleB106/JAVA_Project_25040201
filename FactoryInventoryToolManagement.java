/*
 * BOULANGER AXELLE 
 * 25040201
 * PROJECT JAVA 
 * Factory Inventory & Tool Management System
 */


/*TO DO 
- Install MySQL and connect 
USE SWING 

//Workshop -- 
=================================================================
ITEMs 
Example items:
Servo motors
Microcontrollers (Arduino, ESP32…)
Sensors (temperature, ultrasonic…)
Robot arms & end-effectors
Tools (screwdrivers, soldering iron…)
- Item screnn - roll bar - choose item 
insert new item type -- Quantity + Category


USER - basic screen 
-- User - start app version - either - test version with random number of items already in stock 
-- or admin version where we choose number of stock 
- add items 
- edit items 
- delete items 
- search items 

VISUALS 
Highlight - low-stock items 
- display inventory 
sorting button 

- alert of low stock 
=================================================================
	BASE Appearance done 
CURRENT 
-- ADD Btn to left side + text display just under 

Item: 
	- cdt for name and Qtt
	- Base display 
	- Save - close window 
	- Connect with ADD Button
	- Send Info to SQL Data Base when Save clicked 
		
ADD Button 
	-click - open calls Item class 
	- when item class closes == save btn 
		-Add item to QComboBox
		-Call Refresh 
		
REFRESH Button
	- read from database and 
	- fct to only display the 6 Item wih the lowest qtt 
		-add - Item to display - 
		


	
*/

package Assignments; 

//My SQL imports 
import java.sql.*;
import java.sql.SQLException;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;




//ITEM =================================================================================================
class Item {
	String name;
	int id; //set-up later with auto_increment
	int quantity; 
	Item(){ //CONSTRUCTOR
		//Open a page and ask to fill name etc..
		JFrame ItemSetUp = new JFrame ("NEW ITEM");
		ItemSetUp.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Dispose - else everything closes 
		ItemSetUp.setLocation(1020, 300); 
		
		JLabel Name_Label = new JLabel("Enter Item Name");
		JLabel Qtt_Label = new JLabel("Enter Item Quantity");
		JTextField Name_textfield = new JTextField();
		JTextField Qtt_textfield = new JTextField();
		
		Name_textfield.setColumns(10);
		Qtt_textfield.setColumns(5);	
		Name_textfield.setBorder(BorderFactory.createLineBorder(new Color(0xE2E6F5),2));
		Qtt_textfield.setBorder(BorderFactory.createLineBorder(new Color(0xE2E6F5),2));
		
		//SAVE Button that save into the data BASE 
		
		JButton SAVE_btn = new JButton("SAVE");
		SAVE_btn.setHorizontalTextPosition(SwingConstants.CENTER);
		SAVE_btn.setBackground(new Color(0xE2E6F5));
		SAVE_btn.setFocusPainted(false);
		SAVE_btn.setBorderPainted(false);
		
		//--------------
		SAVE_btn.addActionListener(e -> btnClick(Name_textfield, Qtt_textfield,ItemSetUp));
		
		//PRIMARY-----------------------------------
		JPanel primary = new JPanel();
		primary.setLayout(new GridLayout(3, 1, 5,5));
		primary.setPreferredSize(new Dimension (250, 100));
		primary.setBackground(new Color(0xFCFCFE));
		
		JPanel Nested0 = new JPanel();
		JPanel Nested1 = new JPanel();	
		Nested0.setBackground(new Color(0xFCFCFE));
		Nested1.setBackground(new Color(0xFCFCFE));
		
		Nested0.add(Name_Label);
		Nested0.add(Name_textfield);
		Nested1.add(Qtt_Label);
		Nested1.add(Qtt_textfield);	
		
		primary.add(Nested0);	
		primary.add(Nested1);	
		primary.add(SAVE_btn);	
		
		//final 
		ItemSetUp.getContentPane().add(primary);
		ItemSetUp.pack();
		ItemSetUp.setVisible(true);
				
	}
	void btnClick(JTextField Name_textfield, JTextField Qtt_textfield,JFrame ItemSetUp) {
		
		String Name = Name_textfield.getText();
		String Qtt = Qtt_textfield.getText();
		
		//throwing exception
		//Verify Name
		if (Name.isEmpty()) {
			JOptionPane.showMessageDialog(null,"Name cannot be empty","Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		//Verify Qtt
		if (Qtt.isEmpty()) {
			JOptionPane.showMessageDialog(null,"Qtt cannot be empty","Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		int INtqtt; //  the one we send to the database
		try {INtqtt = Integer.parseInt(Qtt);}
		catch(NumberFormatException ex) {
			JOptionPane.showMessageDialog(null,ex+" : "+Qtt+" need to be an int ","Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		CONNECTsql database = new CONNECTsql();
		//cdt - no repeat of names - else database in disarray		
		
		try {database.WRITEsql(Name,INtqtt);}
		catch(SQLIntegrityConstraintViolationException cons) {
			JOptionPane.showMessageDialog(null,cons,"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		catch (SQLException ex) {
		    ex.printStackTrace();
		    JOptionPane.showMessageDialog(null,"Erreur SQL : " + ex,"Error", JOptionPane.ERROR_MESSAGE);
		    return;
		}
		ItemSetUp.dispose();
	}
}

//LEFT SIDE BASE =================================================================================
//Instead of creating 6 panels - just one class to auto object 

//ITEMPanel ---------------------------------------------------------------------------------------------
class ItemPanel extends JPanel{	
	
	//else cannot refreshItem in Main
	private JButton button;
    private JLabel quantityLabel;
    
	ItemPanel() {
		setBackground(new Color(0xE2E6F5));
		
		button = new JButton("");
		quantityLabel = new JLabel("", SwingConstants.CENTER);
		quantityLabel.setOpaque(true);
		
		JLabel spacerL = new JLabel ("    ", SwingConstants.CENTER);
		JLabel spacerR = new JLabel ("    ", SwingConstants.CENTER);

        button.setBackground(new Color(0xE2E6F5));
        button.setFocusPainted(false);
        button.setBorderPainted(false);

        button.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JOptionPane.showMessageDialog(null,"Next-open Item Window");
					}
				}			
			);
        add(spacerL);
        add(button);
        add(spacerR);
        add(quantityLabel);
	}
	
	//modify name and qtt separately to change with refresh
	void InfoItem(String itemName, int qtt) {
		button.setText(itemName);
		quantityLabel.setText(String.valueOf(qtt));
		
		if (qtt < 10 && itemName != "") {
			quantityLabel.setBackground(new Color(0xFFAB4C));
		}
		else {quantityLabel.setBackground(new Color(0xE2E6F5));}
	}
}

//RIGHT SIDE BASE =================================================================================

class Right_btn extends JButton{ //Style class - because all button have the same class 
	Right_btn(String btnName){
		super(btnName); //give name to btn
		
		setHorizontalTextPosition(SwingConstants.CENTER);
		setBackground(new Color(0xE2E6F5));
		setFocusPainted(false);
		setBorderPainted(false);

		addActionListener(e -> BtnClick()); //call event
	}
	void BtnClick(){} //set-up event in child- each extension catch it's own
}

//BUTTON SET UP ---------------------------------------------------------------------------------
//ADD
class ADD_btn extends Right_btn{
	ADD_btn() { super("ADD");} //gives back name
	void BtnClick() {
		Item newItem = new Item();
		// - acts like refresh button is clicked
	}
}
//REMOVE
class REMOVE_btn extends Right_btn{
	REMOVE_btn() { super("REMOVE");} //gives back name
	void BtnClick() {
		JOptionPane.showMessageDialog(null,"REMOVE- open page ask witch item to remove");
	}
}
//EXIT 
class EXIT_btn extends Right_btn{
	EXIT_btn() { super("EXIT");} //gives back name
	void BtnClick() {
		JOptionPane.showMessageDialog(null,"Close everything and save in database");
	}
}

class CONNECTsql { 
	String DB_URL = "jdbc:mysql://localhost:3306/factoryinventory"; 
	String USER = "root"; 
	String PASS = "surmaRoute54*"; 
	CONNECTsql(){ 
		try { 
			Connection con = DriverManager.getConnection(DB_URL,USER,PASS);
			//System.out.println("DB = " + con.getCatalog()); //name of DB
		} 
		catch (SQLException exe) {exe.printStackTrace();return; }		
	} 
	void WRITEsql(String Name, int Qtt)throws SQLException {
		
		Connection con = DriverManager.getConnection(DB_URL,USER,PASS);
		Statement stmt = con.createStatement();
			
		String q1 = "INSERT INTO ITEM (nameItem,qttItem) VALUES ('"+Name+"','"+Qtt+"');";
		int x = stmt.executeUpdate(q1);
	    if (x < 0)            
	    	System.out.println("Insert Failed");
    		con.close();            
	} 
	void READ1sql(String Name) {
		try { 
			Connection con = DriverManager.getConnection(DB_URL,USER,PASS);
			Statement stmt = con.createStatement();
			
			String q1 = "SELECT nameItem,qttItem FROM ITEM WHERE nameItem = '"+Name+"';";
			ResultSet rs = stmt.executeQuery(q1); //check if executed
			if (rs.next())
            {
                System.out.println("Name :" + rs.getString(1));
                System.out.println("qtt :" + rs.getInt(2));
            }
            else
            {
                System.out.println("No such user name");
            }
	        
	        con.close();
		} 
		catch (SQLException exe) {exe.printStackTrace();return; }	
	}
	void READ6sql(ArrayList <String> Names, ArrayList <Integer> Qtts) {
		//ckeck all items - only keep 6 of them;
		try { 
			Connection con = DriverManager.getConnection(DB_URL,USER,PASS);
			Statement stmt = con.createStatement();
			String q1 = "SELECT nameItem,qttItem FROM ITEM ORDER BY qttItem ASC LIMIT 6";
			ResultSet rs = stmt.executeQuery(q1); //check if executed
			while (rs.next()) {
				String name = rs.getString("nameItem");
				int qtt = rs.getInt("qttItem");
				Names.add(name);
				Qtts.add(qtt);
	        }
	        con.close();
		} 
		catch (SQLException exe) {exe.printStackTrace();return; }	
	}
	void READsql() {
		try { 
			Connection con = DriverManager.getConnection(DB_URL,USER,PASS);
			Statement stmt = con.createStatement();
			
			String q1 = "SELECT nameItem,qttItem FROM ITEM";
			ResultSet rs = stmt.executeQuery(q1); //check if executed
			while (rs.next()) {
	            String name = rs.getString("nameItem");
	            int qtt = rs.getInt("qttItem");
	            System.out.println("Name: " + name + ", qtt: " + qtt);
	        }
	        con.close();
		} 
		catch (SQLException exe) {exe.printStackTrace();return; }	
	}
	void UPDATEsql(String Name, int qtt) { //update qtt by using name
		try { 
			Connection con = DriverManager.getConnection(DB_URL,USER,PASS);
			Statement stmt = con.createStatement();
			
			String q1 = "UPDATE ITEM SET qttItem = '"+qtt+"' WHERE nameItem = '"+Name+"';";
			int x = stmt.executeUpdate(q1);
	        if (x > 0)            
	            System.out.println("Successfully Updated");            
	        else            
	            System.out.println("Insert Failed");
	        
	        con.close();
		} 
		catch (SQLException exe) {exe.printStackTrace();return; }	
	}
	void DELsql(String Name) {
		try { 
			Connection con = DriverManager.getConnection(DB_URL,USER,PASS);
			Statement stmt = con.createStatement();
			
			String q1 = "DELETE FROM ITEM WHERE nameItem = '"+Name+"';";
			int x = stmt.executeUpdate(q1);
	        if (x > 0)            
	            System.out.println("Successfully Deleted");            
	        else            
	            System.out.println("Insert Failed");
	        
	        con.close();
		} 
		catch (SQLException exe) {exe.printStackTrace();return; }	
	}
	
} 


public class FactoryInventoryToolManagement {
	
	static void CalculateLowestQtt(ArrayList <ItemPanel> ItemPanels) {
		ArrayList <String> Names= new ArrayList<>();
		ArrayList <Integer> Qtts= new ArrayList<>();
		CONNECTsql database = new CONNECTsql();
		database.READ6sql(Names, Qtts);
		
		//send thoses names as button names and qtt in Panel Items order
		for (int i = 0; i < ItemPanels.size(); i++) {
            if (i < Names.size()) {
            	ItemPanels.get(i).InfoItem(Names.get(i), Qtts.get(i));
            } else {
                // less than 6 items - just empty
            	ItemPanels.get(i).InfoItem("", 0);
            }
        }
	}
	
	public static void main(String[] args){
		
		//CONNECTION TO SQL ====================================================
		/*
		CONNECTsql database = new CONNECTsql();
		System.out.println("Initial DATA");
		database.READsql();

		System.out.println("Change qtt DATA");
		database.UPDATEsql("Arduino", 30);
		database.READsql();
		
		System.out.println("Del DATA");
		
		database.READsql();
		
		System.out.println("ADD back DATA");
		database.WRITEsql("Arduino", 11);
		database.READsql();
		*/
		
		//INTERFACE ============================================================
		//BASE-----------------
		JFrame mainFrame = new JFrame ("INVENTORY");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setLocation(500, 300); 
		
		
		//LEFT PANEL=============================================================
		JPanel LeftPanel = new JPanel();
		LeftPanel.setBackground(new Color(0xFCFCFE));
		LeftPanel.setPreferredSize(new Dimension (375, 300));
		LeftPanel.setLayout(new GridLayout(3, 3, 20,20));
				
		//- ADD to Left Panel ----------------		
		// add 6 nested panel in Left panel grid layout (best for 6 + (1,2) refresh btn ) 
		ArrayList <ItemPanel> ItemPanels= new ArrayList<>();
		for (int i =0; i<6; i++) {
			ItemPanel p = new ItemPanel();
			ItemPanels.add(p);
			LeftPanel.add(p);
		}
		
		// need to set-up refresh btn in main - else refresh cannot interact with LeftPanel
		JButton btn_Refresh = new JButton(); 
		btn_Refresh.setHorizontalTextPosition(SwingConstants.CENTER);
		btn_Refresh.setBackground(new Color(0xFCFCFE));
		btn_Refresh.setFocusPainted(false);
		btn_Refresh.setBorderPainted(false);
		//--------------
		btn_Refresh.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					CalculateLowestQtt(ItemPanels);
					JOptionPane.showMessageDialog(null,"REFRESH -Lower stock Items");
				}
			}			
		);
		//Empty - make things pretty
		JPanel Null = new JPanel();
		Null.setBackground(new Color(0xFCFCFE));
		
		
		LeftPanel.add(Null);
		LeftPanel.add(btn_Refresh);
		//refresh when launch 
		CalculateLowestQtt(ItemPanels);

		//RIGHT PANEL=============================================================
		JPanel RightPanel = new JPanel();
		RightPanel.setBackground(new Color(0xE2E6F5));
		RightPanel.setPreferredSize(new Dimension (125, 300));
		
		//JComboBox(E[] items): Creates a JComboBox populated with items from an array.
		//take item list read in MySQL storage - if any 
		JComboBox ItemList = new JComboBox(); 
		
		ADD_btn btn_ADD = new ADD_btn();
		REMOVE_btn btn_REMOVE = new REMOVE_btn();
		EXIT_btn btn_EXIT = new EXIT_btn();
						
		RightPanel.add(ItemList);
		RightPanel.add(btn_ADD);
		RightPanel.add(btn_REMOVE);
		RightPanel.add(btn_EXIT);
		
		//PRIMARY PANEL===============================================================
		JPanel nestedprimary = new JPanel();
		nestedprimary.setBackground(new Color(0xFCFCFE));
		nestedprimary.add(LeftPanel);
		nestedprimary.add(RightPanel);
				
		
		//final 
		mainFrame.getContentPane().add(nestedprimary);
		mainFrame.pack();
		mainFrame.setVisible(true);
		   
	}
}
