package com.agh.activitytrackerclient.repository;

import com.agh.activitytrackerclient.models.ActivityUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityUserClientRepository extends JpaRepository<ActivityUser, String> {

}
