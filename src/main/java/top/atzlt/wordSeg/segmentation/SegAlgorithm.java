package top.atzlt.wordSeg.segmentation;

public enum  SegAlgorithm {

    ReverseMaximumMatching("逆向最大匹配算法"),

    MaximumMatching("正向最大匹配算法"),

    MinimumMatching("正向最小匹配算法"),

    ReverseMinimumMatching("逆向最小匹配算法"),

    BidirectionalMaximumMatching("双向最大匹配算法");

//    BidirectionalMinimumMatching("双向最小匹配算法"),

//    BidirectionalMaximumMinimumMatching("双向最大最小匹配算法"),

//    FullSegmentation("全切分算法"),

//    MaxNgramScore("最大Ngram分值算法");

    private SegAlgorithm(String des){
        this.des = des;
    }

    private final String des;

    public String getDes() {
        return des;
    }


    public enum ReverseMinimumMatching {}
}
