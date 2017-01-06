import java.util.ArrayList;
import java.util.List;

// Sparse Matrix class: 2D grid of linked nodes; elements which are
// the fillElem do not have nodes present. Retains a dense array of
// row and column pointers to speed some operations and ease
// implementation.
//
// Target Space Complexity: O(E + R + C)
//   E: number of non-fill elements in the matrix
//   R: number of rows in the matrix
//   C: number of cols in the matrix
public class SparseMatrix<T> {
	// Calss Member variables
	ArrayList<Head<T>> rowsArray;
	ArrayList<Head<T>> colsArray;
	int numsRows, numsCols;			// Rows and Cols
	T fillElement;					// Fill Element of the SparseMatrix
	int elementsCount;				// Total Elements in SparseMatrix
	
	
	// Suggested internal class to represent Row and Column
	// Headers. Tracks indices of the col or row, pointer to the start
	// of row/column. This is a separat class from Node to enable
	// efficient changing of row numbers: all Nodes point to a row Head
	// and col Head to determine their own row/column. You may modify
	// this class as you see fit.
	protected static class Head<X> {

		public int index; // Index of row/col

		public Node<X> nodes; // Dummy node at start of row/column; add data nodes after the dummy

		// Constructor for a new Head of a node list, i is the row or column number for this Head
		public Head(int i) {
			index = i;
			nodes = new Node<X>();
		}

	}

	// Suggested class to store data. Contains single links to next
	// nodes to the right and down. Also contains links to the row and
	// col Heads which store its row# and col#. You may modify this
	// class as you see fit.
	protected static class Node<Y> {

		public Head<Y> rowHead; // My row head

		public Head<Y> colHead; // My col head

		public Node<Y> right; // Next Node to the right

		public Node<Y> down; // Next node down

		public Y data; // Data associated with the node

	}

	// Constructor to create a SparseMatrix with the given number of
	// rows and columns with the given fillElem. The matrix starts out
	// empty: all elements are presumed to be the fillElem.
	//
	// Target Complexity: O(R+C)
	// R: number of rows in the matrix
	// C: number of cols in the matrix
	public SparseMatrix(int r, int c, T fillElem)
	{
		numsRows = r;
		numsCols = c;
		rowsArray = new ArrayList<>(numsRows);
		colsArray = new ArrayList<>(numsCols);
		elementsCount = 0;
		fillElement = fillElem;
		// Create an array for rows HEADs
		for (int i = 0; i < numsRows; i++) {
			Head<T> head = new Head<>(i);
			rowsArray.add(head);
		}
		// Create an array for cols HEADs
		for (int i = 0; i < numsCols; i++) {
			Head<T> head = new Head<>(i);
			colsArray.add(head);
		}
		
	}

	// Constructor to create a 0 by 0 SparseMatrix with the given
	// fillElem
	public SparseMatrix(T fillElem) {
		// Call First Constructor with 0, 0 parameters
		this(0, 0, fillElem);
	}

	// Return the number of non-fill elements in the matrix which have
	// been explicitly set. Corresponds to the number of non-dummy
	// nodes.
	//
	// Target Complexity: O(1)
	public int elementCount()
	{
		return elementsCount;
	}

	// Return the number of rows in the Matrix which is the last indexed
	// row+1.
	//
	// Target Complexity: O(1)
	public int rows() 
	{
		return numsRows;
	}

	// Return the number of cols in the Matrix which is the last indexed
	// col+1.
	//
	// Target Complexity: O(1)
	public int cols()
	{
		return numsCols;
	}

	// Return the fill element with which this matrix was initialized
	//
	// Target Complexity: O(1)
	public T getFillElem()
	{
		return fillElement;
	}

	// Add an empty row on to the bottom of the matrix.
	//
	// Target Complexity: O(1) amortized
	public void addRow()
	{
		// Create a new Head
		Head<T> head = new Head<>(numsRows);
		// Add in Rows
		rowsArray.add(head);
		// Increment number of Rows
		numsRows++;
	}

	// Add an empty col on right side of the matrix.
	//
	// Target Complexity: O(1) amortized
	public void addCol()
	{
		// Create a new Head
		Head<T> head = new Head<T>(numsCols);
		// Add in Cols
		colsArray.add(head);
		// Increment number of Cols
		numsCols++;
	}

