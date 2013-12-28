/*
 * JumpJet.java
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

package mekhq.campaign.parts.equipment;

import megamek.common.CriticalSlot;
import megamek.common.EquipmentType;
import megamek.common.MiscType;
import megamek.common.Mounted;
import mekhq.campaign.Campaign;
import mekhq.campaign.parts.Part;

/**
 *
 * @author Jay Lawson <jaylawson39 at yahoo.com>
 */
public class JumpJet extends EquipmentPart {
	private static final long serialVersionUID = 2892728320891712304L;

	public JumpJet() {
    	this(0, null, -1, null);
    }
    
    public JumpJet(int tonnage, EquipmentType et, int equipNum, Campaign c) {
        // TODO Memorize all entity attributes needed to calculate cost
        // As it is a part bought with one entity can be used on another entity
        // on which it would have a different price (only tonnage is taken into
        // account for compatibility)
        super(tonnage, et, equipNum, c);
    }
    
    public JumpJet clone() {
    	JumpJet clone = new JumpJet(getUnitTonnage(), getType(), getEquipmentNum(), campaign);
        clone.copyBaseData(this);
    	return clone;
    }
	
    @Override
    public double getTonnage() {
    	double ton = 0.5;
    	if(getUnitTonnage() >= 90) {
    		ton = 2.0;
    	} else if(getUnitTonnage() >= 60) {
    		ton = 1.0;
    	}
    	if(type.hasSubType(MiscType.S_IMPROVED)) {
    		ton *= 2;
    	}
    	return ton;
    }
    
    /**
     * Copied from megamek.common.Entity.getWeaponsAndEquipmentCost(StringBuffer detail, boolean ignoreAmmo)
     *
     */
    @Override
    public long getStickerPrice() {
    	return 200 * getUnitTonnage();	
    }

	@Override
	public Part getMissingPart() {
		return new MissingJumpJet(getUnitTonnage(), type, equipmentNum, campaign);
	}

	@Override
	public void updateConditionFromEntity() {
		if(null != unit) {
			Mounted mounted = unit.getEntity().getEquipment(equipmentNum);
			if(null != mounted) {
				if(!mounted.isRepairable()) {
					remove(false);
					return;
				} 
				hits = unit.getEntity().getDamagedCriticals(CriticalSlot.TYPE_EQUIPMENT, equipmentNum, mounted.getLocation());
			}
		}
		if(hits == 0) {
			time = 0;
			difficulty = 0;
		} else if(hits > 0) {
			time = 100;
			difficulty = -3;
		}
		if(isSalvaging()) {
			this.time = 60;
			this.difficulty = 0;
		}
	}

	@Override
	public boolean needsFixing() {
		return hits > 0;
	}
}