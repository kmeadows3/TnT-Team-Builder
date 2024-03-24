BEGIN TRANSACTION;

DROP TABLE IF EXISTS team_item, unit_item, unit_skillset, unit_skill, unit, team, unit_reference, faction, tnt_user, skill_reference, skillset_reference, item_item_trait, item_trait_reference, item_reference;
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
	item_id serial PRIMARY KEY NOT NULL,
	name varchar(50) NOT NULL,
	cost int NOT NULL,
	specialRules text NOT NULL,
	rarity varchar(12) NOT NULL,
	is_relic boolean NOT NULL,
	melee_defense_bonus int,
	ranged_defense_bonus int,
	is_shield boolean,
	cost_2_wounds int,
	cost_3_wounds int,
	melee_range int,
	ranged_ranged int,
	weapon_strength int,
	reliablity int,
	handsRequired int,
	itemCategory varchar(50)
	);

CREATE TABLE item_item_trait(
	item_trait_id int NOT NULL,
	item_id int NOT NULL,
	CONSTRAINT FK_item_item_trait_join_item_trait_id FOREIGN KEY(item_trait_id) REFERENCES item_trait_reference(item_trait_id),
	CONSTRAINT FK_item_item_trait_join_item_id FOREIGN KEY(item_id) REFERENCES item_reference(item_id)
);


CREATE TABLE team_item(
	team_id int NOT NULL,
	item_id int NOT NULL,
	CONSTRAINT FK_team_item_join_item_id FOREIGN KEY(item_id) REFERENCES item_reference(item_id),
	CONSTRAINT FK_team_item_join_team_id FOREIGN KEY(team_id) REFERENCES team(team_id)
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
	CONSTRAINT FK_unit_team FOREIGN KEY(team_id) REFERENCES team(team_id)
);


CREATE TABLE unit_item(
	unit_id int NOT NULL,
	item_id int NOT NULL,
	CONSTRAINT FK_unit_item_join_item_id FOREIGN KEY(item_id) REFERENCES item_reference(item_id),
	CONSTRAINT FK_unit_item_join_unit_id FOREIGN KEY(unit_id) REFERENCES unit(unit_id)
);

CREATE TABLE unit_skillset(
	unit_id int NOT NULL,
	skillset_id int NOT NULL,
	CONSTRAINT FK_unit_skillset_join_skillset_id FOREIGN KEY(skillset_id) REFERENCES skillset_reference(skillset_id),
	CONSTRAINT FK_unit_skillset_join_unit_id FOREIGN KEY(unit_id) REFERENCES unit(unit_id)
);

