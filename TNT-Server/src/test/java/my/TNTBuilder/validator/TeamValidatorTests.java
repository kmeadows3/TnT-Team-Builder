package my.TNTBuilder.validator;

import io.jsonwebtoken.lang.Assert;
import my.TNTBuilder.dao.BaseDaoTests;
import my.TNTBuilder.dao.JdbcTeamDao;
import my.TNTBuilder.model.Team;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class TeamValidatorTests extends BaseDaoTests {

    TeamValidator sut;
    Team teamToUpdate;

    @Before
    public void setup(){
        sut = new TeamValidator();
        teamToUpdate  = new Team(1, 1, "Team 1", "Caravanners", 1, 500,
                Arrays.asList(UNIT1, UNIT3), new ArrayList<>());
    }

    @Test
    public void validMoneyUpdate_true_if_money_increases(){
        teamToUpdate.setMoney(1000);
        Assert.isTrue(sut.validMoneyChange(TEAM_1, teamToUpdate));
    }

    @Test
    public void validMoneyUpdate_true_if_money_decreases(){
        teamToUpdate.setMoney(0);
        Assert.isTrue(sut.validMoneyChange(TEAM_1, teamToUpdate));
    }

    @Test
    public void validMoneyUpdate_false_if_money_becomes_negative(){
        teamToUpdate.setMoney(-10);
        Assert.isTrue(!sut.validMoneyChange(TEAM_1, teamToUpdate));
    }

    @Test
    public void validMoneyUpdate_false_if_money_does_not_change(){
        Assert.isTrue(!sut.validMoneyChange(TEAM_1, teamToUpdate));
    }


    @Test
    public void validMoneyUpdate_false_if_anything_else_changes(){
        teamToUpdate.setMoney(50);
        teamToUpdate.setName("New Name");
        Assert.isTrue(!sut.validMoneyChange(TEAM_1, teamToUpdate));
    }

    @Test
    public void validNameChange_true_if_only_name_changes(){
        teamToUpdate.setName("New Name");
        Assert.isTrue(sut.validNameChange(TEAM_1, teamToUpdate));
    }

    @Test
    public void validNameChange_false_with_other_changes(){
        teamToUpdate.setName("New Name");
        teamToUpdate.setMoney(100);
        Assert.isTrue(!sut.validNameChange(TEAM_1, teamToUpdate));
    }

    @Test
    public void validNameChange_false_if_name_does_not_change(){
        Assert.isTrue(!sut.validNameChange(TEAM_1, teamToUpdate));
    }
}
