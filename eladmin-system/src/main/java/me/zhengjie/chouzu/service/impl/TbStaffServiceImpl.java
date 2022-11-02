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
package me.zhengjie.chouzu.service.impl;

import me.zhengjie.chouzu.domain.TbStaff;
import me.zhengjie.utils.*;
import lombok.RequiredArgsConstructor;
import me.zhengjie.chouzu.repository.TbStaffRepository;
import me.zhengjie.chouzu.service.TbStaffService;
import me.zhengjie.chouzu.service.dto.TbStaffDto;
import me.zhengjie.chouzu.service.dto.TbStaffQueryCriteria;
import me.zhengjie.chouzu.service.mapstruct.TbStaffMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
* @website https://eladmin.vip
* @description 服务实现
* @author cuichuang
* @date 2022-11-01
**/
@Service
@RequiredArgsConstructor
public class TbStaffServiceImpl implements TbStaffService {

    private final TbStaffRepository tbStaffRepository;
    private final TbStaffMapper tbStaffMapper;

    @Override
    public Map<String,Object> queryAll(TbStaffQueryCriteria criteria, Pageable pageable){
        Page<TbStaff> page = tbStaffRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(tbStaffMapper::toDto));
    }

    @Override
    public List<TbStaffDto> queryAll(TbStaffQueryCriteria criteria){
        return tbStaffMapper.toDto(tbStaffRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public TbStaffDto findById(Long staffId) {
        TbStaff tbStaff = tbStaffRepository.findById(staffId).orElseGet(TbStaff::new);
        ValidationUtil.isNull(tbStaff.getStaffId(),"TbStaff","staffId",staffId);
        return tbStaffMapper.toDto(tbStaff);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TbStaffDto create(TbStaff resources) {
        String currentUser = SecurityUtils.getCurrentUsername();
        resources.setCreateBy(currentUser);
        return tbStaffMapper.toDto(tbStaffRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TbStaff resources) {
        String currentUser = SecurityUtils.getCurrentUsername();
        TbStaff tbStaff = tbStaffRepository.findById(resources.getStaffId()).orElseGet(TbStaff::new);
        ValidationUtil.isNull( tbStaff.getStaffId(),"TbStaff","id",resources.getStaffId());
        tbStaff.copy(resources);
        tbStaff.setUpdateBy(currentUser);
        tbStaffRepository.save(tbStaff);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long staffId : ids) {
            tbStaffRepository.deleteById(staffId);
        }
    }

    @Override
    public void download(List<TbStaffDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TbStaffDto tbStaff : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("分组名称", tbStaff.getGruopId());
            map.put("人员名称", tbStaff.getName());
            map.put("性别", tbStaff.getGender());
            map.put("年龄", tbStaff.getAge());
            map.put("手机号码", tbStaff.getPhone());
            map.put("邮箱", tbStaff.getEmail());
            map.put("是否已分组：0未分组，1已分组", tbStaff.getIsGruop());
            map.put("创建者", tbStaff.getCreateBy());
            map.put("创建时间", tbStaff.getCreateTime());
            map.put("更新者", tbStaff.getUpdateBy());
            map.put("更新时间", tbStaff.getUpdateTime());
            map.put("角色", tbStaff.getRoleId());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}