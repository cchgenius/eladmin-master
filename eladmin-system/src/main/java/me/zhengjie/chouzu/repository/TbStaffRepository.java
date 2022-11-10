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
package me.zhengjie.chouzu.repository;

import me.zhengjie.chouzu.domain.TbStaff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author cuichuang
 * @website https://eladmin.vip
 * @date 2022-11-01
 **/
public interface TbStaffRepository extends JpaRepository<TbStaff, Integer>, JpaSpecificationExecutor<TbStaff> {

    @Modifying
    @Transactional
    @Query(value = "update tb_staff\n" +
            "set tb_staff.group_id=?2,\n" +
            "    tb_staff.is_group=?3\n" +
            "where tb_staff.staff_id = ?1", nativeQuery = true)
    int sdfz(Integer staffId, @Param("groupId") Long groupId, @Param("isGroup") String isGroup);

    /*
        后端开发人员总数
     */
    @Query(value = "SELECT staff_id FROM tb_staff WHERE role_id = '1' and is_group = '0'", nativeQuery = true)
    List<Integer> countBackend();

    /*
        前端开发人员总数
     */
    @Query(value = "SELECT staff_id FROM tb_staff WHERE role_id = '2' and is_group = '0'", nativeQuery = true)
    List<Integer> countFront();

    /*
        测试人员总数
     */
    @Query(value = "SELECT staff_id FROM tb_staff WHERE role_id = '3' and is_group = '0'", nativeQuery = true)
    List<Integer> countTest();

    /*
        项目经理人员总数
     */
    @Query(value = "SELECT staff_id FROM tb_staff WHERE role_id = '4' and is_group = '0'", nativeQuery = true)
    List<Integer> countManager();
}