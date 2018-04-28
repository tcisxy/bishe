package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface UserRepository extends JpaRepository<User,Long>,JpaSpecificationExecutor<User> {
    User findByPhone(String phone);

    User findById(long id);

    @Query("update User set name = ?2,age = ?3,sex = ?4,phone = ?5 where id = ?1")
    @Modifying
    @Transactional
    void editUser(long id,String name,int age,int sex,String phone);
}
