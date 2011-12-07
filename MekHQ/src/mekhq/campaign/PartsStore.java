/*
 * PartsStore.java
 * 
 * Copyright (c) 2011 Jay Lawson <jaylawson39 at yahoo.com>. All rights reserved.
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
import java.util.Enumeration;
import java.util.Iterator;

import megamek.common.Aero;
import megamek.common.AmmoType;
import megamek.common.Engine;
import megamek.common.EquipmentType;
import megamek.common.Mech;
import megamek.common.MiscType;
import mekhq.campaign.parts.AeroHeatSink;
import mekhq.campaign.parts.AeroSensor;
import mekhq.campaign.parts.AmmoStorage;
import mekhq.campaign.parts.Armor;
import mekhq.campaign.parts.Avionics;
import mekhq.campaign.parts.EnginePart;
import mekhq.campaign.parts.EquipmentPart;
import mekhq.campaign.parts.FireControlSystem;
import mekhq.campaign.parts.HeatSink;
import mekhq.campaign.parts.JumpJet;
import mekhq.campaign.parts.LandingGear;
import mekhq.campaign.parts.MekActuator;
import mekhq.campaign.parts.MekCockpit;
import mekhq.campaign.parts.MekGyro;
import mekhq.campaign.parts.MekLifeSupport;
import mekhq.campaign.parts.MekLocation;
import mekhq.campaign.parts.MekSensor;
import mekhq.campaign.parts.Part;
import mekhq.campaign.parts.Rotor;
import mekhq.campaign.parts.Turret;
import mekhq.campaign.parts.VeeSensor;
import mekhq.campaign.parts.VeeStabiliser;


/**
 * This is a parts store which will contain one copy of every possible
 * part that might be needed as well as a variety of helper functions to
 * acquire parts.
 * 
 * We could in the future extend this to different types of stores that have different finite numbers of
 * parts in inventory
 * 
 * @author Jay Lawson <jaylawson39 at yahoo.com>
 */
