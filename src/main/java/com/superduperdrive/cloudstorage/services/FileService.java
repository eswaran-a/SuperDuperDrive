package com.superduperdrive.cloudstorage.services;

import com.superduperdrive.cloudstorage.mapper.FileMapper;
import com.superduperdrive.cloudstorage.model.File;
import com.superduperdrive.cloudstorage.model.Note;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class FileService {
    private FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }
    public int addNewFile(File file) { return fileMapper.insertFile(file); }
    public int deleteFile(Integer fileId) {
        return fileMapper.deleteFile(fileId);
    }

    public List<File> getFilesByUserId(int userId) {
        return fileMapper.getFilesByUserId(userId);
    }

    public File getFileById(int fileId) { return fileMapper.getFileById(fileId);}
    public File getFileByNameAndUserId(String fileName, int userId) { return fileMapper.getFileByNameAndUserId(fileName, userId); }

}
