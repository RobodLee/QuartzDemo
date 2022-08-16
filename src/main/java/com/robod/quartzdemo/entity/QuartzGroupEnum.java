package com.robod.quartzdemo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author L.D.
 * @date 2022/8/15 11:10
 * @description
 */
@Getter
@AllArgsConstructor
public enum QuartzGroupEnum {

    T1( "测试分组1"),
    T2("测试分组2"),
    DEPARTURE_NOTICE("发车通知");

    private final String value;

}