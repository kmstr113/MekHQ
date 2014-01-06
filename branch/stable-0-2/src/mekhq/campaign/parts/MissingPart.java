/*
 * MissingPart.java
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
import java.io.Serializable;
import java.util.UUID;

import megamek.common.EquipmentType;
import megamek.common.TargetRoll;
import mekhq.Utilities;
import mekhq.campaign.Campaign;
import mekhq.campaign.MekHqXmlSerializable;
import mekhq.campaign.MekHqXmlUtil;
import mekhq.campaign.personnel.SkillType;
import mekhq.campaign.work.IAcquisitionWork;
import mekhq.campaign.work.IPartWork;
import mekhq.campaign.work.Modes;

/**
 * A missing part is a placeholder on a unit to indicate that a replacement
 * task needs to be performed
 * @author Jay Lawson <jaylawson39 at yahoo.com>
 */
public abstract class MissingPart extends Part implements Serializable, MekHqXmlSerializable, IPartWork, IAcquisitionWork {

	/**
	 * 
	 */
	private static final long serialVersionUID = 300672661487966982L;
	
	
	
	public MissingPart(int tonnage, Campaign c) {
		super(tonnage, c);
	}
	
	public MissingPart clone() {
		//should never be called
		return null;
	}
	
	@Override
	public long getStickerPrice() {
		//missing parts aren't worth a thing
		return 0;
	}
	
	@Override
	public boolean isSalvaging() {
		return false;
	}
	
	@Override
	public String getStatus() {
		return "Destroyed";
	}
	
	@Override 
	public boolean isSamePartType(Part part) {
		//missing parts should always return false
		return false;
	}
	
	public String getDesc() {
		String bonus = getAllMods().getValueAsString();
		if (getAllMods().getValue() > -1) {
			bonus = "+" + bonus;
		}
		bonus = "(" + bonus + ")";
		String toReturn = "<html><font size='2'";
		String scheduled = "";
		if (getAssignedTeamId() != null) {
			scheduled = " (scheduled) ";
		}
	
		//if (this instanceof ReplacementItem
		//		&& !((ReplacementItem) this).hasPart()) {
		//	toReturn += " color='white'";
		//}
		toReturn += ">";
		toReturn += "<b>Replace " + getName() + "</b><br/>";
		toReturn += getDetails() + "<br/>";
		toReturn += "" + getTimeLeft() + " minutes" + scheduled;
		toReturn += ", " + SkillType.getExperienceLevelName(getSkillMin());
		toReturn += " " + bonus;
		if (getMode() != Modes.MODE_NORMAL) {
			toReturn += "<br/><i>" + getCurrentModeName() + "</i>";
		}
		toReturn += "</font></html>";
		return toReturn;
	}
	
	@Override
	public String succeed() {	
		fix();
		return " <font color='green'><b> replaced.</b></font>";
	}
	
	@Override 
	public void fix() {
		Part replacement = findReplacement(false);
		if(null != replacement) {
			Part actualReplacement = replacement.clone();
			unit.addPart(actualReplacement);
			campaign.addPart(actualReplacement);
			replacement.decrementQuantity();
			remove(false);
			//assign the replacement part to the unit			
			actualReplacement.updateConditionFromPart();
		}
	}
	
	@Override
	public void remove(boolean salvage) {
		campaign.removePart(this);
		if(null != unit) {
			unit.removePart(this);
		}	
		setUnit(null);
	}
	
	public abstract boolean isAcceptableReplacement(Part part, boolean refit);
	
	public Part findReplacement(boolean refit) {
		Part bestPart = null;	

		//check to see if we already have a replacement assigned
		if(replacementId > -1) {
			bestPart = campaign.getPart(replacementId);
			if(null != bestPart) {
				return bestPart;
			}
		}
		//dont just return with the first part if it is damaged
		for(Part part : campaign.getSpareParts()) {
			if(part.isReservedForRefit() || part.isBeingWorkedOn() || part.isReservedForReplacement()) {
				continue;
			}
			//TODO: check for being present
			if(isAcceptableReplacement(part, refit)) {
				if(null == bestPart) {
					bestPart = part;
				} else if(bestPart.needsFixing() && !part.needsFixing()) {
					bestPart = part;
				}
			}
		}
		return bestPart;
	}
	
	public boolean isReplacementAvailable() {
		return null != findReplacement(false);
	}

	@Override
    public String getDetails() {
		if(isReplacementAvailable()) {
			return "Replacement part available";
		} else {
			return "<font color='red'>Replacement part not available</font>";
		}
    }
	
	@Override
	public boolean needsFixing() {
		//missing parts always need fixing
		if(null != unit) {
			return !unit.isSalvage() && unit.isRepairable();
		}
		return false;
	}
	
	@Override
	public Part getMissingPart() {
		//do nothing - this should never be accessed
		return null;
	}
	
