package com.robod.quartzdemo.controller;

import com.robod.quartzdemo.entity.QuartzEntity;
import com.robod.quartzdemo.entity.QuartzGroupEnum;
import com.robod.quartzdemo.job.TemplateJob;
import com.robod.quartzdemo.util.QuartzUtil;
import org.quartz.JobDataMap;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author L.D.
 * @date 2022/8/15 15:57
 * @description
 */
@RestController
@RequestMapping("quartz")
public class QuartzController {

    @Autowired
    private QuartzUtil quartzUtil;

    @PostMapping("/testAdd")
    public String testAdd(@RequestBody QuartzEntity entity) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("k1", 666);
        boolean result = quartzUtil.addJob(entity.getJobName(), QuartzGroupEnum.T1.getValue(), TemplateJob.class,
                entity.getStartTime(), entity.getEndTime(), entity.getCron(), jobDataMap);
        return result ? "添加成功" : "添加失败";
    }

    @PostMapping("/testModify")
    public String testModify(@RequestBody QuartzEntity entity) {
        boolean result = quartzUtil.modifyJobTime(entity.getJobName(), QuartzGroupEnum.T1.getValue(),
                entity.getStartTime(), entity.getEndTime(), entity.getCron());
        return result ? "修改成功" : "修改失败";
    }

    @PostMapping("/testCancel")
    public String testCancelTime(@RequestBody QuartzEntity entity) {
        boolean result = quartzUtil.cancelJob(entity.getJobName(), QuartzGroupEnum.T1.getValue());
        return result ? "操作成功" : "操作失败";
    }

    @GetMapping("/getAllJobs")
    public List<QuartzEntity> getAllJobs() throws SchedulerException {
        return quartzUtil.getAllJobs();
    }

}
