package com.superduperdrive.cloudstorage.services;

import com.superduperdrive.cloudstorage.mapper.CredentialMapper;
import com.superduperdrive.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {
    private EncryptionService encryptionService;
    private CredentialMapper credentialMapper;

    public CredentialService(EncryptionService encryptionService,
                             CredentialMapper credentialMapper) {
        this.encryptionService = encryptionService;
        this.credentialMapper = credentialMapper;
    }

    public int addOrUpdateCredential(Credential credential) {

        if(credential.getCredentialId()==null) {

            String encodedKey = getEncodedKey();
            credential.setKey(encodedKey);
            credential.setPassword(getEncryptedPassword(credential.getPassword(), encodedKey));
            System.out.println(credential);
            return credentialMapper.addCredential(credential);

        } else {
            Credential toUpdate = credentialMapper.getCredentialById(credential.getCredentialId());
            if(toUpdate.getUserId() == credential.getUserId()) {
                String encodedKey = toUpdate.getKey();

                toUpdate.setUrl(credential.getUrl());
                toUpdate.setUsername(credential.getUsername());
                toUpdate.setPassword(getEncryptedPassword(credential.getPassword(), encodedKey));

                return credentialMapper.updateCredentialById(toUpdate);
            } else {
                return -1;
            }
        }
    }

    private String getEncodedKey() {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey  = Base64.getEncoder().encodeToString(key);
        return encodedKey;
    }

    private String getEncryptedPassword(String password, String encodedKey) { return encryptionService.encryptValue(password, encodedKey); }

    public String getDecryptedPassword(String password, String encodedKey) { return encryptionService.decryptValue(password, encodedKey); }

    public int deleteCredential(Integer credentialId) {
        return credentialMapper.deleteCredentialById(credentialId);
    }

    public List<Credential> getCredentialsByUserId(Integer userId) { return credentialMapper.getCredentialsByUserId(userId); }

    public Credential getCredentialById(int credentialId) {
        return credentialMapper.getCredentialById(credentialId);
    }
}
