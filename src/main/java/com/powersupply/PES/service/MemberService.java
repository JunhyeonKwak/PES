package com.powersupply.PES.service;

import com.powersupply.PES.domain.dto.MemberDTO;
import com.powersupply.PES.domain.entity.DetailMemberEntity;
import com.powersupply.PES.domain.entity.MemberEntity;
import com.powersupply.PES.exception.AppException;
import com.powersupply.PES.exception.ErrorCode;
import com.powersupply.PES.repository.DetailMemberRepository;
import com.powersupply.PES.repository.MemberRepository;
import com.powersupply.PES.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final DetailMemberRepository detailMemberRepository;
    private final BCryptPasswordEncoder encoder;
    @Value("${jwt.secret}")
    private String secretKey;

    @Transactional
    public String signUp(MemberDTO.MemberSignUpRequest dto) {

        String stuNum = dto.getMemberStuNum();
        String pw = dto.getMemberPw();
        String name = dto.getMemberName();

        // Email 및 password 빈칸 체크
        if (stuNum.isBlank() || pw.isBlank()) {
            throw new AppException(ErrorCode.INVALID_INPUT, "필수 입력 사항을 입력해 주세요.");
        }

        // memberEmail 중복 체크
        memberRepository.findByMemberStuNum(stuNum)
                .ifPresent(member -> {
                    throw new AppException(ErrorCode.USERNAME_DUPLICATED, "이미 가입된 학번입니다.");
                });

        // MemberEntity 생성
        MemberEntity memberEntity = MemberEntity.builder()
                .memberStuNum(stuNum)
                .memberName(name)
                .memberGen(dto.getMemberGen())
                .memberStatus("신입생")
                .memberScore(0)
                .memberGitUrl(dto.getMemberGitUrl())

                .build();
        memberRepository.save(memberEntity);

        // DetailMemberEntity 먼저 생성
        DetailMemberEntity detailMemberEntity = DetailMemberEntity.builder()
                .memberEmail(dto.getMemberEmail())
                .memberPw(encoder.encode(pw))
                .memberMajor(dto.getMemberMajor())
                .memberPhone(dto.getMemberPhone())
                .memberEntity(memberEntity)
                .build();
        detailMemberRepository.save(detailMemberEntity);

        return name;
    }

    //로그인
    public String signIn(MemberDTO.MemberSignInRequest dto) {
        String stuNum = dto.getMemberStuNum();
        String pw = dto.getMemberPw();
        Long expireTimeMs = 1000 * 60 * 60l;

        // Email 및 password 빈칸 체크
        if (stuNum.isBlank() || pw.isBlank()) {
            throw new AppException(ErrorCode.INVALID_INPUT, "필수 입력 사항을 입력해 주세요.");
        }

        // Email 없는 경우
        MemberEntity selectedMember = memberRepository.findByMemberStuNum(stuNum)
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND, "로그인에 실패했습니다."));

        // password 틀린 경우
        if(!encoder.matches(pw, selectedMember.getDetailMemberEntity().getMemberPw())){
            throw new AppException(ErrorCode.INVALID_INPUT, "로그인에 실패했습니다.");
        }

        return JwtUtil.createToken(selectedMember.getMemberStuNum(), secretKey, expireTimeMs);
    }

    public MemberEntity findByMemberStuNum(String memberStuNum) {
        return memberRepository.findByMemberStuNum(memberStuNum)
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND, "해당 학번은 등록되지 않았습니다."));
    }


    public MemberDTO.MemberMyPageResponse getMyPage() {
        MemberEntity memberEntity = memberRepository.findByMemberStuNum(JwtUtil.getMemberStuNumFromToken())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, "오류가 발생했습니다."));

        MemberDTO.MemberMyPageResponse myPageResponse = MemberDTO.MemberMyPageResponse.builder()
                .memberStuNum(memberEntity.getMemberStuNum())
                .memberName(memberEntity.getMemberName())
                .memberGen(memberEntity.getMemberGen())
//                .memberMajor(memberEntity.getMemberMajor())
//                .memberPhone(memberEntity.getMemberPhone())
                .memberStatus(memberEntity.getMemberStatus())
                .memberScore(memberEntity.getMemberScore())
//                .memberEmail(memberEntity.getMemberEmail())
                .memberGitUrl(memberEntity.getMemberGitUrl())
                .build();
        return myPageResponse;
    }

    public void findUser(MemberDTO.MemberFindPwRequest dto) {
        String stuNum = dto.getMemberStuNum();
        String name = dto.getMemberName();

        // 학번 및 password 빈칸 체크
        if (stuNum.isBlank() || name.isBlank()) {
            throw new AppException(ErrorCode.INVALID_INPUT, "필수 입력 사항을 입력해 주세요.");
        }

        // 학번과 이름이 DB에 없는 경우
        MemberEntity selectedMember = memberRepository.findByMemberStuNum(stuNum).orElse(null);

        if(selectedMember == null || !selectedMember.getMemberName().equals(name)){
            throw new AppException(ErrorCode.INVALID_INPUT, "계정 찾기를 실패했습니다.");
        }

        // 추후 이메일로 임의 수정된 비밀번호 전송 로직 추가
    }

    // 상단 사용자 정보 불러오기
    public MemberDTO.NameScoreResponse myUser() {
        String stuNum = JwtUtil.getMemberStuNumFromToken();

        MemberEntity selectedMember = memberRepository.findByMemberStuNum(stuNum)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND,"정보가 존재하지 않습니다."));

        return MemberDTO.NameScoreResponse.builder()
                .memberName(selectedMember.getMemberName())
                .memberScore(selectedMember.getMemberScore())
                .build();
    }

    public List<MemberDTO.NameScoreResponse> memberRank() {

        List<MemberEntity> memberEntityList = memberRepository.findAll();
        List<MemberDTO.NameScoreResponse> nameScoreResponseList = new ArrayList<>();

        // 리스트 체크
        if(memberEntityList.isEmpty()) {
            throw new AppException(ErrorCode.NOT_FOUND,"정보가 존재하지 않습니다.");
        }

        // 저장
        for(MemberEntity memberEntity: memberEntityList) {
            MemberDTO.NameScoreResponse dto = MemberDTO.NameScoreResponse.builder()
                    .memberName(memberEntity.getMemberName())
                    .memberScore(memberEntity.getMemberScore())
                    .build();
            nameScoreResponseList.add(dto);
        }
        return nameScoreResponseList;
    }
}
