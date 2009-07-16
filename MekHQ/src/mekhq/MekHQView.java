/*
 * MekBayView.java
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

import java.awt.Font;
import mekhq.campaign.SupportTeam;
import mekhq.campaign.Campaign;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionEvent;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import megamek.client.ui.MechView;
import megamek.common.Entity;
import megamek.common.EntityListFile;
import mekhq.campaign.Unit;
import mekhq.campaign.work.ReloadItem;
import mekhq.campaign.work.RepairItem;
import mekhq.campaign.work.WorkItem;

/**
 * The application's main frame.
 */
public class MekHQView extends FrameView {

    
    private Campaign campaign = new Campaign();
    private DefaultListModel unitsModel = new DefaultListModel();
    private DefaultListModel taskModel = new DefaultListModel();
    private DefaultListModel teamsModel = new DefaultListModel();
    private int currentUnitId;
    private int currentTaskId;
    private int currentTeamId;
    
    private TaskReportDialog trd;
    
    
    public MekHQView(SingleFrameApplication app) {
        super(app);

        initComponents();

        trd = new TaskReportDialog(this.getFrame(), false);
        
        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String)(evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer)(evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = MekHQApp.getApplication().getMainFrame();
            aboutBox = new MekHQAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        MekHQApp.getApplication().show(aboutBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        btnAdvanceDay = new javax.swing.JButton();
        tabMain = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        lblUnits = new javax.swing.JLabel();
        UnitsScroll = new javax.swing.JScrollPane();
        UnitList = new javax.swing.JList();
        lblTasks = new javax.swing.JLabel();
        btnViewUnit = new javax.swing.JButton();
        btnDeployUnits = new javax.swing.JButton();
        loadListBtn = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        taskScroll = new javax.swing.JScrollPane();
        TaskList = new javax.swing.JList();
        ammoBtn = new javax.swing.JButton();
        replaceBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        TeamsList = new javax.swing.JList();
        assignBtn = new javax.swing.JButton();
        lblTeams = new javax.swing.JLabel();
        btnNewTeam = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        menuLoad = new javax.swing.JMenuItem();
        menuSave = new javax.swing.JMenuItem();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();

        mainPanel.setAutoscrolls(true);
        mainPanel.setName("mainPanel"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(mekhq.MekHQApp.class).getContext().getResourceMap(MekHQView.class);
        btnAdvanceDay.setText(resourceMap.getString("btnAdvanceDay.text")); // NOI18N
        btnAdvanceDay.setToolTipText(resourceMap.getString("btnAdvanceDay.toolTipText")); // NOI18N
        btnAdvanceDay.setName("btnAdvanceDay"); // NOI18N
        btnAdvanceDay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdvanceDayActionPerformed(evt);
            }
        });

        tabMain.setName("tabMain"); // NOI18N

        jPanel1.setName("jPanel1"); // NOI18N

        lblUnits.setText(resourceMap.getString("lblUnits.text")); // NOI18N
        lblUnits.setName("lblUnits"); // NOI18N

        UnitsScroll.setName("UnitsScroll"); // NOI18N

