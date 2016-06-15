/**
 * The GardenDroid, a self monitoring and reporting mini-greenhouse.
 *
 * Copyright (c) 2010-2011 Lee Clarke
 *
 * LICENSE:
 *
 * This file is part of TheGardenDroid (https://github.com/leeclarke/TheGardenDroid).
 *
 * TheGardenDroid is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any
 * later version.
 *
 * TheGardenDroid is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with TheGardenDroid.  If not, see
 * <http://www.gnu.org/licenses/>.
 *
 */
package model;

import com.avaje.ebean.annotation.EnumValue;



/**
 *
 * @author lee Clarke
 */
public enum SensorType {
	@EnumValue("R")	RTC('R',true), 
	@EnumValue("T")	TEMPERATURE('T'), 
	@EnumValue("M")	MOISTURE('M'), 
	@EnumValue("H")	HUMIDITY('H'), 
	@EnumValue("A")	AMBIENT_LIGHT('A'), 
	@EnumValue("L") LOG('L',true),
	@EnumValue("E") ERROR('E',true), 
	@EnumValue("X") INVALID('X',true), 
	@EnumValue("G") GROW_LITE('G'), 
	@EnumValue("I") WATER_IRRIGATION('I'), 
	@EnumValue("W") TEMP_WARNING('W');

	private char sCode = 'X';
	private boolean virtual = false;

	SensorType(char code) {
		sCode = code;
	}

	SensorType(char code, boolean virtual) {
		sCode = code;
		this.virtual = virtual;
	}
	
	public char getCode() {
		return sCode;
	}

	public static SensorType getByCode(char code) {
		switch (code) {
		case 'R':
			return RTC;
		case 'T':
			return TEMPERATURE;
		case 'M':
			return MOISTURE;
		case 'A':
			return AMBIENT_LIGHT;
		case 'L':
			return LOG;
		case 'G':
			return GROW_LITE;
		case 'H':
			return HUMIDITY;
		case 'I':
			return WATER_IRRIGATION;
		case 'W':
			return TEMP_WARNING;
		case 'E':
			return ERROR;
		default:
			return INVALID;
		}
	}

	/**
	 * List valid codes for Regex pattern matching etc..
	 * @return
	 */
	public static String getValidLetters(){
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < SensorType.values().length; i++) {
			sb.append(SensorType.values()[i].getCode());
		}

		return sb.toString();
	}

	public boolean isVirtual() {
		return virtual;
	}

	
}
