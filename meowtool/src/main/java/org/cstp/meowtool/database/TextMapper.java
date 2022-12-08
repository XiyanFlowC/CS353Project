package org.cstp.meowtool.database;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TextMapper {
    @Select("SELECT * FROM texts WHERE id=${id}")
    public Text selectId(Integer id);

    @Select("SELECT * FROM texts WHERE file_id=#{fileid} ORDER BY id ASC")
    public Text[] selectByFile(@Param("fileid") Integer fileId);

    @Update("UPDATE texts SET ori_text=#{ori_text}, comment=#{comment}, file_id=#{file_id}, marked=#{marked} WHERE id=#{id}")
    public int updateText(Text text);

    @Insert("INSERT INTO texts (file_id, ori_text, comment, marked) VALUES (#{file_id}, #{ori_text}, #{comment}, #{marked})")
    public int insertText(Text text);

    @Delete("DELETE FROM texts WHERE file_id=#{file_id}")
    public int deleteTextsByFile(Integer fileid);

    @Delete("DELETE FROM texts WHERE id=#{id}")
    public int deleteText(Integer id);

    @Update("UPDATE texts SET marked = TRUE WHERE id=#{id}")
    public int markTranslation(Integer id);

    @Update("UPDATE texts SET marked = FALSE WHERE id=#{id}")
    public int cleanMark(Integer id);
}
