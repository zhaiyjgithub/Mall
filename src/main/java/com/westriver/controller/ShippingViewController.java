package com.westriver.controller;

import com.westriver.common.ServerResponse;
import com.westriver.pojo.Shipping;
import com.westriver.service.Impl.IShippingServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zack on 2018/11/1.
 */
@RestController
public class ShippingViewController {
    @Autowired
    private IShippingServiceImpl shippingServiceImpl;

    @RequestMapping(value = "/add_address", method = RequestMethod.POST)
    public ServerResponse<String> addAddress(Shipping shipping){
        return shippingServiceImpl.addAddress(shipping);
    }

    @RequestMapping(value = "/update_address", method = RequestMethod.POST)
    public ServerResponse<String> updateAddress(Shipping shipping) {
        return shippingServiceImpl.updateAddress(shipping);
    }


}
