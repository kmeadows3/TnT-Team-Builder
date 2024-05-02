package my.TNTBuilder.validator;

import my.TNTBuilder.dao.BaseDaoTests;
import my.TNTBuilder.dao.JdbcUnitDao;
import my.TNTBuilder.dao.TestingDatabaseConfig;
import my.TNTBuilder.model.Skill;
import my.TNTBuilder.model.Skillset;
import my.TNTBuilder.model.Unit;
import my.TNTBuilder.model.inventory.Item;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.util.ArrayList;

public class UnitValidatorTests extends BaseDaoTests {
     UnitValidator sut;

    @Before
    public void setSut(){
        sut = new UnitValidator(new JdbcUnitDao(new JdbcTemplate(dataSource)));
    }




}

