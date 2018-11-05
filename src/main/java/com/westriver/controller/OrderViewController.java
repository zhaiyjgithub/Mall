package com.westriver.controller;

import com.westriver.common.ServerResponse;
import com.westriver.service.Impl.OrderServiceImpl;
import com.westriver.vo.OrderVo;
import org.apache.ibatis.annotations.ResultMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by zack on 2018/11/5.
 */
@RestController
public class OrderViewController {
    @Autowired
    private OrderServiceImpl orderServiceImpl;

    @RequestMapping(value = "/create_order", method = RequestMethod.POST)
    public ServerResponse<OrderVo> createOrderVo(Integer userId, Integer shippingId) {
        return orderServiceImpl.createOrderVo(userId, shippingId);
    }

    @RequestMapping(value = "/get_order_list", method = RequestMethod.POST)
    public ServerResponse<List<OrderVo>> getOrderList(Integer userId) {
        return orderServiceImpl.getOrderList(userId);
    }

    @RequestMapping(value = "/get_order_detail", method = RequestMethod.POST)
    public ServerResponse<OrderVo> getOrderDetail(Integer userId, Long orderNo) {
        return orderServiceImpl.getOrderDetail(userId, orderNo);
    }

}
