package com.robod.quartzdemo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author L.D.
 * @date 2022/8/15 11:16
 * @description
 */
@Data
public class QuartzEntity {

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    private String jobName;

    private String groupName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endTime;

    private String jobClass;

    private String cron;

    private String jobDataMapJson;

    private String status;

}
