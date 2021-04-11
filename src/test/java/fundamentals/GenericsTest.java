package fundamentals;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author khanhpdt
 */
public class GenericsTest {

	@Test
	public void compareGenericsAndWithoutGenerics_compileTimeTypeChecking() {
		// without generics
		List strings = new ArrayList();
		strings.add("a"); // unchecked warning
		strings.add(1); // unchecked warning

		// with generics
		List<String> genericStrings = new ArrayList<>();
		genericStrings.add("a");
		// genericStrings.add(1); // compile error
	}

	@Test
	public void compareGenericsAndWithoutGenerics_unnecessaryCasts() {
		// without generics
		List strings = new ArrayList();
		strings.add("a");
		String s1 = (String) strings.get(0);

		// with generics
		List<String> genericStrings = new ArrayList<>();
		genericStrings.add("a");
		String s2 = genericStrings.get(0);
	}

	@Test
	public void testSubtypingWithWildcards() {
		List<Integer> ints = new ArrayList<>();
		List<? extends Number> numberInts = ints;

		List<Double> doubles = new ArrayList<>();
		List<? extends Number> numberDoubles = doubles;

		List<Number> numbers = new ArrayList<>();
		List<? super Integer> numbers2 = numbers;
		List<? super Double> numbers3 = numbers;

		List<?> any = ints;
		any = doubles;
		any = numbers;
	}

	private void method(Number[] numbers) {
	}

	private void method(List<Number> numbers) {
	}

	@Test
	public void genericsAreInvariant() {
		method(new Integer[]{1, 2, 3});
		method(new Double[]{1.3, 2.2, 3.1});
		// OK because arrays are covariant

		// with generics
		// method(new ArrayList<Integer>()); // compile error
		// method(new ArrayList<Double>());  // compile error
		// NOK because generic types are invariant
	}

}
