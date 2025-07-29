package org.study.hydrowarehouse.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.study.hydrowarehouse.dao.UserDao;
import org.study.hydrowarehouse.entity.*;
import org.study.hydrowarehouse.entity.Dto.UserCompanyDto;
import org.study.hydrowarehouse.entity.Dto.UserDto;
import org.study.hydrowarehouse.exception.CoreException;
import org.study.hydrowarehouse.exception.ExceptionMessages;
import org.study.hydrowarehouse.service.*;
import org.study.hydrowarehouse.utill.StringUtils;

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

    private static final String ISO_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String USER_STATUS_ACTIVE = "ACTIVE";

    private final UserDao userDao;

    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    private final ServiceMediator serviceMediator;

    @Autowired
    public UserServiceImpl(UserDao userDao, RoleService roleService, PasswordEncoder passwordEncoder, ServiceMediator serviceMediator) {
        this.userDao = userDao;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.serviceMediator = serviceMediator;
    }

    @Override
    public boolean create(UserDto userDto) throws CoreException {
        User user = new User();
        user.setRole(addRoleToNewUser());

        String password = userDto.getPassword();
        if (password != null && !password.isEmpty()) {
            user.setPassword(passwordEncoder.encode(password));
        }

        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());

        user.setPathPhoto(userDto.getPathPhoto());
        user.setStatus(serviceMediator.findByStatus(USER_STATUS_ACTIVE.toUpperCase()).orElseThrow(
                () -> new CoreException(String.format(
                                            ExceptionMessages.USER_STATUS_NOT_FOUND_MESSAGE,
                                            USER_STATUS_ACTIVE))));

        user.setRegistration( getLocalDate());

        if (userDto.getUserCompanyDto() != null) {
            user.setUserCompany(addCompanyToUser(userDto.getUserCompanyDto()));
        }
        return userDao.save(user) > 0;
    }

    @Override
    public boolean update(UserDto userDto) throws CoreException {
        User existingUser = userDao.getUserById(userDto.getUserDtoId())
                .orElseThrow(() -> {
                    //logger
                    throw new CoreException(String.format(
                                                ExceptionMessages.USER_NOT_FOUND_BY_ID_MESSAGE,
                                                userDto.getUserDtoId()));
                });
        existingUser.setFirstName(StringUtils.isBlankOrNullText(userDto.getFirstName())
                ? existingUser.getFirstName() : userDto.getFirstName());
        existingUser.setLastName(StringUtils.isBlankOrNullText(userDto.getLastName())
                ? existingUser.getLastName() : userDto.getLastName());
        existingUser.setEmail(StringUtils.isBlankOrNullText(userDto.getEmail())
                ? existingUser.getEmail() : userDto.getEmail());
        existingUser.setPassword(StringUtils.isBlankOrNullText(userDto.getPassword())
                ? existingUser.getPassword() : passwordEncoder.encode(userDto.getPassword()));
        existingUser.setPathPhoto(StringUtils.isBlankOrNullText(userDto.getPathPhoto())
                ? existingUser.getPathPhoto() : userDto.getPathPhoto());

        UserCompanyDto userCompanyDto = userDto.getUserCompanyDto();
        UserCompany currentCompany = existingUser.getUserCompany();

        if (userCompanyDto != null && (currentCompany == null || !Objects.equals(currentCompany.getUserCompanyId(), userCompanyDto.getCompanyDtoId()))) {
            existingUser.setUserCompany(addCompanyToUser(userCompanyDto));
        }

        return userDao.update(existingUser);
    }

    @Override
    public List<UserDto> findAll(int offset, int limit) throws CoreException {
        return mapToListObjectsDto(userDao.users(limit, offset));
    }

    @Override
    public List<UserDto> findAllByCountry(int countryId, int offset, int limit) throws CoreException {
        return mapToListObjectsDto(userDao.findByCountry(countryId, limit, offset));
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

    @Override
    public boolean changeStatus(Integer userId, String newStatus) throws CoreException {
        User user = userDao.getUserById(userId).orElseThrow(() -> {
            //logger
            throw new CoreException(String.format(
                                        ExceptionMessages.USER_NOT_FOUND_BY_ID_MESSAGE,
                                        userId));
        });
        UserStatus status = serviceMediator.findByStatus(newStatus).orElseThrow(() -> {
            //logger
            throw new CoreException(String.format(
                                        ExceptionMessages.USER_STATUS_NOT_FOUND_MESSAGE,
                                        newStatus));
        });
        user.setStatus(status);
        userDao.update(user);
        return true;
    }

    @Override
    public boolean changeRole(Integer userId, String newRole) throws CoreException {
        User user = userDao.getUserById(userId).orElseThrow(() -> {
            //logger
            throw new CoreException(String.format(
                                        ExceptionMessages.USER_NOT_FOUND_BY_ID_MESSAGE,
                                        userId));
        });
        Role role = serviceMediator.findRole(parseRole(newRole)).orElseThrow(() -> {
            //logger
            throw new CoreException(String.format(
                                        ExceptionMessages.USER_ROLE_NOT_EXIST_MESSAGE,
                                        newRole));
        });
        user.setRole(role);
        userDao.update(user);
        return true;
    }

    @Override
    public List<UserDto> findAllByRole(String role, int offset, int limit) throws CoreException {
        ERole eRole = parseRole(role);
        return mapToListObjectsDto(userDao.findAllByRole(eRole, limit, offset));
    }

    private ERole parseRole(String role) {
        try {
            return ERole.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new CoreException(String.format(
                                        ExceptionMessages.USER_ROLE_NOT_EXIST_MESSAGE,
                                        role));
        }
    }

    @Override
    public List<UserDto> findAllByStatus(String status, int offset, int limit) throws CoreException {
        return mapToListObjectsDto(userDao.findByStatus(status.toUpperCase(), limit, offset));
    }

    /**
     * The method creates a company type DTO from a company for transport between layers.
     * @param userCompany is the company type.
     * @return the CompanyDto.
     */
    protected UserCompanyDto addCompanyDtoToUserDto(UserCompany userCompany) {
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
                () -> new CoreException(String.format(
                                            ExceptionMessages.USER_ROLE_NOT_EXIST_MESSAGE,
                                            ERole.USER)));
    }

    /**
     * The method adds the company to the current user.
     * @param userCompanyDto contains some information for the company.
     * @return The Company instance.
     */
    UserCompany addCompanyToUser(UserCompanyDto userCompanyDto) {
        Integer companyId = userCompanyDto.getCompanyDtoId();
        UserCompany userCompany = null;
        if (companyId != null) {
            return serviceMediator.findUserCompanyById(companyId).orElseThrow(() -> {
                //logger
                throw new CoreException(String.format(
                        ExceptionMessages.USER_COMPANY_NOT_FOUND_MESSAGE,
                        companyId,
                        userCompanyDto.getName(),
                        userCompanyDto.getAddress()));
            });
        } else {
            userCompany = new UserCompany();
            userCompany.setName(userCompanyDto.getName());
            userCompany.setAddress(userCompanyDto.getAddress());
            userCompany.setCountry(serviceMediator.findCountryById(userCompanyDto.getCountryDto().getCountryId()).orElseThrow(() -> {
                //logger
                throw new CoreException(String.format(
                                            ExceptionMessages.COUNTRY_NOT_FOUND_MESSAGE,
                                            userCompanyDto.getCountryDto().getCountryId(),
                                            userCompanyDto.getCountryDto().getName()));
            }));
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
        userDto.setStatus(object.getStatus().getStatus());

        if (object.getUserCompany() != null) {
            userDto.setUserCompanyDto(addCompanyDtoToUserDto(object.getUserCompany()));
        }

        if (object.getRole() != null) {
            userDto.setRole(mapRoles(object.getRole()));
        } else {
            //logging
            throw new CoreException(String.format(
                                        ExceptionMessages.USER_ROLE_NOT_EXIST_MESSAGE,
                                        object.getRole()));
        }
        return userDto;
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
                throw new CoreException(String.format(
                                            ExceptionMessages.USER_ROLE_NOT_EXIST_MESSAGE,
                                            roleName));
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
