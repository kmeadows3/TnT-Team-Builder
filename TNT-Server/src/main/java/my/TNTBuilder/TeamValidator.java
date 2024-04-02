package my.TNTBuilder;

import my.TNTBuilder.model.Team;

import java.util.Objects;

public class TeamValidator {

    public boolean validNameChange (Team team, Team updatedTeam){
        if (!(team.getName().equals(updatedTeam.getName()))){
            return updatedTeam.getId() == team.getId() && updatedTeam.getUserId() == team.getUserId()
                    && updatedTeam.getFactionId() == team.getFactionId() && updatedTeam.getMoney() == team.getMoney()
                    && Objects.equals(updatedTeam.getFaction(), team.getFaction())
                    && Objects.equals(updatedTeam.getUnitList(), team.getUnitList())
                    && Objects.equals(updatedTeam.getInventory(), team.getInventory());
        }
        return false;
    }


    /* Everything is the same
            return updatedTeam.getId() == team.getId() && updatedTeam.getUserId() == team.getUserId()
                    && updatedTeam.getFactionId() == team.getFactionId() && updatedTeam.getMoney() == team.getMoney()
                    && Objects.equals(updatedTeam.getFaction(), team.getFaction())
                    && Objects.equals(updatedTeam.getName(), team.getName())
                    && Objects.equals(updatedTeam.getUnitList(), team.getUnitList())
                    && Objects.equals(updatedTeam.getInventory(), team.getInventory());
     */

}
