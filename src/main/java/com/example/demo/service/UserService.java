package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.param.QueryParam;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {
    List<User> getUserList();

    void save(User user);

    User getUserByPhone(String phone);

    User getUserById(Long id);

    List<User> getUserByParam(QueryParam queryParam);

    void edit(User user);

    void delete(Long id);

    @Transactional
    void checkUserMoney();

    long count();
}
