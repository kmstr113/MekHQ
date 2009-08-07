/*
 * EquipmentPart.java
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

package mekhq.campaign.parts;

import megamek.common.EquipmentType;
import mekhq.campaign.work.EquipmentReplacement;
import mekhq.campaign.work.ReplacementItem;

/**
 *
 * @author Jay Lawson <jaylawson39 at yahoo.com>
 */
public class EquipmentPart extends Part {

    //crap equipmenttype is not serialized!
    protected transient EquipmentType type;
    protected String typeName;
    
    public EquipmentPart(boolean salvage, EquipmentType et) {
        super(salvage);
        this.type = et;
        this.name = type.getDesc();
        this.typeName = type.getInternalName();
    }
    
    @Override
    public boolean canBeUsedBy(ReplacementItem task) {
        if(task instanceof EquipmentReplacement) {
            EquipmentType et = ((EquipmentReplacement)task).getMounted().getType();
            return type.equals(et);
        }
        return false;
    }
    
    /**
     * Restores the equipment from the name
     */
    public void restore() {
        if (typeName == null) {
            typeName = type.getName();
        } else {
            type = EquipmentType.get(typeName);
        }

        if (type == null) {
            System.err
            .println("Mounted.restore: could not restore equipment type \""
                    + typeName + "\"");
        }
    }
}
