package com.karmperis.expensetracker.mapper;

import com.karmperis.expensetracker.dto.UserEditDTO;
import com.karmperis.expensetracker.dto.UserInsertDTO;
import com.karmperis.expensetracker.dto.UserReadOnlyDTO;
import com.karmperis.expensetracker.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    /**
     * Maps UserInsertDTO to a user type entity.
     * @param dto Containing the user details.
     * @return A new user entity populated with the DTO data.
     */
    public User mapToEntity(UserInsertDTO dto) {
        User user = new User();
        user.setUsername(dto.username());
        user.setEmail(dto.email());
        user.setPassword(dto.password());
        user.setActive(true);
        return user;
    }

    /**
     * Maps a user entity to a UserReadOnlyDTO.
     * @param user The user entity to be mapped.
     * @return A UserReadOnlyDTO.
     */
    public UserReadOnlyDTO mapToReadOnlyDTO(User user) {
        return new UserReadOnlyDTO(
                user.getUuid(),
                user.getUsername(),
                user.getEmail(),
                user.getActive()
        );
    }

    /**
     * Updates an existing user entity using data from a UserEditDTO.
     * @param user The existing user entity to be updated.
     * @param dto UserEditDTO.
     */
    public void updateEntityFromEditDTO(User user, UserEditDTO dto) {
        user.setEmail(dto.email());
        if (dto.active() != null) {
            user.setActive(dto.active());
        }
    }
}