package com.westriver.service.Impl;

import com.westriver.common.ServerResponse;
import com.westriver.dao.ProductMapper;
import com.westriver.pojo.Product;
import com.westriver.service.IProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class IProductServiceImpl implements IProductService {
    @Autowired
    private ProductMapper productMapper;

    public ServerResponse<String> saveOrUpdateProduct(Product product) {
        if (product != null) {
            if (product.getId() != null) { //更新产品
                return ServerResponse.createByErrorMessage("更新商品成功");
            }else {//添加产品
                int insertProductCount = productMapper.saveProduct(product);
                if (insertProductCount > 0) {
                    return ServerResponse.createBySuccessMessage("添加商品成功");
                }else {
                    return ServerResponse.createByErrorMessage("添加商品失败");
                }
            }
        }else {
            return ServerResponse.createByErrorMessage("参数错误");
        }
    }
}
