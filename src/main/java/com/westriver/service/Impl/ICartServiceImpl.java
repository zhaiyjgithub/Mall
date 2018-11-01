package com.westriver.service.Impl;

import com.google.common.collect.Lists;
import com.westriver.common.Const;
import com.westriver.common.ServerResponse;
import com.westriver.dao.CartMapper;
import com.westriver.dao.ProductMapper;
import com.westriver.pojo.Cart;
import com.westriver.pojo.Product;
import com.westriver.service.ICartService;
import com.westriver.util.BigDecimalUtil;
import com.westriver.vo.CartProductVo;
import com.westriver.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by zack on 2018/10/31.
 */

@Service("ICartService")
public class ICartServiceImpl implements ICartService {

    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;


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

//    public ServerResponse<String> updateProductQuantity(Integer userId, Integer productId, Integer quantity) {
//        if (userId == null || productId == null || quantity == null) {
//            return ServerResponse.createByErrorMessage("参数出错");
//        }
//
//        Cart cartItem = cartMapper.selectProductById(userId, productId);
//        if (cartItem == null) {
//            return ServerResponse.createByErrorMessage("找不到该商品")
//        }
//    }

    public ServerResponse<CartVo> getCartProductVoList(Integer userId) {
        if (userId == null) {
            return ServerResponse.createByErrorMessage("参数出错");
        }

        List<CartProductVo> cartProductVoList = Lists.newArrayList();
        BigDecimal cartTotalPrice = new BigDecimal("0");

        List<Cart> cartList = cartMapper.selectByUserId(userId);
        for (Cart cart : cartList) {
            CartProductVo cartProductVo = new CartProductVo();
            cartProductVo.setId(cart.getId());
            cartProductVo.setUserId(cart.getUserId());
            cartProductVo.setProductId(cartProductVo.getProductId());

            Product product = productMapper.selectProductById(cart.getProductId());
            if (product != null) {
                cartProductVo.setProductName(product.getName());
                cartProductVo.setProductMainImage(product.getMainImage());
                cartProductVo.setProductSubImages(product.getSubImages());
                cartProductVo.setProductDetail(product.getDetail());
                cartProductVo.setProductPrice(product.getPrice());
                cartProductVo.setStock(product.getStock());

                int buyLimitCount = 0;
                if (product.getStock() > cart.getQuantity()) { //库存充足
                    buyLimitCount = cart.getQuantity();

                }else { //库存不充足，那么同时要更新购物车的数量
                    buyLimitCount = product.getStock();

                    Cart updateCart = new Cart();
                    updateCart.setId(cart.getId());
                    updateCart.setQuantity(buyLimitCount);
                    cartMapper.updateProduct(updateCart);
                }

                cartProductVo.setQuantity(buyLimitCount);

                double signlePrice = product.getPrice().doubleValue();
                int number = cartProductVo.getQuantity();

                BigDecimal productPrice = BigDecimalUtil.mul(signlePrice, cartProductVo.getQuantity());
                cartProductVo.setProductTotalPrice(productPrice);
                cartProductVo.setProductChecked(cart.isChecked());
            }

            if (cartProductVo.getProductChecked()) {
                cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(), cartProductVo.getProductTotalPrice().doubleValue());
            }

            cartProductVoList.add(cartProductVo);
        }

        CartVo cartVo = new CartVo();
        cartVo.setTotalPrice(cartTotalPrice);
        cartVo.setCartProductVoList(cartProductVoList);
        cartVo.setAllChecked(true);

        return ServerResponse.createBySuccess(cartVo);
    }
}
