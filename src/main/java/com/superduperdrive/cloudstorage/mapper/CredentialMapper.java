package com.superduperdrive.cloudstorage.mapper;

import com.superduperdrive.cloudstorage.model.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {
    @Select("SELECT * FROM CREDENTIALS where userId = #{userId}")
    public List<Credential> getCredentialsByUserId(int userId);

    @Select("SELECT * FROM CREDENTIALS where credentialId = #{credentialId}")
    public Credential getCredentialById(int credentialId);

    @Insert("INSERT INTO CREDENTIALS (url, username, key, password, userId) VALUES " +
            "(#{url},#{username},#{key},#{password},#{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    public int addCredential(Credential credential);

    @Update("UPDATE CREDENTIALS SET url = #{url}," +
            " username = #{username}, password = #{password} " +
            "WHERE credentialId = #{credentialId}")
    public int updateCredentialById(Credential credential);

    @Delete("DELETE FROM CREDENTIALS where credentialId = #{credentialId}")
    public int deleteCredentialById(Integer credentialId);
}