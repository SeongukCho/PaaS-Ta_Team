package kopo.poly.controller;

import kopo.poly.dto.GeocodingDTO;
import kopo.poly.service.IGeocodingService;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Slf4j
@RequestMapping(value = "Geocoding")
@RequiredArgsConstructor
@RestController
public class GeocodingController {

    private final IGeocodingService geocodingService;

    @PostMapping(value = "geocoding")
    public GeocodingDTO geocoding(HttpServletRequest request) throws Exception {

        log.info(this.getClass().getName() + ".geocoding Start!");

        String address = CmmUtil.nvl(request.getParameter(("text")));

        log.info("address = " + address);

        GeocodingDTO pDTO = new GeocodingDTO();
        pDTO.setAddress(address);

        GeocodingDTO rDTO = Optional.ofNullable(geocodingService.Geocoding(pDTO)).orElseGet(GeocodingDTO::new);

        log.info(this.getClass().getName() + ".geocoding End!");

        return rDTO;
    }
}


