package com.econovation.recruitdomain.domains.applicant.exception;

import com.econovation.recruitcommon.exception.RecruitCodeException;

public class ApplicantWrongStatusException extends RecruitCodeException {

    public static ApplicantWrongStatusException wrongStatusException = new ApplicantWrongStatusException();

    public ApplicantWrongStatusException(){ super(ApplicantErrorCode.APPLICANT_WRONG_STATUS); }

}
