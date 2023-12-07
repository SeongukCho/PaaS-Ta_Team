package kopo.poly.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CenterDTO {

    private String seq;               // 순번
    private String region;        // 지역
    private String centerName;   // 센터명
    private String address;     // 주소
    private String phone;      // 담당 연락처


    private String centerType;  // 센터분류
    private String directions;  // 찾아오시는 길
    private String business;    //주요 업무
    private String other;  //기타 설명

    //Geocoding
    private String x;    // x 좌표
    private String y;    // y 좌표
    private String GCerror; //Geocoding 에러
    private String mapUrl;  //길찾기 URL


    private String status;           // 상태메시지
    private String errorMessage;    //오류 메시지

}