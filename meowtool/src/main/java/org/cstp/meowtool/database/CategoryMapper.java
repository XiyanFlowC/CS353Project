package org.cstp.meowtool.database;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface CategoryMapper {
    @Select("SELECT * FROM categories WHERE id = #{id}")
    public Category selectCategory(Integer id);

    @Select("SELECT * FROM categories WHERE proj_id=#{projId}")
    public Category[] selectByProject(Integer projId);

    @Select("SELECT * FROM (SELECT * FROM categories WHERE proj_id=#{pid}) AS cates OFFSET (#{page}-1)*#{size} LIMIT #{size}")
    public Category[] selectByProjectWithPaging(@Param("pid") Integer projId, @Param("page") Integer page, @Param("size") Integer size);

    @Insert("INSERT INTO categories (proj_id, name, comment) VALUES (#{proj_id}, #{name}, #{comment})")
    public int insertCategory(Category category);

    @Delete("DELETE FROM categories WHERE id=#{id}")
    public int deleteCategory(Integer id);

    @Update("UPDATE categories SET proj_id=#{proj_id}, name=#{name}, comment=#{comment} WHERE id = #{id}")
    public int updateCategory(Category category);
}
