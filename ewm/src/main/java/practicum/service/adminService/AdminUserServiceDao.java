package practicum.service.adminService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practicum.utility.mappers.UserMapper;
import practicum.model.NewUserRequest;
import practicum.model.UserDto;
import practicum.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserServiceDao implements AdminUserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDto postUser(NewUserRequest newUserRequest) {
        return UserMapper.toUserDto(userRepository.save(UserMapper.fromNewUserRequest(newUserRequest)));
    }

    @Override
    @Transactional
    public List<UserDto> getUsers(List<Long> ids, Pageable pageable) {

        if (ids.isEmpty())
            return userRepository.findAll(pageable).stream().map(UserMapper::toUserDto).collect(Collectors.toList());

        return userRepository.findAllByIdIsIn(ids, pageable).stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(long userId) {
        userRepository.deleteById(userId);
    }

}