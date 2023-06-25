import java.util.ArrayList;
import java.util.List;

public class RedBlackTree {

    private Node root;

    public boolean contain(int value) {
        Node node = findNode(value, root);
        return node != null;
    }

    private Node findNode(int value, Node midNode) {
        if (midNode.value == value) {
            return midNode;
        } else if (midNode.value > value) {
            if (midNode.leftChild != null) {
                return findNode(value, midNode.leftChild);
            } else {
                return null;
            }
        } else {
            if (midNode.rightChild != null) {
                return findNode(value, midNode.rightChild);
            } else {
                return null;
            }
        }
    }

    public void add(int value) {
        if (root == null) {
            root = new Node(value);
            root.color = Color.BLACK;
        } else {
            addNode(value, root);
        }
    }

    private Node addNode(int value, Node node) {
        if (contain(value)) {
            return null;
        } else if (node.value > value) {
            if (node.leftChild == null) {
                node.leftChild = new Node(value);
                return node.leftChild;
            } else {
                Node addedNote = addNode(value, node.leftChild);
                node.leftChild = rebalance(node.leftChild);
                return addedNote;
            }
        } else {
            if (node.rightChild == null) {
                node.rightChild = new Node(value);
                return node.rightChild;
            } else {
                Node addedNote = addNode(value, node.rightChild);
                node.rightChild = rebalance(node.rightChild);
                return addedNote;
            }
        }
    }

    private Node rebalance(Node node) {
        Node result = node;
        boolean needRebalance;
        do {
            needRebalance = false;
            if (result.rightChild != null
                    && result.rightChild.color == Color.RED
                    && (result.leftChild == null || result.leftChild.color == Color.BLACK)) {
                needRebalance = true;
                result = rightSwap(result);
            }
            if (result.leftChild != null
                    && result.leftChild.color == Color.RED
                    && result.leftChild.leftChild != null && result.leftChild.leftChild.color == Color.RED) {
                needRebalance = true;
                result = leftSwap(result);
            }
            if (result.leftChild != null
                    && result.leftChild.color == Color.RED
                    && result.rightChild != null && result.rightChild.color == Color.RED) {
                needRebalance = true;
                colorSwap(result);
            }
        } while (needRebalance);
        return result;
    }

    private void colorSwap(Node node) {
        node.rightChild.color = Color.BLACK;
        node.leftChild.color = Color.BLACK;
        node.color = Color.RED;
    }

    private Node leftSwap(Node node) {
        Node left = node.leftChild;
        Node between = left.rightChild;
        left.rightChild = node;
        node.leftChild = between;
        left.color = node.color;
        node.color = Color.RED;
        return left;
    }

    private Node rightSwap(Node node) {
        Node right = node.rightChild;
        Node between = right.leftChild;
        right.leftChild = node;
        node.rightChild = between;
        right.color = node.color;
        node.color = Color.RED;
        return right;
    }

    private class Node {
        private int value;
        private Node leftChild;
        private Node rightChild;
        private Color color;

        public Node(int value) {
            this.value = value;
            leftChild = null;
            rightChild = null;
            color = Color.RED;
        }

        @Override
        public String toString() {
            return "[" + value + "-" + color + "]";
        }
    }


    // вывод дерева с урока

    private class PrintNode {
        Node node;
        String str;
        int depth;

        public PrintNode() {
            node = null;
            str = " ";
            depth = 0;
        }

        public PrintNode(Node node) {
            depth = 0;
            this.node = node;
            this.str = node.toString();
        }
    }

    public void print() {
        int maxDepth = maxDepth() + 3;
        int nodeCount = nodeCount(root, 0);
        int width = 50;//maxDepth * 4 + 2;
        int height = nodeCount * 5;
        List<List<PrintNode>> list = new ArrayList<List<PrintNode>>();
        for (int i = 0; i < height; i++) /*Создание ячеек массива*/ {
            ArrayList<PrintNode> row = new ArrayList<>();
            for (int j = 0; j < width; j++) {
                row.add(new PrintNode());
            }
            list.add(row);
        }

        list.get(height / 2).set(0, new PrintNode(root));
        list.get(height / 2).get(0).depth = 0;

        for (int j = 0; j < width; j++)  /*Принцип заполнения*/ {
            for (int i = 0; i < height; i++) {
                PrintNode currentNode = list.get(i).get(j);
                if (currentNode.node != null) {
                    currentNode.str = currentNode.node.toString();
                    if (currentNode.node.leftChild != null) {
                        int in = i + (maxDepth / (int) Math.pow(2, currentNode.depth));
                        int jn = j + 3;
                        printLines(list, i, j, in, jn);
                        list.get(in).get(jn).node = currentNode.node.leftChild;
                        list.get(in).get(jn).depth = list.get(i).get(j).depth + 1;

                    }
                    if (currentNode.node.rightChild != null) {
                        int in = i - (maxDepth / (int) Math.pow(2, currentNode.depth));
                        int jn = j + 3;
                        printLines(list, i, j, in, jn);
                        list.get(in).get(jn).node = currentNode.node.rightChild;
                        list.get(in).get(jn).depth = list.get(i).get(j).depth + 1;
                    }

                }
            }
        }
        for (int i = 0; i < height; i++) /*Чистка пустых строк*/ {
            boolean flag = true;
            for (int j = 0; j < width; j++) {
                if (list.get(i).get(j).str != " ") {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                list.remove(i);
                i--;
                height--;
            }
        }

        for (var row : list) {
            for (var item : row) {
                System.out.print(item.str + " ");
            }
            System.out.println();
        }
    }

    private void printLines(List<List<PrintNode>> list, int i, int j, int i2, int j2) {
        if (i2 > i) // Идём вниз
        {
            while (i < i2) {
                i++;
                list.get(i).get(j).str = "|";
            }
            list.get(i).get(j).str = "\\";
            while (j < j2) {
                j++;
                list.get(i).get(j).str = "-";
            }
        } else {
            while (i > i2) {
                i--;
                list.get(i).get(j).str = "|";
            }
            list.get(i).get(j).str = "/";
            while (j < j2) {
                j++;
                list.get(i).get(j).str = "-";
            }
        }
    }

    public int maxDepth() {
        return maxDepth2(0, root);
    }

    private int maxDepth2(int depth, Node node) {
        depth++;
        int left = depth;
        int right = depth;
        if (node.leftChild != null)
            left = maxDepth2(depth, node.leftChild);
        if (node.rightChild != null)
            right = maxDepth2(depth, node.rightChild);
        return left > right ? left : right;
    }

    private int nodeCount(Node node, int count) {
        if (node != null) {
            count++;
            return count + nodeCount(node.leftChild, 0) + nodeCount(node.rightChild, 0);
        }
        return count;
    }

}
