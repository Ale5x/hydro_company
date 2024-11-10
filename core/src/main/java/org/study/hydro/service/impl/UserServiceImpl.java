package org.study.hydro.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.study.hydro.dao.UserDao;
import org.study.hydro.entity.*;
import org.study.hydro.entity.Dto.UserCompanyDto;
import org.study.hydro.entity.Dto.UserDto;
import org.study.hydro.exception.CoreException;
import org.study.hydro.service.EntityMapper;
import org.study.hydro.service.UserCompanyService;
import org.study.hydro.service.RoleService;
import org.study.hydro.service.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


/**
 * The class {@link UserServiceImpl} implements methods of the UserService interface.
 * The class is annotated as a service, which qualifies it to be automatically created by component-scanning.
 *
 * @author Aliaksandr Pishchala
 */
@Service
public class UserServiceImpl  extends EntityMapper<UserDto, User> implements UserService {

    private final UserDao userDao;

    private final RoleService roleService;

    private final UserCompanyService userCompanyService;

    private final PasswordEncoder passwordEncoder;

    private static final String ISO_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String USER_NOT_FOUND_BY_ID_ERROR = "User by id not found.";

    @Autowired
    public UserServiceImpl(UserDao userDao, RoleService roleService, UserCompanyService userCompanyService,
                           PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.roleService = roleService;
        this.userCompanyService = userCompanyService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean create(UserDto userDto) throws CoreException {
        return userDao.save(mapToEntityFromDto(userDto, false)) > 0;
    }

    @Override
    public List<UserDto> findAll(int limit, int offset) throws CoreException {
        return mapToListObjectsDto(userDao.users(limit, offset));
    }

    @Override
    public Optional<UserDto> findUserById(int id) throws CoreException {
        return Optional.of(mapToObjectDto(userDao.getUserById(id)
                .orElseThrow(() -> new CoreException(USER_NOT_FOUND_BY_ID_ERROR))));
    }

    @Override
    public Optional<UserDto> findUserByEmail(String email) {
        return Optional.of(mapToObjectDto(userDao.getUserByEmail(email)
                .orElseThrow(() -> new CoreException(USER_NOT_FOUND_BY_ID_ERROR))));
    }

    /**
     * The method creates a company type DTO from a company for transport between layers.
     * @param userCompany is the company type.
     * @return the CompanyDto.
     */
    private UserCompanyDto addCompanyDtoToUserDto(UserCompany userCompany) {
        UserCompanyDto userCompanyDto = new UserCompanyDto();
        userCompanyDto.setCompanyDtoId(userCompany.getUserCompanyId());
        userCompanyDto.setName(userCompany.getName());
        userCompanyDto.setAddress(userCompany.getAddress());

        return userCompanyDto;
    }

    /**
     * The method returns the list of the collection of the Role's name
     * @param role is user's role.
     * @return the collection of the strings of the Role's name.
     */
    private Collection<String> mapRoles(Role role) {
        return new ArrayList<>(Collections.singleton(role.getName().name()));
    }

    /**
     * The method returns the current time according to the ISO format.
     * @return current time.
     */
    private LocalDateTime getLocalDate() {
        return LocalDateTime.parse(LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern(ISO_TIME_FORMAT)));
    }

    /**
     * The method adds the role to the new user.
     * @return The Role instance.
     */
    private Role addRoleToNewUser() {
        return roleService.findRole(ERole.USER).orElseThrow(
                () -> new CoreException("'User' role doesn't exist"));
    }

    /**
     * The method adds the company to the current user.
     * @param userCompanyDto contains some information for the company.
     * @return The Company instance.
     */
    private UserCompany addCompanyToUser(UserCompanyDto userCompanyDto) {
        Optional<UserCompanyDto> company = userCompanyService.findById(userCompanyDto.getCompanyDtoId());
        if(company.isPresent()) {
            return new UserCompany(userCompanyDto.getCompanyDtoId(),
                    userCompanyDto.getName(),
                    userCompanyDto.getAddress());
                    //add country;
        } else {
            return new UserCompany(userCompanyDto.getName(),
                    userCompanyDto.getAddress());
        }
    }

    @Override
    public List<UserDto> mapToListObjectsDto(List<User> objectsList) {
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user : objectsList) {
            userDtoList.add(mapToObjectDto(user));
        }
        return userDtoList;
    }

    @Override
    public UserDto mapToObjectDto(User object) {
        UserDto userDto = new UserDto();

        userDto.setUserDtoId(object.getUserId());
        userDto.setFirstName(object.getFirstName());
        userDto.setLastName(object.getLastName());
        userDto.setEmail(object.getEmail());
        userDto.setPathPhoto(object.getPathPhoto());
        userDto.setRegistration(object.getRegistration());

        userDto.setUserCompanyDto(addCompanyDtoToUserDto(object.getUserCompany()));

        userDto.setRole(mapRoles(object.getRole()));
        return userDto;
    }

    @Override
    public User mapToEntityFromDto(UserDto objectDto, boolean isUpdate) {
        User user = new User();

        if (isUpdate) {
            user.setUserId(objectDto.getUserDtoId());
        }
        if (objectDto.getPassword() != "") {
            user.setPassword(passwordEncoder.encode(objectDto.getPassword()));
        }

        user.setFirstName(objectDto.getFirstName());
        user.setLastName(objectDto.getLastName());
        user.setEmail(objectDto.getEmail());

        user.setPathPhoto(objectDto.getPathPhoto());
        user.setRegistration(getLocalDate());
        user.setRole(addRoleToNewUser());
        user.setUserCompany(addCompanyToUser(objectDto.getUserCompanyDto()));
        return user;
    }
}
