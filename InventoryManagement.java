/*
 * BOULANGER AXELLE 
 * 25040201
 * PROJECT JAVA 
 * Factory Inventory & Tool Management System
 */

/*
TO DO =================================================================

- Install MySQL and connect 

BASE
	USER - basic screen 
	- add items 
	- edit items 
	- delete items 
	- search items --> ComboBox

FRAME_ADD_Item: 
	- cdt for name and Qtt
	- Base display 
	- Save - close window 
	- Connect with ADD Button
	- Send Info to SQL Data Base when Save clicked 
	
FRAME_DEL_Item
	- Same as FRAME_ADD_Item
	- only name matters 
	- connect w/ DELsql 
		- Name must exist to delete it 
		
ADD Button 
	-click - open calls Item class 
	- when item class closes == save btn 
	- Qtt cannot be negative
		
REFRESH Button
	- read from database and 
	- fct to only display the 6 Item wih the lowest qtt 
	-add - Item to display 
	-Update Combobox
		
ComboBox: 
	- List all Item from database - no order 
	- Add Item when Save clicked -->refresh 
	- Remove Item When DELETE Clicked -->Refresh
	- When item choosen --> act the same as Item name button 
		
FRAME_UPDATE_Item 
	-Opens when Item Button is clicked 
	- Open new Window to modify Item 
	- Display Info from DataBase
	- Save new qtt is changed

=================================================================
	
*/

package Assignments; 

//Imports ----
import java.sql.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import java.util.*;
//---

//RIGHT SIDE ===================================================================================================================================================
class FRAME_ADD_Item {

