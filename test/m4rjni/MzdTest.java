package m4rjni;

import static org.junit.Assert.*;
import m4rjni.Mzd;

import org.junit.Test;

//TODO - most of these don't test how the methods handle bad input

public class MzdTest {

	@Test
	public void testMzdIntInt() {
		//String s = System.getProperty("java.library.path");
		//System.err.println("java.library.path = "+s);
		//s = System.getProperty("user.dir");
		//System.err.println("user.dir = "+s);
		{
			Mzd m = new Mzd(10,10);
			assertNotEquals(null, m);
			assertEquals(10, m.getNcols());
			assertEquals(10, m.getNrows());
			assert(m.isZero());
			//m.destroy();
		}
	}

	@Test
	public void testMzdMzd() {
		{
			Mzd m1 = new Mzd(10,10);
			m1.randomize();
			Mzd m2 = new Mzd(m1);
			assertNotEquals(null, m2);
			assert(m1.equals(m2));
			m1.destroy();
			m2.destroy();
		}
	}

	@Test
	public void testMzdIntArrayArray() {
		{
			int[][] arr =
				{
					{0,1,0,0,0},
					{0,0,1,0,0},
					{0,0,0,0,0},
					{1,0,0,0,0},
					{0,0,0,0,1}
				};
			Mzd m = new Mzd(arr);
			assert(! m.isZero());
			assertEquals(5, m.getNcols());
			assertEquals(5, m.getNrows());
			assertEquals(1, m.readBit(0,1));
			assertEquals(1, m.readBit(3,0));
			assertEquals(0, m.readBit(1,1));
			assertEquals(0, m.readBit(0,3));
			m.destroy();
		}
	}

	@Test
	public void testDestroy() {
		{
			Mzd m = new Mzd(10,10);
			m.destroy();
			boolean exceptionThrown = false;
			try {
			//	Mzd m2 = new Mzd(m);
			} catch (NullPointerException e) {
				exceptionThrown = true;
			}
			assert(exceptionThrown);
		}
	}

	@Test
	public void testGetNrows() {
		{
			Mzd m = new Mzd(123,456);
			assertEquals(123, m.getNrows());
			m.destroy();
		}
	}

	@Test
	public void testGetNcols() {
		{
			Mzd m = new Mzd(123,456);
			assertEquals(456, m.getNcols());
			m.destroy();
		}
	}

	@Test
	public void testWriteBit() {
		{
			Mzd m = new Mzd(10,10);
			assert(m.isZero());
			m.writeBit(2, 5, 1);
			assertEquals(1, m.readBit(2, 5));
			//assertEquals(0, m.readBit(5, 2));
			m.destroy();
		}
	}

	@Test
	public void testReadBit() {
		{
			Mzd m = new Mzd(123,456);
			assert(m.isZero());
			m.writeBit(12, 45, 1);
			assertEquals(1, m.readBit(12, 45));
			assertEquals(0, m.readBit(45, 12));
			m.destroy();
		}
	}

	@Test
	public void testEchelonize() {
		if(false) {
			Mzd m = new Mzd(10,10);
			m.randomize();
			m.echelonize(false);
			m.print();
			m.echelonize(true);
			m.print();
			m.destroy();
		}
		
		{
			int[][] arr =
				{
					{0,1,0,0,0},
					{0,0,1,0,0},
					{0,0,0,0,0},
					{1,0,0,0,0},
					{0,0,0,0,1}
				};
			int[][] arre =
				{
					{1,0,0,0,0},
					{0,1,0,0,0},
					{0,0,1,0,0},
					{0,0,0,0,1},
					{0,0,0,0,0}
				};
			Mzd m = new Mzd(arr);
			Mzd me = new Mzd(arre);
			int rank = m.echelonize(true);
			assertEquals(4, rank);
			// to test non-full version could just test that there's nothing below the diagonal
			assert(m.equals(me));
			m.destroy();
			me.destroy();
		}
	}