        UnitList.setBackground(resourceMap.getColor("UnitList.background")); // NOI18N
        UnitList.setModel(unitsModel);
        UnitList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        UnitList.setName("UnitList"); // NOI18N
        UnitList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                UnitListValueChanged(evt);
            }
        });
        UnitsScroll.setViewportView(UnitList);

        lblTasks.setText(resourceMap.getString("lblTasks.text")); // NOI18N
        lblTasks.setName("lblTasks"); // NOI18N

        btnViewUnit.setText(resourceMap.getString("btnViewUnit.text")); // NOI18N
        btnViewUnit.setToolTipText(resourceMap.getString("btnViewUnit.toolTipText")); // NOI18N
        btnViewUnit.setName("btnViewUnit"); // NOI18N
        btnViewUnit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewUnitActionPerformed(evt);
            }
        });

        btnDeployUnits.setText(resourceMap.getString("btnDeployUnits.text")); // NOI18N
        btnDeployUnits.setToolTipText(resourceMap.getString("btnDeployUnits.toolTipText")); // NOI18N
        btnDeployUnits.setName("btnDeployUnits"); // NOI18N
        btnDeployUnits.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeployUnitsActionPerformed(evt);
            }
        });

        loadListBtn.setText(resourceMap.getString("loadListBtn.text")); // NOI18N
        loadListBtn.setToolTipText(resourceMap.getString("loadListBtn.toolTipText")); // NOI18N
        loadListBtn.setName("loadListBtn"); // NOI18N
        loadListBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadListBtnActionPerformed(evt);
            }
        });

        jTabbedPane1.setName("jTabbedPane1"); // NOI18N

        taskScroll.setName("taskScroll"); // NOI18N

        TaskList.setModel(taskModel);
        TaskList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        TaskList.setName("TaskList"); // NOI18N
        TaskList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                TaskListValueChanged(evt);
            }
        });
        taskScroll.setViewportView(TaskList);

        ammoBtn.setText(resourceMap.getString("ammoBtn.text")); // NOI18N
        ammoBtn.setToolTipText(resourceMap.getString("ammoBtn.toolTipText")); // NOI18N
        ammoBtn.setEnabled(false);
        ammoBtn.setName("ammoBtn"); // NOI18N
        ammoBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ammoBtnActionPerformed(evt);
            }
        });

        replaceBtn.setFont(resourceMap.getFont("replaceBtn.font")); // NOI18N
        replaceBtn.setText(resourceMap.getString("replaceBtn.text")); // NOI18N
        replaceBtn.setToolTipText(resourceMap.getString("replaceBtn.toolTipText")); // NOI18N
        replaceBtn.setEnabled(false);
        replaceBtn.setName("replaceBtn"); // NOI18N
        replaceBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                replaceBtnActionPerformed(evt);
            }
        });

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        TeamsList.setModel(teamsModel);
        TeamsList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        TeamsList.setName("TeamsList"); // NOI18N
        TeamsList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                TeamsListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(TeamsList);

        assignBtn.setText(resourceMap.getString("assignBtn.text")); // NOI18N
        assignBtn.setToolTipText(resourceMap.getString("assignBtn.toolTipText")); // NOI18N
        assignBtn.setEnabled(false);
        assignBtn.setName("assignBtn"); // NOI18N
        assignBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                assignBtnActionPerformed(evt);
            }
        });

        lblTeams.setText(resourceMap.getString("lblTeams.text")); // NOI18N
        lblTeams.setName("lblTeams"); // NOI18N

        btnNewTeam.setText(resourceMap.getString("btnNewTeam.text")); // NOI18N
        btnNewTeam.setToolTipText(resourceMap.getString("btnNewTeam.toolTipText")); // NOI18N
        btnNewTeam.setName("btnNewTeam"); // NOI18N
        btnNewTeam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewTeamActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(UnitsScroll, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 343, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(146, 146, 146))
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(jPanel1Layout.createSequentialGroup()
                                        .add(loadListBtn, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
                                        .add(18, 18, 18))
                                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                            .add(org.jdesktop.layout.GroupLayout.LEADING, btnViewUnit, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
                                            .add(btnDeployUnits, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE))
                                        .add(18, 18, 18)))
                                .add(8, 8, 8)))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED))
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(lblUnits, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 156, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(363, 363, 363)))
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, lblTasks, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 197, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(assignBtn)
                                .addContainerGap(539, Short.MAX_VALUE))
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(lblTeams)
                                .addContainerGap())
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                    .add(taskScroll, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 477, Short.MAX_VALUE)
                                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 477, Short.MAX_VALUE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                    .add(org.jdesktop.layout.GroupLayout.TRAILING, btnNewTeam, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
                                    .add(org.jdesktop.layout.GroupLayout.TRAILING, ammoBtn, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .add(org.jdesktop.layout.GroupLayout.TRAILING, replaceBtn, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblUnits)
                    .add(lblTasks))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(UnitsScroll, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 623, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                .add(jPanel1Layout.createSequentialGroup()
                                    .add(taskScroll, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                    .add(assignBtn)
                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .add(lblTeams)
                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED))
                                .add(jPanel1Layout.createSequentialGroup()
                                    .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                    .add(loadListBtn)
                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                    .add(btnDeployUnits)
                                    .add(18, 18, 18)
                                    .add(btnViewUnit)
                                    .add(215, 215, 215)))
                            .add(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(replaceBtn)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                .add(ammoBtn)
                                .add(287, 287, 287)))
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(btnNewTeam)
                            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 278, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        tabMain.addTab(resourceMap.getString("jPanel1.TabConstraints.tabTitle"), jPanel1); // NOI18N

        org.jdesktop.layout.GroupLayout mainPanelLayout = new org.jdesktop.layout.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(tabMain, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 1218, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnAdvanceDay))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(mainPanelLayout.createSequentialGroup()
                .add(18, 18, 18)
                .add(btnAdvanceDay)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(tabMain, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        menuLoad.setText(resourceMap.getString("menuLoad.text")); // NOI18N
        menuLoad.setEnabled(false);
        menuLoad.setName("menuLoad"); // NOI18N
        fileMenu.add(menuLoad);

        menuSave.setText(resourceMap.getString("menuSave.text")); // NOI18N
        menuSave.setEnabled(false);
        menuSave.setName("menuSave"); // NOI18N
        fileMenu.add(menuSave);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(mekhq.MekHQApp.class).getContext().getActionMap(MekHQView.class, this);
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        org.jdesktop.layout.GroupLayout statusPanelLayout = new org.jdesktop.layout.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(statusPanelSeparator, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 1256, Short.MAX_VALUE)
            .add(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(statusMessageLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 1060, Short.MAX_VALUE)
                .add(progressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(statusPanelLayout.createSequentialGroup()
                .add(statusPanelSeparator, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(statusMessageLabel)
                    .add(statusAnimationLabel)
                    .add(progressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(3, 3, 3))
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

private void btnNewTeamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewTeamActionPerformed
    NewTeamDialog ntd = new NewTeamDialog(this.getFrame(), true, campaign);
    ntd.setVisible(true);
    refreshTeamsList();
}//GEN-LAST:event_btnNewTeamActionPerformed

private void loadListBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadListBtnActionPerformed
    loadListFile();
}//GEN-LAST:event_loadListBtnActionPerformed

private void UnitListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_UnitListValueChanged
    int selected = UnitList.getSelectedIndex();
    if(selected > -1 && selected < campaign.getUnits().size()) {
        currentUnitId = campaign.getUnits().get(selected).getId();
    }
    else if(selected < 0) {
        currentUnitId = -1;
    }
    refreshTaskList();
}//GEN-LAST:event_UnitListValueChanged

private void assignBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_assignBtnActionPerformed
    //assign the task to the team here
    campaign.assignTask(currentTeamId, currentTaskId);
    int next = TaskList.getSelectedIndex() + 1;
    refreshUnitList();
    refreshTaskList();
    TaskList.setSelectedIndex(next);
    refreshTeamsList();
}//GEN-LAST:event_assignBtnActionPerformed

private void TaskListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_TaskListValueChanged
    int selected = TaskList.getSelectedIndex();
    if(selected > -1 && selected < campaign.getTasksForUnit(currentUnitId).size()) {
        currentTaskId = campaign.getTasksForUnit(currentUnitId).get(selected).getId(); 
    }
    else if(selected < 0) {
        currentTaskId = -1;
    }
    updateAssignEnabled();
    updateReplaceEnabled();
    updateAmmoSwapEnabled();
}//GEN-LAST:event_TaskListValueChanged

private void TeamsListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_TeamsListValueChanged
    int selected = TeamsList.getSelectedIndex();
    if(selected > -1 && selected < campaign.getTeams().size()) {
        currentTeamId = campaign.getTeams().get(selected).getId();
    }
    else if(selected < 0) {
        currentTeamId = -1;
    }
    updateAssignEnabled();
    updateReplaceEnabled();
    updateAmmoSwapEnabled();
}//GEN-LAST:event_TeamsListValueChanged

