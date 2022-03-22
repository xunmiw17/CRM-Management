package com.frank.crm.dao;

import com.frank.crm.base.BaseMapper;
import com.frank.crm.vo.CustomerOrder;

import java.util.Map;

public interface CustomerOrderMapper extends BaseMapper<CustomerOrder, Integer> {

    // Query the order details by order id
    Map<String, Object> queryOrderById(Integer orderId);

    CustomerOrder queryLossCustomerOrderByCustomerId(Integer customerId);
}