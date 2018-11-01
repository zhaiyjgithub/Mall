package com.westriver.controller;

import com.westriver.common.ServerResponse;
import com.westriver.service.Impl.ICartServiceImpl;
import com.westriver.service.Impl.ICategoryServiceImpl;
import com.westriver.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.*;

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

    @RequestMapping(value = "/get_cart_list", method = RequestMethod.POST)
    public ServerResponse<CartVo> getCartProductVoList(Integer userId) {
        return iCartServiceImpl.getCartProductVoList(userId);
    }

    @RequestMapping(value = "update_product", method = RequestMethod.POST)
    public ServerResponse<CartVo> updateProductQuantity(Integer userId, Integer productId, Integer quantity) {
        return iCartServiceImpl.updateProductQuantity(userId, productId, quantity);
    }

    @RequestMapping(value = "/delete_product", method = RequestMethod.POST)
    public ServerResponse<CartVo> deleteProduct(Integer userId, Integer productId) {
        return iCartServiceImpl.deleteProduct(userId, productId);
    }

}
