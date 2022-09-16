package org.ort_rehovot.bubble_shooter.state;

public class NullState implements AbstractState{
    @Override
    public void say() {
        System.out.println("null");
    }

    @Override
    public AbstractState transition(char x) {
        if (x == 'd') {
            return new Dog();
        }
        return null;
    }
}
