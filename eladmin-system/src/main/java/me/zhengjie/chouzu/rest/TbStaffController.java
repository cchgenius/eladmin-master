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
import me.zhengjie.chouzu.domain.TbStaff;
import me.zhengjie.chouzu.service.TbStaffService;
import me.zhengjie.chouzu.service.dto.TbStaffQueryCriteria;
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
* @website https://eladmin.vip
* @author cuichuang
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
    public ResponseEntity<Object> queryTbStaff(TbStaffQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(tbStaffService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增人员")
    @ApiOperation("新增人员")
    @PreAuthorize("@el.check('tbStaff:add')")
    public ResponseEntity<Object> createTbStaff(@Validated @RequestBody TbStaff resources){
        return new ResponseEntity<>(tbStaffService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改人员")
    @ApiOperation("修改人员")
    @PreAuthorize("@el.check('tbStaff:edit')")
    public ResponseEntity<Object> updateTbStaff(@Validated @RequestBody TbStaff resources){
        tbStaffService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除人员")
    @ApiOperation("删除人员")
    @PreAuthorize("@el.check('tbStaff:del')")
    public ResponseEntity<Object> deleteTbStaff(@RequestBody Long[] ids) {
        tbStaffService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}