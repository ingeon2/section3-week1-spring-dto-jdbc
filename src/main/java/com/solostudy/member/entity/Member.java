package com.solostudy.member.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
public class Member {
    @Id // 얘가 프라이머리 키 라는 뜻, Member 클래스는 데이터베이스 테이블에서 MEMBER 테이블과 매핑
    Long memberId;

    String email;
    String name;
    String phone;

}
