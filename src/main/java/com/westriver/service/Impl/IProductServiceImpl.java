package com.westriver.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.westriver.common.ServerResponse;
import com.westriver.dao.CategoryMapper;
import com.westriver.dao.ProductMapper;
import com.westriver.pojo.Category;
import com.westriver.pojo.Product;
import com.westriver.service.IProductService;
import com.westriver.vo.ProductDetailVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.server.ServerErrorException;

import javax.rmi.PortableRemoteObject;
import java.util.List;

@Service
public class IProductServiceImpl implements IProductService {
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    public ServerResponse<String> saveOrUpdateProduct(Product product) {
        if (product != null) {
            if (product.getId() != null) { //更新产品
                if (productMapper.checkProductById(product.getId()) > 0) {
                    if (productMapper.updateProduct(product) > 0) {
                        return ServerResponse.createByErrorMessage("更新商品成功");
                    }else {
                        return ServerResponse.createByErrorMessage("更新产品失败");
                    }
                }else {
                    return ServerResponse.createByErrorMessage("没有该商品");
                }
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

    public ServerResponse<String> updateProductSaleStatus(Integer productId , Integer status) {
        if (productMapper.checkProductById(productId) > 0) {
            if (productMapper.updateProductSaleStatus(productId, status) > 0) {
                return ServerResponse.createBySuccessMessage("更新商品状态成功");
            }else {
                return ServerResponse.createByErrorMessage("更新商品状态失败");
            }
        }else {
            return ServerResponse.createByErrorMessage("没有该商品");
        }
    }

    public ServerResponse<ProductDetailVo> managerProductDetail(Integer productId) {
        Product product = productMapper.selectProductById(productId);
        if (product == null) {
            return ServerResponse.createByErrorMessage("没有该商品");
        }

        return ServerResponse.createBySuccess(assembleProductDetailVo(product));
    }

    private ProductDetailVo assembleProductDetailVo(Product product) {
        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setName(product.getName());
        productDetailVo.setSubTitle(product.getSubTitle());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImages(product.getSubImages());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setStock(product.getStock());
        productDetailVo.setStatus(product.getStatus());

        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category == null) {
            productDetailVo.setParentCategoryId(0);
        }else {
            productDetailVo.setParentCategoryId(category.getParentId());
        }

        productDetailVo.setCreateTime(product.getCreateTime());
        productDetailVo.setUpdateTime(product.getUpdateTime());

        return productDetailVo;
    }

    public ServerResponse<PageInfo> getProductList(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Product> productList = productMapper.selectList();

        List<ProductDetailVo> productDetailVoList = Lists.newArrayList();
        for (Product product : productList) {
            ProductDetailVo productDetailVo = assembleProductDetailVo(product);
            productDetailVoList.add(productDetailVo);
        }

        PageInfo pageResult = new PageInfo(productDetailVoList);
        pageResult.setList(productDetailVoList);

        return ServerResponse.createBySuccess(pageResult);
    }

    public ServerResponse<PageInfo> searchProduct(String productName, Integer productId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        if (StringUtils.isNotEmpty(productName)) {
            productName = new StringBuilder().append("%").append(productName).append("%").toString();
        }

        List<Product> productList = productMapper.searchProduct(productName, productId);

        List<ProductDetailVo> productDetailVoList = Lists.newArrayList();

        for (Product product : productList) {
            ProductDetailVo productDetailVo = assembleProductDetailVo(product);
            productDetailVoList.add(productDetailVo);
        }

        PageInfo pageInfo = new PageInfo(productList);

        return ServerResponse.createBySuccess(pageInfo);
    }

}
