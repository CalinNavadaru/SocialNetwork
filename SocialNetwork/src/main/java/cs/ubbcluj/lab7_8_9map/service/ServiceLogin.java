package cs.ubbcluj.lab7_8_9map.service;

import cs.ubbcluj.lab7_8_9map.domain.Utilizator;
import cs.ubbcluj.lab7_8_9map.domain.dto.DTOUtilizator;
import cs.ubbcluj.lab7_8_9map.exceptions.LoginException;
import cs.ubbcluj.lab7_8_9map.repository.Repository;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ServiceLogin {

    private final Repository<Long, Utilizator> utilizatorRepository;

    public ServiceLogin(Repository<Long, Utilizator> utilizatorRepository) {
        this.utilizatorRepository = utilizatorRepository;
    }

    public void tryLogin(DTOUtilizator user, String password) throws LoginException {
        if (!user.getPassword().equals(encrypt(password)))
            throw new LoginException("Parola invalida!");
    }

    public String encrypt(String pass){
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        md.update(pass.getBytes());
        byte [] digest = md.digest();
        StringBuilder hexString = new StringBuilder();
        for (byte b : digest) {
            hexString.append(Integer.toHexString(0xFF & b));
        }
        return hexString.toString();
    }
}
