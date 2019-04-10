package top.atzlt.dao;

import top.atzlt.domain.TrademarkDomain;
import top.atzlt.domain.TrademarkListDomain;
import top.atzlt.domain.TrademarkTempDomain;

import java.util.List;

public interface TrademarkDao {

    public List<TrademarkListDomain> getTrademarkLists();
    public List<TrademarkDomain> getTrademarkDomains();
    public TrademarkDomain getTrademarkDomain(int trademarkId);
    public boolean addTrademarkListDomain(TrademarkListDomain trademarkListDomain);
    public boolean addTrademarkDomain(TrademarkDomain trademarkDomain);
    public boolean addTrademarkTempDomain(TrademarkTempDomain trademarkTempDomain);
    public int getLastTrademarkListId();
    public int getLastTrademarkDomainId();
    public boolean deleteTrademarkDomainById(int trademarkId);
    public boolean deleteTrademarkListDomainById(int trademarkId);
}
