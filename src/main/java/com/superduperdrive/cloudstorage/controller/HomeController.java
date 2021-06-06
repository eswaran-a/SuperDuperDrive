package com.superduperdrive.cloudstorage.controller;

import com.superduperdrive.cloudstorage.model.AppUser;
import com.superduperdrive.cloudstorage.model.Credential;
import com.superduperdrive.cloudstorage.model.File;
import com.superduperdrive.cloudstorage.model.Note;
import com.superduperdrive.cloudstorage.services.CredentialService;
import com.superduperdrive.cloudstorage.services.FileService;
import com.superduperdrive.cloudstorage.services.NotesService;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {

    private FileService fileSerivce;
    private NotesService notesService;
    private CredentialService credentialService;

    public HomeController(FileService fileSerivce, NotesService notesService, CredentialService credentialService) {
        this.fileSerivce = fileSerivce;
        this.notesService = notesService;
        this.credentialService = credentialService;
    }

    @GetMapping()
    public String loadHomePage(Model model, Authentication authentication) {

        Integer userId = ((AppUser)authentication.getPrincipal()).getUserId();

        List<File> fileList = fileSerivce.getFilesByUserId(userId);
        List<Note> noteList = notesService.getNotesByUserId(userId);
        List<Credential> credentialList = credentialService.getCredentialsByUserId(userId);

        model.addAttribute("fileList", fileList);
        model.addAttribute("noteList", noteList);
        model.addAttribute("credentialList", credentialList);

        return "home";
    }
}
