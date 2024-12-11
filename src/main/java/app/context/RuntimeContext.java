package app.context;

public final class RuntimeContext {

    private static final ThreadLocal<ThreadLocalData> THREAD_LOCAL = new ThreadLocal<>();

    private RuntimeContext() {
        throw new AssertionError();
    }

    public static void set(final ThreadLocalData data) {
        THREAD_LOCAL.set(data);
    }

    public static ThreadLocalData get() {
        final ThreadLocalData data = THREAD_LOCAL.get();
        if (data == null) {
            return ThreadLocalData.builder().build();
        }
        return data;
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }
}
