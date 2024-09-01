package com.mycompany.appcs2.App.dao.interfaces;

import com.mycompany.appcs2.App.Dto.UserDTO;
import com.mycompany.appcs2.App.model.Person;
import java.sql.SQLException;

public interface UserDao {

    public UserDTO findByUserName(UserDTO userDto) throws Exception;

    public UserDTO findUserById(long userId) throws Exception;

    public boolean existsByUserName(UserDTO userDto) throws Exception;

    public void createUser(UserDTO userDto) throws Exception;

    public void updateUser(UserDTO userDto) throws Exception;

    

}
