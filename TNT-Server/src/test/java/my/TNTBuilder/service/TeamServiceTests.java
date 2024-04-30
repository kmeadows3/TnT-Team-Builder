package my.TNTBuilder.service;

import my.TNTBuilder.exception.ServiceException;
import my.TNTBuilder.model.Team;
import my.TNTBuilder.model.Unit;
import my.TNTBuilder.validator.TeamValidator;
import my.TNTBuilder.dao.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;

public class TeamServiceTests extends BaseDaoTests {

    private TeamService sut;

    @Before
    public void setup(){
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        TeamDao teamDao = new JdbcTeamDao(jdbcTemplate);
        sut = new TeamService(teamDao, new TeamValidator());

    }

    @Test
    public void updateTeam_updates_team_with_valid_change(){
        TEAM_1.setName("Test Name");
        Team testTeam = sut.updateTeam(TEAM_1, 1);
        Assert.assertEquals(TEAM_1, testTeam);
    }

    @Test (expected = ServiceException.class)
    public void updateTeam_throws_exception_with_invalid_change(){
        TEAM_1.setFactionId(3);
        sut.updateTeam(TEAM_1, 1);
        Assert.fail();
    }

    @Test (expected = ServiceException.class)
    public void updateTeam_throws_exception_with_invalid_user(){
        TEAM_1.setName("Test Name");
        sut.updateTeam(TEAM_1, 2);
        Assert.fail();
    }

    @Test
    public void getTeamById_returns_expected_team(){
        Team testTeam = sut.getTeamById(1,1);
        Assert.assertNotNull(testTeam);
        Assert.assertEquals(TEAM_1, testTeam);
    }

    @Test (expected = ServiceException.class)
    public void getTeamById_throws_exception_for_incorrect_user(){
        sut.getTeamById(1,2);
        Assert.fail();
    }

    @Test (expected = ServiceException.class)
    public void getTeamById_throws_exception_if_team_does_not_exist(){
        sut.getTeamById(41,1);
        Assert.fail();
    }

    @Test
    public void updateTeamAfterPurchase_correctly_updates_with_non_leader(){
        Unit boughtUnit = new Unit(5, 1, "Name", "Defender", "Rank and File",
                "Human", 50,1,6,5,5,4,4,5,0,
                "N/A",0,0,0,0,
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        sut.updateTeamAfterNewUnitPurchase(1, boughtUnit);
        Team testTeam = sut.getTeamById(1, 1);
        Assert.assertEquals(450, testTeam.getMoney());
        Assert.assertFalse(testTeam.isBoughtFirstLeader());
    }

    @Test
    public void updateTeamAfterPurchase_correctly_updates_with_buying_first_leader(){
        Unit boughtUnit = new Unit(5, 1, "Name", "Defender", "Leader",
                "Human", 50,1,6,5,5,4,4,5,0,
                "N/A",0,0,0,0,
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        sut.updateTeamAfterNewUnitPurchase(1, boughtUnit);
        Team testTeam = sut.getTeamById(1, 1);
        Assert.assertEquals(450, testTeam.getMoney());
        Assert.assertTrue(testTeam.isBoughtFirstLeader());
    }

    @Test
    public void updateTeamAfterPurchase_correctly_discounts_price_with_buying_second_leader(){
        Team team1 = sut.getTeamById(1,1);
        team1.setBoughtFirstLeader(true);
        sut.updateTeam(team1,1);

        Unit boughtUnit = new Unit(5, 1, "Name", "Defender", "Leader",
                "Human", 50,1,6,5,5,4,4,5,0,
                "N/A",0,0,0,0,
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>());


        sut.updateTeamAfterNewUnitPurchase(1, boughtUnit);
        Team testTeam = sut.getTeamById(1, 1);
        Assert.assertEquals(475, testTeam.getMoney());
        Assert.assertTrue(testTeam.isBoughtFirstLeader());
    }

}
