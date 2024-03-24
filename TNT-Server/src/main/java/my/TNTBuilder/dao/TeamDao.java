package my.TNTBuilder.dao;

import my.TNTBuilder.exception.DaoException;
import my.TNTBuilder.model.FactionDTO;
import my.TNTBuilder.model.Team;

import java.util.List;

public interface TeamDao {

    Team getTeamById(int teamId, int userId);
    Team createTeam(Team newTeam);
    List<FactionDTO> getAllFactions();
    List<Team> getAllTeamsForUser(int userId);
}
