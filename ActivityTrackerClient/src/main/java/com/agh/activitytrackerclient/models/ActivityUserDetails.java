package com.agh.activitytrackerclient.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;


@Getter
@Setter
@Builder
@AllArgsConstructor
public class ActivityUserDetails {
    private String name;
    private String username;

    @Email
    private String email;
}
