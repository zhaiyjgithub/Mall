package com.westriver.service.Impl;

import com.westriver.common.Const;
import com.westriver.common.ServerResponse;
import com.westriver.dao.CartMapper;
import com.westriver.dao.ProductMapper;
import com.westriver.pojo.Cart;
import com.westriver.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zack on 2018/10/31.
 */

@Service("ICartService")
public class ICartServiceImpl implements ICartService {

    @Autowired
    private CartMapper cartMapper;

    public ServerResponse<String> addProductToCart(Integer userId, Integer productId, Integer quantity) {
        if (productId == null || quantity == null) {
            return ServerResponse.createByErrorMessage("参数出错");
        }

        Cart cart = cartMapper.selectProductById(userId, productId);
        if (cart == null) {
            Cart cartItem = new Cart();
            cartItem.setUserId(userId);
            cartItem.setProductId(productId);
            cartItem.setQuantity(quantity);
            cartItem.setChecked(Const.Cart.CHECKED);

            cartMapper.addProduct(cartItem);
        }else {
            cart.setQuantity(cart.getQuantity() + quantity);
            cart.setChecked(Const.Cart.CHECKED);

            cartMapper.updateProduct(cart);
        }

        return ServerResponse.createBySuccessMessage("添加成功");
    }
}