	// Insert an empty row at position i. Later rows are "shifted down"
	// to a higher index. Importantly, Nodes should not need
	// adjustments; only the Head indices should need alteration.
	//
	// Target Complexity: O(R)
	// R: number of rows in the matrix
	public void insertRow(int i)
	{
		// Create a new Head
		Head<T> head = new Head<T>(i);
		// Add in Rows
		rowsArray.add(head);
		// Shift Down the Rows
		for (int j = rowsArray.size()-1; j > i; j--) {
			rowsArray.get(j-1).index++;
			rowsArray.set(j, rowsArray.get(j-1));
		}
		// Set the value of new Head in desired Location
		rowsArray.set(i, head);
		// Increment number of Rows
		numsRows++;
		
	}

	// Insert an empty col at position i. Later cols are "shifted right"
	// to a higher index. Importantly, Nodes should not need
	// adjustments; only the Head indices should need alteration.
	//
	// Target Complexity: O(C)
	// C: number of cols in the matrix
	public void insertCol(int i)
	{
		// Create a new Head
		Head<T> head = new Head<T>(i);
		// Add in Cols
		colsArray.add(head);
		// Shift Right the Cols
		for (int j = colsArray.size()-1; j > i; j--) {
			colsArray.get(j-1).index++;
			colsArray.set(j, colsArray.get(j-1));
		}
		// Set the value of new Head in desired Location
		colsArray.set(i, head);
		// Increment number of Cols
		numsCols++;
	}

	// Retrieve the element at position (i,j) in the matrix. If the
	// position is out of bounds, throw an IndexOutOfBoundsException
	// with an appropriate message. Otherwise, access the target row or
	// column and walk the list to locate the element. If no node for
	// the element exists, return the fillElem for this
	// matrix. Otherwise return the data found in the target node.
	//
	// Target Complexity: O(E)
	// E: number of non-fill elements in the matrix
	public T get(int i, int j)
	{
		if (i >= numsRows || j >= numsCols)
			throw new IndexOutOfBoundsException("Given i and j are out of Bounds.");
		// Access the Target Row and Search for the Element
		Node<T> rowHead = rowsArray.get(i).nodes;
		while (rowHead.right != null) {
			rowHead = rowHead.right;
			if (rowHead.colHead.index == j) {
				// Return the Searched Data
				return rowHead.data;
			}
		}
		// No Data Found. Return Fill Element
		return fillElement;
	}

	// Set element at position (i,j) to be x.
	//
	// If x is the fillElem, throw an IllegalArgumentException with an
	// appropriate message.
	//
	// If position (i,j) already has an element present, alter that
	// element.
	//
	// Otherwise, allocate a new node and link it into any existing
	// nodes by traversing the appropriate row and column lists.
	//
	// This method automatically expands the size of the matrix via
	// repeated calls to addRow() and addCol() to ensure that position
	// (i,j) is available. It does not throw any
	// IndexOutOfBoundsExceptions. If i or j is negative, also throw an
	// IllegalArgumentException.
	//
	// Target complexity:
	// O(E) when (i,j) is in bounds
	// O(E+R+C) when (i,j) is out of bounds requiring expansion
	// E: number of non-fill elements
	// R: number of rows in the matrix
	// C: number of cols in the matrix
	public void set(int i, int j, T x)
	{
		// Throw an Exception if index is Negative
		if (i < 0 || j < 0)
			throw new IllegalArgumentException("Indexes can't be Negative.");
		// Throw an Exception if x is fillElement
		if (x == fillElement)
			throw new IllegalArgumentException("Indexes can't be Negative.");
		
		// Expand the rows and cols if required.
		if(i >= numsRows) {
			// Expands Rows
			while(numsRows <= i) {
				addRow();
			}
		}
		if(j >= numsCols) {
			// Expands Cols
			while(numsCols <= j) {
				addCol();
			}
		}
		
		// Now check if the Specific Elements Exists or not.
		if (get(i,j).equals(getFillElem())) {
			// Node Doesn't Exists, Create a New Node and Insert
			Node<T> newNode = new Node<T>();
			newNode.data = x;					// Set Node Data
			newNode.rowHead = rowsArray.get(i);	// Set Row Head
			newNode.colHead = colsArray.get(j);	// Set Col Head
			
			///////// Link in the Row List
			Node<T> rowHead = rowsArray.get(i).nodes;
			if (rowHead.right == null) {
				// Empty List. Add Node
				rowHead.right = newNode;
			} else {
				while (rowHead.right != null) {
					if (rowHead.right.colHead.index > j)
						break;
					rowHead = rowHead.right;
				}
				newNode.right = rowHead.right;
				rowHead.right = newNode;
			}
			
			
			///////// Link in the Cols List
			Node<T> colHead = colsArray.get(j).nodes;
			if (colHead.down == null) {
				// Empty List. Add Node
				colHead.down = newNode;
			} else {
				while (colHead.down != null) {
					if (colHead.down.rowHead.index > i)
						break;
					colHead = colHead.down;
				}
				newNode.down = colHead.down;
				colHead.down = newNode;
			}
			
			// Increment the Elements Count
			elementsCount++;
		} else {
			// Node Already Exists. Alter its value
			// Access the Target Row and Search for the Element
			Node<T> rowHead = rowsArray.get(i).nodes;
			while (rowHead.right != null) {
				rowHead = rowHead.right;
				if (rowHead.colHead.index == j) {
					// Alter the Node value
					rowHead.data = x;
					return;
				}
			}
		}
		
	}