CREATE TABLE unit_skill(
	unit_id int NOT NULL,
	skill_id int NOT NULL,
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
	(3, 'Bandit King', 'Leader', 'Human', 80, 2, 6, 7, 5, 6, 6, 6, '[1|2|3|4|5|6|7|8]', '', 3, 'N/A'),
	(3, 'Brute', 'Elite', 'Human', 50, 1, 6, 6, 6, 4, 7, 6, '[1|6|7]', '[6|7|8]', 0, 'Unless Leader is warlord, only 1 per team'),
	(3, 'Raider Champion', 'Elite', 'Human', 50, 1, 6, 6, 5, 5, 5, 5, '[1|6|7|8]', '', 2, 'N/A'),
	(3, 'Raider', 'Rank and File', 'Human', 20, 1, 6, 5, 5, 4, 4, 5, '[1|2|3]', '', 0, 'N/A');

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

INSERT INTO item_trait_reference (name, effect) VALUES
	('Burst', 'Allow the shooter to fire twice in an activation. If all AP is used to shoot, gain 1 extra AP that must be used to fire a final time.'),
	('Pistol', 'When used in close combat, resolve any attacks with the user''s Melee stat instead of ranged but use Strength of pistol instead of Strength when rolling to Wound. May not benefit from Brawn skills. Weapon abilities may still apply unless they specify they affect Ranged Attacks. Fumbles still result in Jams.'),
	('Flammable', 'Units hit by weapon must pass an Agility check during Cleanup Phase or suffer a d6 Strength hit. If test passed, flames are put out and no additional damage taken. Units hit by weapon must make a Morale Test (MET/TN 10) instead of a Grazed Test, but fumbles only count as failures.'),
	('Limited Ammo', 'At start of game, roll d3+2 to determine the number of times this weapon may be used.'),
	('Move or Fire', 'Weapon may not be fired if the attacker moved or intends to move during the same activation.'),
	('Large Blast', 'When fired, designate single model as target. Place 5" template on that model. All models at least partially under template are affected by attack. Template may be shifted as long as original target is fully covered.'),
	('Stun', 'Instead of rolling to Wound target must pass Survival test (MET/TN 10) or lose all AP during its next activation.'),
	('Small Blast', 'When fired, designate single model as target. Place 3" template on that model. All models at least partially under template are affected by attack. Template may be shifted as long as original target is fully covered.'),
	('Gas', 'Ranged attacks with weapon ignore Armor Bonus'),
	('Knock Out', 'Model hit by weapon must past Survival Test (MET/TN 10) or immediately go unconscious. While unconscious, unit is prone and cannot take actions. At beginning of its activations, it must pass a free Survival test (MET/TN10) to recover and act as normal.'),
	('Shield', 'Gain +1 to Melee stat when defending in melee. If also equipped with armor, gain +1 to the armor''s Melee Armor Bonus. Counts as 1-handed weapon for carrying capacity, but model using it may not gain bonus for having two one-handed melee weapons. May be used as Light Improvised Weapon.'),
	('Reduces Movement', 'Model equipped with item loses 1 to Move'),
	('Grants Ungainly', 'Model equipped with item gains the Ungainly trait'),
	('Single Use', 'May only be used once a battle'),
	('Grants Bold', 'Model equipped with item gains the Bold trait'),
	('Malfunction Prone', 'When model rolls fumble when using relic, item breaks and gains 1 malfunction token (unless weapon which gains 1 token per Reliablity). To remove a token, model must spend 1 AP to pass an Intelligence Test (MET/TN 10). Passive items malfunction on fumble during Activation Test. In case of multiple passive relics, opponent gets to choose which relic malfunctions. '),
	('Plasma', 'Gain the Anti-Armor trait. When model crits, gain +2 Strength bonus when attempting to wound'),
	('Thermocycling', 'May not use Burst on consecutive turns'),
	('Hail of Lead', 'Weapon may shoot twice per AP spent. -1 modifier to hit if activated. Stacks with Burst.'),
	('Armored Plating', 'When making defense rolls, roll 1d10 and 1d6 and take either result.'),
	('Distracting', 'Models hit by weapon must pass a Surival test (MET/TN 10) or suffer a -2 penality to all Stat tests and Opposed tests on their next activation.'),
	('Anti-Armor', 'When used against something with Armored Plating, target may not roll the extra dice.'),
	('Laser', 'Roll 2d10 and pick highest result when making a Ranged attack. Malfunctions on 1s or any double result.');

INSERT INTO item_reference (name, cost, specialRules, rarity, is_relic, melee_defense_bonus, ranged_defense_bonus, is_shield,
							cost_2_wounds, cost_3_wounds, melee_range, ranged_ranged, weapon_strength, reliablity,
							handsRequired, itemCategory) VALUES
	('Ballistic Shield', 8, 'N/A', 'N/A', FALSE, 1, 1, TRUE, 10, 12, null, null, null, null, 1, 'Armor'),
	('Combat Armor', 15, 'N/A', 'N/A', FALSE, 1, 2, FALSE, 20, 25, null, null, null, null, null, 'Armor'),
	('Biohazard Suit', 5, 'Benefits against Gas attacks', 'N/A', FALSE, 1, 0, FALSE, 7, 9, null, null, null, null, null, 'Armor'),
	('Combat Shield', 6, 'N/A', 'N/A', FALSE, 2, 0, TRUE, 8, 10, null, null, null, null, 1, 'Armor'),
	('Power Armor', 50, '+2 to bearer Strength, counts as Biohazard suit.', 'Ultra Rare', TRUE, 4, 4, FALSE, 75, 100, null, null, null, null, null, 'Armor'),
	('Shock Shield', 10, 'Counts as having a normal Combat Shield. 1/Turn, make an attack as an improvised light weapon with strength STR + 2', 'Scarce', TRUE, 2, 0, TRUE, 15, 20, null, null, null, null, 1, 'Armor'),
	('Berserker Brew', 3, 'When consumed before battle, gain +1 to move and melee but gain Frenzied', 'N/A', FALSE, null, null, null, null, null, null, null, null, null, null, 'Equipment'),
	('Climbing Gear', 7, 'When testing for climbing, roll 2d10 and take highest result', 'N/A', FALSE, null, null, null, null, null, null, null, null, null, null, 'Equipment'),
	('Net', 4, 'Thrown item with range = STR, ignores all combat modifiers but Concentrate. On hit, target cannot take actions until it spends 1 AP and successfully passes an attempt to free itself (STR/TN 10) during its activation. Psychic powers may be used while in the net. ', 'N/A', FALSE, null, null, null, null, null, null, null, null, null, null, 'Equipment'),
	('War Banner', 8, 'Model may not use 2-handed items, but gains Bold. One per warband.', 'N/A', FALSE, null, null, null, null, null, null, null, null, null, 1, 'Equipment'),
	('Auto-Injector', 15, 'When model is about to go out of action due to wound loss, roll a survival test (MET/TN 10). On pass, model comes back into play with 1 wound remaining. Starts prone but otherwise acts normally.', 'Rare', TRUE, null, null, null, null, null, null, null, null, null, null, 'Equipment'),
	('Grappler', 10, 'May ascend or descend vertical surfaces using normal movement rate. Only fail climbing tests on Fumbles.', 'Scarce', TRUE, null, null, null, null, null, null, null, null, null, null, 'Equipment'),
	('Pre-Fall Ammo', 10, 'Model must nominate a particular firearm. Firearm gains +1 bonus to Ranged when used. If Firearm fumbles during use, lose bonus.', 'Sporadic', TRUE, null, null, null, null, null, null, null, null, null, null, 'Equipment'),
	('Bayonet', 4, 'Doesn''t count against carry capacity; add unit Strength to weapon Strength during melee attack', 'N/A', FALSE, null, null, null, null, null, 0, null, 1, null, null, 'Melee Weapon'),
	('Fist', 0, 'Free; add unit Strength to weapon Strength during melee attack', 'N/A', FALSE, null, null, null, null, null, 0, null, -1, null, null, 'Melee Weapon'),
	('Spear', 5, 'Add unit Strength to weapon Strength during melee attack', 'N/A', FALSE, null, null, null, null, null, 1, 6, 1, null, 2, 'Melee Weapon'),
	('Assault Rifle', 15, 'N/A', 'N/A', FALSE, null, null, null, null, null, null, 24, 7, 2, 2, 'Ranged Weapon'),
	('Pistol', 12, 'N/A', 'N/A', FALSE, null, null, null, null, null, 0, 12, 6, 1, 1, 'Ranged Weapon'),
	('Rifle', 10, 'N/A', 'N/A', FALSE, null, null, null, null, null, null, 30, 7, 1, 2, 'Ranged Weapon'),
	('Flamethrower', 15, 'Uses Flame Template for Range', 'N/A', FALSE, null, null, null, null, null, null, null, 6, 2, 2, 'Support Weapon'),
	('Light Machine Gun', 25, 'N/A', 'N/A', FALSE, null, null, null, null, null, null, 36, 8, 2, 2, 'Support Weapon'),
	('Grenade Launcher', 20, 'Deviate Small Blast; may use any grenade the user purchases seperately', 'N/A', FALSE, null, null, null, null, null, null, 24, 7, 2, 2, 'Support Weapon'),
	('Flash Bang', 8, 'Deviate Large Blast; uses STR for range', 'N/A', FALSE, null, null, null, null, null, null, null, null, null, 1, 'Grenade'),
	('Fragmentation Grenade', 7, 'Deviate Large Blast; uses STR for range', 'N/A', FALSE, null, null, null, null, null, null, null, 7, null, 1, 'Grenade'),
	('Sleep Grenade', 7, 'Deviate Large Blast; uses STR for range', 'N/A', FALSE, null, null, null, null, null, null, null, null, null, 1, 'Grenade'),
	('Actuated Gauntlet', 25, 'Add unit Strength to weapon Strength during melee attack', 'Rare', TRUE, null, null, null, null, null, 0, null, 3, 2, 1, 'Melee Weapon'),
	('Plasma Rifle', 35, 'N/A', 'Rare', TRUE, null, null, null, null, null, null, 24, 9, 2, 2, 'Ranged Weapon'),
	('Gatling Laser', 55, 'N/A', 'Ultra Rare', TRUE, null, null, null, null, null, null, 24, 6, 3, 2, 'Support Weapon'),
	('Plasma Grenade', 17, 'Deviate Small Blast; uses unit strength for range, maximum 6"', 'Rare', TRUE, null, null, null, null, null, null, 6, 8, null, 1, 'Grenade');

INSERT INTO item_item_trait (item_id, item_trait_id) VALUES
	(1, 11),
	(2, 12),
	(3, 13),
	(4, 11),
	(5, 16),
	(5, 20),
	(6, 16),
	(6, 21),
	(6, 11),
	(9, 14),
	(10, 15),
	(11, 14),
	(17, 1),
	(18, 2),
	(20, 3),
	(20, 4),
	(21, 1),
	(21, 5),
	(22, 4),
	(22, 8),
	(23, 4),
	(23, 6),
	(23, 7),
	(24, 4),
	(24, 8),
	(25, 4),
	(25, 9),
	(25, 10),
	(25, 8),
	(26, 16),
	(27, 16),
	(27, 17),
	(28, 1),
	(28, 19),
	(28, 23),
	(28, 5),
	(28, 16),
	(28, 18),
	(29, 4),
	(29, 17),
	(29, 8);

INSERT INTO tnt_user (username,password_hash,role) VALUES ('user1','user1','ROLE_USER');
INSERT INTO tnt_user (username,password_hash,role) VALUES ('user2','user2','ROLE_USER');
INSERT INTO tnt_user (username,password_hash,role) VALUES ('user3','user3','ROLE_USER');

INSERT INTO team(user_id, faction_id, team_name, money) VALUES (1, 1, 'Team 1', 500);
INSERT INTO team(user_id, faction_id, team_name, money) VALUES (1, 3, 'Team 2', 1500);
INSERT INTO team(user_id, faction_id, team_name, money) VALUES (2, 2, 'Team 3', 1000);

INSERT INTO unit (team_id, name, class, rank, species, base_cost, wounds, defense, mettle, move, ranged, melee,
    strength, empty_skills, special_rules, spent_exp)
VALUES (1, 'UnitName1', 'Trade Master', 'Leader', 'Human', 50, 10, 5, 7, 6, 8, 6, 5, 0, 'Special rules description', 100);
VALUES (3, 'UnitName2', 'Soldier', 'Elite', 'Mutant', 51, 11, 6, 8, 7, 9, 7, 6, 1, 'Special rules description', 50);


COMMIT;
