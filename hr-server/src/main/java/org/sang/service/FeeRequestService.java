package org.sang.service;

import org.sang.bean.Resource;
import org.sang.bean.ResourceDetail;
import org.sang.mapper.FeeRequestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * 请求管理
 * Created by purvar on 2018/1/12.
 */
@Service
@Transactional
public class FeeRequestService {
    @Autowired
    FeeRequestMapper feeRequestMapper;
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
    SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
    SimpleDateFormat birthdayFormat = new SimpleDateFormat("yyyy-MM-dd");
    DecimalFormat decimalFormat = new DecimalFormat("##.00");

    public List<Resource> getFeeRequestByPage(Integer page, Integer size, String keywords,String month) {
        Integer start = null;
        if(page != null && size != null){
            start = (page - 1) * size;
        }

        return feeRequestMapper.getFeeRequestByPage(start, size, keywords, month);
    }

    public Long getCountByKeywords(String keywords,String month) {
        return feeRequestMapper.getCountByKeywords(keywords,month);
    }

}
