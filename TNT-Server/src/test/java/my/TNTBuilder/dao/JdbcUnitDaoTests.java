package my.TNTBuilder.dao;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import my.TNTBuilder.exception.DaoException;
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

public class JdbcUnitDaoTests extends BaseDaoTests{
    protected final Unit UNIT1 = new Unit(1, 1, "UnitName1", "Trade Master", "Leader",
            "Human", 50,10,5,7,6,8,6,5,0,
            "Special rules description",100,0,0,0,
            new ArrayList<Skillset>(), new ArrayList<Skill>(), new ArrayList<Item>());
    protected final Unit UNIT2 = new Unit(2, 3, "UnitName2", "Soldier", "Elite",
            "Mutant", 51,11,6,8,7,9,7,6,1,
            "Special rules description",50,0,0,0,
            new ArrayList<Skillset>(), new ArrayList<Skill>(), new ArrayList<Item>());

    private JdbcUnitDao sut;

    @Before
    public void setup(){
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcUnitDao(jdbcTemplate);
    }

    @Test
    public void getUnitById_returns_correct_unit(){
        Unit testUnit = sut.getUnitById(1,1);
        Assert.assertNotNull(testUnit);
        Assert.assertEquals(UNIT1, testUnit);
    }

    @Test (expected = DaoException.class)
    public void getUnitById_throws_exception_with_wrong_userId(){
        sut.getUnitById(1,2);
        Assert.fail();
    }

    @Test (expected = DaoException.class)
    public void getUnitById_throws_exception_with_invalid_id(){
        sut.getUnitById(99,1);
        Assert.fail();
    }

    @Test
    public void addUnitToDatabase_adds_unit(){
        Unit expectedUnit = new Unit(4, 2, "Name", "Defender", "Rank and File",
                "Human", 23,1,6,5,5,4,4,5,0,
                "N/A",0,0,0,0,
                new ArrayList<Skillset>(), new ArrayList<Skill>(), new ArrayList<Item>());
        expectedUnit.getAvailableSkillsets().add(new Skillset(1, "Melee", "Skill"));
        expectedUnit.getAvailableSkillsets().add(new Skillset(2, "Marksmanship", "Skill"));
        expectedUnit.getAvailableSkillsets().add(new Skillset(3, "Survival", "Skill"));
        expectedUnit.getSkills().add(new Skill(5, "Brave", "+2 bonus when making Will tests.",
                7, "Tenacity"));

        Unit newUnit = sut.createUnit(expectedUnit);
        expectedUnit.setId(newUnit.getId());
        Unit testUnit = sut.getUnitById(newUnit.getId(), 1);
        Assert.assertNotNull(testUnit);
        Assert.assertEquals(expectedUnit, testUnit);

    }

    @Test
    public void getFactionIdByUnitId_returns_faction_id(){
        int testFactionId = sut.getFactionIdByUnitId(UNIT1.getId());
        Assert.assertEquals(1, testFactionId);
    }

    @Test(expected = DaoException.class)
    public void getFactionIdByUnitId_throws_exception_on_invalid_unit(){
        sut.getFactionIdByUnitId(50);
        Assert.fail();
    }

    @Test
    public void getListOfUnitsByFactionId_returns_correct_list(){
        Unit expectedUnit = new Unit(4, 0, "", "Defender", "Rank and File",
                "Human", 23,1,6,5,5,4,4,5,0,
                "N/A",0,0,0,0,
                new ArrayList<Skillset>(), new ArrayList<Skill>(), new ArrayList<Item>());
        expectedUnit.getAvailableSkillsets().add(new Skillset(1, "Melee", "Skill"));
        expectedUnit.getAvailableSkillsets().add(new Skillset(2, "Marksmanship", "Skill"));
        expectedUnit.getAvailableSkillsets().add(new Skillset(3, "Survival", "Skill"));
        expectedUnit.getSkills().add(new Skill(5, "Brave", "+2 bonus when making Will tests.",
                7, "Tenacity"));


        List<Unit> testList = sut.getListOfUnitsByFactionId(1);
        Assert.assertNotNull(testList);
        Assert.assertEquals(5, testList.size());
        Assert.assertEquals(expectedUnit, testList.get(3));
        Assert.assertEquals(10, testList.get(4).getId());
    }

}
