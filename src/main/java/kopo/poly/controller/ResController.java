package kopo.poly.controller;

import kopo.poly.dto.NoticeDTO;
import kopo.poly.dto.ResDTO;
import kopo.poly.service.IResService;
import kopo.poly.util.CmmUtil;
import kopo.poly.util.EncryptUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequestMapping(value = "/online")
@RequiredArgsConstructor
@Controller
public class ResController {

    private final IResService resService;

    @GetMapping(value = "calendar")
    public String ResList(HttpSession session, ModelMap model) throws Exception {
        log.info(this.getClass().getName() + ".calendar Start!!!");


        // 달력 정보 가져오기
        List<ResDTO> rList = Optional.ofNullable(resService.getReservationList())
                .orElseGet(ArrayList::new);

        model.addAttribute("rList", rList);

        log.info(this.getClass().getName() + ".calendar Start!!!");
        return "online/calendar";
    }

    @GetMapping(value = "reservationInfo")
    public String reservationInfo(HttpServletRequest request, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".reservationInfo Start!");

        String date = CmmUtil.nvl(request.getParameter("clickedInfo"));

        /*
         * ########################################################################
         * 반드시, 값을 받았으면, 꼭 로그를 찍어서 값이 제대로 들어오는지 파악해야함
         * 반드시 작성할것.
         * ########################################################################
         */
        log.info("date : " + date);

        // 값 전달은 반드시 DTO 객체를 이용해서 처리함. 전달받은 값을 DTO 객체에 넣는다.
        ResDTO pDTO = new ResDTO();
        pDTO.setDateId(date);

        // 예약 상세정보 가져오기
        // Java8부터 제공되는 Optional 활용하여 NPE(Null Pointer Exception) 처리
        ResDTO rDTO = Optional.ofNullable(
                resService.getReservationInfo(pDTO, true)
        ).orElseGet(ResDTO::new);


        log.info("rDTO : " + rDTO.toString());

        // 조회된 리스트 결과값 넣어주기
        model.addAttribute("rDTO", rDTO);

        log.info(this.getClass().getName() + ".reservationInfo End!");

        // 함수처리가 끝나고 보여줄 html 파일명
        return "online/calendar/resInfo";
    }

}