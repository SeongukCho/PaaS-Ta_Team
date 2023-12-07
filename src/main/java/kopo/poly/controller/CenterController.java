package kopo.poly.controller;


import kopo.poly.dto.CenterDTO;
import kopo.poly.dto.MsgDTO;
import kopo.poly.service.ICenterService;
import kopo.poly.service.IGeocodingService;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;


@Slf4j
@RequestMapping(value = "/center")
@RequiredArgsConstructor
@Controller
public class CenterController {

    private final ICenterService centerService;
    private final IGeocodingService geocodingService;

    @GetMapping(value = "center")
    public String CenterList(HttpSession session, ModelMap model, @RequestParam(defaultValue = "1") int page) throws Exception {
        log.info(this.getClass().getName() + ".CenterList Start!");

        session.getAttribute("SS_USER_ID");

        List<CenterDTO> rList = Optional.ofNullable(centerService.getCenterList())
                .orElseGet(ArrayList::new);

        // 페이징 처리
        int totalCenter = rList.size();
        int CenterPerPage = 10; // 페이지당 표시할 공지사항 수 (원하는 값으로 수정)
        int totalPages = (int) Math.ceil((double) totalCenter / CenterPerPage);

        // 페이징된 공지사항 리스트 가져오기
        List<CenterDTO> pagedList = getPagedList(rList, page, CenterPerPage);

        model.addAttribute("rList", pagedList);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        model.addAttribute("dList", rList);


        log.info(this.getClass().getName() + ".CenterList End!");
        return "/center/center";
    }

    // 페이지 번호에 따른 공지사항 리스트 가져오기
    private List<CenterDTO> getPagedList(List<CenterDTO> allNotices, int page, int noticesPerPage) {
        int startIndex = (page - 1) * noticesPerPage;
        int endIndex = Math.min(startIndex + noticesPerPage, allNotices.size());
        return allNotices.subList(startIndex, endIndex);
    }


    @GetMapping(value = "centerReg")
    public String centerReg() {
        log.info(this.getClass().getName() + ".centerReg Start!");
        log.info(this.getClass().getName() + ".centerReg End!");
        return "/center/centerReg";
    }


    @ResponseBody
    @PostMapping(value = "centerInsert")
    public MsgDTO CenterInsert(HttpServletRequest request, HttpSession session) {

        log.info(this.getClass().getName() + ".CenterInsert Start!");
        String msg = "";
        MsgDTO dto = null;
        try {
            String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));
            String region = CmmUtil.nvl(request.getParameter("region"));
            String centerName = CmmUtil.nvl(request.getParameter("centerName"));
            String address = CmmUtil.nvl(request.getParameter("address"));
            String phone = CmmUtil.nvl(request.getParameter("phone"));

            String directions = CmmUtil.nvl(request.getParameter("directions"));
            String centerType = CmmUtil.nvl(request.getParameter("centerType"));
            String business = CmmUtil.nvl(request.getParameter("business"));
            String other = CmmUtil.nvl(request.getParameter("other"));

            log.info("userId : " + userId);
            log.info("region : " + region);
            log.info("centerName : " + centerName);
            log.info("address : " + address);
            log.info("phone : " + phone);

            log.info("directions : " + directions);
            log.info("centerType : " + centerType);
            log.info("business : " + business);
            log.info("other : " + other);

            CenterDTO pDTO = new CenterDTO();


            pDTO.setRegion(region);
            pDTO.setCenterName(centerName);
            pDTO.setAddress(address);
            pDTO.setPhone(phone);

            pDTO.setDirections(directions);
            pDTO.setCenterType(centerType);
            pDTO.setBusiness(business);
            pDTO.setOther(other);

            // geocoding 을 실행하는 코드
            geocodingService.Geocoding(pDTO);

            log.info("현재 CenterDTO 에 저장된 값들 ");
            log.info("region : " + pDTO.getRegion());
            log.info("centerName : " + pDTO.getCenterName());
            log.info("address : " + pDTO.getAddress());
            log.info("phone : " + pDTO.getPhone());

