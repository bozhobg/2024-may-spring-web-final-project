package bg.softuni.recipe.explorer.model.dto;

import bg.softuni.recipe.explorer.model.enums.RoleEnum;

import java.util.ArrayList;
import java.util.List;

public class UserInfoDTO {

    private Long id;

    private String username;

    private String firstName;

    private String lastName;

    private String email;

    private List<RoleEnum> roleNames;

    public UserInfoDTO() {
    }

    public Long getId() {
        return id;
    }

    public UserInfoDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserInfoDTO setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public UserInfoDTO setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public UserInfoDTO setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserInfoDTO setEmail(String email) {
        this.email = email;
        return this;
    }

    public List<RoleEnum> getRoleNames() {
        return roleNames;
    }

    public UserInfoDTO setRoleNames(List<RoleEnum> roleNames) {
        this.roleNames = roleNames;
        return this;
    }

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }
}
