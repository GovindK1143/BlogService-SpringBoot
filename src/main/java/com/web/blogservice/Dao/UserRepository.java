package com.web.blogservice.Dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.web.blogservice.Model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByUsername(String username);

}
