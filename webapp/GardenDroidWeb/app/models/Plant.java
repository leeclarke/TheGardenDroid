package models;

import java.util.*;
import javax.persistence.*;

import play.db.jpa.*;

@Entity
public class Plant extends Model {

    public Date datePlanted;
    public String name;
    public String notes;
    public boolean isActive;
    public int plantCount;
    public Date harvestStart;
    public Date harvestEnd;
    public double harvestYield;
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

}