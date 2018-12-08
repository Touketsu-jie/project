package org.sang.controller.fee;

import org.sang.bean.Resource;
import org.sang.service.FeeRequestService;
import org.sang.service.PrjService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 请求管理
 * Created by Purvar on 2018/2/24.
 */
@RestController
@RequestMapping("/fee/request")
public class FeeBasicController {
    @Autowired
    PrjService prjService;
    @Autowired
    FeeRequestService feeRequestService;

    /**
     * 请求预定列表
     * @param page
     * @param size
     * @param keywords
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Map<String, Object> getFeeRequestByPage(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size,
    		@RequestParam(defaultValue = "") String keywords, @RequestParam(defaultValue = "") String month) {
        Map<String, Object> map = new HashMap<>();
        if(month != null && "null".equals(month)){
            month = null;
        }
        List<Resource> prjResources = feeRequestService.getFeeRequestByPage(page, size, keywords,month);
        Long count = feeRequestService.getCountByKeywords(keywords, month);
        map.put("prjResources", prjResources);
        map.put("count", count);
        return map;
    }

//    /**
//     * 详细资源列表
//     * @param resourceId
//     * @return
//     */
//    @RequestMapping(value = "/detailList", method = RequestMethod.GET)
//    public Map<String, Object> getResourceDetailByPage(@RequestParam(defaultValue = "") Integer resourceId) {
//        Map<String, Object> map = new HashMap<>();
//        List<Resource> resource = resEstService.getResourceByPage(null, null, null, null, resourceId);
//        if(resource != null && resource.size() > 0 ){
//            map.put("resource", resource.get(0));
//            List<ResourceDetail> detailResources = resEstService.getResourceDetailByPage(resourceId);
//            map.put("detailResources", detailResources);
//        }
//        return map;
//    }
}
