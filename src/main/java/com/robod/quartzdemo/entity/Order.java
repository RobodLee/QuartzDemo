package com.robod.quartzdemo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author L.D.
 * @date 2022/8/15 11:23
 * @description
 */
@Data
public class Order {

    private Long id;

    private Long userId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private LocalDateTime departureTime;

    //…………

}
