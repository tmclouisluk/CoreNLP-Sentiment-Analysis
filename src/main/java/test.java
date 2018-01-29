import edu.stanford.nlp.coref.CorefCoreAnnotations;
import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import org.ejml.simple.SimpleMatrix;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class test {
    public static void main(String[] args) throws IOException {
        // set up optional output files
        String line = "Great item! HDMI and decent wifi required as with all streaming devices.\n" +
                "The flow on the homepage is very good and responsive. Watching a series is a doddle, flow is great, no action required.\n" +
                "The remote and controller app both work a treat.\n" +
                "I really like this device.\n" +
                "I'd like to see an Amazon-written mirroring app available for non-Amazon products but no-one likes talking to each other in this field!";

        Long textLength = 0L;
        int sumOfValues = 0;

        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        int mainSentiment = 0;
        if (line != null && line.length() > 0) {
            int longest = 0;
            Annotation annotation = pipeline.process(line);
            for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
                List<CoreLabel> tokens = sentence.get(CoreAnnotations.TokensAnnotation.class);
                for (CoreLabel token : tokens) {
                    String as = token.get(SentimentCoreAnnotations.SentimentClass.class);
                    System.out.println(as + " " + token.get(CoreAnnotations.TextAnnotation.class) + " " + token.get(CoreAnnotations.PartOfSpeechAnnotation.class));
                }
                Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
                SimpleMatrix sentimentMatrix = RNNCoreAnnotations.getPredictions(tree);
                int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
                String partText = sentence.toString();
                if (partText.length() > longest) {
                    textLength += partText.length();
                    sumOfValues = sumOfValues + sentiment * partText.length();

                    System.out.println(sentiment + " " + partText);
                }
            }
        }

        System.out.println("Overall: " + (double)sumOfValues/textLength);
    }

}
