package com.recipia.member.adapter.out.persistenceAdapter;

import com.recipia.member.adapter.out.persistence.constant.MemberStatus;
import com.recipia.member.application.port.out.port.MemberManagementPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MemberManagementAdapter implements MemberManagementPort {

    private final MemberRepository memberRepository;


    /**
     * [READ] email로 중복체크
     * 이메일이 DB에 존재하지 않으면(중복되지 않으면) true, 있으면(중복되면) false 반환한다.
     */
    @Override
    public boolean isEmailAvailable(String email) {
        return !memberRepository.existsByEmailAndStatusNot(email, MemberStatus.DEACTIVATED);
    }

    /**
     * [READ] telNo로 중복체크
     * 전화번호가 DB에 존재하지 않으면(중복되지 않으면) true, 있으면(중복되면) false 반환한다.
     */
    @Override
    public boolean isTelNoAvailable(String telNo) {
        // 휴대폰 번호를 기반으로 DB에서 회원 존재 여부를 확인
        // 존재하지 않으면 true, 존재하면 false 반환
        return !memberRepository.existsByTelNoAndStatusNot(telNo, MemberStatus.DEACTIVATED);
    }

    /**
     * [READ] nickname으로 중복체크
     * 닉네임이 DB에 존재하지 않으면(중복되지 않으면) true, 있으면(중복되면) false 반환한다.
     */
    @Override
    public boolean isNicknameAvailable(String nickname) {
        return !memberRepository.existsByNicknameAndStatusNot(nickname, MemberStatus.DEACTIVATED);
    }


}
