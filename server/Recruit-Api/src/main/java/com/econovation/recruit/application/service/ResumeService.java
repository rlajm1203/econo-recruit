/*
package com.econovation.recruit.application.service;


@Service
@RequiredArgsConstructor
public class ResumeService implements ResumeUseCase {
    private final ResumeRecordPort resumeRecordPort;
    private final ResumeLoadPort resumeLoadPort;

    //    @Override
    //    public List<ResumeInsertDto> submitResume(HashMap<String, Object> param) {
    //        List<ResumeInsertDto> resumeInsertDtos = toList(param);
    //        return resumeRecordPort.saveAll(resumeInsertDtos);
    //    }

    @Override
    public List<Resume> submitResume(List<Resume> resumes) {
        return resumeRecordPort.saveAll(resumes);
    }

    @Override
    public List<Resume> findAll() {
        return resumeLoadPort.findAll();
    }

    @Override
    public Resume findById(Integer resumeId) {
        return resumeLoadPort.findById(resumeId);
    }

    @Override
    public List<Resume> findByApplicantId(UUID applicantId) {
        Applicant applicant = applicantLoadPort.loadApplicantById(applicantId);
        return resumeLoadPort.findByApplicantId(applicant);
    }

    private List<ResumeInsertDto> toList(HashMap<String, Object> param) {
        List<ResumeInsertDto> chunkResume = new LinkedList<>();
        Gson gson = new Gson();
        // JsonParser Deprecated -> JsonParser static import 변경
        JsonElement applicantIds = JsonParser.parseString(param.get("applicantId").toString());
        JsonElement questionIds = JsonParser.parseString(param.get("questionId").toString());
        JsonElement answers = JsonParser.parseString(param.get("answer").toString());
        // JsonElement -> List<String>으로 파싱
        List<Integer> applicantList =
                gson.fromJson(applicantIds, (new TypeToken<List<Integer>>() {}).getType());
        List<Integer> questionList =
                gson.fromJson(questionIds, (new TypeToken<List<Integer>>() {}).getType());
        List<String> answerList =
                gson.fromJson(answers, (new TypeToken<List<String>>() {}).getType());

        for (int i = 0; i < applicantList.size(); i++) {
            chunkResume.add(
                    new ResumeInsertDto(
                            applicantList.get(i), questionList.get(i), answerList.get(i)));
        }
        return chunkResume;
    }
}
*/
