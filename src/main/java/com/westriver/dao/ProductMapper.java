package com.westriver.dao;

import com.westriver.pojo.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    public int saveProduct(Product product);
    public int updateProduct(Product product);
    public int checkProductById(Integer id);
    public int updateProductSaleStatus(@Param("productId") Integer productId , @Param("status") Integer status);
    public Product selectProductById(Integer productId);
    public List<Product> selectList();
    public List<Product> searchProduct(@Param("productName") String productName, @Param("productId") Integer productId);
    public List<Product> searchProductByCategoryId(@Param("productName") String productName, @Param("categoryIdList") List<Integer> categoryIdList);

}
