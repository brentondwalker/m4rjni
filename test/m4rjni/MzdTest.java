package m4rjni;

import static org.junit.Assert.*;
import m4rjni.Mzd;

import org.junit.Test;

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
			// should really just test that there's nothing below the diagonal
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
		//TODO
	}

	@Test
	public void testAddMzdMzd() {
		//TODO
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
		//TODO
	}

	@Test
	public void testIdentityMatrix() {
		//TODO
	}

	@Test
	public void testSrandom() {
		//TODO
	}

	@Test
	public void testRowAdd() {
		//TODO
	}

	@Test
	public void testRowClearOffset() {
		//TODO
	}

	@Test
	public void testSubmatrix() {
		//TODO
	}

	@Test
	public void testVsIntersect() {
		//TODO
	}

}
