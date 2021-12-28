package unsafe;

import parallel.util.ParallelUtil;
import sun.misc.Unsafe;

import java.io.Closeable;
import java.lang.reflect.Field;

public class UnsafeIntArray implements Closeable {
    private final static int SIZE_BYTES = Integer.BYTES;
    private final int size;
    private final Unsafe unsafe;
    private long address;


    public UnsafeIntArray(int size) {
        this.size = size;
        try {
            Field field = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            this.unsafe = (sun.misc.Unsafe) field.get(null);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
        address = unsafe.allocateMemory((long) this.size * SIZE_BYTES);

//        new ParallelUtil().parallelFor(size, i -> this.set(i, 0));
    }

    public void set(int i, int value) {
        unsafe.putInt(address + (long) i * SIZE_BYTES, value);
    }

    public int get(int i) {
        return unsafe.getInt(address + (long) i * SIZE_BYTES);
    }

    public int size() {
        return size;
    }

    public void close() {
        unsafe.freeMemory(address);
    }

}
