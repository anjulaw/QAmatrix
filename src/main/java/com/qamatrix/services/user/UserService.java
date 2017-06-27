package com.qamatrix.services.user;

import java.util.List;

import org.springframework.data.domain.Page;

import com.qamatrix.backend.domain.user.User;
import com.qamatrix.dto.response.Response;
import com.qamatrix.dto.user.UserSearchCriteriaDTO;

public interface UserService {

	public User findUserByEmail(String email);

	public List<User> findActiveUsersByName(String name);
	
	public User getUserByUserName(String name);

	public void saveUser(User user);

	public Page<User> searchUsers(UserSearchCriteriaDTO userSearchCriteria);

	public Response<User> updateUser(User user);
}