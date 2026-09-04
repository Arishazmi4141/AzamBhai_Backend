package e_commerce.khilat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import e_commerce.khilat.entity.Customer;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, String> {

    List<Customer> findByNameContainingIgnoreCase(String name);
}