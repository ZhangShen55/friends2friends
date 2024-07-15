package com.chanson.f2f.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chanson.f2f.mapper.TagMapper;
import com.chanson.f2f.model.domain.Tag;

import com.chanson.f2f.service.TagService;
import org.springframework.stereotype.Service;

/**
* @author ZhangShen
* @description 针对表【tag(标签)】的数据库操作Service实现
* @createDate 2024-07-13 21:31:45
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper,Tag>
    implements TagService {

}




