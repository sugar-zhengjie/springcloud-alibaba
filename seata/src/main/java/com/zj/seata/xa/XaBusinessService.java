package com.zj.seata.xa;

import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class XaBusinessService {

    @Autowired
    private JdbcTemplate jdbcTemplate1;

    @Autowired
    private JdbcTemplate jdbcTemplate2;

    @GlobalTransactional
    public void performBusiness() {
        jdbcTemplate1.execute("BEGIN");
        jdbcTemplate1.update("UPDATE account SET balance = balance - ? where id = ?",100,1);

        jdbcTemplate2.execute("BEGIN");
        jdbcTemplate2.update("UPDATE inventory SET quantity = quantity - ? WHERE product_id = ?", 10, 1);

    }
}
