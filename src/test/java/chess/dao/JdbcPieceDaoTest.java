package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.Board;
import chess.domain.ChessGame;
import chess.domain.Color;
import chess.domain.GameStatus;
import chess.domain.piece.Pawn;
import chess.domain.piece.Rook;
import chess.domain.position.Position;
import chess.dto.GameInfoDto;
import chess.dto.PieceInfoDto;
import java.sql.SQLException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JdbcPieceDaoTest {
    private final JdbcPieceDao pieceDao = new JdbcPieceDao();

    @BeforeEach
    public void deleteAll() {
        pieceDao.deleteAll();
    }

    @Test
    public void connection() {
        try (final var connection = ConnectionProvider.getConnection()) {
            assertThat(connection).isNotNull();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void save() {
        // given
        Position a1 = Position.from("a1");
        Pawn pawn = Pawn.create(Color.BLACK);
        PieceInfoDto a1_pawn = PieceInfoDto.create(a1, pawn);

        Position b2 = Position.from("b2");
        Rook rook = Rook.create(Color.BLACK);
        PieceInfoDto b2_rook = PieceInfoDto.create(b2, rook);

        // when, then
        pieceDao.save(1, List.of(a1_pawn, b2_rook));
    }

    @Test
    public void findById() {
        // given
        Position a1 = Position.from("a1");
        Pawn pawn = Pawn.create(Color.BLACK);
        PieceInfoDto a1_pawn = PieceInfoDto.create(a1, pawn);

        Position b2 = Position.from("b2");
        Rook rook = Rook.create(Color.BLACK);
        PieceInfoDto b2_rook = PieceInfoDto.create(b2, rook);

        pieceDao.save(1, List.of(a1_pawn, b2_rook));

        // when
        List<PieceInfoDto> game1_pieces = pieceDao.findById(1);
        PieceInfoDto firstPiece = game1_pieces.get(0);
        PieceInfoDto secondPiece = game1_pieces.get(1);

        // then
        assertThat(firstPiece.getPosition()).isEqualTo(a1);
        assertThat(firstPiece.getPiece()).isEqualTo(pawn); // piece 캐싱한적 없는데 왜 성공?
        assertThat(secondPiece.getPosition()).isEqualTo(b2);
        assertThat(secondPiece.getPiece()).isEqualTo(rook);
    }

    @Test
    public void updateById() {
        // given
        Position a1 = Position.from("a1");
        Pawn pawn = Pawn.create(Color.BLACK);
        PieceInfoDto a1_pawn = PieceInfoDto.create(a1, pawn);

        Rook rook = Rook.create(Color.BLACK);
        PieceInfoDto a1_rook = PieceInfoDto.create(a1, rook);

        pieceDao.save(1, List.of(a1_pawn));

        // when
        pieceDao.updateById(1, List.of(a1_rook));

        List<PieceInfoDto> game1_pieces = pieceDao.findById(1);
        PieceInfoDto piece = game1_pieces.get(0);

        // then
        assertThat(piece.getPosition()).isEqualTo(a1);
        assertThat(piece.getPiece()).isEqualTo(rook); // piece 캐싱한적 없는데 왜 성공?
    }

    @Test
    public void deleteById() {
        // given
        Position a1 = Position.from("a1");
        Pawn pawn = Pawn.create(Color.BLACK);
        PieceInfoDto a1_pawn = PieceInfoDto.create(a1, pawn);

        Position b2 = Position.from("b2");
        Rook rook = Rook.create(Color.BLACK);
        PieceInfoDto b2_rook = PieceInfoDto.create(b2, rook);

        pieceDao.save(1, List.of(a1_pawn, b2_rook));

        // when
        pieceDao.deleteById(1);

        assertThat(pieceDao.findById(1)).isEmpty();
    }

}