package com.frank.crm.query;

import com.frank.crm.base.BaseQuery;

public class CustomerLossQuery extends BaseQuery {
    private String customerNo;
    private String customerName;
    private Integer state;

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
