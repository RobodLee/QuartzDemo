package com.robod.quartzdemo.job;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author L.D.
 * @date 2022/8/15 11:08
 * @description
 */
public class AwardJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        String userId = jobDataMap.getString("userId");
        String awardName = jobDataMap.getString("awardName");
        String awardId = jobDataMap.getString("awardId");
        double awardValue = jobDataMap.getDouble("awardValue");
        //…………发奖品…………
    }
}
