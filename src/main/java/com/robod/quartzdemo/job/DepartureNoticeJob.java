package com.robod.quartzdemo.job;

import com.robod.quartzdemo.service.OrderService;
import com.robod.quartzdemo.service.QuartzService;
import com.robod.quartzdemo.util.SpringContextJobUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author L.D.
 * @date 2022/8/15 11:24
 * @description
 */
public class DepartureNoticeJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        QuartzService quartzService = (QuartzService) SpringContextJobUtil.getBean("quartzService");
        OrderService orderService = (OrderService) SpringContextJobUtil.getBean("orderService");

        String jobName = context.getJobDetail().getKey().getName();
        long orderId = Long.parseLong(jobName);
        // TODO 获取订单及用户信息，封装短信内容，调用短信发送模块发送短信
        quartzService.modifyTaskStatus(jobName, "1");
    }

}
