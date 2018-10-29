package com.westriver.controller.backend;

import com.sun.org.apache.regexp.internal.RE;
import com.westriver.common.Const;
import com.westriver.common.ServerResponse;
import com.westriver.pojo.Product;
import com.westriver.pojo.User;
import com.westriver.service.Impl.IProductServiceImpl;
import org.apache.ibatis.annotations.ResultMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * Created by zack on 2018/10/29.
 */
@RestController
public class ProductViewController {
    @Autowired
    private IProductServiceImpl iProductService;

    @RequestMapping(value = "/add_update_product", method = RequestMethod.POST)
    public ServerResponse<String> saveOrUpdateProduct(HttpSession session, Product product) {
        User user =(User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("请先登录");
        }

        return iProductService.saveOrUpdateProduct(product);
    }

    @RequestMapping(value = "/update_sale_status", method = RequestMethod.POST)
    public ServerResponse<String> updateProductSaleStatus(HttpSession session, Integer productId, Integer status) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("请先登录");
        }

        return iProductService.updateProductSaleStatus(productId, status);
    }
}
