/*
 * ResolveWizardAssignKillsDialog.java
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

package mekhq;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;

import megamek.common.Entity;
import mekhq.campaign.ResolveScenarioTracker;
import mekhq.campaign.Unit;
import mekhq.campaign.ResolveScenarioTracker.PersonStatus;
import mekhq.campaign.mission.Contract;
import mekhq.campaign.personnel.Person;

/**
 *
 * @author  Taharqa
 */
public class ResolveWizardAssignKillsDialog extends javax.swing.JDialog {
	private static final long serialVersionUID = -8038099101234445018L;
    
	private ResolveScenarioTracker tracker;
    
	private javax.swing.JPanel panButtons;
	private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnNext;
    private javax.swing.JScrollPane scrAssignKills;
	private javax.swing.JPanel panAssignKills;
    private javax.swing.JTextArea txtInstructions;
    private Hashtable<String, JComboBox> choices;

	
    /** Creates new form NewTeamDialog */
    public ResolveWizardAssignKillsDialog(java.awt.Frame parent, boolean modal, ResolveScenarioTracker t) {
        super(parent, modal);
        this.tracker = t;
        initComponents();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
    	java.awt.GridBagConstraints gridBagConstraints;

    	panButtons = new javax.swing.JPanel();
    	panAssignKills = new javax.swing.JPanel();
    	btnNext = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        scrAssignKills = new javax.swing.JScrollPane();
        txtInstructions = new javax.swing.JTextArea();
        choices = new Hashtable<String, JComboBox>();
        
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setName("Form"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(mekhq.MekHQApp.class).getContext().getResourceMap(ResolveWizardAssignKillsDialog.class);
        getContentPane().setLayout(new java.awt.GridBagLayout());
        
        setTitle(resourceMap.getString("title"));
        
        txtInstructions.setText(resourceMap.getString("txtInstructions.text"));
        txtInstructions.setName("txtInstructions");
        txtInstructions.setEditable(false);
        txtInstructions.setEditable(false);
        txtInstructions.setLineWrap(true);
        txtInstructions.setWrapStyleWord(true);
        txtInstructions.setBorder(BorderFactory.createCompoundBorder(
	   			 BorderFactory.createTitledBorder("Instructions"),
	   			 BorderFactory.createEmptyBorder(5,5,5,5)));
        txtInstructions.setMinimumSize(new Dimension(500,125));
        txtInstructions.setPreferredSize(new Dimension(500,125));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        getContentPane().add(txtInstructions, gridBagConstraints);
	
        panAssignKills.setName("panAssignKills");
        panAssignKills.setLayout(new GridBagLayout()); 
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.CENTER;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        panAssignKills.add(new JLabel("Kill"), gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.CENTER;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        panAssignKills.add(new JLabel("Claimed By"), gridBagConstraints);
        
        int i = 2;
        JLabel nameLbl;
        JComboBox comboAssign;
        DefaultComboBoxModel assignModel; 
        for(String killName : tracker.getKillCredits().keySet()) {
        	nameLbl = new JLabel(killName);
        	assignModel = new DefaultComboBoxModel();
        	assignModel.addElement(resourceMap.getString("none"));
        	int idx = 0;
        	int selected = 0;
    		for(Unit u : tracker.getUnits()) {	
    			idx++;
    			assignModel.addElement(u.getCommander().getFullTitle() + ", " + u.getEntity().getDisplayName());
    			if(u.getId() == tracker.getKillCredits().get(killName)) {
    				selected = idx;
    			}
    		}
        	comboAssign = new JComboBox(assignModel);
        	comboAssign.setSelectedIndex(selected);
        	choices.put(killName, comboAssign);
        	gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = i;
            gridBagConstraints.gridwidth = 1;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
            panAssignKills.add(nameLbl, gridBagConstraints);
            gridBagConstraints.gridx = 1;
            panAssignKills.add(comboAssign, gridBagConstraints);
            i++;
        }        
        scrAssignKills.setViewportView(panAssignKills);
        scrAssignKills.setMinimumSize(new Dimension(600,400));
        scrAssignKills.setPreferredSize(new Dimension(600,400));

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        getContentPane().add(scrAssignKills, gridBagConstraints);
        
        panButtons.setName("panButtons");
        panButtons.setLayout(new GridBagLayout());  
        
	   	 btnNext.setText(resourceMap.getString("btnNext.text")); // NOI18N
	   	 btnNext.setName("btnNext"); // NOI18N
	   	 btnNext.addActionListener(new java.awt.event.ActionListener() {
	   		 public void actionPerformed(java.awt.event.ActionEvent evt) {
	   			 btnNextActionPerformed(evt);
	   		 }
	   	 });
	   	 gridBagConstraints = new java.awt.GridBagConstraints();
	   	 gridBagConstraints.gridx = 0;
	   	 gridBagConstraints.gridy = 0;
	   	 gridBagConstraints.gridwidth = 1;
	   	 gridBagConstraints.weightx = 1.0;
	   	 gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
	   	 gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
	   	 panButtons.add(btnCancel, gridBagConstraints);
		
	   	 btnCancel.setText(resourceMap.getString("btnCancel.text")); // NOI18N
	   	 btnCancel.setName("btnClose"); // NOI18N
	   	 btnCancel.addActionListener(new java.awt.event.ActionListener() {
	   		 public void actionPerformed(java.awt.event.ActionEvent evt) {
	   			 btnCancelActionPerformed(evt);
	   		 }
	   	 });
	   	 gridBagConstraints = new java.awt.GridBagConstraints();
	   	 gridBagConstraints.gridx = 1;
	   	 gridBagConstraints.gridy = 0;
	   	 gridBagConstraints.gridwidth = 1;
	   	 gridBagConstraints.weightx = 0.0;
	   	 gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
	   	 gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
	   	 panButtons.add(btnNext, gridBagConstraints);
		
	   	 gridBagConstraints = new java.awt.GridBagConstraints();
	   	 gridBagConstraints.gridx = 0;
	   	 gridBagConstraints.gridy = 2;
	   	 gridBagConstraints.gridwidth = 2;
	   	 gridBagConstraints.weightx = 1.0;
	   	 gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	   	 gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
	   	 gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
	   	 getContentPane().add(panButtons, gridBagConstraints);
	   	 
	   	 pack();
    }

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {
    	this.setVisible(false);
    	for(String killName : tracker.getKillCredits().keySet()) {
    		if(choices.get(killName).getSelectedIndex() == 0) {
    			tracker.getKillCredits().put(killName, Entity.NONE);
    		} else {
	    		Unit u = tracker.getUnits().get(choices.get(killName).getSelectedIndex()-1);
	    		if(null != u) {
	    			tracker.getKillCredits().put(killName, u.getId());
	    		}
    		}
    	}
    	tracker.assignKills();
    	ResolveWizardFinalCheckDialog resolveDialog = new ResolveWizardFinalCheckDialog((Frame)getParent(), true, tracker);
    	resolveDialog.setVisible(true);
    }


    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {
    	this.setVisible(false);
    }
}
