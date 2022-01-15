package cn.master.tsim;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

@SpringBootTest
class TsimApplicationTests {
    @Autowired
    @Qualifier("primaryJdbcTemplate")
    protected JdbcTemplate jdbcTemplate1;

    @Autowired
    @Qualifier("secondaryJdbcTemplate")
    protected JdbcTemplate jdbcTemplate2;
    @Test
    void contextLoads() {
        List<Map<String, Object>> maps = jdbcTemplate1.queryForList("select * from t_user");
        System.out.println(maps);

        List<Map<String, Object>> maps1 = jdbcTemplate2.queryForList("select * from t_s_base_user");
        System.out.println(maps1);
    }

}
