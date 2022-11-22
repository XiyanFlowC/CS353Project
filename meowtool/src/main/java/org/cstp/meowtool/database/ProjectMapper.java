package org.cstp.meowtool.database;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ProjectMapper {
    @Delete("DELETE FROM projects WHERE id=#{id}")
    public int deleteProject(Integer id);

    @Update("UPDATE projects SET owner=#{owner}, type=#{type}, name=#{name}, tags=#{tags}, ori_lang=#{ori_lang}, tar_lang=#{tar_lang} WHERE id=#{id}")
    public int updateProject(Project project);

    @Insert("INSERT INTO projects (owner, type, name, tags, ori_lang, tar_lang) VALUES (#{owner}, #{type}, #{name}, #{tags}, #{ori_lang}, #{tar_lang})")
    public int insertProject(Project project);

    @Select("SELECT * FROM projects WHERE id=#{id}")
    public Project selectProject(Integer id);

    @Select("SELECT * FROM projects WHERE name=#{name}")
    public Project selectProjectName(String name);

    @Select("SELECT * FROM projects WHERE id >= (SELECT id FROM projects ORDER BY id ASC OFFSET (#{page}-1)*#{size} LIMIT 1) LIMIT #{size}")
    public Project[] selectWithPaging (@Param("page") Integer page, @Param("size") Integer size);
}
