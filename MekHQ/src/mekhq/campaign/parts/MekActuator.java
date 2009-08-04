/*
 * MekActuator.java
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

import mekhq.campaign.work.MekActuatorReplacement;
import mekhq.campaign.work.ReplacementItem;

/**
 *
 * @author Jay Lawson <jaylawson39 at yahoo.com>
 */
public class MekActuator extends Part {

    protected float tonnage;
    protected int type;
    
    public MekActuator(boolean salvage, float ton, int type) {
        super(salvage);
        this.tonnage = ton;
        this.type = type;
        this.name = "Actuator";
    }

    @Override
    public boolean canBeUsedBy(ReplacementItem task) {
        return task instanceof MekActuatorReplacement 
                && tonnage == ((MekActuatorReplacement)task).getUnit().getEntity().getWeight()
                && type == ((MekActuatorReplacement)task).getType();
    }
    
}
