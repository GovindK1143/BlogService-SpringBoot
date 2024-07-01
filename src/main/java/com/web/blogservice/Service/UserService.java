package com.web.blogservice.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.blogservice.Dao.UserRepository;
import com.web.blogservice.Model.User;

@Service
public class UserService {
	@Autowired
    private UserRepository userRepository;

    public void saveUser(User user) {
        userRepository.save(user);
    }

	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}	

}
