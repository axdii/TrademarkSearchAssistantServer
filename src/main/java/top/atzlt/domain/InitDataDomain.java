package top.atzlt.domain;

import java.util.List;

public class InitDataDomain extends BaseDomain<InitDataDomain> {

    private List<TrademarkListDomain> trademarkListDomainList;


    public List<TrademarkListDomain> getTrademarkListDomainList() {
        return trademarkListDomainList;
    }

    public void setTrademarkListDomainList(List<TrademarkListDomain> trademarkListDomainList) {
        this.trademarkListDomainList = trademarkListDomainList;
    }
}
