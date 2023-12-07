package kopo.poly.persistance.mapper;

import kopo.poly.dto.CenterDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ICenterMapper {

    //게시판 리스트
    List<CenterDTO> getCenterList() throws Exception;

    //센터 정보 등록
    void insertCenterInfo(CenterDTO pDTO) throws Exception;

    //센터 상세 보기
    CenterDTO getCenterInfo(CenterDTO pDTO) throws Exception;

    //센터 정보 수정
    void updateCenterInfo(CenterDTO pDTO) throws Exception;

    //센터 정보 삭제
    void deleteCenterInfo(CenterDTO pDTO) throws Exception;


    // 검색 기능
    List<CenterDTO> searchCenter_all(String isSido, String centerAddress);

    List<CenterDTO> searchCenter_sido(String isSido);

    List<CenterDTO> searchCenter_address(String centerAddress);

    List<CenterDTO> searchCenterName(String seq);

}
