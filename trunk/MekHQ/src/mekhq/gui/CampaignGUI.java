/*
 * CampaignGUI.java
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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
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
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.UUID;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.JTree;
import javax.swing.KeyStroke;
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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import megamek.common.Aero;
import megamek.common.AmmoType;
import megamek.common.BattleArmor;
import megamek.common.Crew;
import megamek.common.Entity;
import megamek.common.EntityListFile;
import megamek.common.GunEmplacement;
import megamek.common.Infantry;
import megamek.common.Mech;
import megamek.common.MechSummaryCache;
import megamek.common.MechView;
import megamek.common.MiscType;
import megamek.common.Mounted;
import megamek.common.Player;
import megamek.common.Tank;
import megamek.common.TargetRoll;
import megamek.common.UnitType;
import megamek.common.WeaponType;
import megamek.common.XMLStreamParser;
import megamek.common.loaders.BLKFile;
import megamek.common.loaders.EntityLoadingException;
import megamek.common.options.IOption;
import megamek.common.options.PilotOptions;
import megameklab.com.util.UnitPrintManager;
import mekhq.IconPackage;
import mekhq.MekHQ;
import mekhq.Utilities;
import mekhq.Version;
import mekhq.campaign.Campaign;
import mekhq.campaign.JumpPath;
import mekhq.campaign.Kill;
import mekhq.campaign.LogEntry;
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
import mekhq.campaign.parts.BaArmor;
import mekhq.campaign.parts.EnginePart;
import mekhq.campaign.parts.MekActuator;
import mekhq.campaign.parts.MekGyro;
import mekhq.campaign.parts.MekLifeSupport;
import mekhq.campaign.parts.MekLocation;
import mekhq.campaign.parts.MekSensor;
import mekhq.campaign.parts.Part;
import mekhq.campaign.parts.ProtomekArmor;
import mekhq.campaign.parts.Refit;
import mekhq.campaign.parts.TankLocation;
import mekhq.campaign.parts.equipment.AmmoBin;
import mekhq.campaign.parts.equipment.EquipmentPart;
import mekhq.campaign.personnel.Injury;
import mekhq.campaign.personnel.Person;
import mekhq.campaign.personnel.Skill;
import mekhq.campaign.personnel.SkillType;
import mekhq.campaign.report.HangarReport;
import mekhq.campaign.report.PersonnelReport;
import mekhq.campaign.report.RatingReport;
import mekhq.campaign.report.Report;
import mekhq.campaign.report.TransportReport;
import mekhq.campaign.work.IAcquisitionWork;
import mekhq.campaign.work.Modes;
import mekhq.gui.ReportHyperlinkListener;
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
import mekhq.gui.dialog.EditKillLogDialog;
import mekhq.gui.dialog.EditLogEntryDialog;
import mekhq.gui.dialog.EditPersonnelInjuriesDialog;
import mekhq.gui.dialog.EditPersonnelLogDialog;
import mekhq.gui.dialog.EditTransactionDialog;
import mekhq.gui.dialog.GameOptionsDialog;
import mekhq.gui.dialog.HireBulkPersonnelDialog;
import mekhq.gui.dialog.KillDialog;
import mekhq.gui.dialog.MaintenanceReportDialog;
import mekhq.gui.dialog.MekHQAboutBox;
import mekhq.gui.dialog.MercRosterDialog;
import mekhq.gui.dialog.MissionTypeDialog;
import mekhq.gui.dialog.NewLoanDialog;
import mekhq.gui.dialog.NewRecruitDialog;
import mekhq.gui.dialog.NewsReportDialog;
import mekhq.gui.dialog.PartsStoreDialog;
import mekhq.gui.dialog.PayCollateralDialog;
import mekhq.gui.dialog.PersonnelMarketDialog;
import mekhq.gui.dialog.PopupValueChoiceDialog;
import mekhq.gui.dialog.PortraitChoiceDialog;
import mekhq.gui.dialog.QuirksDialog;
import mekhq.gui.dialog.RefitNameDialog;
import mekhq.gui.dialog.ReportDialog;
import mekhq.gui.dialog.ResolveScenarioWizardDialog;
import mekhq.gui.dialog.TextAreaDialog;
import mekhq.gui.dialog.UnitSelectorDialog;
import mekhq.gui.model.AcquisitionTableModel;
import mekhq.gui.model.DocTableModel;
import mekhq.gui.model.FinanceTableModel;
import mekhq.gui.model.LoanTableModel;
import mekhq.gui.model.OrgTreeModel;
import mekhq.gui.model.PartsTableModel;
import mekhq.gui.model.PatientTableModel;
import mekhq.gui.model.PersonnelTableModel;
import mekhq.gui.model.ProcurementTableModel;
import mekhq.gui.model.ScenarioTableModel;
import mekhq.gui.model.ServicedUnitTableModel;
import mekhq.gui.model.TaskTableModel;
import mekhq.gui.model.TechTableModel;
import mekhq.gui.model.UnitTableModel;
import mekhq.gui.XTableColumnModel;
import mekhq.gui.sorter.BonusSorter;
import mekhq.gui.sorter.FormattedNumberSorter;
import mekhq.gui.sorter.LevelSorter;
import mekhq.gui.sorter.RankSorter;
import mekhq.gui.sorter.TargetSorter;
import mekhq.gui.sorter.UnitStatusSorter;
import mekhq.gui.sorter.UnitTypeSorter;
import mekhq.gui.sorter.WeightClassSorter;
import mekhq.gui.view.ContractViewPanel;
import mekhq.gui.view.ForceViewPanel;
import mekhq.gui.view.JumpPathViewPanel;
import mekhq.gui.view.MissionViewPanel;
import mekhq.gui.view.PersonViewPanel;
import mekhq.gui.view.PlanetViewPanel;
import mekhq.gui.view.ScenarioViewPanel;
import mekhq.gui.view.UnitViewPanel;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
    public static final int PG_ACTIVE =  0;
    public static final int PG_COMBAT =  1;
    public static final int PG_SUPPORT = 2;
    public static final int PG_MW =      3;
    public static final int PG_CREW =    4;
    public static final int PG_PILOT =   5;
    public static final int PG_CPILOT =  6;
    public static final int PG_PROTO =   7;
    public static final int PG_BA =      8;
    public static final int PG_SOLDIER = 9;
    public static final int PG_VESSEL =  10;
    public static final int PG_TECH =    11;
    public static final int PG_DOC =     12;
    public static final int PG_ADMIN =   13;
    public static final int PG_RETIRE =  14;
    public static final int PG_MIA =     15;
    public static final int PG_KIA =     16;
    public static final int PG_NUM =     17;

    //parts filter groups
    private static final int SG_ALL      = 0;
    private static final int SG_ARMOR    = 1;
    private static final int SG_SYSTEM   = 2;
    private static final int SG_EQUIP    = 3;
    private static final int SG_LOC      = 4;
    private static final int SG_WEAP     = 5;
    private static final int SG_AMMO     = 6;
    private static final int SG_AMMO_BIN = 7;
    private static final int SG_MISC     = 8;
    private static final int SG_ENGINE   = 9;
    private static final int SG_GYRO     = 10;
    private static final int SG_ACT      = 11;
    private static final int SG_NUM      = 12;
    
    //parts views
    private static final int SV_ALL			= 0;
    private static final int SV_IN_TRANSIT	= 1;
    private static final int SV_UNDAMAGED	= 2;
    private static final int SV_DAMAGED 	= 3;
    private static final int SV_NUM			= 4;

    //personnel views
    private static final int PV_GRAPHIC = 0;
    private static final int PV_GENERAL = 1;
    private static final int PV_PILOT   = 2;
    private static final int PV_INF     = 3;
    private static final int PV_TACTIC  = 4;
    private static final int PV_TECH    = 5;
    private static final int PV_ADMIN   = 6;
    private static final int PV_FLUFF   = 7;
    private static final int PV_NUM     = 8;

    //unit views
    private static final int UV_GRAPHIC = 0;
    private static final int UV_GENERAL = 1;
    private static final int UV_DETAILS = 2;
    private static final int UV_STATUS  = 3;
    private static final int UV_NUM     = 4;

    private JFrame frame;

    private MekHQ app;

    private ResourceBundle resourceMap;

    /*for the main panel*/
    private JPanel mainPanel;
    private JTabbedPane tabMain;
    private JButton btnAdvanceDay;
    private JTextPane txtPaneReport;
    private JLabel lblLocation;
    
    /*For the menu bar*/
    private JMenuBar menuBar;
    private JMenu menuThemes;
    
    /*For the TO&E tab*/
    private JPanel panOrganization;
    private JTree orgTree;
    private JSplitPane splitOrg;
    private JScrollPane scrollForceView;
    
    /*For the briefing room tab*/
    private JPanel panBriefing;
    private JPanel panScenario;
    private JSplitPane splitBrief;
    private JSplitPane splitMission;
    private JTable scenarioTable;
    private JComboBox choiceMission;
    private JScrollPane scrollMissionView;
    private JScrollPane scrollScenarioView;
    private JPanel panMissionButtons;
    private JPanel panScenarioButtons;
    private JButton btnAddScenario;
    private JButton btnAddMission;
    private JButton btnEditMission;
    private JButton btnCompleteMission;
    private JButton btnDeleteMission;
    private JButton btnStartGame;
    private JButton btnLoadGame;
    private JButton btnPrintRS;
    private JButton btnGetMul;
    private JButton btnClearAssignedUnits;
    private JButton btnResolveScenario;
  
    /*For the map tab*/
    private JPanel panMapView;
    InterstellarMapPanel panMap;
    private JSplitPane splitMap;
    private JScrollPane scrollPlanetView;
    private JSuggestField suggestPlanet;

    /*For the personnel tab*/
    private JPanel panPersonnel;
    private JSplitPane splitPersonnel;
    private JTable personnelTable;
    private JComboBox choicePerson;
    private JComboBox choicePersonView;
    private JScrollPane scrollPersonnelView;
    
    /*For the hangar tab*/
    private JPanel panHangar;
    private JSplitPane splitUnit;
    private JTable unitTable;
    private JTable acquireUnitsTable;
    private JComboBox choiceUnit;
    private JComboBox choiceUnitView;
    private JScrollPane scrollUnitView;
    
    /*For the warehouse tab*/
    private JPanel panSupplies;
    private JSplitPane splitWarehouse;
    private JTable partsTable;
    private JTable acquirePartsTable;
    private JTable whTechTable;
    private JButton btnDoTaskWarehouse;
    private JToggleButton btnShowAllTechsWarehouse;
    private JLabel lblTargetNumWarehouse;
    private JTextArea textTargetWarehouse;
    private JLabel astechPoolLabelWarehouse;
    private JComboBox choiceParts;
    private JComboBox choicePartsView;

    /*For the repair bay tab*/
    private JPanel panRepairBay;
    private JPanel panDoTask;
    private JTabbedPane tabTasks;
    private JSplitPane splitServicedUnits;
    private JTable servicedUnitTable;
    private JTable taskTable;
    private JTable acquisitionTable;
    private JTable techTable;
    private JButton btnDoTask;
    private JToggleButton btnShowAllTechs;
    private JScrollPane scrTextTarget;
    private JScrollPane scrollPartsTable;
    private JLabel lblTargetNum;
    private JTextPane txtServicedUnitView;
    private JTextArea textTarget;
    private JLabel astechPoolLabel;
    
    /*For the infirmary tab*/
    private JPanel panInfirmary;
    private JTable docTable;
    private JButton btnAssignDoc;
    private JButton btnUnassignDoc;
    private JList listAssignedPatient;
    private JList listUnassignedPatient;
    
    /*For the mek lab tab*/
    private MekLabPanel panMekLab;
    
    /*For the finances tab*/
    private JPanel panFinances;
    private JTable financeTable;
    private JTable loanTable;
    private JTextArea areaNetWorth;
    private JButton btnAddFunds;

    /*Components for the status panel*/
    private JPanel statusPanel;
    private JToggleButton btnGMMode;
    private JToggleButton btnOvertime;
    private JLabel lblRating;
    private JLabel lblFunds;
    private JLabel lblTempAstechs;
    private JLabel lblTempMedics;
    private JLabel lblCargo;
        
    /*Table models that we will need*/
    private TaskTableModel taskModel;
    private AcquisitionTableModel acquireModel;
    private ServicedUnitTableModel servicedUnitModel;
    private TechTableModel techsModel;
    private PatientTableModel assignedPatientModel;
    private PatientTableModel unassignedPatientModel;
    private DocTableModel doctorsModel;
    private PersonnelTableModel personModel;
    private UnitTableModel unitModel;
    private PartsTableModel partsModel;
    private ProcurementTableModel acquirePartsModel;
    private ProcurementTableModel acquireUnitsModel;
    private FinanceTableModel financeModel;
    private LoanTableModel loanModel;
    private ScenarioTableModel scenarioModel;
    private OrgTreeModel orgModel;
    
    /*table sorters for tables that can be filtered*/
    private TableRowSorter<PersonnelTableModel> personnelSorter;
    private TableRowSorter<PartsTableModel> partsSorter;
    private TableRowSorter<ProcurementTableModel> acquirePartsSorter;
    private TableRowSorter<UnitTableModel> unitSorter;
    private TableRowSorter<ServicedUnitTableModel> servicedUnitSorter;
    private TableRowSorter<TechTableModel> techSorter;
    private TableRowSorter<TechTableModel> whTechSorter;
    
    ReportHyperlinkListener reportHLL;
    
    public int selectedMission;

    public CampaignGUI(MekHQ app) {
        this.app = app;        
        reportHLL = new ReportHyperlinkListener(this);
        selectedMission = -1;
        initComponents();
    }

    public void showAboutBox() {
        MekHQAboutBox aboutBox = new MekHQAboutBox(getFrame());
        aboutBox.setLocationRelativeTo(getFrame());
        aboutBox.setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    private void initComponents() {
    	
        resourceMap = ResourceBundle.getBundle("mekhq.resources.CampaignGUI");
        
        frame = new JFrame("MekHQ"); //$NON-NLS-1$
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        tabMain = new JTabbedPane();
        tabMain.setToolTipText(resourceMap.getString("tabMain.toolTipText")); // NOI18N
        tabMain.setMinimumSize(new java.awt.Dimension(600, 200));
        tabMain.setPreferredSize(new java.awt.Dimension(900, 300));

        initToeTab();
        tabMain.addTab(
                resourceMap.getString("panOrganization.TabConstraints.tabTitle"),
                panOrganization); // NOI18N

        
        initBriefingTab();
        tabMain.addTab(
                resourceMap.getString("panBriefing.TabConstraints.tabTitle"),
                splitBrief); // NOI18N

        initMap();
        tabMain.addTab(
                resourceMap.getString("panMap.TabConstraints.tabTitle"),
                splitMap); // NOI18N

        initPersonnelTab();       
        tabMain.addTab(
                resourceMap.getString("panPersonnel.TabConstraints.tabTitle"),
                panPersonnel); // NOI18N

        initHangarTab();
        tabMain.addTab(
                resourceMap.getString("panHangar.TabConstraints.tabTitle"),
                panHangar); // NOI18N

        initWarehouseTab();       
        tabMain.addTab(
                resourceMap.getString("panSupplies.TabConstraints.tabTitle"),
                splitWarehouse); // NOI18N

        initRepairTab();
        tabMain.addTab(
                resourceMap.getString("panRepairBay.TabConstraints.tabTitle"),
                panRepairBay); // NOI18N

        initInfirmaryTab();
        tabMain.addTab(
                resourceMap.getString("panInfirmary.TabConstraints.tabTitle"),
                panInfirmary); // NOI18N

        panMekLab = new MekLabPanel(this);
        tabMain.addTab(
                resourceMap.getString("panMekLab.TabConstraints.tabTitle"),
                new JScrollPane(panMekLab)); // NOI18N

        initFinanceTab();
        tabMain.addTab(
                resourceMap.getString("panFinances.TabConstraints.tabTitle"),
                panFinances); // NOI18N
        
        initMain();
        initStatusBar();

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
        refreshRating();
        refreshFinancialTransactions();
        refreshOrganization();
        refreshMissions();
        refreshLocation();
        refreshTempAstechs();
        refreshTempMedics();
        refreshCargo();
        refreshScenarioList();

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

        initMenu();
        frame.setJMenuBar(menuBar);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(this, BorderLayout.CENTER);
        frame.validate();

        if (isMacOSX()) {
            enableFullScreenMode(frame);
        }
        
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                getApplication().exit();
            }
        });
    }
    
    private void initToeTab() {
        GridBagConstraints gridBagConstraints;
        
        panOrganization = new JPanel(new GridBagLayout());

        orgModel = new OrgTreeModel(getCampaign());
        orgTree = new JTree(orgModel);
        orgTree.addMouseListener(new OrgTreeMouseAdapter());
        orgTree.setCellRenderer(new ForceRenderer(getIconPackage()));
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

        scrollForceView = new JScrollPane();
        scrollForceView.setMinimumSize(new java.awt.Dimension(550, 600));
        scrollForceView.setPreferredSize(new java.awt.Dimension(550, 600));
        scrollForceView.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        splitOrg = new JSplitPane(javax.swing.JSplitPane.HORIZONTAL_SPLIT,new JScrollPane(orgTree), scrollForceView);
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

    }
    
    private void initBriefingTab() {
        GridBagConstraints gridBagConstraints;

        panBriefing = new JPanel(new GridBagLayout());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.NONE;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.CENTER;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.weighty = 0.0;
        panBriefing.add(new JLabel(resourceMap.getString("lblMission.text")), gridBagConstraints);

        choiceMission = new JComboBox();
        choiceMission.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeMission();
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.0;
        panBriefing.add(choiceMission, gridBagConstraints);


        panMissionButtons = new JPanel(new GridLayout(2,3));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.0;
        panBriefing.add(panMissionButtons, gridBagConstraints);

        btnAddMission = new JButton(resourceMap.getString("btnAddMission.text")); // NOI18N
        btnAddMission.setToolTipText(resourceMap.getString("btnAddMission.toolTipText")); // NOI18N
        btnAddMission.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addMission();
            }
        });
        panMissionButtons.add(btnAddMission);

        btnAddScenario = new JButton(resourceMap.getString("btnAddScenario.text")); // NOI18N
        btnAddScenario.setToolTipText(resourceMap.getString("btnAddScenario.toolTipText")); // NOI18N
        btnAddScenario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addScenario();
            }
        });
        panMissionButtons.add(btnAddScenario);

        btnEditMission = new JButton(resourceMap.getString("btnEditMission.text")); // NOI18N
        btnEditMission.setToolTipText(resourceMap.getString("btnEditMission.toolTipText")); // NOI18N
        btnEditMission.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editMission();
            }
        });
        panMissionButtons.add(btnEditMission);

        btnCompleteMission = new JButton(resourceMap.getString("btnCompleteMission.text")); // NOI18N
        btnCompleteMission.setToolTipText(resourceMap .getString("btnCompleteMission.toolTipText")); // NOI18N
        btnCompleteMission.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                completeMission();
            }
        });
        panMissionButtons.add(btnCompleteMission);

        btnDeleteMission = new JButton(resourceMap.getString("btnDeleteMission.text")); // NOI18N
        btnDeleteMission.setToolTipText(resourceMap.getString("btnDeleteMission.toolTipText")); // NOI18N
        btnDeleteMission.setName("btnDeleteMission"); // NOI18N
        btnDeleteMission.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteMission();
            }
        });
        panMissionButtons.add(btnDeleteMission);

        scrollMissionView = new JScrollPane();
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

        scenarioModel = new ScenarioTableModel(getCampaign());
        scenarioTable = new JTable(scenarioModel);
        scenarioTable.setShowGrid(false);
        scenarioTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scenarioTable.addMouseListener(new ScenarioTableMouseAdapter());
        scenarioTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        scenarioTable.setIntercellSpacing(new Dimension(0, 0));
        scenarioTable.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                refreshScenarioView();
            }
        });
        JScrollPane scrollScenarioTable = new JScrollPane(scenarioTable);
        scrollScenarioTable.setMinimumSize(new java.awt.Dimension(200, 200));
        scrollScenarioTable.setPreferredSize(new java.awt.Dimension(200, 200));

        splitMission = new javax.swing.JSplitPane(javax.swing.JSplitPane.VERTICAL_SPLIT, panBriefing, scrollScenarioTable);
        splitMission.setOneTouchExpandable(true);
        splitMission.setResizeWeight(1.0);

        panScenario = new JPanel(new GridBagLayout());

        panScenarioButtons = new JPanel(new GridLayout(2,3));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.0;
        panScenario.add(panScenarioButtons, gridBagConstraints);

        btnStartGame = new JButton(resourceMap.getString("btnStartGame.text")); // NOI18N
        btnStartGame.setToolTipText(resourceMap.getString("btnStartGame.toolTipText")); // NOI18N
        btnStartGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startScenario();
            }
        });
        btnStartGame.setEnabled(false);
        panScenarioButtons.add(btnStartGame);

        btnLoadGame = new JButton(resourceMap.getString("btnLoadGame.text")); // NOI18N
        btnLoadGame.setToolTipText(resourceMap.getString("btnLoadGame.toolTipText")); // NOI18N
        btnLoadGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadScenario();
            }
        });
        btnLoadGame.setEnabled(false);
        panScenarioButtons.add(btnLoadGame);

        btnPrintRS = new JButton(resourceMap.getString("btnPrintRS.text")); // NOI18N
        btnPrintRS.setToolTipText(resourceMap.getString("btnPrintRS.toolTipText")); // NOI18N
        btnPrintRS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printRecordSheets();
            }
        });
        btnPrintRS.setEnabled(false);
        panScenarioButtons.add(btnPrintRS);

        btnGetMul = new JButton(resourceMap.getString("btnGetMul.text")); // NOI18N
        btnGetMul.setToolTipText(resourceMap.getString("btnGetMul.toolTipText")); // NOI18N
        btnGetMul.setName("btnGetMul"); // NOI18N
        btnGetMul.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deployListFile();
            }
        });
        btnGetMul.setEnabled(false);
        panScenarioButtons.add(btnGetMul);

        btnResolveScenario = new JButton(resourceMap.getString("btnResolveScenario.text")); // NOI18N
        btnResolveScenario.setToolTipText(resourceMap.getString("btnResolveScenario.toolTipText")); // NOI18N
        btnResolveScenario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resolveScenario();
            }
        });
        btnResolveScenario.setEnabled(false);
        panScenarioButtons.add(btnResolveScenario);

        btnClearAssignedUnits = new JButton(resourceMap.getString("btnClearAssignedUnits.text")); // NOI18N
        btnClearAssignedUnits.setToolTipText(resourceMap.getString("btnClearAssignedUnits.toolTipText")); // NOI18N
        btnClearAssignedUnits.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearAssignedUnits();
            }
        });
        btnClearAssignedUnits.setEnabled(false);
        panScenarioButtons.add(btnClearAssignedUnits);

        scrollScenarioView = new JScrollPane();
        scrollScenarioView.setViewportView(null);
        scrollScenarioView.setMinimumSize(new java.awt.Dimension(450, 600));
        scrollScenarioView.setPreferredSize(new java.awt.Dimension(450, 600));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panScenario.add(scrollScenarioView, gridBagConstraints);

        splitBrief = new javax.swing.JSplitPane(javax.swing.JSplitPane.HORIZONTAL_SPLIT, splitMission, panScenario);
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
    }
    
    private void initMap() {
        GridBagConstraints gridBagConstraints;

        panMapView = new JPanel(new GridBagLayout());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panMapView.add(new JLabel(resourceMap.getString("lblFindPlanet.text")), gridBagConstraints);

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
        panMapView.add(suggestPlanet, gridBagConstraints);

        JButton btnCalculateJumpPath = new JButton(resourceMap.getString("btnCalculateJumpPath.text")); // NOI18N
        btnCalculateJumpPath.setToolTipText(resourceMap.getString("btnCalculateJumpPath.toolTipText")); // NOI18N
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
        panMapView.add(btnCalculateJumpPath, gridBagConstraints);

        JButton btnBeginTransit = new JButton(resourceMap.getString("btnBeginTransit.text")); // NOI18N
        btnBeginTransit.setToolTipText(resourceMap.getString("btnBeginTransit.toolTipText")); // NOI18N
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
        panMapView.add(btnBeginTransit, gridBagConstraints);

        panMap = new InterstellarMapPanel(getCampaign(), this);
        //lets go ahead and zoom in on the current location
        panMap.setSelectedPlanet(getCampaign().getLocation().getCurrentPlanet());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panMapView.add(panMap, gridBagConstraints);

        scrollPlanetView = new JScrollPane();
        scrollPlanetView.setMinimumSize(new java.awt.Dimension(400, 600));
        scrollPlanetView.setPreferredSize(new java.awt.Dimension(400, 600));
        scrollPlanetView.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPlanetView.setViewportView(null);
        splitMap = new javax.swing.JSplitPane(javax.swing.JSplitPane.HORIZONTAL_SPLIT,panMapView, scrollPlanetView);
        splitMap.setOneTouchExpandable(true);
        splitMap.setResizeWeight(1.0);
        splitMap.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                //this can mess up the planet view panel so refresh it
                refreshPlanetView();
            }
        });
        
        panMap.setCampaign(getCampaign());
    }
    
    private void initPersonnelTab() {
        GridBagConstraints gridBagConstraints;

        panPersonnel = new JPanel(new GridBagLayout());

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        panPersonnel.add(new JLabel(resourceMap.getString("lblPersonChoice.text")), gridBagConstraints);

        DefaultComboBoxModel personGroupModel = new DefaultComboBoxModel();
        for (int i = 0; i < PG_NUM; i++) {
            personGroupModel.addElement(getPersonnelGroupName(i));
        }
        choicePerson = new JComboBox(personGroupModel);
        choicePerson.setSelectedIndex(0);
        choicePerson.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                filterPersonnel();
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.NONE;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        panPersonnel.add(choicePerson, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        panPersonnel.add(new JLabel(resourceMap.getString("lblPersonView.text")), gridBagConstraints);

        DefaultComboBoxModel personViewModel = new DefaultComboBoxModel();
        for (int i = 0; i < PV_NUM; i++) {
            personViewModel.addElement(getPersonnelViewName(i));
        }
        choicePersonView = new JComboBox(personViewModel);
        choicePersonView.setSelectedIndex(0);
        choicePersonView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changePersonnelView();
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.NONE;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        panPersonnel.add(choicePersonView, gridBagConstraints);

        personModel = new PersonnelTableModel(getCampaign());
        personnelTable = new JTable(personModel);
        personnelTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        personnelTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        XTableColumnModel personColumnModel = new XTableColumnModel();
        personnelTable.setColumnModel(personColumnModel);
        personnelTable.createDefaultColumnsFromModel();
        personnelSorter = new TableRowSorter<PersonnelTableModel>(personModel);
        personnelSorter.setComparator(PersonnelTableModel.COL_RANK, new RankSorter(getCampaign()));
        personnelSorter.setComparator(PersonnelTableModel.COL_SKILL, new LevelSorter());
        personnelSorter.setComparator(PersonnelTableModel.COL_TACTICS, new BonusSorter());
        personnelSorter.setComparator(PersonnelTableModel.COL_TOUGH, new BonusSorter());
        personnelSorter.setComparator(PersonnelTableModel.COL_SALARY, new FormattedNumberSorter());
        personnelTable.setRowSorter(personnelSorter);
        ArrayList<RowSorter.SortKey> sortKeys = new ArrayList<RowSorter.SortKey>();
        sortKeys.add(new RowSorter.SortKey(PersonnelTableModel.COL_RANK, SortOrder.DESCENDING));
        sortKeys.add(new RowSorter.SortKey(PersonnelTableModel.COL_SKILL, SortOrder.DESCENDING));
        personnelSorter.setSortKeys(sortKeys);
        personnelTable.addMouseListener(new PersonnelTableMouseAdapter());
        TableColumn column = null;
        for (int i = 0; i < PersonnelTableModel.N_COL; i++) {
            column = personnelTable.getColumnModel().getColumn(i);
            column.setPreferredWidth(personModel.getColumnWidth(i));
            column.setCellRenderer(personModel.getRenderer(choicePersonView.getSelectedIndex() == PV_GRAPHIC, getIconPackage()));
        }
        personnelTable.setIntercellSpacing(new Dimension(0, 0));
        personnelTable.setShowGrid(false);
        changePersonnelView();
        personnelTable.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(ListSelectionEvent evt) {
                refreshPersonnelView();
            }
        });

        scrollPersonnelView = new JScrollPane();
        scrollPersonnelView.setMinimumSize(new java.awt.Dimension(500, 600));
        scrollPersonnelView.setPreferredSize(new java.awt.Dimension(500, 600));
        scrollPersonnelView.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPersonnelView.setViewportView(null);

        splitPersonnel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,new JScrollPane(personnelTable), scrollPersonnelView);
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

    }
    
    private void initHangarTab() {
        GridBagConstraints gridBagConstraints;
        
        panHangar = new JPanel(new GridBagLayout());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(5, 5, 0, 0);
        panHangar.add(new JLabel(resourceMap.getString("lblUnitChoice.text")), gridBagConstraints);

        DefaultComboBoxModel unitGroupModel = new DefaultComboBoxModel();
        unitGroupModel.addElement("All Units");
        for (int i = 0; i < UnitType.SIZE; i++) {
            unitGroupModel.addElement(UnitType.getTypeDisplayableName(i));
        }
        choiceUnit = new JComboBox(unitGroupModel);
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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        panHangar.add(new JLabel(resourceMap.getString("lblUnitView.text")), gridBagConstraints);

        DefaultComboBoxModel unitViewModel = new DefaultComboBoxModel();
        for (int i = 0; i < UV_NUM; i++) {
            unitViewModel.addElement(getUnitViewName(i));
        }
        choiceUnitView = new JComboBox(unitViewModel);
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

        unitModel = new UnitTableModel(getCampaign());
        unitTable = new JTable(unitModel);
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
        ArrayList<RowSorter.SortKey> sortKeys = new ArrayList<RowSorter.SortKey>();
        sortKeys.add(new RowSorter.SortKey(UnitTableModel.COL_TYPE, SortOrder.DESCENDING));
        sortKeys.add(new RowSorter.SortKey(UnitTableModel.COL_WCLASS, SortOrder.DESCENDING));
        unitSorter.setSortKeys(sortKeys);
        unitTable.addMouseListener(new UnitTableMouseAdapter());
        unitTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        TableColumn column = null;
        for (int i = 0; i < UnitTableModel.N_COL; i++) {
            column = unitTable.getColumnModel().getColumn(i);
            column.setPreferredWidth(unitModel.getColumnWidth(i));
            column.setCellRenderer(unitModel.getRenderer(choiceUnitView.getSelectedIndex() == 0, getIconPackage()));
        }
        unitTable.setIntercellSpacing(new Dimension(0, 0));
        unitTable.setShowGrid(false);
        changeUnitView();
        unitTable.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                refreshUnitView();
            }
        });
        
        acquireUnitsModel = new ProcurementTableModel(getCampaign());
        acquireUnitsTable = new JTable(acquireUnitsModel);
        TableRowSorter<ProcurementTableModel> acquireUnitsSorter = new TableRowSorter<ProcurementTableModel>(acquireUnitsModel);
        acquireUnitsSorter.setComparator(ProcurementTableModel.COL_COST, new FormattedNumberSorter());
        acquireUnitsSorter.setComparator(ProcurementTableModel.COL_TARGET, new TargetSorter());
        acquireUnitsTable.setRowSorter(acquireUnitsSorter);
        column = null;
        for (int i = 0; i < ProcurementTableModel.N_COL; i++) {
            column = acquireUnitsTable.getColumnModel().getColumn(i);
            column.setPreferredWidth(acquireUnitsModel.getColumnWidth(i));
            column.setCellRenderer(acquireUnitsModel.getRenderer());
        }
        acquireUnitsTable.setIntercellSpacing(new Dimension(0, 0));
        acquireUnitsTable.setShowGrid(false);
        acquireUnitsTable.addMouseListener(new ProcurementTableMouseAdapter());
        acquireUnitsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        acquireUnitsTable.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "ADD");
        acquireUnitsTable.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, 0), "ADD");
        acquireUnitsTable.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "REMOVE");
        acquireUnitsTable.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, 0), "REMOVE");
        
        acquireUnitsTable.getActionMap().put("ADD", new AbstractAction() {
            /**
             * 
             */
            private static final long serialVersionUID = 4958203340754214211L;

            public void actionPerformed(ActionEvent e) {
                if(acquireUnitsTable.getSelectedRow() < 0) {
                    return;
                }
                acquireUnitsModel.incrementItem(acquireUnitsTable.convertRowIndexToModel(acquireUnitsTable.getSelectedRow()));
            }
         });
        
        acquireUnitsTable.getActionMap().put("REMOVE", new AbstractAction() {
            /**
             * 
             */
            private static final long serialVersionUID = -8377486575329708963L;

            public void actionPerformed(ActionEvent e) {
                if(acquireUnitsTable.getSelectedRow() < 0) {
                    return;
                }
                if(acquireUnitsModel.getAcquisition(acquireUnitsTable.convertRowIndexToModel(acquireUnitsTable.getSelectedRow())).getQuantity() > 0) {
                    acquireUnitsModel.decrementItem(acquireUnitsTable.convertRowIndexToModel(acquireUnitsTable.getSelectedRow()));
                } 
            }
         });
        
        JScrollPane scrollAcquireUnitTable = new JScrollPane(acquireUnitsTable);
        JPanel panAcquireUnit = new JPanel(new GridLayout(0,1));
        panAcquireUnit.setBorder(BorderFactory.createTitledBorder("Procurement List"));
        panAcquireUnit.add(scrollAcquireUnitTable);
        panAcquireUnit.setMinimumSize(new Dimension(200,200));
        panAcquireUnit.setPreferredSize(new Dimension(200,200));
        
        JSplitPane splitLeftUnit = new JSplitPane(javax.swing.JSplitPane.VERTICAL_SPLIT,new JScrollPane(unitTable), panAcquireUnit);
        splitLeftUnit.setOneTouchExpandable(true);
        splitLeftUnit.setResizeWeight(1.0);
        
        scrollUnitView = new JScrollPane();
        scrollUnitView.setMinimumSize(new java.awt.Dimension(450, 600));
        scrollUnitView.setPreferredSize(new java.awt.Dimension(450, 600));
        scrollUnitView.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollUnitView.setViewportView(null);

        splitUnit = new javax.swing.JSplitPane(javax.swing.JSplitPane.HORIZONTAL_SPLIT,splitLeftUnit, scrollUnitView);
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
    }
    
    private void initWarehouseTab() {
        GridBagConstraints gridBagConstraints;
        
        panSupplies = new JPanel(new GridBagLayout());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        panSupplies.add(new JLabel(resourceMap.getString("lblPartsChoice.text")), gridBagConstraints);

        DefaultComboBoxModel partsGroupModel = new DefaultComboBoxModel();
        for (int i = 0; i < SG_NUM; i++) {
            partsGroupModel.addElement(getPartsGroupName(i));
        }
        choiceParts = new JComboBox(partsGroupModel);
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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        panSupplies.add(new JLabel(resourceMap.getString("lblPartsChoiceView.text")), gridBagConstraints);

        DefaultComboBoxModel partsGroupViewModel = new DefaultComboBoxModel();
        for (int i = 0; i < SV_NUM; i++) {
            partsGroupViewModel.addElement(getPartsGroupViewName(i));
        }
        choicePartsView = new JComboBox(partsGroupViewModel);
        choicePartsView.setSelectedIndex(0);
        choicePartsView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filterParts();
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.NONE;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        panSupplies.add(choicePartsView, gridBagConstraints);
       
        partsModel = new PartsTableModel();
        partsTable = new JTable(partsModel);
        partsSorter = new TableRowSorter<PartsTableModel>(partsModel);
        partsSorter.setComparator(PartsTableModel.COL_COST, new FormattedNumberSorter());
        partsTable.setRowSorter(partsSorter);
        TableColumn column = null;
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
        partsTable.addMouseListener(new PartsTableMouseAdapter());

        scrollPartsTable = new JScrollPane(partsTable);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panSupplies.add(scrollPartsTable, gridBagConstraints);
        
        acquirePartsModel = new ProcurementTableModel(getCampaign());
        acquirePartsTable = new JTable(acquirePartsModel);
        acquirePartsSorter = new TableRowSorter<ProcurementTableModel>(acquirePartsModel);
        acquirePartsSorter.setComparator(ProcurementTableModel.COL_COST, new FormattedNumberSorter());
        acquirePartsSorter.setComparator(ProcurementTableModel.COL_TARGET, new TargetSorter());
        acquirePartsTable.setRowSorter(acquirePartsSorter);
        column = null;
        for (int i = 0; i < ProcurementTableModel.N_COL; i++) {
            column = acquirePartsTable.getColumnModel().getColumn(i);
            column.setPreferredWidth(acquirePartsModel.getColumnWidth(i));
            column.setCellRenderer(acquirePartsModel.getRenderer());
        }
        acquirePartsTable.setIntercellSpacing(new Dimension(0, 0));
        acquirePartsTable.setShowGrid(false);
        acquirePartsTable.addMouseListener(new ProcurementTableMouseAdapter());
        acquirePartsTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        acquirePartsTable.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "ADD");
        acquirePartsTable.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, 0), "ADD");
        acquirePartsTable.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "REMOVE");
        acquirePartsTable.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, 0), "REMOVE");
        
        acquirePartsTable.getActionMap().put("ADD", new AbstractAction() {
            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent e) {
                if(acquirePartsTable.getSelectedRow() < 0) {
                    return;
                }
                acquirePartsModel.incrementItem(acquirePartsTable.convertRowIndexToModel(acquirePartsTable.getSelectedRow()));
            }
         });
        
        acquirePartsTable.getActionMap().put("REMOVE", new AbstractAction() {
            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent e) {
                if(acquirePartsTable.getSelectedRow() < 0) {
                    return;
                }
                if(acquirePartsModel.getAcquisition(acquirePartsTable.convertRowIndexToModel(acquirePartsTable.getSelectedRow())).getQuantity() > 0) {
                    acquirePartsModel.decrementItem(acquirePartsTable.convertRowIndexToModel(acquirePartsTable.getSelectedRow()));
                } 
            }
         });
        
        JScrollPane scrollPartsAcquireTable = new JScrollPane(acquirePartsTable);
        
        JPanel acquirePartsPanel = new JPanel(new GridBagLayout());
        acquirePartsPanel.setBorder(BorderFactory.createTitledBorder("Procurement List"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        acquirePartsPanel.add(scrollPartsAcquireTable, gridBagConstraints);
        acquirePartsPanel.setMinimumSize(new Dimension(200,200));
        acquirePartsPanel.setPreferredSize(new Dimension(200,200));
        
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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        panelDoTaskWarehouse.add(new JLabel(resourceMap.getString("lblTarget.text")), gridBagConstraints);

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

        btnShowAllTechsWarehouse = new JToggleButton(resourceMap.getString("btnShowAllTechs.text"));
        btnShowAllTechsWarehouse.setToolTipText(resourceMap.getString("btnShowAllTechs.toolTipText")); // NOI18N
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

        techsModel = new TechTableModel(getCampaign());
        whTechTable = new JTable(techsModel);
        whTechTable.setRowHeight(60);
        whTechTable.getColumnModel().getColumn(0).setCellRenderer(techsModel.getRenderer(getIconPackage()));
        whTechTable.getSelectionModel().addListSelectionListener(
                new javax.swing.event.ListSelectionListener() {
                    public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                        updateTechTarget();
                    }
                });
        whTechSorter = new TableRowSorter<TechTableModel>(techsModel);
        whTechSorter.setComparator(0, new TechSorter());
        whTechTable.setRowSorter(whTechSorter);
        ArrayList<RowSorter.SortKey> sortKeys = new ArrayList<RowSorter.SortKey>();
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
        whTechSorter.setSortKeys(sortKeys);
        JScrollPane scrollWhTechTable = new JScrollPane(whTechTable);
        scrollWhTechTable.setMinimumSize(new java.awt.Dimension(200, 200));
        scrollWhTechTable.setPreferredSize(new java.awt.Dimension(300, 300));
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

        astechPoolLabelWarehouse = new JLabel("<html><b>Astech Pool Minutes:</> " + getCampaign().getAstechPoolMinutes() + " (" + getCampaign().getNumberAstechs() + " Astechs)</html>"); // NOI18N
        astechPoolLabelWarehouse.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panelDoTaskWarehouse.add(astechPoolLabelWarehouse, gridBagConstraints);

        JSplitPane splitLeft = new javax.swing.JSplitPane(javax.swing.JSplitPane.VERTICAL_SPLIT, panSupplies, acquirePartsPanel);
        splitLeft.setOneTouchExpandable(true);
        splitLeft.setResizeWeight(1.0);
        splitWarehouse = new javax.swing.JSplitPane(javax.swing.JSplitPane.HORIZONTAL_SPLIT, splitLeft, panelDoTaskWarehouse);
        splitWarehouse.setOneTouchExpandable(true);
        splitWarehouse.setResizeWeight(1.0);   
    }
    
    private void initRepairTab() {
        GridBagConstraints gridBagConstraints;
        
        panRepairBay = new JPanel(new GridBagLayout());

        JPanel panServicedUnits = new JPanel(new GridBagLayout());
        
        servicedUnitModel = new ServicedUnitTableModel();
        servicedUnitTable = new JTable(servicedUnitModel);
        servicedUnitTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        servicedUnitTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        servicedUnitSorter = new TableRowSorter<ServicedUnitTableModel>(servicedUnitModel);
        servicedUnitSorter.setComparator(ServicedUnitTableModel.COL_STATUS, new UnitStatusSorter());
        servicedUnitSorter.setComparator(ServicedUnitTableModel.COL_TYPE, new UnitTypeSorter());
        servicedUnitTable.setRowSorter(servicedUnitSorter);
        ArrayList<RowSorter.SortKey> sortKeys = new ArrayList<RowSorter.SortKey>();
        sortKeys.add(new RowSorter.SortKey(ServicedUnitTableModel.COL_TYPE, SortOrder.DESCENDING));
        servicedUnitSorter.setSortKeys(sortKeys);
        TableColumn column = null;
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
        servicedUnitTable.addMouseListener(new ServicedUnitsTableMouseAdapter());
        JScrollPane scrollServicedUnitTable = new JScrollPane(servicedUnitTable);
        scrollServicedUnitTable.setMinimumSize(new java.awt.Dimension(350, 200));
        scrollServicedUnitTable.setPreferredSize(new java.awt.Dimension(350, 200));

        txtServicedUnitView = new JTextPane();
        txtServicedUnitView.setEditable(false);
        txtServicedUnitView.setContentType("text/html");
        JScrollPane scrollServicedUnitView = new JScrollPane(txtServicedUnitView);
        scrollServicedUnitView.setMinimumSize(new java.awt.Dimension(350, 400));
        scrollServicedUnitView.setPreferredSize(new java.awt.Dimension(350, 400));
        
        splitServicedUnits = new JSplitPane(JSplitPane.VERTICAL_SPLIT,scrollServicedUnitTable, scrollServicedUnitView);
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
  
        techTable = new JTable(techsModel);
        techTable.setRowHeight(60);
        techTable.getColumnModel().getColumn(0).setCellRenderer(techsModel.getRenderer(getIconPackage()));
        techTable.getSelectionModel().addListSelectionListener(
                new javax.swing.event.ListSelectionListener() {
                    public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                        updateTechTarget();
                    }
                });
        techSorter = new TableRowSorter<TechTableModel>(techsModel);
        techSorter.setComparator(0, new TechSorter());
        techTable.setRowSorter(techSorter);
        sortKeys = new ArrayList<RowSorter.SortKey>();
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
        techSorter.setSortKeys(sortKeys);
        JScrollPane scrollTechTable = new JScrollPane(techTable);
        scrollTechTable.setMinimumSize(new java.awt.Dimension(200, 200));
        scrollTechTable.setPreferredSize(new java.awt.Dimension(300, 300));

        tabTasks = new JTabbedPane();
        tabTasks.setMinimumSize(new java.awt.Dimension(300, 200));
        tabTasks.setName("tabTasks"); // NOI18N
        tabTasks.setPreferredSize(new java.awt.Dimension(300, 300));
        
        panDoTask = new JPanel(new GridBagLayout());
        panDoTask.setMinimumSize(new java.awt.Dimension(300, 100));
        panDoTask.setName("panelDoTask"); // NOI18N
        panDoTask.setPreferredSize(new java.awt.Dimension(300, 100));

        btnDoTask = new JButton(resourceMap.getString("btnDoTask.text")); // NOI18N
        btnDoTask.setToolTipText(resourceMap.getString("btnDoTask.toolTipText")); // NOI18N
        btnDoTask.setEnabled(false);
        btnDoTask.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doTask();
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panDoTask.add(btnDoTask, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        panDoTask.add(new JLabel(resourceMap.getString("lblTarget.text")), gridBagConstraints);

        lblTargetNum = new JLabel(resourceMap.getString("lblTargetNum.text")); // NOI18N
        lblTargetNum.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTargetNum.setName("lblTargetNum"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        panDoTask.add(lblTargetNum, gridBagConstraints);

        textTarget = new JTextArea();
        textTarget.setColumns(20);
        textTarget.setEditable(false);
        textTarget.setLineWrap(true);
        textTarget.setRows(5);
        textTarget.setText(resourceMap.getString("textTarget.text")); // NOI18N
        textTarget.setWrapStyleWord(true);
        textTarget.setBorder(null);
        textTarget.setName("textTarget"); // NOI18N
        scrTextTarget = new JScrollPane(textTarget);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDoTask.add(scrTextTarget, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        panTasks.add(panDoTask, gridBagConstraints);

        
        taskModel = new TaskTableModel();
        taskTable = new JTable(taskModel);
        taskTable.setRowHeight(70);
        taskTable.getColumnModel().getColumn(0).setCellRenderer(taskModel.getRenderer(getIconPackage()));
        taskTable.getSelectionModel().addListSelectionListener(
                new javax.swing.event.ListSelectionListener() {
                    public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                        taskTableValueChanged(evt);
                    }
                });
        taskTable.addMouseListener(new TaskTableMouseAdapter());
        JScrollPane scrollTaskTable = new JScrollPane(taskTable);
        scrollTaskTable.setMinimumSize(new java.awt.Dimension(200, 200));
        scrollTaskTable.setPreferredSize(new java.awt.Dimension(300, 300));

        
        acquireModel = new AcquisitionTableModel();
        acquisitionTable = new JTable(acquireModel);
        acquisitionTable.setName("AcquisitionTable"); // NOI18N
        acquisitionTable.setRowHeight(70);
        acquisitionTable.getColumnModel().getColumn(0).setCellRenderer(acquireModel.getRenderer(getIconPackage()));
        acquisitionTable.getSelectionModel().addListSelectionListener(
                new javax.swing.event.ListSelectionListener() {
                    public void valueChanged(
                            javax.swing.event.ListSelectionEvent evt) {
                        acquisitionTableValueChanged(evt);
                    }
                });
        acquisitionTable.addMouseListener(new AcquisitionTableMouseAdapter());
        JScrollPane scrollAcquisitionTable = new JScrollPane(acquisitionTable);
        scrollAcquisitionTable.setMinimumSize(new java.awt.Dimension(200, 200));
        scrollAcquisitionTable.setPreferredSize(new java.awt.Dimension(300, 300));

        tabTasks.addTab(resourceMap.getString("scrollTaskTable.TabConstraints.tabTasks"), scrollTaskTable); // NOI18N
        tabTasks.addTab(resourceMap.getString("scrollAcquisitionTable.TabConstraints.tabTasks"), scrollAcquisitionTable); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panTasks.add(tabTasks, gridBagConstraints);

        tabTasks.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                taskTabChanged();
            }
        });
        
        JPanel panTechs = new JPanel(new GridBagLayout());

        btnShowAllTechs = new JToggleButton(resourceMap.getString("btnShowAllTechs.text")); // NOI18N
        btnShowAllTechs.setToolTipText(resourceMap.getString("btnShowAllTechs.toolTipText")); // NOI18N
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

        astechPoolLabel = new JLabel("<html><b>Astech Pool Minutes:</> " + getCampaign().getAstechPoolMinutes() + " (" + getCampaign().getNumberAstechs() + " Astechs)</html>"); // NOI18N
        astechPoolLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
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

    }
    
    private void initInfirmaryTab() {
        GridBagConstraints gridBagConstraints;

        panInfirmary = new JPanel(new GridBagLayout());

        doctorsModel = new DocTableModel(getCampaign());
        docTable = new JTable(doctorsModel);
        docTable.setRowHeight(60);
        docTable.getColumnModel().getColumn(0).setCellRenderer(doctorsModel.getRenderer(getIconPackage()));
        docTable.getSelectionModel().addListSelectionListener(
                new javax.swing.event.ListSelectionListener() {
                    public void valueChanged(
                            javax.swing.event.ListSelectionEvent evt) {
                        docTableValueChanged(evt);
                    }
                });
        JScrollPane scrollDocTable = new JScrollPane(docTable);
        scrollDocTable.setMinimumSize(new java.awt.Dimension(300, 300));
        scrollDocTable.setPreferredSize(new java.awt.Dimension(300, 300));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.weighty = 1.0;
        panInfirmary.add(scrollDocTable, gridBagConstraints);


        btnAssignDoc = new JButton(resourceMap.getString("btnAssignDoc.text")); // NOI18N
        btnAssignDoc.setToolTipText(resourceMap.getString("btnAssignDoc.toolTipText")); // NOI18N
        btnAssignDoc.setEnabled(false);
        btnAssignDoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                assignDoctor();
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panInfirmary.add(btnAssignDoc, gridBagConstraints);

        btnUnassignDoc = new JButton(resourceMap.getString("btnUnassignDoc.text")); // NOI18N
        btnUnassignDoc.setEnabled(false);
        btnUnassignDoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                unassignDoctor();
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panInfirmary.add(btnUnassignDoc, gridBagConstraints);

       
        assignedPatientModel = new PatientTableModel(getCampaign());
        listAssignedPatient = new JList(assignedPatientModel);
        listAssignedPatient.setCellRenderer(assignedPatientModel.getRenderer(getIconPackage()));
        listAssignedPatient.setLayoutOrientation(JList.VERTICAL_WRAP);
        listAssignedPatient.setVisibleRowCount(5);
        listAssignedPatient.getSelectionModel().addListSelectionListener(
                new javax.swing.event.ListSelectionListener() {
                    public void valueChanged(
                            javax.swing.event.ListSelectionEvent evt) {
                        patientTableValueChanged();
                    }
                });    
        JScrollPane scrollAssignedPatient = new JScrollPane(listAssignedPatient);
        scrollAssignedPatient.setMinimumSize(new java.awt.Dimension(300, 360));
        scrollAssignedPatient.setPreferredSize(new java.awt.Dimension(300, 360));

        unassignedPatientModel = new PatientTableModel(getCampaign());
        listUnassignedPatient = new JList(unassignedPatientModel);
        listUnassignedPatient.setCellRenderer(unassignedPatientModel.getRenderer(getIconPackage()));
        listUnassignedPatient.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        listUnassignedPatient.setVisibleRowCount(-1);
        listUnassignedPatient.getSelectionModel().addListSelectionListener(
                new javax.swing.event.ListSelectionListener() {
                    public void valueChanged(
                            javax.swing.event.ListSelectionEvent evt) {
                        patientTableValueChanged();
                    }
                });
        JScrollPane scrollUnassignedPatient = new JScrollPane(listUnassignedPatient);
        scrollUnassignedPatient.setMinimumSize(new java.awt.Dimension(300, 200));
        scrollUnassignedPatient.setPreferredSize(new java.awt.Dimension(300, 300));

        
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
    }
    
    private void initFinanceTab() {
        GridBagConstraints gridBagConstraints;
        
        panFinances = new JPanel(new GridBagLayout());

        financeModel = new FinanceTableModel();
        financeTable = new JTable(financeModel);
        financeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        financeTable.addMouseListener(new FinanceTableMouseAdapter());
        financeTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        TableColumn column = null;
        for (int i = 0; i < FinanceTableModel.N_COL; i++) {
            column = financeTable.getColumnModel().getColumn(i);
            column.setPreferredWidth(financeModel.getColumnWidth(i));
            column.setCellRenderer(financeModel.getRenderer());
        }
        financeTable.setIntercellSpacing(new Dimension(0, 0));
        financeTable.setShowGrid(false);
     
        loanModel = new LoanTableModel();
        loanTable = new JTable(loanModel);
        loanTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        loanTable.addMouseListener(new LoanTableMouseAdapter());
        loanTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        column = null;
        for (int i = 0; i < LoanTableModel.N_COL; i++) {
            column = loanTable.getColumnModel().getColumn(i);
            column.setPreferredWidth(loanModel.getColumnWidth(i));
            column.setCellRenderer(loanModel.getRenderer());
        }
        loanTable.setIntercellSpacing(new Dimension(0, 0));
        loanTable.setShowGrid(false);
        JScrollPane scrollLoanTable = new JScrollPane(loanTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        JPanel panBalance = new JPanel(new GridBagLayout());
        panBalance.add(new JScrollPane(financeTable), gridBagConstraints);
        panBalance.setBorder(BorderFactory.createTitledBorder("Balance Sheet"));
        JPanel panLoan = new JPanel(new GridBagLayout());
        panLoan.add(scrollLoanTable, gridBagConstraints);
        scrollLoanTable.setMinimumSize(new java.awt.Dimension(450, 150));
        scrollLoanTable.setPreferredSize(new java.awt.Dimension(450, 150));
        panLoan.setBorder(BorderFactory.createTitledBorder("Active Loans"));
        //JSplitPane splitFinances = new JSplitPane(javax.swing.JSplitPane.VERTICAL_SPLIT,panBalance, panLoan);
        //splitFinances.setOneTouchExpandable(true);
        //splitFinances.setResizeWeight(1.0);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panFinances.add(panBalance, gridBagConstraints);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.0;
        panFinances.add(panLoan, gridBagConstraints);
        
        JPanel panelFinanceRight = new JPanel(new BorderLayout());

        JPanel pnlFinanceBtns = new JPanel(new GridLayout(1,2));
        btnAddFunds = new JButton("Add Funds (GM)");
        btnAddFunds.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addFundsActionPerformed(evt);
            }
        });
        btnAddFunds.setEnabled(getCampaign().isGM());
        pnlFinanceBtns.add(btnAddFunds);
        JButton btnGetLoan = new JButton("Get Loan");
        btnGetLoan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showNewLoanDialog();
            }
        });
        pnlFinanceBtns.add(btnGetLoan);
        //pnlFinanceBtns.add(new JButton("Manage Asset"));
        //pnlFinanceBtns.add(new JButton("Manage Income"));

        panelFinanceRight.add(pnlFinanceBtns, BorderLayout.NORTH);
        
        areaNetWorth = new JTextArea();
        areaNetWorth.setLineWrap(true);
        areaNetWorth.setWrapStyleWord(true);
        areaNetWorth.setFont(new Font("Courier New", Font.PLAIN, 12));
        areaNetWorth.setText(getCampaign().getFinancialReport());
        areaNetWorth.setEditable(false);

        JScrollPane descriptionScroll = new JScrollPane(areaNetWorth);
        panelFinanceRight.add(descriptionScroll, BorderLayout.CENTER);
        areaNetWorth.setCaretPosition(0);
        descriptionScroll.setMinimumSize(new Dimension(300,200));
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.weighty = 1.0;
        panFinances.add(panelFinanceRight, gridBagConstraints);
        

    }
    
    private void initMenu() {
        
        menuBar = new JMenuBar();
        
        /*File Menu*/
        JMenu menuFile = new JMenu(resourceMap.getString("fileMenu.text")); // NOI18N

        JMenuItem menuLoad = new JMenuItem(resourceMap.getString("menuLoad.text")); // NOI18N
        menuLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuLoadXmlActionPerformed(evt);
            }
        });
        menuFile.add(menuLoad);

        JMenuItem menuSave = new JMenuItem(resourceMap.getString("menuSave.text")); // NOI18N
        menuSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuSaveXmlActionPerformed(evt);
            }
        });
        menuFile.add(menuSave);

        JMenu menuImport = new JMenu(resourceMap.getString("menuImport.text")); // NOI18N
        JMenu menuExport = new JMenu(resourceMap.getString("menuExport.text")); // NOI18N

        /* 
         * Taharqa: I think it is confusing and bad gui feng-shui to put this in the menu
         * even though it is driven by user selections that might not even be in the 
         * visible tab at the moment. If we keep this it should be for all personnel and parts
         * and be clearly labeled as such
         * 
        miExportPerson.setText(resourceMap.getString("miExportPerson.text")); // NOI18N
        miExportPerson.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miExportPersonActionPerformed(evt);
            }
        });
        menuExport.add(miExportPerson);
        
        miExportParts.setText(resourceMap.getString("miExportParts.text")); // NOI18N
        miExportParts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miExportPartsActionPerformed(evt);
            }
        });
        menuExport.add(miExportParts);
        */
        
        JMenuItem miExportPersonCSV = new JMenuItem(resourceMap.getString("miExportPersonCSV.text")); // NOI18N
        miExportPersonCSV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportTable(personnelTable, getCampaign().getName()
                        + getCampaign().getShortDateAsString() + "_ExportedPersonnel" + ".csv");
            }
        });
        menuExport.add(miExportPersonCSV);
        
        JMenuItem miExportUnitCSV = new JMenuItem(resourceMap.getString("miExportUnitCSV.text")); // NOI18N
        miExportUnitCSV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportTable(unitTable, getCampaign().getName()
                        + getCampaign().getShortDateAsString() + "_ExportedUnit" + ".csv");
            }
        });
        menuExport.add(miExportUnitCSV);
        
        JMenuItem miImportPerson = new JMenuItem(resourceMap.getString("miImportPerson.text")); // NOI18N
        miImportPerson.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miImportPersonActionPerformed(evt);
            }
        });
        menuImport.add(miImportPerson);      
        
        JMenuItem miImportParts = new JMenuItem(resourceMap.getString("miImportParts.text")); // NOI18N
        miImportParts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miImportPartsActionPerformed(evt);
            }
        });
        menuImport.add(miImportParts);
        
        JMenuItem miLoadForces = new JMenuItem(resourceMap.getString("miLoadForces.text")); // NOI18N
        miLoadForces.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miLoadForcesActionPerformed(evt);
            }
        });
        miLoadForces.setEnabled(false);
        menuImport.add(miLoadForces);

        menuFile.add(menuImport);
        menuFile.add(menuExport);

        
        JMenuItem miMercRoster = new JMenuItem(resourceMap.getString("miMercRoster.text")); // NOI18N
        miMercRoster.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showMercRosterDialog();
            }
        });
        menuFile.add(miMercRoster);
        
        JMenuItem menuOptions = new JMenuItem(resourceMap.getString("menuOptions.text")); // NOI18N
        menuOptions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuOptionsActionPerformed(evt);
            }
        });
        menuFile.add(menuOptions);

        JMenuItem menuOptionsMM = new JMenuItem(resourceMap.getString("menuOptionsMM.text")); // NOI18N
        menuOptionsMM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuOptionsMMActionPerformed(evt);
            }
        });
        
        menuFile.add(menuOptionsMM);
        
        menuThemes =new JMenu("Themes");
        refreshThemeChoices();
        menuFile.add(menuThemes);

        JMenuItem menuExitItem = new JMenuItem("Exit");
        menuExitItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getApplication().exit();
            }
        });
        menuFile.add(menuExitItem);

        menuBar.add(menuFile);

        JMenu menuMarket = new JMenu(resourceMap.getString("menuMarket.text")); // NOI18N
        
        // Personnel Market
        JMenuItem miPersonnelMarket = new JMenuItem("Personnel Market");
        miPersonnelMarket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hirePersonMarket(evt);
            }
        });
        menuMarket.add(miPersonnelMarket);

        JMenuItem miPurchaseUnit = new JMenuItem(resourceMap.getString("miPurchaseUnit.text")); // NOI18N
        miPurchaseUnit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miPurchaseUnitActionPerformed(evt);
            }
        });
        menuMarket.add(miPurchaseUnit);

        JMenuItem miBuyParts = new JMenuItem(resourceMap.getString("miBuyParts.text")); // NOI18N
        miBuyParts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buyParts();
            }
        });
        menuMarket.add(miBuyParts);
        JMenuItem miHireBulk = new JMenuItem("Hire Personnel in Bulk");
        miHireBulk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hireBulkPersonnel();
            }
        });
        menuMarket.add(miHireBulk);
        
        JMenu menuHire = new JMenu(resourceMap.getString("menuHire.text")); // NOI18N
        
        JMenuItem miHire;
        for(int i = Person.T_MECHWARRIOR; i < Person.T_NUM; i++) {
            miHire = new JMenuItem(Person.getRoleDesc(i)); // NOI18N
            miHire.setActionCommand(Integer.toString(i));
            miHire.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    hirePerson(evt);
                }
            });
            menuHire.add(miHire);
        }
        menuMarket.add(menuHire);
        
        JMenu menuAstechPool = new JMenu("Astech Pool");
       
        JMenuItem miHireAstechs = new JMenuItem("Hire Astechs");
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
        
        JMenuItem miFireAstechs = new JMenuItem("Release Astechs");
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
        
        JMenuItem miFullStrengthAstechs = new JMenuItem("Bring All Tech Teams to Full Strength");
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
        
        JMenuItem miFireAllAstechs = new JMenuItem("Release All Astechs from Pool");
        miFireAllAstechs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getCampaign().decreaseAstechPool(getCampaign().getAstechPool());
                refreshTechsList();
                refreshTempAstechs();
            }
        });
        menuAstechPool.add(miFireAllAstechs);
        menuMarket.add(menuAstechPool);
        
        JMenu menuMedicPool = new JMenu("Medic Pool");
        JMenuItem miHireMedics = new JMenuItem("Hire Medics");
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
        
        JMenuItem miFireMedics = new JMenuItem("Release Medics");
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
        JMenuItem miFullStrengthMedics = new JMenuItem("Bring All Medical Teams to Full Strength");
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
        JMenuItem miFireAllMedics = new JMenuItem("Release All Medics from Pool");
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

        JMenu menuReports = new JMenu(resourceMap.getString("menuReports.text")); // NOI18N

        JMenuItem miDragoonsRating = new JMenuItem(resourceMap.getString("miDragoonsRating.text")); // NOI18N
        miDragoonsRating.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showReport(new RatingReport(getCampaign()));
            }
        });
        menuReports.add(miDragoonsRating);
        
        JMenuItem miPersonnelReport = new JMenuItem(resourceMap.getString("miPersonnelReport.text")); // NOI18N
        miPersonnelReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showReport(new PersonnelReport(getCampaign()));
            }
        });
        menuReports.add(miPersonnelReport);
        
        JMenuItem miHangarBreakdown = new JMenuItem(resourceMap.getString("miHangarBreakdown.text")); // NOI18N
        miHangarBreakdown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showReport(new HangarReport(getCampaign()));
            }
        });
        menuReports.add(miHangarBreakdown);
        
        JMenuItem miTransportReport = new JMenuItem(resourceMap.getString("miTransportReport.text")); // NOI18N
        miTransportReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showReport(new TransportReport(getCampaign()));
            }
        });
        menuReports.add(miTransportReport);
        
        menuBar.add(menuReports);
        
        JMenu menuCommunity = new JMenu(resourceMap.getString("menuCommunity.text")); // NOI18N

        JMenuItem miChat = new JMenuItem(resourceMap.getString("miChat.text")); // NOI18N
        miChat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miChatActionPerformed(evt);
            }
        });
        menuCommunity.add(miChat);

        //menuBar.add(menuCommunity);

        JMenu menuHelp = new JMenu(resourceMap.getString("helpMenu.text")); // NOI18N
        menuHelp.setName("helpMenu"); // NOI18N
        JMenuItem menuAboutItem = new JMenuItem("aboutMenuItem"); // NOI18N
        menuAboutItem.setText("About");
        menuAboutItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showAboutBox();
            }
        });
        menuHelp.add(menuAboutItem);
        menuBar.add(menuHelp);
    }

    private void initMain() {
        GridBagConstraints gridBagConstraints;
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setAutoscrolls(true);
        
        lblLocation = new JLabel(getCampaign().getLocation().getReport(getCampaign().getCalendar().getTime())); // NOI18N
        lblLocation.setMinimumSize(new java.awt.Dimension(250,100));
        lblLocation.setPreferredSize(new java.awt.Dimension(250, 100));
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


        txtPaneReport = new JTextPane();
        txtPaneReport.setContentType(resourceMap.getString("txtPaneReport.contentType")); // NOI18N
        txtPaneReport.setEditable(false);
        txtPaneReport.setText(getCampaign().getCurrentReportHTML());
        txtPaneReport.addHyperlinkListener(reportHLL);
        JScrollPane txtPaneReportScrollPane = new JScrollPane(txtPaneReport);     
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

        btnAdvanceDay = new JButton(resourceMap.getString("btnAdvanceDay.text")); // NOI18N
        btnAdvanceDay.setToolTipText(resourceMap.getString("btnAdvanceDay.toolTipText")); // NOI18N
        btnAdvanceDay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                advanceDay();
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

        
        
    }
    
    private void initStatusBar() {
        
        statusPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 20, 5));
             
        btnGMMode = new JToggleButton(resourceMap.getString("btnGMMode.text")); // NOI18N
        btnGMMode.setToolTipText(resourceMap.getString("btnGMMode.toolTipText")); // NOI18N
        btnGMMode.setSelected(getCampaign().isGM());
        btnGMMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGMModeActionPerformed(evt);
            }
        });

        btnOvertime = new JToggleButton(resourceMap.getString("btnOvertime.text")); // NOI18N
        btnOvertime.setToolTipText(resourceMap.getString("btnOvertime.toolTipText")); // NOI18N
        btnOvertime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOvertimeActionPerformed(evt);
            }
        });

        statusPanel.add(btnGMMode);
        statusPanel.add(btnOvertime);
        
        lblRating = new JLabel();
        lblFunds = new JLabel();
        lblTempAstechs = new JLabel();
        lblTempMedics = new JLabel();
        lblCargo = new  JLabel();  
        
        statusPanel.add(lblRating);
        statusPanel.add(lblFunds);
        statusPanel.add(lblTempAstechs);
        statusPanel.add(lblTempMedics);
        statusPanel.add(lblCargo);
    }
    
    private static void enableFullScreenMode(Window window) {
        String className = "com.apple.eawt.FullScreenUtilities";
        String methodName = "setWindowCanFullScreen";
 
        try {
            Class<?> clazz = Class.forName(className);
            Method method = clazz.getMethod(methodName, new Class<?>[] {
                    Window.class, boolean.class });
            method.invoke(null, window, true);
        } catch (Throwable t) {
            System.err.println("Full screen mode is not supported");
            t.printStackTrace();
        }
    }
 
    private static boolean isMacOSX() {
        return System.getProperty("os.name").indexOf("Mac OS X") >= 0;
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

    private void addMission() {
        MissionTypeDialog mtd = new MissionTypeDialog(getFrame(), true, getCampaign(), this);
        mtd.setVisible(true);
    }

    private void editMission() {
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

    private void completeMission() {
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

    private void deleteMission() {
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

    private void addScenario() {
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
		// int selectedTechRow = -1;
        Person tech = getSelectedTech();
        if(onWarehouseTab()) {
            selectedRow = partsTable.getSelectedRow();
			// selectedTechRow = whTechTable.getSelectedRow();
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
            getCampaign().addPart(repairable, 0);
            //if the break off part failed to be repaired, then follow it with the focus
            //otherwise keep the focus on the current row
            if(repairable.needsFixing() && !repairable.isBeingWorkedOn()) {
                partId = repairable.getId();
            }
        }
        else if(repairsSelected()) {
            selectedRow = taskTable.getSelectedRow();
			// selectedTechRow = TechTable.getSelectedRow();
            Part part = getSelectedTask();
            if(null == part) {
                return;
            }
            Unit u = part.getUnit();
            if(null != u && u.isSelfCrewed()) {
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
            selectedRow = acquisitionTable.getSelectedRow();
            IAcquisitionWork acquisition = getSelectedAcquisition();
            if(null == acquisition) {
                return;
            }
            getCampaign().getShoppingList().addShoppingItem(acquisition, 1, getCampaign());
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
        refreshCargo();

        //get the selected row back for tasks
        if(selectedRow != -1) {
            if(acquireSelected()) {
                if(acquisitionTable.getRowCount() > 0) {
                    if(acquisitionTable.getRowCount() == selectedRow) {
                        acquisitionTable.setRowSelectionInterval(selectedRow-1, selectedRow-1);
                    } else {
                        acquisitionTable.setRowSelectionInterval(selectedRow, selectedRow);
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
                if(taskTable.getRowCount() > 0) {
                    if(taskTable.getRowCount() == selectedRow) {
                        taskTable.setRowSelectionInterval(selectedRow-1, selectedRow-1);
                    } else {
                        taskTable.setRowSelectionInterval(selectedRow, selectedRow);
                    }
                }
            }
            //also get the selected tech back
            JTable table = techTable;
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
        JTable table = techTable;
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
        int row = docTable.getSelectedRow();
        if(row < 0) {
            return null;
        }
        return  doctorsModel.getDoctorAt(docTable.convertRowIndexToModel(row));
    }

    private Part getSelectedTask() {
        if(onWarehouseTab()) {
            int row = partsTable.getSelectedRow();
            if(row < 0) {
                return null;
            }
            return  partsModel.getPartAt(partsTable.convertRowIndexToModel(row));
        }
        int row = taskTable.getSelectedRow();
        if(row < 0) {
            return null;
        }
        return  taskModel.getTaskAt(taskTable.convertRowIndexToModel(row));
    }

    private IAcquisitionWork getSelectedAcquisition() {
        int row = acquisitionTable.getSelectedRow();
        if(row < 0) {
            return null;
        }
        return  acquireModel.getAcquisitionAt(acquisitionTable.convertRowIndexToModel(row));
    }

    private Unit getSelectedServicedUnit() {
        int row = servicedUnitTable.getSelectedRow();
        if(row < 0) {
            return null;
        }
        return  servicedUnitModel.getUnit(servicedUnitTable.convertRowIndexToModel(row));
    }

    private void taskTableValueChanged(javax.swing.event.ListSelectionEvent evt) {
        filterTechs(false);
        updateTechTarget();
    }

    private void acquisitionTableValueChanged(javax.swing.event.ListSelectionEvent evt) {
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
    
    public void focusOnUnit(UUID id) {
        if(null == id) {
            return;
        }
        tabMain.setSelectedIndex(4);
        int row = -1;
        for(int i=0; i< unitTable.getRowCount(); i++) {
            if(unitModel.getUnit(unitTable.convertRowIndexToModel(i)).getId().equals(id)) {
                row = i;
                break;
            }
        }
        if(row == -1) {
            //try expanding the filter to all units
            choiceUnit.setSelectedIndex(0);
            for(int i=0; i< unitTable.getRowCount(); i++) {
                if(unitModel.getUnit(unitTable.convertRowIndexToModel(i)).getId().equals(id)) {
                    row = i;
                    break;
                }
            }
            
        }
        if(row != -1) {
            unitTable.setRowSelectionInterval(row, row);
            unitTable.scrollRectToVisible(unitTable.getCellRect(row, 0, true));
        }
        
    }
    
    public void focusOnPerson(UUID id) {
        if(null == id) {
            return;
        }
        tabMain.setSelectedIndex(3);
        int row = -1;
        for(int i=0; i< personnelTable.getRowCount(); i++) {
            if(personModel.getPerson(personnelTable.convertRowIndexToModel(i)).getId().equals(id)) {
                row = i;
                break;
            }
        }
        if(row == -1) {
            //try expanding the filter to all units
            choicePerson.setSelectedIndex(0);
            for(int i=0; i< personnelTable.getRowCount(); i++) {
                if(personModel.getPerson(personnelTable.convertRowIndexToModel(i)).getId().equals(id)) {
                    row = i;
                    break;
                }
            }
            
        }
        if(row != -1) {
            personnelTable.setRowSelectionInterval(row, row);
            personnelTable.scrollRectToVisible(personnelTable.getCellRect(row, 0, true));
        }
    }
    
    public void showNews(String headline, String article) {
        NewsReportDialog nrd = new NewsReportDialog(frame, headline, article);
        nrd.setVisible(true);
    }

    private void taskTabChanged() {
        filterTechs(false);
        updateTechTarget();
    }

    private void patientTableValueChanged() {
        updateAssignDoctorEnabled();
    }

    private void docTableValueChanged(javax.swing.event.ListSelectionEvent evt) {
        refreshPatientList();
        updateAssignDoctorEnabled();

    }

    private void PartsTableValueChanged(javax.swing.event.ListSelectionEvent evt) {
        filterTechs(true);
        updateTechTarget();
    }

    private void advanceDay() {
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
        if(nagShortMaintenance()) {
            return;
        }
        getCampaign().newDay();
        refreshServicedUnitList();
        refreshUnitList();
        refreshPersonnelList();
        refreshTaskList();
        refreshAcquireList();
        refreshTechsList();
        refreshPartsList();
        refreshPatientList();
        refreshDoctorsList();
        refreshCalendar();
        refreshLocation();
        refreshOrganization();
        refreshReport();
        refreshFunds();
        refreshFinancialTransactions();
        refreshCargo();
        panMap.repaint();
    }// GEN-LAST:event_btnAdvanceDayActionPerformed

    private boolean nagShortMaintenance() {
        if(!getCampaign().getCampaignOptions().checkMaintenance()) {
            return false;
        }
        Vector<Unit> notMaintained = new Vector<Unit>();
        int totalAstechMinutesNeeded = 0;
        for(Unit u : getCampaign().getUnits()) {
            if(u.requiresMaintenance() && null == u.getTech()) {
                notMaintained.add(u);
            } else {
                //only add astech minutes for non-crewed units
                if(null == u.getEngineer()) {
                    totalAstechMinutesNeeded += (u.getMaintenanceTime() * 6);
                }
            }
        }
        
        if(notMaintained.size() > 0) {
            if(0 != JOptionPane.showConfirmDialog(null,
                    "You have unmaintained units. Do you really wish to advance the day?", 
                    "Unmaintained Units",
                    JOptionPane.YES_NO_OPTION)) {
                return true;
            }
        }
        
        if(getCampaign().getAstechPoolMinutes() < totalAstechMinutesNeeded) {
            if(0 != JOptionPane.showConfirmDialog(null,
                    "You do not have enough astechs to provide for full maintenance. Do you wish to proceed?", 
                    "Astech shortage",
                    JOptionPane.YES_NO_OPTION)) {
                return true;
            }
        }
        
        return false;
    }
    
    private void assignDoctor() {
        Person doctor = getSelectedDoctor();
        for(Person p : getSelectedUnassignedPatients()) {
            if (null != p && null != doctor && (p.needsFixing() || (getCampaign().getCampaignOptions().useAdvancedMedical() && p.needsAMFixing()))
                    && getCampaign().getPatientsFor(doctor)<25
                    && getCampaign().getTargetFor(p, doctor).getValue() != TargetRoll.IMPOSSIBLE) {
                p.setDoctorId(doctor.getId(), getCampaign().getCampaignOptions().getHealingWaitingPeriod());
            }
        }

        refreshTechsList();
        refreshDoctorsList();
        refreshPatientList();
    }

    private void unassignDoctor() {
        for(Person p : getSelectedAssignedPatients()) {
            if ((null != p)) {
                p.setDoctorId(null, getCampaign().getCampaignOptions().getNaturalHealingWaitingPeriod());
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
                this, getIconPackage().getPortraits());
        npd.setVisible(true);
    }

    private void hirePersonMarket(java.awt.event.ActionEvent evt) {
        PersonnelMarketDialog pmd = new PersonnelMarketDialog(getFrame(), this,
                getCampaign(),
                getIconPackage().getPortraits());
        pmd.setVisible(true);
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
        case PG_CPILOT:
            return "Conventional Pilots";
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
        case SG_AMMO_BIN:
            return "Ammunition Bins";
        case SG_MISC:
            return "Miscellaneous Equipment";
        case SG_ENGINE:
            return "Engines";
        case SG_GYRO:
            return "Gyros";
        case SG_ACT:
            return "Actuators";
        default:
            return "?";
        }
    }
    
    public static String getPartsGroupViewName(int view) {
    	switch (view) {
    	case SV_ALL:
    		return "All";
    	case SV_IN_TRANSIT:
    		return "In Transit";
    	case SV_UNDAMAGED:
    		return "Undamaged";
    	case SV_DAMAGED:
    		return "Damaged";
    	default:
        	return "?";
    	}
    }

    public static String getPersonnelViewName(int group) {
        switch(group) {
        case PV_GRAPHIC:
            return "Graphic";
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
        case UV_GRAPHIC:
            return "Graphic";
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
        btnAddFunds.setEnabled(btnGMMode.isSelected());
    }// GEN-LAST:event_btnGMModeActionPerformed

    private void menuOptionsActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_menuOptionsActionPerformed
        CampaignOptionsDialog cod = new CampaignOptionsDialog(getFrame(), true,
                getCampaign(), getIconPackage().getCamos());
        cod.setVisible(true);
        refreshCalendar();
        getCampaign().reloadNews();
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


    private void miImportPersonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_miImportPersonActionPerformed
        try {
            loadPersonFile();
        } catch (IOException ex) {
            Logger.getLogger(CampaignGUI.class.getName()).log(Level.SEVERE, null,
                    ex);
        }
    }// GEN-LAST:event_miImportPersonActionPerformed


    private void miExportPersonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_miExportPersonActionPerformed
        try {
            savePersonFile();
        } catch (IOException ex) {
            Logger.getLogger(CampaignGUI.class.getName()).log(Level.SEVERE, null,
                    ex);
        }
    }// GEN-LAST:event_miExportPersonActionPerformed


    private void miImportPartsActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_miImportPersonActionPerformed
        try {
            loadPartsFile();
        } catch (IOException ex) {
            Logger.getLogger(CampaignGUI.class.getName()).log(Level.SEVERE, null,
                    ex);
        }
    }// GEN-LAST:event_miImportPersonActionPerformed


    private void miExportPartsActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_miExportPersonActionPerformed
        try {
            savePartsFile();
        } catch (IOException ex) {
            Logger.getLogger(CampaignGUI.class.getName()).log(Level.SEVERE, null,
                    ex);
        }
    }// GEN-LAST:event_miExportPersonActionPerformed

    private void miPurchaseUnitActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_miPurchaseUnitActionPerformed
        UnitSelectorDialog usd = new UnitSelectorDialog(getFrame(), this, getCampaign(), true);

        usd.setVisible(true);
        refreshServicedUnitList();
        refreshUnitList();
        refreshPersonnelList();
        refreshReport();
        refreshFunds();
        refreshFinancialTransactions();
        refreshCargo();
    }// GEN-LAST:event_miPurchaseUnitActionPerformed

    private void buyParts() {
        PartsStoreDialog psd = new PartsStoreDialog(true, this);
        psd.setVisible(true);
        refreshPartsList();
        refreshAcquireList();
        refreshCargo();
    }
    
    private void showNewLoanDialog() {
        NewLoanDialog nld = new NewLoanDialog(getFrame(), true, getCampaign());
        nld.setVisible(true);
        refreshFinancialTransactions();
        refreshFunds();
        refreshReport();
        refreshRating();
    }
    
    private void showMercRosterDialog() {
        MercRosterDialog mrd = new MercRosterDialog(getFrame(), true, getCampaign());
        mrd.setVisible(true);
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
                "Are you sure you want to refit "+r.getUnit().getName()+"?"
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
        } catch(IOException e) {
            JOptionPane.showMessageDialog(null,
                    e.getMessage(),
                    "IO Exception",
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
        refreshCargo();
    }
    
    private void showReport(Report report) {
        ReportDialog rd = new ReportDialog(getFrame(), report);
        rd.setVisible(true);
    }
    
    public void showMaintenanceReport(UUID id) {
        if(null == id) {
            return;
        }
        Unit u = getCampaign().getUnit(id);
        if(null == u) {
            return;
        }
        MaintenanceReportDialog mrd = new MaintenanceReportDialog(getFrame(), u);
        mrd.setVisible(true);
    }
    
    public UUID selectTech(Unit u, String desc) {
        String name;
        HashMap<String,Person> techHash = new HashMap<String,Person>();
        for(Person tech : getCampaign().getTechs()) {
            if(tech.canTech(u.getEntity())) {
                name = tech.getName() + ", " + SkillType.getExperienceLevelName(tech.getSkillForWorkingOn(u).getExperienceLevel());
                techHash.put(name, tech);
            }
        }
        if(techHash.isEmpty()) {
            JOptionPane.showMessageDialog(frame,
                    "You have no techs available.",
                    "No Techs",
                    JOptionPane.WARNING_MESSAGE);
            return null;
        }
        String[] techNames = new String[techHash.keySet().size()];
        int i = 0;
        for(String n : techHash.keySet()) {
            techNames[i] = n;
            i++;
        }
        String s = (String)JOptionPane.showInputDialog(
                frame,
                "Which tech should work on " + desc + "?",
                "Select Tech",
                JOptionPane.PLAIN_MESSAGE,
                null,
                techNames,
                techNames[0]);
        if(null == s) {
            return null;
        }
        return techHash.get(s).getId();
    }

    private void refreshPersonnelView() {
        int row = personnelTable.getSelectedRow();
        if(row < 0) {
            scrollPersonnelView.setViewportView(null);
            return;
        }
        Person selectedPerson = personModel.getPerson(personnelTable.convertRowIndexToModel(row));
        scrollPersonnelView.setViewportView(new PersonViewPanel(selectedPerson, getCampaign(), getIconPackage().getPortraits()));
        //This odd code is to make sure that the scrollbar stays at the top
        //I can't just call it here, because it ends up getting reset somewhere later
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
        scrollUnitView.setViewportView(new UnitViewPanel(selectedUnit, getCampaign(), getIconPackage().getCamos(), getIconPackage().getMechTiles()));
        //This odd code is to make sure that the scrollbar stays at the top
        //I can't just call it here, because it ends up getting reset somewhere later
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
        scrollScenarioView.setViewportView(new ScenarioViewPanel(scenario, getCampaign(), getIconPackage()));
        //This odd code is to make sure that the scrollbar stays at the top
        //I can't just call it here, because it ends up getting reset somewhere later
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
                tabUnit.add(name, new PersonViewPanel(p, getCampaign(), getIconPackage().getPortraits()));
            }
            tabUnit.add("Unit", new UnitViewPanel(u, getCampaign(), getIconPackage().getCamos(), getIconPackage().getMechTiles()));
            scrollForceView.setViewportView(tabUnit);
            //This odd code is to make sure that the scrollbar stays at the top
            //I can't just call it here, because it ends up getting reset somewhere later
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    scrollForceView.getVerticalScrollBar().setValue(0);
                }
            });
        } else if (node instanceof Force) {
            scrollForceView.setViewportView(new ForceViewPanel((Force)node, getCampaign(), getIconPackage()));
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
                getCampaign().addUnit(entity, allowNewPilots, 0);
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
        refreshCargo();
    }

    protected void loadPersonFile() throws IOException {
        JFileChooser loadList = new JFileChooser(".");
        loadList.setDialogTitle("Load Personnel");

        loadList.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File dir) {
                if (dir.isDirectory()) {
                    return true;
                }
                return dir.getName().endsWith(".prsx");
            }

            @Override
            public String getDescription() {
                return "Personnel file";
            }
        });

        int returnVal = loadList.showOpenDialog(mainPanel);

        if ((returnVal != JFileChooser.APPROVE_OPTION)
                || (loadList.getSelectedFile() == null)) {
            // I want a file, y'know!
            return;
        }

        File personnelFile = loadList.getSelectedFile();

        if (personnelFile != null) {
            // Open up the file.
            InputStream fis = new FileInputStream(personnelFile);
            
            MekHQ.logMessage("Starting load of personnel file from XML...");
    		// Initialize variables.
    		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    		Document xmlDoc = null;

    		try {
    			// Using factory get an instance of document builder
    			DocumentBuilder db = dbf.newDocumentBuilder();

    			// Parse using builder to get DOM representation of the XML file
    			xmlDoc = db.parse(fis);
    		} catch (Exception ex) {
    			MekHQ.logError(ex);
    		}

    		Element personnelEle = xmlDoc.getDocumentElement();
    		NodeList nl = personnelEle.getChildNodes();
    				
    		// Get rid of empty text nodes and adjacent text nodes...
    		// Stupid weird parsing of XML.  At least this cleans it up.
    		personnelEle.normalize(); 
    		
    		Version version = new Version(personnelEle.getAttribute("version"));

    		//we need to iterate through three times, the first time to collect
    		//any custom units that might not be written yet
    		for (int x = 0; x < nl.getLength(); x++) {
    			Node wn2 = nl.item(x);

    			// If it's not an element node, we ignore it.
    			if (wn2.getNodeType() != Node.ELEMENT_NODE)
    				continue;
    			
    			if (!wn2.getNodeName().equalsIgnoreCase("person")) {
    				// Error condition of sorts!
    				// Errr, what should we do here?
    				MekHQ.logMessage("Unknown node type not loaded in Personnel nodes: "+wn2.getNodeName());

    				continue;
    			}

    			Person p = Person.generateInstanceFromXML(wn2, version);
    			if(getCampaign().getPerson(p.getId()) != null && getCampaign().getPerson(p.getId()).getName().equals(p.getName())) {
    				MekHQ.logMessage("ERROR: Cannot load person who exists, ignoring. (Name: "+p.getName()+")");
    				p = null;
    			}
    			if (p != null) {
    				getCampaign().addPersonWithoutId(p, true);
    			}
    		}
    		MekHQ.logMessage("Finished load of personnel file");
        }

        refreshPersonnelList();
        refreshPatientList();
        refreshTechsList();
        refreshDoctorsList();
        refreshReport();
        refreshFinancialTransactions();
    }

    private void savePersonFile() throws IOException {
    	JFileChooser savePersonnel = new JFileChooser(".");
        savePersonnel.setDialogTitle("Save Personnel");
        savePersonnel.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File dir) {
                if (dir.isDirectory()) {
                    return true;
                }
                return dir.getName().endsWith(".prsx");
            }

            @Override
            public String getDescription() {
                return "Personnel file";
            }
        });
        savePersonnel.setSelectedFile(new File(getCampaign().getName()
                + getCampaign().getShortDateAsString() + "_ExportedPersonnel" + ".prsx")); //$NON-NLS-1$
        int returnVal = savePersonnel.showSaveDialog(mainPanel);

        if ((returnVal != JFileChooser.APPROVE_OPTION)
                || (savePersonnel.getSelectedFile() == null)) {
            // I want a file, y'know!
            return;
        }

        File file = savePersonnel.getSelectedFile();
        if (file == null) {
            // I want a file, y'know!
            return;
        }
        String path = file.getPath();
        if (!path.endsWith(".prsx")) {
            path += ".prsx";
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
        	int row = personnelTable.getSelectedRow();
            if(row < 0) {
            	MekHQ.logMessage("ERROR: Cannot export person if no one is selected! Ignoring.");
                return;
            }
            Person selectedPerson = personModel.getPerson(personnelTable.convertRowIndexToModel(row));
            int[] rows = personnelTable.getSelectedRows();
            Person[] people = new Person[rows.length];
            for(int i=0; i<rows.length; i++) {
                people[i] = personModel.getPerson(personnelTable.convertRowIndexToModel(rows[i]));
            }
            fos = new FileOutputStream(file);
            pw = new PrintWriter(new OutputStreamWriter(fos, "UTF-8"));

            // File header
    		pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

        	ResourceBundle resourceMap = ResourceBundle.getBundle("mekhq.resources.MekHQ");
    		// Start the XML root.
    		pw.println("<personnel version=\""
    				+resourceMap.getString("Application.version")
    				+"\">");

            if(rows.length > 1) {
            	for(int i=0; i<rows.length; i++) {
            		people[i].writeToXml(pw, 1);
            	}
            } else {
            	selectedPerson.writeToXml(pw, 1);
            }
            // Okay, we're done.
    		// Close everything out and be done with it.
    		pw.println("</personnel>");
            pw.flush();
            pw.close();
            fos.close();
            //delete the backup file because we didn't need it
            if(backupFile.exists()) {
                backupFile.delete();
            }
            MekHQ.logMessage("Personnel saved to " + file);
        } catch (Exception ex) {
            MekHQ.logError(ex);
            JOptionPane.showMessageDialog(getFrame(),
                    "Oh no! The program was unable to correctly export your personnel. We know this\n" +
                    "is annoying and apologize. Please help us out and submit a bug with the\n" +
                    "mekhqlog.txt file from this game so we can prevent this from happening in\n" +
                    "the future.",
                    "Could not export personnel",
                    JOptionPane.ERROR_MESSAGE);
            //restore the backup file
            file.delete();
            if(backupFile.exists()) {
                Utilities.copyfile(backupFile, file);
                backupFile.delete();
            }
        }
    }

    protected void loadPartsFile() throws IOException {
        JFileChooser loadList = new JFileChooser(".");
        loadList.setDialogTitle("Load Parts");

        loadList.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File dir) {
                if (dir.isDirectory()) {
                    return true;
                }
                return dir.getName().endsWith(".parts");
            }

            @Override
            public String getDescription() {
                return "Parts file";
            }
        });

        int returnVal = loadList.showOpenDialog(mainPanel);

        if ((returnVal != JFileChooser.APPROVE_OPTION)
                || (loadList.getSelectedFile() == null)) {
            // I want a file, y'know!
            return;
        }

        File partsFile = loadList.getSelectedFile();

        if (partsFile != null) {
            // Open up the file.
            InputStream fis = new FileInputStream(partsFile);
            
            MekHQ.logMessage("Starting load of parts file from XML...");
    		// Initialize variables.
    		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    		Document xmlDoc = null;

    		try {
    			// Using factory get an instance of document builder
    			DocumentBuilder db = dbf.newDocumentBuilder();

    			// Parse using builder to get DOM representation of the XML file
    			xmlDoc = db.parse(fis);
    		} catch (Exception ex) {
    			MekHQ.logError(ex);
    		}

    		Element partsEle = xmlDoc.getDocumentElement();
    		NodeList nl = partsEle.getChildNodes();
    				
    		// Get rid of empty text nodes and adjacent text nodes...
    		// Stupid weird parsing of XML.  At least this cleans it up.
    		partsEle.normalize(); 
    		
    		Version version = new Version(partsEle.getAttribute("version"));

    		//we need to iterate through three times, the first time to collect
    		//any custom units that might not be written yet
    		for (int x = 0; x < nl.getLength(); x++) {
    			Node wn2 = nl.item(x);

    			// If it's not an element node, we ignore it.
    			if (wn2.getNodeType() != Node.ELEMENT_NODE)
    				continue;
    			
    			if (!wn2.getNodeName().equalsIgnoreCase("part")) {
    				// Error condition of sorts!
    				// Errr, what should we do here?
    				MekHQ.logMessage("Unknown node type not loaded in Parts nodes: "+wn2.getNodeName());

    				continue;
    			}

    			Part p = Part.generateInstanceFromXML(wn2, version);
    			if (p != null) {
    				p.setCampaign(getCampaign());
    				getCampaign().addPartWithoutId(p);
    			}
    		}
    		MekHQ.logMessage("Finished load of parts file");
        }

        refreshPartsList();
        refreshReport();
        refreshFinancialTransactions();
    }

    private void savePartsFile() throws IOException {
    	JFileChooser saveParts = new JFileChooser(".");
        saveParts.setDialogTitle("Save Parts");
        saveParts.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File dir) {
                if (dir.isDirectory()) {
                    return true;
                }
                return dir.getName().endsWith(".parts");
            }

            @Override
            public String getDescription() {
                return "Parts file";
            }
        });
        saveParts.setSelectedFile(new File(getCampaign().getName()
                + getCampaign().getShortDateAsString() + "_ExportedParts" + ".parts")); //$NON-NLS-1$
        int returnVal = saveParts.showSaveDialog(mainPanel);

        if ((returnVal != JFileChooser.APPROVE_OPTION)
                || (saveParts.getSelectedFile() == null)) {
            // I want a file, y'know!
            return;
        }

        File file = saveParts.getSelectedFile();
        if (file == null) {
            // I want a file, y'know!
            return;
        }
        String path = file.getPath();
        if (!path.endsWith(".parts")) {
            path += ".parts";
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
        	int row = partsTable.getSelectedRow();
            if(row < 0) {
            	MekHQ.logMessage("ERROR: Cannot export parts if none are selected! Ignoring.");
                return;
            }
            Part selectedPart = partsModel.getPartAt(partsTable.convertRowIndexToModel(row));
            int[] rows = partsTable.getSelectedRows();
            Part[] parts = new Part[rows.length];
            for(int i=0; i<rows.length; i++) {
                parts[i] = partsModel.getPartAt(partsTable.convertRowIndexToModel(rows[i]));
            }
            fos = new FileOutputStream(file);
            pw = new PrintWriter(new OutputStreamWriter(fos, "UTF-8"));

            // File header
    		pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

        	ResourceBundle resourceMap = ResourceBundle.getBundle("mekhq.resources.MekHQ");
    		// Start the XML root.
    		pw.println("<parts version=\""
    				+resourceMap.getString("Application.version")
    				+"\">");

            if(rows.length > 1) {
            	for(int i=0; i<rows.length; i++) {
            		parts[i].writeToXml(pw, 1);
            	}
            } else {
            	selectedPart.writeToXml(pw, 1);
            }
            // Okay, we're done.
    		// Close everything out and be done with it.
    		pw.println("</parts>");
            pw.flush();
            pw.close();
            fos.close();
            //delete the backup file because we didn't need it
            if(backupFile.exists()) {
                backupFile.delete();
            }
            MekHQ.logMessage("Parts saved to " + file);
        } catch (Exception ex) {
            MekHQ.logError(ex);
            JOptionPane.showMessageDialog(getFrame(),
                    "Oh no! The program was unable to correctly export your parts. We know this\n" +
                    "is annoying and apologize. Please help us out and submit a bug with the\n" +
                    "mekhqlog.txt file from this game so we can prevent this from happening in\n" +
                    "the future.",
                    "Could not export parts",
                    JOptionPane.ERROR_MESSAGE);
            //restore the backup file
            file.delete();
            if(backupFile.exists()) {
                Utilities.copyfile(backupFile, file);
                backupFile.delete();
            }
        }
    }
    
    private void exportTable(JTable table, String suggestedName) {
        JFileChooser fileChooser = new JFileChooser(".");
        fileChooser.setDialogTitle("Save Table");
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File dir) {
                if (dir.isDirectory()) {
                    return true;
                }
                return dir.getName().endsWith(".csv");
            }

            @Override
            public String getDescription() {
                return "comma-separated text file";
            }
        });
        fileChooser.setSelectedFile(new File(suggestedName)); //$NON-NLS-1$
        int returnVal = fileChooser.showSaveDialog(mainPanel);

        if ((returnVal != JFileChooser.APPROVE_OPTION)
                || (fileChooser.getSelectedFile() == null)) {
            // I want a file, y'know!
            return;
        }

        File file = fileChooser.getSelectedFile();
        if (file == null) {
            // I want a file, y'know!
            return;
        }
        String path = file.getPath();
        if (!path.endsWith(".csv")) {
            path += ".csv";
            file = new File(path);
        }
        
        //check for existing file and make a back-up if found
        String path2 = path + "_backup";
        File backupFile = new File(path2);
        if(file.exists()) {     
            Utilities.copyfile(file, backupFile);
        }

        Utilities.exportTabletoCSV(table, file);
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
            refreshPersonnelList();
            refreshServicedUnitList();
            refreshUnitList();
            refreshOrganization();
            refreshTaskList();
            refreshUnitView();
            refreshPartsList();
            refreshAcquireList();
            refreshReport();
            refreshPatientList();
            refreshPersonnelList();
            refreshScenarioList();
            refreshCargo();
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
        refreshCargo();
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
            // TODO: Inoperable and Advanced Medical Checks
            if(u.isUnmanned() || !u.isFunctional()) {
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
        refreshCargo();

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
        personModel.refreshData();
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
                //I can't just call it here, because it ends up getting reset somewhere later
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
        acquireUnitsModel.setData(getCampaign().getShoppingList().getUnitList());
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
        int selected = techTable.getSelectedRow();
        techsModel.setData(getCampaign().getTechs());
        if ((selected > -1) && (selected < getCampaign().getTechs().size())) {
            techTable.setRowSelectionInterval(selected, selected);
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
        int selected = docTable.getSelectedRow();
        doctorsModel.setData(getCampaign().getDoctors());
        if ((selected > -1) && (selected < getCampaign().getDoctors().size())) {
            docTable.setRowSelectionInterval(selected, selected);
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
        getCampaign().getShoppingList().removeZeroQuantityFromList(); // To prevent zero quantity from hanging around
        acquirePartsModel.setData(getCampaign().getShoppingList().getPartList());
    }

    public void refreshFinancialTransactions() {
        financeModel.setData(getCampaign().getFinances().getAllTransactions());
        loanModel.setData(getCampaign().getFinances().getAllLoans());
        refreshFunds();
        refreshRating();
        refreshFinancialReport();
    }
    
    public void refreshFinancialReport() {
        areaNetWorth.setText(getCampaign().getFinancialReport());
        areaNetWorth.setCaretPosition(0);
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
    
    protected void refreshCargo() {
    	double cargoTonnage = getCampaign().getCargoTonnage(false);
    	double cargoTonnageInTransit = getCampaign().getCargoTonnage(true);
    	double cargoCapacity = getCampaign().getTotalCombinedCargoCapacity();
    	String cargoTonnageString = new DecimalFormat("#.##").format(cargoTonnage);
    	String cargoTonnageInTransitString = new DecimalFormat("#.##").format(cargoTonnageInTransit);
    	String cargoCapacityString = new DecimalFormat("#.##").format(cargoCapacity);
    	String color = cargoTonnage > cargoCapacity ? "<font color='red'>" : "<font color = 'black'>";
    	String text = "<html>"+color+"<b>Cargo Tonnage (+In Transit)/Capacity:</b> "+cargoTonnageString+" ("+cargoTonnageInTransitString+")/"+cargoCapacityString+"</font></html>";
    	lblCargo.setText(text);
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
            String text = "<html><b>Dragoons Rating:</b> " + getCampaign().getDragoonRating() + "</html>";
            lblRating.setText(text);
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
                if(null != u && u.isSelfCrewed()) {
                    tech = u.getEngineer();
                    if(null == tech) {
                        target = new TargetRoll(TargetRoll.IMPOSSIBLE, "You must have a crew assigned to large vessels to attempt repairs.");
                    }
                }
                if(null != tech) {
                	boolean wasNull = false;
					// Temporarily set the Team ID if it isn't already.
					// This is needed for the Clan Tech flag
                	if (part.getAssignedTeamId() == null) {
						part.setTeamId(tech.getId());
                		wasNull = true;
					}
                    target = getCampaign().getTargetFor(part, tech);
					if (wasNull) { // If it was null, make it null again
						part.setTeamId(null);
					}
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
                        (nGroup == PG_CPILOT && type == Person.T_CONV_PILOT) ||
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
        final int nGroupView = choicePartsView.getSelectedIndex();
        partsTypeFilter = new RowFilter<PartsTableModel,Integer>() {
            @Override
            public boolean include(Entry<? extends PartsTableModel, ? extends Integer> entry) {
                PartsTableModel partsModel = entry.getModel();
                Part part = partsModel.getPartAt(entry.getIdentifier());
                boolean inGroup = false;
                boolean inView = false;
                
                // Check grouping
                if(nGroup == SG_ALL) {
                    inGroup = true;
                } else if(nGroup == SG_ARMOR) {
                    inGroup = (part instanceof Armor || part instanceof ProtomekArmor || part instanceof BaArmor);
                } else if(nGroup == SG_SYSTEM) {
                    inGroup = part instanceof MekGyro
                        || part instanceof EnginePart
                        || part instanceof MekActuator
                        || part instanceof MekLifeSupport
                        || part instanceof MekSensor;
                } else if(nGroup == SG_EQUIP) {
                    inGroup = part instanceof EquipmentPart;
                } else if(nGroup == SG_LOC) {
                    inGroup = part instanceof MekLocation || part instanceof TankLocation;
                } else if(nGroup == SG_WEAP) {
                    inGroup = part instanceof EquipmentPart && ((EquipmentPart)part).getType() instanceof WeaponType;
                } else if(nGroup == SG_AMMO) {
                    inGroup = part instanceof EquipmentPart && !(part instanceof AmmoBin) && ((EquipmentPart)part).getType() instanceof AmmoType;
                } else if(nGroup == SG_AMMO_BIN) {
                    inGroup = part instanceof EquipmentPart && (part instanceof AmmoBin) && ((EquipmentPart)part).getType() instanceof AmmoType;
                } else if(nGroup == SG_MISC) {
                    inGroup = part instanceof EquipmentPart && ((EquipmentPart)part).getType() instanceof MiscType;
                } else if(nGroup == SG_ENGINE) {
                    inGroup = part instanceof EnginePart;
                } else if(nGroup == SG_GYRO) {
                    inGroup = part instanceof MekGyro;
                } else if(nGroup == SG_ACT) {
                    inGroup = part instanceof MekActuator;
                }
                
                // Check view
                if (nGroupView == SV_ALL) {
                	inView = true;
                } else if (nGroupView == SV_IN_TRANSIT) {
                	inView = !part.isPresent();
                } else if (nGroupView == SV_UNDAMAGED) {
                	inView = !part.needsFixing();
                } else if (nGroupView == SV_DAMAGED) {
                	inView = part.needsFixing();
                }
                return (inGroup && inView);
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
                if(part.getSkillMin() > SkillType.EXP_ELITE) {
                    return false;
                }
                return (getCampaign().getCampaignOptions().isDestroyByMargin() || part.getSkillMin() <= (skill.getExperienceLevel()-modePenalty));
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
        personnelTable.setRowHeight(15);
        
        
        //set the renderer
        TableColumn column = null;
        for (int i = 0; i < PersonnelTableModel.N_COL; i++) {
            column = columnModel.getColumnByModelIndex(i);
            column.setCellRenderer(personModel.getRenderer(choicePersonView.getSelectedIndex() == 0, getIconPackage()));
            if(i == PersonnelTableModel.COL_RANK) {
                if(view == PV_GRAPHIC) {
                    column.setPreferredWidth(125);
                    column.setHeaderValue("Person");
                } else {
                    column.setPreferredWidth(personModel.getColumnWidth(i));
                    column.setHeaderValue("Rank");
                }
            }
        }
        
        if(view == PV_GRAPHIC) {
            personnelTable.setRowHeight(80);
            columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_RANK), true);
            columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_NAME), false);
            columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_CALL), false);
            columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_AGE), false);
            columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_GENDER), false);
            columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_TYPE), false);
            columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_SKILL), false);
            columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_ASSIGN), true);
            columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_FORCE), true);
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
            columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_HITS), true);
            columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_SALARY), false);
            columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_KILLS), false);
            columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_XP), false);
        }
        else if(view == PV_GENERAL) {
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
            columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_KILLS), false);
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
            columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_KILLS), false);
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
            columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_KILLS), false);
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
            columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_KILLS), false);
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
            columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_KILLS), false);
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
            columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_KILLS), false);
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
            columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_KILLS), true);
            columnModel.setColumnVisible(columnModel.getColumnByModelIndex(PersonnelTableModel.COL_XP), false);
        }
    }

    private void changeUnitView() {

        int view = choiceUnitView.getSelectedIndex();
        XTableColumnModel columnModel = (XTableColumnModel)unitTable.getColumnModel();
        unitTable.setRowHeight(15);
              
        //set the renderer
        TableColumn column = null;
        for (int i = 0; i < UnitTableModel.N_COL; i++) {
            column = columnModel.getColumnByModelIndex(i);
            column.setCellRenderer(unitModel.getRenderer(choiceUnitView.getSelectedIndex() == 0, getIconPackage()));
            if(i == UnitTableModel.COL_WCLASS) {
                if(view == UV_GRAPHIC) {
                    column.setPreferredWidth(125);
                    column.setHeaderValue("Unit");
                } else {
                    column.setPreferredWidth(personModel.getColumnWidth(i));
                    column.setHeaderValue("Weight Class");
                }
            }
        }
        
        if(view == UV_GRAPHIC) {
            unitTable.setRowHeight(80);
            columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_NAME), false);
            columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_TYPE), false);
            columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_WCLASS), true);
            columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_TECH), false);
            columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_WEIGHT), false);
            columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_COST), false);
            columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_MAINTAIN), false);
            columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_QUALITY), false);
            columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_STATUS), false);
            columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_PILOT), true);
            columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_TECH_CRW), true);
            columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_CREW), false);
            columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_BV), false);
            columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_REPAIR), false);
            columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_PARTS), false);
            columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_QUIRKS), false);
        } 
        else if(view == UV_GENERAL) {
            columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_NAME), true);
            columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_TYPE), true);
            columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_WCLASS), true);
            columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_TECH), false);
            columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_WEIGHT), false);
            columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_COST), false);
            columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_MAINTAIN), false);
            columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_QUALITY), false);
            columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_STATUS), true);
            columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_PILOT), true);
            columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_TECH_CRW), true);
            columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_CREW), false);
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
            columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_TECH_CRW), false);
            columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_CREW), true);
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
            columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_TECH_CRW), false);
            columnModel.setColumnVisible(columnModel.getColumnByModelIndex(UnitTableModel.COL_CREW), true);
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

    public IconPackage getIconPackage() {
        return getApplication().getIconPackage();
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
        return tabMain.getTitleAt(tabMain.getSelectedIndex()).equals(resourceMap.getString("panSupplies.TabConstraints.tabTitle"));
    }

    public class TaskTableMouseAdapter extends MouseInputAdapter implements
            ActionListener {

        public void actionPerformed(ActionEvent action) {
            String command = action.getActionCommand();
            Part part = taskModel.getTaskAt(taskTable.convertRowIndexToModel(taskTable.getSelectedRow()));
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
                refreshCargo();
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
                refreshCargo();
            } else if (command.contains("UNASSIGN")) {
            /*  for (WorkItem task : tasks) {
                    task.resetTimeSpent();
                    task.setTeam(null);
                    refreshServicedUnitList();
                    refreshUnitList();
                    refreshTaskList();
                    refreshAcquireList();
                    refreshCargo();
                	refreshOverview();
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
                    refreshCargo();
                	refreshOverview();
                }*/
                if (part.checkFixable() == null) {
                    getCampaign().addReport(part.succeed());
                    
                    refreshServicedUnitList();
                    refreshUnitList();
                    refreshTaskList();
                    refreshAcquireList();
                    refreshPartsList();
                    refreshCargo();
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
                int row = taskTable.getSelectedRow();
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
    
    public class AcquisitionTableMouseAdapter extends MouseInputAdapter implements ActionListener {
        public void actionPerformed(ActionEvent action) {
            String command = action.getActionCommand();
            IAcquisitionWork acquisitionWork = acquireModel.getAcquisitionAt(acquisitionTable.convertRowIndexToModel(acquisitionTable.getSelectedRow()));
            if(acquisitionWork instanceof AmmoBin) {
            	acquisitionWork = ((AmmoBin) acquisitionWork).getAcquisitionWork();
            }
            if(null == acquisitionWork) {
                return;
            }
            if (command.contains("FIX")) {
                getCampaign().addReport(acquisitionWork.find(0));
                
                refreshServicedUnitList();
                refreshUnitList();
                refreshTaskList();
                refreshAcquireList();
                refreshPartsList();
                refreshCargo();
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
                int row = acquisitionTable.getSelectedRow();
                if(row < 0) {
                    return;
                }
                JMenuItem menuItem = null;
                JMenu menu = null;
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
                refreshCargo();
                refreshOverview();
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
                refreshCargo();
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
                refreshCargo();
            } else if (command.equalsIgnoreCase("SALVAGE")) {
                for (Unit unit : units) {
                    if (!unit.isDeployed()) {
                        unit.setSalvage(true);
                        unit.runDiagnostic();
                    }
                }
                refreshServicedUnitList();
                refreshUnitList();
                refreshCargo();
            } else if (command.equalsIgnoreCase("REPAIR")) {
                for (Unit unit : units) {
                    if (!unit.isDeployed() && unit.isRepairable()) {
                        unit.setSalvage(false);
                        unit.runDiagnostic();
                    }
                }
                refreshServicedUnitList();
                refreshUnitList();
                refreshCargo();
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
                refreshCargo();
            } else if (command.equalsIgnoreCase("UNDEPLOY")) {
                for (Unit unit : units) {
                    if (unit.isDeployed()) {
                        undeployUnit(unit);
                    }
                }
                refreshPersonnelList();
                refreshServicedUnitList();
                refreshUnitList();
                refreshOrganization();
                refreshTaskList();
                refreshUnitView();
                refreshPartsList();
                refreshAcquireList();
                refreshReport();
                refreshPatientList();
                refreshScenarioList();
                refreshCargo();
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
                menu.setEnabled(unit.isAvailable());
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
                menu.setEnabled(unit.isAvailable());
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
                menu.setEnabled(unit.isAvailable());
                popup.add(menu);
                // Salvage / Repair
                if (unit.isSalvage()) {
                    menuItem = new JMenuItem("Repair");
                    menuItem.setActionCommand("REPAIR");
                    menuItem.addActionListener(this);
                    menuItem.setEnabled(unit.isAvailable()
                            && unit.isRepairable());
                    popup.add(menuItem);
                } else {
                    menuItem = new JMenuItem("Salvage");
                    menuItem.setActionCommand("SALVAGE");
                    menuItem.addActionListener(this);
                    menuItem.setEnabled(unit.isAvailable());
                    popup.add(menuItem);
                }
                popup.show(e.getComponent(), e.getX(), e.getY());
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
                        refreshCargo();
                    }
                }
            } else if(command.contains("UNDEPLOY_FORCE")) {
                for(Force force : forces) {
                	undeployForce(force);
                }
                refreshOrganization();
                refreshPersonnelList();
                refreshUnitList();
                refreshServicedUnitList();
                refreshScenarioList();
                refreshCargo();
            } else if(command.contains("DEPLOY_FORCE")) {
                int sid = Integer.parseInt(target);
                Scenario scenario = getCampaign().getScenario(sid);
                for(Force force : forces) {
                	force.clearScenarioIds(getCampaign(), true);
                    if(null != force && null != scenario) {
                        scenario.addForces(force.getId());
                        force.setScenarioId(scenario.getId());
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
                refreshCargo();
            } else if(command.contains("CHANGE_ICON")) {
                if(null != singleForce) {
                    PortraitChoiceDialog pcd = new PortraitChoiceDialog(getFrame(), true,
                            singleForce.getIconCategory(),
                            singleForce.getIconFileName(), getIconPackage().getForceIcons());
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
                refreshUnitList();
                refreshCargo();
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
                refreshUnitList();
                refreshCargo();
            } else if(command.contains("UNDEPLOY_UNIT")) {
                for(Unit unit : units) {
                    undeployUnit(unit);
                }
                refreshScenarioList();
                refreshOrganization();
                refreshPersonnelList();
                refreshUnitList();
                refreshServicedUnitList();
                refreshCargo();
            } else if(command.contains("GOTO_UNIT")) {
                if(null != singleUnit) {
                    focusOnUnit(singleUnit.getId());
                }
            } else if(command.contains("GOTO_PILOT")) {
                if(null != singleUnit && null != singleUnit.getCommander()) {
                    focusOnPerson(singleUnit.getCommander().getId());
                }
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
                refreshCargo();
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
        
        private boolean areAllUnitsAvailable(Vector<Unit> units) {
            for(Unit unit : units) {
                if(!unit.isAvailable()) {
                    return false;
                }
            }
            return true;
        }

        private boolean areAllForcesDeployed(Vector<Force> forces) {
            for(Force force : forces) {
                if(!force.isDeployed()) {
                    return false;
                }
            }
            return true;
        }
        
        private boolean areAllUnitsDeployed(Vector<Unit> units) {
            for(Unit unit : units) {
                if(!unit.isDeployed()) {
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
                        menu.setEnabled(false);
                        //only add units that have commanders
                        // Or Gun Emplacements!
                        for(Unit u : getCampaign().getUnits()) {
                            if(null != u.getCommander()) {
                                Person p = u.getCommander();
                                if(p.isActive() && u.getForceId() < 1 && u.isPresent()) {
                                    menuItem = new JMenuItem(p.getFullTitle() + ", " + u.getName());
                                    menuItem.setActionCommand("ADD_UNIT|FORCE|"  + u.getId() + "|" + forceIds);
                                    menuItem.addActionListener(this);
                                    menuItem.setEnabled(u.isAvailable());
                                    menu.add(menuItem);
                                    menu.setEnabled(true);
                                }
                            }
                            if(u.getEntity() instanceof GunEmplacement) {
                                if(u.getForceId() < 1 && u.isPresent()) {
                                	menuItem = new JMenuItem("AutoTurret, " + u.getName());
                                    menuItem.setActionCommand("ADD_UNIT|FORCE|"  + u.getId() + "|" + forceIds);
                                    menuItem.addActionListener(this);
                                    menuItem.setEnabled(u.isAvailable());
                                    menu.add(menuItem);
                                    menu.setEnabled(true);
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
                        menu.setEnabled(false);
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
                                    menu.setEnabled(true);
                                }
                            }
                            menu.add(missionMenu);
                        }
                        popup.add(menu);
                    }
                    if(areAllForcesDeployed(forces)) {
                        menuItem = new JMenuItem("Undeploy Force");
                        menuItem.setActionCommand("UNDEPLOY_FORCE|FORCE|empty|" + forceIds);
                        menuItem.addActionListener(this);
                        menuItem.setEnabled(true);
                        popup.add(menuItem);
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
                    if(areAllUnitsAvailable(units)) {
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
                    if(areAllUnitsDeployed(units)) {
                        menuItem = new JMenuItem("Undeploy Unit");
                        menuItem.setActionCommand("UNDEPLOY_UNIT|UNIT|empty|" + unitIds);
                        menuItem.addActionListener(this);
                        menuItem.setEnabled(true);
                        popup.add(menuItem);
                    }
                    if (!multipleSelection) {
                        menuItem = new JMenuItem("Go to Unit in Hangar");
                        menuItem.setActionCommand("GOTO_UNIT|UNIT|empty|" + unitIds);
                        menuItem.addActionListener(this);
                        menuItem.setEnabled(true);
                        popup.add(menuItem);
                        menuItem = new JMenuItem("Go to Pilot/Commander in Personnel");
                        menuItem.setActionCommand("GOTO_PILOT|UNIT|empty|" + unitIds);
                        menuItem.addActionListener(this);
                        menuItem.setEnabled(true);
                        popup.add(menuItem);
                    }
                }
                popup.show(e.getComponent(), e.getX(), e.getY());
            }
        }

    }

    

    public class PersonnelTableMouseAdapter extends MouseInputAdapter implements
            ActionListener {

        public PersonnelTableMouseAdapter() {
            super();
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
                refreshCargo();
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
                refreshCargo();
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
                refreshCargo();
            } else if (command.contains("REMOVE_UNIT")) {
            	for(Person person : people) {
	                Unit u = getCampaign().getUnit(person.getUnitId());
	                if(null != u) {
	                    u.remove(person, true);
	                }
	                //check for tech unit assignments
	                if(!person.getTechUnitIDs().isEmpty()) {
	                    //I need to create a new array list to avoid concurrent problems
	                    ArrayList<UUID> temp = new ArrayList<UUID>();
	                    for(UUID i : person.getTechUnitIDs()) {
	                        temp.add(i);
	                    }
	                    for(UUID i : temp) {
	                        u = getCampaign().getUnit(i);
	                        if(null != u) {
	                            u.remove(person, true);
	                        }
	                    }             
	                }
            	}
                refreshServicedUnitList();
                refreshUnitList();
                refreshPersonnelList();
                refreshOrganization();
                refreshCargo();
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
                refreshCargo();
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
                refreshCargo();
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
                refreshCargo();
            } else if (command.contains("ADD_VESSEL_PILOT")) {
                UUID selected = UUID.fromString(st.nextToken());
                Unit u = getCampaign().getUnit(selected);
                if(null != u) {
                    for (Person p : people) {
                        if (u.canTakeMoreDrivers()) {
                            Unit oldUnit = getCampaign().getUnit(p.getUnitId());
                            if(null != oldUnit) {
                                oldUnit.remove(p, true);
                            }
                            u.addDriver(p);
                        }
                    }
                }
                u.resetPilotAndEntity();
                u.runDiagnostic();
                refreshServicedUnitList();
                refreshUnitList();
                refreshPersonnelList();
                refreshOrganization();
                refreshCargo();
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
                refreshCargo();
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
                refreshCargo();
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
                refreshCargo();
            } else if (command.contains("ADD_TECH")) {
                UUID selected = UUID.fromString(st.nextToken());
                Unit u = getCampaign().getUnit(selected);
                if(null != u) {
                    if(u.canTakeTech()) {
                        u.setTech(selectedPerson);
                    }
                }
                u.resetPilotAndEntity();
                u.runDiagnostic();
                refreshServicedUnitList();
                refreshUnitList();
                refreshPersonnelList();
                refreshOrganization();
                refreshCargo();
            } else if (command.contains("IMPROVE")) {
                String type = st.nextToken();
                int cost =  Integer.parseInt(st.nextToken());
                selectedPerson.improveSkill(type);
                getCampaign().personUpdated(selectedPerson);
                selectedPerson.setXp(selectedPerson.getXp() - cost);
                getCampaign().addReport(selectedPerson.getHyperlinkedName() + " improved " + type + "!");
                refreshServicedUnitList();
                refreshUnitList();
                refreshPersonnelList();
                refreshTechsList();
                refreshDoctorsList();
                refreshReport();
                refreshOrganization();
                refreshCargo();
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
                refreshCargo();
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
                refreshCargo();
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
                refreshCargo();
            } else if (command.equalsIgnoreCase("STATUS")) {
                int selected = Integer.parseInt(st.nextToken());
                for(Person person : people) {
                    if (selected == Person.S_ACTIVE
                            || (0 == JOptionPane.showConfirmDialog(
                                    null,
                                    "Do you really want to change the status of "
                                    + person.getFullTitle()
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
                refreshCargo();
            } else if (command.equalsIgnoreCase("PRISONER_STATUS")) {
                int selected = Integer.parseInt(st.nextToken());
                for(Person person : people) {
                    getCampaign().changePrisonerStatus(person, selected);
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
                refreshCargo();
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
                                    + person.getFullTitle() + "?",
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
                refreshCargo();
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
                refreshCargo();
            } else if (command.equalsIgnoreCase("HEAL")) {
                for(Person person : people) {
                    person.setHits(0);
                    person.setDoctorId(null, getCampaign().getCampaignOptions().getNaturalHealingWaitingPeriod());
                }
                getCampaign().personUpdated(selectedPerson);
                refreshPatientList();
                refreshDoctorsList();
                refreshServicedUnitList();
                refreshUnitList();
                refreshPersonnelList();
                refreshOrganization();
                refreshCargo();
            } else if (command.equalsIgnoreCase("PORTRAIT")) {
                PortraitChoiceDialog pcd = new PortraitChoiceDialog(getFrame(), true,
                        selectedPerson.getPortraitCategory(),
                        selectedPerson.getPortraitFileName(), getIconPackage().getPortraits());
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
            } else if(command.equalsIgnoreCase("COMMANDER")) {
            	selectedPerson.setCommander(!selectedPerson.isCommander());
            	if (selectedPerson.isCommander()) {
	            	for (Person p : getCampaign().getPersonnel()) {
	            		if (p.isCommander() && !p.getId().equals(selectedPerson.getId())) {
		            		p.setCommander(false);
		            		getCampaign().addReport(p.getHyperlinkedFullTitle()+" has been removed as the overall unit commander.");
		            		getCampaign().personUpdated(p);
	            		}
	            	}
	            	getCampaign().addReport(selectedPerson.getHyperlinkedFullTitle()+" has been set as the overall unit commander.");
	                getCampaign().personUpdated(selectedPerson);
            	}
                refreshReport();
            } else if(command.equalsIgnoreCase("DEPENDENT")) {
            	selectedPerson.setDependent(!selectedPerson.isDependent());
                getCampaign().personUpdated(selectedPerson);
            } else if(command.equalsIgnoreCase("CLANTECH")) {
            	selectedPerson.setIsClanTech(!selectedPerson.isClanTech());
                getCampaign().personUpdated(selectedPerson);
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
            } else if(command.equalsIgnoreCase("CLEAR_INJURIES")) {
            	for(Person person : people) {
            		person.clearInjuries();
                    Unit u = getCampaign().getUnit(person.getUnitId());
                    if(null != u) {
                        u.resetPilotAndEntity();
                    }
                }
                refreshPatientList();
                refreshPersonnelList();
            } else if(command.contains("REMOVE_INJURY")) {
                String sel = command.split(":")[1];
                Injury toRemove = null;
                for (Injury i : selectedPerson.getInjuries()) {
                	if (i.getUUIDAsString().equals(sel)) {
                		toRemove = i;
                		break;
                	}
                }
                if (toRemove != null) {
                	selectedPerson.removeInjury(toRemove);
                }
                Unit u = getCampaign().getUnit(selectedPerson.getUnitId());
                if(null != u) {
                    u.resetPilotAndEntity();
                }
                refreshPatientList();
                refreshPersonnelList();
            } else if (command.equalsIgnoreCase("EDIT_INJURIES")) {
                EditPersonnelInjuriesDialog epid = new EditPersonnelInjuriesDialog(getFrame(), true, getCampaign(), selectedPerson);
                epid.setVisible(true);
                refreshPatientList();
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

        private boolean areAllVesselGunners(Person[] people) {
            for (Person person : people) {
                if (Person.T_SPACE_GUNNER != person.getPrimaryRole()) {
                    return false;
                }
            }
            return true;
        }

        private boolean areAllVesselCrew(Person[] people) {
            for (Person person : people) {
                if (Person.T_SPACE_CREW != person.getPrimaryRole()) {
                    return false;
                }
            }
            return true;
        }

        private boolean areAllVesselPilots(Person[] people) {
            for (Person person : people) {
                if (Person.T_SPACE_PILOT != person.getPrimaryRole()) {
                    return false;
                }
            }
            return true;
        }

        private boolean areAllVesselNavigators(Person[] people) {
            for (Person person : people) {
                if (Person.T_NAVIGATOR != person.getPrimaryRole()) {
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
        
        private boolean areAllEligible(Person[] people) {
            for (Person person : people) {
                if (person.isPrisoner() || person.isBondsman()) {
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
                Person[] selected = getSelectedPeople();
                // **lets fill the pop up menu**//
                if (areAllEligible(selected)) {
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
                }
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
                menu = new JMenu("Change Prisoner Status");
                for(int s = 0; s < Person.PRISONER_NUM; s++) {
                    cbMenuItem = new JCheckBoxMenuItem(Person.getPrisonerStatusName(s));
                    if(person.getPrisonerStatus() == s) {
                        cbMenuItem.setSelected(true);
                    }
                    cbMenuItem.setActionCommand("PRISONER_STATUS|" + s);
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
                    if(i == Person.T_NONE || (person.canPerformRole(i) && person.getPrimaryRole() != i)) {
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
                menu = new JMenu("Assign to Unit");
                JMenu pilotMenu = new JMenu("As Pilot");
                JMenu crewMenu = new JMenu("As Crewmember");
                JMenu driverMenu = new JMenu("As Driver");
                JMenu gunnerMenu = new JMenu("As Gunner");
                JMenu soldierMenu = new JMenu("As Soldier");
                JMenu techMenu = new JMenu("As Tech");
                JMenu navMenu = new JMenu("As Navigator");
                cbMenuItem = new JCheckBoxMenuItem("None");
                /*if(!person.isAssigned()) {
                    cbMenuItem.setSelected(true);
                }*/
                cbMenuItem.setActionCommand("REMOVE_UNIT|" + -1);
                cbMenuItem.addActionListener(this);
                menu.add(cbMenuItem);
                if(oneSelected && person.isActive() && !(person.isPrisoner() || person.isBondsman())) {
                        for (Unit unit : getCampaign().getUnits()) {
                            if(!unit.isAvailable()) {
                                continue;
                            }
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
                            if(unit.canTakeTech() && person.canTech(unit.getEntity()) && (person.getMaintenanceTimeUsing(getCampaign()) + unit.getMaintenanceTime()) <= 480) {
                                cbMenuItem = new JCheckBoxMenuItem(unit.getName() + " (" + unit.getMaintenanceTime() + " minutes/day)");
                                //TODO: check the box
                                cbMenuItem.setActionCommand("ADD_TECH|" + unit.getId());
                                cbMenuItem.addActionListener(this);
                                techMenu.add(cbMenuItem);
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
                        if(techMenu.getItemCount() > 0) {
                            menu.add(techMenu);
                            if(techMenu.getItemCount() > 20) {
                                MenuScroller.setScrollerFor(techMenu, 20);
                            }
                        }
                        menu.setEnabled(!person.isDeployed(getCampaign()));
                        popup.add(menu);
                } else if (areAllActive(selected)  && areAllEligible(selected)) {
                    for (Unit unit : getCampaign().getUnits()) {
                        if(!unit.isAvailable()) {
                            continue;
                        }
	                    if (areAllInfantry(selected)) {
                        	if (!(unit.getEntity() instanceof Infantry) || unit.getEntity() instanceof BattleArmor) {
                        		continue;
                        	}
                            if(unit.canTakeMoreGunners() && person.canGun(unit.getEntity())) {
                                cbMenuItem = new JCheckBoxMenuItem(unit.getName());
                                //TODO: check the box
                                cbMenuItem.setActionCommand("ADD_SOLDIER|" + unit.getId());
                                cbMenuItem.addActionListener(this);
                                soldierMenu.add(cbMenuItem);
                            }
	                        menu.setEnabled(!person.isDeployed(getCampaign()));
	                        popup.add(menu);
	                    } else if (areAllBattleArmor(selected)) {
                        	if (!(unit.getEntity() instanceof BattleArmor)) {
                        		continue;
                        	}
                            if(unit.canTakeMoreGunners() && person.canGun(unit.getEntity())) {
                                cbMenuItem = new JCheckBoxMenuItem(unit.getName());
                                //TODO: check the box
                                cbMenuItem.setActionCommand("ADD_SOLDIER|" + unit.getId());
                                cbMenuItem.addActionListener(this);
                                soldierMenu.add(cbMenuItem);
                            }
	                        menu.setEnabled(!person.isDeployed(getCampaign()));
	                        popup.add(menu);
	                    } else if (areAllVeeGunners(selected)) {
                        	if (!(unit.getEntity() instanceof Tank)) {
                        		continue;
                        	}
                            if(unit.canTakeMoreGunners() && person.canGun(unit.getEntity())) {
                                cbMenuItem = new JCheckBoxMenuItem(unit.getName());
                                //TODO: check the box
                                cbMenuItem.setActionCommand("ADD_GUNNER|" + unit.getId());
                                cbMenuItem.addActionListener(this);
                                gunnerMenu.add(cbMenuItem);
                            }
	                    } else if (areAllVesselGunners(selected)) {
                        	if (!(unit.getEntity() instanceof Aero)) {
                        		continue;
                        	}
                            if(unit.canTakeMoreGunners() && person.canGun(unit.getEntity())) {
                                cbMenuItem = new JCheckBoxMenuItem(unit.getName());
                                //TODO: check the box
                                cbMenuItem.setActionCommand("ADD_GUNNER|" + unit.getId());
                                cbMenuItem.addActionListener(this);
                                gunnerMenu.add(cbMenuItem);
                            }
	                    } else if (areAllVesselCrew(selected)) {
                        	if (!(unit.getEntity() instanceof Aero)) {
                        		continue;
                        	}
                        	if(unit.canTakeMoreVesselCrew() && person.hasSkill(SkillType.S_TECH_VESSEL)) {
                                cbMenuItem = new JCheckBoxMenuItem(unit.getName());
                                //TODO: check the box
                                cbMenuItem.setActionCommand("ADD_CREW|" + unit.getId());
                                cbMenuItem.addActionListener(this);
                                crewMenu.add(cbMenuItem);
                            }
	                    } else if (areAllVesselPilots(selected)) {
                        	if (!(unit.getEntity() instanceof Aero)) {
                        		continue;
                        	}
                            if(unit.canTakeMoreDrivers() && person.canDrive(unit.getEntity())) {
                                cbMenuItem = new JCheckBoxMenuItem(unit.getName());
                                //TODO: check the box
                                cbMenuItem.setActionCommand("ADD_VESSEL_PILOT|" + unit.getId());
                                cbMenuItem.addActionListener(this);
                                pilotMenu.add(cbMenuItem);
                            }
	                    } else if (areAllVesselNavigators(selected)) {
                        	if (!(unit.getEntity() instanceof Aero)) {
                        		continue;
                        	}
                        	if(unit.canTakeNavigator() && person.hasSkill(SkillType.S_NAV)) {
                                cbMenuItem = new JCheckBoxMenuItem(unit.getName());
                                //TODO: check the box
                                cbMenuItem.setActionCommand("ADD_NAV|" + unit.getId());
                                cbMenuItem.addActionListener(this);
                                navMenu.add(cbMenuItem);
                            }
	                    }
                        if(soldierMenu.getItemCount() > 0) {
                            menu.add(soldierMenu);
                            if(soldierMenu.getItemCount() > 20) {
                                MenuScroller.setScrollerFor(soldierMenu, 20);
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
                    }
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
                        cbMenuItem = new JCheckBoxMenuItem("MASC Failures");
                        if (person.getOptions().booleanOption("edge_when_masc_fails")) {
                            cbMenuItem.setSelected(true);
                        }
                        cbMenuItem.setActionCommand("EDGE|edge_when_masc_fails");
                        cbMenuItem.addActionListener(this);
                        menu.add(cbMenuItem);
                        popup.add(menu);
                    }
                    menu = new JMenu("Special Flags...");
                    cbMenuItem = new JCheckBoxMenuItem("Dependent");
                    cbMenuItem.setSelected(person.isDependent());
                    cbMenuItem.setActionCommand("DEPENDENT");
                    cbMenuItem.addActionListener(this);
                    menu.add(cbMenuItem);
                    cbMenuItem = new JCheckBoxMenuItem("Commander");
                    cbMenuItem.setSelected(person.isCommander());
                    cbMenuItem.setActionCommand("COMMANDER");
                    cbMenuItem.addActionListener(this);
                    menu.add(cbMenuItem);
                    cbMenuItem = new JCheckBoxMenuItem("Clan Trained Technician");
                    cbMenuItem.setSelected(person.isClanTech());
                    cbMenuItem.setActionCommand("CLANTECH");
                    cbMenuItem.addActionListener(this);
                    if (person.isTech()) {
                    	menu.add(cbMenuItem);
                    }
                    popup.add(menu);
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
                menuItem = new JMenuItem("Export Personnel");
                menuItem.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        miExportPersonActionPerformed(evt);
                    }
                });
                menuItem.setEnabled(true);
                popup.add(menuItem);
                menu = new JMenu("GM Mode");
                menuItem = new JMenuItem("Remove Person");
                menuItem.setActionCommand("REMOVE");
                menuItem.addActionListener(this);
                menuItem.setEnabled(getCampaign().isGM());
                menu.add(menuItem);
                if (!getCampaign().getCampaignOptions().useAdvancedMedical()) {
	                menuItem = new JMenuItem("Heal Person");
	                menuItem.setActionCommand("HEAL");
	                menuItem.addActionListener(this);
	                menuItem.setEnabled(getCampaign().isGM());
	                menu.add(menuItem);
                }
                menuItem = new JMenuItem("Add XP");
                menuItem.setActionCommand("XP_ADD");
                menuItem.addActionListener(this);
                menuItem.setEnabled(getCampaign().isGM());
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
                if (getCampaign().getCampaignOptions().useAdvancedMedical()) {
	                menuItem = new JMenuItem("Remove All Injuries");
	                menuItem.setActionCommand("CLEAR_INJURIES");
	                menuItem.addActionListener(this);
	                menuItem.setEnabled(getCampaign().isGM());
	                menu.add(menuItem);
	                if (oneSelected) {
		                for (Injury i : person.getInjuries()) {
		                	menuItem = new JMenuItem("Remove Injury: "+i.getName());
			                menuItem.setActionCommand("REMOVE_INJURY:"+i.getUUIDAsString());
			                menuItem.addActionListener(this);
			                menuItem.setEnabled(getCampaign().isGM());
			                menu.add(menuItem);
		                }

		                menuItem = new JMenuItem("Edit Injuries");
		                menuItem.setActionCommand("EDIT_INJURIES");
		                menuItem.addActionListener(this);
		                menuItem.setEnabled(getCampaign().isGM());
		                menu.add(menuItem);
	                }
                }
                popup.addSeparator();
                popup.add(menu);
                popup.show(e.getComponent(), e.getX(), e.getY());
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
                refreshCargo();
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
                refreshCargo();
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
            } 
            else if (command.equalsIgnoreCase("CANCEL_ORDER")) {
                double refund = getCampaign().getCampaignOptions().GetCanceledOrderReimbursement();
                long refundAmount = 0;
                for(Part p : parts) {
                    if(null != p) {
                        refundAmount += (refund * p.getStickerPrice() * p.getQuantity());
                        getCampaign().removePart(p);
                    }
                }
                getCampaign().getFinances().credit(refundAmount, Transaction.C_EQUIP, "refund for cancelled equipmemt sale", getCampaign().getDate());
                refreshFinancialTransactions();
                refreshPartsList();
                refreshTaskList();
                refreshAcquireList();
                refreshReport();
                refreshCargo();
            }
            else if (command.equalsIgnoreCase("ARRIVE")) {
                for(Part p : parts) {
                    if(null != p) {
                        getCampaign().arrivePart(p);
                    }
                }
                refreshPartsList();
                refreshTaskList();
                refreshAcquireList();
                refreshReport();
                refreshCargo();
            }
            else if (command.equalsIgnoreCase("REMOVE")) {
                for(Part p : parts) {
                    if(null != p) {
                        getCampaign().removePart(p);
                    }
                }
                refreshPartsList();
                refreshTaskList();
                refreshAcquireList();
                refreshReport();
                refreshCargo();
            } else if (command.contains("CHANGE_MODE")) {
                String sel = command.split(":")[1];
                int selected = Integer.parseInt(sel);
                selectedPart.setMode(selected);
                refreshPartsList();
                refreshCargo();
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
        
        public boolean areAllPartsPresent(Part[] parts) {
            for(Part p : parts) {
                if(!p.isPresent()) {
                    return false;
                }
            }
            return true;
        }
        
        public boolean areAllPartsInTransit(Part[] parts) {
            for(Part p : parts) {
                if(p.isPresent()) {
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
                if(getCampaign().getCampaignOptions().canSellParts() && areAllPartsPresent(parts)) {
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
                if(oneSelected && part.needsFixing() && part.isPresent()) {
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
                if(areAllPartsInTransit(parts)) {
                    menuItem = new JMenuItem("Cancel This Delivery");
                    menuItem.setActionCommand("CANCEL_ORDER");
                    menuItem.addActionListener(this);
                    popup.add(menuItem);
                }
                menuItem = new JMenuItem("Export Parts");
                menuItem.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        miExportPartsActionPerformed(evt);
                    }
                });
                menuItem.setEnabled(true);
                popup.add(menuItem);
                // GM mode
                menu = new JMenu("GM Mode");
                if(areAllPartsInTransit(parts)) {
                    menuItem = new JMenuItem("Deliver Part Now");
                    menuItem.setActionCommand("ARRIVE");
                    menuItem.addActionListener(this);
                    menuItem.setEnabled(getCampaign().isGM());
                    menu.add(menuItem);
                }
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
    
    public class ProcurementTableMouseAdapter extends MouseInputAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }
        
        @Override
        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }
        
        @SuppressWarnings("serial")
		private void maybeShowPopup(MouseEvent e) {
            JPopupMenu popup = new JPopupMenu();
            JMenuItem menuItem;
            JMenu menu;
            final JTable table = (JTable)e.getSource();
            final ProcurementTableModel model = (ProcurementTableModel)table.getModel();
            if(table.getSelectedRow() < 0) {
                return;
            }
            if(table.getSelectedRowCount() == 0) {
                return;
            }
            final int row = table.convertRowIndexToModel(table.getSelectedRow());
            final int[] rows = table.getSelectedRows();
            final boolean oneSelected = table.getSelectedRowCount() == 1;
            if (e.isPopupTrigger()) {              
                // **lets fill the pop up menu**//
                // GM mode
                menu = new JMenu("GM Mode");
                
                menuItem = new JMenuItem("Procure single item now");
                menuItem.addActionListener(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(row < 0) {
                            return;
                        }
                        if (oneSelected) {
	                        IAcquisitionWork acquisition = model.getAcquisition(row);
	                        Object equipment = acquisition.getNewEquipment();
	                        if(equipment instanceof Part) {
	                            if(getCampaign().buyPart((Part)equipment, getCampaign().calculatePartTransitTime(0))) {
	                                getCampaign().addReport("<font color='Green'><b>" + acquisition.getAcquisitionName() + " found.</b></font>");
	                                acquisition.decrementQuantity();
	                            } else {
	                                getCampaign().addReport("<font color='red'><b>You cannot afford to purchase " + acquisition.getAcquisitionName() + "</b></font>");
	                            }
	                        }
	                        else if(equipment instanceof Entity) {
	                            if(getCampaign().buyUnit((Entity)equipment, getCampaign().calculatePartTransitTime(0))) {
	                                getCampaign().addReport("<font color='Green'><b>" + acquisition.getAcquisitionName() + " found.</b></font>");
	                                acquisition.decrementQuantity();
	                            } else {
	                                getCampaign().addReport("<font color='red'><b>You cannot afford to purchase " + acquisition.getAcquisitionName() + "</b></font>");
	                            }
	                        }
                        } else {
                        	for (int curRow : rows) {
                        		if (curRow < 0) {
                        			continue;
                        		}
                        		int row = table.convertRowIndexToModel(curRow);
    	                        IAcquisitionWork acquisition = model.getAcquisition(row);
    	                        Object equipment = acquisition.getNewEquipment();
    	                        if(equipment instanceof Part) {
    	                            if(getCampaign().buyPart((Part)equipment, getCampaign().calculatePartTransitTime(0))) {
    	                                getCampaign().addReport("<font color='Green'><b>" + acquisition.getAcquisitionName() + " found.</b></font>");
    	                                acquisition.decrementQuantity();
    	                            } else {
    	                                getCampaign().addReport("<font color='red'><b>You cannot afford to purchase " + acquisition.getAcquisitionName() + "</b></font>");
    	                            }
    	                        }
    	                        else if(equipment instanceof Entity) {
    	                            if(getCampaign().buyUnit((Entity)equipment, getCampaign().calculatePartTransitTime(0))) {
    	                                getCampaign().addReport("<font color='Green'><b>" + acquisition.getAcquisitionName() + " found.</b></font>");
    	                                acquisition.decrementQuantity();
    	                            } else {
    	                                getCampaign().addReport("<font color='red'><b>You cannot afford to purchase " + acquisition.getAcquisitionName() + "</b></font>");
    	                            }
    	                        }
                        	}
                        }
                        
                        refreshPartsList();
                        refreshUnitList();
                        refreshTaskList();
                        refreshAcquireList();    
                        refreshReport();
                        refreshCargo();
                    }                  
                });
                menuItem.setEnabled(getCampaign().isGM());
                menu.add(menuItem);
                menuItem = new JMenuItem("Procure all items now");
                menuItem.addActionListener(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(row < 0) {
                            return;
                        }
                        if (oneSelected) {
	                        IAcquisitionWork acquisition = model.getAcquisition(row);
	                        boolean canAfford = true;
	                        while(canAfford && acquisition.getQuantity() > 0) {
	                            Object equipment = acquisition.getNewEquipment();
	                            if(equipment instanceof Part) {
	                                if(getCampaign().buyPart((Part)equipment, getCampaign().calculatePartTransitTime(0))) {
	                                    getCampaign().addReport("<font color='Green'><b>" + acquisition.getAcquisitionName() + " found.</b></font>");
	                                    acquisition.decrementQuantity();
	                                } else {
	                                    getCampaign().addReport("<font color='red'><b>You cannot afford to purchase " + acquisition.getAcquisitionName() + "</b></font>");
	                                    canAfford = false;
	                                }
	                            }
	                            else if(equipment instanceof Entity) {
	                                if(getCampaign().buyUnit((Entity)equipment, getCampaign().calculatePartTransitTime(0))) {
	                                    getCampaign().addReport("<font color='Green'><b>" + acquisition.getAcquisitionName() + " found.</b></font>");
	                                    acquisition.decrementQuantity();
	                                } else {
	                                    getCampaign().addReport("<font color='red'><b>You cannot afford to purchase " + acquisition.getAcquisitionName() + "</b></font>");
	                                    canAfford = false;
	                                }
	                            }
	                        }
                        } else {
                        	for (int curRow : rows) {
                        		if (curRow < 0) {
                        			continue;
                        		}
                        		int row = table.convertRowIndexToModel(curRow);
                        		IAcquisitionWork acquisition = model.getAcquisition(row);
    	                        boolean canAfford = true;
    	                        while(canAfford && acquisition.getQuantity() > 0) {
    	                            Object equipment = acquisition.getNewEquipment();
    	                            if(equipment instanceof Part) {
    	                                if(getCampaign().buyPart((Part)equipment, getCampaign().calculatePartTransitTime(0))) {
    	                                    getCampaign().addReport("<font color='Green'><b>" + acquisition.getAcquisitionName() + " found.</b></font>");
    	                                    acquisition.decrementQuantity();
    	                                } else {
    	                                    getCampaign().addReport("<font color='red'><b>You cannot afford to purchase " + acquisition.getAcquisitionName() + "</b></font>");
    	                                    canAfford = false;
    	                                }
    	                            }
    	                            else if(equipment instanceof Entity) {
    	                                if(getCampaign().buyUnit((Entity)equipment, getCampaign().calculatePartTransitTime(0))) {
    	                                    getCampaign().addReport("<font color='Green'><b>" + acquisition.getAcquisitionName() + " found.</b></font>");
    	                                    acquisition.decrementQuantity();
    	                                } else {
    	                                    getCampaign().addReport("<font color='red'><b>You cannot afford to purchase " + acquisition.getAcquisitionName() + "</b></font>");
    	                                    canAfford = false;
    	                                }
    	                            }
    	                        }
                        	}
                        }
                        
                        refreshPartsList();
                        refreshUnitList();
                        refreshTaskList();
                        refreshAcquireList();    
                        refreshReport();
                        refreshCargo();
                    }                  
                });
                menuItem.setEnabled(getCampaign().isGM());
                menu.add(menuItem);
                menuItem = new JMenuItem("Clear From the List");
                menuItem.addActionListener(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(row < 0) {
                            return;
                        }
                        if (oneSelected) {
                        	model.removeRow(row);
                        } else {
                        	for (int curRow : rows) {
                        		if (curRow < 0) {
                        			continue;
                        		}
                        		int row = table.convertRowIndexToModel(curRow);
                        		model.removeRow(row);
                        	}
                        }
                        refreshPartsList();
                        refreshUnitList();
                        refreshTaskList();
                        refreshAcquireList();
                        refreshCargo();
                    }                  
                });
                menuItem.setEnabled(getCampaign().isGM());
                menu.add(menuItem);
                // end
                popup.addSeparator();
                popup.add(menu);
                popup.show(e.getComponent(), e.getX(), e.getY());
            }
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
                    refreshCargo();
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
    
   
    
    public class LoanTableMouseAdapter extends MouseInputAdapter implements ActionListener {

        public void actionPerformed(ActionEvent action) {
            String command = action.getActionCommand();
            int row = loanTable.getSelectedRow();
            if(row < 0) {
                return;
            }
            Loan selectedLoan = loanModel.getLoan(loanTable.convertRowIndexToModel(row));
            if(null == selectedLoan) {
                return;
            }
            if (command.equalsIgnoreCase("DEFAULT")) {
                if (0 == JOptionPane
                        .showConfirmDialog(
                                null,
                                "Defaulting on this loan will affect your unit rating the same as a contract breach.\nDo you wish to proceed?",
                                "Default on " + selectedLoan.getDescription() + "?", JOptionPane.YES_NO_OPTION)) {
                    PayCollateralDialog pcd = new PayCollateralDialog(getFrame(), true, getCampaign(), selectedLoan);
                    pcd.setVisible(true);
                    if(pcd.wasCancelled()) {
                        return;
                    }
                    getCampaign().getFinances().defaultOnLoan(selectedLoan, pcd.wasPaid());
                    if(pcd.wasPaid()) {    
                        for(UUID id : pcd.getUnits()) {
                            getCampaign().removeUnit(id);
                        }
                        for(int[] part : pcd.getParts()) {
                            Part p = getCampaign().getPart(part[0]);
                            if(null != p) {
                                int quantity = part[1];
                                while(quantity > 0 && p.getQuantity() > 0) {
                                    p.decrementQuantity();
                                    quantity--;
                                }
                            }
                        }
                    }
                    refreshFinancialTransactions();
                    refreshUnitList();
                    refreshReport();
                    refreshPartsList();
                    refreshCargo();
                }
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
                Loan loan = loanModel.getLoan(loanTable.convertRowIndexToModel(row));
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
                refreshCargo();
            } else if (command.contains("QUIRK")) {
                String sel = command.split(":")[1];
                    selectedUnit.acquireQuirk(sel, true);
                    refreshServicedUnitList();
                    refreshUnitList();
                    refreshTechsList();
                    refreshReport();
                    refreshCargo();
            } else if (command.contains("MAINTENANCE_REPORT")) {
                showMaintenanceReport(selectedUnit.getId());
            } else if (command.contains("ASSIGN")) {
                String sel = command.split(":")[1];
                UUID id = UUID.fromString(sel);
                Person tech = getCampaign().getPerson(id);
                if(null != tech) {
                    //remove any existing techs
                    if(null != selectedUnit.getTech()) {
                        selectedUnit.remove(selectedUnit.getTech(), true);
                    }
                    selectedUnit.setTech(tech);
                }
                refreshServicedUnitList();
                refreshUnitList();
                refreshTechsList();
                refreshPersonnelList();
                refreshReport();
                refreshCargo();
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
                refreshCargo();
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
                refreshCargo();
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
                refreshCargo();
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
                refreshCargo();
            } else if (command.equalsIgnoreCase("SALVAGE")) {
                for (Unit unit : units) {
                    if (!unit.isDeployed()) {
                        unit.setSalvage(true);
                        unit.runDiagnostic();
                    }
                }
                refreshServicedUnitList();
                refreshUnitList();
                refreshCargo();
            } else if (command.equalsIgnoreCase("REPAIR")) {
                for (Unit unit : units) {
                    if (!unit.isDeployed() && unit.isRepairable()) {
                        unit.setSalvage(false);
                        unit.runDiagnostic();
                    }
                }
                refreshServicedUnitList();
                refreshUnitList();
                refreshCargo();
            } else if (command.equalsIgnoreCase("TAG_CUSTOM")) {
                String sCustomsDir = "data/mechfiles/customs/";
        		String sCustomsDirCampaign = sCustomsDir+getCampaign().getName()+"/";
        	    File customsDir = new File(sCustomsDir);
        	    if(!customsDir.exists()) {
        	    	customsDir.mkdir();
        	    }
        	    File customsDirCampaign = new File(sCustomsDirCampaign);
        	    if(!customsDirCampaign.exists()) {
        	    	customsDir.mkdir();
        	    }
                for (Unit unit : units) {
                    String fileName = unit.getEntity().getChassis() + " " + unit.getEntity().getModel();
                    try {
            	        if (unit.getEntity() instanceof Mech) {
            			    //if this file already exists then don't overwrite it or we will end up with a bunch of copies
            				String fileOutName = sCustomsDir + File.separator + fileName + ".mtf";
                            String fileNameCampaign = sCustomsDirCampaign + File.separator + fileName + ".mtf";
                            if((new File(fileOutName)).exists() || (new File(fileNameCampaign)).exists()) {
                                JOptionPane.showMessageDialog(null,
                                        "A file already exists for this unit, cannot tag as custom. (Unit name and model)",
                                        "File Already Exists",
                                        JOptionPane.ERROR_MESSAGE);
                    			return;
                            }
            	            FileOutputStream out = new FileOutputStream(fileNameCampaign);
            	            PrintStream p = new PrintStream(out);
            	            p.println(((Mech) unit.getEntity()).getMtf());
            	            p.close();
            	            out.close();
            	        } else {
            			    //if this file already exists then don't overwrite it or we will end up with a bunch of copies
            				String fileOutName = sCustomsDir + File.separator + fileName + ".blk";
                            String fileNameCampaign = sCustomsDirCampaign + File.separator + fileName + ".blk";
                            if((new File(fileOutName)).exists() || (new File(fileNameCampaign)).exists()) {
                                JOptionPane.showMessageDialog(null,
                                        "A file already exists for this unit, cannot tag as custom. (Unit name and model)",
                                        "File Already Exists",
                                        JOptionPane.ERROR_MESSAGE);
                    			return;
                            }
            	            BLKFile.encode(fileNameCampaign, unit.getEntity());
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
                refreshCargo();
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
                refreshCargo();
            } else if (command.equalsIgnoreCase("UNDEPLOY")) {
                for (Unit unit : units) {
                    if (unit.isDeployed()) {
                        undeployUnit(unit);
                    }
                }
                refreshPersonnelList();
                refreshServicedUnitList();
                refreshUnitList();
                refreshOrganization();
                refreshTaskList();
                refreshUnitView();
                refreshPartsList();
                refreshAcquireList();
                refreshReport();
                refreshPatientList();
                refreshScenarioList();
                refreshCargo();
            } else if(command.contains("HIRE_FULL")) {
                for(Unit unit : units) {
                    getCampaign().hirePersonnelFor(unit.getId());
                }
                refreshServicedUnitList();
                refreshUnitList();
                refreshPersonnelList();
                refreshOrganization();
                refreshFinancialTransactions();
                refreshReport();
                refreshCargo();
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
                refreshCargo();
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
                        refreshCargo();
                    }
                }
            }
            else if (command.contains("REMOVE_INDI_CAMO")) {
            	selectedUnit.getEntity().setCamoCategory(null);
                selectedUnit.getEntity().setCamoFileName(null);
            }
            else if (command.contains("INDI_CAMO")) {
                String category = selectedUnit.getCamoCategory();
                if ("".equals(category)) {
                    category = Player.ROOT_CAMO;
                }
                CamoChoiceDialog ccd = new CamoChoiceDialog(getFrame(), true, category, selectedUnit.getCamoFileName(),
                        getCampaign().getColorIndex(), getIconPackage().getCamos());
                ccd.setLocationRelativeTo(getFrame());
                ccd.setVisible(true);

                if (ccd.clickedSelect() == true) {
                    selectedUnit.getEntity().setCamoCategory(ccd.getCategory());
                    selectedUnit.getEntity().setCamoFileName(ccd.getFileName());

                    refreshForceView();
                    refreshUnitView();
                }
            }
            else if (command.equalsIgnoreCase("CANCEL_ORDER")) {
                double refund = getCampaign().getCampaignOptions().GetCanceledOrderReimbursement();
                if(null != selectedUnit) {
                    long refundAmount = (long)(refund * selectedUnit.getBuyCost());
                    getCampaign().removeUnit(selectedUnit.getId());
                    getCampaign().getFinances().credit(refundAmount, Transaction.C_EQUIP, "refund for cancelled equipmemt sale", getCampaign().getDate());

                }
                refreshFinancialTransactions();
                refreshUnitList();
                refreshReport();
                refreshCargo();
            }
            else if (command.equalsIgnoreCase("ARRIVE")) {
                if(null != selectedUnit) {
                    selectedUnit.setDaysToArrival(0);
                }
                refreshUnitList();
                refreshReport();
                refreshCargo();
            } else if (command.equalsIgnoreCase("MOTHBALL")) {
                UUID id = null;
                if(!selectedUnit.isSelfCrewed()) {
                    id = selectTech(selectedUnit, "mothball");
                    if(null == id) {
                        return;
                    }
                }
                if(null != selectedUnit) {
                    selectedUnit.startMothballing(id);
                }
                refreshUnitList();
                refreshServicedUnitList();
                refreshReport();
                refreshCargo();
            } else if (command.equalsIgnoreCase("ACTIVATE")) {
                UUID id = null;
                if(!selectedUnit.isSelfCrewed()) {
                    id = selectTech(selectedUnit, "activation");
                    if(null == id) {
                        return;
                    }
                }
                if(null != selectedUnit) {
                    selectedUnit.startMothballing(id);
                }
                refreshUnitList();
                refreshServicedUnitList();
                refreshReport();
                refreshCargo();
            } else if (command.equalsIgnoreCase("CANCEL_MOTHBALL")) {
                if(null != selectedUnit) {
                    selectedUnit.setMothballTime(0);
                }
                refreshUnitList();
                refreshServicedUnitList();
                refreshReport();
                refreshCargo();
            } else if(command.equalsIgnoreCase("BOMBS")) {
                if(null != selectedUnit && selectedUnit.getEntity() instanceof Aero) {
                    BombsDialog dialog = new BombsDialog((Aero)selectedUnit.getEntity(), getCampaign(), frame);
                    dialog.setVisible(true);
                    refreshUnitList();
                }
            } else if(command.equalsIgnoreCase("QUIRKS")) {
                if(null != selectedUnit) {
                    QuirksDialog dialog = new QuirksDialog(selectedUnit.getEntity(), frame);
                    dialog.setVisible(true);
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
                if(oneSelected && !unit.isPresent()) {
                    menuItem = new JMenuItem("Cancel This Delivery");
                    menuItem.setActionCommand("CANCEL_ORDER");
                    menuItem.addActionListener(this);
                    popup.add(menuItem);
                    // GM mode
                    menu = new JMenu("GM Mode");
                    menuItem = new JMenuItem("Deliver Part Now");
                    menuItem.setActionCommand("ARRIVE");
                    menuItem.addActionListener(this);
                    menuItem.setEnabled(getCampaign().isGM());
                    menu.add(menuItem);
                    popup.addSeparator();
                    popup.add(menu);
                    popup.show(e.getComponent(), e.getX(), e.getY());
                    return;
                }
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
                menu.setEnabled(unit.isAvailable());
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
                    menu.setEnabled(unit.isAvailable());
                    if(menu.getItemCount() > 20) {
                        MenuScroller.setScrollerFor(menu, 20);
                    }
                    popup.add(menu);
                }
                //Select bombs.
                if (oneSelected && (unit.getEntity() instanceof Aero)) {
                    menuItem = new JMenuItem("Select Bombs");
                    menuItem.setActionCommand("BOMBS");
                    menuItem.addActionListener(this);
                    popup.add(menuItem);
                }
                // Salvage / Repair
                if(oneSelected && !(unit.getEntity() instanceof Infantry && !(unit.getEntity() instanceof BattleArmor))) {
                    menu = new JMenu("Repair Status");
                    menu.setEnabled(unit.isAvailable());
                    cbMenuItem = new JCheckBoxMenuItem("Repair");
                    if(!unit.isSalvage()) {
                        cbMenuItem.setSelected(true);
                    }
                    cbMenuItem.setActionCommand("REPAIR");
                    cbMenuItem.addActionListener(this);
                    cbMenuItem.setEnabled(unit.isAvailable() && unit.isRepairable());
                    menu.add(cbMenuItem);
                    cbMenuItem = new JCheckBoxMenuItem("Salvage");
                    if(unit.isSalvage()) {
                        cbMenuItem.setSelected(true);
                    }
                    cbMenuItem.setActionCommand("SALVAGE");
                    cbMenuItem.addActionListener(this);
                    cbMenuItem.setEnabled(unit.isAvailable());
                    menu.add(cbMenuItem);
                    popup.add(menu);
                }
                if(oneSelected  && !(unit.getEntity() instanceof Infantry && !(unit.getEntity() instanceof BattleArmor))) {
                    if(unit.isMothballing()) {
                        menuItem = new JMenuItem("Cancel Mothballing/Activation");
                        menuItem.setActionCommand("CANCEL_MOTHBALL");
                        menuItem.addActionListener(this);
                        menuItem.setEnabled(true);
                        popup.add(menuItem);
                    } else if(unit.isMothballed()) {
                        menuItem = new JMenuItem("Activate Unit");
                        menuItem.setActionCommand("ACTIVATE");
                        menuItem.addActionListener(this);
                        menuItem.setEnabled(!unit.isSelfCrewed() || null != unit.getEngineer());
                        popup.add(menuItem);
                    } else {
                        menuItem = new JMenuItem("Mothball Unit");
                        menuItem.setActionCommand("MOTHBALL");
                        menuItem.addActionListener(this);
                        menuItem.setEnabled(unit.isAvailable() && (!unit.isSelfCrewed() || null != unit.getEngineer()));
                        popup.add(menuItem);
                    } 
                }
                if(oneSelected && unit.requiresMaintenance() && !unit.isSelfCrewed() && unit.isAvailable()) {
                    menu = new JMenu("Assign Tech");
                    for(Person tech : getCampaign().getTechs()) {
                        if(tech.canTech(unit.getEntity()) 
                                && (tech.getMaintenanceTimeUsing(getCampaign()) + unit.getMaintenanceTime()) <= 480) {
                            String skillLvl = "Unknown";
                            if(null != tech.getSkillForWorkingOn(unit)) {
                                skillLvl = SkillType.getExperienceLevelName(tech.getSkillForWorkingOn(unit).getExperienceLevel());
                            }
                            cbMenuItem = new JCheckBoxMenuItem(tech.getFullTitle() + " (" + skillLvl + ", " + tech.getMaintenanceTimeUsing(getCampaign()) + "m)");                          
                            cbMenuItem.setActionCommand("ASSIGN:" + tech.getId());
                            cbMenuItem.setEnabled(true);
                            if(null != unit.getTechId() && unit.getTechId().equals(tech.getId())) {
                                cbMenuItem.setSelected(true);
                            } else {
                                cbMenuItem.addActionListener(this);
                            }
                            menu.add(cbMenuItem);
                        }
                    }
                    if(menu.getItemCount() > 0) {
                        popup.add(menu);
                        if(menu.getItemCount() > 20) {
                            MenuScroller.setScrollerFor(menu, 20);
                        }
                    }
                }
                if(oneSelected && unit.requiresMaintenance()) {
                    menuItem = new JMenuItem("Show Last Maintenance Report");
                    menuItem.setActionCommand("MAINTENANCE_REPORT");
                    menuItem.addActionListener(this);
                    popup.add(menuItem);
                }
                if(oneSelected && unit.getEntity() instanceof Infantry && !(unit.getEntity() instanceof BattleArmor)) {
                    menuItem = new JMenuItem("Disband");
                    menuItem.setActionCommand("DISBAND");
                    menuItem.addActionListener(this);
                    menuItem.setEnabled(unit.isAvailable());
                    popup.add(menuItem);
                }
                // Customize
                if(oneSelected && (unit.getEntity() instanceof Mech 
                        || unit.getEntity() instanceof Tank
                		|| unit.getEntity() instanceof Aero
                        || (unit.getEntity() instanceof Infantry && !(unit.getEntity() instanceof BattleArmor)))) {
                    menu = new JMenu("Customize");
                    menuItem = new JMenuItem("Choose Refit Kit...");
                    menuItem.setActionCommand("REFIT_KIT");
                    menuItem.addActionListener(this);
                    menuItem.setEnabled(unit.isAvailable()
                            && (unit.getEntity() instanceof megamek.common.Mech
                            		|| unit.getEntity() instanceof megamek.common.Tank
                            		|| unit.getEntity() instanceof megamek.common.Aero
                                    || (unit.getEntity() instanceof Infantry && !(unit.getEntity() instanceof BattleArmor))));
                    menu.add(menuItem);
                    menuItem = new JMenuItem("Customize in Mek Lab...");
                    menuItem.setActionCommand("CUSTOMIZE");
                    menuItem.addActionListener(this);
                    menuItem.setEnabled(unit.isAvailable()
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
                    menu.setEnabled(unit.isAvailable() && unit.isRepairable());
                    popup.add(menu);
                }
                //fill with personnel
                if(unit.getCrew().size() < unit.getFullCrewSize()) {
                    menuItem = new JMenuItem("Hire full complement");
                    menuItem.setActionCommand("HIRE_FULL");
                    menuItem.addActionListener(this);
                    menuItem.setEnabled(unit.isAvailable());
                    popup.add(menuItem);
                }
                // Camo
                if(oneSelected) {
                	if (!unit.isEntityCamo()) {
	                    menuItem = new JMenuItem(resourceMap.getString("customizeMenu.individualCamo.text"));
	                    menuItem.setActionCommand("INDI_CAMO");
	                    menuItem.addActionListener(this);
	                    menuItem.setEnabled(true);
	                    popup.add(menuItem);
                	} else {
	                    menuItem = new JMenuItem(resourceMap.getString("customizeMenu.removeIndividualCamo.text"));
	                    menuItem.setActionCommand("REMOVE_INDI_CAMO");
	                    menuItem.addActionListener(this);
	                    menuItem.setEnabled(true);
	                    popup.add(menuItem);
                	}
                }
                if(oneSelected && !getCampaign().isCustom(unit)) {
                    menuItem = new JMenuItem("Tag as a custom unit");
                    menuItem.setActionCommand("TAG_CUSTOM");
                    menuItem.addActionListener(this);
                    menuItem.setEnabled(true);
                    popup.add(menuItem);
                }
                if(oneSelected && getCampaign().getCampaignOptions().useQuirks()) {
                    menuItem = new JMenuItem("Edit Quirks");
                    menuItem.setActionCommand("QUIRKS");
                    menuItem.addActionListener(this);
                    popup.add(menuItem);
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
    
    private void undeployUnit(Unit u) {
    	Force f = getCampaign().getForce(u.getForceId());
    	if (f != null) {
        	undeployForce(f, false);
    	}
    	getCampaign().getScenario(u.getScenarioId()).removeUnit(u.getId());
    	u.undeploy();
    }
    
    private void undeployForce(Force f) {
    	undeployForce(f, true);
    }
    
    private void undeployForce(Force f, boolean killSubs) {
    	int sid = f.getScenarioId();
        Scenario scenario = getCampaign().getScenario(sid);
        if(null != f && null != scenario) {
        	f.clearScenarioIds(getCampaign(), killSubs);
        	scenario.removeForce(f.getId());
        	if (killSubs) {
	            for(UUID uid : f.getAllUnits()) {
	                Unit u = getCampaign().getUnit(uid);
	                if(null != u) {
	                	scenario.removeUnit(u.getId());
	                	u.undeploy();
	                }
	            }
        	}
            
            // We have to clear out the parents as well.
            Force parent = f;
            int prevId = f.getId();
            while ((parent = parent.getParentForce()) != null) {
            	if (parent.getScenarioId() == -1) {
            		break;
            	}
            	parent.clearScenarioIds(getCampaign(), false);
            	scenario.removeForce(parent.getId());
            	for (Force sub : parent.getSubForces()) {
            		if (sub.getId() == prevId) {
            			continue;
            		}
            		scenario.addForces(sub.getId());
            		sub.setScenarioId(scenario.getId());
            	}
            	prevId = parent.getId();
            }
        }
    }
}
