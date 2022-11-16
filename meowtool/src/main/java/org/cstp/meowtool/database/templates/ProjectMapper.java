package org.cstp.meowtool.database.templates;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ProjectMapper {
    @Insert("INSERT INTO projects (type, name, tags, ori_lang, tar_lang) VALUE (#{type}, #{name}, #{tags}, #{ori_lang}, #{tar_lang})")
    public int insertProject(Project project);

    @Select("SELECT * FROM projects WHERE id=#{id}")
    public Project selectProject(Integer id);

    @Select("SELECT * FROM projects WHERE name=#{name}")
    public Project selectProjectName(String name);

    @Select("SELECT * FROM projects WHERE id >= (SELECT id FROM projects ORDER BY id ASC LIMIT #{page}-1*#{size}, 1) LIMIT #{size}")
    public Project[] selectWithPage (@Param("page") Integer page, @Param("size") Integer size);
}
