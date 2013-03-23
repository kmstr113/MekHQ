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
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.UUID;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DropMode;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.ScrollPaneConstants;
import javax.swing.SortOrder;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import megamek.client.ui.Messages;
import megamek.client.ui.swing.MechTileset;
import megamek.client.ui.swing.util.PlayerColors;
import megamek.common.Aero;
import megamek.common.AmmoType;
import megamek.common.BattleArmor;
import megamek.common.Crew;
import megamek.common.Dropship;
import megamek.common.Entity;
import megamek.common.EntityListFile;
import megamek.common.EntityWeightClass;
import megamek.common.Infantry;
import megamek.common.Jumpship;
import megamek.common.Mech;
import megamek.common.MechSummaryCache;
import megamek.common.MechView;
import megamek.common.MiscType;
import megamek.common.Mounted;
import megamek.common.Player;
import megamek.common.SmallCraft;
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
import mekhq.campaign.LogEntry;
import mekhq.MekHQ;
import mekhq.Utilities;
import mekhq.campaign.Campaign;
import mekhq.campaign.JumpPath;
import mekhq.campaign.Kill;
import mekhq.campaign.Planet;
import mekhq.campaign.ResolveScenarioTracker;
import mekhq.campaign.Unit;
import mekhq.campaign.finances.Loan;
import mekhq.campaign.finances.Transaction;
import mekhq.campaign.force.Force;
import mekhq.campaign.mission.Contract;
import mekhq.campaign.mission.Mission;
import mekhq.campaign.mission.Scenario;
import mekhq.campaign.parts.AmmoStorage;
import mekhq.campaign.parts.Armor;
import mekhq.campaign.parts.EnginePart;
import mekhq.campaign.parts.MekActuator;
import mekhq.campaign.parts.MekGyro;
import mekhq.campaign.parts.MekLifeSupport;
import mekhq.campaign.parts.MekLocation;
import mekhq.campaign.parts.MekSensor;
import mekhq.campaign.parts.Part;
import mekhq.campaign.parts.Refit;
import mekhq.campaign.parts.TankLocation;
import mekhq.campaign.parts.equipment.AmmoBin;
import mekhq.campaign.parts.equipment.EquipmentPart;
import mekhq.campaign.personnel.Person;
import mekhq.campaign.personnel.Skill;
import mekhq.campaign.personnel.SkillType;
import mekhq.campaign.work.IAcquisitionWork;
import mekhq.campaign.work.Modes;
import mekhq.gui.dialog.AddFundsDialog;
import mekhq.gui.dialog.BombsDialog;
import mekhq.gui.dialog.CamoChoiceDialog;
import mekhq.gui.dialog.CampaignOptionsDialog;
import mekhq.gui.dialog.ChooseMulFilesDialog;
import mekhq.gui.dialog.ChooseRefitDialog;
import mekhq.gui.dialog.CompleteMissionDialog;
import mekhq.gui.dialog.CustomizeMissionDialog;
import mekhq.gui.dialog.CustomizePersonDialog;
import mekhq.gui.dialog.CustomizeScenarioDialog;
import mekhq.gui.dialog.DataLoadingDialog;
import mekhq.gui.dialog.DragoonsRatingDialog;
import mekhq.gui.dialog.EditKillLogDialog;
import mekhq.gui.dialog.EditLogEntryDialog;
import mekhq.gui.dialog.EditPersonnelLogDialog;
import mekhq.gui.dialog.EditTransactionDialog;
import mekhq.gui.dialog.GameOptionsDialog;
import mekhq.gui.dialog.HireBulkPersonnelDialog;
import mekhq.gui.dialog.KillDialog;
import mekhq.gui.dialog.MekHQAboutBox;
import mekhq.gui.dialog.MissionTypeDialog;
import mekhq.gui.dialog.NewLoanDialog;
import mekhq.gui.dialog.NewRecruitDialog;
import mekhq.gui.dialog.PartsStoreDialog;
import mekhq.gui.dialog.PopupValueChoiceDialog;
import mekhq.gui.dialog.PortraitChoiceDialog;
import mekhq.gui.dialog.QuirksDialog;
import mekhq.gui.dialog.RefitNameDialog;
import mekhq.gui.dialog.ResolveScenarioWizardDialog;
import mekhq.gui.dialog.ShoppingListDialog;
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
import chat.ChatClient;

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
    private static final int PG_SOLDIER = 8;
    private static final int PG_VESSEL =  9;
    private static final int PG_TECH =    10;
    private static final int PG_DOC =     11;
    private static final int PG_ADMIN =   12;
    private static final int PG_RETIRE =  13;
    private static final int PG_MIA =     14;
    private static final int PG_KIA =     15;
    private static final int PG_NUM =     16;

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

    private ResourceBundle resourceMap = ResourceBundle.getBundle("mekhq.resources.CampaignGUI");
        
    private TaskTableModel taskModel = new TaskTableModel();
    private AcquisitionTableModel acquireModel = new AcquisitionTableModel();
    private ServicedUnitTableModel servicedUnitModel = new ServicedUnitTableModel();
    private TechTableModel techsModel = new TechTableModel();
    private PatientTableModel assignedPatientModel = new PatientTableModel();
    private PatientTableModel unassignedPatientModel = new PatientTableModel();
    private DocTableModel doctorsModel = new DocTableModel();
    private PersonnelTableModel personModel = new PersonnelTableModel();
    private UnitTableModel unitModel = new UnitTableModel();
    private PartsTableModel partsModel = new PartsTableModel();
    private FinanceTableModel financeModel = new FinanceTableModel();
    private LoanTableModel loanModel = new LoanTableModel();
    private FinanceTableMouseAdapter financeMouseAdapter;
    private LoanTableMouseAdapter loanMouseAdapter;
    private ScenarioTableModel scenarioModel = new ScenarioTableModel();
    private OrgTreeModel orgModel;
    private UnitTableMouseAdapter unitMouseAdapter;
    private ServicedUnitsTableMouseAdapter servicedUnitMouseAdapter;
    private PartsTableMouseAdapter partsMouseAdapter;
    private TaskTableMouseAdapter taskMouseAdapter;
    private AcquisitionTableMouseAdapter acquisitionMouseAdapter;
    private PersonnelTableMouseAdapter personnelMouseAdapter;
    private OrgTreeMouseAdapter orgMouseAdapter;
    private ScenarioTableMouseAdapter scenarioMouseAdapter;
    private TableRowSorter<PersonnelTableModel> personnelSorter;
    private TableRowSorter<PartsTableModel> partsSorter;
    private TableRowSorter<UnitTableModel> unitSorter;
    private TableRowSorter<ServicedUnitTableModel> servicedUnitSorter;
    private TableRowSorter<TechTableModel> techSorter;
    private TableRowSorter<TechTableModel> whTechSorter;
    private DragoonsRatingDialog dragoonDialog = null;

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
        financeMouseAdapter = new FinanceTableMouseAdapter();
        acquisitionMouseAdapter = new AcquisitionTableMouseAdapter();
        loanMouseAdapter = new LoanTableMouseAdapter();


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
        btnDeleteMission = new javax.swing.JButton();
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
        scrollServicedUnitView = new javax.swing.JScrollPane();
        txtServicedUnitView = new javax.swing.JTextPane();
        servicedUnitTable = new javax.swing.JTable();
        scrollTechTable = new javax.swing.JScrollPane();
        TechTable = new javax.swing.JTable();
        scrollWhTechTable = new javax.swing.JScrollPane();
        whTechTable = new javax.swing.JTable();
        panelDoTask = new javax.swing.JPanel();
        btnDoTask = new javax.swing.JButton();
        lblTarget = new javax.swing.JLabel();
        lblTargetNum = new javax.swing.JLabel();
        astechPoolLabel = new javax.swing.JLabel();
        astechPoolLabelWarehouse = new javax.swing.JLabel();
        scrTextTarget = new javax.swing.JScrollPane();
        textTarget = new javax.swing.JTextArea();
        panSupplies = new javax.swing.JPanel();
        scrollPartsTable = new javax.swing.JScrollPane();
        partsTable = new javax.swing.JTable();
        panInfirmary = new javax.swing.JPanel();
        btnAssignDoc = new javax.swing.JButton();
        btnUnassignDoc = new javax.swing.JButton();
        scrollUnassignedPatient = new javax.swing.JScrollPane();
        scrollDocTable = new javax.swing.JScrollPane();
        DocTable = new javax.swing.JTable();
        panFinances = new javax.swing.JPanel();
        scrollFinanceTable = new javax.swing.JScrollPane();
        financeTable = new javax.swing.JTable();
        scrollLoanTable = new javax.swing.JScrollPane();
        loanTable = new javax.swing.JTable();
        txtPaneReportScrollPane = new javax.swing.JScrollPane();
        txtPaneReport = new javax.swing.JTextPane();
        btnAdvanceDay = new javax.swing.JButton();
        btnOvertime = new javax.swing.JToggleButton();
        btnShowAllTechs = new javax.swing.JToggleButton();
        btnShowAllTechsWarehouse = new javax.swing.JToggleButton();
        btnGMMode = new javax.swing.JToggleButton();
        lblRating = new javax.swing.JLabel();
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
        miGetLoan = new javax.swing.JMenuItem();
        miShoppingList = new javax.swing.JMenuItem();
        menuMarket = new javax.swing.JMenu();
        miPurchaseUnit = new javax.swing.JMenuItem();
        miBuyParts = new javax.swing.JMenuItem();
        menuHire = new javax.swing.JMenu();
        miHireBulk = new javax.swing.JMenuItem();
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

        ArrayList <RowSorter.SortKey> sortKeys;

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
        orgTree.setRowHeight(60);
        orgTree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
        orgTree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
               refreshForceView();
            }
        });
        orgTree.setDragEnabled(true);
        orgTree.setDropMode(DropMode.ON);
        orgTree.setTransferHandler(new OrgTreeTransferHandler());
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

        btnDeleteMission.setText(resourceMap.getString("btnDeleteMission.text")); // NOI18N
        btnDeleteMission.setToolTipText(resourceMap
                .getString("btnDeleteMission.toolTipText")); // NOI18N
        btnDeleteMission.setName("btnDeleteMission"); // NOI18N
        btnDeleteMission.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteMissionActionPerformed(evt);
            }
        });
        panelMissionButtons.add(btnDeleteMission);

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
        personnelSorter.setComparator(PersonnelTableModel.COL_SALARY, new FormattedNumberSorter());
        personnelTable.setRowSorter(personnelSorter);
        sortKeys = new ArrayList<RowSorter.SortKey>();
        sortKeys.add(new RowSorter.SortKey(PersonnelTableModel.COL_RANK, SortOrder.DESCENDING));
        sortKeys.add(new RowSorter.SortKey(PersonnelTableModel.COL_SKILL, SortOrder.DESCENDING));
        personnelSorter.setSortKeys(sortKeys);
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
        unitSorter.setComparator(UnitTableModel.COL_TYPE, new UnitTypeSorter());
        unitSorter.setComparator(UnitTableModel.COL_WCLASS, new WeightClassSorter());
        unitSorter.setComparator(UnitTableModel.COL_COST, new FormattedNumberSorter());
        unitTable.setRowSorter(unitSorter);
        sortKeys = new ArrayList<RowSorter.SortKey>();
        sortKeys.add(new RowSorter.SortKey(UnitTableModel.COL_TYPE, SortOrder.DESCENDING));
        sortKeys.add(new RowSorter.SortKey(UnitTableModel.COL_WCLASS, SortOrder.DESCENDING));
        unitSorter.setSortKeys(sortKeys);
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

        JPanel panelDoTaskWarehouse = new JPanel(new GridBagLayout());

        btnDoTaskWarehouse = new JButton(resourceMap.getString("btnDoTask.text")); // NOI18N
        btnDoTaskWarehouse.setToolTipText(resourceMap.getString("btnDoTask.toolTipText")); // NOI18N
        btnDoTaskWarehouse.setEnabled(false);
        btnDoTaskWarehouse.setName("btnDoTask"); // NOI18N
        btnDoTaskWarehouse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doTask();
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panelDoTaskWarehouse.add(btnDoTaskWarehouse, gridBagConstraints);

        JLabel lblTargetWarehouse = new JLabel(resourceMap.getString("lblTarget.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        panelDoTaskWarehouse.add(lblTargetWarehouse, gridBagConstraints);

        lblTargetNumWarehouse = new JLabel(resourceMap.getString("lblTargetNum.text"));
        lblTargetNumWarehouse.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        panelDoTaskWarehouse.add(lblTargetNumWarehouse, gridBagConstraints);

        textTargetWarehouse = new JTextArea();
        textTargetWarehouse.setColumns(20);
        textTargetWarehouse.setEditable(false);
        textTargetWarehouse.setLineWrap(true);
        textTargetWarehouse.setRows(5);
        textTargetWarehouse.setText(resourceMap.getString("textTarget.text")); // NOI18N
        textTargetWarehouse.setWrapStyleWord(true);
        textTargetWarehouse.setBorder(null);
        textTargetWarehouse.setName("textTarget"); // NOI18N
        JScrollPane scrTargetWarehouse = new JScrollPane(textTargetWarehouse);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panelDoTaskWarehouse.add(scrTargetWarehouse, gridBagConstraints);

        btnShowAllTechsWarehouse.setText(resourceMap.getString("btnShowAllTechs.text")); // NOI18N
        btnShowAllTechsWarehouse.setToolTipText(resourceMap
                .getString("btnShowAllTechs.toolTipText")); // NOI18N
        btnShowAllTechsWarehouse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filterTechs(true);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.NONE;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panelDoTaskWarehouse.add(btnShowAllTechsWarehouse, gridBagConstraints);

        scrollWhTechTable.setMinimumSize(new java.awt.Dimension(200, 200));

        scrollWhTechTable.setName("scrollWhTechTable"); // NOI18N
        scrollWhTechTable.setPreferredSize(new java.awt.Dimension(300, 300));

        whTechTable.setModel(techsModel);
        whTechTable.setName("whTechTable"); // NOI18N
        whTechTable.setRowHeight(60);
        whTechTable.getColumnModel().getColumn(0)
                .setCellRenderer(techsModel.getRenderer());
        whTechTable.getSelectionModel().addListSelectionListener(
                new javax.swing.event.ListSelectionListener() {
                    public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                        updateTechTarget();
                    }
                });
        whTechSorter = new TableRowSorter<TechTableModel>(techsModel);
        whTechSorter.setComparator(0, new TechSorter());
        whTechTable.setRowSorter(whTechSorter);
        sortKeys = new ArrayList<RowSorter.SortKey>();
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
        whTechSorter.setSortKeys(sortKeys);
        scrollWhTechTable.setViewportView(whTechTable);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panelDoTaskWarehouse.add(scrollWhTechTable, gridBagConstraints);

        astechPoolLabelWarehouse.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        astechPoolLabelWarehouse.setText("<html><b>Astech Pool Minutes:</> " + getCampaign().getAstechPoolMinutes() + " (" + getCampaign().getNumberAstechs() + " Astechs)</html>"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panelDoTaskWarehouse.add(astechPoolLabelWarehouse, gridBagConstraints);

        JSplitPane splitWarehouse = new javax.swing.JSplitPane(javax.swing.JSplitPane.HORIZONTAL_SPLIT,panSupplies, panelDoTaskWarehouse);
        splitWarehouse.setOneTouchExpandable(true);
        splitWarehouse.setResizeWeight(1.0);

        tabMain.addTab(
                resourceMap.getString("panSupplies.TabConstraints.tabTitle"),
                splitWarehouse); // NOI18N


        panRepairBay.setName("panRepairBay"); // NOI18N
        panRepairBay.setLayout(new java.awt.GridBagLayout());

        JPanel panServicedUnits = new JPanel(new GridBagLayout());

        scrollServicedUnitTable.setMinimumSize(new java.awt.Dimension(350, 200));
        scrollServicedUnitTable.setName("scrollServicedUnitTable"); // NOI18N
        scrollServicedUnitTable.setPreferredSize(new java.awt.Dimension(350, 200));

        servicedUnitTable.setModel(servicedUnitModel);
        servicedUnitTable.setName("servicedUnitTable"); // NOI18N
        servicedUnitTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        servicedUnitTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        servicedUnitSorter = new TableRowSorter<ServicedUnitTableModel>(servicedUnitModel);
        servicedUnitSorter.setComparator(ServicedUnitTableModel.COL_STATUS, new UnitStatusSorter());
        servicedUnitSorter.setComparator(ServicedUnitTableModel.COL_TYPE, new UnitTypeSorter());
        servicedUnitTable.setRowSorter(servicedUnitSorter);
        sortKeys = new ArrayList<RowSorter.SortKey>();
        sortKeys.add(new RowSorter.SortKey(ServicedUnitTableModel.COL_TYPE, SortOrder.DESCENDING));
        servicedUnitSorter.setSortKeys(sortKeys);
        column = null;
        for (int i = 0; i < ServicedUnitTableModel.N_COL; i++) {
            column = servicedUnitTable.getColumnModel().getColumn(i);
            column.setPreferredWidth(servicedUnitModel.getColumnWidth(i));
            column.setCellRenderer(servicedUnitModel.getRenderer());
        }
        servicedUnitTable.setIntercellSpacing(new Dimension(0, 0));
        servicedUnitTable.setShowGrid(false);
        servicedUnitTable.getSelectionModel().addListSelectionListener(
                new javax.swing.event.ListSelectionListener() {
                    public void valueChanged(
                            javax.swing.event.ListSelectionEvent evt) {
                        servicedUnitTableValueChanged(evt);
                    }
                });
        servicedUnitTable.addMouseListener(servicedUnitMouseAdapter);
        scrollServicedUnitTable.setViewportView(servicedUnitTable);

        scrollServicedUnitView.setMinimumSize(new java.awt.Dimension(350, 400));
        scrollServicedUnitView.setName("scrollServicedUnitView"); // NOI18N
        scrollServicedUnitView.setPreferredSize(new java.awt.Dimension(350, 400));
        txtServicedUnitView.setEditable(false);
        txtServicedUnitView.setContentType("text/html");
        scrollServicedUnitView.setViewportView(txtServicedUnitView);

        splitServicedUnits = new javax.swing.JSplitPane(javax.swing.JSplitPane.VERTICAL_SPLIT,scrollServicedUnitTable, scrollServicedUnitView);
        splitServicedUnits.setOneTouchExpandable(true);
        splitServicedUnits.setResizeWeight(0.0);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panServicedUnits.add(splitServicedUnits, gridBagConstraints);

        JPanel panTasks = new JPanel(new GridBagLayout());

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
                        updateTechTarget();
                    }
                });
        techSorter = new TableRowSorter<TechTableModel>(techsModel);
        techSorter.setComparator(0, new TechSorter());
        TechTable.setRowSorter(techSorter);
        sortKeys = new ArrayList<RowSorter.SortKey>();
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
        techSorter.setSortKeys(sortKeys);
        scrollTechTable.setViewportView(TechTable);
        
        tabTasks.setMinimumSize(new java.awt.Dimension(300, 200));
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
        TaskTable.setRowHeight(70);
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
        AcquisitionTable.setRowHeight(70);
        AcquisitionTable.getColumnModel().getColumn(0)
                .setCellRenderer(acquireModel.getRenderer());
        AcquisitionTable.getSelectionModel().addListSelectionListener(
                new javax.swing.event.ListSelectionListener() {
                    public void valueChanged(
                            javax.swing.event.ListSelectionEvent evt) {
                        AcquisitionTableValueChanged(evt);
                    }
                });
        AcquisitionTable.addMouseListener(acquisitionMouseAdapter);
        scrollAcquisitionTable.setViewportView(AcquisitionTable);

        tabTasks.addTab(resourceMap.getString("scrollTaskTable.TabConstraints.tabTasks"), scrollTaskTable); // NOI18N
        tabTasks.addTab(resourceMap.getString("scrollAcquisitionTable.TabConstraints.tabTasks"), scrollAcquisitionTable); // NOI18N

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panTasks.add(tabTasks, gridBagConstraints);

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
                doTask();
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

        scrTextTarget.setName("jScrollPane6"); // NOI18N

        textTarget.setColumns(20);
        textTarget.setEditable(false);
        textTarget.setLineWrap(true);
        textTarget.setRows(5);
        textTarget.setText(resourceMap.getString("textTarget.text")); // NOI18N
        textTarget.setWrapStyleWord(true);
        textTarget.setBorder(null);
        textTarget.setName("textTarget"); // NOI18N
        scrTextTarget.setViewportView(textTarget);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panelDoTask.add(scrTextTarget, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        panTasks.add(panelDoTask, gridBagConstraints);

        JPanel panTechs = new JPanel(new GridBagLayout());

        btnShowAllTechs.setText(resourceMap.getString("btnShowAllTechs.text")); // NOI18N
        btnShowAllTechs.setToolTipText(resourceMap
                .getString("btnShowAllTechs.toolTipText")); // NOI18N
        btnShowAllTechs.setName("btnShowAllTechs"); // NOI18N
        btnShowAllTechs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filterTechs(false);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panTechs.add(btnShowAllTechs, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panTechs.add(scrollTechTable, gridBagConstraints);

        astechPoolLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        astechPoolLabel.setText("<html><b>Astech Pool Minutes:</> " + getCampaign().getAstechPoolMinutes() + " (" + getCampaign().getNumberAstechs() + " Astechs)</html>"); // NOI18N
        astechPoolLabel.setName("astechPoolLabel"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panTechs.add(astechPoolLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panRepairBay.add(panServicedUnits, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panRepairBay.add(panTasks, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panRepairBay.add(panTechs, gridBagConstraints);

        filterTechs(true);
        filterTechs(false);

        tabMain.addTab(
                resourceMap.getString("panRepairBay.TabConstraints.tabTitle"),
                panRepairBay); // NOI18N

        panInfirmary.setName("panInfirmary"); // NOI18N
        panInfirmary.setLayout(new java.awt.GridBagLayout());

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
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.weighty = 1.0;
        panInfirmary.add(scrollDocTable, gridBagConstraints);


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
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panInfirmary.add(btnAssignDoc, gridBagConstraints);

        btnUnassignDoc.setText(resourceMap.getString("btnUnassignDoc.text")); // NOI18N
        btnUnassignDoc.setEnabled(false);
        btnUnassignDoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUnassignDocActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panInfirmary.add(btnUnassignDoc, gridBagConstraints);

        scrollUnassignedPatient.setMinimumSize(new java.awt.Dimension(300, 200));
        scrollUnassignedPatient.setName("scrollPatientTable"); // NOI18N
        scrollUnassignedPatient.setPreferredSize(new java.awt.Dimension(300, 300));

        listAssignedPatient = new JList(assignedPatientModel);
        listAssignedPatient.setCellRenderer(assignedPatientModel.getRenderer());
        listAssignedPatient.setLayoutOrientation(JList.VERTICAL_WRAP);
        JScrollPane scrollAssignedPatient = new JScrollPane(listAssignedPatient);
        listAssignedPatient.setVisibleRowCount(5);
        listAssignedPatient.getSelectionModel().addListSelectionListener(
                new javax.swing.event.ListSelectionListener() {
                    public void valueChanged(
                            javax.swing.event.ListSelectionEvent evt) {
                        patientTableValueChanged();
                    }
                });

        listUnassignedPatient = new JList(unassignedPatientModel);
        listUnassignedPatient.setCellRenderer(unassignedPatientModel.getRenderer());
        listUnassignedPatient.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        listUnassignedPatient.setVisibleRowCount(-1);
        listUnassignedPatient.getSelectionModel().addListSelectionListener(
                new javax.swing.event.ListSelectionListener() {
                    public void valueChanged(
                            javax.swing.event.ListSelectionEvent evt) {
                        patientTableValueChanged();
                    }
                });
        scrollUnassignedPatient.setViewportView(listUnassignedPatient);

        scrollAssignedPatient.setMinimumSize(new java.awt.Dimension(300, 360));
        scrollAssignedPatient.setPreferredSize(new java.awt.Dimension(300, 360));

        listAssignedPatient.setBorder(BorderFactory.createTitledBorder(resourceMap.getString("panAssignedPatient.title")));
        listUnassignedPatient.setBorder(BorderFactory.createTitledBorder(resourceMap.getString("panUnassignedPatient.title")));

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.0;
        panInfirmary.add(scrollAssignedPatient, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panInfirmary.add(scrollUnassignedPatient, gridBagConstraints);

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
        financeTable.addMouseListener(financeMouseAdapter);
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
     
        loanTable.setModel(loanModel);
        loanTable.setName("loanTable"); // NOI18N
        loanTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        loanTable.addMouseListener(loanMouseAdapter);
        loanTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        column = null;
        for (int i = 0; i < LoanTableModel.N_COL; i++) {
            column = loanTable.getColumnModel().getColumn(i);
            column.setPreferredWidth(loanModel.getColumnWidth(i));
            column.setCellRenderer(loanModel.getRenderer());
        }
        loanTable.setIntercellSpacing(new Dimension(0, 0));
        loanTable.setShowGrid(false);
        scrollLoanTable.setViewportView(loanTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        JPanel panBalance = new JPanel(new GridBagLayout());
        panBalance.add(scrollFinanceTable, gridBagConstraints);
        panBalance.setBorder(BorderFactory.createTitledBorder("Balance Sheet"));
        JPanel panLoan = new JPanel(new GridBagLayout());
        panLoan.add(scrollLoanTable, gridBagConstraints);
        scrollLoanTable.setMinimumSize(new java.awt.Dimension(450, 150));
        scrollLoanTable.setPreferredSize(new java.awt.Dimension(450, 150));
        panLoan.setBorder(BorderFactory.createTitledBorder("Active Loans"));
        JSplitPane splitFinances = new JSplitPane(javax.swing.JSplitPane.VERTICAL_SPLIT,panBalance, panLoan);
        splitFinances.setOneTouchExpandable(true);
        splitFinances.setResizeWeight(1.0);
        
        panFinances.add(splitFinances, gridBagConstraints);
        
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

        
        miGetLoan.setText(resourceMap.getString("miGetLoan.text")); // NOI18N
        miGetLoan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showNewLoanDialog();
            }
        });
        menuManage.add(miGetLoan);
        
        miShoppingList.setText(resourceMap.getString("miShoppingList.text")); // NOI18N
        miShoppingList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showShoppingList();
            }
        });
        menuManage.add(miShoppingList);
        
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
        miHireBulk.setText("Hire Personnel in Bulk");
        miHireBulk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hireBulkPersonnel();
            }
        });
        menuMarket.add(miHireBulk);
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

        //menuBar.add(menuCommunity);

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
        statusPanel.add(btnOvertime);
        refreshRating();
        statusPanel.add(lblRating);
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
                if(cmd.getStatus() > Mission.S_ACTIVE) {
                    getCampaign().completeMission(mission.getId(), cmd.getStatus());
                }
                if(!mission.isActive()) {
                    if(getCampaign().getSortedMissions().size() > 0) {
                        selectedMission = getCampaign().getSortedMissions().get(0).getId();
                    } else {
                        selectedMission = -1;
                    }
                    refreshMissions();
                }
            }
        }
        refreshReport();
        refreshFunds();
        refreshFinancialTransactions();
        refreshRating();
    }

    private void btnDeleteMissionActionPerformed(java.awt.event.ActionEvent evt) {
        Mission mission = getCampaign().getMission(selectedMission);
        MekHQ.logMessage("Attempting to Delete Mission, Mission ID: " + mission.getId());
        if(0 != JOptionPane.showConfirmDialog(null,
                "Are you sure you want to delete this mission?"
            , "Delete mission?",
                JOptionPane.YES_NO_OPTION)) {
            return;
        }
        getCampaign().removeMission(mission.getId());
        if(getCampaign().getSortedMissions().size() > 0) {
            selectedMission = getCampaign().getSortedMissions().get(0).getId();
        } else {
            selectedMission = -1;
        }
        refreshMissions();
        refreshReport();
        refreshFunds();
        refreshFinancialTransactions();
        refreshRating();
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

    private void doTask() {// GEN-FIRST:event_btnDoTaskActionPerformed
        int selectedRow = -1;
        int partId = -1;
        int selectedTechRow = -1;
        Person tech = getSelectedTech();
        if(onWarehouseTab()) {
            selectedRow = partsTable.getSelectedRow();
            selectedTechRow = whTechTable.getSelectedRow();
            Part part = getSelectedTask();
            if(null == part) {
                return;
            }
            if(null == tech) {
                return;
            }
            partId = part.getId();
            //get a new cloned part to work with and decrement original
            Part repairable = part.clone();
            part.decrementQuantity();
            getCampaign().fixPart(repairable, tech);
            getCampaign().addPart(repairable);
            //if the break off part failed to be repaired, then follow it with the focus
            //otherwise keep the focus on the current row
            if(repairable.needsFixing() && !repairable.isBeingWorkedOn()) {
                partId = repairable.getId();
            }
        }
        else if(repairsSelected()) {
            selectedRow = TaskTable.getSelectedRow();
            selectedTechRow = TechTable.getSelectedRow();
            Part part = getSelectedTask();
            if(null == part) {
                return;
            }
            Unit u = part.getUnit();
            if(u.getEntity() instanceof Dropship || u.getEntity() instanceof Jumpship) {
                tech = u.getEngineer();
            }
            if(null == tech) {
                return;
            }
            if(part.onBadHipOrShoulder() && !part.isSalvaging()) {
                if(part instanceof MekLocation && ((MekLocation)part).isBreached()
                        && 0!= JOptionPane.showConfirmDialog(
                            frame,
                            "You are sealing a limb with a bad shoulder or hip.\n"
                                    +"You may continue, but this limb cannot be repaired and you will have to\n"
                                    +"scrap it in order to repair the internal structure and fix the shoulder/hip.\n"
                                    +"Do you wish to continue?",
                            "Busted Hip/Shoulder",
                            JOptionPane.YES_NO_OPTION)) {
                    return;
                }
                else if(part instanceof MekLocation && ((MekLocation)part).isBlownOff()
                        && 0!= JOptionPane.showConfirmDialog(
                            frame,
                            "You are re-attaching a limb with a bad shoulder or hip.\n"
                                    +"You may continue, but this limb cannot be repaired and you will have to\n"
                                    +"scrap it in order to repair the internal structure and fix the shoulder/hip.\n"
                                    +"Do you wish to continue?",
                            "Busted Hip/Shoulder",
                            JOptionPane.YES_NO_OPTION)) {
                    return;
                }
                else if(0!= JOptionPane.showConfirmDialog(
                            frame,
                            "You are repairing/replacing a part on a limb with a bad shoulder or hip.\n"
                                    +"You may continue, but this limb cannot be repaired and you will have to\n"
                                    +"remove this equipment if you wish to scrap and then replace the limb.\n"
                                    +"Do you wish to continue?",
                            "Busted Hip/Shoulder",
                            JOptionPane.YES_NO_OPTION)) {
                    return;
                }
            }
            getCampaign().fixPart(part, tech);
            if(null !=  u && !u.isRepairable() && u.getSalvageableParts().size() == 0) {
                selectedRow = -1;
                getCampaign().removeUnit(u.getId());
            }
            if(null != u && !getCampaign().getServiceableUnits().contains(u)) {
                selectedRow = -1;
            }
        }
        else if(acquireSelected()) {
            selectedRow = AcquisitionTable.getSelectedRow();
            IAcquisitionWork acquisition = getSelectedAcquisition();
            if(null == acquisition) {
                return;
            }
            getCampaign().getShoppingList().addShoppingItem(acquisition.getNewPart(), 1);
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

        //get the selected row back for tasks
        if(selectedRow != -1) {
            if(acquireSelected()) {
                if(AcquisitionTable.getRowCount() > 0) {
                    if(AcquisitionTable.getRowCount() == selectedRow) {
                        AcquisitionTable.setRowSelectionInterval(selectedRow-1, selectedRow-1);
                    } else {
                        AcquisitionTable.setRowSelectionInterval(selectedRow, selectedRow);
                    }
                }
            }
            else if(onWarehouseTab()) {
                boolean found = false;
                for(int i = 0; i < partsTable.getRowCount(); i++) {
                    Part p = partsModel.getPartAt(partsTable.convertRowIndexToModel(i));
                    if(p.getId() == partId) {
                        partsTable.setRowSelectionInterval(i, i);
                        partsTable.scrollRectToVisible(partsTable.getCellRect(i, 0, true));
                        found = true;
                        break;
                    }
                }
                if(!found) {
                    //then set to the current selected row
                    if(partsTable.getRowCount() > 0) {
                        if(partsTable.getRowCount() == selectedRow) {
                            partsTable.setRowSelectionInterval(selectedRow-1, selectedRow-1);
                            //partsTable.scrollRectToVisible(partsTable.getCellRect(selectedRow-1, 0, true));
                        } else {
                            partsTable.setRowSelectionInterval(selectedRow, selectedRow);
                            //partsTable.scrollRectToVisible(partsTable.getCellRect(selectedRow, 0, true));
                        }
                    }
                }
            }
            else if(repairsSelected()) {
                if(TaskTable.getRowCount() > 0) {
                    if(TaskTable.getRowCount() == selectedRow) {
                        TaskTable.setRowSelectionInterval(selectedRow-1, selectedRow-1);
                    } else {
                        TaskTable.setRowSelectionInterval(selectedRow, selectedRow);
                    }
                }
            }
            //also get the selected tech back
            JTable table = TechTable;
            if(onWarehouseTab()) {
                table = whTechTable;
            }
            for(int i = 0; i < table.getRowCount(); i++) {
                Person p = techsModel.getTechAt(table.convertRowIndexToModel(i));
                if(tech.getId().equals(p.getId())) {
                    table.setRowSelectionInterval(i, i);
                    break;
                }
            }
        }

    }// GEN-LAST:event_btnDoTaskActionPerformed

    private Person getSelectedTech() {
        JTable table = TechTable;
        if(onWarehouseTab()) {
            table = whTechTable;
        }
        int row = table.getSelectedRow();
        if(row < 0) {
            return null;
        }
        return  techsModel.getTechAt(table.convertRowIndexToModel(row));
    }

    private Person getSelectedDoctor() {
        int row = DocTable.getSelectedRow();
        if(row < 0) {
            return null;
        }
        return  doctorsModel.getDoctorAt(DocTable.convertRowIndexToModel(row));
    }

    private Part getSelectedTask() {
        if(onWarehouseTab()) {
            int row = partsTable.getSelectedRow();
            if(row < 0) {
                return null;
            }
            return  partsModel.getPartAt(partsTable.convertRowIndexToModel(row));
        }
        int row = TaskTable.getSelectedRow();
        if(row < 0) {
            return null;
        }
        return  taskModel.getTaskAt(TaskTable.convertRowIndexToModel(row));
    }

    private IAcquisitionWork getSelectedAcquisition() {
        int row = AcquisitionTable.getSelectedRow();
        if(row < 0) {
            return null;
        }
        return  acquireModel.getAcquisitionAt(AcquisitionTable.convertRowIndexToModel(row));
    }

    private Unit getSelectedServicedUnit() {
        int row = servicedUnitTable.getSelectedRow();
        if(row < 0) {
            return null;
        }
        return  servicedUnitModel.getUnit(servicedUnitTable.convertRowIndexToModel(row));
    }

    private void TaskTableValueChanged(javax.swing.event.ListSelectionEvent evt) {
        filterTechs(false);
        updateTechTarget();
    }

    private void AcquisitionTableValueChanged(javax.swing.event.ListSelectionEvent evt) {
        filterTechs(false);
        updateTechTarget();
    }

    private void servicedUnitTableValueChanged(javax.swing.event.ListSelectionEvent evt) {
        refreshTaskList();
        refreshAcquireList();
        int selected = servicedUnitTable.getSelectedRow();
        txtServicedUnitView.setText("");
        if(selected > -1) {
            Unit unit = servicedUnitModel.getUnit(servicedUnitTable.convertRowIndexToModel(selected));
            if (null != unit) {
                MechView mv = new MechView(unit.getEntity(), false);
                txtServicedUnitView.setText("<div style='font: 12pt monospaced'>" + mv.getMechReadoutBasic() + "<br>" + mv.getMechReadoutLoadout() + "</div>");
            }
        }
    }

    private void taskTabChanged() {
        filterTechs(false);
        updateTechTarget();
    }

    private void patientTableValueChanged() {
        updateAssignDoctorEnabled();
    }

    private void DocTableValueChanged(javax.swing.event.ListSelectionEvent evt) {
        refreshPatientList();
        updateAssignDoctorEnabled();

    }

    private void PartsTableValueChanged(javax.swing.event.ListSelectionEvent evt) {
        filterTechs(true);
        updateTechTarget();
    }

    private void btnAdvanceDayActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnAdvanceDayActionPerformed
        //first check for overdue loan payments - dont allow advancement until these are addressed
        long overdueAmount = getCampaign().getFinances().checkOverdueLoanPayments(getCampaign());
        if(overdueAmount > 0) {
            JOptionPane.showMessageDialog(frame,
                    "You have overdue loan payments totaling " + DecimalFormat.getInstance().format(overdueAmount) + " C-bills.\nYou must deal with these payments before advancing the day.\nHere are some options:\n  - Sell off equipment to generate funds.\n  - Pay off the collateral on the loan.\n  - Default on the loan.\n  - Just cheat and remove the loan via GM mode.",
                    "Overdue Loan Payments",
                    JOptionPane.WARNING_MESSAGE);
            refreshFunds();
            refreshFinancialTransactions();
            refreshReport();
            return;
        }
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

    private void btnAssignDocActionPerformed(java.awt.event.ActionEvent evt) {
        Person doctor = getSelectedDoctor();
        for(Person p : getSelectedUnassignedPatients()) {
            if (null != p && p.needsFixing() && null != doctor
                    && getCampaign().getPatientsFor(doctor)<25
                    && getCampaign().getTargetFor(p, doctor).getValue() != TargetRoll.IMPOSSIBLE) {
                p.setDoctorId(doctor.getId());
            }
        }

        refreshTechsList();
        refreshDoctorsList();
        refreshPatientList();
    }

    private void btnUnassignDocActionPerformed(java.awt.event.ActionEvent evt) {
        for(Person p : getSelectedAssignedPatients()) {
            if ((null != p)) {
                p.setDoctorId(null);
            }
        }

        refreshTechsList();
        refreshDoctorsList();
        refreshPatientList();
    }

    private void hirePerson(java.awt.event.ActionEvent evt) {
        int type = Integer.parseInt(evt.getActionCommand());
        NewRecruitDialog npd = new NewRecruitDialog(getFrame(), true,
                getCampaign().newPerson(type),
                getCampaign(),
                this, getPortraits());
        npd.setVisible(true);
    }

    private void hireBulkPersonnel() {
        HireBulkPersonnelDialog hbpd = new HireBulkPersonnelDialog(getFrame(), true, getCampaign(), this);
        hbpd.setVisible(true);
    }

    private void menuSaveXmlActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_menuSaveActionPerformed
        MekHQ.logMessage("Saving campaign...");
        // Choose a file...
        File file = selectSaveCampaignFile();
        if (file == null) {
            // I want a file, y'know!
            return;
        }
        String path = file.getPath();
        if (!path.endsWith(".cpnx")) {
            path += ".cpnx";
            file = new File(path);
        }
        
        //check for existing file and make a back-up if found
        String path2 = path + "_backup";
        File backupFile = new File(path2);
        if(file.exists()) {     
            Utilities.copyfile(file, backupFile);
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
            //delete the backup file because we didn't need it
            if(backupFile.exists()) {
                backupFile.delete();
            }
            MekHQ.logMessage("Campaign saved to " + file);
        } catch (Exception ex) {
            MekHQ.logError(ex);
            JOptionPane.showMessageDialog(getFrame(),
                    "Oh no! The program was unable to correctly save your game. We know this\n" +
                    "is annoying and apologize. Please help us out and submit a bug with the\n" +
                    "mekhqlog.txt file from this game so we can prevent this from happening in\n" +
                    "the future.",
                    "Could not save game",
                    JOptionPane.ERROR_MESSAGE);
            //restore the backup file
            file.delete();
            if(backupFile.exists()) {
                Utilities.copyfile(backupFile, file);
                backupFile.delete();
            }
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
        case PG_SOLDIER:
            return "Conventional Infantry";
        case PG_SUPPORT:
            return "Support Personnel";
        case PG_VESSEL:
            return "Large Vessel Crews";
        case PG_TECH:
            return "Techs";
        case PG_DOC:
            return "Medical Staff";
        case PG_ADMIN:
            return "Administrators";
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
    
    private void showShoppingList() {
        ShoppingListDialog sld = new ShoppingListDialog(true, this);
        sld.setVisible(true);
        refreshPartsList();
        refreshAcquireList();
    }
    
    private void showNewLoanDialog() {
        NewLoanDialog nld = new NewLoanDialog(getFrame(), true, getCampaign());
        nld.setVisible(true);
        refreshFinancialTransactions();
        refreshFunds();
        refreshReport();
        refreshRating();
    }

    public void refitUnit(Refit r, boolean selectModelName) {
        if(getCampaign().getTechs().size() > 0) {
            String name;
            HashMap<String,Person> techHash = new HashMap<String,Person>();
            for(Person tech : getCampaign().getTechs()) {
                if(getCampaign().isWorkingOnRefit(tech)) {
                    continue;
                }
                name = tech.getName() + ", " + tech.getPrimaryRoleDesc() + " (" + getCampaign().getTargetFor(r, tech).getValueAsString() + "+)";
                techHash.put(name, tech);
            }
            String[] techNames = new String[techHash.keySet().size()];
            int i = 0;
            for(String n : techHash.keySet()) {
                techNames[i] = n;
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
            if(null == s) {
                return;
            }
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
            RefitNameDialog rnd = new RefitNameDialog(frame, true, r);
            rnd.setVisible(true);
            if(rnd.wasCancelled()) {
                return;
            }
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
        try {
            r.begin();
        } catch(EntityLoadingException ex) {
            JOptionPane.showMessageDialog(null,
                    "For some reason, the unit you are trying to customize cannot be loaded\n and so the customization was cancelled. Please report the bug with a description\nof the unit being customized.",
                    "Could not customize unit",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
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
        String description = addFundsDialog.getFundsDescription();
        int category = addFundsDialog.getCategory();
        getCampaign().addFunds(funds, description, category);
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
            for (Crew pilot : parser.getPilots()) {
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
        boolean control = JOptionPane.showConfirmDialog(getFrame(),
                "Did your side control the battlefield at the end of the scenario?",
                "Control of Battlefield?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE) ==
                JOptionPane.YES_OPTION;
        ResolveScenarioTracker tracker = new ResolveScenarioTracker(scenario, getCampaign());
        ChooseMulFilesDialog chooseFilesDialog = new ChooseMulFilesDialog(getFrame(), true, tracker, control);
        chooseFilesDialog.setVisible(true);
        if(chooseFilesDialog.wasCancelled()) {
            return;
        }
        tracker.postProcessEntities(control);
        ResolveScenarioWizardDialog resolveDialog = new ResolveScenarioWizardDialog(getFrame(), true, tracker);
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
        Vector<UUID> uids = scenario.getForces(getCampaign()).getAllUnits();

        if(uids.size() == 0) {
            return;
        }

        Vector<Entity> chosen = new Vector<Entity>();
        //ArrayList<Unit> toDeploy = new ArrayList<Unit>();
        StringBuffer undeployed = new StringBuffer();

        for(UUID uid : uids) {
            Unit u = getCampaign().getUnit(uid);
            if(u.isUnmanned()) {
                continue;
            }
            if (null != u.getEntity()) {
                if (null == u.checkDeployment()) {
                    chosen.add(u.getEntity());
                } else {
                    undeployed.append("\n")
                    .append(u.getName())
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
        Vector<UUID> uids = scenario.getForces(getCampaign()).getAllUnits();

        if(uids.size() == 0) {
            return;
        }

        ArrayList<Unit> chosen = new ArrayList<Unit>();
        //ArrayList<Unit> toDeploy = new ArrayList<Unit>();
        StringBuffer undeployed = new StringBuffer();

        for(UUID uid : uids) {
            Unit u = getCampaign().getUnit(uid);
            if(u.isUnmanned()) {
                continue;
            }
            if (null != u.getEntity()) {
                if (null == u.checkDeployment()) {
                    chosen.add(u);
                } else {
                    undeployed.append("\n")
                    .append(u.getName())
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
        Vector<UUID> uids = scenario.getForces(getCampaign()).getAllUnits();

        if(uids.size() == 0) {
            return;
        }

        ArrayList<Entity> chosen = new ArrayList<Entity>();
        //ArrayList<Unit> toDeploy = new ArrayList<Unit>();
        StringBuffer undeployed = new StringBuffer();

        for(UUID uid : uids) {
            Unit u = getCampaign().getUnit(uid);
            if(u.isUnmanned()) {
                continue;
            }
            if (null != u.getEntity()) {
                if (null == u.checkDeployment()) {
                    chosen.add(u.getEntity());
                } else {
                    undeployed.append("\n")
                    .append(u.getName())
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
        if(selected == servicedUnitTable.getRowCount()) {
            selected--;
        }
        if ((selected > -1) && (selected < servicedUnitTable.getRowCount())) {
            servicedUnitTable.setRowSelectionInterval(selected, selected);
        }
        refreshRating();
    }

    public void refreshPersonnelList() {
        UUID selectedUUID = null;
        int selectedRow = personnelTable.getSelectedRow();
        if(selectedRow != -1) {
            Person p = personModel.getPerson(personnelTable.convertRowIndexToModel(selectedRow));
            if(null != p) {
                selectedUUID = p.getId();
            }
        }
        personModel.setData(getCampaign().getPersonnel());
        //try to put the focus back on same person if they are still available
        for(int row = 0; row < personnelTable.getRowCount(); row++) {
            Person p = personModel.getPerson(personnelTable.convertRowIndexToModel(row));
            if(p.getId().equals(selectedUUID)) {
                personnelTable.setRowSelectionInterval(row, row);
                refreshPersonnelView();
                break;
            }
        }
        refreshRating();
    }

    public void changeMission() {
        int idx = choiceMission.getSelectedIndex();
        btnEditMission.setEnabled(false);
        btnCompleteMission.setEnabled(false);
        btnDeleteMission.setEnabled(false);
        btnAddScenario.setEnabled(false);
        if(idx >= 0 && idx < getCampaign().getSortedMissions().size()) {
            Mission m = getCampaign().getSortedMissions().get(idx);
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
                btnEditMission.setEnabled(true);
                btnCompleteMission.setEnabled(m.isActive());
                btnDeleteMission.setEnabled(true);
                btnAddScenario.setEnabled(m.isActive());
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
        UUID selectedUUID = null;
        int selectedRow = unitTable.getSelectedRow();
        if(selectedRow != -1) {
            Unit u = unitModel.getUnit(unitTable.convertRowIndexToModel(selectedRow));
            if(null != u) {
                selectedUUID = u.getId();
            }
        }
        unitModel.setData(getCampaign().getUnits());
        //try to put the focus back on same person if they are still available
        for(int row = 0; row < unitTable.getRowCount(); row++) {
            Unit u = unitModel.getUnit(unitTable.convertRowIndexToModel(row));
            if(u.getId().equals(selectedUUID)) {
                unitTable.setRowSelectionInterval(row, row);
                refreshUnitView();
                break;
            }
        }
        refreshLab();
        refreshRating();
    }

    public void refreshTaskList() {
        UUID uuid = null;
        if(null != getSelectedServicedUnit()) {
            uuid = getSelectedServicedUnit().getId();
        }
        taskModel.setData(getCampaign().getPartsNeedingServiceFor(uuid));
    }

    public void refreshAcquireList() {
        UUID uuid = null;
        if(null != getSelectedServicedUnit()) {
            uuid = getSelectedServicedUnit().getId();
        }
        acquireModel.setData(getCampaign().getAcquisitionsForUnit(uuid));
    }

    public void refreshMissions() {
        choiceMission.removeAllItems();
        for(Mission m : getCampaign().getSortedMissions()) {
            String desc = m.getName();
            if(!m.isActive()) {
                desc += " (Complete)";
            }
            choiceMission.addItem(desc);
            if(m.getId() == selectedMission) {
                choiceMission.setSelectedItem(m.getName());
            }
        }
        if(choiceMission.getSelectedIndex() == -1 && getCampaign().getSortedMissions().size() > 0) {
            selectedMission = getCampaign().getSortedMissions().get(0).getId();
            choiceMission.setSelectedIndex(0);
        }
        changeMission();
        refreshRating();
    }

    public void refreshLab() {
        if(null == panMekLab) {
            return;
        }
        Unit u = panMekLab.getUnit();
        if(null == u) {
            return;
        }
        if(null == getCampaign().getUnit(u.getId())) {
            //this unit has been removed so clear the mek lab
            panMekLab.clearUnit();
        } else {
            //put a try-catch here so that bugs in the meklab don't screw up
            //other stuff
            try {
                panMekLab.refreshSummary();
            } catch (Exception err) {
                err.printStackTrace();
            }
        }
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
        astechPoolLabelWarehouse.setText(astechString); // NOI18N
    }

    public void refreshDoctorsList() {
        int selected = DocTable.getSelectedRow();
        doctorsModel.setData(getCampaign().getDoctors());
        if ((selected > -1) && (selected < getCampaign().getDoctors().size())) {
            DocTable.setRowSelectionInterval(selected, selected);
        }
    }

    public void refreshPatientList() {
        Person doctor = getSelectedDoctor();
        ArrayList<Person> assigned = new ArrayList<Person>();
        ArrayList<Person> unassigned = new ArrayList<Person>();
        for(Person patient : getCampaign().getPatients()) {
            if(null == patient.getDoctorId()) {
                unassigned.add(patient);
            } else if(null != doctor && patient.getDoctorId().equals(doctor.getId())) {
                assigned.add(patient);
            }
        }
        int assignedIdx = listAssignedPatient.getSelectedIndex();
        int unassignedIdx = listUnassignedPatient.getSelectedIndex();
        assignedPatientModel.setData(assigned);
        unassignedPatientModel.setData(unassigned);
        if(assignedIdx < assignedPatientModel.getSize()) {
            listAssignedPatient.setSelectedIndex(assignedIdx);
        } else {
            listAssignedPatient.setSelectedIndex(-1);
        }
        if(unassignedIdx < unassignedPatientModel.getSize()) {
            listUnassignedPatient.setSelectedIndex(unassignedIdx);
        } else {
            listUnassignedPatient.setSelectedIndex(-1);
        }

    }

    public void refreshPartsList() {
        partsModel.setData(getCampaign().getSpareParts());
    }

    public void refreshFinancialTransactions() {
        financeModel.setData(getCampaign().getFinances().getAllTransactions());
        loanModel.setData(getCampaign().getFinances().getAllLoans());
        refreshFunds();
        refreshRating();
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
                orgTree.setSelectionPath(null);
                refreshForceView();
            }
        });
        refreshRating();
    }

    protected void refreshFunds() {
        long funds = getCampaign().getFunds();
        NumberFormat numberFormat = NumberFormat.getIntegerInstance();
        String inDebt = "";
        if(getCampaign().getFinances().isInDebt()) {
            inDebt = " <font color='red'>(in Debt)</font>";
        }
        String text = "<html><b>Funds:</b> " + numberFormat.format(funds) + " C-Bills" + inDebt + "</html>";
        lblFunds.setText(text);
    }

    protected void refreshRating() {
        if(getCampaign().getCampaignOptions().useDragoonRating()) {
            if (dragoonDialog != null && dragoonDialog.isVisible())
                dragoonDialog.setVisible(false);
            dragoonDialog = null;
            String text = "<html><b>Dragoons Rating:</b> " + getCampaign().getDragoonRating() + "</html>";
            lblRating.setText(text);
            lblRating.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    //Right-click only.
                    if (SwingUtilities.isRightMouseButton(e)) {
                        if (dragoonDialog == null) {
                            dragoonDialog = new DragoonsRatingDialog(getFrame(), false, getCampaign());
                        }
                        dragoonDialog.setVisible(true);
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    //not used
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    //not used
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    //not used
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    //not used
                }
            });
        } else {
            lblRating.setText("");
        }
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

    protected ArrayList<Person> getSelectedUnassignedPatients() {
        ArrayList<Person> patients = new ArrayList<Person>();
        int[] indices = listUnassignedPatient.getSelectedIndices();
        if(unassignedPatientModel.getSize() == 0) {
            return patients;
        }
        for(int idx : indices) {
            patients.add((Person)unassignedPatientModel.getElementAt(idx));
        }
        return patients;
    }

    protected ArrayList<Person> getSelectedAssignedPatients() {
        ArrayList<Person> patients = new ArrayList<Person>();
        int[] indices = listAssignedPatient.getSelectedIndices();
        if(assignedPatientModel.getSize() == 0) {
            return patients;
        }
        for(int idx : indices) {
            patients.add((Person)assignedPatientModel.getElementAt(idx));
        }
        return patients;
    }

    protected void updateAssignDoctorEnabled() {
        Person doctor = getSelectedDoctor();
        if (!getSelectedUnassignedPatients().isEmpty()  && null != doctor
                && getCampaign().getPatientsFor(doctor)<25) {
            btnAssignDoc.setEnabled(true);
        } else {
            btnAssignDoc.setEnabled(false);
        }
        btnUnassignDoc.setEnabled(!getSelectedAssignedPatients().isEmpty());
    }

    protected void updateTechTarget() {
        TargetRoll target = null;
        if(acquireSelected()) {
            IAcquisitionWork acquire = getSelectedAcquisition();           
            if(null != acquire) {
                Person admin = getCampaign().getLogisticsPerson();
                target = getCampaign().getTargetForAcquisition(acquire, admin);
            }
        }
        else  {
            Part part = getSelectedTask();
            if(null != part) {
                Unit u = part.getUnit();
                Person tech = getSelectedTech();
                if(null != u && (u.getEntity() instanceof Dropship || u.getEntity() instanceof Jumpship)) {
                    tech = u.getEngineer();
                    if(null == tech) {
                        target = new TargetRoll(TargetRoll.IMPOSSIBLE, "You must have a crew assigned to large vessels to attempt repairs.");
                    }
                }
                if(null != tech) {
                    target = getCampaign().getTargetFor(part, tech);
                }
            }
        }
        JButton btn = btnDoTask;
        JTextArea text = textTarget;
        JLabel lbl = lblTargetNum;
        if(onWarehouseTab()) {
            btn = btnDoTaskWarehouse;
            text = textTargetWarehouse;
            lbl = lblTargetNumWarehouse;
        }
        if(null != target) {
            btn.setEnabled(target.getValue() != TargetRoll.IMPOSSIBLE);
            text.setText(target.getDesc());
            lbl.setText(target.getValueAsString());
        } else {
            btn.setEnabled(false);
            text.setText("");
            lbl.setText("-");
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
                        (nGroup == PG_COMBAT && type <= Person.T_SPACE_GUNNER) ||
                        (nGroup == PG_SUPPORT && type > Person.T_SPACE_GUNNER) ||
                        (nGroup == PG_MW && type == Person.T_MECHWARRIOR) ||
                        (nGroup == PG_CREW && (type == Person.T_GVEE_DRIVER || type == Person.T_NVEE_DRIVER || type == Person.T_VTOL_PILOT || type == Person.T_VEE_GUNNER)) ||
                        (nGroup == PG_PILOT && type == Person.T_AERO_PILOT) ||
                        (nGroup == PG_PROTO && type == Person.T_PROTO_PILOT) ||
                        (nGroup == PG_BA && type == Person.T_BA) ||
                        (nGroup == PG_SOLDIER && type == Person.T_INFANTRY) ||
                        (nGroup == PG_VESSEL && (type == Person.T_SPACE_PILOT || type == Person.T_SPACE_CREW || type == Person.T_SPACE_GUNNER || type == Person.T_NAVIGATOR)) ||
                        (nGroup == PG_TECH && type >= Person.T_MECH_TECH && type < Person.T_DOCTOR) ||
                        (nGroup == PG_DOC && ((type == Person.T_DOCTOR) || (type == Person.T_MEDIC))) ||
                        (nGroup == PG_ADMIN && type > Person.T_MEDIC)
                        ) {
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

    public void filterTechs(boolean warehouse) {
        RowFilter<TechTableModel, Integer> techTypeFilter = null;
        final Part part = getSelectedTask();
        techTypeFilter = new RowFilter<TechTableModel,Integer>() {
            @Override
            public boolean include(Entry<? extends TechTableModel, ? extends Integer> entry) {
                if(acquireSelected()) {
                    return false;
                }
                if(null == part) {
                    return false;
                }
                if(!part.needsFixing() && !part.isSalvaging()) {
                    return false;
                }
                TechTableModel techModel = entry.getModel();
                Person tech = techModel.getTechAt(entry.getIdentifier());
                if(!onWarehouseTab() && !tech.isRightTechTypeFor(part) && !btnShowAllTechs.isSelected()) {
                    return false;
                }
                if(onWarehouseTab() && !tech.isRightTechTypeFor(part) && !btnShowAllTechsWarehouse.isSelected()) {
                    return false;
                }
                Skill skill = tech.getSkillForWorkingOn(part);
                int modePenalty = Modes.getModeExperienceReduction(part.getMode());
                if(skill == null) {
                    return false;
                }
                return (part.getSkillMin() <= (skill.getExperienceLevel()-modePenalty));
            }
        };
        if(warehouse) {
            whTechSorter.setRowFilter(techTypeFilter);
        } else {
            techSorter.setRowFilter(techTypeFilter);
        }
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
            columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_NAME), true);
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
            columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_NAME), true);
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
            columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_NAME), true);
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

    public DirectoryItems getPortraits() {
        return getApplication().getPortraits();
    }

    protected DirectoryItems getCamos() {
        return getApplication().getCamos();
    }

    public DirectoryItems getForceIcons() {
        return getApplication().getForceIcons();
    }

    protected MechTileset getMechTiles() {
        return getApplication().getMechTiles();
    }

    public JFrame getFrame() {
        return frame;
    }

    public CampaignGUI getCampaignGUI() {
        return this;
    }

    protected boolean repairsSelected() {
        return tabTasks.getSelectedIndex() == 0;
    }

    protected boolean acquireSelected() {
        return tabTasks.getSelectedIndex() == 1 && !onWarehouseTab();
    }

    protected boolean onWarehouseTab() {
        //TODO: dont do this with a hard number
        return tabMain.getSelectedIndex() == 5;
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
                int actualCol = table.convertColumnIndexToModel(column);
                int actualRow = table.convertRowIndexToModel(row);
                Image imgTool = getToolkit().getImage("data/images/misc/tools.png"); //$NON-NLS-1$
                this.setImage(imgTool);
                setOpaque(true);
                setText("<html>" + getValueAt(actualRow, actualCol).toString() + "</html>");
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
            Part part = taskModel.getTaskAt(TaskTable.convertRowIndexToModel(TaskTable.getSelectedRow()));
            if(null == part) {
                return;
            }
            if (command.equalsIgnoreCase("SCRAP")) {
                if(null != part.checkScrappable()) {
                    JOptionPane.showMessageDialog(frame,
                            part.checkScrappable(),
                            "Cannot scrap part",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
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
            /*  for (WorkItem task : tasks) {
                    task.resetTimeSpent();
                    task.setTeam(null);
                    refreshServicedUnitList();
                    refreshUnitList();
                    refreshTaskList();
                    refreshAcquireList();
                }*/
            } else if (command.contains("FIX")) {
            /*  for (WorkItem task : tasks) {
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
                if (part.checkFixable() == null) {
                    getCampaign().addReport(part.succeed());
                    
                    refreshServicedUnitList();
                    refreshUnitList();
                    refreshTaskList();
                    refreshAcquireList();
                    refreshPartsList();
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
                    cbMenuItem.setEnabled(!part.isBeingWorkedOn());
                    menu.add(cbMenuItem);
                }
                popup.add(menu);
                // Scrap component
                if(!part.canNeverScrap()) {
                    menuItem = new JMenuItem("Scrap component");
                    menuItem.setActionCommand("SCRAP");
                    menuItem.addActionListener(this);
                    menuItem.setEnabled(!part.isBeingWorkedOn());
                    popup.add(menuItem);
                }
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
                
                menuItem = new JMenuItem("Complete Task");
                menuItem.setActionCommand("FIX");
                menuItem.addActionListener(this);
                menuItem.setEnabled(getCampaign().isGM()
                        && (null == part.checkFixable()));
                menu.add(menuItem);
                
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
    
    public class AcquisitionTableMouseAdapter extends MouseInputAdapter implements ActionListener {
        public void actionPerformed(ActionEvent action) {
            String command = action.getActionCommand();
            IAcquisitionWork acquisitionWork = acquireModel.getAcquisitionAt(AcquisitionTable.convertRowIndexToModel(AcquisitionTable.getSelectedRow()));
            if(null == acquisitionWork) {
                return;
            }
            if (command.contains("FIX")) {
                getCampaign().addReport(acquisitionWork.find());
                
                refreshServicedUnitList();
                refreshUnitList();
                refreshTaskList();
                refreshAcquireList();
                refreshPartsList();
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
                int row = AcquisitionTable.getSelectedRow();
                if(row < 0) {
                    return;
                }
                IAcquisitionWork acquisitionWork = acquireModel.getAcquisitionAt(row);
                JMenuItem menuItem = null;
                JMenu menu = null;
                JCheckBoxMenuItem cbMenuItem = null;
                menu = new JMenu("GM Mode");
                popup.add(menu);
                // Auto complete task
    
                menuItem = new JMenuItem("Complete Task");
                menuItem.setActionCommand("FIX");
                menuItem.addActionListener(this);
                menuItem.setEnabled(getCampaign().isGM());
                menu.add(menuItem);
                   
                popup.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }

    /**
     * A table model for displaying units that are being serviced in the repair bay
     */
    public class ServicedUnitTableModel extends AbstractTableModel {
        private static final long serialVersionUID = 3314061779690077204L;

        private final static int COL_NAME    =    0;
        private final static int COL_TYPE    =    1;
        private final static int COL_STATUS   =   2;
        private final static int COL_SITE =   3;
        private final static int COL_REPAIR  =    4;
        private final static int COL_PARTS    =   5;
        private final static int N_COL =          6;

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
                case COL_NAME:
                    return "Name";
                case COL_TYPE:
                    return "Type";
                case COL_STATUS:
                    return "Status";
                case COL_SITE:
                    return "Site";
                case COL_REPAIR:
                    return "# Repairs";
                case COL_PARTS:
                    return "# Parts";
                default:
                    return "?";
            }
        }

        public int getColumnWidth(int c) {
            switch(c) {
            case COL_TYPE:
            case COL_STATUS:
            case COL_SITE:
                return 50;
            case COL_NAME:
                return 100;
            default:
                return 25;
            }
        }

        public int getAlignment(int col) {
            switch(col) {
            case COL_REPAIR:
            case COL_PARTS:
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
            if(null == e) {
                return "?";
            }
            if(col == COL_NAME) {
                return u.getName();
            }
            if(col == COL_TYPE) {
                return UnitType.getTypeDisplayableName(UnitType.determineUnitTypeCode(e));
            }
            if(col == COL_STATUS) {
                return u.getStatus();
            }
            if(col == COL_SITE) {
                return Unit.getSiteName(u.getSite());
            }
            if(col == COL_REPAIR) {
                return u.getPartsNeedingFixing().size();
            }
            if(col == COL_PARTS) {
                return u.getPartsNeeded().size();
            }
            return "?";
        }

        public ServicedUnitTableModel.Renderer getRenderer() {
            return new ServicedUnitTableModel.Renderer();
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
                    } else if (u.getEntity() instanceof Infantry
                            && u.getActiveCrew().size() < u.getFullCrewSize()) {
                        setBackground(Color.RED);
                    }
                    else {
                        setBackground(Color.WHITE);
                    }
                }
                return this;
            }

        }
        /*
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
        */
    }

    public class ServicedUnitsTableMouseAdapter extends MouseInputAdapter implements
            ActionListener {

        public void actionPerformed(ActionEvent action) {
            String command = action.getActionCommand();
            Unit selectedUnit = servicedUnitModel.getUnit(servicedUnitTable.convertRowIndexToModel(servicedUnitTable.getSelectedRow()));
            int[] rows = servicedUnitTable.getSelectedRows();
            Unit[] units = new Unit[rows.length];
            for(int i=0; i<rows.length; i++) {
                units[i] = servicedUnitModel.getUnit(servicedUnitTable.convertRowIndexToModel(rows[i]));
            }
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
                                        + unit.getName()
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
                if(servicedUnitTable.getSelectedRowCount() == 0) {
                    return;
                }
                int[] rows = servicedUnitTable.getSelectedRows();
                int row = servicedUnitTable.getSelectedRow();
                boolean oneSelected = servicedUnitTable.getSelectedRowCount() == 1;
                Unit unit = servicedUnitModel.getUnit(servicedUnitTable.convertRowIndexToModel(row));
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
                    for (AmmoType atype : Utilities.getMunitionsFor(unit.getEntity(), curType, getCampaign().getCampaignOptions().getTechLevel())) {
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
                } else {
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
            return getTechAt(row);
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
                int actualRow = table.convertRowIndexToModel(row);
                setOpaque(true);
                setPortrait(getTechAt(actualRow));
                setText(getTechAt(actualRow).getTechDesc(getCampaign().isOvertimeAllowed()));
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
    public class PatientTableModel extends AbstractListModel {
        private static final long serialVersionUID = -1615929049408417297L;

        ArrayList<Person> patients;

        public PatientTableModel() {
            patients = new ArrayList<Person>();
        }

        //fill table with values
        public void setData(ArrayList<Person> data) {
            patients = data;
            this.fireContentsChanged(this, 0, patients.size());
            //fireTableDataChanged();
        }

        @Override
        public Object getElementAt(int index) {
            return patients.get(index);
        }

        @Override
        public int getSize() {
            return patients.size();
        }
        public PatientTableModel.Renderer getRenderer() {
            return new PatientTableModel.Renderer(getCamos(), getPortraits(), getMechTiles());
        }

        public class Renderer extends BasicInfo implements ListCellRenderer {
            public Renderer(DirectoryItems camos, DirectoryItems portraits,
                    MechTileset mt) {
                super(camos, portraits, mt);
            }

            private static final long serialVersionUID = -406535109900807837L;

            public Component getListCellRendererComponent(
                    JList list,
                    Object value,
                    int index,
                    boolean isSelected,
                    boolean cellHasFocus) {
                Component c = this;
                setOpaque(true);
                Person p = (Person)getElementAt(index);
                setText(p.getPatientDesc());
                setPortrait(p);

                if (isSelected) {
                    select();
                } else {
                    unselect();
                }
/*
                if ((null != p) && p.isDeployed(getCampaign())) {
                    c.setBackground(Color.GRAY);
                } else if ((null != p) && p.needsFixing()) {
                    c.setBackground(new Color(205, 92, 92));
                } else {
                    c.setBackground(new Color(220, 220, 220));
                }
*/
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
    }

    public class OrgTreeMouseAdapter extends MouseInputAdapter implements
    ActionListener {

        @Override
        public void actionPerformed(ActionEvent action) {
            StringTokenizer st = new StringTokenizer(action.getActionCommand(), "|");
            String command = st.nextToken();
            String type = st.nextToken();
            String target = st.nextToken();
            Vector<Force> forces = new Vector<Force>();
            Vector<Unit> units = new Vector<Unit>();
            while(st.hasMoreTokens()) {
                String id = st.nextToken();
                if(type.equals("FORCE")) {
                    Force force = getCampaign().getForce(Integer.parseInt(id));
                    if(null != force) {
                        forces.add(force);
                    }
                }
                if(type.equals("UNIT")) {
                    Unit unit = getCampaign().getUnit(UUID.fromString(id));
                    if(null != unit) {
                        units.add(unit);
                    }
                }
            }
            if(type.equals("FORCE")) {
                Vector<Force> newForces = new Vector<Force>();
                for(Force force : forces) {
                    boolean duplicate = false;
                    for(Force otherForce : forces) {
                        if(otherForce.getId() == force.getId()) {
                            continue;
                        }
                        if(otherForce.isAncestorOf(force)) {
                            duplicate = true;
                            break;
                        }
                    }
                    if(!duplicate) {
                        newForces.add(force);
                    }
                }
                forces = newForces;
            }
            //TODO: eliminate any forces that are descendants of other forces in the vector
            Force singleForce = null;
            if(!forces.isEmpty()) {
                singleForce = forces.get(0);
            }
            Unit singleUnit = null;
            if(!units.isEmpty()) {
                singleUnit = units.get(0);
            }
            if(command.contains("ADD_FORCE")) {
                if(null != singleForce) {
                    String name = (String)JOptionPane.showInputDialog(
                            null,
                            "Enter the force name",
                            "Force Name",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            null,
                            "My Lance");
                    if(null != name) {
                        getCampaign().addForce(new Force(name), singleForce);
                        refreshOrganization();
                    }
                }
            } if(command.contains("ADD_UNIT")) {
                if(null != singleForce) {
                    Unit u = getCampaign().getUnit(UUID.fromString(target));
                    if(null != u) {
                        getCampaign().addUnitToForce(u, singleForce.getId());
                        refreshOrganization();
                        refreshScenarioList();
                        refreshPersonnelList();
                        refreshUnitList();
                        refreshServicedUnitList();
                    }
                }
            } else if(command.contains("DEPLOY_FORCE")) {
                int sid = Integer.parseInt(target);
                Scenario scenario = getCampaign().getScenario(sid);
                for(Force force : forces) {
                    if(null != force && null != scenario) {
                        scenario.addForces(force.getId());
                        force.setScenarioId(scenario.getId());
                        refreshScenarioList();
                        for(UUID uid : force.getAllUnits()) {
                            Unit u = getCampaign().getUnit(uid);
                            if(null != u) {
                                u.setScenarioId(scenario.getId());
                            }
                        }
                    }
                }
                refreshScenarioList();
                refreshOrganization();
                refreshPersonnelList();
                refreshUnitList();
                refreshServicedUnitList();
            } else if(command.contains("CHANGE_ICON")) {
                if(null != singleForce) {
                    PortraitChoiceDialog pcd = new PortraitChoiceDialog(getFrame(), true,
                            singleForce.getIconCategory(),
                            singleForce.getIconFileName(), getForceIcons());
                    pcd.setVisible(true);
                    singleForce.setIconCategory(pcd.getCategory());
                    singleForce.setIconFileName(pcd.getFileName());
                    refreshOrganization();
                }
            } else if(command.contains("CHANGE_NAME")) {
                if(null != singleForce) {
                    String name = (String)JOptionPane.showInputDialog(
                            null,
                            "Enter the force name",
                            "Force Name",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            null,
                            singleForce.getName());
                    singleForce.setName(name);
                    refreshOrganization();
                }
            } else if(command.contains("CHANGE_DESC")) {
                if(null != singleForce) {
                    TextAreaDialog tad = new TextAreaDialog(getFrame(), true,
                            "Edit Force Description",
                            singleForce.getDescription());
                    tad.setVisible(true);
                    if(tad.wasChanged()) {
                        singleForce.setDescription(tad.getText());
                        refreshOrganization();
                    }
                }
            } else if(command.contains("REMOVE_FORCE")) {
                for(Force force : forces) {
                    if(null != force && null != force.getParentForce()) {
                        if(0 != JOptionPane.showConfirmDialog(null,
                                "Are you sure you want to delete " + force.getFullName() + "?"
                            , "Delete Force?",
                                JOptionPane.YES_NO_OPTION)) {
                            return;
                        }
                        getCampaign().removeForce(force);
                    }
                }
                refreshOrganization();
                refreshPersonnelList();
                refreshScenarioList();
            } else if(command.contains("REMOVE_UNIT")) {
                for(Unit unit : units) {
                    if(null != unit) {
                        Force parentForce = getCampaign().getForceFor(unit);
                        if(null != parentForce) {
                            getCampaign().removeUnitFromForce(unit);
                            
                        }
                    }
                }
                refreshOrganization();
                refreshPersonnelList();
                refreshScenarioList();
            } else if(command.contains("DEPLOY_UNIT")) {
                int sid = Integer.parseInt(target);
                Scenario scenario = getCampaign().getScenario(sid);
                for(Unit unit : units) {
                    if(null != unit && null != scenario) {
                        scenario.addUnit(unit.getId());
                        unit.setScenarioId(scenario.getId());
                        
                    }
                }
                refreshScenarioList();
                refreshOrganization();
                refreshPersonnelList();
                refreshUnitList();
                refreshServicedUnitList();
            } else if(command.contains("C3I")) {
                //don't set them directly, set the C3i UUIDs and then 
                //run refreshNetworks on the campaign 
                //TODO: is that too costly?
                Vector<String> uuids = new Vector<String>();
                for(Unit unit : units) {
                    if(null == unit.getEntity()) {
                        continue;
                    }
                    uuids.add(unit.getEntity().getC3UUIDAsString());
                }
                for(int pos = 0; pos < uuids.size(); pos++) {
                    for(Unit unit : units) {
                        if(null == unit.getEntity()) {
                            continue;
                        }
                        unit.getEntity().setC3iNextUUIDAsString(pos, uuids.get(pos));
                    }
                }
                getCampaign().refreshNetworks();
                refreshOrganization();
            } else if(command.contains("REMOVE_NETWORK")) {
                getCampaign().removeUnitsFromNetwork(units);
                refreshOrganization();
            } else if(command.contains("DISBAND_NETWORK")) {
                if(null != singleUnit) {
                    getCampaign().disbandNetworkOf(singleUnit);
                }
                refreshOrganization();
            } else if(command.contains("ADD_NETWORK")) {
                getCampaign().addUnitsToNetwork(units, target);
                refreshOrganization();
            } else if(command.contains("ADD_SLAVES")) {
                for(Unit u : units) {
                    u.getEntity().setC3MasterIsUUIDAsString(target);
                }
                getCampaign().refreshNetworks();
                refreshOrganization();
            } else if(command.contains("SET_MM")) {
                for(Unit u : units) {
                    getCampaign().removeUnitsFromC3Master(u);
                    u.getEntity().setC3MasterIsUUIDAsString(u.getEntity().getC3UUIDAsString());
                }
                getCampaign().refreshNetworks();
                refreshOrganization();
            } else if(command.contains("SET_IND_M")) {
                for(Unit u : units) {
                    u.getEntity().setC3MasterIsUUIDAsString(null);
                    u.getEntity().setC3Master(null, true);
                    getCampaign().removeUnitsFromC3Master(u);
                }
                getCampaign().refreshNetworks();
                refreshOrganization();
            } if(command.contains("REMOVE_C3")) {
                for(Unit u : units) {
                    u.getEntity().setC3MasterIsUUIDAsString(null);
                    u.getEntity().setC3Master(null, true);
                }
                getCampaign().refreshNetworks();
                refreshOrganization();
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

        private boolean areAllForcesUndeployed(Vector<Force> forces) {
            for(Force force : forces) {
                if(force.isDeployed()) {
                    return false;
                }
            }
            return true;
        }
        
        private boolean areAllUnitsUndeployed(Vector<Unit> units) {
            for(Unit unit : units) {
                if(unit.isDeployed()) {
                    return false;
                }
            }
            return true;
        }
        
        private boolean doAllUnitsHaveC3i(Vector<Unit> units) {
            for(Unit unit : units) {
                Entity e = unit.getEntity();
                if(null == e) {
                    return false;
                }
                if(!e.hasC3i()) {
                    return false;
                }
            }
            return true;
        }
        
        private boolean areAllUnitsNotC3iNetworked(Vector<Unit> units) {
            for(Unit unit : units) {
                Entity e = unit.getEntity();
                if(null == e) {
                    return false;
                }
                if(e.hasC3i() && e.calculateFreeC3Nodes() < 5) {
                    return false;
                }
            }
            return true;
        }
        
        private boolean areAllUnitsC3iNetworked(Vector<Unit> units) {
            for(Unit unit : units) {
                Entity e = unit.getEntity();
                if(null == e) {
                    return false;
                }
                if(!e.hasC3i() && !e.hasC3()) {
                    return false;
                }
                if(e.hasC3i() && e.calculateFreeC3Nodes() == 5) {
                    return false;
                }
            }
            return true;
        }
        
        private boolean areAllUnitsOnSameC3iNetwork(Vector<Unit> units) {
            String network = null;
            for(Unit unit : units) {
                Entity e = unit.getEntity();
                if(null == e) {
                    return false;
                }
                if(null == e.getC3NetId()) {
                    return false;
                }
                if(null == network) {
                    network = e.getC3NetId();
                } else if(!e.getC3NetId().equals(network)) {
                    return false;
                }
            }
            return true;
        }
        
        private boolean areAllUnitsC3Slaves(Vector<Unit> units) {
            for(Unit unit : units) {
                Entity e = unit.getEntity();
                if(null == e) {
                    return false;
                }
                if(!e.hasC3S()) {
                    return false;
                }
            }
            return true;
        }
        
        private boolean areAllUnitsIndependentC3Masters(Vector<Unit> units) {
            for(Unit unit : units) {
                Entity e = unit.getEntity();
                if(null == e) {
                    return false;
                }
                if(!e.hasC3M()) {
                    return false;
                }
                if(e.hasC3MM()) {
                    return false;
                }
                if(e.C3MasterIs(unit.getEntity())) {
                    return false;
                }
            }
            return true;
        }
        
        private boolean areAllUnitsCompanyLevelMasters(Vector<Unit> units) {
            for(Unit unit : units) {
                Entity e = unit.getEntity();
                if(null == e) {
                    return false;
                }
                if(!e.hasC3M()) {
                    return false;
                }
                if(e.hasC3MM()) {
                    return false;
                }
                if(!e.C3MasterIs(unit.getEntity())) {
                    return false;
                }
            }
            return true;
        
        }
        
        private boolean doAllUnitsHaveC3Master(Vector<Unit> units) {
            for(Unit unit : units) {
                Entity e = unit.getEntity();
                if(null == e) {
                    return false;
                }
                if(!e.hasC3()) {
                    return false;
                }
                if(null == e.getC3Master() || e.C3MasterIs(unit.getEntity())) {
                    return false;
                }
            }
            return true;
        
        }
        
        private void maybeShowPopup(MouseEvent e) {

            if (e.isPopupTrigger()) {
                JPopupMenu popup = new JPopupMenu();
                JMenuItem menuItem;
                JMenu menu;
                JTree tree = (JTree)e.getSource();
                if (tree == null)
                        return;
                //this is a little tricky because we want to 
                //distinguish forces and units, but the user can 
                //select multiple items of both types
                //we will allow multiple selection of either units or forces but
                //not both - if both are selected then default to 
                //unit and deselect all forces
                Vector<Force> forces = new Vector<Force>();
                Vector<Unit> units = new Vector<Unit>();
                Vector<TreePath> uPath = new Vector<TreePath>();
                for(TreePath path : tree.getSelectionPaths()) {
                    Object node = path.getLastPathComponent();
                    if(node instanceof Force) {
                        forces.add((Force)node);
                    }
                    if(node instanceof Unit) {
                        units.add((Unit)node);
                        uPath.add(path);
                    }
                }
                boolean forcesSelected = !forces.isEmpty();
                boolean unitsSelected = !units.isEmpty();
                //if both are selected then we prefer units 
                //and will deselect forces
                if(forcesSelected & unitsSelected) {
                    forcesSelected = false;
                    TreePath[] paths = new TreePath[uPath.size()];
                    int i = 0;
                    for(TreePath p : uPath) {
                        paths[i] = p;
                        i++;
                    }
                    tree.setSelectionPaths(paths);
                }
                boolean multipleSelection = (forcesSelected && forces.size() > 1) || (unitsSelected && units.size() > 1);
                if(forcesSelected) {
                    Force force = forces.get(0);
                    String forceIds = "" + force.getId();
                    for(int i = 1; i<forces.size(); i++) {
                        forceIds += "|" + forces.get(i).getId();
                    }
                    if(!multipleSelection) {
                        menuItem = new JMenuItem("Change Name...");
                        menuItem.setActionCommand("CHANGE_NAME|FORCE|empty|" + forceIds);
                        menuItem.addActionListener(this);
                        menuItem.setEnabled(true);
                        popup.add(menuItem);
                        menuItem = new JMenuItem("Change Description...");
                        menuItem.setActionCommand("CHANGE_DESC|FORCE|empty|" + forceIds);
                        menuItem.addActionListener(this);
                        menuItem.setEnabled(true);
                        popup.add(menuItem);
                        menuItem = new JMenuItem("Add New Force...");
                        menuItem.setActionCommand("ADD_FORCE|FORCE|empty|" + forceIds);
                        menuItem.addActionListener(this);
                        menuItem.setEnabled(true);
                        popup.add(menuItem);
                        menu = new JMenu("Add Unit");
                        //only add units that have commanders
                        for(Unit u : getCampaign().getUnits()) {
                            if(null != u.getCommander()) {
                                Person p = u.getCommander();
                                if(p.isActive() && u.getForceId() < 1) {
                                    menuItem = new JMenuItem(p.getFullTitle() + ", " + u.getName());
                                    menuItem.setActionCommand("ADD_UNIT|FORCE|"  + u.getId() + "|" + forceIds);
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
                        menuItem = new JMenuItem("Change Force Icon...");
                        menuItem.setActionCommand("CHANGE_ICON|FORCE|empty|" + forceIds);
                        menuItem.addActionListener(this);
                        menuItem.setEnabled(true);
                        popup.add(menuItem);
                    }
                    if(areAllForcesUndeployed(forces)) {
                        menu = new JMenu("Deploy Force");
                        JMenu missionMenu;
                        for(Mission m : getCampaign().getMissions()) {
                            if(!m.isActive()) {
                                continue;
                            }
                            missionMenu = new JMenu(m.getName());
                            for(Scenario s : m.getScenarios()) {
                                if(s.isCurrent()) {
                                    menuItem = new JMenuItem(s.getName());
                                    menuItem.setActionCommand("DEPLOY_FORCE|FORCE|"  + s.getId() + "|" + forceIds);
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
                    menuItem.setActionCommand("REMOVE_FORCE|FORCE|empty|" + forceIds);
                    menuItem.addActionListener(this);
                    menuItem.setEnabled(true);
                    popup.add(menuItem);
                }
                else if(unitsSelected) {
                    Unit unit = units.get(0);
                    String unitIds = "" + unit.getId().toString();
                    for(int i = 1; i<units.size(); i++) {
                        unitIds += "|" + units.get(i).getId().toString();
                    }
                    JMenu networkMenu = new JMenu("Network");
                    JMenu availMenu;                    
                    if(areAllUnitsC3Slaves(units)) {
                        availMenu = new JMenu("Slave to");
                        for(String[] network : getCampaign().getAvailableC3MastersForSlaves()) {
                            int nodesFree = Integer.parseInt(network[1]);
                            if(nodesFree >= units.size()) {
                                menuItem = new JMenuItem(network[2] + ": " + network[1] + " nodes free");
                                menuItem.setActionCommand("ADD_SLAVES|UNIT|" + network[0] + "|" + unitIds);
                                menuItem.addActionListener(this);
                                menuItem.setEnabled(true);
                                availMenu.add(menuItem);
                            }
                        }
                        if(availMenu.getItemCount() > 0) {
                            networkMenu.add(availMenu);
                        }
                    }            
                    if(areAllUnitsIndependentC3Masters(units)) {
                        menuItem = new JMenuItem("Set as Company Level Master");
                        menuItem.setActionCommand("SET_MM|UNIT|empty|" + unitIds);
                        menuItem.addActionListener(this);
                        menuItem.setEnabled(true);
                        networkMenu.add(menuItem);
                        availMenu = new JMenu("Slave to");
                        for(String[] network : getCampaign().getAvailableC3MastersForMasters()) {
                            int nodesFree = Integer.parseInt(network[1]);
                            if(nodesFree >= units.size()) {
                                menuItem = new JMenuItem(network[2] + ": " + network[1] + " nodes free");
                                menuItem.setActionCommand("ADD_SLAVES|UNIT|" + network[0] + "|" + unitIds);
                                menuItem.addActionListener(this);
                                menuItem.setEnabled(true);
                                availMenu.add(menuItem);
                            }
                        }
                        if(availMenu.getItemCount() > 0) {
                            networkMenu.add(availMenu);
                        }
                    }
                    if(areAllUnitsCompanyLevelMasters(units)) {
                        menuItem = new JMenuItem("Set as Independent Master");
                        menuItem.setActionCommand("SET_IND_M|UNIT|empty|" + unitIds);
                        menuItem.addActionListener(this);
                        menuItem.setEnabled(true);
                        networkMenu.add(menuItem);
                    }
                    if(doAllUnitsHaveC3Master(units)) {
                        menuItem = new JMenuItem("Remove from network");
                        menuItem.setActionCommand("REMOVE_C3|UNIT|empty|" + unitIds);
                        menuItem.addActionListener(this);
                        menuItem.setEnabled(true);
                        networkMenu.add(menuItem);
                    }
                    if(doAllUnitsHaveC3i(units)) {
                                        
                        if(multipleSelection && areAllUnitsNotC3iNetworked(units) && units.size() < 7) {
                            menuItem = new JMenuItem("Create new C3i network");
                            menuItem.setActionCommand("C3I|UNIT|empty|" + unitIds);
                            menuItem.addActionListener(this);
                            menuItem.setEnabled(true);
                            networkMenu.add(menuItem);
                        }
                        if(areAllUnitsNotC3iNetworked(units)) {
                            availMenu = new JMenu("Add to network");
                            for(String[] network : getCampaign().getAvailableC3iNetworks()) {
                                int nodesFree = Integer.parseInt(network[1]);
                                if(nodesFree >= units.size()) {
                                    menuItem = new JMenuItem(network[0] + ": " + network[1] + " nodes free");
                                    menuItem.setActionCommand("ADD_NETWORK|UNIT|" + network[0] + "|" + unitIds);
                                    menuItem.addActionListener(this);
                                    menuItem.setEnabled(true);
                                    availMenu.add(menuItem);
                                }
                            }
                            if(availMenu.getItemCount() > 0) {
                                networkMenu.add(availMenu);
                            }
                        }
                        if(areAllUnitsC3iNetworked(units)) {
                            menuItem = new JMenuItem("Remove from network");
                            menuItem.setActionCommand("REMOVE_NETWORK|UNIT|empty|" + unitIds);
                            menuItem.addActionListener(this);
                            menuItem.setEnabled(true);
                            networkMenu.add(menuItem);
                            if(areAllUnitsOnSameC3iNetwork(units)) {
                                 menuItem = new JMenuItem("Disband this network");
                                 menuItem.setActionCommand("DISBAND_NETWORK|UNIT|empty|" + unitIds);
                                 menuItem.addActionListener(this);
                                 menuItem.setEnabled(true);
                                 networkMenu.add(menuItem);
                             }
                        }        
                    }
                    if(networkMenu.getItemCount() > 0) {
                        popup.add(networkMenu);
                    }
                    menuItem = new JMenuItem("Remove Unit from TO&E");
                    menuItem.setActionCommand("REMOVE_UNIT|UNIT|empty|" + unitIds);
                    menuItem.addActionListener(this);
                    menuItem.setEnabled(true);
                    popup.add(menuItem);
                    if(areAllUnitsUndeployed(units)) {
                        menu = new JMenu("Deploy Unit");
                        JMenu missionMenu;
                        for(Mission m : getCampaign().getMissions()) {
                            if(!m.isActive()) {
                                continue;
                            }
                            missionMenu = new JMenu(m.getName());
                            for(Scenario s : m.getScenarios()) {
                                if(s.isCurrent()) {
                                    menuItem = new JMenuItem(s.getName());
                                    menuItem.setActionCommand("DEPLOY_UNIT|UNIT|" + s.getId() + "|" + unitIds);
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
            if(sel) {
                setBackground(Color.DARK_GRAY);
                setForeground(Color.WHITE);
            }

            if(value instanceof Unit) {
                String name = "<font color='red'>No Crew</font>";
                String uname = "";
                String c3network = "";
                Unit u = (Unit)value;
                Person pp = u.getCommander();
                if(null != pp) {
                    name = pp.getFullTitle();
                    name += " (" + u.getEntity().getCrew().getGunnery() + "/" + u.getEntity().getCrew().getPiloting() + ")";
                    if(pp.needsFixing()) {
                        name = "<font color='red'>" + name + "</font>";
                    }
                }
                uname = "<i>" + u.getName() + "</i>";
                if(u.isDamaged()) {
                    uname = "<font color='red'>" + uname + "</font>";
                }
                Entity entity = u.getEntity();
                if (entity.hasC3i()) {
                    if (entity.calculateFreeC3Nodes() >= 5) {
                        c3network += Messages.getString("ChatLounge.C3iNone");
                    } else {
                         c3network += c3network += Messages
                                 .getString("ChatLounge.C3iNetwork")
                                 + entity.getC3NetId();
                         if (entity.calculateFreeC3Nodes() > 0) {
                             c3network += Messages.getString("ChatLounge.C3Nodes",
                                     new Object[] { entity.calculateFreeC3Nodes() });
                         }
                     }
                 } else if (entity.hasC3()) {
                     if (entity.C3MasterIs(entity)) {
                         c3network += Messages.getString("ChatLounge.C3Master");
                         c3network += Messages.getString("ChatLounge.C3MNodes",
                                     new Object[] { entity.calculateFreeC3MNodes() });
                         if(entity.hasC3MM()) {
                            c3network += Messages.getString("ChatLounge.C3SNodes",
                                     new Object[] { entity.calculateFreeC3Nodes() });
                         }
                     } else if (!entity.hasC3S()) {
                         c3network += Messages.getString("ChatLounge.C3Master");
                         c3network += Messages.getString("ChatLounge.C3SNodes",
                                     new Object[] { entity.calculateFreeC3Nodes() });
                         // an independent master might also be a slave to a company
                         // master
                         if (entity.getC3Master() != null) {
                             c3network += "<br>" + Messages.getString("ChatLounge.C3Slave") + entity.getC3Master().getShortName(); //$NON-NLS-1$
                         }
                     } else if (entity.getC3Master() != null) {
                         c3network += Messages.getString("ChatLounge.C3Slave") + entity.getC3Master().getShortName(); //$NON-NLS-1$
                     } else {
                         c3network += Messages.getString("ChatLounge.C3None");
                     }
                }
                if(!c3network.isEmpty()) {
                    c3network = "<br><i>" + c3network + "</i>";
                }
                setText("<html>" + name + ", " + uname + c3network + "</html>");
                if(u.isDeployed() && !sel) {
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

            if(Crew.ROOT_PORTRAIT.equals(category)) {
                category = "";
            }

            // Return a null if the player has selected no portrait file.
            if ((null == category) || (null == file) || Crew.PORTRAIT_NONE.equals(file)) {
                file = "default.gif";
            }
            // Try to get the player's portrait file.
            Image portrait = null;
            try {
                portrait = (Image) getPortraits().getItem(category, file);
                if(null != portrait) {
                    portrait = portrait.getScaledInstance(58, -1, Image.SCALE_DEFAULT);
                } else {
                    portrait = (Image) getPortraits().getItem("", "default.gif");
                    if(null != portrait) {
                        portrait = portrait.getScaledInstance(58, -1, Image.SCALE_DEFAULT);
                    }
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

            if(Crew.ROOT_PORTRAIT.equals(category)) {
             category = "";
            }

            // Return a null if the player has selected no portrait file.
            if ((null == category) || (null == file) || Crew.PORTRAIT_NONE.equals(file)) {
             file = "empty.png";
            }

            // Try to get the player's portrait file.
            Image portrait = null;
            try {
             portrait = (Image) getForceIcons().getItem(category, file);
            if(null != portrait) {
                portrait = portrait.getScaledInstance(58, -1, Image.SCALE_DEFAULT);
            } else {
                portrait = (Image) getForceIcons().getItem("", "empty.png");
                if(null != portrait) {
                    portrait = portrait.getScaledInstance(58, -1, Image.SCALE_DEFAULT);
                }
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
                    getCampaign().changeRank(person, rank, true);
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
                    u.remove(selectedPerson, true);
                }
                refreshServicedUnitList();
                refreshUnitList();
                refreshPersonnelList();
                refreshOrganization();
            } else if (command.contains("ADD_PILOT")) {
                UUID selected = UUID.fromString(st.nextToken());
                Unit u = getCampaign().getUnit(selected);
                Unit oldUnit = getCampaign().getUnit(selectedPerson.getUnitId());
                if(null != oldUnit) {
                    oldUnit.remove(selectedPerson, true);
                }
                if(null != u) {
                    u.addPilotOrSoldier(selectedPerson);
                }
                u.resetPilotAndEntity();
                u.runDiagnostic();
                refreshServicedUnitList();
                refreshUnitList();
                refreshPersonnelList();
                refreshOrganization();
            } else if (command.contains("ADD_SOLDIER")) {
                UUID selected = UUID.fromString(st.nextToken());
                Unit u = getCampaign().getUnit(selected);
                if(null != u) {
                    for (Person p : people) {
                        if (u.canTakeMoreGunners()) {
                            Unit oldUnit = getCampaign().getUnit(p.getUnitId());
                            if(null != oldUnit) {
                                oldUnit.remove(p, true);
                            }
                            u.addPilotOrSoldier(p);
                        }
                    }
                }
                u.resetPilotAndEntity();
                u.runDiagnostic();
                refreshServicedUnitList();
                refreshUnitList();
                refreshPersonnelList();
                refreshOrganization();
            } else if (command.contains("ADD_DRIVER")) {
                UUID selected = UUID.fromString(st.nextToken());
                Unit u = getCampaign().getUnit(selected);
                Unit oldUnit = getCampaign().getUnit(selectedPerson.getUnitId());
                if(null != oldUnit) {
                    oldUnit.remove(selectedPerson, true);
                }
                if(null != u) {
                    u.addDriver(selectedPerson);
                }
                u.resetPilotAndEntity();
                u.runDiagnostic();
                refreshServicedUnitList();
                refreshUnitList();
                refreshPersonnelList();
                refreshOrganization();
            } else if (command.contains("ADD_GUNNER")) {
                UUID selected = UUID.fromString(st.nextToken());
                Unit u = getCampaign().getUnit(selected);
                if(null != u) {
                    for (Person p : people) {
                        if (u.canTakeMoreGunners()) {
                            Unit oldUnit = getCampaign().getUnit(p.getUnitId());
                            if(null != oldUnit) {
                                oldUnit.remove(p, true);
                            }
                            u.addGunner(p);
                        }
                    }
                }
                u.resetPilotAndEntity();
                u.runDiagnostic();
                refreshServicedUnitList();
                refreshUnitList();
                refreshPersonnelList();
                refreshOrganization();
            } else if (command.contains("ADD_CREW")) {
                UUID selected = UUID.fromString(st.nextToken());
                Unit u = getCampaign().getUnit(selected);
                if(null != u) {
                    for (Person p : people) {
                        if (u.canTakeMoreVesselCrew()) {
                            Unit oldUnit = getCampaign().getUnit(p.getUnitId());
                            if(null != oldUnit) {
                                oldUnit.remove(p, true);
                            }
                            u.addVesselCrew(p);
                        }
                    }
                }
                u.resetPilotAndEntity();
                u.runDiagnostic();
                refreshServicedUnitList();
                refreshUnitList();
                refreshPersonnelList();
                refreshOrganization();
            } else if (command.contains("ADD_NAV")) {
                UUID selected = UUID.fromString(st.nextToken());
                Unit u = getCampaign().getUnit(selected);
                if(null != u) {
                    for (Person p : people) {
                        if (u.canTakeNavigator()) {
                            Unit oldUnit = getCampaign().getUnit(p.getUnitId());
                            if(null != oldUnit) {
                                oldUnit.remove(p, true);
                            }
                            u.setNavigator(p);
                        }
                    }
                }
                u.resetPilotAndEntity();
                u.runDiagnostic();
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
                        getCampaign());
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
                    person.setDoctorId(null);
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
                for(Person person : people) {
                    person.setXp(i);
                }
                refreshPatientList();
                refreshPersonnelList();
            } else if (command.equalsIgnoreCase("EDGE_SET")) {
                PopupValueChoiceDialog pvcd = new PopupValueChoiceDialog(
                        getFrame(), true, "Edge", selectedPerson.getEdge(), 0, 10);
                pvcd.setVisible(true);
                int i = pvcd.getValue();
                for(Person person : people) {
                    person.setEdge(i);
                    getCampaign().personUpdated(person);
                }
                refreshPersonnelList();
            } else if (command.equalsIgnoreCase("KILL")) {
                KillDialog nkd = new KillDialog(getFrame(), true, new Kill(selectedPerson.getId(), "?", "Bare Hands", getCampaign().getDate()), selectedPerson.getName());
                nkd.setVisible(true);
                if(!nkd.wasCancelled()) {
                    getCampaign().addKill(nkd.getKill());
                }
                refreshPersonnelList();
            } else if (command.equalsIgnoreCase("KILL_LOG")) {
                EditKillLogDialog ekld = new EditKillLogDialog(getFrame(), true, getCampaign(), selectedPerson);
                ekld.setVisible(true);
                refreshPersonnelList();
            } else if (command.equalsIgnoreCase("LOG")) {
                EditPersonnelLogDialog epld = new EditPersonnelLogDialog(getFrame(), true, getCampaign(), selectedPerson);
                epld.setVisible(true);
                refreshPersonnelList();
            } else if (command.equalsIgnoreCase("LOG_SINGLE")) {
                EditLogEntryDialog eeld = new EditLogEntryDialog(frame, true, new LogEntry(getCampaign().getDate(), ""));
                eeld.setVisible(true);
                LogEntry entry = eeld.getEntry();
                if(null != entry) {
                    for(Person person : people) {
                        person.addLogEntry(entry.clone());
                    }
                }
                refreshPersonnelList();
            } else if(command.equalsIgnoreCase("CALLSIGN")) {
                String s = (String)JOptionPane.showInputDialog(
                        frame,
                        "Enter new callsign",
                        "Edit Callsign",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        selectedPerson.getCallsign());
                if(null != s) {
                    selectedPerson.setCallsign(s);
                }
                getCampaign().personUpdated(selectedPerson);
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

        private boolean areAllInfantry(Person[] people) {
            for (Person person : people) {
                if (Person.T_INFANTRY != person.getPrimaryRole()) {
                    return false;
                }
            }
            return true;
        }

        private boolean areAllBattleArmor(Person[] people) {
            for (Person person : people) {
                if (Person.T_BA != person.getPrimaryRole()) {
                    return false;
                }
            }
            return true;
        }

        private boolean areAllVeeGunners(Person[] people) {
            for (Person person : people) {
                if (Person.T_VEE_GUNNER != person.getPrimaryRole()) {
                    return false;
                }
            }
            return true;
        }

        private boolean areAllActive(Person[] people) {
            for (Person person : people) {
                if (!person.isActive()) {
                    return false;
                }
            }
            return true;
        }

        private Person[] getSelectedPeople() {
            Person[] selected = new Person[personnelTable.getSelectedRowCount()];
            int[] rows = personnelTable.getSelectedRows();
            for (int i = 0; i < rows.length; i++) {
                Person person = personModel.getPerson(personnelTable.convertRowIndexToModel(rows[i]));
                selected[i] = person;
            }
            return selected;
        }

        private void maybeShowPopup(MouseEvent e) {
            JPopupMenu popup = new JPopupMenu();

            if (e.isPopupTrigger()) {
                if(personnelTable.getSelectedRowCount() == 0) {
                    return;
                }
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
                Person[] selected = getSelectedPeople();
                if(oneSelected && person.isActive()) {
                        menu = new JMenu("Assign to Unit");
                        JMenu pilotMenu = new JMenu("As Pilot");
                        JMenu crewMenu = new JMenu("As Crewmember");
                        JMenu driverMenu = new JMenu("As Driver");
                        JMenu gunnerMenu = new JMenu("As Gunner");
                        JMenu soldierMenu = new JMenu("As Soldier");
                        JMenu navMenu = new JMenu("As Navigator");
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
                                    cbMenuItem = new JCheckBoxMenuItem(unit.getName());
                                    //TODO: check the box
                                    cbMenuItem.setActionCommand("ADD_PILOT|" + unit.getId());
                                    cbMenuItem.addActionListener(this);
                                    pilotMenu.add(cbMenuItem);
                                }
                            }
                            else if(unit.usesSoldiers()) {
                                if(unit.canTakeMoreGunners() && person.canGun(unit.getEntity())) {
                                    cbMenuItem = new JCheckBoxMenuItem(unit.getName());
                                    //TODO: check the box
                                    cbMenuItem.setActionCommand("ADD_SOLDIER|" + unit.getId());
                                    cbMenuItem.addActionListener(this);
                                    soldierMenu.add(cbMenuItem);
                                }
                            } else {
                                if(unit.canTakeMoreDrivers() && person.canDrive(unit.getEntity())) {
                                    cbMenuItem = new JCheckBoxMenuItem(unit.getName());
                                    //TODO: check the box
                                    cbMenuItem.setActionCommand("ADD_DRIVER|" + unit.getId());
                                    cbMenuItem.addActionListener(this);
                                    if(unit.getEntity() instanceof Aero) {
                                        pilotMenu.add(cbMenuItem);
                                    } else {
                                        driverMenu.add(cbMenuItem);
                                    }
                                }
                                if(unit.canTakeMoreGunners() && person.canGun(unit.getEntity())) {
                                    cbMenuItem = new JCheckBoxMenuItem(unit.getName());
                                    //TODO: check the box
                                    cbMenuItem.setActionCommand("ADD_GUNNER|" + unit.getId());
                                    cbMenuItem.addActionListener(this);
                                    gunnerMenu.add(cbMenuItem);
                                }
                                if(unit.canTakeMoreVesselCrew() && person.hasSkill(SkillType.S_TECH_VESSEL)) {
                                    cbMenuItem = new JCheckBoxMenuItem(unit.getName());
                                    //TODO: check the box
                                    cbMenuItem.setActionCommand("ADD_CREW|" + unit.getId());
                                    cbMenuItem.addActionListener(this);
                                    crewMenu.add(cbMenuItem);
                                }
                                if(unit.canTakeNavigator() && person.hasSkill(SkillType.S_NAV)) {
                                    cbMenuItem = new JCheckBoxMenuItem(unit.getName());
                                    //TODO: check the box
                                    cbMenuItem.setActionCommand("ADD_NAV|" + unit.getId());
                                    cbMenuItem.addActionListener(this);
                                    navMenu.add(cbMenuItem);
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
                        if(crewMenu.getItemCount() > 0) {
                            menu.add(crewMenu);
                            if(crewMenu.getItemCount() > 20) {
                                MenuScroller.setScrollerFor(crewMenu, 20);
                            }
                        }
                        if(navMenu.getItemCount() > 0) {
                            menu.add(navMenu);
                            if(navMenu.getItemCount() > 20) {
                                MenuScroller.setScrollerFor(navMenu, 20);
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
                } else if (areAllActive(selected)) {
                    JMenu soldierMenu = new JMenu("As Soldier");
                    JMenu gunnerMenu = new JMenu("As Gunner");
                    if (areAllInfantry(selected)) {
                        menu = new JMenu("Assign to Unit");
                        for (Unit unit : getCampaign().getUnits()) {
                            if(unit.canTakeMoreGunners() && person.canGun(unit.getEntity())) {
                                cbMenuItem = new JCheckBoxMenuItem(unit.getName());
                                //TODO: check the box
                                cbMenuItem.setActionCommand("ADD_SOLDIER|" + unit.getId());
                                cbMenuItem.addActionListener(this);
                                soldierMenu.add(cbMenuItem);
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
                    } else if (areAllBattleArmor(selected)) {
                        menu = new JMenu("Assign to Unit");
                        for (Unit unit : getCampaign().getUnits()) {
                            if(unit.canTakeMoreGunners() && person.canGun(unit.getEntity())) {
                                cbMenuItem = new JCheckBoxMenuItem(unit.getName());
                                //TODO: check the box
                                cbMenuItem.setActionCommand("ADD_SOLDIER|" + unit.getId());
                                cbMenuItem.addActionListener(this);
                                soldierMenu.add(cbMenuItem);
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
                    } else if (areAllVeeGunners(selected)) {
                        menu = new JMenu("Assign to Unit");
                        for (Unit unit : getCampaign().getUnits()) {
                            if(unit.canTakeMoreGunners() && person.canGun(unit.getEntity())) {
                                cbMenuItem = new JCheckBoxMenuItem(unit.getName());
                                //TODO: check the box
                                cbMenuItem.setActionCommand("ADD_GUNNER|" + unit.getId());
                                cbMenuItem.addActionListener(this);
                                gunnerMenu.add(cbMenuItem);
                            }
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
                if(oneSelected && person.isActive()) {
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
                                    menuItem.setActionCommand("SPECIALIST|" + Crew.SPECIAL_LASER + "|" + cost);
                                    menuItem.addActionListener(this);
                                    menuItem.setEnabled(cost >= 0 && person.getXp() >= cost);
                                    specialistMenu.add(menuItem);
                                    menuItem = new JMenuItem("Missile Specialist" + costDesc);
                                    menuItem.setActionCommand("SPECIALIST|" + Crew.SPECIAL_MISSILE + "|" + cost);
                                    menuItem.addActionListener(this);
                                    menuItem.setEnabled(cost >= 0 && person.getXp() >= cost);
                                    specialistMenu.add(menuItem);
                                    menuItem = new JMenuItem("Ballistic Specialist" + costDesc);
                                    menuItem.setActionCommand("SPECIALIST|" + Crew.SPECIAL_BALLISTIC + "|" + cost);
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
                if(oneSelected && person.isActive()) {
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
                    menuItem = new JMenuItem("Change Callsign...");
                    menuItem.setActionCommand("CALLSIGN");
                    menuItem.addActionListener(this);
                    menuItem.setEnabled(true);
                    popup.add(menuItem);
                    menuItem = new JMenuItem("Edit Personnel Log...");
                    menuItem.setActionCommand("LOG");
                    menuItem.addActionListener(this);
                    menuItem.setEnabled(true);
                    popup.add(menuItem);

                }
                menuItem = new JMenuItem("Add Single Log Entry...");
                menuItem.setActionCommand("LOG_SINGLE");
                menuItem.addActionListener(this);
                menuItem.setEnabled(true);
                popup.add(menuItem);
                if(oneSelected) {
                    menuItem = new JMenuItem("Assign Kill...");
                    menuItem.setActionCommand("KILL");
                    menuItem.addActionListener(this);
                    menuItem.setEnabled(true);
                    popup.add(menuItem);
                    menuItem = new JMenuItem("Edit Kill Log...");
                    menuItem.setActionCommand("KILL_LOG");
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
                menuItem = new JMenuItem("Add XP");
                menuItem.setActionCommand("XP_ADD");
                menuItem.addActionListener(this);
                menuItem.setEnabled(true);
                popup.add(menuItem);
                menu.add(menuItem);
                menuItem = new JMenuItem("Set XP");
                menuItem.setActionCommand("XP_SET");
                menuItem.addActionListener(this);
                menuItem.setEnabled(getCampaign().isGM());
                menu.add(menuItem);
                if(getCampaign().getCampaignOptions().useEdge()) {
                    menuItem = new JMenuItem("Set Edge");
                    menuItem.setActionCommand("EDGE_SET");
                    menuItem.addActionListener(this);
                    menuItem.setEnabled(getCampaign().isGM());
                    menu.add(menuItem);
                }
                if(oneSelected) {
                    menuItem = new JMenuItem("Edit...");
                    menuItem.setActionCommand("EDIT");
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
            if( i >= data.size()) {
                return null;
            }
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
                    String name = u.getName();
                    if(u.getEntity() instanceof Tank) {
                        if(u.isDriver(p)) {
                            name = name + " [Driver]";
                        } else {
                            name = name + " [Gunner]";
                        }
                    }
                    if(u.getEntity() instanceof SmallCraft || u.getEntity() instanceof Jumpship) {
                        if(u.isNavigator(p)) {
                            name = name + " [Navigator]";
                        }
                        else if(u.isDriver(p)) {
                            name =  name + " [Pilot]";
                        }
                        else if(u.isGunner(p)) {
                            name = name + " [Gunner]";
                        } else {
                            name = name + " [Crew]";
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
     * A comparator that sorts techs by skill level
     * @author Jay Lawson
     *
     */
    public class TechSorter implements Comparator<Person> {

        @Override
        public int compare(Person p0, Person p1) {
            return ((Comparable<Integer>)p0.getBestTechLevel()).compareTo(p1.getBestTechLevel());
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
     *   * @author Jay Lawson
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
     * A comparator for unit types
     * @author Jay Lawson
     *
     */
    public class UnitTypeSorter implements Comparator<String> {

        @Override
        public int compare(String s0, String s1) {
            //lets find the weight class integer for each name
            int l0 = 0;
            int l1 = 0;
            for(int i = 0; i <= UnitType.SPACE_STATION; i++) {
                if(UnitType.getTypeDisplayableName(i).equals(s0)) {
                    l0 = i;
                }
                if(UnitType.getTypeDisplayableName(i).equals(s1)) {
                    l1 = i;
                }
            }
            return ((Comparable<Integer>)l1).compareTo(l0);
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

        private final static int COL_QUANTITY   = 0;
        private final static int COL_NAME    =    1;
        private final static int COL_DETAIL   =   2;
        private final static int COL_TECH_BASE  = 3;
        private final static int COL_STATUS   =   4;
        private final static int COL_REPAIR   =   5;
        private final static int COL_COST     =   6;
        private final static int COL_TON       =  7;
        private final static int N_COL          = 8;

        public PartsTableModel() {
            data = new ArrayList<Part>();
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
                case COL_REPAIR:
                    return "Repair Details";
                default:
                    return "?";
            }
        }

        public Object getValueAt(int row, int col) {
            Part part;
            if(data.isEmpty()) {
                return "";
            } else {
                part = (Part)data.get(row);
            }
            DecimalFormat format = new DecimalFormat();
            if(col == COL_NAME) {
                return part.getName();
            }
            if(col == COL_DETAIL) {
                return part.getDetails();
            }
            if(col == COL_COST) {
                return format.format(part.getActualValue());
            }
            if(col == COL_QUANTITY) {
                return part.getQuantity();
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
            if(col == COL_REPAIR) {
                return part.getRepairDesc();
            }
            return "?";
        }

        public Part getPartAt(int row) {
            return ((Part) data.get(row));

        }

         public int getColumnWidth(int c) {
                switch(c) {
                case COL_NAME:
                case COL_DETAIL:
                    return 120;
                case COL_REPAIR:
                    return 150;
                case COL_STATUS:
                    return 40;
                case COL_TECH_BASE:
                    return 20;
                case COL_COST:
                    return 10;
                default:
                    return 3;
                }
            }

            public int getAlignment(int col) {
                switch(col) {
                case COL_COST:
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
            int[] rows = partsTable.getSelectedRows();
            Part[] parts = new Part[rows.length];
            for(int i=0; i<rows.length; i++) {
                parts[i] = partsModel.getPartAt(partsTable.convertRowIndexToModel(rows[i]));
            }
            if (command.equalsIgnoreCase("SELL")) {
                for(Part p : parts) {
                    if(null != p) {
                        getCampaign().sellPart(p, 1);
                    }
                }
                refreshPartsList();
                refreshTaskList();
                refreshAcquireList();
                refreshReport();
                refreshFunds();
                refreshFinancialTransactions();
            } else if (command.equalsIgnoreCase("SELL_ALL")) {
                for(Part p : parts) {
                    if(null != p) {
                        if(p instanceof AmmoStorage) {
                            getCampaign().sellAmmo((AmmoStorage)p, ((AmmoStorage)p).getShots());
                        } else {
                            getCampaign().sellPart(p, p.getQuantity());
                        }
                    }
                }
                refreshPartsList();
                refreshTaskList();
                refreshAcquireList();
                refreshReport();
                refreshFunds();
                refreshFinancialTransactions();
            } else if(command.equalsIgnoreCase("SELL_N")) {
                if(null != selectedPart) {
                    int n = selectedPart.getQuantity();
                    if(selectedPart instanceof AmmoStorage) {
                        n = ((AmmoStorage)selectedPart).getShots();
                    }
                    PopupValueChoiceDialog pvcd = new PopupValueChoiceDialog(
                            getFrame(), true, "Sell How Many " + selectedPart.getName() + "s?", 1, 1, n);
                    pvcd.setVisible(true);
                    int q = pvcd.getValue();
                    getCampaign().sellPart(selectedPart, q);
                }
            } else if (command.equalsIgnoreCase("REMOVE")) {
                for(Part p : parts) {
                    if(null != p) {
                        getCampaign().removePart(p);
                    }
                }
                refreshPartsList();
                refreshTaskList();
                refreshAcquireList();
                refreshReport();
            } else if (command.contains("CHANGE_MODE")) {
                String sel = command.split(":")[1];
                int selected = Integer.parseInt(sel);
                selectedPart.setMode(selected);
                refreshPartsList();
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
        
        public boolean areAllPartsAmmo(Part[] parts) {
            for(Part p : parts) {
                if(!(p instanceof AmmoStorage)) {
                    return false;
                }
            }
            return true;
        }
        
        public boolean areAllPartsNotAmmo(Part[] parts) {
            for(Part p : parts) {
                if(p instanceof AmmoStorage) {
                    return false;
                }
            }
            return true;
        }

        private void maybeShowPopup(MouseEvent e) {
            JPopupMenu popup = new JPopupMenu();
            if (e.isPopupTrigger()) {
                if(partsTable.getSelectedRowCount() == 0) {
                    return;
                }
                int[] rows = partsTable.getSelectedRows();
                JMenuItem menuItem = null;
                JMenu menu = null;
                JCheckBoxMenuItem cbMenuItem = null;
                Part[] parts = new Part[rows.length];
                boolean oneSelected = false;
                for(int i = 0; i < rows.length; i++) {
                    parts[i] = partsModel.getPartAt(partsTable.convertRowIndexToModel(rows[i]));
                }
                Part part = null;
                if(parts.length == 1) {
                    oneSelected = true;
                    part = parts[0];
                }
                // **lets fill the pop up menu**//
                // sell part
                if(getCampaign().getCampaignOptions().canSellParts()) {
                    menu = new JMenu("Sell");
                    if(areAllPartsAmmo(parts)) {
                        menuItem = new JMenuItem("Sell All Ammo of This Type");
                        menuItem.setActionCommand("SELL_ALL");
                        menuItem.addActionListener(this);
                        menu.add(menuItem);
                        if(oneSelected && ((AmmoStorage)part).getShots() > 1) {
                            menuItem = new JMenuItem("Sell # Ammo of This Type...");
                            menuItem.setActionCommand("SELL_N");
                            menuItem.addActionListener(this);
                            menu.add(menuItem);
                        }
                    } else if (areAllPartsNotAmmo(parts)){
                        menuItem = new JMenuItem("Sell Single Part of This Type");
                        menuItem.setActionCommand("SELL");
                        menuItem.addActionListener(this);
                        menu.add(menuItem);
                        menuItem = new JMenuItem("Sell All Parts of This Type");
                        menuItem.setActionCommand("SELL_ALL");
                        menuItem.addActionListener(this);
                        menu.add(menuItem);
                        if(oneSelected && part.getQuantity() > 2) {
                            menuItem = new JMenuItem("Sell # Parts of This Type...");
                            menuItem.setActionCommand("SELL_N");
                            menuItem.addActionListener(this);
                            menu.add(menuItem);
                        }
                    } else {
                        //when both ammo and non-ammo only allow sell all
                        menuItem = new JMenuItem("Sell All Parts of This Type");
                        menuItem.setActionCommand("SELL_ALL");
                        menuItem.addActionListener(this);
                        menu.add(menuItem);
                    }
                    popup.add(menu);
                }
                if(oneSelected && part.needsFixing()) {
                    menu = new JMenu("Repair Mode");
                    for (int i = 0; i < Modes.MODE_N; i++) {
                        cbMenuItem = new JCheckBoxMenuItem(Modes.getModeName(i));
                        if (part.getMode() == i) {
                            cbMenuItem.setSelected(true);
                        } else {
                            cbMenuItem.setActionCommand("CHANGE_MODE:" + i);
                            cbMenuItem.addActionListener(this);
                        }
                        cbMenuItem.setEnabled(!part.isBeingWorkedOn());
                        menu.add(cbMenuItem);
                    }
                    popup.add(menu);
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
        private final static int COL_DATE       = 2;
        private final static int COL_ASSIGN     = 3;
        private final static int N_COL          = 4;

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
                case COL_DATE:
                    return "Date";
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
            if(col == COL_DATE) {
                if(null == scenario.getDate()) {
                    return "-";
                } else {
                    SimpleDateFormat shortDateFormat = new SimpleDateFormat("MM/dd/yyyy");
                    return shortDateFormat.format(scenario.getDate());
                }
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
                if (0 == JOptionPane.showConfirmDialog(null,
                        "Do you really want to delete the scenario?",
                                "Delete Scenario?",
                        JOptionPane.YES_NO_OPTION)) {
                    getCampaign().removeScenario(scenario.getId());
                    refreshScenarioList();
                    refreshOrganization();
                    refreshPersonnelList();
                    refreshUnitList();
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
                // remove scenario
                menuItem = new JMenuItem("Remove Scenario");
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

        public void setTransaction(int row, Transaction transaction) {
            data.set(row, transaction);
        }

        public void deleteTransaction(int row) {
            data.remove(row);
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

    public class FinanceTableMouseAdapter extends MouseInputAdapter implements
            ActionListener {

        public void actionPerformed(ActionEvent action) {
            String command = action.getActionCommand();
            FinanceTableModel financeModel = (FinanceTableModel)financeTable.getModel();
            Transaction transaction = financeModel.getTransaction(financeTable.getSelectedRow());
            int row = financeTable.getSelectedRow();
            if(null == transaction) {
                return;
            }
            if (command.equalsIgnoreCase("DELETE")) {
                getCampaign().addReport(transaction.voidTransaction());
                financeModel.deleteTransaction(row);
                refreshFinancialTransactions();
                refreshReport();
            } else if (command.contains("EDIT")) {
                EditTransactionDialog dialog = new EditTransactionDialog(transaction, getFrame(), true);
                dialog.setVisible(true);
                transaction = dialog.getNewTransaction();
                financeModel.setTransaction(row, transaction);
                getCampaign().addReport(transaction.updateTransaction(dialog.getOldTransaction()));
                refreshFinancialTransactions();
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
                int row = financeTable.getSelectedRow();
                if(row < 0) {
                    return;
                }
                JMenu menu = new JMenu("GM Mode");
                popup.add(menu);

                JMenuItem deleteItem = new JMenuItem("Delete Transaction");
                deleteItem.setActionCommand("DELETE");
                deleteItem.addActionListener(this);
                deleteItem.setEnabled(getCampaign().isGM());
                menu.add(deleteItem);

                JMenuItem editItem = new JMenuItem("Edit Transaction");
                editItem.setActionCommand("EDIT");
                editItem.addActionListener(this);
                editItem.setEnabled(getCampaign().isGM());
                menu.add(editItem);

                popup.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }
    
    /**
     * A table model for displaying active loans
     */
    public class LoanTableModel extends AbstractTableModel {
        private static final long serialVersionUID = 534443424190075264L;

        private final static int COL_DESC      =    0;
        private final static int COL_RATE       =   1;
        private final static int COL_PRINCIPAL  =   2;
        private final static int COL_COLLATERAL =   3;
        private final static int COL_VALUE        = 4;
        private final static int COL_PAYMENT     =  5;
        private final static int COL_SCHEDULE   =   6;
        private final static int COL_NLEFT      =   7;
        private final static int COL_NEXT_PAY   =   8;
        private final static int N_COL            = 9;

        private ArrayList<Loan> data = new ArrayList<Loan>();

        public int getRowCount() {
            return data.size();
        }

        public int getColumnCount() {
            return N_COL;
        }

        @Override
        public String getColumnName(int column) {
            switch(column) {
                case COL_DESC:
                    return "Description";
                case COL_COLLATERAL:
                    return "Collateral";
                case COL_VALUE:
                    return "Remaining";
                case COL_PAYMENT:
                    return "Payment";
                case COL_NLEFT:
                    return "# Left";
                case COL_NEXT_PAY:
                    return "Next Payment Due";
                case COL_RATE:
                    return "APR";
                case COL_SCHEDULE:
                    return "Schedule";
                case COL_PRINCIPAL:
                    return "Principal";
                default:
                    return "?";
            }
        }

        public Object getValueAt(int row, int col) {
            Loan loan = data.get(row);           
            if(col == COL_DESC) {
                return loan.getDescription();
            }
            if(col == COL_COLLATERAL) {
                return DecimalFormat.getInstance().format(loan.getCollateralAmount());
            }
            if(col == COL_VALUE) {
                return DecimalFormat.getInstance().format(loan.getRemainingValue());
            }
            if(col == COL_PAYMENT) {
                return DecimalFormat.getInstance().format(loan.getPaymentAmount());
            }
            if(col == COL_PRINCIPAL) {
                return DecimalFormat.getInstance().format(loan.getPrincipal());
            }
            if(col == COL_SCHEDULE) {
                return Loan.getScheduleName(loan.getPaymentSchedule());
            }
            if(col == COL_RATE) {
                return loan.getInterestRate() + "%";
            }
            if(col == COL_NLEFT) {
                return loan.getRemainingPayments();
            }
            
            if(col == COL_NEXT_PAY) {
                return DateFormat.getDateInstance().format(loan.getNextPayDate());
            }
            return "?";
        }

        public int getColumnWidth(int c) {
            switch(c) {
            case COL_DESC:
                return 200;
            case COL_RATE:
                return 20;
            default:
                return 50;
            }
        }

        public int getAlignment(int col) {
            switch(col) {
            case COL_NLEFT:
            case COL_RATE:
                return SwingConstants.CENTER;
            case COL_DESC:
                return SwingConstants.LEFT;
            default:
                return SwingConstants.RIGHT;
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
        public void setData(ArrayList<Loan> loans) {
            data = loans;
            fireTableDataChanged();
        }

        public Loan getLoan(int row) {
            return data.get(row);
        }

        public LoanTableModel.Renderer getRenderer() {
            return new LoanTableModel.Renderer();
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
                Loan loan = getLoan(table.convertRowIndexToModel(row));
                
                setForeground(Color.BLACK);
                if (isSelected) {
                    setBackground(Color.DARK_GRAY);
                    setForeground(Color.WHITE);
                } else {
                    if(loan.isOverdue()) {
                        setBackground(Color.RED);
                    } else {
                        setBackground(Color.WHITE);
                    }
                }

                return this;
            }
        }
    }
    
    public class LoanTableMouseAdapter extends MouseInputAdapter implements ActionListener {

        public void actionPerformed(ActionEvent action) {
            String command = action.getActionCommand();
            int row = loanTable.getSelectedRow();
            if(row < 0) {
                return;
            }
            Loan selectedLoan = loanModel.getLoan(partsTable.convertRowIndexToModel(row));
            if(null == selectedLoan) {
                return;
            }
            if (command.equalsIgnoreCase("DEFAULT")) {
                if (0 == JOptionPane
                        .showConfirmDialog(
                                null,
                                "Defaulting on this loan will affect your unit rating the same as a contract breach.\nDo you wish to proceed?",
                                "Default on " + selectedLoan.getDescription() + "?", JOptionPane.YES_NO_OPTION)) {
                    getCampaign().getFinances().defaultOnLoan(selectedLoan);
                    refreshFinancialTransactions();
                }
            } else if (command.equalsIgnoreCase("COLLATERAL")) {
                //IMPLEMENTME
            } else if(command.equalsIgnoreCase("PAY_BALANCE")) {
                getCampaign().payOffLoan(selectedLoan);
                refreshFinancialTransactions();
                refreshReport();
            } else if (command.equalsIgnoreCase("REMOVE")) {
                getCampaign().getFinances().removeLoan(selectedLoan);
                refreshFinancialTransactions();
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
                if(loanTable.getSelectedRowCount() == 0) {
                    return;
                }
                int row = loanTable.getSelectedRow();
                Loan loan = loanModel.getLoan(partsTable.convertRowIndexToModel(row));
                JMenuItem menuItem = null;
                JMenu menu = null;
                // **lets fill the pop up menu**//
                menuItem = new JMenuItem("Pay Off Full Balance (" + DecimalFormat.getInstance().format(loan.getRemainingValue()) + ")");
                menuItem.setActionCommand("PAY_BALANCE");
                menuItem.setEnabled(getCampaign().getFunds() >= loan.getRemainingValue());
                menuItem.addActionListener(this);
                popup.add(menuItem);
                menuItem = new JMenuItem("Default on This Loan");
                menuItem.setActionCommand("DEFAULT");
                menuItem.addActionListener(this);
                popup.add(menuItem);
                menuItem = new JMenuItem("Pay Off Collateral");
                menuItem.setActionCommand("COLLATERAL");
                menuItem.addActionListener(this);
                popup.add(menuItem);
                // GM mode
                menu = new JMenu("GM Mode");
                // remove part
                menuItem = new JMenuItem("Remove Loan");
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
     * A table Model for displaying information about units
     * @author Jay lawson
     */
    public class UnitTableModel extends AbstractTableModel {

        private static final long serialVersionUID = -5207167419079014157L;

        private final static int COL_NAME    =    0;
        private final static int COL_TYPE    =    1;
        private final static int COL_WCLASS    =  2;
        private final static int COL_TECH     =   3;
        private final static int COL_WEIGHT =     4;
        private final static int COL_COST    =    5;
        private final static int COL_MAINTAIN  =  6;
        private final static int COL_QUALITY  =   7;
        private final static int COL_STATUS   =   8;
        private final static int COL_PILOT    =   9;
        private final static int COL_CREW     =   10;
        private final static int COL_DEPLOY   =   11;
        private final static int COL_BV        =  12;
        private final static int COL_REPAIR  =    13;
        private final static int COL_PARTS    =   14;
        private final static int COL_QUIRKS   =   15;
        private final static int N_COL =          16;

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
                case COL_NAME:
                    return "Name";
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
            case COL_WCLASS:
            case COL_TYPE:
                return 50;
            case COL_COST:
            case COL_STATUS:
                return 80;
            case COL_PILOT:
            case COL_TECH:
            case COL_NAME:
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
            case COL_STATUS:
                if(u.isRefitting()) {
                    return u.getRefit().getDesc();
                }
                return null;
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
            if(i >= data.size()) {
                return null;
            }
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
            if(col == COL_NAME) {
                return u.getName();
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
                    } else if (u.getEntity() instanceof Infantry
                            && u.getActiveCrew().size() < u.getFullCrewSize()) {
                        setBackground(Color.RED);
                    }
                    else {
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
                        unit.remove(p, true);
                    }
                }
                refreshServicedUnitList();
                refreshUnitList();
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
                        long sellValue = unit.getSellValue();
                        NumberFormat numberFormat = NumberFormat.getNumberInstance();
                        String text = numberFormat.format(sellValue) + " "
                                + (sellValue != 0 ? "CBills" : "CBill");
                        if (0 == JOptionPane.showConfirmDialog(null,
                                "Do you really want to sell "
                                        + unit.getName()
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
                                    + unit.getName()
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
                                        + unit.getName()
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
            } else if (command.equalsIgnoreCase("DISBAND")) {
                for (Unit unit : units) {
                    if (!unit.isDeployed()) {
                        if (0 == JOptionPane.showConfirmDialog(null,
                                "Do you really want to disband this unit "
                                        + unit.getName()
                                        + "?", "Disband Unit?",
                                JOptionPane.YES_NO_OPTION)) {
                            Vector<Part> parts = new Vector<Part>();
                            for(Part p : unit.getParts()) {
                                parts.add(p);
                            }
                            for(Part p : parts) {
                                p.remove(true);
                            }
                            getCampaign().removeUnit(unit.getId());
                        }
                    }
                }
                refreshServicedUnitList();
                refreshUnitList();
                refreshPartsList();
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
            } else if(command.contains("HIRE_FULL")) {
                for(Unit unit : units) {
                    getCampaign().hirePersonnelFor(unit.getId());
                }
                refreshServicedUnitList();
                refreshUnitList();
                refreshPersonnelList();
                refreshOrganization();
                refreshReport();
            } else if (command.contains("CUSTOMIZE")
                    && !command.contains("CANCEL")) {
                panMekLab.loadUnit(selectedUnit);
                tabMain.setSelectedIndex(8);
            } else if (command.contains("CANCEL_CUSTOMIZE")) {
                if(selectedUnit.isRefitting()) {
                    selectedUnit.getRefit().cancel();
                }
                refreshServicedUnitList();
                refreshUnitList();
                refreshPartsList();
            } else if(command.contains("REFIT_KIT")) {
                ChooseRefitDialog crd = new ChooseRefitDialog(getFrame(), true, getCampaign(), selectedUnit, getCampaignGUI());
                crd.setVisible(true);
            } else if(command.contains("CHANGE_HISTORY")) {
                if(null != selectedUnit) {
                    TextAreaDialog tad = new TextAreaDialog(getFrame(), true,
                            "Edit Unit History",
                            selectedUnit.getHistory());
                    tad.setVisible(true);
                    if(tad.wasChanged()) {
                        selectedUnit.setHistory(tad.getText());
                        refreshUnitList();
                    }
                }
            }
            else if (command.contains("INDI_CAMO")) {
                String category = selectedUnit.getCamoCategory();
                if ("".equals(category)) {
                    category = Player.ROOT_CAMO;
                }
                CamoChoiceDialog ccd = new CamoChoiceDialog(getFrame(), true, category, selectedUnit.getCamoFileName(),
                        getCampaign().getColorIndex(), getCamos());
                ccd.setLocationRelativeTo(getFrame());
                ccd.setVisible(true);

                if (ccd.clickedSelect() == true) {
                    selectedUnit.getEntity().setCamoCategory(ccd.getCategory());
                    selectedUnit.getEntity().setCamoFileName(ccd.getFileName());

                    refreshForceView();
                    refreshUnitView();
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
                        for (AmmoType atype : Utilities.getMunitionsFor(unit.getEntity(), curType, getCampaign().getCampaignOptions().getTechLevel())) {
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
                //Select bombs.
                if (oneSelected && (unit.getEntity() instanceof Aero)) {
                    JMenuItem bombItem = new JMenuItem("Select Bombs");
                    bombItem.addActionListener(new BombMenuListener(unit));
                    popup.add(bombItem);
                }
                // Salvage / Repair
                if(oneSelected && !(unit.getEntity() instanceof Infantry && !(unit.getEntity() instanceof BattleArmor))) {
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
                if(oneSelected && unit.getEntity() instanceof Infantry && !(unit.getEntity() instanceof BattleArmor)) {
                    menuItem = new JMenuItem("Disband");
                    menuItem.setActionCommand("DISBAND");
                    menuItem.addActionListener(this);
                    menuItem.setEnabled(!unit.isDeployed());
                    popup.add(menuItem);
                }
                // Customize
                if(oneSelected && (unit.getEntity() instanceof Mech 
                        || unit.getEntity() instanceof Tank 
                        || (unit.getEntity() instanceof Infantry && !(unit.getEntity() instanceof BattleArmor)))) {
                    menu = new JMenu("Customize");
                    menuItem = new JMenuItem("Choose Refit Kit...");
                    menuItem.setActionCommand("REFIT_KIT");
                    menuItem.addActionListener(this);
                    menuItem.setEnabled(!unit.isRefitting()
                            && (unit.getEntity() instanceof megamek.common.Mech ||
                                    unit.getEntity() instanceof megamek.common.Tank
                                    || (unit.getEntity() instanceof Infantry && !(unit.getEntity() instanceof BattleArmor))));
                    menu.add(menuItem);
                    menuItem = new JMenuItem("Customize in Mek Lab...");
                    menuItem.setActionCommand("CUSTOMIZE");
                    menuItem.addActionListener(this);
                    menuItem.setEnabled(!unit.isRefitting()
                            && (unit.getEntity() instanceof megamek.common.Mech ||
                                    unit.getEntity() instanceof megamek.common.Tank
                                    || (unit.getEntity() instanceof Infantry && !(unit.getEntity() instanceof BattleArmor))));
                    menu.add(menuItem);
                    if (unit.isRefitting()) {
                        menuItem = new JMenuItem("Cancel Customization");
                        menuItem.setActionCommand("CANCEL_CUSTOMIZE");
                        menuItem.addActionListener(this);
                        menuItem.setEnabled(true);
                        menu.add(menuItem);
                    }
                    menu.setEnabled(!unit.isDeployed() && unit.isRepairable());
                    menuItem = new JMenuItem(resourceMap.getString("customizeMenu.individualCamo.text"));
                    menuItem.setActionCommand("INDI_CAMO");
                    menuItem.addActionListener(this);
                    menuItem.setEnabled(!unit.isDeployed());
                    menu.add(menuItem);
                    popup.add(menu);

                }
                //fill with personnel
                if(unit.getCrew().size() < unit.getFullCrewSize()) {
                    menuItem = new JMenuItem("Hire full complement");
                    menuItem.setActionCommand("HIRE_FULL");
                    menuItem.addActionListener(this);
                    menuItem.setEnabled(!unit.isDeployed());
                    popup.add(menuItem);
                }
                if(oneSelected && !getCampaign().isCustom(unit)) {
                    menuItem = new JMenuItem("Tag as a custom unit");
                    menuItem.setActionCommand("TAG_CUSTOM");
                    menuItem.addActionListener(this);
                    menuItem.setEnabled(true);
                    popup.add(menuItem);
                }
                if(oneSelected && getCampaign().getCampaignOptions().useQuirks()) {
                    JMenuItem quirkItem = new JMenuItem("Edit Quirks");
                    quirkItem.addActionListener(new QuirkMenuListener(unit));
                    popup.add(quirkItem);
                }
                if(oneSelected) {
                    menuItem = new JMenuItem("Edit Unit History...");
                    menuItem.setActionCommand("CHANGE_HISTORY");
                    menuItem.addActionListener(this);
                    menuItem.setEnabled(true);
                    popup.add(menuItem);
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

            if(Crew.ROOT_PORTRAIT.equals(category)) {
                category = "";
            }

            // Return a null if the player has selected no portrait file.
            if ((null == category) || (null == file) || Crew.PORTRAIT_NONE.equals(file)) {
                file = "default.gif";
            }

            // Try to get the player's portrait file.
            Image portrait = null;
            try {
                portrait = (Image) portraits.getItem(category, file);
                if(null != portrait) {
                    portrait = portrait.getScaledInstance(60, -1, Image.SCALE_DEFAULT);
                } else {
                    portrait = (Image) portraits.getItem("", "default.gif");
                    if(null != portrait) {
                        portrait = portrait.getScaledInstance(60, -1, Image.SCALE_DEFAULT);
                    }
                }
                this.setImage(portrait);
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

    public class OrgTreeTransferHandler extends TransferHandler {

        /**
         *
         */
        private static final long serialVersionUID = -1276891849078287710L;

        @Override
        public int getSourceActions(JComponent c) {
            return MOVE;
        }

        @Override
        public void exportDone(JComponent c, Transferable t, int action) {
            if (action == MOVE) {
                refreshOrganization();
            }
        }

        @Override
        protected Transferable createTransferable(JComponent c) {
            JTree tree = (JTree)c;
            Object node = tree.getLastSelectedPathComponent();
            if(node instanceof Unit) {
                return new StringSelection("UNIT|" + ((Unit)node).getId().toString());
            }
            else if(node instanceof Force) {
                return new StringSelection("FORCE|" + Integer.toString(((Force)node).getId()));
            }
            return null;
        }

        public boolean canImport(TransferHandler.TransferSupport support) {
            if(!support.isDrop()) {
                return false;
            }
            support.setShowDropLocation(true);
            if (!support.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                return false;
            }
            // Extract transfer data.
            Unit unit = null;
            Force force = null;
            Transferable t = support.getTransferable();
            try {
                StringTokenizer st = new StringTokenizer((String) t.getTransferData(DataFlavor.stringFlavor), "|");
                String type = st.nextToken();
                String id = st.nextToken();
                if(type.equals("UNIT")) {
                    unit = getCampaign().getUnit(UUID.fromString(id));
                }
                if(type.equals("FORCE")) {
                    force = getCampaign().getForce(Integer.parseInt(id));
                }
            } catch(UnsupportedFlavorException ufe) {
                System.out.println("UnsupportedFlavor: " + ufe.getMessage());
            } catch(java.io.IOException ioe) {
                System.out.println("I/O error: " + ioe.getMessage());
            }
            // Do not allow a drop on the drag source selections.
            JTree.DropLocation dl =
                    (JTree.DropLocation)support.getDropLocation();
            JTree tree = (JTree)support.getComponent();
            int dropRow = tree.getRowForPath(dl.getPath());
            int[] selRows = tree.getSelectionRows();
            for(int i = 0; i < selRows.length; i++) {
                if(selRows[i] == dropRow) {
                    return false;
                }
            }
            TreePath dest = dl.getPath();
            Object parent = dest.getLastPathComponent();
            Force superForce = null;
            if(parent instanceof Force) {
                superForce = (Force)parent;
            }
            else if(parent instanceof Unit) {
                superForce = getCampaign().getForce(((Unit)parent).getForceId());
            }
            if(null != force && null != superForce && force.isAncestorOf(superForce)) {
                return false;
            }
                        
            return parent instanceof Force || parent instanceof Unit;
        }

        public boolean importData(TransferHandler.TransferSupport support) {
            if(!canImport(support)) {
                return false;
            }
            // Extract transfer data.
            Unit unit = null;
            Force force = null;
            Transferable t = support.getTransferable();
            try {
                StringTokenizer st = new StringTokenizer((String) t.getTransferData(DataFlavor.stringFlavor), "|");
                String type = st.nextToken();
                String id = st.nextToken();
                if(type.equals("UNIT")) {
                    unit = getCampaign().getUnit(UUID.fromString(id));
                }
                if(type.equals("FORCE")) {
                    force = getCampaign().getForce(Integer.parseInt(id));
                }
            } catch(UnsupportedFlavorException ufe) {
                System.out.println("UnsupportedFlavor: " + ufe.getMessage());
            } catch(java.io.IOException ioe) {
                System.out.println("I/O error: " + ioe.getMessage());
            }  
            // Get drop location info.  
            JTree.DropLocation dl = (JTree.DropLocation)support.getDropLocation();  
            TreePath dest = dl.getPath();  
            Force superForce = null;
            Object parent = dest.getLastPathComponent();
            if(parent instanceof Force) {
                superForce = (Force)parent;
            }
            else if(parent instanceof Unit) {
                superForce = getCampaign().getForce(((Unit)parent).getForceId());
            }
            if(null != superForce) {
                if(null != unit) {
                    getCampaign().addUnitToForce(unit, superForce.getId());
                    return true;
                }
                if(null != force) {
                    getCampaign().moveForce(force, superForce);
                    return true;
                }
            }
            return false;  
        }  
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable DocTable;
    private javax.swing.JTable partsTable;
    private javax.swing.JTable TaskTable;
    private javax.swing.JTable AcquisitionTable;
    private javax.swing.JTable TechTable;
    private javax.swing.JTable whTechTable;
    private javax.swing.JTable servicedUnitTable;
    private javax.swing.JTable unitTable;
    private javax.swing.JTable personnelTable;
    private javax.swing.JTable scenarioTable;
    private javax.swing.JTable financeTable;
    private javax.swing.JTable loanTable;
    private javax.swing.JMenuItem addFunds;
    private javax.swing.JButton btnAdvanceDay;
    private javax.swing.JButton btnAssignDoc;
    private javax.swing.JButton btnUnassignDoc;
    private javax.swing.JButton btnDoTask;
    private javax.swing.JButton btnDoTaskWarehouse;
    private javax.swing.JToggleButton btnGMMode;
    private javax.swing.JToggleButton btnOvertime;
    private javax.swing.JToggleButton btnShowAllTechs;
    private javax.swing.JToggleButton btnShowAllTechsWarehouse;
    private javax.swing.JScrollPane scrTextTarget;
    private javax.swing.JScrollPane scrollPartsTable;
    private javax.swing.JLabel lblTarget;
    private javax.swing.JLabel lblTargetNum;
    private javax.swing.JLabel lblTargetNumWarehouse;
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
    private javax.swing.JMenuItem miHireBulk;
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
    private javax.swing.JMenuItem miShoppingList;
    private javax.swing.JMenuItem miGetLoan;
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
    private javax.swing.JScrollPane scrollUnassignedPatient;
    private javax.swing.JScrollPane scrollTaskTable;
    private javax.swing.JScrollPane scrollAcquisitionTable;
    private javax.swing.JScrollPane scrollTechTable;
    private javax.swing.JScrollPane scrollWhTechTable;
    private javax.swing.JScrollPane scrollServicedUnitTable;
    private javax.swing.JScrollPane scrollServicedUnitView;
    private javax.swing.JScrollPane scrollPersonnelTable;
    private javax.swing.JScrollPane scrollScenarioTable;
    private javax.swing.JScrollPane scrollUnitTable;
    private javax.swing.JScrollPane scrollFinanceTable;
    private javax.swing.JScrollPane scrollLoanTable;
    private javax.swing.JTextPane txtServicedUnitView;
    private javax.swing.JSplitPane splitServicedUnits;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JTabbedPane tabMain;
    private javax.swing.JTabbedPane tabTasks;
    private javax.swing.JTextArea textTarget;
    private javax.swing.JTextArea textTargetWarehouse;
    private javax.swing.JLabel astechPoolLabel;
    private javax.swing.JLabel astechPoolLabelWarehouse;
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
    private javax.swing.JButton btnDeleteMission;
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
    private JList listAssignedPatient;
    private JList listUnassignedPatient;
    // End of variables declaration//GEN-END:variables

    private javax.swing.JLabel lblRating;
    private javax.swing.JLabel lblFunds;
    private javax.swing.JLabel lblTempAstechs;
    private javax.swing.JLabel lblTempMedics;
    

    private JDialog aboutBox;

    /**
     * Private inner class to handle opening the {@link QuirksDialog} with the appropriate arguments.
     */
    private class QuirkMenuListener implements ActionListener {
        
        private Unit unit;

        /**
         * Creates the listener for the Quirks menu item.
         * @param unit The {@link Unit} being edited.
         */
        public QuirkMenuListener(Unit unit) {
            this.unit = unit;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            //Open the dialog.
            QuirksDialog dialog = new QuirksDialog(unit.getEntity(), frame);
            dialog.setVisible(true);
        }
    }

    /**
     * Private inner class to handle opening the {@link QuirksDialog} with the appropriate arguments.
     */
    private class BombMenuListener implements ActionListener {

        private Unit unit;

        /**
         * Creates the listener for the Quirks menu item.
         * @param unit The {@link Unit} being edited.
         */
        public BombMenuListener(Unit unit) {
            this.unit = unit;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            //Open the dialog.
            BombsDialog dialog = new BombsDialog((Aero)unit.getEntity(), getCampaign(), frame);
            dialog.setVisible(true);
        }
    }
}
