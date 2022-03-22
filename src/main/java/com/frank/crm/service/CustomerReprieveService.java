package com.frank.crm.service;

import com.frank.crm.base.BaseService;
import com.frank.crm.dao.CustomerLossMapper;
import com.frank.crm.dao.CustomerReprieveMapper;
import com.frank.crm.query.CustomerReprieveQuery;
import com.frank.crm.utils.AssertUtil;
import com.frank.crm.vo.CustomerReprieve;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class CustomerReprieveService extends BaseService<CustomerReprieve, Integer> {

    @Resource
    private CustomerReprieveMapper customerReprieveMapper;

    @Resource
    private CustomerLossMapper customerLossMapper;

    public Map<String, Object> queryCustomerReprieveByParams(CustomerReprieveQuery customerReprieveQuery) {
        Map<String, Object> map = new HashMap<>();

        // Starts paging
        PageHelper.startPage(customerReprieveQuery.getPage(), customerReprieveQuery.getLimit());
        // Gets correspondent page object
        PageInfo<CustomerReprieve> pageInfo = new PageInfo<>(customerReprieveMapper.selectByParams(customerReprieveQuery));

        map.put("code", 0);
        map.put("msg", "success");
        map.put("count", pageInfo.getTotal());
        map.put("data", pageInfo.getList());

        return map;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void addCustomerRepr(CustomerReprieve customerReprieve) {
        AssertUtil.isTrue(customerReprieve.getLossId() == null || customerLossMapper.selectByPrimaryKey(customerReprieve.getLossId()) == null, "The loss customer data do not exist");
        AssertUtil.isTrue(StringUtils.isBlank(customerReprieve.getMeasure()), "The loss customer measure should not be empty");
        customerReprieve.setIsValid(1);
        customerReprieve.setCreateDate(new Date());
        customerReprieve.setUpdateDate(new Date());
        AssertUtil.isTrue(customerReprieveMapper.insertSelective(customerReprieve) != 1, "Adding reprieve data failure");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCustomerRepr(CustomerReprieve customerReprieve) {
        AssertUtil.isTrue(customerReprieve.getLossId() == null || customerReprieveMapper.selectByPrimaryKey(customerReprieve.getLossId()) == null, "The loss customer data do not exist");
        AssertUtil.isTrue(StringUtils.isBlank(customerReprieve.getMeasure()), "The los customer measure should not be empty");
        AssertUtil.isTrue(customerReprieve.getId() == null || customerReprieveMapper.selectByPrimaryKey(customerReprieve.getId()) == null, "The record to be updated does not exist");
        customerReprieve.setUpdateDate(new Date());
        AssertUtil.isTrue(customerReprieveMapper.updateByPrimaryKeySelective(customerReprieve) < 1, "Updating reprieve data failure");
    }

    /**
     * Deletes the customer reprieve
     * @param id
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteCustomerRepr(Integer id) {
        AssertUtil.isTrue(id == null, "The data to be deleted do not exist");
        CustomerReprieve customerReprieve = customerReprieveMapper.selectByPrimaryKey(id);
        AssertUtil.isTrue(customerReprieve == null, "The data to be deleted do not exist");
        customerReprieve.setIsValid(0);
        customerReprieve.setUpdateDate(new Date());
        AssertUtil.isTrue(customerReprieveMapper.updateByPrimaryKeySelective(customerReprieve) < 1, "Deleting customer reprieve data failed");
    }
}