public class PartsStore implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1686222527383868364L;
	
	private ArrayList<Part> parts;
	
	public PartsStore() {
		parts = new ArrayList<Part>();
		stock();
	}
	
	public ArrayList<PartInventory> getInventory(Campaign c) {
		ArrayList<PartInventory> partsInventory = new ArrayList<PartInventory>();

		Iterator<Part> itParts = parts.iterator();
		while (itParts.hasNext()) {
			Part part = itParts.next();
			if(part.isClanTechBase() && !c.getCampaignOptions().allowClanPurchases()) {
				continue;
			}
			if(!part.isClanTechBase() && !c.getCampaignOptions().allowISPurchases()) {
				continue;
			}
			//TODO: limit by tech class (experimental, advanced)
			//TODO: limit by year
			partsInventory.add(new PartInventory(part, 1));
		}

		return partsInventory;
	}
	
	public void stock() {
		stockWeaponsAmmoAndEquipment();	
		stockMekActuators();
		stockEngines();
		stockGyros();
		stockMekComponents();
		stockAeroComponents();
		stockVeeComponents();
		stockArmor();
		stockMekLocations();
		stockVeeLocations();
	}
	
	private void stockWeaponsAmmoAndEquipment() {
        for (Enumeration<EquipmentType> e = EquipmentType.getAllTypes(); e.hasMoreElements();) {
            EquipmentType et = e.nextElement();
            if(!et.isHittable()) {
            	continue;
            }
			if(et instanceof AmmoType) {
				parts.add(new AmmoStorage(0, et, ((AmmoType)et).getShots()));
			}
			else if(et instanceof MiscType && (et.hasFlag(MiscType.F_HEAT_SINK) || et.hasFlag(MiscType.F_DOUBLE_HEAT_SINK))) {
            	parts.add(new HeatSink(0, et, -1));
			} else if(et instanceof MiscType && et.hasFlag(MiscType.F_JUMP_JET)) {
				parts.add(new JumpJet(0, et, -1));
			} else {
				parts.add(new EquipmentPart(0, et, -1));
			}
        }
        //lets throw aero heat sinks in here as well
        parts.add(new AeroHeatSink(0, Aero.HEAT_SINGLE));
        parts.add(new AeroHeatSink(0, Aero.HEAT_DOUBLE));
	}
	
	private void stockMekActuators() {
		for(int i = Mech.ACTUATOR_UPPER_ARM; i <= Mech.ACTUATOR_FOOT; i++) {
			if(i == Mech.ACTUATOR_HIP) {
				continue;
			}
			int ton = 20;
			while(ton <= 100) {
				parts.add(new MekActuator(ton, i, -1));
				ton += 5;
			}
		}
	}
	
	private void stockEngines() {
		int rating = 5;
		while(rating <= 400) {
			for(int i = 0; i <= Engine.FISSION; i++) {
				Engine engine = new Engine(rating, i, 0);
				if(engine.engineValid) {
					parts.add(new EnginePart(0, engine));
				}
				engine = new Engine(rating, i, Engine.CLAN_ENGINE);
				if(engine.engineValid) {
					parts.add(new EnginePart(0, engine));
				}
			}
			rating += 5;
		}
	}
	
	private void stockGyros() {
		for(double i = 0.5; i <= 8.0; i += 0.5) {
			//standard at intervals of 1.0, up to 4
			if(i % 1.0 == 0 && i <= 4.0) {
				parts.add(new MekGyro(0, Mech.GYRO_STANDARD, i));
			}
			//compact at intervals of 1.5, up to 6
			if(i % 1.5 == 0 && i <= 6.0) {
				parts.add(new MekGyro(0, Mech.GYRO_COMPACT, i));
			}
			//XL at 0.5 intervals up to 2
			if(i % 0.5 == 0 && i <= 2.0) {
				parts.add(new MekGyro(0, Mech.GYRO_XL, i));
			}
			//Heavy duty at 2.0 intervals
			if(i % 2.0 == 0) {
				parts.add(new MekGyro(0, Mech.GYRO_HEAVY_DUTY, i));
			}
			
		}
	}
	
	private void stockMekComponents() {
		parts.add(new MekLifeSupport(0));
		for(int ton = 20; ton <= 100; ton += 5) {
			parts.add(new MekSensor(ton));
		}
		for(int type = Mech.COCKPIT_STANDARD; type < Mech.COCKPIT_STRING.length; type++) {
			parts.add(new MekCockpit(0, type));
		}
	}
	
	private void stockAeroComponents() {
		parts.add(new AeroHeatSink(0, Aero.HEAT_SINGLE));
		parts.add(new AeroHeatSink(0, Aero.HEAT_DOUBLE));
		parts.add(new AeroSensor(0, false));
		parts.add(new AeroSensor(0, true));
		parts.add(new Avionics(0));
		parts.add(new FireControlSystem(0));
		parts.add(new LandingGear(0));
	}
	
	private void stockVeeComponents() {
		parts.add(new VeeSensor(0));
		parts.add(new VeeStabiliser(0,-1));
		for(int ton = 5; ton <= 100; ton=ton+5) {
			parts.add(new Rotor(ton));
			parts.add(new Turret(ton, -1));
		}
	}
	
	private void stockArmor() {
		//Standard armor
		int amount = (int) (5.0 * 16.0 * EquipmentType.getArmorPointMultiplier(EquipmentType.T_ARMOR_STANDARD, false));
		parts.add(new Armor(0, EquipmentType.T_ARMOR_STANDARD, amount, -1, false, false));
		amount = (int) (5.0 * 16.0 * EquipmentType.getArmorPointMultiplier(EquipmentType.T_ARMOR_STANDARD, true));
		parts.add(new Armor(0, EquipmentType.T_ARMOR_STANDARD, amount, -1, false, true));
		//Ferro-Fibrous
		amount = (int) (5.0 * 16.0 * EquipmentType.getArmorPointMultiplier(EquipmentType.T_ARMOR_FERRO_FIBROUS, false));
		parts.add(new Armor(0, EquipmentType.T_ARMOR_FERRO_FIBROUS, amount, -1, false, false));
		amount = (int) (5.0 * 16.0 * EquipmentType.getArmorPointMultiplier(EquipmentType.T_ARMOR_FERRO_FIBROUS, true));
		parts.add(new Armor(0, EquipmentType.T_ARMOR_FERRO_FIBROUS, amount, -1, false, true));
		//Reactive
		amount = (int) (5.0 * 16.0 * EquipmentType.getArmorPointMultiplier(EquipmentType.T_ARMOR_REACTIVE, false));
		parts.add(new Armor(0, EquipmentType.T_ARMOR_REACTIVE, amount, -1, false, false));
		amount = (int) (5.0 * 16.0 * EquipmentType.getArmorPointMultiplier(EquipmentType.T_ARMOR_REACTIVE, true));
		parts.add(new Armor(0, EquipmentType.T_ARMOR_REACTIVE, amount, -1, false, true));
		//Reflective
		amount = (int) (5.0 * 16.0 * EquipmentType.getArmorPointMultiplier(EquipmentType.T_ARMOR_REFLECTIVE, false));
		parts.add(new Armor(0, EquipmentType.T_ARMOR_REFLECTIVE, amount, -1, false, false));
		amount = (int) (5.0 * 16.0 * EquipmentType.getArmorPointMultiplier(EquipmentType.T_ARMOR_REFLECTIVE, true));
		parts.add(new Armor(0, EquipmentType.T_ARMOR_REFLECTIVE, amount, -1, false, true));
		//Hardened
		amount = (int) (5.0 * 16.0 * EquipmentType.getArmorPointMultiplier(EquipmentType.T_ARMOR_HARDENED, false));
		parts.add(new Armor(0, EquipmentType.T_ARMOR_HARDENED, amount, -1, false, false));
		amount = (int) (5.0 * 16.0 * EquipmentType.getArmorPointMultiplier(EquipmentType.T_ARMOR_HARDENED, true));
		parts.add(new Armor(0, EquipmentType.T_ARMOR_HARDENED, amount, -1, false, true));
		//Light/Heavy FF
		amount = (int) (5.0 * 16.0 * EquipmentType.getArmorPointMultiplier(EquipmentType.T_ARMOR_LIGHT_FERRO, false));
		parts.add(new Armor(0, EquipmentType.T_ARMOR_LIGHT_FERRO, amount, -1, false, false));
		amount = (int) (5.0 * 16.0 * EquipmentType.getArmorPointMultiplier(EquipmentType.T_ARMOR_HEAVY_FERRO, false));
		parts.add(new Armor(0, EquipmentType.T_ARMOR_HEAVY_FERRO, amount, -1, false, false));
		//Stealth
		amount = (int) (5.0 * 16.0 * EquipmentType.getArmorPointMultiplier(EquipmentType.T_ARMOR_STEALTH, false));
		parts.add(new Armor(0, EquipmentType.T_ARMOR_STEALTH, amount, -1, false, false));
		//Commercial
		amount = (int) (5.0 * 16.0 * EquipmentType.getArmorPointMultiplier(EquipmentType.T_ARMOR_COMMERCIAL, false));
		parts.add(new Armor(0, EquipmentType.T_ARMOR_COMMERCIAL, amount, -1, false, false));
		//Industrial
		amount = (int) (5.0 * 16.0 * EquipmentType.getArmorPointMultiplier(EquipmentType.T_ARMOR_INDUSTRIAL, false));
		parts.add(new Armor(0, EquipmentType.T_ARMOR_INDUSTRIAL, amount, -1, false, false));
		//Heavy Industrial
		amount = (int) (5.0 * 16.0 * EquipmentType.getArmorPointMultiplier(EquipmentType.T_ARMOR_HEAVY_INDUSTRIAL, false));
		parts.add(new Armor(0, EquipmentType.T_ARMOR_HEAVY_INDUSTRIAL, amount, -1, false, false));
		//Ferro-Lamellor
		amount = (int) (5.0 * 16.0 * EquipmentType.getArmorPointMultiplier(EquipmentType.T_ARMOR_FERRO_LAMELLOR, false));
		parts.add(new Armor(0, EquipmentType.T_ARMOR_FERRO_LAMELLOR, amount, -1, false, true));
		//Primitive
		amount = (int) (5.0 * 16.0 * EquipmentType.getArmorPointMultiplier(EquipmentType.T_ARMOR_PRIMITIVE, false));
		parts.add(new Armor(0, EquipmentType.T_ARMOR_PRIMITIVE, amount, -1, false, false));
		/*
		 * These are all warship armors
		//Ferro-Carbide
		amount = (int) (5.0 * 16.0 * EquipmentType.getArmorPointMultiplier(EquipmentType.T_ARMOR_FERRO_CARBIDE, false));
		parts.add(new Armor(0, EquipmentType.T_ARMOR_FERRO_CARBIDE, amount, -1, false, false));
		//Lemellor Ferro Carbide
		amount = (int) (5.0 * 16.0 * EquipmentType.getArmorPointMultiplier(EquipmentType.T_ARMOR_LAMELLOR_FERRO_CARBIDE, false));
		parts.add(new Armor(0, EquipmentType.T_ARMOR_LAMELLOR_FERRO_CARBIDE, amount, -1, false, false));
		//Ferro Improved
		amount = (int) (5.0 * 16.0 * EquipmentType.getArmorPointMultiplier(EquipmentType.T_ARMOR_FERRO_IMP, false));
		parts.add(new Armor(0, EquipmentType.T_ARMOR_FERRO_IMP, amount, -1, false, false));
		*/
	}
	
	private void stockMekLocations() {
		for(int loc = Mech.LOC_HEAD; loc <= Mech.LOC_LLEG; loc++) {
			for(int ton = 20; ton <= 100; ton=ton+5) {
				for(int type = 0; type < EquipmentType.structureNames.length; type++) {
					parts.add(new MekLocation(loc, ton, type, false, false));
					parts.add(new MekLocation(loc, ton, type, true, false));
					if(loc > Mech.LOC_LT) {
						parts.add(new MekLocation(loc, ton, type, false, true));
						parts.add(new MekLocation(loc, ton, type, true, true));
					}
				}
			}
		}
	}
	
	private void stockVeeLocations() {
		//TODO: implement me
	}
	
}