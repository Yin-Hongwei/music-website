package com.example.yin.dao;

import com.example.yin.domain.Consumer;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsumerMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(Consumer record);

    /**
     * 增加新用户
     * @param record - 用户信息
     * @return int
     */
    int insertSelective(Consumer record);

    Consumer selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Consumer record);

    int updateByPrimaryKey(Consumer record);

    int verifyPassword(String username, String password);

    int existUsername(String username);

    int addUser(Consumer consumer);

    int updateUserMsg(Consumer record);

    int updateUserAvator(Consumer record);

    int updatePassword(Consumer record);

    int deleteUser(Integer id);

    List<Consumer> allUser();

    List<Consumer> userOfId(Integer id);

    List<Consumer> loginStatus(String username);

}
