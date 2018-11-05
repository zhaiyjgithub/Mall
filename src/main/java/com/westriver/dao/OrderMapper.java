package com.westriver.dao;
import com.westriver.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by zack on 2018/11/5.
 */
public interface OrderMapper {
    public int insertOrder(Order order);
    public Order selectOrderById(@Param("userId") Integer userId, @Param("orderNo") Long orderNo);
    public int updateOrder(Order order);
    public List<Order> selectOrderByUserId(Integer userId);
}
