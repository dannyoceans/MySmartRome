package config;

/**
 * Created by francesconi on 01/03/2017.
 */

public class AppConfig {


        // global topic to receive app wide push notifications
        public static final String TOPIC_GLOBAL = "global";

        // broadcast receiver intent filters
        public static final String REGISTRATION_COMPLETE = "registrationComplete";
        public static final String PUSH_NOTIFICATION = "pushNotification";

        // id to handle the notification in the notification tray
        public static final int NOTIFICATION_ID = 100;
        public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

        public static final String SHARED_PREF = "ah_firebase";

    public static final String SERVER_ADDRESS="http://progettomagistrale.altervista.org/picture/";

    public static final String URL_GETNOME="http://progettomagistrale.altervista.org/nomecognomeutenti.php";
    public static final String URL_GETMSG ="http://progettomagistrale.altervista.org/getMsg.php";
    public static final String URL_READMSG ="http://progettomagistrale.altervista.org/leggimsg.php";
    public static final String URL_SENDMSG ="http://progettomagistrale.altervista.org/salvamsg2.php" ;
    // Server user login url
    public static String URL_LOGIN="http://progettomagistrale.altervista.org/richiesta.php" ;
    //Server update position url
    public static String URL_UPDATEPOSITION="http://progettomagistrale.altervista.org/modificaposizione.php";

    public static String URL_GETAMICI="http://progettomagistrale.altervista.org/selezionautenti.php";
    public static String URL_PUSHNOTIFY="http://progettomagistrale.altervista.org/push_notificationwithid.php";
    // Server user register url
    public static String URL_REGISTER="http://progettomagistrale.altervista.org/registrazione.php" ;
    public static String URL_PERSONAL="http://progettomagistrale.altervista.org/spostamenti.php" ;
    public static String URL_TRACKER="http://progettomagistrale.altervista.org/tracker.php";
    public static String ImageUrl="http://progettomagistrale.altervista.org/picture/";
}
