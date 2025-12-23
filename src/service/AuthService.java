package service;

import dao.UserDAO;
import model.User;

public class AuthService {

    public User login(String username, String password) {
        User user = UserDAO.findByUsername(username);

        if (user == null) {
            return null;
        }

        if (user.getPasswordHash().equals(password)) {
            return user;
        }

        return null;
    }
}