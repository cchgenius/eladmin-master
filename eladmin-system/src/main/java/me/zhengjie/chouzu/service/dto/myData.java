package me.zhengjie.chouzu.service.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class myData implements Serializable {
    /*
        人员的id
     */
    private Integer[] ids;

    /*
        分组方式
     */
    private String groupWay;

    /*
        分组的id
     */
    private Long groupId;

    /*
        后端开发的数量
     */
    private Integer backend;

    /*
       前端开发的数量
    */
    private Integer front;

    /*
       测试的数量
    */
    private Integer test;

    /*
       项目经理的数量
    */
    private Integer manager;

}
