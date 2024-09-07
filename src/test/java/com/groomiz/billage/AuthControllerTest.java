package com.groomiz.billage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.groomiz.billage.auth.dto.LoginRequest;
import com.groomiz.billage.auth.jwt.JwtUtil;
import com.groomiz.billage.member.dto.JoinRequest;
import com.groomiz.billage.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberService memberService;

    @Autowired
    private JwtUtil jwtUtil;

    @BeforeEach
    public void setUp() {
        JoinRequest joinRequest = new JoinRequest("test", "1234");

        if(!memberService.isExists(joinRequest.getUsername())) {
            memberService.register(joinRequest);
        }

    }

    @Test
    @DisplayName("로그인 성공하였습니다.")
    public void testLoginSuccess() throws Exception {
        LoginRequest loginRequest = new LoginRequest("test", "1234");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"))
                .andExpect(header().exists("RefreshToken"));
    }

    @Test
    @DisplayName("토큰이 만료 되었습니다.")
    public void testExpiredToken() throws Exception {


        // 만료된 Access Token을 사용한 요청 테스트
        String expiredToken = jwtUtil.createJwt("AccessToken", "test", "ADMIN", 1L);

        Thread.sleep(1000);

        mockMvc.perform(get("/api/admin")
                        .header("Authorization", "Bearer " + expiredToken))
                .andExpect(status().isUnauthorized());
    }



}
