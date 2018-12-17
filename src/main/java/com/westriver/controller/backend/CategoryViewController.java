package com.westriver.controller.backend;

import com.westriver.common.Const;
import com.westriver.common.ServerResponse;
import com.westriver.pojo.User;
import com.westriver.service.Impl.ICategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * Created by zack on 2018/10/24.
 */
@RestController
public class CategoryViewController {
    @Autowired
    private ICategoryServiceImpl iCategoryServiceImpl;

    @RequestMapping(value = "/add_category", method = RequestMethod.POST)
    public ServerResponse<String> addCategory(String categoryName, @RequestParam(value = "parentId", defaultValue = "0") Integer parentId, HttpSession session) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("请先登录");
        }

        return iCategoryServiceImpl.addCategory(categoryName, parentId);
    }

    @RequestMapping(value = "/update_category", method = RequestMethod.POST)
    public ServerResponse<String> updateCategoryName(Integer categoryId, String categoryName, HttpSession session) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return  ServerResponse.createByErrorMessage("请先登录");
        }

        return iCategoryServiceImpl.updateCategoryName(categoryId, categoryName);
    }

    @RequestMapping("/get_deep_category")
    @ResponseBody
    public ServerResponse getCategoryAndDeepChildrenCategory(HttpSession session,@RequestParam(value = "categoryId" ,defaultValue = "0") Integer categoryId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorMessage("用户未登录,请登录");
        }

        return iCategoryServiceImpl.selectCategoryAndChildrenById(categoryId);
    }

    @RequestMapping(value = "/get_children_category", method = RequestMethod.POST)
    public ServerResponse getParallelChildrenByParentId(Integer parentId, HttpSession session) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("请先登录");
        }

        return iCategoryServiceImpl.getParallelChildrenByParentId(parentId);
    }
}
