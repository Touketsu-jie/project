package org.sang.service;

import org.sang.bean.Employee;
import org.sang.bean.Nation;
import org.sang.bean.PoliticsStatus;
import org.sang.bean.Project;
import org.sang.mapper.EmpMapper;
import org.sang.mapper.PrjMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by sang on 2018/1/12.
 */
@Service
@Transactional
public class PrjService {
    @Autowired
    PrjMapper prjMapper;
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
    SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
    SimpleDateFormat birthdayFormat = new SimpleDateFormat("yyyy-MM-dd");
    DecimalFormat decimalFormat = new DecimalFormat("##.00");

    public int addPrj(Project project) {
        return prjMapper.addPrj(project);
    }

    public List<Project> getProjectByPage(Integer page, Integer size, String keywords) {
        int start = (page - 1) * size;
        return prjMapper.getProjectByPage(start, size, keywords);
    }

    public Long getCountByKeywords(String keywords) {
        return prjMapper.getCountByKeywords(keywords);
    }

    public int updatePrj(Project project) {
        return prjMapper.updateProject(project);
    }

    public boolean deletePrjById(String ids) {
        String[] split = ids.split(",");
        return prjMapper.deletePrjById(split) == split.length;
    }

//    public List<Project> getAllProjects() {
//        return prjMapper.getProjectByPage("", null, null, null, null, null, null, null, null);
//    }

    public int addProjects(List<Project> prjs) {
        return prjMapper.addPrjs(prjs);
    }

    public List<Project> getProjectByPageShort(Integer page, Integer size) {
        int start = (page - 1) * size;
        return prjMapper.getProjectByPageShort(start,size);
    }
}
