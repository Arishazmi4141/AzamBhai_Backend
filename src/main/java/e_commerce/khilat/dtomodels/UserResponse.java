package e_commerce.khilat.dtomodels;

import e_commerce.khilat.entity.User;

public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;

    public static UserResponse fromEntity(User user) {
        UserResponse res = new UserResponse();
        res.id = user.getId();
        res.name = user.getName();
        res.email = user.getEmail();
        res.phone = user.getPhone();
        return res;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
}