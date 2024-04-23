package my.TNTBuilder.dao;

import my.TNTBuilder.model.*;
import my.TNTBuilder.model.inventory.Item;
import my.TNTBuilder.model.userModels.User;
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestingDatabaseConfig.class)
public abstract class BaseDaoTests {
    protected static final User USER_1 = new User(1, "user1", "user1", "USER");
    protected static final User USER_2 = new User(2, "user2", "user2", "USER");
    protected static final User USER_3 = new User(3, "user3", "user3", "USER");

    protected static final Unit UNIT1 = new Unit(1, 1, "UnitName1", "Trade Master", "Leader",
            "Human", 50,10,5,7,6,8,6,5,0,
            "Special rules description",100,0,0,0,
            Arrays.asList(new Skillset(3, "Survival", "Skill"),
                    new Skillset(4, "Quickness", "Skill")),
            new ArrayList<Skill>(), new ArrayList<Item>());
    protected static final Unit UNIT2 = new Unit(2, 3, "UnitName2", "Soldier", "Elite",
            "Mutant", 51,11,6,8,7,9,7,6,1,
            "Special rules description",50,0,0,0,
            Arrays.asList(new Skillset[]{new Skillset(6, "Brawn", "Skill")}), new ArrayList<Skill>(), new ArrayList<Item>());

    protected static final Unit UNIT3 = new Unit(3, 1, "UnitName3", "Class Name", "Specialist",
            "Human", 40,10,5,7,6,8,6,5,0,
            "Special rules description",100,0,0,0,
            Arrays.asList(new Skillset[]{new Skillset(6, "Brawn", "Skill")}),
            Arrays.asList(new Skill[]{new Skill(7, "Bully", "All enemies defeated by this model in" +
                    " close combat are knocked prone in addition to any other combat result.", 6, "Brawn")}),
            new ArrayList<Item>());
    protected final Team TEAM_1 = new Team(1, 1, "Team 1", "Caravanners", 1, 500,
            Arrays.asList(new Unit[]{UNIT1, UNIT3}), new ArrayList<Item>());
    protected final Team TEAM_2 = new Team(2, 1, "Team 2", "Raiders", 3, 1500,
            new ArrayList<Unit>(), new ArrayList<Item>());
    protected final Team TEAM_3 = new Team(3, 2, "Team 3", "Mutants", 2, 1000,
            Arrays.asList(new Unit[]{UNIT2}), new ArrayList<Item>());
    protected final FactionDTO faction1 = new FactionDTO(1, "Caravanners");
    protected final FactionDTO faction2 = new FactionDTO(2, "Mutants");



    @Autowired
    protected DataSource dataSource;

    @After
    public void rollback() throws SQLException {
        dataSource.getConnection().rollback();
    }

}