	FRAME_ADD_Item(){ //CONSTRUCTOR
		//Open a page and ask to fill name etc..
		JFrame AddItemFrame = new JFrame ("NEW ITEM");
		AddItemFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Dispose - else everything closes 
		AddItemFrame.setLocation(1020, 300); 
		
		JLabel LABEL_ItemName = new JLabel("Enter Item Name");
		JLabel LABEL_ItemQtt = new JLabel("Enter Item Quantity");
		JTextField TEXTFIELD_ItemName = new JTextField();
		JTextField TEXTFIELD_ItemQtt = new JTextField();
		
		TEXTFIELD_ItemName.setColumns(10);
		TEXTFIELD_ItemQtt.setColumns(5);	
		TEXTFIELD_ItemName.setBorder(BorderFactory.createLineBorder(new Color(0xE2E6F5),2));
		TEXTFIELD_ItemQtt.setBorder(BorderFactory.createLineBorder(new Color(0xE2E6F5),2));
		
		//SAVE Button that save into the data BASE 
		
		JButton btn_SAVE_data = new JButton("SAVE");
		btn_SAVE_data.setHorizontalTextPosition(SwingConstants.CENTER);
		btn_SAVE_data.setBackground(new Color(0xE2E6F5));
		btn_SAVE_data.setFocusPainted(false);
		btn_SAVE_data.setBorderPainted(false);
		
		//--------------
		btn_SAVE_data.addActionListener(e -> btnClick(TEXTFIELD_ItemName, TEXTFIELD_ItemQtt, AddItemFrame));
		
		//PRIMARY-----------------------------------
		JPanel primary_add = new JPanel();
		primary_add.setLayout(new GridLayout(3, 1, 5,5));
		primary_add.setPreferredSize(new Dimension (250, 105));
		primary_add.setBackground(new Color(0xFCFCFE));
		
		JPanel Nested0 = new JPanel();
		JPanel Nested1 = new JPanel();	
		Nested0.setBackground(new Color(0xFCFCFE));
		Nested1.setBackground(new Color(0xFCFCFE));
		
		Nested0.add(LABEL_ItemName);
		Nested0.add(TEXTFIELD_ItemName);
		Nested1.add(LABEL_ItemQtt);	
		Nested1.add(TEXTFIELD_ItemQtt);
		
		primary_add.add(Nested0);	
		primary_add.add(Nested1);	
		primary_add.add(btn_SAVE_data);	
		
		//final 
		AddItemFrame.getContentPane().add(primary_add);
		AddItemFrame.pack();
		AddItemFrame.setVisible(true);
				
	}
	void btnClick(JTextField TEXTFIELD_ItemName, JTextField TEXTFIELD_ItemQtt,JFrame AddItemFrame) {
		
		String ItemName = TEXTFIELD_ItemName.getText();
		String ItemQtt = TEXTFIELD_ItemQtt.getText();
		
		//throwing exception
		//Verify Name
		if (ItemName.isEmpty()) {
			JOptionPane.showMessageDialog(null,"Name cannot be empty","Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		//Verify Qtt
		if (ItemQtt.isEmpty()) {
			JOptionPane.showMessageDialog(null,"Qtt cannot be empty","Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		int INT_ItemQtt; //  the one we send to the database
		try {INT_ItemQtt = Integer.parseInt(ItemQtt);}
		catch(NumberFormatException ex) {
			JOptionPane.showMessageDialog(null,ex+" : "+ItemQtt+" need to be an int ","Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (INT_ItemQtt < 0) {
			JOptionPane.showMessageDialog(null,"Qtt cannot be Negative","Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		CONNECTsql database = new CONNECTsql();
		//cdt - no repeat of names - else database in disarray		
		
		try {database.WRITEsql(ItemName,INT_ItemQtt);}
		catch(SQLIntegrityConstraintViolationException cons) {
			JOptionPane.showMessageDialog(null,cons,"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		catch (SQLException ex) {
		    ex.printStackTrace();
		    JOptionPane.showMessageDialog(null,"Erreur SQL : " + ex,"Error", JOptionPane.ERROR_MESSAGE);
		    return;
		}
		AddItemFrame.dispose();
	}
}

class FRAME_DEL_Item {

	FRAME_DEL_Item(){ //CONSTRUCTOR
		//Open a page and ask to fill name etc..
		JFrame DelItemFrame = new JFrame ("DELETE ITEM");
		DelItemFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Dispose - else everything closes 
		DelItemFrame.setLocation(1020, 300); 
		
		JLabel LABEL_ItemName = new JLabel("Enter Item Name");
		JTextField TEXTFIELD_ItemName = new JTextField();
		
		TEXTFIELD_ItemName.setColumns(10);
		TEXTFIELD_ItemName.setBorder(BorderFactory.createLineBorder(new Color(0xE2E6F5),2));
		
		//SAVE Button that save into the data BASE 
		
		JButton btn_SAVE_data = new JButton("SAVE");
		btn_SAVE_data.setHorizontalTextPosition(SwingConstants.CENTER);
		btn_SAVE_data.setBackground(new Color(0xE2E6F5));
		btn_SAVE_data.setFocusPainted(false);
		btn_SAVE_data.setBorderPainted(false);
		
		//--------------
		btn_SAVE_data.addActionListener(e -> btnClick(TEXTFIELD_ItemName, DelItemFrame));
		
		//PRIMARY-----------------------------------
		JPanel primary_delete = new JPanel();
		primary_delete.setLayout(new GridLayout(2, 1, 5,5));
		primary_delete.setPreferredSize(new Dimension (250, 70));
		primary_delete.setBackground(new Color(0xFCFCFE));
		
		JPanel Nested0 = new JPanel();
		Nested0.setBackground(new Color(0xFCFCFE));
		
		Nested0.add(LABEL_ItemName);
		Nested0.add(TEXTFIELD_ItemName);
		
		primary_delete.add(Nested0);		
		primary_delete.add(btn_SAVE_data);	
		
		//final 
		DelItemFrame.getContentPane().add(primary_delete);
		DelItemFrame.pack();
		DelItemFrame.setVisible(true);
				
	}
	void btnClick(JTextField TEXTFIELD_ItemName ,JFrame DelItemFrame) {
		
		String ItemName = TEXTFIELD_ItemName.getText();
		
		//throwing exception
		//Verify Name
		if (ItemName.isEmpty()) {
			JOptionPane.showMessageDialog(null,"Name cannot be empty","Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		CONNECTsql database = new CONNECTsql();
		//cdt - verify name really exists 	
		try {database.DELsql(ItemName);}
		catch (SQLException ex) {
		    JOptionPane.showMessageDialog(null,"Erreur SQL : " + ex.getMessage(),"Error", JOptionPane.ERROR_MESSAGE);
		    return;
		}
		
		DelItemFrame.dispose();
	}
}

//RIGHT SIDE ===================================================================================================================================================
//Instead of creating 6 panels - just one class to call 6 times 

class PANEL_Item extends JPanel{	
	
	//else cannot refreshItem in Main
	private JButton btn_ItemName;
    private JLabel LABEL_ItemQtt;
    
    PANEL_Item() {
		setBackground(new Color(0xE2E6F5));
		
		btn_ItemName = new JButton("");
		LABEL_ItemQtt = new JLabel("", SwingConstants.CENTER);
		
		LABEL_ItemQtt.setOpaque(true);
		LABEL_ItemQtt.setPreferredSize(new Dimension(95, 20));
		ItemNameBtnStyle(btn_ItemName);
	
		btn_ItemName.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String selectedName = btn_ItemName.getText();
					new FRAME_UPDATE_Item(selectedName);
				}
			}			
		);
        add(btn_ItemName);
        add(LABEL_ItemQtt);
	}
    static void ItemNameBtnStyle(JButton Btn) {
    	Btn.setPreferredSize(new Dimension(100, 30));
    	Btn.setBackground(new Color(0xE2E6F5));
    	Btn.setFocusPainted(false);
	    Btn.setBorder(new CompoundBorder(
	            new LineBorder(new Color(0xFCFCFE), 2),   
	            new EmptyBorder(5, 12, 5, 12)             
	        ));
	}
	//modify name and qtt separately to change with refresh
	void INPUT_InfoItem(String ItemName, int ItemQtt) {
		btn_ItemName.setText(ItemName);
		LABEL_ItemQtt.setText(String.valueOf(ItemQtt));
		
		if (ItemQtt < 10 && !ItemName.isEmpty()) {
			LABEL_ItemQtt.setBackground(new Color(0xF39393));
		}
		else {LABEL_ItemQtt.setBackground(new Color(0xE2E6F5));}
	}	
}

class FRAME_UPDATE_Item {
	FRAME_UPDATE_Item(String ItemName){ 
		
		JFrame UpdateItemFrame = new JFrame ("ITEM");
		UpdateItemFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Dispose - else everything closes 
		UpdateItemFrame.setLocation(1020, 300); 
		
		//get Qtt from Name in database 
		CONNECTsql database = new CONNECTsql();
		int ItemQtt = database.READQttsql(ItemName);
		
		JLabel LABEL_ItemName = new JLabel(ItemName.toUpperCase());
		JTextField TEXTFIELD_ItemQtt = new JTextField(String.valueOf(ItemQtt));
		
		TEXTFIELD_ItemQtt.setColumns(10);	
		TEXTFIELD_ItemQtt.setBorder(BorderFactory.createLineBorder(new Color(0xE2E6F5),2));
		
		//SAVE Button that save into the data BASE 
		JButton btn_SAVE_data = new JButton("SAVE");
		btn_SAVE_data.setHorizontalTextPosition(SwingConstants.CENTER);
		btn_SAVE_data.setBackground(new Color(0xE2E6F5));
		btn_SAVE_data.setFocusPainted(false);
		btn_SAVE_data.setBorderPainted(false);
		
		//--------------
		btn_SAVE_data.addActionListener(e -> btnClick(ItemName, TEXTFIELD_ItemQtt ,UpdateItemFrame));
		
		//PRIMARY-----------------------------------
		JPanel primary_update = new JPanel();
		primary_update.setLayout(new GridLayout(3, 1, 5,5));
		primary_update.setPreferredSize(new Dimension (150, 105));
		primary_update.setBackground(new Color(0xFCFCFE));
		
		primary_update.add(LABEL_ItemName);	
		primary_update.add(TEXTFIELD_ItemQtt);	
		primary_update.add(btn_SAVE_data);	
		
		//final 
		UpdateItemFrame.getContentPane().add(primary_update);
		UpdateItemFrame.pack();
		UpdateItemFrame.setVisible(true);
				
	}
	void btnClick(String ItemName, JTextField TEXTFIELD_ItemQtt, JFrame UpdateItemFrame) {
		
		String ItemQtt = TEXTFIELD_ItemQtt.getText();
		
		//throwing exception
		//Verify Qtt
		if (ItemQtt.isEmpty()) {
			JOptionPane.showMessageDialog(null,"Qtt cannot be empty","Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		int INT_ItemQtt; //  the one we send to the database
		try {INT_ItemQtt = Integer.parseInt(ItemQtt);}
		catch(NumberFormatException ex) {
			JOptionPane.showMessageDialog(null,ex+" : "+ItemQtt+" need to be an int ","Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (INT_ItemQtt < 0) {
			JOptionPane.showMessageDialog(null,"Qtt cannot be Negative","Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		//Update Database - with new qtt for Item 
		CONNECTsql database = new CONNECTsql();
		database.UPDATEsql(ItemName,INT_ItemQtt);
		
		UpdateItemFrame.dispose();
	}
}


//SQL database =================================================================================

class CONNECTsql { 
	String DB_URL = "jdbc:mysql://localhost:3306/factoryinventory"; 
	String USER = "root"; 
	String PASS = "surmaRoute54*"; 

	void WRITEsql(String ItemName, int ItemQtt)throws SQLException {
		Connection con = DriverManager.getConnection(DB_URL,USER,PASS);
		Statement stmt = con.createStatement();
			
		String q1 = "INSERT INTO ITEM (nameItem,qttItem) VALUES ('"+ItemName+"','"+ItemQtt+"');";
		int x = stmt.executeUpdate(q1);
	    if (x < 0) {JOptionPane.showMessageDialog(null,"Insert Failed","Error", JOptionPane.ERROR_MESSAGE);}
    	con.close();            
	} 
	void DELsql(String ItemName)throws SQLException {
		Connection con = DriverManager.getConnection(DB_URL,USER,PASS);
		Statement stmt = con.createStatement();
			
		String q1 = "DELETE FROM ITEM WHERE nameItem = '"+ItemName+"';";
		int x = stmt.executeUpdate(q1);
		if (x == 0) {throw new SQLException("No such item name: " + ItemName);} 
		else if (x < 0) {JOptionPane.showMessageDialog(null,"Delete Failed","Error", JOptionPane.ERROR_MESSAGE);}
	    con.close();    

	}
	void UPDATEsql(String ItemName, int ItemQtt) { //update qtt by using name
		try { 
			Connection con = DriverManager.getConnection(DB_URL,USER,PASS);
			Statement stmt = con.createStatement();
			
			String q1 = "UPDATE ITEM SET qttItem = '"+ItemQtt+"' WHERE nameItem = '"+ItemName+"';";
			int x = stmt.executeUpdate(q1);
			if (x < 0) {JOptionPane.showMessageDialog(null,"Update Failed","Error", JOptionPane.ERROR_MESSAGE);}
	        
	        con.close();
		} 
		catch (SQLException exe) {exe.printStackTrace();return; }	
	}
	void READsql(ArrayList <String> LIST_ItemName) {
		try { 
			Connection con = DriverManager.getConnection(DB_URL,USER,PASS);
			Statement stmt = con.createStatement();
			
			String q1 = "SELECT nameItem FROM ITEM";
			ResultSet rs = stmt.executeQuery(q1);
			while (rs.next()) {
	            String ItemName = rs.getString("nameItem");
	            LIST_ItemName.add(ItemName);
	        }
	        con.close();
		} 
		catch (SQLException exe) {exe.printStackTrace();return; }	
	}
	void READ6sql(ArrayList <String> LIST_ItemName, ArrayList <Integer> LIST_ItemQtt) {
		//ckeck all items - only keep 6 of them;
		try { 
			Connection con = DriverManager.getConnection(DB_URL,USER,PASS);
			Statement stmt = con.createStatement();
			String q1 = "SELECT nameItem,qttItem FROM ITEM ORDER BY qttItem ASC LIMIT 6";
			ResultSet rs = stmt.executeQuery(q1); //check if executed
			while (rs.next()) {
				String ItemName = rs.getString("nameItem");
				int ItemQtt = rs.getInt("qttItem");
				LIST_ItemName.add(ItemName);
				LIST_ItemQtt.add(ItemQtt);
	        }
	        con.close();
		} 
		catch (SQLException exe) {exe.printStackTrace();return; }	
	}
	int READQttsql(String ItemName) {
		int ItemQtt = 0;
		try { 
			Connection con = DriverManager.getConnection(DB_URL,USER,PASS);
			Statement stmt = con.createStatement();
			
			String q1 = "SELECT qttItem FROM ITEM WHERE nameItem = '"+ItemName+"';";
			ResultSet rs = stmt.executeQuery(q1); //check if executed
			if (rs.next())
            {
				ItemQtt = rs.getInt(1);
				return ItemQtt; 
            }
	        con.close();
		} 
		catch (SQLException exe) {exe.printStackTrace(); }	
		return ItemQtt; 
	}
	void READAllsql(ArrayList <String> LIST_ItemName, ArrayList <Integer> LIST_ItemQtt) {
		try { 
			Connection con = DriverManager.getConnection(DB_URL,USER,PASS);
			Statement stmt = con.createStatement();
			
			String q1 = "SELECT nameItem, qttItem FROM ITEM ORDER BY qttItem ASC";
			ResultSet rs = stmt.executeQuery(q1);
			while (rs.next()) {
				String ItemName = rs.getString("nameItem");
				int ItemQtt = rs.getInt("qttItem");
				LIST_ItemName.add(ItemName);
				LIST_ItemQtt.add(ItemQtt);
	        }
	        con.close();
		} 
		catch (SQLException exe) {exe.printStackTrace();return; }	
	}
} 


public class InventoryManagement {
	
	static void CalculateLowestQtt(ArrayList <PANEL_Item> LIST_ItemPanels) {
		ArrayList <String> LIST_ItemName= new ArrayList<>();
		ArrayList <Integer> LIST_ItemQtt= new ArrayList<>();
		CONNECTsql database = new CONNECTsql();
		database.READ6sql(LIST_ItemName, LIST_ItemQtt);
		
		//send thoses names as button names and qtt in Panel Items order
		for (int i = 0; i < LIST_ItemPanels.size(); i++) { 
            if (i < LIST_ItemName.size()) {
            	LIST_ItemPanels.get(i).INPUT_InfoItem(LIST_ItemName.get(i), LIST_ItemQtt.get(i));
            } else {
                // less than 6 items - just empty
            	LIST_ItemPanels.get(i).INPUT_InfoItem("", 0);
            }
        }
	}
	
	static void RightBtnStyle(JButton Btn) {
		Btn.setHorizontalTextPosition(SwingConstants.CENTER);
		Btn.setBackground(new Color(0xE2E6F5));
		Btn.setFocusPainted(false);
	    Btn.setBorder(new CompoundBorder(
	            new LineBorder(new Color(0xFCFCFE), 2),   
	            new EmptyBorder(5, 12, 5, 12)             
	        ));
	    Btn.setPreferredSize(new Dimension(100, 30));
	}
	
	static void LowStockAlert(ArrayList <PANEL_Item> LIST_ItemPanels) {
		//Pop-up - only when app launch
		ArrayList <String> LIST_ItemName= new ArrayList<>();
		ArrayList <Integer> LIST_ItemQtt= new ArrayList<>();
		ArrayList <String> LIST_ItemName_Lowstock= new ArrayList<>();
		CONNECTsql database = new CONNECTsql();
		database.READAllsql(LIST_ItemName, LIST_ItemQtt);
		
		for (int i = 0; i < LIST_ItemPanels.size(); i++) { 
            if (LIST_ItemQtt.get(i) <= 10) {
            	LIST_ItemName_Lowstock.add(LIST_ItemName.get(i));            	
            }
            else {break;}
        }
		if (LIST_ItemName_Lowstock.size() != 0) {
			JOptionPane.showMessageDialog(null,"Low Stock for :"+ LIST_ItemName_Lowstock.toString(),"Low stock alert",JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	public static void main(String[] args){
		
		//INIT ====================================================
		ArrayList<String> LIST_ItemName= new ArrayList<>();	
		CONNECTsql database = new CONNECTsql();
		
		//MAIN INTERFACE ============================================================
		JFrame mainFrame = new JFrame ("INVENTORY");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setLocation(500, 300); 
		
		//RIGHT PANEL=============================================================
				
		JPanel RightPanel = new JPanel();	
		RightPanel.setBackground(new Color(0xE2E6F5));
		RightPanel.setPreferredSize(new Dimension (125, 300));
				
		//ComboBox ----
		database.READsql(LIST_ItemName);
		JComboBox<String> COMBOBOX_ItemNames = new JComboBox<>(); // Uneditable combobox --> use string[]
		COMBOBOX_ItemNames.setPreferredSize(new Dimension(100, 25));
		for (String n : LIST_ItemName) {COMBOBOX_ItemNames.addItem(n);}
		
		COMBOBOX_ItemNames.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String selectedName = (String) COMBOBOX_ItemNames.getSelectedItem();
					new FRAME_UPDATE_Item(selectedName);
					}
				}			
			);
		//--
		
		//ADD BUTTON ----
		JButton btn_ADD_Item = new JButton("ADD");		
		RightBtnStyle(btn_ADD_Item);	
		btn_ADD_Item.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					new FRAME_ADD_Item();	
				}
			}			
		);
		//--
		
		//DELETE BUTTON ----
		JButton btn_DEL_Item = new JButton("REMOVE");
		RightBtnStyle(btn_DEL_Item);	
		btn_DEL_Item.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					new FRAME_DEL_Item();
				}
			}			
		);
		//--
		
		//EXIT BUTTON ----
		JButton btn_EXIT_APP = new JButton("EXIT");
		RightBtnStyle(btn_EXIT_APP);	
		btn_EXIT_APP.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					mainFrame.setVisible(false);
					mainFrame.dispose();
					System.exit(0);
				}
			}			
		);
		//--
								
		RightPanel.add(COMBOBOX_ItemNames);
		RightPanel.add(btn_ADD_Item);
		RightPanel.add(btn_DEL_Item);
		RightPanel.add(btn_EXIT_APP);
		
		//LEFT PANEL=============================================================
		
		JPanel LeftPanel = new JPanel();
		LeftPanel.setBackground(new Color(0xFCFCFE));
		LeftPanel.setPreferredSize(new Dimension (375, 300));
		LeftPanel.setLayout(new GridLayout(3, 3, 20,20));
				
		//ITEM PANELS ----
		ArrayList <PANEL_Item> LIST_ItemPanels= new ArrayList<>(); //An array list of 6 panels is easier to reorganize 
		for (int i =0; i<6; i++) {
			PANEL_Item nItemPanel = new PANEL_Item();
			LIST_ItemPanels.add(nItemPanel);
			LeftPanel.add(nItemPanel);
		}
		//--
		
		//need to set-up refresh btn in main - else refresh cannot interact with LeftPanel
		//REFRESH BUTTON ----
		JButton btn_REFRESH_Display = new JButton("REFRESH"); 
		btn_REFRESH_Display.setHorizontalTextPosition(SwingConstants.CENTER);
		btn_REFRESH_Display.setBackground(new Color(0xFCFCFE));
		btn_REFRESH_Display.setFocusPainted(false);
		btn_REFRESH_Display.setBorderPainted(false);
		//--------------
		btn_REFRESH_Display.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					CalculateLowestQtt(LIST_ItemPanels);
					//Remove and Add for combobox --> need to click save - however save is defined somewhere else 
					// to difficult to set-up super event track - so Combobox update when hit refresh
					LIST_ItemName.clear();
					database.READsql(LIST_ItemName);
					String[] STRING_ItemName = LIST_ItemName.toArray(new String[0]);
					
					DefaultComboBoxModel<String> ComboBBuffer = new DefaultComboBoxModel<>(STRING_ItemName);
					COMBOBOX_ItemNames.setModel(ComboBBuffer); // replace model - do not trigger action event

				}
			}			
		);
		//-- 
		
		//Empty - make things pretty
		JPanel Null = new JPanel();
		Null.setBackground(new Color(0xFCFCFE));
		
		LeftPanel.add(Null);
		LeftPanel.add(btn_REFRESH_Display);
		
		//refresh when launch 
		CalculateLowestQtt(LIST_ItemPanels);
		LowStockAlert(LIST_ItemPanels);
		
		//PRIMARY PANEL===============================================================
		JPanel primary = new JPanel();
		primary.setBackground(new Color(0xFCFCFE));
		primary.add(LeftPanel);
		primary.add(RightPanel);
				
		//final 
		mainFrame.getContentPane().add(primary);
		mainFrame.pack();
		mainFrame.setVisible(true);
		  
	}
}
