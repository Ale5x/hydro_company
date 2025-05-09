package org.study.hydro.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
@Transactional
public class UserServiceImpl  extends EntityMapper<UserDto, User> implements UserService {

    private final UserDao userDao;

    private final RoleService roleService;

    private final UserCompanyService userCompanyService;

    private final PasswordEncoder passwordEncoder;

    private static final String ISO_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String USER_NOT_FOUND_BY_ID_ERROR = "User by id not found.";
    private static final String USER_ROLE_NOT_EXIST_ERROR = "The User's role doesn't exist. The role=%s";

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
    public List<UserDto> findAll(int offset, int limit) throws CoreException {
        return mapToListObjectsDto(userDao.users(limit, offset));
    }

    @Override
    public Optional<UserDto> findUserById(int id) throws CoreException {
        Optional<User> user = userDao.getUserById(id);
        if (user.isEmpty()) {
            // logger
            return Optional.empty();
        }
        return user.map(this::mapToObjectDto);
    }

    @Override
    public Optional<UserDto> findUserByEmail(String email) {
        Optional<User> user = userDao.getUserByEmail(email);
        if (user.isEmpty()) {
            return Optional.empty();
        }
        return user.map(this::mapToObjectDto);
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
                () -> new CoreException(USER_ROLE_NOT_EXIST_ERROR));
    }

    /**
     * The method adds the company to the current user.
     * @param userCompanyDto contains some information for the company.
     * @return The Company instance.
     */
    private UserCompany addCompanyToUser(UserCompanyDto userCompanyDto) {
        Integer companyId = userCompanyDto.getCompanyDtoId();
        UserCompany userCompany = null;

        if (companyId != null && companyId > 0) {
            Optional<UserCompany> existingCompany = userCompanyService.findCompanyById(companyId);
            if (existingCompany.isPresent()) {
                userCompany = existingCompany.get();
            }
        } else {
            userCompany = new UserCompany();
            userCompany.setName(userCompanyDto.getName());
            userCompany.setAddress(userCompanyDto.getAddress());
        }
        return userCompany;
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

        if (object.getUserCompany() != null) {
            userDto.setUserCompanyDto(addCompanyDtoToUserDto(object.getUserCompany()));
        }

        if (object.getRole() != null) {
            userDto.setRole(mapRoles(object.getRole()));
        } else {
            //logging
            throw new CoreException(String.format(USER_ROLE_NOT_EXIST_ERROR, object.getRole()));
        }
        return userDto;
    }

    @Override
    public User mapToEntityFromDto(UserDto objectDto, boolean isUpdate) {
        User user = new User();

        if (isUpdate && objectDto.getUserDtoId() != null) {
            user.setUserId(objectDto.getUserDtoId());
            user.setRole(extractUserRole(objectDto));
        } else {
            user.setRole(addRoleToNewUser());
        }

        String password = objectDto.getPassword();
        if (password != null && !password.isEmpty()) {
            user.setPassword(passwordEncoder.encode(password));
        }

        user.setFirstName(objectDto.getFirstName());
        user.setLastName(objectDto.getLastName());
        user.setEmail(objectDto.getEmail());

        user.setPathPhoto(objectDto.getPathPhoto());

        user.setRegistration(objectDto.getRegistration() != null
                ? objectDto.getRegistration()
                : getLocalDate());

        if (objectDto.getUserCompanyDto() != null) {
            user.setUserCompany(addCompanyToUser(objectDto.getUserCompanyDto()));
        }
        return user;
    }

    /**
     * Resolves a {@link Role} entity from the provided {@link UserDto}.
     * <p>
     * This method is intended to be used during user update or creation to determine
     * the appropriate role to assign based on the role names passed in the DTO.
     * It assumes that the DTO may contain a single role name (as a string) within a collection.
     * </p>
     *
     * <p>
     * The method performs the following:
     * <ul>
     *   <li>If the DTO contains a role name, it converts it to an {@link ERole} enum,</li>
     *   <li>Fetches the corresponding {@link Role} from the {@code roleService},</li>
     *   <li>Throws a {@link CoreException} if the role cannot be found,</li>
     *   <li>If no role is provided in the DTO, returns a default role of {@code ERole.USER}.</li>
     * </ul>
     * </p>
     *
     * @param objectDto the data transfer object containing user role information
     * @return a {@link Role} entity corresponding to the provided role name, or default {@code ERole.USER} if none is provided
     * @throws CoreException if the specified role is not found in the system
     */
    private Role extractUserRole(UserDto objectDto) throws CoreException {
        if (objectDto.getRole() != null && !objectDto.getRole().isEmpty()) {
            String roleName = objectDto.getRole().iterator().next();
            ERole roleEnum = ERole.valueOf(roleName.toUpperCase());
            Optional<Role> role = roleService.findRole(roleEnum);

            if (role.isPresent()) {
               return role.get();
            } else {
                // logging
                throw new CoreException(String.format(USER_ROLE_NOT_EXIST_ERROR, roleName));
            }
        }
        return new Role(ERole.USER);
    }

/**
 * Maps the role information from the given {@link UserDto} to a {@link Role} entity. If the DTO contains a role,
 * it attempts to resolve the corresponding {@link Role} entity from the {@link RoleService} using the {@link ERole}
 * enum. If no role is present in the DTO, or if the role cannot be found, the method will return a default role (USER)
 * or throw a {@link CoreException}, respectively.
 *
 * @param dto the {@link UserDto} containing role information
 * @return a resolved {@link Role} entity based on the DTO input
 * @throws CoreException if the role specified in the DTO does not exist in the system
 */
    protected Role mapUserRole(UserDto dto) throws CoreException {
        return extractUserRole(dto);
    }
}
