package com.robod.quartzdemo.service.impl;

import com.robod.quartzdemo.entity.Order;
import com.robod.quartzdemo.entity.QuartzGroupEnum;
import com.robod.quartzdemo.job.DepartureNoticeJob;
import com.robod.quartzdemo.service.OrderService;
import com.robod.quartzdemo.util.QuartzUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author L.D.
 * @date 2022/8/15 11:21
 * @description
 */
@Service("orderService")
public class OrderServiceImpl implements OrderService {

    @Autowired
    private QuartzUtil quartzUtil;

    @Override
    public String bookTicket(String userId, String ticketId) {
        //TODO 查询余票下订单等一些列操作
        Order order = new Order();
        //…………
        //创建一个定时任务
        LocalDateTime noticeTime = order.getDepartureTime().minusHours(2); //通知时间为发车前两小时
        quartzUtil.addJob(String.valueOf(order.getId()), QuartzGroupEnum.DEPARTURE_NOTICE.getValue(),
                DepartureNoticeJob.class, noticeTime, null, null, null);
        return "";
    }

    @Override
    public String rebook(Order order) {
        //TODO 修改订单等一系列操作
        //修改定时任务
        LocalDateTime noticeTime = order.getDepartureTime().minusHours(2); //通知时间为发车前两小时
        quartzUtil.modifyJobTime(String.valueOf(order.getId()), QuartzGroupEnum.DEPARTURE_NOTICE.getValue(),
                noticeTime, null, null);
        return "";
    }

    @Override
    public String cancelOrder(Order order) {
        //TODO 取消订单等一系列操作
        //取消定时任务
        quartzUtil.cancelJob(String.valueOf(order.getId()), QuartzGroupEnum.DEPARTURE_NOTICE.getValue());
        return "";
    }

}
