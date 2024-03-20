package my.TNTBuilder.dao;

import my.TNTBuilder.model.RegisterUserDto;
import my.TNTBuilder.model.User;

import java.util.List;

public interface UserDao {

    List<User> getUsers();

    User getUserById(int id);

    User getUserByUsername(String username);

    User createUser(RegisterUserDto user);






}
