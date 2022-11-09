package com.agh.activitytrackerclient.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivityUserWithOverviewStatistics {
    private ActivityUser user;
    private Long activitiesCount;
}
