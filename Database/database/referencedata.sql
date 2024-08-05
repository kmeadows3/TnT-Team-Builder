BEGIN TRANSACTION;

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
	(1, 'Trade Master', 'Leader', 'Human', 80, 2, 6, 7, 5, 6, 6, 6, '[1|2|3|4|5|6|7|8]', '[30]', 2, 'N/A'),
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

INSERT INTO skill_reference (skillset_id, name, description, phase, skill_cost) VALUES
(1, 'Against All Odds', 'When this model is outnumbered in close combat, it gains +1 to its melee stat and enemies in base contact with this model do not gain bonuses for outnumbering it.', 'Game', 0), --ID 1
(1, 'Careful Blow', 'All melee attacks by this model have the Ignore Armor (1) Ability, meaning enemies defend attacks as if their bonus to defense from Armor is one lower (minimum 0).', 'Game', 0), --ID 2
(1, 'Defender', 'When attacked in melle, this model gains +1 to it’s melee and defense stats.', 'Game', 0), --ID 3
(1, 'Flurry of Blows', 'On any turn this model makes a melee attack, it gains an additional free AP that may only be used to make another melee attack. The second attack suffers a -1 Melee stat penalty.', 'Game', 0), --ID 4
(1, 'Opportunist', 'During the to-hit phase of melee combat, this model treats ties as wins instead.', 'Game', 0), --ID 5
(1, 'Quick Charge', 'On any turn this model fails an activation test, it may make  a second move action for free - but only to complete a charge move into contact with an enemy.', 'Game', 0), --ID 6
(2, 'Called Shot', 'Prior to rolling hit in a ranged combat, model may declare a Called Shot. The TN of the shot raises to 12, but if successful the shot has +1 Strength and Ignore Armor (1).', 'Game', 0), --ID 7
(2, 'Fast-Tracker', 'Model ignores the -1 penalty when firing at targets that double-moved.', 'Game', 0), --ID 8
(2, 'Field-Strip', 'When using the unjam action, model may remove two Jammed tokens unstead of one.', 'Game', 0), --ID 9
(2, 'Marksman', 'Model ignores the -1 penalty when shooting at a model in soft cover and reduces the penalty for shooting at a model in hard cover to -1.', 'Game', 0), --ID 10
(2, 'Range Finder', 'When shooting a weapon, model adds 6” to the weapon’s maximum range. This does not affect or apply to any range-dependant abilities the weapon may have, and may not be used with relic weapons, support weapons, or weapons that use the Flame template.', 'Game', 0), --ID 11
(2, 'Steady Hands', 'Model gains a +1 bonus when shooting if it does not move during its activation, both before and after shooting.', 'Game', 0), --ID 12
(3, 'Brace', 'Model may spend 1 AP on its turn to Brace, granting it +1 to Defense until the start of its next activation.', 'Game', 0), --ID 13
(3, 'Hard As Nails', '(Once per Game) After being hit by an attack but before the roll to wound, model may roll 1d10 and reduce the strength of the attack by the result of the roll. If the Strength is reduced to 0 or below, the attack is negated entirely.', 'Game', 0), --ID 14
(3, 'Ranger', 'Regardless of scenario conditions, this model may be deployed anywhere on the board that is at least 12” away from any any model and is behind a terrain feature large enough to conceal them. If this model’s warband deploys first, this model may be put on the table after the enemy deploys. If multiple warbands have Rangers, roll off to determin who uses the ability first. A warband may not have more than 3 models with the Ranger skills, and a model may not use Ranger and Reconnoiter in the same game.', 'Deployment', 0), --ID 15
(3, 'Reactive', 'Model gains +1 to Activation tests. Model may not have the Nimble skill.', 'Game', 0), --ID 16
(3, 'Survivalist', 'Model gains a +2 bonus when taking any Survival Test.', 'Game', 0), --ID 17
(3, 'Trekker', 'When moving through difficult terrain, make an Agility test (MET/TN 10) for free. On success, model moves through the terrain without penalty to Move.', 'Game', 0), --ID 18
(4, 'Flighty', 'Model may reroll a failed Agility test when attempting to disengage in Close Combat. Model may not have the Push Off skill.', 'Game', 0), --ID 19
(4, 'Leap', 'During a move action, model may cross any linear terrain obstacle that is 1” or lower without any reduction in movement.', 'Game', 0), --ID 20
(4, 'Nimble', 'Model gains a +1 bonus to Activation tests. Model may not have the Reactive skill.', 'Game', 0), --ID 21
(4, 'Quick', 'Model gains a +2 bonus when taking any Agility test.', 'Game', 0), --ID 22
(4, 'Reconnoiter', 'At the start of the game after all models are deployed, model may make a free Move action. A model may not use Ranger and Reconnoiter in the same game.', 'Deployment', 0), --ID 23
(4, 'Spring-Heeled', 'When prone, this model may stand during its turn without paying any AP.', 'Game', 0), --ID 24
(5, 'Clever', 'Model gains a +2 bonus when taking any Intelligence Test', 'Game', 0), --ID 25
(5, 'Gunsmith', 'The entire warband ignores the first Jam result of the game, regardless of the number of Jam tokens generated. This ability may not be saved up and occurs during the first Fumble when shooting.', 'Game', 0), --ID 26
(5, 'Haggler', 'Once after each game, when buying items, model may roll a D10 and reduce the cost of a single piece of equipment by the die result (min: 1). The item affected must be declared before the roll is made, but the player is not requried to buy the reduced-price item. ', 'End of Game', 0), --ID 27
(5, 'Maintainer', 'The Reliablity rating of any weapon carried by the model is reduced by 1, to a minimum of 1.', 'Game', 0), --ID 28
(5, 'Resourceful', 'Model adds an additional 1d10 BS to the warband’s total when determining income. A warband may only have 2 models with this ability.', 'End of Game', 0), --ID 29
(5, 'Scavenger', 'When taking any weapon with the Limited Ammo special rule, model may roll 2d3+2 instead of 1d3+2 when determining ammo quantity and take the higher result. Additionally, upkeep costs do not need to be paid for the model. Freelancers cannot take Scavenger.', 'Deployment/Postgame', 0), --ID 30
(6, 'Brute', 'Model gains +1 to Strength when making Melee attacks and ignores the Heavy weapon rules for any weapon it uses.', 'Game', 0), --ID 31
(6, 'Blitzer', 'When charging, model may choose to suffer a -1 penalty to hit for +2 Strength for its melee attack.', 'Game', 0), --ID 32
(6, 'Bully', 'All enemies defeated by this model in close combat are knocked prone in addtion to any other combat result.', 'Game', 0), --ID 33
(6, 'Muscular', 'Model gains a +2 bonus when taking any Strength test.', 'Game', 0), --ID 34
(6, 'Push Off', 'Model may reroll any failed Strength test to attempt to disengage in Close Combat. Model may  not have the Flighty skill.', 'Game', 0), --ID 35
(6, 'Strong Point', 'Model suffers no penalites to Move or Agility when wearing any type of armor.', 'Game', 0), --ID 36
(6, 'Tosser', 'After hitting an enemy model in melee, this model may force the enemy to make an opposed Strength test against it. If this model wins, the enemy is thrown 3” in any direction (thrower’s choice). The thrown model and any intervening model take a Strength 5 hit after landing, and the thrown model becomes Prone. If the enemy model wins the Strength roll, nothing happens and combat continues as normal.', 'Game', 0), --ID 37
(7, 'Brave', 'Model gains a +2 bonus to any Will tests.', 'Game', 0), --ID 38
(7, 'Confident', 'Model may reroll all failed Morale and Grazed tests.', 'Game', 0), --ID 39
(7, 'Fearful Reputation', 'All enemy models that attempt to charge this model or move in base contact must pass a Will test (MET/TN 10). If the attacker fails, they cannot get any closer to the model this turn but may perform any alternative action.', 'Game', 0), --ID 40
(7, 'Frother', 'Model receives the Frenzied rule if it does not have it already. Model may voluntarily choose to failed any Intelligence test to avoid becoming frenzied. While frenzied, the model receive a +1 to its Melee and Strength stats until the end of the game.', 'Game', 0), --ID 41
(7, 'Gung Ho', 'When this model is taken out-of-action, do not remove them from the table or place them prone. Instead, model remains on table until the end of its next Activation, after which is it taken out -of-action as normal. During this final round, the model is immune to all Will tests and adds +2 to its Defense stat. If this model suffers any further wounds before its final activation, remove it as normal.', 'Game', 0), --ID 42
(7, 'Unfazed', 'Model automaticallly passes all Grazed tests, but still may choose to fail as normal.', 'Game', 0), --ID 43
(8, 'All Together', 'Model may spend an AP during its activation to allow any number of friendly models in 3” to immediately make a single move action, which may be a charge. This counts as those model’s activation for the turn, and no activation test is required. Resolve any charge attacks for one model before moving to the next.', 'Game', 0), --ID 44
(8, 'Assertive', 'If this model rolls a Critical result on an activation, they gain 3 AP instead of the normal 2 AP.', 'Game', 0), --ID 45
(8, 'Bold', 'Any friendly models within 12” of this model and in line of sight gain +1 to all Will tests. This does not stack with other models that have Bold.', 'Game', 0), --ID 46
(8, 'Motivatior', 'All friendly models within 6” of this model gain +1 to Activation tests. This does not stack with other models that have Motivator.', 'Game', 0), --ID 47
(8, 'Tactician', 'After all models are placed at the start of the game but before initiative is determined, the player owning this model may roll 1d6 and redeploy that many members of their warband. This may only be used once per player. If two players have this abilitiy, roll off. Winner may choose to redeploy first or second.', 'Deployment', 0), --ID 48
(9, 'Chameleon', 'Any model that is 12” or farther away from this model must pass an Intelligence test (MET/TN 10) to attempt to make a Ranged attack on the model. On fail, the attack cannot be made, but the model may choose to do any other action.', 'Game', 0), --ID 49
(9, 'Electric Shock', 'Any enemy model coming in base contact with this model must pass an Agility test (MET/TN 10) or suffer an immediate Strength 6 hit. This must be resolved before any actions are performed by either the enemy or the mutant. Only one enemy may be affected per game turn.', 'Game', 0), --ID 50
(9, 'Vampiric Drain', 'All non-Robot models within 4” of this model suffer a -1 penalty to Defense.', 'Game', 0), --ID 51
(9, 'Energy Absorption', 'Model gains a +2 bonus to Defense against hits from Flammable, Laser, and Plasma weapons. It may not be set on fire by any means.', 'Game', 0), --ID 52
(9, 'Caustic Blood', 'Whenver this model is wounded or taken out-of-action, all models within 2” suffer an immediate Strength 7 hit.', 'Game', 0), --ID 53
(9, 'Psychic', 'Model may spend 1 AP and take a Will test to use a psychic ability. On fumble, model takes a 1d6 Strength hit. If the power used is an attack, it automatically hits after the Will test, but any ranged modifiers counta s a penalty to the test. Psychics do not need line of sight if the target is 16” or closer. When first gaining Psychic, user also gains a free Psychic Mutation.', 'Game', 0), --ID 54
(10, 'Flame Breath', 'Model gains a Strength 4 ranged attack that uses the Flammable rule. This abiltiy may not be reused until the model passes a Strength test (STR/TN 10) at the beginning of its activation. This test does not cost AP.', 'Game', 0), --ID 55
(10, 'Caustic Blood', 'Whenver this model is wounded or taken out-of-action, all models within 2” suffer an immediate Strength 7 hit.', 'Game', 0), --ID 56
(10, 'Acid Spit', 'Model gains a ranged attack with a range of 16”, a Strength of 7, and Ignore Armor (1).', 'Game', 0), --ID 57
(10, 'Poisonous Breath', 'Model gains a ranged attack with a range of 12”, a Strength of 6, and has the Gas, Poison, and Small Blast rules.', 'Game', 0), --ID 58
(10, 'Webs', 'Model gainst a ranged attack with a range of 12” and the Burst rule. Instead of rolling to wound, the target hit counts as webbed. A webbed model cannot take any actions that require movement (shooting, melee, most non-passive special abilities, but not psychic powers) until it breaks free by passing a Strength test (STR/TN 10) at the beginning of its next turn. The Strength test costs 1 AP.', 'Game', 0), --ID 59
(10, 'Radioactive', 'Model may spend 1 AP to  place a Large Blast template over itself and irradiate all models, friend and foe, in range. Affected models must pass two survival tests (MET/TN 10) as if they were in an irradiated area with an intensity of 2. Each failure resutls in a -1 to the victim’s defense for the rest of the game. Model must pass a Strength test (STR/TN 10) at the beginning of its activation to reuse this action. Test does not cost AP. In addition, model is immune to the effects of radiation.', 'Game', 0), --ID 60
(11, 'Spikes', 'Any model that makes a melee attack against this model must pass an Agility test (MET/TN 10) or suffer a Strength 6 hit. Models armed with a weapon with a melee range of 1” or greater may ignore this rule.', 'Game', 0), --ID 61
(11, 'Big', 'Model recieves +1 to Strength and counts as having the Large general ability.', 'Game', 15), --ID 62
(11, 'Blob Form', 'Model gains +2 to defense against all ranged attacks from firearms, excluding any wepaons with the Flammable, Laser, or Plasma abilities. All of its close combat attacks have a melee range of 1”, regardless of the weapon used. However, when affected by a weapon with the Flammable abilty, it must roll two dice for the Agility test and choose the lower result.', 'Game', 5), --ID 63
(11, 'Burrow', 'Model may spend 1 AP to go underground. While underground, it can move ignoring terrain and cannot be targetted by ranged weapons, special abilities, or melee attacks. Model must spend 1 AP to reemerge. Model may not begin to burrow or reemerge in solid rock, concrete, asphalt, water, or other similar circumstances. However, while underground, unit can bypass those obstructions. Unit cannot use this to enter enclosed spaces like vaults or bunkers. Unit cannot claim or carry scenario objectives while burrowed. Should any scenario conditions require a warband to specifically attack or engage this model and the game ends with the model underground, the enemy warband gains full Victory Points as if this model was taken down.', 'Game', 10), --ID 64
(11, 'Carapace', 'Model gains a +2 Armor Bonus.', 'Game', 10), --ID 65
(11, 'Crushing Claws', 'Model cannot use any weapons or equipment and must pass an Agility test (MET/TN 10) to open doors or perform any other task that would require hands. However, model counts as being armed with natural weapons and gains a totaly of +3 to strength when attacking with them instead of just +1.', 'Game', 15), --ID 66
(11, 'Fearful Reputation', 'All enemy models that attempt to charge this model or move in base contact must pass a Will test (MET/TN 10). If the attacker fails, they cannot get any closer to the model this turn but may perform any alternative action.', 'Game', 0), --ID 67
(11, 'Grasping Tentacles', 'Any model in base contact with is model must pass a Strength test (STR/TN 10) when making a melee attack or suffer -2 to its Melee stat and -1 to its Strength stat. Penalty goes away at end of its attack.', 'Game', 0), --ID 68
(11, 'Long Arms', 'Model gains +1 to its melee stat and all close combat attacks this model does have a melee range of 1”.', 'Game', 3), --ID 69
(11, 'Long / Multiple Legs', 'Model gains a +1 bonus to its Move stat.', 'Game', 0), --ID 70
(11, 'Multi-Limbed / Prehensile Tail', 'Model may choose either the Flurry of Blows or Steady Hands skill for free.', 'Game', 0), --ID 71
(11, 'Scorpion Tail', 'On any turn the model makes a melee attack, it gains 1 free AP that may only be used to make another melee attack at the model’s normal Strength. The extra attack also has the Poison ability', 'Game', 5), --ID 72
(11, 'Suction', 'Model can traverse walls and other vertical areas as if it was open terrain. Should it ever be hit by an attack while on a vertical surface, it must pass an Agility test (MET/TN 10) or fall down, using the normal rules for falling.', 'Game', 10), --ID 73
(11, 'Two-Headed', 'When taking an Activation test, model may reroll failed result. However, a second failure means the heads have a falling out and the model gains no AP that turn.', 'Game', 10), --ID 74
(11, 'Weapon Growths', 'Model counts as being equipped with natural weapons and gaints +1 to their Melee stat. However, they can only ever use one-handed weapons and equipment. If model takes this twice, they gain a total of +2 to melee, but cannot use any other weapons or equipment and must pass an Agility test (MET/TN 10) to open doors or do any other task that requires the use of hands.', 'Game', 0), --ID 75
(11, 'Wings', 'Model may ignore terrain while moving, but must land at end of turn. If it lands in difficult terrain for any reason, it must pass an Agility test (MET/TN 10) or suffer a D6 Strength hit. Finally, should it fall for any reason, they may make an Agility test (MET/TN 10) to deploy their wings and avoid taking damage by landing softly.', 'Game', 10), --ID 76
(12, 'Cloud Mind', '(Psychic TN 10) The psychic and all friendly models in 3” count as being in heavy cover until the beginning of the psychic’s next activation. Models already in heavy cover recieve no benefit.', 'Game', 0), --ID 77
(12, 'Demoralize', '(Psychic, TN 10) 24” Ranged attack. If successful, target model and all models in 3” of it must immediately pass a Morale test (MET/TN 10)', 'Game', 0), --ID 78
(12, 'Healing Touch', '(Psychic TN 11) One model in base contact that has gone out of action may be brought back into play. They will be Prone and have 1 Wound. Alternatively, may be used to heal a multi-wound modely by 1 wound. Does not affect robots.', 'Game', 0), --ID 79
(12, 'Levitation', '(Psychic TN 10) Model may make an immediate free Move action that ignores terrain, but must land at end of its turn. If it lands in difficult terrain, it must pass an Agility test (MET/TN 10) or suffer a 1d6 Strength hit.', 'Game', 0), --ID 80
(12, 'Mind Shield', '(Psychic TN 10) Model gains an +2 armor bonus that lasts as long as the model can maintain concentration. To do so, model must pass a Will Test at the start of each activation. Test does not cost AP and does not count as using a psychic power. If failed, the Armor Bonus is lost. It is also lost if the model fails a Will test at any point.', 'Game', 0), --ID 81
(12, 'Psychic Battery', '(Psychic) Model may use two psychic powers, including the same one twice, during their activation for 1 AP each. However, the second Will test suffers a -1 penalty and any fumbles will result in a Strength 6 hit. In addition, models with Psychic Battery get another randomly determined psychic mutation at no cost.', 'Game', 0), --ID 82
(12, 'Psychic Bolt', '(Psychic TN 10) Psychic gains a Ranged attack with a 24” range, Strength of 7, and the Small Blast weapon ability.', 'Game', 0), --ID 83
(13, 'Atrophied Muscles', 'Model suffers -1 to its Strength stat.', 'Game', -5), --ID 84
(13, 'Daylight Sensitivity', 'Model suffers a -1 penalty to Mettle in daylight (all scenarios occur in daylight unless otherwise noted). During night time or when in a fully enclosed space, model gains a +1 bonus to Mettle.\', 'Game', -5), --ID 85
(13, 'Frailty', 'Model suffers -1 to its Defense stat.', 'Game', -5), --ID 86
(13, 'Rotted Mind', 'Model suffers -1 penalty to Activation Tests if not within 3” of a friendly model.', 'Game', -5), --ID 87
(13, 'Unending Pain', 'Model gains the Frenzied special rule, and suffers a -2 Penalty to the intelligence test to restrain itself.', 'Game', -5), --ID 88
(13, 'Weakening Sight', 'Model suffers a -1 penalty to its Ranged stat.', 'Game', -5), --ID 89
(14, 'Atrophied Arm', 'Model may not use two one-handed weapons at the same time. If using a 2-handed weapon, suffer a -2 roll to any roll using it as they must grip it awkwardly.', 'Game', -7), --ID 90
(14, 'Inert Twin', 'Model suffers a -1 penalty to its Move stat and a -1 penalty to all Agility tests.', 'Game', -7), --ID 91
(14, 'No Arms', 'Model may only wield a single one-handed weapon and must pass an Agility test (MET/TN 10) to use any action normally completed with hands. However, model gains +1 to Move. Model loses detriment if it ever gains any type of appendage. Model may not have the multi-limbed / Prehensile Tail mutation.', 'Game', -7), --ID 92
(14, 'No Legs', 'All ranged weapons the model carries count as having the Move or Fire ability, and the model suffers -1 to Movement.', 'Game', -7), --ID 93
(14, 'Obese', 'Model gains +1 to Defense, but takes a -1 penalty to its Move stat and all Agility tests. It may never take more than 1 move action per turn.', 'Game', -7), --ID 94
(14, 'Stumpy Leg', 'Model takes a -2 penalty to its Move stat.', 'Game', -7), --ID 95
(15, 'Soft-Bellied', 'Model recieves -2 to all Survival tests.', 'Game', 0), --ID 96
(15, 'Coward', 'Model takes a -2 penalty to all Moral or Grazed tests.', 'Game', 0), --ID 97
(15, 'Dumb', 'Model takes a -2 penalty to all Intelligence tests.', 'Game', 0), --ID 98
(15, 'Frenzied', 'At the beginning of each activation, model must make an Intelligence test (MET/TN 10). On pass, it acts as normal. On fail, it enters a Frenzy. While in a Frenzy, it may only move or charge towards the nearest enemy model, or make a melee attack if it is already in base contact. It also gets a +2 bonus against Will tests. On subsequent activations, model may test Intelligence (MET/TN 10) to try to return to normal. This test does not consume AP.', 'Game', 0), --ID 99
(15, 'Hatred', 'Model gains an enmity towards a particular person or group. When fighting against this group, gain Frienzed.', 'Game', 0), --ID 100
(15, 'Huge', 'Model stands 12” tall and gains a additional wound and gains a +1 bonus to Melee when in combat with other non-Huge units. Model may make a Strength test (STR/TN 10) to resist any ability that would involuntarily move or knock the model prone. Smaller models do no block line of sight to this model.', 'Game', 0), --ID 101
(15, 'Integral', 'Robot has a melee or ranged weapon permanently attacked to one of its arms. Weapon may not be disarmed or lost by the model unless they are killed. This integral weapon may be two-handed, in which case model may use a two-handed and one-handed weapon at the same time. Robot may change out the integral weapon between games. The robot may only have two integral weapons. Having an integral weapon inhibits the robot’s ability to function in an environment, so plays should agree that it suffers a penalty if it attempts an action that needs good function (such as climbing).', 'Game', 0), --ID 102
(15, 'Large', 'Model stands 8” tall and gains a additional wound and gains a +1 bonus to Melee when in combat with normal-sized units. Model may make a Strength test (STR/TN 10) to resist any ability that would involuntarily move or knock the model prone. Smaller models do no block line of sight to this model.', 'Game', 0), --ID 103
(15, 'Medic', 'One model in base contact that has gone out of action may be brought back into play. They will be Prone and have 1 Wound. Alternatively, may be used to heal a multi-wound modely by 1 wound. Does not affect robots. Additional, out of combat, if this unit did not go out of action, one model in the warband may be nominated to gain a +1 bonus when rolling on the Survival table.', 'Game', 0), --ID 104
(15, 'Ragtag', 'Model may never have more than 15 BS in equipment, including weaponry and armor.', 'Game', 0), --ID 105
(15, 'Up-Armed', 'This model may purchase and start the game with a support weapon.', 'Postgame', 0), --ID 106
(15, 'Ungainly', 'This model recieves a -2 penalty to all Agility tests.', 'Game', 0); --ID 107; 


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
	('Power Armor', 50, '+2 to bearer Strength, counts as Biohazard suit.', 'Ultra Rare', TRUE, 4, 4, FALSE, 75, 100, null, null, null, null, 0, 'Armor', null), --ID 5
	('Shock Shield', 10, 'Counts as having a normal Combat Shield. 1/Turn, make an attack as an improvised light weapon with strength STR + 2', 'Scarce', TRUE, 2, 0, TRUE, 15, 20, null, null, null, null, 1, 'Armor', null),
	('Berserker Brew', 3, 'When consumed before battle, gain +1 to Move and Melee but gain Frenzied', 'N/A', FALSE, null, null, null, null, null, null, null, null, null, 0, 'Equipment', 11),
	('Climbing Gear', 7, 'When testing for climbing, roll 2d10 and take highest result', 'N/A', FALSE, null, null, null, null, null, null, null, null, null, 0, 'Equipment', null),
	('Net', 4, 'Thrown item with range = STR, ignores all combat modifiers but Concentrate. On hit, target cannot take actions until it spends 1 AP and successfully passes an attempt to free itself (STR/TN 10) during its activation. Psychic powers may be used while in the net. ', 'N/A', FALSE, null, null, null, null, null, null, null, null, null, 0, 'Equipment', null),
	('War Banner', 8, 'Model may not use 2-handed items, but gains Bold. One per warband.', 'N/A', FALSE, null, null, null, null, null, null, null, null, null, 1, 'Equipment', 10), --ID 10
	('Auto-Injector', 15, 'When model is about to go out of action due to wound loss, roll a survival test (MET/TN 10). On pass, model comes back into play with 1 wound remaining. Starts prone but otherwise acts normally.', 'Rare', TRUE, null, null, null, null, null, null, null, null, null, 0, 'Equipment', null),
	('Grappler', 10, 'May ascend or descend vertical surfaces using normal movement rate. Only fail climbing tests on Fumbles.', 'Scarce', TRUE, null, null, null, null, null, null, null, null, null, 0, 'Equipment', null),
	('Pre-Fall Ammo', 10, 'Model must nominate a particular firearm. Firearm gains +1 bonus to Ranged when used. If Firearm fumbles during use, lose bonus.', 'Sporadic', TRUE, null, null, null, null, null, null, null, null, null, 0, 'Equipment', null),
	('Bayonet', 4, 'Doesn''t count against carry capacity; add unit Strength to weapon Strength during melee attack', 'N/A', FALSE, null, null, null, null, null, 0, null, 1, null, 0, 'Melee Weapon', null),
	('Fist', 0, 'Free; add unit Strength to weapon Strength during melee attack', 'N/A', FALSE, null, null, null, null, null, 0, null, -1, null, 0, 'Melee Weapon', null), --ID 15
	('Spear', 5, 'Add unit Strength to weapon Strength during melee attack', 'N/A', FALSE, null, null, null, null, null, 1, 6, 1, null, 2, 'Melee Weapon', null),
	('Assault Rifle', 15, 'N/A', 'N/A', FALSE, null, null, null, null, null, null, 24, 7, 2, 2, 'Ranged Weapon', null),
	('Pistol', 12, 'N/A', 'N/A', FALSE, null, null, null, null, null, 0, 12, 6, 1, 1, 'Ranged Weapon', null),
	('Rifle', 10, 'N/A', 'N/A', FALSE, null, null, null, null, null, null, 30, 7, 1, 2, 'Ranged Weapon', null),
	('Flamethrower', 15, 'Uses Flame Template for Range', 'N/A', FALSE, null, null, null, null, null, null, null, 6, 2, 2, 'Support Weapon', null), --ID 20
	('Light Machine Gun', 25, 'N/A', 'N/A', FALSE, null, null, null, null, null, null, 36, 8, 2, 2, 'Support Weapon', null),
	('Grenade Launcher', 20, 'Deviate Small Blast; may use any grenade the user purchases seperately', 'N/A', FALSE, null, null, null, null, null, null, 24, 7, 2, 2, 'Support Weapon', null),
	('Flash Bang', 8, 'Deviate Large Blast; uses STR for range', 'N/A', FALSE, null, null, null, null, null, null, null, null, null, 1, 'Grenade', null),
	('Fragmentation Grenade', 7, 'Deviate Large Blast; uses STR for range', 'N/A', FALSE, null, null, null, null, null, null, null, 7, null, 1, 'Grenade', null),
	('Sleep Grenade', 7, 'Deviate Large Blast; uses STR for range', 'N/A', FALSE, null, null, null, null, null, null, null, null, null, 1, 'Grenade', null), --ID 25
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

COMMIT TRANSACTION;

