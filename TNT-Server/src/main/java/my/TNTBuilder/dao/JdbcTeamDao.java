package my.TNTBuilder.dao;

import my.TNTBuilder.exception.DaoException;
import my.TNTBuilder.model.Team;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class JdbcTeamDao implements TeamDao{

    private final JdbcTemplate jdbcTemplate;
    private final String SELECT_ALL_FROM_TEAM = "SELECT team_id, user_id, faction_id, team_name, money FROM team ";

    public JdbcTeamDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Team getTeamById(int teamId) {
        String sql = SELECT_ALL_FROM_TEAM + "WHERE team_id = ?";
        Team team = null;
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, teamId);
            while (results.next()){
                team = mapRowToTeam(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return team;
    }

    @Override
    public Team createTeam(Team newTeam) {
        String sql = "INSERT INTO team(user_id, faction_id, team_name, money) VALUES " +
                "(?, ?, ?, ?) RETURNING team_id";
        Team team = null;
        try {
            int team_id = jdbcTemplate.queryForObject(sql, Integer.class, newTeam.getUserId(), newTeam.getFactionId(),
                    newTeam.getName(), newTeam.getMoney());
            team = getTeamById(team_id);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

        return team;
    }


    private Team mapRowToTeam(SqlRowSet row){
        Team team = new Team();
        team.setId(row.getInt("team_id"));
        team.setFactionId(row.getInt("faction_id"));
        team.setUserId(row.getInt("user_id"));
        team.setName(row.getString("team_name"));
        team.setMoney(row.getInt("money"));
        //TODO deal with inventory later (new method solely focused on inventory?)
        return team;
    }

}
