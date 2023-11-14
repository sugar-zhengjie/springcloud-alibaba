package com.zj.stock.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author clinflash_zhengjie
 */
@Service
public class StockService {

    @Transactional(rollbackFor = Exception.class)
    public String reduce() {
        return "库存减一成功";
    }
}
