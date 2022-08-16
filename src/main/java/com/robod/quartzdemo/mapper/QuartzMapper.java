package com.robod.quartzdemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.robod.quartzdemo.entity.QuartzEntity;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author L.D.
 * @date 2022/8/15 11:30
 * @description
 */
@Repository
public interface QuartzMapper extends BaseMapper<QuartzEntity> {

    @Select("SELECT " +
            " *  " +
            "FROM " +
            " quartz_entity  " +
            "WHERE " +
            " ( end_time IS NULL  " +                                  // 没有结束时间的
            "  OR ( start_time < NOW() AND end_time > NOW())  " +      // 已经开始但未结束的
            "  OR start_time > NOW()  " +                              // 还未开始的
            " )  " +
            " AND `status` = '0'")
    List<QuartzEntity> notStartOrNotEndJobs();

}