	@Test
	public void testSolveLeft() {
		{
			Mzd a = new Mzd(30,30);
			Mzd.srandom(1);
			a.randomize();
			Mzd x = new Mzd(30,30);
			x.randomize();
			Mzd b = a.multiply(x);
			// actually if a and b aren't full rank the solution might not be unique
			Mzd xsol = Mzd.solveLeft(a, b, 0, true);
			assert(x.equals(xsol));
			a.destroy();
			b.destroy();
			x.destroy();
			xsol.destroy();
		}
	}

	@Test
	public void testKernelLeft() {
		{
			// make a matrix with a non-trivial non-obvious nullspace
			int dim = 30;
			Mzd m = new Mzd(dim,dim);
			Mzd.srandom(1);
			m.randomize();
			m.rowClearOffset(2, 0);
			m.rowClearOffset(4, 0);
			m.rowClearOffset(6, 0);
			Mzd m2 = new Mzd(dim,dim);
			m2.randomize();
			Mzd a = m.multiply(m2);
			Mzd x = a.kernelLeft(0);
			Mzd b = a.multiply(x);
			assert(b.isZero());
			int aRank = a.echelonize(false);
			int xRank = x.echelonize(false);
			assertEquals(dim, aRank+xRank);
			m.destroy();
			m2.destroy();
			a.destroy();
			x.destroy();
			b.destroy();
		}
	}

	@Test
	public void testRandomize() {
		{
			Mzd m = new Mzd(10,10);
			assert(m.isZero());
			m.randomize();
			assert(! m.isZero());
			m.destroy();
		}
	}

	@Test
	public void testInfo() {
		//TODO
		// this just prints some stuff to stdout.
		// no good way to test this
	}

	@Test
	public void testConcat() {
		{
			int[][] arr =
				{
					{0,1},
					{0,0},
					{0,0},
					{1,0},
					{0,0}
				};
			int[][] arrTest =
				{
					{0,1,0,1},
					{0,0,0,0},
					{0,0,0,0},
					{1,0,1,0},
					{0,0,0,0}
				};
			Mzd ma = new Mzd(arr);
			Mzd mb = new Mzd(arr);
			Mzd mTest = new Mzd(arrTest);
			Mzd mc = Mzd.concat(ma, mb);  //XXX- this crashes!
			//assertEquals(4, mc.getNcols());
			//assert(mTest.equals(mc));
			ma.destroy();
			mTest.destroy();
			//mc.destroy();
		}
	}

	@Test
	public void testStack() {
		{
			int[][] arr =
				{
					{0,1,0,0,0},
					{0,0,1,0,0}
				};
			int[][] arrTest =
				{
					{0,1,0,0,0},
					{0,0,1,0,0},
					{0,1,0,0,0},
					{0,0,1,0,0}
				};
			Mzd ma = new Mzd(arr);
			Mzd mTest = new Mzd(arrTest);
			Mzd mc = Mzd.stack(ma, ma);
			assertEquals(4, mc.getNrows());
			assert(mTest.equals(mc));
			ma.destroy();
			mTest.destroy();
			mc.destroy();
		}
	}

	@Test
	public void testIsZero() {
		{
			Mzd m = new Mzd(10,10);
			assert(m.isZero());
			m.writeBit(5, 5, 1);
			assert(! m.isZero());
			m.destroy();
		}
	}

	@Test
	public void testMultiply() {
		{
			int[][] arr1 =
				{
					{0,1,0,1,0},
					{0,0,1,0,0},
					{1,1,0,1,0},
					{1,0,1,0,0}
				};
			int[][] arr2 =
				{
					{1,0,1,1},
					{0,0,0,1},
					{0,1,0,0},
					{0,1,0,0},
					{1,0,1,0}
				};
			int[][] arr12 = 
				{
					{0,1,0,1},
					{0,1,0,0},
					{1,1,1,0},
					{1,1,1,1}
				};
			int[][] arr21 = 
				{
					{0,0,1,0,0},
					{1,0,1,0,0},
					{0,0,1,0,0},
					{0,0,1,0,0},
					{1,0,0,0,0}
				};

			Mzd m1 = new Mzd(arr1);
			Mzd m2 = new Mzd(arr2);
			Mzd m12 = new Mzd(arr12);
			Mzd m21 = new Mzd(arr21);
			Mzd b = m1.multiply(m2);
			assert(b.equals(m12));
			//b.print();
			b.destroy();
			b = m2.multiply(m1);
			assert(b.equals(m21));
			//b.print();
			b.destroy();
			m1.destroy();
			m2.destroy();
			m12.destroy();
			m21.destroy();
		}
	}

