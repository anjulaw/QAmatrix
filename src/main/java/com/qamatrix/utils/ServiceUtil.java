package com.qamatrix.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

import com.qamatrix.backend.domain.user.User;

public class ServiceUtil {

	public static List<User> mapUsersResults(List<User> results) {

		List<User> userList = new ArrayList<>();
		if (results != null) {

			for (User user : results) {
				userList.add(user);
			}
		}

		return userList;
	}
}
