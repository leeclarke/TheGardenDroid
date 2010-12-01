package models;

import java.util.*;

import javax.persistence.*;

import org.apache.log4j.Logger;

import play.db.jpa.*;

@Entity
public class PlantData extends Model {

	static Logger logger = Logger.getLogger(PlantData.class);
	
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
	public List<Plant> plants;

    public PlantData(String name) {
		this.created = new Date();
        this.name = name;
        this.plants = new ArrayList<Plant>();
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
		this.plants = new ArrayList<Plant>();
    }

	public PlantData addPlant(Date datePlanted, String name, String notes, boolean isActive, boolean isDroidFarmed) {
	    Plant newPlant = new Plant(datePlanted, name, notes, isActive, isDroidFarmed).save();
	    this.plants.add(newPlant);
	    this.save();
	    return this;
	}

	public PlantData addPlant(Plant newPlant) {
		if(newPlant != null)
		{
			newPlant.save();
			this.plants.add(newPlant);
		    this.save();
		}
		else {
			logger.warn("newPLant was NULL");
			System.out.println("newPLant was NULL");
		}
			
		return this;
		    
	}
}