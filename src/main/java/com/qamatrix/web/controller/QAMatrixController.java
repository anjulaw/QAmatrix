package com.qamatrix.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.print.ServiceUI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qamatrix.backend.domain.user.Role;
import com.qamatrix.backend.domain.user.User;
import com.qamatrix.services.role.RoleService;
import com.qamatrix.services.user.UserService;
import com.qamatrix.utils.ServiceUtil;
import com.qamatrix.dto.response.Response;
import com.qamatrix.dto.user.UserDTO;
import com.qamatrix.dto.user.UserSearchCriteriaDTO;

@Controller
@RequestMapping("/v1")
public class QAMatrixController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;

	private static final Logger LOG = LoggerFactory.getLogger(QAMatrixController.class);
	
	@RequestMapping(value = "/loginuserdetails", method = RequestMethod.GET)
	@ResponseBody
	public Response loginUserDetails(ModelMap model) {
		
		Response<List<User>> response = new Response<>();

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName(); // get logged in username	
		
		//String name = "supun";
		
		User user = userService.getUserByUserName(name);
		List<User> list = new ArrayList<>();
		list.add(user);
		
		response.setData(list);	
		return response;

	}

	@RequestMapping(value = "/users/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Response findActiveUsersByName(@PathVariable("name") String name) {

		Response<User> dto = new Response<User>();

		List<User> userList = userService.findActiveUsersByName(name);

		//dto.setData(userList);

		return dto;
	}
	

	@RequestMapping(value = "/roles", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Response getRoles() {

		Response<List<Role>> response = new Response<>();
		
		try {
			List<Role> roleList = roleService.getRoles();
			response.setData(roleList);
			response.setStatus(Response.SUCCESS);
		} catch (Exception ex) {
			response = new Response<>();
			response.setStatus(Response.ERROR);
		}
		
		return response;
		
	}

	/*
	 * @RequestMapping(value = "/users", method = RequestMethod.GET)
	 * 
	 * @ResponseBody public String users(UserDTO user) {
	 * LOG.info("criteria: {}", user); return "OK"; }
	 */

	@RequestMapping(value = "/usersearch", method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Response users(UserSearchCriteriaDTO userSearchCriteria) {
		LOG.info("criteria: {}", userSearchCriteria);

		Response<List<User>> response = new Response<>();

		try {
			Page<User> results = userService.searchUsers(userSearchCriteria);
			List<User> userList = new ArrayList<>();
			userList = ServiceUtil.mapUsersResults(results.getContent());
			response.setData(userList);
			response.setStatus(Response.SUCCESS);
		} catch (Exception ex) {
			response = new Response<>();
			response.setStatus(Response.ERROR);
		}

		return response;
	}
	
	@RequestMapping(value = "/userupdate", method = { RequestMethod.PUT }, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Response<User> update( @RequestBody User user ){
		
		Response<User> response = new Response<>();

		try {
			response = userService.updateUser(user);
			response.setStatus(Response.SUCCESS);
		} catch (Exception ex) {
			response = new Response<>();
			response.setStatus(Response.ERROR);
		}

		return response;
		
	}

}