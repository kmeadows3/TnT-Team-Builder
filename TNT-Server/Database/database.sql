BEGIN TRANSACTION;

DROP TABLE IF EXISTS team, unit_reference, faction, tnt_user, skill_reference, skillset_reference, item_trait_reference;
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
	new_purchase_note text,
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


















INSERT INTO faction (faction_name) VALUES 
	('Caravanners'),
	('Mutants'),
	('Raiders'),
	('Preservers'),
	('Tribals'),
	('Peacekeepers'),
	('Freelancers');
	
INSERT INTO unit_reference (faction_id, class, rank, species, base_cost, wounds, 
							defense, mettle, move, ranged, melee, strength, skillsets,
							starting_skills, starting_free_skills, new_purchase_note) VALUES
	(1, 'Trade Master', 'Leader', 'Human', 80, 2, 6, 7, 5, 6, 6, 6, '[1|2|3|4|5|6|7|8]', '[1]', 2, 'Starts with 2 empty skills'),
	(1, 'Lieutenant', 'Elite', 'Human', 45, 1, 6, 6, 5, 5, 5, 5, '[2|3|7|8]', '[2]', 1, 'Starts with 1 empty skill'),
	(1, 'Tracker', 'Elite', 'Human', 50, 1, 6, 6, 5, 5, 5, 5, '[2|3|4]', '[3|4]', 0, ''),
	(1, 'Defender', 'Rank and File', 'Human', 23, 1, 6, 5, 5, 4, 4, 5, '[1|2|3]', '[5]', 0, ''),
	(3, 'Bandit King', 'Leader', 'Human', 80, 2, 6, 7, 5, 6, 6, 6, '[1|2|3|4|5|6|7|8]', '', 3, 'Starts with 3 empty skills'),
	(3, 'Brute', 'Elite', 'Human', 50, 1, 6, 6, 6, 4, 7, 6, '[1|6|7]', '[6|7|8]', 0, 'Unless Leader is warlord only 1 per team'),
	(3, 'Raider Champion', 'Elite', 'Human', 50, 1, 6, 6, 5, 5, 5, 5, '[1|6|7|8]', '', 2, 'Starts with 2 skills'),
	(3, 'Raider', 'Rank and File', 'Human', 20, 1, 6, 5, 5, 4, 4, 5, '[1|2|3]', '', 0, '');

INSERT INTO skillset_reference (skillset_name, category) VALUES 
	('Melee', 'Skill'),
	('Marksmanship', 'Skill'),
	('Survival', 'Skill'),
	('Quickness', 'Skill'),
	('Smarts', 'Skill'),
	('Brawn', 'Skill'),
	('Tenacity', 'Skill'),
	('Leadership', 'Skill'),
	('Hidden Defensive Mutations', 'Mutation'),
	('Hidden Offensive Mutations', 'Mutation'),
	('Physical Mutations', 'Mutation'),
	('Psychic Mutations', 'Mutation'),
	('Hidden Detriments', 'Detriment'),
	('Physical Detriments', 'Detriment'),
	('General Abilities', 'General');
	
INSERT INTO skill_reference (skillset_id, name, description) VALUES
	(5, 'Scavenger', 'When taking a weapon with limited ammo roll 2d3 when determining ammo quantity and take the higher of the two. Upkeep does not need to be paid for this unit. May not be taken by Freelancers.'),
	(8, 'Motivator', 'All friendly models withing 6" of this model gain +1 to activation tests. Motivator may not stack with itself.'),
	(4, 'Reconnoiter', 'At the start of the game after all models have deployed but before init is determined make a free move action.'),
	(3, 'Trekker', 'When moving through Difficult Terrain attempt an Agility test (MET/TN 10) for free. On pass move through terrain without movement penalty.'),
	(7, 'Brave', '+2 bonus when making Will tests.'),
	(6, 'Brute', 'Gaint +1 to Strength Stat when making Melee attacks. Ignore heavy weapons rule.'),
	(6, 'Bully', 'All enemies defeated by this model in close combat are knocked prone in addition to any other combat result.'),
	(15, 'Dumb', 'Takes a -2 penalty to intelligence tests');
	
INSERT INTO item_trait_reference (name, effect)






COMMIT;