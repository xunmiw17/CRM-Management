package com.frank.crm.service;

import com.frank.crm.base.BaseService;
import com.frank.crm.dao.SaleChanceMapper;
import com.frank.crm.enums.DevResult;
import com.frank.crm.enums.StateStatus;
import com.frank.crm.query.SaleChanceQuery;
import com.frank.crm.utils.AssertUtil;
import com.frank.crm.utils.PhoneUtil;
import com.frank.crm.vo.SaleChance;
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
public class SaleChanceService extends BaseService<SaleChance, Integer> {

    @Resource
    private SaleChanceMapper saleChanceMapper;

    /**
     * The method queries the sale chance by multiple conditions and finally splits the result into pages. The data
     * type to be returned must conform to what LayUI requires.
     * @param saleChanceQuery
     * @return
     */
    public Map<String, Object> querySaleChanceByParams(SaleChanceQuery saleChanceQuery) {
        Map<String, Object> map = new HashMap<>();

        // Starts paging
        PageHelper.startPage(saleChanceQuery.getPage(), saleChanceQuery.getLimit());
        // Gets correspondent page object
        PageInfo<SaleChance> pageInfo = new PageInfo<>(saleChanceMapper.selectByParams(saleChanceQuery));

        map.put("code", 0);
        map.put("msg", "success");
        map.put("count", pageInfo.getTotal());
        map.put("data", pageInfo.getList());

        return map;
    }

    /**
     * Adds sale chance when the user clicks the "Add" button
     * 1. Checks parameters
     *      customerName -> Not Null
     *      linkMan -> Not Null
     *      linkPhone -> Not Null, and phone number is of correct form
     *
     * 2. Sets the default value of some parameters
     *      createMan -> name of current login user (default)
     *      assignMan -> If no provided value (default), then state would be "not assigned", or value 0; Assign time would be null; devResult would be "Not developed", or value 0
     *                   If having provided value, then state would be "assigned"; Assign time would be the current system time; devResult would be "Developing", or value 1
     *      isValid -> Set to valid, or value 1 (default)
     *      createDate -> current system time (default)
     *      updateDate -> current system time (default)
     *
     * 3. Executing the "add" operation and determining the affected number of lines
     *
     * @param saleChance
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addSaleChance(SaleChance saleChance) {
        // Checks the parameters
        checkSaleChanceParams(saleChance.getCustomerName(), saleChance.getLinkMan(), saleChance.getLinkPhone());
        // Sets default value
        saleChance.setIsValid(1);
        saleChance.setCreateDate(new Date());
        saleChance.setUpdateDate(new Date());
        if (StringUtils.isBlank(saleChance.getAssignMan())) {
            saleChance.setState(StateStatus.UNSTATE.getType());
            saleChance.setAssignTime(null);
            saleChance.setDevResult(DevResult.UNDEV.getStatus());
        } else {
            saleChance.setState(StateStatus.STATED.getType());
            saleChance.setAssignTime(new Date());
            saleChance.setDevResult(DevResult.DEVING.getStatus());
        }

        // Executes the add operation
        AssertUtil.isTrue(saleChanceMapper.insertSelective(saleChance) != 1, "Failed to add sale chance");
    }

    /**
     * Updates the sale chance records
     * 1. Check parameters
     *      - Sale Chance ID -> Not null and there exists corresponding record in database
     *      - customerName -> Not Null
     *      - linkMan -> Not Null
     *      - linkPhone -> Not Null, and phone number is of correct form
     * 2. Sets default value
     *      - updateDate -> Set to current system time
     *      - assignMan ->
     *              1) Original value not set
     *                  a) Updating not set -> No need for any operations
     *                  b) Updating set -> assignTime set to current system time; state set to "has assigned" (1); devState set to "developing" (1).
     *              2) Original value set
     *                  a) Updating not set ->  assignTime set to null; state set to "not assigned" (0); devState set to "not developed" (0)
     *                  b) Updating set
     *                      i) set to same assignMan -> No need for operations
     *                      ii) set to different assignMan -> assignTime set to current system time
     * 3. Executing update operations and determine the number of lines affected
     *
     * @param saleChance
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSaleChance(SaleChance saleChance) {
        // 1.
        AssertUtil.isTrue(saleChance.getId() == null, "The record to be updated does not exist");
        SaleChance temp = saleChanceMapper.selectByPrimaryKey(saleChance.getId());
        AssertUtil.isTrue(temp == null, "The record to be updated does not exist");
        checkSaleChanceParams(saleChance.getCustomerName(), saleChance.getLinkMan(), saleChance.getLinkPhone());

        // 2.
        saleChance.setUpdateDate(new Date());
        if (StringUtils.isBlank(temp.getAssignMan())) {
            if (!StringUtils.isBlank(saleChance.getAssignMan())) {
                saleChance.setAssignTime(new Date());
                saleChance.setState(StateStatus.STATED.getType());
                saleChance.setDevResult(DevResult.DEVING.getStatus());
            }
        } else {
            if (StringUtils.isBlank(saleChance.getAssignMan())) {
                saleChance.setAssignTime(null);
                saleChance.setState(StateStatus.UNSTATE.getType());
                saleChance.setDevResult(DevResult.UNDEV.getStatus());
            } else {
                if (!saleChance.getAssignMan().equals(temp.getAssignMan())) {
                    saleChance.setAssignTime(new Date());
                } else {
                    saleChance.setAssignTime(temp.getAssignTime());
                }
            }
        }

        // 3.
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance) != 1, "Failure to update sale chance");
    }

    /**
     * Checks the parameters of customer name, contact person, and contact phone
     * @param customerName
     * @param linkMan
     * @param linkPhone
     */
    private void checkSaleChanceParams(String customerName, String linkMan, String linkPhone) {
        AssertUtil.isTrue(StringUtils.isBlank(customerName), "User name cannot be empty");
        AssertUtil.isTrue(StringUtils.isBlank(linkMan), "Contact person should not be empty");
        AssertUtil.isTrue(StringUtils.isBlank(linkPhone), "Contact phone should not be empty");
        AssertUtil.isTrue(!PhoneUtil.isMobile(linkPhone), "Contact phone is not in the correct form");
    }

    /**
     * This method deletes sale chance
     * @param ids
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteSaleChance(Integer[] ids) {
        AssertUtil.isTrue(ids == null || ids.length == 0, "The data to be deleted do not exist");
        AssertUtil.isTrue(saleChanceMapper.deleteBatch(ids) != ids.length, "Sale Chance data delete failed");
    }

    /**
     * Updates the sale chance dev result
     * @param id
     * @param devResult
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSaleChanceDevResult(Integer id, Integer devResult) {
        AssertUtil.isTrue(id == null, "The data record to be updated does not exist");
        SaleChance saleChance = saleChanceMapper.selectByPrimaryKey(id);
        AssertUtil.isTrue(saleChance == null, "The data record to be updated does not exist");
        saleChance.setDevResult(devResult);
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance) != 1, "Sale chance dev result updating failure");
    }
}
