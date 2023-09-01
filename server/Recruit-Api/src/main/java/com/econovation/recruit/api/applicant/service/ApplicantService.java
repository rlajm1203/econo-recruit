package com.econovation.recruit.api.applicant.service;

import com.econovation.recruit.api.applicant.usecase.ApplicantRegisterUseCase;
import com.econovation.recruitcommon.utils.Result;
import com.econovation.recruitdomain.common.aop.domainEvent.Events;
import com.econovation.recruitdomain.domain.applicant.Applicant;
import com.econovation.recruitdomain.domains.applicant.adaptor.AnswerAdaptor;
import com.econovation.recruitdomain.domains.applicant.adaptor.QuestionAdaptor;
import com.econovation.recruitdomain.domains.applicant.domain.Answer;
import com.econovation.recruitdomain.domains.applicant.domain.Question;
import com.econovation.recruitdomain.domains.applicant.dto.BlockRequestDto;
import com.econovation.recruitdomain.domains.applicant.event.ApplicantRegisterEvent;
import com.econovation.recruitdomain.domains.applicant.exception.ApplicantDuplicateSubmitException;
import com.econovation.recruitdomain.domains.applicant.exception.QuestionNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplicantService implements ApplicantRegisterUseCase {
    private final QuestionAdaptor questionAdaptor;
    private final AnswerAdaptor answerAdaptor;

    @Override
    @Transactional
    public UUID execute(List<BlockRequestDto> blocks) {
        List<Question> questions = questionAdaptor.findAll();
        UUID applicantId = UUID.randomUUID();

        List<Answer> results =
                blocks.stream()
                        .map(
                                block -> {
                                    Result<Question> matchingQuestionResult =
                                            questions.stream()
                                                    .filter(
                                                            question ->
                                                                    question.getName()
                                                                            .equals(
                                                                                    block
                                                                                            .getName()))
                                                    .findFirst()
                                                    .map(Result::success)
                                                    .orElseThrow(
                                                            () ->
                                                                    QuestionNotFoundException
                                                                            .EXCEPTION);
                                    return matchingQuestionResult
                                            .map(
                                                    question ->
                                                            Answer.builder()
                                                                    .question(question)
                                                                    .answer(block.getAnswer())
                                                                    .applicantId(
                                                                            applicantId.toString())
                                                                    .build())
                                            .getValue();
                                })
                        .collect(Collectors.toList());
        // classOf 가 기존에 존재하면 중복된 지원자 입니다.
        String studentId =
                results.stream()
                        .filter(answer -> answer.getQuestion().getName().equals("classOf"))
                        .findFirst()
                        .get()
                        .getAnswer();

        // 이미 제출한 학생은 중복 지원자입니다. (학번으로 검증)
        if (answerAdaptor.findByAnswer(studentId) != null)
            throw ApplicantDuplicateSubmitException.EXCEPTION;

        // Result 를 save 하게 된다.
        answerAdaptor.saveAll(results);

        String hopeField =
                results.stream()
                        .filter(answer -> answer.getQuestion().getName().equals("field"))
                        .findFirst()
                        .map(Answer::getAnswer)
                        .orElseThrow(() -> new RuntimeException("희망 분야를 찾을 수 없습니다."));

        String name =
                results.stream()
                        .filter(answer -> answer.getQuestion().getName().equals("name"))
                        .findFirst()
                        .map(Answer::getAnswer)
                        .orElseThrow(() -> new RuntimeException("이름을 찾을 수 없습니다."));

        ApplicantRegisterEvent applicantRegisterEvent =
                ApplicantRegisterEvent.of(applicantId.toString(), name, hopeField);
        Events.raise(applicantRegisterEvent);
        return applicantId;
    }

    private String convertToSubmitApplicantEventTitle(List<Answer> results) {
        final String HOPE_FIELD_QUESTION_KEY = "field";
        final String NAME_QUESTION_KEY = "name";

        StringBuilder titleBuilder = new StringBuilder();
        String hopeField = "";
        String name = "";

        for (Answer answer : results) {
            String question = answer.getQuestion().getName();
            String userInputValue = answer.getAnswer();

            if (question.equals(HOPE_FIELD_QUESTION_KEY)) {
                hopeField = userInputValue;
            }
            if (question.equals(NAME_QUESTION_KEY)) {
                name = userInputValue;
            }
        }

        titleBuilder.append("[").append(hopeField).append("] ").append(name);
        return titleBuilder.toString();
    }

    private Applicant createApplicantFromUserInput(Map<String, String> userInput) {
        return Applicant.builder()
                .id(UUID.randomUUID())
                .hopeField(userInput.get("hopeField"))
                .firstPriority(userInput.get("firstPriority"))
                .secondPriority(userInput.get("secondPriority"))
                .name(userInput.get("name"))
                .phoneNumber(userInput.get("phoneNumber"))
                .studentId(Integer.parseInt(userInput.get("studentId")))
                .grade(Integer.parseInt(userInput.get("grade")))
                .semester(Integer.parseInt(userInput.get("semester")))
                .major(userInput.get("major"))
                .doubleMajor(userInput.get("doubleMajor"))
                .minor(userInput.get("minor"))
                .supportPath(userInput.get("supportPath"))
                .email(userInput.get("email"))
                .build();
    }
}
