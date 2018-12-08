package org.sang.mapper;

import org.apache.ibatis.annotations.Param;
import org.sang.bean.Project;
import org.sang.bean.Resource;
import org.sang.bean.ResourceDetail;
import org.sang.dto.ResourceDetailDto;

import java.util.List;
import java.util.Map;

/**
 * Created by sang on 2018/1/12.
 */
public interface ResEstMapper {

    Long addRes(Resource resource);

    List<Resource> getResourceByPage(@Param("start") Integer start, @Param("size") Integer size, @Param("keywords") String keywords,@Param("month") String month,@Param("resourceId") Integer resourceId);

    Long getCountByKeywords(@Param("keywords") String keywords,@Param("month") String month);

    int updateResource(@Param("prj") Resource resource);

    int deleteResById(@Param("ids") String[] ids);

    int addResourceDetail(@Param("details") List<ResourceDetailDto> details);

    List<Project> getResourceByPageShort(@Param("start") int start, @Param("size") Integer size);
    
    List<Map<String, Object>> selectEmployeeByName(@Param("keywords") String keywords);

    List<ResourceDetail> getResourceDetailByPage(@Param("resourceId") Integer resourceId);
}
