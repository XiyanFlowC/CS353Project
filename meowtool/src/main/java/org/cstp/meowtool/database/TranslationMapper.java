package org.cstp.meowtool.database;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TranslationMapper {
    @Select("SELECT * FROM translations WHERE id = #{id}")
    public Translation selectTranslation(Integer id);

    @Select("SELECT * FROM translations WHERE ori_id = #{oriId} ORDER BY id DESC LIMIT 1")
    public Translation selectByOriId(Integer oriId);

    @Select("SELECT * FROM translations WHERE ori_id = #{oriId} ORDER BY id DESC")
    public Translation[] selectAllTranslationOfOriId(Integer oriId);

    @Insert("INSERT INTO translations (ori_id, trans, commiter) VALUES (#{ori_id}, #{trans}, #{usr_id})")
    public int insertNewTranslation(@Param("ori_id") Integer oriId, @Param("trans") String translation, @Param("usr_id") Integer userId);

    @Select("SELECT * FROM translations ORDER BY levenshtein_less_equal(trans, #{target}, #{thershold}) ASC LIMIT #{limit}")
    public Translation[] selectSimilar(@Param("target") String target, @Param("thershold") Integer thershold, @Param("limit") Integer limit);
}
