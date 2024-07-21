package com.chanson.f2f.once;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;


@Data
public class UserInfoExcel {

    /**
     * id
     */
//    @ExcelProperty("用户id")
//    private Long id;

    /**
     * 星球编号
     */
    @ExcelProperty("成员编号")
    private String planetCode;

    /**
     * 用户昵称
     */
    @ExcelProperty("成员昵称")
    private String username;

}