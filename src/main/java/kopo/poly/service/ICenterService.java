package kopo.poly.service;

import kopo.poly.dto.CenterDTO;
import kopo.poly.dto.GeocodingDTO;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public interface ICenterService {

    List<CenterDTO> getCenterList() throws Exception;

    //센터 추기등록
    void insertCenterInfo(CenterDTO pDTO) throws Exception;

    //센터 상제 정보보기
    CenterDTO getCenterInfo(CenterDTO pDTO) throws Exception;

    //센터 정보 수정
    void updateCenterList(CenterDTO centerDTO) throws Exception;

    //센터 정보 삭제
    void deleteCenterList(CenterDTO pDTO) throws Exception;

    //검색기능 (도시 와 주소를 함께 입력하였을 때)
    List<CenterDTO> searchCenter_all(String isSido, String centerAddress) throws Exception;

    //검색기능 (도시만 입력하였을 떄)
    List<CenterDTO> searchCenter_sido(String isSido) throws Exception;

    //검색기능 (주소만 입력하였을 떄)
    List<CenterDTO> searchCenter_address(String centerAddress) throws Exception;

    //검색기능 (센터명을 눌러 클릭하였을 때)
    List<CenterDTO> searchCenterName(String seq) throws Exception;
}
