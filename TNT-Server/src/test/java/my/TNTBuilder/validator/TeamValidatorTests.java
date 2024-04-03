package my.TNTBuilder.validator;

import io.jsonwebtoken.lang.Assert;
import my.TNTBuilder.model.Skill;
import my.TNTBuilder.model.Skillset;
import my.TNTBuilder.model.Team;
import my.TNTBuilder.model.Unit;
import my.TNTBuilder.model.inventory.Item;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class TeamValidatorTests {

    protected static final Unit UNIT1 = new Unit(1, 1, "UnitName1", "Trade Master", "Leader",
            "Human", 50,10,5,7,6,8,6,5,0,
            "Special rules description",100,0,0,0,
            new ArrayList<Skillset>(), new ArrayList<Skill>(), new ArrayList<Item>());

    protected static final Unit UNIT3 = new Unit(3, 1, "UnitName3", "Class Name", "Specialist",
            "Human", 40,10,5,7,6,8,6,5,0,
            "Special rules description",100,0,0,0,
            new ArrayList<Skillset>(), new ArrayList<Skill>(), new ArrayList<Item>());
    protected final Team TEAM_1 = new Team(1, 1, "Team 1", "Caravanners", 1, 500,
            Arrays.asList(new Unit[]{UNIT1, UNIT3}), new ArrayList<Item>());

    TeamValidator sut;

    @Before
    public void setup(){
        sut = new TeamValidator();
    }

    @Test
    public void validMoneyUpdate_true_if_money_increases(){
        Team updatedTeam = new Team(1, 1, "Team 1", "Caravanners", 1, 1000,
                Arrays.asList(new Unit[]{UNIT1, UNIT3}), new ArrayList<Item>());
        Assert.isTrue(sut.validMoneyChange(TEAM_1, updatedTeam));
    }

    @Test
    public void validMoneyUpdate_true_if_money_decreases(){
        Team updatedTeam = new Team(1, 1, "Team 1", "Caravanners", 1, 0,
                Arrays.asList(new Unit[]{UNIT1, UNIT3}), new ArrayList<Item>());
        Assert.isTrue(sut.validMoneyChange(TEAM_1, updatedTeam));
    }

    @Test
    public void validMoneyUpdate_false_if_money_becomes_negative(){
        Team updatedTeam = new Team(1, 1, "Team 1", "Caravanners", 1, -50,
                Arrays.asList(new Unit[]{UNIT1, UNIT3}), new ArrayList<Item>());
        Assert.isTrue(!sut.validMoneyChange(TEAM_1, updatedTeam));
    }

    @Test
    public void validMoneyUpdate_false_if_money_does_not_change(){
        Team updatedTeam = new Team(1, 1, "Team 1", "Caravanners", 1, 500,
                Arrays.asList(new Unit[]{UNIT1, UNIT3}), new ArrayList<Item>());
        Assert.isTrue(!sut.validMoneyChange(TEAM_1, updatedTeam));
    }


    @Test
    public void validMoneyUpdate_false_if_anything_else_changes(){
        Team updatedTeam = new Team(1, 1, "Name Change", "Caravanners", 1, 50,
                Arrays.asList(new Unit[]{UNIT1, UNIT3}), new ArrayList<Item>());
        Assert.isTrue(!sut.validMoneyChange(TEAM_1, updatedTeam));
    }

    @Test
    public void validNameChange_true_if_only_name_changes(){
        Team updatedTeam = new Team(1, 1, "Name Change", "Caravanners", 1, 500,
                Arrays.asList(new Unit[]{UNIT1, UNIT3}), new ArrayList<Item>());
        Assert.isTrue(sut.validNameChange(TEAM_1, updatedTeam));
    }

    @Test
    public void validNameChange_false_with_other_changes(){
        Team updatedTeam = new Team(1, 1, "Name Change", "Caravanners", 1, 5000,
                Arrays.asList(new Unit[]{UNIT1, UNIT3}), new ArrayList<Item>());
        Assert.isTrue(!sut.validNameChange(TEAM_1, updatedTeam));
    }

    @Test
    public void validNameChange_false_if_name_does_not_change(){
        Team updatedTeam = new Team(1, 1, "Team 1", "Caravanners", 1, 500,
                Arrays.asList(new Unit[]{UNIT1, UNIT3}), new ArrayList<Item>());
        Assert.isTrue(!sut.validNameChange(TEAM_1, updatedTeam));
    }
}
