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

    private JdbcTeamDao sut;

    @Before
    public void setSut(){
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcTeamDao(jdbcTemplate);
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
        Assert.assertTrue(testList.contains(TEAM_3));
    }

    @Test
    public void getAllTeamsForUser_returns_multiple_teams(){
        List<Team> testList = sut.getAllTeamsForUser(1);
        Assert.assertNotNull(testList);
        Assert.assertEquals(2, testList.size());
        Assert.assertTrue(testList.contains(TEAM_1));
        Assert.assertTrue(testList.contains(TEAM_2));
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
        Assert.assertTrue(testList.contains(faction1));
    }

    //TODO more tests about this
    @Test
    public void updateTeam_updates_team(){
        TEAM_1.setName("New Name");
        TEAM_1.setMoney(1000);
        sut.updateTeam(TEAM_1);
        Team testTeam = sut.getTeamById(TEAM_1.getId(), 1);
        Assert.assertEquals(TEAM_1, testTeam);
    }

}
