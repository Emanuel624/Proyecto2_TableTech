package ServerApp;

public class AVLTree {
     private AvlNode rootNode;

        //Constructor to set null value to the rootNode
        public AVLTree() {
            rootNode = null;
        }

        //create removeAll() method to make AVL Tree empty
        public void removeAll()
        {
            rootNode = null;
        }

        // create checkEmpty() method to check whether the AVL Tree is empty or not
        public boolean checkEmpty() {
            return rootNode == null;
        }

        // create insertElement() to insert element to the AVL Tree
        public void insertElement(int element)
        {
            rootNode = insertElement(element, rootNode);
        }

        //create getHeight() method to get the height of the AVL Tree
        private int getHeight(AvlNode node )
        {
            return node == null ? -1 : node.height;
        }

        //create maxNode() method to get the maximum height from left and right node
        private int getMaxHeight(int leftNodeHeight, int rightNodeHeight)
        {
            return leftNodeHeight > rightNodeHeight ? leftNodeHeight : rightNodeHeight;
        }


        //create insertElement() method to insert data in the AVL Tree recursively
        private AvlNode insertElement(int element, AvlNode node)
        {
            //check whether the node is null or not
            if (node == null)
                node = new AvlNode(element);
                //insert a node in case when the given element is lesser than the element of the root node
            else if (element < node.element)
            {
                node.leftChild = insertElement( element, node.leftChild );
                if( getHeight( node.leftChild ) - getHeight( node.rightChild ) == 2 )
                    if( element < node.leftChild.element )
                        node = rotateWithLeftChild( node );
                    else
                        node = doubleWithLeftChild( node );
            }
            else if( element > node.element )
            {
                node.rightChild = insertElement( element, node.rightChild );
                if( getHeight( node.rightChild ) - getHeight( node.leftChild ) == 2 )
                    if( element > node.rightChild.element)
                        node = rotateWithRightChild( node );
                    else
                        node = doubleWithRightChild( node );
            }
            else
                  // if the element is already present in the tree, we will do nothing
            node.height = getMaxHeight( getHeight( node.leftChild ), getHeight( node.rightChild ) ) + 1;

            return node;

        }

        // creating rotateWithLeftChild() method to perform rotation of binary tree node with left child
        private AvlNode rotateWithLeftChild(AvlNode node2) {
            AvlNode node1 = node2.leftChild;
            node2.leftChild = node1.rightChild;
            node1.rightChild = node2;
            node2.height = getMaxHeight( getHeight( node2.leftChild ), getHeight( node2.rightChild ) ) + 1;
            node1.height = getMaxHeight( getHeight( node1.leftChild ), node2.height ) + 1;
            return node1;
        }

        // creating rotateWithRightChild() method to perform rotation of binary tree node with right child
        private AvlNode rotateWithRightChild(AvlNode node1) {
            AvlNode node2 = node1.rightChild;
            node1.rightChild = node2.leftChild;
            node2.leftChild = node1;
            node1.height = getMaxHeight( getHeight( node1.leftChild ), getHeight( node1.rightChild ) ) + 1;
            node2.height = getMaxHeight( getHeight( node2.rightChild ), node1.height ) + 1;
            return node2;
        }

        //create doubleWithLeftChild() method to perform double rotation of binary tree node. This method first rotate the left child with its right child, and after that, node3 with the new left child
        private AvlNode doubleWithLeftChild(AvlNode node3) {
            node3.leftChild = rotateWithRightChild( node3.leftChild );
            return rotateWithLeftChild( node3 );
        }

        //create doubleWithRightChild() method to perform double rotation of binary tree node. This method first rotate the right child with its left child and after that node1 with the new right child
        private AvlNode doubleWithRightChild(AvlNode node1) {
            node1.rightChild = rotateWithLeftChild( node1.rightChild );
            return rotateWithRightChild( node1 );
        }

    }
