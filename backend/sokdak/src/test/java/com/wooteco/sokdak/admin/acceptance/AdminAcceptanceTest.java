package com.wooteco.sokdak.admin.acceptance;

import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpDeleteWithAuthorization;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpGetWithAuthorization;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPatchWithAuthorization;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPost;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPostWithAuthorization;
import static com.wooteco.sokdak.util.fixture.PostFixture.addNewPost;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.wooteco.sokdak.admin.dto.PostReportElement;
import com.wooteco.sokdak.admin.dto.TicketElement;
import com.wooteco.sokdak.admin.dto.TicketRequest;
import com.wooteco.sokdak.auth.dto.LoginRequest;
import com.wooteco.sokdak.util.AcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("관리자 인수테스트")
public class AdminAcceptanceTest extends AcceptanceTest {

    private static final int reportCount = 5;
    private static final String SERIAL_NUMBER = "test1@gmail.com";

    @DisplayName("관리자 페이지에서 특정 게시글을 삭제할 수 있다.")
    @Test
    void deletePost() {
        String adminToken = getAdminToken();
        addNewPost();

        httpDeleteWithAuthorization("admin/posts/1", adminToken);

        ExtractableResponse<Response> actual = httpGetWithAuthorization("posts/1", adminToken);
        assertAll(
                () -> assertThat(actual.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value()),
                () -> assertThat(actual.body().asString()).contains("게시물을 찾을 수 없습니다.")
        );
    }

    @DisplayName("관리자 페이지에서 특정 글 블락 처리, 신고목록 조회, 블락 해제가 가능하다.")
    @Test
    void blockAndFindReportsAndunblockPost() {
        String adminToken = getAdminToken();
        addNewPost();

        httpPostWithAuthorization("", "admin/posts/1/postreports/" + reportCount, adminToken);
        ExtractableResponse<Response> response = httpGetWithAuthorization("admin/postreports", adminToken);
        httpDeleteWithAuthorization("admin/posts/1/postreports", adminToken);
        ExtractableResponse<Response> response2 = httpGetWithAuthorization("admin/postreports", adminToken);

        assertAll(
                () -> assertThat(getPostReports(response)).hasSize(reportCount),
                () -> assertThat(getPostReports(response2)).isEmpty()
        );
    }

    @DisplayName("관리자 페이지에서 티켓추가, 티켓목록 조회, 티켓 상태 변경이 가능하다.")
    @Test
    void saveAndFindAndUpdateTicket() {
        String adminToken = getAdminToken();
        TicketRequest initTicketRequest = TicketRequest.builder()
                .serialNumber(SERIAL_NUMBER)
                .used(false)
                .build();
        TicketRequest usedTicketRequest = TicketRequest.builder()
                .serialNumber(SERIAL_NUMBER)
                .used(true)
                .build();
        int lastTicketIndex = 7;

        httpPostWithAuthorization(initTicketRequest, "admin/tickets", adminToken);
        TicketElement initTicketElement = getTicketElements(httpGetWithAuthorization("admin/tickets", adminToken))
                .get(lastTicketIndex);
        httpPatchWithAuthorization(usedTicketRequest, "admin/tickets", adminToken);
        TicketElement usedTicketElement = getTicketElements(httpGetWithAuthorization("admin/tickets", adminToken))
                .get(lastTicketIndex);

        assertAll(
                () -> assertThat(initTicketElement.isUsed()).isFalse(),
                () -> assertThat(usedTicketElement.isUsed()).isTrue()
        );
    }

    private List<TicketElement> getTicketElements(ExtractableResponse<Response> response) {
        return response.body()
                .jsonPath()
                .getList("tickets", TicketElement.class);
    }

    private List<PostReportElement> getPostReports(ExtractableResponse<Response> response) {
        return response.body()
                .jsonPath()
                .getList("postReports", PostReportElement.class);
    }

    private String getAdminToken() {
        LoginRequest loginRequest = new LoginRequest("admin1", "Abcd123!@");
        return httpPost(loginRequest, "/login").header(AUTHORIZATION);
    }
}
