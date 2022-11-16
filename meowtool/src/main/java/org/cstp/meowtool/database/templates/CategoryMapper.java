package org.cstp.meowtool.database.templates;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface CategoryMapper {
    @Select("SELECT * FROM categories WHERE id = #{id}")
    public Category selectCategory(Integer id);

    @Select("SELECT * FROM categories WHERE proj_id=#{proj_id}")
    public Category[] selectByProject(Integer proj_id);

    @Insert("INSERT INTO categories (proj_id, name, comment) VALUE (#{proj_id}, #{name}, #{comment})")
    public int insertCategory(Category category);

    @Delete("DELETE FROM categories WHERE id=#{id}")
    public int deleteCategory(Integer id);

    @Update("UPDATE categories SET proj_id=#{proj_id}, name=#{name}, comment=#{comment} WHERE id = #{id}")
    public int updateCategory(Category category);
}
