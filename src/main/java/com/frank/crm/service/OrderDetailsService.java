package com.frank.crm.service;

import com.frank.crm.base.BaseService;
import com.frank.crm.dao.OrderDetailsMapper;
import com.frank.crm.query.OrderDetailsQuery;
import com.frank.crm.vo.OrderDetails;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class OrderDetailsService extends BaseService<OrderDetails, Integer> {

    @Resource
    private OrderDetailsMapper orderDetailsMapper;

    /**
     * Query the order details through paging
     * @param orderDetailsQuery
     * @return
     */
    public Map<String, Object> queryOrderDetailsByParams(OrderDetailsQuery orderDetailsQuery) {
        Map<String, Object> map = new HashMap<>();
        PageHelper.startPage(orderDetailsQuery.getPage(), orderDetailsQuery.getLimit());
        PageInfo<OrderDetails> pageInfo = new PageInfo<>(orderDetailsMapper.selectByParams(orderDetailsQuery));
        map.put("code", 0);
        map.put("msg", "success");
        map.put("count", pageInfo.getTotal());
        map.put("data", pageInfo.getList());

        return map;
    }
}
