package org.sang.controller.prj;

import org.sang.bean.Employee;
import org.sang.bean.Position;
import org.sang.bean.Project;
import org.sang.bean.RespBean;
import org.sang.common.EmailRunnable;
import org.sang.common.poi.PoiUtils;
import org.sang.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * 项目管理
 * Created by Purvar on 2018/2/24.
 */
@RestController
@RequestMapping("/project/basic")
public class PrjBasicController {
    @Autowired
    EmpService empService;
    @Autowired
    DepartmentService departmentService;
    @Autowired
    PositionService positionService;
    @Autowired
    JobLevelService jobLevelService;
    @Autowired
    ExecutorService executorService;
    @Autowired
    PrjService prjService;

    @RequestMapping(value = "/basicdata", method = RequestMethod.GET)
    public Map<String, Object> getAllNations() {
        Map<String, Object> map = new HashMap<>();
        map.put("deps", departmentService.getDepByPid(-1L));
        return map;
    }

    @RequestMapping("/maxWorkID")
    public String maxWorkID() {
        return String.format("%08d", empService.getMaxWorkID() + 1);
    }

    @RequestMapping(value = "/prj", method = RequestMethod.POST)
    public RespBean addPrj(Project project) {
        if (prjService.addPrj(project) == 1) {
            return RespBean.ok("添加成功!");
        }
        return RespBean.error("添加失败!");
    }

    @RequestMapping(value = "/del", method = RequestMethod.PUT)
    public RespBean updateEmp(Employee employee) {
        if (empService.updateEmp(employee) == 1) {
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }

    @RequestMapping(value = "/del/{ids}", method = RequestMethod.DELETE)
    public RespBean deleteProjectById(@PathVariable String ids) {
        if (prjService.deletePrjById(ids)) {
            return RespBean.ok("删除成功!");
        }
        return RespBean.error("删除失败!");
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Map<String, Object> getProjectByPage(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, @RequestParam(defaultValue = "") String keywords) {
        Map<String, Object> map = new HashMap<>();
        List<Project> projectByPage = prjService.getProjectByPage(page, size, keywords);
        Long count = prjService.getCountByKeywords(keywords);
        map.put("projects", projectByPage);
        map.put("count", count);
        return map;
    }

    @RequestMapping(value = "/exportEmp", method = RequestMethod.GET)
    public ResponseEntity<byte[]> exportEmp() {
        return PoiUtils.exportEmp2Excel(empService.getAllEmployees());
    }

    @RequestMapping(value = "/importEmp", method = RequestMethod.POST)
    public RespBean importEmp(MultipartFile file) {
        List<Employee> emps = PoiUtils.importEmp2List(file,empService.getAllNations(),empService.getAllPolitics(),departmentService.getAllDeps(),positionService.getAllPos(),jobLevelService.getAllJobLevels());
        if (empService.addEmps(emps) == emps.size()) {
            return RespBean.ok("导入成功!");
        }
        return RespBean.error("导入失败!");
    }
}
