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

    @Update("UPDATE texts SET ori_text=#{ori_text}, trans=#{trans}, file_id=#{file_id} WHERE id=#{id}")
    public int updateText(Text text);

    /**
     * Update the translation only. DO NOT RECOMMEND!
     * 
     * @param id
     * @param translation
     * @return
     * @deprecated for future version control, this method should be avoid to use.
     */
    @Deprecated
    @Update("UPDATE texts SET trans=#{trans} WHERE id=#{id}")
    public int updateTranslation(@Param("id") Integer id, @Param("trans") String translation);

    @Insert("INSERT INTO texts (file_id, ori_text, trans) VALUES (#{file_id}, #{ori_text}, #{trans})")
    public int insertText(Text text);

    @Delete("DELETE FROM texts WHERE file_id=#{file_id}")
    public int deleteTextsByFile(Integer fileid);

    @Delete("DELETE FROM texts WHERE id=#{id}")
    public int deleteText(Integer id);
}
