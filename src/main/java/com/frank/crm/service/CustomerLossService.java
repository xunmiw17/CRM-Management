package com.frank.crm.service;

import com.frank.crm.base.BaseService;
import com.frank.crm.dao.CustomerLossMapper;
import com.frank.crm.query.CustomerLossQuery;
import com.frank.crm.utils.AssertUtil;
import com.frank.crm.vo.CustomerLoss;
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
public class CustomerLossService extends BaseService<CustomerLoss, Integer> {

    @Resource
    private CustomerLossMapper customerLossMapper;

    /**
     * Multi-condition query with paging
     * @param customerLossQuery
     * @return
     */
    public Map<String, Object> queryCustomerLossByParams(CustomerLossQuery customerLossQuery) {
        Map<String, Object> map = new HashMap<>();

        // Starts paging
        PageHelper.startPage(customerLossQuery.getPage(), customerLossQuery.getLimit());
        // Gets correspondent page object
        PageInfo<CustomerLoss> pageInfo = new PageInfo<>(customerLossMapper.selectByParams(customerLossQuery));

        map.put("code", 0);
        map.put("msg", "success");
        map.put("count", pageInfo.getTotal());
        map.put("data", pageInfo.getList());

        return map;
    }

    /**
     * Updates the customer loss state by id
     * @param id
     * @param lossReason
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCustomerLossStateById(Integer id, String lossReason) {
        AssertUtil.isTrue(id == null, "The data to be updated do not exist");
        CustomerLoss customerLoss = customerLossMapper.selectByPrimaryKey(id);
        AssertUtil.isTrue(customerLoss == null, "The data to be updated do not exist");
        AssertUtil.isTrue(StringUtils.isBlank(lossReason), "Loss reason should not be empty");
        customerLoss.setState(1);
        customerLoss.setLossReason(lossReason);
        customerLoss.setUpdateDate(new Date());
        customerLoss.setConfirmLossTime(new Date());
        AssertUtil.isTrue(customerLossMapper.updateByPrimaryKeySelective(customerLoss) != 1, "Data updating failure");
    }
}
