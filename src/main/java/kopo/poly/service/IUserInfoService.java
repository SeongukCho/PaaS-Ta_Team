package kopo.poly.service;

import kopo.poly.dto.UserInfoDTO;

import java.util.List;

public interface IUserInfoService {

    // 아이디 중복 체크
    UserInfoDTO getUserIdExists(UserInfoDTO pDTO) throws Exception;

    // 이메일 주소 중복체크 및 인증 값
    UserInfoDTO getEmailExists(UserInfoDTO pDTO) throws Exception;

    // 회원 가입 하기(회원정보 등록하기)
    int insertUserInfo(UserInfoDTO pDTO) throws Exception;

    List<UserInfoDTO> getUserList() throws Exception;

    UserInfoDTO getUserInfo(UserInfoDTO pDTO, boolean type) throws Exception;

    // 로그인을 위해 아이디와 비밀번호가 일치하는지 확인하기
    UserInfoDTO getLogin(UserInfoDTO pDTO) throws Exception;

    UserInfoDTO searchUserIdOrPasswordProc(UserInfoDTO pDTO) throws Exception;

    //비밀번호 재설정
    int newPasswordProc(UserInfoDTO pDTO) throws Exception;

    //비밀번호 찾기 이메일 인증번호 발송
    UserInfoDTO passEmailAuth(UserInfoDTO pDTO) throws Exception;
}
