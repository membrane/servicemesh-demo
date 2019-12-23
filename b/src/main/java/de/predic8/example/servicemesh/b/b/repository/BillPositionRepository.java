package de.predic8.example.servicemesh.b.b.repository;

import de.predic8.example.servicemesh.b.b.entity.BillPosition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BillPositionRepository extends JpaRepository<BillPosition, Long> {

    List<BillPosition> getAllByBillNoOrderByNo(long no);
}
