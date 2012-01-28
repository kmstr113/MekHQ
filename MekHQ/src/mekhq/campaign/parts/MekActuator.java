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

import java.io.PrintWriter;

import megamek.common.BipedMech;
import megamek.common.CriticalSlot;
import megamek.common.EquipmentType;
import megamek.common.Mech;
import megamek.common.TechConstants;
import mekhq.campaign.Campaign;
import mekhq.campaign.MekHqXmlUtil;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Jay Lawson <jaylawson39 at yahoo.com>
 */
public class MekActuator extends Part {
	private static final long serialVersionUID = 719878556021696393L;
	protected int type;
	protected int location;

	public MekActuator() {
		this(0, 0, null);
	}
	
	public MekActuator clone() {
		return new MekActuator(getUnitTonnage(), type, location, campaign);
	}
	
    public int getType() {
        return type;
    }
    
    public void setLocation(int loc) {
    	this.location = loc;
    }
    
    public MekActuator(int tonnage, int type, Campaign c) {
        this(tonnage, type, -1, c);
    }
    
    public MekActuator(int tonnage, int type, int loc, Campaign c) {
    	super(tonnage, c);
        this.type = type;
        Mech m = new BipedMech();
        this.name = m.getSystemName(type) + " Actuator" ;
        this.location = loc;
    }

    @Override
    public double getTonnage() {
    	//TODO: how much do actuators weight?
    	//apparently nothing
    	return 0;
    }
    
    @Override
    public long getStickerPrice() {
        long unitCost = 0;
        switch (getType()) {
            case (Mech.ACTUATOR_UPPER_ARM) : {
                unitCost = 100;
                break;
            }
            case (Mech.ACTUATOR_LOWER_ARM) : {
                unitCost = 50;
                break;
            }
            case (Mech.ACTUATOR_HAND) : {
                unitCost = 80;
                break;
            }
            case (Mech.ACTUATOR_UPPER_LEG) : {
                unitCost = 150;
                break;
            }
            case (Mech.ACTUATOR_LOWER_LEG) : {
                unitCost = 80;
                break;
            }
            case (Mech.ACTUATOR_FOOT) : {
                unitCost = 120;
                break;
            }
            case (Mech.ACTUATOR_HIP) : {
                // not used
                unitCost = 0;
                break;
            }
            case (Mech.ACTUATOR_SHOULDER) : {
                // not used
                unitCost = 0;
                break;
            }
        }
        return getUnitTonnage() * unitCost;
    }

    @Override
    public boolean isSamePartTypeAndStatus (Part part) {
    	if(needsFixing() || part.needsFixing()) {
    		return false;
    	}
        return part instanceof MekActuator
                && getType() == ((MekActuator)part).getType()
                && getUnitTonnage() == ((MekActuator)part).getUnitTonnage();
    }

    @Override
    public int getPartType() {
        return PART_TYPE_MEK_ACTUATOR;
    }
    
    public int getLocation() {
    	return location;
    }
    
	@Override
	public void writeToXml(PrintWriter pw1, int indent) {
		writeToXmlBegin(pw1, indent);
		pw1.println(MekHqXmlUtil.indentStr(indent+1)
				+"<type>"
				+type
				+"</type>");
		pw1.println(MekHqXmlUtil.indentStr(indent+1)
				+"<location>"
				+location
				+"</location>");
		writeToXmlEnd(pw1, indent);
	}

	@Override
	protected void loadFieldsFromXmlNode(Node wn) {
		NodeList nl = wn.getChildNodes();
		
		for (int x=0; x<nl.getLength(); x++) {
			Node wn2 = nl.item(x);
			
			if (wn2.getNodeName().equalsIgnoreCase("type")) {
				type = Integer.parseInt(wn2.getTextContent());
			} else if (wn2.getNodeName().equalsIgnoreCase("location")) {
				location = Integer.parseInt(wn2.getTextContent());
			} 
		}
	}

	@Override
	public int getAvailability(int era) {
		return EquipmentType.RATING_C;
	}

	@Override
	public int getTechRating() {
		return EquipmentType.RATING_C;
	}
	
	@Override
	public void fix() {
		super.fix();
		if(null != unit) {
			unit.repairSystem(CriticalSlot.TYPE_SYSTEM, type, location);
		}
	}
	
	@Override
	public int getTechBase() {
		return T_BOTH;
	}
	
	@Override
	public int getTechLevel() {
		return TechConstants.T_INTRO_BOXSET;
	}

	@Override
	public Part getMissingPart() {
		return new MissingMekActuator(getUnitTonnage(), type, location, campaign);
	}

	@Override
	public void remove(boolean salvage) {
		if(null != unit) {
			unit.destroySystem(CriticalSlot.TYPE_SYSTEM, type, location);
			if(!salvage) {
				campaign.removePart(this);
			}
			unit.removePart(this);
			Part missing = getMissingPart();
			campaign.addPart(missing);
			unit.addPart(missing);
		}	
		setUnit(null);
		location = -1;
	}

	@Override
	public void updateConditionFromEntity() {
		if(null != unit) {
			hits = unit.getEntity().getHitCriticals(CriticalSlot.TYPE_SYSTEM, type, location);	
			if(hits == 0) {
				time = 0;
				difficulty = 0;
			} 
			else if(hits >= 1) {
				time = 120;
				difficulty = 0;
			}
			if(isSalvaging()) {
				this.time = 90;
				this.difficulty = -3;
			}
		}
		
	}

	@Override
	public boolean needsFixing() {
		return hits > 0;
	}
	
	@Override
	public String getDetails() {
		if(null != unit) {
			return unit.getEntity().getLocationName(location);
		}
		return getUnitTonnage() + " tons";
	}

	@Override
	public void updateConditionFromPart() {
		if(null != unit) {
			if(hits > 0) {
				unit.destroySystem(CriticalSlot.TYPE_SYSTEM, type, location);
			} else {
				unit.repairSystem(CriticalSlot.TYPE_SYSTEM, type, location);
			}
		}	
	}
	
	@Override
	public String checkFixable() {
		if(isSalvaging()) {
			return null;
		}
		if(unit.isLocationDestroyed(location)) {
			return unit.getEntity().getLocationName(location) + " is destroyed.";
		}
		return null;
	}
	
	@Override
	public boolean onBadHipOrShoulder() {
		return null != unit && unit.hasBadHipOrShoulder(location);
	}
}
