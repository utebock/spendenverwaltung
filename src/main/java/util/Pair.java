package util;

/**
 * Simple implementation of a different-type pair
 * 
 * @author manuel-bichler
 * 
 * @param <A>
 * @param <B>
 */
public class Pair<A, B> {
	public A a;
	public B b;

	public Pair(A a, B b) {
		this.a = a;
		this.b = b;
	}
}
