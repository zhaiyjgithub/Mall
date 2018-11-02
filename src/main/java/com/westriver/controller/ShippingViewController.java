package com.westriver.controller;

import com.westriver.common.Const;
import com.westriver.common.ServerResponse;
import com.westriver.pojo.Shipping;
import com.westriver.pojo.User;
import com.westriver.service.Impl.IShippingServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.swing.text.html.HTML;

/**
 * Created by zack on 2018/11/1.
 */
@RestController
public class ShippingViewController {
    @Autowired
    private IShippingServiceImpl shippingServiceImpl;

    @RequestMapping(value = "/add_address", method = RequestMethod.POST)
    public ServerResponse<String> addAddress(HttpSession session, Shipping shipping){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user != null) {
            shipping.setUserId(user.getId());
            return shippingServiceImpl.addAddress(shipping);
        }else {
            return ServerResponse.createByErrorMessage("请先登录");
        }

    }

    @RequestMapping(value = "/update_address", method = RequestMethod.POST)
    public ServerResponse<String> updateAddress(HttpSession session, Shipping shipping) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user != null) {
            shipping.setUserId(user.getId());
            return shippingServiceImpl.updateAddress(shipping);
        }else {
            return ServerResponse.createByErrorMessage("请先登录");
        }

    }


}
