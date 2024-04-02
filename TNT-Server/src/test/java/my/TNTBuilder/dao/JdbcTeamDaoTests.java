package my.TNTBuilder.dao;

import my.TNTBuilder.exception.DaoException;
import my.TNTBuilder.model.FactionDTO;
import my.TNTBuilder.model.Team;
import my.TNTBuilder.model.Unit;
import my.TNTBuilder.model.inventory.Item;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class JdbcTeamDaoTests extends BaseDaoTests{
    protected final Team TEAM_1 = new Team(1, 1, "Team 1", "Caravanners", 1, 500, new ArrayList<Unit>(), new ArrayList<Item>());
    protected final Team TEAM_2 = new Team(2, 1, "Team 2", "Raiders", 3, 1500, new ArrayList<Unit>(), new ArrayList<Item>());
    protected final Team TEAM_3 = new Team(3, 2, "Team 3", "Mutants", 2, 1000, new ArrayList<Unit>(), new ArrayList<Item>());
    protected final FactionDTO faction1 = new FactionDTO(1, "Caravanners");
    protected final FactionDTO faction2 = new FactionDTO(2, "Mutants");

    private JdbcTeamDao sut;

    @Before
    public void setSut(){
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcTeamDao(jdbcTemplate);
        TEAM_1.getUnitList().add(JdbcUnitDaoTests.UNIT1);
        TEAM_1.getUnitList().add(JdbcUnitDaoTests.UNIT3);
        TEAM_3.getUnitList().add(JdbcUnitDaoTests.UNIT2);
    }

    @Test
    public void get_team_by_id_returns_correct_team(){
        Team testTeam = sut.getTeamById(1, 1);
        Assert.assertNotNull(testTeam);
        Assert.assertEquals(TEAM_1, testTeam);
    }

    @Test
    public void get_team_by_id_returns_null_with_invalid_id(){
        Team testTeam = sut.getTeamById(99, 1);
        Assert.assertNull(testTeam);
    }

    @Test
    public void create_team_creates_new_empty_team(){
        Team newTeam = new Team();
        newTeam.setUserId(1);
        newTeam.setFactionId(1);
        newTeam.setFaction("Caravanners");
        newTeam.setName("New Team");
        newTeam.setMoney(500);
        Team returnedTeam = sut.createTeam(newTeam);
        newTeam.setId(returnedTeam.getId());
        Team teamFromDatabase = sut.getTeamById(newTeam.getId(), 1);
        Assert.assertEquals(0, teamFromDatabase.getInventory().size());
        Assert.assertEquals(0, teamFromDatabase.getUnitList().size());
        Assert.assertEquals(newTeam, teamFromDatabase);
    }

    @Test(expected = DaoException.class)
    public void create_team_invalid_faction_throws_exception(){
        sut.createTeam(new Team(4, 1, "Name", "Mutants", 99, 500, new ArrayList<Unit>(), new ArrayList<Item>()));
        Assert.fail();
    }

    @Test(expected = DaoException.class)
    public void create_team_invalid_user_throws_exception(){
        sut.createTeam(new Team(4, 99, "Name", "Caravanners", 1, 500, new ArrayList<Unit>(), new ArrayList<Item>()));
        Assert.fail();
    }

    @Test
    public void getAllTeamsForUser_returns_team(){
        List<Team> testList = sut.getAllTeamsForUser(2);
        Assert.assertNotNull(testList);
        Assert.assertEquals(1, testList.size());
        Assert.assertEquals(TEAM_3, testList.get(0));
    }

    @Test
    public void getAllTeamsForUser_returns_multiple_teams(){
        List<Team> testList = sut.getAllTeamsForUser(1);
        Assert.assertNotNull(testList);
        Assert.assertEquals(2, testList.size());
        Assert.assertEquals(TEAM_1, testList.get(0));
        Assert.assertEquals(TEAM_2, testList.get(1));
    }

    @Test(expected = DaoException.class)
    public void getAllTeamsForUser_throws_exception_if_no_teams(){
        sut.getAllTeamsForUser(3);
        Assert.fail();
    }

    @Test
    public void getAllFactions_returns_all_factions(){
        List<FactionDTO> testList = sut.getAllFactions();
        Assert.assertNotNull(testList);
        Assert.assertEquals(6, testList.size());
        Assert.assertEquals(faction1, testList.get(0));
    }

}
