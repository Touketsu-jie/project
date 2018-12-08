package org.sang.mapper;

import org.apache.ibatis.annotations.Param;
import org.sang.bean.Employee;
import org.sang.bean.Nation;
import org.sang.bean.PoliticsStatus;
import org.sang.bean.Project;

import java.util.Date;
import java.util.List;

/**
 * Created by sang on 2018/1/12.
 */
public interface PrjMapper {

    int addPrj(Project project);

    Long getMaxPrjID();

    List<Project> getProjectByPage(@Param("start") Integer start, @Param("size") Integer size, @Param("keywords") String keywords);

    Long getCountByKeywords(@Param("keywords") String keywords);

    int updateProject(@Param("prj") Project project);

    int deletePrjById(@Param("ids") String[] ids);

    int addPrjs(@Param("prjs") List<Project> prjs);

    List<Project> getProjectByPageShort(@Param("start") int start, @Param("size") Integer size);
}
