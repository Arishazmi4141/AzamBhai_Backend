package e_commerce.khilat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import e_commerce.khilat.entity.Job;
import e_commerce.khilat.entity.PaymentStatus;
import e_commerce.khilat.entity.WorkStatus;

@Repository
public interface JobRepo extends JpaRepository<Job, String> {

    List<Job> findByCustomerId(String customerId);

    List<Job> findByCustomerIdAndWorkStatus(String customerId, WorkStatus workStatus);

    List<Job> findByCustomerIdAndPaymentStatus(String customerId, PaymentStatus paymentStatus);

    List<Job> findByCustomerIdAndWorkStatusAndPaymentStatus(String customerId, WorkStatus workStatus, PaymentStatus paymentStatus);

    List<Job> findByWorkStatus(WorkStatus workStatus);

    List<Job> findByPaymentStatus(PaymentStatus paymentStatus);

    List<Job> findByWorkStatusAndPaymentStatus(WorkStatus workStatus, PaymentStatus paymentStatus);

    long countByWorkStatus(WorkStatus workStatus);

    long countByPaymentStatus(PaymentStatus paymentStatus);
}