package com.project.lgw.user.vo;

import java.util.Properties;

import lombok.Data;

@Data
public class SnsApiResponse {   
	private Long id;
	
	private String connectedAt;
	
	// API 호출 결과 코드
    private String resultcode;

    // 호출 결과 메시지
    private String message;

    // kakao 전용 
    private Properties properties;
    
    // Profile 상세
    private UserProfile response;
    
    private KakaoAccount kakaoAccount;
    
    public static class KakaoAccount {
        private Boolean profile_needs_agreement;
        private UserProfile profile;
        private Boolean email_needs_agreement;
        private Boolean is_email_valid;
        private Boolean is_email_verified;
        private String email;
        private Boolean age_range_needs_agreement;
        private String age_range;
        private Boolean birthday_needs_agreement;
        private String birthday;
        private String gender;

        public Boolean getProfile_needs_agreement() {
            return profile_needs_agreement;
        }

        public void setProfile_needs_agreement(Boolean profile_needs_agreement) {
            this.profile_needs_agreement = profile_needs_agreement;
        }

        public UserProfile getProfile() {
            return profile;
        }

        public void setProfile(UserProfile profile) {
            this.profile = profile;
        }

        public Boolean getEmail_needs_agreement() {
            return email_needs_agreement;
        }

        public void setEmail_needs_agreement(Boolean email_needs_agreement) {
            this.email_needs_agreement = email_needs_agreement;
        }

        public Boolean getIs_email_valid() {
            return is_email_valid;
        }

        public void setIs_email_valid(Boolean is_email_valid) {
            this.is_email_valid = is_email_valid;
        }

        public Boolean getIs_email_verified() {
            return is_email_verified;
        }

        public void setIs_email_verified(Boolean is_email_verified) {
            this.is_email_verified = is_email_verified;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Boolean getAge_range_needs_agreement() {
            return age_range_needs_agreement;
        }

        public void setAge_range_needs_agreement(Boolean age_range_needs_agreement) {
            this.age_range_needs_agreement = age_range_needs_agreement;
        }
        
        public String getAge_range() {
            return age_range;
        }

        public void setAge_range(String age_range) {
            this.age_range = age_range;
        }
        
        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }
        
        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

		public Boolean getBirthday_needs_agreement() {
			return birthday_needs_agreement;
		}

		public void setBirthday_needs_agreement(Boolean birthday_needs_agreement) {
			this.birthday_needs_agreement = birthday_needs_agreement;
		}
    }

}
