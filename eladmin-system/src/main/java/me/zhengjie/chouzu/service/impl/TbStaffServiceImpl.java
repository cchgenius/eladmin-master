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
import me.zhengjie.chouzu.domain.TbStaff;
import me.zhengjie.chouzu.repository.TbGroupRepository;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.utils.*;
import lombok.RequiredArgsConstructor;
import me.zhengjie.chouzu.repository.TbStaffRepository;
import me.zhengjie.chouzu.service.TbStaffService;
import me.zhengjie.chouzu.service.dto.TbStaffDto;
import me.zhengjie.chouzu.service.dto.TbStaffQueryCriteria;
import me.zhengjie.chouzu.service.mapstruct.TbStaffMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.io.IOException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletResponse;

/**
 * @author cuichuang
 * @website https://eladmin.vip
 * @description 服务实现
 * @date 2022-11-01
 **/
@Service
@RequiredArgsConstructor
public class TbStaffServiceImpl implements TbStaffService {

    private final TbStaffRepository tbStaffRepository;
    private final TbStaffMapper tbStaffMapper;

    private final TbGroupRepository tbGroupRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private Long batchSize;

    @Override
    public Map<String, Object> queryAll(TbStaffQueryCriteria criteria, Pageable pageable) {
        Page<TbStaff> page = tbStaffRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(tbStaffMapper::toDto));
    }

    @Override
    public List<TbStaffDto> queryAll(TbStaffQueryCriteria criteria) {
        return tbStaffMapper.toDto(tbStaffRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    @Transactional
    public TbStaffDto findById(Integer staffId) {
        TbStaff tbStaff = tbStaffRepository.findById(staffId).orElseGet(TbStaff::new);
        ValidationUtil.isNull(tbStaff.getStaffId(), "TbStaff", "staffId", staffId);
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
    public void zidong(Integer backend, Integer front, Integer test, Integer manager, Long groupId, String groupWay) {
        String currentUser = SecurityUtils.getCurrentUsername();
        List<Integer> backendList = tbStaffRepository.countBackend();
        List<Integer> frontList = tbStaffRepository.countFront();
        List<Integer> testList = tbStaffRepository.countTest();
        List<Integer> managerList = tbStaffRepository.countManager();
        System.out.println(backendList);
        System.out.println(frontList);
        System.out.println(testList);
        System.out.println(managerList);

        Random random = new Random();
        List<Integer> tbStaffList = new ArrayList<>();
        Integer num = backend;
        for (int i = 1; i <= num; i++) {
            backend = random.nextInt(backendList.size());
            tbStaffList.add(backendList.get(backend));
            backendList.remove(backend);
        }

        Integer num1 = front;
        for (int i = 1; i <= num1; i++) {
            front = random.nextInt(frontList.size());
            tbStaffList.add(frontList.get(front));
            frontList.remove(front);
        }

        Integer num2 = test;
        for (int i = 1; i <= num2; i++) {
            test = random.nextInt(testList.size());
            tbStaffList.add(testList.get(test));
            testList.remove(test);
        }

        Integer num3 = manager;
        for (int i = 1; i <= num3; i++) {
            manager = random.nextInt(managerList.size());
            tbStaffList.add(managerList.get(manager));
            managerList.remove(manager);
        }

        for (Integer id : tbStaffList) {
            System.out.println(id);
            TbStaff tbStaff = tbStaffRepository.findById(id).orElseGet(TbStaff::new);
            if (Objects.equals(tbStaff.getIsGroup(), "0")) {
                if (tbStaff.getStaffId() == null) {
                    continue;
                }
                tbStaff.setGroupId(groupId);
                tbStaff.setIsGroup("1");
                tbStaff.setUpdateBy(currentUser);
                tbStaffRepository.save(tbStaff);

                TbGroup tbGroup = tbGroupRepository.findById(groupId).orElseGet(TbGroup::new);
                if (tbGroup.getGroupId().equals(groupId)) {
                    tbGroup.setGroupWay(groupWay);
                }
            } else {
                throw new BadRequestException(HttpStatus.IM_USED, "已分组的人员不能再分组");
            }
        }
    }


    public void sdfz(Integer staffId, Long groupId, String isGroup) {
        tbStaffRepository.sdfz(staffId, groupId, isGroup);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TbStaff resources) {
        String currentUser = SecurityUtils.getCurrentUsername();
        TbStaff tbStaff = tbStaffRepository.findById(resources.getStaffId()).orElseGet(TbStaff::new);
        ValidationUtil.isNull(tbStaff.getStaffId(), "TbStaff", "id", resources.getStaffId());
        tbStaff.copy(resources);
        tbStaff.setUpdateBy(currentUser);
        tbStaffRepository.save(tbStaff);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sd(List<TbStaff> staffList) {
        if (!ObjectUtils.isEmpty(staffList)) {
            for (int i = 0; i < staffList.size(); i++) {
                entityManager.merge(staffList.get(i));
                if (i % batchSize == 0) {
                    entityManager.flush();
                    entityManager.clear();
                }
            }
            entityManager.flush();
            entityManager.clear();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void shoudong(Integer[] ids, Long groupId, String groupWay) {
        String currentUser = SecurityUtils.getCurrentUsername();
        for (Integer id : ids) {
            TbStaff tbStaff = tbStaffRepository.findById(id).orElseGet(TbStaff::new);
            if (Objects.equals(tbStaff.getIsGroup(), "0")) {
                if (tbStaff.getStaffId() == null) {
                    continue;
                }
                tbStaff.setGroupId(groupId);
                tbStaff.setIsGroup("1");
                tbStaff.setUpdateBy(currentUser);
                tbStaffRepository.save(tbStaff);

                TbGroup tbGroup = tbGroupRepository.findById(groupId).orElseGet(TbGroup::new);
                if (tbGroup.getGroupId().equals(groupId)) {
                    tbGroup.setGroupWay(groupWay);
                }
            } else {
                throw new BadRequestException(HttpStatus.IM_USED, "已分组的人员不能再分组");
            }

        }
    }

    @Override
    public void deleteAll(Integer[] ids) {
        for (Integer staffId : ids) {
            tbStaffRepository.deleteById(staffId);
        }
    }

    @Override
    public void download(List<TbStaffDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TbStaffDto tbStaff : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("分组名称", tbStaff.getGroupId());
            map.put("人员名称", tbStaff.getName());
            map.put("性别", tbStaff.getGender());
            map.put("年龄", tbStaff.getAge());
            map.put("手机号码", tbStaff.getPhone());
            map.put("邮箱", tbStaff.getEmail());
            map.put("是否已分组：0未分组，1已分组", tbStaff.getIsGroup());
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