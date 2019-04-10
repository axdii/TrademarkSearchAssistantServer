package top.atzlt.service;

import top.atzlt.domain.SegmentationDomain;

public interface HMIService {

    public SegmentationDomain segAndDealText(SegmentationDomain segmentationDomain);
    public String segText(String rawText);

}
