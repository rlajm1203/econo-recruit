package com.econovation.recruitdomain.out;

import com.econovation.recruitdomain.domains.timetable.domain.TimeTable;
import java.util.List;

public interface TimeTableRecordPort {
    //    List<TimeTableInsertDto> saveAll(List<TimeTableInsertDto> timeTableInsertDtos,Integer
    // applicantId);
    List<TimeTable> saveAll(List<TimeTable> timeTables);
}
