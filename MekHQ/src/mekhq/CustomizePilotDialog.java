/*
 * NewPilotDialog.java
 *
 * Created on July 16, 2009, 5:30 PM
 */

package mekhq;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.GregorianCalendar;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;

import megamek.client.ui.swing.DialogOptionComponent;
import megamek.client.ui.swing.DialogOptionListener;
import megamek.common.EquipmentType;
import megamek.common.Pilot;
import megamek.common.WeaponType;
import megamek.common.options.IOption;
import megamek.common.options.IOptionGroup;
import megamek.common.options.PilotOptions;
import mekhq.campaign.Campaign;
import mekhq.campaign.personnel.Person;
import mekhq.campaign.personnel.PilotPerson;

/**
 *
 * @author  Jay Lawson <jaylawson39 at yahoo.com>
 */
public class CustomizePilotDialog extends javax.swing.JDialog implements DialogOptionListener {

    /**
	 * This dialog is used to both hire new pilots and to edit existing ones
	 * 
	 */
	private static final long serialVersionUID = -6265589976779860566L;
	private Pilot pilot;
	private PilotPerson person;
    private ArrayList<DialogOptionComponent> optionComps = new ArrayList<DialogOptionComponent>();
    private PilotOptions options;
    private GregorianCalendar birthdate;
    private SimpleDateFormat dateFormat;
    private Frame frame;
    private boolean newHire;
    
    private Campaign campaign;
    
    private MekHQView hqView;

    /** Creates new form CustomizePilotDialog */
    public CustomizePilotDialog(java.awt.Frame parent, boolean modal, PilotPerson person, boolean hire, Campaign campaign, MekHQView view) {
        super(parent, modal);
        this.campaign = campaign;
        this.hqView = view;
        this.frame = parent;
        this.dateFormat = new SimpleDateFormat("MMMM d yyyy");
        this.person = person;
        this.newHire = hire;
        initializePilotAndOptions();
        setLocationRelativeTo(parent);
    }

    private void initializePilotAndOptions () {
        refreshPilotAndOptions();
    }

