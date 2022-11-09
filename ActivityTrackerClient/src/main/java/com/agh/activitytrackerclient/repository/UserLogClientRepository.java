package com.agh.activitytrackerclient.repository;

import com.agh.activitytrackerclient.models.UserLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserLogClientRepository extends JpaRepository<UserLog, Integer> {
}
