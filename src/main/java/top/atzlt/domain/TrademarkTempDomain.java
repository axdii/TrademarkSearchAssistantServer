package top.atzlt.domain;


import org.springframework.web.multipart.MultipartFile;

//用于商标添加和删除
public class TrademarkTempDomain {

    private int trademarkId;//商标id
    private String trademarkName;//商标名称
    private int trademarkType;//国际分类
    private String similarId;//类似群
    private String briefDescription;//简要描述
    private String timeOfApplication;//注册时间
    private String registeredAddress;//注册地址
    private String petitioner;//申请人
    private String useTime;//专用权期限
    private String color;//颜色
    private String shape;//形状

    private MultipartFile trademarkImg;//原图
    private MultipartFile trademarkTbImg;//缩略图


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

    public String getSimilarId() {
        return similarId;
    }

    public void setSimilarId(String similarId) {
        this.similarId = similarId;
    }

    public String getBriefDescription() {
        return briefDescription;
    }

    public void setBriefDescription(String briefDescription) {
        this.briefDescription = briefDescription;
    }

    public String getTimeOfApplication() {
        return timeOfApplication;
    }

    public void setTimeOfApplication(String timeOfApplication) {
        this.timeOfApplication = timeOfApplication;
    }

    public String getRegisteredAddress() {
        return registeredAddress;
    }

    public void setRegisteredAddress(String registeredAddress) {
        this.registeredAddress = registeredAddress;
    }

    public String getPetitioner() {
        return petitioner;
    }

    public void setPetitioner(String petitioner) {
        this.petitioner = petitioner;
    }

    public String getUseTime() {
        return useTime;
    }

    public void setUseTime(String useTime) {
        this.useTime = useTime;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public MultipartFile getTrademarkImg() {
        return trademarkImg;
    }

    public void setTrademarkImg(MultipartFile trademarkImg) {
        this.trademarkImg = trademarkImg;
    }

    public MultipartFile getTrademarkTbImg() {
        return trademarkTbImg;
    }

    public void setTrademarkTbImg(MultipartFile trademarkTbImg) {
        this.trademarkTbImg = trademarkTbImg;
    }
}
