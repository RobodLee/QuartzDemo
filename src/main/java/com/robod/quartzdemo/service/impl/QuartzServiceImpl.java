package com.robod.quartzdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.robod.quartzdemo.entity.QuartzEntity;
import com.robod.quartzdemo.mapper.QuartzMapper;
import com.robod.quartzdemo.service.QuartzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author L.D.
 * @date 2022/8/15 11:29
 * @description
 */
@Service("quartzService")
public class QuartzServiceImpl implements QuartzService {

    @Autowired
    private QuartzMapper quartzMapper;

    @Override
    public void save(QuartzEntity entity) {
        quartzMapper.insert(entity);
    }

    @Override
    public boolean modifyJob(QuartzEntity entity) {
        LambdaQueryWrapper<QuartzEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(QuartzEntity::getJobName, entity.getJobName());
        QuartzEntity one = quartzMapper.selectOne(wrapper);
        if (one != null) {
            entity.setId(one.getId());
            return quartzMapper.updateById(entity) > 0;
        }
        return false;
    }

    @Override
    public boolean modifyTaskStatus(String jobName, String status) {
        LambdaQueryWrapper<QuartzEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(QuartzEntity::getJobName, jobName);
        QuartzEntity one = quartzMapper.selectOne(wrapper);
        if (one != null) {
            one.setStatus(status);
            return quartzMapper.updateById(one) > 0;
        }
        return false;
    }

    @Override
    public List<QuartzEntity> notStartOrNotEndJobs() {
        return quartzMapper.notStartOrNotEndJobs();
    }

}
