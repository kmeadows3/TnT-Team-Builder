package my.TNTBuilder.dao;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcItemDaoTests extends BaseDaoTests{

    private JdbcItemDao sut;

    @Before
    public void setSut(){
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcItemDao(jdbcTemplate);
    }

    @Test
    public void getAllItemsForUnit_returns_full_list(){

    }
}
