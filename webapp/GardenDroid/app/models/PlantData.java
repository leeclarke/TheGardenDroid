package models;

import java.util.*;
import javax.persistence.*;

import play.db.jpa.*;

@Entity
public class PlantData extends Model {

    public Date created;
    public String name;
    public String scientificName;
    public String notes;
    public int daysTillHarvest;
    public int daysTillHarvestEnd;
    public String sunlight;
    public double lowTemp;
    public double highTemp;
    public int waterFreqDays;

	@OneToMany(mappedBy="plantData", cascade=CascadeType.ALL)
	public List<Plant> planted;

    public PlantData(String name) {
		this.created = new Date();
        this.name = name;
    }

    public PlantData(String name, String scientificName, int daysTillHarvest, int daysTillHarvestEnd, String sunlight, double lowTemp, double highTemp, int waterFreqDays) {

		this.created = new Date();
		this.name = name;
		this.scientificName = scientificName;
		this.daysTillHarvest = daysTillHarvest;
		this.daysTillHarvestEnd = daysTillHarvestEnd;
		this.sunlight = sunlight;
		this.lowTemp = lowTemp;
		this.highTemp = highTemp;
		this.waterFreqDays = waterFreqDays;
    }

	public PlantData addPlant(Date datePlanted, String name, String notes, boolean isActive, boolean isDroidFarmed) {
	    Plant newPlant = new Plant(datePlanted, name, notes, isActive, isDroidFarmed).save();
	    this.planted.add(newPlant);
	    this.save();
	    return this;
	}

	public PlantData addPlant(Plant newPlant) {
		    newPlant.save();
		    this.planted.add(newPlant);
		    this.save();
		    return this;
	}
}