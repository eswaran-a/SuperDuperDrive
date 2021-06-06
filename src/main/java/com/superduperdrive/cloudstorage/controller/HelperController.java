package com.superduperdrive.cloudstorage.controller;

import com.superduperdrive.cloudstorage.model.AppUser;
import com.superduperdrive.cloudstorage.model.Credential;
import com.superduperdrive.cloudstorage.services.CredentialService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelperController {
    private CredentialService credentialService;

    public HelperController(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    @RequestMapping(value = "/decrypt/{credentialId}",method = RequestMethod.GET)
    public ResponseEntity<?> deleteCredential(@PathVariable("credentialId") Integer credentialId,
                                   Authentication authentication) {

        Integer loggedInUserId = ((AppUser) authentication.getPrincipal()).getUserId();

        Credential toBeDecrypted = credentialService.getCredentialById(credentialId);
        if(toBeDecrypted !=null && toBeDecrypted.getUserId() == loggedInUserId) {

            String decryptedPassword = credentialService.getDecryptedPassword(toBeDecrypted.getPassword(), toBeDecrypted.getKey());

            return ResponseEntity.ok().body(decryptedPassword);
        } else {
            return new ResponseEntity<String>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
    }
}
