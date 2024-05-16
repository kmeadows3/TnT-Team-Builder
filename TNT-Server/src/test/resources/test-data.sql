BEGIN TRANSACTION;

DROP TABLE IF EXISTS item, unit_skillset, unit_skill, unit, team, unit_reference, faction, tnt_user, skill_reference, skillset_reference, item_ref_item_trait, item_trait_reference, item_reference;
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
	class varchar(20) NOT NULL,
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
	name varchar(25) NOT NULL,
	description text NOT NULL,
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
	CONSTRAINT CHK_item_ref_valid_category CHECK( item_category IN ('Armor', 'Equipment', 'Melee Weapon', 'Ranged Weapon', 'Support Weapon', 'Grenade'))
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
	class varchar(20) NOT NULL,
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
	is_banged_up boolean DEFAULT false,
	is_long_recovery boolean DEFAULT false,
	CONSTRAINT FK_unit_team FOREIGN KEY(team_id) REFERENCES team(team_id)
);

CREATE TABLE item(
	item_id serial PRIMARY KEY NOT NULL,
	item_ref_id int NOT NULL,
	unit_id int,
	team_id int,
	CONSTRAINT CHK_item_unit_id_or_team_id_null CHECK ( unit_id IS NULL OR team_id IS NULL),
	CONSTRAINT CHK_item_not_both_null CHECK (NOT(unit_id IS NULL AND team_id IS NULL)),
	CONSTRAINT FK_item_item_ref_id FOREIGN KEY(item_ref_id) REFERENCES item_reference(item_ref_id),
	CONSTRAINT FK_item_unit_id FOREIGN KEY(unit_id) REFERENCES unit(unit_id),
	CONSTRAINT FK_item_team_id FOREIGN KEY(team_id) REFERENCES team(team_id)
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














INSERT INTO faction (faction_name) VALUES
	('Caravanners'),
	('Mutants'),
	('Raiders'),
	('Preservers'),
	('Tribals'),
	('Peacekeepers'),
	('Freelancers');

INSERT INTO unit_reference (faction_id, class, rank, species, base_cost, wounds, defense, mettle, move, ranged, melee,
							strength, skillsets, starting_skills, starting_free_skills, special_rules) VALUES
	(1, 'Trade Master', 'Leader', 'Human', 80, 2, 6, 7, 5, 6, 6, 6, '[1|2|3|4|5|6|7|8]', '[1]', 2, 'N/A'),
	(1, 'Lieutenant', 'Elite', 'Human', 45, 1, 6, 6, 5, 5, 5, 5, '[2|3|7|8]', '[2]', 1, 'N/A'),
	(1, 'Tracker', 'Elite', 'Human', 50, 1, 6, 6, 5, 5, 5, 5, '[2|3|4]', '[3|4]', 0, 'N/A'),
	(1, 'Defender', 'Rank and File', 'Human', 23, 1, 6, 5, 5, 4, 4, 5, '[1|2|3]', '[5]', 0, 'N/A'),
	(3, 'Bandit King', 'Leader', 'Human', 80, 2, 6, 7, 5, 6, 6, 6, '[1|2|3|4|5|6|7|8]', '', 3, 'N/A'),  -- ID 5
	(3, 'Brute', 'Elite', 'Human', 50, 1, 6, 6, 6, 4, 7, 6, '[1|6|7]', '[6|7|8]', 0, 'Unless Leader is warlord, only 1 per team'),
	(3, 'Raider Champion', 'Elite', 'Human', 50, 1, 6, 6, 5, 5, 5, 5, '[1|6|7|8]', '', 2, 'N/A'),
	(3, 'Raider', 'Rank and File', 'Human', 20, 1, 6, 5, 5, 4, 4, 5, '[1|2|3]', '', 0, 'N/A'),
    (2, 'Raider2', 'Rank and File', 'Human', 20, 1, 6, 5, 5, 4, 4, 5, '[1|2|3]', '', 0, 'N/A'),
    (7, 'Freelancer', 'Freelancer', 'Human', 20, 1, 6, 5, 5, 4, 4, 5, '[1|2|3]', '', 0, 'N/A'), -- ID 10
    (1, 'Specialist', 'Specialist', 'Human', 23, 1, 6, 5, 5, 4, 4, 5, '[1|2|3]', '[5]', 0, 'N/A');



INSERT INTO skillset_reference (skillset_name, category) VALUES
	('Melee', 'Skill'), -- ID 1
	('Marksmanship', 'Skill'), -- ID 2
	('Survival', 'Skill'), -- ID 3
	('Quickness', 'Skill'), -- ID 4
	('Smarts', 'Skill'), -- ID 5
	('Brawn', 'Skill'), -- ID 6
	('Tenacity', 'Skill'), -- ID 7
	('Leadership', 'Skill'), -- ID 8
	('Hidden Defensive Mutations', 'Mutation'), -- ID 9
	('Hidden Offensive Mutations', 'Mutation'), -- ID 10
	('Physical Mutations', 'Mutation'), -- ID 11
	('Psychic Mutations', 'Mutation'), -- ID 12
	('Hidden Detriments', 'Detriment'), -- ID 13
	('Physical Detriments', 'Detriment'), -- ID 14
	('General Abilities', 'General'), -- ID 15
	('Injuries', 'Injury'); -- ID 16

INSERT INTO skill_reference (skillset_id, name, description) VALUES
	(5, 'Scavenger', 'When taking a weapon with limited ammo roll 2d3 when determining ammo quantity and take the higher of the two. Upkeep does not need to be paid for this unit. May not be taken by Freelancers.'),
	(8, 'Motivator', 'All friendly models withing 6" of this model gain +1 to activation tests. Motivator may not stack with itself.'),
	(4, 'Reconnoiter', 'At the start of the game after all models have deployed but before init is determined make a free move action.'),
	(3, 'Trekker', 'When moving through Difficult Terrain attempt an Agility test (MET/TN 10) for free. On pass move through terrain without movement penalty.'),
	(7, 'Brave', '+2 bonus when making Will tests.'),
	(6, 'Brute', 'Gain +1 to Strength Stat when making Melee attacks. Ignore heavy weapons rule.'),
	(6, 'Bully', 'All enemies defeated by this model in close combat are knocked prone in addition to any other combat result.'),
	(15, 'Dumb', 'Takes a -2 penalty to intelligence tests'),
	(16, 'Gashed Leg', '-1 penalty to Move'),
	(16, 'Banged Head', '-1 penalty to Mettle');

INSERT INTO item_trait_reference (name, effect) VALUES
	('Trait 1', 'Trait 1 Desc'),
	('Trait 2', 'Trait 2 Desc'),
	('Trait 3', 'Trait 3 Desc');

INSERT INTO item_reference (name, cost, special_rules, rarity, is_relic, melee_defense_bonus, ranged_defense_bonus, is_shield,
							cost_2_wounds, cost_3_wounds, melee_range, ranged_range, weapon_strength, reliability,
							hands_required, item_category) VALUES
	('Armor 1', 1, 'N/A', 'N/A', FALSE, 1, 1, TRUE, 2, 3, null, null, null, null, 1, 'Armor'),
	('Relic Armor', 2, 'Relic Armor Desc', 'Ultra Rare', TRUE, 2, 2, FALSE, 2, 2, null, null, null, null, 0, 'Armor'),
	('Equipment 1', 3, 'N/A', 'N/A', FALSE, null, null, null, null, null, null, null, null, null, 0, 'Equipment'),
	('Relic Equipment', 4, 'Relic Equipment Desc', 'Rare', TRUE, null, null, null, null, null, null, null, null, null, 0, 'Equipment'),
	('Weapon 1', 5, 'N/A', 'N/A', FALSE, null, null, null, null, null, null, 5, 5, 5, 1, 'Ranged Weapon'),
	('Relic Weapon', 6, 'Relic Weapon Desc', 'Rare', TRUE, null, null, null, null, null, 6, null, 6, 6, 2, 'Melee Weapon'),
    ('Support Weapon', 7, 'N/A', 'N/A', FALSE, null, null, null, null, null, null, null, 7, 7, 2, 'Support Weapon'),
	('Relic Support Weapon', 8, 'Relic Support Desc', 'Ultra Rare', TRUE, null, null, null, null, null, null, 8, 8, 8, 2, 'Support Weapon'),
    ('Grenade', 9, 'N/A', 'N/A', FALSE, null, null, null, null, null, null, null, null, null, 1, 'Grenade'),
	('Relic Grenade', 10, 'Relic Grenade Desc', 'Rare', TRUE, null, null, null, null, null, null, 10, 10, null, 1, 'Grenade');

INSERT INTO item_ref_item_trait (item_ref_id, item_trait_id) VALUES
	(1, 1),
	(2, 1),
	(2, 2);


INSERT INTO tnt_user (username,password_hash,role) VALUES ('user1','user1','ROLE_USER');
INSERT INTO tnt_user (username,password_hash,role) VALUES ('user2','user2','ROLE_USER');
INSERT INTO tnt_user (username,password_hash,role) VALUES ('user3','user3','ROLE_USER');
INSERT INTO tnt_user (username,password_hash,role) VALUES ('user4','user4','ROLE_USER');

INSERT INTO team(user_id, faction_id, team_name, money) VALUES (1, 1, 'Team 1', 500); -- ID 1
INSERT INTO team(user_id, faction_id, team_name, money) VALUES (1, 3, 'Team 2', 1500); -- ID 2
INSERT INTO team(user_id, faction_id, team_name, money) VALUES (2, 2, 'Team 3', 1000); -- ID 3
INSERT INTO team(user_id, faction_id, team_name, money) VALUES (4, 1, 'High BS Team', 1000); -- ID 4
INSERT INTO team(user_id, faction_id, team_name, money) VALUES (4, 1, 'Poor Team', 1); -- ID 5
INSERT INTO team(user_id, faction_id, team_name, money) VALUES (4, 1, 'Team Without Leader', 1000); -- ID 6
INSERT INTO team(user_id, faction_id, team_name, money) VALUES (4, 1, 'Team With 3 Elites', 1000); -- ID 7



INSERT INTO unit (team_id, name, class, rank, species, base_cost, wounds, defense, mettle, move, ranged, melee,
    strength, empty_skills, special_rules, spent_exp)
VALUES (1, 'UnitName1', 'Trade Master', 'Leader', 'Human', 50, 10, 5, 7, 6, 8, 6, 5, 0, 'Special rules description', 100),
    (3, 'UnitName2', 'Soldier', 'Elite', 'Mutant', 51, 11, 6, 8, 7, 9, 7, 6, 1, 'Special rules description', 50),
    (1, 'UnitName3', 'Class Name', 'Specialist', 'Human', 40, 10, 5, 7, 6, 8, 6, 5, 0, 'Special rules description', 100),
    (4, 'UnitName4', 'High BS Unit', 'Leader', 'Human', 200, 10, 5, 7, 6, 8, 6, 5, 0, 'Special rules description', 100),
    (7, 'UnitName5', 'Class Name', 'Elite', 'Human', 50, 10, 5, 7, 6, 8, 6, 5, 0, 'Special rules description', 100),
    (7, 'UnitName6', 'Class Name', 'Elite', 'Human', 50, 10, 5, 7, 6, 8, 6, 5, 0, 'Special rules description', 100),
    (7, 'UnitName7', 'Class Name', 'Elite', 'Human', 50, 10, 5, 7, 6, 8, 6, 5, 0, 'Special rules description', 100),
    (5, 'UnitName8', 'Class Name', 'Elite', 'Human', 50, 10, 5, 7, 6, 8, 6, 5, 0, 'Special rules description', 100);

INSERT INTO unit_skillset (unit_id, skillset_id)
VALUES (1, 3),
    (1, 4),
    (2, 6),
    (3, 6);

INSERT INTO unit_skill (unit_id, skill_id)
VALUES (3, 7);

INSERT INTO item (unit_id, item_ref_id)
VALUES (1, 1), -- ID 1
       (1, 5), -- ID 2
       (1, 3), -- ID 3
       (2, 6); -- ID 4

INSERT INTO item (team_id, item_ref_id)
VALUES (1, 1),  -- ID 5
       (1, 5),  -- ID 6
       (1, 3), -- ID 7
       (2, 6); -- ID 8


COMMIT;
