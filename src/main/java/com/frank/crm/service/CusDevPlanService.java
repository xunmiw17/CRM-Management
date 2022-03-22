package com.frank.crm.service;

import com.frank.crm.base.BaseService;
import com.frank.crm.dao.CusDevPlanMapper;
import com.frank.crm.dao.SaleChanceMapper;
import com.frank.crm.query.CusDevPlanQuery;
import com.frank.crm.utils.AssertUtil;
import com.frank.crm.vo.CusDevPlan;
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
public class CusDevPlanService extends BaseService<CusDevPlan, Integer> {

    @Resource
    private CusDevPlanMapper cusDevPlanMapper;

    @Resource
    private SaleChanceMapper saleChanceMapper;

    /**
     * The method queries the customer dev plan by multiple conditions and finally splits the result into pages. The data
     * type to be returned must conform to what LayUI requires.
     * @param cusDevPlanQuery
     * @return
     */
    public Map<String, Object> queryCusDevPlanByParams(CusDevPlanQuery cusDevPlanQuery) {
        Map<String, Object> map = new HashMap<>();

        // Starts paging
        PageHelper.startPage(cusDevPlanQuery.getPage(), cusDevPlanQuery.getLimit());
        // Gets correspondent page object
        PageInfo<CusDevPlan> pageInfo = new PageInfo<>(cusDevPlanMapper.selectByParams(cusDevPlanQuery));

        map.put("code", 0);
        map.put("msg", "success");
        map.put("count", pageInfo.getTotal());
        map.put("data", pageInfo.getList());

        return map;
    }

    /**
     * Adds customer development plan
     *  1. Checks parameter
     *          sale chance id -> Not Null and data record exists
     *          Content of dev plan -> not null
     *          plan time -> not null
     *  2. Sets parameters default value
     *          isValid -> default valid
     *          createTime -> default current system time
     *          update Time -> default current system time
     *  3. Executes the add operation and determine the affected number of lines
     * @param cusDevPlan
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addCusDevPlan(CusDevPlan cusDevPlan) {
        checkCusDevPlanParams(cusDevPlan);

        cusDevPlan.setIsValid(1);
        cusDevPlan.setCreateDate(new Date());
        cusDevPlan.setUpdateDate(new Date());

        AssertUtil.isTrue(cusDevPlanMapper.insertSelective(cusDevPlan) != 1, "Cus dev plan data adding failed");
    }

    private void checkCusDevPlanParams(CusDevPlan cusDevPlan) {
        Integer sId = cusDevPlan.getSaleChanceId();
        AssertUtil.isTrue(sId == null || saleChanceMapper.selectByPrimaryKey(sId) == null, "Data is not correct, please try again");
        AssertUtil.isTrue(StringUtils.isBlank(cusDevPlan.getPlanItem()), "Dev Plan content should not be empty");
        AssertUtil.isTrue(cusDevPlan.getPlanDate() == null, "Plan time should not be empty");
    }

    /**
     * Updates customer development plan
     *      1. Checks parameter
     *          Plan item id -> Not null and data record exists
     *          sale chance id -> Not Null and data record exists
     *          Content of dev plan -> not null
     *          plan time -> not null
     *      2. Sets default value of parameters
     *          update time -> current system time
     *      3. Executes the updating operation and determines the affected number of lines
     * @param cusDevPlan
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCusDevPlan(CusDevPlan cusDevPlan) {
        AssertUtil.isTrue(cusDevPlan.getId() == null || cusDevPlanMapper.selectByPrimaryKey(cusDevPlan.getId()) == null, "Error in data, please try again");
        checkCusDevPlanParams(cusDevPlan);
        cusDevPlan.setUpdateDate(new Date());
        AssertUtil.isTrue(cusDevPlanMapper.updateByPrimaryKeySelective(cusDevPlan) != 1, "Plan item updating failed");
    }

    /**
     * Deletes the plan item.
     *      1. Determines if the id is not null and the corresponding data exists
     *      2. Changes the isValid
     *      3. Executes the updating operation
     * @param id
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteDevPlan(Integer id) {
        AssertUtil.isTrue(id == null, "The data to be deleted does not exist");
        CusDevPlan cusDevPlan = cusDevPlanMapper.selectByPrimaryKey(id);
        cusDevPlan.setIsValid(0);
        cusDevPlan.setUpdateDate(new Date());
        AssertUtil.isTrue(cusDevPlanMapper.updateByPrimaryKeySelective(cusDevPlan) != 1, "Plan item data deleting failure");
    }
}
