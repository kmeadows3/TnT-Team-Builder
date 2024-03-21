package my.TNTBuilder.dao;

import my.TNTBuilder.model.userModels.RegisterUserDto;
import my.TNTBuilder.model.userModels.User;

import java.util.List;

public interface UserDao {

    List<User> getUsers();

    User getUserById(int id);

    User getUserByUsername(String username);

    int getUserIdByUsername(String username);
    User createUser(RegisterUserDto user);






}
