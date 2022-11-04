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

import me.zhengjie.chouzu.domain.TbGroup;
import me.zhengjie.utils.*;
import lombok.RequiredArgsConstructor;
import me.zhengjie.chouzu.repository.TbGroupRepository;
import me.zhengjie.chouzu.service.TbGroupService;
import me.zhengjie.chouzu.service.dto.TbGroupDto;
import me.zhengjie.chouzu.service.dto.TbGroupQueryCriteria;
import me.zhengjie.chouzu.service.mapstruct.TbGroupMapper;
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
 * @author cuichuang
 * @website https://eladmin.vip
 * @description 服务实现
 * @date 2022-11-01
 **/
@Service
@RequiredArgsConstructor
public class TbGroupServiceImpl implements TbGroupService {

    private final TbGroupRepository tbGroupRepository;
    private final TbGroupMapper tbGroupMapper;

    @Override
    public Map<String, Object> queryAll(TbGroupQueryCriteria criteria, Pageable pageable) {
        Page<TbGroup> page = tbGroupRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(tbGroupMapper::toDto));
    }

    @Override
    public List<TbGroupDto> queryAll(TbGroupQueryCriteria criteria) {
        return tbGroupMapper.toDto(tbGroupRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    @Transactional
    public TbGroupDto findById(Long groupId) {
        TbGroup tbGroup = tbGroupRepository.findById(groupId).orElseGet(TbGroup::new);
        ValidationUtil.isNull(tbGroup.getGroupId(), "TbGroup", "groupId", groupId);
        return tbGroupMapper.toDto(tbGroup);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TbGroupDto create(TbGroup resources) {
        String currentUser = SecurityUtils.getCurrentUsername();
        resources.setCreateBy(currentUser);
        return tbGroupMapper.toDto(tbGroupRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TbGroup resources) {
        String currentUser = SecurityUtils.getCurrentUsername();
        TbGroup tbGroup = tbGroupRepository.findById(resources.getGroupId()).orElseGet(TbGroup::new);
        ValidationUtil.isNull(tbGroup.getGroupId(), "TbGroup", "id", resources.getGroupId());
        tbGroup.copy(resources);
        tbGroup.setUpdateBy(currentUser);
        tbGroupRepository.save(tbGroup);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long groupId : ids) {
            tbGroupRepository.deleteById(groupId);
        }
    }

    @Override
    public void download(List<TbGroupDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TbGroupDto tbGroup : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("分组名称", tbGroup.getGroupName());
            map.put("分组方式：0手动，1自动", tbGroup.getGroupWay());
            map.put("创建者", tbGroup.getCreateBy());
            map.put("创建时间", tbGroup.getCreateTime());
            map.put("更新者", tbGroup.getUpdateBy());
            map.put("更新时间", tbGroup.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}