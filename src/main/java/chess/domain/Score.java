package chess.domain;

import java.util.Objects;

public class Score {

    public final static Score ZERO = new Score(0.0);
    
    private final Double value;

    public Score(Double value) {
        this.value = value;
    }

    public Score add(Score score) {
        return new Score(value + score.value);
    }

    public Score multiply(Long count) {
        return new Score(value * count);
    }

    public boolean isMoreThan(Score score){
        return value>score.value;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Score score = (Score) o;
        return Objects.equals(value, score.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    public Double getValue() {
        return value;
    }
}
