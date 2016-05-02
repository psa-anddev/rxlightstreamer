package rx.plugins;

/**
 * <p>This class allows to reset the RxJava plugins in the test environment.</p>
 */
public class RxJavaTestPlugins extends RxJavaPlugins {
    /**
     * <p>Reset the Rx Java plugins in the test environment.</p>
     */
    public static void resetPlugins()
    {
        RxJavaPlugins.getInstance().reset();
    }
}
