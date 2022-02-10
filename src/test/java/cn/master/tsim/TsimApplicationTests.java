package cn.master.tsim;

import cn.master.tsim.service.CaseReviewRefService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
class TsimApplicationTests {
    @Autowired
    @Qualifier("primaryJdbcTemplate")
    protected JdbcTemplate jdbcTemplate1;

    @Autowired
    @Qualifier("secondaryJdbcTemplate")
    protected JdbcTemplate jdbcTemplate2;

    @Autowired
    CaseReviewRefService mapper;
    @Test
    void contextLoads() {

    }

}