private void btnAdvanceDayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdvanceDayActionPerformed
    campaign.processDay();
    trd.report(campaign);
    trd.setVisible(true);
    refreshUnitList();
    refreshTaskList();
    refreshTeamsList();
}//GEN-LAST:event_btnAdvanceDayActionPerformed

private void btnDeployUnitsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeployUnitsActionPerformed
    saveListFile();
}//GEN-LAST:event_btnDeployUnitsActionPerformed

private void replaceBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_replaceBtnActionPerformed
    WorkItem task = campaign.getTask(currentTaskId);
    if(task instanceof RepairItem) {
        campaign.mutateTask(task, ((RepairItem)task).replace());
        refreshTaskList();
    }
}//GEN-LAST:event_replaceBtnActionPerformed

private void ammoBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ammoBtnActionPerformed
    WorkItem task = campaign.getTask(currentTaskId);
    Unit unit = campaign.getUnit(currentUnitId);
    if(null != task && null != unit && task instanceof ReloadItem) {
        AmmoDialog ammod = new AmmoDialog(this.getFrame(), true, (ReloadItem)task, unit.getEntity());
        ammod.setVisible(true);
        refreshUnitList();
        refreshTaskList();
        refreshTeamsList();
    }
}//GEN-LAST:event_ammoBtnActionPerformed

