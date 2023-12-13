package kopo.poly.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResDTO {
    private Long resId;     // 예약정보 기본키(순번)

    private String dateId;     // 날짜 테이블의 날짜 (ex 20231207)
    private String userId;  // 예약자 아이디

    private String startTime;
    private String endTime;
}
