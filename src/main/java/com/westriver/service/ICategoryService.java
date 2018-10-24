package com.westriver.service;

import com.westriver.common.ServerResponse;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by zack on 2018/10/24.
 */
public interface ICategoryService {
    public ServerResponse<String> addCategory(String categoryName, Integer parentId);
    public ServerResponse<String> updateCategoryName(Integer categoryId, String categoryName);
}
