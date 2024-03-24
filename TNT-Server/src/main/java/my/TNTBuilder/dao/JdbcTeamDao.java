package my.TNTBuilder.dao;

import my.TNTBuilder.exception.DaoException;
import my.TNTBuilder.model.FactionDTO;
import my.TNTBuilder.model.Team;
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
    private final String SELECT_ALL_FROM_TEAM = "SELECT team_id, user_id, f.faction_id, team_name, money, faction_name " +
            "FROM team JOIN faction f ON f.faction_id = team.faction_id ";

    public JdbcTeamDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Team createTeam(Team newTeam) {
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
    public Team getTeamById(int teamId, int userId) {
        String sql = SELECT_ALL_FROM_TEAM + "WHERE team_id = ? AND user_id = ?";
        Team team = null;
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, teamId, userId);
            while (results.next()){
                team = mapRowToTeam(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return team;
    }

    @Override
    public List<Team> getAllTeamsForUser(int userId) {
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
            throw new DaoException("Unable to connect to server or database", e);
        }
        return allTeams;
    }

    @Override
    public List<FactionDTO> getAllFactions() {
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


    /*
    PRIVATE METHODS
     */
    private Team mapRowToTeam(SqlRowSet row){
        Team team = new Team();
        team.setId(row.getInt("team_id"));
        team.setFactionId(row.getInt("faction_id"));
        team.setFaction(row.getString("faction_name"));
        team.setUserId(row.getInt("user_id"));
        team.setName(row.getString("team_name"));
        team.setMoney(row.getInt("money"));
        //TODO deal with inventory later (new method solely focused on inventory?)
        return team;
    }

}
