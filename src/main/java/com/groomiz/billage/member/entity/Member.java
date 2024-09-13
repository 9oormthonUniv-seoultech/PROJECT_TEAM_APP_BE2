package com.groomiz.billage.member.entity;

import com.groomiz.billage.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "is_admin", nullable = false)
    private boolean isAdmin;

    @Builder.Default
    @Column(name = "is_valid", nullable = false)
    private boolean isValid = false;

    @Column(name = "agreed_to_terms", nullable = false)
    private boolean agreedToTerms;  // 약관 동의 여부

    @Column(name = "student_number")
    private Integer studentNumber;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private College college;

    @Enumerated(EnumType.STRING)
    private Major major;

    @Column(name = "student_email")
    private String studentEmail;

    @Column(name = "verification_code")
    private Integer verificationCode;

    @Builder
    public Member(String username, String password, Role role, boolean isAdmin, boolean isValid, Integer studentNumber, String phoneNumber, boolean agreedToTerms) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.isAdmin = isAdmin;
        this.isValid = isValid;
        this.studentNumber = studentNumber;
        this.phoneNumber = phoneNumber;
        this.agreedToTerms = agreedToTerms;
    }
}
