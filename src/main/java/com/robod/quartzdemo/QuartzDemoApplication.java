package com.robod.quartzdemo;

import com.robod.quartzdemo.util.QuartzUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Robod
 */
@SpringBootApplication
@MapperScan("com.robod.quartzdemo.mapper")
public class QuartzDemoApplication implements ApplicationRunner {

    @Autowired
    private QuartzUtil quartzUtil;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(QuartzDemoApplication.class, args);
//        JobDetail jobDetail = JobBuilder.newJob(TemplateJob.class).withIdentity("任务名", "任务组名").build();
//        SimpleTrigger trigger = (SimpleTrigger) TriggerBuilder.newTrigger()
//                .withIdentity("这里填触发器名", "这里填触发器组名")
//                .startNow()
//                .build();
//        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
//        scheduler.scheduleJob(jobDetail, trigger);
//        scheduler.start();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        quartzUtil.recoveryAllJob();
    }

}
