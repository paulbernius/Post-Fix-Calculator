import java.util.Stack;

class Node {
    String data;
    Node left, right;

    Node(String data) {
        this.data = data;
        this.left = this.right = null;
    }

    Node(String data, Node left, Node right) {
        this.data = data;
        this.left = left;
        this.right = right;
    }
}

class main {
    // Return true if string is operator
    public static boolean isOperator(String c) {
        return (c.equals("+") || c.equals("-") || c.equals("*") || c.equals("/") || c.equals("^"));
    }

    // Print expression in postorder (postfix)
    // Pre: Tree is not empty
    public static void postorder(Node root) {

        if (root == null) {
            return;
        }

        postorder(root.left);
        postorder(root.right);
        System.out.print(root.data);
    }

    // Print expression in inorder (infix)
    // Pre: Tree is not empty
    public static void inorder(Node root) {

        if (root == null) {
            return;
        }

        if (isOperator(root.data)) {
            System.out.print("(");
        }

        inorder(root.left);
        System.out.print(root.data);
        inorder(root.right);

        if (isOperator(root.data)) {
            System.out.print(")");
        }
    }

    // Remove spaces from string
    public static String removeWhiteSpaces(String postfix) {
        return postfix.replaceAll(" ", "");
    }

    // Builds a tree from string (infix expression)
    static Node build(String s) {

        if (s.indexOf('(') == -1) {
            s = '(' + s;
        }
        if (s.indexOf(')') == -1) {
            s = s + ')';
        }

        s = removeWhiteSpaces(s);
        Stack<Node> stackNode = new Stack<>();
        Stack<String> stackString = new Stack<>();
        Node t, t1, t2;

        // Order of operations
        int []p = new int[123];
        p['+'] = p['-'] = 1;
        p['/'] = p['*'] = 2;
        p['^'] = 3;
        p[')'] = 0;

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                stackString.add("" + s.charAt(i));
            // If char is a number, get all numbers following
            } else if (Character.isDigit(s.charAt(i))) {
                String numString = "";
                while (Character.isDigit(s.charAt(i))) {
                    numString += s.charAt(i);
                    i++;
                }
                i--;
                // If length of string is 1, create node with char at current index
                if (numString.length() == 1) {
                    t = new Node("" + s.charAt(i));
                // If length of string is longer than 1, create node with string
                } else {
                    t = new Node(numString);
                }
                stackNode.add(t);
            } else if (p[s.charAt(i)] > 0) {
                String stringTemp = stackString.peek();
                char charTemp = stringTemp.charAt(0);
                while (!stackString.isEmpty() && charTemp != '(' && ((s.charAt(i) != '^' && p[charTemp] >= p[s.charAt(i)]) || (s.charAt(i) == '^' && p[charTemp] > p[s.charAt(i)]))) {
                    t = new Node("" + stackString.peek());
                    stackString.pop();
                    t1 = stackNode.peek();
                    stackNode.pop();
                    t2 = stackNode.peek();
                    stackNode.pop();
                    t.left = t2;
                    t.right = t1;
                    stackNode.add(t);
                    stringTemp = stackString.peek();
                    charTemp = stringTemp.charAt(0);
                }
                stackString.push("" + s.charAt(i));

            } else if (s.charAt(i) == ')') {
                while (!stackString.isEmpty() && !stackString.peek().equals("(")) {
                    t = new Node("" + stackString.peek());
                    stackString.pop();
                    t1 = stackNode.peek();
                    stackNode.pop();
                    t2 = stackNode.peek();
                    stackNode.pop();
                    t.left = t2;
                    t.right = t1;
                    stackNode.add(t);
                }
                stackString.pop();
            }
        }
        t = stackNode.peek();
        return t;
    }

    // Checks if node is leaf node
    public static boolean isLeaf(Node node) {
        return node.left == null && node.right == null;
    }

    // Returns result of an expression
    public static double process(String op, double x, double y) {

        if (op.equals("+")) { return x + y; }
        if (op.equals("-")) { return x - y; }
        if (op.equals("*")) { return x * y; }
        if (op.equals("/")) { return x / y; }

        return 0;
    }

    // Evaluates the expression
    // Pre: Tree is not empty
    public static double evaluate(Node root) {

        if (root == null) {
            return 0;
        }

        // If leaf get value
        if (isLeaf(root)) {
            return Double.valueOf(root.data);
        }

        double x = evaluate(root.left);
        double y = evaluate(root.right);
        return process(root.data, x, y);

    }

    public static void main(String[] args) {

        String input1 = "100";
        String input2 = "(100       +200 )";
        String input3 = "(100*   (200-300))";
        String input4 = "( (100*(200+300))* ( (400-200)/ (100+100) ))";

        System.out.println("(a) Arthimetic expression in array or string (without unnecessary spaces) \n------------------------------------------------------------------------");

        System.out.println("Input: " + input1);
        System.out.println("Output: " + removeWhiteSpaces(input1));

        System.out.println("\nInput: " + input2);
        System.out.println("Output: " + removeWhiteSpaces(input2));

        System.out.println("\nInput: " + input3);
        System.out.println("Output: " + removeWhiteSpaces(input3));

        System.out.println("\nInput: " + input4);
        System.out.println("Output: " + removeWhiteSpaces(input4));

        System.out.println("------------------------------------------------------------------------\n\n");


        System.out.println("(b) Expression-to-tree algorithm \n------------------------------------------------------------------------");

        System.out.println("Input: " + input1);
        Node bTestCase1 = build(input1);
        System.out.print("Output (POST-ORDER): ");
        postorder(bTestCase1);
        System.out.print("\nOutput (IN-ORDER): ");
        inorder(bTestCase1);

        System.out.println("\n\nInput: " + input2);
        Node bTestCase2 = build(input2);
        System.out.print("Output (POST-ORDER): ");
        postorder(bTestCase2);
        System.out.print("\nOutput (IN-ORDER): ");
        inorder(bTestCase2);

        System.out.println("\n\nInput: " + input3);
        Node bTestCase3 = build(input3);
        System.out.print("Output (POST-ORDER): ");
        postorder(bTestCase3);
        System.out.print("\nOutput (IN-ORDER): ");
        inorder(bTestCase3);

        System.out.println("\n\nInput: " + input4);
        Node bTestCase4 = build(input4);
        System.out.print("Output (POST-ORDER): ");
        postorder(bTestCase4);
        System.out.print("\nOutput (IN-ORDER): ");
        inorder(bTestCase4);
        System.out.println("\n------------------------------------------------------------------------\n\n");


        System.out.println("(c) Tree-to-Postfix algorithm \n------------------------------------------------------------------------");

        Node cTestCase1 = new Node("100");
        System.out.println("Input: Hard coded tree of '" + input1 + "'");
        System.out.print("Output (POST-ORDER): ");
        postorder(cTestCase1);
        System.out.println("\n");

        Node cTestCase2 = new Node("+");
        cTestCase2.left = new Node("100");
        cTestCase2.right = new Node("200");
        System.out.println("Input: Hard coded tree of '" + input2 + "'");
        System.out.print("Output (POST-ORDER): ");
        postorder(cTestCase2);
        System.out.println("\n");

        Node cTestCase3 = new Node("*");
        cTestCase3.left = new Node("100");
        cTestCase3.right = new Node("-");
        cTestCase3.right.left = new Node("200");
        cTestCase3.right.right = new Node("300");
        System.out.println("Input: Hard coded tree of '" + input3 + "'");
        System.out.print("Output (POST-ORDER): ");
        postorder(cTestCase3);
        System.out.println("\n");

        Node cTestCase4 = new Node("*");
        cTestCase4.left = new Node("*");
        cTestCase4.left.left = new Node("100");
        cTestCase4.left.right = new Node("+");
        cTestCase4.left.right.left = new Node("200");
        cTestCase4.left.right.right = new Node("300");
        cTestCase4.right = new Node("/");
        cTestCase4.right.right = new Node("+");
        cTestCase4.right.right.right = new Node("100");
        cTestCase4.right.right.left = new Node("100");
        cTestCase4.right.left = new Node("-");
        cTestCase4.right.left.left = new Node("400");
        cTestCase4.right.left.right = new Node("200");
        System.out.println("Input: Hard coded tree of '" + input4 + "'");
        System.out.print("Output: ");
        postorder(cTestCase4);

        System.out.println("\n------------------------------------------------------------------------\n\n");

        
        System.out.println("(d) Evaluation algorithm \n------------------------------------------------------------------------");

        System.out.println("Input: " + input1);
        Node dTestCase1 = build(input1);
        System.out.println("Output: " + (int)evaluate(dTestCase1));

        System.out.println("\nInput: " + input2);
        Node dTestCase2 = build(input2);
        System.out.println("Output: " + (int)evaluate(dTestCase2));

        System.out.println("\nInput: " + input3);
        Node dTestCase3 = build(input3);
        System.out.println("Output: " + (int)evaluate(dTestCase3));

        System.out.println("\nInput: " + input4);
        Node dTestCase4 = build(input4);
        System.out.println("Output: " + (int)evaluate(dTestCase4));

        System.out.println("------------------------------------------------------------------------");
    }
}