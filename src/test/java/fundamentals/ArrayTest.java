package fundamentals;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsArrayContainingInOrder.arrayContaining;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.object.IsCompatibleType.typeCompatibleWith;

/**
 * @author khanhpdt
 */
public class ArrayTest {

	@Test
	public void testArraysOfSameTypeHaveSameClassObject() {
		int[] intArray1 = new int[1];
		int[] intArray2 = new int[1];
		assertThat("arrays of same primitive type", intArray1.getClass() == intArray2.getClass());

		Integer[] integerArray1 = new Integer[1];
		Integer[] integerArray2 = new Integer[1];
		assertThat("arrays of same type", integerArray1.getClass() == integerArray2.getClass());
	}

	@Test
	public void testArraysOfDifferentTypesHaveDifferentClassObjects() {
		Integer[] integerArray = new Integer[1];
		Long[] longArray = new Long[1];
		assertThat("arrays of different type have different class objects",
				!integerArray.getClass().equals(longArray.getClass()));
	}

	@Test
	public void testCreateArraysWithNonReifiableTypes() {
		// compile error: could not init an array with non-reifiable types
//        List<Integer>[] array = new List<Integer>[] {Arrays.asList(1), Arrays.asList(2)};

		// a workaround to guarantee type-safe later
		@SuppressWarnings("unchecked")
		List<Integer>[] array = (List<Integer>[]) new List[]{
				new ArrayList<Integer>(Arrays.asList(1)),
				new ArrayList<Integer>(Arrays.asList(2))};

		// compile error
//        array[0].add("a_string");

		List<Integer> integerList = array[0];
		integerList.add(3);

		assertThat(array[0].get(1), instanceOf(Integer.class));
		assertThat(array[0].get(1), is(3));
	}

	@Test
	public void testVariousWaysToCreateArrays() {
		Integer[] a;
		a = new Integer[]{1, 2, 3, 4};
		assertThat(a, arrayContaining(1, 2, 3, 4));

		// shortcut to init arrays
		Integer[] b = {1, 2, 3, 4};
		assertThat(b, arrayContaining(1, 2, 3, 4));
	}

	@Test
	public void testLengthIsTheDeclaredLength() throws Exception {
		int[] c = new int[3];
		assertThat("length is the declared length of the array, not the number of elements that the array is holding",
				c.length, is(3));
	}

	@Test
	public void testCovariantTypeInArrays() throws Exception {
		Number[] numberArray = new Number[1];
		Integer[] integerArray = new Integer[1];

		assertThat(integerArray.getClass(), typeCompatibleWith(numberArray.getClass()));
	}
}
