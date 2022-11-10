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

import me.zhengjie.annotation.Log;
import me.zhengjie.chouzu.domain.TbRole;
import me.zhengjie.chouzu.service.TbRoleService;
import me.zhengjie.chouzu.service.dto.TbRoleQueryCriteria;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
 * @author cuichuang
 * @website https://eladmin.vip
 * @date 2022-11-01
 **/
@RestController
@RequiredArgsConstructor
@Api(tags = "角色表管理")
@RequestMapping("/api/tbRole")
public class TbRoleController {

    private final TbRoleService tbRoleService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('tbRole:list')")
    public void exportTbRole(HttpServletResponse response, TbRoleQueryCriteria criteria) throws IOException {
        tbRoleService.download(tbRoleService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询角色表")
    @ApiOperation("查询角色表")
    @PreAuthorize("@el.check('tbRole:list')")
    public ResponseEntity<Object> queryTbRole(TbRoleQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(tbRoleService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增角色表")
    @ApiOperation("新增角色表")
    @PreAuthorize("@el.check('tbRole:add')")
    public ResponseEntity<Object> createTbRole(@Validated @RequestBody TbRole resources) {
        return new ResponseEntity<>(tbRoleService.create(resources), HttpStatus.CREATED);
    }

//    @PutMapping
//    @Log("修改角色表")
//    @ApiOperation("修改角色表")
//    @PreAuthorize("@el.check('tbRole:edit')")
//    public ResponseEntity<Object> updateTbRole(@Validated @RequestBody TbRole resources) {
//        tbRoleService.update(resources);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

    @DeleteMapping
    @Log("删除角色表")
    @ApiOperation("删除角色表")
    @PreAuthorize("@el.check('tbRole:del')")
    public ResponseEntity<Object> deleteTbRole(@RequestBody String[] ids) {
        tbRoleService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}