package kopo.poly.service;

import kopo.poly.dto.CenterDTO;

public interface IGeocodingService {

    String GeocodingApiURL = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode";


    // GeocodingAPI를 호출하여 입력된 주소를 변환하기
    CenterDTO Geocoding(CenterDTO pDTO) throws Exception;
}
