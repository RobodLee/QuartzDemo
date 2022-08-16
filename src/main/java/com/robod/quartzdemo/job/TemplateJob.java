package com.robod.quartzdemo.job;

import com.robod.quartzdemo.service.QuartzService;
import com.robod.quartzdemo.util.SpringContextJobUtil;
import org.quartz.*;

import java.time.LocalDateTime;

/**
 * @author L.D.
 * @date 2022/8/15 11:04
 * @description
 */
public class TemplateJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        QuartzService quartzService = (QuartzService) SpringContextJobUtil.getBean("quartzService");

        // 欢迎关注我的微信公众号：Robod
        JobKey key = context.getJobDetail().getKey();
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        System.out.println(key.getName());
        System.out.println(key.getGroup());
        System.out.println(jobDataMap.get("k1"));
        System.out.println(LocalDateTime.now() + "------------------------");
    }

}
