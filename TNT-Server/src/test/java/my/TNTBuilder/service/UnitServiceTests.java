package my.TNTBuilder.service;

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

    @Before
    public void setup(){
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        unitDao = new JdbcUnitDao(jdbcTemplate);
        teamDao = new JdbcTeamDao(jdbcTemplate);
        sut = new UnitService(unitDao, teamDao, new UnitValidator());

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


        List<Unit> testList = sut.getUnitsForFaction(1);
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


}
