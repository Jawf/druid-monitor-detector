package com.druid.monitor.detector.model;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;

/**
 * Repository for the entity User.
 * 
 * @see netgloo.models.UserBaseRepository
 */
@Transactional
public interface UserRepository extends UserBaseRepository<User> {

	@Query(value = "select * from users", nativeQuery = true)
	List<User> findUserByQuery();
}
