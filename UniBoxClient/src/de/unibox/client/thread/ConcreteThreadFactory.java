package de.unibox.client.thread;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ThreadFactory;

import de.unibox.client.thread.implementation.ThreadFactoryImpl;

/**
 * A factory for creating ConcreteThread objects.
 */
public class ConcreteThreadFactory extends ThreadFactoryImpl {

    /**
     * Instantiates a new concrete thread factory.
     */
    public ConcreteThreadFactory() {
        this.setNamePrefix(ConcreteThreadFactory.class.getSimpleName());
        this.setDaemon(true).setPriority(Thread.NORM_PRIORITY);
        this.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(final Thread t, final Throwable e) {
                System.err.println(String.format(
                        "Thread %s threw exception - %s", t.getName(),
                        e.getMessage()));
                e.printStackTrace();
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.unibox.client.thread.implementation.ThreadFactoryImpl#build()
     */
    @Override
    public ThreadFactory build() {
        return super.build(this);
    }

}
