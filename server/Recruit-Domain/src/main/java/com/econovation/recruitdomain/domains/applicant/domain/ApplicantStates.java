package com.econovation.recruitdomain.domains.applicant.domain;

import com.econovation.recruitdomain.domains.applicant.exception.ApplicantWrongStateException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ApplicantStates {

    NONPASSED("non-passed"),
    FIRSTPASSED("first-passed"),
    FINALPASSED("final-passed");

    private String status;

    ApplicantStates(String status){ this.status = status; }

    public static ApplicantStates findStatus(String status){
        return Arrays.stream(ApplicantStates.values())
                .filter(s -> s.getStatus().equals(status))
                .findFirst()
                .orElseThrow(ApplicantWrongStateException::new);
    }

}
