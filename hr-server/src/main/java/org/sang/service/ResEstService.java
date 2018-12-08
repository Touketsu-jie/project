package org.sang.service;

import org.sang.bean.Resource;
import org.sang.bean.ResourceDetail;
import org.sang.dto.ResourceDetailDto;
import org.sang.mapper.ResEstMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * 预计资源
 * Created by purvar on 2018/1/12.
 */
@Service
@Transactional
public class ResEstService {
    @Autowired
    ResEstMapper resEstMapper;
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
    SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
    SimpleDateFormat birthdayFormat = new SimpleDateFormat("yyyy-MM-dd");
    DecimalFormat decimalFormat = new DecimalFormat("##.00");

    public Long addRes(Resource resource) {
        Long reourceCnt = resEstMapper.addRes(resource);
        if(resource.getDetails() != null && resource.getDetails().size() > 0){
            for(ResourceDetailDto detailDto : resource.getDetails()){
                detailDto.setResourceId(resource.getResourceId());
                detailDto.setStartDate(detailDto.getDateRange()[0]);
                detailDto.setEndDate(detailDto.getDateRange()[1]);
            }
            resEstMapper.addResourceDetail(resource.getDetails());
        }
        return reourceCnt;
    }

    public List<Resource> getResourceByPage(Integer page, Integer size, String keywords,String month,Integer resourceId) {
        Integer start = null;
        if(page != null && size != null){
            start = (page - 1) * size;
        }

        return resEstMapper.getResourceByPage(start, size, keywords,month,resourceId);
    }

    public Long getCountByKeywords(String keywords,String month) {
        return resEstMapper.getCountByKeywords(keywords,month);
    }

    public int updateResource(Resource resource) {
        return resEstMapper.updateResource(resource);
    }

    public boolean deleteResById(String ids) {
        String[] split = ids.split(",");
        return resEstMapper.deleteResById(split) == split.length;
    }

//    public List<Project> getAllProjects() {
//        return prjMapper.getProjectByPage("", null, null, null, null, null, null, null, null);
//    }

    public List<Map<String, Object>> getEmployeeByName(String keywords) {
        return resEstMapper.selectEmployeeByName(keywords);
    }

    public List<ResourceDetail> getResourceDetailByPage(Integer resourceId) {
        return resEstMapper.getResourceDetailByPage(resourceId);
    }

}
