package com.qamatrix.services.user;

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

@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public void saveUser(User user) {
		// TODO Auto-generated method stub

		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setActive(1);
		Role userRole = roleRepository.findByRole("ADMIN");
		user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
		userRepository.save(user);
	}

	public List<User> findActiveUsersByName(String name) {
		// TODO Auto-generated method stub
		return userRepository.findActiveUsersByName(name);
	}

	public Page<User> searchUsers(UserSearchCriteriaDTO criteria) {

		int page = criteria.getStart() / criteria.getSize();
		Sort.Direction sortDirection = ( criteria.getSortDirection() != null && criteria.getSortDirection() == SortDirection.DESC ) ? Sort.Direction.DESC : Sort.Direction.ASC;
		PageRequest pageRequest = new PageRequest( page, criteria.getSize(), sortDirection, criteria.getSortBy() );

		Page<User> results = userRepository.findAll( ( Root<User> root, CriteriaQuery<?> cq, CriteriaBuilder cb ) -> {
			List<Predicate> predicates = new ArrayList<>();


			if ( !StringUtils.isEmpty( criteria.getUsername()) )
			{
				StringBuffer sb = new StringBuffer();
				sb.append( criteria.getUsername() ).append( "%" );
				predicates.add( cb.like( cb.upper( root.get( "username" ) ), sb.toString().toUpperCase() ) );
			}
			
			if ( !StringUtils.isEmpty( criteria.getFirstName()) )
			{
				StringBuffer sb = new StringBuffer();
				sb.append( criteria.getFirstName() ).append( "%" );
				predicates.add( cb.like( cb.upper( root.get( "name" ) ), sb.toString().toUpperCase() ) );
			}

			if ( !StringUtils.isEmpty( criteria.getLastName() ) )
			{
				StringBuffer sb = new StringBuffer();
				sb.append( criteria.getLastName() ).append( "%" );
				predicates.add( cb.like( cb.upper( root.get( "lastName" ) ), sb.toString().toUpperCase() ) );
			}
			
			if ( !StringUtils.isEmpty( criteria.getEmail() ) )
			{
				StringBuffer sb = new StringBuffer();
				sb.append( criteria.getEmail() ).append( "%" );
				predicates.add( cb.like( cb.upper( root.get( "email" ) ), sb.toString().toUpperCase() ) );
			}

			if(!StringUtils.isEmpty(criteria.getRole())){

				Join<Role,User> roleJoin =  root.join("roles");
				predicates.add(cb.equal(roleJoin.get("id"), criteria.getRole() ) );
			}
			

			predicates.add( cb.equal( root.get( "active" ), criteria.getActive()) );

			return cb.and( predicates.toArray( new Predicate[] {} ) );
		}, pageRequest );
		
		

		return results;
	}

	@Override
	public Response<User> updateUser(User user) {
		
		Response<User> response = null;

		switch ( user.getStatus())
		{
			case AbstractContent.NEW:
				response = insert( user );
				break;
			case AbstractContent.MODIFIED:
				response = modify( user );
				break;
			case AbstractContent.DELETED:
				//response = remove( user );
				break;
			default:
				break;

		}
		return response;
	}
	
	@Transactional
	public Response<User> insert( User user )
	{
		Response<User> response = new Response<>();
		
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setActive(1);
		
		if(user.getRoles()!=null){
			
			for (Role role : user.getRoles()) {
				
				Role userRole = roleRepository.findByRole(role.getRole());
				user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
			}
			
		}
		
		User userx = userRepository.save(user);
		response.setData(userx);
		response.setData(user);
		
		
		return response;
	}
	
	@Transactional
	public Response<User> modify( User user )
	{
		Response<User> response = new Response<>();
		User userx = userRepository.save(user);
		response.setData(userx);
		response.setData(user);
		return response;
	}

	@Override
	public User getUserByUserName(String name) {
		return userRepository.getUserByUserName(name);
	}

}