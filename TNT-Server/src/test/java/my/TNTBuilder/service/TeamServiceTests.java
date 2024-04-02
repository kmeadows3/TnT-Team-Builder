package my.TNTBuilder.service;

import my.TNTBuilder.TeamValidator;
import my.TNTBuilder.UnitValidator;
import my.TNTBuilder.dao.*;
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
    public void updateTeam_updates_team(){

    }

}
