package Controller;

import Models.MedicalJournalToken;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SentimentAnalyzer {
    StanfordCoreNLP tokenizer;
    StanfordCoreNLP pipeline;

    public SentimentAnalyzer() {
        Properties pipelineProps = new Properties();
        Properties tokenizerProps = new Properties();
        pipelineProps.setProperty("annotators", "parse, sentiment");
        pipelineProps.setProperty("parse.binaryTrees", "true");
        pipelineProps.setProperty("enforceRequirements", "false");
        tokenizerProps.setProperty("annotators", "tokenize ssplit");
        tokenizer = new StanfordCoreNLP(tokenizerProps);
        pipeline = new StanfordCoreNLP(pipelineProps);
    }

    public void analyze(String line, String articleId) {
        Annotation annotation = tokenizer.process(line);
        pipeline.annotate(annotation);
        //String output = "";
        for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
            List<CoreLabel> tokens = sentence.get(CoreAnnotations.TokensAnnotation.class);
            //List<MedicalJournalToken> dbTokens = new ArrayList<>();
            List<Document> dbTokens = new ArrayList<>();
            for (CoreLabel token : tokens) {
                String tokenSentiment = token.get(SentimentCoreAnnotations.SentimentClass.class);
                //MedicalJournalToken mjt = new MedicalJournalToken(token.get(CoreAnnotations.TextAnnotation.class), token.get(CoreAnnotations.PartOfSpeechAnnotation.class), tokenSentiment);
                Document tokenDoc = new Document("text", token.get(CoreAnnotations.TextAnnotation.class))
                        .append("sentiment", tokenSentiment)
                        .append("pos", token.get(CoreAnnotations.PartOfSpeechAnnotation.class));
                dbTokens.add(tokenDoc);
                //System.out.println(tokenSentiment + " " + token.get(CoreAnnotations.TextAnnotation.class) + " " + token.get(CoreAnnotations.PartOfSpeechAnnotation.class));
            }
            String sentenceSentiment = sentence.get(SentimentCoreAnnotations.SentimentClass.class);

            Document setenceDoc = new Document("sentence", sentence.toString())
                    .append("sentiment", sentenceSentiment)
                    .append("articleId", articleId)
                    .append("tokens", dbTokens);

            MongoDBDriver.insertSentences(setenceDoc);
            System.out.println(articleId + ": " + sentence.toString());
            //output += "\n";
        }

    }
}