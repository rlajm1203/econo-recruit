package com.econovation.recruit.api.card.controller;

import static com.econovation.recruitcommon.consts.RecruitStatic.*;

import com.econovation.recruit.api.applicant.usecase.AnswerLoadUseCase;
import com.econovation.recruit.api.card.docs.CreateBoardExceptionDocs;
import com.econovation.recruit.api.card.docs.CreateColumnsExceptionDocs;
import com.econovation.recruit.api.card.docs.CreateNavigationExceptionDocs;
import com.econovation.recruit.api.card.docs.FindNavigationExceptionDocs;
import com.econovation.recruit.api.card.docs.UpdateBoardExceptionDocs;
import com.econovation.recruit.api.card.usecase.BoardLoadUseCase;
import com.econovation.recruit.api.card.usecase.BoardRegisterUseCase;
import com.econovation.recruit.api.card.usecase.CardLoadUseCase;
import com.econovation.recruit.api.card.usecase.CardRegisterUseCase;
import com.econovation.recruit.api.card.usecase.NavigationUseCase;
import com.econovation.recruitcommon.annotation.ApiErrorExceptionsExample;
import com.econovation.recruitdomain.domains.board.domain.Navigation;
import com.econovation.recruitdomain.domains.board.dto.ColumnsResponseDto;
import com.econovation.recruitdomain.domains.card.dto.CardResponseDto;
import com.econovation.recruitdomain.domains.dto.CreateWorkCardDto;
import com.econovation.recruitdomain.domains.dto.UpdateLocationBoardDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SecurityRequirement(name = "access-token")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "[2.0]. 칸반보드 API", description = "칸반보드 관련 API")
@Slf4j
public class BoardRestController {
    private final BoardLoadUseCase boardLoadUseCase;
    private final BoardRegisterUseCase boardRecordUseCase;
    private final CardRegisterUseCase cardRegisterUseCase;
    private final CardLoadUseCase cardLoadUseCase;
    private final NavigationUseCase navigationUseCase;
    private final AnswerLoadUseCase answerLoadUseCase;
    //    ---------- Navigation ----------
    @Operation(summary = "네비게이션 바 생성", description = "카드 생성 전에 네비게이션 바를 생성하셔야 합니다.")
    @ApiErrorExceptionsExample(value = CreateNavigationExceptionDocs.class)
    @PostMapping("/boards/navigations")
    public ResponseEntity<Navigation> createNavigation(String navTitle) {
        Navigation navigation = navigationUseCase.createNavigation(navTitle);
        return new ResponseEntity<>(navigation, HttpStatus.OK);
    }

    @Operation(summary = "네비게이션 바 수정", description = "navigation 수정.")
    @ApiErrorExceptionsExample(value = CreateNavigationExceptionDocs.class)
    @PostMapping("/boards/navigations/{navigation-id}/update")
    public ResponseEntity<Navigation> createNavigation(
            @PathVariable(name = "navigation-id") Integer navigationId,
            @RequestBody String navTitle) {
        Navigation navigation = navigationUseCase.updateNavigationByNavLoc(navigationId, navTitle);
        return new ResponseEntity<>(navigation, HttpStatus.OK);
    }

    @Operation(summary = "네비게이션 바 조회", description = "navigationId에 해당하는 네비게이션 바를 조회합니다.")
    @GetMapping("/boards/navigations/{navigation-id}")
    @ApiErrorExceptionsExample(value = FindNavigationExceptionDocs.class)
    public ResponseEntity<Navigation> getNavigationByNavLoc(
            @PathVariable(name = "navigation-id") Integer navigationId) {
        return new ResponseEntity(
                boardLoadUseCase.getNavigationByNavLoc(navigationId), HttpStatus.OK);
    }

    @Operation(summary = "네비게이션 바 전체 조회", description = "네비게이션 바 전체를 조회합니다.")
    @ApiErrorExceptionsExample(value = FindNavigationExceptionDocs.class)
    @GetMapping("/boards/navigations")
    public ResponseEntity<List<Navigation>> getAllNavigation() {
        List<Navigation> navigations = boardLoadUseCase.getAllNavigation();
        return new ResponseEntity<>(navigations, HttpStatus.OK);
    }

