package excelsior.utils.database;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import ecore.ECore;
import excelsior.Excelsior;
import excelsior.game.match.Arena;
import excelsior.game.match.field.GridNormal;
import excelsior.game.match.gamemodes.Gamemode;
import excelsior.game.match.gamemodes.GamemodeDuel;
import org.bson.Document;
import org.bukkit.Material;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;

public class MongoUtils {

    private static final String COLLECTION_ARENAS = "arenas";

    /**
     * Arenas:
     * UUID id
     * String world
     * Gamemode (0 = duel)
     * Grid (vector start, int x, int z, int cellX, int cellZ)
     */

    public static void readArenas(){
        if(ECore.INSTANCE.getMongo().isConnected()){
            MongoDatabase database = ECore.INSTANCE.getMongo().getDatabase();
            MongoCollection<Document> arenas = database.getCollection(COLLECTION_ARENAS);
            arenas.find().forEach(new Block<Document>() {
                @Override
                public void apply(Document document) {
                    UUID id = UUID.fromString(document.getString("id"));
                    String world = document.getString("world");

                    Document gridDoc = (Document) document.get("grid");
                    String[] temp = gridDoc.getString("startPos").split(",");
                    Vector v = new Vector(Double.valueOf(temp[0]) + 1, Double.valueOf(temp[1]) + 1, Double.valueOf(temp[2]) + 1);
                    int gx = gridDoc.getInteger("gridX"), gz = gridDoc.getInteger("gridZ");
                    int cx = gridDoc.getInteger("cellX"), cz = gridDoc.getInteger("cellZ");
                    Material gridBorder = Material.valueOf(document.getString("gridBorder"));
                    Material cellMat = Material.valueOf(document.getString("cellMat"));
                    byte gridBorderData = Byte.valueOf(document.getString("gridBorderData"));
                    byte cellData = Byte.valueOf(document.getString("cellData"));
                    Excelsior.INSTANCE.getArenaManager().add(new Arena(new GridNormal(v, world, gx, gz, cx, cz, false,
                            gridBorder, gridBorderData, cellMat, cellData), world, id));
                }
            });
        }
    }

    public static void writeArenas(){
        if(ECore.INSTANCE.getMongo().isConnected()){
            List<Document> write = new ArrayList<>();

            MongoDatabase database = ECore.INSTANCE.getMongo().getDatabase();
            MongoCollection<Document> arenas = database.getCollection(COLLECTION_ARENAS);

            for(Arena arena: Excelsior.INSTANCE.getArenaManager().getObjects()){
                if(arenas.find(eq("id", arena.getID().toString())).first() != null){
                    continue;
                }

                String startPos = "";
                Vector startPosV = arena.getGrid().getStartPos();

                startPos += startPosV.getBlockX();
                startPos += ",";
                startPos += startPosV.getBlockY();
                startPos += ",";
                startPos += startPosV.getBlockZ();

                write.add(new Document("id", arena.getID().toString())
                        .append("world", arena.getWorld())
                        .append("grid", new Document("startPos", startPos)
                                    .append("gridX", arena.getGrid().getGridX())
                                    .append("gridZ", arena.getGrid().getGridZ())
                                    .append("cellX", arena.getGrid().getCellX())
                                    .append("cellZ", arena.getGrid().getCellZ())
                        )
                        .append("gridBorder", arena.getGrid().getGridBorder().toString())
                        .append("gridBorderData", String.valueOf(arena.getGrid().getGridBorderData()))
                        .append("cellMat", arena.getGrid().getCellMat().toString())
                        .append("cellData", String.valueOf(arena.getGrid().getCellData()))
                );
            }

            if(write.size() > 0){
                arenas.insertMany(write);
            }
        }
    }

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
