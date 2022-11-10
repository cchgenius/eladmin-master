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
import me.zhengjie.chouzu.domain.TbGroup;
import me.zhengjie.chouzu.service.TbGroupService;
import me.zhengjie.chouzu.service.dto.TbGroupQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author cuichuang
 * @website https://eladmin.vip
 * @date 2022-11-01
 **/
@RestController
@RequiredArgsConstructor
@Api(tags = "分组表管理")
@RequestMapping("/api/tbGroup")
public class TbGroupController {

    private final TbGroupService tbGroupService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('tbGroup:list')")
    public void exportTbGroup(HttpServletResponse response, TbGroupQueryCriteria criteria) throws IOException {
        tbGroupService.download(tbGroupService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询分组表")
    @ApiOperation("查询分组表")
    @PreAuthorize("@el.check('tbGroup:list')")
    public ResponseEntity<Object> queryTbGroup(TbGroupQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(tbGroupService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增分组表")
    @ApiOperation("新增分组表")
    @PreAuthorize("@el.check('tbGroup:add')")
    public ResponseEntity<Object> createTbGroup(@Validated @RequestBody TbGroup resources) {
        return new ResponseEntity<>(tbGroupService.create(resources), HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改分组表")
    @ApiOperation("修改分组表")
    @PreAuthorize("@el.check('tbGroup:edit')")
    public ResponseEntity<Object> updateTbGroup(@Validated @RequestBody TbGroup resources) {
        tbGroupService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除分组表")
    @ApiOperation("删除分组表")
    @PreAuthorize("@el.check('tbGroup:del')")
    public ResponseEntity<Object> deleteTbGroup(@RequestBody Long[] ids) {
        tbGroupService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}