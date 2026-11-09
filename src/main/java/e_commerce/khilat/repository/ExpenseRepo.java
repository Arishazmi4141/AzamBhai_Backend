package e_commerce.khilat.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import e_commerce.khilat.entity.Expense;

@Repository
public interface ExpenseRepo extends JpaRepository<Expense, String> {

    @Query("select coalesce(sum(e.amount), 0) from Expense e")
    BigDecimal sumAll();

    @Query("select coalesce(sum(e.amount), 0) from Expense e " +
           "where e.expenseDate between :from and :to")
    BigDecimal sumBetween(@Param("from") LocalDate from, @Param("to") LocalDate to);

    @Query("select e from Expense e " +
           "where (lower(e.paidTo) like lower(concat('%', :keyword, '%')) " +
           "   or lower(e.purpose) like lower(concat('%', :keyword, '%'))) " +
           "and e.expenseDate between :from and :to " +
           "order by e.expenseDate desc, e.createdAt desc")
    List<Expense> search(@Param("keyword") String keyword,
                         @Param("from") LocalDate from,
                         @Param("to") LocalDate to);
}