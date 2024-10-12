package com.mycompany.app.dao.interfaces;

import com.mycompany.app.Dto.UserDTO;


public interface UserDao {

   

    public UserDTO findUserById(long userId) throws Exception;

    public boolean existsByUserName(UserDTO userDto) throws Exception ;

    public void createUser(UserDTO userDto) throws Exception;

    public void updateUser(UserDTO userDto) throws Exception;

    public void deleteUser(long id) throws Exception ;

    public UserDTO findByUserName(UserDTO userDto) throws Exception;

    

}
