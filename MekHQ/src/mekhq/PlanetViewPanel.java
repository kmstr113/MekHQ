/*
 * PlanetViewPanel
 *
 * Created on July 26, 2009, 11:32 PM
 */

package mekhq;

import java.awt.Color;

import javax.swing.BorderFactory;

import megamek.common.TechConstants;
import mekhq.campaign.Campaign;
import mekhq.campaign.Faction;
import mekhq.campaign.Planet;

/**
 * A custom panel that gets filled in with goodies from a Planet record
 * @author  Jay Lawson <jaylawson39 at yahoo.com>
 */
public class PlanetViewPanel extends javax.swing.JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7004741688464105277L;

	private Planet planet;
	private Campaign campaign;
	
	private javax.swing.JPanel pnlStats;
	private javax.swing.JTextArea txtDesc;
	
	private javax.swing.JLabel lblOwner;
	private javax.swing.JLabel lblJumpPoint1;
	private javax.swing.JLabel lblJumpPoint2;
	private javax.swing.JLabel lblGravity1;
	private javax.swing.JLabel lblGravity2;
	private javax.swing.JLabel lblPressure1;
	private javax.swing.JLabel lblPressure2;
	private javax.swing.JLabel lblRecharge1;
	private javax.swing.JLabel lblRecharge2;

	
	public PlanetViewPanel(Planet p, Campaign c) {
		this.planet = p;
		this.campaign = c;
		initComponents();
	}
	
	private void initComponents() {
		java.awt.GridBagConstraints gridBagConstraints;

		pnlStats = new javax.swing.JPanel();
		txtDesc = new javax.swing.JTextArea();
		       
		setLayout(new java.awt.GridBagLayout());

		setBackground(Color.WHITE);

		pnlStats.setName("pnlStats");
		pnlStats.setBorder(BorderFactory.createTitledBorder(planet.getName()));
		pnlStats.setBackground(Color.WHITE);
		fillStats();
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridheight = 1;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 20);
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;	
		add(pnlStats, gridBagConstraints);
		
		txtDesc.setName("txtDesc");
		txtDesc.setText("Nothing here yet. Who wants to volunteer to enter planet data?");
		txtDesc.setEditable(false);
		txtDesc.setLineWrap(true);
		txtDesc.setWrapStyleWord(true);
		txtDesc.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Description"),
                BorderFactory.createEmptyBorder(5,5,5,5)));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 20);
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		add(txtDesc, gridBagConstraints);
	}

    private void fillStats() {
    	
    	org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(mekhq.MekHQApp.class).getContext().getResourceMap(PlanetViewPanel.class);
    	
    	lblOwner = new javax.swing.JLabel();
    	lblJumpPoint1 = new javax.swing.JLabel();
    	lblJumpPoint2 = new javax.swing.JLabel();
    	lblGravity1 = new javax.swing.JLabel();
    	lblGravity2 = new javax.swing.JLabel();
    	lblPressure1 = new javax.swing.JLabel();
    	lblPressure2 = new javax.swing.JLabel();
    	lblRecharge1 = new javax.swing.JLabel();
    	lblRecharge2 = new javax.swing.JLabel();
    	
    	
    	java.awt.GridBagConstraints gridBagConstraints;
		pnlStats.setLayout(new java.awt.GridBagLayout());
		
		lblOwner.setName("lblOwner"); // NOI18N
		lblOwner.setText("<html><i>" + Faction.getFactionName(planet.getFaction()) + "</i></html>");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
		gridBagConstraints.fill = java.awt.GridBagConstraints.NONE;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		pnlStats.add(lblOwner, gridBagConstraints);
		
		lblJumpPoint1.setName("lblJumpPoint1"); // NOI18N
		lblJumpPoint1.setText(resourceMap.getString("lblJumpPoint1.text"));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.NONE;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		pnlStats.add(lblJumpPoint1, gridBagConstraints);
		
		lblJumpPoint2.setName("lblJumpPoint2"); // NOI18N
		lblJumpPoint2.setText(Double.toString(planet.getTimeToJumpPoint()));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.weightx = 0.5;
		gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
		gridBagConstraints.fill = java.awt.GridBagConstraints.NONE;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		pnlStats.add(lblJumpPoint2, gridBagConstraints);
		
		lblGravity1.setName("lblGravity1"); // NOI18N
		lblGravity1.setText(resourceMap.getString("lblGravity1.text"));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.NONE;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		pnlStats.add(lblGravity1, gridBagConstraints);
		
		lblGravity2.setName("lblGravity2"); // NOI18N
		lblGravity2.setText(Double.toString(planet.getGravity()));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.weightx = 0.5;
		gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
		gridBagConstraints.fill = java.awt.GridBagConstraints.NONE;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		pnlStats.add(lblGravity2, gridBagConstraints);
		
		lblPressure1.setName("lblPressure1"); // NOI18N
		lblPressure1.setText(resourceMap.getString("lblPressure1.text"));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.NONE;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		pnlStats.add(lblPressure1, gridBagConstraints);
		
		lblPressure2.setName("lblPressure2"); // NOI18N
		lblPressure2.setText(planet.getPressureName());
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.weightx = 0.5;
		gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
		gridBagConstraints.fill = java.awt.GridBagConstraints.NONE;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		pnlStats.add(lblPressure2, gridBagConstraints);
		
		lblRecharge1.setName("lblRecharge1"); // NOI18N
		lblRecharge1.setText(resourceMap.getString("lblRecharge1.text"));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.fill = java.awt.GridBagConstraints.NONE;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		pnlStats.add(lblRecharge1, gridBagConstraints);
		
		lblRecharge2.setName("lblRecharge2"); // NOI18N
		lblRecharge2.setText(planet.getRechargeStations());
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.weightx = 0.5;
		gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
		gridBagConstraints.fill = java.awt.GridBagConstraints.NONE;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		pnlStats.add(lblRecharge2, gridBagConstraints);
		
    }
}