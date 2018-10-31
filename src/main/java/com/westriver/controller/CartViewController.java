package com.westriver.controller;

import com.westriver.common.ServerResponse;
import com.westriver.service.Impl.ICartServiceImpl;
import com.westriver.service.Impl.ICategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zack on 2018/10/31.
 */
@RestController
public class CartViewController {
    @Autowired
    private ICartServiceImpl iCartServiceImpl;

    @RequestMapping(value = "/add_cart", method = RequestMethod.POST)
    public ServerResponse<String> addProductToCart(Integer userId, Integer productId, Integer quantity) {
       return iCartServiceImpl.addProductToCart(userId, productId, quantity);
    }
}
