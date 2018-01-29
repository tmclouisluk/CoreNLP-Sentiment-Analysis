package Controller;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class MongoDBDriver {
    private static MongoClientURI connectionString = new MongoClientURI("mongodb://localhost:27017");
    private static MongoClient mongoClient = new MongoClient(connectionString);

    public static List<org.bson.Document> getMedicalJournalData(){
        MongoDatabase database = mongoClient.getDatabase("local");
        MongoCollection<org.bson.Document> collection = database.getCollection("medical_journal");
        MongoCursor<org.bson.Document> cursor  = collection.find().iterator();
        List<org.bson.Document> medicalJournalList = new ArrayList<>();
        try {
            while (cursor.hasNext()) {
                medicalJournalList.add(cursor.next());
            }
        } finally {
            cursor.close();
        }
        return medicalJournalList;
    }

    public static void insertSentences(Document sentence){
        MongoDatabase database = mongoClient.getDatabase("local");
        MongoCollection<org.bson.Document> collection = database.getCollection("sentences");

        collection.insertOne(sentence);
    }
}
