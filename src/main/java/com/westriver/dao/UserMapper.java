package com.westriver.dao;

import com.westriver.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by zack on 2018/10/15.
 */

public interface UserMapper {
   public List<User> list();
   public int checkUserName(String userName);
   public User selectLogin(@Param("username") String userName, @Param("password") String password);
   public int checkEmail(String email);
   public int insert(User user);
   public String selectQuestion(String username);
   public int checkAnswer(@Param("username") String username, @Param("question") String question, @Param("answer") String answer);
   public int updatePasswordByUsername(@Param("username") String username, @Param("passwordNew") String PasswordNew);
   public int checkPassword(@Param("passwordOld") String passwordOld, @Param("userId") Integer userId);
   public int updatePasswordByUserId(@Param("passwordNew") String passwordNew, @Param("userId") Integer userId);
   public User getUserInformationByUserId(Integer userId);
   public int checkEmailByUserId(@Param("email") String email, @Param("userId") Integer userId);
   public int updateUserInformation(User user);
}
