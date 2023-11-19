package kopo.poly.service.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import kopo.poly.dto.CenterDTO;
import kopo.poly.dto.GeocodingDTO;
import kopo.poly.persistance.mapper.ICtCenterMapper;
import kopo.poly.service.ICenterService;
import kopo.poly.service.IGeocodingService;
import kopo.poly.util.CmmUtil;
import kopo.poly.util.NetworkUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class CtCenterService implements ICenterService {

    private final ICtCenterMapper centerMapper;

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

        log.info("ClientID : " + ClientID);
        log.info("ClientSecret : " + ClientSecret);

        return requestHeader;
    }


    @Override
    public CenterDTO Geocoding(CenterDTO pDTO) throws Exception {
        log.info(this.getClass().getName() + ".Geocoding Start!");
        log.info("변환할 address : " + pDTO.getAddress());
        String address = CmmUtil.nvl(pDTO.getAddress());  // 변환할 주소

        //호출할 Geocoding API 정보 설정
        String param = "?query=" + URLEncoder.encode(address, "UTF-8");

        String url = IGeocodingService.GeocodingApiURL + param + this.setNaverInfo();
        log.info("url : " + url);
        log.info("씹" + NetworkUtil.get(IGeocodingService.GeocodingApiURL, param, this.setNaverInfo()));

        //GeocodingAPI 호출하기
        String json = NetworkUtil.get(IGeocodingService.GeocodingApiURL, param, this.setNaverInfo());
        /**
         * 호출 예시)
         * json =
         * https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocodem,
         * X-NCP-APIGW-API-KEY-ID : "ClientID",
         * X-NCP-APIGW-API-KEY : "ClientSecret",
         * "query= {변활할 주소}"
         */

        //json 확인
        log.info("json : "+ json);

        //Json 구조를 Map 데이터 구조로 변경
        CenterDTO rDTO = new ObjectMapper().readValue(json, CenterDTO.class);

        log.info(this.getClass().getName() + ".Geocoding End!");

        return rDTO;
    }

    @Override
    public List<CenterDTO> getCenterList() throws Exception {
        log.info(this.getClass().getName() + ".getCenterList start!");

        List<CenterDTO> resultList = centerMapper.getCT_CenterList();
        if (resultList == null) {
            log.info("resultList is null!");
        } else if (resultList.isEmpty()) {
            log.info("resultList is empty!");
        } else {
            log.info("resultList size: " + resultList.size());
        }

        return centerMapper.getCT_CenterList();
    }

    @Transactional
    @Override
    public void insertCenterInfo(CenterDTO centerDTO) throws Exception {
        log.info(this.getClass().getName() + ".insertCenterInfo Start!");
        centerMapper.insertCT_CenterInfo(centerDTO);
        centerMapper.insertGeocoding(centerDTO);

        log.info("잘 들어갔는지 봐볼까?");
        log.info("상태메시지 : " + centerDTO.getStatus());
        log.info("지역명 : " + centerDTO.getRegion());
        log.info("센터명 : " + centerDTO.getCenterName());
        log.info("주소 : " + centerDTO.getAddress());
        log.info("x좌표 : " + centerDTO.getX());
        log.info("y좌표 : " + centerDTO.getY());
        log.info(this.getClass().getName() + ".insertCenterInfo End!");
    }

    @Transactional
    @Override
    public void updateCenterList(CenterDTO centerDTO) throws Exception {
        log.info(this.getClass().getName() + ".updateCenterList start!");
        centerMapper.updateCT_CenterInfo(centerDTO);
        log.info(this.getClass().getName() + ".updateCenterList End!");
    }



    @Transactional
    @Override
    public void deleteCenterList(CenterDTO pDTO) throws Exception {
        log.info(this.getClass().getName() + ".deleteCenterList start!");
        centerMapper.deleteCT_CenterInfo(pDTO);
        log.info(this.getClass().getName() + ".deleteCenterList End!");
    }
}