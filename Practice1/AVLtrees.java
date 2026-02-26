public class AVLTree<T extends Comparable<T>>{

    private static class AVLNode<T>{
        private T data; 
        private AVLNode<T> left; 
        private AVLNode<T> right; 
        private int balancefactor; 

        public AVLNode(T data){
            this.data= data
            this.balancefactor=0
        }
    }

    private AVLNode<T> root;

    // single right 
    private AVLNode<T> rotateRight(AVLNode<T> k2) {
        AVLNode<T> k1 = k2.left;
        
        // Perform rotation
        k2.left = k1.right;
        k1.right = k2;
        
        // Update balance factors (textbook page 146)
        // After rotation, both nodes become balanced
        k2.balanceFactor = 0;
        k1.balanceFactor = 0;
        
        return k1;  // New root
    }
    
    //single left
    private AVLNode<T> rotateLeft(AVLNode<T> k2) {
        AVLNode<T> k1 = k2.right;
        
        // Perform rotation
        k2.right = k1.left;
        k1.left = k2;
        
        // Update balance factors
        k2.balanceFactor = 0;
        k1.balanceFactor = 0;
        
        return k1;  // New root
    }

     private AVLNode<T> rotateLeftRight(AVLNode<T> k3) {
        AVLNode<T> k1 = k3.left;
        AVLNode<T> k2 = k1.right;
        
        // Step 1: Left rotation on left child
        k1.right = k2.left;
        k2.left = k1;
        k3.left = k2;
        
        // Step 2: Right rotation on parent
        k3.left = k2.right;
        k2.right = k3;
        
        // Update balance factors based on textbook analysis (page 150)
        if (k2.balanceFactor == 1) {      // Insert in subtree C
            k1.balanceFactor = -1;
            k3.balanceFactor = 0;
        } else if (k2.balanceFactor == -1) { // Insert in subtree B
            k1.balanceFactor = 0;
            k3.balanceFactor = 1;
        } else {                           // k2 is the new node
            k1.balanceFactor = 0;
            k3.balanceFactor = 0;
        }
        k2.balanceFactor = 0;
        
        return k2;  // New root
    }
    
    /**
     * RIGHT-LEFT DOUBLE ROTATION
     * Textbook Figure 4.36 (Case 3: Right-Left)
     * First right rotation on right child, then left rotation on parent
     */
    private AVLNode<T> rotateRightLeft(AVLNode<T> k1) {
        AVLNode<T> k3 = k1.right;
        AVLNode<T> k2 = k3.left;
        
        // Step 1: Right rotation on right child
        k3.left = k2.right;
        k2.right = k3;
        k1.right = k2;
        
        // Step 2: Left rotation on parent
        k1.right = k2.left;
        k2.left = k1;
        
        // Update balance factors (symmetric to Left-Right case)
        if (k2.balanceFactor == -1) {      // Insert in subtree B
            k1.balanceFactor = 1;
            k3.balanceFactor = 0;
        } else if (k2.balanceFactor == 1) { // Insert in subtree C
            k1.balanceFactor = 0;
            k3.balanceFactor = -1;
        } else {                            // k2 is the new node
            k1.balanceFactor = 0;
            k3.balanceFactor = 0;
        }
        k2.balanceFactor = 0;
        
        return k2;  // New root
    }


     public void insert(T data) {
        root = insert(root, data);
    }
    
    /**
     * Recursive insert that maintains AVL property
     * Based on textbook algorithm (page 153)
     */
    private AVLNode<T> insert(AVLNode<T> node, T data) {
        // Base case: empty spot found
        if (node == null) {
            return new AVLNode<>(data);
        }
        
        // Standard BST insert
        int compare = data.compareTo(node.data);
        if (compare < 0) {
            node.left = insert(node.left, data);
            
            // Update balance factor after left insertion
            node.balanceFactor--;
            
        } else if (compare > 0) {
            node.right = insert(node.right, data);
            
            // Update balance factor after right insertion
            node.balanceFactor++;
            
        } else {
            // Duplicate - ignore (or handle as needed)
            return node;
        }
        
        // Check if node became unbalanced
        return balance(node);
    }
    
    /**
     * Balance the node if needed
     * Based on textbook's four cases (page 153)
     */
    private AVLNode<T> balance(AVLNode<T> node) {
        // Case 1 & 2: Left subtree too tall (balance factor < -1)
        if (node.balanceFactor < -1) {
            if (node.left.balanceFactor <= 0) {
                // Case 1: Left-Left (single right rotation)
                // Textbook Figure 4.31
                node = rotateRight(node);
            } else {
                // Case 2: Left-Right (double rotation)
                // Textbook Figure 4.35
                node = rotateLeftRight(node);
            }
        }
        // Case 3 & 4: Right subtree too tall (balance factor > 1)
        else if (node.balanceFactor > 1) {
            if (node.right.balanceFactor >= 0) {
                // Case 4: Right-Right (single left rotation)
                // Textbook Figure 4.33
                node = rotateLeft(node);
            } else {
                // Case 3: Right-Left (double rotation)
                // Textbook Figure 4.36
                node = rotateRightLeft(node);
            }
        }
        
        return node;
    }
    
    // === DELETION (Textbook pages 154-157) ===
    
    /**
     * Public delete method
     */
    public void delete(T data) {
        root = delete(root, data);
    }
    
    /**
     * Recursive delete that maintains AVL property
     * Based on textbook algorithm (page 157)
     */
    private AVLNode<T> delete(AVLNode<T> node, T data) {
        if (node == null) {
            return null;
        }
        
        int compare = data.compareTo(node.data);
        if (compare < 0) {
            node.left = delete(node.left, data);
            if (node.left != null) {
                // Adjust balance factor after deletion from left
                node.balanceFactor++;
            }
        } else if (compare > 0) {
            node.right = delete(node.right, data);
            if (node.right != null) {
                // Adjust balance factor after deletion from right
                node.balanceFactor--;
            }
        } else {
            // Node to delete found
            
            // Case 1: No children
            if (node.left == null && node.right == null) {
                return null;
            }
            
            // Case 2: One child
            if (node.left == null) {
                return node.right;
            }
            if (node.right == null) {
                return node.left;
            }
            
            // Case 3: Two children
            // Find inorder successor (smallest in right subtree)
            AVLNode<T> successor = findMin(node.right);
            node.data = successor.data;
            node.right = delete(node.right, successor.data);
            
            // Adjust balance factor after successor deletion
            if (node.right != null) {
                node.balanceFactor--;
            }
        }
        
        // Rebalance if needed (textbook page 156-157)
        return balance(node);
    }
    
    private AVLNode<T> findMin(AVLNode<T> node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }
    
    // === TRAVERSAL METHODS ===
    
    /**
     * In-order traversal (sorted order)
     */
    public void inOrderTraversal() {
        inOrderTraversal(root);
        System.out.println();
    }
    
    private void inOrderTraversal(AVLNode<T> node) {
        if (node != null) {
            inOrderTraversal(node.left);
            System.out.print(node.data + " ");
            inOrderTraversal(node.right);
        }
    }
    
    /**
     * Print tree structure with balance factors
     */
    public void printWithBalanceFactors() {
        printWithBalanceFactors(root, 0);
    }
    
    private void printWithBalanceFactors(AVLNode<T> node, int level) {
        if (node == null) return;
        
        printWithBalanceFactors(node.right, level + 1);
        
        for (int i = 0; i < level; i++) {
            System.out.print("    ");
        }
        System.out.println(node.data + "(bf=" + node.balanceFactor + ")");
        
        printWithBalanceFactors(node.left, level + 1);
    }

}