    @Operation(summary = "네비게이션 바 삭제", description = "navigationId에 해당하는 네비게이션 바를 삭제합니다.")
    @ApiErrorExceptionsExample(value = FindNavigationExceptionDocs.class)
    @PostMapping("/boards/navigations/{navigation-id}/delete")
    public ResponseEntity deleteNavigation(
            @PathVariable(name = "navigation-id") Integer navigationId) {
        navigationUseCase.deleteById(navigationId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Operation(summary = "지원서 칸반보드 열(세로줄) 생성", description = "지원서 칸반보드 열(세로줄) 생성")
    @ApiErrorExceptionsExample(CreateColumnsExceptionDocs.class)
    @PostMapping("/boards/navigation/{navigation-id}/columns")
    public ResponseEntity<String> createBoardColumn(
            @PathVariable("navigation-id") Integer navigationId, String title) {
        boardRecordUseCase.createColumn(title, navigationId);
        return new ResponseEntity(COLUMN_SUCCESS_REGISTER_MESSAGE, HttpStatus.OK);
    }
    // 칸반보드 전체 조회 by navLoc
    @Operation(summary = "업무 칸반보드 생성", description = "업무 칸반(지원자가 아닌) 생성")
    @ApiErrorExceptionsExample(CreateBoardExceptionDocs.class)
    @PostMapping("/boards/work-cards")
    public ResponseEntity<String> createWorkBoard(
            // TODO : navigation 확장 예정
            @RequestBody CreateWorkCardDto createWorkCardDto) {
        cardRegisterUseCase.saveWorkCard(createWorkCardDto);
        return new ResponseEntity(BOARD_SUCCESS_REGISTER_MESSAGE, HttpStatus.OK);
    }

    @Operation(summary = "지원서 칸반보드 위치 수정")
    @ApiErrorExceptionsExample(UpdateBoardExceptionDocs.class)
    @PostMapping("/boards/location")
    public ResponseEntity<String> updateLocationBoard(
            UpdateLocationBoardDto updateLocationBoardDto) {
        boardRecordUseCase.relocateCard(updateLocationBoardDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Operation(summary = "지원자 id로 지원서를 조회합니다.")
    @GetMapping("/applicants/{applicant-id}")
    public ResponseEntity<Map<String, String>> getApplicantById(
            @PathVariable(value = "applicant-id") String applicantId) {
        return new ResponseEntity<>(answerLoadUseCase.execute(applicantId), HttpStatus.OK);
    }

    @Operation(summary = "모든 지원자의 지원서를 조회합니다.")
    @GetMapping("/applicants")
    public ResponseEntity<List<Map<String, String>>> getApplicants() {
        return new ResponseEntity<>(answerLoadUseCase.execute(), HttpStatus.OK);
    }

    @Operation(
            summary = "지원서 칸반보드 조회 by navigationId",
            description = "navigationId에 해당하는 모든 칸반을 조회합니다.")
    @GetMapping("/boards/navigation/{navigation-id}")
    public ResponseEntity<List<Map<ColumnsResponseDto, CardResponseDto>>> getBoardByNavigationId(
            @PathVariable("navigation-id") Integer navigationId) {
        return new ResponseEntity<>(cardLoadUseCase.getByNavigationId(navigationId), HttpStatus.OK);
    }

    //    @GetMapping("/boards/cards")
    //    public List<Card> getCardAll() {
    //        return new ArrayList<>(cardLoadUseCase.findAll());
    //    }

    @Operation(summary = "카드 생성", description = "카드를 삭제합니다")
    @PostMapping("/boards/cards/{card-id}/delete")
    public ResponseEntity<String> deleteCard(Long cardId) {
        cardRegisterUseCase.deleteById(cardId);
        return new ResponseEntity<>(BOARD_SUCCESS_DELETE_MESSAGE, HttpStatus.OK);
    }
}