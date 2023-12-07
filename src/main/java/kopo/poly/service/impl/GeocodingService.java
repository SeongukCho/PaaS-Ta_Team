package kopo.poly.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import kopo.poly.dto.CenterDTO;
import kopo.poly.dto.GeocodingDTO;
import kopo.poly.service.IGeocodingService;
import kopo.poly.util.CmmUtil;
import kopo.poly.util.NetworkUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class GeocodingService implements IGeocodingService {

    /**
     * Naver API 사용을 위한 접속 정보 설정
     */
    @Value("${naver.geocode.clientId}")
    private String ClientID;

    @Value("${naver.geocode.clientSecret}")
    private String ClientSecret;

    private Map<String, String> setNaverInfo() {
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("X-NCP-APIGW-API-KEY-ID", ClientID);
        requestHeader.put("X-NCP-APIGW-API-KEY", ClientSecret);
        return requestHeader;
    }

    @Override
    public CenterDTO Geocoding(CenterDTO pDTO) throws Exception {
        log.info(this.getClass().getName() + ".Geocoding Start!");

        log.info("변환할 address : " + pDTO.getAddress());
        String address = CmmUtil.nvl(pDTO.getAddress());  // 변환할 주소

        pDTO.setAddress(address);

        // 호출할 Geocoding API 정보 설정
        String param = "?query=" + URLEncoder.encode(address, "UTF-8");

        log.info("query : " + param);

        String url = IGeocodingService.GeocodingApiURL + param;

        log.info("url : " + url);

        // 헤더 정보 설정
        Map<String, String> headers = setNaverInfo();

        // GeocodingAPI 호출하기
        String json = NetworkUtil.get(url, param, headers);

        // json 확인
        log.info("json : " + json);

        // Json 구조를 Map 데이터 구조로 변경
//        CenterDTO rDTO = new ObjectMapper().readValue(json, CenterDTO.class);

        // 수정된 부분
        Map<String, Object> rMap = new ObjectMapper().readValue(json, LinkedHashMap.class);
        List<Map<String, Object>> myList = (List<Map<String, Object>>) rMap.get("addresses");  // 수정된 부분

        if (myList != null && !myList.isEmpty()) {
            String x = (String) myList.get(0).get("x");
            String y = (String) myList.get(0).get("y");

            // x 값 처리
            double xValue = Double.parseDouble(x);
            x = String.format("%.5f", xValue); // 소수점 다섯 자리까지 유지

            // y 값 처리
            double yValue = Double.parseDouble(y);
            y = String.format("%.5f", yValue); // 소수점 다섯 자리까지 유지

            pDTO.setX(x);
            pDTO.setY(y);

            log.info("값이 잘 들어갔는지 볼까?");
            log.info("x 주소 : " + pDTO.getX());
            log.info("y 주소 : " + pDTO.getY());
            log.info("address : " + pDTO.getAddress());
        } else {
            log.warn("주소 정보가 없습니다.");
        }

        return pDTO;
    }
}