import java.time.Duration;
import java.time.Instant;

public class BTree {
    int t;
    BtreeNode root;
    public BTree(int t) {
        this.t = t;
    }

    static class BtreeNode{
        int t;
        int n;
        BtreeNode parent;
        int[] keys;
        BtreeNode[] childes;
        boolean leaf;

        public BtreeNode(int t, boolean leaf) {
            this.t = t;
            this.leaf = leaf;
            keys = new int[(2 * t) - 1];
            childes = new BtreeNode[2 * t];
        }
        public void traverse(){
            int i;
            for (i = 0; i < n; i++) {
                if (!leaf ) {
                    childes[i].traverse();
                }
                System.out.print(keys[i] + " ");
            }
            if (!leaf) {
                childes[i].traverse();
            }
        }
        public BtreeNode findKey(int key){
            int i = 0;
            while (i < n && key > keys[i]) {
                i++;
            }
            if (keys[i] == key && !(key == 0 && i == n)) {
                return this;
            }
            if (leaf) {
                return null;
            }
            return childes[i].findKey(key);
        }
        public int size(){
            int size = n;
            for (int i = 0; i <= n && !leaf; i++) {
                size += childes[i].size();
            }
            return size;
        }
    }

    public void insert(int key){
        if (root == null) {
            root = new BtreeNode(t, true);
            root.keys[0] = key;
            root.n = 1;
            return;
        }
        BtreeNode temp = root;
        while (temp != null){
            if (temp.n == (2 * t) - 1){
                splitNode(temp);
                insert(key);
                return;
            }

            int i = 0;
            while (i < temp.n && key > temp.keys[i]){
                i ++;
            }
            if (key == temp.keys[i]){
                return;
            }
            if (temp.leaf){
                doPushKey(i, key, temp.keys, temp.n);
                temp.n ++;
                break;
            }else {
                temp = temp.childes[i];
            }
        }
    }
    private void splitNode(BtreeNode node){
        int mid = (node.n - 1) / 2;
        BtreeNode left = new BtreeNode(node.t, node.leaf);
        BtreeNode right = new BtreeNode(node.t, node.leaf);
        left.parent = node.parent;
        right.parent = node.parent;
        int i;
        for (i = 0; i < mid; i++) {
            left.keys[i] = node.keys[i];
            left.n ++;
            if (!left.leaf){
                left.childes[i] = node.childes[i];
                left.childes[i].parent = left;
            }
        }
        if (!left.leaf){
            left.childes[i] = node.childes[i];
            left.childes[i].parent = left;
        }

        for (i = 0; i < node.n - mid - 1; i++) {
            right.keys[i] = node.keys[mid + i + 1];
            right.n ++;
            if (!right.leaf){
                right.childes[i] = node.childes[mid + i + 1];
                right.childes[i].parent = right;
            }
        }
        if (!right.leaf){
            right.childes[i] = node.childes[mid + i + 1];
            right.childes[i].parent = right;
        }

        if (node == root){
            root = new BtreeNode(t, false);
            root.keys[0] = node.keys[mid];
            root.n = 1;
            root.childes[0] = left;
            root.childes[1] = right;
            left.parent = root;
            right.parent = root;
        }else {
            pushKey(node.keys[mid], node.parent, left, right);
        }
    }
    private void pushKey(int key, BtreeNode parent ,BtreeNode left, BtreeNode right){
        int i = 0;
        while (i < parent.n && key > parent.keys[i]){
            i ++;
        }
        parent.n ++;
        doPushKey(i, key, parent.keys, parent.n);
        parent.leaf = false;
        doPushNodes(i, left, right, parent.childes, parent.n);
    }
    private void doPushKey(int i, int key, int[] keys, int n){
        int[] temp = new int[n];
        for (int j = 0; j < n; j++) {
            temp[j] = keys[j];
        }
        keys[i] = key;
        for (int j = i + 1; j <= n && j != keys.length; j++) {
            keys[j] = temp[j - 1];
        }
    }
    private void doPushNodes(int i, BtreeNode left, BtreeNode right, BtreeNode[] childes, int n){
        BtreeNode[] temp = new BtreeNode[n + 1];
        for (int j = 0; j < n + 1; j++) {
            temp[j] = childes[j];
        }
        childes[i] = left;
        childes[i + 1] = right;
        for (int j = i + 2; j <= n + 1 && j != childes.length; j++) {
            childes[j] = temp[j - 1];
        }
    }
    private void pushNode(int i, BtreeNode node, BtreeNode[] childes, int n){
        BtreeNode[] temp = new BtreeNode[n + 1];
        for (int j = 0; j <= n; j++) {
            temp[j] = childes[j];
        }
        childes[i] = node;
        for (int j = i + 1; j <= n + 1 && j != childes.length; j++) {
            childes[j] = temp[j - 1];
        }
    }

