package com.frank.crm.service;

import com.frank.crm.base.BaseService;
import com.frank.crm.dao.CustomerMapper;
import com.frank.crm.dao.CustomerServeMapper;
import com.frank.crm.dao.UserMapper;
import com.frank.crm.enums.CustomerServeStatus;
import com.frank.crm.query.CustomerServeQuery;
import com.frank.crm.utils.AssertUtil;
import com.frank.crm.vo.CustomerServe;
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
public class CustomerServeService extends BaseService<CustomerServe, Integer> {

    @Resource
    private CustomerServeMapper customerServeMapper;

    @Resource
    private CustomerMapper customerMapper;

    @Resource
    private UserMapper userMapper;

    /**
     * Query the serve data list by multi-condition and paging
     * @param customerServeQuery
     * @return
     */
    public Map<String, Object> queryCustomerServeByParams(CustomerServeQuery customerServeQuery) {
        Map<String, Object> map = new HashMap<>();
        PageHelper.startPage(customerServeQuery.getPage(), customerServeQuery.getLimit());
        PageInfo<CustomerServe> customerServePageInfo = new PageInfo<>(customerServeMapper.selectByParams(customerServeQuery));
        map.put("code", 0);
        map.put("msg", "success");
        map.put("count", customerServePageInfo.getTotal());
        map.put("data", customerServePageInfo.getList());
        return map;
    }

    /**
     * Creates a customer service
     *      1. Check parameters
     *          customer name -> Not null, and customer table has this customer
     *          serve type -> Not null
     *          service request -> Not null
     *      2. Sets default value
     *          serve state -> fw_001
     *          isValid
     *          createDate
     *          updateDate
     *          createPeople -> Got from front end by cookie and passed to backend
     *      3. Executes adding operation
     * @param customerServe
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addCustomerServe(CustomerServe customerServe) {
        AssertUtil.isTrue(StringUtils.isBlank(customerServe.getCustomer()), "The customer name should not be empty");
        AssertUtil.isTrue(customerMapper.queryCustomerByName(customerServe.getCustomer()) == null, "The customer does not exist");
        AssertUtil.isTrue(StringUtils.isBlank(customerServe.getServeType()), "The service type should not be blank");
        AssertUtil.isTrue(StringUtils.isBlank(customerServe.getServiceRequest()), "The service request should not be empty");

        customerServe.setState(CustomerServeStatus.CREATED.getState());
        customerServe.setIsValid(1);
        customerServe.setCreateDate(new Date());
        customerServe.setUpdateDate(new Date());

        AssertUtil.isTrue(customerServeMapper.insertSelective(customerServe) != 1, "Creating service failed");
    }

    /**
     * Service assigned/Service handling/Service feedback
     *      1. Check parameters and set default values
     *          customer serve id -> Not null, record exists
     *          Customer service state
     *                  Assigned state fw_002 -> assigned person: not null and data exist
     *                                          assigned time: current time
     *                                          update time: current time
     *                  Handling state fw_003 -> handling content: not null
     *                                      handling time: current time
     *                                      update time: current time
     *                  Feedback state fw_004 ->
     *                                      feedback content: not null
     *                                      degree of satisfaction: not null
     *                                      update time: current time
     *                                      service state: set to fw_005 (archived)
     *       2. Executes the updating operation
     * @param customerServe
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCustomerServeState(CustomerServe customerServe) {
        AssertUtil.isTrue(customerServe.getId() == null || customerServeMapper.selectByPrimaryKey(customerServe.getId()) == null, "The data to be updated do not exist");
        if (customerServe.getState().equals(CustomerServeStatus.ASSIGNED.getState())) {
            AssertUtil.isTrue(StringUtils.isBlank(customerServe.getAssigner()) || userMapper.selectByPrimaryKey(Integer.parseInt(customerServe.getAssigner())) == null, "The user to be assigned does not exist");
            customerServe.setAssignTime(new Date());
        } else if (customerServe.getState().equals(CustomerServeStatus.PROCED.getState())) {
            AssertUtil.isTrue(StringUtils.isBlank(customerServe.getServiceProce()), "The service handling content should not be empty");
            customerServe.setServiceProceTime(new Date());
        } else if (customerServe.getState().equals(CustomerServeStatus.FEED_BACK.getState())) {
            AssertUtil.isTrue(StringUtils.isBlank(customerServe.getServiceProceResult()), "The feedback content should not be empty");
            AssertUtil.isTrue(StringUtils.isBlank(customerServe.getMyd()), "Please select the degree of satisfaction");
            customerServe.setState(CustomerServeStatus.ARCHIVED.getState());
        }
        customerServe.setUpdateDate(new Date());
        AssertUtil.isTrue(customerServeMapper.updateByPrimaryKeySelective(customerServe) != 1, "Updaing the customer service state failed");
    }
}
