package com.superduperdrive.cloudstorage.controller;

import com.superduperdrive.cloudstorage.model.AppUser;
import com.superduperdrive.cloudstorage.model.Credential;
import com.superduperdrive.cloudstorage.services.CredentialService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/credentials")
public class CredentialController {

    private CredentialService credentialService;

    public CredentialController(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    @GetMapping()
    public String loadHomePage() {
        return "redirect:/home";
    }

    @PostMapping
    public String addOrUpdateCredential(@ModelAttribute Credential credential,
                                Model model, Authentication authentication,
                                RedirectAttributes redirectAttributes) {

        Integer loggedInUserId = ((AppUser)authentication.getPrincipal()).getUserId();
        credential.setUserId(loggedInUserId);

        int rowAddedorUpdated = credentialService.addOrUpdateCredential(credential);

        if(rowAddedorUpdated > 0) {
            redirectAttributes.addFlashAttribute ("opSuccessMsg", "Credential added / updated successfully !!!");
        } else {
            redirectAttributes.addFlashAttribute ("opErrorMsg", "Error in adding / updating the requested credential !!!");
        }
        redirectAttributes.addFlashAttribute ("isCredentialsActive", true);
        return "redirect:/home";
    }

    @RequestMapping(value = "/delete/{credentialId}",method = RequestMethod.GET)
    public String deleteCredential(@PathVariable("credentialId") Integer credentialId,
                                   Authentication authentication,
                                   RedirectAttributes redirectAttributes) {

        Integer loggedInUserId = ((AppUser)authentication.getPrincipal()).getUserId();

        Credential toBeDeleted = credentialService.getCredentialById(credentialId);

        if(toBeDeleted !=null && toBeDeleted.getUserId() == loggedInUserId) {
            credentialService.deleteCredential(credentialId);
            redirectAttributes.addFlashAttribute ("opSuccessMsg", "Credential deleted successfully !!!");
        } else {
            redirectAttributes.addFlashAttribute ("opErrorMsg", "Error in deleting the requested Credential !!!");
        }
        redirectAttributes.addFlashAttribute ("isCredentialsActive", true);
        return "redirect:/home";
    }
}
