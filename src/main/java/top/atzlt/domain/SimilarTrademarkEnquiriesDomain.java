package top.atzlt.domain;

import java.util.List;

public class SimilarTrademarkEnquiriesDomain extends BaseDomain<SimilarTrademarkEnquiriesDomain>{

    private int trademarkId;//商标编号，也代表图形编号
    private String trademarkName;//使用文本查询时的商标名称
    private int trademarkType;//国际类型
    private String enquiriesType;//查询方式
    private List<Integer> returnTrademarkIds;//返回的结果

    public int getTrademarkId() {
        return trademarkId;
    }

    public void setTrademarkId(int trademarkId) {
        this.trademarkId = trademarkId;
    }

    public String getTrademarkName() {
        return trademarkName;
    }

    public void setTrademarkName(String trademarkName) {
        this.trademarkName = trademarkName;
    }

    public int getTrademarkType() {
        return trademarkType;
    }

    public void setTrademarkType(int trademarkType) {
        this.trademarkType = trademarkType;
    }

    public List<Integer> getReturnTrademarkIds() {
        return returnTrademarkIds;
    }

    public void setReturnTrademarkIds(List<Integer> returnTrademarkIds) {
        this.returnTrademarkIds = returnTrademarkIds;
    }

    public String getEnquiriesType() {
        return enquiriesType;
    }

    public void setEnquiriesType(String enquiriesType) {
        this.enquiriesType = enquiriesType;
    }
}
