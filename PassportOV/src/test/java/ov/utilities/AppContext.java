package ov.utilities;

public class AppContext {

    // Holds the active application name for the entire test run
    private static final String APP_NAME;

    static {
        // Read from JVM argument: -Dapp=passport or passportOS
        String app = System.getProperty("app");

        // Default safety net
        if (app == null || app.isEmpty()) {
            APP_NAME = "passportOS";
        } else {
            APP_NAME = app;
        }
    }

    /**
     * Returns the active application name.
     * Example: passport / passportOS
     */
    public static String getAppName() {
        return APP_NAME;
    }
}
