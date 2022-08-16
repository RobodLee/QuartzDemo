package com.robod.quartzdemo.service;

import com.robod.quartzdemo.entity.Order;

/**
 * @author L.D.
 * @date 2022/8/15 11:21
 * @description
 */
public interface OrderService {

    String bookTicket(String userId, String ticketId);

    String rebook(Order order);

    String cancelOrder(Order order);

}
