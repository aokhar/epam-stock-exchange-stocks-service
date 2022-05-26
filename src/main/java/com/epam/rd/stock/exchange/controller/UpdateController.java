package com.epam.rd.stock.exchange.controller;

import com.epam.rd.stock.exchange.dto.ValuableUpdateDto;
import com.epam.rd.stock.exchange.facade.UpdateFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UpdateController {

    @Value("${developer.mode}")
    private boolean developerMode;

    private final UpdateFacade updateFacade;

    @PostMapping("/update")
    public String update(@RequestBody ValuableUpdateDto valuableUpdateDto){
        if(!developerMode){
            return "Not available";
        }

        updateFacade.update(valuableUpdateDto);
        return "updated";
    }


}
