package com.superduperdrive.cloudstorage.mapper;

import com.superduperdrive.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {
    @Select("SELECT * FROM FILES where userId = #{userId}")
    public List<File> getFilesByUserId(int userId);

    @Insert("INSERT INTO FILES (filename, contentType, fileSize, userId, filedata) VALUES " +
            "(#{fileName},#{contentType},#{fileSize},#{userId},#{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    public int insertFile(File file);

    @Delete("DELETE FROM FILES where fileid = #{fileId}")
    public int deleteFile(Integer fileId);

    @Select("SELECT filename FROM FILES WHERE EXISTS " +
            "(SELECT * FROM FILES WHERE fileId = #{fileId}")
    public boolean isFileExists(int fileId);

    @Select("SELECT * FROM FILES where fileid = #{fileId}")
    public File getFileById(int fileId);

    @Select("SELECT * FROM FILES where fileName = #{fileName} and userId = #{userId}")
    public File getFileByNameAndUserId(String fileName, int userId);
}