	@Override
	public void updateConditionFromEntity() {
		//do nothing
	}
	
	@Override
	public String fail(int rating) {
		skillMin = ++rating;
		timeSpent = 0;
		shorthandedMod = 0;
		if(skillMin > SkillType.EXP_ELITE) {
			Part part = findReplacement(false);
			if(null != part) {
				part.decrementQuantity();
				skillMin = SkillType.EXP_GREEN;
			}
			return " <font color='red'><b> failed and part destroyed.</b></font>";
		} else {
			return " <font color='red'><b> failed.</b></font>";
		}
	}
	
	@Override
	public TargetRoll getAllAcquisitionMods() {
        TargetRoll target = new TargetRoll();
        // Faction and Tech mod
        int factionMod = 0;
        if (campaign.getCampaignOptions().useFactionModifiers()) {
        	factionMod = campaign.getFaction().getTechMod(this, campaign);
        }   
        //availability mod
        int avail = getAvailability(campaign.getEra());
        int availabilityMod = Availability.getAvailabilityModifier(avail);
        target.addModifier(availabilityMod, "availability (" + EquipmentType.getRatingName(avail) + ")");
        if(factionMod != 0) {
     	   target.addModifier(factionMod, "faction");
        }
        return target;
    }
	
	@Override 
	public boolean hasCheckedToday() {
		return checkedToday;
	}
	
	@Override
	public void setCheckedToday(boolean b) {
		this.checkedToday = b;
	}
	
	@Override
	public String getAcquisitionDesc() {
		String bonus = getAllAcquisitionMods().getValueAsString();
		if(getAllAcquisitionMods().getValue() > -1) {
			bonus = "+" + bonus;
		}
		bonus = "(" + bonus + ")";
		String toReturn = "<html><font size='2'";
		
		toReturn += ">";
		toReturn += "<b>" + getAcquisitionName() + "</b> " + bonus + "<br/>";
		toReturn += Utilities.getCurrencyString(getNewPart().getActualValue()) + "<br/>";
		toReturn += "</font></html>";
		return toReturn;
	}
	
	@Override
	public String find() {
		Part newPart = getNewPart();
		campaign.buyPart(newPart);
		setCheckedToday(true);
		return "<font color='green'> part found.</font>";
	}
	
	@Override
	public String failToFind() {
		setCheckedToday(true);
		return "<font color='red'> part not found.</font>";
	}
	
	@Override
	public void writeToXmlBegin(PrintWriter pw1, int indent) {
		super.writeToXmlBegin(pw1, indent);
		pw1.println(MekHqXmlUtil.indentStr(indent+1)
				+"<checkedToday>"
				+checkedToday
				+"</checkedToday>");
		pw1.println(MekHqXmlUtil.indentStr(indent+1)
				+"<replacementId>"
				+replacementId
				+"</replacementId>");
	}
	
	@Override
	public void writeToXml(PrintWriter pw1, int indent) {
		writeToXmlBegin(pw1, indent);
		writeToXmlEnd(pw1, indent);
	}
	
	@Override
	public String checkScrappable() {
		if(!isReplacementAvailable()) {
			return "Nothing to scrap";
		}
		return null;
	}
	
	@Override
	public String scrap() {
		Part replace = findReplacement(false);
		if(null != replace) {
			replace.decrementQuantity();
		}
		skillMin = SkillType.EXP_GREEN;
		return replace.getName() + " scrapped.";
	}
	
	@Override
	public String getAcquisitionName() {
		return getPartName();
	}
	
	@Override
	public int getTechLevel() {
		return getNewPart().getTechLevel();
	}
	
	@Override
	public void setTeamId(UUID i) {
		super.setTeamId(i);
		Part replacement = findReplacement(false);
		if(null != i) {
			//this is being set as an overnight repair, so
			//we also need to reserve the replacement. If the 
			//quantity of the replacement is more than one, we will 
			//also need to split off a separate one
			//shouldn't be null, but it never hurts to check
			if(null != replacement) {
				if(replacement.getQuantity() > 1) {
					Part actualReplacement = replacement.clone();
					actualReplacement.setReserveId(i);
					campaign.addPart(actualReplacement);
					replacementId = actualReplacement.getId();
					replacement.decrementQuantity();
				} else {
					replacement.setReserveId(i);
					replacementId = replacement.getId();
				}
			}
		} else {
			//the replacement either succeeded or failed. Either way, we need
			//to be sure to clear out the reservation status of the replacement
			//part
			if(replacementId > -1 && null != replacement) {
				replacementId = -1;
				replacement.setReserveId(null);
				if(replacement.isSpare()) {
					Part spare = campaign.checkForExistingSparePart(replacement);
					if(null != spare) {
						spare.incrementQuantity();
						campaign.removePart(replacement);
					}
				}
			}
		}
		
	}
}
