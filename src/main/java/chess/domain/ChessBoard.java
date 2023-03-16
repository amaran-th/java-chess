package chess.domain;

import chess.domain.piece.info.Team;
import chess.domain.position.Position;
import java.math.BigInteger;
import java.util.List;

public class ChessBoard {

    private static final int NUMBER_OF_PLAYER = 2;
    private final List<Square> squares;
    private Turn turn;

    ChessBoard(List<Square> squares) {
        this.squares = squares;
        this.turn = new Turn();
    }

    public List<Square> getSquares() {
        return squares;
    }


    public boolean isEmptyAt(final Position position) {
        return findSquareByPosition(position)
                .isEmpty();
    }

    private Square findSquareByPosition(final Position position) {
        return squares.stream()
                .filter(square -> square.isSamePosition(position))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("칸이 초기화되지 않았습니다."));
    }


    public void move(final Position startPosition, final Position endPosition) {
        validateAllyPiece(startPosition);
        validateNotExistAllyAt(endPosition);
        validateNotBlocked(startPosition, endPosition);
        if (validateCanAttack(startPosition, endPosition)) {
            executeMove(startPosition, endPosition);
            return;
        }
        if (validateMove(startPosition, endPosition)) {
            executeMove(startPosition, endPosition);
            return;
        }
        throw new IllegalArgumentException("해당 위치는 기물의 이동 범위 밖입니다.");

    }

    private void executeMove(final Position startPosition, final Position endPosition) {
        findSquareByPosition(startPosition).moveTo(turn, findSquareByPosition(endPosition));
        turn = turn.next();
    }

    private void validateAllyPiece(final Position startPosition) {
        if (findSquareByPosition(startPosition).isSameTeam(turn.whoseTurn().enemy())) {
            throw new IllegalArgumentException("상대방의 기물은 이동시킬 수 없습니다.");
        }
    }

    private boolean validateMove(final Position startPosition, final Position endPosition) {
        return isEmptyAt(endPosition) &&
                findSquareByPosition(startPosition).canMove(startPosition, endPosition);
    }

    private boolean validateCanAttack(final Position startPosition, final Position endPosition) {
        final Square userSquare = findSquareByPosition(startPosition);
        final Square targetSquare = findSquareByPosition(endPosition);
        return !targetSquare.isSameTeam(turn.whoseTurn().enemy())
                || userSquare.canAttack(endPosition);
    }

    private void validateNotExistAllyAt(final Position endPosition) {
        final Team team = turn.whoseTurn();
        if (isMyPiece(team, endPosition)) {
            throw new IllegalArgumentException("도착지에 아군 기물이 있습니다.");
        }
    }

    private boolean isMyPiece(final Team team, final Position endPosition) {
        return findSquareByPosition(endPosition).isSameTeam(team);
    }

    private void validateNotBlocked(final Position startPosition, final Position endPosition) {
        int diffFile = endPosition.calculateFileDistance(startPosition);
        int diffRank = endPosition.calculateRankDistance(startPosition);
        BigInteger gcd = BigInteger.valueOf(diffFile).gcd(BigInteger.valueOf(diffRank));
        int fileDirection = diffFile / gcd.intValue();
        int rankDirection = diffRank / gcd.intValue();
        Position tempPosition = startPosition.move(rankDirection, fileDirection);
        while (!tempPosition.equals(endPosition)) {
            validateIsEmpty(tempPosition);
            tempPosition = tempPosition.move(rankDirection, fileDirection);
        }
    }

    private void validateIsEmpty(final Position tempPosition) {
        if (!isEmptyAt(tempPosition)) {
            throw new IllegalArgumentException("이동 경로에 다른 기물이 있습니다.");
        }
    }

    public boolean isKingDead() {
        return squares.stream()
                .filter(Square::isKing)
                .count() < NUMBER_OF_PLAYER;
    }
}
