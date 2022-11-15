package org.cstp.meowtool.database.templates;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM users WHERE id=#{id}")
    public User selectId(Integer id);

    @Select("SELECT * FROM users WHERE email=#{email}")
    public User selectEmail(String email);

    @Select("SELECT * FROM users WHERE username=#{name}")
    public User selectName(String name);

    @Insert("INSERT INTO users (username, email, role, password, salt, rating, disable) VALUES (#{username}, #{email}, #{role}, #{rating}, #{password}, #{salt}, #{disable})")
    public int insertUser(User newUser);

    @Update("UPDATE users SET username=#{username}, role=#{role}, password=#{password}, salt=#{salt}, rating=#{rating}, disable=#{disable} WHERE id=#{id}")
    public int updateUser(User user);

    @Delete("DELETE FROM users WHERE id=#{id}")
    public int deleteUserById(Integer id);

    @Delete("DELETE FROM users WHERE name=#{name}")
    public int deleteUser(String name);

    @Select("SELECT * FROM users")
    public User[] selectAll();
}
