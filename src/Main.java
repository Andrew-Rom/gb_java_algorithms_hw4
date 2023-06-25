import java.util.Random;

public class Main {
    public static void main(String[] args) {

        RedBlackTree binTree = new RedBlackTree();

        for (int i = 0; i < 20; i++) {
           binTree.add(new Random().nextInt(100));
        }

        binTree.print();

    }
}