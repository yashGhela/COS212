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
}