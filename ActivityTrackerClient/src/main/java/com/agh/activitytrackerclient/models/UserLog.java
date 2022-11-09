package com.agh.activitytrackerclient.models;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "user_logs")
@Getter
@Setter
public class UserLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "activity_user_id")
    private String activityUserId;

    @Column(name="user_session_id")
    private String userSessionId;

    @Column(name="acitivity_start")
    private Long activityStart;

    @Column(name="acitivity_end")
    private Long activityEnd;

    @Column(name = "endpoint")
    private String endpoint;
}
