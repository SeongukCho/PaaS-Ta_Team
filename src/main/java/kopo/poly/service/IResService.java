package kopo.poly.service;

import kopo.poly.dto.NoticeDTO;
import kopo.poly.dto.ResDTO;

import java.util.List;

public interface IResService {
    void insertReservation(ResDTO pDTO) throws Exception;

    List<ResDTO> getReservationList() throws Exception;

    ResDTO getReservationInfo(ResDTO pDTO, boolean type) throws Exception;

    void updateResInfo(NoticeDTO pDTO) throws Exception;

    void deleteResInfo(NoticeDTO pDTO) throws Exception;
}
