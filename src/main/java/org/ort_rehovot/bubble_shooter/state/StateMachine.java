package org.ort_rehovot.bubble_shooter.state;

import java.util.Scanner;

public class StateMachine {
    private AbstractState state;

    public StateMachine() {
        state = new NullState();
    }

    public void say() {
        state.say();
    }

    public void handleEvent(char x) {
        AbstractState transition = state.transition(x);
        if (transition != null) {
            state = transition;
        }
    }

    public static void main(String[] args) {
        StateMachine fsm = new StateMachine();
        Scanner input = new Scanner(System.in);
        while (input.hasNext()) {
            String s = input.nextLine();
            char ch = s.charAt(0);
            if (ch == '!') {
                fsm.say();
            } else if (ch == '.') {
                break;
            } else {
                fsm.handleEvent(ch);
            }
        }

    }
}
