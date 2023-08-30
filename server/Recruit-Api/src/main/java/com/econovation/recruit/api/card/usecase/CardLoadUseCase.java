package com.econovation.recruit.api.card.usecase;

import com.econovation.recruitcommon.annotation.UseCase;
import com.econovation.recruitdomain.domains.card.domain.Card;
import com.econovation.recruitdomain.domains.card.dto.BoardCardResponseDto;
import java.util.List;

@UseCase
public interface CardLoadUseCase {
    List<Card> findAll();

    List<BoardCardResponseDto> getByNavigationId(Integer navigationId);
}
