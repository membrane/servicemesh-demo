package de.predic8.example.servicemesh.b.b.repository;

import de.predic8.example.servicemesh.b.b.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository extends JpaRepository<Bill, Long> {

    Bill findOneByReceiverUsernameAndMonth(String receiverUsername, String month);
}
