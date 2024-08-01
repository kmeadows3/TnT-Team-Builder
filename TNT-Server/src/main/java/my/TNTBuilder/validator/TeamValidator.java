package my.TNTBuilder.validator;

import my.TNTBuilder.model.Team;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;

@Component
public class TeamValidator {
    public boolean validNameChange (Team team, Team updatedTeam){
        if (!(team.getName().equals(updatedTeam.getName()))){
            return updatedTeam.getId() == team.getId() && updatedTeam.getUserId() == team.getUserId()
                    && updatedTeam.getFactionId() == team.getFactionId() && updatedTeam.getMoney() == team.getMoney()
                    && updatedTeam.isBoughtFirstLeader() == team.isBoughtFirstLeader()
                    && Objects.equals(updatedTeam.getFaction(), team.getFaction())
                    && Objects.equals(updatedTeam.getUnitList(), team.getUnitList())
                    && new HashSet<>(updatedTeam.getInventory()).containsAll(team.getInventory())
                    && new HashSet<>(team.getInventory()).containsAll(updatedTeam.getInventory());
        }
        return false;
    }

    public boolean validMoneyChange (Team team, Team updatedTeam){
        if (updatedTeam.getMoney() != team.getMoney() && updatedTeam.getMoney() >= 0 ){
            return updatedTeam.getId() == team.getId() && updatedTeam.getUserId() == team.getUserId()
                    && updatedTeam.getFactionId() == team.getFactionId()
                    && updatedTeam.isBoughtFirstLeader() == team.isBoughtFirstLeader()
                    && Objects.equals(updatedTeam.getFaction(), team.getFaction())
                    && Objects.equals(updatedTeam.getName(), team.getName())
                    && Objects.equals(updatedTeam.getUnitList(), team.getUnitList())
                    && new HashSet<>(updatedTeam.getInventory()).containsAll(team.getInventory())
                    && new HashSet<>(team.getInventory()).containsAll(updatedTeam.getInventory());
        }
        return false;
    }

    public boolean validFirstLeaderChange (Team team, Team updatedTeam){
        if (updatedTeam.isBoughtFirstLeader() != team.isBoughtFirstLeader() ){
            return updatedTeam.getId() == team.getId() && updatedTeam.getUserId() == team.getUserId()
                    && updatedTeam.getFactionId() == team.getFactionId()
                    && updatedTeam.getMoney() == team.getMoney()
                    && Objects.equals(updatedTeam.getFaction(), team.getFaction())
                    && Objects.equals(updatedTeam.getName(), team.getName())
                    && Objects.equals(updatedTeam.getUnitList(), team.getUnitList())
                    && new HashSet<>(updatedTeam.getInventory()).containsAll(team.getInventory())
                    && new HashSet<>(team.getInventory()).containsAll(updatedTeam.getInventory());
        }
        return false;
    }


    /* Everything is the same
            return updatedTeam.getId() == team.getId() && updatedTeam.getUserId() == team.getUserId()
                    && updatedTeam.getFactionId() == team.getFactionId()
                    && updatedTeam.getMoney() == team.getMoney()
                    && updatedTeam.isBoughtFirstLeader() == team.isBoughtFirstLeader()
                    && Objects.equals(updatedTeam.getFaction(), team.getFaction())
                    && Objects.equals(updatedTeam.getName(), team.getName())
                    && Objects.equals(updatedTeam.getUnitList(), team.getUnitList())
                    && new HashSet<>(updatedTeam.getInventory()).containsAll(team.getInventory())
                    && new HashSet<>(team.getInventory()).containsAll(updatedTeam.getInventory());
     */




    /* private methods */



}
