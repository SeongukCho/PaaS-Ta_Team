package kopo.poly.persistance.mapper;

import kopo.poly.dto.NoticeDTO;
import kopo.poly.dto.ResDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IResMapper {
    List<ResDTO> getReservationList() throws Exception;

    void insertReservation(ResDTO pDTO) throws Exception;

    ResDTO getReservationInfo(ResDTO pDTO) throws Exception;

    //게시판 글 수정
    void updateReservationInfo(ResDTO pDTO) throws Exception;

    //게시판 글 삭제
    void deleteReservationInfo(ResDTO pDTO) throws Exception;
}
