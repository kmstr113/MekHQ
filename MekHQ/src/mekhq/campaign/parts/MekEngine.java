/*
 * MekEngine.java
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
import java.util.ArrayList;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import megamek.common.CriticalSlot;
import megamek.common.Engine;
import megamek.common.Entity;
import megamek.common.EquipmentType;
import megamek.common.Mech;
import megamek.common.TechConstants;
import mekhq.campaign.Faction;
import mekhq.campaign.MekHqXmlUtil;
import mekhq.campaign.work.MekEngineReplacement;
import mekhq.campaign.work.ReplacementItem;

/**
 * 
 * @author Jay Lawson <jaylawson39 at yahoo.com>
 */
public class MekEngine extends Part {
	private static final long serialVersionUID = -6961398614705924172L;
	protected Engine engine;

	public MekEngine() {
		this(false, 0, 0, new Engine(0, -1, -1), 0);
	}

	public MekEngine(boolean salvage, int tonnage, int faction, Engine e,
			double clanMultiplier) {
		super(salvage, tonnage);
		this.engine = e;
		this.name = engine.getEngineName() + " Engine";
		this.engine = e;

		double c = getEngine().getBaseCost() * getEngine().getRating()
				* getTonnage() / 75.0;
		this.cost = (long) Math.round(c);

		// Increase cost for Clan parts when player is IS faction
		// Increase cost for Clan parts when player is IS faction
		if (isClanTechBase() && !Faction.isClanFaction(faction))
			this.cost *= clanMultiplier;
	}

	public Engine getEngine() {
		return engine;
	}


	@Override
	public boolean canBeUsedBy(ReplacementItem task) {
		if (task instanceof MekEngineReplacement
				&& task.getUnit().getEntity() instanceof Mech) {
			Engine eng = task.getUnit().getEntity().getEngine();
			if (null != eng) {
				return getEngine().getEngineType() == eng.getEngineType()
						&& getEngine().getRating() == eng.getRating()
						&& getEngine().getTechType() == eng.getTechType()
						&& getTonnage() == task.getUnit().getEntity()
								.getWeight();
			}
		}
		return false;
	}

	@Override
	public boolean isSamePartTypeAndStatus(Part part) {
		return part instanceof MekEngine
				&& getName().equals(part.getName())
				&& getStatus().equals(part.getStatus())
				&& getEngine().getEngineType() == ((MekEngine) part)
						.getEngine().getEngineType()
				&& getEngine().getRating() == ((MekEngine) part).getEngine()
						.getRating()
				&& getEngine().getTechType() == ((MekEngine) part).getEngine()
						.getTechType()
				&& getTonnage() == ((MekEngine) part).getTonnage();
	}

	@Override
	public int getPartType() {
		return PART_TYPE_MEK_ENGINE;
	}

	@Override
	public boolean isClanTechBase() {
		String techBase = TechConstants.getTechName(getEngine().getTechType());

		if (techBase.equals("Clan"))
			return true;
		else if (techBase.equals("Inner Sphere"))
			return false;
		else
			return false;
	}

	@Override
	public int getTech() {
		if (getEngine().getTechType() < 0
				|| getEngine().getTechType() >= TechConstants.SIZE)
			return TechConstants.T_IS_TW_NON_BOX;
		else
			return getEngine().getTechType();
	}

	@Override
	public void writeToXml(PrintWriter pw1, int indent, int id) {
		writeToXmlBegin(pw1, indent, id);
		// The engine is a MM object...
		// And doesn't support XML serialization...
		// But it's defined by 3 ints. So we'll save those here.
		pw1.println(MekHqXmlUtil.indentStr(indent + 1) + "<engineType>"
				+ engine.getEngineType() + "</engineType>");
		pw1.println(MekHqXmlUtil.indentStr(indent + 1) + "<engineRating>"
				+ engine.getRating() + "</engineRating>");
		// TODO: Modify MM to get access to engine flags.
		// Without those flags, the engine has a good chance of being loaded wrong!
		/*
		 * pw1.println(MekHqXmlUtil.indentStr(indent+1)
		 * +"<engineFlags>"
		 * +engine.getFlags()
		 * +"</engineFlags>");
		 */
		writeToXmlEnd(pw1, indent, id);
	}

	@Override
	protected void loadFieldsFromXmlNode(Node wn) {
		NodeList nl = wn.getChildNodes();
		int engineType = -1;
		int engineRating = -1;
		int engineFlags = 0;
		
		for (int x=0; x<nl.getLength(); x++) {
			Node wn2 = nl.item(x);
			
			if (wn2.getNodeName().equalsIgnoreCase("engineType")) {
				engineType = Integer.parseInt(wn2.getTextContent());
			} else if (wn2.getNodeName().equalsIgnoreCase("engineRating")) {
				engineRating = Integer.parseInt(wn2.getTextContent());
			} else if (wn2.getNodeName().equalsIgnoreCase("engineFlags")) {
				engineFlags = Integer.parseInt(wn2.getTextContent());
			} 
		}
		
		engine = new Engine(engineType, engineRating, engineFlags);
	}

