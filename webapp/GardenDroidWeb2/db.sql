CREATE TABLE user_data_type
(
  ID serial,
  name text NOT NULL,
  active boolean,
  description text
);

ALTER TABLE user_data_type ADD PRIMARY KEY(ID);


CREATE TABLE sensor_data
(
    ID serial,
    dateTime timestamp,
    data_value numeric,
    sensorType char,
    tempF numeric,
    tempC numeric
);
ALTER TABLE sensor_data ADD PRIMARY KEY(ID);

CREATE TABLE log_data 
(
    ID serial,
    dateTime timestamp,
    message text,
    is_error boolean
);
ALTER TABLE log_data ADD PRIMARY KEY(ID);

CREATE TABLE plant_data
(
    ID serial,
    create_date timestamp,
    name text,
    scientific_name text,
    notes text,
    days_to_harvest integer,
    days_to_harvest_end integer,
    sunlight text,
    low_temp numeric(3,2),
    high_temp numeric(3,2),
    water_frequency_days integer
);
ALTER TABLE plant_data ADD PRIMARY KEY(ID);

CREATE TABLE plant
(
    ID serial,
    date_planted timestamp,
    name text,
    notes text,
    is_active boolean,
    plant_count integer,
    harvest_start_date timestamp,
    harvest_end_date timestamp,
    is_droid_farmed boolean,
    plant_data_id INTEGER REFERENCES PLANT_DATA (id)
);
ALTER TABLE plant ADD PRIMARY KEY(ID);

CREATE TABLE observation_data
(
    ID serial,
    date_created timestamp,
    data_value numeric,
    user_data_type_id INTEGER REFERENCES user_data_type (id),
    plant_id  INTEGER REFERENCES PLANT (id)
);
ALTER TABLE observation_data ADD PRIMARY KEY(ID);


CREATE TABLE sensor_record_Frequency
(
    ID serial,
    sensor_type char,
    frequency_seconds numeric,
    last_post_time numeric
);
ALTER TABLE sensor_record_Frequency ADD PRIMARY KEY(ID);

CREATE TABLE warning 
(
	ID serial,
	date_time timestamp,
	message text,
	is_active boolean,
	alert_type text not null
);
ALTER TABLE warning ADD PRIMARY KEY(ID);

/*
This is not created yet.. honestly it shoud be revisited
create table options (
    email                     varchar(255),
    enable_warning_notification boolean,
    enable_low_temp_warning   boolean,
    low_temp_threshold        float,
    enable_high_temp_warning  boolean,
    high_temp_threshold       float,
    enable_planted_warnings   boolean,
    remote_alive_check_mins   integer
    snooze_active_warnings_hours integer
)

alter table observation_data add constraint fk_observation_data_plant_1 foreign key (plant_id) references plants (id);

alter table observation_data add constraint fk_observation_data_dataType_2 foreign key (data_type_id) references user_data_type (id);

alter table plants add constraint fk_plants_plantData_3 foreign key (plant_data_id) references plant_data (id);
*/