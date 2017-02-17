package src.p4_collectag;

/**
 * Created by Adrien on 16/02/2017.
 */

public class StaticEnvironment {
    public static MainActivity mainActivity;

    public static void setStaticEnvironment(MainActivity mainactivity) {
        mainActivity = mainactivity;
    }
}
