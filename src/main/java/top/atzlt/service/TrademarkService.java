package top.atzlt.service;

import top.atzlt.domain.*;

import java.util.List;

public interface TrademarkService {

    public InitDataDomain initData();
    public void initServerData();
    public TrademarkDomain getTrademarkDomainById(TrademarkDomain requestTrademarkDomain);
    public boolean addTrademarkTempDomain(TrademarkTempDomain trademarkTempDomain) throws Throwable;
    public List<TrademarkListDomain> getTrademarkListDomain();
    public TrademarkTempDomain getTrademarkTempDomainById(int trademarkId);
    public boolean deleteTrademarkDomainById(int trademarkId);
    public SimilarTrademarkEnquiriesDomain getSimilarTrademarkListByTextEnquiries(SimilarTrademarkEnquiriesDomain similarTrademarkEnquiriesDomain);
    public SimilarTrademarkEnquiriesDomain getSimilarTrademarkListByImageEnquiries(SimilarTrademarkEnquiriesDomain similarTrademarkEnquiriesDomain);

}
