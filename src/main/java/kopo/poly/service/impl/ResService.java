package kopo.poly.service.impl;

import kopo.poly.dto.NoticeDTO;
import kopo.poly.dto.ResDTO;
import kopo.poly.persistance.mapper.IResMapper;
import kopo.poly.service.IResService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@RequestMapping(value = "online")
@Service
@RequiredArgsConstructor
public class ResService implements IResService {

    private final IResMapper resMapper;

    @Override
    public void insertReservation(ResDTO pDTO) throws Exception {
        resMapper.insertReservation(pDTO);
    }

    @Override
    public List<ResDTO> getReservationList() throws Exception {

        log.info(this.getClass().getName() + ".getReservationList start!");

        return resMapper.getReservationList();

    }

    @Transactional
    @Override
    public ResDTO getReservationInfo(ResDTO pDTO, boolean type) throws Exception {

        log.info(this.getClass().getName() + ".getNoticeInfo Start!");


        log.info(this.getClass().getName() + ".getNoticeInfo End!");
        return resMapper.getReservationInfo(pDTO);
    }



    @Override
    public void updateResInfo(NoticeDTO pDTO) throws Exception {

    }

    @Override
    public void deleteResInfo(NoticeDTO pDTO) throws Exception {

    }
}
