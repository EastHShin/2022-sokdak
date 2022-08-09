package com.wooteco.sokdak.profile.acceptance;

import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpGetWithAuthorization;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPatchWithAuthorization;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPost;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.wooteco.sokdak.auth.dto.LoginRequest;
import com.wooteco.sokdak.profile.dto.EditedNicknameRequest;
import com.wooteco.sokdak.profile.dto.NicknameResponse;
import com.wooteco.sokdak.util.AcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class ProfileAcceptanceTest extends AcceptanceTest {

    @DisplayName("닉네임을 수정할 수 있다.")
    @Test
    void editNickname() {
        String nickname = "chrisNick2";
        EditedNicknameRequest nicknameRequest = new EditedNicknameRequest(nickname);

        ExtractableResponse<Response> patchWithAuthorization = httpPatchWithAuthorization(nicknameRequest,
                "/members/nickname", getToken());
        ExtractableResponse<Response> getWithAuthorization = httpGetWithAuthorization("/members/nickname", getToken());

        NicknameResponse nicknameResponse = getWithAuthorization.body()
                .jsonPath()
                .getObject(".", NicknameResponse.class);
        assertAll(
                () -> assertThat(patchWithAuthorization.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(nicknameResponse.getNickname()).isEqualTo(nickname)
        );
    }

    @DisplayName("중복된 닉네임으로 수정할 수 없다.")
    @Test
    void editNickname_Exception_Duplicate() {
        EditedNicknameRequest wrongNicknameRequest = new EditedNicknameRequest("eastNickname");

        ExtractableResponse<Response> patchWithAuthorization = httpPatchWithAuthorization(wrongNicknameRequest,
                "/members/nickname", getToken());
        ExtractableResponse<Response> getWithAuthorization = httpGetWithAuthorization("/members/nickname", getToken());

        NicknameResponse nicknameResponse = getWithAuthorization.body()
                .jsonPath()
                .getObject(".", NicknameResponse.class);
        assertAll(
                () -> assertThat(patchWithAuthorization.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(nicknameResponse.getNickname()).isEqualTo("chrisNickname")
        );
    }

    private String getToken() {
        LoginRequest loginRequest = new LoginRequest("chris", "Abcd123!@");
        return httpPost(loginRequest, "/login").header(AUTHORIZATION);
    }
}
