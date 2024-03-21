package my.TNTBuilder.dao;

import my.TNTBuilder.exception.DaoException;
import my.TNTBuilder.model.Team;
import my.TNTBuilder.model.Unit;
import my.TNTBuilder.model.inventory.Item;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;

public class JdbcTeamDaoTests extends BaseDaoTests{
    protected final Team team1 = new Team(1, 1, "Team 1", 1, 500, new ArrayList<Unit>(), new ArrayList<Item>());
    protected final Team team2 = new Team(1, 1, "Team 2", 3, 1500, new ArrayList<Unit>(), new ArrayList<Item>());
    protected final Team team3 = new Team(1, 2, "Team 3", 2, 1000, new ArrayList<Unit>(), new ArrayList<Item>());

    private JdbcTeamDao sut;

    @Before
    public void setSut(){
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcTeamDao(jdbcTemplate);
    }

    @Test
    public void get_team_by_id_returns_correct_team(){
        Team testTeam = sut.getTeamById(1);
        Assert.assertNotNull(testTeam);
        Assert.assertEquals(team1, testTeam);
    }

    @Test
    public void get_team_by_id_returns_null_with_invalid_id(){
        Team testTeam = sut.getTeamById(99);
        Assert.assertNull(testTeam);
    }

    @Test
    public void create_team_creates_new_empty_team(){
        Team newTeam = new Team();
        newTeam.setUserId(1);
        newTeam.setFactionId(1);
        newTeam.setName("New Team");
        newTeam.setMoney(500);
        Team returnedTeam = sut.createTeam(newTeam);
        Team teamFromDatabase = sut.getTeamById(4);
        Assert.assertEquals(0, teamFromDatabase.getInventory().size());
        Assert.assertEquals(0, teamFromDatabase.getUnitList().size());
        Assert.assertEquals(returnedTeam, teamFromDatabase);
    }

    @Test(expected = DaoException.class)
    public void create_team_invalid_faction_throws_exception(){
        sut.createTeam(new Team(4, 1, "Name", 99, 500, new ArrayList<Unit>(), new ArrayList<Item>()));
    }

    @Test(expected = DaoException.class)
    public void create_team_invalid_user_throws_exception(){
        sut.createTeam(new Team(4, 99, "Name", 1, 500, new ArrayList<Unit>(), new ArrayList<Item>()));
    }

}
