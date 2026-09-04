package e_commerce.khilat.dtomodels;

import java.util.List;
import java.util.stream.Collectors;

import e_commerce.khilat.entity.Customer;

public class CustomerDetailResponse {

    private String id;   // ✅ Long se String
    private String name;
    private String phone;
    private String email;
    private String address;

    private List<CustomerJobResponse> jobs;

    public static CustomerDetailResponse build(Customer customer) {

        CustomerDetailResponse res = new CustomerDetailResponse();

        res.id = customer.getId();
        res.name = customer.getName();
        res.phone = customer.getPhone();
        res.email = customer.getEmail();
        res.address = customer.getAddress();

        if (customer.getJobs() != null) {
            res.jobs = customer.getJobs()
                    .stream()
                    .map(CustomerJobResponse::build)
                    .collect(Collectors.toList());
        } else {
            res.jobs = List.of();
        }

        return res;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }
    public List<CustomerJobResponse> getJobs() { return jobs; }
}