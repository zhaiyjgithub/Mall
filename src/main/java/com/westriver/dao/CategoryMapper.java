package com.westriver.dao;

import com.westriver.pojo.Category;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by zack on 2018/10/24.
 */
public interface CategoryMapper {
    public int addCategory(Category category);
    public int updateCategoryName(Category category);
    public Category selectByPrimaryKey(Integer id);
    public List<Category> selectCategoryChildrenByParentId(Integer parentId);
}
