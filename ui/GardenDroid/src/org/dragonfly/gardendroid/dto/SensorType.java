package org.dragonfly.gardendroid.dto;


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
		case 'E':
			return ERROR;
		default:
			return INVALID;
		}
	}

}
