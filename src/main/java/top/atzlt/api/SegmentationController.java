package top.atzlt.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import top.atzlt.domain.SegmentationDomain;
import top.atzlt.service.HMIService;

import javax.servlet.http.HttpServletRequest;

@RestController
public class SegmentationController extends BaseController {

    private HMIService hmiService;

    @Autowired
    public void setHmiService(HMIService hmiService) {
        this.hmiService = hmiService;
    }

    @RequestMapping(value = "/seg", method = RequestMethod.POST)
    public SegmentationDomain seg(HttpServletRequest request){
        SegmentationDomain segmentationDomain = (SegmentationDomain) getObjectFromRequest(request, SegmentationDomain.class);
        return hmiService.segAndDealText(segmentationDomain);
    }



}
