package it.polimi.ingsw.model.helpers;

public class Quartet<Z,F,S,T> {
    private final Z first;
    private final F second;
    private final S third;
    private final T fourth;

    public Quartet(Z first, F second, S third, T fourth){
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
    }
    public Z getFirst() {
        return first;
    }
    public F getSecond() {
        return second;
    }
    public S getThird() {
        return third;
    }
    public T getFourth() {
        return fourth;
    }
}
