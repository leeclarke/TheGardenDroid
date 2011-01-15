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
package models;

import java.util.*;
import javax.persistence.*;

import play.db.jpa.*;

@Entity
public class TempSensorData extends SensorData {

    public double tempF;
    public double tempC;

    public TempSensorData(Date dateTime, double tempF, double tempC) {
        super(dateTime,tempF,SensorType.TEMPERATURE);
        this.tempF = tempF;
        this.tempC = tempC;
    }

	/**
	 * Helper method because it gets called more then once.
	 * @return
	 */
	public static TempSensorData getCurrentReading() {
		TempSensorData temp = TempSensorData.find("sensorType = ? order by dateTime desc", SensorType.TEMPERATURE).first();
		return temp;
	}

}