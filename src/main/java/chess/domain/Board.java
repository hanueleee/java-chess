package chess.domain;

import chess.domain.piece.Empty;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceType;
import chess.domain.position.Direction;
import chess.domain.position.File;
import chess.domain.position.Position;
import chess.domain.position.Rank;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Board {

    private static final double MULTIPLE_PAWN_POINT = 0.5;
    private static final double MULTIPLE_PAWN_COUNT = 2;

    private final Map<Position, Piece> board;

    public Board(final Map<Position, Piece> board) {
        this.board = board;
    }

    public static Board create() {
        Map<Position, Piece> board = new HashMap<>();
        board.putAll(BoardGenerator.generate());
        return new Board(board);
    }

    public void validateSourcePiece(final Position source, final Color color) {
        if (isEmpty(source)) {
            throw new IllegalArgumentException("피스가 존재하지 않습니다.");
        }
        if (!isSameColor(source, color)) {
            throw new IllegalArgumentException("상대편 피스입니다.");
        }
    }

    public void checkBetweenRoute(final Position source, final Position destination) {
        Direction direction = source.calculateDirection(destination);
        Position move = source.addDirection(direction);
        while (!destination.equals(move)) {
            checkOtherPieceInRoute(move);
            move = move.addDirection(direction);
        }
    }

    private void checkOtherPieceInRoute(final Position move) {
        if (!isEmpty(move)) {
            throw new IllegalArgumentException("경로에 다른 피스가 존재합니다.");
        }
    }

    public void checkRestrictionForPawn(final Position source, final Position destination, final Color color) {
        Direction direction = source.calculateDirection(destination);
        if (direction == Direction.N || direction == Direction.S) {
            checkOtherPieceInRoute(destination);
        }
        if (Direction.isDiagonal(direction)) {
            checkDiagonalPiece(destination);
            checkSameColor(destination, color);
        }
    }

    private void checkDiagonalPiece(final Position destination) {
        if (isEmpty(destination)) {
            throw new IllegalArgumentException("비어있기 때문에 대각선으로 이동할 수 없습니다.");
        }
    }

    public void checkSameColor(final Position destination, Color color) {
        if (isSameColor(destination, color)) {
            throw new IllegalArgumentException("목적지에 같은 색깔의 피스가 있습니다.");
        }
    }

    public void replace(final Position source, final Position destination) {
        board.put(destination, board.get(source));
        board.put(source, Empty.create(Color.NONE));
    }

    public double calculatePoint(Color color) {
        return pieceScore(color) - multiplePawnScore(color);
    }

    private double pieceScore(final Color color) {
        return board.values().stream()
                .filter(piece -> piece.isSameColor(color))
                .mapToDouble(piece -> piece.getType().getPoint())
                .sum();
    }

    private double multiplePawnScore(final Color color) {
        final Map<File, Long> pawnCount = Arrays.stream(Rank.values())
                .flatMap(file -> Arrays.stream(File.values()).map(rank -> Position.from(rank, file)))
                .filter(position -> board.get(position).isSameColor(color))
                .filter(position -> board.get(position).isSameType(PieceType.PAWN))
                .collect(Collectors.groupingBy(Position::getFile, Collectors.counting()));

        return pawnCount.values().stream()
                .filter(value -> value >= MULTIPLE_PAWN_COUNT)
                .mapToDouble(value -> value * MULTIPLE_PAWN_POINT)
                .sum();
    }

    private boolean isEmpty(final Position position) {
        return board.get(position).isEmpty();
    }

    private boolean isSameColor(final Position position, final Color color) {
        return board.get(position).isSameColor(color);
    }

    public List<Piece> findAllByRank(Rank rank) {
        List<Piece> result = new ArrayList<>();
        for (File file : File.values()) {
            Position position = Position.from(file, rank);
            result.add(board.get(position));
        }
        return result;
    }

    public Map<Position, Piece> getPositionAndPiece() {
        return Collections.unmodifiableMap(board);
    }

    public Piece getPieceAtPosition(final Position position) {
        return board.get(position);
    }
}
