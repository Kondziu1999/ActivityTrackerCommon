package com.agh.activitytrackerclient.utils;


import com.agh.activitytrackerclient.models.ActivityUserDetails;

/**
 * Should be implemented by library user.
 * This is where plugin is looking for user details
 */
public interface ActivityUserDetailsProvider {
    ActivityUserDetails getAppActivityUserDetails(String userIdString);
}
