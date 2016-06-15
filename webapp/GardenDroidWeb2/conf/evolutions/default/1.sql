# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table log_data (
  id                        bigint not null,
  dateTime                  timestamp,
  message                   varchar(255),
  is_error                  boolean,
  constraint pk_log_data primary key (id))
;

create table observation_data (
  id                        bigint not null,
  plant_id                  bigint,
  date_created              timestamp,
  data_type_id              bigint,
  data_value                float,
  constraint pk_observation_data primary key (id))
;

create table options (
  email                     varchar(255),
  enable_warning_notification boolean,
  enable_low_temp_warning   boolean,
  low_temp_threshold        float,
  enable_high_temp_warning  boolean,
  high_temp_threshold       float,
  enable_planted_warnings   boolean,
  remote_alive_check_mins   integer,
  snooze_active_warnings_hours integer)
;

create table plants (
  id                        bigint not null,
  date_planted              timestamp,
  name                      varchar(255),
  notes                     varchar(255),
  is_active                 boolean,
  plant_count               integer,
  harvest_start_date        timestamp,
  harvest_end_date          timestamp,
  is_droid_farmed           boolean,
  plant_data_id             bigint,
  constraint pk_plants primary key (id))
;

create table plant_data (
  id                        bigint not null,
  create_date               timestamp,
  name                      varchar(255),
  scientific_name           varchar(255),
  notes                     varchar(255),
  days_to_harvest           integer,
  days_to_harvest_end       integer,
  sunlight                  varchar(255),
  low_temp                  float,
  high_temp                 float,
  water_frequency_days      integer,
  constraint pk_plant_data primary key (id))
;

create table user_report_scripts (
  id                        bigint not null,
  date_created              timestamp,
  name                      varchar(255),
  description               varchar(255),
  script                    varchar(255),
  start_date                timestamp,
  end_date                  timestamp,
  active_only_plantings     boolean,
  report_type               integer,
  report_field              varchar(255),
  constraint ck_user_report_scripts_report_type check (report_type in (0,1,2)),
  constraint pk_user_report_scripts primary key (id))
;

create table sensor_data (
  id                        bigint not null,
  dateTime                  timestamp,
  data_value                float,
  sensorType                integer,
  constraint ck_sensor_data_sensorType check (sensorType in (0,1,2,3,4,5,6,7,8,9,10)),
  constraint pk_sensor_data primary key (id))
;

create table sensor_record_Frequency (
  id                        bigint not null,
  sensor_type               integer,
  frequency_seconds         integer,
  last_post_time            bigint,
  constraint ck_sensor_record_Frequency_sensor_type check (sensor_type in (0,1,2,3,4,5,6,7,8,9,10)),
  constraint pk_sensor_record_Frequency primary key (id))
;

create table sensor_data (
  id                        bigint not null,
  dateTime                  timestamp,
  data_value                float,
  sensorType                integer,
  tempF                     float,
  tempC                     float,
  constraint ck_sensor_data_sensorType check (sensorType in (0,1,2,3,4,5,6,7,8,9,10)),
  constraint pk_sensor_data primary key (id))
;

create table user (
  user_name                 varchar(255),
  password                  varchar(255))
;

create table user_data_type (
  id                        bigint not null,
  description               varchar(255),
  name                      varchar(255),
  is_active                 boolean,
  constraint pk_user_data_type primary key (id))
;

create table warning (
  date_time                 timestamp,
  message                   varchar(255),
  is_active                 boolean,
  alert_type                integer,
  constraint ck_warning_alert_type check (alert_type in (0,1,2,3,4,5,6)))
;

create sequence log_data_seq;

create sequence observation_data_seq;

create sequence plants_seq;

create sequence plant_data_seq;

create sequence user_report_scripts_seq;

create sequence sensor_data_seq;

create sequence sensor_record_Frequency_seq;

create sequence sensor_data_seq;

create sequence user_data_type_seq;

alter table observation_data add constraint fk_observation_data_plant_1 foreign key (plant_id) references plants (id);
create index ix_observation_data_plant_1 on observation_data (plant_id);
alter table observation_data add constraint fk_observation_data_dataType_2 foreign key (data_type_id) references user_data_type (id);
create index ix_observation_data_dataType_2 on observation_data (data_type_id);
alter table plants add constraint fk_plants_plantData_3 foreign key (plant_data_id) references plant_data (id);
create index ix_plants_plantData_3 on plants (plant_data_id);



# --- !Downs

drop table if exists log_data cascade;

drop table if exists observation_data cascade;

drop table if exists options cascade;

drop table if exists plants cascade;

drop table if exists plant_data cascade;

drop table if exists user_report_scripts cascade;

drop table if exists sensor_data cascade;

drop table if exists sensor_record_Frequency cascade;

drop table if exists sensor_data cascade;

drop table if exists user cascade;

drop table if exists user_data_type cascade;

drop table if exists warning cascade;

drop sequence if exists log_data_seq;

drop sequence if exists observation_data_seq;

drop sequence if exists plants_seq;

drop sequence if exists plant_data_seq;

drop sequence if exists user_report_scripts_seq;

drop sequence if exists sensor_data_seq;

drop sequence if exists sensor_record_Frequency_seq;

drop sequence if exists sensor_data_seq;

drop sequence if exists user_data_type_seq;

