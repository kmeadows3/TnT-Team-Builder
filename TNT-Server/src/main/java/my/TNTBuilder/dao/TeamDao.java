package my.TNTBuilder.dao;

import my.TNTBuilder.exception.DaoException;
import my.TNTBuilder.model.FactionDTO;
import my.TNTBuilder.model.Team;

import java.util.List;

public interface TeamDao {

     /**
     * Retrieves a specific team
     * @param teamId the id of the team
     * @param userId the id of the logged-in user
     * @return the team from the database
     */
    Team getTeamById(int teamId, int userId);

    /**
     * Takes a team and inserts it into the database
     * @param newTeam the team to be added
     * @return the newly inserted team, updated with the correct ID
     */
    Team createTeam(Team newTeam);

    /**
     * Retrieves a list of all factions from the database
     * @return list of factions
     */
    List<FactionDTO> getAllFactions();

    /**
     * Get all the teams belonging to a user
     * @param userId the id of the logged-in user
     * @return the teams belonging to that user
     */
    List<Team> getAllTeamsForUser(int userId);

    /**
     * Updates a team in the database
     * @param team the updated version of the team
     */
    void updateTeam(Team team);
}
