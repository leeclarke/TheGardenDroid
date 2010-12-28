package models;

import java.util.*;

import javax.persistence.*;

import play.db.jpa.*;
import play.db.jpa.GenericModel.JPAQuery;

@Entity
public class Plant extends Model {

    public Date datePlanted;
    public String name;
    public String notes;
    public boolean isActive;
    public Integer plantCount;
    public Date harvestStart;
    public Date harvestEnd;
    public Double harvestYield;
	public boolean isDroidFarmed;
	@ManyToOne
	public PlantData plantData;

	public Plant(){
		
	}
	
    public Plant(Date datePlanted, String name, String notes, boolean isActive, boolean isDroidFarmed) {
        this.datePlanted = datePlanted;
        this.name = name;
        this.notes = notes;
        this.isActive = isActive;
        this.isDroidFarmed = isDroidFarmed;
    }

    public Plant(Date datePlanted, String name, String notes) {
	        this.datePlanted = datePlanted;
	        this.name = name;
	        this.notes = notes;
	        this.isActive = false;
    }

	public static List<Plant> getActivePlantings() {
		List<Plant> planted = Plant.find("isActive = ? order by datePlanted desc", true).fetch();
		return planted;
	}

}