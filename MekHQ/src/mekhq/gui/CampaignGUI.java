/*
 * MekHQView.java
 *
 * Copyright (c) 2009 Jay Lawson <jaylawson39 at yahoo.com>. All rights reserved.
 *
 * This file is part of MekHQ.
 *
 * MekHQ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MekHQ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MekHQ.  If not, see <http://www.gnu.org/licenses/>.
 */
package mekhq.gui;

import gd.xml.ParseException;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import chat.ChatClient;

import megamek.client.ui.swing.MechTileset;
import megamek.client.ui.swing.MechView;
import megamek.client.ui.swing.util.PlayerColors;
import megamek.common.AmmoType;
import megamek.common.Entity;
import megamek.common.EntityListFile;
import megamek.common.EntityWeightClass;
import megamek.common.Mech;
import megamek.common.MechFileParser;
import megamek.common.MechSummary;
import megamek.common.MechSummaryCache;
import megamek.common.MiscType;
import megamek.common.Mounted;
import megamek.common.Pilot;
import megamek.common.Player;
import megamek.common.Tank;
import megamek.common.TargetRoll;
import megamek.common.TechConstants;
import megamek.common.UnitType;
import megamek.common.WeaponType;
import megamek.common.XMLStreamParser;
import megamek.common.loaders.BLKFile;
import megamek.common.loaders.EntityLoadingException;
import megamek.common.options.IOption;
import megamek.common.options.PilotOptions;
import megamek.common.util.DirectoryItems;
import megameklab.com.util.UnitPrintManager;
import mekhq.MekHQ;
import mekhq.Utilities;
import mekhq.campaign.Campaign;
import mekhq.campaign.Force;
import mekhq.campaign.JumpPath;
import mekhq.campaign.PartInventory;
import mekhq.campaign.Planet;
import mekhq.campaign.ResolveScenarioTracker;
import mekhq.campaign.Unit;
import mekhq.campaign.finances.Transaction;
import mekhq.campaign.mission.Contract;
import mekhq.campaign.mission.Mission;
import mekhq.campaign.mission.Scenario;
import mekhq.campaign.parts.AmmoBin;
import mekhq.campaign.parts.Armor;
import mekhq.campaign.parts.EnginePart;
import mekhq.campaign.parts.EquipmentPart;
import mekhq.campaign.parts.MekActuator;
import mekhq.campaign.parts.MekGyro;
import mekhq.campaign.parts.MekLifeSupport;
import mekhq.campaign.parts.MekLocation;
import mekhq.campaign.parts.MekSensor;
import mekhq.campaign.parts.Part;
import mekhq.campaign.parts.Refit;
import mekhq.campaign.parts.TankLocation;
import mekhq.campaign.personnel.Person;
import mekhq.campaign.personnel.SkillType;
import mekhq.campaign.work.IAcquisitionWork;
import mekhq.campaign.work.Modes;
import mekhq.gui.dialog.AddFundsDialog;
import mekhq.gui.dialog.CampaignOptionsDialog;
import mekhq.gui.dialog.CompleteMissionDialog;
import mekhq.gui.dialog.CustomizeMissionDialog;
import mekhq.gui.dialog.CustomizePersonDialog;
import mekhq.gui.dialog.CustomizeScenarioDialog;
import mekhq.gui.dialog.DataLoadingDialog;
import mekhq.gui.dialog.GameOptionsDialog;
import mekhq.gui.dialog.MekHQAboutBox;
import mekhq.gui.dialog.MekViewDialog;
import mekhq.gui.dialog.MissionTypeDialog;
import mekhq.gui.dialog.NewKillDialog;
import mekhq.gui.dialog.PartsStoreDialog;
import mekhq.gui.dialog.PopupValueChoiceDialog;
import mekhq.gui.dialog.PortraitChoiceDialog;
import mekhq.gui.dialog.ResolveWizardChooseFilesDialog;
import mekhq.gui.dialog.TextAreaDialog;
import mekhq.gui.dialog.UnitSelectorDialog;
import mekhq.gui.view.ContractViewPanel;
import mekhq.gui.view.ForceViewPanel;
import mekhq.gui.view.JumpPathViewPanel;
import mekhq.gui.view.MissionViewPanel;
import mekhq.gui.view.PersonViewPanel;
import mekhq.gui.view.PlanetViewPanel;
import mekhq.gui.view.ScenarioViewPanel;
import mekhq.gui.view.UnitViewPanel;

/**
 * The application's main frame.
 */
