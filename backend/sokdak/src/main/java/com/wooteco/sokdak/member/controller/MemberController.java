package com.wooteco.sokdak.member.controller;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.member.dto.EmailRequest;
import com.wooteco.sokdak.member.dto.SignupRequest;
import com.wooteco.sokdak.member.dto.UniqueResponse;
import com.wooteco.sokdak.member.dto.VerificationRequest;
import com.wooteco.sokdak.member.service.EmailService;
import com.wooteco.sokdak.member.service.MemberService;
import com.wooteco.sokdak.member.dto.NicknameResponse;
import com.wooteco.sokdak.member.dto.NicknameUpdateRequest;
import com.wooteco.sokdak.support.token.Login;
import com.wooteco.sokdak.support.token.TokenManager;
import javax.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final EmailService emailService;
    private final MemberService memberService;
    private final TokenManager tokenManager;

    public MemberController(EmailService emailService, MemberService memberService,
                            TokenManager tokenManager) {
        this.emailService = emailService;
        this.memberService = memberService;
        this.tokenManager = tokenManager;
    }

    @PostMapping("/signup/email")
    public ResponseEntity<Void> sendEmail(@RequestBody EmailRequest emailRequest) {
        emailService.sendCodeToValidUser(emailRequest);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/signup/email/verification")
    public ResponseEntity<Void> verifyAuthCode(@Valid @RequestBody VerificationRequest verificationRequest) {
        emailService.verifyAuthCode(verificationRequest);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/signup/exists", params = "username")
    public ResponseEntity<UniqueResponse> validateUniqueUsername(@RequestParam String username) {
        UniqueResponse uniqueResponse = memberService.checkUniqueUsername(username);
        return ResponseEntity.ok(uniqueResponse);
    }

    @GetMapping(value = "/signup/exists", params = "nickname")
    public ResponseEntity<UniqueResponse> validateUniqueNickname(@RequestParam String nickname) {
        UniqueResponse uniqueResponse = memberService.checkUniqueNickname(nickname);
        return ResponseEntity.ok(uniqueResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@Valid @RequestBody SignupRequest signupRequest) {
        memberService.signUp(signupRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/nickname")
    public ResponseEntity<NicknameResponse> findNickname(@Login AuthInfo authInfo) {
        NicknameResponse nicknameResponse = memberService.findNickname(authInfo);
        return ResponseEntity.ok(nicknameResponse);
    }

    @PatchMapping("/nickname")
    public ResponseEntity<Void> editNickname(@RequestBody NicknameUpdateRequest nicknameUpdateRequest,
                                             @Login AuthInfo authInfo) {
        memberService.editNickname(nicknameUpdateRequest, authInfo);
        String accessToken = getTokenOfNewNickname(nicknameUpdateRequest, authInfo);
        return ResponseEntity.noContent()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .build();
    }

    private String getTokenOfNewNickname(NicknameUpdateRequest nicknameUpdateRequest, AuthInfo authInfo) {
        AuthInfo newAuthInfo = new AuthInfo(authInfo.getId(), authInfo.getRole(), nicknameUpdateRequest.getNickname());
        return tokenManager.createAccessToken(newAuthInfo);
    }
}
