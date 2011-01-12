package org.dragonfly.gardendroid.dto;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * 
 * @author lee Clarke
 */
public enum SensorType {
	RTC('R'), TEMPERATURE('T'), MOISTURE('M'), HUMIDITY('H'), AMBIENT_LIGHT('A'), LOG('L'),
		ERROR('E'), INVALID('X'), GROW_LITE('G'), WATER_IRRIGATION('I'), TEMP_WARNING('W');

	private char sCode = 'X';

	SensorType(char code) {
		sCode = code;
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
}
