package warranty_helper;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.jaunt.NotFound;
import com.jaunt.ResponseException;
import com.jaunt.UserAgent;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.awt.event.ItemEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class WarrantyHelper {

	private JFrame frame;
	public static JTextField txtTx;
	private JTextField txtWO;
	public static boolean ip = false;
	public static boolean date = false;
	private JTextField txtRow;
	public static ProcessBuilder pb;
	private JTextField txtSheetID;
	private JTextField txtSheetName;
	private JTextField txtWo;
	private JTextField txtX;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WarrantyHelper window = new WarrantyHelper();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public WarrantyHelper() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frame = new JFrame("Warranty Helper - Levi wuz here");
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JButton btnComplete = new JButton("Complete");
		
		JLabel lblAbbreviatedStateW = new JLabel("Abbreviated state w/ site #");
		
		txtTx = new JTextField();
		txtTx.setText("TX2744");
		txtTx.setColumns(9);
		
		JButton btnSendToRyan = new JButton("Send to Ryan");
		btnSendToRyan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// sheetson write "Send to Ryan TX2722 [no IP] [no commission date]"
				String note = "Send to Ryan ";
				note = note + WarrantyHelper.txtTx.getText();
				if(ip) note = note + "no IP ";
				if(date) note = note + "no commission date";
				
				// TODO implement sheetson
				// UPDATE ROW WITH CURL INPUT
				// requires process builder
				String curlUpdateRow = "curl -X PUT \\ -H \"X-Sheetson-Spreadsheet-Id: " + txtSheetID.getText() + "\" \\ -d \'{\"" + txtWO.getText() + "\": \"" + note + "\"}\' \\ https://api.sheetson.com/v1/sheets/" + txtSheetName.getText() + "/" + txtRow.getText();
			}
		});
		
		JCheckBox chckbxNoCommissionDate = new JCheckBox("No commission date");
		chckbxNoCommissionDate.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {//checkbox has been selected
		            WarrantyHelper.date = true;
		        } else {//checkbox has been deselected
		            WarrantyHelper.date = false;
		        };
			}
		});
		
		JCheckBox chckbxNoIpAddress = new JCheckBox("No IP address");
		chckbxNoIpAddress.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {//checkbox has been selected
		            WarrantyHelper.ip = true;
		        } else {//checkbox has been deselected
		            WarrantyHelper.ip = false;
		        };
			}
		});
		
		JButton btnNext = new JButton("Next");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtRow.setText(String.valueOf(Integer.valueOf(txtRow.getText())+1));
				//String retrieveRow = "curl -X GET \\ -H \"X-Sheetson-Spreadsheet-Id: " + txtSheetID.getText() + "\" \\ -H \"Content-Type: application/json\" \\ -d \"Content-Type: application/json\" \\ https://api.sheetson.com/v1/sheets/" + txtSheetName.getText() + "/" + txtRow.getText();
				String retrieveRow = "curl -X GET \\ -H \"X-Sheetson-Spreadsheet-Id: 16ZP4uBU7SaAH_Jlh9PpO2v_R1O7KaTM0e2Y3m_lZseQ\" \\ https://api.sheetson.com/v1/sheets/" + txtSheetName.getText();
				System.out.println(retrieveRow);
				pb = new ProcessBuilder(retrieveRow.split(" "));
				pb.directory(new File("/home/"));
				try {
					// this gave error "java.io.IOException: Cannot run program "curl" (in directory "\home"): CreateProcess error=267, The directory name is invalid"
					//trying simple alternative found here https://www.baeldung.com/java-curl
					//Process p = pb.start();
					Process p = Runtime.getRuntime().exec(retrieveRow);
					InputStream inputStream = p.getInputStream();
					
					ArrayList<String> streamResult = new ArrayList<String>();
					Scanner s = new Scanner(inputStream).useDelimiter("\\A");
					int i = 0;
					while(s.hasNext()) {
						streamResult.add(s.nextLine());
						System.out.println(streamResult.get(i++));
					}
					
					s.close();
					int exitCode = p.exitValue();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					System.out.println("Sorry, failed to start process from process builder");
				}
				// this failed, trying web scraping instead
				// using jaunt
				/*UserAgent ua = new UserAgent();
				String site = "http://api.sheetson.com/v1/sheets/" + txtSheetName.getText() + "?spreadsheetId=" + txtSheetID.getText();
				//String site = "http://oracle.com";
				System.out.println(site);
				try {
					ua.visit(site);
					if(!ua.doc.equals(null))
						System.out.println(ua.doc.innerHTML());
				} catch (ResponseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}*/
				// ... have to use THEIR method
			}
		});
		
		JLabel lblCurrentWo = new JLabel("Current WO#");
		
		txtWO = new JTextField();
		txtWO.setText("202722");
		txtWO.setColumns(9);
		
		txtRow = new JTextField("1");
		txtRow.setColumns(9);
		
		JLabel lblStartAtLine = new JLabel("Start at line #...");
		
		JLabel lblSheetId = new JLabel("Sheet ID");
		
		JLabel lblSheetName = new JLabel("Sheet name");
		
		txtSheetID = new JTextField();
		txtSheetID.setText("16ZP4uBU7SaAH_Jlh9PpO2v_R1O7KaTM0e2Y3m_lZseQ");
		txtSheetID.setColumns(10);
		
		txtSheetName = new JTextField();
		txtSheetName.setText("Sheet1");
		txtSheetName.setColumns(10);
		
		txtWo = new JTextField();
		txtWo.setText("WO#");
		txtWo.setColumns(10);
		
		txtX = new JTextField();
		txtX.setText("x");
		txtX.setColumns(10);
		
		JLabel lblColaname = new JLabel("ColAName");
		
		JLabel lblColbname = new JLabel("ColBName");
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addComponent(chckbxNoIpAddress)
								.addComponent(chckbxNoCommissionDate)
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
									.addGroup(groupLayout.createSequentialGroup()
										.addComponent(btnComplete)
										.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(btnSendToRyan))
									.addGroup(groupLayout.createSequentialGroup()
										.addComponent(lblAbbreviatedStateW)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(txtTx, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
							.addPreferredGap(ComponentPlacement.RELATED, 121, Short.MAX_VALUE)
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addComponent(btnNext)
								.addComponent(txtWO, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblCurrentWo)
								.addComponent(txtRow, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addComponent(lblStartAtLine, Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblSheetId)
							.addGap(18)
							.addComponent(txtSheetID, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 132, Short.MAX_VALUE)
							.addComponent(lblColaname)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(txtWo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblSheetName)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(txtSheetName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 131, Short.MAX_VALUE)
							.addComponent(lblColbname)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(txtX, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblSheetId)
						.addComponent(txtSheetID, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtWo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblColaname))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblSheetName)
						.addComponent(txtSheetName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtX, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblColbname))
					.addPreferredGap(ComponentPlacement.RELATED, 77, Short.MAX_VALUE)
					.addComponent(lblStartAtLine)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(chckbxNoIpAddress)
						.addComponent(txtRow, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(chckbxNoCommissionDate)
						.addComponent(lblCurrentWo))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblAbbreviatedStateW)
						.addComponent(txtTx, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtWO, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnComplete)
						.addComponent(btnSendToRyan)
						.addComponent(btnNext))
					.addContainerGap())
		);
		frame.getContentPane().setLayout(groupLayout);
	}
}
