package my.TNTBuilder.validator;

import my.TNTBuilder.model.Team;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class TeamValidator {

    //TODO TEST ALL
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

    public boolean validMoneyChange (Team team, Team updatedTeam){
        if (updatedTeam.getMoney() != team.getMoney() && updatedTeam.getMoney() >= 0 ){
            return updatedTeam.getId() == team.getId() && updatedTeam.getUserId() == team.getUserId()
                    && updatedTeam.getFactionId() == team.getFactionId()
                    && Objects.equals(updatedTeam.getFaction(), team.getFaction())
                    && Objects.equals(updatedTeam.getName(), team.getName())
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