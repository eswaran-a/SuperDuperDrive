package com.superduperdrive.cloudstorage.controller;

import com.superduperdrive.cloudstorage.model.AppUser;
import com.superduperdrive.cloudstorage.model.Note;
import com.superduperdrive.cloudstorage.services.NotesService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/notes")
public class NotesController {

    private NotesService notesService;

    public NotesController(NotesService notesService) {
        this.notesService = notesService;
    }

    @GetMapping()
    public String loadHomePage() {
        return "redirect:/home";
    }

    @PostMapping
    public String addOrUpdateNote(@ModelAttribute Note note,
                          RedirectAttributes redirectAttributes,
                          Authentication authentication) {

        Integer loggedInUserId = ((AppUser)authentication.getPrincipal()).getUserId();
        note.setUserId(loggedInUserId);

        int rowAddedorUpdated = notesService.addOrUpdateNote(note);
        if(rowAddedorUpdated > 0) {
            redirectAttributes.addFlashAttribute ("opSuccessMsg", "Note added / updated successfully !!!");
        } else {
            redirectAttributes.addFlashAttribute ("opErrorMsg", "Error in adding / updating the requested note !!!");
        }
        redirectAttributes.addFlashAttribute ("isNotesActive", true);
        return "redirect:/home";
    }

    @RequestMapping(value = "/delete/{noteId}",method = RequestMethod.GET)
    public String deleteNote(@PathVariable("noteId") Integer noteId,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {

        Integer loggedInUserId = ((AppUser)authentication.getPrincipal()).getUserId();

        Note toBeDeleted = notesService.getNoteById(noteId);

        if(toBeDeleted !=null && toBeDeleted.getUserId() == loggedInUserId) {
            notesService.deleteNote(noteId);
            redirectAttributes.addFlashAttribute ("opSuccessMsg", "Note deleted successfully !!!");
        } else {
            redirectAttributes.addFlashAttribute ("opErrorMsg", "Error in deleting the requested Note !!!");
        }
        redirectAttributes.addFlashAttribute ("isNotesActive", true);
        return "redirect:/home";
    }
}
