package top.atzlt.wordSeg.segmentation;

import java.util.List;

public interface Segmentation {

    public List<Word> seg(String text);

    public SegAlgorithm getSegAlgorithm();

}
