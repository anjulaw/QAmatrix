package com.qamatrix.backend.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.qamatrix.backend.domain.user.User;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
	User findByEmail(String email);

	@Query("SELECT u FROM User u WHERE u.active= 1 and u.name like :name%")
    public List<User> findActiveUsersByName(@Param("name") String name);
	
	@Query("SELECT u FROM User u WHERE u.active= 1 and u.name=:username")
	 public User getUserByUserName(@Param("username") String username);
	
}