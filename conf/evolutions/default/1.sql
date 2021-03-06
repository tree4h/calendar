# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table calendar_day (
  id                        bigint not null,
  owner_id                  bigint not null,
  year                      integer not null,
  month                     integer not null,
  day                       integer not null,
  dow                       integer not null,
  next_cal_day_id           bigint,
  type                      integer,
  name                      varchar(255),
  _passed_day               integer not null,
  _passed_working_day       integer not null,
  is_holiday                boolean not null,
  is_working_day            boolean not null,
  _week                     integer,
  constraint ck_calendar_day_year check (year in (0,1,2,3,4,5,6,7,8)),
  constraint ck_calendar_day_month check (month in (0,1,2,3,4,5,6,7,8,9,10,11)),
  constraint ck_calendar_day_day check (day in (0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30)),
  constraint ck_calendar_day_dow check (dow in (0,1,2,3,4,5,6)),
  constraint ck_calendar_day_type check (type in (0,1,2,3,4,5)),
  constraint uq_calendar_day_1 unique (owner_id,year,month,day),
  constraint pk_calendar_day primary key (id))
;

create table calendar_year (
  id                        bigint not null,
  year                      integer not null,
  owner_id                  bigint not null,
  working_days              integer not null,
  constraint ck_calendar_year_year check (year in (0,1,2,3,4,5,6,7,8)),
  constraint uq_calendar_year_1 unique (owner_id,year),
  constraint pk_calendar_year primary key (id))
;

create table holiday (
  id                        bigint not null,
  rule_json                 varchar(5120) not null,
  constraint pk_holiday primary key (id))
;

create table holiday_rule (
  id                        bigint not null,
  type                      integer,
  name                      varchar(255),
  holi_day_id               bigint,
  constraint ck_holiday_rule_type check (type in (0,1,2,3,4,5)),
  constraint pk_holiday_rule primary key (id))
;

create table owner (
  id                        bigint not null,
  name                      varchar(255) not null,
  constraint uq_owner_1 unique (name),
  constraint pk_owner primary key (id))
;

create sequence calendar_day_seq;

create sequence calendar_year_seq;

create sequence holiday_seq;

create sequence holiday_rule_seq;

create sequence owner_seq;

alter table calendar_day add constraint fk_calendar_day_owner_1 foreign key (owner_id) references owner (id) on delete restrict on update restrict;
create index ix_calendar_day_owner_1 on calendar_day (owner_id);
alter table calendar_day add constraint fk_calendar_day_nextCalDay_2 foreign key (next_cal_day_id) references calendar_day (id) on delete restrict on update restrict;
create index ix_calendar_day_nextCalDay_2 on calendar_day (next_cal_day_id);
alter table calendar_year add constraint fk_calendar_year_owner_3 foreign key (owner_id) references owner (id) on delete restrict on update restrict;
create index ix_calendar_year_owner_3 on calendar_year (owner_id);
alter table holiday_rule add constraint fk_holiday_rule_holiDay_4 foreign key (holi_day_id) references holiday (id) on delete restrict on update restrict;
create index ix_holiday_rule_holiDay_4 on holiday_rule (holi_day_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists calendar_day;

drop table if exists calendar_year;

drop table if exists holiday;

drop table if exists holiday_rule;

drop table if exists owner;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists calendar_day_seq;

drop sequence if exists calendar_year_seq;

drop sequence if exists holiday_seq;

drop sequence if exists holiday_rule_seq;

drop sequence if exists owner_seq;

