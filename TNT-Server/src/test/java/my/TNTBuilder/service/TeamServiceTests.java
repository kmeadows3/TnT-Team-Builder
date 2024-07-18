package my.TNTBuilder.service;

import my.TNTBuilder.exception.DaoException;
import my.TNTBuilder.exception.ServiceException;
import my.TNTBuilder.model.FactionDTO;
import my.TNTBuilder.model.Team;
import my.TNTBuilder.model.Unit;
import my.TNTBuilder.model.inventory.Item;
import my.TNTBuilder.validator.TeamValidator;
import my.TNTBuilder.dao.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class TeamServiceTests extends BaseDaoTests {

    private TeamService sut;

    @Before
    public void setup(){
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        TeamDao teamDao = new JdbcTeamDao(jdbcTemplate);
        sut = new TeamService(teamDao, new TeamValidator(), jdbcTemplate);
        TEAM_1.setMoney(500);

    }

    @Test
    public void updateTeam_updates_team_with_valid_change() throws ServiceException{
        TEAM_1.setName("Test Name");
        Team testTeam = sut.updateTeam(TEAM_1, 1);
        Assert.assertEquals(TEAM_1, testTeam);
    }

    @Test (expected = ServiceException.class)
    public void updateTeam_throws_exception_with_invalid_change() throws ServiceException{
        TEAM_1.setFactionId(3);
        sut.updateTeam(TEAM_1, 1);
        Assert.fail();
    }

    @Test (expected = ServiceException.class)
    public void updateTeam_throws_exception_with_invalid_user() throws ServiceException{
        TEAM_1.setName("Test Name");
        sut.updateTeam(TEAM_1, 2);
        Assert.fail();
    }

    @Test
    public void getTeamById_returns_expected_team() throws ServiceException{
        Team testTeam = sut.getTeamById(1,1);
        Assert.assertNotNull(testTeam);
        Assert.assertEquals(TEAM_1, testTeam);
    }

    @Test (expected = ServiceException.class)
    public void getTeamById_throws_exception_for_incorrect_user() throws ServiceException{
        sut.getTeamById(1,2);
        Assert.fail();
    }

    @Test (expected = ServiceException.class)
    public void getTeamById_throws_exception_if_team_does_not_exist() throws ServiceException{
        sut.getTeamById(41,1);
        Assert.fail();
    }

    @Test
    public void getTeamByUnitId_returns_correct_team() throws ServiceException{
        Team testTeam = sut.getTeamByUnitId(1);
        Assert.assertNotNull(testTeam);
        Assert.assertEquals(TEAM_1, testTeam);
    }

    @Test (expected = ServiceException.class)
    public void getTeamByUnitId_throws_exception_with_invalid_unit() throws ServiceException{
        sut.getTeamByUnitId(99);
        Assert.fail();
    }

    @Test
    public void spendMoney_spends_money_on_valid_purchase_amount() throws ServiceException{
        sut.spendMoney(50, TEAM_1);
        Assert.assertEquals(450, TEAM_1.getMoney());
    }

    @Test
    public void spendMoney_spends_money_correctly_multiple_purcahses() throws ServiceException{
        sut.spendMoney(50, TEAM_1);
        sut.spendMoney(300, TEAM_1);
        Assert.assertEquals(150, TEAM_1.getMoney());
    }

    @Test (expected = ServiceException.class)
    public void spendMoney_throws_exception_if_not_enough_money_for_purchase() throws ServiceException{
        sut.spendMoney(5000, TEAM_1);
        Assert.fail();
    }

    @Test
    public void updateTeamAfterPurchase_correctly_updates_with_non_leader() throws ServiceException{
        Unit boughtUnit = new Unit(5, 1, "Name", "Defender", "Rank and File",
                "Human", 50,1,6,5,5,4,4,5,0,
                "N/A",0,0,0,0,
                new ArrayList<>(), new ArrayList<>(),  new ArrayList<>(), new ArrayList<>());

        sut.updateTeamAfterNewUnitPurchase(1, boughtUnit);
        Team testTeam = sut.getTeamById(1, 1);
        Assert.assertEquals(450, testTeam.getMoney());
        Assert.assertFalse(testTeam.isBoughtFirstLeader());
    }

    @Test
    public void updateTeamAfterPurchase_correctly_updates_with_buying_first_leader() throws ServiceException{
        Unit boughtUnit = new Unit(5, 1, "Name", "Defender", "Leader",
                "Human", 50,1,6,5,5,4,4,5,0,
                "N/A",0,0,0,0,
                new ArrayList<>(), new ArrayList<>(),  new ArrayList<>(), new ArrayList<>());

        sut.updateTeamAfterNewUnitPurchase(1, boughtUnit);
        Team testTeam = sut.getTeamById(1, 1);
        Assert.assertEquals(450, testTeam.getMoney());
        Assert.assertTrue(testTeam.isBoughtFirstLeader());
    }

    @Test
    public void updateTeamAfterPurchase_correctly_discounts_price_with_buying_second_leader() throws ServiceException{
        Team team1 = sut.getTeamById(1,1);
        team1.setBoughtFirstLeader(true);
        sut.updateTeam(team1,1);

        Unit boughtUnit = new Unit(5, 1, "Name", "Defender", "Leader",
                "Human", 50,1,6,5,5,4,4,5,0,
                "N/A",0,0,0,0,
                new ArrayList<>(), new ArrayList<>(),  new ArrayList<>(), new ArrayList<>());


        sut.updateTeamAfterNewUnitPurchase(1, boughtUnit);
        Team testTeam = sut.getTeamById(1, 1);
        Assert.assertEquals(475, testTeam.getMoney());
        Assert.assertTrue(testTeam.isBoughtFirstLeader());
    }

    @Test
    public void create_team_creates_new_empty_team() throws ServiceException{
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

    @Test(expected = ServiceException.class)
    public void create_team_invalid_faction_throws_exception() throws ServiceException{
        sut.createTeam(new Team(4, 1, "Name", "Mutants", 99, 500, new ArrayList<>(), new ArrayList<>()));
        Assert.fail();
    }

    @Test(expected = ServiceException.class)
    public void create_team_invalid_user_throws_exception() throws ServiceException{
        sut.createTeam(new Team(4, 99, "Name", "Caravanners", 1, 500, new ArrayList<>(), new ArrayList<>()));
        Assert.fail();
    }

    @Test
    public void getAllFactions_returns_all_factions() throws ServiceException{
        List<FactionDTO> testList = sut.getAllFactions();
        Assert.assertNotNull(testList);
        Assert.assertEquals(6, testList.size());
        Assert.assertTrue(testList.contains(faction1));
    }

    @Test
    public void getAllTeamsForUser_returns_team() throws ServiceException{
        List<Team> testList = sut.getAllTeamsForUser(2);
        Assert.assertNotNull(testList);
        Assert.assertEquals(1, testList.size());
        Assert.assertTrue(testList.contains(TEAM_3));
    }

    @Test
    public void getAllTeamsForUser_returns_multiple_teams() throws ServiceException{
        List<Team> testList = sut.getAllTeamsForUser(1);
        Assert.assertNotNull(testList);
        Assert.assertEquals(2, testList.size());
        Assert.assertTrue(testList.contains(TEAM_1));
        Assert.assertTrue(testList.contains(TEAM_2));
    }

    @Test(expected = ServiceException.class)
    public void getAllTeamsForUser_throws_exception_if_no_teams() throws ServiceException{
        sut.getAllTeamsForUser(3);
        Assert.fail();
    }

    @Test
    public void deleteTeams_deletes_team_no_units_or_items() throws ServiceException {
        sut.deleteTeam(6, 4);
        List<Team> teamList = sut.getAllTeamsForUser(4);
        Assert.assertEquals(3, teamList.size());
        for (Team team : teamList){
            Assert.assertNotEquals(6, team.getId());
        }
    }

    @Test
    public void deleteTeams_deletes_team_both_units_and_items() throws ServiceException {
        sut.deleteTeam(1, 1);
        List<Team> teamList = sut.getAllTeamsForUser(1);
        Assert.assertEquals(1, teamList.size());
        for (Team team : teamList){
            Assert.assertNotEquals(1, team.getId());
        }
    }

    @Test
    public void deleteTeams_deletes_team_no_units_only_items() throws ServiceException {
        sut.deleteTeam(2, 1);
        List<Team> teamList = sut.getAllTeamsForUser(1);
        Assert.assertEquals(1, teamList.size());
        for (Team team : teamList){
            Assert.assertNotEquals(2, team.getId());
        }
    }

    @Test
    public void deleteTeams_deletes_team_no_items_only_units() throws ServiceException {
        sut.deleteTeam(7, 4);
        List<Team> teamList = sut.getAllTeamsForUser(4);
        Assert.assertEquals(3, teamList.size());
        for (Team team : teamList){
            Assert.assertNotEquals(7, team.getId());
        }
    }

    @Test (expected = ServiceException.class)
    public void deleteTeams_throws_exception_user_does_not_own_team() throws ServiceException {
        sut.deleteTeam(1, 4);
        Assert.fail();

    }
}
