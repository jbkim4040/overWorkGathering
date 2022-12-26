package com.overWorkGathering.main.controller;

import com.overWorkGathering.main.DTO.WorkCollectionDtlReqDTO;
import com.overWorkGathering.main.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/excel")
public class ExcelController {

    @Autowired
    private ExcelService excelService;

    @PostMapping(value = "/workCollection")
    public void createOverworkGatheringExcel(@RequestBody Map<String, Object> workCollectionDtl, HttpServletResponse response){
        excelService.createExcel(workCollectionDtl, response);
    }
}
