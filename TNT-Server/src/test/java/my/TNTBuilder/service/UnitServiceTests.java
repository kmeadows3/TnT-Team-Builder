package my.TNTBuilder.service;

import my.TNTBuilder.model.Team;
import my.TNTBuilder.validator.TeamValidator;
import my.TNTBuilder.validator.UnitValidator;
import my.TNTBuilder.dao.*;
import my.TNTBuilder.exception.ServiceException;
import my.TNTBuilder.model.Skill;
import my.TNTBuilder.model.Skillset;
import my.TNTBuilder.model.Unit;
import my.TNTBuilder.model.inventory.Item;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class UnitServiceTests extends BaseDaoTests {
    private UnitService sut;
    private UnitDao unitDao;
    private TeamDao teamDao;
    private UserDao userDao;

    @Before
    public void setup(){
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        unitDao = new JdbcUnitDao(jdbcTemplate);
        teamDao = new JdbcTeamDao(jdbcTemplate);
        userDao = new JdbcUserDao(jdbcTemplate);
        sut = new UnitService(unitDao, new UnitValidator(), new TeamService(teamDao, userDao, new TeamValidator()));

    }

    @Test
    public void createNewUnit_creates_new_unit(){
        Unit expectedUnit = new Unit(4, 1, "Name", "Defender", "Rank and File",
                "Human", 23,1,6,5,5,4,4,5,0,
                "N/A",0,0,0,0,
                new ArrayList<Skillset>(), new ArrayList<Skill>(), new ArrayList<Item>());
        expectedUnit.getAvailableSkillsets().add(new Skillset(1, "Melee", "Skill"));
        expectedUnit.getAvailableSkillsets().add(new Skillset(2, "Marksmanship", "Skill"));
        expectedUnit.getAvailableSkillsets().add(new Skillset(3, "Survival", "Skill"));
        expectedUnit.getSkills().add(new Skill(5, "Brave", "+2 bonus when making Will tests.",
                7, "Tenacity"));
        Unit newUnit = sut.createNewUnit(expectedUnit, 1);
        expectedUnit.setId(newUnit.getId());
        Unit testUnit = unitDao.getUnitById(expectedUnit.getId(), 1);
        Assert.assertNotNull(testUnit);
        Assert.assertEquals(expectedUnit, testUnit);
    }

    //TODO fix me, created before extra validation added
    @Test
    public void getUnitsForFaction_returns_list_correctly(){
        Unit expectedUnit = new Unit(4, 0, "", "Defender", "Rank and File",
                "Human", 23,1,6,5,5,4,4,5,0,
                "N/A",0,0,0,0,
                new ArrayList<Skillset>(), new ArrayList<Skill>(), new ArrayList<Item>());
        expectedUnit.getAvailableSkillsets().add(new Skillset(1, "Melee", "Skill"));
        expectedUnit.getAvailableSkillsets().add(new Skillset(2, "Marksmanship", "Skill"));
        expectedUnit.getAvailableSkillsets().add(new Skillset(3, "Survival", "Skill"));
        expectedUnit.getSkills().add(new Skill(5, "Brave", "+2 bonus when making Will tests.",
                7, "Tenacity"));


        List<Unit> testList = sut.getUnitsForFaction(1, new Team());
        Assert.assertNotNull(testList);
        Assert.assertEquals(5, testList.size());
        Assert.assertEquals(expectedUnit, testList.get(3));
        Assert.assertEquals(10, testList.get(4).getId());
    }

    @Test (expected = ServiceException.class)
    public void createNewUnit_throws_exception_with_invalid_unit(){
        Unit invalidUnit = new Unit();
        invalidUnit.setId(1);
        invalidUnit.setTeamId(2);
        sut.createNewUnit(invalidUnit, 1);
        Assert.fail();
    }

    @Test (expected = ServiceException.class)
    public void createNewUnit_throws_exception_if_team_does_not_belong_to_user(){
        Unit invalidUnit = new Unit();
        invalidUnit.setId(9);
        invalidUnit.setTeamId(3);
        sut.createNewUnit(invalidUnit, 1);
        Assert.fail();
    }

    @Test
    public void createNewUnit_does_not_throw_exception_with_freelancer_units(){
        try {
            Unit invalidUnit = new Unit();
            invalidUnit.setId(10);
            invalidUnit.setTeamId(1);
            sut.createNewUnit(invalidUnit, 1);
        } catch (ServiceException e){
            Assert.fail();
        }
    }


    @Test
    public void getPotentialSkills_returns_correct_list_one_skillset(){
        Skill skill1 = new Skill(6, "Brute", "Gain +1 to Strength Stat when making Melee attacks. " +
                "Ignore heavy weapons rule.", 6, "Brawn");
        Skill skill2 = new Skill(7, "Bully", "All enemies defeated by this model in close combat are knocked prone " +
                "in addition to any other combat result.", 6, "Brawn");

        List<Skill> skillList = sut.getPotentialSkills(2);

        Assert.assertEquals(2, skillList.size());
        Assert.assertTrue(skillList.contains(skill1));
        Assert.assertTrue(skillList.contains(skill2));

    }

    @Test
    public void getPotentialSkills_returns_correct_list_multiple_skillsets(){
        Skill skill1 = new Skill(3, "Reconnoiter", "At the start of the game after all models have " +
                "deployed but before init is determined make a free move action.", 4, "Quickness");
        Skill skill2 = new Skill(4, "Trekker", "When moving through Difficult Terrain attempt an " +
                "Agility test (MET/TN 10) for free. On pass move through terrain without movement penalty.",
                3, "Survival");

        List<Skill> skillList = sut.getPotentialSkills(1);

        Assert.assertEquals(2, skillList.size());
        Assert.assertTrue(skillList.contains(skill1));
        Assert.assertTrue(skillList.contains(skill2));

    }

    @Test
    public void getPotentialSkills_does_not_return_existing_skills(){
        Skill skill1 = new Skill(6, "Brute", "Gain +1 to Strength Stat when making Melee attacks. " +
                "Ignore heavy weapons rule.", 6, "Brawn");
        Skill skill2 = new Skill(7, "Bully", "All enemies defeated by this model in close combat are knocked prone " +
                "in addition to any other combat result.", 6, "Brawn");

        List<Skill> skillList = sut.getPotentialSkills(3);

        Assert.assertEquals(1, skillList.size());
        Assert.assertTrue(skillList.contains(skill1));
        Assert.assertFalse(skillList.contains(skill2));

    }

    @Test
    public void addSkillToUnit_adds_skill_to_unit(){
        Skill skill1 = new Skill(6, "Brute", "Gain +1 to Strength Stat when making Melee attacks. " +
                "Ignore heavy weapons rule.", 6, "Brawn");
        sut.addSkillToUnit(6, 2, 2);
        Unit testUnit = unitDao.getUnitById(2, 2);
        Assert.assertEquals(1, testUnit.getSkills().size());
        Assert.assertTrue(testUnit.getSkills().contains(skill1));
    }

    @Test (expected = ServiceException.class)
    public void addSkillToUnit_throws_exception_if_user_does_not_own_unit(){
        Skill skill1 = new Skill(6, "Brute", "Gain +1 to Strength Stat when making Melee attacks. " +
                "Ignore heavy weapons rule.", 6, "Brawn");
        sut.addSkillToUnit(6, 2, 1);
        Assert.fail();
    }

    @Test (expected = ServiceException.class)
    public void addSkillToUnit_throws_exception_if_unit_cannot_have_skill(){
        Skill skill1 = new Skill(3, "Reconnoiter", "At the start of the game after all models have " +
                "deployed but before init is determined make a free move action.", 4, "Quickness");
        sut.addSkillToUnit(3, 2, 2);
        Assert.fail();
    }

    @Test
    public void getUnitById_returns_correct_unit(){
        Unit testUnit = sut.getUnitById(1,1);
        Assert.assertNotNull(testUnit);
        Assert.assertEquals(UNIT1, testUnit);
    }

    @Test (expected = ServiceException.class)
    public void getUnitById_throws_exception_incorrect_user(){
        Unit testUnit = sut.getUnitById(1,2);
        Assert.fail();
    }

}
