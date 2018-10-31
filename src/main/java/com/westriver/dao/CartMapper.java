package com.westriver.dao;

import com.westriver.pojo.Cart;
import org.apache.ibatis.annotations.Param;

/**
 * Created by zack on 2018/10/31.
 */
public interface CartMapper {
    public int addProduct(Cart cart);
    public int selectProductById(@Param("userId") Integer userId, @Param("productId") Integer productId);
    public int updateProduct(Cart cart);
}
