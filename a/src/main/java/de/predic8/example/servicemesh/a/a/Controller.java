package de.predic8.example.servicemesh.a.a;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class Controller {

    @Value("${b.baseurl}")
    String bBaseUrl;

    @Value("${c.baseurl}")
    String cBaseUrl;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ObjectMapper om;

    @GetMapping(path = "/v1/bill", produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody
    ResponseEntity<Resource> getBill(
            HttpServletRequest request,
            @RequestParam String month,
            @RequestParam String receiverUsername) throws IOException {
        // TODO: make sure that only the user $receiverUsername can actually call this

        ResponseEntity<Map> billResponse = restTemplate.exchange(
                bBaseUrl + "/v1/bills?receiverUsername={receiverUsername}&month={month}",
                HttpMethod.GET,
                new HttpEntity<Object>(newMultiValueMap("X-username", receiverUsername, request)),
                Map.class,
                ImmutableMap.of(
                        "month", month,
                        "receiverUsername", receiverUsername));

        HashMap pdfRequest = new HashMap(billResponse.getBody());
        pdfRequest.put("filenameBase", "rechnung-" + month);
        pdfRequest.put("billNo", pdfRequest.get("no"));

        ResponseEntity<List> positionsResponse = restTemplate.exchange(
                bBaseUrl + "/v1/bills/{no}/positions",
                HttpMethod.GET,
                new HttpEntity<Object>(newMultiValueMap("X-username", receiverUsername, request)),
                List.class,
                ImmutableMap.of(
                        "no", pdfRequest.get("no")));

        List<Map> positions = positionsResponse.getBody();

        Integer nettoSum = positions.stream().map(m -> (Integer) m.get("amount")).reduce(0, Integer::sum);
        Integer ustFactor = positions.stream().map(m -> (Integer) m.get("ustFactor")).reduce(null, (i1, i2) -> {
            if (i1 != null && !i1.equals(i2))
                throw new RuntimeException("Bill with different tax factors is not supported.");
            return i2;
        });
        if (ustFactor <= 0 || ustFactor > 99)
            throw new RuntimeException("ustFactor out of range [1..99] " + ustFactor + ".");
        Integer ustSum = nettoSum * ustFactor / 100;

        pdfRequest.put("amountNetto", formatCurrency(nettoSum));
        pdfRequest.put("ustFactor", ustFactor);
        pdfRequest.put("amountUst", formatCurrency(ustSum));
        pdfRequest.put("amountBrutto", formatCurrency(nettoSum + ustSum));

        pdfRequest.put("positions", positions
            .stream()
                .map(m -> {
                    m.put("amountNetto", formatCurrency(m.get("amount")));
                    return m;
                })
                .collect(Collectors.toList()));

        ResponseEntity<Resource> pdfResponse = restTemplate.exchange(
                cBaseUrl + "/v1/pdf",
                HttpMethod.POST,
                new HttpEntity<Object>(
                        pdfRequest,
                        newMultiValueMap("X-username", receiverUsername, request)),
                Resource.class);

        return pdfResponse;
    }

    private String[] FORWARD_HEADERS = new String[] {
            "x-client-ip", // a header we use to indicate the IP the original HTTP request came from
            "x-b3-traceid", "x-b3-spanid", "x-b3-sampled", "x-b3-parentspanid" // headers used for internal tracing
    };

    private MultiValueMap<String, String> newMultiValueMap(String k, String v, HttpServletRequest request) {
        MultiValueMap<String, String> res = new HttpHeaders();
        res.add(k, v);

        for (String header : FORWARD_HEADERS) {
            String value = request.getHeader(header);
            if (value != null) {
                res.add(header, value);
            }
        }

        return res;
    }

    private String formatCurrency(Object amount) {
        return formatCurrency((Integer)amount);
    }

    private String formatCurrency(long amount) {
        return formatCurrency(Long.toString(amount));
    }

    private String formatCurrency(Integer amount) {
        return formatCurrency(amount.toString());
    }

    private String formatCurrency(String amount) {
        return amount.replaceAll("(\\d\\d)$", ",$1");
    }
}
