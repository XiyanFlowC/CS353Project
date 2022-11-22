package org.cstp.meowtool.database;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface GroupMapper {
    @Select("SELECT * FROM user_group_info WHERE id=@{id}")
    public Group selectGroup (Integer id);

    @Select("SELECT * FROM user_group_info WHERE proj_id=#{projId}")
    public Group[] selectGroupsByProject (Integer projId);

    @Select("SELECT * FROM user_group_info WHERE user_id=#{userId}")
    public Group[] selectGroupsByUser (Integer userId);

    @Insert("INSERT INTO user_group_info (proj_id, user_id, role) VALUES (#{proj_id}, #{user_id}, #{role})")
    public int insertGroup (Group group);

    @Delete("DELETE FROM user_group_info WHERE id=#{id}")
    public int deleteGroup (Integer id);

    @Delete("DELETE FROM user_group_info WHERE proj_id=${prj_id} AND user_id=#{usr_id}")
    public int deleteGroupByProjectUser (@Param("prj_id") Integer projId, @Param("usr_id") Integer userId);

    @Select("SELECT * FROM user_group_info WHERE proj_id=#{prj_id} AND user_id=${usr_id}")
    public Group selectGroupByProjectUser (@Param("prj_id") Integer projId, @Param("usr_id") Integer userId);

    @Update("UPDATE user_group_info SET role=#{role} WHERE id=#{id}") // do not update usr id/prj id!
    public Group updateGroupRole (Group group);

    @Update("UPDATE user_group_info SET role=#{role} WHERE proj_id=#{proj_id} AND user_id=#{user_id}")
    public Group updateGroupByProjectUser (Group group);
}