    public void remove(int key){
        BtreeNode node = findKey(key);
        if (node != null) {
            doRemove(node, key);
        }
    }
    private void doRemove(BtreeNode node, int key){
        int i = 0;
        while (i < node.n && key > node.keys[i]){
            i++;
        }
        if (node.leaf){
            node.keys = removeKey(i, node.keys, node.n);
            node.n--;
            if (node.n < t - 1 && node != root){
                rebalancing(node);
            }
        }else {
            BtreeNode maxLeft = findMax(node.childes[i]);
            int temp = maxLeft.keys[maxLeft.n - 1];
            maxLeft.keys = removeKey(maxLeft.n - 1, maxLeft.keys, maxLeft.n);
            maxLeft.n--;
            node.keys[i] = temp;
            if (maxLeft.n < t - 1){
                rebalancing(maxLeft);
            }
        }
    }

    double balanceTime;


    private void rebalancing(BtreeNode node){
        System.out.println("method call");
        Instant instant1 = Instant.now();
        BtreeNode leftBr = null, rightBr = null;
        int temp;

        int i = 0;
        while (node.parent.childes[i] != node){
            i++;
        }

        if (i != 0){
            leftBr = node.parent.childes[i - 1];
        }
        if (i != node.parent.n){
            rightBr = node.parent.childes[i + 1];
        }

        if (rightBr != null && rightBr.n != t - 1){
            temp = node.parent.keys[i];
            doPushKey(node.n, temp, node.keys, node.n);
            node.n++;
            temp = rightBr.keys[0];
            node.parent.keys[i] = temp;

            if (node.leaf) {
                doRemove(rightBr, temp);
            }else{
                BtreeNode tempNode = rightBr.childes[0];
                pushNode(node.n, tempNode, node.childes, node.n);
                tempNode.parent = node;
                rightBr.childes = removeNode(0, rightBr.childes, rightBr.n);
                rightBr.keys = removeKey(0, rightBr.keys, rightBr.n);
                rightBr.n--;
            }
        }else if (leftBr != null && leftBr.n != t - 1){
            temp = node.parent.keys[i - 1];
            doPushKey(0, temp, node.keys, node.n);
            node.n++;
            temp = leftBr.keys[leftBr.n - 1];
            node.parent.keys[i - 1] = temp;

            if (node.leaf) {
                doRemove(leftBr, temp);
            }else{
                BtreeNode tempNode = leftBr.childes[leftBr.n];
                pushNode(0, tempNode, node.childes, node.n);
                tempNode.parent = node;
                leftBr.childes = removeNode(leftBr.n, leftBr.childes, leftBr.n);
                leftBr.keys = removeKey(leftBr.n - 1, leftBr.keys, leftBr.n);
                leftBr.n--;
            }

        }else{
            BtreeNode newRoot = null;
            if (rightBr != null){
                temp = node.parent.keys[i];
                doPushKey(node.n, temp, node.keys, node.n);
                node.n++;
                merge(node, rightBr);
                newRoot = node;
                node.parent.keys = removeKey(i, node.parent.keys, node.parent.n);
                node.parent.childes = removeNode(i + 1, node.parent.childes, node.parent.n);
                node.parent.n--;
            }else if (leftBr != null){
                temp = node.parent.keys[i - 1];
                doPushKey(leftBr.n, temp, leftBr.keys, leftBr.n);
                leftBr.n++;
                merge(leftBr, node);
                newRoot = leftBr;
                node.parent.keys = removeKey(i - 1, node.parent.keys, node.parent.n);
                node.parent.childes = removeNode(i, node.parent.childes, node.parent.n);
                node.parent.n--;
            }
            if (node.parent == root && node.parent.n == 0){
                if (newRoot != null) {
                    newRoot.parent = null;
                }
                root = newRoot;
            }else if (node.parent != root && node.parent.n < t - 1){
                rebalancing(node.parent);
            }
        }
        Instant instant2 = Instant.now();
        balanceTime += Duration.between(instant1,instant2).toMillis();

    }
    private int[] removeKey(int i, int[] keys, int n){
        int[] temp = new int[(2 * t) - 1];
        int j = 0, k = 0;
        while (k < n){
            if (k != i){
                temp[j] = keys[k];
                j++;
            }
            k++;
        }
        return temp;
    }
    private BtreeNode[] removeNode(int i, BtreeNode[] childes, int n){
        BtreeNode[] temp = new BtreeNode[2 * t];
        int j = 0, k = 0;
        while (k <= n){
            if (k != i){
                temp[j] = childes[k];
                j++;
            }
            k++;
        }
        return temp;
    }
    private void merge(BtreeNode left, BtreeNode right){
        int n = left.n;
        left.n += right.n;
        for (int i = 0; i <= right.n; i++) {
            if (i != right.n){
                left.keys[n + i] = right.keys[i];
            }
            if (!left.leaf){
                BtreeNode temp = right.childes[i];
                left.childes[n + i] = temp;
                temp.parent = left;
            }
        }
    }

    public BtreeNode findMin(BtreeNode node){
        if (node.leaf){
            return node;
        }
        return findMin(node.childes[0]);
    }
    public BtreeNode findMax(BtreeNode node){
        if (node.leaf){
            return node;
        }
        return findMax(node.childes[node.n]);
    }

    public void traverse(){
        if (root != null){
            root.traverse();
        }
        System.out.println();
    }
    public BtreeNode findKey(int key){
        return (root == null) ? null : root.findKey(key);
    }
    public int size(){
        return (root == null) ? 0 : root.size();
    }
}
