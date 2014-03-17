package com.wuseguang.web.utils.app;

import java.awt.EventQueue;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextPane;
import javax.swing.JTextArea;
import javax.swing.JPanel;

import java.awt.Font;
import java.awt.Color;

import javax.swing.UIManager;

import com.sun.codemodel.JAnnotationArrayMember;
import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class CodeModelApp {

	private JFrame frame;
	private JTextField projectLocationField;
	private JTextField basePackageField;
	private JTextArea textArea;
	private JPanel panel_columns;

	// private List<JCheckBox> columns = new ArrayList<JCheckBox>();
	// private List<String> fieldNames=new ArrayList<String>();
	private Map<String, JTextField> name2label = new HashMap<String, JTextField>();
	public Map<String, JTextField> getName2label() {
		return name2label;
	}

	public void setName2label(Map<String, JTextField> name2label) {
		this.name2label = name2label;
	}

	private JTextField pojoField;
	private JPanel name2labelPanel;
	private ServletGenerator servletGenerator=null;
	private HtmlGenerator htmlGenerator=null;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CodeModelApp window = new CodeModelApp();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * 
	 * @throws IOException
	 */
	public CodeModelApp() throws IOException {
		initialize();
		
		//System.out.println(templatePath);
		
		this.servletGenerator=new ServletGenerator(this);
		this.htmlGenerator=new HtmlGenerator(this);
		// System.out.println(insertTemplate);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(800, 200, 1104, 490);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblWeb = new JLabel("web代码自动生成器");
		lblWeb.setForeground(UIManager
				.getColor("CheckBoxMenuItem.acceleratorForeground"));
		lblWeb.setFont(new Font("Dialog", Font.BOLD, 16));
		lblWeb.setBounds(206, 11, 223, 17);
		frame.getContentPane().add(lblWeb);

		JLabel label = new JLabel("项目路径：");
		label.setBounds(12, 54, 94, 17);
		frame.getContentPane().add(label);

		projectLocationField = new JTextField();
		projectLocationField.setText("/home/suqing/git/report/ReportSystem");
		projectLocationField.setBounds(93, 49, 362, 21);
		frame.getContentPane().add(projectLocationField);
		projectLocationField.setColumns(10);

		JLabel lblDao = new JLabel("项目包:");
		lblDao.setBounds(12, 94, 59, 17);
		frame.getContentPane().add(lblDao);

		basePackageField = new JTextField();
		basePackageField.setText("com.wuseguang.report");
		basePackageField.setBounds(93, 94, 362, 21);
		frame.getContentPane().add(basePackageField);
		basePackageField.setColumns(10);

		JButton button = new JButton("生成");
		
		button.setBounds(364, 238, 102, 27);
		frame.getContentPane().add(button);

		JLabel label_ = new JLabel("控制台：");
		label_.setBounds(12, 262, 59, 17);
		frame.getContentPane().add(label_);

		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setBounds(22, 286, 435, 114);
		frame.getContentPane().add(textArea);

		JLabel label_1 = new JLabel("列名选择：");
		label_1.setBounds(524, 193, 551, 17);
		frame.getContentPane().add(label_1);

		panel_columns = new JPanel();
		panel_columns.setBounds(526, 207, 549, 114);
		frame.getContentPane().add(panel_columns);

		// JCheckBox chckbxNewCheckBox = new JCheckBox("New check box");
		// panel_columns.add(chckbxNewCheckBox);

		JButton button_1 = new JButton("解析");
		button_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					analyseCode();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		button_1.setBounds(235, 238, 102, 27);
		frame.getContentPane().add(button_1);

		JLabel lblPojo = new JLabel("pojo:");
		lblPojo.setBounds(12, 138, 59, 17);
		frame.getContentPane().add(lblPojo);

		pojoField = new JTextField();
		pojoField.setText("EntShareholder");
		pojoField.setBounds(93, 136, 364, 21);
		frame.getContentPane().add(pojoField);
		pojoField.setColumns(10);

		JLabel label_2 = new JLabel("列名标签：");
		label_2.setBounds(524, 31, 551, 17);
		frame.getContentPane().add(label_2);

		name2labelPanel = new JPanel();
		name2labelPanel.setBounds(524, 50, 551, 114);
		frame.getContentPane().add(name2labelPanel);
		
		final JCheckBox chckbxInsert = new JCheckBox("insert");
		chckbxInsert.setBounds(26, 179, 74, 25);
		frame.getContentPane().add(chckbxInsert);
		
		final JCheckBox chckbxUpdate = new JCheckBox("update");
		chckbxUpdate.setBounds(108, 178, 66, 25);
		frame.getContentPane().add(chckbxUpdate);
		
		final JCheckBox chckbxSearch = new JCheckBox("search");
		chckbxSearch.setBounds(176, 178, 83, 25);
		frame.getContentPane().add(chckbxSearch);
		
		final JCheckBox chckbxDelete = new JCheckBox("delete");
		chckbxDelete.setBounds(259, 178, 75, 25);
		frame.getContentPane().add(chckbxDelete);
		
		JCheckBox chckbxInfo = new JCheckBox("info");
		chckbxInfo.setBounds(341, 178, 115, 25);
		frame.getContentPane().add(chckbxInfo);
		
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				try {
					if(chckbxInsert.isSelected()){
						servletGenerator.generateInserServlet();
						htmlGenerator.generateInsertHtml();
					}
					if(chckbxUpdate.isSelected()){
						servletGenerator.generateUpdateServlet();
						htmlGenerator.generateUpdateHtml();
					}
					if(chckbxSearch.isSelected()){
						servletGenerator.generateSearchServlet();
					}
					if(chckbxDelete.isSelected()){
						servletGenerator.generateDeleteServlet();
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
	}

	protected void analyseCode() throws Exception {
		String baseDir = this.getBaseDir();
		// String daoName = this.getDaoName();
		String pojoName = this.getPojoName();
		// String pojoShortName=this.getPojoShortName();
		String classesDir = this.getClassesDir();// +daoName.replaceAll("\\.",
													// "/")+".class";
		String srcDir = this.getSrcDir();
		File f = new File(classesDir);
		ClassLoader cl = new URLClassLoader(new URL[] { f.toURI().toURL() });
		// Class daoClass=cl.loadClass(daoName);
		Class pojoClass = cl.loadClass(pojoName);
		// fieldNames.clear();
		panel_columns.removeAll();
		// columns.clear();
		for (Field field : pojoClass.getDeclaredFields()) {
			// fieldNames.add(field.getName());
			JCheckBox checkbox = new JCheckBox(field.getName());
			// checkbox.setVisible(true);

			checkbox.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent event) {
					JCheckBox checkbox = (JCheckBox) event.getSource();
					if (checkbox.isSelected()) {
						JTextField textField = new JTextField(checkbox
								.getText());
						textField.setSize(100, 21);
						name2label.put(checkbox.getText(), textField);
						textArea.setText(checkbox.getText() + "选中");
					} else {
						name2label.remove(checkbox.getText());
						textArea.setText(checkbox.getText() + "未选中");
					}
					updateName2LabelPanel();
				}
			});

			textArea.setText(field.getName());
			// columns.add(checkbox);
			panel_columns.add(checkbox);
			// panel_columns.repaint();
			//checkbox.repaint();
		}

		// frame.repaint();
		panel_columns.updateUI();
		// panel_columns.repaint();
		// textArea.setText(""+panel_columns.countComponents());
	}

	String getPojoName() {
		return getBasePackage() + ".db.pojo." + getPojo();
	}

	String getPojo() {
		return pojoField.getText();
	}

	String getBasePackage() {
		return basePackageField.getText();
	}

	private String getBaseDir() {
		return projectLocationField.getText();
	}

	private String getClassesDir() {
		return getBaseDir() + "/build/classes/";
	}

	String getSrcDir() {
		return getBaseDir() + "/src/";
	}

	String getLowerPojo() {
		return this.getPojo().substring(0, 1).toLowerCase()
				+ this.getPojo().substring(1);
	}

	String getUrlPattern() {
		return "/api/" + this.getLowerPojo() + "/";
	}

	String getDaoName() {
		return this.getBasePackage() + ".db.dao." + this.getPojo() + "Mapper";
	}

	String getInsertServletName() {
		return this.getBasePackage() + ".servlet.codeModel." + this.getLowerPojo()
				+ "." + this.getPojo() + "InsertServlet";
	}
	String getUpdateServletName(){
		return this.getBasePackage() + ".servlet.codeModel." + this.getLowerPojo()
				+ "." + this.getPojo() + "UpdateServlet";
	}
	String getSearchServletName(){
		return this.getBasePackage() + ".servlet.codeModel." + this.getLowerPojo()
				+ "." + this.getPojo() + "SearchServlet";
	}
	String getDeleteServletName(){
		return this.getBasePackage() + ".servlet.codeModel." + this.getLowerPojo()
				+ "." + this.getPojo() + "DeleteServlet";
	}
	String getInfoServletName(){
		return this.getBasePackage() + ".servlet.codeModel." + this.getLowerPojo()
				+ "." + this.getPojo() + "InfoServlet";
	}
	String getWidgetPath(){
		return this.getBaseDir()+"/WebContent/widgets/"+this.getLowerPojo()+"/";
	}
	String getComment(){
		final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String comment="由CodeModel-web工具自动生成，生成时间："+df.format(new Date());
		return comment;
	}



	

	public JPanel getPanel_columns() {
		return panel_columns;
	}

	private String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return encoding.decode(ByteBuffer.wrap(encoded)).toString();
	}

	String readFile(String path) throws IOException {
		return readFile(path, StandardCharsets.UTF_8);
	}
	void writeFile(String filename,String content){
		File dir = new File(this.getWidgetPath());
		if (!dir.exists())
			dir.mkdirs();
		FileWriter writer;
		try {
			writer = new FileWriter(this.getWidgetPath() + filename);
			writer.write(content);
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(content);
	}
	private void updateName2LabelPanel() {
		name2labelPanel.removeAll();
		for (Entry<String, JTextField> entry : name2label.entrySet()) {
			name2labelPanel.add(new JLabel(entry.getKey() + ":"));
			name2labelPanel.add(entry.getValue());
		}
		name2labelPanel.updateUI();
	}

	public JPanel getName2labelPanel() {
		return name2labelPanel;
	}
	public Set<String> getColumns(){
		return name2label.keySet();
	}
	public String getJoinColumns(){
		return clc2String(this.getColumns());
	}
	private String clc2String(Collection<String> clc){
		StringBuffer sb=new StringBuffer();
		for(String item:clc){
			sb.append(",").append(CodeModelApp.camel4underline(item));
		}
		sb.deleteCharAt(0);
		return sb.toString();
	}

	public static String camel4underline(String s){
		return s.replaceAll(
			      String.format("%s|%s|%s",
			         "(?<=[A-Z])(?=[A-Z][a-z])",
			         "(?<=[^A-Z])(?=[A-Z])",
			         "(?<=[A-Za-z])(?=[^A-Za-z])"
			      ),
			      "_"
			   ).toLowerCase();
	}

}