public class CampaignGUI extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -687162569841072579L;
	
	//personnel filter groups
	private static final int PG_ACTIVE =  0;
	private static final int PG_COMBAT =  1;
	private static final int PG_SUPPORT = 2;
	private static final int PG_MW =      3;
	private static final int PG_CREW =    4;
	private static final int PG_PILOT =   5;
	private static final int PG_PROTO =   6;
	private static final int PG_BA =      7;
	private static final int PG_TECH =    8;
	private static final int PG_DOC =     9;
	private static final int PG_RETIRE =  10;
	private static final int PG_MIA =     11;
	private static final int PG_KIA =     12;
	private static final int PG_NUM =     13;
	
	//parts filter groups
	private static final int SG_ALL      = 0;
	private static final int SG_ARMOR    = 1;
	private static final int SG_SYSTEM   = 2;
	private static final int SG_EQUIP    = 3;
	private static final int SG_LOC      = 4;
	private static final int SG_WEAP     = 5;
	private static final int SG_AMMO     = 6;
	private static final int SG_MISC     = 7;
	private static final int SG_ENGINE   = 8;
	private static final int SG_GYRO     = 9;
	private static final int SG_ACT      = 10;
	private static final int SG_DAMAGE   = 11;
	private static final int SG_NUM      = 12;
	
	//personnel views
	private static final int PV_GENERAL = 0;
	private static final int PV_PILOT   = 1;
	private static final int PV_INF     = 2;
	private static final int PV_TACTIC  = 3;
	private static final int PV_TECH    = 4;
	private static final int PV_ADMIN   = 5;
	private static final int PV_FLUFF   = 6;
	private static final int PV_NUM     = 7;
	
	//unit views
	private static final int UV_GENERAL = 0;
	private static final int UV_DETAILS = 1;
	private static final int UV_STATUS  = 2;
	private static final int UV_NUM     = 3;

	private JFrame frame;
	
	private MekHQ app;
	
	private TaskTableModel taskModel = new TaskTableModel();
	private AcquisitionTableModel acquireModel = new AcquisitionTableModel();
	private ServicedUnitTableModel servicedUnitModel = new ServicedUnitTableModel();
	private TechTableModel techsModel = new TechTableModel();
	private PatientTableModel patientModel = new PatientTableModel();
	private DocTableModel doctorsModel = new DocTableModel();
	private PersonnelTableModel personModel = new PersonnelTableModel();
	private UnitTableModel unitModel = new UnitTableModel();
	private PartsTableModel partsModel = new PartsTableModel();
	private FinanceTableModel financeModel = new FinanceTableModel();
	private ScenarioTableModel scenarioModel = new ScenarioTableModel();
	private OrgTreeModel orgModel;
	private UnitTableMouseAdapter unitMouseAdapter;
	private ServicedUnitsTableMouseAdapter servicedUnitMouseAdapter;
	private PartsTableMouseAdapter partsMouseAdapter;
	private TaskTableMouseAdapter taskMouseAdapter;
	private PersonnelTableMouseAdapter personnelMouseAdapter;
	private OrgTreeMouseAdapter orgMouseAdapter;
	private ScenarioTableMouseAdapter scenarioMouseAdapter;
	private TableRowSorter<PersonnelTableModel> personnelSorter;
	private TableRowSorter<PartsTableModel> partsSorter;
	private TableRowSorter<UnitTableModel> unitSorter;
	private int currentServicedUnitId;
	private int currentAcquisitionId;
	private int currentTechId;
	private int currentPatientId;
	private int currentDoctorId;
	private int currentServiceablePartsId;
	private int[] selectedTasksIds;
		
	public int selectedMission = -1;
	
	public CampaignGUI(MekHQ app) {
		
		this.app = app;

		unitMouseAdapter = new UnitTableMouseAdapter();
		servicedUnitMouseAdapter = new ServicedUnitsTableMouseAdapter();
		partsMouseAdapter = new PartsTableMouseAdapter();
		taskMouseAdapter = new TaskTableMouseAdapter();
		personnelMouseAdapter = new PersonnelTableMouseAdapter(this);
		orgMouseAdapter = new OrgTreeMouseAdapter();
		scenarioMouseAdapter = new ScenarioTableMouseAdapter();
       
		initComponents();
	}

	public void showAboutBox() {
		if (aboutBox == null) {
			aboutBox = new MekHQAboutBox(getFrame());
			aboutBox.setLocationRelativeTo(getFrame());
		}
		aboutBox.setVisible(true);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	private void initComponents() {
		java.awt.GridBagConstraints gridBagConstraints;

		frame = new JFrame("MekHQ"); //$NON-NLS-1$
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		mainPanel = new javax.swing.JPanel();
		tabMain = new javax.swing.JTabbedPane();
		tabTasks = new javax.swing.JTabbedPane();
		panOrganization = new javax.swing.JPanel();
		scrollOrgTree = new javax.swing.JScrollPane();
		orgTree = new javax.swing.JTree();
		panBriefing = new javax.swing.JPanel();
		panelScenario = new javax.swing.JPanel();
		panelMapView = new javax.swing.JPanel();
		lblFindPlanet = new javax.swing.JLabel();
		btnCalculateJumpPath = new javax.swing.JButton();
		btnBeginTransit = new javax.swing.JButton();
		scrollScenarioTable = new javax.swing.JScrollPane();
		scenarioTable = new javax.swing.JTable();
		scrollMissionView = new javax.swing.JScrollPane();
		scrollScenarioView = new javax.swing.JScrollPane();
		panelMissionButtons = new javax.swing.JPanel();
		lblMission = new javax.swing.JLabel();
		choiceMission = new javax.swing.JComboBox();
		btnAddMission = new javax.swing.JButton();
		btnEditMission = new javax.swing.JButton();
		btnCompleteMission = new javax.swing.JButton();
		panelScenarioButtons = new javax.swing.JPanel();
		btnAddScenario = new javax.swing.JButton();
		btnStartGame = new javax.swing.JButton();
		btnLoadGame = new javax.swing.JButton();
		btnPrintRS = new javax.swing.JButton();
		btnGetMul = new javax.swing.JButton();
		btnClearAssignedUnits = new javax.swing.JButton();
		btnResolveScenario = new javax.swing.JButton();
		panPersonnel = new javax.swing.JPanel();
		scrollPersonnelTable = new javax.swing.JScrollPane();
		personnelTable = new javax.swing.JTable();
		panHangar = new javax.swing.JPanel();
		scrollUnitTable = new javax.swing.JScrollPane();
		unitTable = new javax.swing.JTable();
		panRepairBay = new javax.swing.JPanel();
		scrollTaskTable = new javax.swing.JScrollPane();
		TaskTable = new javax.swing.JTable();
		scrollAcquisitionTable = new javax.swing.JScrollPane();
		AcquisitionTable = new javax.swing.JTable();
		scrollServicedUnitTable = new javax.swing.JScrollPane();
		servicedUnitTable = new javax.swing.JTable();
		scrollTechTable = new javax.swing.JScrollPane();
		TechTable = new javax.swing.JTable();
		panelDoTask = new javax.swing.JPanel();
		btnDoTask = new javax.swing.JButton();
		lblTarget = new javax.swing.JLabel();
		lblTargetNum = new javax.swing.JLabel();
		astechPoolLabel = new javax.swing.JLabel();
		jScrollPane6 = new javax.swing.JScrollPane();
		textTarget = new javax.swing.JTextArea();
		panSupplies = new javax.swing.JPanel();
		scrollPartsTable = new javax.swing.JScrollPane();
		partsTable = new javax.swing.JTable();
		panInfirmary = new javax.swing.JPanel();
		btnAssignDoc = new javax.swing.JButton();
		scrollPatientTable = new javax.swing.JScrollPane();
		patientTable = new javax.swing.JTable();
		scrollDocTable = new javax.swing.JScrollPane();
		DocTable = new javax.swing.JTable();
		panFinances = new javax.swing.JPanel();
		scrollFinanceTable = new javax.swing.JScrollPane();
		financeTable = new javax.swing.JTable();
		txtPaneReportScrollPane = new javax.swing.JScrollPane();
		txtPaneReport = new javax.swing.JTextPane();
		btnAdvanceDay = new javax.swing.JButton();
		btnOvertime = new javax.swing.JToggleButton();
		btnGMMode = new javax.swing.JToggleButton();
		lblFunds = new javax.swing.JLabel();
		lblTempAstechs = new javax.swing.JLabel();
		lblTempMedics = new javax.swing.JLabel();
		menuBar = new javax.swing.JMenuBar();
		javax.swing.JMenu fileMenu = new javax.swing.JMenu();
		menuLoad = new javax.swing.JMenuItem();
		menuSave = new javax.swing.JMenuItem();
		menuOptions = new javax.swing.JMenuItem();
		menuOptionsMM = new javax.swing.JMenuItem();
		menuThemes = new javax.swing.JMenu();
		javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
		menuManage = new javax.swing.JMenu();
		miLoadForces = new javax.swing.JMenuItem();
		addFunds = new javax.swing.JMenuItem();
		menuMarket = new javax.swing.JMenu();
		miPurchaseUnit = new javax.swing.JMenuItem();
		miBuyParts = new javax.swing.JMenuItem();
		menuHire = new javax.swing.JMenu();
		miHireAstechs = new javax.swing.JMenuItem();
		miFireAstechs = new javax.swing.JMenuItem();
		miFireAllAstechs = new javax.swing.JMenuItem();
		menuAstechPool = new javax.swing.JMenu();
		miFullStrengthAstechs = new javax.swing.JMenuItem();
		miHireMedics = new javax.swing.JMenuItem();
		miFireMedics = new javax.swing.JMenuItem();
		miFireAllMedics = new javax.swing.JMenuItem();
		menuMedicPool = new javax.swing.JMenu();
		miFullStrengthMedics = new javax.swing.JMenuItem();
		menuCommunity = new javax.swing.JMenu();
		miChat = new javax.swing.JMenuItem();
		javax.swing.JMenu helpMenu = new javax.swing.JMenu();
		javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
		statusPanel = new javax.swing.JPanel();
		lblPersonChoice = new javax.swing.JLabel();
		choicePerson = new javax.swing.JComboBox();
		choicePersonView = new javax.swing.JComboBox();
		lblPersonView = new javax.swing.JLabel();
		scrollPersonnelView = new javax.swing.JScrollPane();
		lblUnitChoice = new javax.swing.JLabel();
		choiceUnit = new javax.swing.JComboBox();
		choiceUnitView = new javax.swing.JComboBox();
		lblUnitView = new javax.swing.JLabel();
		scrollUnitView = new javax.swing.JScrollPane();
		scrollForceView = new javax.swing.JScrollPane();
		scrollPlanetView = new javax.swing.JScrollPane();
		lblPartsChoice = new javax.swing.JLabel();
		choiceParts = new javax.swing.JComboBox();
		panMekLab = new MekLabPanel(this);
		scrollMekLab = new javax.swing.JScrollPane();
		lblLocation = new javax.swing.JLabel();

		ResourceBundle resourceMap = ResourceBundle.getBundle("mekhq.resources.CampaignGUI");
		tabMain.setToolTipText(resourceMap.getString("tabMain.toolTipText")); // NOI18N
		tabMain.setMinimumSize(new java.awt.Dimension(600, 200));
		tabMain.setName("tabMain"); // NOI18N
		tabMain.setPreferredSize(new java.awt.Dimension(900, 300));

		panOrganization.setName("panOrganization"); // NOI18N
		panOrganization.setLayout(new java.awt.GridBagLayout());
		
		//refreshOrganization();
		orgModel = new OrgTreeModel(getCampaign().getForces());
		orgTree.setModel(orgModel);
		orgTree.addMouseListener(orgMouseAdapter);
        orgTree.setCellRenderer(new ForceRenderer());
        orgTree.setRowHeight(50);
        orgTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        orgTree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
               refreshForceView();
            }
        });
		scrollOrgTree.setViewportView(orgTree);
		
		scrollForceView.setMinimumSize(new java.awt.Dimension(550, 600));
		scrollForceView.setPreferredSize(new java.awt.Dimension(550, 600));
		scrollForceView.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollForceView.setViewportView(null);
		
		splitOrg = new javax.swing.JSplitPane(javax.swing.JSplitPane.HORIZONTAL_SPLIT,scrollOrgTree, scrollForceView);
		splitOrg.setOneTouchExpandable(true);
		splitOrg.setResizeWeight(1.0);
		splitOrg.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent pce) {
				//this can mess up the unit view panel so refresh it
				refreshForceView();
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		panOrganization.add(splitOrg, gridBagConstraints);
		
		
		tabMain.addTab(
				resourceMap.getString("panOrganization.TabConstraints.tabTitle"),
				panOrganization); // NOI18N
		
		panBriefing.setName("panBriefing"); // NOI18N
		panBriefing.setLayout(new java.awt.GridBagLayout());
		
		lblMission.setText(resourceMap.getString("lblMission.text")); // NOI18N
		lblMission.setName("lblMission"); // NOI18N
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.NONE;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.CENTER;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.weighty = 0.0;
		panBriefing.add(lblMission, gridBagConstraints);
		
		refreshMissions();
		choiceMission.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				changeMission();
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 0.0;
		panBriefing.add(choiceMission, gridBagConstraints);	
		
		
		panelMissionButtons.setLayout(new java.awt.GridLayout(2,3));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridheight = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 0.0;
		panBriefing.add(panelMissionButtons, gridBagConstraints);	
		
		btnAddMission.setText(resourceMap.getString("btnAddMission.text")); // NOI18N
		btnAddMission.setToolTipText(resourceMap
				.getString("btnAddMission.toolTipText")); // NOI18N
		btnAddMission.setName("btnAddMission"); // NOI18N
		btnAddMission.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnAddMissionActionPerformed(evt);
			}
		});
		panelMissionButtons.add(btnAddMission);
		
		btnAddScenario.setText(resourceMap.getString("btnAddScenario.text")); // NOI18N
		btnAddScenario.setToolTipText(resourceMap
				.getString("btnAddScenario.toolTipText")); // NOI18N
		btnAddScenario.setName("btnAddScenario"); // NOI18N
		btnAddScenario.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnAddScenarioActionPerformed(evt);
			}
		});
		panelMissionButtons.add(btnAddScenario);
		
		
		
		btnEditMission.setText(resourceMap.getString("btnEditMission.text")); // NOI18N
		btnEditMission.setToolTipText(resourceMap
				.getString("btnEditMission.toolTipText")); // NOI18N
		btnEditMission.setName("btnEditMission"); // NOI18N
		btnEditMission.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnEditMissionActionPerformed(evt);
			}
		});
		panelMissionButtons.add(btnEditMission);
		
		btnCompleteMission.setText(resourceMap.getString("btnCompleteMission.text")); // NOI18N
		btnCompleteMission.setToolTipText(resourceMap
				.getString("btnCompleteMission.toolTipText")); // NOI18N
		btnCompleteMission.setName("btnCompleteMission"); // NOI18N
		btnCompleteMission.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnCompleteMissionActionPerformed(evt);
			}
		});
		panelMissionButtons.add(btnCompleteMission);
		
		scrollMissionView.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollMissionView.setViewportView(null);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		panBriefing.add(scrollMissionView, gridBagConstraints);
		
		scenarioTable.setModel(scenarioModel);
		scenarioTable.setName("scenarioTable"); // NOI18N
		scenarioTable.setShowGrid(false);
		scenarioTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //scenarioTable.setRowSorter(new TableRowSorter<ScenarioTableModel>(scenarioModel));
		scenarioTable.addMouseListener(scenarioMouseAdapter);
		scenarioTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		/*
		TableColumn column = null;
        for (int i = 0; i < PersonnelTableModel.N_COL; i++) {
            column = personnelTable.getColumnModel().getColumn(i);
            column.setPreferredWidth(personModel.getColumnWidth(i));
            column.setCellRenderer(personModel.getRenderer());
        }
        */
		refreshScenarioList();
        scenarioTable.setIntercellSpacing(new Dimension(0, 0));
        scenarioTable.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                refreshScenarioView();
            }
        });
		scrollScenarioTable.setMinimumSize(new java.awt.Dimension(200, 200));
		scrollScenarioTable.setPreferredSize(new java.awt.Dimension(200, 200));
        scrollScenarioTable.setViewportView(scenarioTable);
		
		splitMission = new javax.swing.JSplitPane(javax.swing.JSplitPane.VERTICAL_SPLIT, panBriefing, scrollScenarioTable);
		splitMission.setOneTouchExpandable(true);
		splitMission.setResizeWeight(1.0);
		
		panelScenario.setName("panelScenario"); // NOI18N
		panelScenario.setLayout(new java.awt.GridBagLayout());
		
		panelScenarioButtons.setLayout(new java.awt.GridLayout(2,3));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 0.0;
		panelScenario.add(panelScenarioButtons, gridBagConstraints);
		
		btnStartGame.setText(resourceMap.getString("btnStartGame.text")); // NOI18N
		btnStartGame.setToolTipText(resourceMap
				.getString("btnStartGame.toolTipText")); // NOI18N
		btnStartGame.setName("btnStartGame"); // NOI18N
		btnStartGame.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				startScenario();
			}
		});
		btnStartGame.setEnabled(false);
		panelScenarioButtons.add(btnStartGame);
		
		btnLoadGame.setText(resourceMap.getString("btnLoadGame.text")); // NOI18N
		btnLoadGame.setToolTipText(resourceMap
				.getString("btnLoadGame.toolTipText")); // NOI18N
		btnLoadGame.setName("btnLoadGame"); // NOI18N
		btnLoadGame.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				loadScenario();
			}
		});
		btnLoadGame.setEnabled(false);
		panelScenarioButtons.add(btnLoadGame);
		
		btnPrintRS.setText(resourceMap.getString("btnPrintRS.text")); // NOI18N
		btnPrintRS.setToolTipText(resourceMap
				.getString("btnPrintRS.toolTipText")); // NOI18N
		btnPrintRS.setName("btnPrintRS"); // NOI18N
		btnPrintRS.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				printRecordSheets();
			}
		});
		btnPrintRS.setEnabled(false);
		panelScenarioButtons.add(btnPrintRS);
		
		btnGetMul.setText(resourceMap.getString("btnGetMul.text")); // NOI18N
		btnGetMul.setToolTipText(resourceMap
				.getString("btnGetMul.toolTipText")); // NOI18N
		btnGetMul.setName("btnGetMul"); // NOI18N
		btnGetMul.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				deployListFile();
			}
		});
		btnGetMul.setEnabled(false);
		panelScenarioButtons.add(btnGetMul);
		
		btnResolveScenario.setText(resourceMap.getString("btnResolveScenario.text")); // NOI18N
		btnResolveScenario.setToolTipText(resourceMap
				.getString("btnResolveScenario.toolTipText")); // NOI18N
		btnResolveScenario.setName("btnResolveScenario"); // NOI18N
		btnResolveScenario.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				resolveScenario();
			}
		});	
		btnResolveScenario.setEnabled(false);
		panelScenarioButtons.add(btnResolveScenario);
		
		btnClearAssignedUnits.setText(resourceMap.getString("btnClearAssignedUnits.text")); // NOI18N
		btnClearAssignedUnits.setToolTipText(resourceMap
				.getString("btnClearAssignedUnits.toolTipText")); // NOI18N
		btnClearAssignedUnits.setName("btnClearAssignedUnits"); // NOI18N
		btnClearAssignedUnits.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				clearAssignedUnits();
			}
		});
		btnClearAssignedUnits.setEnabled(false);
		panelScenarioButtons.add(btnClearAssignedUnits);
		
		scrollScenarioView.setViewportView(null);
		scrollScenarioView.setMinimumSize(new java.awt.Dimension(450, 600));
		scrollScenarioView.setPreferredSize(new java.awt.Dimension(450, 600));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		panelScenario.add(scrollScenarioView, gridBagConstraints);
		
		splitBrief = new javax.swing.JSplitPane(javax.swing.JSplitPane.HORIZONTAL_SPLIT, splitMission, panelScenario);
		splitBrief.setOneTouchExpandable(true);
		splitBrief.setResizeWeight(0.5);
		splitBrief.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent pce) {
				//this can mess up the view panel so refresh it
				changeMission();
				refreshScenarioView();
			}
		});
	
		tabMain.addTab(
				resourceMap.getString("panBriefing.TabConstraints.tabTitle"),
				splitBrief); // NOI18N
		
		panelMapView.setName("panelMapView"); // NOI18N
		panelMapView.setLayout(new java.awt.GridBagLayout());
		
		lblFindPlanet.setText(resourceMap.getString("lblFindPlanet.text")); // NOI18N
		lblFindPlanet.setName("lblFindPlanet"); // NOI18N
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.weighty = 0.0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		panelMapView.add(lblFindPlanet, gridBagConstraints);
		
		suggestPlanet = new JSuggestField(getFrame(), getCampaign().getPlanetNames());
		suggestPlanet.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				Planet p = getCampaign().getPlanet(suggestPlanet.getText());
				if(null != p) {
					panMap.setSelectedPlanet(p);
					refreshPlanetView();
				}
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 0.0;
		panelMapView.add(suggestPlanet, gridBagConstraints);
		
		btnCalculateJumpPath.setText(resourceMap.getString("btnCalculateJumpPath.text")); // NOI18N
		btnCalculateJumpPath.setToolTipText(resourceMap
				.getString("btnCalculateJumpPath.toolTipText")); // NOI18N
		btnCalculateJumpPath.setName("btnCalculateJumpPath"); // NOI18N
		btnCalculateJumpPath.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				calculateJumpPath();
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 0.5;
		gridBagConstraints.weighty = 0.0;
		panelMapView.add(btnCalculateJumpPath, gridBagConstraints);
		
		btnBeginTransit.setText(resourceMap.getString("btnBeginTransit.text")); // NOI18N
		btnBeginTransit.setToolTipText(resourceMap
				.getString("btnBeginTransit.toolTipText")); // NOI18N
		btnBeginTransit.setName("btnBeginTransit"); // NOI18N
		btnBeginTransit.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				beginTransit();
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 0.5;
		gridBagConstraints.weighty = 0.0;
		panelMapView.add(btnBeginTransit, gridBagConstraints);
		
		panMap = new InterstellarMapPanel(getCampaign(), this);
		panMap.setName("panMap"); // NOI18N		
		//lets go ahead and zoom in on the current location
		panMap.setSelectedPlanet(getCampaign().getLocation().getCurrentPlanet());
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 4;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		panelMapView.add(panMap, gridBagConstraints);
		
		scrollPlanetView.setMinimumSize(new java.awt.Dimension(400, 600));
		scrollPlanetView.setPreferredSize(new java.awt.Dimension(400, 600));
		scrollPlanetView.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPlanetView.setViewportView(null);
		splitMap = new javax.swing.JSplitPane(javax.swing.JSplitPane.HORIZONTAL_SPLIT,panelMapView, scrollPlanetView);
		splitMap.setOneTouchExpandable(true);
		splitMap.setResizeWeight(1.0);
		splitMap.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent pce) {
				//this can mess up the planet view panel so refresh it
				refreshPlanetView();
			}
		});
		tabMain.addTab(
				resourceMap.getString("panMap.TabConstraints.tabTitle"),
				splitMap); // NOI18N
		
		panPersonnel.setName("panPersonnel"); // NOI18N
		panPersonnel.setLayout(new java.awt.GridBagLayout());
		
		lblPersonChoice.setText(resourceMap.getString("lblPersonChoice.text")); // NOI18N
		lblPersonChoice.setName("lblPersonChoice"); // NOI18N
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
		panPersonnel.add(lblPersonChoice, gridBagConstraints);
		
		DefaultComboBoxModel personGroupModel = new DefaultComboBoxModel();
		for (int i = 0; i < PG_NUM; i++) {
			personGroupModel.addElement(getPersonnelGroupName(i));
		}
		choicePerson.setModel(personGroupModel);
		choicePerson.setName("choicePerson"); // NOI18N
		choicePerson.setSelectedIndex(0);
		choicePerson.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				filterPersonnel();
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.NONE;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.weighty = 0.0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
		panPersonnel.add(choicePerson, gridBagConstraints);
			
		lblPersonView.setText(resourceMap.getString("lblPersonView.text")); // NOI18N
		lblPersonView.setName("lblPersonView"); // NOI18N
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
		panPersonnel.add(lblPersonView, gridBagConstraints);
		
		DefaultComboBoxModel personViewModel = new DefaultComboBoxModel();
		for (int i = 0; i < PV_NUM; i++) {
			personViewModel.addElement(getPersonnelViewName(i));
		}
		choicePersonView.setModel(personViewModel);
		choicePersonView.setName("choicePersonView"); // NOI18N
		choicePersonView.setSelectedIndex(0);
		choicePersonView.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				changePersonnelView();
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.NONE;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 0.0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
		panPersonnel.add(choicePersonView, gridBagConstraints);
		
		personnelTable.setModel(personModel);
		personnelTable.setName("personnelTable"); // NOI18N
		personnelTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        XTableColumnModel personColumnModel = new XTableColumnModel();
        personnelTable.setColumnModel(personColumnModel);
        personnelTable.createDefaultColumnsFromModel();
        personnelSorter = new TableRowSorter<PersonnelTableModel>(personModel);
        personnelSorter.setComparator(PersonnelTableModel.COL_RANK, new RankSorter());
        personnelSorter.setComparator(PersonnelTableModel.COL_SKILL, new LevelSorter());
        personnelSorter.setComparator(PersonnelTableModel.COL_TACTICS, new BonusSorter());
        personnelSorter.setComparator(PersonnelTableModel.COL_TOUGH, new BonusSorter());
        personnelTable.setRowSorter(personnelSorter);
		personnelTable.addMouseListener(personnelMouseAdapter);
		personnelTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		TableColumn column = null;
        for (int i = 0; i < PersonnelTableModel.N_COL; i++) {
            column = personnelTable.getColumnModel().getColumn(i);
            column.setPreferredWidth(personModel.getColumnWidth(i));
            column.setCellRenderer(personModel.getRenderer());
        }
        personnelTable.setIntercellSpacing(new Dimension(0, 0));
		personnelTable.setShowGrid(false);
        changePersonnelView();
        personnelTable.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                refreshPersonnelView();
            }
        });
        scrollPersonnelTable.setViewportView(personnelTable);
	
		scrollPersonnelView.setMinimumSize(new java.awt.Dimension(500, 600));
		scrollPersonnelView.setPreferredSize(new java.awt.Dimension(500, 600));
		scrollPersonnelView.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPersonnelView.setViewportView(null);
		
		splitPersonnel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,scrollPersonnelTable, scrollPersonnelView);
		splitPersonnel.setOneTouchExpandable(true);
		splitPersonnel.setResizeWeight(1.0);
		splitPersonnel.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent pce) {
				//this can mess up the pilot view pane so refresh it
				refreshPersonnelView();
			}
		});
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 4;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		panPersonnel.add(splitPersonnel, gridBagConstraints);
		
		filterPersonnel();
		
		tabMain.addTab(
				resourceMap.getString("panPersonnel.TabConstraints.tabTitle"),
				panPersonnel); // NOI18N
		
		panHangar.setName("panHangar"); // NOI18N
		panHangar.setLayout(new java.awt.GridBagLayout());
		
		lblUnitChoice.setText(resourceMap.getString("lblUnitChoice.text")); // NOI18N
		lblUnitChoice.setName("lblUnitChoice"); // NOI18N
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
		panHangar.add(lblUnitChoice, gridBagConstraints);
		
		DefaultComboBoxModel unitGroupModel = new DefaultComboBoxModel();
		unitGroupModel.addElement("All Units");
		for (int i = 0; i < UnitType.SIZE; i++) {
			unitGroupModel.addElement(UnitType.getTypeDisplayableName(i));
		}
		choiceUnit.setModel(unitGroupModel);
		choiceUnit.setName("choiceUnit"); // NOI18N
		choiceUnit.setSelectedIndex(0);
		choiceUnit.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				filterUnits();
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.NONE;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
		panHangar.add(choiceUnit, gridBagConstraints);
			
		lblUnitView.setText(resourceMap.getString("lblUnitView.text")); // NOI18N
		lblPersonView.setName("lblUnitView"); // NOI18N
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
		panHangar.add(lblUnitView, gridBagConstraints);
		
		DefaultComboBoxModel unitViewModel = new DefaultComboBoxModel();
		for (int i = 0; i < UV_NUM; i++) {
			unitViewModel.addElement(getUnitViewName(i));
		}
		choiceUnitView.setModel(unitViewModel);
		choiceUnitView.setName("choiceUnitView"); // NOI18N
		choiceUnitView.setSelectedIndex(0);
		choiceUnitView.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				changeUnitView();
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.NONE;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
		panHangar.add(choiceUnitView, gridBagConstraints);
		
		unitTable.setModel(unitModel);
		unitTable.setName("unitTable"); // NOI18N
		unitTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		XTableColumnModel unitColumnModel = new XTableColumnModel();
        unitTable.setColumnModel(unitColumnModel);
        unitTable.createDefaultColumnsFromModel();
        unitSorter = new TableRowSorter<UnitTableModel>(unitModel);
        unitSorter.setComparator(UnitTableModel.COL_STATUS, new UnitStatusSorter());
        unitSorter.setComparator(UnitTableModel.COL_WCLASS, new WeightClassSorter());
        unitSorter.setComparator(UnitTableModel.COL_COST, new FormattedNumberSorter());
        unitTable.setRowSorter(unitSorter);
		unitTable.addMouseListener(unitMouseAdapter);
		unitTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		column = null;
        for (int i = 0; i < UnitTableModel.N_COL; i++) {
            column = unitTable.getColumnModel().getColumn(i);
            column.setPreferredWidth(unitModel.getColumnWidth(i));
            column.setCellRenderer(unitModel.getRenderer());
        }
        unitTable.setIntercellSpacing(new Dimension(0, 0));
		unitTable.setShowGrid(false);
        changeUnitView();
        unitTable.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                refreshUnitView();
            }
        });
        
        scrollUnitTable.setViewportView(unitTable);

		scrollUnitView.setMinimumSize(new java.awt.Dimension(450, 600));
		scrollUnitView.setPreferredSize(new java.awt.Dimension(450, 600));
		scrollUnitView.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollUnitView.setViewportView(null);
		
		splitUnit = new javax.swing.JSplitPane(javax.swing.JSplitPane.HORIZONTAL_SPLIT,scrollUnitTable, scrollUnitView);
		splitUnit.setOneTouchExpandable(true);
		splitUnit.setResizeWeight(1.0);
		splitUnit.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent pce) {
				//this can mess up the unit view panel so refresh it
				refreshUnitView();
			}
		});
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 6;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		panHangar.add(splitUnit, gridBagConstraints);
		
		tabMain.addTab(
				resourceMap.getString("panHangar.TabConstraints.tabTitle"),
				panHangar); // NOI18N
	
		panSupplies.setName("panSupplies"); // NOI18N
		panSupplies.setLayout(new java.awt.GridBagLayout());
		
		lblPartsChoice.setText(resourceMap.getString("lblPartsChoice.text")); // NOI18N
		lblPartsChoice.setName("lblPartsChoice"); // NOI18N
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
		panSupplies.add(lblPartsChoice, gridBagConstraints);
		
		DefaultComboBoxModel partsGroupModel = new DefaultComboBoxModel();
		for (int i = 0; i < SG_NUM; i++) {
			partsGroupModel.addElement(getPartsGroupName(i));
		}
		choiceParts.setModel(partsGroupModel);
		choiceParts.setName("choiceParts"); // NOI18N
		choiceParts.setSelectedIndex(0);
		choiceParts.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				filterParts();
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.NONE;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 0.0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
		panSupplies.add(choiceParts, gridBagConstraints);
		
		partsTable.setModel(partsModel);
		partsTable.setName("partsTable"); // NOI18N
		partsSorter = new TableRowSorter<PartsTableModel>(partsModel);
        partsSorter.setComparator(PartsTableModel.COL_COST, new FormattedNumberSorter());
        unitTable.setRowSorter(unitSorter);
        //personnelSorter.setComparator(PersonnelTableModel.COL_GUN, new SkillSorter());
        partsTable.setRowSorter(partsSorter);
		column = null;
        for (int i = 0; i < PartsTableModel.N_COL; i++) {
            column = partsTable.getColumnModel().getColumn(i);
            column.setPreferredWidth(partsModel.getColumnWidth(i));
            column.setCellRenderer(partsModel.getRenderer());
        }
        partsTable.setIntercellSpacing(new Dimension(0, 0));
		partsTable.setShowGrid(false);
		partsTable.getSelectionModel().addListSelectionListener(
				new javax.swing.event.ListSelectionListener() {
					public void valueChanged(
							javax.swing.event.ListSelectionEvent evt) {
						PartsTableValueChanged(evt);
					}
				});
		partsTable.addMouseListener(partsMouseAdapter);
		
		scrollPartsTable.setName("scrollPartsTable"); // NOI18N
		scrollPartsTable.setViewportView(partsTable);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		panSupplies.add(scrollPartsTable, gridBagConstraints);

		tabMain.addTab(
				resourceMap.getString("panSupplies.TabConstraints.tabTitle"),
				panSupplies); // NOI18N

		
		panRepairBay.setName("panRepairBay"); // NOI18N
		panRepairBay.setLayout(new java.awt.GridBagLayout());
		
		tabTasks.setMinimumSize(new java.awt.Dimension(600, 200));
		tabTasks.setName("tabTasks"); // NOI18N
		tabTasks.setPreferredSize(new java.awt.Dimension(300, 300));
		tabTasks.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
		        taskTabChanged();
		    }
		});

		scrollTaskTable.setMinimumSize(new java.awt.Dimension(200, 200));
		scrollTaskTable.setName("scrollTaskTable"); // NOI18N
		scrollTaskTable.setPreferredSize(new java.awt.Dimension(300, 300));

		TaskTable.setModel(taskModel);
		TaskTable.setName("TaskTable"); // NOI18N
		TaskTable.setRowHeight(60);
		TaskTable.getColumnModel().getColumn(0)
				.setCellRenderer(taskModel.getRenderer());
		TaskTable.getSelectionModel().addListSelectionListener(
				new javax.swing.event.ListSelectionListener() {
					public void valueChanged(
							javax.swing.event.ListSelectionEvent evt) {
						TaskTableValueChanged(evt);
					}
				});
		TaskTable.addMouseListener(taskMouseAdapter);
		scrollTaskTable.setViewportView(TaskTable);

		scrollAcquisitionTable.setMinimumSize(new java.awt.Dimension(200, 200));
		scrollAcquisitionTable.setName("scrollAcquisitionTable"); // NOI18N
		scrollAcquisitionTable.setPreferredSize(new java.awt.Dimension(300, 300));

		AcquisitionTable.setModel(acquireModel);
		AcquisitionTable.setName("AcquisitionTable"); // NOI18N
		AcquisitionTable.setRowHeight(60);
		AcquisitionTable.getColumnModel().getColumn(0)
				.setCellRenderer(acquireModel.getRenderer());
		AcquisitionTable.getSelectionModel().addListSelectionListener(
				new javax.swing.event.ListSelectionListener() {
					public void valueChanged(
							javax.swing.event.ListSelectionEvent evt) {
						AcquisitionTableValueChanged(evt);
					}
				});
		//AcquisitionTable.addMouseListener(acquisitionMouseAdapter);
		scrollAcquisitionTable.setViewportView(AcquisitionTable);

		
		tabTasks.addTab(resourceMap.getString("scrollTaskTable.TabConstraints.tabTasks"), scrollTaskTable); // NOI18N
		tabTasks.addTab(resourceMap.getString("scrollAcquisitionTable.TabConstraints.tabTasks"), scrollAcquisitionTable); // NOI18N

		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 0.5;
		gridBagConstraints.weighty = 1.0;
		panRepairBay.add(tabTasks, gridBagConstraints);

		scrollServicedUnitTable.setMinimumSize(new java.awt.Dimension(300, 200));
		scrollServicedUnitTable.setName("scrollServicedUnitTable"); // NOI18N
		scrollServicedUnitTable.setPreferredSize(new java.awt.Dimension(300, 300));

		servicedUnitTable.setModel(servicedUnitModel);
		servicedUnitTable.setName("servicedUnitTable"); // NOI18N
		servicedUnitTable.setRowHeight(80);
		servicedUnitTable.getColumnModel().getColumn(0)
				.setCellRenderer(servicedUnitModel.getRenderer());
		servicedUnitTable.getSelectionModel().addListSelectionListener(
				new javax.swing.event.ListSelectionListener() {
					public void valueChanged(
							javax.swing.event.ListSelectionEvent evt) {
						servicedUnitTableValueChanged(evt);
					}
				});
		servicedUnitTable.addMouseListener(servicedUnitMouseAdapter);
		scrollServicedUnitTable.setViewportView(servicedUnitTable);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridheight = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 0.5;
		gridBagConstraints.weighty = 1.0;
		panRepairBay.add(scrollServicedUnitTable, gridBagConstraints);

		btnOvertime.setText(resourceMap.getString("btnOvertime.text")); // NOI18N
		btnOvertime.setToolTipText(resourceMap
				.getString("btnOvertime.toolTipText")); // NOI18N
		btnOvertime.setName("btnOvertime"); // NOI18N
		btnOvertime.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnOvertimeActionPerformed(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		panRepairBay.add(btnOvertime, gridBagConstraints);
		
		astechPoolLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		astechPoolLabel.setText("<html><b>Astech Pool Minutes:</> " + getCampaign().getAstechPoolMinutes() + " (" + getCampaign().getNumberAstechs() + " Astechs)</html>"); // NOI18N
		astechPoolLabel.setName("astechPoolLabel"); // NOI18N
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		panRepairBay.add(astechPoolLabel, gridBagConstraints);
		
		scrollTechTable.setMinimumSize(new java.awt.Dimension(200, 200));
		scrollTechTable.setName("scrollTechTable"); // NOI18N
		scrollTechTable.setPreferredSize(new java.awt.Dimension(300, 300));

		TechTable.setModel(techsModel);
		TechTable.setName("TechTable"); // NOI18N
		TechTable.setRowHeight(60);
		TechTable.getColumnModel().getColumn(0)
				.setCellRenderer(techsModel.getRenderer());
		TechTable.getSelectionModel().addListSelectionListener(
				new javax.swing.event.ListSelectionListener() {
					public void valueChanged(
							javax.swing.event.ListSelectionEvent evt) {
						TechTableValueChanged(evt);
					}
				});
		scrollTechTable.setViewportView(TechTable);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridheight = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 0.5;
		gridBagConstraints.weighty = 1.0;
		panRepairBay.add(scrollTechTable, gridBagConstraints);

		panelDoTask.setMinimumSize(new java.awt.Dimension(300, 100));
		panelDoTask.setName("panelDoTask"); // NOI18N
		panelDoTask.setPreferredSize(new java.awt.Dimension(300, 100));
		panelDoTask.setLayout(new java.awt.GridBagLayout());

		btnDoTask.setText(resourceMap.getString("btnDoTask.text")); // NOI18N
		btnDoTask
				.setToolTipText(resourceMap.getString("btnDoTask.toolTipText")); // NOI18N
		btnDoTask.setEnabled(false);
		btnDoTask.setName("btnDoTask"); // NOI18N
		btnDoTask.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnDoTaskActionPerformed(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		panelDoTask.add(btnDoTask, gridBagConstraints);

		lblTarget.setText(resourceMap.getString("lblTarget.text")); // NOI18N
		lblTarget.setName("lblTarget"); // NOI18N
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
		panelDoTask.add(lblTarget, gridBagConstraints);

		lblTargetNum.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		lblTargetNum.setText(resourceMap.getString("lblTargetNum.text")); // NOI18N
		lblTargetNum.setName("lblTargetNum"); // NOI18N
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
		panelDoTask.add(lblTargetNum, gridBagConstraints);

		jScrollPane6.setName("jScrollPane6"); // NOI18N

		textTarget.setColumns(20);
		textTarget.setEditable(false);
		textTarget.setLineWrap(true);
		textTarget.setRows(5);
		textTarget.setText(resourceMap.getString("textTarget.text")); // NOI18N
		textTarget.setWrapStyleWord(true);
		textTarget.setBorder(null);
		textTarget.setName("textTarget"); // NOI18N
		jScrollPane6.setViewportView(textTarget);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridheight = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
		panelDoTask.add(jScrollPane6, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridheight = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 0.5;
		panRepairBay.add(panelDoTask, gridBagConstraints);

		tabMain.addTab(
				resourceMap.getString("panRepairBay.TabConstraints.tabTitle"),
				panRepairBay); // NOI18N
	
		panInfirmary.setName("panInfirmary"); // NOI18N
		panInfirmary.setLayout(new java.awt.GridBagLayout());

		btnAssignDoc.setText(resourceMap.getString("btnAssignDoc.text")); // NOI18N
		btnAssignDoc.setToolTipText(resourceMap
				.getString("btnAssignDoc.toolTipText")); // NOI18N
		btnAssignDoc.setEnabled(false);
		btnAssignDoc.setName("btnAssignDoc"); // NOI18N
		btnAssignDoc.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnAssignDocActionPerformed(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
		panInfirmary.add(btnAssignDoc, gridBagConstraints);

		scrollPatientTable.setMinimumSize(new java.awt.Dimension(300, 200));
		scrollPatientTable.setName("scrollPatientTable"); // NOI18N
		scrollPatientTable.setPreferredSize(new java.awt.Dimension(300, 300));

		patientTable.setModel(patientModel);
		patientTable.setName("PersonTable"); // NOI18N
		patientTable.setRowHeight(80);
		patientTable.getColumnModel().getColumn(0)
				.setCellRenderer(patientModel.getRenderer());
		patientTable.getSelectionModel().addListSelectionListener(
				new javax.swing.event.ListSelectionListener() {
					public void valueChanged(
							javax.swing.event.ListSelectionEvent evt) {
						patientTableValueChanged(evt);
					}
				});
		scrollPatientTable.setViewportView(patientTable);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.weightx = 0.5;
		gridBagConstraints.weighty = 1.0;
		panInfirmary.add(scrollPatientTable, gridBagConstraints);

		scrollDocTable.setMinimumSize(new java.awt.Dimension(300, 300));
		scrollDocTable.setName("scrollDocTable"); // NOI18N
		scrollDocTable.setPreferredSize(new java.awt.Dimension(300, 300));

		DocTable.setModel(doctorsModel);
		DocTable.setName("DocTable"); // NOI18N
		DocTable.setRowHeight(60);
		DocTable.getColumnModel().getColumn(0)
				.setCellRenderer(doctorsModel.getRenderer());
		DocTable.getSelectionModel().addListSelectionListener(
				new javax.swing.event.ListSelectionListener() {
					public void valueChanged(
							javax.swing.event.ListSelectionEvent evt) {
						DocTableValueChanged(evt);
					}
				});
		scrollDocTable.setViewportView(DocTable);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.weightx = 0.5;
		gridBagConstraints.weighty = 1.0;
		panInfirmary.add(scrollDocTable, gridBagConstraints);

		tabMain.addTab(
				resourceMap.getString("panInfirmary.TabConstraints.tabTitle"),
				panInfirmary); // NOI18N

		
		panMekLab.setName("panMekLab"); // NOI18N
		scrollMekLab.setName("scrollFinanceTable");
        scrollMekLab.setViewportView(panMekLab);
		
        tabMain.addTab(
				resourceMap.getString("panMekLab.TabConstraints.tabTitle"),
				scrollMekLab); // NOI18N
		
		panFinances.setName("panFinances"); // NOI18N
		panFinances.setLayout(new java.awt.GridBagLayout());

		financeTable.setModel(financeModel);
		financeTable.setName("financeTable"); // NOI18N
		financeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//		financeTable.addMouseListener(personnelMouseAdapter);
		financeTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		column = null;
		for (int i = 0; i < FinanceTableModel.N_COL; i++) {
			column = financeTable.getColumnModel().getColumn(i);
			column.setPreferredWidth(financeModel.getColumnWidth(i));
			column.setCellRenderer(financeModel.getRenderer());
		}
        financeTable.setIntercellSpacing(new Dimension(0, 0));
		financeTable.setShowGrid(false);
        scrollFinanceTable.setName("scrollFinanceTable");
        scrollFinanceTable.setViewportView(financeTable);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 4;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		panFinances.add(scrollFinanceTable, gridBagConstraints);

		tabMain.addTab(
				resourceMap.getString("panFinances.TabConstraints.tabTitle"),
				panFinances); // NOI18N
		
		mainPanel.setAutoscrolls(true);
		mainPanel.setName("mainPanel"); // NOI18N
		mainPanel.setLayout(new java.awt.GridBagLayout());

		lblLocation.setMinimumSize(new java.awt.Dimension(250,100));
		lblLocation.setPreferredSize(new java.awt.Dimension(250, 100));
		lblLocation.setText(getCampaign().getLocation().getReport(getCampaign().getCalendar().getTime())); // NOI18N
		lblLocation.setName("lblLocation"); // NOI18N
		lblLocation.setVerticalAlignment(SwingConstants.TOP);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.weighty = 0.0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
		mainPanel.add(lblLocation, gridBagConstraints);
		
		
		txtPaneReportScrollPane.setName("txtPaneReportScrollPane"); // NOI18N
		txtPaneReport.setContentType(resourceMap
				.getString("txtPaneReport.contentType")); // NOI18N
		txtPaneReport.setEditable(false);
		txtPaneReport.setText(getCampaign().getCurrentReportHTML());
		txtPaneReport.setName("txtPaneReport"); // NOI18N
		txtPaneReportScrollPane.setViewportView(txtPaneReport);

		txtPaneReportScrollPane.setMinimumSize(new java.awt.Dimension(250,100));
		txtPaneReportScrollPane.setPreferredSize(new java.awt.Dimension(250, 100));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 0.0;
		gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
		mainPanel.add(txtPaneReportScrollPane, gridBagConstraints);

		btnAdvanceDay.setText(resourceMap.getString("btnAdvanceDay.text")); // NOI18N
		btnAdvanceDay.setToolTipText(resourceMap
				.getString("btnAdvanceDay.toolTipText")); // NOI18N
		btnAdvanceDay.setName("btnAdvanceDay"); // NOI18N
		btnAdvanceDay.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnAdvanceDayActionPerformed(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weighty = 0.0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
		mainPanel.add(btnAdvanceDay, gridBagConstraints);
	
		btnGMMode.setText(resourceMap.getString("btnGMMode.text")); // NOI18N
		btnGMMode
				.setToolTipText(resourceMap.getString("btnGMMode.toolTipText")); // NOI18N
		btnGMMode.setName("btnGMMode"); // NOI18N
		btnGMMode.setSelected(getCampaign().isGM());
		btnGMMode.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnGMModeActionPerformed(evt);
			}
		});
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		//gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
		mainPanel.add(tabMain, gridBagConstraints);
	
		menuBar.setName("menuBar"); // NOI18N

		fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
		fileMenu.setName("fileMenu"); // NOI18N

		menuLoad.setText(resourceMap.getString("menuLoad.text")); // NOI18N
		menuLoad.setName("menuLoadXml"); // NOI18N
		menuLoad.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				menuLoadXmlActionPerformed(evt);
			}
		});
		fileMenu.add(menuLoad);

		menuSave.setText(resourceMap.getString("menuSave.text")); // NOI18N
		menuSave.setName("menuSaveXml"); // NOI18N
		menuSave.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				menuSaveXmlActionPerformed(evt);
			}
		});
		fileMenu.add(menuSave);

		menuOptions.setText(resourceMap.getString("menuOptions.text")); // NOI18N
		menuOptions.setName("menuOptions"); // NOI18N
		menuOptions.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				menuOptionsActionPerformed(evt);
			}
		});
		fileMenu.add(menuOptions);
		
		menuOptionsMM.setText(resourceMap.getString("menuOptionsMM.text")); // NOI18N
		menuOptionsMM.setName("menuOptionsMM"); // NOI18N
		menuOptionsMM.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				menuOptionsMMActionPerformed(evt);
			}
		});
		fileMenu.add(menuOptionsMM);

		menuThemes =new JMenu("Themes");
		refreshThemeChoices();
		fileMenu.add(menuThemes);
		
		exitMenuItem.setName("exitMenuItem"); // NOI18N
		exitMenuItem.setText("Exit");
		exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				getApplication().exit();
			}
		});
		fileMenu.add(exitMenuItem);
		
		menuBar.add(fileMenu);

		menuManage.setText(resourceMap.getString("menuManage.text")); // NOI18N
		menuManage.setName("menuManage"); // NOI18N

		miLoadForces.setText(resourceMap.getString("miLoadForces.text")); // NOI18N
		miLoadForces.setName("miLoadForces"); // NOI18N
		miLoadForces.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				miLoadForcesActionPerformed(evt);
			}
		});
		menuManage.add(miLoadForces);

		addFunds.setText(resourceMap.getString("addFunds.text")); // NOI18N
		addFunds.setName("addFunds"); // NOI18N
		addFunds.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				addFundsActionPerformed(evt);
			}
		});
		menuManage.add(addFunds);

		menuBar.add(menuManage);

		menuMarket.setText(resourceMap.getString("menuMarket.text")); // NOI18N
		menuMarket.setName("menuMarket"); // NOI18N

		miPurchaseUnit.setText(resourceMap.getString("miPurchaseUnit.text")); // NOI18N
		miPurchaseUnit.setName("miPurchaseUnit"); // NOI18N
		miPurchaseUnit.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				miPurchaseUnitActionPerformed(evt);
			}
		});
		menuMarket.add(miPurchaseUnit);

		miBuyParts.setText(resourceMap.getString("miBuyParts.text")); // NOI18N
		miPurchaseUnit.setName("miBuyParts"); // NOI18N
		miBuyParts.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				buyParts();
			}
		});
		menuMarket.add(miBuyParts);

		
		menuHire.setText(resourceMap.getString("menuHire.text")); // NOI18N
		menuHire.setName("menuHire"); // NOI18N
		JMenuItem miHire;
		for(int i = Person.T_MECHWARRIOR; i < Person.T_NUM; i++) {		
			miHire = new JMenuItem();
			miHire.setText(Person.getRoleDesc(i)); // NOI18N
			miHire.setActionCommand(Integer.toString(i));
			miHire.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					hirePerson(evt);
				}
			});
			menuHire.add(miHire);
		}
		menuMarket.add(menuHire);
		menuAstechPool.setText("Astech Pool");
		miHireAstechs.setText("Hire Astechs");
		miHireAstechs.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				PopupValueChoiceDialog pvcd = new PopupValueChoiceDialog(getFrame(), true, "Hire How Many Astechs?", 1, 0, 100);
				pvcd.setVisible(true);
				getCampaign().increaseAstechPool(pvcd.getValue());
				refreshTechsList();
				refreshTempAstechs();
			}
		});
		menuAstechPool.add(miHireAstechs);
		miFireAstechs.setText("Release Astechs");
		miFireAstechs.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				PopupValueChoiceDialog pvcd = new PopupValueChoiceDialog(getFrame(), true, "Release How Many Astechs?", 1, 0, getCampaign().getAstechPool());
				pvcd.setVisible(true);
				getCampaign().decreaseAstechPool(pvcd.getValue());
				refreshTechsList();
				refreshTempAstechs();
			}
		});
		menuAstechPool.add(miFireAstechs);
		miFullStrengthAstechs.setText("Bring All Tech Teams to Full Strength");
		miFullStrengthAstechs.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				int need = (getCampaign().getTechs().size() * 6) - getCampaign().getNumberAstechs();
				if(need > 0) {
					getCampaign().increaseAstechPool(need);
				}
				refreshTechsList();
				refreshTempAstechs();
			}
		});
		menuAstechPool.add(miFullStrengthAstechs);
		miFireAllAstechs.setText("Release All Astechs from Pool");
		miFireAllAstechs.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				getCampaign().decreaseAstechPool(getCampaign().getAstechPool());
				refreshTechsList();
				refreshTempAstechs();
			}
		});
		menuAstechPool.add(miFireAllAstechs);
		menuMarket.add(menuAstechPool);
		menuMedicPool.setText("Medic Pool");
		miHireMedics.setText("Hire Medics");
		miHireMedics.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				PopupValueChoiceDialog pvcd = new PopupValueChoiceDialog(getFrame(), true, "Hire How Many Medics?", 1, 0, 100);
				pvcd.setVisible(true);
				getCampaign().increaseMedicPool(pvcd.getValue());
				refreshDoctorsList();
				refreshTempMedics();
			}
		});
		menuMedicPool.add(miHireMedics);
		miFireMedics.setText("Release Medics");
		miFireMedics.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				PopupValueChoiceDialog pvcd = new PopupValueChoiceDialog(getFrame(), true, "Release How Many Medics?", 1, 0, getCampaign().getMedicPool());
				pvcd.setVisible(true);
				getCampaign().decreaseMedicPool(pvcd.getValue());
				refreshDoctorsList();				
				refreshTempMedics();
			}
		});
		menuMedicPool.add(miFireMedics);
		miFullStrengthMedics.setText("Bring All Medical Teams to Full Strength");
		miFullStrengthMedics.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				int need = (getCampaign().getDoctors().size() * 4) - getCampaign().getNumberMedics();
				if(need > 0) {
					getCampaign().increaseMedicPool(need);
				}
				refreshDoctorsList();
				refreshTempMedics();
			}
		});
		menuMedicPool.add(miFullStrengthMedics);
		miFireAllMedics.setText("Release All Medics from Pool");
		miFireAllMedics.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				getCampaign().decreaseMedicPool(getCampaign().getMedicPool());
				refreshDoctorsList();
				refreshTempMedics();
			}
		});
		menuMedicPool.add(miFireAllMedics);
		menuMarket.add(menuMedicPool);
		menuBar.add(menuMarket);
		
		menuCommunity.setText(resourceMap.getString("menuCommunity.text")); // NOI18N
		menuCommunity.setName("menuCommunity"); // NOI18N

		miChat.setText(resourceMap.getString("miChat.text")); // NOI18N
		miChat.setName("miChat"); // NOI18N
		miChat.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				miChatActionPerformed(evt);
			}
		});
		menuCommunity.add(miChat);

		menuBar.add(menuCommunity);
		
		helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
		helpMenu.setName("helpMenu"); // NOI18N
		aboutMenuItem.setName("aboutMenuItem"); // NOI18N
		aboutMenuItem.setText("About");
		aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				showAboutBox();
			}
		});
		helpMenu.add(aboutMenuItem);
		menuBar.add(helpMenu);
		
		statusPanel.setName("statusPanel"); // NOI18N
		statusPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 20, 5));
		
		statusPanel.add(btnGMMode);
		refreshFunds();
		statusPanel.add(lblFunds);
		refreshTempAstechs();
		statusPanel.add(lblTempAstechs);
		refreshTempMedics();
		statusPanel.add(lblTempMedics);
		
		refreshServicedUnitList();
		refreshUnitList();
		refreshPersonnelList();
		changePersonnelView();
		refreshTaskList();
		refreshAcquireList();
		refreshTechsList();
		refreshPatientList();
		refreshDoctorsList();
		refreshPartsList();
		refreshCalendar();
		refreshReport();
		refreshFunds();
		refreshFinancialTransactions();
		refreshOrganization();
		refreshMissions();
		refreshLocation();
		refreshTempAstechs();
		refreshTempMedics();
		panMap.setCampaign(getCampaign());
	
		setLayout(new BorderLayout());
		add(mainPanel, BorderLayout.CENTER);
		add(statusPanel, BorderLayout.PAGE_END);
	
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	    
        frame.setSize(1000, 800);
        
	    // Determine the new location of the window
	    int w = frame.getSize().width;
	    int h = frame.getSize().height;
	    int x = (dim.width-w)/2;
	    int y = (dim.height-h)/2;
	    
	    // Move the window
	    frame.setLocation(x, y);
		
        frame.setJMenuBar(menuBar);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(this, BorderLayout.CENTER);
        frame.validate();
        
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                getApplication().exit();
            }
        });
	}
	
	private void miChatActionPerformed(ActionEvent evt) {
        JDialog chatDialog = new JDialog(getFrame(), "MekHQ Chat", false); //$NON-NLS-1$

		ChatClient client = new ChatClient("test", "localhost");
        client.listen();
        //chatDialog.add(client);
        chatDialog.add(new JLabel("Testing"));
        chatDialog.setResizable(true);
        chatDialog.setVisible(true);
	}

	private void btnAddMissionActionPerformed(java.awt.event.ActionEvent evt) {
		MissionTypeDialog mtd = new MissionTypeDialog(getFrame(), true, getCampaign(), this);
		mtd.setVisible(true);
	}
	
	private void btnEditMissionActionPerformed(java.awt.event.ActionEvent evt) {
		Mission mission = getCampaign().getMission(selectedMission);
		if(null != mission) {
			CustomizeMissionDialog cmd = new CustomizeMissionDialog(getFrame(), true, mission, getCampaign());
			cmd.setVisible(true);
			if(cmd.getMissionId() != -1) {
				selectedMission = cmd.getMissionId();
			}
			refreshMissions();
		}
		
	}
	
	private void changeTheme(java.awt.event.ActionEvent evt) {
		final String lafClassName = evt.getActionCommand();
		Runnable runnable = new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(lafClassName);
					SwingUtilities.updateComponentTreeUI(frame);
					refreshThemeChoices();
				} catch (Exception exception) {
					JOptionPane.showMessageDialog(frame,
							"Can't change look and feel",
							"Invalid PLAF",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		};
		SwingUtilities.invokeLater(runnable);
	}
	
	private void refreshThemeChoices() {
		menuThemes.removeAll();
		JCheckBoxMenuItem miPlaf;
		for(LookAndFeelInfo plaf : UIManager.getInstalledLookAndFeels()) {
			miPlaf = new JCheckBoxMenuItem(plaf.getName());
			if(plaf.getName().equalsIgnoreCase(UIManager.getLookAndFeel().getName())) {
				miPlaf.setSelected(true);
			}
			menuThemes.add(miPlaf);
			miPlaf.setActionCommand(plaf.getClassName());
			miPlaf.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					changeTheme(evt);
				}
			});
		}
	}
	
	private void btnCompleteMissionActionPerformed(java.awt.event.ActionEvent evt) {
		Mission mission = getCampaign().getMission(selectedMission);
		if(null != mission) {
			if(mission.hasPendingScenarios()) {
				JOptionPane.showMessageDialog(getFrame(),
					    "You cannot complete a mission that has pending scenarios",
					    "Pending Scenarios",
					    JOptionPane.WARNING_MESSAGE);
			} else {
				CompleteMissionDialog cmd = new CompleteMissionDialog(getFrame(), true, mission);
				cmd.setVisible(true);
				if(!mission.isActive()) {
					if(getCampaign().getActiveMissions().size() > 0) {
						selectedMission = getCampaign().getActiveMissions().get(0).getId();
					} else {
						selectedMission = -1;
					}
					refreshMissions();
				}
			}
		}	
	}
	
	private void btnAddScenarioActionPerformed(java.awt.event.ActionEvent evt) {
		Mission m = getCampaign().getMission(selectedMission);
		if(null != m) {
			CustomizeScenarioDialog csd = new CustomizeScenarioDialog(getFrame(), true, null, m, getCampaign());
			csd.setVisible(true);
			refreshScenarioList();
		}
	}

	private void calculateJumpPath() {
		if(null != panMap.getSelectedPlanet()) {
			panMap.setJumpPath(getCampaign().calculateJumpPath(getCampaign().getCurrentPlanetName(), panMap.getSelectedPlanet().getName()));
			refreshPlanetView();
		}
	}
	
	private void beginTransit() {
		if(panMap.getJumpPath().isEmpty()) {
			return;
		}
		getCampaign().getLocation().setJumpPath(panMap.getJumpPath());
		refreshPlanetView();
		refreshLocation();
		panMap.setJumpPath(new JumpPath());
		panMap.repaint();
	}
	
	private void btnDoTaskActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnDoTaskActionPerformed
		
		Person tech = getCampaign().getPerson(currentTechId);		
	
		if (null != tech) {
			if(acquireSelected()) {
				Part part = getCampaign().getPart(currentAcquisitionId);
				if(null != part && part instanceof IAcquisitionWork) {
					getCampaign().acquirePart((IAcquisitionWork)part, tech);
				}
			} else if(repairsSelected()) {
				Part part = getCampaign().getPart(currentServiceablePartsId);
				Unit u = part.getUnit();
				if(null != part) {
					getCampaign().fixPart(part, tech);
				}
				if(null !=  u && !u.isRepairable() && u.getSalvageableParts().size() == 0) {
					getCampaign().removeUnit(u.getId());
				}
			}
		}
		
		refreshServicedUnitList();
		refreshUnitList();
		refreshPersonnelList();
		refreshTaskList();
		refreshAcquireList();
		refreshTechsList();
		refreshPartsList();
		refreshReport();
		refreshFunds();
		refreshFinancialTransactions();
	}// GEN-LAST:event_btnDoTaskActionPerformed

	private void TechTableValueChanged(javax.swing.event.ListSelectionEvent evt) {
		int selected = TechTable.getSelectedRow();
		
		if ((selected > -1) && (selected < getCampaign().getTechs().size())) {
			currentTechId = getCampaign().getTechs().get(selected).getId();
		} else if (selected < 0) {
			currentTechId = -1;
		}
		
		updateAssignEnabled();
		updateTargetText();
	}

	private void TaskTableValueChanged(javax.swing.event.ListSelectionEvent evt) {
		int selected = TaskTable.getSelectedRow();
		
		if ((selected > -1)
				&& (selected < getCampaign().getPartsNeedingServiceFor(currentServicedUnitId).size())) {
			currentServiceablePartsId = getCampaign().getPartsNeedingServiceFor(currentServicedUnitId)
					.get(selected).getId();
		} else {
			currentServiceablePartsId = -1;
		}

		selectedTasksIds = new int[TaskTable.getSelectedRowCount()];
		
		for (int i = 0; i < TaskTable.getSelectedRowCount(); i++) {
			int sel = TaskTable.getSelectedRows()[i];
			
			if ((sel > -1)
					&& (sel < getCampaign().getPartsNeedingServiceFor(currentServicedUnitId).size())) {
				selectedTasksIds[i] = getCampaign().getPartsNeedingServiceFor(currentServicedUnitId)
						.get(sel).getId();
			} else {
				selectedTasksIds[i] = -1;
			}
		}

		updateAssignEnabled();
		updateTargetText();
	}
	
	private void AcquisitionTableValueChanged(javax.swing.event.ListSelectionEvent evt) {
		int selected = AcquisitionTable.getSelectedRow();
		
		if ((selected > -1)
				&& (selected < getCampaign().getAcquisitionsForUnit(currentServicedUnitId).size())) {
			currentAcquisitionId = getCampaign().getAcquisitionsForUnit(currentServicedUnitId)
					.get(selected).getId();
		} else {
			currentAcquisitionId = -1;
		}

		updateAssignEnabled();
		updateTargetText();
	}

	private void servicedUnitTableValueChanged(javax.swing.event.ListSelectionEvent evt) {
		int selected = servicedUnitTable.getSelectedRow();
		
		if ((selected > -1) && (selected < getCampaign().getServiceableUnits().size())) {
			currentServicedUnitId = getCampaign().getServiceableUnits().get(selected).getId();
		} else if (selected < 0) {
			currentServicedUnitId = -1;
		}
		
		refreshTaskList();
		refreshAcquireList();
	}
	
	private void taskTabChanged() {
		updateAssignEnabled();
		updateTargetText();
	}

	private void patientTableValueChanged(
			javax.swing.event.ListSelectionEvent evt) {
		int selected = patientTable.getSelectedRow();
		
		if ((selected > -1) && (selected < getCampaign().getPatients().size())) {
			currentPatientId = getCampaign().getPatients().get(selected).getId();
		} else if (selected < 0) {
			currentPatientId = -1;
		}
		
		updateAssignDoctorEnabled();
	}

	private void DocTableValueChanged(javax.swing.event.ListSelectionEvent evt) {
		int selected = DocTable.getSelectedRow();
		
		if ((selected > -1) && (selected < getCampaign().getDoctors().size())) {
			currentDoctorId = getCampaign().getDoctors().get(selected).getId();
		} else if (selected < 0) {
			currentDoctorId = -1;
		}
		
		updateAssignDoctorEnabled();
		
	}

	private void PartsTableValueChanged(javax.swing.event.ListSelectionEvent evt) {
		int selected = partsTable.getSelectedRow();
		
		if ((selected > -1) && (selected < getCampaign().getParts().size())) {
		//	currentPartsId = getCampaign().getParts().get(selected).getId();
		} else if (selected < 0) {
			//currentPartsId = -1;
		}
	}

	private void btnAdvanceDayActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnAdvanceDayActionPerformed
		getCampaign().newDay();
		refreshServicedUnitList();
		refreshUnitList();
		refreshPersonnelList();
		refreshTaskList();
		refreshAcquireList();
		refreshTechsList();
		refreshPatientList();
		refreshPartsList();
		refreshDoctorsList();
		refreshCalendar();
		refreshReport();
		refreshFunds();
		refreshLocation();
		refreshOrganization();
		panMap.repaint();
		refreshFinancialTransactions();
	}// GEN-LAST:event_btnAdvanceDayActionPerformed

	private void btnAssignDocActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnAssignDocActionPerformed
		if (currentPatientId == -1) {
			return;
		}
		
		int row = patientTable.getSelectedRow();
		Person p = getCampaign().getPerson(currentPatientId);
		
		if ((null != p) && (p.needsFixing())) {
			p.setDoctorId(currentDoctorId);
			row++;
		}
		
		refreshTechsList();
		refreshDoctorsList();
		refreshPatientList();	
		
		if (row >= patientTable.getRowCount()) {
			row = 0;
		}
		patientTable.setRowSelectionInterval(row, row);	
	}// GEN-LAST:event_btnAssignDocActionPerformed

	private void hirePerson(java.awt.event.ActionEvent evt) {
		int type = Integer.parseInt(evt.getActionCommand());
		CustomizePersonDialog npd = new CustomizePersonDialog(getFrame(), true, 
				getCampaign().newPerson(type), 
				true,
				getCampaign(),
				this);
		npd.setVisible(true);
	}

	private void menuSaveXmlActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_menuSaveActionPerformed
		MekHQ.logMessage("Saving campaign...");
		// Choose a file...
		File file = selectSaveCampaignFile();

		if (file == null) {
			// I want a file, y'know!
			return;
		}

		// Then save it out to that file.
		FileOutputStream fos = null;
		PrintWriter pw = null;

		try {
			fos = new FileOutputStream(file);
			pw = new PrintWriter(new OutputStreamWriter(fos, "UTF-8"));
			getCampaign().writeToXml(pw);
			pw.flush();
			pw.close();
			fos.close();
			MekHQ.logMessage("Campaign saved to " + file);
		} catch (IOException ex) {
			MekHQ.logError(ex);
		}
	}

	private File selectSaveCampaignFile() {
		JFileChooser saveCpgn = new JFileChooser("./campaigns/");
		saveCpgn.setDialogTitle("Save Campaign");
		saveCpgn.setFileFilter(new CampaignFileFilter());
		saveCpgn.setSelectedFile(new File(getCampaign().getName()
				+ getCampaign().getShortDateAsString() + ".cpnx")); //$NON-NLS-1$
		int returnVal = saveCpgn.showSaveDialog(mainPanel);

		if ((returnVal != JFileChooser.APPROVE_OPTION)
				|| (saveCpgn.getSelectedFile() == null)) {
			// I want a file, y'know!
			return null;
		}

		File file = saveCpgn.getSelectedFile();

		return file;
	}

	private void menuLoadXmlActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_menuLoadActionPerformed
		File f = selectLoadCampaignFile();
		if(null == f) {
			return;
		}
		DataLoadingDialog dataLoadingDialog = new DataLoadingDialog(getApplication(), getFrame(), f);   	
		//TODO: does this effectively deal with memory management issues?
		dataLoadingDialog.setVisible(true);
	}

	private File selectLoadCampaignFile() {
		JFileChooser loadCpgn = new JFileChooser("./campaigns/");
		loadCpgn.setDialogTitle("Load Campaign");
		loadCpgn.setFileFilter(new CampaignFileFilter());
		int returnVal = loadCpgn.showOpenDialog(mainPanel);

		if ((returnVal != JFileChooser.APPROVE_OPTION)
				|| (loadCpgn.getSelectedFile() == null)) {
			// I want a file, y'know!
			return null;
		}
		
		File file = loadCpgn.getSelectedFile();

		return file;
	}
	
	public static String getPersonnelGroupName(int group) {
    	switch(group) {
    	case PG_ACTIVE:
    		return "Active Personnel";
    	case PG_COMBAT:
    		return "Combat Personnel";
    	case PG_MW:
    		return "Mechwarriors";
    	case PG_CREW:
    		return "Vehicle Crews";
    	case PG_PILOT:
    		return "Aerospace Pilots";
    	case PG_PROTO:
    		return "Protomech Pilots";
    	case PG_BA:
    		return "Battle Armor Infantry";
    	case PG_SUPPORT:
    		return "Support Personnel";
    	case PG_TECH:
    		return "Techs";
    	case PG_DOC:
    		return "Doctors";
    	case PG_RETIRE:
    		return "Retired Personnel";
    	case PG_MIA:
    		return "Personnel MIA";
    	case PG_KIA:
    		return "Rolls of Honor (KIA)";
    	default:
    		return "?";
    	}
    }
	
	public static String getPartsGroupName(int group) {
    	switch(group) {
    	case SG_ALL:
    		return "All Parts";
    	case SG_ARMOR:
    		return "Armor";
    	case SG_SYSTEM:
    		return "System Components";
    	case SG_EQUIP:
    		return "Equipment";
    	case SG_LOC:
    		return "Locations";
    	case SG_WEAP:
    		return "Weapons";
    	case SG_AMMO:
    		return "Ammunition";
    	case SG_MISC:
    		return "Miscellaneous Equipment";
    	case SG_ENGINE:
    		return "Engines";
    	case SG_GYRO:
    		return "Gyros";
    	case SG_ACT:
    		return "Actuators";
    	case SG_DAMAGE:
    		return "Damaged Parts";
    	default:
    		return "?";
    	}
    }
	
	public static String getPersonnelViewName(int group) {
    	switch(group) {
    	case PV_GENERAL:
    		return "General";
    	case PV_PILOT:
    		return "Piloting/Gunnery Skills";
    	case PV_INF:
    		return "Infantry Skills";
    	case PV_TACTIC:
    		return "Tactical Skills";
    	case PV_TECH:
    		return "Tech Skills";
    	case PV_ADMIN:
    		return "Admin Skills";
    	case PV_FLUFF:
    		return "Fluff Information";
    	default:
    		return "?";
    	}
    }
	
	public static String getUnitViewName(int group) {
    	switch(group) {
    	case UV_GENERAL:
    		return "General";
    	case UV_DETAILS:
    		return "Details";
    	case UV_STATUS:
    		return "Status";
    	default:
    		return "?";
    	}
    }

	private void btnOvertimeActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnOvertimeActionPerformed
		getCampaign().setOvertime(btnOvertime.isSelected());
		refreshTechsList();
		refreshTaskList();
		refreshAcquireList();
	}// GEN-LAST:event_btnOvertimeActionPerformed

	private void btnGMModeActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnGMModeActionPerformed
		getCampaign().setGMMode(btnGMMode.isSelected());
	}// GEN-LAST:event_btnGMModeActionPerformed

	private void menuOptionsActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_menuOptionsActionPerformed
		CampaignOptionsDialog cod = new CampaignOptionsDialog(getFrame(), true,
				getCampaign(), getCamos());
		cod.setVisible(true);
		refreshCalendar();
		changePersonnelView();
		refreshPersonnelList();
		panMap.repaint();
	}// GEN-LAST:event_menuOptionsActionPerformed

	private void menuOptionsMMActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_menuOptionsActionPerformed
		GameOptionsDialog god = new GameOptionsDialog(getFrame(), getCampaign().getGameOptions());
		god.refreshOptions();
		god.setVisible(true);
		if(!god.wasCancelled()) {
			getCampaign().setGameOptions(god.getOptions());
			refreshCalendar();
			changePersonnelView();
			refreshPersonnelList();
		}
	}// GEN-LAST:event_menuOptionsActionPerformed

	
	private void miLoadForcesActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_miLoadForcesActionPerformed
		try {
			loadListFile(true);
		} catch (IOException ex) {
			Logger.getLogger(CampaignGUI.class.getName()).log(Level.SEVERE, null,
					ex);
		}
	}// GEN-LAST:event_miLoadForcesActionPerformed

	private void miPurchaseUnitActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_miPurchaseUnitActionPerformed
		UnitSelectorDialog usd = new UnitSelectorDialog(true, this);

		if (!getCampaign().isGM()) {
			usd.restrictToYear(getCampaign().getCalendar().get(Calendar.YEAR));
		}

		usd.setVisible(true);
		refreshServicedUnitList();
		refreshUnitList();
		refreshPersonnelList();
		refreshReport();
		refreshFunds();
		refreshFinancialTransactions();
	}// GEN-LAST:event_miPurchaseUnitActionPerformed

	private void buyParts() {
		PartsStoreDialog psd = new PartsStoreDialog(true, this);
		psd.setVisible(true);
		refreshPartsList();
		refreshAcquireList();
	}
	
    public void refitUnit(Refit r, boolean selectModelName) {
    	if(getCampaign().getTechs().size() > 0) {
	    	String[] techNames = new String[getCampaign().getTechs().size()];
	    	HashMap<String,Person> techHash = new HashMap<String,Person>();
	    	int i = 0;
	    	for(Person tech : getCampaign().getTechs()) {
	    		techNames[i] = tech.getName() + ", " + tech.getPrimaryRoleDesc() + " (" + getCampaign().getTargetFor(r, tech).getValueAsString() + "+)";
	    		techHash.put(techNames[i], tech);
	    		i++;
	    	}
	    	String s = (String)JOptionPane.showInputDialog(
	                frame,
	                "Which tech should work on the refit?",
	                "Select Tech",
	                JOptionPane.PLAIN_MESSAGE,
	                null,
	                techNames,
	                techNames[0]);
	    	r.setTeamId(techHash.get(s).getId());
    	} else {
    		JOptionPane.showMessageDialog(frame,
    			    "You have no techs available to work on this refit.",
    			    "No Techs",
    			    JOptionPane.WARNING_MESSAGE);
    		return;
    	}
    	if(selectModelName) {
	    	//select a model name
	    	String s = (String)JOptionPane.showInputDialog(
	                null,
	                "Choose a new model name",
	                "Designate Model",
	                JOptionPane.PLAIN_MESSAGE,
	                null,
	                null,
	                r.getOriginalEntity().getModel() + " Mk II");
	    	r.getNewEntity().setModel(s);
    	}
    	//TODO: allow overtime work?
		//check to see if user really wants to do it - give some info on what will be done
    	//TODO: better information
    	if(0 != JOptionPane.showConfirmDialog(null,
				"Are you sure you want to refit this unit?"
			, "Proceed?",
				JOptionPane.YES_NO_OPTION)) {
    		return;
    	}    	
    	r.begin();
    	getCampaign().refit(r);
    	panMekLab.clearUnit();
    	refreshReport();
    	refreshFunds();
    	refreshFinancialTransactions();
    	refreshUnitList();
    	refreshServicedUnitList();
    	refreshOrganization();
    	refreshPartsList();
    }
	
	private void refreshPersonnelView() {
		int row = personnelTable.getSelectedRow();
		if(row < 0) {
			scrollPersonnelView.setViewportView(null);
			return;
		}
		Person selectedPerson = personModel.getPerson(personnelTable.convertRowIndexToModel(row));
		scrollPersonnelView.setViewportView(new PersonViewPanel(selectedPerson, getCampaign(), getPortraits()));
		//This odd code is to make sure that the scrollbar stays at the top
		//I cant just call it here, because it ends up getting reset somewhere later
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() { 
				scrollPersonnelView.getVerticalScrollBar().setValue(0);
			}
		});
		
	}
	
	private void refreshUnitView() {
		int row = unitTable.getSelectedRow();
		if(row < 0) {
			scrollUnitView.setViewportView(null);
			return;
		}
		Unit selectedUnit = unitModel.getUnit(unitTable.convertRowIndexToModel(row));
		scrollUnitView.setViewportView(new UnitViewPanel(selectedUnit, getCampaign(), getCamos(), getMechTiles()));
		//This odd code is to make sure that the scrollbar stays at the top
		//I cant just call it here, because it ends up getting reset somewhere later
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() { 
				scrollUnitView.getVerticalScrollBar().setValue(0);
			}
		});
		
	}
	
	private void refreshScenarioView() {
		int row = scenarioTable.getSelectedRow();
		if(row < 0) {
			scrollScenarioView.setViewportView(null);
			btnStartGame.setEnabled(false);
			btnLoadGame.setEnabled(false);
			btnGetMul.setEnabled(false);
			btnClearAssignedUnits.setEnabled(false);
			btnResolveScenario.setEnabled(false);
			btnPrintRS.setEnabled(false);
			return;
		}
		Scenario scenario = scenarioModel.getScenario(scenarioTable.convertRowIndexToModel(row));
		scrollScenarioView.setViewportView(new ScenarioViewPanel(scenario, getCampaign(), this));
		//This odd code is to make sure that the scrollbar stays at the top
		//I cant just call it here, because it ends up getting reset somewhere later
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() { 
				scrollScenarioView.getVerticalScrollBar().setValue(0);
			}
		});
		boolean unitsAssigned = scenario.getForces(getCampaign()).getAllUnits().size() > 0;
		btnStartGame.setEnabled(scenario.isCurrent() && unitsAssigned);
		btnLoadGame.setEnabled(scenario.isCurrent() && unitsAssigned);
		btnGetMul.setEnabled(scenario.isCurrent() && unitsAssigned);
		btnClearAssignedUnits.setEnabled(scenario.isCurrent() && unitsAssigned);
		btnResolveScenario.setEnabled(scenario.isCurrent() && unitsAssigned);
		btnPrintRS.setEnabled(scenario.isCurrent() && unitsAssigned);
		
	}
	
	protected void refreshForceView() {
		Object node = orgTree.getLastSelectedPathComponent();
		if(null == node || -1 == orgTree.getRowForPath(orgTree.getSelectionPath())) {
			scrollForceView.setViewportView(null);
			return;
		}
		if(node instanceof Unit) {
			Unit u = ((Unit)node);
			JTabbedPane tabUnit = new JTabbedPane();
			Person p = u.getCommander();
			if(p != null) {
				String name = "Commander";
				if(u.usesSoloPilot()) {
					name = "Pilot";
				}
				tabUnit.add(name, new PersonViewPanel(p, getCampaign(), getPortraits()));
			}
			tabUnit.add("Unit", new UnitViewPanel(u, getCampaign(), getCamos(), getMechTiles()));
			scrollForceView.setViewportView(tabUnit);
			//This odd code is to make sure that the scrollbar stays at the top
			//I cant just call it here, because it ends up getting reset somewhere later
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
				public void run() { 
					scrollForceView.getVerticalScrollBar().setValue(0);
				}
			});
		} else if (node instanceof Force) {
			scrollForceView.setViewportView(new ForceViewPanel((Force)node, getCampaign(), getPortraits(), getForceIcons(), getCamos(), getMechTiles()));
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
				public void run() { 
					scrollForceView.getVerticalScrollBar().setValue(0);
				}
			});
		}
	}
	
	public void refreshPlanetView() {
		JumpPath path = panMap.getJumpPath();
		if(null != path && !path.isEmpty()) {
			scrollPlanetView.setViewportView(new JumpPathViewPanel(path, getCampaign()));
			return;
		}
		Planet planet = panMap.getSelectedPlanet();
		if(null != planet) {
			scrollPlanetView.setViewportView(new PlanetViewPanel(planet, getCampaign()));
		}
	}
	
	private void addFundsActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_addFundsActionPerformed
		AddFundsDialog addFundsDialog = new AddFundsDialog(null, true);
		addFundsDialog.setVisible(true);
		long funds = addFundsDialog.getFundsQuantity();
		getCampaign().addFunds(funds);
		refreshReport();
		refreshFunds();
		refreshFinancialTransactions();
	}// GEN-LAST:event_addFundsActionPerformed

	protected void loadListFile(boolean allowNewPilots) throws IOException {
		JFileChooser loadList = new JFileChooser(".");
		loadList.setDialogTitle("Load Units");
		
		loadList.setFileFilter(new FileFilter() {
			@Override
			public boolean accept(File dir) {
				if (dir.isDirectory()) {
					return true;
				}
				return dir.getName().endsWith(".mul");
			}

			@Override
			public String getDescription() {
				return "MUL file";
			}
		});

		int returnVal = loadList.showOpenDialog(mainPanel);
		
		if ((returnVal != JFileChooser.APPROVE_OPTION)
				|| (loadList.getSelectedFile() == null)) {
			// I want a file, y'know!
			return;
		}

		File unitFile = loadList.getSelectedFile();
		
		if (unitFile != null) {
			// I need to get the parser myself, because I want to pull both
			// entities and pilots from it
			// Create an empty parser.
			XMLStreamParser parser = new XMLStreamParser();

			// Open up the file.
			InputStream listStream = new FileInputStream(unitFile);

			// Read a Vector from the file.
			try {

				parser.parse(listStream);
				listStream.close();
			} catch (ParseException excep) {
				excep.printStackTrace(System.err);
				// throw new IOException("Unable to read from: " +
				// unitFile.getName());
			}

			// Was there any error in parsing?
			if (parser.hasWarningMessage()) {
				MekHQ.logMessage(parser.getWarningMessage());
			}

			// Add the units from the file.
			for (Entity entity : parser.getEntities()) {
				getCampaign().addUnit(entity, allowNewPilots);
			}
			
			// add any ejected pilots
			for (Pilot pilot : parser.getPilots()) {
				if (pilot.isEjected()) {
					//getCampaign().addPilot(pilot, PilotPerson.T_MECHWARRIOR, false);
				}
			}
		}
		
		refreshServicedUnitList();
		refreshUnitList();
		refreshPersonnelList();
		refreshPatientList();
		refreshReport();
	}

	protected void clearAssignedUnits() {
		if (0 == JOptionPane.showConfirmDialog(null,
				"Do you really want to remove all units from this scenario?","Clear Units?",
				JOptionPane.YES_NO_OPTION)) {
			int row = scenarioTable.getSelectedRow();
			Scenario scenario = scenarioModel.getScenario(scenarioTable.convertRowIndexToModel(row));
			if(null == scenario) {
				return;
			}
			scenario.clearAllForcesAndPersonnel(getCampaign());
			refreshScenarioList();
			refreshPersonnelList();
			refreshUnitList();
			refreshOrganization();
		}
	}
	
	protected void resolveScenario() {
		int row = scenarioTable.getSelectedRow();
		Scenario scenario = scenarioModel.getScenario(scenarioTable.convertRowIndexToModel(row));
		if(null == scenario) {
			return;
		}
		ResolveWizardChooseFilesDialog resolveDialog = new ResolveWizardChooseFilesDialog(getFrame(), true, new ResolveScenarioTracker(scenario, getCampaign()));
		resolveDialog.setVisible(true);
		
		refreshScenarioList();
		refreshOrganization();
		refreshServicedUnitList();
		refreshUnitList();
		filterPersonnel();
		refreshPersonnelList();
		refreshPatientList();
		refreshReport();
		changeMission();
		refreshFinancialTransactions();
	}
	
	protected void printRecordSheets() {
		int row = scenarioTable.getSelectedRow();
		if(row < 0) {
			return;
		}
		Scenario scenario = scenarioModel.getScenario(scenarioTable.convertRowIndexToModel(row));
		Vector<Integer> uids = scenario.getForces(getCampaign()).getAllUnits();
		
		if(uids.size() == 0) {
			return;
		}
		
		Vector<Entity> chosen = new Vector<Entity>();
		//ArrayList<Unit> toDeploy = new ArrayList<Unit>();
		StringBuffer undeployed = new StringBuffer();
		
		for(int uid : uids) {
			Unit u = getCampaign().getUnit(uid);
			if(u.isUnmanned()) {
				continue;
			}
			if (null != u.getEntity()) {
				if (null == u.checkDeployment()) {
					chosen.add(u.getEntity());
				} else {
					undeployed.append("\n")
					.append(u.getEntity().getDisplayName())
					.append(" (").append(u.checkDeployment())
					.append(")");
				}
			}
		}
		
		if (undeployed.length() > 0) {
			JOptionPane.showMessageDialog(
					getFrame(),
					"The following units could not be deployed:"
							+ undeployed.toString(),
					"Could not deploy some units", JOptionPane.WARNING_MESSAGE);
		}
	
		if(chosen.size() > 0) {
			UnitPrintManager.printAllUnits(chosen, true);
		}
	}
	
	protected void loadScenario() {
		int row = scenarioTable.getSelectedRow();
		if(row < 0) {
			return;
		}
		Scenario scenario = scenarioModel.getScenario(scenarioTable.convertRowIndexToModel(row));
		if(null != scenario) {
			((MekHQ)getApplication()).startHost(scenario, true, null);
		}
	}
	
	protected void startScenario() {
		int row = scenarioTable.getSelectedRow();
		if(row < 0) {
			return;
		}
		Scenario scenario = scenarioModel.getScenario(scenarioTable.convertRowIndexToModel(row));
		Vector<Integer> uids = scenario.getForces(getCampaign()).getAllUnits();
		
		if(uids.size() == 0) {
			return;
		}
		
		ArrayList<Unit> chosen = new ArrayList<Unit>();
		//ArrayList<Unit> toDeploy = new ArrayList<Unit>();
		StringBuffer undeployed = new StringBuffer();
		
		for(int uid : uids) {
			Unit u = getCampaign().getUnit(uid);
			if(u.isUnmanned()) {
				continue;
			}
			if (null != u.getEntity()) {
				if (null == u.checkDeployment()) {
					chosen.add(u);
				} else {
					undeployed.append("\n")
					.append(u.getEntity().getDisplayName())
					.append(" (").append(u.checkDeployment())
					.append(")");
				}
			}
		}
		
		if (undeployed.length() > 0) {
			JOptionPane.showMessageDialog(
					getFrame(),
					"The following units could not be deployed:"
							+ undeployed.toString(),
					"Could not deploy some units", JOptionPane.WARNING_MESSAGE);
		}
	
		if(chosen.size() > 0) {
			((MekHQ)getApplication()).startHost(scenario, false, chosen);
		}
	}
	
	protected void deployListFile() {
		int row = scenarioTable.getSelectedRow();
		if(row < 0) {
			return;
		}
		Scenario scenario = scenarioModel.getScenario(scenarioTable.convertRowIndexToModel(row));
		Vector<Integer> uids = scenario.getForces(getCampaign()).getAllUnits();
		
		if(uids.size() == 0) {
			return;
		}
		
		ArrayList<Entity> chosen = new ArrayList<Entity>();
		//ArrayList<Unit> toDeploy = new ArrayList<Unit>();
		StringBuffer undeployed = new StringBuffer();
		
		for(int uid : uids) {
			Unit u = getCampaign().getUnit(uid);
			if(u.isUnmanned()) {
				continue;
			}
			if (null != u.getEntity()) {
				if (null == u.checkDeployment()) {
					chosen.add(u.getEntity());
				} else {
					undeployed.append("\n")
					.append(u.getEntity().getDisplayName())
					.append(" (").append(u.checkDeployment())
					.append(")");
				}
			}
		}

		JFileChooser saveList = new JFileChooser(".");
		saveList.setDialogTitle("Deploy Units");
		
		saveList.setFileFilter(new FileFilter() {
			@Override
			public boolean accept(File dir) {
				if (dir.isDirectory()) {
					return true;
				}
				return dir.getName().endsWith(".mul");
			}

			@Override
			public String getDescription() {
				return "MUL file";
			}
		});
		
		saveList.setSelectedFile(new File(scenario.getName() + ".mul")); //$NON-NLS-1$
		int returnVal = saveList.showSaveDialog(mainPanel);
		
		if ((returnVal != JFileChooser.APPROVE_OPTION)
				|| (saveList.getSelectedFile() == null)) {
			// I want a file, y'know!
			return;
		}

		File unitFile = saveList.getSelectedFile();
		
		if (unitFile != null) {
			if (!(unitFile.getName().toLowerCase().endsWith(".mul") //$NON-NLS-1$
			|| unitFile.getName().toLowerCase().endsWith(".xml"))) { //$NON-NLS-1$
				try {
					unitFile = new File(unitFile.getCanonicalPath() + ".mul"); //$NON-NLS-1$
				} catch (IOException ie) {
					// nothing needs to be done here
					return;
				}
			}

			try {
				// Save the player's entities to the file.
				// FIXME: this is not working
				EntityListFile.saveTo(unitFile, chosen);

			} catch (IOException excep) {
				excep.printStackTrace(System.err);
			}
		}
		
		refreshServicedUnitList();
		refreshUnitList();
		refreshPatientList();
		refreshPersonnelList();

		if (undeployed.length() > 0) {
			JOptionPane.showMessageDialog(
					getFrame(),
					"The following units could not be deployed:"
							+ undeployed.toString(),
					"Could not deploy some units", JOptionPane.WARNING_MESSAGE);
		}
	}

	public void refreshServicedUnitList() {
		int selected = servicedUnitTable.getSelectedRow();
		servicedUnitModel.setData(getCampaign().getServiceableUnits());
		if ((selected > -1) && (selected < getCampaign().getServiceableUnits().size())) {
			servicedUnitTable.setRowSelectionInterval(selected, selected);
		}
	}
	
	public void refreshPersonnelList() {
		personModel.setData(getCampaign().getPersonnel());
	}
	
	public void changeMission() {
		int idx = choiceMission.getSelectedIndex();
		if(idx >= 0 && idx < getCampaign().getActiveMissions().size()) {
			Mission m = getCampaign().getActiveMissions().get(idx);
			if(null != m) {
				selectedMission = m.getId();		
				if(m instanceof Contract) {
					scrollMissionView.setViewportView(new ContractViewPanel((Contract)m));
				} else {
					scrollMissionView.setViewportView(new MissionViewPanel(m));
				}
				//This odd code is to make sure that the scrollbar stays at the top
				//I cant just call it here, because it ends up getting reset somewhere later
				javax.swing.SwingUtilities.invokeLater(new Runnable() {
					public void run() { 
						scrollMissionView.getVerticalScrollBar().setValue(0);
					}
				});
			}
		} else {
			selectedMission = -1;
			scrollMissionView.setViewportView(null);
		}
		refreshScenarioList();
	}
	
	public void refreshScenarioList() {
		Mission m = getCampaign().getMission(selectedMission);
		if(null != m) {
			scenarioModel.setData(m.getScenarios());
		} else {
			scenarioModel.setData(new ArrayList<Scenario>());
		}
	}
	
	public void refreshUnitList() {
		unitModel.setData(getCampaign().getUnits());
	}

	public void refreshTaskList() {
		taskModel.setData(getCampaign().getPartsNeedingServiceFor(currentServicedUnitId));
	}
	
	public void refreshAcquireList() {
		acquireModel.setData(getCampaign().getAcquisitionsForUnit(currentServicedUnitId));
	}
	
	public void refreshMissions() {
		choiceMission.removeAllItems();
		for(Mission m : getCampaign().getActiveMissions()) {
			choiceMission.addItem(m.getName());
			if(m.getId() == selectedMission) {
				choiceMission.setSelectedItem(m.getName());
			}
		}
		if(choiceMission.getSelectedIndex() == -1 && getCampaign().getActiveMissions().size() > 0) {
			selectedMission = getCampaign().getActiveMissions().get(0).getId();
			choiceMission.setSelectedIndex(0);
		}
		changeMission();
	}

	public void refreshTechsList() {
		int selected = TechTable.getSelectedRow();
		techsModel.setData(getCampaign().getTechs());
		if ((selected > -1) && (selected < getCampaign().getTechs().size())) {
			TechTable.setRowSelectionInterval(selected, selected);
		}
		String astechString = "<html><b>Astech Pool Minutes:</> " + getCampaign().getAstechPoolMinutes();
		if(getCampaign().isOvertimeAllowed()) {
			astechString += " [" + getCampaign().getAstechPoolOvertime() + " overtime]";
		}
		astechString += " (" + getCampaign().getNumberAstechs() + " Astechs)</html>";
		astechPoolLabel.setText(astechString); // NOI18N
	}

	public void refreshDoctorsList() {
		int selected = DocTable.getSelectedRow();
		doctorsModel.setData(getCampaign().getDoctors());
		if ((selected > -1) && (selected < getCampaign().getDoctors().size())) {
			DocTable.setRowSelectionInterval(selected, selected);
		}
	}

	public void refreshPatientList() {
		int selected = patientTable.getSelectedRow();
		patientModel.setData(getCampaign().getPatients());
		if ((selected > -1) && (selected < getCampaign().getPatients().size())) {
			patientTable.setRowSelectionInterval(selected, selected);
		}
	}

	public void refreshPartsList() {
		partsModel.setData(getCampaign().getPartsInventory());
	}
	
	public void refreshFinancialTransactions() {
		financeModel.setData(getCampaign().getFinances().getAllTransactions());
		refreshFunds();
	}

	public void refreshCalendar() {
		getFrame().setTitle(getCampaign().getTitle());
	}

	public void refreshReport() {
		txtPaneReport.setText(getCampaign().getCurrentReportHTML());
		txtPaneReport.setCaretPosition(0);
	}
	
	public void refreshOrganization() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() { 
				orgTree.updateUI();
				refreshForceView();
			}
		});
	}

	protected void refreshFunds() {
		long funds = getCampaign().getFunds();
		NumberFormat numberFormat = NumberFormat.getIntegerInstance();
		String text = "<html><b>Balance:</b> " + numberFormat.format(funds) + " C-Bills</html>";
		lblFunds.setText(text);
	}
	
	protected void refreshTempAstechs() {
		String text = "<html><b>Temp Astechs:</b> " +getCampaign().getAstechPool() + "</html>";
		lblTempAstechs.setText(text);
	}
	
	protected void refreshTempMedics() {
		String text = "<html><b>Temp Medics:</b> " +getCampaign().getMedicPool() + "</html>";
		lblTempMedics.setText(text);
	}
	
	
	public void refreshLocation() {
		lblLocation.setText(getCampaign().getLocation().getReport(getCampaign().getCalendar().getTime()));
	}

	protected void updateAssignEnabled() {
		// must have a valid tech and an unassigned task	
		Person tech = getCampaign().getPerson(currentTechId);
		if (null != tech) {
			if(repairsSelected()) {
				Part part = getCampaign().getPart(currentServiceablePartsId);
				if(null != part) {
					btnDoTask.setEnabled(getCampaign().getTargetFor(part, tech).getValue() != TargetRoll.IMPOSSIBLE);
				} else {
					btnDoTask.setEnabled(false);
				}
			} else if(acquireSelected()) {
				Part part = getCampaign().getPart(currentAcquisitionId);
				if(null != part && part instanceof IAcquisitionWork) {
					btnDoTask.setEnabled(getCampaign().getTargetForAcquisition((IAcquisitionWork)part, tech).getValue() != TargetRoll.IMPOSSIBLE);
				} else {
					btnDoTask.setEnabled(false);
				}
			} else {
				btnDoTask.setEnabled(false);
			}
		} else {
			btnDoTask.setEnabled(false);
		}
	}

	protected void updateAssignDoctorEnabled() {
		// must have a valid doctor and an unassigned task
		
		Person curPerson = getCampaign().getPerson(currentPatientId);
		Person doctor = getCampaign().getPerson(currentDoctorId);
		if (null != curPerson && curPerson.getAssignedTeamId() == -1 && null != doctor 
				&& getCampaign().getTargetFor(curPerson, doctor).getValue() != TargetRoll.IMPOSSIBLE) {
			btnAssignDoc.setEnabled(true);
		} else {
			btnAssignDoc.setEnabled(false);
		}
	}

	protected void updateTargetText() {
		// must have a valid team and an unassigned task
		Person tech = getCampaign().getPerson(currentTechId);
		if (null != tech) {
			TargetRoll target = null;
			if(acquireSelected()) {
				Part part = getCampaign().getPart(currentAcquisitionId);
				if(null != part && part instanceof IAcquisitionWork)
				target = getCampaign().getTargetForAcquisition((IAcquisitionWork)part, tech);
			} else {
				Part part = getCampaign().getPart(currentServiceablePartsId);
				if(null != part) {
					target = getCampaign().getTargetFor(part, tech);
				}
			}
			if(null != target) {
				textTarget.setText(target.getDesc());
				lblTargetNum.setText(target.getValueAsString());
			} else {
				textTarget.setText("");
				lblTargetNum.setText("-");
			}
		} else {
			textTarget.setText("");
			lblTargetNum.setText("-");
		}
	}
	
	public void filterPersonnel() {
        RowFilter<PersonnelTableModel, Integer> personTypeFilter = null;
        final int nGroup = choicePerson.getSelectedIndex();
        personTypeFilter = new RowFilter<PersonnelTableModel,Integer>() {
        	@Override
        	public boolean include(Entry<? extends PersonnelTableModel, ? extends Integer> entry) {
        		PersonnelTableModel personModel = entry.getModel();
        		Person person = personModel.getPerson(entry.getIdentifier());
        		int type = person.getPrimaryRole();
        		if ((nGroup == PG_ACTIVE) ||
        				(nGroup == PG_COMBAT && type <= Person.T_BA) ||
        				(nGroup == PG_SUPPORT && type > Person.T_BA) ||
        				(nGroup == PG_MW && type == Person.T_MECHWARRIOR) ||
        				(nGroup == PG_CREW && (type == Person.T_GVEE_DRIVER || type == Person.T_NVEE_DRIVER || type == Person.T_VTOL_PILOT || type == Person.T_VEE_GUNNER)) ||
        				(nGroup == PG_PILOT && type == Person.T_AERO_PILOT) ||
        				(nGroup == PG_PROTO && type == Person.T_PROTO_PILOT) ||
        				(nGroup == PG_BA && type == Person.T_BA) ||
        				(nGroup == PG_TECH && type > Person.T_BA && type != Person.T_DOCTOR) ||
        				(nGroup == PG_DOC && type == Person.T_DOCTOR)) {
        			return person.isActive();
        		} else if(nGroup == PG_RETIRE) {
        			return person.getStatus() == Person.S_RETIRED;
        		} else if(nGroup == PG_MIA) {
        			return person.getStatus() == Person.S_MIA;
        		} else if(nGroup == PG_KIA) {
        			return person.getStatus() == Person.S_KIA;
        		}
        		return false;
        	}
        };
        personnelSorter.setRowFilter(personTypeFilter);
    }
	
	public void filterParts() {
        RowFilter<PartsTableModel, Integer> partsTypeFilter = null;
        final int nGroup = choiceParts.getSelectedIndex();
        partsTypeFilter = new RowFilter<PartsTableModel,Integer>() {
        	@Override
        	public boolean include(Entry<? extends PartsTableModel, ? extends Integer> entry) {
        		PartsTableModel partsModel = entry.getModel();
        		Part part = partsModel.getPartAt(entry.getIdentifier());
        		if(nGroup == SG_ALL) {
        			return true;
        		} else if(nGroup == SG_ARMOR) {
        			return part instanceof Armor;
        		} else if(nGroup == SG_SYSTEM) {
        			return part instanceof MekGyro 
        				|| part instanceof EnginePart
        				|| part instanceof MekActuator
        				|| part instanceof MekLifeSupport
        				|| part instanceof MekSensor;
        		} else if(nGroup == SG_EQUIP) {
        			return part instanceof EquipmentPart;
        		} else if(nGroup == SG_LOC) {
        			return part instanceof MekLocation || part instanceof TankLocation;
        		} else if(nGroup == SG_WEAP) {
        			return part instanceof EquipmentPart && ((EquipmentPart)part).getType() instanceof WeaponType;
        		} else if(nGroup == SG_AMMO) {
        			return part instanceof EquipmentPart && ((EquipmentPart)part).getType() instanceof AmmoType;
        		} else if(nGroup == SG_MISC) {
        			return part instanceof EquipmentPart && ((EquipmentPart)part).getType() instanceof MiscType;
        		} else if(nGroup == SG_ENGINE) {
        			return part instanceof EnginePart;
        		} else if(nGroup == SG_GYRO) {
        			return part instanceof MekGyro;
        		} else if(nGroup == SG_ACT) {
        			return part instanceof MekActuator;
        		} else if(nGroup == SG_DAMAGE) {
        			return part.needsFixing();
        		}
        		return false;
        	}
        };
        partsSorter.setRowFilter(partsTypeFilter);
    }
	
	public void filterUnits() {
		RowFilter<UnitTableModel, Integer> unitTypeFilter = null;
		final int nGroup = choiceUnit.getSelectedIndex() - 1;
        unitTypeFilter = new RowFilter<UnitTableModel,Integer>() {
        	@Override
        	public boolean include(Entry<? extends UnitTableModel, ? extends Integer> entry) {
        		if(nGroup < 0) {
        			return true;
        		}
        		UnitTableModel unitModel = entry.getModel();
        		Unit unit = unitModel.getUnit(entry.getIdentifier());
        		Entity en = unit.getEntity();
        		int type = -1;
        		if(null != en) {
        			type = UnitType.determineUnitTypeCode(en);
        		}
        		return type == nGroup;
        	}
        };
        unitSorter.setRowFilter(unitTypeFilter);
    }
	
	private void changePersonnelView() {
	
		int view = choicePersonView.getSelectedIndex();
		XTableColumnModel columnModel = (XTableColumnModel)personnelTable.getColumnModel();
		if(view == PV_GENERAL) {
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_RANK), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_NAME), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_CALL), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_AGE), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_GENDER), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_TYPE), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_SKILL), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_ASSIGN), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_FORCE), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_DEPLOY), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_MECH), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_AERO), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_JET), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_SPACE), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_VEE), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_NVEE), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_VTOL), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_GUN_BA), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_SMALL_ARMS), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_ANTI_MECH), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_ARTY), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_TACTICS), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_STRATEGY), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_TECH_MECH), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_TECH_AERO), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_TECH_VEE), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_TECH_BA), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_MEDICAL), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_ADMIN), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_NEG), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_SCROUNGE), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_TOUGH), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_EDGE), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_NABIL), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_NIMP), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_HITS), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_SALARY), getCampaign().getCampaignOptions().payForSalaries());
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_XP), true);	
		} else if(view == PV_PILOT) {
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_RANK), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_NAME), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_CALL), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_AGE), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_GENDER), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_TYPE), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_SKILL), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_ASSIGN), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_FORCE), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_DEPLOY), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_MECH), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_AERO), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_JET), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_SPACE), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_VEE), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_NVEE), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_VTOL), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_GUN_BA), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_SMALL_ARMS), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_ANTI_MECH), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_ARTY), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_TACTICS), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_STRATEGY), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_TECH_MECH), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_TECH_AERO), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_TECH_VEE), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_TECH_BA), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_MEDICAL), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_ADMIN), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_NEG), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_SCROUNGE), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_TOUGH), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_EDGE), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_NABIL), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_NIMP), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_HITS), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_SALARY), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_XP), false);	
		} else if(view == PV_INF) {
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_RANK), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_NAME), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_CALL), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_AGE), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_GENDER), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_TYPE), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_SKILL), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_ASSIGN), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_FORCE), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_DEPLOY), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_MECH), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_AERO), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_JET), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_SPACE), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_VEE), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_NVEE), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_VTOL), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_GUN_BA), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_SMALL_ARMS), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_ANTI_MECH), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_ARTY), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_TACTICS), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_STRATEGY), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_TECH_MECH), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_TECH_AERO), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_TECH_VEE), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_TECH_BA), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_MEDICAL), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_ADMIN), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_NEG), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_SCROUNGE), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_TOUGH), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_EDGE), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_NABIL), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_NIMP), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_HITS), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_SALARY), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_XP), false);	
		} else if(view == PV_TACTIC) {
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_RANK), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_NAME), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_CALL), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_AGE), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_GENDER), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_TYPE), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_SKILL), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_ASSIGN), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_FORCE), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_DEPLOY), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_MECH), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_AERO), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_JET), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_SPACE), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_VEE), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_NVEE), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_VTOL), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_GUN_BA), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_SMALL_ARMS), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_ANTI_MECH), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_ARTY), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_TACTICS), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_STRATEGY), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_TECH_MECH), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_TECH_AERO), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_TECH_VEE), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_TECH_BA), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_MEDICAL), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_ADMIN), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_NEG), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_SCROUNGE), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_TOUGH), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_EDGE), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_NABIL), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_NIMP), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_HITS), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_SALARY), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_XP), false);	
		} else if(view == PV_TECH) {
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_RANK), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_NAME), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_CALL), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_AGE), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_GENDER), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_TYPE), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_SKILL), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_ASSIGN), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_FORCE), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_DEPLOY), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_MECH), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_AERO), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_JET), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_SPACE), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_VEE), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_NVEE), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_VTOL), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_GUN_BA), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_SMALL_ARMS), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_ANTI_MECH), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_ARTY), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_TACTICS), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_STRATEGY), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_TECH_MECH), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_TECH_AERO), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_TECH_VEE), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_TECH_BA), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_MEDICAL), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_ADMIN), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_NEG), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_SCROUNGE), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_TOUGH), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_EDGE), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_NABIL), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_NIMP), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_HITS), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_SALARY), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_XP), false);	
		} else if(view == PV_ADMIN) {
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_RANK), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_NAME), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_CALL), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_AGE), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_GENDER), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_TYPE), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_SKILL), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_ASSIGN), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_FORCE), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_DEPLOY), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_MECH), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_AERO), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_JET), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_SPACE), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_VEE), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_NVEE), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_VTOL), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_GUN_BA), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_SMALL_ARMS), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_ANTI_MECH), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_ARTY), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_TACTICS), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_STRATEGY), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_TECH_MECH), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_TECH_AERO), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_TECH_VEE), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_TECH_BA), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_MEDICAL), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_ADMIN), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_NEG), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_SCROUNGE), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_TOUGH), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_EDGE), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_NABIL), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_NIMP), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_HITS), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_SALARY), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_XP), false);	
		} else if(view == PV_FLUFF) {
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_RANK), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_NAME), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_CALL), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_AGE), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_GENDER), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_TYPE), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_SKILL), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_ASSIGN), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_FORCE), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_DEPLOY), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_MECH), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_AERO), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_JET), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_SPACE), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_VEE), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_NVEE), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_VTOL), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_GUN_BA), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_SMALL_ARMS), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_ANTI_MECH), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_ARTY), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_TACTICS), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_STRATEGY), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_TECH_MECH), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_TECH_AERO), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_TECH_VEE), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_TECH_BA), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_MEDICAL), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_ADMIN), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_NEG), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_SCROUNGE), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_TOUGH), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_EDGE), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_NABIL), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_NIMP), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_HITS), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_SALARY), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_XP), false);	
		}
	}
	
	private void changeUnitView() {
		
		int view = choiceUnitView.getSelectedIndex();
		XTableColumnModel columnModel = (XTableColumnModel)unitTable.getColumnModel();
		if(view == PV_GENERAL) {
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_CHASSIS), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_MODEL), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_TYPE), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_WCLASS), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_TECH), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_WEIGHT), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_COST), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_MAINTAIN), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_QUALITY), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_STATUS), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_PILOT), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_CREW), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_DEPLOY), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_BV), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_REPAIR), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_PARTS), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_QUIRKS), false);
		} else if(view == UV_DETAILS) {
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_CHASSIS), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_MODEL), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_TYPE), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_WCLASS), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_TECH), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_WEIGHT), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_COST), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_MAINTAIN), getCampaign().getCampaignOptions().payForMaintain());
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_QUALITY), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_STATUS), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_PILOT), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_CREW), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_DEPLOY), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_BV), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_REPAIR), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_PARTS), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_QUIRKS), true);
		} else if(view == UV_STATUS) {
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_CHASSIS), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_MODEL), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_TYPE), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_WCLASS), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_TECH), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_WEIGHT), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_COST), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_MAINTAIN), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_QUALITY), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_STATUS), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_PILOT), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_CREW), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_DEPLOY), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_BV), false);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_REPAIR), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_PARTS), true);
			columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_QUIRKS), false);
		}
	}
	
	protected MekHQ getApplication() {
		return app;
	}
	
	public Campaign getCampaign() {
		return getApplication().getCampaign();
	}
	
	protected DirectoryItems getPortraits() {
		return getApplication().getPortraits();
	}
	
	protected DirectoryItems getCamos() {
		return getApplication().getCamos();
	}
	
	protected DirectoryItems getForceIcons() {
		return getApplication().getForceIcons();
	}
	
	protected MechTileset getMechTiles() {
		return getApplication().getMechTiles();
	}
	
	public JFrame getFrame() {
		return frame;
	}
	
	protected int getSelectedTaskId() {
		if(repairsSelected()) {
			return currentServiceablePartsId;
		} else if(acquireSelected()) {
			return currentAcquisitionId;
		} else {
			return -1;
		}
	}
	
	protected boolean repairsSelected() {
		return tabTasks.getSelectedIndex() == 0;
	}
	
	protected boolean acquireSelected() {
		return tabTasks.getSelectedIndex() == 1;
	}

	/**
	 * A table model for displaying work items
	 */
	public abstract class ArrayTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 9081706049165214129L;
		protected String[] columnNames;
		protected ArrayList<?> data;

		public int getRowCount() {
			return data.size();
		}

		public int getColumnCount() {
			return 1;
		}

		@Override
		public String getColumnName(int column) {
			return columnNames[column];
		}

		@Override
		public Class<? extends Object> getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}

		@Override
		public boolean isCellEditable(int row, int col) {
			return false;
		}

		// fill table with values
		public void setData(ArrayList<?> array) {
			data = array;
			fireTableDataChanged();
		}

	}

	/**
	 * A table model for displaying work items
	 */
	public class TaskTableModel extends ArrayTableModel {
		private static final long serialVersionUID = -6256038046416893994L;

		public TaskTableModel() {
			columnNames = new String[] { "Tasks" };
			data = new ArrayList<Part>();
		}

		public Object getValueAt(int row, int col) {
			return ((Part) data.get(row)).getDesc();
		}

		public Part getTaskAt(int row) {
			return (Part) data.get(row);
		}

		public Part[] getTasksAt(int[] rows) {
			Part[] tasks = new Part[rows.length];
			for (int i = 0; i < rows.length; i++) {
				int row = rows[i];
				tasks[i] = (Part) data.get(row);
			}
			return tasks;
		}

		public TaskTableModel.Renderer getRenderer() {
			return new TaskTableModel.Renderer(getCamos(), getPortraits(), getMechTiles());
		}

		public class Renderer extends BasicInfo implements TableCellRenderer {
			
			public Renderer(DirectoryItems camos, DirectoryItems portraits,
					MechTileset mt) {
				super(camos, portraits, mt);
			}

			private static final long serialVersionUID = -3052618135259621130L;

			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				Component c = this;
		        Image imgTool = getToolkit().getImage("data/images/misc/tools.png"); //$NON-NLS-1$
		        this.setImage(imgTool);
				setOpaque(true);
				setText("<html>" + getValueAt(row, column).toString() + "</html>");
				//setToolTipText(task.getToolTip());
				if (isSelected) {
					select();
				} else {
					unselect();
				}

				return c;
			}

		}
	}

	public class TaskTableMouseAdapter extends MouseInputAdapter implements
			ActionListener {

		public void actionPerformed(ActionEvent action) {
			String command = action.getActionCommand();
			Part part = taskModel.getTaskAt(TaskTable.getSelectedRow());
			if(null == part) {
				return;
			}
			if (command.equalsIgnoreCase("SCRAP")) {
				Unit u = part.getUnit();
				getCampaign().addReport(part.scrap());
				if(null !=  u && !u.isRepairable() && u.getSalvageableParts().size() == 0) {
					getCampaign().removeUnit(u.getId());
				}
				refreshServicedUnitList();
				refreshUnitList();
				refreshTaskList();
				refreshUnitView();
				refreshPartsList();
				refreshAcquireList();
				refreshReport();
			} else if (command.contains("SWAP_AMMO")) {
				/*
				WorkItem task = taskModel.getTaskAt(TaskTable.getSelectedRow());
				if (task instanceof ReloadItem) {
					ReloadItem reload = (ReloadItem) task;
					Entity en = reload.getUnit().getEntity();
					Mounted m = reload.getMounted();
					if (null == m) {
						return;
					}
					AmmoType curType = (AmmoType) m.getType();
					String sel = command.split(":")[1];
					int selType = Integer.parseInt(sel);
					AmmoType newType = Utilities.getMunitionsFor(en, curType)
							.get(selType);
					reload.swapAmmo(newType);
					refreshTaskList();
					refreshUnitView();
					refreshAcquireList();
				}*/
			} else if (command.contains("CHANGE_MODE")) {
				String sel = command.split(":")[1];
				int selected = Integer.parseInt(sel);
				part.setMode(selected);
				refreshServicedUnitList();
				refreshUnitList();
				refreshTaskList();
				refreshUnitView();
				refreshAcquireList();
			} else if (command.contains("UNASSIGN")) {
			/*	for (WorkItem task : tasks) {
					task.resetTimeSpent();
					task.setTeam(null);
					refreshServicedUnitList();
					refreshUnitList();
					refreshTaskList();
					refreshAcquireList();
				}*/
			} else if (command.contains("FIX")) {
			/*	for (WorkItem task : tasks) {
					if (task.checkFixable() == null) {
						if ((task instanceof ReplacementItem)
								&& !((ReplacementItem) task).hasPart()) {
							ReplacementItem replace = (ReplacementItem) task;
							Part part = replace.partNeeded();
							replace.setPart(part);
							getCampaign().addPart(part);
						}
						task.succeed();
						if (task.isCompleted()) {
							getCampaign().removeTask(task);
						}
					}
					refreshServicedUnitList();
					refreshUnitList();
					refreshTaskList();
					refreshAcquireList();
					refreshPartsList();
				}*/
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			maybeShowPopup(e);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			maybeShowPopup(e);
		}

		private void maybeShowPopup(MouseEvent e) {
			JPopupMenu popup = new JPopupMenu();
			if (e.isPopupTrigger()) {
				int row = TaskTable.getSelectedRow();
				if(row < 0) {
					return;
				}
				Part part = taskModel.getTaskAt(row);
				JMenuItem menuItem = null;
				JMenu menu = null;
				JCheckBoxMenuItem cbMenuItem = null;
				// Mode (extra time, rush job, ...
				menu = new JMenu("Mode");
				for (int i = 0; i < Modes.MODE_N; i++) {
					cbMenuItem = new JCheckBoxMenuItem(
							Modes.getModeName(i));
					if (part.getMode() == i) {
						cbMenuItem.setSelected(true);
					} else {
						cbMenuItem.setActionCommand("CHANGE_MODE:" + i);
						cbMenuItem.addActionListener(this);
					}
					menu.add(cbMenuItem);
				}		
				popup.add(menu);
				// Scrap component
				menuItem = new JMenuItem("Scrap component");
				menuItem.setActionCommand("SCRAP");
				menuItem.addActionListener(this);
				menuItem.setEnabled(part.canScrap());
				popup.add(menuItem);
				// Remove assigned team for scheduled tasks
				/*
				menuItem = new JMenuItem("Remove Assigned Team");
				menuItem.setActionCommand("UNASSIGN");
				menuItem.addActionListener(this);
				menuItem.setEnabled(task.isAssigned());
				popup.add(menuItem);
				*/
				/*
				if (task instanceof ReloadItem) {
					ReloadItem reload = (ReloadItem) task;
					Entity en = reload.getUnit().getEntity();
					Mounted m = reload.getMounted();
					// Swap ammo
					menu = new JMenu("Swap Ammo");
					int i = 0;
					AmmoType curType = (AmmoType) m.getType();
					for (AmmoType atype : Utilities
							.getMunitionsFor(en, curType)) {
						cbMenuItem = new JCheckBoxMenuItem(atype.getDesc());
						if (atype.equals(curType)) {
							cbMenuItem.setSelected(true);
						} else {
							cbMenuItem.setActionCommand("SWAP_AMMO:" + i);
							cbMenuItem.addActionListener(this);
						}
						menu.add(cbMenuItem);
						i++;
					}
					if(menu.getItemCount() > 20) {
	                	MenuScroller.setScrollerFor(menu, 20);
	                }
					popup.add(menu);
				}
				*/
				menu = new JMenu("GM Mode");
				popup.add(menu);
				// Auto complete task
				/*
				menuItem = new JMenuItem("Complete Task");
				menuItem.setActionCommand("FIX");
				menuItem.addActionListener(this);
				menuItem.setEnabled(getCampaign().isGM()
						&& (null == task.checkFixable()));
				menu.add(menuItem);
				*/
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		}	
	}
	

	/**
	 * A table model for displaying work items
	 */
	public class AcquisitionTableModel extends ArrayTableModel {
		private static final long serialVersionUID = -6256038046416893994L;

		public AcquisitionTableModel() {
			columnNames = new String[] { "Parts Needed" };
			data = new ArrayList<IAcquisitionWork>();
		}

		public Object getValueAt(int row, int col) {
			return ((IAcquisitionWork) data.get(row)).getAcquisitionDesc();
		}

		public IAcquisitionWork getAcquisitionAt(int row) {
			return (IAcquisitionWork) data.get(row);
		}

		public AcquisitionTableModel.Renderer getRenderer() {
			return new AcquisitionTableModel.Renderer(getCamos(), getPortraits(), getMechTiles());
		}

		public class Renderer extends BasicInfo implements TableCellRenderer {
			public Renderer(DirectoryItems camos, DirectoryItems portraits,
					MechTileset mt) {
				super(camos, portraits, mt);
			}

			private static final long serialVersionUID = -3052618135259621130L;

			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				Component c = this;
				//MissingPart task = getAcquisitionAt(row);
		        Image imgTool = getToolkit().getImage("data/images/misc/tools.png"); //$NON-NLS-1$
		        this.setImage(imgTool);
				setOpaque(true);
				setText(getValueAt(row, column).toString());
				//setToolTipText(task.getToolTip());
				if (isSelected) {
					select();
				} else {
					unselect();
				}

				c.setBackground(new Color(220, 220, 220));
				return c;
			}

		}
	}

	/**
	 * A table model for displaying units that are being serviced in the repair bay
	 */
	public class ServicedUnitTableModel extends ArrayTableModel {
		private static final long serialVersionUID = 3314061779690077204L;

		public ServicedUnitTableModel() {
			columnNames = new String[] { "Units" };
			data = new ArrayList<Unit>();
		}

		public Object getValueAt(int row, int col) {
			return getCampaign().getUnitDesc(((Unit) data.get(row)).getId());
		}

		public Unit getUnitAt(int row) {
			return (Unit) data.get(row);
		}

		public Unit[] getUnitsAt(int[] rows) {
			Unit[] units = new Unit[rows.length];
			for (int i = 0; i < rows.length; i++) {
				int row = rows[i];
				units[i] = (Unit) data.get(row);
			}
			return units;
		}

		public ServicedUnitTableModel.Renderer getRenderer() {
			return new ServicedUnitTableModel.Renderer(getCamos(), getPortraits(), getMechTiles());
		}

		public class Renderer extends BasicInfo implements TableCellRenderer {

			public Renderer(DirectoryItems camos, DirectoryItems portraits,
					MechTileset mt) {
				super(camos, portraits, mt);
			}

			private static final long serialVersionUID = 6767431355690868748L;

			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				Component c = this;
				Unit u = getUnitAt(row);
				setOpaque(true);
				setUnit(u);
				setText(getValueAt(row, column).toString());
				//setToolTipText(getCampaign().getTaskListFor(u));
				if (isSelected) {
					select();
				} else {
					unselect();
				}
	
				c.setBackground(new Color(220, 220, 220));
				return c;
			}

		}
	}

	public class ServicedUnitsTableMouseAdapter extends MouseInputAdapter implements
			ActionListener {

		public void actionPerformed(ActionEvent action) {
			String command = action.getActionCommand();
			Unit selectedUnit = servicedUnitModel.getUnitAt(servicedUnitTable.getSelectedRow());
			Unit[] units = servicedUnitModel.getUnitsAt(servicedUnitTable.getSelectedRows());
			if (command.contains("ASSIGN_TECH")) {
				/*
				String sel = command.split(":")[1];
				int selected = Integer.parseInt(sel);
				if ((selected > -1)
						&& (selected < getCampaign().getTechTeams().size())) {
					SupportTeam team = getCampaign().getTechTeams().get(selected);
					if (null != team) {
						for (WorkItem task : getCampaign()
								.getTasksForUnit(selectedUnit.getId())) {
							if (team.getTargetFor(task).getValue() != TargetRoll.IMPOSSIBLE) {
								getCampaign().processTask(task, team);
							}
						}
					}
				}
				refreshServicedUnitList();
				refreshUnitList();
				refreshTaskList();
				refreshAcquireList();
				refreshTechsList();
				refreshReport();
				refreshPartsList();
				*/
			} else if (command.contains("SWAP_AMMO")) {
				String sel = command.split(":")[1];
				int selAmmoId = Integer.parseInt(sel);
				Part part = getCampaign().getPart(selAmmoId);
				if (null == part || !(part instanceof AmmoBin)) {
					return;
				}
				AmmoBin ammo = (AmmoBin)part;
				sel = command.split(":")[2];
				long munition = Long.parseLong(sel);
				ammo.changeMunition(munition);
				refreshTaskList();
				refreshAcquireList();
				refreshServicedUnitList();
				refreshUnitList();
			} else if (command.contains("CHANGE_SITE")) {
				for (Unit unit : units) {
					if (!unit.isDeployed()) {
						String sel = command.split(":")[1];
						int selected = Integer.parseInt(sel);
						if ((selected > -1) && (selected < Unit.SITE_N)) {
							unit.setSite(selected);
						}
					}
				}
				refreshServicedUnitList();
				refreshUnitList();
				refreshTaskList();
				refreshAcquireList();
			} else if (command.equalsIgnoreCase("SALVAGE")) {
				for (Unit unit : units) {
					if (!unit.isDeployed()) {
						unit.setSalvage(true);
						unit.runDiagnostic();
					}
				}
				refreshServicedUnitList();
				refreshUnitList();
			} else if (command.equalsIgnoreCase("REPAIR")) {
				for (Unit unit : units) {
					if (!unit.isDeployed() && unit.isRepairable()) {
						unit.setSalvage(false);
						unit.runDiagnostic();
					}
				}
				refreshServicedUnitList();
				refreshUnitList();
			} else if (command.equalsIgnoreCase("REMOVE")) {
				for (Unit unit : units) {
					if (!unit.isDeployed()) {
						if (0 == JOptionPane.showConfirmDialog(null,
								"Do you really want to remove "
										+ unit.getEntity().getDisplayName()
										+ "?", "Remove Unit?",
								JOptionPane.YES_NO_OPTION)) {
							getCampaign().removeUnit(unit.getId());
						}
					}
				}
				refreshServicedUnitList();
				refreshUnitList();
				refreshReport();
			} else if (command.equalsIgnoreCase("UNDEPLOY")) {
				for (Unit unit : units) {
					if (unit.isDeployed()) {
						/*
						if (null != unit.getPilot()) {
							unit.getPilot().undeploy(getCampaign());
						}
						*/
					}
				}
				refreshServicedUnitList();
				refreshUnitList();
				refreshPatientList();
				refreshPersonnelList();
				refreshScenarioList();
			} 
		}

		@Override
		public void mouseClicked(MouseEvent e) {

			if (e.getClickCount() == 2) {
				int row = servicedUnitTable.rowAtPoint(e.getPoint());
				Unit unit = servicedUnitModel.getUnitAt(row);
				if (null != unit) {
					MechView mv = new MechView(unit.getEntity(), false);
					MekViewDialog mvd = new MekViewDialog(null, true, mv);
					mvd.setVisible(true);
				}
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			maybeShowPopup(e);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			maybeShowPopup(e);
		}

		private void maybeShowPopup(MouseEvent e) {
			JPopupMenu popup = new JPopupMenu();
			if (e.isPopupTrigger()) {
				int row = servicedUnitTable.rowAtPoint(e.getPoint());
				Unit unit = servicedUnitModel.getUnitAt(row);
				JMenuItem menuItem = null;
				JMenu menu = null;
				JCheckBoxMenuItem cbMenuItem = null;
				// **lets fill the pop up menu**//
				// change the location
				menu = new JMenu("Change site");
				int i = 0;
				for (i = 0; i < Unit.SITE_N; i++) {
					cbMenuItem = new JCheckBoxMenuItem(Unit.getSiteName(i));
					if (unit.getSite() == i) {
						cbMenuItem.setSelected(true);
					} else {
						cbMenuItem.setActionCommand("CHANGE_SITE:" + i);
						cbMenuItem.addActionListener(this);
					}
					menu.add(cbMenuItem);
				}
				menu.setEnabled(!unit.isDeployed());
				popup.add(menu);
				// assign all tasks to a certain tech
				menu = new JMenu("Assign all tasks");
				i = 0;
				for (Person tech : getCampaign().getTechs()) {
					menuItem = new JMenuItem(tech.getDesc());
					menuItem.setActionCommand("ASSIGN_TECH:" + i);
					menuItem.addActionListener(this);
					menuItem.setEnabled(tech.getMinutesLeft() > 0);
					menu.add(menuItem);
					i++;
				}
				menu.setEnabled(!unit.isDeployed());
				if(menu.getItemCount() > 20) {
                	MenuScroller.setScrollerFor(menu, 20);
                }
				popup.add(menu);
				// swap ammo
				menu = new JMenu("Swap ammo");
				JMenu ammoMenu = null;
				for (AmmoBin ammo : unit.getWorkingAmmoBins()) {
					ammoMenu = new JMenu(ammo.getType().getDesc());
					AmmoType curType = (AmmoType) ammo.getType();
					for (AmmoType atype : Utilities.getMunitionsFor(unit.getEntity(), curType)) {
						cbMenuItem = new JCheckBoxMenuItem(atype.getDesc());
						if (atype.equals(curType)) {
							cbMenuItem.setSelected(true);
						} else {
							cbMenuItem.setActionCommand("SWAP_AMMO:"
									+ ammo.getId() + ":"
									+ atype.getMunitionType());
							cbMenuItem.addActionListener(this);
						}
						ammoMenu.add(cbMenuItem);
						i++;
					}
					if(menu.getItemCount() > 20) {
	                	MenuScroller.setScrollerFor(menu, 20);
	                }
					menu.add(ammoMenu);
				}
				menu.setEnabled(!unit.isDeployed());
				popup.add(menu);
				// Salvage / Repair
				if (unit.isSalvage()) {
					menuItem = new JMenuItem("Repair");
					menuItem.setActionCommand("REPAIR");
					menuItem.addActionListener(this);
					menuItem.setEnabled(!unit.isDeployed()
							&& unit.isRepairable() && !unit.isRefitting());
					popup.add(menuItem);
				} else if (!unit.isSalvage()) {
					menuItem = new JMenuItem("Salvage");
					menuItem.setActionCommand("SALVAGE");
					menuItem.addActionListener(this);
					menuItem.setEnabled(!unit.isDeployed()
							&& !unit.isRefitting());
					popup.add(menuItem);
				}
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}

	/**
	 * A table model for displaying work items
	 */
	public class TechTableModel extends ArrayTableModel {
		private static final long serialVersionUID = 2738333372316332962L;

		public TechTableModel() {
			columnNames = new String[] { "Techs" };
			data = new ArrayList<Person>();
		}

		public Object getValueAt(int row, int col) {
			return ((Person) data.get(row)).getTechDesc(getCampaign().isOvertimeAllowed());
		}

		public Person getTechAt(int row) {
			return (Person) data.get(row);
		}

		public TechTableModel.Renderer getRenderer() {
			return new TechTableModel.Renderer(getCamos(), getPortraits(), getMechTiles());
		}

		public class Renderer extends BasicInfo implements TableCellRenderer {
			public Renderer(DirectoryItems camos, DirectoryItems portraits,
					MechTileset mt) {
				super(camos, portraits, mt);
			}

			private static final long serialVersionUID = -4951696376098422679L;

			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				Component c = this;
				setOpaque(true);
				setPortrait(getTechAt(row));
				setText(getValueAt(row, column).toString());
				// setToolTipText(getCampaign().getToolTipFor(u));
				if (isSelected) {
					select();
				} else {
					unselect();
				}
				c.setBackground(new Color(220, 220, 220));
				return c;
			}

		}
	}

	/**
	 * A table model for displaying personnel in the infirmary
	 */
	public class PatientTableModel extends ArrayTableModel {
		private static final long serialVersionUID = -1615929049408417297L;

		public PatientTableModel() {
			columnNames = new String[] { "Personnel" };
			data = new ArrayList<Person>();
		}

		public Object getValueAt(int row, int col) {
			Person psn = ((Person) data.get(row));

			return psn.getPatientDesc(getCampaign());
		}

		public Person getPersonAt(int row) {
			return (Person) data.get(row);
		}

		public Person[] getPersonsAt(int[] rows) {
			Person[] persons = new Person[rows.length];

			for (int i = 0; i < rows.length; i++) {
				persons[i] = getPersonAt(rows[i]);
			}

			return persons;
		}

		public PatientTableModel.Renderer getRenderer() {
			return new PatientTableModel.Renderer(getCamos(), getPortraits(), getMechTiles());
		}

		public class Renderer extends BasicInfo implements TableCellRenderer {
			public Renderer(DirectoryItems camos, DirectoryItems portraits,
					MechTileset mt) {
				super(camos, portraits, mt);
			}

			private static final long serialVersionUID = -406535109900807837L;

			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				Component c = this;
				setOpaque(true);
				setPortrait(getPersonAt(row));
				setText(getValueAt(row, column).toString());
				Person p = getPersonAt(row);
				setPortrait(p);
				// setToolTipText(getCampaign().getToolTipFor(u));

				if (isSelected) {
					select();
				} else {
					unselect();
				}

				if ((null != p) && p.isDeployed(getCampaign())) {
					c.setBackground(Color.GRAY);
				} else if ((null != p) && p.needsFixing()) {
					c.setBackground(new Color(205, 92, 92));
				} else {
					c.setBackground(new Color(220, 220, 220));
				}

				return c;
			}
		}
	}

	public class OrgTreeModel implements TreeModel {

		private Force rootForce;
		private Vector<TreeModelListener> listeners = new Vector<TreeModelListener>();
		
		public OrgTreeModel(Force root) {
	        rootForce = root;
	    }
	
		@Override
		public Object getChild(Object parent, int index) {
			if(parent instanceof Force) {
				return ((Force)parent).getAllChildren(getCampaign()).get(index);
			} 
			return null;
		}

		@Override
		public int getChildCount(Object parent) {
			if(parent instanceof Force) {
				return ((Force)parent).getAllChildren(getCampaign()).size();
			}
			return 0;
		}

		@Override
		public int getIndexOfChild(Object parent, Object child) {
			if(parent instanceof Force) {
				return ((Force)parent).getAllChildren(getCampaign()).indexOf(child);
			}
			return 0;
		}

		@Override
		public Object getRoot() {
			return rootForce;
		}

		@Override
		public boolean isLeaf(Object node) {
			return node instanceof Unit || (node instanceof Force && ((Force)node).getAllChildren(getCampaign()).size() == 0);
		}

		@Override
		public void valueForPathChanged(TreePath arg0, Object arg1) {
			// TODO Auto-generated method stub
			
		}
		
		public void addTreeModelListener( TreeModelListener listener ) {
		      if ( listener != null && !listeners.contains( listener ) ) {
		         listeners.addElement( listener );
		      }
		   }

		   public void removeTreeModelListener( TreeModelListener listener ) {
		      if ( listener != null ) {
		         listeners.removeElement( listener );
		      }
		   }
/*
		   public void fireTreeNodesChanged( TreeModelEvent e ) {
		      Enumeration<TreeModelListener> listeners = listeners.elements();
		      while ( listeners.hasMoreElements() ) {
		         TreeModelListener listener = (TreeModelListener)listeners.nextElement();
		         listener.treeNodesChanged( e );
		      }
		   }

		   public void fireTreeNodesInserted( TreeModelEvent e ) {
		      Enumeration listeners = vector.elements();
		      while ( listeners.hasMoreElements() ) {
		         TreeModelListener listener = (TreeModelListener)listeners.nextElement();
		         listener.treeNodesInserted( e );
		      }
		   }

		   public void fireTreeNodesRemoved( TreeModelEvent e ) {
		      Enumeration listeners = vector.elements();
		      while ( listeners.hasMoreElements() ) {
		         TreeModelListener listener = (TreeModelListener)listeners.nextElement();
		         listener.treeNodesRemoved( e );
		      }
		   }

		   public void fireTreeStructureChanged( TreeModelEvent e ) {
		      Enumeration listeners = vector.elements();
		      while ( listeners.hasMoreElements() ) {
		         TreeModelListener listener = (TreeModelListener)listeners.nextElement();
		         listener.treeStructureChanged( e );
		      }
		   }
	*/	
	}
	
	public class OrgTreeMouseAdapter extends MouseInputAdapter implements
	ActionListener {

		@Override
		public void actionPerformed(ActionEvent action) {
			StringTokenizer st = new StringTokenizer(action.getActionCommand(), "|");
            String command = st.nextToken();
            int id = Integer.parseInt(st.nextToken());
            Force force = getCampaign().getForce(id);
            Unit unit = getCampaign().getUnit(id);
            if(command.contains("ADD_FORCE")) {
            	if(null != force) {
	            	String name = (String)JOptionPane.showInputDialog(
	                        null,
	                        "Enter the force name",
	                        "Force Name",
	                        JOptionPane.PLAIN_MESSAGE,
	                        null,
	                        null,
	                        "My Lance");
	            	getCampaign().addForce(new Force(name), force);
	            	refreshOrganization();
            	}
            } if(command.contains("ADD_UNIT")) {
            	if(null != force) {
                    Unit u = getCampaign().getUnit(Integer.parseInt(st.nextToken()));
                    if(null != u) {
                    	getCampaign().addUnitToForce(u, force.getId());
                    	refreshOrganization();
                    	refreshScenarioList();
                    	refreshPersonnelList();
                    	refreshUnitList();
                    	refreshServicedUnitList();
                    }
            	}
            } else if(command.contains("DEPLOY_FORCE")) {
                int sid = Integer.parseInt(st.nextToken());
            	Scenario scenario = getCampaign().getScenario(sid);
            	if(null != force && null != scenario) {
                    scenario.addForces(force.getId());
                    force.setScenarioId(scenario.getId());
                    refreshScenarioList();
                    for(int uid : force.getAllUnits()) {
                    	Unit u = getCampaign().getUnit(uid);
                    	if(null != u) {
                    		u.setScenarioId(scenario.getId());
                    	}
                    }
            	}
            	refreshScenarioList();
            	refreshOrganization();
            	refreshPersonnelList();
            	refreshUnitList();
            	refreshServicedUnitList();
            } else if(command.contains("CHANGE_ICON")) {
            	if(null != force) {
            		PortraitChoiceDialog pcd = new PortraitChoiceDialog(getFrame(), true,
    						force.getIconCategory(),
    						force.getIconFileName(), getForceIcons());
    				pcd.setVisible(true);
    				force.setIconCategory(pcd.getCategory());
    				force.setIconFileName(pcd.getFileName());
	            	refreshOrganization();
            	}
            } else if(command.contains("CHANGE_NAME")) {
            	if(null != force) {
	            	String name = (String)JOptionPane.showInputDialog(
	                        null,
	                        "Enter the force name",
	                        "Force Name",
	                        JOptionPane.PLAIN_MESSAGE,
	                        null,
	                        null,
	                        force.getName());
	            	force.setName(name);
	            	refreshOrganization();
            	}
            } else if(command.contains("CHANGE_DESC")) {
            	if(null != force) {
            		TextAreaDialog tad = new TextAreaDialog(getFrame(), true,
    						"Edit Force Description",
    						force.getDescription());
    				tad.setVisible(true);
    				if(tad.wasChanged()) {
    					force.setDescription(tad.getText());
    					refreshOrganization();
    				}        	
            	}
            } else if(command.contains("REMOVE_FORCE")) {
            	if(null != force) {
            		getCampaign().removeForce(force);
            		refreshOrganization();   
            		refreshPersonnelList();
            		refreshScenarioList();
            	}
            } else if(command.contains("REMOVE_UNIT")) {
            	if(null != unit) {
            		Force parentForce = getCampaign().getForceFor(unit);
            		if(null != parentForce) {
            			getCampaign().removeUnitFromForce(unit);
            			refreshOrganization();
            			refreshPersonnelList();
            			refreshScenarioList();
            		}
            	}
            } else if(command.contains("DEPLOY_UNIT")) {
                int sid = Integer.parseInt(st.nextToken());
            	Scenario scenario = getCampaign().getScenario(sid);
            	if(null != unit && null != scenario) {
                    scenario.addUnit(unit.getId());
                    unit.setScenarioId(scenario.getId());
                    refreshScenarioList();
                    refreshOrganization();
                    refreshPersonnelList();
                	refreshUnitList();
                	refreshServicedUnitList();
            	}
            }
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			maybeShowPopup(e);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			maybeShowPopup(e);
		}

		private void maybeShowPopup(MouseEvent e) {
				
			if (e.isPopupTrigger()) {
				JPopupMenu popup = new JPopupMenu();
				JMenuItem menuItem;
				JMenu menu;
				int x = e.getX();
                int y = e.getY();
                JTree tree = (JTree)e.getSource();
                TreePath path = tree.getPathForLocation(x, y);
                if (path == null)
                        return; 
                tree.setSelectionPath(path);
                Object node = path.getLastPathComponent();
                Force force = null;
                Unit unit = null;
                if(node instanceof Force) {
                	force = (Force)node;
                }
                if(node instanceof Unit) {
                	unit = (Unit)node;
                }
                if(null != force) {
                	int forceId = force.getId();
	                menuItem = new JMenuItem("Change Name...");
	                menuItem.setActionCommand("CHANGE_NAME|" + forceId);
					menuItem.addActionListener(this);
					menuItem.setEnabled(true);
	                popup.add(menuItem);
	                menuItem = new JMenuItem("Change Description...");
	                menuItem.setActionCommand("CHANGE_DESC|" + forceId);
					menuItem.addActionListener(this);
					menuItem.setEnabled(true);
	                popup.add(menuItem);
	                menuItem = new JMenuItem("Add New Force...");
	                menuItem.setActionCommand("ADD_FORCE|" + forceId);
					menuItem.addActionListener(this);
					menuItem.setEnabled(true);
	                popup.add(menuItem);
	                menu = new JMenu("Add Unit");
	                //only add units that have commanders
	                for(Unit u : getCampaign().getUnits()) {
	                	if(null != u.getCommander()) {
	                		Person p = u.getCommander();
	                		if(p.isActive() && u.getForceId() < 1) {
				                menuItem = new JMenuItem(p.getFullTitle() + ", " + u.getEntity().getDisplayName());
			                	menuItem.setActionCommand("ADD_UNIT|" + forceId + "|" + u.getId());
			                	menuItem.addActionListener(this);
			                	menuItem.setEnabled(!u.isDeployed());
			                	menu.add(menuItem);
		                	}
	                	}
	                }
	                if(menu.getItemCount() > 30) {
	                	MenuScroller.setScrollerFor(menu, 30);
	                }
	                popup.add(menu);   
	                if(!force.isDeployed() && force.getAllUnits().size()>0) {
	                	menu = new JMenu("Deploy Force");
	                	JMenu missionMenu;
	                	for(Mission m : getCampaign().getActiveMissions()) {
	                		missionMenu = new JMenu(m.getName());
	                		for(Scenario s : m.getScenarios()) {
	                			if(s.isCurrent()) {
	                				menuItem = new JMenuItem(s.getName());
		                			menuItem.setActionCommand("DEPLOY_FORCE|" + forceId + "|" + s.getId());
		    						menuItem.addActionListener(this);
		    						menuItem.setEnabled(true);
		    		                missionMenu.add(menuItem);
	                			}
	                		}
	                		menu.add(missionMenu);
	                	}
	                	popup.add(menu);
	                }
	                menuItem = new JMenuItem("Remove Force");
	                menuItem.setActionCommand("REMOVE_FORCE|" + forceId);
					menuItem.addActionListener(this);
					menuItem.setEnabled(null != force.getParentForce());
	                popup.add(menuItem);
	                menuItem = new JMenuItem("Change Force Icon...");
	                menuItem.setActionCommand("CHANGE_ICON|" + forceId);
					menuItem.addActionListener(this);
					menuItem.setEnabled(true);
	                popup.add(menuItem);
                }
                else if(null != unit) {
                	int uid = unit.getId();
                	Force parentForce = getCampaign().getForceFor(unit);
                	if(null != parentForce) {
	                	menuItem = new JMenuItem("Remove Unit from " + parentForce.getName());
		                menuItem.setActionCommand("REMOVE_UNIT|" + uid);
						menuItem.addActionListener(this);
						menuItem.setEnabled(true);
		                popup.add(menuItem);
                	}
                	if(!unit.isDeployed()) {
                		menu = new JMenu("Deploy Unit");
	                	JMenu missionMenu;
	                	for(Mission m : getCampaign().getActiveMissions()) {
	                		missionMenu = new JMenu(m.getName());
	                		for(Scenario s : m.getScenarios()) {
	                			if(s.isCurrent()) {
	                				menuItem = new JMenuItem(s.getName());
		                			menuItem.setActionCommand("DEPLOY_UNIT|" + uid + "|" + s.getId());
		    						menuItem.addActionListener(this);
		    						menuItem.setEnabled(true);
		    		                missionMenu.add(menuItem);
	                			}
	                		}
	                		if(missionMenu.getItemCount() > 30) {
	    	                	MenuScroller.setScrollerFor(missionMenu, 30);
	    	                }
	                		menu.add(missionMenu);
	                	}
	                	if(menu.getItemCount() > 30) {
		                	MenuScroller.setScrollerFor(menu, 30);
		                }
	                	popup.add(menu);
                	}
                }
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		}
		
	}
	
	public class ForceRenderer extends DefaultTreeCellRenderer {
     
		private static final long serialVersionUID = -553191867660269247L;

		public ForceRenderer() {
        	
        }

        public Component getTreeCellRendererComponent(
                            JTree tree,
                            Object value,
                            boolean sel,
                            boolean expanded,
                            boolean leaf,
                            int row,
                            boolean hasFocus) {

            super.getTreeCellRendererComponent(
                            tree, value, sel,
                            expanded, leaf, row,
                            hasFocus);
            setOpaque(true);
            setBackground(Color.WHITE);
            setForeground(Color.BLACK);
            if(hasFocus) {
            	setBackground(Color.DARK_GRAY);
                setForeground(Color.WHITE);
            }
            
            if(value instanceof Unit) {
            	String name = "<font color='red'>No Crew</font>";
            	String uname = "";
            	Unit u = (Unit)value;
            	Person pp = u.getCommander();
            	if(null != pp) {
            		name = pp.getFullTitle();
            		name += " (" + u.getEntity().getCrew().getGunnery() + "/" + u.getEntity().getCrew().getPiloting() + ")";
            		if(pp.needsFixing()) {
            			name = "<font color='red'>" + name + "</font>";
            		}     
            	}
            	uname = "<i>" + u.getEntity().getDisplayName() + "</i>";
            	if(u.isDamaged()) {
            		uname = "<font color='red'>" + uname + "</font>";
            	}          	
            	setText("<html>" + name + ", " + uname + "</html>");
            	if(u.isDeployed() && !hasFocus) {   		
            		setBackground(Color.LIGHT_GRAY);
            	} 
            }
            if(value instanceof Force) {
            	if(!hasFocus && ((Force)value).isDeployed()) {
            		setBackground(Color.LIGHT_GRAY);
            	}
            }
            setIcon(getIcon(value));

            
            
            return this;
        }
        
        protected Icon getIcon(Object node) {
        	
        	if(node instanceof Unit) {
        		return getIconFrom((Unit)node);
        	} else if(node instanceof Force) {
        		return getIconFrom((Force)node);
        	} else {
        		return null;
        	}
        }
        
        protected Icon getIconFrom(Unit unit) {
        	Person person = unit.getCommander();
        	if(null == person) {
        		return null;
        	}
        	String category = person.getPortraitCategory();
        	String file = person.getPortraitFileName();
        	
        	if(Pilot.ROOT_PORTRAIT.equals(category)) {
        		category = "";
        	}
        	
        	// Return a null if the player has selected no portrait file.
        	if ((null == category) || (null == file) || Pilot.PORTRAIT_NONE.equals(file)) {
        		file = "default.gif";
        	}

        	// Try to get the player's portrait file.
        	Image portrait = null;
        	try {
        		portrait = (Image) getPortraits().getItem(category, file);
        		//make sure no images are longer than 50 pixels
            	 if(null != portrait && portrait.getHeight(this) > 50) {
            		 portrait = portrait.getScaledInstance(-1, 50, Image.SCALE_DEFAULT);               
            	 }
            	 return new ImageIcon(portrait);
             } catch (Exception err) {
            	 err.printStackTrace();
            	 return null;     	
             }
        }
        
        protected Icon getIconFrom(Force force) {
            String category = force.getIconCategory();
            String file = force.getIconFileName();

            if(Pilot.ROOT_PORTRAIT.equals(category)) {
           	 category = "";
            }

            // Return a null if the player has selected no portrait file.
            if ((null == category) || (null == file) || Pilot.PORTRAIT_NONE.equals(file)) {
           	 file = "empty.png";
            }

            // Try to get the player's portrait file.
            Image portrait = null;
            try {
           	 portrait = (Image) getForceIcons().getItem(category, file);
           	 //make sure no images are longer than 50 pixels
           	 if(null != portrait && portrait.getHeight(this) > 50) {
           		 portrait = portrait.getScaledInstance(-1, 50, Image.SCALE_DEFAULT);               
           	 }
           	 return new ImageIcon(portrait);
            } catch (Exception err) {
           	 err.printStackTrace();
           	 return null;     	
            }
       }
    }	
	public class PersonnelTableMouseAdapter extends MouseInputAdapter implements
			ActionListener {
		
		private CampaignGUI view;
		
		public PersonnelTableMouseAdapter(CampaignGUI view) {
			super();
			this.view = view;
		}
		
		public void actionPerformed(ActionEvent action) {
			StringTokenizer st = new StringTokenizer(action.getActionCommand(), "|");
            String command = st.nextToken();
			int row = personnelTable.getSelectedRow();
			if(row < 0) {
				return;
			}
			Person selectedPerson = personModel.getPerson(personnelTable.convertRowIndexToModel(row));
			int[] rows = personnelTable.getSelectedRows();
			Person[] people = new Person[rows.length];
			for(int i=0; i<rows.length; i++) {
				people[i] = personModel.getPerson(personnelTable.convertRowIndexToModel(rows[i]));
			}
			if(command.contains("RANK")) {
				int rank = Integer.parseInt(st.nextToken());
				for(Person person : people) {
					person.setRank(rank);
					getCampaign().personUpdated(person);
				}
				refreshServicedUnitList();
				refreshUnitList();
				refreshPatientList();
				refreshPersonnelList();
				refreshTechsList();
				refreshDoctorsList();
				refreshOrganization();
			} else if(command.contains("PROLE")) {
				int role = Integer.parseInt(st.nextToken());
				for(Person person : people) {
					person.setPrimaryRole(role);
					getCampaign().personUpdated(person);
				}
				refreshServicedUnitList();
				refreshUnitList();
				refreshPatientList();
				refreshPersonnelList();
				refreshTechsList();
				refreshDoctorsList();
				refreshOrganization();
			} else if(command.contains("SROLE")) {
				int role = Integer.parseInt(st.nextToken());
				for(Person person : people) {
					person.setSecondaryRole(role);
					getCampaign().personUpdated(person);
				}
				refreshServicedUnitList();
				refreshUnitList();
				refreshPatientList();
				refreshPersonnelList();
				refreshTechsList();
				refreshDoctorsList();
				refreshOrganization();
			} else if (command.contains("REMOVE_UNIT")) {	
				Unit u = getCampaign().getUnit(selectedPerson.getUnitId());
				if(null != u) {
					u.remove(selectedPerson);
				}
				refreshServicedUnitList();
				refreshUnitList();
				refreshPersonnelList();
				refreshOrganization();
			} else if (command.contains("ADD_PILOT") || command.contains("ADD_SOLDIER")) {
				int selected = Integer.parseInt(st.nextToken());
				Unit u = getCampaign().getUnit(selected);
				Unit oldUnit = getCampaign().getUnit(selectedPerson.getUnitId());
				if(null != oldUnit) {
					oldUnit.remove(selectedPerson);
				}
				if(null != u) {
					u.addPilotOrSoldier(selectedPerson);
				}				
				refreshServicedUnitList();
				refreshUnitList();
				refreshPersonnelList();
				refreshOrganization();
			} else if (command.contains("ADD_DRIVER")) {
				int selected = Integer.parseInt(st.nextToken());		
				Unit u = getCampaign().getUnit(selected);
				Unit oldUnit = getCampaign().getUnit(selectedPerson.getUnitId());
				if(null != oldUnit) {
					oldUnit.remove(selectedPerson);
				} 
				if(null != u) {
					u.addDriver(selectedPerson);
				}
				refreshServicedUnitList();
				refreshUnitList();
				refreshPersonnelList();
				refreshOrganization();
			} else if (command.contains("ADD_GUNNER")) {
				int selected = Integer.parseInt(st.nextToken());		
				Unit u = getCampaign().getUnit(selected);
				Unit oldUnit = getCampaign().getUnit(selectedPerson.getUnitId());
				if(null != oldUnit) {
					oldUnit.remove(selectedPerson);
				}
				if(null != u) {
					u.addGunner(selectedPerson);
				}
				refreshServicedUnitList();
				refreshUnitList();
				refreshPersonnelList();
				refreshOrganization();
			} else if (command.contains("IMPROVE")) {
				String type = st.nextToken();
				int cost =  Integer.parseInt(st.nextToken());
				selectedPerson.improveSkill(type);
				getCampaign().personUpdated(selectedPerson);
				selectedPerson.setXp(selectedPerson.getXp() - cost);
				getCampaign().addReport(selectedPerson.getName() + " improved " + type + "!");
				refreshServicedUnitList();
				refreshUnitList();
				refreshPersonnelList();
				refreshTechsList();
				refreshDoctorsList();
				refreshReport();
				refreshOrganization();
			} else if (command.contains("ABILITY")) {
				String selected = st.nextToken();
				int cost =  Integer.parseInt(st.nextToken());
				selectedPerson.acquireAbility(PilotOptions.LVL3_ADVANTAGES, selected, true);
				getCampaign().personUpdated(selectedPerson);
				selectedPerson.setXp(selectedPerson.getXp() - cost);
				//TODO: add getCampaign() report
				refreshServicedUnitList();
				refreshUnitList();
				refreshPersonnelList();
				refreshTechsList();
				refreshDoctorsList();
				refreshReport();
			} else if (command.contains("WSPECIALIST")) {
				String selected = st.nextToken();
				int cost =  Integer.parseInt(st.nextToken());
				selectedPerson.acquireAbility(PilotOptions.LVL3_ADVANTAGES, "weapon_specialist", selected);
				getCampaign().personUpdated(selectedPerson);
				selectedPerson.setXp(selectedPerson.getXp() - cost);
				//TODO: add campaign report
				refreshServicedUnitList();
				refreshUnitList();
				refreshPersonnelList();
				refreshTechsList();
				refreshDoctorsList();
				refreshReport();
			} else if (command.contains("SPECIALIST")) {
				String selected = st.nextToken();
				int cost =  Integer.parseInt(st.nextToken());
				selectedPerson.acquireAbility(PilotOptions.LVL3_ADVANTAGES, "specialist", selected);
				getCampaign().personUpdated(selectedPerson);
				selectedPerson.setXp(selectedPerson.getXp() - cost);
				//TODO: add campaign report
				refreshServicedUnitList();
				refreshUnitList();
				refreshPersonnelList();
				refreshTechsList();
				refreshDoctorsList();
				refreshReport();
			} else if (command.equalsIgnoreCase("STATUS")) {
				int selected = Integer.parseInt(st.nextToken());
				for(Person person : people) {				
					if (selected == Person.S_ACTIVE
							|| (0 == JOptionPane.showConfirmDialog(
									null,
									"Do you really want to change the status of "
									+ person.getDesc()
									+ " to a non-active status?", "KIA?",
									JOptionPane.YES_NO_OPTION))) {
						getCampaign().changeStatus(person, selected);
					}
				}
				refreshServicedUnitList();
				refreshUnitList();
				refreshPatientList();
				refreshPersonnelList();
				filterPersonnel();
				refreshTechsList();
				refreshDoctorsList();
				refreshReport();
				refreshOrganization();
			} else if (command.equalsIgnoreCase("EDGE")) {
				String trigger = st.nextToken();
				selectedPerson.changeEdgeTrigger(trigger);
				getCampaign().personUpdated(selectedPerson);
				refreshPersonnelList();
				refreshPersonnelView();
			} else if (command.equalsIgnoreCase("REMOVE")) {
				for(Person person : people) {
					if (0 == JOptionPane
							.showConfirmDialog(
									null,
									"Do you really want to remove "
									+ person.getDesc() + "?",
									"Remove?", JOptionPane.YES_NO_OPTION)) {
						getCampaign().removePerson(person.getId());
					}
				}
				refreshServicedUnitList();
				refreshUnitList();
				refreshPatientList();
				refreshPersonnelList();
				refreshTechsList();
				refreshDoctorsList();
				refreshOrganization();
				refreshReport();
			} else if (command.equalsIgnoreCase("EDIT")) {
				CustomizePersonDialog npd = new CustomizePersonDialog(getFrame(), true, 
						selectedPerson, 
						false,
						getCampaign(),
						view);
				npd.setVisible(true);
				getCampaign().personUpdated(selectedPerson);
				refreshPatientList();
				refreshDoctorsList();
				refreshServicedUnitList();
				refreshUnitList();
				refreshPersonnelList();
				refreshOrganization();
			} else if (command.equalsIgnoreCase("HEAL")) {
				for(Person person : people) {
					person.setHits(0);
					person.setDoctorId(-1);
				}
				getCampaign().personUpdated(selectedPerson);
				refreshPatientList();
				refreshDoctorsList();
				refreshServicedUnitList();
				refreshUnitList();
				refreshPersonnelList();
				refreshOrganization();
			} else if (command.equalsIgnoreCase("PORTRAIT")) {
				PortraitChoiceDialog pcd = new PortraitChoiceDialog(getFrame(), true,
						selectedPerson.getPortraitCategory(),
						selectedPerson.getPortraitFileName(), getPortraits());
				pcd.setVisible(true);
				selectedPerson.setPortraitCategory(pcd.getCategory());
				selectedPerson.setPortraitFileName(pcd.getFileName());
				getCampaign().personUpdated(selectedPerson);
				refreshPatientList();
				refreshPersonnelList();
				refreshOrganization();
			} else if (command.equalsIgnoreCase("BIOGRAPHY")) {
				TextAreaDialog tad = new TextAreaDialog(getFrame(), true,
						"Edit Biography",
						selectedPerson.getBiography());
				tad.setVisible(true);
				if(tad.wasChanged()) {
					selectedPerson.setBiography(tad.getText());
				}
				refreshPersonnelList();
			} else if (command.equalsIgnoreCase("XP_ADD")) {
				for(Person person : people) {
					person.setXp(person.getXp() + 1);
				}
				refreshPatientList();
				refreshPersonnelList();
			} else if (command.equalsIgnoreCase("XP_SET")) {
				PopupValueChoiceDialog pvcd = new PopupValueChoiceDialog(
						getFrame(), true, "XP", selectedPerson.getXp(), 0, Math.max(selectedPerson.getXp()+10,100));
				pvcd.setVisible(true);
				int i = pvcd.getValue();
				selectedPerson.setXp(i);
				refreshPatientList();
				refreshPersonnelList();
			} else if (command.equalsIgnoreCase("EDGE_SET")) {
				PopupValueChoiceDialog pvcd = new PopupValueChoiceDialog(
						getFrame(), true, "Edge", selectedPerson.getEdge(), 0, 10);
				pvcd.setVisible(true);
				int i = pvcd.getValue();
				selectedPerson.setEdge(i);
				getCampaign().personUpdated(selectedPerson);
				refreshPersonnelList();
			} else if (command.equalsIgnoreCase("KILL")) {
				NewKillDialog nkd = new NewKillDialog(getFrame(), true, getCampaign(), selectedPerson);
				nkd.setVisible(true);
				refreshPersonnelList();
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			maybeShowPopup(e);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			maybeShowPopup(e);
		}

		private void maybeShowPopup(MouseEvent e) {
			JPopupMenu popup = new JPopupMenu();

			if (e.isPopupTrigger()) {
				if(personnelTable.getSelectedRowCount() == 0) {
	            	return;
	            }
	            int[] rows = personnelTable.getSelectedRows();
	            int row = personnelTable.getSelectedRow();
	            boolean oneSelected = personnelTable.getSelectedRowCount() == 1;
				Person person = personModel.getPerson(personnelTable.convertRowIndexToModel(row));
				JMenuItem menuItem = null;
				JMenu menu = null;
				JCheckBoxMenuItem cbMenuItem = null;
				// **lets fill the pop up menu**//
				menu = new JMenu("Change Rank");
				int rankOrder = 0;
				for(String rank : getCampaign().getRanks().getAllRanks()) {
					cbMenuItem = new JCheckBoxMenuItem(rank);
					cbMenuItem.setActionCommand("RANK|" + rankOrder);
					if(person.getRank() == rankOrder) {
						cbMenuItem.setSelected(true);
					}
					cbMenuItem.addActionListener(this);
					cbMenuItem.setEnabled(true);
					menu.add(cbMenuItem);
					rankOrder++;
				}
				if(menu.getItemCount() > 20) {
                	MenuScroller.setScrollerFor(menu, 20);
                }
				popup.add(menu);
				menu = new JMenu("Change Status");
				for(int s = 0; s < Person.S_NUM; s++) {
					cbMenuItem = new JCheckBoxMenuItem(Person.getStatusName(s));
					if(person.getStatus() == s) {
						cbMenuItem.setSelected(true);
					}
					cbMenuItem.setActionCommand("STATUS|" + s);
					cbMenuItem.addActionListener(this);
					cbMenuItem.setEnabled(true);
					menu.add(cbMenuItem);
				}
				popup.add(menu);
				menu = new JMenu("Change Primary Role");
				for(int i = Person.T_MECHWARRIOR; i < Person.T_NUM; i++) {
					if(person.canPerformRole(i) && person.getSecondaryRole() != i) {
						cbMenuItem = new JCheckBoxMenuItem(Person.getRoleDesc(i));
						cbMenuItem.setActionCommand("PROLE|" + i);
						if(person.getPrimaryRole() == i) {
							cbMenuItem.setSelected(true);
						}
						cbMenuItem.addActionListener(this);
						cbMenuItem.setEnabled(true);
						menu.add(cbMenuItem);
					}
				}
				if(menu.getItemCount() > 20) {
                	MenuScroller.setScrollerFor(menu, 20);
                }
				popup.add(menu);
				menu = new JMenu("Change Secondary Role");
				for(int i = 0; i < Person.T_NUM; i++) {
					if(i == Person.T_NONE || (i >= Person.T_MECH_TECH && person.canPerformRole(i) && person.getPrimaryRole() != i)) {
						//you cant be an astech if you are a tech, or a medic if you are a doctor
						if(person.isTechPrimary() && i == Person.T_ASTECH) {
							continue;
						}
						if(person.getPrimaryRole() == Person.T_DOCTOR && i == Person.T_MEDIC) {
							continue;
						}
						cbMenuItem = new JCheckBoxMenuItem(Person.getRoleDesc(i));
						cbMenuItem.setActionCommand("SROLE|" + i);
						if(person.getSecondaryRole() == i) {
							cbMenuItem.setSelected(true);
						}
						cbMenuItem.addActionListener(this);
						cbMenuItem.setEnabled(true);
						menu.add(cbMenuItem);
					}
				}
				if(menu.getItemCount() > 20) {
                	MenuScroller.setScrollerFor(menu, 20);
                }
				popup.add(menu);
				// switch pilot
				if(oneSelected && person.isActive()) {
						menu = new JMenu("Assign to Unit");
						JMenu pilotMenu = new JMenu("As Pilot");
						JMenu driverMenu = new JMenu("As Driver");
						JMenu gunnerMenu = new JMenu("As Gunner");
						JMenu soldierMenu = new JMenu("As Soldier");					
						cbMenuItem = new JCheckBoxMenuItem("None");
						/*if(!person.isAssigned()) {
							cbMenuItem.setSelected(true);
						}*/
						cbMenuItem.setActionCommand("REMOVE_UNIT|" + -1);
						cbMenuItem.addActionListener(this);
						menu.add(cbMenuItem);
						for (Unit unit : getCampaign().getUnits()) {
							if(unit.usesSoloPilot()) {
								if(unit.canTakeMoreDrivers() && person.canDrive(unit.getEntity()) && person.canGun(unit.getEntity())) {
									cbMenuItem = new JCheckBoxMenuItem(unit.getEntity().getDisplayName());
									//TODO: check the box
									cbMenuItem.setActionCommand("ADD_PILOT|" + unit.getId());
									cbMenuItem.addActionListener(this);
									pilotMenu.add(cbMenuItem);
								}
							}
							else if(unit.usesSoldiers()) {
								if(unit.canTakeMoreGunners() && person.canGun(unit.getEntity())) {
									cbMenuItem = new JCheckBoxMenuItem(unit.getEntity().getDisplayName());
									//TODO: check the box
									cbMenuItem.setActionCommand("ADD_SOLDIER|" + unit.getId());
									cbMenuItem.addActionListener(this);
									soldierMenu.add(cbMenuItem);
								}
							} else {
								if(unit.canTakeMoreDrivers() && person.canDrive(unit.getEntity())) {
									cbMenuItem = new JCheckBoxMenuItem(unit.getEntity().getDisplayName());
									//TODO: check the box
									cbMenuItem.setActionCommand("ADD_DRIVER|" + unit.getId());
									cbMenuItem.addActionListener(this);
									driverMenu.add(cbMenuItem);
								}
								if(unit.canTakeMoreGunners() && person.canGun(unit.getEntity())) {
									cbMenuItem = new JCheckBoxMenuItem(unit.getEntity().getDisplayName());
									//TODO: check the box
									cbMenuItem.setActionCommand("ADD_GUNNER|" + unit.getId());
									cbMenuItem.addActionListener(this);
									gunnerMenu.add(cbMenuItem);
								}
							}
						}
						if(pilotMenu.getItemCount() > 0) {
							menu.add(pilotMenu);
							if(pilotMenu.getItemCount() > 20) {
			                	MenuScroller.setScrollerFor(pilotMenu, 20);
			                }
						}
						if(driverMenu.getItemCount() > 0) {
							menu.add(driverMenu);
							if(driverMenu.getItemCount() > 20) {
			                	MenuScroller.setScrollerFor(driverMenu, 20);
			                }
						}
						if(gunnerMenu.getItemCount() > 0) {
							menu.add(gunnerMenu);
							if(gunnerMenu.getItemCount() > 20) {
			                	MenuScroller.setScrollerFor(gunnerMenu, 20);
			                }
						}
						if(soldierMenu.getItemCount() > 0) {
							menu.add(soldierMenu);
							if(soldierMenu.getItemCount() > 20) {
			                	MenuScroller.setScrollerFor(soldierMenu, 20);
			                }
						}
						menu.setEnabled(!person.isDeployed(getCampaign()));					
						popup.add(menu);
				}
				menuItem = new JMenuItem("Add XP");
				menuItem.setActionCommand("XP_ADD");
				menuItem.addActionListener(this);
				menuItem.setEnabled(true);
				popup.add(menuItem);
				if(oneSelected) {
					menu = new JMenu("Spend XP");
					JMenu currentMenu = new JMenu("Current Skills");
					JMenu newMenu = new JMenu("New Skills");
					for(int i = 0; i < SkillType.getSkillList().length; i++) {
						String type = SkillType.getSkillList()[i];
						if(person.hasSkill(type)) {
							int cost = person.getSkill(type).getCostToImprove();
							if(cost >= 0) {
								String costDesc = " (" + cost + "XP)";
								menuItem = new JMenuItem(type + costDesc);
								menuItem.setActionCommand("IMPROVE|" + type + "|" + cost);
								menuItem.addActionListener(this);
								menuItem.setEnabled(person.getXp() >= cost);
								currentMenu.add(menuItem);
							}
						} else {
							int cost = SkillType.getType(type).getCost(0);
							if(cost >= 0) {
								String costDesc = " (" + cost + "XP)";
								menuItem = new JMenuItem(type + costDesc);
								menuItem.setActionCommand("IMPROVE|" + type + "|" + cost);
								menuItem.addActionListener(this);
								menuItem.setEnabled(person.getXp() >= cost);
								newMenu.add(menuItem);
							}
						}
					}
					menu.add(currentMenu);
					menu.add(newMenu);
					if(getCampaign().getCampaignOptions().useAbilities()) {
						JMenu abMenu = new JMenu("Special Abilities");
						int cost = -1;
						String costDesc = "";
						for (Enumeration<IOption> i = person.getOptions(PilotOptions.LVL3_ADVANTAGES); i.hasMoreElements();) {
							IOption ability = i.nextElement();
							if(!ability.booleanValue()) {
								cost = SkillType.getAbilityCost(ability.getName());
								costDesc = " (" + cost + "XP)";
								if(cost < 0) {
									costDesc = " (Not Possible)";
								}
								if(ability.getName().equals("weapon_specialist")) {
									Unit u = getCampaign().getUnit(person.getUnitId());
									if(null != u) {
										JMenu specialistMenu = new JMenu("Weapon Specialist");
										TreeSet<String> uniqueWeapons = new TreeSet<String>();
										for (int j = 0; j < u.getEntity().getWeaponList().size(); j++) {
											Mounted m = u.getEntity().getWeaponList().get(j);
											uniqueWeapons.add(m.getName());
										}
										for (String name : uniqueWeapons) {
											menuItem = new JMenuItem(name + costDesc);
											menuItem.setActionCommand("WSPECIALIST|" + name + "|" + cost);
											menuItem.addActionListener(this);
											menuItem.setEnabled(cost >= 0 && person.getXp() >= cost);
											specialistMenu.add(menuItem);
										}
										abMenu.add(specialistMenu);
									}
								} else if(ability.getName().equals("specialist")) {
									JMenu specialistMenu = new JMenu("Specialist");
									menuItem = new JMenuItem("Laser Specialist" + costDesc);
									menuItem.setActionCommand("SPECIALIST|" + Pilot.SPECIAL_LASER + "|" + cost);
									menuItem.addActionListener(this);
									menuItem.setEnabled(cost >= 0 && person.getXp() >= cost);
									specialistMenu.add(menuItem);
									menuItem = new JMenuItem("Missile Specialist" + costDesc);
									menuItem.setActionCommand("SPECIALIST|" + Pilot.SPECIAL_MISSILE + "|" + cost);
									menuItem.addActionListener(this);
									menuItem.setEnabled(cost >= 0 && person.getXp() >= cost);
									specialistMenu.add(menuItem);
									menuItem = new JMenuItem("Ballistic Specialist" + costDesc);
									menuItem.setActionCommand("SPECIALIST|" + Pilot.SPECIAL_BALLISTIC + "|" + cost);
									menuItem.addActionListener(this);
									menuItem.setEnabled(cost >= 0 && person.getXp() >= cost);
									specialistMenu.add(menuItem);
									abMenu.add(specialistMenu);
								} else {
									menuItem = new JMenuItem(ability.getDisplayableName() + costDesc);
									menuItem.setActionCommand("ABILITY|" + ability.getName() + "|" + cost);
									menuItem.addActionListener(this);
									menuItem.setEnabled(cost >= 0 && person.getXp() >= cost);
									abMenu.add(menuItem);
								}
							}
						}
						if(abMenu.getItemCount() > 20) {
							MenuScroller.setScrollerFor(abMenu, 20);
						}
						menu.add(abMenu);
					}
					popup.add(menu);
				}
				if(oneSelected) {
					if(getCampaign().getCampaignOptions().useEdge()) {
						menu = new JMenu("Set Edge Triggers");
						cbMenuItem = new JCheckBoxMenuItem("Head Hits");
						if (person.getOptions().booleanOption("edge_when_headhit")) {
							cbMenuItem.setSelected(true);
						}
						cbMenuItem.setActionCommand("EDGE|edge_when_headhit");
						cbMenuItem.addActionListener(this);
						menu.add(cbMenuItem);
						cbMenuItem = new JCheckBoxMenuItem("Through Armor Crits");
						if (person.getOptions().booleanOption("edge_when_tac")) {
							cbMenuItem.setSelected(true);
						}
						cbMenuItem.setActionCommand("EDGE|edge_when_tac");
						cbMenuItem.addActionListener(this);
						menu.add(cbMenuItem);
						cbMenuItem = new JCheckBoxMenuItem("Fail KO check");
						if (person.getOptions().booleanOption("edge_when_ko")) {
							cbMenuItem.setSelected(true);
						}
						cbMenuItem.setActionCommand("EDGE|edge_when_ko");
						cbMenuItem.addActionListener(this);
						menu.add(cbMenuItem);
						cbMenuItem = new JCheckBoxMenuItem("Ammo Explosion");
						if (person.getOptions().booleanOption("edge_when_explosion")) {
							cbMenuItem.setSelected(true);
						}
						cbMenuItem.setActionCommand("EDGE|edge_when_explosion");
						cbMenuItem.addActionListener(this);
						menu.add(cbMenuItem);
						popup.add(menu);
					}
				}
				if(oneSelected) {
					// change portrait
					menuItem = new JMenuItem("Change Portrait...");
					menuItem.setActionCommand("PORTRAIT");
					menuItem.addActionListener(this);
					menuItem.setEnabled(true);
					popup.add(menuItem);	
					// change Biography
					menuItem = new JMenuItem("Change Biography...");
					menuItem.setActionCommand("BIOGRAPHY");
					menuItem.addActionListener(this);
					menuItem.setEnabled(true);
					popup.add(menuItem);
					menuItem = new JMenuItem("Assign Kill...");
					menuItem.setActionCommand("KILL");
					menuItem.addActionListener(this);
					menuItem.setEnabled(true);
					popup.add(menuItem);
				}
				menu = new JMenu("GM Mode");
				menuItem = new JMenuItem("Remove Person");
				menuItem.setActionCommand("REMOVE");
				menuItem.addActionListener(this);
				menuItem.setEnabled(getCampaign().isGM());
				menu.add(menuItem);
				menuItem = new JMenuItem("Heal Person");
				menuItem.setActionCommand("HEAL");
				menuItem.addActionListener(this);
				menuItem.setEnabled(getCampaign().isGM());
				menu.add(menuItem);
				if(oneSelected) {
					/*
					if (person instanceof PilotPerson) {
						menuItem = new JMenuItem("Undeploy Pilot");
						menuItem.setActionCommand("UNDEPLOY");
						menuItem.addActionListener(this);
						menuItem.setEnabled(getCampaign().isGM() && person.isDeployed());
						menu.add(menuItem);
					}*/		
					menuItem = new JMenuItem("Edit...");
					menuItem.setActionCommand("EDIT");
					menuItem.addActionListener(this);
					menuItem.setEnabled(getCampaign().isGM());
					menu.add(menuItem);
					menuItem = new JMenuItem("Set XP");
					menuItem.setActionCommand("XP_SET");
					menuItem.addActionListener(this);
					menuItem.setEnabled(getCampaign().isGM());
					menu.add(menuItem);
				}
				if(getCampaign().getCampaignOptions().useEdge()) {
					menuItem = new JMenuItem("Set Edge");
					menuItem.setActionCommand("EDGE_SET");
					menuItem.addActionListener(this);
					menuItem.setEnabled(getCampaign().isGM());
					menu.add(menuItem);
				}
				popup.addSeparator();
				popup.add(menu);
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}

	/**
	 * A table Model for displaying information about personnel
	 * @author Jay lawson
	 */
	public class PersonnelTableModel extends AbstractTableModel {
	
		private static final long serialVersionUID = -5207167419079014157L;
		
		private final static int COL_RANK =    0;
		private final static int COL_NAME =    1;
        private final static int COL_CALL =    2;
        private final static int COL_AGE =     3;
        private final static int COL_GENDER =  4;
        private final static int COL_SKILL =   5;
        private final static int COL_TYPE =    6;
        private final static int COL_ASSIGN =  7;
        private final static int COL_FORCE  =  8;
        private final static int COL_DEPLOY =  9;
        private final static int COL_MECH =       10;
        private final static int COL_AERO =       11;
        private final static int COL_JET =        12;
        private final static int COL_VEE =        13;
        private final static int COL_VTOL       = 14;
        private final static int COL_NVEE       = 15;
        private final static int COL_SPACE     =  16;
        private final static int COL_ARTY     =   17;
        private final static int COL_GUN_BA     = 18;
        private final static int COL_SMALL_ARMS = 19;
        private final static int COL_ANTI_MECH  = 20;
        private final static int COL_TACTICS    = 21;
        private final static int COL_STRATEGY   = 22;
        private final static int COL_TECH_MECH  = 23;
        private final static int COL_TECH_AERO  = 24;
        private final static int COL_TECH_VEE   = 25;
        private final static int COL_TECH_BA    = 26;
        private final static int COL_MEDICAL    = 27;
        private final static int COL_ADMIN      = 28;
        private final static int COL_NEG        = 29;
        private final static int COL_SCROUNGE   = 30;     
        private final static int COL_TOUGH =   31;
        private final static int COL_EDGE  =   32;
        private final static int COL_NABIL =   33;
        private final static int COL_NIMP  =   34;
        private final static int COL_HITS  =   35;
        private final static int COL_SALARY =  36;
        private final static int COL_XP =      37;
        private final static int N_COL =       38;
        
        private ArrayList<Person> data = new ArrayList<Person>();
        
        public int getRowCount() {
            return data.size();
        }

        public int getColumnCount() {
            return N_COL;
        }

        @Override
        public String getColumnName(int column) {
            switch(column) {
            	case COL_RANK:
            		return "Rank";
                case COL_NAME:
                    return "Name";
                case COL_CALL:
                    return "Callsign";
                case COL_AGE:
                    return "Age";
                case COL_GENDER:
                    return "Gender";
                case COL_TYPE:
                    return "Role";
                case COL_MECH:
                    return "Mech";
                case COL_AERO:
                    return "Aero";
                case COL_JET:
                    return "Aircraft";
                case COL_VEE:
                    return "Vehicle";
                case COL_VTOL:
                    return "VTOL";
                case COL_NVEE:
                    return "Naval";
                case COL_SPACE:
                    return "Spacecraft";
                case COL_ARTY:
                    return "Artillery";
                case COL_GUN_BA:
                    return "G/Battlesuit";
                case COL_SMALL_ARMS:
                    return "Small Arms";
                case COL_ANTI_MECH:
                    return "Anti-Mech";
                case COL_TACTICS:
                    return "Tactics";
                case COL_STRATEGY:
                    return "Strategy";
                case COL_TECH_MECH:
                    return "Tech/Mech";
                case COL_TECH_AERO:
                    return "Tech/Aero";
                case COL_TECH_VEE:
                    return "Mechanic";
                case COL_TECH_BA:
                    return "Tech/BA";
                case COL_MEDICAL:
                    return "Medical";
                case COL_ADMIN:
                    return "Admin";
                case COL_NEG:
                    return "Negotiation";
                case COL_SCROUNGE:
                    return "Scrounge";
                case COL_TOUGH:
                    return "Toughness";
                case COL_SKILL:
                    return "Skill Level";
                case COL_ASSIGN:
                    return "Unit Assignment";
                case COL_EDGE:
                    return "Edge";
                case COL_NABIL:
                    return "# Abilities";
                case COL_NIMP:
                    return "# Implants";
                case COL_HITS:
                    return "Hits";
                case COL_XP:
                    return "XP";
                case COL_DEPLOY:
                    return "Deployed";
                case COL_FORCE:
                    return "Force";
                case COL_SALARY:
                    return "Salary";
                default:
                    return "?";
            }
        }
        
        public int getColumnWidth(int c) {
            switch(c) {
        	case COL_RANK:
        	case COL_DEPLOY:
        		return 70;
            case COL_CALL:
            case COL_SALARY:
            case COL_SKILL:
                return 50;        
            case COL_TYPE:
            case COL_FORCE:
                return 100;
            case COL_NAME:
            case COL_ASSIGN:
                return 125;
            default:
                return 20;
            }
        }
        
        public int getAlignment(int col) {
            switch(col) {
            case COL_SALARY:
            	return SwingConstants.RIGHT;
            case COL_RANK:
            case COL_NAME:
            case COL_GENDER:
            case COL_TYPE:
            case COL_DEPLOY:
            case COL_FORCE:
            case COL_ASSIGN:
            case COL_SKILL:
            	return SwingConstants.LEFT;
            default:
            	return SwingConstants.CENTER;
            }
        }

        public String getTooltip(int row, int col) {
        	Person p = data.get(row);
        	switch(col) {
        	case COL_NABIL:
        		return p.getAbilityList(PilotOptions.LVL3_ADVANTAGES);
        	case COL_NIMP:
        		return p.getAbilityList(PilotOptions.MD_ADVANTAGES);
            default:
            	return null;
            }
        }
        
        @Override
        public Class<?> getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            return false;
        }

        public Person getPerson(int i) {
            return data.get(i);
        }

        //fill table with values
        public void setData(ArrayList<Person> people) {
            data = people;
            fireTableDataChanged();
        }
        
        public boolean isDeployed(int row) {
        	return getPerson(row).isDeployed(getCampaign());
        }

        public Object getValueAt(int row, int col) {
        	Person p;
        	DecimalFormat formatter = new DecimalFormat();
        	if(data.isEmpty()) {
        		return "";
        	} else {
        		p = data.get(row);
        	}
            if(col == COL_RANK) {
                return getCampaign().getRanks().getRank(p.getRank());
            }
            if(col == COL_NAME) {
                return p.getName();
            }
            if(col == COL_CALL) {
                return p.getCallsign();
            }
            if(col == COL_GENDER) {
                return p.getGenderName();
            }
            if(col == COL_AGE) {
                return p.getAge(getCampaign().getCalendar());
            }
            if(col == COL_TYPE) {
                return p.getRoleDesc();
            }
            if(col == COL_MECH) {
            	String toReturn = "";
            	if(p.hasSkill(SkillType.S_GUN_MECH)) {
            		toReturn += Integer.toString(p.getSkill(SkillType.S_GUN_MECH).getFinalSkillValue());
            	} else {
            		toReturn += "-";
            	}
            	toReturn += "/";
            	if(p.hasSkill(SkillType.S_PILOT_MECH)) {
            		toReturn += Integer.toString(p.getSkill(SkillType.S_PILOT_MECH).getFinalSkillValue());
            	} else {
            		toReturn += "-";
            	}
            	return toReturn;
            }
            if(col == COL_AERO) {
            	String toReturn = "";
            	if(p.hasSkill(SkillType.S_GUN_AERO)) {
            		toReturn += Integer.toString(p.getSkill(SkillType.S_GUN_AERO).getFinalSkillValue());
            	} else {
            		toReturn += "-";
            	}
            	toReturn += "/";
            	if(p.hasSkill(SkillType.S_PILOT_AERO)) {
            		toReturn += Integer.toString(p.getSkill(SkillType.S_PILOT_AERO).getFinalSkillValue());
            	} else {
            		toReturn += "-";
            	}
            	return toReturn;

            }
            if(col == COL_JET) {
            	String toReturn = "";
            	if(p.hasSkill(SkillType.S_GUN_JET)) {
            		toReturn += Integer.toString(p.getSkill(SkillType.S_GUN_JET).getFinalSkillValue());
            	} else {
            		toReturn += "-";
            	}
            	toReturn += "/";
            	if(p.hasSkill(SkillType.S_PILOT_JET)) {
            		toReturn += Integer.toString(p.getSkill(SkillType.S_PILOT_JET).getFinalSkillValue());
            	} else {
            		toReturn += "-";
            	}
            	return toReturn;

            }
            if(col == COL_SPACE) {
            	String toReturn = "";
            	if(p.hasSkill(SkillType.S_GUN_SPACE)) {
            		toReturn += Integer.toString(p.getSkill(SkillType.S_GUN_SPACE).getFinalSkillValue());
            	} else {
            		toReturn += "-";
            	}
            	toReturn += "/";
            	if(p.hasSkill(SkillType.S_PILOT_SPACE)) {
            		toReturn += Integer.toString(p.getSkill(SkillType.S_PILOT_SPACE).getFinalSkillValue());
            	} else {
            		toReturn += "-";
            	}
            	return toReturn;

            }
            if(col == COL_VEE) {
            	String toReturn = "";
            	if(p.hasSkill(SkillType.S_GUN_VEE)) {
            		toReturn += Integer.toString(p.getSkill(SkillType.S_GUN_VEE).getFinalSkillValue());
            	} else {
            		toReturn += "-";
            	}
            	toReturn += "/";
            	if(p.hasSkill(SkillType.S_PILOT_GVEE)) {
            		toReturn += Integer.toString(p.getSkill(SkillType.S_PILOT_GVEE).getFinalSkillValue());
            	} else {
            		toReturn += "-";
            	}
            	return toReturn;

            }
            if(col == COL_NVEE) {
            	String toReturn = "";
            	if(p.hasSkill(SkillType.S_GUN_VEE)) {
            		toReturn += Integer.toString(p.getSkill(SkillType.S_GUN_VEE).getFinalSkillValue());
            	} else {
            		toReturn += "-";
            	}
            	toReturn += "/";
            	if(p.hasSkill(SkillType.S_PILOT_NVEE)) {
            		toReturn += Integer.toString(p.getSkill(SkillType.S_PILOT_NVEE).getFinalSkillValue());
            	} else {
            		toReturn += "-";
            	}
            	return toReturn;

            }
            if(col == COL_VTOL) {
            	String toReturn = "";
            	if(p.hasSkill(SkillType.S_GUN_VEE)) {
            		toReturn += Integer.toString(p.getSkill(SkillType.S_GUN_VEE).getFinalSkillValue());
            	} else {
            		toReturn += "-";
            	}
            	toReturn += "/";
            	if(p.hasSkill(SkillType.S_PILOT_VTOL)) {
            		toReturn += Integer.toString(p.getSkill(SkillType.S_PILOT_VTOL).getFinalSkillValue());
            	} else {
            		toReturn += "-";
            	}
            	return toReturn;

            }
            if(col == COL_GUN_BA) {
            	if(p.hasSkill(SkillType.S_GUN_BA)) {
            		return Integer.toString(p.getSkill(SkillType.S_GUN_BA).getFinalSkillValue());
            	} else {
            		return "-";
            	}
            }
            if(col == COL_ANTI_MECH) {
            	if(p.hasSkill(SkillType.S_ANTI_MECH)) {
            		return Integer.toString(p.getSkill(SkillType.S_ANTI_MECH).getFinalSkillValue());
            	} else {
            		return "-";
            	}
            }
            if(col == COL_SMALL_ARMS) {
            	if(p.hasSkill(SkillType.S_SMALL_ARMS)) {
            		return Integer.toString(p.getSkill(SkillType.S_SMALL_ARMS).getFinalSkillValue());
            	} else {
            		return "-";
            	}
            }
            if(col == COL_ARTY) {
            	if(p.hasSkill(SkillType.S_ARTILLERY)) {
            		return Integer.toString(p.getSkill(SkillType.S_ARTILLERY).getFinalSkillValue());
            	} else {
            		return "-";
            	}
            }
            if(col == COL_TACTICS) {
            	if(p.hasSkill(SkillType.S_TACTICS)) {
            		return Integer.toString(p.getSkill(SkillType.S_TACTICS).getFinalSkillValue());
            	} else {
            		return "-";
            	}
            }
            if(col == COL_STRATEGY) {
            	if(p.hasSkill(SkillType.S_STRATEGY)) {
            		return Integer.toString(p.getSkill(SkillType.S_STRATEGY).getFinalSkillValue());
            	} else {
            		return "-";
            	}
            }
            if(col == COL_TECH_MECH) {
            	if(p.hasSkill(SkillType.S_TECH_MECH)) {
            		return Integer.toString(p.getSkill(SkillType.S_TECH_MECH).getFinalSkillValue());
            	} else {
            		return "-";
            	}
            }
            if(col == COL_TECH_AERO) {
            	if(p.hasSkill(SkillType.S_TECH_AERO)) {
            		return Integer.toString(p.getSkill(SkillType.S_TECH_AERO).getFinalSkillValue());
            	} else {
            		return "-";
            	}
            }
            if(col == COL_TECH_VEE) {
            	if(p.hasSkill(SkillType.S_TECH_MECHANIC)) {
            		return Integer.toString(p.getSkill(SkillType.S_TECH_MECHANIC).getFinalSkillValue());
            	} else {
            		return "-";
            	}
            }
            if(col == COL_TECH_BA) {
            	if(p.hasSkill(SkillType.S_TECH_BA)) {
            		return Integer.toString(p.getSkill(SkillType.S_TECH_BA).getFinalSkillValue());
            	} else {
            		return "-";
            	}
            }
            if(col == COL_MEDICAL) {
            	if(p.hasSkill(SkillType.S_DOCTOR)) {
            		return Integer.toString(p.getSkill(SkillType.S_DOCTOR).getFinalSkillValue());
            	} else {
            		return "-";
            	}
            }
            if(col == COL_ADMIN) {
            	if(p.hasSkill(SkillType.S_ADMIN)) {
            		return Integer.toString(p.getSkill(SkillType.S_ADMIN).getFinalSkillValue());
            	} else {
            		return "-";
            	}
            }
            if(col == COL_NEG) {
            	if(p.hasSkill(SkillType.S_NEG)) {
            		return Integer.toString(p.getSkill(SkillType.S_NEG).getFinalSkillValue());
            	} else {
            		return "-";
            	}
            }
            if(col == COL_SCROUNGE) {
            	if(p.hasSkill(SkillType.S_SCROUNGE)) {
            		return Integer.toString(p.getSkill(SkillType.S_SCROUNGE).getFinalSkillValue());
            	} else {
            		return "-";
            	}
            }
            if(col == COL_TOUGH) {
            	return "?";
            }
            if(col == COL_EDGE) {
            	return Integer.toString(p.getEdge());
            }
            if(col == COL_NABIL) {
            	return Integer.toString(p.countOptions(PilotOptions.LVL3_ADVANTAGES));
            }
            if(col == COL_NIMP) {
            	return Integer.toString(p.countOptions(PilotOptions.MD_ADVANTAGES));

            }
            if(col == COL_HITS) {
            	return p.getHits();
            }
            if(col == COL_SKILL) {
            	return p.getSkillSummary();
            }
            if(col == COL_ASSIGN) {
            	Unit u = getCampaign().getUnit(p.getUnitId());
            	if(null != u) {
            		String name = u.getEntity().getDisplayName();
            		if(u.getEntity() instanceof Tank) {
            			if(u.isDriver(p)) {
            				name = "[Driver] " + name;
            			} else {
            				name = "[Gunner] " + name;
            			}
            		}
            		return name;
            	}
            	return "-";
            }
            if(col == COL_XP) {
                return p.getXp();
            }
            if(col == COL_DEPLOY) {
            	Unit u = getCampaign().getUnit(p.getUnitId());
            	if(null != u && u.isDeployed()) {
            		return getCampaign().getScenario(u.getScenarioId()).getName();
            	} else {
            		return "-";
            	}
            }
            if(col == COL_FORCE) {
            	Force force = getCampaign().getForceFor(p);
            	if(null != force) {
            		return force.getName();
            	} else {
            		return "None";
            	}
            }
            if(col == COL_SALARY) {
            	return formatter.format(p.getSalary());
            }
            return "?";
        }

        public PersonnelTableModel.Renderer getRenderer() {
			return new PersonnelTableModel.Renderer();
		}

		public class Renderer extends DefaultTableCellRenderer {

			private static final long serialVersionUID = 9054581142945717303L;

			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				super.getTableCellRendererComponent(table, value, isSelected,
						hasFocus, row, column);
				setOpaque(true);
				int actualCol = table.convertColumnIndexToModel(column);
				int actualRow = table.convertRowIndexToModel(row);
				setHorizontalAlignment(getAlignment(actualCol));
				setToolTipText(getTooltip(actualRow, actualCol));
				
				setForeground(Color.BLACK);
				if (isSelected) {
                    setBackground(Color.DARK_GRAY);
                    setForeground(Color.WHITE);
                } else {
                    // tiger stripes
                	if (isDeployed(actualRow)) {
                		setBackground(Color.LIGHT_GRAY);
                	} else if((Integer)getValueAt(actualRow,COL_HITS) > 0) {
                		setBackground(Color.RED);
                    } else {
                        setBackground(Color.WHITE);
                    }
                }
				return this;
			}

		}
        
	}
	
	/**
	 * A comparator for ranks written as strings with "-" sorted to the bottom always
	 * @author Jay Lawson
	 *
	 */
	public class RankSorter implements Comparator<String> {

		@Override
		public int compare(String s0, String s1) {	
			if(s0.equals("-") && s1.equals("-")) {
				return 0;
			} else if(s0.equals("-")) {
				return 1;
			} else if(s1.equals("-")) {
				return -1;
			} else {
				//get the numbers associated with each rank string
				int r0 = getCampaign().getRanks().getRankOrder(s0);
				int r1 = getCampaign().getRanks().getRankOrder(s1);
				return ((Comparable<Integer>)r0).compareTo(r1);
			}			
		}
	}
	
	/**
	 * A comparator for skills written as strings with "-" sorted to the bottom always
	 * @author Jay Lawson
	 *
	 */
	public class SkillSorter implements Comparator<String> {

		@Override
		public int compare(String s0, String s1) {	
			if(s0.equals("-") && s1.equals("-")) {
				return 0;
			} else if(s0.equals("-")) {
				return 1;
			} else if(s1.equals("-")) {
				return -1;
			} else {
				return ((Comparable<String>)s0).compareTo(s1);
			}			
		}
	}
	
	/**
	 * A comparator for bonuses written as strings with "-" sorted to the bottom always
	 * @author Jay Lawson
	 *
	 */
	public class BonusSorter implements Comparator<String> {

		@Override
		public int compare(String s0, String s1) {	
			if(s0.equals("-") && s1.equals("-")) {
				return 0;
			} else if(s0.equals("-")) {
				return 1;
			} else if(s1.equals("-")) {
				return -1;
			} else {
				return ((Comparable<String>)s1).compareTo(s0);
			}			
		}
	}
	
	/**
	 * A comparator for skills levels (e.g. Regular, Veteran, etc)
	 * 	 * @author Jay Lawson
	 *
	 */
	public class LevelSorter implements Comparator<String> {

		@Override
		public int compare(String s0, String s1) {	
			if(s0.equals("-") && s1.equals("-")) {
				return 0;
			} else if(s0.equals("-")) {
				return -1;
			} else if(s1.equals("-")) {
				return 1;
			} else {
				//probably easiest to turn into numbers and then sort that way
				int l0 = 0;
				int l1 = 0;
				if(s0.contains("Green")) {
					l0 = 1;
				}
				if(s1.contains("Green")) {
					l1 = 1;
				}
				if(s0.contains("Regular")) {
					l0 = 2;
				}
				if(s1.contains("Regular")) {
					l1 = 2;
				}
				if(s0.contains("Veteran")) {
					l0 = 3;
				}
				if(s1.contains("Veteran")) {
					l1 = 3;
				}
				if(s0.contains("Elite")) {
					l0 = 4;
				}
				if(s1.contains("Elite")) {
					l1 = 4;
				}
				return ((Comparable<Integer>)l0).compareTo(l1);
			}			
		}
	}
	
	/**
	 * A comparator for unit status strings
	 * @author Jay Lawson
	 *
	 */
	public class UnitStatusSorter implements Comparator<String> {

		@Override
		public int compare(String s0, String s1) {	
			//probably easiest to turn into numbers and then sort that way
			int l0 = 0;
			int l1 = 0;
			if(s0.contains("Salvage")) {
				l0 = 1;
			}
			if(s1.contains("Salvage")) {
				l1 = 1;
			}
			if(s0.contains("Inoperable")) {
				l0 = 2;
			}
			if(s1.contains("Inoperable")) {
				l1 = 2;
			}
			if(s0.contains("Crippled")) {
				l0 = 3;
			}
			if(s1.contains("Crippled")) {
				l1 = 3;
			}
			if(s0.contains("Heavy")) {
				l0 = 4;
			}
			if(s1.contains("Heavy")) {
				l1 = 4;
			}
			if(s0.contains("Light")) {
				l0 = 5;
			}
			if(s1.contains("Light")) {
				l1 = 5;
			}
			if(s0.contains("Undamaged")) {
				l0 = 6;
			}
			if(s1.contains("Undamaged")) {
				l1 = 6;
			}
			return ((Comparable<Integer>)l0).compareTo(l1);		
		}
	}
	
	/**
	 * A comparator for unit weight classes
	 * @author Jay Lawson
	 *
	 */
	public class WeightClassSorter implements Comparator<String> {

		@Override
		public int compare(String s0, String s1) {	
			//lets find the weight class integer for each name
			int l0 = 0;
			int l1 = 0;
			for(int i = 0; i < EntityWeightClass.SIZE; i++) {
				if(EntityWeightClass.getClassName(i).equals(s0)) {
					l0 = i;
				}
				if(EntityWeightClass.getClassName(i).equals(s1)) {
					l1 = i;
				}			
			}
			return ((Comparable<Integer>)l0).compareTo(l1);		
		}
	}
	
	/**
	 * A comparator for numbers that have been formatted with DecimalFormat
	 * @author Jay Lawson
	 *
	 */
	public class FormattedNumberSorter implements Comparator<String> {

		@Override
		public int compare(String s0, String s1) {	
			//lets find the weight class integer for each name
			DecimalFormat format = new DecimalFormat();
			int l0 = 0;
			try {
				l0 = format.parse(s0).intValue();
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int l1 = 0;
			try {
				l1 = format.parse(s1).intValue();
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return ((Comparable<Integer>)l0).compareTo(l1);		
		}
	}
	
	/**
	 * A table model for displaying doctors
	 */
	public class DocTableModel extends ArrayTableModel {
		private static final long serialVersionUID = -6934834363013004894L;

		public DocTableModel() {
			columnNames = new String[] { "Doctors" };
			data = new ArrayList<Person>();
		}

		public Object getValueAt(int row, int col) {
			return ((Person) data.get(row)).getDocDesc(getCampaign());
		}

		public Person getDoctorAt(int row) {
			return (Person) data.get(row);
		}

		public DocTableModel.Renderer getRenderer() {
			return new DocTableModel.Renderer(getCamos(), getPortraits(), getMechTiles());
		}

		public class Renderer extends BasicInfo implements TableCellRenderer {
			public Renderer(DirectoryItems camos, DirectoryItems portraits,
					MechTileset mt) {
				super(camos, portraits, mt);
			}

			private static final long serialVersionUID = -818080358678474607L;

			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				Component c = this;
				setOpaque(true);
				setPortrait(getDoctorAt(row));
				setText(getValueAt(row, column).toString());
				//setToolTipText(getCampaign().getTargetFor(getDoctorAt(row), getDoctorAt(row)).getDesc());
				if (isSelected) {
					select();
				} else {
					unselect();
				}
				c.setBackground(new Color(220, 220, 220));
				return c;
			}

		}
	}

	/**
	 * A table model for displaying parts
	 */
	public class PartsTableModel extends ArrayTableModel {
		private static final long serialVersionUID = 534443424190075264L;

		private final static int COL_NAME    =    0;
		private final static int COL_DETAIL   =   1;
		private final static int COL_TECH_BASE  = 2;
		private final static int COL_STATUS   =   3;
		private final static int COL_COST     =   4;
		private final static int COL_QUANTITY   = 5;
		private final static int COL_TON       =  6;
		private final static int N_COL          = 7;
		
		public PartsTableModel() {
			data = new ArrayList<PartInventory>();
		}
		
		public int getRowCount() {
            return data.size();
        }

        public int getColumnCount() {
            return N_COL;
        }

        @Override
        public String getColumnName(int column) {
            switch(column) {
            	case COL_NAME:
            		return "Name";
                case COL_COST:
                    return "Value";
                case COL_QUANTITY:
                    return "#";
                case COL_TON:
                    return "Tonnage";
                case COL_STATUS:
                    return "Status";
                case COL_DETAIL:
                    return "Detail";
                case COL_TECH_BASE:
                    return "Tech Base";
                default:
                    return "?";
            }
        }

		public Object getValueAt(int row, int col) {
	        PartInventory partInventory;
	        if(data.isEmpty()) {
	        	return "";
	        } else {
	        	partInventory = (PartInventory)data.get(row);
	        }
			Part part = partInventory.getPart();
			DecimalFormat format = new DecimalFormat();
			if(col == COL_NAME) {
				return part.getName();
			}
			if(col == COL_DETAIL) {
				return part.getDetails();
			}
			if(col == COL_COST) {
				return format.format(part.getCurrentValue());
			}
			if(col == COL_QUANTITY) {
				return partInventory.getQuantity();
			}
			if(col == COL_TON) {
				return Math.round(part.getTonnage() * 100) / 100.0;
			}
			if(col == COL_STATUS) {
				return part.getStatus();
			}
			if(col == COL_TECH_BASE) {
				return part.getTechBaseName();
			}
			return "?";
		}

		public Part getPartAt(int row) {
			return ((PartInventory) data.get(row)).getPart();
		}

		public Part[] getPartstAt(int[] rows) {
			Part[] parts = new Part[rows.length];
			for (int i = 0; i < rows.length; i++) {
				int row = rows[i];
				parts[i] = ((PartInventory) data.get(row)).getPart();
			}
			return parts;
		}
		
		 public int getColumnWidth(int c) {
	            switch(c) {
	            case COL_NAME:
	        	case COL_DETAIL:
	        		return 100;
	            case COL_STATUS:
	            case COL_TECH_BASE:
	                return 40;        
	            default:
	                return 10;
	            }
	        }
	        
	        public int getAlignment(int col) {
	            switch(col) {
	            case COL_COST:
	            case COL_QUANTITY:
	            case COL_TON:
	            	return SwingConstants.RIGHT;
	            default:
	            	return SwingConstants.LEFT;
	            }
	        }

	        public String getTooltip(int row, int col) {
	        	switch(col) {
	            default:
	            	return null;
	            }
	        }
	        public PartsTableModel.Renderer getRenderer() {
				return new PartsTableModel.Renderer();
			}

			public class Renderer extends DefaultTableCellRenderer {

				private static final long serialVersionUID = 9054581142945717303L;

				public Component getTableCellRendererComponent(JTable table,
						Object value, boolean isSelected, boolean hasFocus,
						int row, int column) {
					super.getTableCellRendererComponent(table, value, isSelected,
							hasFocus, row, column);
					setOpaque(true);
					int actualCol = table.convertColumnIndexToModel(column);
					int actualRow = table.convertRowIndexToModel(row);
					setHorizontalAlignment(getAlignment(actualCol));
					setToolTipText(getTooltip(actualRow, actualCol));
					
					return this;
				}

			}
	}

	public class PartsTableMouseAdapter extends MouseInputAdapter implements
			ActionListener {

		public void actionPerformed(ActionEvent action) {
			String command = action.getActionCommand();
			int row = partsTable.getSelectedRow();
			if(row < 0) {
				return;
			}
			Part selectedPart = partsModel.getPartAt(partsTable.convertRowIndexToModel(row));
			if (command.equalsIgnoreCase("SELL")) {
				getCampaign().sellPart(selectedPart);
				refreshPartsList();
				refreshTaskList();
				refreshAcquireList();
				refreshReport();
				refreshFunds();
				refreshFinancialTransactions();
			} else if (command.equalsIgnoreCase("REMOVE")) {
				getCampaign().removePart(selectedPart);
				refreshPartsList();
				refreshTaskList();
				refreshAcquireList();
				refreshReport();
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			maybeShowPopup(e);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			maybeShowPopup(e);
		}

		private void maybeShowPopup(MouseEvent e) {
			JPopupMenu popup = new JPopupMenu();
			if (e.isPopupTrigger()) {
				int row = partsTable.rowAtPoint(e.getPoint());
				Part part = partsModel.getPartAt(row);
				JMenuItem menuItem = null;
				JMenu menu = null;
				JCheckBoxMenuItem cbMenuItem = null;
				// **lets fill the pop up menu**//
				// sell part
				if(getCampaign().getCampaignOptions().canSellParts()) {
					menuItem = new JMenuItem("Sell Part");
					menuItem.setActionCommand("SELL");
					menuItem.addActionListener(this);
					popup.add(menuItem);
				}
				// GM mode
				menu = new JMenu("GM Mode");
				// remove part
				menuItem = new JMenuItem("Remove Part");
				menuItem.setActionCommand("REMOVE");
				menuItem.addActionListener(this);
				menuItem.setEnabled(getCampaign().isGM());
				menu.add(menuItem);
				// end
				popup.addSeparator();
				popup.add(menu);
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}
	
	/**
	 * A table model for displaying scenarios
	 */
	public class ScenarioTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 534443424190075264L;

		private final static int COL_NAME       = 0;
		private final static int COL_STATUS     = 1;
        private final static int COL_ASSIGN     = 2;
        private final static int N_COL          = 3;
		
        private ArrayList<Scenario> data = new ArrayList<Scenario>();
		
		public int getRowCount() {
            return data.size();
        }

        public int getColumnCount() {
            return N_COL;
        }

        @Override
        public String getColumnName(int column) {
            switch(column) {
            	case COL_NAME:
            		return "Scenario Name";
                case COL_STATUS:
                    return "Resolution";
                case COL_ASSIGN:
                    return "# Units";
                default:
                    return "?";
            }
        }

		public Object getValueAt(int row, int col) {
			Scenario scenario = data.get(row);
			if(col == COL_NAME) {
				return scenario.getName();
			}
			if(col == COL_STATUS) {
				return scenario.getStatusName();
			}
			if(col == COL_ASSIGN) {
				return scenario.getForces(getCampaign()).getAllUnits().size();
			}
			return "?";
		}
		
		public int getColumnWidth(int c) {
            switch(c) {
            case COL_NAME:
                return 100;
            case COL_STATUS:
            	return 50;
            default:
                return 20;
            }
        }
        
        public int getAlignment(int col) {
            switch(col) {
            default:
            	return SwingConstants.LEFT;
            }
        }
		
		@Override
        public Class<?> getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            return false;
        }

        //fill table with values
        public void setData(ArrayList<Scenario> scenarios) {
            data = scenarios;
            fireTableDataChanged();
        }

		public Scenario getScenario(int row) {
			return data.get(row);
		}
	}
	
	public class ScenarioTableMouseAdapter extends MouseInputAdapter implements ActionListener {

		public void actionPerformed(ActionEvent action) {
			String command = action.getActionCommand();
			Scenario scenario = scenarioModel.getScenario(scenarioTable.getSelectedRow());
			Mission mission = getCampaign().getMission(selectedMission);
			if (command.equalsIgnoreCase("EDIT")) {
				if(null != mission && null != scenario) {
					CustomizeScenarioDialog csd = new CustomizeScenarioDialog(getFrame(), true, scenario, mission, getCampaign());
					csd.setVisible(true);
					refreshScenarioList();
				}
			} else if (command.equalsIgnoreCase("REMOVE")) {
				getCampaign().removeScenario(scenario.getId());
				refreshScenarioList();
				refreshOrganization();
				refreshPersonnelList();
				refreshUnitList();
			}
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			maybeShowPopup(e);
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			maybeShowPopup(e);
		}
		
		private void maybeShowPopup(MouseEvent e) {
			JPopupMenu popup = new JPopupMenu();
			if (e.isPopupTrigger()) {
				int row = scenarioTable.getSelectedRow();
				if(row < 0) {
					return;
				}
				Scenario scenario = scenarioModel.getScenario(row);
				JMenuItem menuItem = null;
				JMenu menu = null;
				JCheckBoxMenuItem cbMenuItem = null;
				// **lets fill the pop up menu**//
				menuItem = new JMenuItem("Edit...");
				menuItem.setActionCommand("EDIT");
				menuItem.addActionListener(this);
				popup.add(menuItem);
				// GM mode
				menu = new JMenu("GM Mode");
				// remove part
				if(scenario.isCurrent()) {
					menuItem = new JMenuItem("Remove Scenario");
					menuItem.setActionCommand("REMOVE");
					menuItem.addActionListener(this);
					menuItem.setEnabled(getCampaign().isGM());
					menu.add(menuItem);
				}
				// end
				popup.addSeparator();
				popup.add(menu);
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}
	
	/**
	 * A table model for displaying financial transactions (i.e. a ledger)
	 */
	public class FinanceTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 534443424190075264L;

		private final static int COL_DATE    =    0;
		private final static int COL_CATEGORY =   1;
        private final static int COL_DESC       = 2;
        private final static int COL_DEBIT     =  3;
        private final static int COL_CREDIT   =   4;
        private final static int COL_BALANCE  =   5;
        private final static int N_COL          = 6;
		
        private ArrayList<Transaction> data = new ArrayList<Transaction>();
		
		public int getRowCount() {
            return data.size();
        }

        public int getColumnCount() {
            return N_COL;
        }

        @Override
        public String getColumnName(int column) {
            switch(column) {
            	case COL_DATE:
            		return "Date";
                case COL_CATEGORY:
                    return "Category";
                case COL_DESC:
                    return "Notes";
                case COL_DEBIT:
                    return "Debit";
                case COL_CREDIT:
                    return "Credit";
                case COL_BALANCE:
                    return "Balance";
                default:
                    return "?";
            }
        }

		public Object getValueAt(int row, int col) {
			Transaction transaction = data.get(row);
			long amount = transaction.getAmount();
			long balance = 0;
			for(int i = 0; i <= row; i++) {
				balance += data.get(i).getAmount();
			}
			DecimalFormat formatter = new DecimalFormat();
			if(col == COL_CATEGORY) {
				return transaction.getCategoryName();
			}
			if(col == COL_DESC) {
				return transaction.getDescription();
			}
			if(col == COL_DEBIT) {
				if(amount < 0) {
					return formatter.format(-1 * amount);
				} else {
					return "";
				}	
			}
			if(col == COL_CREDIT) {
				if(amount > 0) {
					return formatter.format(amount);
				} else {
					return "";
				}
			}
			if(col == COL_BALANCE) {
				return formatter.format(balance);
			}
			if(col == COL_DATE) {
				SimpleDateFormat shortDateFormat = new SimpleDateFormat("MM/dd/yyyy");
				return shortDateFormat.format(transaction.getDate());
			}
			return "?";
		}
		
		public int getColumnWidth(int c) {
            switch(c) {
            case COL_DESC:
                return 150;
            case COL_CATEGORY:
                return 100;
            default:
                return 50;
            }
        }
        
        public int getAlignment(int col) {
            switch(col) {
            case COL_DEBIT:
            case COL_CREDIT:
            case COL_BALANCE:
            	return SwingConstants.RIGHT;
            default:
            	return SwingConstants.LEFT;
            }
        }
		
		@Override
        public Class<?> getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            return false;
        }

        //fill table with values
        public void setData(ArrayList<Transaction> transactions) {
            data = transactions;
            fireTableDataChanged();
        }

		public Transaction getTransaction(int row) {
			return data.get(row);
		}
		
		public FinanceTableModel.Renderer getRenderer() {
			return new FinanceTableModel.Renderer();
		}

		public class Renderer extends DefaultTableCellRenderer {

			private static final long serialVersionUID = 9054581142945717303L;

			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				super.getTableCellRendererComponent(table, value, isSelected,
						hasFocus, row, column);
				setOpaque(true);
				setHorizontalAlignment(getAlignment(column));
				
				setForeground(Color.BLACK);
				if (isSelected) {
                    setBackground(Color.DARK_GRAY);
                    setForeground(Color.WHITE);
                } else {
                    // tiger stripes
                	if (row % 2 == 1) {
                        setBackground(Color.LIGHT_GRAY);
                	} else {
                        setBackground(Color.WHITE);
                    }
                }
				return this;
			}

		}
	}
	
	/**
	 * A table Model for displaying information about units
	 * @author Jay lawson
	 */
	public class UnitTableModel extends AbstractTableModel {
	
		private static final long serialVersionUID = -5207167419079014157L;
		
		private final static int COL_CHASSIS =    0;
		private final static int COL_MODEL   =    1;
        private final static int COL_TYPE    =    2;
        private final static int COL_WCLASS    =  3;
        private final static int COL_TECH     =   4;
        private final static int COL_WEIGHT =     5;    
        private final static int COL_COST    =    6;
        private final static int COL_MAINTAIN  =  7;
        private final static int COL_QUALITY  =   8;
        private final static int COL_STATUS   =   9;
        private final static int COL_PILOT    =   10;
        private final static int COL_CREW     =   11;
        private final static int COL_DEPLOY   =   12;
        private final static int COL_BV        =  13;
        private final static int COL_REPAIR  =    14;
        private final static int COL_PARTS    =   15;
        private final static int COL_QUIRKS   =   16;
        private final static int N_COL =          17;
        
        private ArrayList<Unit> data = new ArrayList<Unit>();
        
        public int getRowCount() {
            return data.size();
        }

        public int getColumnCount() {
            return N_COL;
        }

        @Override
        public String getColumnName(int column) {
            switch(column) {
            	case COL_CHASSIS:
            		return "Chassis";
                case COL_MODEL:
                    return "Model";
                case COL_TYPE:
                    return "Type";
                case COL_WEIGHT:
                    return "Weight";
                case COL_WCLASS:
                    return "Class";
                case COL_COST:
                    return "Value";
                case COL_TECH:
                    return "Tech";
                case COL_QUALITY:
                    return "Quality";
                case COL_STATUS:
                    return "Status";
                case COL_PILOT:
                    return "Assigned to";
                case COL_CREW:
                    return "Crew";
                case COL_BV:
                    return "BV";
                case COL_REPAIR:
                    return "# Repairs";
                case COL_PARTS:
                    return "# Parts";
                case COL_QUIRKS:
                    return "Quirks";
                case COL_DEPLOY:
                    return "Deployed";
                case COL_MAINTAIN:
                    return "Maintenance Costs";
                default:
                    return "?";
            }
        }
        
        public int getColumnWidth(int c) {
            switch(c) {
        	case COL_CHASSIS:
        		return 100;
            case COL_MODEL:
            case COL_WCLASS:
            case COL_TYPE:
            	return 50;
            case COL_COST:
            case COL_STATUS:
                return 80;        
            case COL_PILOT:          
            case COL_TECH:
            	return 150;
            default:
                return 20;
            }
        }
        
        public int getAlignment(int col) {
            switch(col) {
            case COL_QUALITY:
            case COL_QUIRKS:
            case COL_DEPLOY:
            case COL_CREW:
            	return SwingConstants.CENTER;
            case COL_WEIGHT:
            case COL_COST:
            case COL_MAINTAIN:
            case COL_REPAIR:
            case COL_PARTS:
            case COL_BV:
            	return SwingConstants.RIGHT;
            default:
            	return SwingConstants.LEFT;
            }
        }

        public String getTooltip(int row, int col) {
        	Unit u = data.get(row);
        	switch(col) {
        	//case COL_REPAIR:
        		//return getCampaign().getTaskListFor(u);
        	case COL_QUIRKS:
        		return u.getQuirksList();
            default:		
            	return null;
            }
        }
        
        @Override
        public Class<?> getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            return false;
        }

        public Unit getUnit(int i) {
            return data.get(i);
        }

        //fill table with values
        public void setData(ArrayList<Unit> units) {
            data = units;
            fireTableDataChanged();
        }

        public Object getValueAt(int row, int col) {
        	Unit u;
        	if(data.isEmpty()) {
        		return "";
        	} else {
        		u = data.get(row);
        	}
            Entity e = u.getEntity();
            //PilotPerson pp = u.getPilot();
            DecimalFormat format = new DecimalFormat();
            if(null == e) {
            	return "?";
            }
            if(col == COL_CHASSIS) {
                return e.getChassis();
            }  
            if(col == COL_MODEL) {
                return e.getModel();
            }
            if(col == COL_TYPE) {
            	return UnitType.getTypeDisplayableName(UnitType.determineUnitTypeCode(e));
            }
            if(col == COL_WEIGHT) {
                return e.getWeight();
            }
            if(col == COL_WCLASS) {
                return e.getWeightClassName();
            }
            if(col == COL_COST) {
                return format.format(u.getSellValue());
            }
            if(col == COL_MAINTAIN) {
                return u.getMaintenanceCost();
            }
            if(col == COL_TECH) {
                return TechConstants.getLevelDisplayableName(e.getTechLevel());
            }
            if(col == COL_QUALITY) {
                return u.getQualityName();
            }
            if(col == COL_STATUS) {
                return u.getStatus();
            }
            if(col == COL_PILOT) {
            	if(null == u.getCommander()) {
            		return "-";
            	} else {
            		return u.getCommander().getFullTitle();
            	}
            }
            if(col == COL_BV) {
            	if(null == u.getEntity().getCrew()) {
            		return e.calculateBattleValue(true, true);
            	} else {
            		return e.calculateBattleValue(true, false);
            	}
            }
            if(col == COL_REPAIR) {
                return u.getPartsNeedingFixing().size();
            }
            if(col == COL_PARTS) {
                return u.getPartsNeeded().size();
            }
            if(col == COL_QUIRKS) {
            	return e.countQuirks();
            }
            if(col == COL_DEPLOY) {
            	if(u.isDeployed()) {
            		return "Y";
            	} else {
            		return "N";
            	}
            }
            if(col == COL_CREW) {
            	return u.getActiveCrew().size() + "/" + u.getFullCrewSize();
            }
            return "?";
        }

        public UnitTableModel.Renderer getRenderer() {
			return new UnitTableModel.Renderer();
		}

		public class Renderer extends DefaultTableCellRenderer {

			private static final long serialVersionUID = 9054581142945717303L;

			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				super.getTableCellRendererComponent(table, value, isSelected,
						hasFocus, row, column);
				setOpaque(true);
				int actualCol = table.convertColumnIndexToModel(column);
				int actualRow = table.convertRowIndexToModel(row);
				setHorizontalAlignment(getAlignment(actualCol));
				setToolTipText(getTooltip(actualRow, actualCol));
				Unit u = getUnit(actualRow);
				
				setForeground(Color.BLACK);
				if (isSelected) {
                    setBackground(Color.DARK_GRAY);
                    setForeground(Color.WHITE);
                } else {
                	if (u.isDeployed()) {
                		setBackground(Color.LIGHT_GRAY);
                	}
                	if(u.isRefitting()) {
                		setBackground(Color.CYAN);
                	}
                	else if (null != u && !u.isRepairable()) {
    					setBackground(new Color(190, 150, 55));
    				} else if ((null != u) && !u.isFunctional()) {
    					setBackground(new Color(205, 92, 92));
    				} else if ((null != u)
    						&& (u.getPartsNeedingFixing().size() > 0)) {
    					setBackground(new Color(238, 238, 0));
                    } else {
                        setBackground(Color.WHITE);
                    }
                }
				return this;
			}

		}
        
	}
	
	public class UnitTableMouseAdapter extends MouseInputAdapter implements
		ActionListener {
	
		public void actionPerformed(ActionEvent action) {
			String command = action.getActionCommand();
			Unit selectedUnit = unitModel.getUnit(unitTable.convertRowIndexToModel(unitTable.getSelectedRow()));
			int[] rows = unitTable.getSelectedRows();
			Unit[] units = new Unit[rows.length];
			for(int i=0; i<rows.length; i++) {
				units[i] = unitModel.getUnit(unitTable.convertRowIndexToModel(rows[i]));
			}
			if (command.equalsIgnoreCase("REMOVE_PILOT")) {
				for (Unit unit : units) {
					for(Person p : unit.getCrew()) {
						unit.remove(p);
					}
				}
				refreshServicedUnitList();
				refreshUnitList();
				refreshOrganization();
			} else if (command.contains("CHANGE_PILOT")) {
				String sel = command.split(":")[1];
				int selected = Integer.parseInt(sel);
				Person p = getCampaign().getPerson(selected);
				//if (null != p && p instanceof PilotPerson) {
					//campaign.changePilot(selectedUnit, (PilotPerson)p);
				//}
				refreshServicedUnitList();
				refreshUnitList();
				refreshPersonnelList();
				refreshOrganization();
			} else if (command.contains("QUIRK")) {
				String sel = command.split(":")[1];
					selectedUnit.acquireQuirk(sel, true);
					refreshServicedUnitList();
					refreshUnitList();
					refreshTechsList();
					refreshReport();
			} else if (command.equalsIgnoreCase("SELL")) {
				for (Unit unit : units) {
					if (!unit.isDeployed()) {
						int sellValue = unit.getSellValue();
						NumberFormat numberFormat = NumberFormat
								.getIntegerInstance();
						String text = numberFormat.format(sellValue) + " "
								+ (sellValue != 0 ? "CBills" : "CBill");
						if (0 == JOptionPane.showConfirmDialog(null,
								"Do you really want to sell "
										+ unit.getEntity().getDisplayName()
										+ " for " + text, "Sell Unit?",
								JOptionPane.YES_NO_OPTION)) {
							getCampaign().sellUnit(unit.getId());
						}
					}
				}
				refreshServicedUnitList();
				refreshUnitList();
				refreshPersonnelList();
				refreshOrganization();
				refreshReport();
				refreshFunds();
				refreshFinancialTransactions();
			} else if (command.equalsIgnoreCase("LOSS")) {
				for (Unit unit : units) {
					if (0 == JOptionPane.showConfirmDialog(null,
							"Do you really want to consider "
									+ unit.getEntity().getDisplayName()
									+ " a combat loss?", "Remove Unit?",
							JOptionPane.YES_NO_OPTION)) {
						getCampaign().removeUnit(unit.getId());
					}
				}
				refreshServicedUnitList();
				refreshUnitList();
				refreshPersonnelList();
				refreshOrganization();
				refreshReport();
			} else if (command.contains("SWAP_AMMO")) {
				String sel = command.split(":")[1];
				int selAmmoId = Integer.parseInt(sel);
				Part part = getCampaign().getPart(selAmmoId);
				if (null == part || !(part instanceof AmmoBin)) {
					return;
				}
				AmmoBin ammo = (AmmoBin)part;
				sel = command.split(":")[2];
				long munition = Long.parseLong(sel);
				ammo.changeMunition(munition);
				refreshTaskList();
				refreshAcquireList();
				refreshServicedUnitList();
				refreshUnitList();
			} else if (command.contains("CHANGE_SITE")) {
				for (Unit unit : units) {
					if (!unit.isDeployed()) {
						String sel = command.split(":")[1];
						int selected = Integer.parseInt(sel);
						if ((selected > -1) && (selected < Unit.SITE_N)) {
							unit.setSite(selected);
						}
					}
				}
				refreshServicedUnitList();
				refreshUnitList();
				refreshTaskList();
				refreshAcquireList();
			} else if (command.equalsIgnoreCase("SALVAGE")) {
				for (Unit unit : units) {
					if (!unit.isDeployed()) {
						unit.setSalvage(true);
						unit.runDiagnostic();
					}
				}
				refreshServicedUnitList();
				refreshUnitList();
			} else if (command.equalsIgnoreCase("REPAIR")) {
				for (Unit unit : units) {
					if (!unit.isDeployed() && unit.isRepairable()) {
						unit.setSalvage(false);
						unit.runDiagnostic();
					}
				}
				refreshServicedUnitList();
				refreshUnitList();
			} else if (command.equalsIgnoreCase("TAG_CUSTOM")) {		    
			    String sCustomsDir = "data/mechfiles/customs/";
			    File customsDir = new File(sCustomsDir);
			    if(!customsDir.exists()) {
			    	customsDir.mkdir();
			    }
				for (Unit unit : units) {
					String fileName = unit.getEntity().getChassis() + " " + unit.getEntity().getModel();
					try {
				        if (unit.getEntity() instanceof Mech) {
				            FileOutputStream out = new FileOutputStream(sCustomsDir + File.separator + fileName + ".mtf");
				            PrintStream p = new PrintStream(out);
				            p.println(((Mech) unit.getEntity()).getMtf());
				            p.close();
				            out.close();
				        } else {
				            BLKFile.encode(sCustomsDir + File.separator + fileName + ".blk", unit.getEntity());
				        }
				    } catch (Exception ex) {
				        ex.printStackTrace();
				    }
					getCampaign().addCustom(unit.getEntity().getChassis() + " " + unit.getEntity().getModel());
				}
			    MechSummaryCache.getInstance().loadMechData();
			}  else if (command.equalsIgnoreCase("REMOVE")) {
				for (Unit unit : units) {
					if (!unit.isDeployed()) {
						if (0 == JOptionPane.showConfirmDialog(null,
								"Do you really want to remove "
										+ unit.getEntity().getDisplayName()
										+ "?", "Remove Unit?",
								JOptionPane.YES_NO_OPTION)) {
							getCampaign().removeUnit(unit.getId());
						}
					}
				}
				refreshServicedUnitList();
				refreshUnitList();
				refreshPersonnelList();
				refreshOrganization();
				refreshReport();
			} else if (command.equalsIgnoreCase("UNDEPLOY")) {
				for (Unit unit : units) {
					if (unit.isDeployed()) {
						//if (null != unit.getPilot()) {
							//unit.getPilot().undeploy(getCampaign());
						//}
					}
				}
				refreshServicedUnitList();
				refreshUnitList();
				refreshPatientList();
				refreshPersonnelList();
				refreshScenarioList();
			} else if (command.contains("CUSTOMIZE")
					&& !command.contains("CANCEL")) {
				if (selectedUnit.getEntity() instanceof Mech) {
					panMekLab.loadUnit(selectedUnit);
					tabMain.setSelectedIndex(8);
				}
			} else if (command.contains("CANCEL_CUSTOMIZE")) {
				if(selectedUnit.isRefitting()) {
					selectedUnit.getRefit().cancel();
				}
				refreshServicedUnitList();
				refreshUnitList();
			} else if(command.contains("REFIT")) {
				String model = command.split(":")[1];
				MechSummary summary = MechSummaryCache.getInstance().getMech(selectedUnit.getEntity().getChassis() + " " + model);
				try {
                    Entity refitEn = new MechFileParser(summary.getSourceFile(), summary.getEntryName()).getEntity();
					if(null != refitEn) {
						refitUnit(new Refit(selectedUnit, refitEn, false), false);						
					}
				} catch (EntityLoadingException ex) {
					Logger.getLogger(CampaignGUI.class.getName())
							.log(Level.SEVERE, null, ex);
				}	
			}
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			maybeShowPopup(e);
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			maybeShowPopup(e);
		}
		
		private void maybeShowPopup(MouseEvent e) {
			JPopupMenu popup = new JPopupMenu();
			if (e.isPopupTrigger()) {
				if(unitTable.getSelectedRowCount() == 0) {
	            	return;
	            }
	            int[] rows = unitTable.getSelectedRows();
	            int row = unitTable.getSelectedRow();
	            boolean oneSelected = unitTable.getSelectedRowCount() == 1;
				Unit unit = unitModel.getUnit(unitTable.convertRowIndexToModel(row));
				JMenuItem menuItem = null;
				JMenu menu = null;
				JCheckBoxMenuItem cbMenuItem = null;
				// **lets fill the pop up menu**//
				// change the location
				menu = new JMenu("Change site");
				int i = 0;
				for (i = 0; i < Unit.SITE_N; i++) {
					cbMenuItem = new JCheckBoxMenuItem(Unit.getSiteName(i));
					if (unit.getSite() == i) {
						cbMenuItem.setSelected(true);
					} else {
						cbMenuItem.setActionCommand("CHANGE_SITE:" + i);
						cbMenuItem.addActionListener(this);
					}
					menu.add(cbMenuItem);
				}
				menu.setEnabled(!unit.isDeployed() && !unit.isRefitting());
				popup.add(menu);
				// swap ammo
				if(oneSelected) {			
					menu = new JMenu("Swap ammo");
					JMenu ammoMenu = null;
					for (AmmoBin ammo : unit.getWorkingAmmoBins()) {
						ammoMenu = new JMenu(ammo.getType().getDesc());
						AmmoType curType = (AmmoType) ammo.getType();
						for (AmmoType atype : Utilities.getMunitionsFor(unit.getEntity(), curType)) {
							cbMenuItem = new JCheckBoxMenuItem(atype.getDesc());
							if (atype.equals(curType)) {
								cbMenuItem.setSelected(true);
							} else {
								cbMenuItem.setActionCommand("SWAP_AMMO:"
										+ ammo.getId() + ":"
										+ atype.getMunitionType());
								cbMenuItem.addActionListener(this);
							}
							ammoMenu.add(cbMenuItem);
						}
						if(ammoMenu.getItemCount() > 20) {
		                	MenuScroller.setScrollerFor(ammoMenu, 20);
		                }
						menu.add(ammoMenu);
					}
					menu.setEnabled(!unit.isDeployed() && !unit.isRefitting());
					if(menu.getItemCount() > 20) {
	                	MenuScroller.setScrollerFor(menu, 20);
	                }
					popup.add(menu);
				}
				// Salvage / Repair
				if(oneSelected) {
					menu = new JMenu("Repair Status");
					menu.setEnabled(!unit.isDeployed() && !unit.isRefitting());
					cbMenuItem = new JCheckBoxMenuItem("Repair");
					if(!unit.isSalvage()) {
						cbMenuItem.setSelected(true);
					}
					cbMenuItem.setActionCommand("REPAIR");
					cbMenuItem.addActionListener(this);
					cbMenuItem.setEnabled(!unit.isDeployed()  && !unit.isRefitting()
							&& unit.isRepairable());
					menu.add(cbMenuItem);
					cbMenuItem = new JCheckBoxMenuItem("Salvage");
					if(unit.isSalvage()) {
						cbMenuItem.setSelected(true);
					}
					cbMenuItem.setActionCommand("SALVAGE");
					cbMenuItem.addActionListener(this);
					cbMenuItem.setEnabled(!unit.isDeployed() && !unit.isRefitting());
					menu.add(cbMenuItem);
					popup.add(menu);
				}
				// Customize
				if(oneSelected && unit.getEntity() instanceof Mech) {
					menu = new JMenu("Customize");
			
					JMenu refitMenu = new JMenu("Refit Kit");
					for(String model : Utilities.getAllVariants(unit.getEntity())) {
						MechSummary summary = MechSummaryCache.getInstance().getMech(unit.getEntity().getChassis() + " " + model);
						if(null == summary) {
							continue;
						}
						try {
                            Entity refitEn = new MechFileParser(summary.getSourceFile(), summary.getEntryName()).getEntity();
							if(null != refitEn) {
								Refit r = new Refit(unit, refitEn, false);
								menuItem = new JMenuItem(model + " (" + r.getRefitClassName() + "/" + r.getTimeLeft() + " minutes/" + Utilities.getCurrencyString(r.getCost()) + ")");
								menuItem.setActionCommand("REFIT:" + model);
								menuItem.addActionListener(this);
								menuItem.setEnabled(!unit.isRefitting());
								refitMenu.add(menuItem);
							}
						} catch (EntityLoadingException ex) {
							Logger.getLogger(CampaignGUI.class.getName())
									.log(Level.SEVERE, null, ex);
						}		
					}
					menu.add(refitMenu);
			
					menuItem = new JMenuItem("Customize in Mek Lab");
					menuItem.setActionCommand("CUSTOMIZE");
					menuItem.addActionListener(this);
					menuItem.setEnabled(!unit.isRefitting()
							&& (unit.getEntity() instanceof megamek.common.Mech));
					menu.add(menuItem);		
					if (unit.isRefitting()) {
						menuItem = new JMenuItem("Cancel Customization");
						menuItem.setActionCommand("CANCEL_CUSTOMIZE");
						menuItem.addActionListener(this);
						menuItem.setEnabled(true);
						menu.add(menuItem);
					}
					menu.setEnabled(!unit.isDeployed() && unit.isRepairable());
					popup.add(menu);
					
				}
				if(oneSelected && !getCampaign().isCustom(unit)) {
					menuItem = new JMenuItem("Tag as a custom unit");
					menuItem.setActionCommand("TAG_CUSTOM");
					menuItem.addActionListener(this);
					menuItem.setEnabled(true);
					popup.add(menuItem);
				}
				if(oneSelected && getCampaign().getCampaignOptions().useQuirks()) {
					menu = new JMenu("Add Quirk");			
					for (Enumeration<IOption> q = unit.getEntity().getQuirks().getOptions(); q.hasMoreElements();) {
			        	IOption quirk = q.nextElement();
			        	if(!quirk.booleanValue()) {
			        		menuItem = new JMenuItem(quirk.getDisplayableName());
			        		menuItem.setActionCommand("QUIRK:" + quirk.getName());
			        		menuItem.addActionListener(this);
			        		menuItem.setEnabled(true);
			        		menu.add(menuItem);
			        	}
					}
					if(menu.getItemCount() > 20) {
	                	MenuScroller.setScrollerFor(menu, 20);
	                }
					popup.add(menu);
				}
				if(oneSelected) {
					// remove pilot
					popup.addSeparator();
					menuItem = new JMenuItem("Remove all personnel");
					menuItem.setActionCommand("REMOVE_PILOT");
					menuItem.addActionListener(this);
					menuItem.setEnabled(!unit.isUnmanned() && !unit.isDeployed());
					popup.add(menuItem);
				}
				// sell unit
				if(getCampaign().getCampaignOptions().canSellUnits()) {
					popup.addSeparator();
					menuItem = new JMenuItem("Sell Unit");
					menuItem.setActionCommand("SELL");
					menuItem.addActionListener(this);
					menuItem.setEnabled(!unit.isDeployed());
					popup.add(menuItem);
				}
				// GM mode
				menu = new JMenu("GM Mode");
				menuItem = new JMenuItem("Remove Unit");
				menuItem.setActionCommand("REMOVE");
				menuItem.addActionListener(this);
				menuItem.setEnabled(getCampaign().isGM());
				menu.add(menuItem);
				menuItem = new JMenuItem("Undeploy Unit");
				menuItem.setActionCommand("UNDEPLOY");
				menuItem.addActionListener(this);
				menuItem.setEnabled(getCampaign().isGM() && unit.isDeployed());
				menu.add(menuItem);
				popup.addSeparator();
				popup.add(menu);
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}
	
	public class BasicInfo extends javax.swing.JPanel {
		private static final long serialVersionUID = -2890605770494694319L;
		DirectoryItems camos;
		DirectoryItems portraits;
		MechTileset mt;
		
	    public BasicInfo(DirectoryItems camos, DirectoryItems portraits, MechTileset mt) {
	    	this.camos = camos;
	    	this.portraits = portraits;
	    	this.mt = mt;
	        initComponents();
	    }

	    private void initComponents() {
	        lblImage = new javax.swing.JLabel();
	        setName("Form"); // NOI18N
	        setLayout(new java.awt.GridLayout(1, 0));        
	        lblImage.setText(""); // NOI18N
	        lblImage.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
	        lblImage.setName("lblImage"); // NOI18N
	        add(lblImage);
	    }// </editor-fold>//GEN-END:initComponents

	    public void setText(String text) {
	        lblImage.setText(text);
	    }
	    
	    public void setImage(Image img) {
	        lblImage.setIcon(new ImageIcon(img));
	    }

	    public void select() {
	        lblImage.setBorder(new javax.swing.border.LineBorder(Color.BLACK, 5, true));
	    }
	    
	    public void unselect() {
	        lblImage.setBorder(javax.swing.BorderFactory.createEtchedBorder());
	    }
	    
	    /**
	     * sets the image as a portrait for the given person.
	     */
	    public void setPortrait(Person person) {

	        String category = person.getPortraitCategory();
	        String file = person.getPortraitFileName();

	        if(Pilot.ROOT_PORTRAIT.equals(category)) {
	            category = "";
	        }

	        // Return a null if the player has selected no portrait file.
	        if ((null == category) || (null == file) || Pilot.PORTRAIT_NONE.equals(file)) {
	        	file = "default.gif";
	        }

	        // Try to get the player's portrait file.
	        Image portrait = null;
	        try {
	            portrait = (Image) portraits.getItem(category, file);
	            //make sure no images are longer than 72 pixels
	            if(null != portrait) {
	                portrait = portrait.getScaledInstance(-1, 65, Image.SCALE_DEFAULT);
	                this.setImage(portrait);
	            }
	        } catch (Exception err) {
	            err.printStackTrace();
	        }
	    }
	    
	    public void setUnit(Unit u) {
	        Image unit = getImageFor(u, lblImage);     
	        setImage(unit);
	    }
	    
	    public Image getImageFor(Unit u, Component c) {
	        
	    	if(null == mt) {
	    		return null;
	    	}
	        Image base = mt.imageFor(u.getEntity(), c, -1);
	        int tint = PlayerColors.getColorRGB(u.campaign.getColorIndex());
	        EntityImage entityImage = new EntityImage(base, tint, getCamo(u.campaign), c);
	        return entityImage.loadPreviewImage();
	    }
	    
	    public Image getCamo(Campaign c) {

	        // Return a null if the campaign has selected no camo file.
	        if (null == c.getCamoCategory()
	                || Player.NO_CAMO.equals(c.getCamoCategory())) {
	            return null;
	        }

	        // Try to get the player's camo file.
	        Image camo = null;
	        try {

	            // Translate the root camo directory name.
	            String category = c.getCamoCategory();
	            if (Player.ROOT_CAMO.equals(category))
	                category = ""; //$NON-NLS-1$
	            camo = (Image) camos.getItem(category, c.getCamoFileName());

	        } catch (Exception err) {
	            err.printStackTrace();
	        }
	        return camo;
	    }
	    
	    // Variables declaration - do not modify//GEN-BEGIN:variables
	    private javax.swing.JLabel lblImage;
	    // End of variables declaration//GEN-END:variables

	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JTable DocTable;
	private javax.swing.JTable partsTable;
	private javax.swing.JTable patientTable;
	private javax.swing.JTable TaskTable;
	private javax.swing.JTable AcquisitionTable;
	private javax.swing.JTable TechTable;
	private javax.swing.JTable servicedUnitTable;
	private javax.swing.JTable unitTable;
	private javax.swing.JTable personnelTable;
	private javax.swing.JTable scenarioTable;
	private javax.swing.JTable financeTable;
	private javax.swing.JMenuItem addFunds;
	private javax.swing.JButton btnAdvanceDay;
	private javax.swing.JButton btnAssignDoc;
	private javax.swing.JButton btnDoTask;
	private javax.swing.JToggleButton btnGMMode;
	private javax.swing.JToggleButton btnOvertime;
	private javax.swing.JScrollPane jScrollPane6;
	private javax.swing.JScrollPane scrollPartsTable;
	private javax.swing.JLabel lblTarget;
	private javax.swing.JLabel lblTargetNum;
	private javax.swing.JPanel mainPanel;
	private javax.swing.JMenuBar menuBar;
	private javax.swing.JMenu menuHire;
	private javax.swing.JMenuItem menuLoad;
	private javax.swing.JMenu menuManage;
	private javax.swing.JMenu menuMarket;
	private javax.swing.JMenuItem menuOptions;
	private javax.swing.JMenuItem menuOptionsMM;
	private javax.swing.JMenu menuThemes;
	private javax.swing.JMenuItem menuSave;
	private javax.swing.JMenuItem miHireAstechs;
	private javax.swing.JMenuItem miFireAstechs;
	private javax.swing.JMenuItem miFireAllAstechs;
	private javax.swing.JMenuItem miFullStrengthAstechs;
	private javax.swing.JMenu menuAstechPool;
	private javax.swing.JMenuItem miHireMedics;
	private javax.swing.JMenuItem miFireMedics;
	private javax.swing.JMenuItem miFireAllMedics;
	private javax.swing.JMenuItem miFullStrengthMedics;
	private javax.swing.JMenu menuMedicPool;
	private javax.swing.JMenuItem miLoadForces;
	private javax.swing.JMenuItem miPurchaseUnit;
	private javax.swing.JMenuItem miBuyParts;
	private javax.swing.JMenu menuCommunity;
	private javax.swing.JMenuItem miChat;
	private javax.swing.JPanel panFinances;
	private javax.swing.JPanel panHangar;
	private javax.swing.JPanel panOrganization;
	private javax.swing.JPanel panRepairBay;
	private javax.swing.JPanel panInfirmary;
	private javax.swing.JPanel panPersonnel;
	private javax.swing.JPanel panBriefing;
	private javax.swing.JPanel panelScenario;
	private javax.swing.JPanel panSupplies;
	private javax.swing.JPanel panelDoTask;
	private javax.swing.JPanel panelMapView;
	private javax.swing.JScrollPane scrollDocTable;
	private javax.swing.JScrollPane scrollPatientTable;
	private javax.swing.JScrollPane scrollTaskTable;
	private javax.swing.JScrollPane scrollAcquisitionTable;
	private javax.swing.JScrollPane scrollTechTable;
	private javax.swing.JScrollPane scrollServicedUnitTable;
	private javax.swing.JScrollPane scrollPersonnelTable;
	private javax.swing.JScrollPane scrollScenarioTable;
	private javax.swing.JScrollPane scrollUnitTable;
	private javax.swing.JScrollPane scrollFinanceTable;
	private javax.swing.JPanel statusPanel;
	private javax.swing.JTabbedPane tabMain;
	private javax.swing.JTabbedPane tabTasks;
	private javax.swing.JTextArea textTarget;
	private javax.swing.JLabel astechPoolLabel;
	private javax.swing.JTextPane txtPaneReport;
	private javax.swing.JScrollPane txtPaneReportScrollPane;
	private javax.swing.JComboBox choicePerson;
	private javax.swing.JLabel lblPersonChoice;
	private javax.swing.JComboBox choicePersonView;
	private javax.swing.JLabel lblPersonView;
	private javax.swing.JScrollPane scrollPersonnelView;
    private javax.swing.JSplitPane splitPersonnel;
    private javax.swing.JComboBox choiceUnit;
	private javax.swing.JLabel lblUnitChoice;
	private javax.swing.JComboBox choiceUnitView;
	private javax.swing.JLabel lblUnitView;
	private javax.swing.JScrollPane scrollUnitView;
    private javax.swing.JSplitPane splitUnit;
	private javax.swing.JScrollPane scrollOrgTree;
	private javax.swing.JTree orgTree;
    private javax.swing.JSplitPane splitOrg;
	private javax.swing.JScrollPane scrollForceView;
	InterstellarMapPanel panMap;
    private javax.swing.JSplitPane splitMap;
	private javax.swing.JScrollPane scrollPlanetView;
    private javax.swing.JComboBox choiceMission;
	private javax.swing.JScrollPane scrollMissionView;
	private javax.swing.JScrollPane scrollScenarioView;
	private javax.swing.JPanel panelMissionButtons;
	private javax.swing.JPanel panelScenarioButtons;
	private javax.swing.JButton btnAddScenario;
	private javax.swing.JButton btnAddMission;
	private javax.swing.JButton btnEditMission;
	private javax.swing.JButton btnCompleteMission;
	private javax.swing.JButton btnStartGame;
	private javax.swing.JButton btnLoadGame;
	private javax.swing.JButton btnPrintRS;
	private javax.swing.JButton btnGetMul;
	private javax.swing.JButton btnClearAssignedUnits;
	private javax.swing.JButton btnResolveScenario;
    private javax.swing.JSplitPane splitBrief;
    private javax.swing.JSplitPane splitMission;
    private javax.swing.JLabel lblMission;
    private javax.swing.JComboBox choiceParts;
	private javax.swing.JLabel lblPartsChoice;
    private javax.swing.JLabel lblFindPlanet;
	private JSuggestField suggestPlanet;
	private javax.swing.JButton btnCalculateJumpPath;
	private javax.swing.JButton btnBeginTransit;
	private MekLabPanel panMekLab;
	private javax.swing.JScrollPane scrollMekLab;
    private javax.swing.JLabel lblLocation;
	// End of variables declaration//GEN-END:variables

	private javax.swing.JLabel lblFunds;
	private javax.swing.JLabel lblTempAstechs;
	private javax.swing.JLabel lblTempMedics;

	private JDialog aboutBox;
}
