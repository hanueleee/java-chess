package chess.domain;

import chess.domain.piece.Empty;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceType;
import chess.domain.position.Position;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChessGame {

    private final Board board;
    private Color turn;
    private GameStatus status;
    
    public ChessGame() {
        status = GameStatus.START;
        board = Board.create();
        turn = Color.WHITE;
    }

    public ChessGame(Board board, Color turn, GameStatus status) {
        this.board = board;
        this.turn = turn;
        this.status = status;
    }

    public void start() {
        if (status != GameStatus.START) {
            throw new IllegalStateException("이미 게임이 시작되었습니다.");
        }
//        board.initialize();
        status = GameStatus.MOVE;
    }
    
    public Map<Position, Piece> move(final List<String> arguments) {
        if (status != GameStatus.MOVE) {
            throw new IllegalStateException("게임이 시작되지 않았습니다.");
        }
        Position source = Position.from(arguments.get(0));
        Position destination = Position.from(arguments.get(1));

        checkPieceCanMove(source, destination);
        checkCatchKing(destination);
        board.replace(source, destination);
        turn = turn.reverse();

        Map<Position, Piece> update = new HashMap<>();
        update.put(source, Empty.create(Color.NONE));
        update.put(destination, board.getPieceAtPosition(destination));

        return update;
    }

    private void checkPieceCanMove(final Position source, final Position destination) {
        board.validateSourcePiece(source, turn);
        Piece sourcePiece = board.getPieceAtPosition(source);

        sourcePiece.canMove(source, destination);
        board.checkSameColor(destination, turn);
        checkRoute(source, destination, sourcePiece);
        checkPawnMove(source, destination, sourcePiece);
    }

    private void checkCatchKing(Position destination) {
        if (board.getPieceAtPosition(destination).getType() == PieceType.KING) {
            status = GameStatus.CATCH;
        }
    }
    
    private void checkPawnMove(final Position source, final Position destination, final Piece sourcePiece) {
        if (sourcePiece.getType() == PieceType.PAWN) {
            board.checkRestrictionForPawn(source, destination, turn);
        }
    }
    
    private void checkRoute(final Position source, final Position destination, final Piece sourcePiece) {
        if (!(sourcePiece.getType() == PieceType.KNIGHT)) {
            board.checkBetweenRoute(source, destination);
        }
    }

    public Map<Color, Double> status() {
        if (status == GameStatus.START) {
            throw new IllegalStateException("게임이 시작되지 않았습니다.");
        }
        Map<Color, Double> status = new HashMap<>();
        status.put(Color.WHITE, board.calculatePoint(Color.WHITE));
        status.put(Color.BLACK, board.calculatePoint(Color.BLACK));
        return status;
    }

    public void end() {
        if (status != GameStatus.MOVE) {
            throw new IllegalStateException("게임이 시작되지 않았습니다.");
        }
        status = GameStatus.END;
    }

    public boolean isEnd() {
        return status == GameStatus.END;
    }

    public boolean isCatch() {
        return status == GameStatus.CATCH;
    }

    public Board getBoard() {
        return board;
    }

    public Color getTurn() {
        return turn;
    }

    public GameStatus getStatus() {
        return status;
    }
}

