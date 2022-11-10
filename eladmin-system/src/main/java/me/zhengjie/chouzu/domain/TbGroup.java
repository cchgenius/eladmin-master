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
@Table(name = "tb_group")
public class TbGroup implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`group_id`")
    @ApiModelProperty(value = "id")
    private Long groupId;

    @Column(name = "`group_name`")
    @ApiModelProperty(value = "分组名称")
    private String groupName;

    @Column(name = "`group_way`", nullable = false)
    @NotNull
    @ApiModelProperty(value = "分组方式：0手动，1自动")
    private String groupWay;

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

    public void copy(TbGroup source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
