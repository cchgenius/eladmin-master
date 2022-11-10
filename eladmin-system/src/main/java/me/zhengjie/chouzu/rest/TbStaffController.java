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
package me.zhengjie.chouzu.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.chouzu.domain.TbStaff;
import me.zhengjie.chouzu.service.TbStaffService;
import me.zhengjie.chouzu.service.dto.TbStaffQueryCriteria;
import me.zhengjie.chouzu.service.dto.myData;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author cuichuang
 * @website https://eladmin.vip
 * @date 2022-11-01
 **/
@RestController
@RequiredArgsConstructor
@Api(tags = "人员管理")
@RequestMapping("/api/tbStaff")
public class TbStaffController {

    private final TbStaffService tbStaffService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('tbStaff:list')")
    public void exportTbStaff(HttpServletResponse response, TbStaffQueryCriteria criteria) throws IOException {
        tbStaffService.download(tbStaffService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询人员")
    @ApiOperation("查询人员")
    @PreAuthorize("@el.check('tbStaff:list')")
    public ResponseEntity<Object> queryTbStaff(TbStaffQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(tbStaffService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @GetMapping("/notGroup")
    @Log("查询未分组人员")
    @ApiOperation("查询未分组人员")
    @PreAuthorize("@el.check('tbStaff:list')")
    public ResponseEntity<Object> notGroup(TbStaffQueryCriteria criteria, Pageable pageable) {
        criteria.setIsGroup("0");
        return new ResponseEntity<>(tbStaffService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增人员")
    @ApiOperation("新增人员")
    @PreAuthorize("@el.check('tbStaff:add')")
    public ResponseEntity<Object> createTbStaff(@Validated @RequestBody TbStaff resources) {
        return new ResponseEntity<>(tbStaffService.create(resources), HttpStatus.CREATED);
    }

    @PutMapping("/shoudong")
    @Log("手动分组")
    @ApiOperation("手动分组")
    @PreAuthorize("@el.check('tbStaff:edit')")
    public ResponseEntity<Object> shoudong(@Validated @RequestBody myData data) {
        System.out.println("*****************" + data);
        Integer[] ids = data.getIds();
        Long groupId = data.getGroupId();
        String groupWay = data.getGroupWay();
        tbStaffService.shoudong(ids, groupId, groupWay);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/sd")
    @PreAuthorize("@el.check('tbStaff:edit')")
    public ResponseEntity<Object> sd(@Validated @RequestBody List<TbStaff> list) {
        tbStaffService.sd(list);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/zidong")
    @Log("自动分组")
    @ApiOperation("自动分组")
    @PreAuthorize("@el.check('tbStaff:edit')")
    public ResponseEntity<Object> zidong(@Validated @RequestBody myData data) {
        System.out.println("*****************" + data);
        Integer backend = data.getBackend();
        Integer front = data.getFront();
        Integer test = data.getTest();
        Integer manager = data.getManager();
        Long groupId = data.getGroupId();
        String groupWay = data.getGroupWay();
        tbStaffService.zidong(backend, front, test, manager, groupId, groupWay);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改人员")
    @ApiOperation("修改人员")
    @PreAuthorize("@el.check('tbStaff:edit')")
    public ResponseEntity<Object> updateTbStaff(@Validated @RequestBody TbStaff resources) {
        tbStaffService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除人员")
    @ApiOperation("删除人员")
    @PreAuthorize("@el.check('tbStaff:del')")
    public ResponseEntity<Object> deleteTbStaff(@RequestBody Integer[] ids) {
        tbStaffService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}