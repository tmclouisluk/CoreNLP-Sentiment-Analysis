import Controller.MongoDBDriver;
import Controller.SentimentAnalyzer;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.regex.Pattern;

public class main {
    public static void main( String[] args )
    {
        //Get data
        //MongoDBDriver mdbb = new MongoDBDriver();
        List<Document> dataList = MongoDBDriver.getMedicalJournalData();
        /*
        int count = 0;
        for(int i=0; i<dataList.size(); i++){
            String id = dataList.get(i).getObjectId("_id").toString();
            String id2 = "5a663a19967e4f250c52372d";
            if(id.equals(id2)){
                count = i;
            }
        }
        dataList.

        count = 1;
        */
        //String line = "Amazingly grateful beautiful friends are fulfilling an incredibly joyful accomplishment. What an truly terrible idea.";

        SentimentAnalyzer sa = new SentimentAnalyzer();
        for (int i=1803; i<dataList.size(); i++){
            Document data = dataList.get(i);
            String content = data.get("content").toString();
            String articleId = data.get("_id").toString();
            String[] sentences = content.split(Pattern.quote(". "));
            for (String sentence : sentences){
                sa.analyze(sentence, articleId);
                //System.out.println(sentence + " " + output);
            }

        }


    }
}
