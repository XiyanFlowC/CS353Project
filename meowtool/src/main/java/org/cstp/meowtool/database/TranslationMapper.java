package org.cstp.meowtool.database;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TranslationMapper {
    @Select("SELECT * FROM translations WHERE id = #{id}")
    public Translation selectTranslation(Integer id);

    @Select("SELECT * FROM translations WHERE ori_id = #{ori_id} ORDER BY id DESC LIMIT 1")
    public Translation selectByOriId(Integer oriId);

    @Select("SELECT * FROM translations WHERE ori_id = #{ori_id}")
    public Translation selectAllTranslationOfOriId(Integer oriId);

    @Insert("INSERT INTO translations (ori_id, trans, commiter) VALUES (#{ori_id}, #{trans}, #{usr_id})")
    public int insertNewTranslation(@Param("ori_id") Integer oriId, @Param("trans") String translation, @Param("usr_id") Integer userId);
}
