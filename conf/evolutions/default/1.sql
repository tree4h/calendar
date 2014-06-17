# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table カレンダ年 (
  id                        bigint not null,
  year                      integer not null,
  owner_id                  bigint not null,
  _稼働日数                     integer,
  _休日数                      integer,
  holi_day_id               bigint,
  constraint uq_カレンダ年_1 unique (year,owner_id),
  constraint pk_カレンダ年 primary key (id))
;

create table カレンダ日 (
  id                        bigint not null,
  year_id                   bigint not null,
  month                     integer not null,
  day                       integer not null,
  dow                       integer,
  type                      integer,
  name                      varchar(255),
  _week                     integer,
  constraint ck_カレンダ日_month check (month in (0,1,2,3,4,5,6,7,8,9,10,11)),
  constraint ck_カレンダ日_day check (day in (0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30)),
  constraint ck_カレンダ日_dow check (dow in (0,1,2,3,4,5,6)),
  constraint ck_カレンダ日_type check (type in (0,1,2,3,4,5)),
  constraint uq_カレンダ日_1 unique (year_id,month,day),
  constraint pk_カレンダ日 primary key (id))
;

create table カレンダ日インデックス (
  id                        bigint not null,
  year_id                   bigint not null,
  month                     integer not null,
  day                       integer not null,
  index                     integer not null,
  constraint uq_カレンダ日インデックス_1 unique (year_id,month,day),
  constraint pk_カレンダ日インデックス primary key (id))
;

create table 休日 (
  id                        bigint not null,
  rule_json                 varchar(5120) not null,
  constraint pk_休日 primary key (id))
;

create table 休日規則 (
  id                        bigint not null,
  type                      integer,
  name                      varchar(255),
  holi_day_id               bigint,
  constraint ck_休日規則_type check (type in (0,1,2,3,4,5)),
  constraint pk_休日規則 primary key (id))
;

create table 所有者 (
  id                        bigint not null,
  name                      varchar(255) not null,
  constraint uq_所有者_1 unique (name),
  constraint pk_所有者 primary key (id))
;

create sequence カレンダ年_seq;

create sequence カレンダ日_seq;

create sequence カレンダ日インデックス_seq;

create sequence 休日_seq;

create sequence 休日規則_seq;

create sequence 所有者_seq;

alter table カレンダ年 add constraint fk_カレンダ年_owner_1 foreign key (owner_id) references 所有者 (id) on delete restrict on update restrict;
create index ix_カレンダ年_owner_1 on カレンダ年 (owner_id);
alter table カレンダ年 add constraint fk_カレンダ年_holiDay_2 foreign key (holi_day_id) references 休日 (id) on delete restrict on update restrict;
create index ix_カレンダ年_holiDay_2 on カレンダ年 (holi_day_id);
alter table カレンダ日 add constraint fk_カレンダ日_year_3 foreign key (year_id) references カレンダ年 (id) on delete restrict on update restrict;
create index ix_カレンダ日_year_3 on カレンダ日 (year_id);
alter table カレンダ日インデックス add constraint fk_カレンダ日インデックス_year_4 foreign key (year_id) references カレンダ年 (id) on delete restrict on update restrict;
create index ix_カレンダ日インデックス_year_4 on カレンダ日インデックス (year_id);
alter table 休日規則 add constraint fk_休日規則_holiDay_5 foreign key (holi_day_id) references 休日 (id) on delete restrict on update restrict;
create index ix_休日規則_holiDay_5 on 休日規則 (holi_day_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists カレンダ年;

drop table if exists カレンダ日;

drop table if exists カレンダ日インデックス;

drop table if exists 休日;

drop table if exists 休日規則;

drop table if exists 所有者;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists カレンダ年_seq;

drop sequence if exists カレンダ日_seq;

drop sequence if exists カレンダ日インデックス_seq;

drop sequence if exists 休日_seq;

drop sequence if exists 休日規則_seq;

drop sequence if exists 所有者_seq;

