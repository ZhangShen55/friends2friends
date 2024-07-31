package com.chanson.f2f.common;


import lombok.Data;

import java.io.Serializable;


/**
 * 通过Page包装类
 */
@Data
public class PageRequest implements Serializable {


    private static final long serialVersionUID = -3757139226203870883L;
    protected Integer pageNum = 1;
    protected Integer pageSize = 10;
}
