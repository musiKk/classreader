package classreader;

import java.util.ArrayList;
import java.util.List;

public class Test {

	private final static int FOO = 5;

	public static void main(String[] args) throws Exception {

		List<Integer> foo = new ArrayList<Integer>();
		System.out.println(foo.size());

		// lookupswitch
		switch (FOO) {
		case 1:
			System.out.println("one");
			break;
		case 2:
			System.out.println("two");
			break;
		default:
			System.out.println("not one or two");
		}

		// tableswitch
		switch (FOO) {
		case 123:
			System.out.println("foo");
			break;
		case 6634:
			System.out.println("bar");
			break;
		default:
			System.out.println("baz");
		}

	}

}