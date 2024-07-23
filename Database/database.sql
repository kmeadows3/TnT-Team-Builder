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
	('General Abilities', 'General'); -- ID 15

INSERT INTO skill_reference (skillset_id, name, description) VALUES
	(5, 'Scavenger', 'When taking a weapon with limited ammo roll 2d3 when determining ammo quantity and take the higher of the two. Upkeep does not need to be paid for this unit. May not be taken by Freelancers.'), --ID 1
	(8, 'Motivator', 'All friendly models within 6" of this model gain +1 to activation tests. Motivator may not stack with itself.'),
	(4, 'Reconnoiter', 'At the start of the game after all models have deployed but before init is determined make a free move action.'),
	(3, 'Trekker', 'When moving through Difficult Terrain attempt an Agility test (MET/TN 10) for free. On pass move through terrain without movement penalty.'),
	(7, 'Brave', '+2 bonus when making Will tests.'), --ID 5
	(7, 'Fearful Repuatation', 'All enemy models that attempt to charge this model or move in base contact must pass a Will test (MET/TN10). On fail, the enemy can do any other action, but cannot approach the model this turn.'),
	(6, 'Brute', 'Gain +1 to Strength Stat when making Melee attacks. Ignore heavy weapons rule.'),
	(6, 'Bully', 'All enemies defeated by this model in close combat are knocked prone in addition to any other combat result.'),
	(15, 'Dumb', 'Takes a -2 penalty to intelligence tests'),
	(8, 'Bold', 'Any friendly models in 12" of this model gain +1 to all Will tests. Does not stack if multiple units have Bold.'), --ID 10
	(15, 'Frenzied', 'At the beginning of its activation, this model must make a free Intelligence Test (MET/TN 10). On success, 
		it acts as normal. On fail, it goes into a frenzy and may not take any actions but charging or moving towards the nearest enemy, 
		or melee attacking them if already in base contact. However, it also gains +2 to Will tests while frenzied. On subsequent activations, model may repeat 
		the intelligence test to regain control.'),
	(15, 'Ungainly', 'Model takes -2 penalty to all Agility tests.'); 


INSERT INTO injury_reference (name, description, is_stat_damage, stat_damaged, is_removeable, is_stackable, grants) VALUES
	('Gashed Leg', '-1 penalty to Move', true, 'Move', false, true, null),
	('Banged Head', '-1 penalty to Mettle', true, 'Mettle', false, true, null),
	('Brain Sprain', 'Model gains the Dumb general ability', false, null, false, false, 9),
	('Captured', 'Model is captured and your opponent determines what happens to them (See Rulebook).', false, null, true, false, null),
	('Banged Up', 'Model has -1 to all rolls it makes during the next campaign game.', false, null, true, false, null),
	('Long Recovery', 'Model misses the next campaign game.', false, null, true, false, null),
	('Horrible Scarring', 'Model gains Fearful Reputation for free.', false, null, false, false, 6);

	
INSERT INTO item_trait_reference (name, effect) VALUES
	('Burst', 'Allow the shooter to fire twice in an activation. If all AP is used to shoot, gain 1 extra AP that must be used to fire a final time.'),
	('Pistol', 'When used in close combat, resolve any attacks with the user''s Melee stat instead of ranged but use Strength of pistol instead of Strength when rolling to Wound. May not benefit from Brawn skills. Weapon abilities may still apply unless they specify they affect Ranged Attacks. Fumbles still result in Jams.'),
	('Flammable', 'Units hit by weapon must pass an Agility check during Cleanup Phase or suffer a d6 Strength hit. If test passed, flames are put out and no additional damage taken. Units hit by weapon must make a Morale Test (MET/TN 10) instead of a Grazed Test, but fumbles only count as failures.'),
	('Limited Ammo', 'At start of game, roll d3+2 to determine the number of times this weapon may be used.'),
	('Move or Fire', 'Weapon may not be fired if the attacker moved or intends to move during the same activation.'), --ID 5
	('Large Blast', 'When fired, designate single model as target. Place 5" template on that model. All models at least partially under template are affected by attack. Template may be shifted as long as original target is fully covered.'),
	('Stun', 'Instead of rolling to Wound target must pass Survival test (MET/TN 10) or lose all AP during its next activation.'),
	('Small Blast', 'When fired, designate single model as target. Place 3" template on that model. All models at least partially under template are affected by attack. Template may be shifted as long as original target is fully covered.'),
	('Gas', 'Ranged attacks with weapon ignore Armor Bonus'),
	('Knock Out', 'Model hit by weapon must past Survival Test (MET/TN 10) or immediately go unconscious. While unconscious, unit is prone and cannot take actions. At beginning of its activations, it must pass a free Survival test (MET/TN10) to recover and act as normal.'), --ID 10
	('Shield', 'Gain +1 to Melee stat when defending in melee. If also equipped with armor, gain +1 to the armor''s Melee Armor Bonus. Counts as 1-handed weapon for carrying capacity, but model using it may not gain bonus for having two one-handed melee weapons. May be used as Light Improvised Weapon.'),
	('Reduces Movement', 'Model equipped with item loses 1 to Move'),
	('Grants Ungainly', 'Model equipped with item gains the Ungainly trait'),
	('Single Use', 'May only be used once a battle'),
	('Grants Bold', 'Model equipped with item gains the Bold trait'), --ID 15
	('Malfunction Prone', 'When model rolls fumble when using relic, item breaks and gains 1 malfunction token (unless weapon which gains 1 token per Reliablity). To remove a token, model must spend 1 AP to pass an Intelligence Test (MET/TN 10). Passive items malfunction on fumble during Activation Test. In case of multiple passive relics, opponent gets to choose which relic malfunctions. '),
	('Plasma', 'Gain the Anti-Armor trait. When model crits, gain +2 Strength bonus when attempting to wound'),
	('Thermocycling', 'May not use Burst on consecutive turns'),
	('Hail of Lead', 'Weapon may shoot twice per AP spent. -1 modifier to hit if activated. Stacks with Burst.'),
	('Armored Plating', 'When making defense rolls, roll 1d10 and 1d6 and take either result.'), --ID 20
	('Distracting', 'Models hit by weapon must pass a Surival test (MET/TN 10) or suffer a -2 penality to all Stat tests and Opposed tests on their next activation.'),
	('Anti-Armor', 'When used against something with Armored Plating, target may not roll the extra dice.'),
	('Laser', 'Roll 2d10 and pick highest result when making a Ranged attack. Malfunctions on 1s or any double result.');

INSERT INTO item_reference (name, cost, special_rules, rarity, is_relic, melee_defense_bonus, ranged_defense_bonus, is_shield,
							cost_2_wounds, cost_3_wounds, melee_range, ranged_range, weapon_strength, reliability, 
							hands_required, item_category, grants) VALUES
	('Ballistic Shield', 8, 'N/A', 'N/A', FALSE, 1, 1, TRUE, 10, 12, null, null, null, null, 1, 'Armor', null),
	('Combat Armor', 15, 'N/A', 'N/A', FALSE, 1, 2, FALSE, 20, 25, null, null, null, null, 0, 'Armor', null),
	('Biohazard Suit', 5, 'Benefits against Gas attacks, but grants Ungainly', 'N/A', FALSE, 1, 0, FALSE, 7, 9, null, null, null, null, 0, 'Armor', 12),
	('Combat Shield', 6, 'N/A', 'N/A', FALSE, 2, 0, TRUE, 8, 10, null, null, null, null, 1, 'Armor', null),
	('Power Armor', 50, '+2 to bearer Strength, counts as Biohazard suit.', 'Ultra Rare', TRUE, 4, 4, FALSE, 75, 100, null, null, null, null, 0, 'Armor', null),
	('Shock Shield', 10, 'Counts as having a normal Combat Shield. 1/Turn, make an attack as an improvised light weapon with strength STR + 2', 'Scarce', TRUE, 2, 0, TRUE, 15, 20, null, null, null, null, 1, 'Armor', null),
	('Berserker Brew', 3, 'When consumed before battle, gain +1 to Move and Melee but gain Frenzied', 'N/A', FALSE, null, null, null, null, null, null, null, null, null, 0, 'Equipment', 11),
	('Climbing Gear', 7, 'When testing for climbing, roll 2d10 and take highest result', 'N/A', FALSE, null, null, null, null, null, null, null, null, null, 0, 'Equipment', null),
	('Net', 4, 'Thrown item with range = STR, ignores all combat modifiers but Concentrate. On hit, target cannot take actions until it spends 1 AP and successfully passes an attempt to free itself (STR/TN 10) during its activation. Psychic powers may be used while in the net. ', 'N/A', FALSE, null, null, null, null, null, null, null, null, null, 0, 'Equipment', null),
	('War Banner', 8, 'Model may not use 2-handed items, but gains Bold. One per warband.', 'N/A', FALSE, null, null, null, null, null, null, null, null, null, 1, 'Equipment', 10),
	('Auto-Injector', 15, 'When model is about to go out of action due to wound loss, roll a survival test (MET/TN 10). On pass, model comes back into play with 1 wound remaining. Starts prone but otherwise acts normally.', 'Rare', TRUE, null, null, null, null, null, null, null, null, null, 0, 'Equipment', null),
	('Grappler', 10, 'May ascend or descend vertical surfaces using normal movement rate. Only fail climbing tests on Fumbles.', 'Scarce', TRUE, null, null, null, null, null, null, null, null, null, 0, 'Equipment', null),
	('Pre-Fall Ammo', 10, 'Model must nominate a particular firearm. Firearm gains +1 bonus to Ranged when used. If Firearm fumbles during use, lose bonus.', 'Sporadic', TRUE, null, null, null, null, null, null, null, null, null, 0, 'Equipment', null),
	('Bayonet', 4, 'Doesn''t count against carry capacity; add unit Strength to weapon Strength during melee attack', 'N/A', FALSE, null, null, null, null, null, 0, null, 1, null, 0, 'Melee Weapon', null),
	('Fist', 0, 'Free; add unit Strength to weapon Strength during melee attack', 'N/A', FALSE, null, null, null, null, null, 0, null, -1, null, 0, 'Melee Weapon', null),
	('Spear', 5, 'Add unit Strength to weapon Strength during melee attack', 'N/A', FALSE, null, null, null, null, null, 1, 6, 1, null, 2, 'Melee Weapon', null),
	('Assault Rifle', 15, 'N/A', 'N/A', FALSE, null, null, null, null, null, null, 24, 7, 2, 2, 'Ranged Weapon', null),
	('Pistol', 12, 'N/A', 'N/A', FALSE, null, null, null, null, null, 0, 12, 6, 1, 1, 'Ranged Weapon', null),
	('Rifle', 10, 'N/A', 'N/A', FALSE, null, null, null, null, null, null, 30, 7, 1, 2, 'Ranged Weapon', null),
	('Flamethrower', 15, 'Uses Flame Template for Range', 'N/A', FALSE, null, null, null, null, null, null, null, 6, 2, 2, 'Support Weapon', null),
	('Light Machine Gun', 25, 'N/A', 'N/A', FALSE, null, null, null, null, null, null, 36, 8, 2, 2, 'Support Weapon', null),
	('Grenade Launcher', 20, 'Deviate Small Blast; may use any grenade the user purchases seperately', 'N/A', FALSE, null, null, null, null, null, null, 24, 7, 2, 2, 'Support Weapon', null),
	('Flash Bang', 8, 'Deviate Large Blast; uses STR for range', 'N/A', FALSE, null, null, null, null, null, null, null, null, null, 1, 'Grenade', null),
	('Fragmentation Grenade', 7, 'Deviate Large Blast; uses STR for range', 'N/A', FALSE, null, null, null, null, null, null, null, 7, null, 1, 'Grenade', null),
	('Sleep Grenade', 7, 'Deviate Large Blast; uses STR for range', 'N/A', FALSE, null, null, null, null, null, null, null, null, null, 1, 'Grenade', null),
	('Actuated Gauntlet', 25, 'Add unit Strength to weapon Strength during melee attack', 'Rare', TRUE, null, null, null, null, null, 0, null, 3, 2, 1, 'Melee Weapon', null),
	('Plasma Rifle', 35, 'N/A', 'Rare', TRUE, null, null, null, null, null, null, 24, 9, 2, 2, 'Ranged Weapon', null),
	('Gatling Laser', 55, 'N/A', 'Ultra Rare', TRUE, null, null, null, null, null, null, 24, 6, 3, 2, 'Support Weapon', null),
	('Plasma Grenade', 17, 'Deviate Small Blast; uses unit strength for range, maximum 6"', 'Rare', TRUE, null, null, null, null, null, null, 6, 8, null, 1, 'Grenade', null);

INSERT INTO item_ref_item_trait (item_ref_id, item_trait_id) VALUES
	(1, 11),
	(2, 12),
	(4, 11),
	(5, 16),
	(5, 20),
	(6, 16),
	(6, 21),
	(6, 11),
	(9, 14),
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

COMMIT;