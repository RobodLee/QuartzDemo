package com.robod.quartzdemo.service;

import com.robod.quartzdemo.entity.QuartzEntity;

import java.util.List;

/**
 * @author L.D.
 * @date 2022/8/15 11:29
 * @description
 */
public interface QuartzService {

    void save(QuartzEntity entity);

    boolean modifyJob(QuartzEntity entity);

    boolean modifyTaskStatus(String jobName,String status);

    List<QuartzEntity> notStartOrNotEndJobs();
}
