package com.econovation.recruitdomain.domains.applicant.event.domainevent;

import lombok.Getter;

@Getter
public enum ApplicantStateEvents {

    NON_PASS("non-pass"),
    PASS("pass");


    private String event;

    ApplicantStateEvents(String event){ this.event = event; }

}
