package de.predic8.example.servicemesh.b.b;

import de.predic8.example.servicemesh.b.b.entity.Bill;
import de.predic8.example.servicemesh.b.b.entity.BillPosition;
import de.predic8.example.servicemesh.b.b.repository.BillPositionRepository;
import de.predic8.example.servicemesh.b.b.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class Controller {

    @Autowired
    BillRepository billRepository;

    @Autowired
    BillPositionRepository billPositionRepository;

    @GetMapping(path="/v1/bills/{no}")
    public Bill getBill(@PathVariable("no") long no) {
        return billRepository.getOne(no);
    }

    @GetMapping(path="/v1/bills")
    public Bill getBill(@RequestParam("receiverUsername") String receiverUsername, @RequestParam("month") String month) {
        return billRepository.findOneByReceiverUsernameAndMonth(receiverUsername, month);
    }

    @GetMapping(path="/v1/bills/{no}/positions")
    public List<BillPosition> getBillPositions(@PathVariable("no") long no) {
        return billPositionRepository.getAllByBillNoOrderByNo(no);
    }

}