private void btnViewUnitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewUnitActionPerformed
  if(currentUnitId == -1) {
      return;
  }
  MechView mv = new MechView(campaign.getUnit(currentUnitId).getEntity(), false);
  MekViewDialog mvd = new MekViewDialog(this.getFrame(), true, mv);
  mvd.setVisible(true);
}//GEN-LAST:event_btnViewUnitActionPerformed

protected void loadListFile() {
    JFileChooser loadList = new JFileChooser(".");
    int returnVal = loadList.showOpenDialog(mainPanel);
    if ((returnVal != JFileChooser.APPROVE_OPTION) || (loadList.getSelectedFile() == null)) {
       // I want a file, y'know!
       return;
    }
        
    File unitFile = loadList.getSelectedFile();
    if (unitFile != null) {
       try {
           // Read the units from the file.
           Vector<Entity> loadedUnits = EntityListFile.loadFrom(unitFile);
           // Add the units from the file.
           for (Entity entity : loadedUnits) {
              campaign.addUnit(entity);
           }
        } catch (IOException excep) {
            excep.printStackTrace(System.err);
        }
    }
    refreshUnitList();
}

protected void saveListFile() {
    JFileChooser saveList = new JFileChooser(".");
    int returnVal = saveList.showSaveDialog(mainPanel);
    if ((returnVal != JFileChooser.APPROVE_OPTION) || (saveList.getSelectedFile() == null)) {
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
                //FIXME: this is not working
                EntityListFile.saveTo(unitFile, campaign.getEntities());
                //clear the entities, so that if the user wants to read them back you wont get duplicates
                campaign.clearUnits();
                
            } catch (IOException excep) {
                excep.printStackTrace(System.err);
            }
    }
    refreshUnitList();
}
    
protected void refreshUnitList() {
    int selected = UnitList.getSelectedIndex();
    unitsModel.removeAllElements();
    int len = 0;
    for(Unit unit: campaign.getUnits()) {
        campaign.getTasksForUnit(unit.getId());
        unitsModel.addElement(unit.getEntity().getDisplayName() + campaign.getUnitTaskDesc(unit.getId()));
        len++;
    }
    if(selected < len) {
        UnitList.setSelectedIndex(selected);
    }
}

protected void refreshTaskList() {
        taskModel.removeAllElements();
        for(WorkItem task: campaign.getTasksForUnit(currentUnitId)) {
            taskModel.addElement(task.getDesc());
        }
}

protected void refreshTeamsList() {
    int selected = TeamsList.getSelectedIndex();
    teamsModel.removeAllElements();
    int len = 0;
    for(SupportTeam team : campaign.getTeams()) {
        teamsModel.addElement(team.getDesc());
        len++;
    }
    if(selected < len) {
        TeamsList.setSelectedIndex(selected);
    }
}

protected void updateAssignEnabled() {
    //must have a valid team and an unassigned task
    WorkItem curTask = campaign.getTask(currentTaskId);
    SupportTeam team = campaign.getTeam(currentTeamId);
    if(null != curTask && curTask.getTeamId() == WorkItem.TEAM_NONE && null != team && team.canDo(curTask)) {
        assignBtn.setEnabled(true);
    } else {
        assignBtn.setEnabled(false);
    }    
}

protected void updateReplaceEnabled() {
    //must have a valid team and an unassigned task
    WorkItem curTask = campaign.getTask(currentTaskId);
    if(null != curTask && curTask instanceof RepairItem) {
        replaceBtn.setEnabled(true);
    } else {
        replaceBtn.setEnabled(false);
    }    
}

protected void updateAmmoSwapEnabled() {
    WorkItem curTask = campaign.getTask(currentTaskId);
    if(null != curTask && curTask instanceof ReloadItem) {
        ammoBtn.setEnabled(true);
    } else {
        ammoBtn.setEnabled(false);
    }    
}



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList TaskList;
    private javax.swing.JList TeamsList;
    private javax.swing.JList UnitList;
    private javax.swing.JScrollPane UnitsScroll;
    private javax.swing.JButton ammoBtn;
    private javax.swing.JButton assignBtn;
    private javax.swing.JButton btnAdvanceDay;
    private javax.swing.JButton btnDeployUnits;
    private javax.swing.JButton btnNewTeam;
    private javax.swing.JButton btnViewUnit;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblTasks;
    private javax.swing.JLabel lblTeams;
    private javax.swing.JLabel lblUnits;
    private javax.swing.JButton loadListBtn;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem menuLoad;
    private javax.swing.JMenuItem menuSave;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JButton replaceBtn;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JTabbedPane tabMain;
    private javax.swing.JScrollPane taskScroll;
    // End of variables declaration//GEN-END:variables

    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;

    private JDialog aboutBox;
}
