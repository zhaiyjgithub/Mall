package com.westriver.dao;

import com.westriver.pojo.Shipping;

/**
 * Created by zack on 2018/11/1.
 */
public interface ShippingMapper {
    public int addAddress(Shipping shipping);
    public int selectAddressById(Integer shippingId);
    public int updateAddress(Shipping shipping);
    public Shipping selectById(Integer shippingId);
}
