package org.sang.mapper;

import org.apache.ibatis.annotations.Param;
import org.sang.bean.Resource;

import java.util.List;

/**
 * Created by sang on 2018/1/12.
 */
public interface FeeRequestMapper {

    List<Resource> getFeeRequestByPage(@Param("start") Integer start, @Param("size") Integer size, @Param("keywords") String keywords, @Param("month") String month);

    Long getCountByKeywords(@Param("keywords") String keywords,@Param("month") String month);

}
