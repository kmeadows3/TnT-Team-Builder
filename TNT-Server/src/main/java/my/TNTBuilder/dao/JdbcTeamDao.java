package my.TNTBuilder.dao;

import my.TNTBuilder.exception.DaoException;
import my.TNTBuilder.model.FactionDTO;
import my.TNTBuilder.model.Team;
import my.TNTBuilder.service.UnitService;
import my.TNTBuilder.validator.UnitValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTeamDao implements TeamDao{

    private final JdbcTemplate jdbcTemplate;

    private final String SELECT_ALL_FROM_TEAM = "SELECT t.team_id, user_id, f.faction_id, team_name, money, " +
            "faction_name, bought_first_leader " +
            "FROM team t JOIN faction f ON f.faction_id = t.faction_id ";

    /*
    CONSTRUCTOR
     */
    public JdbcTeamDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    /*
    PUBLIC METHODS
     */

    @Override
    public Team createTeam(Team newTeam) throws DaoException {
        String sql = "INSERT INTO team(user_id, faction_id, team_name, money) VALUES " +
                "(?, ?, ?, ?) RETURNING team_id";
        Team team = null;
        try {
            int team_id = jdbcTemplate.queryForObject(sql, Integer.class, newTeam.getUserId(), newTeam.getFactionId(),
                    newTeam.getName(), newTeam.getMoney());
            team = getTeamById(team_id, newTeam.getUserId());
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

        return team;
    }

    @Override
    public Team getTeamById(int teamId, int userId) throws DaoException {
        String sql = SELECT_ALL_FROM_TEAM + "WHERE team_id = ? AND user_id = ?";
        Team team = null;
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, teamId, userId);
            while (results.next()){
                team = mapRowToTeam(results);
            }

            if (team == null) {
                throw new DaoException("Unable to retrieve team, either team does not exist or user does not own team");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return team;
    }

    @Override
    public void updateTeam(Team team) throws DaoException{
        String sql = "UPDATE team SET team_name = ?, money = ?, bought_first_leader = ? WHERE team_id = ?";
        Team updatedTeam = null;
        try {
            int rowsAffected = jdbcTemplate.update(sql, team.getName(), team.getMoney(), team.isBoughtFirstLeader(),
                    team.getId());
            if (rowsAffected != 1){
                throw new DaoException("Incorrect number of rows affected");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Invalid data provided, cannot update unit", e);
        }
    }

    @Override
    public List<Team> getAllTeamsForUser(int userId) throws DaoException {
        List<Team> allTeams = new ArrayList<>();
        String sql = SELECT_ALL_FROM_TEAM + "WHERE user_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
            while (results.next()){
                Team team = mapRowToTeam(results);
                allTeams.add(team);
            }
            if (allTeams.isEmpty()){
                throw new DaoException("User has no saved teams");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database", e);
        }
        return allTeams;
    }

    @Override
    public List<FactionDTO> getAllFactions() throws DaoException {
        List<FactionDTO> allFactions = new ArrayList<>();
        String sql = "SELECT faction_id, faction_name FROM faction WHERE faction_name != 'Freelancers' ORDER BY faction_id";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                FactionDTO currentFaction = new FactionDTO();
                currentFaction.setFactionId(results.getInt("faction_id"));
                currentFaction.setFactionName(results.getString("faction_name"));
                allFactions.add(currentFaction);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return allFactions;
    }

    @Override
    public Team getTeamByUnitId(int unitId) throws DaoException {
        String sql = SELECT_ALL_FROM_TEAM + "JOIN unit u ON u.team_id = t.team_id WHERE unit_id = ?";

        Team team = null;
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, unitId);
            while (results.next()){
                team = mapRowToTeam(results);
            }

            if (team == null) {
                throw new DaoException("Unable to retrieve team.");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }

        return team;
    }

    @Override
    public void deleteTeam(int teamId) throws DaoException {
        try {
            String sql = "DELETE FROM team WHERE team_id = ?";
            int rowsDeleted = jdbcTemplate.update(sql, teamId);
            if (rowsDeleted != 1){
                throw new DaoException("Incorrect number of teams deleted");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }


    /*
    PRIVATE METHODS
     */

    /**
     * Converts a SqlRowSet into a Team
     * @param row the SqlRowSet from the database
     * @return The team extracted from the SqlRowSet
     */
    private Team mapRowToTeam(SqlRowSet row) throws DaoException{
        Team team = new Team();
        team.setId(row.getInt("team_id"));
        team.setFactionId(row.getInt("faction_id"));
        team.setFaction(row.getString("faction_name"));
        team.setUserId(row.getInt("user_id"));
        team.setName(row.getString("team_name"));
        team.setMoney(row.getInt("money"));
        team.setBoughtFirstLeader(row.getBoolean("bought_first_leader"));

        UnitDao unitDao = new JdbcUnitDao(jdbcTemplate);
        team.setUnitList(unitDao.getAllUnitsForTeam(team.getId()));

        ItemDao itemDao = new JdbcItemDao(jdbcTemplate);
        team.setInventory(itemDao.getAllItemsForTeam(team.getId()));
        return team;
    }

}