	// Set the element at position (i,j) to be the fill element.
	// Internally this should remove any node at that position by
	// unlinking it from row and column lists. If no data exists at
	// (i,j), no changes are made to the matrix. If (i,j) is out of
	// bounds, throw an IndexOutOfBoundsException with an appropriate
	// message.
	//
	// Target Complexity: O(E)
	// E: number of non-fill elements in the matrix
	public void setToFill(int i, int j)
	{
		if (i >= numsRows || j >= numsCols)
			throw new IndexOutOfBoundsException("Given i and j are out of Bounds.");
		
		// Do This only if it is a non-Fill Element
		if ( ! get(i,j).equals(getFillElem()) ) {
			// Unlink from Row
			Node<T> prevRowNode = rowsArray.get(i).nodes;
			Node<T> rowHead = prevRowNode.right;
			while(rowHead.colHead.index != j) {
				prevRowNode = rowHead;
				rowHead = rowHead.right;
			}
			prevRowNode.right = rowHead.right;
			
			// Unlink from Col
			Node<T> prevColNode = colsArray.get(j).nodes;
			Node<T> colHead = prevColNode.down;
			while(colHead.rowHead.index != i) {
				prevColNode = colHead;
				colHead = colHead.down;
			}
			prevColNode.down = colHead.down;
			
			// Decrement the Elements Count
			elementsCount--;
		}
	}

