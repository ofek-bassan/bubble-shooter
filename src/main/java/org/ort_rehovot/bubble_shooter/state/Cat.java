package org.ort_rehovot.bubble_shooter.state;

public class Cat implements AbstractState{
    @Override
    public void say() {
        System.out.println("Meow");
    }

    @Override
    public AbstractState transition(char x) {
        if (x == 'd') {
            return new Dog();
        }
        if (x == 'n') {
            return new NullState();
        }
        return null;
    }
}
