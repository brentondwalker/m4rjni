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
		//TODO	
	}

	@Test
	public void testKernelLeft() {
		//TODO
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
		//TODO
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
					{0,1,0,1,0},
					{0,0,1,0,0},
					{1,1,0,1,0},
					{1,0,1,0,0}
				};
			Mzd m = new Mzd(arr);
			Mzd ms = Mzd.add(m,m);
			assert(ms.isZero());
			m.destroy();
			ms.destroy();
		}
	}

	@Test
	public void testTranspose() {
		//TODO
	}

	@Test
	public void testEqualsMzd() {
		//TODO
	}

	@Test
	public void testCopyRow() {
		//TODO
	}

	@Test
	public void testCopyRows() {
		//TODO
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
		//TODO
	}

}
