package com.robod.quartzdemo;

import com.robod.quartzdemo.job.TemplateJob;
import org.junit.jupiter.api.Test;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class QuartzDemoApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void createJob() {
        JobDetail jobDetail = JobBuilder.newJob(TemplateJob.class)
                .usingJobData("userId", "VIP2345678")
                .usingJobData("awardName", "100元优惠券")
                .usingJobData("awardId", "YHQ675567687765")
                .usingJobData("awardValue", 6.6)
                .withIdentity("任务名", "任务组名")
                .build();
//或者
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("userId", "VIP2345678");
        jobDataMap.put("awardName", "100元优惠券");
        jobDataMap.put("awardId", "YHQ675567687765");
        jobDataMap.put("awardValue", 6.6);
        JobDetail jobDetail2 = JobBuilder.newJob(TemplateJob.class)
                .setJobData(jobDataMap)
                .withIdentity("任务名", "任务组名")
                .build();
    }

}
