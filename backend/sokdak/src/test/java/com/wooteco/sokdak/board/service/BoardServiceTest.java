package com.wooteco.sokdak.board.service;

import static com.wooteco.sokdak.util.fixture.BoardFixture.BOARD_REQUEST_1;
import static com.wooteco.sokdak.util.fixture.BoardFixture.BOARD_REQUEST_2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.wooteco.sokdak.board.domain.Board;
import com.wooteco.sokdak.board.domain.PostBoard;
import com.wooteco.sokdak.board.dto.BoardsResponse;
import com.wooteco.sokdak.board.dto.NewBoardResponse;
import com.wooteco.sokdak.board.repository.BoardRepository;
import com.wooteco.sokdak.board.repository.PostBoardRepository;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.exception.MemberNotFoundException;
import com.wooteco.sokdak.member.repository.MemberRepository;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.post.repository.PostRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Sql("classpath:truncate.sql")
class BoardServiceTest {

    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostBoardRepository postBoardRepository;

    private Post post;

    @DisplayName("게시판을 생성한다.")
    @Test
    void createBoard() {
        NewBoardResponse board = boardService.createBoard(BOARD_REQUEST_1);

        Optional<Board> savedBoard = boardRepository.findById(board.getId());

        assertAll(
                () -> assertThat(savedBoard).isNotNull(),
                () -> assertThat(savedBoard.get().getTitle()).isEqualTo(BOARD_REQUEST_1.getName())
        );
    }

    @DisplayName("게시판 목록을 조회한다.")
    @Test
    void findBoards() {
        boardService.createBoard(BOARD_REQUEST_1);
        boardService.createBoard(BOARD_REQUEST_2);

        BoardsResponse boards = boardService.findBoards();

        assertThat(boards.getBoards()).hasSize(2)
                .extracting("title")
                .containsExactly(BOARD_REQUEST_1.getName(), BOARD_REQUEST_2.getName());
    }

    @DisplayName("게시글과 게시판을 연결한다.")
    @Test
    void savePostBoard() {
        Member member = memberRepository.findById(1L).get();
        post = Post.builder()
                .title("제목")
                .content("본문")
                .member(member)
                .likes(new ArrayList<>())
                .build();
        postRepository.save(post);
        Board board = Board.builder()
                .name("Hot 게시판")
                .build();
        Board savedBoard = boardRepository.save(board);

        boardService.savePostBoard(post, savedBoard.getId());
        Optional<PostBoard> postBoard = postBoardRepository.findPostBoardByPostAndBoard(post, board);

        assertAll(
                () -> assertThat(postBoard).isNotEmpty(),
                () -> assertThat(postBoard.get().getBoard().getTitle()).isEqualTo("Hot 게시판"),
                () -> assertThat(postBoard.get().getPost().getTitle()).isEqualTo("제목")
        );
    }
}
