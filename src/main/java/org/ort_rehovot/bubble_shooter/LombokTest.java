package org.ort_rehovot.bubble_shooter;


import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class LombokTest {
    private final int x;
    private int y;


    public static void main(String[] args) {
        val x = new LombokTest(1, 2);

        extracted(x);


    }

    private static void extracted(LombokTest x) {
        int x1 = x.getX();
        System.out.println(x1);
        System.out.println(x.getY());
        x.setY(10);

        System.out.println(x.toString());
    }
}
