package com.frank.crm.service;

import com.frank.crm.base.BaseService;
import com.frank.crm.dao.CustomerOrderMapper;
import com.frank.crm.query.CustomerOrderQuery;
import com.frank.crm.vo.CustomerOrder;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class CustomerOrderService extends BaseService<CustomerOrder, Integer> {

    @Resource
    private CustomerOrderMapper customerOrderMapper;

    public Map<String, Object> queryCustomerOrderByParams(CustomerOrderQuery customerOrderQuery) {
        Map<String, Object> map = new HashMap<>();

        // Starts paging
        PageHelper.startPage(customerOrderQuery.getPage(), customerOrderQuery.getLimit());
        // Gets correspondent page object
        PageInfo<CustomerOrder> pageInfo = new PageInfo<>(customerOrderMapper.selectByParams(customerOrderQuery));

        map.put("code", 0);
        map.put("msg", "success");
        map.put("count", pageInfo.getTotal());
        map.put("data", pageInfo.getList());

        return map;
    }

    /**
     * Query the order record by order id
     * @param orderId
     * @return
     */
    public Map<String, Object> queryOrderById(Integer orderId) {
        return customerOrderMapper.queryOrderById(orderId);
    }
}
