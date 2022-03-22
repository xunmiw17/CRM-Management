package com.frank.crm.query;

import com.frank.crm.base.BaseQuery;

/**
 * Querying class for Sale Chance.
 */
public class SaleChanceQuery extends BaseQuery {

    // Conditional Query On Sale Chance Management (Appearing at the page)
    private String customerName;
    private String createMan;
    private Integer state;

    // Conditional Query on Customer develompent
    private String devResult;
    private Integer assignMan;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCreateMan() {
        return createMan;
    }

    public void setCreateMan(String createMan) {
        this.createMan = createMan;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getDevResult() {
        return devResult;
    }

    public void setDevResult(String devResult) {
        this.devResult = devResult;
    }

    public Integer getAssignMan() {
        return assignMan;
    }

    public void setAssignMan(Integer assignMan) {
        this.assignMan = assignMan;
    }
}
