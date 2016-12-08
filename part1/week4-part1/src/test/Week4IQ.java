package test;

public class Week4IQ {
    public static void main(String[] args) {
        Week4IQ w = new Week4IQ();
        w.subtask1();
    }

    private void subtask1() {
        double a = 0.0;
        double b = -0.0;
        Double x = new Double(a);
        Double y = new Double(b);
        if (a == b) {
            if (!x.equals(y)) {
                System.out.println("Yay!");
            } else {
                System.out.println("Nope 1");
            }
        } else {
            System.out.println("Nope 2");
        }
    }

    private void subtask2() {
        double a = Double.NaN;
        double b = Double.NaN;
        Double x = new Double(a);
        Double y = new Double(b);
        if (a != b) {
            if (x.equals(y)) {
                System.out.println("Yay!");
            } else {
                System.out.println("Nope 1");
            }
        }
        else {
            System.out.println("Nope 2");
        }
    }
}
