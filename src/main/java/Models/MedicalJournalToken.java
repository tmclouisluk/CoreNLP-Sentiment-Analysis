package Models;

public class MedicalJournalToken {
    String text;
    String pos;
    String sentiment;

    public MedicalJournalToken(String text, String pos, String sentiment ){
        this.text = text;
        this.pos = pos;
        this.sentiment = sentiment;
    }

    public String getPos() {
        return pos;
    }

    public String getSentiment() {
        return sentiment;
    }

    public String getText() {
        return text;
    }
}
