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

import lombok.RequiredArgsConstructor;
import me.zhengjie.chouzu.domain.TbRole;
import me.zhengjie.chouzu.repository.TbRoleRepository;
import me.zhengjie.chouzu.service.TbRoleService;
import me.zhengjie.chouzu.service.dto.TbRoleDto;
import me.zhengjie.chouzu.service.dto.TbRoleQueryCriteria;
import me.zhengjie.chouzu.service.mapstruct.TbRoleMapper;
import me.zhengjie.utils.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author cuichuang
 * @website https://eladmin.vip
 * @description 服务实现
 * @date 2022-11-01
 **/
@Service
@RequiredArgsConstructor
public class TbRoleServiceImpl implements TbRoleService {

    private final TbRoleRepository tbRoleRepository;
    private final TbRoleMapper tbRoleMapper;

    @Override
    public Map<String, Object> queryAll(TbRoleQueryCriteria criteria, Pageable pageable) {
        Page<TbRole> page = tbRoleRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(tbRoleMapper::toDto));
    }

    @Override
    public List<TbRoleDto> queryAll(TbRoleQueryCriteria criteria) {
        return tbRoleMapper.toDto(tbRoleRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    @Transactional
    public TbRoleDto findById(String roleId) {
        TbRole tbRole = tbRoleRepository.findById(Long.valueOf(roleId)).orElseGet(TbRole::new);
        ValidationUtil.isNull(tbRole.getRoleId(), "TbRole", "roleId", roleId);
        return tbRoleMapper.toDto(tbRole);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TbRoleDto create(TbRole resources) {
        String currentUser = SecurityUtils.getCurrentUsername();
        resources.setCreateBy(currentUser);
        return tbRoleMapper.toDto(tbRoleRepository.save(resources));
    }

//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void update(TbRole resources) {
//        String currentUser = SecurityUtils.getCurrentUsername();
//        TbRole tbRole = tbRoleRepository.findById(resources.getRoleId()).orElseGet(TbRole::new);
//        ValidationUtil.isNull(tbRole.getRoleId(), "TbRole", "id", resources.getRoleId());
//        tbRole.copy(resources);
//        tbRole.setUpdateBy(currentUser);
//        tbRoleRepository.save(tbRole);
//    }

    @Override
    public void deleteAll(String[] ids) {
        for (String roleId : ids) {
            tbRoleRepository.deleteById(Long.valueOf(roleId));
        }
    }

    @Override
    public void download(List<TbRoleDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TbRoleDto tbRole : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("角色名称", tbRole.getRoleName());
            map.put("创建者", tbRole.getCreateBy());
            map.put("创建时间", tbRole.getCreateTime());
            map.put("更新者", tbRole.getUpdateBy());
            map.put("更新时间", tbRole.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}