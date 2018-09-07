package excelsior.utils.database;

import com.mongodb.MongoClient;
import ecore.ECore;

public class MongoUtils {

    public static void writeCards(){
        if(ECore.INSTANCE.getMongo().isConnected()){
            MongoClient client = ECore.INSTANCE.getMongo().getClient();
        }
    }

    public static void readCards(){
        if(ECore.INSTANCE.getMongo().isConnected()){
            MongoClient client = ECore.INSTANCE.getMongo().getClient();
        }
    }
}
