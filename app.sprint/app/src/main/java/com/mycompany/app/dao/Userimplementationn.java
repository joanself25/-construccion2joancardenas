package com.mycompany.app.dao;

import com.mycompany.app.Dto.UserDTO;
import com.mycompany.app.Helpers.Helpers;
import com.mycompany.app.model.User;
import com.mycompany.app.dao.interfaces.UserDao;
import com.mycompany.app.dao.repositories.UserRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
@Setter
@Getter
public class Userimplementationn implements UserDao {

    @Autowired
    UserRepository userRepository;

    @Override
    public void createUser(UserDTO userDto) throws Exception {
        User user = Helpers.parse(userDto);
        userRepository.save(user);
        userDto.setId(user.getId());
    }

    @Override //modificar
    public UserDTO findByUserName(UserDTO userDto) throws Exception {
        User user = userRepository.findByUsername(userDto.getUsername());
        return Helpers.parse(user);

    }

    @Override
    public boolean existsByUserName(UserDTO userDto) throws Exception {
        return userRepository.existsByUsername(userDto.getUsername());
    }

    @Override
    public UserDTO findUserById(long userId) throws Exception {
        User user = userRepository.findUserById(userId);
        return Helpers.parse(user);
    }

    @Override
    public void updateUser(UserDTO userDto) throws Exception {
       //Esta línea verifica si el usuario con el ID especificado en el userDto
       //existe en la base de datos.
        if (!userRepository.existsById(userDto.getId())) {
            throw new Exception("User no encontrado con id : " + userDto.getId());
        }
        User user = Helpers.parse(userDto);
        userRepository.save(user);
    }

    @Override
    public void deleteUser(long userId) throws Exception {
        //metodo del repositorio
        if (!userRepository.existsById(userId)) {
            throw new Exception("User no encontrado con id : " + userId);
        }                  //metodo del repositorio 
        userRepository.deleteById(userId);
    }

    /*public List<UserDTO> getAllUsers() throws Exception {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(Helpers::parse)
                .collect(Collectors.toList());
    }
     */
}
