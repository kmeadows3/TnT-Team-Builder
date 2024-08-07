BEGIN TRANSACTION;

DROP TABLE IF EXISTS inventory, unit_skillset, unit_skill, unit_injury, injury_reference, unit, team, unit_reference, faction, tnt_user, skill_reference, skillset_reference, item_ref_item_trait, item_trait_reference, item_reference;
DROP SEQUENCE IF EXISTS seq_user_id;

CREATE SEQUENCE seq_user_id;
CREATE TABLE tnt_user (
	user_id int NOT NULL DEFAULT nextval('seq_user_id'),
	username varchar(50) UNIQUE NOT NULL,
	password_hash varchar(200) NOT NULL,
	role varchar(20),
	CONSTRAINT PK_tnt_user PRIMARY KEY (user_id),
	CONSTRAINT UQ_username UNIQUE (username)
);

CREATE TABLE faction (
	faction_id serial PRIMARY KEY NOT NULL,
	faction_name varchar(50) NOT NULL
);

CREATE TABLE team (
	team_id serial PRIMARY KEY NOT NULL,
	user_id int NOT NULL,
	faction_id int NOT NULL,
	team_name varchar(100) NOT NULL,
	money int NOT NULL,
	bought_first_leader boolean DEFAULT false,
	CONSTRAINT FK_team_user FOREIGN KEY(user_id) REFERENCES tnt_user(user_id),
	CONSTRAINT fk_team_faction FOREIGN KEY(faction_id) REFERENCES faction(faction_id)
);

CREATE TABLE unit_reference (
	unit_ref_id serial PRIMARY KEY NOT NULL,
	faction_id int NOT NULL,
	class varchar(30) NOT NULL,
	rank varchar(20) NOT NULL,
	species varchar(20) NOT NULL,
	base_cost int NOT NULL,
	wounds int NOT NULL,
	defense int NOT NULL,
	mettle int NOT NULL,
	move int NOT NULL,
	ranged int NOT NULL,
	melee int NOT NULL,
	strength int NOT NULL,
	skillsets varchar(50) NOT NULL,
	starting_skills varchar(50),
	starting_free_skills int NOT NULL,
	special_rules text NOT NULL,
	CONSTRAINT FK_unit_faction FOREIGN KEY(faction_id) REFERENCES faction(faction_id)
);

CREATE TABLE skillset_reference(
	skillset_id serial PRIMARY KEY NOT NULL,
	skillset_name varchar(50) NOT NULL,
	category varchar(25) NOT NULL
);

CREATE TABLE skill_reference(
	skill_id serial PRIMARY KEY NOT NULL,
	skillset_id int NOT NULL,
	name varchar(30) NOT NULL,
	description text NOT NULL,
	phase varchar(25) DEFAULT 'Game',
	skill_cost int DEFAULT 0,
	CONSTRAINT fk_skill_skillset FOREIGN KEY(skillset_id) REFERENCES skillset_reference(skillset_id)
);

CREATE TABLE item_trait_reference(
	item_trait_id serial PRIMARY KEY NOT NULL,
	name varchar(50) NOT NULL,
	effect text NOT NULL
);

CREATE TABLE item_reference(
	item_ref_id serial PRIMARY KEY NOT NULL,
	name varchar(50) NOT NULL,
	cost int NOT NULL,
	special_rules text NOT NULL,
	rarity varchar(12) NOT NULL,
	is_relic boolean NOT NULL,
	item_category varchar(50) NOT NULL,
	hands_required int DEFAULT 0, 
	melee_defense_bonus int,
	ranged_defense_bonus int,
	is_shield boolean,
	cost_2_wounds int,
	cost_3_wounds int,
	melee_range int,
	ranged_range int,
	weapon_strength int,
	reliability int,
	grants int,
	CONSTRAINT CHK_item_ref_valid_category CHECK( item_category IN ('Armor', 'Equipment', 'Melee Weapon', 'Ranged Weapon', 'Support Weapon', 'Grenade')),
	CONSTRAINT FK_grants_item_ref_skill_id FOREIGN KEY(grants) REFERENCES skill_reference(skill_id)
	);

CREATE TABLE item_ref_item_trait(
	item_trait_id int NOT NULL,
	item_ref_id int NOT NULL,
	PRIMARY KEY (item_trait_id, item_ref_id),
	CONSTRAINT FK_item_ref_item_trait_join_item_trait_id FOREIGN KEY(item_trait_id) REFERENCES item_trait_reference(item_trait_id),
	CONSTRAINT FK_item_ref_item_trait_join_item_ref_id FOREIGN KEY(item_ref_id) REFERENCES item_reference(item_ref_id)
);

