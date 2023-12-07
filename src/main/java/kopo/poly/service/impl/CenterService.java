package kopo.poly.service.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import kopo.poly.dto.CenterDTO;
import kopo.poly.persistance.mapper.ICenterMapper;
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
public class CenterService implements ICenterService {

    private final ICenterMapper ICenterMapper;




    //센터 리스트 전체 보기
    @Override
    public List<CenterDTO> getCenterList() throws Exception {
        log.info(this.getClass().getName() + ".getCenterList start!");

        CenterDTO pDTO = new CenterDTO();

        List<CenterDTO> resultList = ICenterMapper.getCenterList();
        if (resultList == null) {
            log.info("resultList 값이 Null 임");
        } else if (resultList.isEmpty()) {
            log.info("resultList에 값이 비어있음.");
        } else {
            log.info("resultList size: " + resultList.size());
        }
        return ICenterMapper.getCenterList();
    }



    // 센터 정보 등록하기
    @Transactional  // 도중에 실행이 끊기면 처음으로 롤백함. 안 끊기면 그대로 진행.
    @Override
    public void insertCenterInfo(CenterDTO pDTO) throws Exception {
        log.info(this.getClass().getName() + ".insertCenterInfo Start!");
        ICenterMapper.insertCenterInfo(pDTO);

        log.info("잘 들어갔는지 봐볼까?");
        log.info("상태메시지 : " + pDTO.getStatus());
        log.info("지역명 : " + pDTO.getRegion());
        log.info("센터명 : " + pDTO.getCenterName());
        log.info("주소 : " + pDTO.getAddress());

        log.info(this.getClass().getName() + ".insertCenterInfo End!");
    }


    //센터 상세보기 조회 쿼리 실행
    @Transactional
    @Override
    public CenterDTO getCenterInfo(CenterDTO pDTO) throws Exception {
        log.info(this.getClass().getName() + ".getCenterInfo start!");

        return ICenterMapper.getCenterInfo(pDTO);
    }

    // 센터 정보 수정하기
    @Transactional
    @Override
    public void updateCenterList(CenterDTO centerDTO) throws Exception {
        log.info(this.getClass().getName() + ".updateCenterList start!");

        ICenterMapper.updateCenterInfo(centerDTO);

        log.info(this.getClass().getName() + ".updateCenterList End!!");

    }


    // 센터 정보 삭제하기
    @Transactional
    @Override
    public void deleteCenterList(CenterDTO pDTO) throws Exception {
        log.info(this.getClass().getName() + ".deleteCenterList start!");

        ICenterMapper.deleteCenterInfo(pDTO);

        log.info(this.getClass().getName() + ".deleteCenterList End!!");
    }


    @Override
    public List<CenterDTO> searchCenter_all(String isSido, String centerAddress) throws Exception {
        log.info(this.getClass().getName() + ".searchCenter_all 시작~");
        log.info("결과 들어온 값들 : " + "isSido = " + isSido + " centerAddress = " + centerAddress);

        // 실제 매퍼 호출하여 센터를 조회하고 결과를 반환하는 코드
        return ICenterMapper.searchCenter_all(isSido, centerAddress);
    }

    @Override
    public List<CenterDTO> searchCenter_sido(String isSido) throws Exception {
        log.info(this.getClass().getName() + ".searchCenter_sido 시작~");
        log.info("도시 값 : " + isSido);

        // 실제 매퍼 호출하여 센터를 조회하고 결과를 반환하는 코드
        return ICenterMapper.searchCenter_sido(isSido);
    }

    @Override
    public List<CenterDTO> searchCenter_address(String centerAddress) throws Exception {
        log.info(this.getClass().getName() + ".searchCenter_address 시작~");
        log.info("주소 값 : " + centerAddress);

        // 실제 매퍼 호출하여 센터를 조회하고 결과를 반환하는 코드
        return ICenterMapper.searchCenter_address(centerAddress);
    }

    @Override
    public List<CenterDTO> searchCenterName(String seq) throws Exception {
        log.info(this.getClass().getName() + ".searchCenterName 시작~");
        log.info("seq 값 : " + seq);
        CenterDTO pdto = new CenterDTO();
        pdto.setSeq(seq);
        log.info("DTO에 SEQ값이 들어 갔냐? : " + pdto.getSeq());
        return ICenterMapper.searchCenterName(seq);
    }
}