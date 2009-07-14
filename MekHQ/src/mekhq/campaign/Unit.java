/*
 * Unit.java
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

package mekhq.campaign;

import java.io.Serializable;
import java.util.ArrayList;
import megamek.common.Compute;
import megamek.common.CriticalSlot;
import megamek.common.Entity;
import megamek.common.Mech;
import megamek.common.MiscType;
import megamek.common.Mounted;
import mekhq.campaign.work.*;

/**
 * This is a wrapper class for entity, so that we can add some 
 * functionality to it
 * @author Jay Lawson <jaylawson39 at yahoo.com>
 */
public class Unit implements Serializable {

    private Entity entity;
    
    public Unit(Entity en) {
        this.entity = en;
    }
    
    public Entity getEntity() {
        return entity;
    }
    
    public int getId() {
        return getEntity().getId();
    }
    
    //definitely need to refactor this but I can put it here now
    /**
     * Run a diagnostic on the given entity and build and ArrayList of WorkItems to return
     * TODO: this should really be a function in Entity, I should create a wrapper function
     *       for entity
     */
    public void runDiagnostic(Campaign campaign) {
        
        //It is somewhat unclear but the language of StratOps implies that even equipment that
        //is "combat destroyed" can be repaired.  For example, a gyro with 2 hits can still be repaired
        //although with a +4 mod.  Weapons and other equipment must pass a roll to be repairable
            
        //cycle through the locations and assign repairs and replacements
        //don't do weapons and equipment here because some are spreadable
        int engineHits = 0;
        int engineCrits = 0;
        for(int i = 0; i < entity.locations(); i++) {
            boolean locDestroyed = entity.isLocationBad(i);
            //TODO: on mechs, hip and shoulder criticals also make the location effectively destroyed
            
            //replace location?
            if(locDestroyed) {
                campaign.addWork(new LocationReplacement(this, i));
            } else {
                //repair internal
                double pctInternal = entity.getInternal(i)/entity.getOInternal(i);
                if(pctInternal < 1.00) {
                    campaign.addWork(new InternalRepair(this, i, pctInternal));
                }
            }
            
            //replace armor?
            //TODO: get rear locations as well
            int diff = entity.getOArmor(i, false) - entity.getArmor(i, false);
            diff +=  entity.getOArmor(i, true) - entity.getArmor(i, true);
            if(diff > 0) {
                campaign.addWork(new ArmorReplacement(this, i, diff));
            }
            
            
            
            //check for various component damage
            if(entity instanceof Mech) {
                engineHits += entity.getHitCriticals(CriticalSlot.TYPE_SYSTEM, Mech.SYSTEM_ENGINE, i);
                engineCrits += entity.getNumberOfCriticals(CriticalSlot.TYPE_SYSTEM, Mech.SYSTEM_ENGINE, i);
                int sensorHits = entity.getHitCriticals(CriticalSlot.TYPE_SYSTEM, Mech.SYSTEM_SENSORS, i);
                if(sensorHits > 0) {
                    campaign.addWork(new MekSensorRepair(this, sensorHits));
                }
                int lifeHits = entity.getHitCriticals(CriticalSlot.TYPE_SYSTEM, Mech.SYSTEM_LIFE_SUPPORT, i);
                if(lifeHits > 0) {
                    campaign.addWork(new MekLifeSupportRepair(this, lifeHits));
                }
                int gyroHits = entity.getHitCriticals(CriticalSlot.TYPE_SYSTEM, Mech.SYSTEM_GYRO, i);
                if(gyroHits > 0) {
                    campaign.addWork(new MekGyroRepair(this, gyroHits));
                }
                //check actuators
                //don't check hips and shoulders because that should be accounted for in location replacement
                if(entity.getHitCriticals(CriticalSlot.TYPE_SYSTEM, Mech.ACTUATOR_UPPER_ARM, i) > 0) {
                    campaign.addWork(new MekUpArmActuatorRepair(this, 1, i));
                }
                if(entity.getHitCriticals(CriticalSlot.TYPE_SYSTEM, Mech.ACTUATOR_LOWER_ARM, i) > 0) {
                    campaign.addWork(new MekLowArmActuatorRepair(this, 1, i));
                }
                if(entity.getHitCriticals(CriticalSlot.TYPE_SYSTEM, Mech.ACTUATOR_HAND, i) > 0) {
                    campaign.addWork(new MekHandActuatorRepair(this, 1, i));
                }
                if(entity.getHitCriticals(CriticalSlot.TYPE_SYSTEM, Mech.ACTUATOR_UPPER_LEG, i) > 0) {
                    campaign.addWork(new MekUpLegActuatorRepair(this, 1, i));
                }
                if(entity.getHitCriticals(CriticalSlot.TYPE_SYSTEM, Mech.ACTUATOR_LOWER_LEG, i) > 0) {
                    campaign.addWork(new MekLowLegActuatorRepair(this, 1, i));
                }
                if(entity.getHitCriticals(CriticalSlot.TYPE_SYSTEM, Mech.ACTUATOR_FOOT, i) > 0) {
                    campaign.addWork(new MekFootActuatorRepair(this, 1, i));
                }                
            }//end mech check
        }//end location checks
        
        if(entity instanceof Mech && engineHits > 0) {
            if(engineHits >= engineCrits) {
                campaign.addWork(new MekEngineReplacement(this));
            } else {
                campaign.addWork(new MekEngineRepair(this, engineHits));
            }
        }
        
        //now lets cycle through equipment
        for(Mounted m : entity.getEquipment()) {
            if(m.isHit() || m.isDestroyed()) {
                //TODO: check flags for jump jets and heat sinks because those are handled differently
                if(m.getType().hasFlag(MiscType.F_JUMP_JET)) {
                    campaign.addWork(new JumpJetRepair(this, 1, m));
                    continue;
                } else if(m.getType().hasFlag(MiscType.F_HEAT_SINK)) {
                    campaign.addWork(new HeatSinkRepair(this, 1, m));
                    continue;
                }             
                //TODO: some slots need to be skipped (like armor, endo-steel, etc.)
                
                //combat destroyed is not the same as really destroyed
                //you get a roll to see if it can be repaired
                //TODO: I think this check should probably be made from within MM when a crit is received
                //and added to the MUL file.             
                if(Compute.d6(2) >= 10) {
                    campaign.addWork(new EquipmentRepair(this, getCrits(m), m));
                } else {
                    campaign.addWork(new EquipmentReplacement(this, m));
                }
            }
        }
    }
    
    /**
     * @param m - A Mounted class to find crits for
     * @return the number of crits exising for this Mounted
     */
    public int getCrits(Mounted m) {
        //TODO: I should probably just add this method to Entity in MM
        int hits = 0;
        for(int loc = 0; loc < entity.locations(); loc++) {
            for (int i = 0; i < entity.getNumberOfCriticals(loc); i++) {
                CriticalSlot slot = entity.getCritical(loc, i);
                // ignore empty & system slots
                if ((slot == null) || (slot.getType() != CriticalSlot.TYPE_EQUIPMENT)) {
                    continue;
                }
                if (entity.getEquipmentNum(m) == slot.getIndex() 
                        && (slot.isHit() || slot.isDestroyed())) {
                    hits++;
                }
            }
        }
        return hits;
    }
    
}
