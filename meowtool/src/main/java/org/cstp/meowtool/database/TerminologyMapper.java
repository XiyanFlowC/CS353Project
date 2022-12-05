package org.cstp.meowtool.database;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface TerminologyMapper {
    @Select("SELECT * FROM terms WHERE proj_id = proj_id")
    public Terminology[] selectAllTerminologies (Integer projId);

    @Select("SELECT * FROM terms WHERE id=#{id}")
    public Terminology selectTerminology (Integer id);

    @Select("SELECT * FROM terms WHERE proj_id=#{proj_id} AND ori_word=#{ori_word}")
    public Terminology selectByOriWord (@Param("proj_id") Integer projId, @Param("ori_word") String oriWord);

    @Insert("INSERT INTO terms (proj_id, ori_word, tar_word, comment, commiter) VALUES (#{proj_id}, #{ori_word}, #{tar_word}, #{comment}, #{commiter})")
    public int insertTerminology (Terminology term);

    @Delete("DELETE FROM terms WHERE id=#{id}")
    public int deleteTerminology (Integer id);

    @Update("UPDATE terms SET proj_id=#{proj_id}, ori_word=#{ori_word}, tar_word=#{tar_word}, comment=#{comment}, commiter=#{commiter} WHERE id=#{id}")
    public int updateTerminology (Terminology term);

    @Update("UPDATE terms SET comment = #{comment} WHERE id=#{id}")
    public int updateComment (@Param("id") Integer id, @Param("comment") String comment);
}
