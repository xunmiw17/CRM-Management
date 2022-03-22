package com.frank.crm.controller;

import com.frank.crm.base.BaseController;
import com.frank.crm.query.CustomerOrderQuery;
import com.frank.crm.service.CustomerOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("order")
public class CustomerOrderController extends BaseController {

    @Resource
    private CustomerOrderService customerOrderService;

    /**
     * Query the customer order list by multi-conditions and paging
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> queryCustomerOrderByParams(CustomerOrderQuery customerOrderQuery) {
        return customerOrderService.queryCustomerOrderByParams(customerOrderQuery);
    }

    /**
     * Opens the order detail page
     * @return
     */
    @RequestMapping("toOrderDetailPage")
    public String toOrderDetailPage(@RequestParam Integer orderId, Model model) {
        Map<String, Object> map = customerOrderService.queryOrderById(orderId);
        model.addAttribute("order", map);
        return "customer/customer_order_detail";
    }
}
