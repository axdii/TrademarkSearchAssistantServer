package top.atzlt.wordSeg.segmentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

//单词
public class Word implements Comparable{

    private static final Logger logger = LoggerFactory.getLogger(Word.class);
    private String text;//内容
    private PartOfSpeech partOfSpeech = null;//词性

    public Word(String text) {
        this.text = text;
    }

    public Word(String text, PartOfSpeech partOfSpeech) {
        this.text = text;
        this.partOfSpeech = partOfSpeech;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public PartOfSpeech getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(PartOfSpeech partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.text);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Word other = (Word) obj;
        return Objects.equals(this.text, other.text);
    }

    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        if(text!=null){
            str.append(text);
        }
        if (partOfSpeech != null) {
            str.append("(").append(partOfSpeech.getPos()).append(",").append(partOfSpeech.getDes()).append(")");
        }
        return str.toString();
    }

    @Override
    public int compareTo(Object o) {
        if(this == o){
            return 0;
        }
        if(this.text == null){
            return -1;
        }
        if(o == null){
            return 1;
        }
        if(!(o instanceof Word)){
            return 1;
        }
        String t = ((Word)o).getText();
        if(t == null){
            return 1;
        }
        return this.text.compareTo(t);
    }
}