	@Test
	public void testAddMzd() {
		// this one behaves not like you'd expect.
		// calling m.add(n) you'd expect the result to modify m,
		// but in fact m is unchanged and it returns a new matrix.
		// maybe should change this.  But it would be hiding the
		// underlying behavior of m4ri.
		{
			int[][] arr =
				{
					{0,1,0,1,0},
					{0,0,1,0,0},
					{1,1,0,1,0},
					{1,0,1,0,0}
				};
			Mzd m = new Mzd(arr);
			Mzd ms = m.add(m);
			assert(ms.isZero());
			m.destroy();
			ms.destroy();
		}
	}

	@Test
	public void testAddMzdMzd() {
		{
			int[][] arr =
				{
					{0,1,0,1},
					{0,0,1,0},
					{1,1,0,1},
					{1,0,1,0}
				};
			int[][] arrTest =
				{
					{0,0,1,1},
					{1,0,1,0},
					{0,1,0,1},
					{1,0,1,0}
				};
			Mzd m = new Mzd(arr);
			Mzd mt = m.transpose();
			Mzd mTest = new Mzd(arrTest);
			assert(mt.equals(mTest));
			m.destroy();
			mt.destroy();
			mTest.destroy();
		}
	}
	
	@Test
	public void testTranspose() {
		{
			int[][] arr =
				{
					{0,1,0,1},
					{0,0,1,0},
					{1,1,0,1},
					{1,0,1,0}
				};
			int[][] arrTest =
				{
					{0,0,1,1},
					{1,0,1,0},
					{0,1,0,1},
					{1,0,1,0}
				};
			Mzd m = new Mzd(arr);
			Mzd ms = Mzd.add(m,m);
			assert(ms.isZero());
			m.destroy();
			ms.destroy();
		}
	}
	
	@Test
	public void testEqualsMzd() {
		{
			int[][] arr =
				{
					{0,1,0,1},
					{0,0,1,0},
					{1,1,0,1},
					{1,0,1,0}
				};
			Mzd m1 = new Mzd(arr);
			Mzd m2 = new Mzd(arr);
			assert(m1.equals(m2));
			assert(m2.equals(m1));
			m1.writeBit(0, 0, 1);
			assert(! m1.equals(m2));
			m1.destroy();
			m2.destroy();
		}
	}
	
	@Test
	public void testCopyRow() {
		{
			int[][] arr =
				{
					{0,1,0,1},
					{0,0,1,0},
					{1,1,0,1},
					{1,0,1,0}
				};
			int[][] arrTest =
				{
					{1,1,0,1},
					{1,0,1,0},
					{0,0,0,0},
					{0,0,0,0}
				};
			Mzd m1 = new Mzd(arr);
			Mzd m2 = new Mzd(4,4);
			Mzd.copyRow(m2, 0, m1, 2);
			Mzd.copyRow(m2, 1, m1, 3);
			Mzd mTest = new Mzd(arrTest);
			assert(m2.equals(mTest));
			m1.destroy();
			m2.destroy();
			mTest.destroy();
		}
	}
	
