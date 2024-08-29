package com.econovation.recruitdomain.domains.applicant.domain;

import com.econovation.recruitdomain.domains.applicant.exception.ApplicantWrongStatusException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ApplicantStatus {

    NONPASSED("non-passed"),
    FIRSTPASSED("first-passed"),
    FINALPASSED("final-passed");

    private String status;

    ApplicantStatus(String status){ this.status = status; }

    public static ApplicantStatus findStatus(String status){
        return Arrays.stream(ApplicantStatus.values())
                .filter(s -> s.getStatus().equals(status))
                .findFirst()
                .orElseThrow(ApplicantWrongStatusException::new);
    }

}
