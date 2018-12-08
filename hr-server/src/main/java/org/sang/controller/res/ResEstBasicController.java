package org.sang.controller.res;

import org.sang.bean.Resource;
import org.sang.bean.ResourceDetail;
import org.sang.bean.RespBean;
import org.sang.service.PrjService;
import org.sang.service.ResEstService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 预计资源管理
 * Created by Purvar on 2018/2/24.
 */
@RestController
@RequestMapping("/resource/est")
public class ResEstBasicController {
    @Autowired
    PrjService prjService;
    @Autowired
    ResEstService resEstService;

    @RequestMapping(value = "/basicdata", method = RequestMethod.GET)
    public Map<String, Object> getAllProjects() {
        Map<String, Object> map = new HashMap<>();
        map.put("projects", prjService.getProjectByPage(1, 1000, null));
        map.put("employees", resEstService.getEmployeeByName(null));
        return map;
    }
    
    /**
     * 添加资源
     * @param resource
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public RespBean addRes(@RequestBody Resource resource) {
        if (resEstService.addRes(resource) == 1) {
            return RespBean.ok("添加成功!");
        }
        return RespBean.error("添加失败!");
    }

    /**
     * 更新资源
     * @param resource
     * @return
     */
    @RequestMapping(value = "/del", method = RequestMethod.PUT)
    public RespBean updateRes(Resource resource) {
        if (resEstService.updateResource(resource) == 1) {
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }

    /**
     * 删除资源
     * @param ids
     * @return
     */
    @RequestMapping(value = "/del/{ids}", method = RequestMethod.DELETE)
    public RespBean deleteResById(@PathVariable String ids) {
        if (resEstService.deleteResById(ids)) {
            return RespBean.ok("删除成功!");
        }
        return RespBean.error("删除失败!");
    }

    /**
     * 资源列表
     * @param page
     * @param size
     * @param keywords
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Map<String, Object> getResourceByPage(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, 
    		@RequestParam(defaultValue = "") String keywords, @RequestParam(defaultValue = "") String month) {
        Map<String, Object> map = new HashMap<>();
        List<Resource> projectByPage = resEstService.getResourceByPage(page, size, keywords,month,null);
        Long count = resEstService.getCountByKeywords(keywords,month);
        map.put("resources", projectByPage);
        map.put("count", count);
        return map;
    }
    
    /**
     * 查找人员
     * @param keywords
     * @return
     */
    @RequestMapping(value = "/emp", method = RequestMethod.GET)
    public Map<String, Object> getEmployeeByName(@RequestParam(defaultValue = "") String keywords) {
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> resources = resEstService.getEmployeeByName(keywords);
        map.put("resources", resources);
        return map;
    }

    /**
     * 详细资源列表
     * @param resourceId
     * @return
     */
    @RequestMapping(value = "/detailList", method = RequestMethod.GET)
    public Map<String, Object> getResourceDetailByPage(@RequestParam(defaultValue = "") Integer resourceId) {
        Map<String, Object> map = new HashMap<>();
        List<Resource> resource = resEstService.getResourceByPage(null, null, null, null, resourceId);
        if(resource != null && resource.size() > 0 ){
            map.put("resource", resource.get(0));
            List<ResourceDetail> detailResources = resEstService.getResourceDetailByPage(resourceId);
            map.put("detailResources", detailResources);
        }
        return map;
    }
}
