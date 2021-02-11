    package com.bank.agencies.endpoint;

    import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bank.agencies.domain.AgencyGatewayResponse;
import com.bank.agencies.domain.AgencyResponse;
import com.bank.agencies.usecase.FindAllAgenciesUseCase;

    @RestController
    @RequestMapping(value = "/agencies", produces = MediaType.APPLICATION_JSON_VALUE)
    public class AgenciesController {

        private final FindAllAgenciesUseCase findAllAgenciesUseCase;

        public AgenciesController(FindAllAgenciesUseCase findAllAgenciesUseCase) {
            this.findAllAgenciesUseCase = findAllAgenciesUseCase;
        }

        @GetMapping
        @ResponseStatus(HttpStatus.OK)
        public ResponseEntity<Map<String, List<AgencyResponse>>> findAllAgencies() {

            List<AgencyGatewayResponse> agencies = findAllAgenciesUseCase.execute();

            List<AgencyResponse> agencyResponse = agencies.stream()
                    .map(agencyGateway -> AgencyResponse.AgencyResponseBuilder.anAgencyResponse()
                    .bank(agencyGateway.getBank())
                    .name(agencyGateway.getName())
                    .city(agencyGateway.getCity()).build())
                    .collect(Collectors.toList());
            
            Map<String, List<AgencyResponse>> agenciesGroupByState =  agencyResponse.stream().collect(Collectors.groupingBy(AgencyResponse::getState));
            

            return new ResponseEntity<>(agenciesGroupByState, HttpStatus.OK);
        }
        
                
        
    }
