package com.econovation.recruit.api.applicant.handler;

import com.econovation.recruitdomain.domains.applicant.domain.MongoAnswer;
import com.econovation.recruitdomain.domains.applicant.domain.MongoAnswerAdaptor;
import com.econovation.recruitdomain.domains.applicant.event.domainevent.ApplicantStateEvents;
import com.econovation.recruitdomain.domains.applicant.event.domainevent.ApplicantStateUpdateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ApplicantStateUpdateEventHandler {

    private final MongoAnswerAdaptor answerAdaptor;

    public String handle(ApplicantStateUpdateEvent event){
        MongoAnswer answer = answerAdaptor.findById(event.getApplicantId()).get();
        ApplicantStateEvents command = event.getEvent();

        switch (command) {
            case PASS:
                answer.pass();
                break;
            case NON_PASS:
                answer.nonPass();
                break;
        }

        answerAdaptor.save(answer);
        return answer.getApplicantState().getPassState();
    }

}
