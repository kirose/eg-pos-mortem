package com.fimet.utils.concurrent;

import java.util.concurrent.atomic.AtomicLong;
import static java.lang.Double.doubleToLongBits;
import static java.lang.Double.longBitsToDouble;
/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public class AtomicDouble extends Number {

	private static final long serialVersionUID = 1L;
	private AtomicLong bits;

    public AtomicDouble() {
        this(0f);
    }

    public AtomicDouble(double initialValue) {
        bits = new AtomicLong(doubleToLongBits(initialValue));
    }

    public final boolean compareAndSet(double expect, double update) {
        return bits.compareAndSet(doubleToLongBits(expect),
                                  doubleToLongBits(update));
    }

    public final void set(double newValue) {
        bits.set(doubleToLongBits(newValue));
    }

    public final double get() {
        return longBitsToDouble(bits.get());
    }

    public double doubleValue() {
        return get();
    }

    public final double getAndSet(double newValue) {
        return longBitsToDouble(bits.getAndSet(doubleToLongBits(newValue)));
    }

    public final boolean weakCompareAndSet(double expect, double update) {
        return bits.weakCompareAndSet(doubleToLongBits(expect),
                                      doubleToLongBits(update));
    }

    public float floatValue() { return (float) doubleValue(); }
    public int intValue()       { return (int) get();           }
    public long longValue()     { return (long) get();          }

}
