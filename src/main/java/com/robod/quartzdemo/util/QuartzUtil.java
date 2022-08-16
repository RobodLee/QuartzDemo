package com.robod.quartzdemo.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.robod.quartzdemo.entity.QuartzEntity;
import com.robod.quartzdemo.service.QuartzService;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.quartz.TriggerBuilder.newTrigger;

/**
 * @author L.D.
 * @date 2022/8/15 11:10
 * @description
 */
@Component
public class QuartzUtil {

    private static final SchedulerFactory SCHEDULER_FACTORY = new StdSchedulerFactory();

    @Autowired
    private QuartzService quartzService;

    /**
     * 添加一个定时任务
     *
     * @param name      任务名。每个任务唯一，不能重复。方便起见，触发器名也设为这个
     * @param group     任务分组。方便起见，触发器分组也设为这个
     * @param jobClass  任务的类类型  eg:TemplateJob.class
     * @param startTime 任务开始时间。传null就是立即开始
     * @param endTime   任务结束时间。如果是一次性任务或永久执行的任务就传null
     * @param cron      时间设置表达式。传null就是一次性任务
     */
    public boolean addJob(String name, String group, Class<? extends Job> jobClass,
                       LocalDateTime startTime, LocalDateTime endTime, String cron, JobDataMap jobDataMap) {
        try {
            // 第一步: 定义一个JobDetail
            JobDetail jobDetail = JobBuilder.newJob(jobClass).
                    withIdentity(name, group).setJobData(jobDataMap).build();
            // 第二步: 设置触发器
            TriggerBuilder<Trigger> triggerBuilder = newTrigger();
            triggerBuilder.withIdentity(name, group);
            triggerBuilder.startAt(toStartDate(startTime));
            triggerBuilder.endAt(toEndDate(endTime)); //设为null则表示不会停止
            if (StrUtil.isNotEmpty(cron)) {
                triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
            }
            Trigger trigger = triggerBuilder.build();
            //第三步：调度器设置
            Scheduler scheduler = SCHEDULER_FACTORY.getScheduler();
            scheduler.scheduleJob(jobDetail, trigger);
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        //存储到数据库中
        QuartzEntity entity = new QuartzEntity();
        entity.setJobName(name);
        entity.setGroupName(group);
        entity.setStartTime(startTime != null ? startTime : LocalDateTime.now());
        entity.setEndTime(endTime);
        entity.setJobClass(jobClass.getName());
        entity.setCron(cron);
        entity.setJobDataMapJson(JSONUtil.toJsonStr(jobDataMap));
        entity.setStatus("0");
        quartzService.save(entity);
        return true;
    }

    /**
     * 修改一个任务的开始时间、结束时间、cron。不改的就传null
     *
     * @param name         任务名。每个任务唯一，不能重复。方便起见，触发器名也设为这个
     * @param group        任务分组。方便起见，触发器分组也设为这个
     * @param newStartTime 新的开始时间
     * @param newEndTime   新的结束时间
     * @param cron         新的时间表达式
     */
    public boolean modifyJobTime(String name, String group, LocalDateTime newStartTime,
                             LocalDateTime newEndTime, String cron) {
        try {
            Scheduler scheduler = SCHEDULER_FACTORY.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(name, group);
            Trigger oldTrigger = scheduler.getTrigger(triggerKey);
            if (oldTrigger == null) {
                return false;
            }
            TriggerBuilder<Trigger> triggerBuilder = newTrigger();
            triggerBuilder.withIdentity(name, group);
            if (newStartTime != null) {
                triggerBuilder.startAt(toStartDate(newStartTime));   // 任务开始时间设定
            } else if (oldTrigger.getStartTime() != null) {
                triggerBuilder.startAt(oldTrigger.getStartTime()); //没有传入新的开始时间就不变
            }
            if (newEndTime != null) {
                triggerBuilder.endAt(toEndDate(newEndTime));   // 任务结束时间设定
            } else if (oldTrigger.getEndTime() != null) {
                triggerBuilder.endAt(oldTrigger.getEndTime()); //没有传入新的结束时间就不变
            }
            if (StrUtil.isNotEmpty(cron)) {
                triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
            } else if (oldTrigger instanceof CronTrigger) {
                String oldCron = ((CronTrigger) oldTrigger).getCronExpression();
                triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(oldCron));
            }
            Trigger newTrigger = triggerBuilder.build();
            scheduler.rescheduleJob(triggerKey, newTrigger);    // 修改触发时间
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        // 修改数据库中的记录
        QuartzEntity entity = new QuartzEntity();
        entity.setJobName(name);
        entity.setGroupName(group);
        if (newStartTime != null) {
            entity.setStartTime(newStartTime);
        }
        if (newEndTime != null) {
            entity.setEndTime(newEndTime);
        }
        if (StrUtil.isNotEmpty(cron)) {
            entity.setCron(cron);
        }
        return quartzService.modifyJob(entity);
    }

    public boolean cancelJob(String jobName, String groupName) {
        try {
            Scheduler scheduler = SCHEDULER_FACTORY.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, groupName);
            scheduler.pauseTrigger(triggerKey); // 停止触发器
            scheduler.unscheduleJob(triggerKey);    // 移除触发器
            scheduler.deleteJob(JobKey.jobKey(jobName, groupName)); // 删除任务
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        //将数据库中的任务状态设为 取消
        return quartzService.modifyTaskStatus(jobName, "2");
    }

    public List<QuartzEntity> getAllJobs() throws SchedulerException {
        Scheduler scheduler = SCHEDULER_FACTORY.getScheduler();

        List<QuartzEntity> quartzJobs = new ArrayList<>();
        try {
            List<String> triggerGroupNames = scheduler.getTriggerGroupNames();
            for (String groupName : triggerGroupNames) {
                GroupMatcher<TriggerKey> groupMatcher = GroupMatcher.groupEquals(groupName);
                Set<TriggerKey> triggerKeySet = scheduler.getTriggerKeys(groupMatcher);
                for (TriggerKey triggerKey : triggerKeySet) {
                    Trigger trigger = scheduler.getTrigger(triggerKey);
                    JobKey jobKey = trigger.getJobKey();
                    JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                    //组装数据
                    QuartzEntity entity = new QuartzEntity();
                    entity.setJobName(jobDetail.getKey().getName());
                    entity.setGroupName(jobDetail.getKey().getGroup());
                    entity.setStartTime(LocalDateTimeUtil.of(trigger.getStartTime()));
                    entity.setEndTime(LocalDateTimeUtil.of(trigger.getStartTime()));
                    entity.setJobClass(jobDetail.getJobClass().getName());
                    if (trigger instanceof CronTrigger) {
                        entity.setCron(((CronTrigger) trigger).getCronExpression());
                    }
                    entity.setJobDataMapJson(JSONUtil.toJsonStr(jobDetail.getJobDataMap()));
                    quartzJobs.add(entity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return quartzJobs;
    }

    public void recoveryAllJob() {
        List<QuartzEntity> tasks = quartzService.notStartOrNotEndJobs();
        if (tasks != null && tasks.size() > 0) {
            for (QuartzEntity task : tasks) {
                try {
                    JobDataMap jobDataMap = JSONUtil.toBean(task.getJobDataMapJson(), JobDataMap.class);
                    JobDetail jobDetail = JobBuilder.newJob((Class<? extends Job>) Class.forName(task.getJobClass()))
                            .withIdentity(task.getJobName(), task.getGroupName())
                            .setJobData(jobDataMap).build();
                    TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
                    triggerBuilder.withIdentity(task.getJobName(), task.getGroupName());
                    triggerBuilder.startAt(toStartDate(task.getStartTime()));
                    triggerBuilder.endAt(toEndDate(task.getEndTime()));
                    if (StrUtil.isNotEmpty(task.getCron())) {
                        triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(task.getCron()));
                    }
                    Trigger trigger = triggerBuilder.build();
                    Scheduler scheduler = SCHEDULER_FACTORY.getScheduler();
                    scheduler.scheduleJob(jobDetail, trigger);
                    if (!scheduler.isShutdown()) {
                        scheduler.start();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static Date toEndDate(LocalDateTime endDateTime) {
        // endDateTime为null时转换会报空指针异常，所以需要进行null判断。
        // 结束时间可以为null，所以endDateTime为null，直接返回null即可
        return endDateTime != null ?
                DateUtil.date(endDateTime) : null;
    }

    private static Date toStartDate(LocalDateTime startDateTime) {
        // startDateTime为空时返回当前时间，表示立即开始
        return startDateTime != null ?
                DateUtil.date(startDateTime) : new Date();
    }

}