CREATE TABLE unit(
	unit_id serial PRIMARY KEY NOT NULL,
	team_id int NOT NULL,
	name varchar(100) NOT NULL,
	class varchar(30) NOT NULL,
	rank varchar(20) NOT NULL,
	species varchar(20) NOT NULL,
	base_cost int NOT NULL,
	wounds int NOT NULL,
	defense int NOT NULL,
	mettle int NOT NULL,
	move int NOT NULL,
	ranged int NOT NULL,
	melee int NOT NULL,
	strength int NOT NULL,
	empty_skills int NOT NULL,
	special_rules text NOT NULL,
	spent_exp int NOT NULL,
	unspent_exp int DEFAULT 0,
	total_advances int DEFAULT 0,
	ten_point_advances int DEFAULT 0,
	CONSTRAINT FK_unit_team FOREIGN KEY(team_id) REFERENCES team(team_id)
);

CREATE TABLE inventory(
	item_id serial PRIMARY KEY NOT NULL,
	item_ref_id int NOT NULL,
	unit_id int,
	team_id int,
	is_equipped boolean DEFAULT false,
	is_masterwork boolean DEFAULT false,
	is_large_caliber boolean DEFAULT false,
	has_prefall_ammo boolean DEFAULT false,
	CONSTRAINT CHK_inventory_unit_id_or_team_id_null CHECK ( unit_id IS NULL OR team_id IS NULL),
	CONSTRAINT CHK_inventory_not_both_null CHECK (NOT(unit_id IS NULL AND team_id IS NULL)),
	CONSTRAINT FK_inventory_item_ref_id FOREIGN KEY(item_ref_id) REFERENCES item_reference(item_ref_id),
	CONSTRAINT FK_inventory_unit_id FOREIGN KEY(unit_id) REFERENCES unit(unit_id),
	CONSTRAINT FK_inventory_team_id FOREIGN KEY(team_id) REFERENCES team(team_id)
);

CREATE TABLE unit_skillset(
	unit_id int NOT NULL,
	skillset_id int NOT NULL,
	PRIMARY KEY (unit_id, skillset_id),
	CONSTRAINT FK_unit_skillset_join_skillset_id FOREIGN KEY(skillset_id) REFERENCES skillset_reference(skillset_id),
	CONSTRAINT FK_unit_skillset_join_unit_id FOREIGN KEY(unit_id) REFERENCES unit(unit_id)
);

CREATE TABLE unit_skill(
	unit_id int NOT NULL,
	skill_id int NOT NULL,
	PRIMARY KEY (unit_id, skill_id),
	CONSTRAINT FK_unit_skill_join_skill_id FOREIGN KEY(skill_id) REFERENCES skill_reference(skill_id),
	CONSTRAINT FK_unit_skill_join_unit_id FOREIGN KEY(unit_id) REFERENCES unit(unit_id)
);

CREATE TABLE injury_reference(
	injury_id serial PRIMARY KEY NOT NULL,
	name varchar(50) NOT NULL,
	description text NOT NULL,
	is_stat_damage boolean DEFAULT false,
	stat_damaged varchar(10),
	is_removeable boolean DEFAULT false,
	is_stackable boolean DEFAULT true,
	grants int,
	CONSTRAINT CHK_injury_stat_damaged_identifies_stat_damaged CHECK ( (is_stat_damage IS false AND stat_damaged IS NULL) OR (is_stat_damage IS true AND stat_damaged IS NOT NULL)),
	CONSTRAINT CHK_stat_damaged_is_valid_stat CHECK (stat_damaged IN ('Mettle', 'Move', 'Ranged', 'Defense', 'Melee')),
	CONSTRAINT FK_grants_injury_skill_id FOREIGN KEY (grants) REFERENCES skill_reference(skill_id)
);

CREATE TABLE unit_injury(
	unit_id int NOT NULL,
	injury_id int NOT NULL,
	count int DEFAULT 1,
	PRIMARY KEY (unit_id, injury_id),
	CONSTRAINT FK_unit_injury_join_unit_id FOREIGN KEY(unit_id) REFERENCES unit(unit_id),
	CONSTRAINT FK_unit_injury_join_injury_id FOREIGN KEY(injury_id) REFERENCES injury_reference(injury_id)
);

COMMIT TRANSACTION; 