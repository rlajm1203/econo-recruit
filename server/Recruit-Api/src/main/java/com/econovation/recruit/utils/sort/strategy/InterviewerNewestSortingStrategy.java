package com.econovation.recruit.utils.sort.strategy;

import com.econovation.recruitdomain.domains.interviewer.domain.Interviewer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("interviewerNewestSort")
public class InterviewerNewestSortingStrategy implements SortStrategy<Interviewer> {
    @Override
    public int compare(Interviewer obj1, Interviewer obj2) {
        //         createdAt 값을 가져와 비교하여 정렬 순서를 결정합니다.
        return obj1.getCreatedAt().compareTo(obj2.getCreatedAt());
    }
}