    private void refreshPilotAndOptions () {
    	this.pilot = person.getPilot();
    	this.birthdate = (GregorianCalendar)person.getBirthday().clone();
    	this.options = pilot.getOptions();		
    	getContentPane().removeAll();
    	initComponents();
    	refreshOptions();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        lblName = new javax.swing.JLabel();
        lblGender = new javax.swing.JLabel();
        lblBday = new javax.swing.JLabel();
        lblNickname = new javax.swing.JLabel();
        textName = new javax.swing.JTextField();
        textNickname = new javax.swing.JTextField();
        textGunnery = new javax.swing.JTextField();
        textPiloting = new javax.swing.JTextField();
        lblGunnery = new javax.swing.JLabel();
        lblPiloting = new javax.swing.JLabel();
        textInitB = new javax.swing.JTextField();
        textCommandB = new javax.swing.JTextField();
        textToughness = new javax.swing.JTextField();
        textArtillery = new javax.swing.JTextField();
        lblInitB = new javax.swing.JLabel();
        lblCommandB = new javax.swing.JLabel();
        lblToughness = new javax.swing.JLabel();
        lblArtillery = new javax.swing.JLabel();
        choiceType = new javax.swing.JComboBox();
        choiceGender = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        panOptions = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtBio = new javax.swing.JTextPane();
        panButtons = new javax.swing.JPanel();
        btnOk = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        btnRandomName = new javax.swing.JButton();
        btnDate = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(mekhq.MekHQApp.class).getContext().getResourceMap(CustomizePilotDialog.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N
        getContentPane().setLayout(new java.awt.GridBagLayout());

        DefaultComboBoxModel pilotTypeModel = new DefaultComboBoxModel();
        pilotTypeModel.addElement(PilotPerson.getTypeDesc(Person.T_MECHWARRIOR));
        pilotTypeModel.addElement(PilotPerson.getTypeDesc(Person.T_VEE_CREW));
        pilotTypeModel.addElement(PilotPerson.getTypeDesc(Person.T_AERO_PILOT));
        pilotTypeModel.addElement(PilotPerson.getTypeDesc(Person.T_PROTO_PILOT));
        pilotTypeModel.addElement(PilotPerson.getTypeDesc(Person.T_BA));
        choiceType.setModel(pilotTypeModel);
        choiceType.setMinimumSize(new java.awt.Dimension(200, 27));
        choiceType.setName("choiceType"); // NOI18N
        choiceType.setPreferredSize(new java.awt.Dimension(200, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        choiceType.setSelectedIndex(person.getType());
        getContentPane().add(choiceType, gridBagConstraints);
 
        DefaultComboBoxModel genderModel = new DefaultComboBoxModel();
        genderModel.addElement(Person.getGenderName(Person.G_MALE));
        genderModel.addElement(Person.getGenderName(Person.G_FEMALE));
        choiceGender.setModel(genderModel);
        choiceGender.setName("choiceGender"); // NOI18N
        choiceGender.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	if(isNewHire()) {
            		randomName();
            	}
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.NONE;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        choiceGender.setSelectedIndex(person.getGender());
        getContentPane().add(choiceGender, gridBagConstraints);
        
        btnDate.setText(getDateAsString());
        btnDate.setName("btnDate"); // NOI18N
        btnDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	btnDateActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(btnDate, gridBagConstraints);

        
        lblName.setText(resourceMap.getString("lblName.text")); // NOI18N
        lblName.setName("lblName"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        getContentPane().add(lblName, gridBagConstraints);

        lblGender.setText(resourceMap.getString("lblGender.text")); // NOI18N
        lblGender.setName("lblGender"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        getContentPane().add(lblGender, gridBagConstraints);

        lblBday.setText(resourceMap.getString("lblBday.text")); // NOI18N
        lblBday.setName("lblBday"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        getContentPane().add(lblBday, gridBagConstraints);

        
        lblNickname.setText(resourceMap.getString("lblNickname.text")); // NOI18N
        lblNickname.setName("lblNickname"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        getContentPane().add(lblNickname, gridBagConstraints);

        //textName.setText(pilot.getName());
        textName.setMinimumSize(new java.awt.Dimension(250, 28));
        textName.setName("textName"); // NOI18N
        textName.setPreferredSize(new java.awt.Dimension(250, 28));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        textName.setText(pilot.getName());
        getContentPane().add(textName, gridBagConstraints);

        btnRandomName.setText(resourceMap.getString("btnRandomName.text")); // NOI18N
        btnRandomName.setName("btnRandomName"); // NOI18N
        btnRandomName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	randomName();
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(btnRandomName, gridBagConstraints);
        
        textNickname.setText(pilot.getNickname());
        textNickname.setMinimumSize(new java.awt.Dimension(250, 28));
        textNickname.setName("textNickname"); // NOI18N
        textNickname.setPreferredSize(new java.awt.Dimension(250, 28));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(textNickname, gridBagConstraints);

        textGunnery.setText(Integer.toString(pilot.getGunnery()));
        textGunnery.setMinimumSize(new java.awt.Dimension(50, 28));
        textGunnery.setName("textGunnery"); // NOI18N
        textGunnery.setPreferredSize(new java.awt.Dimension(50, 28));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(textGunnery, gridBagConstraints);

        textPiloting.setText(Integer.toString(pilot.getPiloting()));
        textPiloting.setMinimumSize(new java.awt.Dimension(50, 28));
        textPiloting.setName("textPiloting"); // NOI18N
        textPiloting.setPreferredSize(new java.awt.Dimension(50, 28));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(textPiloting, gridBagConstraints);

        lblGunnery.setText(resourceMap.getString("lblGunnery.text")); // NOI18N
        lblGunnery.setName("lblGunnery"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        getContentPane().add(lblGunnery, gridBagConstraints);

        lblPiloting.setText(resourceMap.getString("lblPiloting.text")); // NOI18N
        lblPiloting.setName("lblPiloting"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        getContentPane().add(lblPiloting, gridBagConstraints);

    
        if(campaign.getCampaignOptions().useArtillery()) {
        	lblArtillery.setText(resourceMap.getString("lblArtillery.text")); // NOI18N
        	lblArtillery.setName("lblArtillery"); // NOI18N
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 7;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
            getContentPane().add(lblArtillery, gridBagConstraints);
        
            textArtillery.setText(Integer.toString(pilot.getArtillery()));
            textArtillery.setMinimumSize(new java.awt.Dimension(50, 28));
            textArtillery.setName("textArtillery"); // NOI18N
            textArtillery.setPreferredSize(new java.awt.Dimension(50, 28));
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 7;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
            getContentPane().add(textArtillery, gridBagConstraints);
        }
        
        if(campaign.getCampaignOptions().useInitBonus()) {
        	lblInitB.setText(resourceMap.getString("lblInitB.text")); // NOI18N
            lblInitB.setName("lblInitB"); // NOI18N
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 2;
            gridBagConstraints.gridy = 5;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
            getContentPane().add(lblInitB, gridBagConstraints);
        
            textInitB.setText(Integer.toString(pilot.getInitBonus()));
            textInitB.setMinimumSize(new java.awt.Dimension(50, 28));
            textInitB.setName("textInitB"); // NOI18N
            textInitB.setPreferredSize(new java.awt.Dimension(50, 28));
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 3;
            gridBagConstraints.gridy = 5;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
            getContentPane().add(textInitB, gridBagConstraints);
        }

        if(campaign.getCampaignOptions().useTactics()) {
        	lblCommandB.setText(resourceMap.getString("lblCommandB.text")); // NOI18N
            lblCommandB.setName("lblCommandB"); // NOI18N
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 2;
            gridBagConstraints.gridy = 6;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
            getContentPane().add(lblCommandB, gridBagConstraints);

        	textCommandB.setText(Integer.toString(pilot.getCommandBonus()));
        	textCommandB.setMinimumSize(new java.awt.Dimension(50, 28));
        	textCommandB.setName("textCommandB"); // NOI18N
        	textCommandB.setPreferredSize(new java.awt.Dimension(50, 28));
        	gridBagConstraints = new java.awt.GridBagConstraints();
        	gridBagConstraints.gridx = 3;
        	gridBagConstraints.gridy = 6;
        	gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        	getContentPane().add(textCommandB, gridBagConstraints);
        }
        
        if(campaign.getCampaignOptions().useToughness()) {
        	lblToughness.setText(resourceMap.getString("lblToughness.text")); // NOI18N
        	lblToughness.setName("lblToughness"); // NOI18N
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 2;
            gridBagConstraints.gridy = 7;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
            getContentPane().add(lblToughness, gridBagConstraints);
        
            textToughness.setText(Integer.toString(pilot.getToughness()));
            textToughness.setMinimumSize(new java.awt.Dimension(50, 28));
            textToughness.setName("textToughness"); // NOI18N
            textToughness.setPreferredSize(new java.awt.Dimension(50, 28));
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 3;
            gridBagConstraints.gridy = 7;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
            getContentPane().add(textToughness, gridBagConstraints);
        }
        
        jScrollPane2.setName("jScrollPane2"); // NOI18N

        txtBio.setName("txtBio"); // NOI18N
        txtBio.setText(person.getBiography());
        jScrollPane2.setViewportView(txtBio);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jScrollPane2, gridBagConstraints);

        
        jScrollPane1.setMinimumSize(new java.awt.Dimension(300, 500));
        jScrollPane1.setName("jScrollPane1"); // NOI18N
        jScrollPane1.setPreferredSize(new java.awt.Dimension(300, 500));

        panOptions.setName("panOptions"); // NOI18N

        org.jdesktop.layout.GroupLayout panOptionsLayout = new org.jdesktop.layout.GroupLayout(panOptions);
        panOptions.setLayout(panOptionsLayout);
        panOptionsLayout.setHorizontalGroup(
            panOptionsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 516, Short.MAX_VALUE)
        );
        panOptionsLayout.setVerticalGroup(
            panOptionsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 510, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(panOptions);

        if(campaign.getCampaignOptions().useAbilities() 
        		|| campaign.getCampaignOptions().useImplants()) {
        	gridBagConstraints = new java.awt.GridBagConstraints();
        	gridBagConstraints.gridx = 5;
        	gridBagConstraints.gridy = 0;
        	gridBagConstraints.gridheight = 9;
        	gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        	gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        	gridBagConstraints.weightx = 1.0;
        	gridBagConstraints.weighty = 1.0;
        	gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        	getContentPane().add(jScrollPane1, gridBagConstraints);
        }
        
        panButtons.setName("panButtons"); // NOI18N
        panButtons.setLayout(new java.awt.GridBagLayout());

        if(isNewHire()) {
        	btnOk.setText(resourceMap.getString("btnHire.text")); // NOI18N
        } else {
        	btnOk.setText(resourceMap.getString("btnOk.text")); // NOI18N
        }
        btnOk.setName("btnOk"); // NOI18N
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panButtons.add(btnOk, gridBagConstraints);

        btnClose.setText(resourceMap.getString("btnClose.text")); // NOI18N
        btnClose.setName("btnClose"); // NOI18N
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panButtons.add(btnClose, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        getContentPane().add(panButtons, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        setVisible(false);
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        int piloting = Integer.parseInt(textPiloting.getText());
        int gunnery = Integer.parseInt(textGunnery.getText());
        String name = textName.getText();
        String nick = textNickname.getText();      
        person.setPilot(pilot);
        person.setType(choiceType.getSelectedIndex());
        person.setBiography(txtBio.getText());
        person.setGender(choiceGender.getSelectedIndex());
        person.setBirthday(birthdate);              
        if(isNewHire()) {
        	pilot = new Pilot(name, gunnery, piloting);
            if(campaign.getCampaignOptions().useInitBonus()) {
            	int initb = Integer.parseInt(textInitB.getText());
            	pilot.setInitBonus(initb);
            }
            if(campaign.getCampaignOptions().useTactics()) {
            	int commandb = Integer.parseInt(textCommandB.getText());
            	pilot.setCommandBonus(commandb);
            }
            if(campaign.getCampaignOptions().useToughness()) {
            	int tough = Integer.parseInt(textToughness.getText());
            	pilot.setToughness(tough);
            }
            if(campaign.getCampaignOptions().useArtillery()) {
            	int artillery = Integer.parseInt(textArtillery.getText());
            	pilot.setArtillery(artillery);
            }
            pilot.setNickname(nick);
            setOptions(pilot);
            person.setPilot(pilot);
            campaign.addPerson(person);
        	hqView.refreshPersonnelList();
        	hqView.refreshPatientList();
    		hqView.refreshReport();
        	person = campaign.newPilotPerson(PilotPerson.T_MECHWARRIOR);
        	refreshPilotAndOptions();
        } else {
        	Pilot p = person.getPilot();
        	p.setName(name);
        	p.setGunnery(gunnery);
        	p.setPiloting(piloting);
        	p.setNickname(nick);
        	if(campaign.getCampaignOptions().useInitBonus()) {
            	int initb = Integer.parseInt(textInitB.getText());
            	p.setInitBonus(initb);
            }
            if(campaign.getCampaignOptions().useTactics()) {
            	int commandb = Integer.parseInt(textCommandB.getText());
            	p.setCommandBonus(commandb);
            }
            if(campaign.getCampaignOptions().useToughness()) {
            	int tough = Integer.parseInt(textToughness.getText());
            	p.setToughness(tough);
            }
            if(campaign.getCampaignOptions().useArtillery()) {
            	int artillery = Integer.parseInt(textArtillery.getText());
            	p.setArtillery(artillery);
            }
            setOptions(p);
        	hqView.refreshPersonnelList();
        	hqView.refreshPatientList();
        	hqView.refreshServicedUnitList();
        	setVisible(false);
        }
    }//GEN-LAST:event_btnOkActionPerformed

    private void randomName() {
		textName.setText(campaign.getRNG().generate(choiceGender.getSelectedIndex() == Person.G_FEMALE));
	}
    
    private boolean isNewHire() {
    	return newHire;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                CustomizePilotDialog dialog = new CustomizePilotDialog(new javax.swing.JFrame(), true, null, false, null, null);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    public void refreshOptions() {
        panOptions.removeAll();
        optionComps = new ArrayList<DialogOptionComponent>();

        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        panOptions.setLayout(gridbag);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 0, 0, 0);
        c.ipadx = 0;
        c.ipady = 0;

        for (Enumeration<IOptionGroup> i = options.getGroups(); i
        .hasMoreElements();) {
            IOptionGroup group = i.nextElement();

            if (group.getKey().equalsIgnoreCase(PilotOptions.LVL3_ADVANTAGES)
                    && !campaign.getCampaignOptions().useAbilities()) {
                continue;
            }

            if (group.getKey().equalsIgnoreCase(PilotOptions.MD_ADVANTAGES)
                    && !campaign.getCampaignOptions().useImplants()) {
                continue;
            }
            
            addGroup(group, gridbag, c);

            for (Enumeration<IOption> j = group.getOptions(); j
            .hasMoreElements();) {
                IOption option = j.nextElement();

                addOption(option, gridbag, c, true);
            }
        }
    }

    private void addGroup(IOptionGroup group, GridBagLayout gridbag,
            GridBagConstraints c) {
        JLabel groupLabel = new JLabel(group.getDisplayableName());

        gridbag.setConstraints(groupLabel, c);
        panOptions.add(groupLabel);
    }

    private void addOption(IOption option, GridBagLayout gridbag,
            GridBagConstraints c, boolean editable) {
        DialogOptionComponent optionComp = new DialogOptionComponent(this,
                option, editable);

        if ("weapon_specialist".equals(option.getName())) { //$NON-NLS-1$
            optionComp.addValue("None"); //$NON-NLS-1$
            //holy crap, do we really need to add every weapon?
            for (Enumeration<EquipmentType> i = EquipmentType.getAllTypes(); i.hasMoreElements();) {
                EquipmentType etype = i.nextElement();
                if(etype instanceof WeaponType) {
                    optionComp.addValue(etype.getName());
                }
            }
        }
        
        if ("specialist".equals(option.getName())) { //$NON-NLS-1$
            optionComp.addValue(Pilot.SPECIAL_NONE);
            optionComp.addValue(Pilot.SPECIAL_LASER);
            optionComp.addValue(Pilot.SPECIAL_BALLISTIC);
            optionComp.addValue(Pilot.SPECIAL_MISSILE);
            optionComp.setSelected(option.stringValue());
        }

        gridbag.setConstraints(optionComp, c);
        panOptions.add(optionComp);

        optionComps.add(optionComp);
    }

    private void setOptions(Pilot p) {
        IOption option;
        for (final Object newVar : optionComps) {
            DialogOptionComponent comp = (DialogOptionComponent) newVar;
            option = comp.getOption();
            if ((comp.getValue().equals("None"))) { // NON-NLS-$1
                p.getOptions().getOption(option.getName())
                .setValue("None"); // NON-NLS-$1
            } else {
                p.getOptions().getOption(option.getName())
                .setValue(comp.getValue());
            }
        }
    }
    
    private String getDateAsString() {
        return dateFormat.format(birthdate.getTime());
    }
    
    private void btnDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDateActionPerformed
        // show the date chooser
        DateChooser dc = new DateChooser(frame, birthdate);
        // user can eiter choose a date or cancel by closing
        if (dc.showDateChooser() == DateChooser.OK_OPTION) {
            birthdate = dc.getDate();
            btnDate.setText(getDateAsString());
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnOk;
    private javax.swing.JButton btnRandomName;
    private javax.swing.JButton btnDate;
    private javax.swing.JComboBox choiceType;
    private javax.swing.JComboBox choiceGender;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblCommandB;
    private javax.swing.JLabel lblGunnery;
    private javax.swing.JLabel lblArtillery;
    private javax.swing.JLabel lblToughness;
    private javax.swing.JLabel lblInitB;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblGender;
    private javax.swing.JLabel lblBday;
    private javax.swing.JLabel lblNickname;
    private javax.swing.JLabel lblPiloting;
    private javax.swing.JPanel panButtons;
    private javax.swing.JPanel panOptions;
    private javax.swing.JTextField textCommandB;
    private javax.swing.JTextField textGunnery;
    private javax.swing.JTextField textArtillery;
    private javax.swing.JTextField textToughness;
    private javax.swing.JTextField textInitB;
    private javax.swing.JTextField textName;
    private javax.swing.JTextField textNickname;
    private javax.swing.JTextField textPiloting;
    private javax.swing.JTextPane txtBio;
    // End of variables declaration//GEN-END:variables

    public void optionClicked(DialogOptionComponent arg0, IOption arg1, boolean arg2) {
        //IMplement me!!
    }

}
