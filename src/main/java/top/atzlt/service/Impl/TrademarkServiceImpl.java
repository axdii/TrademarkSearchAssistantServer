package top.atzlt.service.Impl;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.atzlt.cons.CommonConstant;
import top.atzlt.dao.TrademarkDao;
import top.atzlt.domain.*;
import top.atzlt.service.TrademarkService;
import top.atzlt.similarity.impl.CNNImg;
import top.atzlt.similarity.impl.ImgSimilarity;
import top.atzlt.similarity.impl.TextSimilarity;
import top.atzlt.tmp.TrademarkData;
import top.atzlt.web.DirManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class TrademarkServiceImpl implements TrademarkService {

    private static final Logger logger = LoggerFactory.getLogger(TrademarkServiceImpl.class);

    private TrademarkDao trademarkDao;

    @Autowired
    public void setTrademarkDao(TrademarkDao trademarkDao) {
        this.trademarkDao = trademarkDao;
    }


    public InitDataDomain initData() {
        InitDataDomain initDataDomain = new InitDataDomain();
        List<TrademarkListDomain> trademarkListDomains = new ArrayList<TrademarkListDomain>();
        trademarkListDomains = trademarkDao.getTrademarkLists();
        initDataDomain.setTrademarkListDomainList(trademarkListDomains);
        if (trademarkListDomains.size() == 0) {
            initDataDomain.setStatusCode(CommonConstant.GAIN_INIT_DATA_FAULT);
        } else {
            initDataDomain.setStatusCode(CommonConstant.OPERATE_SUCCESS);
        }
        return initDataDomain;
    }

    @Override
    public void initServerData() {

    }


    public TrademarkDomain getTrademarkDomainById(TrademarkDomain requestTrademarkDomain) {
        int trademarkId = requestTrademarkDomain.getTrademarkId();
        TrademarkDomain trademarkDomain = trademarkDao.getTrademarkDomain(trademarkId);
        if (trademarkDomain == null) {
            trademarkDomain = requestTrademarkDomain;
            trademarkDomain.setStatusCode(CommonConstant.GAIN_TRADEMARK_INFO_FAULT);
        } else {
            trademarkDomain.setStatusCode(CommonConstant.OPERATE_SUCCESS);
        }

        return trademarkDomain;
    }


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public boolean addTrademarkTempDomain(TrademarkTempDomain trademarkTempDomain) throws Throwable {

        int trademarkId = trademarkDao.getLastTrademarkListId() + 1;

        trademarkTempDomain.setTrademarkId(trademarkId);
        boolean sign1 = trademarkDao.addTrademarkTempDomain(trademarkTempDomain);

        TrademarkListDomain trademarkListDomain = new TrademarkListDomain();
        trademarkListDomain.setTrademarkId(trademarkId);
        trademarkListDomain.setTrademarkName(trademarkTempDomain.getTrademarkName());

        boolean sign2 = trademarkDao.addTrademarkListDomain(trademarkListDomain);

        if (!sign1 || !sign2) {
            throw new Throwable("插入数据到数据库时出错");
        }

        //存放原图
        String trademarkImgPath = DirManager.getDirPath(CommonConstant.TRADEMARK_IMG_DIR);
        String trademarkImgFile = trademarkImgPath + "/" + trademarkTempDomain.getTrademarkId() + ".JPG";
        trademarkTempDomain.getTrademarkImg().transferTo(new File(trademarkImgFile));

        //存放缩略图
        String trademarkTbImgPath = DirManager.getDirPath(CommonConstant.TRADEMARK_TB_IMG_DIR);
        String trademarkTbImgFile = trademarkTbImgPath + "/tb" + trademarkTempDomain.getTrademarkId() + ".JPG";
        trademarkTempDomain.getTrademarkTbImg().transferTo(new File(trademarkTbImgFile));

        return true;
    }

    @Override
    public List<TrademarkListDomain> getTrademarkListDomain() {
        return trademarkDao.getTrademarkLists();
    }

    @Override
    public TrademarkTempDomain getTrademarkTempDomainById(int trademarkId) {
        TrademarkTempDomain trademarkTempDomain = new TrademarkTempDomain();


        return null;
    }

    @Override
    public boolean deleteTrademarkDomainById(int trademarkId) {
        return trademarkDao.deleteTrademarkDomainById(trademarkId)&&trademarkDao.deleteTrademarkListDomainById(trademarkId);
    }

    @Override
    public SimilarTrademarkEnquiriesDomain getSimilarTrademarkListByTextEnquiries(SimilarTrademarkEnquiriesDomain similarTrademarkEnquiriesDomain) {
        String trademarkName = similarTrademarkEnquiriesDomain.getTrademarkName();

        int trademarkType = similarTrademarkEnquiriesDomain.getTrademarkType();
        List<TrademarkListDomain> trademarkListDomains = trademarkDao.getTrademarkLists();
        List<Integer> trademarkIds = new ArrayList<>();

        long startTime = System.currentTimeMillis();

        for (TrademarkListDomain trademarkListDomain : trademarkListDomains) {

            if (trademarkType != -1 && trademarkListDomain.getTrademarkType() != trademarkType) {
                continue;
            }

            if (trademarkListDomain.getTrademarkName().equals(trademarkName)) {
                trademarkIds.add(0, trademarkListDomain.getTrademarkId());
                continue;
            }

            double sim = TextSimilarity.staticCalSimilarityValues(trademarkName, trademarkListDomain.getTrademarkName());
            logger.info(trademarkName + " " + trademarkListDomain.getTrademarkName() + "：" + sim);

            //如果相似度大于50%，视为相似商标
            if (sim > 0.5) {
                trademarkIds.add(trademarkListDomain.getTrademarkId());
            }

        }

        long endTime = System.currentTimeMillis();
        logger.info("计算一次文本相似所花费的时间：" + ((endTime - startTime)/1000) + "秒。");


        similarTrademarkEnquiriesDomain.setReturnTrademarkIds(trademarkIds);
        similarTrademarkEnquiriesDomain.setStatusCode(CommonConstant.OPERATE_SUCCESS);
        return similarTrademarkEnquiriesDomain;
    }

    @Override
    public SimilarTrademarkEnquiriesDomain getSimilarTrademarkListByImageEnquiries(SimilarTrademarkEnquiriesDomain similarTrademarkEnquiriesDomain) {

        int trademarkId = similarTrademarkEnquiriesDomain.getTrademarkId();
        int trademarkType = similarTrademarkEnquiriesDomain.getTrademarkType();
        List<TrademarkListDomain> trademarkListDomains = trademarkDao.getTrademarkLists();
        List<Integer> trademarkIds = new ArrayList<>();

        long startTime = System.currentTimeMillis();

        for (TrademarkListDomain trademarkListDomain : trademarkListDomains) {

            //排除相同图片和非同一国际类型的情况
            if (trademarkListDomain.getTrademarkType() != trademarkType || trademarkListDomain.getTrademarkId() == trademarkId) {
                continue;
            }

            String trademarkImgPath = DirManager.getDirPath(CommonConstant.TRADEMARK_TB_IMG_DIR) + "/tb" + trademarkId + ".JPG";
            String comparisonTrademarkImgPath = DirManager.getDirPath(CommonConstant.TRADEMARK_TB_IMG_DIR) + "/tb" + trademarkListDomain.getTrademarkId() + ".JPG";

            try {
                ImgSimilarity imgSim = new ImgSimilarity(trademarkImgPath, comparisonTrademarkImgPath);
                CNNImg cnnImg = new CNNImg(trademarkImgPath, comparisonTrademarkImgPath);

                double sim1 = imgSim.caculateSimilarity();
                double sim2 = cnnImg.caculateSimilarity();
//              cnnImg.getBinarySimilarity();

//                double sim = (sim1 + sim2) / 2;
                logger.info(trademarkId + " " + trademarkListDomain.getTrademarkId() + "：sim1=" + sim1 + ", sim2=" + sim2);
                //相似度大于60%的话视为相似
                if (sim1 >= 0.5 || sim2 >= 0.5) {
                    trademarkIds.add(trademarkListDomain.getTrademarkId());
                }
            } catch (IOException e) {
//                e.printStackTrace();
                logger.debug("判断相似出现问题，图片1ID：" + trademarkId + ", 图片2ID：" + trademarkListDomain.getTrademarkId(), e);

            }
        }

        trademarkIds.add(0, trademarkId);

        long endTime = System.currentTimeMillis();
        logger.info("计算一次图片相似所花费的时间：" + ((endTime - startTime)/1000) + "秒。");

        similarTrademarkEnquiriesDomain.setReturnTrademarkIds(trademarkIds);
        similarTrademarkEnquiriesDomain.setStatusCode(CommonConstant.OPERATE_SUCCESS);
        return similarTrademarkEnquiriesDomain;
    }

    @Test
    public void test(){
        System.out.println(DirManager.getDirPath(CommonConstant.TRADEMARK_TB_IMG_DIR) + "/tb" + 1 + ".JPG");
    }


}