	@Test
	public void testCopyRows() {
		{
			int[][] arr =
				{
					{0,1,0,1},
					{0,0,1,0},
					{1,1,0,1},
					{1,0,1,0}
				};
			int[][] arrTest =
				{
					{1,1,0,1},
					{1,0,1,0},
					{0,0,0,0},
					{0,0,0,0}
				};
			Mzd m1 = new Mzd(arr);
			Mzd m2 = new Mzd(4,4);
			Mzd.copyRows(m2, 0, m1, 2, 2);
			Mzd mTest = new Mzd(arrTest);
			assert(m2.equals(mTest));
			m1.destroy();
			m2.destroy();
			mTest.destroy();
		}
	}
	
	@Test
	public void testStandardBasis() {
		int dim = 10;
		Mzd[] basis = Mzd.standardBasis(dim);
		for (int i=0; i<dim; i++) {
			assertEquals(1, basis[i].readBit(0, i));
			basis[i].writeBit(0, i, 0);
			assert(basis[i].isZero());
			basis[i].destroy();
		}
	}

	@Test
	public void testIdentityMatrix() {
		int dim = 10;
		Mzd m = Mzd.identityMatrix(dim);
		for (int i=0; i<dim; i++) {
			assertEquals(1, m.readBit(i, i));
			m.writeBit(i, i, 0);
		}
		assert(m.isZero());
		m.destroy();
	}

	@Test
	public void testSrandom() {
		{
			Mzd m1 = new Mzd(100,100);
			Mzd.srandom(1);
			m1.randomize();
			Mzd m2 = new Mzd(100,100);
			Mzd.srandom(1);
			m2.randomize();
			assert(m1.equals(m2));
			m1.destroy();
			m2.destroy();
		}
	}

	@Test
	public void testRowAdd() {
		{
			int[][] arr =
				{
					{0,1,0,1,0},
					{0,0,1,0,0},
					{1,1,0,1,0},
					{1,0,1,0,0}
				};
			int[][] arrTest =
				{
					{1,0,0,0,0},
					{0,0,1,0,0},
					{1,1,0,1,0},
					{1,0,1,0,0}
				};
			Mzd m = new Mzd(arr);
			m.rowAdd(2, 0);
			Mzd mTest = new Mzd(arrTest);
			assert(m.equals(mTest));
			m.destroy();
			mTest.destroy();
		}
	}

	@Test
	public void testRowClearOffset() {
		{
			int[][] arr =
				{
					{0,0,0,0,0},
					{0,0,1,1,1},
					{0,0,0,0,0},
					{0,0,0,0,0}
				};
			Mzd m = new Mzd(arr);
			m.rowClearOffset(1, 2);
			assert(m.isZero());
			m.destroy();
		}
	}

	@Test
	public void testSubmatrix() {
		int[][] arr =
			{
				{0,1,0,1,0},
				{0,0,1,0,0},
				{1,1,0,1,0},
				{1,0,1,0,0}
			};
		int[][] arrTest =
			{
				{1,0,1},
				{0,1,0}
			};
		Mzd m = new Mzd(arr);
		Mzd m2 = m.submatrix(null, 0, 1, 1, 4);
		Mzd mTest = new Mzd(arrTest);
		assert(m2.equals(mTest));
		m.destroy();
		m2.destroy();
		mTest.destroy();
	}

	@Test
	public void testVsIntersect() {
		{
			// create two random matrices of less than full rank
			Mzd A = new Mzd(20,100);
			Mzd.srandom(1);
			A.randomize();
			Mzd B = new Mzd(30,100);
			B.randomize();
			
			// compute a basis for the intersection of their rowspaces
			Mzd C = Mzd.vsIntersect(A, B);
			
			// test that the span of C is in the span of both A and B
			int aRank = A.echelonize(false);
			int bRank = B.echelonize(false);
			Mzd tmp = Mzd.stack(A, C);
			int tRank = tmp.echelonize(false);
			assertEquals(aRank,tRank);
			tmp.destroy();
			tmp = Mzd.stack(B, C);
			tRank = tmp.echelonize(false);
			assertEquals(bRank,tRank);
			tmp.destroy();
			A.destroy();
			B.destroy();
			C.destroy();
		}
	}

}
