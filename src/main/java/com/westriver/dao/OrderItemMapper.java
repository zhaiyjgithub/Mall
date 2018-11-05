package com.westriver.dao;

import com.westriver.pojo.OrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by zack on 2018/11/5.
 */
public interface OrderItemMapper {
    public int batchInsert(@Param("orderItemList") List<OrderItem> orderItemList);
    public List<OrderItem> selectOrderItemList(@Param("userId") Integer userId, @Param("orderNo") Long orderNo);
}
