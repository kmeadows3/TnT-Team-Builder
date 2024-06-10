package my.TNTBuilder.dao;

import my.TNTBuilder.exception.DaoException;
import my.TNTBuilder.model.userModels.RegisterUserDto;
import my.TNTBuilder.model.userModels.User;

import java.util.List;

public interface UserDao {

    List<User> getUsers() throws DaoException;

    User getUserById(int id) throws DaoException;

    User getUserByUsername(String username) throws DaoException;

    int getUserIdByUsername(String username) throws DaoException;
    User createUser(RegisterUserDto user) throws DaoException;






}
