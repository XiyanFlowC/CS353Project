package org.cstp.meowtool.database.templates;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface FileMapper {
    @Select("SELECT * FROM files WHERE id = #{id}")
    public File selectFile(Integer id);

    @Select("SELECT * FROM files WHERE category_id = #{category_id}                                                                                                                                                              ")
    public File selectByCategory(Integer category_id);

    @Insert("INSERT INTO files (category_id, name, converter, comment) VALUE (#{category_id}, #{name}, #{converter}, #{comment})")
    public int insertFile(File file);

    @Update("UPDATE files SET category_id=#{category_id}, name=#{name}, converter=#{converter}, comment=#{comment} WHERE id=#{id}")
    public int updateFile(File file);
}
