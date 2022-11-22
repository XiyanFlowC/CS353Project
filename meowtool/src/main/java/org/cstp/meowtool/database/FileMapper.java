package org.cstp.meowtool.database;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface FileMapper {
    @Select("SELECT * FROM files WHERE id = #{id}")
    public File selectFile(Integer id);

    @Select("SELECT * FROM files WHERE category_id = #{categoryId}                                                                                                                                                              ")
    public File[] selectByCategory(Integer categoryId);

    @Insert("INSERT INTO files (category_id, name, converter, comment) VALUES (#{category_id}, #{name}, #{converter}, #{comment})")
    public int insertFile(File file);

    @Update("UPDATE files SET category_id=#{category_id}, name=#{name}, converter=#{converter}, comment=#{comment} WHERE id=#{id}")
    public int updateFile(File file);
}
