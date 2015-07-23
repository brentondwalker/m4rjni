/*
 * Mzd.java
 *
 * Copyright (C) 2015 Brenton Walker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class Mzd {

    /*
     * the main class variable is a pointer to the mzd_t object,
     * stored here as an int. Also the matrix dimensions
     */
    protected long _mzd_t_pointer = -1;
    protected int _m, _n;
	
    static 
    {
        /*
         * load the necessary shared libraries
         */
        System.loadLibrary("m4ri_wrapper");
        System.loadLibrary("m4ri");

        m4ri_init();
    }

    /**
     * native method to initialize m4ri data structures.
     * I'm not sure this is really necessary to call; mdz_init() may take care of
     * things if the aren't init'ed already.
     */
    private static native void m4ri_init();
	
	
    /**
     * Useful method to run in each accessor before doing anything else.
     * Since we're dealing with the underlying m4ri objects it makes sense to be extra careful.
     */
    protected void sanityCheck() {
        if (_mzd_t_pointer==-1) {
            System.out.println("ERROR: called an Mzd method on an un-allocated matrix.");
            System.exit(-1);
        }
    }
    protected void sanityCheck(int x, int y) {
        if (_mzd_t_pointer==-1) {
            System.out.println("ERROR: called an Mzd method on an un-allocated matrix.");
            System.exit(-1);
        }
        if ((x<0) || (y<0) || (x>_m) || (y>_n)) {
            System.out.println("ERROR: passed in invalid row and column: ("+x+" , "+y+")");
            System.exit(-1);
        }
    }
	
	
    /**
     * Constructor
     * @param m
     * @param n
     */
    private native long mzd_init(int m, int n);
    public Mzd(int m, int n) {
        _mzd_t_pointer = mzd_init(m,n);
        _m = m;
        _n = n;
        //System.out.println("mzd_t_pointer="+_mzd_t_pointer);
    }
	
    /**
     * Copy constructor
     * @param A
     */
    private native long mzd_copy(long ptr);
    public Mzd(Mzd A) {
        if (A==null) {
            System.out.println("ERROR: trying to use copy constructor with a null object!");
            System.exit(-1);
        }
		
        _mzd_t_pointer = mzd_copy(A._mzd_t_pointer);
        _m = A._m;
        _n = A._n;
    }
	
    /**
     * Private internal constructor for when we have a mzd_t* that we
     * need to wrap in a Mzd object.
     * 
     * This does NOT copy the object passed in.
     * 
     * @param ptr
     */
    private native static int mzd_get_nrows(long ptr);
    private native static int mzd_get_ncols(long ptr);
    private Mzd(long ptr) {
        if (ptr == 0) {
            System.out.println("ERROR: trying to use wrapper constructor on a null object!");
            System.exit(-1);
        }
        _mzd_t_pointer = ptr;
        _m = mzd_get_nrows(ptr);
        _n = mzd_get_ncols(ptr);
    }
	
	
    /**
     * handy constructor to initialize a matrix from an array of integers.
     * 
     * @param X
     */
    public Mzd(int[][] X) {
        if (X==null) {
            System.out.println("ERROR: trying to use int-matrix constructor on a null object!");
            System.exit(-1);
        }
        this._m = X.length;
        this._n = X[0].length;
        _mzd_t_pointer = mzd_init(_m,_n);
		
        for (int i=0; i<_m; i++) {
            for (int j=0; j<_n; j++) {
                this.writeBit(i, j, X[i][j]%2);
            }
        }
    }
	
	
	
    /**
     * Methods to deallocate the matrix
     */
    private native void mzd_destroy(long ptr);
    public void destroy() {
        mzd_destroy(_mzd_t_pointer);
        _mzd_t_pointer = -1;
        _m = -1;
        _n = -1;
    }
	
	
    /**
     * Get the number of rows in this object.
     * 
     * @return
     */
    public int getNrows() {
        this.sanityCheck();
        return mzd_get_nrows(this._mzd_t_pointer);
    }

	
    /**
     * Get the number of columns in this object.
     * 
     * @return
     */
    public int getNcols() {
        this.sanityCheck();
        return mzd_get_ncols(this._mzd_t_pointer);
    }
	
	
    /**
     * Method to write a specified bit in the matrix
     * @param row
     * @param col
     * @param val
     */
    private native void mzd_write_bit(long ptr, int row, int col, int val);
    public void writeBit(int row, int col, int val) {
        sanityCheck(row,col);
        mzd_write_bit(_mzd_t_pointer, row, col, val);
    }

	
    /**
     * Method to read a specified bit in the matrix
     * @param row
     * @param col
     * @param val
     */
    private native int mzd_read_bit(long ptr, int row, int col);
    public int readBit(int row, int col) {
        sanityCheck(row,col);
        return mzd_read_bit(_mzd_t_pointer, row, col);
    }
	

    /**
     * Method to row-reduce a matrix
     * 
     * @param full - if true return reduced row echelon form, if false upper triangular
     * @return the rank of the matrix
     */
    private native int mzd_echelonize(long ptr, boolean full);
    public int echelonize(boolean full) {
        sanityCheck();
        return mzd_echelonize(_mzd_t_pointer,full);
    }

	
    /**
     * Solves A X = B with A and B matrices. 
     * returns a new Mzd object, X
     * 
     * @param A
     * @param B
     * @param cutoff - Minimal dimension for Strassen recursion (default: 0). 
     * @param inconsistency_check - decide whether or not to perform a check for incosistency (faster without but output not defined if system is not consistent). 
     * @return
     */
    private native static int mzd_solve_left(long A, long B, int cutoff, boolean inconsistency_check);
    public static Mzd solveLeft(Mzd A, Mzd B, int cutoff, boolean inconsistency_check) {
        // need to copy A and B b/c they'll get overwritten,
        // and B will be our return value
        Mzd Atmp = new Mzd(A);
        Mzd Btmp = new Mzd(B);
        int result = mzd_solve_left(Atmp._mzd_t_pointer, Btmp._mzd_t_pointer, cutoff, inconsistency_check);
        Atmp.destroy();
        if (result!=0) {
            Btmp.destroy();
            return null;
        }
        return Btmp;
    }
	
	
    /**
     * Solve X for A X = 0. 
     * If r is the rank of the nr x nc matrix A,
     * return the nc x (nc-r) matrix X such that A*X == 0 and
     * that the columns of X are linearly independent.
     * 
     * @param cutoff - Minimal dimension for Strassen recursion (default: 0).
     * @return
     */
    private native long mzd_kernel_left_pluq(long A, int cutoff);
    public Mzd kernelLeft(int cutoff) {
        // need to copy A b/c it will get overwritten
        long Aptr = mzd_copy(this._mzd_t_pointer);
        long Xptr = mzd_kernel_left_pluq(Aptr, cutoff);
        //System.out.println("Aptr:");
        //mzd_print(Aptr);
        //System.out.println("Xptr:");
        //mzd_print(Xptr);
        mzd_destroy(Aptr);
        if (Xptr==0) {
            return null;
        }
        return new Mzd(Xptr);
    }
	
	
    /**
     * Fill matrix M with uniformly distributed bits. 
     */
    private native void mzd_randomize(long ptr);
    public void randomize() {
        this.sanityCheck();
        mzd_randomize(this._mzd_t_pointer);
    }
	

    /**
     * Print out the matrix.  Use this instead of trying toString().
     */
    private native void mzd_print(long ptr);
    public void print() {
        this.sanityCheck();
        mzd_print(this._mzd_t_pointer);
    }
	
	
    /**
     * Print out compact info about the matrix.
     */
    private native void mzd_info(long ptr, boolean do_rank);
    public void info(boolean do_rank) {
        this.sanityCheck();
        mzd_info(this._mzd_t_pointer, do_rank);
    }
	
    /**
     * Concatenate B to A and write the result to C.
     * That is,
     * [ A ], [ B ] -> [ A  B ] = C
     * 
     * @param A
     * @param B
     * @return C
     */
    private static native long mzd_concat(long Aptr, long Bptr);
    public static Mzd concat(Mzd A, Mzd B) {
        A.sanityCheck(); B.sanityCheck();
        long Cptr = mzd_concat(A._mzd_t_pointer, B._mzd_t_pointer);
        if (Cptr==0) {
            return null;
        }
        return new Mzd(Cptr);
    }
	
	
    /**
     * Stack A on top of B and write the result to C.
     * That is,
     * [ A ], [ B ] -> [ A ] = C
     *                 [ B ]
     * 
     * @param A
     * @param B
     * @return C
     */
    private static native long mzd_stack(long Aptr, long Bptr);
    public static Mzd stack(Mzd A, Mzd B) {
        //System.out.println("stack()");
        A.sanityCheck(); B.sanityCheck();
        //System.out.println("\t...passed sanityCheck");
        long Cptr = mzd_stack(A._mzd_t_pointer, B._mzd_t_pointer);
        //System.out.println("\t...called mzd_stack");
        if (Cptr==0) {
            return null;
        }
        return new Mzd(Cptr);
    }
	
	
    /**
     * isZero()
     * 
     * @return true if the matrix==[0]
     */
    private native int mzd_is_zero(long ptr);
    public boolean isZero() {
        this.sanityCheck();
        return (mzd_is_zero(this._mzd_t_pointer)!=0);
    }
	
    /**
     * Naive cubic matrix multiplication.
     * 
     * Compute C such that C == [this][B].
     * 
     * @param B
     * @return C
     */
    private static native long mzd_mul_naive(long Aptr, long Bptr);
    public Mzd multiply(Mzd B) {
        this.sanityCheck();
        if (B._m != this._n) {
            System.out.println("ERROR: Multiply() - inner dimensions of matrices don't match.");
            return null;
        }
        long Cptr = mzd_mul_naive(this._mzd_t_pointer, B._mzd_t_pointer);
        if (Cptr==0) {
            return null;
        }
        return new Mzd(Cptr);
    }
	
	
    /** 
     * Compute new matrix C such that C == [this] + [B].
     * 
     * I am pretty sure this gives a new Mzd object that needs to be disposed later
     * Should really make this static
     * 
     * @param B
     * @return C
     */
    private static native long mzd_add(long Aptr, long Bptr);
    public Mzd add(Mzd B) {
        this.sanityCheck();
        if (B._m != this._m || B._n != this._n) {
            System.out.println("ERROR: Add() - dimensions of matrices don't match.");
            return null;
        }
        long Cptr = mzd_add(this._mzd_t_pointer, B._mzd_t_pointer);
        if (Cptr==0) {
            return null;
        }
        return new Mzd(Cptr);
    }

    /**
     * Same thing as above, but static method
     * 
     * @param A
     * @param B
     * @return
     */
    public static Mzd add(Mzd A, Mzd B) {
        A.sanityCheck();
        B.sanityCheck();
        if (B._m != A._m || B._n != A._n) {
            System.out.println("ERROR: Add() - dimensions of matrices don't match.");
            return null;
        }
        long Cptr = mzd_add(A._mzd_t_pointer, B._mzd_t_pointer);
        if (Cptr==0) {
            return null;
        }
        return new Mzd(Cptr);
    }

	
    /**
     * Transpose a matrix. 
     * This function uses the fact that:
     *    [ A B ]T    [AT CT]
     *    [ C D ]  =  [BT DT] 
     *    
     *    and thus rearranges the blocks recursively.

     * @return new matrix containing transpose.
     */
    private static native long mzd_transpose(long ptr);
    public Mzd transpose() {
        this.sanityCheck();
        long Dptr = mzd_transpose(this._mzd_t_pointer);
        if (Dptr==0) {
            return null;
        }
        return new Mzd(Dptr);
    }
	
    /**
     * Tests if B==this as matrices
     * 
     * @param B
     * @return true or false
     */
    private static native int mzd_equal(long A, long B);
    public boolean equals(Mzd B) {
        this.sanityCheck();
        return (mzd_equal(this._mzd_t_pointer, B._mzd_t_pointer)!=0);
    }
	
	
    /**
     * copy row j from A to row i from B.
     * 
     * The offsets of A and B must match and the number of columns of A must be less
     * than or equal to the number of columns of B.
     * 
     * @param B
     * @param i
     * @param A
     * @param j
     */
    private native static void mzd_copy_row(long B, int i, long A, int j);
    public static void copyRow(Mzd B, int i, Mzd A, int j) {
        A.sanityCheck();
        B.sanityCheck();
        if (i<0 || j<0 || i>=B._m || j>=A._m) {
            System.out.println("ERROR: copyRow() - passed invalid row indices: ("+i+","+j+")");
            return;
        }
        mzd_copy_row(B._mzd_t_pointer, i, A._mzd_t_pointer, j);
    }
	
	
    /**
     * copy numRows rows of A, starting at row j, to B, starting at row i
     * 
     * There is no corresponding function in m4ri, so this is implemented in terms
     * of single row copies in the c wrapper
     * 
     * @param B
     * @param i
     * @param A
     * @param j
     * @param numRows
     */
    private native static void mzd_copy_rows(long B, int i, long A, int j, int numRows);
    public static void copyRows(Mzd B, int i, Mzd A, int j, int numRows) {
        //System.out.println("copyRows("+B+" , "+i+" , "+A+" , "+j+" , "+numRows+")");
        //System.out.println("B:");
        //B.print();
        //System.out.println("A:");
        //A.print();
		
        A.sanityCheck();
        B.sanityCheck();
        if (i<0 || j<0 || (i+numRows)>B._m || (j+numRows)>A._m) {
            System.out.println("ERROR: copyRows() - passed invalid row indices: ("+i+","+j+","+numRows+")");
            return;
        }
		
        mzd_copy_rows(B._mzd_t_pointer, i, A._mzd_t_pointer, j, numRows);
    }

    /**
     * Return an array of dim vectors from the standard basis
     * 
     * @param dim
     * @return
     */
    public static Mzd[] standardBasis(int dim) {
        Mzd[] M = new Mzd[dim];
        for (int i=0; i<dim; i++) {
            Mzd v = new Mzd(1,dim);
            v.writeBit(0, i, 1);
            M[i] = v;
        }
        return M;
    }
	
	
    /**
     * Return the dim*dim identity matrix
     * 
     * @param dim
     * @return
     */
    public static Mzd identityMatrix(int dim) {
        Mzd M = new Mzd(dim, dim);
        for (int i=0; i<dim; i++) {
            M.writeBit(i, i, 1);
        }
        return M;
    }
	
	
    /**
     * Call srandom for m4ri.  Use the seed provided.
     * 
     * @param seed
     */
    private native static void m4ri_srandom(int seed);
    public static void srandom(int seed) {
        m4ri_srandom(seed);
    }
	

    /**
     * Add the rows sourcerow and destrow and stores the total in the row destrow.
     * 
     * @param srcRow
     * @param dstRow
     */
    private native void m4ri_row_add(long M, int srcRow, int dstRow);
    public void rowAdd(int srcRow, int dstRow) {
        this.sanityCheck(srcRow,0);
        this.sanityCheck(dstRow,0);
        m4ri_row_add(this._mzd_t_pointer, srcRow, dstRow);
    }
	
	
    /**
     * Clear the given row, but only begins at the column coloffset.
     * 
     * @param row
     * @param colOffset
     */
    private native void m4ri_row_clear_offset(long M, int row, int colOffset);
    public void rowClearOffset(int row, int colOffset) {
        this.sanityCheck(row,colOffset);
        m4ri_row_clear_offset(this._mzd_t_pointer, row, colOffset);
    }
	
	
    /**
     * Copy a submatrix.
     * 
     * This version takes M as a parameter and fills it in.
     * If M is null it allocates the matrix and returns it.
     * 
     * Note that the upper bounds are not included.
     * 
     * @param M
     * @param lowr   - start row
     * @param lowc   - start column
     * @param highr  - stop row
     * @param highc  - stop column
     * @return
     */
    private native long m4ri_submatrix(long S, long M, int lowr, int lowc, int highr, int highc);
    public Mzd submatrix(Mzd S, int lowr, int lowc, int highr, int highc) {
        //System.out.println("submatrix("+lowr+","+lowc+","+highr+","+highc+")");
        this.sanityCheck(lowr, lowc);
        this.sanityCheck(highr, highc);
        if ((lowr>=highr) || (lowc>=highc)) {
            System.out.println("ERROR: submatrix() called with nonsense rows and columns");
            return null;
        }
        //System.out.println("\t...passed sanity checks.");
		
        if (S==null) {
            //System.out.println("\t...S==nulll case");
            long Mptr = m4ri_submatrix(0, this._mzd_t_pointer, lowr, lowc, highr, highc);
            S = new Mzd(Mptr);
        } else {
            //System.out.println("\t...S!=nulll case");
            S.sanityCheck(highr-lowr, highc-lowc);
            m4ri_submatrix(S._mzd_t_pointer, this._mzd_t_pointer, lowr, lowc, highr, highc);
        }
		
        return S;
    }
	
	
    /**
     * Not a direct interface to an m4ri routine, this is a natural place to
     * put this functionality.  This computes a basis for the intersection of
     * the rowspace of two matrices (same column dimension).
     * 
     * @param U
     * @param W
     * @return  - a new matrix containing the basis for the intersection in its rows.
     */
    public static Mzd vsIntersect(Mzd U, Mzd W) {
        //System.out.println("vsIntersect()");
        if (U==null || W==null) {
            System.out.println("ERROR: vsIntersect() called with null matrix.");
            return null;
        }
        if (U.getNcols() != W.getNcols()) {
            System.out.println("ERROR: vsIntersect() called with matrices of different dimension.");
            return null;
        }

        int numcols = U.getNcols();
		
        Mzd UW = stack(U,W);
        //System.out.println("\nUW:");
        //UW.print();
        Mzd UWt = UW.transpose();
        //System.out.println("\nUWt:");
        //UWt.print();
        Mzd N = UWt.kernelLeft(0);
        if (N==null) {
            //System.out.println("\nN = 0   (empty nullspace)");
            UW.destroy();
            UWt.destroy();
            return new Mzd(0,numcols);
        }
        //System.out.println("\nN:");
        //N.print();
        Mzd NU = N.submatrix(null, 0, 0, U.getNrows(), N.getNcols());
        //System.out.println("\nNU:");
        //NU.print();
        Mzd NUt = NU.transpose();
        //System.out.println("\nNUt:");
        //NUt.print();
		
        Mzd S = NUt.multiply(U);
        int urank = S.echelonize(false);
		
        UW.destroy();
        UWt.destroy();
        N.destroy();
        NU.destroy();
        NUt.destroy();
		
        Mzd result = null;
        if (urank > 0) {
            result = S.submatrix(null, 0, 0, urank, numcols);
        } else {
            result = new Mzd(0,numcols);
        }

        S.destroy();
        //System.out.println("computed intersection:");
        //result.print();
        return result;
    }
	
	
}
