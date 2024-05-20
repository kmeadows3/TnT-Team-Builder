package my.TNTBuilder.validator;

import my.TNTBuilder.dao.BaseDaoTests;
import my.TNTBuilder.dao.JdbcUnitDao;
import org.junit.Before;
import org.springframework.jdbc.core.JdbcTemplate;

public class UnitValidatorTests extends BaseDaoTests {
     UnitValidator sut;

    @Before
    public void setSut(){
        sut = new UnitValidator(new JdbcUnitDao(new JdbcTemplate(dataSource)));
    }




}

