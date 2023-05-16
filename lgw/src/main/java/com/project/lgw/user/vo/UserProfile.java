package com.project.lgw.user.vo;

import lombok.Data;

@Data
public class UserProfile {

    private String id;

    // 사용자 별명
    private String nickname;

    // 사용자 이름
    private String name;

    // 	사용자 메일 주소
    private String email;

    // 성별
    private String gender;

    // 사용자 연령대
    private String age;

    // 사용자 생일(MM-DD 형식)
    private String birthday;

    // 사용자 프로필 사진 URL
    private String profile_image;

    // 출생연도
    private String birthyear;

    // 휴대전화번호
    private String mobile;
    

}
