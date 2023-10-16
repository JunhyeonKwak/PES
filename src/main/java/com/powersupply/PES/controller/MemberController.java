package com.powersupply.PES.controller;

import com.powersupply.PES.domain.dto.MemberDTO;
import com.powersupply.PES.service.MemberService;
import com.powersupply.PES.utils.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 회원가입 페이지 불러오기
    @GetMapping("/signup")
    public String getSignUp() {
        return "signup";
    }

    // 로그인 페이지 불러오기
    @GetMapping("/signin")
    public String getSignIn() {
        return "signin";
    }

    // 계정 찾기 페이지 불러오기
    @GetMapping("/finduser")
    public String getFindUser() {
        return "finduser";
    }

    // 마이페이지 불러오기
    @GetMapping("/mypage")
    public String getMyPage() {
        return "mypage";
    }

    // 메인화면 불러오기
    @GetMapping("/main")
    public String getMain() {
        return "main";
    }

    // 회원가입 정보 저장
    @PostMapping("/api/signup")
    public ResponseEntity<?> postSignUp(@RequestBody MemberDTO.MemberSignUpRequest dto) {
        String name = memberService.signUp(dto);

        // 회원가입 완료시 로그인 페이지로 이동
        return ResponseUtil.successResponse(name + "님, 가입에 성공했습니다.");
    }

    // 로그인 진행
    @PostMapping("/api/signin")
    public ResponseEntity<?> postSignIn(HttpServletResponse response, @RequestBody MemberDTO.MemberSignInRequest dto) {
        String token = memberService.signIn(dto);

        Cookie cookie = new Cookie("Authorization", token);
        cookie.setMaxAge(60 * 60); // 쿠키 유효 시간 (예: 1시간)
        //cookie.setSecure(true); // HTTPS에서만 쿠키 사용
        cookie.setPath("/"); // 도메인 전체에서 사용 가능하도록 설정
        //cookie.setHttpOnly(true); // JavaScript에서 쿠키에 접근할 수 없도록 설정
        // SameSite=None과 Secure 플래그를 설정
        // response.addHeader("Set-Cookie", String.format("%s=%s; Max-Age=%s; Secure; SameSite=None",
        //         cookie.getName(), cookie.getValue(), cookie.getMaxAge()));
        response.addCookie(cookie);

        return ResponseUtil.successResponse("로그인에 성공했습니다.");
    }

    // 마이페이지
    @GetMapping("/api/mypage")
    public ResponseEntity<MemberDTO.MemberMyPageResponse> getMyPageInfo() {

        return ResponseEntity.ok().body(memberService.getMyPage());
    }

    // 비밀번호 찾기
    @PostMapping("/api/finduser")
    public ResponseEntity<?> findUser(@RequestBody MemberDTO.MemberFindPwRequest dto) {
        memberService.findUser(dto);

        return ResponseUtil.successResponse("가입한 이메일로 임시 비밀번호를 전송했습니다.");
    }

    // 상단 사용자 정보
    @GetMapping("/api/myuser")
    public ResponseEntity<MemberDTO.NameScoreStatusResponse> myUser() {
        return ResponseEntity.ok().body(memberService.myUser());
    }

    // 랭킹 확인하기
    @GetMapping("/api/rank")
    public ResponseEntity<List<MemberDTO.NameScoreResponse>> memberRank() {
        return ResponseEntity.ok().body(memberService.memberRank());
    }
}
