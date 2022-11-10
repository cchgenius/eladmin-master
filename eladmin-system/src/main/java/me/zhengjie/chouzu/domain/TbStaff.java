/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package me.zhengjie.chouzu.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiModelProperty;
import cn.hutool.core.bean.copier.CopyOptions;

import javax.persistence.*;
import javax.validation.constraints.*;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.*;

import java.sql.Timestamp;
import java.io.Serializable;

/**
 * @author cuichuang
 * @website https://eladmin.vip
 * @description /
 * @date 2022-11-01
 **/
@Entity
@Data
@Table(name = "tb_staff")
public class TbStaff implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`staff_id`")
    @ApiModelProperty(value = "人员id")
    private Integer staffId;

    @Column(name = "`group_id`")
    @ApiModelProperty(value = "分组名称")
    private Long groupId;

    @Column(name = "`name`")
    @ApiModelProperty(value = "人员名称")
    private String name;

    @Column(name = "`gender`")
    @ApiModelProperty(value = "性别")
    private String gender;

    @Column(name = "`age`")
    @ApiModelProperty(value = "年龄")
    private Integer age;

    @Column(name = "`phone`")
    @ApiModelProperty(value = "手机号码")
    private String phone;

    @Column(name = "`email`")
    @ApiModelProperty(value = "邮箱")
    private String email;

    @Column(name = "`is_group`")
    @ApiModelProperty(value = "是否已分组：0未分组，1已分组")
    private String isGroup;

    @Column(name = "`create_by`")
    @ApiModelProperty(value = "创建者")
    private String createBy;

    @Column(name = "`create_time`")
    @CreationTimestamp
    @ApiModelProperty(value = "创建时间")
    private Timestamp createTime;

    @Column(name = "`update_by`")
    @ApiModelProperty(value = "更新者")
    private String updateBy;

    @Column(name = "`update_time`")
    @UpdateTimestamp
    @ApiModelProperty(value = "更新时间")
    private Timestamp updateTime;

    @Column(name = "`role_id`")
    @ApiModelProperty(value = "角色")
    private String roleId;

    public void copy(TbStaff source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
