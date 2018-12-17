package com.westriver.service.Impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.westriver.common.ServerResponse;
import com.westriver.dao.CategoryMapper;
import com.westriver.pojo.Category;
import com.westriver.service.ICategoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;

/**
 * Created by zack on 2018/10/24.
 */
@Service
public class ICategoryServiceImpl implements ICategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    public ServerResponse<String> addCategory(String categoryName, Integer parentId) {
        if (StringUtils.isEmpty(categoryName) || parentId == null) {
            return ServerResponse.createByErrorMessage("参数错误");
        }

        Category category = new Category();
        category.setName(categoryName);
        category.setStatus(true);
        category.setParentId(parentId);

        int addCategoryCount = categoryMapper.addCategory(category);
        if (addCategoryCount > 0) {
            return ServerResponse.createBySuccessMessage("添加成功");
            
        }

        return ServerResponse.createByErrorMessage("添加失败");

    }

    public ServerResponse<String> updateCategoryName(Integer categoryId, String categoryName) {
        if (categoryId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("参数错误");
        }

        Category category = new Category();
        category.setName(categoryName);
        category.setId(categoryId);

        int updateCount = categoryMapper.updateCategoryName(category);
        if (updateCount > 0) {
            return ServerResponse.createBySuccessMessage("更新成功");
        }

        return ServerResponse.createByErrorMessage("更新失败");
    }

    public ServerResponse<List<Category>> selectCategoryAndChildrenById(Integer categoryId){
        Set<Category> categorySet = Sets.newHashSet();
        findChildCategory(categorySet,categoryId);


        List<Category> categoryIdList = Lists.newArrayList();
        if(categoryId != null){
            for(Category categoryItem : categorySet){
                categoryIdList.add(categoryItem);
            }
        }
        return ServerResponse.createBySuccess(categoryIdList);
    }

    public ServerResponse<List<Integer>> selectCategoryIdAndChildrenById(Integer categoryId) {
        Set<Category> categorySet = Sets.newHashSet();
        findChildCategory(categorySet, categoryId);

        List<Integer> categoryList = Lists.newArrayList();

        for (Category categoryItem : categorySet) {
            categoryList.add(categoryItem.getId());
        }

        return ServerResponse.createBySuccess(categoryList);
    }


    //递归算法,算出子节点
    private Set<Category> findChildCategory(Set<Category> categorySet ,Integer categoryId){

        //查找子节点,递归算法一定要有一个退出的条件
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        if (categoryList.size() > 0) {
            for(Category categoryItem : categoryList){
                findChildCategory(categorySet,categoryItem.getId());
            }
        }else {
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if(category != null){
                categorySet.add(category);
            }
        }

        return categorySet;
    }

    public ServerResponse getParallelChildrenByParentId(Integer parentId) {
        if (parentId == null) {
            return ServerResponse.createByErrorMessage("参数错误");
        }

        List<Category> list = categoryMapper.selectParallelChildrenByParentId(parentId);

        return ServerResponse.createBySuccess(list);
    }
}
