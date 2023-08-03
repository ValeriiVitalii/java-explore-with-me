package practicum.utility.mappers;


import lombok.experimental.UtilityClass;
import practicum.model.NewUserRequest;
import practicum.model.User;
import practicum.model.UserDto;
import practicum.model.UserShortDto;

@UtilityClass
public class UserMapper {

    public User fromNewUserRequest(NewUserRequest newUserRequest) {
        return User.builder()
                .name(newUserRequest.getName())
                .email(newUserRequest.getEmail())
                .build();
    }

    public UserDto toUserDto(User user) {
        return UserDto.builder()
                .name(user.getName())
                .email(user.getEmail())
                .id(user.getId())
                .build();
    }

    public UserShortDto toUserShortDto(User user) {
        return UserShortDto.builder()
                .name(user.getName())
                .id(user.getId())
                .build();
    }

}
