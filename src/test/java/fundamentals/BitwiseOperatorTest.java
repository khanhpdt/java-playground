package fundamentals;

import org.hamcrest.Matchers;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author khanhpdt
 */
public class BitwiseOperatorTest {

	@Test
	public void testOperandTypes() {
		byte r2 = 11 | 123;
		char result1 = 'a' & 'b';
		short r3 = 123 | 22312;
		int r4 = 123 | 223123123;
		long r5 = 123 | 22312312300000L;
	}

	@Test
	public void testCouldNotUseNotOperatorOnBoolean() {
		// System.out.println(~true); // compile error
		System.out.println(!true); // false
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNoShortCircuitOnBoolean() throws Exception {
		// short circuit with logical operators
		if (true || throwIllegalStateException()) {
		}

		// no short circuit with bitwise operators
		if (true | throwIllegalArgumentException()) {
		}
	}

	private boolean throwIllegalStateException() {
		throw new IllegalStateException();
	}

	private boolean throwIllegalArgumentException() {
		throw new IllegalArgumentException();
	}

	@Test
	public void testOperators() {
		assertThat(0b1010111 |
				   0b1000010,
				is(0b1010111));
		assertThat(0b1010111 &
				   0b1000010,
				is(0b1000010));
		assertThat(0b1010111 ^
				   0b1000010,
				is(0b0010101));
		assertThat(~ 0b1010111, is(0b11111111_11111111_11111111_10101000));
	}

	@Test
	public void testShiftOperators() {
		assertThat(0b1010111 << 3, is(0b1010111_000));

		assertThat(0b10101, Matchers.greaterThan(0));
		assertThat(0b10_101 >> 3, is(0b000_10));

		assertThat(0b11111111111111111111111110011011, Matchers.lessThan(0));

		// ">>" uses the sign bit (left most bit) to fill the trailing positions after shift.
		assertThat(0b11111111111111111111111110011_011 >> 3, is(0b111_11111111111111111111111110011));

		// ">>>" is unsigned right shift operator. It always fills 0 irrespective of the sign of the number.
		assertThat(0b11111111111111111111111110011_011 >>> 3, is(0b000_11111111111111111111111110011));
	}

	@Test
	public void testIdioms() {
		assertThat("clear all except bit ith from right to left (e.g., 2) (counting from 0)",
				0b11_1_11 & (1 << 2), is(0b00_1_00));
		assertThat("turn off bit ith from right to left (e.g., 2) (counting from 0)",
				0b11_1_11 & (~(1 << 2)), is(0b11_0_11));
		assertThat("turn on bit ith from right to left (e.g., 2) (counting from 0)",
				0b11_0_11 | (1 << 2), is(0b11_1_11));
		assertThat("set bit ith from right to left (e.g., 2) (counting from 0) to 0",
				(0b11_1_11 & (~(1 << 2))) | (0 << 2), is(0b11_0_11));
		assertThat("set bit ith from right to left (e.g., 2) (counting from 0) to 1",
				(0b11_0_11 & (~ (1 << 2))) | (1 << 2), is(0b11_1_11));
	}
}
