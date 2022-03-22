package com.frank.crm.dao;

import com.frank.crm.base.BaseMapper;
import com.frank.crm.query.CustomerQuery;
import com.frank.crm.vo.Customer;

import java.util.List;
import java.util.Map;

public interface CustomerMapper extends BaseMapper<Customer, Integer> {

    Customer queryCustomerByName(String name);

    List<Customer> queryLossCustomers();

    int updateCustomerStateByIds(List<Integer> lossCustomerIds);

    List<Map<String, Object>> queryCustomerContributionByParams(CustomerQuery customerQuery);

    List<Map<String, Object>> countCustomerMakeup();
}