	// Create a display version of the sparse matrix. Each element
	// (including fills) is shown in a grid of elements. Each element
	// will be in a field of width 5 characters with a space after
	// it. Using String.format("%5s ",el) is useful to create the
	// string. Each row is on its own line.
	//
	// Example:
	// SparseMatrix<Double> x = new SparseMatrix<Double>(5,4, 0.0);
	// x.set(1,1, 1.0);
	// x.set(2,1, 2.0);
	// x.set(0,3, 3.0);
	// System.out.println(x);
	// 0.0 0.0 0.0 3.0
	// 0.0 1.0 0.0 0.0
	// 0.0 2.0 0.0 0.0
	// 0.0 0.0 0.0 0.0
	// 0.0 0.0 0.0 0.0
	//
	// Target Complexity: O(R*C)
	// R: number of rows in the matrix
	// C: number of cols in the matrix
	// E: number of non-fill elements in the matrix
	// Note: repeated calls to get(i,j) will not adhere to this
	// complexity
	public String toString()
	{
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < numsRows; i++) {
			for (int j = 0; j < numsCols; j++) {
				// Format the String to Return
				str.append(String.format("%5s ", get(i, j)));
			}
			str.append("\n");
		}
		return str.toString();
	}

	// Required but may simply return "". This method will be called
	// and the string it produces will be reported when tests fail to
	// aid in viewing the internal state of the SparseMatrix for
	// debugging.
	public String debugString()
	{
		StringBuffer str = new StringBuffer("SparseMatrix\n");
		str.append("Rows:         " + numsRows + "\n");
		str.append("Cols:         " + numsCols + "\n");
		str.append("ElementCount: " + elementsCount + "\n");
		str.append("fillElement:  " + fillElement + "\n");
		str.append("toString():\n");
		str.append(this.toString());
		return str.toString();
	}

	// Produce a List of all elements as triplets of (i,j,data). The
	// List may be any kind (ArrayList/LinkedList) and in any order.
	//
	// Target Complexity: O(R + E)
	// R: number of rows in the matrix
	// E: number of non-fill elements in the matrix
	// Note: repeated calls to get(i,j) will not adhere to this
	// complexity
	public List<Triple<Integer, Integer, T>> allElements()
	{
		List<Triple<Integer, Integer, T>> listOfElements = new ArrayList<Triple<Integer, Integer, T>>(elementsCount);
		// Iterate through all Rows.
		for (int i = 0; i < rowsArray.size(); i++) {
			Node<T> rowHead = rowsArray.get(i).nodes.right;
			while(rowHead != null) {
				// Get the Element Indices and Data
				Triple<Integer, Integer, T> newEle = new Triple<Integer, Integer, T>(i, rowHead.colHead.index, rowHead.data);
				// Add Element to List
				listOfElements.add(newEle);
				// Next Element in the Row
				rowHead = rowHead.right;
			}
		}
		// Return the List of Elements
		return listOfElements;
	}

	// Add two sparse matrices of Doubles together in an elementwise
	// fashion and produce another SparseMatrix as the result. The fill
	// element of the resulting matrix is the sum of the two fill
	// elements from matrices x and y. If the sum of any two elements
	// in the matrices equals the resulting fill element, that should
	// not occupy a node in the resulting sparse matrix.
	//
	// Matrices must have the same size (rows,cols) to be added. Throw
	// an IllegalArgumentException with an appropriate message if not.
	//
	// Target Complexity: O(R + C + Ex + Ey)
	// R: Number of rows in matrices x and y
	// C: Number of cols in matrices x and y
	// Ex, Ey: Number of non-fill elements in matrix x and y
	// Memory constraint: O(1)
	// The memory constraint does not count the size of x, y, or the
	// result matrix which is returned.
	public static SparseMatrix<Double> addFast(SparseMatrix<Double> x, SparseMatrix<Double> y) 
	{
		if (x.numsRows != y.numsRows || x.numsCols != y.numsCols) {
			throw new IllegalArgumentException("Couldn't be added. Matrices are not of Same Sizes.");
		}
		int rows = x.numsRows, cols = x.numsCols;
		Double resultFill = x.getFillElem() + y.getFillElem();
		SparseMatrix<Double> z = new SparseMatrix<Double>(rows, cols, resultFill);
		
		for (int i = 0; i < rows; i++) {
			// Get the Row Heads of Both Matrices
			Node<Double> xRowHead = x.rowsArray.get(i).nodes.right;
			Node<Double> yRowHead = y.rowsArray.get(i).nodes.right;
			// Loop Until one of the Rows Ends
			while(xRowHead != null && yRowHead != null) {
				// If the Elements in same column exists
				if (xRowHead.colHead.index == yRowHead.colHead.index) {
					// Add both elements and insert in z if it is a non-Fill Element
					double addData = xRowHead.data + yRowHead.data;
					if (!z.getFillElem().equals(addData)) {
						z.set(i, xRowHead.colHead.index, addData);
					}
					// Next Elements in the Rows of Both Matrices
					xRowHead = xRowHead.right;
					yRowHead = yRowHead.right;
					
				} else if (xRowHead.colHead.index < yRowHead.colHead.index) {
					
					// Insert the Element from xRow and Fill Element of Y to Result Matrix
					double addData = xRowHead.data + y.getFillElem();
					if (!z.getFillElem().equals(addData)) {
						z.set(i, xRowHead.colHead.index, addData);
					}
					
					// Next Element in x Matrix
					xRowHead = xRowHead.right;
					
				} else if (xRowHead.colHead.index > yRowHead.colHead.index) {
					
					// Insert the Element from yRow and Fill Element of X to Result Matrix
					double addData = yRowHead.data + x.getFillElem();
					if (!z.getFillElem().equals(addData)) {
						z.set(i, yRowHead.colHead.index, addData);
					}
					
					// Next Element in y Matrix
					yRowHead = yRowHead.right;
					
				}
				
			}
			
			// Check if xRow is Ended
			if (xRowHead == null) {
				// Add All Elements of yRow to Result Matrix
				while(yRowHead != null) {
					// Insert the Element from yRow and Fill Element of X to Result Matrix
					double addData = yRowHead.data + x.getFillElem();
					if (!z.getFillElem().equals(addData)) {
						z.set(i, yRowHead.colHead.index, addData);
					}
					yRowHead = yRowHead.right;
				}
			}
			
			// Check if yRow is Ended
			if (yRowHead == null) {
				// Add All Elements of xRow to Result Matrix
				while(xRowHead != null) {
					// Insert the Element from xRow and Fill Element of Y to Result Matrix
					double addData = xRowHead.data + y.getFillElem();
					if (!z.getFillElem().equals(addData)) {
						z.set(i, xRowHead.colHead.index, addData);
					}
					xRowHead = xRowHead.right;
				}
			}
			
			// Loop Back for Next Row
		}
		
		return z;
	}
	
	public static void main(String args[])
	{
		
	}

}