package com.qamatrix.services.role;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import com.qamatrix.backend.domain.user.AbstractContent;
import com.qamatrix.backend.domain.user.Role;
import com.qamatrix.backend.domain.user.User;
import com.qamatrix.backend.repository.RoleRepository;
import com.qamatrix.backend.repository.UserRepository;
import com.qamatrix.dto.criteria.SortDirection;
import com.qamatrix.dto.response.Response;
import com.qamatrix.dto.user.UserSearchCriteriaDTO;
import javax.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

@Service("roleService")
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public List<Role> getRoles() {
		
		return roleRepository.findAll();
	}

	
	

}