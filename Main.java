import java.util.Random;

public class Main {
    public static void main(String[] args) {
        BTree bTree1 = new BTree(2);
        BTree bTree2 = new BTree(2);

        System.out.println("BTree insertion: r/e");

        double startTime = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            bTree1.insert((int) (Math.random() * 1000000 - 500000));
        }
        System.out.println( (System.nanoTime() - startTime) / 1000000 + "ms");

        System.out.println("BTree insertion: c/e");

        startTime = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            bTree2.insert(i * 100 - 50);
        }
        System.out.println( (System.nanoTime() - startTime) / 1000000 + "ms");

        System.out.println(bTree1.balanceTime/100000 + "ms");
        System.out.println(bTree2.balanceTime/100000 + "ms");

        bTree1.insert(12345);
        bTree2.insert(12345);

        System.out.println("\nBTree search(r/e)");

        startTime = System.nanoTime();
        bTree1.findKey(12345);
        System.out.println((System.nanoTime() - startTime) / 1000000 + "ms");

        System.out.println("\n BTree search(c/e)");

        startTime = System.nanoTime();
        bTree2.findKey(12345);
        System.out.println((System.nanoTime() - startTime) / 1000000 + "ms");

        System.out.println("\nBTree deletion(r/e)");

        startTime = System.nanoTime();
        bTree1.remove(12345);
        System.out.println((System.nanoTime() - startTime) / 1000000 + "ms");

        System.out.println("\nBTree deletion(c/e)");

        startTime = System.nanoTime();
        bTree2.remove(12345);
        System.out.println((System.nanoTime() - startTime) / 1000000 + "ms");





    }
    static void printInfo(BTree test){
        System.out.println("Size: " + test.size());
        test.traverse();
        System.out.println("Root:");
        System.out.println(test.root.leaf);
        print(test.root.keys);
        System.out.println("1 Childes:");
        print(test.root.childes);
        System.out.println();
        if (test.root.childes[0] != null) {
            for (int i = 0; i < test.root.n + 1; i++) {
                System.out.println(i + 1 + " :");
                System.out.println(test.root.childes[i].leaf);
                print(test.root.childes[i].keys);
                print(test.root.childes[i].childes);
                System.out.println("Parent");
                print(test.root.childes[i].parent.keys);
                System.out.println();
            }
        }
        System.out.println();
        System.out.println("2 Childes");
        if (test.root.childes[0] != null) {
            for (int i = 0; i < test.root.n + 1; i++) {
                if (test.root.childes[i].childes[0] != null) {
                    for (int j = 0; j < test.root.childes[i].n + 1; j++) {
                        System.out.println((i + 1) + " " + (j + 1) + " :");
                        System.out.println(test.root.childes[i].childes[j].leaf);
                        print(test.root.childes[i].childes[j].keys);
                        print(test.root.childes[i].childes[j].childes);
                        System.out.println("Parent");
                        print(test.root.childes[i].childes[j].parent.keys);
                        System.out.println();
                    }
                }
            }
        }
    }
    static void print(BTree.BtreeNode[] array){
        for (BTree.BtreeNode el: array) {
            System.out.print(el + " ");
        }
        System.out.println();
    }
    static void print(int[] array){
        for (int el: array) {
            System.out.print(el + " ");
        }
        System.out.println();
    }
}
