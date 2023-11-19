package kopo.poly.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CenterDTO {

    private int seq;          // 순번
    private String region;        // 지역
    private String centerName;   // 센터명
    private String address;     // 주소
    private String phone;     // 담당 연락처

    private String status;

    private String errorMessage;

    private String x; // 경도
    private String y; // 위도
}