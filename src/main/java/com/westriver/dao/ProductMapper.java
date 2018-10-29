package com.westriver.dao;

import com.westriver.pojo.Product;
import org.apache.ibatis.annotations.Param;

public interface ProductMapper {
    public int saveProduct(Product product);
    public int updateProduct(Product product);
    public int checkProductById(Integer id);
    public int updateProductSaleStatus(@Param("productId") Integer productId , @Param("status") Integer status);
}
