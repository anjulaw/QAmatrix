package com.qamatrix.services.role;

import java.util.List;

import org.springframework.data.domain.Page;

import com.qamatrix.backend.domain.user.Role;
import com.qamatrix.backend.domain.user.User;
import com.qamatrix.dto.response.Response;
import com.qamatrix.dto.user.UserSearchCriteriaDTO;

public interface RoleService {
	
	public List<Role> getRoles();
	
}