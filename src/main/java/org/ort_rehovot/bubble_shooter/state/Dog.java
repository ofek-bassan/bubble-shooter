package org.ort_rehovot.bubble_shooter.state;

public class Dog implements AbstractState{
    @Override
    public void say() {
        System.out.println("Bark");
    }

    @Override
    public AbstractState transition(char x) {
        if (x == 'c') {
            return new Cat();
        }
        return null;
    }
}
