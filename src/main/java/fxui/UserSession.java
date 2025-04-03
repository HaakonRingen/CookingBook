package fxui;

/**
 * Singleton class to manage user sessions.
 */
public class UserSession {
    
    private static UserSession instance;

    private int id;

    private UserSession(int id) {
        this.id = id;
    }

    /**
     * Returns the singleton instance of UserSession.
     * If the instance does not exist, it creates one with the given id.
     *
     * @param id the id to initialize the UserSession
     * @return the singleton instance of UserSession
     */
    public static synchronized UserSession getInstance(int id) {
        if (instance == null) {
            instance = new UserSession(id);
        }
        return instance;
    }

    public int getId() {
        return id;
    }

    public static void logOff() {
        instance = null;
    }

    /**
     * Returns the current instance of UserSession without creating a new one.
     *
     * @return the current UserSession instance, or null if none exists
     */
    public static synchronized UserSession getCurrentInstance() {
        return instance;
    }

}
