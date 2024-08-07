package org.study.hydro.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.study.hydro.dao.UserDao;
import org.study.hydro.entity.Company;
import org.study.hydro.entity.Dto.CompanyDto;
import org.study.hydro.entity.Dto.UserDto;
import org.study.hydro.entity.ERole;
import org.study.hydro.entity.Role;
import org.study.hydro.entity.User;
import org.study.hydro.exception.CoreException;
import org.study.hydro.service.CompanyService;
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
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleService roleService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String ISO_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    @Override
    public boolean create(UserDto userDto) throws CoreException {
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setPathPhoto(userDto.getPathPhoto());
        user.setRegistration(getLocalDate());
        user.setRole(addRoleToNewUser());
        user.setCompany(addCompanyToUser(userDto.getCompanyDto()));

        return userDao.save(user) > 0;
    }

    @Override
    public List<UserDto> findAll(int limit, int offset) {
        return buildUserDto(userDao.users(limit, offset));
    }

    @Override
    public Optional<UserDto> findUserById(int id) {
        Optional<User> user = userDao.getUserById(id);
        return user.map(value -> buildUserDto(Collections.singletonList(value)).get(0));
    }

    @Override
    public Optional<UserDto> findUserByEmail(String email) {
        Optional<User> user = userDao.getUserByEmail(email);
        return user.map(value -> buildUserDto(Collections.singletonList(value)).get(0));
    }

    /**
     * The method creates a list of the user type DTO from the user's list for transport between layers.
     * @param userList contains users.
     * @return The list of the users Dto.
     */
    private List<UserDto> buildUserDto(List<User> userList) {
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user : userList) {
            UserDto userDto = new UserDto();
            userDto.setUserDtoId(user.getUserId());
            userDto.setFirstName(user.getFirstName());
            userDto.setLastName(user.getLastName());
            userDto.setEmail(user.getEmail());
            userDto.setPathPhoto(user.getPathPhoto());
            userDto.setRegistration(user.getRegistration());

            userDto.setCompanyDto(addCompanyDtoToUserDto(user.getCompany()));

            userDto.setRole(mapRoles(user.getRole()));

            userDtoList.add(userDto);
        }
        return userDtoList;
    }

    /**
     * The method creates a company type DTO from a company for transport between layers.
     * @param company is the company type.
     * @return the CompanyDto.
     */
    private CompanyDto addCompanyDtoToUserDto(Company company) {
        CompanyDto companyDto = new CompanyDto();
        companyDto.setCompanyDtoId(company.getCompanyId());
        companyDto.setName(company.getName());
        companyDto.setAddress(company.getAddress());
        return companyDto;
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
     * @param companyDto contains some information for the company.
     * @return The Company instance.
     */
    private Company addCompanyToUser(CompanyDto companyDto) {
        Optional<CompanyDto> company = companyService.findById(companyDto.getCompanyDtoId());
        if(company.isPresent()) {
            return new Company(companyDto.getCompanyDtoId(),
                    companyDto.getName(),
                    companyDto.getAddress());
        } else {
            return new Company(companyDto.getName(),
                    companyDto.getAddress());
        }
    }
}
