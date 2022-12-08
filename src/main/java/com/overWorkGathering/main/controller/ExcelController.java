package com.overWorkGathering.main.controller;

import com.overWorkGathering.main.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExcelController {

    @Autowired
    private ExcelService excelService;

    public void createOverworkGatheringExcel(){

    }
}
