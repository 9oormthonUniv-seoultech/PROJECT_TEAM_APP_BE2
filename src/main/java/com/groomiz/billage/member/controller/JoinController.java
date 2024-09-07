package com.groomiz.billage.member.controller;

import com.groomiz.billage.member.dto.JoinRequest;
import com.groomiz.billage.member.service.MemberService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class JoinController {

    private final MemberService memberService;

    public JoinController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/join")
    public String joinProcess(@RequestBody JoinRequest joinRequest) {

        memberService.register(joinRequest);

        return "ok";
    }

}
