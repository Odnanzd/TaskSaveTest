package com.example.tasksave.test.servicos;

import org.mindrot.jbcrypt.BCrypt;

public class ServicosSenhaCriptografia {


    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }
}