	@Override
	public int getAvailability(int era) {
		switch(engine.getTechType()) {
		case Engine.COMBUSTION_ENGINE:
			if(era == EquipmentType.ERA_SL) {
				return EquipmentType.RATING_A;
			} else if(era == EquipmentType.ERA_SW) {
				return EquipmentType.RATING_A;
			} else {
				return EquipmentType.RATING_A;
			}
		case Engine.FUEL_CELL:
			if(era == EquipmentType.ERA_SL) {
				return EquipmentType.RATING_C;
			} else if(era == EquipmentType.ERA_SW) {
				return EquipmentType.RATING_D;
			} else {
				return EquipmentType.RATING_D;
			}
		case Engine.FISSION:
			if(era == EquipmentType.ERA_SL) {
				return EquipmentType.RATING_E;
			} else if(era == EquipmentType.ERA_SW) {
				return EquipmentType.RATING_E;
			} else {
				return EquipmentType.RATING_D;
			}
		case Engine.XL_ENGINE:
			if(era == EquipmentType.ERA_SL) {
				return EquipmentType.RATING_D;
			} else if(era == EquipmentType.ERA_SW) {
				return EquipmentType.RATING_F;
			} else {
				return EquipmentType.RATING_E;
			}
		case Engine.LIGHT_ENGINE:
		case Engine.COMPACT_ENGINE:
			if(era == EquipmentType.ERA_SL) {
				return EquipmentType.RATING_X;
			} else if(era == EquipmentType.ERA_SW) {
				return EquipmentType.RATING_X;
			} else {
				return EquipmentType.RATING_E;
			}
		case Engine.XXL_ENGINE:
			if(era == EquipmentType.ERA_SL) {
				return EquipmentType.RATING_X;
			} else if(era == EquipmentType.ERA_SW) {
				return EquipmentType.RATING_X;
			} else {
				return EquipmentType.RATING_F;
			}
		default:
			if(era == EquipmentType.ERA_SL) {
				return EquipmentType.RATING_C;
			} else if(era == EquipmentType.ERA_SW) {
				return EquipmentType.RATING_E;
			} else {
				return EquipmentType.RATING_D;
			}
		}
	}

	@Override
	public int getTechRating() {
		switch(engine.getTechType()) {
		case Engine.COMBUSTION_ENGINE:
			return EquipmentType.RATING_C;
		case Engine.FUEL_CELL:
		case Engine.FISSION:
			return EquipmentType.RATING_D;
		case Engine.XL_ENGINE:	
		case Engine.LIGHT_ENGINE:
		case Engine.COMPACT_ENGINE:
			return EquipmentType.RATING_E;
		case Engine.XXL_ENGINE:
			return EquipmentType.RATING_F;
		default:
			return EquipmentType.RATING_D;
		}
	}

	@Override
	public void fix() {
		hits = 0;
		if(null != unit) {
			unit.repairSystem(CriticalSlot.TYPE_SYSTEM, Mech.SYSTEM_ENGINE);
		}
	}

	@Override
	public Part getMissingPart() {
		return new MissingMekEngine(isSalvage(), getTonnage(), 0, getEngine(), 0);
	}

	@Override
	public void remove(boolean salvage) {
		if(null != unit) {
			unit.destroySystem(CriticalSlot.TYPE_SYSTEM, Mech.SYSTEM_ENGINE);
			if(!salvage) {
				unit.campaign.removePart(this);
			}
			unit.removePart(this);
			Part missing = getMissingPart();
			unit.campaign.addPart(missing);
			unit.addPart(missing);
		}
		setUnit(null);	
	}

	@Override
	public void updateConditionFromEntity() {
		if(null != unit) {
			int engineHits = 0;
			int engineCrits = 0;
			Entity entity = unit.getEntity();
			for (int i = 0; i < entity.locations(); i++) {
				engineHits += entity.getHitCriticals(CriticalSlot.TYPE_SYSTEM,
						Mech.SYSTEM_ENGINE, i);
				engineCrits += entity.getNumberOfCriticals(
						CriticalSlot.TYPE_SYSTEM, Mech.SYSTEM_ENGINE, i);
			}
			if(engineHits > engineCrits) {
				remove(false);
				return;
			} 
			else if(engineHits > 0) {
				hits = engineHits;
			} else {
				hits = 0;
			}
			this.time = 0;
			this.difficulty = 0;
			if (hits == 1) {
	            this.time = 100;
	            this.difficulty = -1;
	        } else if (hits == 2) {
	            this.time = 200;
	            this.difficulty = 0;
	        } else if (hits > 2) {
	            this.time = 300;
	            this.difficulty = 2;
	        }
			if(isSalvaging()) {
				this.time = 360;
				this.difficulty = -1;
			}
		}		
	}

	@Override
	public boolean needsFixing() {
		return hits > 0;
	}

	@Override
	public void updateConditionFromPart() {
		if(null != unit) {
			if(hits == 0) {
				unit.repairSystem(CriticalSlot.TYPE_SYSTEM, Mech.SYSTEM_ENGINE);
			} else {
				for(int i = 0; i < hits; i++) {
					unit.hitSystem(CriticalSlot.TYPE_SYSTEM, Mech.SYSTEM_ENGINE);
				}
			}
		}
	}
	
	@Override
	 public String checkFixable() {
		if(isSalvaging()) {
			return null;
		}
		 for(int i = 0; i < unit.getEntity().locations(); i++) {
			 if(unit.getEntity().getNumberOfCriticals(CriticalSlot.TYPE_SYSTEM, Mech.SYSTEM_ENGINE, i) > 0
					 && unit.isLocationDestroyed(i)) {
				 return unit.getEntity().getLocationName(i) + " is destroyed.";
			 }
		 }
		 return null;
	 }
}