            log.info("phone : " + pDTO.getCenterType());
            log.info("phone : " + pDTO.getDirections());
            log.info("phone : " + pDTO.getBusiness());
            log.info("phone : " + pDTO.getOther());

            log.info("XAddress : " + pDTO.getX());
            log.info("YAddress : " + pDTO.getY());

            if(pDTO.getGCerror() != "1") {
                // 값을 DB에 저장하는 코드
                centerService.insertCenterInfo(pDTO);
                msg = "등록되었습니다.";
            } else {
                msg = "실패하였습니다. 실패 원인 : 주소 값이 정확하지 않습니다.";
                pDTO.setGCerror("0");
            }
        } catch (Exception e) {
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();
        } finally {
            dto = new MsgDTO();
            dto.setMsg(msg);
            log.info(this.getClass().getName() + ".CenterInsert End!");
        }
        return dto;
    }





    @GetMapping("/Search")
    @ResponseBody
    public Map<String, Object> searchCenter(HttpServletRequest request, HttpSession session, ModelMap model,
                                            @RequestParam(value = "is_sido", required = false) String isSido,
                                            @RequestParam(value = "searchText", required = false) String searchText,
                                            @RequestParam(defaultValue = "1") int page) {
        log.info(this.getClass().getName() + ".searchCenter Start!");

        Map<String, Object> response = new HashMap<>();

        try {
            List<CenterDTO> searchResults;
            if (!isSido.equals("-1") && !searchText.equals("-1")) {
                // 도시와 주소가 함께 입력된 경우
                searchResults = centerService.searchCenter_all(isSido, searchText);
            } else if (!isSido.equals("-1")) {
                // 도시만 입력된 경우
                log.info("도시 입력 완료");
                searchResults = centerService.searchCenter_sido(isSido);
            } else if (!searchText.equals("-1")) {
                // 주소만 입력된 경우
                log.info("주소 입력 완료");
                searchResults = centerService.searchCenter_address(searchText);
            } else {
                // 검색 조건이 없는 경우 전체 리스트 가져오기
                log.info("아무 조건 없이 검색 됨.");
                searchResults = centerService.getCenterList();
            }

            // 페이징 처리
            int totalResults = searchResults.size();
            int resultsPerPage = 10; // 페이지당 표시할 결과 수 (원하는 값으로 수정)
            int totalPages = (int) Math.ceil((double) totalResults / resultsPerPage);

            // 페이징된 결과 리스트 가져오기
            List<CenterDTO> pagedList = getPagedList(searchResults, page, resultsPerPage);

            response.put("searchResults", pagedList);
            response.put("totalPages", totalPages);
            response.put("currentPage", page);
            response.put("success", true);

        } catch (Exception e) {
            log.error("검색 중 오류 발생: " + e.getMessage());
            response.put("success", false);
            response.put("error", e.getMessage());
        }

        log.info(this.getClass().getName() + ".searchCenter End!");
        return response;
    }

    // 센터 정보 상세보기 폼
    @GetMapping(value = "centerinfo")
    public String centerInfo(HttpSession session, ModelMap model, @RequestParam(value = "seq", required = false) String seq) throws Exception {
        log.info(this.getClass().getName() + ".centerInfo Start!");

        session.getAttribute("SS_USER_ID");
        session.setAttribute("SS_CENTER_SEQ", seq);

        List<CenterDTO> rList = Optional.ofNullable(centerService.searchCenterName(seq))
                .orElseGet(ArrayList::new);

        // 데이터를 모델에 추가
        model.addAttribute("rList", rList);
        log.info("전달 받은 seq의 값은 : " + seq);
        log.info("출력할 DB 열 크기 : " + rList.size());


        log.info(this.getClass().getName() + ".centerInfo End!");

        // 해당 뷰로 이동
        return "/center/centerinfo";
    }

    // 센터 정보 수정하기 폼
    @GetMapping(value = "centerEditInfo")
    public String centerEditInfo(HttpServletRequest request, ModelMap model) throws Exception {
        log.info(this.getClass().getName() + ".centerEditInfo Start!");
        log.info(this.getClass().getName() + ".centerEditInfo End!");
        return "/center/centerEditInfo";
    }

    @ResponseBody
    @PostMapping(value = "centerUpdate")
    public MsgDTO centerUpdate(HttpSession session, HttpServletRequest request) {
        log.info(this.getClass().getName() + ".centerUpdate Start!");
        String msg = "";
        MsgDTO dto = null;

        try {
            CenterDTO pDTO = new CenterDTO();

            String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));
            String seq = CmmUtil.nvl((String) session.getAttribute("SS_CENTER_SEQ"));

            String region = CmmUtil.nvl(request.getParameter("region"));
            String centerName = CmmUtil.nvl(request.getParameter("centerName"));
            String address = CmmUtil.nvl(request.getParameter("address"));
            String phone = CmmUtil.nvl(request.getParameter("phone"));

            String directions = CmmUtil.nvl(request.getParameter("directions"));
            String centerType = CmmUtil.nvl(request.getParameter("centerType"));
            String business = CmmUtil.nvl(request.getParameter("business"));
            String other = CmmUtil.nvl(request.getParameter("other"));

            log.info("userId : " + userId);
            log.info("seq : " + seq);
            log.info("region : " + region);
            log.info("centerName : " + centerName);
            log.info("address : " + address);
            log.info("phone : " + phone);

            log.info("directions : " + directions);
            log.info("centerType : " + centerType);
            log.info("business : " + business);
            log.info("other : " + other);

            pDTO.setRegion(region);
            pDTO.setSeq(seq);
            pDTO.setCenterName(centerName);
            pDTO.setAddress(address);
            pDTO.setPhone(phone);

            pDTO.setDirections(directions);
            pDTO.setCenterType(centerType);
            pDTO.setBusiness(business);
            pDTO.setOther(other);

            // geocoding 을 실행하는 코드
            geocodingService.Geocoding(pDTO);

            log.info("현재 CenterDTO 에 저장된 값들 ");
            log.info("SEQ : " + pDTO.getSeq());
            log.info("region : " + pDTO.getRegion());
            log.info("centerName : " + pDTO.getCenterName());
            log.info("address : " + pDTO.getAddress());
            log.info("phone : " + pDTO.getPhone());

            log.info("CenterType : " + pDTO.getCenterType());
            log.info("Directions : " + pDTO.getDirections());
            log.info("Business : " + pDTO.getBusiness());
            log.info("Other : " + pDTO.getOther());

            log.info("XAddress : " + pDTO.getX());
            log.info("YAddress : " + pDTO.getY());

            if(pDTO.getGCerror() != "1") {
                // 값을 DB에 저장하는 코드
                centerService.updateCenterList(pDTO);
                msg = "등록되었습니다.";
            } else {
                msg = "실패하였습니다. 실패 원인 : 주소 값이 정확하지 않습니다.";
                pDTO.setGCerror("0");
            }



        } catch (Exception e) {
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();
        } finally {
            dto = new MsgDTO();
            dto.setMsg(msg);
            log.info(this.getClass().getName() + ".centerUpdate End!");
        }
        return dto;
    }

    @ResponseBody
    @RequestMapping(value = "centerDelete")
    public MsgDTO centerDelete(HttpServletRequest request, HttpSession session) {
        log.info(this.getClass().getName() + ".centerDelete Start!");

        String msg = ""; // 메시지 내용
        MsgDTO dto = null; // 결과 메시지 구조

        try {
            String seq = CmmUtil.nvl((String)session.getAttribute("SS_CENTER_SEQ"));

            log.info("seq : " + seq);

            /*
             * 값 전달은 반드시 DTO 객체를 이용해서 처리함 전달 받은 값을 DTO 객체에 넣는다.
             */
            CenterDTO pDTO = new CenterDTO();
            pDTO.setSeq(seq); // String 타입을 long 타입으로 변경

            // 게시글 삭제하기 DB
            centerService.deleteCenterList(pDTO);

            msg = "삭제되었습니다.";

        } catch (Exception e) {
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

        } finally {
            // 결과 메시지 전달하기
            dto = new MsgDTO();
            dto.setMsg(msg);

            log.info(this.getClass().getName() + ".centerDelete End!");

        }

        return dto;
    }

}