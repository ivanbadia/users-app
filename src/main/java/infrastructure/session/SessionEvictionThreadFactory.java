package infrastructure.session;


import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

class SessionEvictionThreadFactory implements ThreadFactory {

    private static final ThreadFactory THREAD_FACTORY = Executors.defaultThreadFactory();

    @Override
    public Thread newThread(Runnable r) {
        final Thread thread = THREAD_FACTORY.newThread(r);
        thread.setDaemon(true);
        thread.setName("Session eviction thread");
        return thread;
    }
}
