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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
import mekhq.campaign.personnel.SkillType;

/**
 *
 * @author  Jay Lawson <jaylawson39 at yahoo.com>
 */
public class CustomizePersonDialog extends javax.swing.JDialog implements DialogOptionListener {

    /**
	 * This dialog is used to both hire new pilots and to edit existing ones
	 * 
	 */
	private static final long serialVersionUID = -6265589976779860566L;
	private Person person;
    private ArrayList<DialogOptionComponent> optionComps = new ArrayList<DialogOptionComponent>();
    private Hashtable<String, JSpinner> skillLvls = new Hashtable<String, JSpinner>();
    private Hashtable<String, JSpinner> skillBonus = new Hashtable<String, JSpinner>();
    private Hashtable<String, JLabel> skillValues = new Hashtable<String, JLabel>();
    private Hashtable<String, JCheckBox> skillChks = new Hashtable<String, JCheckBox>();
    private PilotOptions options;
    private GregorianCalendar birthdate;
    private SimpleDateFormat dateFormat;
    private Frame frame;
    private boolean newHire;
    
    private Campaign campaign;
    
    private MekHQView hqView;

    /** Creates new form CustomizePilotDialog */
    public CustomizePersonDialog(java.awt.Frame parent, boolean modal, Person person, boolean hire, Campaign campaign, MekHQView view) {
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
    	this.birthdate = (GregorianCalendar)person.getBirthday().clone();
    	this.options = person.getOptions();		
    	getContentPane().removeAll();
    	initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panDemog = new javax.swing.JPanel();
        tabStats = new javax.swing.JTabbedPane();
        lblName = new javax.swing.JLabel();
        lblGender = new javax.swing.JLabel();
        lblBday = new javax.swing.JLabel();
        lblAge = new javax.swing.JLabel();
        lblNickname = new javax.swing.JLabel();
        textName = new javax.swing.JTextField();
        textNickname = new javax.swing.JTextField();
        textToughness = new javax.swing.JTextField();
        lblToughness = new javax.swing.JLabel();
        choiceType = new javax.swing.JComboBox();
        choiceGender = new javax.swing.JComboBox();
        scrOptions = new javax.swing.JScrollPane();
        panOptions = new javax.swing.JPanel();
        scrSkills = new javax.swing.JScrollPane();
        panSkills = new javax.swing.JPanel();
        scrBio = new javax.swing.JScrollPane();
        txtBio = new javax.swing.JTextPane();
        panButtons = new javax.swing.JPanel();
        btnOk = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        btnRandomName = new javax.swing.JButton();
        btnDate = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(mekhq.MekHQApp.class).getContext().getResourceMap(CustomizePersonDialog.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N
        getContentPane().setLayout(new java.awt.GridBagLayout());

        panDemog.setLayout(new java.awt.GridBagLayout());
        DefaultComboBoxModel personTypeModel = new DefaultComboBoxModel();
        for(int i = 0; i < Person.T_NUM; i++) {
        	personTypeModel.addElement(Person.getRoleDesc(i));
        }
        choiceType.setModel(personTypeModel);
        choiceType.setMinimumSize(new java.awt.Dimension(200, 27));
        choiceType.setName("choiceType"); // NOI18N
        choiceType.setPreferredSize(new java.awt.Dimension(200, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        choiceType.setSelectedIndex(person.getPrimaryRole());
        choiceType.setEnabled(isNewHire());
        choiceType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	person = campaign.newPerson(choiceType.getSelectedIndex());
            	refreshPilotAndOptions();
            }
        });
        panDemog.add(choiceType, gridBagConstraints);
    
        lblName.setText(resourceMap.getString("lblName.text")); // NOI18N
        lblName.setName("lblName"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        panDemog.add(lblName, gridBagConstraints);

        textName.setMinimumSize(new java.awt.Dimension(150, 28));
        textName.setName("textName"); // NOI18N
        textName.setPreferredSize(new java.awt.Dimension(150, 28));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        textName.setText(person.getName());
        panDemog.add(textName, gridBagConstraints);

        btnRandomName.setText(resourceMap.getString("btnRandomName.text")); // NOI18N
        btnRandomName.setName("btnRandomName"); // NOI18N
        btnRandomName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	randomName();
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panDemog.add(btnRandomName, gridBagConstraints);
        
        lblNickname.setText(resourceMap.getString("lblNickname.text")); // NOI18N
        lblNickname.setName("lblNickname"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        panDemog.add(lblNickname, gridBagConstraints);
        
        textNickname.setText(person.getCallsign());
        textNickname.setName("textNickname"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        panDemog.add(textNickname, gridBagConstraints);
        
        lblGender.setText(resourceMap.getString("lblGender.text")); // NOI18N
        lblGender.setName("lblGender"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        panDemog.add(lblGender, gridBagConstraints);

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
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.NONE;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        choiceGender.setSelectedIndex(person.getGender());
        panDemog.add(choiceGender, gridBagConstraints);

        lblBday.setText(resourceMap.getString("lblBday.text")); // NOI18N
        lblBday.setName("lblBday"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        panDemog.add(lblBday, gridBagConstraints);

        btnDate.setText(getDateAsString());
        btnDate.setName("btnDate"); // NOI18N
        btnDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	btnDateActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panDemog.add(btnDate, gridBagConstraints);
        
        lblAge.setText(person.getAge(campaign.getCalendar()) + " years old"); // NOI18N
        lblAge.setName("lblAge"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        panDemog.add(lblAge, gridBagConstraints);
        
        if(campaign.getCampaignOptions().useToughness()) {
        	lblToughness.setText(resourceMap.getString("lblToughness.text")); // NOI18N
        	lblToughness.setName("lblToughness"); // NOI18N
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 5;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
            getContentPane().add(lblToughness, gridBagConstraints);
        
            textToughness.setText("");
            textToughness.setName("textToughness"); // NOI18N
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 5;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
            getContentPane().add(textToughness, gridBagConstraints);
        }
        
        scrBio.setName("jScrollPane2"); // NOI18N

        txtBio.setName("txtBio"); // NOI18N
        txtBio.setText(person.getBiography());
		txtBio.setBorder(BorderFactory.createTitledBorder("Biography"));
		scrBio.setMinimumSize(new java.awt.Dimension(300, 300));
		scrBio.setPreferredSize(new java.awt.Dimension(300, 300));
        scrBio.setViewportView(txtBio);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panDemog.add(scrBio, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
    	gridBagConstraints.gridx = 0;
    	gridBagConstraints.gridy = 0;
    	gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    	gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    	gridBagConstraints.weightx = 0.0;
    	gridBagConstraints.weighty = 1.0;
    	getContentPane().add(panDemog, gridBagConstraints);
       
        panSkills.setName("panSkills"); // NOI18N
        refreshSkills();
        scrSkills.setViewportView(panSkills);
        scrSkills.setMinimumSize(new java.awt.Dimension(500, 500));
        scrSkills.setPreferredSize(new java.awt.Dimension(500, 500));
    	  
        panOptions.setName("panOptions"); // NOI18N
        refreshOptions();
        scrOptions.setViewportView(panOptions);
        scrOptions.setMinimumSize(new java.awt.Dimension(500, 500));
        scrOptions.setPreferredSize(new java.awt.Dimension(500, 500));

        tabStats.addTab(resourceMap.getString("scrSkills.TabConstraints.tabTitle"),scrSkills); // NOI18N
        if(campaign.getCampaignOptions().useAbilities() 
        		|| campaign.getCampaignOptions().useEdge()
        		|| campaign.getCampaignOptions().useImplants()) {
        	tabStats.addTab(resourceMap.getString("scrOptions.TabConstraints.tabTitle"),scrOptions); // NOI18N
        }
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(tabStats, gridBagConstraints);
        
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
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        getContentPane().add(panButtons, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        setVisible(false);
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        
        person.setName(textName.getText());
        person.setPrimaryRole(choiceType.getSelectedIndex());
        person.setCallsign(textNickname.getText());   
        person.setBiography(txtBio.getText());
        person.setGender(choiceGender.getSelectedIndex());
        person.setBirthday(birthdate);  
        setSkills();
        setOptions();       
        if(isNewHire()) {
            campaign.addPerson(person);  
            hqView.refreshPersonnelList();
    		hqView.refreshTechsList();
        	hqView.refreshPatientList();
        	hqView.refreshDoctorsList();
    		hqView.refreshReport();
        	person = campaign.newPerson(choiceType.getSelectedIndex());
        	//set the skills based on current so we stay at those levels
        	setSkills();
        	refreshPilotAndOptions();
        } else {
        	hqView.refreshPersonnelList();
        	hqView.refreshPatientList();
    		hqView.refreshTechsList();
        	hqView.refreshDoctorsList();
    		hqView.refreshReport();
        	setVisible(false);
        }
    }//GEN-LAST:event_btnOkActionPerformed

    private void randomName() {
		textName.setText(campaign.getRNG().generate(choiceGender.getSelectedIndex() == Person.G_FEMALE));
	}
    
    private boolean isNewHire() {
    	return newHire;
    }

    public void refreshSkills() {
        panSkills.removeAll();
        
        JCheckBox chkSkill;
        JLabel lblName;
	    JLabel lblValue;
	    JLabel lblLevel;
	    JLabel lblBonus;
	    JSpinner spnLevel;
	    JSpinner spnBonus;

        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        panSkills.setLayout(gridbag);

        c.gridwidth = 1;
        c.fill = GridBagConstraints.NONE;
        c.insets = new java.awt.Insets(0, 10, 0, 0);
        c.gridx = 0;

        for(int i = 0; i < SkillType.getSkillList().length; i++) {
        	c.gridy = i;
        	c.gridx = 0;
        	final String type = SkillType.getSkillList()[i];
        	chkSkill = new JCheckBox();
        	chkSkill.setSelected(person.hasSkill(type));
        	skillChks.put(type, chkSkill);
        	chkSkill.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					changeSkillValue(type);
					changeValueEnabled(type);
				}
    		});
        	lblName = new JLabel(type);
        	lblValue = new JLabel();
    		if(person.hasSkill(type)) {
    			lblValue.setText(person.getSkill(type).toString());
    		} else {
    			lblValue.setText("-");
    		}
    		skillValues.put(type, lblValue);
    		lblLevel = new JLabel("Level:");
    		lblBonus = new JLabel("Bonus:");
    		int level = 0;
    		int bonus = 0;
    		if(person.hasSkill(type)) {
    			level = person.getSkill(type).getLevel();
    			bonus = person.getSkill(type).getBonus();
    		}
    		spnLevel = new JSpinner(new SpinnerNumberModel(level, 0, 10, 1));
    		spnLevel.addChangeListener(new ChangeListener() {
    			@Override
    			public void stateChanged(ChangeEvent evt) {
    				changeSkillValue(type);
    			}
    		});
    		spnLevel.setEnabled(chkSkill.isSelected());
    		spnBonus = new JSpinner(new SpinnerNumberModel(bonus, -8, 8, 1));
    		spnBonus.addChangeListener(new ChangeListener() {
    			@Override
    			public void stateChanged(ChangeEvent evt) {
    				changeSkillValue(type);
    			}
    		});
    		spnBonus.setEnabled(chkSkill.isSelected());
            skillLvls.put(type, spnLevel);
            skillBonus.put(type, spnBonus);
    		
            c.anchor = java.awt.GridBagConstraints.WEST;    
    		c.weightx = 0;
            panSkills.add(chkSkill, c);
            
            c.gridx = 1;
    		c.anchor = java.awt.GridBagConstraints.WEST;    
            panSkills.add(lblName, c);
            
            c.gridx = 2;
            c.anchor = java.awt.GridBagConstraints.CENTER;
            panSkills.add(lblValue, c);
    		
            c.gridx = 3;
            c.anchor = java.awt.GridBagConstraints.WEST;
            panSkills.add(lblLevel, c);
            
            c.gridx = 4;
            c.anchor = java.awt.GridBagConstraints.WEST;
            panSkills.add(spnLevel, c);
            
            c.gridx = 5;
            c.anchor = java.awt.GridBagConstraints.WEST;
            panSkills.add(lblBonus, c);
            
            c.gridx = 6;
            c.anchor = java.awt.GridBagConstraints.WEST;
            c.weightx = 1.0;
            panSkills.add(spnBonus, c);
        }
    }
    
    private void setSkills() {
    	for(int i = 0; i < SkillType.getSkillList().length; i++) {
        	final String type = SkillType.getSkillList()[i];
    		if(skillChks.get(type).isSelected()) {
    			int lvl = (Integer)skillLvls.get(type).getModel().getValue();
    			int b = (Integer)skillBonus.get(type).getModel().getValue();
    			person.addSkill(type, lvl, b);
    		} else {
    			person.removeSkill(type);
    		}
    	}
        IOption option;
        for (final Object newVar : optionComps) {
            DialogOptionComponent comp = (DialogOptionComponent) newVar;
            option = comp.getOption();
            if ((comp.getValue().equals("None"))) { // NON-NLS-$1
                person.getOptions().getOption(option.getName())
                .setValue("None"); // NON-NLS-$1
            } else {
                person.getOptions().getOption(option.getName())
                .setValue(comp.getValue());
            }
        }
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
            
            if (group.getKey().equalsIgnoreCase(PilotOptions.EDGE_ADVANTAGES)
                    && !campaign.getCampaignOptions().useEdge()) {
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

    private void setOptions() {
        IOption option;
        for (final Object newVar : optionComps) {
            DialogOptionComponent comp = (DialogOptionComponent) newVar;
            option = comp.getOption();
            if ((comp.getValue().equals("None"))) { // NON-NLS-$1
                person.getOptions().getOption(option.getName())
                .setValue("None"); // NON-NLS-$1
            } else {
                person.getOptions().getOption(option.getName())
                .setValue(comp.getValue());
            }
        }
    }
    
    private String getDateAsString() {
        return dateFormat.format(birthdate.getTime());
    }
    
    private void changeSkillValue(String type) {
    	if(!skillChks.get(type).isSelected()) {
    		skillValues.get(type).setText("-");
    		return;
    	}
		SkillType stype = SkillType.getType(type);
		int lvl = (Integer)skillLvls.get(type).getModel().getValue();
		int b = (Integer)skillBonus.get(type).getModel().getValue();
		int target = stype.getTarget() - lvl - b;
		if(stype.countUp()) {
			target = stype.getTarget() + lvl + b;
			skillValues.get(type).setText("+" + target);
		} else {
			skillValues.get(type).setText(target + "+");
		}
	}
    
    private void changeValueEnabled(String type) {
    	skillLvls.get(type).setEnabled(skillChks.get(type).isSelected());
    	skillBonus.get(type).setEnabled(skillChks.get(type).isSelected());
    }
    
    private void btnDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDateActionPerformed
        // show the date chooser
        DateChooser dc = new DateChooser(frame, birthdate);
        // user can eiter choose a date or cancel by closing
        if (dc.showDateChooser() == DateChooser.OK_OPTION) {
            birthdate = dc.getDate();
            btnDate.setText(getDateAsString());
            lblAge.setText(getAge() + " years old"); // NOI18N
        }
    }

    public int getAge() {
    	// Get age based on year
    	int age = campaign.getCalendar().get(Calendar.YEAR) - birthdate.get(Calendar.YEAR);

    	// Add the tentative age to the date of birth to get this year's birthday
    	GregorianCalendar tmpDate = (GregorianCalendar) birthdate.clone();
    	tmpDate.add(Calendar.YEAR, age);

    	// If this year's birthday has not happened yet, subtract one from age
    	if (campaign.getCalendar().before(tmpDate)) {
    	    age--;
    	}
    	return age;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnOk;
    private javax.swing.JButton btnRandomName;
    private javax.swing.JButton btnDate;
    private javax.swing.JComboBox choiceType;
    private javax.swing.JComboBox choiceGender;
    private javax.swing.JScrollPane scrOptions;
    private javax.swing.JScrollPane scrBio;
    private javax.swing.JScrollPane scrSkills;
    private javax.swing.JLabel lblToughness;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblGender;
    private javax.swing.JLabel lblBday;
    private javax.swing.JLabel lblAge;
    private javax.swing.JLabel lblNickname;
    private javax.swing.JPanel panButtons;
    private javax.swing.JPanel panDemog;
    private javax.swing.JTabbedPane tabStats;
    private javax.swing.JPanel panSkills;
    private javax.swing.JPanel panOptions;
    private javax.swing.JTextField textToughness;
    private javax.swing.JTextField textName;
    private javax.swing.JTextField textNickname;
    private javax.swing.JTextPane txtBio;
    // End of variables declaration//GEN-END:variables

    public void optionClicked(DialogOptionComponent arg0, IOption arg1, boolean arg2) {
        //IMplement me!!
    }

}
