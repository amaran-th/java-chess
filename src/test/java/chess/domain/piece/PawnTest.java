package chess.domain.piece;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.Score;
import chess.domain.Turn;
import chess.domain.piece.info.Team;
import chess.domain.position.File;
import chess.domain.position.Rank;
import chess.domain.position.Position;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.api.Nested;

class PawnTest {


    @Nested
    class 이동 {

        @ParameterizedTest
        @CsvSource(value = {"c:4", "c:3"}, delimiter = ':')
        void WHITE_팀일때_움직일_수_있는_위치라면_true반환(String rank, String file) {
            //given
            Position source = Position.of(File.C, Rank.TWO);
            Position destination = Position.of(File.from(rank), Rank.from(file));
            Pawn pawn = new Pawn(Team.WHITE);

            //when
            boolean actual = pawn.canMove(source, destination);

            //then
            assertThat(actual).isTrue();
        }

        @ParameterizedTest
        @CsvSource(value = {"c:6", "c:5"}, delimiter = ':')
        void BLACK_팀일때_움직일_수_있는_위치라면_true반환(String rank, String file) {
            //given
            Position source = Position.of(File.C, Rank.SEVEN);
            Position destination = Position.of(File.from(rank), Rank.from(file));
            Pawn pawn = new Pawn(Team.BLACK);

            //when
            boolean actual = pawn.canMove(source, destination);

            //then
            assertThat(actual).isTrue();
        }

        @ParameterizedTest
        @CsvSource(value = {"c:2", "c:1", "d:3"}, delimiter = ':')
        void 기물이_움직일_수_없는_위치라면_false반환(String rank, String file) {
            //given
            Position source = Position.of(File.C, Rank.TWO);
            Position destination = Position.of(File.from(rank), Rank.from(file));
            Pawn pawn = new Pawn(Team.WHITE);

            //when
            boolean actual = pawn.canMove(source, destination);

            //then
            assertThat(actual).isFalse();
        }

        @Test
        void 움직인_기록이_있을_때_2칸을_움직이려하면_false반환() {
            //given
            Position source = Position.of(File.C, Rank.TWO);
            Position destination = Position.of(File.C, Rank.FOUR);
            Pawn pawn = new Pawn(Team.WHITE);
            pawn.addTrace(new Turn(), source);

            //when
            boolean actual = pawn.canMove(source, destination);

            //then
            assertThat(actual).isFalse();
        }
    }


    @Nested
    class 공격 {

        @ParameterizedTest
        @CsvSource(value = {"b:3", "d:3"}, delimiter = ':')
        void 팀이_WHITE일때_공격할_수_있는_위치라면_ture반환(String rank, String file) {
            //given
            Position source = Position.of(File.C, Rank.TWO);
            Position destination = Position.of(File.from(rank), Rank.from(file));
            Pawn pawn = new Pawn(Team.WHITE);

            //when
            boolean actual = pawn.canAttack(source, destination);

            //then
            assertThat(actual).isTrue();
        }

        @ParameterizedTest
        @CsvSource(value = {"b:1", "d:1"}, delimiter = ':')
        void 팀이_BLACK일때_공격할_수_있는_위치라면_ture반환(String rank, String file) {
            //given
            Position source = Position.of(File.C, Rank.TWO);
            Position destination = Position.of(File.from(rank), Rank.from(file));
            Pawn pawn = new Pawn(Team.BLACK);

            //when
            boolean actual = pawn.canAttack(source, destination);

            //then
            assertThat(actual).isTrue();
        }

        @ParameterizedTest
        @CsvSource(value = {"c:2", "c:3", "c:4", "c:1"}, delimiter = ':')
        void 공격할_수_없는_위치라면_false반환(String rank, String file) {
            //given
            Position source = Position.of(File.C, Rank.TWO);
            Position destination = Position.of(File.from(rank), Rank.from(file));
            Pawn pawn = new Pawn(Team.WHITE);

            //when
            boolean actual = pawn.canAttack(source, destination);

            //then
            assertThat(actual).isFalse();
        }
    }

    @Test
    void 기물의_점수_계산() {
        //given
        Pawn pawn = new Pawn(Team.WHITE);

        //when
        Score actual = pawn.findScore();

        //then
        assertThat(actual).isEqualTo(new Score(1.0));
    }

}
