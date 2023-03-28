package chess.dao;

import chess.domain.Board;
import chess.domain.ChessGame;
import chess.domain.Color;
import chess.domain.GameStatus;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceType;
import chess.domain.position.File;
import chess.domain.position.Position;
import chess.domain.position.Rank;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JdbcChessGameDao implements ChessGameDao {
    private static final String SERVER = "localhost:3306"; // MySQL 서버 주소
    private static final String DATABASE = "chess"; // MySQL DATABASE 이름
    private static final String OPTION = "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USERNAME = "username"; //  MySQL 서버 아이디
    private static final String PASSWORD = "password"; // MySQL 서버 비밀번호

    public Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://" + SERVER + "/" + DATABASE + OPTION, USERNAME, PASSWORD);
        } catch (final SQLException e) {
            System.err.println("DB 연결 오류:" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Integer> findAllPossibleId() {
        final var query = "SELECT game_id FROM game WHERE game_status != ? AND game_status != ?";
        try (final var connection = getConnection();
             final var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "END");
            preparedStatement.setString(2, "CATCH");

            final var resultSet = preparedStatement.executeQuery();
            List<Integer> ids = new ArrayList<>();
            while (resultSet.next()) {
                ids.add(Integer.parseInt(resultSet.getString("game_id")));
            }
            return ids;
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Integer> findAllImpossibleId() {
        final var query = "SELECT game_id FROM game WHERE game_status = ? OR game_status = ?";
        try (final var connection = getConnection();
             final var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "END");
            preparedStatement.setString(2, "CATCH");

            final var resultSet = preparedStatement.executeQuery();
            List<Integer> ids = new ArrayList<>();
            while (resultSet.next()) {
                ids.add(Integer.parseInt(resultSet.getString("game_id")));
            }
            return ids;
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ChessGame findById(int gameId) {
        GameStatus gameStatus = GameStatus.NONE;
        Color turn = Color.NONE;
        Map<Position, Piece> pieces = new HashMap<>(64);

        final var gameQuery = "SELECT * FROM game WHERE game_id = ?";
        try (final var connection = getConnection();
             final var preparedStatement = connection.prepareStatement(gameQuery)) {
            preparedStatement.setInt(1, gameId);

            final var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                gameStatus = GameStatus.valueOf(resultSet.getString("game_status"));
                turn = Color.valueOf(resultSet.getString("game_turn"));
            } else {
                return null;
            }
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }

        final var pieceQuery = "SELECT * FROM piece WHERE game_id = ?";
        try (final var connection = getConnection();
             final var preparedStatement = connection.prepareStatement(pieceQuery)) {
            preparedStatement.setInt(1, gameId);

            final var resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                File pieceFile = File.valueOf(resultSet.getString("piece_file"));
                Rank pieceRank = Rank.valueOf(resultSet.getString("piece_rank"));
                Color color = Color.valueOf(resultSet.getString("piece_team"));
                PieceType pieceType = PieceType.valueOf(resultSet.getString("piece_type"));

                Position position = Position.from(pieceFile, pieceRank);
                Piece piece = pieceType.getInstance(color);
                pieces.put(position, piece);
            }
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }

        return new ChessGame(new Board(pieces), turn, gameStatus);
    }

    @Override
    public void save(int gameId, ChessGame chessGame) {
        final var gameQuery = "INSERT INTO game VALUES(?, ?, ?)";
        try (final var connection = getConnection();
             final var preparedStatement = connection.prepareStatement(gameQuery)) {
            preparedStatement.setInt(1, gameId);
            preparedStatement.setString(2, chessGame.getStatus().name());
            preparedStatement.setString(3, chessGame.getTurn().name());
            preparedStatement.executeUpdate();
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }

        Map<Position, Piece> positionAndMap = chessGame.getBoard().getPositionAndPiece();
        for (final Map.Entry<Position, Piece> positionPieceEntry : positionAndMap.entrySet()) {
            final var pieceQuery = "INSERT INTO piece VALUES(?, ?, ?, ?, ?)";
            try (final var connection = getConnection();
                 final var preparedStatement = connection.prepareStatement(pieceQuery)) {
                preparedStatement.setInt(1, gameId);
                preparedStatement.setString(2, positionPieceEntry.getKey().getFile().name());
                preparedStatement.setString(3, positionPieceEntry.getKey().getRank().name());
                preparedStatement.setString(4, positionPieceEntry.getValue().getType().name());
                preparedStatement.setString(5, positionPieceEntry.getValue().getColor().name());
                preparedStatement.executeUpdate();
            } catch (final SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void updateById(int gameId, ChessGame chessGame) {
        deleteById(gameId);
        save(gameId, chessGame);
    }

    @Override
    public void deleteById(int gameId) {
        final var gameQuery = "DELETE FROM game WHERE game_id = ?";
        try (final var connection = getConnection();
             final var preparedStatement = connection.prepareStatement(gameQuery)) {
            preparedStatement.setInt(1, gameId);
            preparedStatement.executeUpdate();
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }

        final var pieceQuery = "DELETE FROM piece WHERE game_id = ?";
        try (final var connection = getConnection();
             final var preparedStatement = connection.prepareStatement(pieceQuery)) {
            preparedStatement.setInt(1, gameId);
            preparedStatement.executeUpdate();
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteAll() {
        final var gameQuery = "DELETE FROM game";
        try (final var connection = getConnection();
             final var preparedStatement = connection.prepareStatement(gameQuery)) {
            preparedStatement.executeUpdate();
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }

        final var pieceQuery = "DELETE FROM piece";
        try (final var connection = getConnection();
             final var preparedStatement = connection.prepareStatement(pieceQuery)) {
            preparedStatement.executeUpdate();
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
