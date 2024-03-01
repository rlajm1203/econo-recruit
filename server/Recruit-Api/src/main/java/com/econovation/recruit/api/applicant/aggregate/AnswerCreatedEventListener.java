package com.econovation.recruit.api.applicant.aggregate;

import com.econovation.recruitcommon.exception.RecruitCodeException;
import com.econovation.recruitdomain.common.aop.domainEvent.Events;
import com.econovation.recruitdomain.domains.applicant.domain.MongoAnswer;
import com.econovation.recruitdomain.domains.applicant.domain.MongoAnswerAdaptor;
import com.econovation.recruitdomain.domains.applicant.event.aggregateevent.AnswerCreatedEvent;
import com.econovation.recruitdomain.domains.applicant.event.domainevent.ApplicantRegisterEvent;
import com.econovation.recruitdomain.domains.applicant.exception.ApplicantDuplicateSubmitException;
import com.econovation.recruitdomain.domains.applicant.exception.ApplicantWrongPositionException;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AnswerCreatedEventListener {
    private final MongoAnswerAdaptor answerAdaptor;

    @Value("${econovation.year}")
    private Integer year;

    @EventHandler
    @Transactional
    public void handle(AnswerCreatedEvent event) {
        Map<String, Object> qna = event.getQna();
        MongoAnswer answer = new MongoAnswer(event.getId(), event.getYear(), qna);
        // 학번으로 중복 체크
        validateRegisterApplicant(qna);

        answerAdaptor.save(answer);

        String name = qna.get("name").toString().replace("\"", "");
        ;
        String hopeField = qna.get("field").toString().replace("\"", "");
        ;
        String email = qna.get("email").toString().replace("\"", "");
        ;

        // email 전송 event처리
        ApplicantRegisterEvent applicantRegisterEvent =
                ApplicantRegisterEvent.of(answer.getId(), name, hopeField, email);
        Events.raise(applicantRegisterEvent);
    }

    private Validation<Seq<RecruitCodeException>, Map<String, Object>> validateRegisterApplicant(
            Map<String, Object> qna) {
        return Validation.combine(validateDuplicateSudentId(qna), validateIsRightPosition(qna))
                .ap((a, b) -> qna);
    }

    private Validation<RecruitCodeException, Object> validateIsRightPosition(
            Map<String, Object> qna) {
        String field = qna.get("field").toString().replace("\"", "");
        ;
        if (field.equals("기획자") || field.equals("개발자") || field.equals("디자이너")) {
            return Validation.valid(qna);
        }
        throw ApplicantWrongPositionException.EXCEPTION;
    }

    private Validation<RecruitCodeException, Map<String, Object>> validateDuplicateSudentId(
            Map<String, Object> qna) {
        String studentId = qna.get("classOf").toString().replace("\"", "");
        ;
        if (answerAdaptor.existsByAnswer(studentId, year)) {
            throw ApplicantDuplicateSubmitException.EXCEPTION;
        }
        return Validation.valid(qna);
    }
}
