package chess.domain.movestrategy;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.board.Board;
import chess.domain.board.position.InitPosition;
import chess.domain.board.position.Position;
import chess.domain.piece.Bishop;
import chess.domain.piece.King;
import chess.domain.piece.Pawn;
import chess.domain.piece.Piece;
import chess.domain.piece.Queen;
import chess.domain.piece.Rook;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CommonMoveStrategyTest {

    private Map<Position, Piece> squares;

    @BeforeEach
    void setUp() {
        this.squares = InitPosition.initSquares();
    }

    @Test
    @DisplayName("주변이 아군일 때 룩 이동 가능 경로 확인")
    void rookMovableSurroundedByFriends() {

        this.squares.replace(Position.of("c7"), Pawn.createWhite());
        this.squares.replace(Position.of("d7"), Pawn.createWhite());
        this.squares.replace(Position.of("e7"), Pawn.createWhite());
        this.squares.replace(Position.of("f7"), Pawn.createWhite());
        this.squares.replace(Position.of("g7"), Pawn.createWhite());

        this.squares.replace(Position.of("c3"), Pawn.createWhite());
        this.squares.replace(Position.of("d3"), Pawn.createWhite());
        this.squares.replace(Position.of("e3"), Pawn.createWhite());
        this.squares.replace(Position.of("f3"), Pawn.createWhite());
        this.squares.replace(Position.of("g3"), Pawn.createWhite());

        this.squares.replace(Position.of("c6"), Pawn.createWhite());
        this.squares.replace(Position.of("c5"), Pawn.createWhite());
        this.squares.replace(Position.of("c4"), Pawn.createWhite());

        this.squares.replace(Position.of("g6"), Pawn.createWhite());
        this.squares.replace(Position.of("g5"), Pawn.createWhite());
        this.squares.replace(Position.of("g4"), Pawn.createWhite());

        this.squares.replace(Position.of("e5"), Rook.createWhite());

        Board board = new Board(this.squares);

        MoveStrategy moveStrategy = Rook.createWhite().moveStrategy();
        assertThat(moveStrategy.currentPositionMoveStrategy(board, Position.of("e5")))
            .containsExactlyInAnyOrder(
                Position.of("e6"), Position.of("e4"),
                Position.of("d5"), Position.of("f5"));
    }

    @Test
    @DisplayName("주변이 적일 때 룩 이동 가능 경로 확인")
    void rookMovableSurroundedByEnemies() {
        this.squares.replace(Position.of("c7"), Pawn.createBlack());
        this.squares.replace(Position.of("d7"), Pawn.createBlack());
        this.squares.replace(Position.of("e7"), Pawn.createBlack());
        this.squares.replace(Position.of("f7"), Pawn.createBlack());
        this.squares.replace(Position.of("g7"), Pawn.createBlack());

        this.squares.replace(Position.of("c3"), Pawn.createBlack());
        this.squares.replace(Position.of("d3"), Pawn.createBlack());
        this.squares.replace(Position.of("e3"), Pawn.createBlack());
        this.squares.replace(Position.of("f3"), Pawn.createBlack());
        this.squares.replace(Position.of("g3"), Pawn.createBlack());

        this.squares.replace(Position.of("c6"), Pawn.createBlack());
        this.squares.replace(Position.of("c5"), Pawn.createBlack());
        this.squares.replace(Position.of("c4"), Pawn.createBlack());

        this.squares.replace(Position.of("g6"), Pawn.createBlack());
        this.squares.replace(Position.of("g5"), Pawn.createBlack());
        this.squares.replace(Position.of("g4"), Pawn.createBlack());

        this.squares.replace(Position.of("e5"), Rook.createWhite());

        Board board = new Board(this.squares);

        MoveStrategy moveStrategy = Rook.createWhite().moveStrategy();
        assertThat(moveStrategy.currentPositionMoveStrategy(board, Position.of("e5")))
            .containsExactlyInAnyOrder(
                Position.of("e6"), Position.of("e7"),
                Position.of("e4"), Position.of("e3"),
                Position.of("d5"), Position.of("c5"),
                Position.of("f5"), Position.of("g5"));
    }

    @Test
    @DisplayName("주변이 아군일 때 비숍 이동 가능 경로 확인")
    void bishopMovableSurroundedByFriends() {
        this.squares.replace(Position.of("c7"), Pawn.createWhite());
        this.squares.replace(Position.of("d7"), Pawn.createWhite());
        this.squares.replace(Position.of("e7"), Pawn.createWhite());
        this.squares.replace(Position.of("f7"), Pawn.createWhite());
        this.squares.replace(Position.of("g7"), Pawn.createWhite());

        this.squares.replace(Position.of("c3"), Pawn.createWhite());
        this.squares.replace(Position.of("d3"), Pawn.createWhite());
        this.squares.replace(Position.of("e3"), Pawn.createWhite());
        this.squares.replace(Position.of("f3"), Pawn.createWhite());
        this.squares.replace(Position.of("g3"), Pawn.createWhite());

        this.squares.replace(Position.of("c6"), Pawn.createWhite());
        this.squares.replace(Position.of("c5"), Pawn.createWhite());
        this.squares.replace(Position.of("c4"), Pawn.createWhite());

        this.squares.replace(Position.of("g6"), Pawn.createWhite());
        this.squares.replace(Position.of("g5"), Pawn.createWhite());
        this.squares.replace(Position.of("g4"), Pawn.createWhite());

        this.squares.replace(Position.of("e5"), Bishop.createWhite());

        Board board = new Board(this.squares);

        MoveStrategy moveStrategy = Bishop.createWhite().moveStrategy();
        assertThat(moveStrategy.currentPositionMoveStrategy(board, Position.of("e5")))
            .containsExactlyInAnyOrder(
                Position.of("d6"), Position.of("f6"),
                Position.of("d4"), Position.of("f4"));
    }

    @Test
    @DisplayName("주변이 적일 때 비숍 이동 가능 경로 확인")
    void bishopMovableSurroundedByEnemies() {
        this.squares.replace(Position.of("c7"), Pawn.createBlack());
        this.squares.replace(Position.of("d7"), Pawn.createBlack());
        this.squares.replace(Position.of("e7"), Pawn.createBlack());
        this.squares.replace(Position.of("f7"), Pawn.createBlack());
        this.squares.replace(Position.of("g7"), Pawn.createBlack());

        this.squares.replace(Position.of("c3"), Pawn.createBlack());
        this.squares.replace(Position.of("d3"), Pawn.createBlack());
        this.squares.replace(Position.of("e3"), Pawn.createBlack());
        this.squares.replace(Position.of("f3"), Pawn.createBlack());
        this.squares.replace(Position.of("g3"), Pawn.createBlack());

        this.squares.replace(Position.of("c6"), Pawn.createBlack());
        this.squares.replace(Position.of("c5"), Pawn.createBlack());
        this.squares.replace(Position.of("c4"), Pawn.createBlack());

        this.squares.replace(Position.of("g6"), Pawn.createBlack());
        this.squares.replace(Position.of("g5"), Pawn.createBlack());
        this.squares.replace(Position.of("g4"), Pawn.createBlack());

        this.squares.replace(Position.of("e5"), Bishop.createWhite());

        Board board = new Board(this.squares);

        MoveStrategy moveStrategy = Bishop.createWhite().moveStrategy();
        assertThat(moveStrategy.currentPositionMoveStrategy(board, Position.of("e5")))
            .containsExactlyInAnyOrder(
                Position.of("d6"), Position.of("c7"),
                Position.of("f6"), Position.of("g7"),
                Position.of("d4"), Position.of("c3"),
                Position.of("f4"), Position.of("g3"));
    }

    @Test
    @DisplayName("주변이 아군일 때 퀸 이동 가능 경로 확인")
    void queenMovableSurroundedByFriends() {
        this.squares.replace(Position.of("c7"), Pawn.createWhite());
        this.squares.replace(Position.of("d7"), Pawn.createWhite());
        this.squares.replace(Position.of("e7"), Pawn.createWhite());
        this.squares.replace(Position.of("f7"), Pawn.createWhite());
        this.squares.replace(Position.of("g7"), Pawn.createWhite());

        this.squares.replace(Position.of("c3"), Pawn.createWhite());
        this.squares.replace(Position.of("d3"), Pawn.createWhite());
        this.squares.replace(Position.of("e3"), Pawn.createWhite());
        this.squares.replace(Position.of("f3"), Pawn.createWhite());
        this.squares.replace(Position.of("g3"), Pawn.createWhite());

        this.squares.replace(Position.of("c6"), Pawn.createWhite());
        this.squares.replace(Position.of("c5"), Pawn.createWhite());
        this.squares.replace(Position.of("c4"), Pawn.createWhite());

        this.squares.replace(Position.of("g6"), Pawn.createWhite());
        this.squares.replace(Position.of("g5"), Pawn.createWhite());
        this.squares.replace(Position.of("g4"), Pawn.createWhite());

        this.squares.replace(Position.of("e5"), Queen.createWhite());

        Board board = new Board(this.squares);

        MoveStrategy moveStrategy = Queen.createWhite().moveStrategy();
        assertThat(moveStrategy.currentPositionMoveStrategy(board, Position.of("e5")))
            .containsExactlyInAnyOrder(
                Position.of("e6"), Position.of("e4"),
                Position.of("d5"), Position.of("f5"),
                Position.of("d6"), Position.of("f6"),
                Position.of("d4"), Position.of("f4"));
    }

    @Test
    @DisplayName("주변이 적일 때 퀸 이동 가능 경로 확인")
    void queenMovableSurroundedByEnemies() {
        this.squares.replace(Position.of("c7"), Pawn.createBlack());
        this.squares.replace(Position.of("d7"), Pawn.createBlack());
        this.squares.replace(Position.of("e7"), Pawn.createBlack());
        this.squares.replace(Position.of("f7"), Pawn.createBlack());
        this.squares.replace(Position.of("g7"), Pawn.createBlack());

        this.squares.replace(Position.of("c3"), Pawn.createBlack());
        this.squares.replace(Position.of("d3"), Pawn.createBlack());
        this.squares.replace(Position.of("e3"), Pawn.createBlack());
        this.squares.replace(Position.of("f3"), Pawn.createBlack());
        this.squares.replace(Position.of("g3"), Pawn.createBlack());

        this.squares.replace(Position.of("c6"), Pawn.createBlack());
        this.squares.replace(Position.of("c5"), Pawn.createBlack());
        this.squares.replace(Position.of("c4"), Pawn.createBlack());

        this.squares.replace(Position.of("g6"), Pawn.createBlack());
        this.squares.replace(Position.of("g5"), Pawn.createBlack());
        this.squares.replace(Position.of("g4"), Pawn.createBlack());

        this.squares.replace(Position.of("e5"), Queen.createWhite());

        Board board = new Board(this.squares);

        MoveStrategy moveStrategy = Queen.createWhite().moveStrategy();
        assertThat(moveStrategy.currentPositionMoveStrategy(board, Position.of("e5")))
            .containsExactlyInAnyOrder(
                Position.of("e6"), Position.of("e7"),
                Position.of("e4"), Position.of("e3"),
                Position.of("d5"), Position.of("c5"),
                Position.of("f5"), Position.of("g5"),
                Position.of("d6"), Position.of("c7"),
                Position.of("f6"), Position.of("g7"),
                Position.of("d4"), Position.of("c3"),
                Position.of("f4"), Position.of("g3"));
    }

    @Test
    @DisplayName("주변이 아군일 때 킹 이동 가능 경로 확인")
    void kingMovableSurroundedByFriends() {
        this.squares.replace(Position.of("d7"), Pawn.createWhite());
        this.squares.replace(Position.of("e7"), Pawn.createWhite());
        this.squares.replace(Position.of("f7"), Pawn.createWhite());

        this.squares.replace(Position.of("d3"), Pawn.createWhite());
        this.squares.replace(Position.of("e3"), Pawn.createWhite());
        this.squares.replace(Position.of("f3"), Pawn.createWhite());

        this.squares.replace(Position.of("d6"), Pawn.createWhite());
        this.squares.replace(Position.of("d5"), Pawn.createWhite());
        this.squares.replace(Position.of("d4"), Pawn.createWhite());

        this.squares.replace(Position.of("f6"), Pawn.createWhite());
        this.squares.replace(Position.of("f5"), Pawn.createWhite());
        this.squares.replace(Position.of("f4"), Pawn.createWhite());

        this.squares.replace(Position.of("e5"), King.createWhite());

        Board board = new Board(this.squares);

        MoveStrategy moveStrategy = King.createWhite().moveStrategy();
        assertThat(moveStrategy.currentPositionMoveStrategy(board, Position.of("e5")))
            .containsExactlyInAnyOrder(
                Position.of("e6"), Position.of("e4"));
    }

    @Test
    @DisplayName("주변이 적일 때 킹 이동 가능 경로 확인")
    void kingMovableSurroundedByEnemies() {
        this.squares.replace(Position.of("d7"), Pawn.createBlack());
        this.squares.replace(Position.of("e7"), Pawn.createBlack());
        this.squares.replace(Position.of("f7"), Pawn.createBlack());

        this.squares.replace(Position.of("d3"), Pawn.createBlack());
        this.squares.replace(Position.of("e3"), Pawn.createBlack());
        this.squares.replace(Position.of("f3"), Pawn.createBlack());

        this.squares.replace(Position.of("d6"), Pawn.createBlack());
        this.squares.replace(Position.of("d5"), Pawn.createBlack());
        this.squares.replace(Position.of("d4"), Pawn.createBlack());

        this.squares.replace(Position.of("f6"), Pawn.createBlack());
        this.squares.replace(Position.of("f5"), Pawn.createBlack());
        this.squares.replace(Position.of("f4"), Pawn.createBlack());

        this.squares.replace(Position.of("e5"), King.createWhite());

        Board board = new Board(this.squares);

        MoveStrategy moveStrategy = King.createWhite().moveStrategy();
        assertThat(moveStrategy.currentPositionMoveStrategy(board, Position.of("e5")))
            .containsExactlyInAnyOrder(
                Position.of("e6"), Position.of("e4"),
                Position.of("d5"), Position.of("f5"),
                Position.of("d6"), Position.of("f6"),
                Position.of("d4"), Position.of("f4"));
    }
}