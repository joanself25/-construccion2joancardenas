package com.mycompany.appcs2.App.dao.interfaces;

import com.mycompany.appcs2.App.Dto.UserDTO;

public interface Userdao {

    public UserDTO findByUserName(UserDTO userDto) throws Exception;

    public boolean existsByUserName(UserDTO userDto) throws Exception;

    public void createUser(UserDTO userDto) throws Exception;

}
