package my.TNTBuilder.dao;

import my.TNTBuilder.exception.DaoException;
import my.TNTBuilder.model.Team;

public interface TeamDao {

    Team getTeamById(int teamId);
    Team createTeam(Team newTeam);
}
