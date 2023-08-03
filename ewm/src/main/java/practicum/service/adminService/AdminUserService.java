package practicum.service.adminService;


import org.springframework.data.domain.Pageable;
import practicum.model.NewUserRequest;
import practicum.model.UserDto;


import java.util.List;

public interface AdminUserService {

    UserDto postUser(NewUserRequest newUserRequest);

    List<UserDto> getUsers(List<Long> ids, Pageable pageable);

    void deleteUser(long userId);

}