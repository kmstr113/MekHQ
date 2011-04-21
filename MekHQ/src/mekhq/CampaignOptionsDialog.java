/*
 * CampaignOptionsDialog.java
 *
 * Created on August 19, 2009, 11:22 AM
 */

package mekhq;

import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Iterator;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JFormattedTextField;
import megamek.client.ui.swing.util.ImageFileFactory;
import megamek.client.ui.swing.util.PlayerColors;
import megamek.common.Player;
import megamek.common.util.DirectoryItems;
import mekhq.campaign.Campaign;
import mekhq.campaign.CampaignOptions;
import mekhq.campaign.Faction;

/**
 *
 * @author  Jay Lawson <jaylawson39 at yahoo.com>
 */
public class CampaignOptionsDialog extends javax.swing.JDialog {
	private static final long serialVersionUID = 1935043247792962964L;
	private Campaign campaign;
    private CampaignOptions options;
    private GregorianCalendar date;
    private SimpleDateFormat dateFormat;
    private Frame frame;
    private String camoCategory;
    private String camoFileName;
    private int colorIndex;
    private DirectoryItems camos;
    
    
    /** Creates new form CampaignOptionsDialog */
    public CampaignOptionsDialog(java.awt.Frame parent, boolean modal, Campaign c) {
        super(parent, modal);
        this.campaign = c;
        this.options = c.getCampaignOptions();
        //this is a hack but I have no idea what is going on here
        this.frame = parent;
        this.date = campaign.calendar;
        dateFormat = new SimpleDateFormat("EEEE, MMMM d yyyy");
        this.camoCategory = campaign.getCamoCategory();
        this.camoFileName = campaign.getCamoFileName();
        this.colorIndex = campaign.getColorIndex();
        // Parse the camo directory.
        try {
            camos = new DirectoryItems(new File("data/images/camo"), "", //$NON-NLS-1$ //$NON-NLS-2$
                    ImageFileFactory.getInstance());
        } catch (Exception e) {
            camos = null;
        }
        initComponents();
        setCamoIcon();
        
        // Rules panel
        chkUseFinances.setSelected(options.useFinances());
        useFactionModifiersCheckBox.setSelected(options.useFactionModifiers());
        clanPriceModifierJFormattedTextField.setValue(options.getClanPriceModifier());
        useEasierRefitCheckBox.setSelected(options.useEasierRefit());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        tabOptions = new javax.swing.JTabbedPane();
        panGeneral = new javax.swing.JPanel();
        txtName = new javax.swing.JTextField();
        lblName = new javax.swing.JLabel();
        lblFaction = new javax.swing.JLabel();
        lblFactionNames = new javax.swing.JLabel();
        lblDate = new javax.swing.JLabel();
        btnDate = new javax.swing.JButton();
        comboFaction = new javax.swing.JComboBox();
        comboFactionNames = new javax.swing.JComboBox();
        btnCamo = new javax.swing.JButton();
        lblCamo = new javax.swing.JLabel();
        panRules = new javax.swing.JPanel();
        useFactionModifiersCheckBox = new javax.swing.JCheckBox();
        javax.swing.JLabel clanPriceModifierLabel = new javax.swing.JLabel();
        DecimalFormat numberFormat = (DecimalFormat) DecimalFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setGroupingSeparator(' ');
        decimalFormatSymbols.setDecimalSeparator('.');
        numberFormat.setDecimalFormatSymbols(decimalFormatSymbols);
        clanPriceModifierJFormattedTextField = new JFormattedTextField(numberFormat);
        useEasierRefitCheckBox = new javax.swing.JCheckBox();
        repairSystemComboBox = new javax.swing.JComboBox();
        javax.swing.JLabel repairSystemComboBoxLabel = new javax.swing.JLabel();
        chkUseFinances = new javax.swing.JCheckBox();
        btnOkay = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setName("Form"); // NOI18N
        getContentPane().setLayout(new java.awt.GridBagLayout());

        tabOptions.setName("tabOptions"); // NOI18N

        panGeneral.setName("panGeneral"); // NOI18N
        panGeneral.setLayout(new java.awt.GridBagLayout());

        txtName.setText(campaign.getName());
        txtName.setMinimumSize(new java.awt.Dimension(500, 30));
        txtName.setName("txtName"); // NOI18N
        txtName.setPreferredSize(new java.awt.Dimension(500, 30));
        txtName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNameActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panGeneral.add(txtName, gridBagConstraints);

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(mekhq.MekHQApp.class).getContext().getResourceMap(CampaignOptionsDialog.class);
        lblName.setText(resourceMap.getString("lblName.text")); // NOI18N
        lblName.setName("lblName"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panGeneral.add(lblName, gridBagConstraints);

        lblFaction.setText(resourceMap.getString("lblFaction.text")); // NOI18N
        lblFaction.setName("lblFaction"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panGeneral.add(lblFaction, gridBagConstraints);

        lblFactionNames.setText(resourceMap.getString("lblFactionNames.text")); // NOI18N
        lblFactionNames.setName("lblFactionNames"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panGeneral.add(lblFactionNames, gridBagConstraints);

        
        lblDate.setText(resourceMap.getString("lblDate.text")); // NOI18N
        lblDate.setName("lblDate"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panGeneral.add(lblDate, gridBagConstraints);

        btnDate.setText(getDateAsString());
        btnDate.setMinimumSize(new java.awt.Dimension(400, 30));
        btnDate.setName("btnDate"); // NOI18N
        btnDate.setPreferredSize(new java.awt.Dimension(400, 30));
        btnDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDateActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panGeneral.add(btnDate, gridBagConstraints);

        DefaultComboBoxModel factionModel = new DefaultComboBoxModel();
        for(int i = 0; i < Faction.F_NUM; i++) {
            factionModel.addElement(Faction.getFactionName(i));
        }
        factionModel.setSelectedItem(campaign.getFactionName());
        comboFaction.setModel(factionModel);
        comboFaction.setMinimumSize(new java.awt.Dimension(400, 30));
        comboFaction.setName("comboFaction"); // NOI18N
        comboFaction.setPreferredSize(new java.awt.Dimension(400, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panGeneral.add(comboFaction, gridBagConstraints);

        DefaultComboBoxModel factionNamesModel = new DefaultComboBoxModel();
        for (Iterator<String> i = campaign.getRNG().getFactions(); i.hasNext(); ) {
            String faction = (String) i.next();
            factionNamesModel.addElement(faction);
        }
        factionNamesModel.setSelectedItem(campaign.getRNG().getChosenFaction());
        comboFactionNames.setModel(factionNamesModel);
        comboFactionNames.setMinimumSize(new java.awt.Dimension(400, 30));
        comboFactionNames.setName("comboFactionNames"); // NOI18N
        comboFactionNames.setPreferredSize(new java.awt.Dimension(400, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panGeneral.add(comboFactionNames, gridBagConstraints);
        
        btnCamo.setMaximumSize(new java.awt.Dimension(84, 72));
        btnCamo.setMinimumSize(new java.awt.Dimension(84, 72));
        btnCamo.setName("btnCamo"); // NOI18N
        btnCamo.setPreferredSize(new java.awt.Dimension(84, 72));
        btnCamo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCamoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panGeneral.add(btnCamo, gridBagConstraints);

        lblCamo.setText(resourceMap.getString("lblCamo.text")); // NOI18N
        lblCamo.setName("lblCamo"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panGeneral.add(lblCamo, gridBagConstraints);

        tabOptions.addTab(resourceMap.getString("panGeneral.TabConstraints.tabTitle"), panGeneral); // NOI18N

        panRules.setName("panRules"); // NOI18N
        panRules.setLayout(new java.awt.GridBagLayout());

        useFactionModifiersCheckBox.setText(resourceMap.getString("useFactionModifiersCheckBox.text")); // NOI18N
        useFactionModifiersCheckBox.setToolTipText(resourceMap.getString("useFactionModifiersCheckBox.toolTipText")); // NOI18N
        useFactionModifiersCheckBox.setName("useFactionModifiersCheckBox"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panRules.add(useFactionModifiersCheckBox, gridBagConstraints);

        clanPriceModifierLabel.setText(resourceMap.getString("clanPriceModifierLabel.text")); // NOI18N
        clanPriceModifierLabel.setName("clanPriceModifierLabel"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panRules.add(clanPriceModifierLabel, gridBagConstraints);

        clanPriceModifierJFormattedTextField.setColumns(4);
        clanPriceModifierJFormattedTextField.setToolTipText(resourceMap.getString("clanPriceModifierJFormattedTextField.toolTipText")); // NOI18N
        clanPriceModifierJFormattedTextField.setName("clanPriceModifierJFormattedTextField"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panRules.add(clanPriceModifierJFormattedTextField, gridBagConstraints);

        useEasierRefitCheckBox.setText(resourceMap.getString("useEasierRefitCheckBox.text")); // NOI18N
        useEasierRefitCheckBox.setToolTipText(resourceMap.getString("useEasierRefitCheckBox.toolTipText")); // NOI18N
        useEasierRefitCheckBox.setName("useEasierRefitCheckBox"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panRules.add(useEasierRefitCheckBox, gridBagConstraints);

        DefaultComboBoxModel repairSystemComboBoxModel = new DefaultComboBoxModel();
        for (int i=0;i<CampaignOptions.REPAIR_SYSTEM_NAMES.length; i++) {
            repairSystemComboBoxModel.addElement(CampaignOptions.getRepairSystemName(i));
        }
        repairSystemComboBox.setModel(repairSystemComboBoxModel);
        repairSystemComboBox.setToolTipText(resourceMap.getString("repairSystemComboBox.toolTipText")); // NOI18N
        repairSystemComboBox.setName("repairSystemComboBox"); // NOI18N
        repairSystemComboBox.setSelectedIndex(options.getRepairSystem());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panRules.add(repairSystemComboBox, gridBagConstraints);

        repairSystemComboBoxLabel.setText(resourceMap.getString("repairSystemComboBoxLabel.text")); // NOI18N
        repairSystemComboBoxLabel.setName("repairSystemComboBoxLabel"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panRules.add(repairSystemComboBoxLabel, gridBagConstraints);

        chkUseFinances.setText(resourceMap.getString("chkUseFinances.text")); // NOI18N
        chkUseFinances.setName("chkUseFinances"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panRules.add(chkUseFinances, gridBagConstraints);

        tabOptions.addTab(resourceMap.getString("panRules.TabConstraints.tabTitle"), panRules); // NOI18N

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(tabOptions, gridBagConstraints);

        btnOkay.setText(resourceMap.getString("btnOkay.text")); // NOI18N
        btnOkay.setName("btnOkay"); // NOI18N
        btnOkay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkayActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.5;
        getContentPane().add(btnOkay, gridBagConstraints);

        btnCancel.setText(resourceMap.getString("btnCancel.text")); // NOI18N
        btnCancel.setName("btnCancel"); // NOI18N
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.5;
        getContentPane().add(btnCancel, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void txtNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNameActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_txtNameActionPerformed

private void btnOkayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkayActionPerformed
    if(txtName.getText().length() > 0) {
        campaign.setName(txtName.getText());
        this.setVisible(false);
    }
    campaign.calendar = date;
    campaign.setFaction(comboFaction.getSelectedIndex());
    campaign.getRNG().setChosenFaction((String)comboFactionNames.getSelectedItem());
    campaign.setCamoCategory(camoCategory);
    campaign.setCamoFileName(camoFileName);
    campaign.setColorIndex(colorIndex);
    
    // Rules panel
    options.setFinances(chkUseFinances.isSelected());
    options.setFactionModifiers(useFactionModifiersCheckBox.isSelected());
    String clanPriceModifierString = clanPriceModifierJFormattedTextField.getText();
    options.setClanPriceModifier(new Double(clanPriceModifierString));
    options.setEasierRefit(useEasierRefitCheckBox.isSelected());
    options.setRepairSystem(repairSystemComboBox.getSelectedIndex());

    campaign.refreshAllUnitDiagnostics();
}//GEN-LAST:event_btnOkayActionPerformed

private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
    this.setVisible(false);
}//GEN-LAST:event_btnCancelActionPerformed

private void btnDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDateActionPerformed
    // show the date chooser
    DateChooser dc = new DateChooser(frame, date);
    // user can eiter choose a date or cancel by closing
    if (dc.showDateChooser() == DateChooser.OK_OPTION) {
        date = dc.getDate();
        btnDate.setText(getDateAsString());
    }
}//GEN-LAST:event_btnDateActionPerformed

private void btnCamoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCamoActionPerformed
    CamoChoiceDialog ccd = new CamoChoiceDialog(frame, true, camoCategory, camoFileName, colorIndex);
    ccd.setVisible(true);
    camoCategory = ccd.getCategory();
    camoFileName = ccd.getFileName();
    if(ccd.getColorIndex() != -1) {
        colorIndex = ccd.getColorIndex();
    }
    setCamoIcon();
}//GEN-LAST:event_btnCamoActionPerformed

public String getDateAsString() {
    return dateFormat.format(date.getTime());
}

    public void setCamoIcon() {
        if (null == camoCategory) {
            return;
        }
        
        if(Player.NO_CAMO.equals(camoCategory)) {
            int colorInd = colorIndex;
            if (colorInd == -1) {
                colorInd = 0;
            }
            BufferedImage tempImage = new BufferedImage(84, 72,
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = tempImage.createGraphics();
            graphics.setColor(PlayerColors.getColor(colorInd));
            graphics.fillRect(0, 0, 84, 72);
            btnCamo.setIcon(new ImageIcon(tempImage));
            return;
        }

        // Try to get the camo file.
        try {

            // Translate the root camo directory name.
            if (Player.ROOT_CAMO.equals(camoCategory))
                camoCategory = ""; //$NON-NLS-1$
            Image camo = (Image) camos.getItem(camoCategory, camoFileName);
            btnCamo.setIcon(new ImageIcon(camo));
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                CampaignOptionsDialog dialog = new CampaignOptionsDialog(new javax.swing.JFrame(), true, null);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCamo;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnDate;
    private javax.swing.JButton btnOkay;
    private javax.swing.JCheckBox chkUseFinances;
    private javax.swing.JFormattedTextField clanPriceModifierJFormattedTextField;
    private javax.swing.JComboBox comboFaction;
    private javax.swing.JComboBox comboFactionNames;
    private javax.swing.JLabel lblCamo;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblFaction;
    private javax.swing.JLabel lblFactionNames;
    private javax.swing.JLabel lblName;
    private javax.swing.JPanel panGeneral;
    private javax.swing.JPanel panRules;
    private javax.swing.JComboBox repairSystemComboBox;
    private javax.swing.JTabbedPane tabOptions;
    private javax.swing.JTextField txtName;
    private javax.swing.JCheckBox useEasierRefitCheckBox;
    private javax.swing.JCheckBox useFactionModifiersCheckBox;
    // End of variables declaration//GEN-END:variables

}
