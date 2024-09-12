package com.econovation.recruitdomain.domains.applicant.event.domainevent;

import com.econovation.recruitdomain.common.aop.domainEvent.DomainEvent;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
public class ApplicantStateUpdateEvent extends DomainEvent {

    private final String applicantId;
    private final ApplicantStateEvents event;

    public static ApplicantStateUpdateEvent of(String applicantId, String eventName){
        return ApplicantStateUpdateEvent.builder()
                .applicantId(applicantId)
                .event(ApplicantStateEvents.find(eventName))
                .build();
    }

}
