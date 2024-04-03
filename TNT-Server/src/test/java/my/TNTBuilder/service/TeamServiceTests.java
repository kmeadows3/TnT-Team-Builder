package my.TNTBuilder.service;

import my.TNTBuilder.exception.ServiceException;
import my.TNTBuilder.model.Team;
import my.TNTBuilder.validator.TeamValidator;
import my.TNTBuilder.dao.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

public class TeamServiceTests extends BaseDaoTests {

    private TeamService sut;
    private UserDao userDao;
    private TeamDao teamDao;

    @Before
    public void setup(){
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        userDao = new JdbcUserDao(jdbcTemplate);
        teamDao = new JdbcTeamDao(jdbcTemplate);
        sut = new TeamService(teamDao, userDao, new TeamValidator());

    }

    @Test
    public void updateTeam_updates_team_with_valid_change(){
        TEAM_1.setName("Test Name");
        Team testTeam = sut.updateTeam(TEAM_1, "user1");
        Assert.assertEquals(TEAM_1, testTeam);
    }

    @Test (expected = ServiceException.class)
    public void updateTeam_throws_exception_with_invalid_change(){
        TEAM_1.setFactionId(3);
        sut.updateTeam(TEAM_1, "user1");
        Assert.fail();
    }

    @Test (expected = ServiceException.class)
    public void updateTeam_throws_exception_with_invalid_user(){
        TEAM_1.setName("Test Name");
        sut.updateTeam(TEAM_1, "user2");
        Assert.fail();
    }

}
