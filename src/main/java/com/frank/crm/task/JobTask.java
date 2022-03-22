package com.frank.crm.task;

import com.frank.crm.service.CustomerService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Timed task
 */
@Component
public class JobTask {

    @Resource
    private CustomerService customerService;

    /**
     * Timed task (executed every 2 second)
     */
    @Scheduled(cron = "0/2 * * * * ?")
    public void job() {
        System.out.println("The timed task begins to execute -->" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        customerService.updateCustomerState();
    }
}
