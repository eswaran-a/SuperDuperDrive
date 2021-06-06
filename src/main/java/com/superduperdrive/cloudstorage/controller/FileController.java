package com.superduperdrive.cloudstorage.controller;

import com.superduperdrive.cloudstorage.model.AppUser;
import com.superduperdrive.cloudstorage.model.File;
import com.superduperdrive.cloudstorage.services.FileService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/files")
public class FileController {

    private FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping()
    public String loadHomePage() {
        return "redirect:/home";
    }

    @PostMapping
    public String uploadFile(@RequestParam("fileUpload") MultipartFile multipartFile,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {
        Integer loggedInUserId = ((AppUser)authentication.getPrincipal()).getUserId();
        File file = null;
        String opErrorMsg = null;
        String opSuccessMsg = null;
        try {
            if(multipartFile!=null) {
                if(fileService.getFileByNameAndUserId(multipartFile.getOriginalFilename(), loggedInUserId)!=null){
                    opErrorMsg =  "File with name '"+ multipartFile.getOriginalFilename()
                            +"' already exists. File name must be unique !!!";
                } else {
                    if(multipartFile.getSize() == 0) {
                        opErrorMsg = "Error attempting to upload empty file !!!";
                    } else {
                        file = new File(multipartFile.getOriginalFilename(), multipartFile.getContentType(),
                                String.valueOf(multipartFile.getSize()), loggedInUserId, multipartFile.getBytes());
                        int rowAdded = fileService.addNewFile(file);
                        if(rowAdded > 0) {
                            opSuccessMsg = "File uploaded successfully !!!";
                        } else {
                            opErrorMsg = "Error in uploading the requested file !!!";
                        }
                    }
                }
            } else {
                opErrorMsg = "Error in uploading the requested file !!!";
            }
        } catch (IOException e) {
            opErrorMsg = "Error in uploading the requested file !!!";
        }
        if(opErrorMsg!=null) redirectAttributes.addFlashAttribute ("opErrorMsg", opErrorMsg);
        if(opSuccessMsg!=null) redirectAttributes.addFlashAttribute ("opSuccessMsg", opSuccessMsg);

        redirectAttributes.addFlashAttribute ("isFilesActive", true);
        return "redirect:/home";
    }

    @RequestMapping(value = "/delete/{fileId}",method = RequestMethod.GET)
    public String deleteFile(@PathVariable("fileId") Integer fileId,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {
        Integer loggedInUserId = ((AppUser)authentication.getPrincipal()).getUserId();

        File toBeDeleted = fileService.getFileById(fileId);

        if(toBeDeleted !=null && toBeDeleted.getUserId() == loggedInUserId) {
            fileService.deleteFile(fileId);
            redirectAttributes.addFlashAttribute ("opSuccessMsg", "File deleted successfully !!!");
        } else {
            redirectAttributes.addFlashAttribute ("opErrorMsg", "Error in deleting the requested file !!!");
        }
        redirectAttributes.addFlashAttribute ("isFilesActive", true);
        return "redirect:/home";
    }

    @RequestMapping(value="/download/{fileId}", method=RequestMethod.GET)
    public ResponseEntity<?> downloadFile(@PathVariable("fileId") Integer fileId,
                                                 Authentication authentication,
                                                 RedirectAttributes redirectAttributes) {

        Integer loggedInUserId = ((AppUser)authentication.getPrincipal()).getUserId();
        File fileToDownload = fileService.getFileById(fileId);

        if(fileToDownload !=null && fileToDownload.getUserId() == loggedInUserId) {
            ByteArrayResource resource = new ByteArrayResource(fileToDownload.getFileData());

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileToDownload.getFileName());
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(Long.valueOf(fileToDownload.getFileSize()))
                    .body(resource);
        } else {
            return new ResponseEntity<String>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

    }
}
