package com.overWorkGathering.main.service;

import com.overWorkGathering.main.DTO.WorkCollectionDtlReqDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Service
public class ExcelService {
    // 날짜 계산 함수
    private int[] calculateDate (int year, int month, int day){
        Calendar cal = Calendar.getInstance();
        cal.set(year, month-1, day); //월은 -1해줘야 해당월로 인식

        int[] rtn = new int[2];
        rtn[0] = cal.getMinimum(Calendar.DAY_OF_MONTH);
        rtn[1] = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        return rtn;
    }

    public String createExcel(List<WorkCollectionDtlReqDTO> workCollectionDtl) throws IOException {
        HashMap<String, List<WorkCollectionDtlReqDTO>> workCollectionListMap = new HashMap<>();
        List<WorkCollectionDtlReqDTO> workCollection;
        String name = "";

        for(WorkCollectionDtlReqDTO dto : workCollectionDtl){
            if(!name.equals(dto.getName())){
                workCollection = new ArrayList<WorkCollectionDtlReqDTO>();
                workCollectionListMap.put(dto.getName(), workCollection);
            }

            //workCollection.add(dto);
        }

        Set<String> keySet = workCollectionListMap.keySet();

        System.out.println("map 변환 >>> " + workCollectionListMap.toString());

        int year = 2022;
        int month = 8;
        int day = 1;

        int[] rtn = calculateDate(year, month, day);

        Workbook workbook = new XSSFWorkbook();

        // 지출내역서 excel sheet 생성
        Sheet outcomeHistorySheet = workbook.createSheet("지출내역서");
        outcomeHistorySheet.setColumnWidth(0, 5000);
        outcomeHistorySheet.setColumnWidth(1, 4000);
        outcomeHistorySheet.setColumnWidth(2, 4000);
        outcomeHistorySheet.setColumnWidth(3, 4000);
        outcomeHistorySheet.setDisplayGridlines(false);

        //헤더//
        Row header = outcomeHistorySheet.createRow(0);
        Row header2 = outcomeHistorySheet.createRow(1);

        // 헤더 제목 셀 생성
        Cell headerTitleCell = header.createCell(0);
        outcomeHistorySheet.addMergedRegion(new CellRangeAddress(0,1,0,0));
        headerTitleCell.setCellValue("지출 내역서");

        // 헤더 제목 셀 스타일
        CellStyle headerTitleStyle = workbook.createCellStyle();
        headerTitleStyle.setAlignment(HorizontalAlignment.CENTER);
        headerTitleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // 헤더 제목 셀 폰트
        Font headerTitleFont = workbook.createFont();
        //headerTitleFont.setFontHeight((short) 14);
        headerTitleFont.setBold(true);
        headerTitleFont.setFontName("돋음");
        //headerTitleFont.setUnderline(Font.U_SINGLE);
        headerTitleStyle.setFont(headerTitleFont);

        // 헤더 제목 셀 스타일, 폰트 적용
        headerTitleCell.setCellStyle(headerTitleStyle);


        // 헤더 입안 셀 생성
        Cell headerPlanningCell = header.createCell(2);
        Cell headerPlanningCell2 = header2.createCell(2);
        headerPlanningCell.setCellValue("입안");

        // 헤더 입안 셀 스타일
        CellStyle headerPlanningStyle = workbook.createCellStyle();
        headerPlanningStyle.setAlignment(HorizontalAlignment.CENTER);
        headerPlanningStyle.setBorderBottom(BorderStyle.THIN);
        headerPlanningStyle.setBorderTop(BorderStyle.THIN);
        headerPlanningStyle.setBorderLeft(BorderStyle.THIN);
        headerPlanningStyle.setBorderRight(BorderStyle.THIN);

        // 헤더 입안 셀 폰트
        Font headerPlanningFont = workbook.createFont();
        headerPlanningFont.setBold(true);
        headerPlanningFont.setFontName("돋음");
        headerPlanningStyle.setFont(headerPlanningFont);

        // 헤더 입안 셀 스타일, 폰트 적용
        headerPlanningCell.setCellStyle(headerPlanningStyle);
        headerPlanningCell2.setCellStyle(headerPlanningStyle);

        // 헤더 원장 셀 생성
        Cell headerLedgerCell = header.createCell(3);
        Cell headerLedgerCell2 = header2.createCell(3);
        headerLedgerCell.setCellValue("팀장");

        // 헤더 원장 셀 스타일
        CellStyle headerLedgerStyle = workbook.createCellStyle();
        headerLedgerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerLedgerStyle.setBorderBottom(BorderStyle.THIN);
        headerLedgerStyle.setBorderTop(BorderStyle.THIN);
        headerLedgerStyle.setBorderLeft(BorderStyle.THIN);
        headerLedgerStyle.setBorderRight(BorderStyle.THIN);

        // 헤더 원장 셀 폰트
        Font headerLedgerFont = workbook.createFont();
        headerLedgerFont.setBold(true);
        headerLedgerFont.setFontName("돋음");
        headerLedgerStyle.setFont(headerLedgerFont);

        // 헤더 원장 셀 스타일, 폰트 적용
        headerLedgerCell.setCellStyle(headerLedgerStyle);
        headerLedgerCell2.setCellStyle(headerLedgerStyle);

        // 바디
        Row body = outcomeHistorySheet.createRow(3);
        Cell bodyCell = body.createCell(3);
        bodyCell.setCellValue("작성자 :");

        outcomeHistorySheet.addMergedRegion(new CellRangeAddress(5,6,0,0));
        outcomeHistorySheet.addMergedRegion(new CellRangeAddress(5,5,1,2));
        outcomeHistorySheet.addMergedRegion(new CellRangeAddress(5,6,3,3));

        Row body1Row = outcomeHistorySheet.createRow(5);

        // 바디 타이틀 셀 - 발의일자
        Cell body1Cell = body1Row.createCell(0);
        body1Cell.setCellValue("발의일자");

        // 바디 타이틀 스타일 - 발의일자
        CellStyle body1Style = workbook.createCellStyle();
        body1Style.setAlignment(HorizontalAlignment.CENTER);
        body1Style.setVerticalAlignment(VerticalAlignment.CENTER);
        body1Style.setBorderTop(BorderStyle.THICK);
        body1Style.setBorderLeft(BorderStyle.THICK);
        body1Style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
        body1Style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // 바디 타이틀 셀 폰트 - 발의일자
        Font body1Font = workbook.createFont();
        //headerTitleFont.setFontHeight((short) 14);
        body1Font.setBold(true);
        //headerTitleFont.setUnderline(Font.U_SINGLE);
        body1Font.setFontName("돋음");
        body1Style.setFont(body1Font);

        body1Cell.setCellStyle(body1Style);


        // 바디 타이틀 셀 - 금액
        Cell body2Cell = body1Row.createCell(1);
        Cell body2_1Cell = body1Row.createCell(2);
        body2Cell.setCellValue("금액");

        // 바디 타이틀 스타일 - 금액
        CellStyle body2Style = workbook.createCellStyle();
        body2Style.setAlignment(HorizontalAlignment.CENTER);
        body2Style.setVerticalAlignment(VerticalAlignment.CENTER);
        body2Style.setFillBackgroundColor((short) 3);
        //body1Style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        body2Style.setBorderTop(BorderStyle.THICK);
        body2Style.setBorderLeft(BorderStyle.THIN);
        body2Style.setBorderRight(BorderStyle.THIN);
        body2Style.setBorderBottom(BorderStyle.THIN);
        body2Style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
        body2Style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // 바디 타이틀 셀 폰트 - 금액
        Font body2Font = workbook.createFont();
        //headerTitleFont.setFontHeight((short) 14);
        body2Font.setBold(true);
        body2Font.setFontName("돋음");
        //headerTitleFont.setUnderline(Font.U_SINGLE);
        body2Style.setFont(body2Font);

        body2Cell.setCellStyle(body2Style);

        CellStyle body2_1Style = workbook.createCellStyle();
        body2_1Style.setBorderTop(BorderStyle.THICK);
        body2_1Cell.setCellStyle(body2_1Style);

        // 바디 타이틀 셀 - 비고
        Cell body3Cell = body1Row.createCell(3);
        body3Cell.setCellValue("비고");

        // 바디 타이틀 스타일 - 비고
        CellStyle body3Style = workbook.createCellStyle();
        body3Style.setAlignment(HorizontalAlignment.CENTER);
        body3Style.setVerticalAlignment(VerticalAlignment.CENTER);
        body3Style.setFillBackgroundColor((short) 3);
        //body1Style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        body3Style.setBorderTop(BorderStyle.THICK);
        body3Style.setBorderRight(BorderStyle.THICK);
        body3Style.setBorderLeft(BorderStyle.THIN);
        body3Style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
        body3Style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // 바디 타이틀 셀 폰트 - 비고
        Font body3Font = workbook.createFont();
        //headerTitleFont.setFontHeight((short) 14);
        body3Font.setBold(true);
        body3Font.setFontName("돋음");
        //headerTitleFont.setUnderline(Font.U_SINGLE);
        body3Style.setFont(body3Font);

        body3Cell.setCellStyle(body3Style);


        Row body2Row = outcomeHistorySheet.createRow(6);

        // 바디 타이틀 셀 - 발의일자
        Cell body4Cell = body2Row.createCell(0);

        // 바디 타이틀 스타일 - 발의일자
        CellStyle body4Style = workbook.createCellStyle();
        body4Style.setFillBackgroundColor((short) 3);
        //body1Style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        body4Style.setBorderLeft(BorderStyle.THICK);
        body4Style.setBorderBottom(BorderStyle.THIN);
        body4Style.setBorderRight(BorderStyle.THIN);
        body4Cell.setCellStyle(body4Style);


        // 바디 타이틀 셀 - 야근식대
        Cell body5Cell = body2Row.createCell(1);
        body5Cell.setCellValue("야근식대 (원)");

        // 바디 타이틀 스타일 - 야근식대
        CellStyle body5Style = workbook.createCellStyle();
        body5Style.setAlignment(HorizontalAlignment.CENTER);
        body5Style.setVerticalAlignment(VerticalAlignment.CENTER);
        body5Style.setFillBackgroundColor((short) 3);
        //body1Style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        body5Style.setBorderTop(BorderStyle.THIN);
        body5Style.setBorderRight(BorderStyle.THIN);
        body5Style.setBorderBottom(BorderStyle.THIN);
        body5Style.setBorderLeft(BorderStyle.THIN);
        body5Style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
        body5Style.setFillPattern(FillPatternType.SOLID_FOREGROUND);


        // 바디 타이틀 셀 폰트 - 야근식대
        Font body5Font = workbook.createFont();
        //headerTitleFont.setFontHeight((short) 14);
        body5Font.setBold(true);
        body5Font.setFontName("돋음");
        //headerTitleFont.setUnderline(Font.U_SINGLE);
        body5Style.setFont(body5Font);
        body5Cell.setCellStyle(body5Style);


        // 바디 타이틀 셀 - 교통비
        Cell body6Cell = body2Row.createCell(2);
        body6Cell.setCellValue("교통비 (원)");

        // 바디 타이틀 스타일 - 교통비
        CellStyle body6Style = workbook.createCellStyle();
        body6Style.setAlignment(HorizontalAlignment.CENTER);
        body6Style.setVerticalAlignment(VerticalAlignment.CENTER);
        body6Style.setFillBackgroundColor((short) 3);
        //body1Style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        body6Style.setBorderTop(BorderStyle.THIN);
        body6Style.setBorderRight(BorderStyle.THIN);
        body6Style.setBorderBottom(BorderStyle.THIN);
        body6Style.setBorderLeft(BorderStyle.THIN);
        body6Style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
        body6Style.setFillPattern(FillPatternType.SOLID_FOREGROUND);


        // 바디 타이틀 셀 폰트 - 교통비
        Font body6Font = workbook.createFont();
        //headerTitleFont.setFontHeight((short) 14);
        body6Font.setBold(true);
        body6Font.setFontName("돋음");
        //headerTitleFont.setUnderline(Font.U_SINGLE);
        body6Style.setFont(body6Font);

        body6Cell.setCellStyle(body6Style);


        // 바디 타이틀 셀 - 발의일자
        Cell body7Cell = body2Row.createCell(3);

        // 바디 타이틀 스타일 - 발의일자
        CellStyle body7Style = workbook.createCellStyle();
        body7Style.setFillBackgroundColor((short) 3);
        //body1Style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        body7Style.setBorderRight(BorderStyle.THICK);
        body7Style.setBorderBottom(BorderStyle.THIN);
        body7Style.setBorderLeft(BorderStyle.THIN);
        body7Cell.setCellStyle(body7Style);


        CellStyle monthCellStyle = workbook.createCellStyle();
        monthCellStyle.setAlignment(HorizontalAlignment.RIGHT);
        monthCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        monthCellStyle.setBorderTop(BorderStyle.THIN);
        monthCellStyle.setBorderRight(BorderStyle.THIN);
        monthCellStyle.setBorderBottom(BorderStyle.THIN);
        monthCellStyle.setBorderLeft(BorderStyle.THIN);

        CellStyle monthLeftCellStyle = workbook.createCellStyle();
        monthLeftCellStyle.setAlignment(HorizontalAlignment.CENTER);
        monthLeftCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        monthLeftCellStyle.setBorderTop(BorderStyle.THIN);
        monthLeftCellStyle.setBorderRight(BorderStyle.THIN);
        monthLeftCellStyle.setBorderBottom(BorderStyle.THIN);
        monthLeftCellStyle.setBorderLeft(BorderStyle.THICK);

        CellStyle monthRightCellStyle = workbook.createCellStyle();
        monthRightCellStyle.setBorderTop(BorderStyle.THIN);
        monthRightCellStyle.setBorderRight(BorderStyle.THICK);
        monthRightCellStyle.setBorderBottom(BorderStyle.THIN);
        monthRightCellStyle.setBorderLeft(BorderStyle.THIN);

        int amt = 0;
        int dinnerAmt = 0;
        int transportAmt = 0;
        String MM = month < 10 ? "0" + month : Integer.toString(month);

        // 월별 동적 셀 생성
        // 금액 저장시 yyyyMMdd 형식 지키기
        for(int i = rtn[0]; i <= rtn[1]; i++){
            String dd = i < 10 ? "0" + i : Integer.toString(i);

            Row body3Row = outcomeHistorySheet.createRow(6 + i);

            Cell body8Cell = body3Row.createCell(0);
            body8Cell.setCellStyle(monthLeftCellStyle);
            body8Cell.setCellValue(year + "년" + MM + "월" + dd + "일");

            Cell body9Cell = body3Row.createCell(1);
            body9Cell.setCellStyle(monthCellStyle);
            body9Cell.setCellValue(i);		// 야근식대 금액 세팅


            Cell body10Cell = body3Row.createCell(2);
            body10Cell.setCellStyle(monthCellStyle);
            body10Cell.setCellValue(i);		// 교통비 금액 세팅


            Cell body11Cell = body3Row.createCell(3);
            body11Cell.setCellStyle(monthRightCellStyle);

            dinnerAmt += i;	// 야근식대 금액 취합
            transportAmt += i;	// 교통비 금액 취합
        }
        amt += dinnerAmt;
        amt += transportAmt;

        // 총합계 ROW
        Row body4Row = outcomeHistorySheet.createRow(6 + rtn[1] + 1);


        // 바디 타이틀 셀 - 총합계
        Cell body12Cell = body4Row.createCell(0);

        // 바디 타이틀 스타일 - 총합계
        CellStyle body12Style = workbook.createCellStyle();
        body12Style.setBorderTop(BorderStyle.THICK);
        body12Style.setBorderLeft(BorderStyle.THICK);
        body12Style.setBorderBottom(BorderStyle.THICK);
        body12Style.setBorderRight(BorderStyle.THIN);
        body12Cell.setCellStyle(body12Style);


        // 바디 타이틀 셀 - 총합계
        Cell body13Cell = body4Row.createCell(1);
        body13Cell.setCellValue(dinnerAmt);

        // 바디 타이틀 스타일 - 총합계
        CellStyle body13Style = workbook.createCellStyle();
        body13Style.setBorderTop(BorderStyle.THICK);
        body13Style.setBorderBottom(BorderStyle.THICK);
        body13Style.setBorderRight(BorderStyle.THIN);
        body13Style.setBorderLeft(BorderStyle.THIN);
        body13Style.setAlignment(HorizontalAlignment.RIGHT);
        body13Style.setVerticalAlignment(VerticalAlignment.CENTER);
        body13Cell.setCellStyle(body13Style);


        // 바디 타이틀 셀 - 총합계
        Cell body14Cell = body4Row.createCell(2);
        body14Cell.setCellValue(transportAmt);

        // 바디 타이틀 스타일 - 총합계
        CellStyle body14Style = workbook.createCellStyle();
        body14Style.setBorderTop(BorderStyle.THICK);
        body14Style.setBorderBottom(BorderStyle.THICK);
        body14Style.setBorderRight(BorderStyle.THIN);
        body14Style.setBorderLeft(BorderStyle.THIN);
        body14Style.setAlignment(HorizontalAlignment.RIGHT);
        body14Style.setVerticalAlignment(VerticalAlignment.CENTER);
        body14Cell.setCellStyle(body14Style);


        // 바디 타이틀 셀 - 총합계
        Cell body15Cell = body4Row.createCell(3);

        // 바디 타이틀 스타일 - 총합계
        CellStyle body15Style = workbook.createCellStyle();
        body15Style.setBorderTop(BorderStyle.THICK);
        body15Style.setBorderBottom(BorderStyle.THICK);
        body15Style.setBorderRight(BorderStyle.THICK);
        body15Style.setBorderLeft(BorderStyle.THIN);
        body15Cell.setCellStyle(body15Style);


        // 총합계 ROW
        Row body5Row = outcomeHistorySheet.createRow(6 + rtn[1] + 2);
        outcomeHistorySheet.addMergedRegion(new CellRangeAddress(6 + rtn[1] + 2,6 + rtn[1] + 2,1,2));

        // 바디 타이틀 셀 - 총합계
        Cell body16Cell = body5Row.createCell(0);
        body16Cell.setCellValue("총합계 (원)");

        // 바디 타이틀 스타일 - 총합계
        XSSFCellStyle body16Style = (XSSFCellStyle) workbook.createCellStyle();
        body16Style.setBorderTop(BorderStyle.THICK);
        body16Style.setBorderLeft(BorderStyle.THICK);
        body16Style.setBorderBottom(BorderStyle.THICK);
        body16Style.setBorderRight(BorderStyle.THIN);
        body16Style.setAlignment(HorizontalAlignment.CENTER);
        body16Style.setVerticalAlignment(VerticalAlignment.CENTER);

        // custom 배경색
        body16Style.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 255, 153)));	// 255 255 153
        body16Style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // 바디 타이틀 셀 폰트 - 총합계
        Font body16Font = workbook.createFont();
        //headerTitleFont.setFontHeight((short) 14);
        body16Font.setBold(true);
        body16Font.setFontName("돋음");
        //headerTitleFont.setUnderline(Font.U_SINGLE);
        body16Style.setFont(body16Font);

        body16Cell.setCellStyle(body16Style);

        // 바디 타이틀 셀
        Cell body17Cell = body5Row.createCell(1);
        body17Cell.setCellValue(amt);

        // 바디 타이틀 스타일
        XSSFCellStyle body17Style = (XSSFCellStyle) workbook.createCellStyle();
        body17Style.setBorderTop(BorderStyle.THICK);
        body17Style.setBorderBottom(BorderStyle.THICK);
        body17Style.setBorderLeft(BorderStyle.THIN);
        body17Style.setAlignment(HorizontalAlignment.RIGHT);
        body17Style.setVerticalAlignment(VerticalAlignment.CENTER);
        body17Cell.setCellStyle(body17Style);

        // custom 배경색
        body17Style.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 255, 153)));	// 255 255 153
        body17Style.setFillPattern(FillPatternType.SOLID_FOREGROUND);


        // 바디 타이틀 셀
        Cell body18Cell = body5Row.createCell(2);

        // 바디 타이틀 스타일
        XSSFCellStyle body18Style = (XSSFCellStyle) workbook.createCellStyle();
        body18Style.setBorderTop(BorderStyle.THICK);
        body18Style.setBorderBottom(BorderStyle.THICK);
        body18Style.setBorderRight(BorderStyle.THIN);
        body18Cell.setCellStyle(body18Style);

        // custom 배경색
        body18Style.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 255, 153)));	// 255 255 153
        body18Style.setFillPattern(FillPatternType.SOLID_FOREGROUND);


        // 바디 타이틀 셀
        Cell body19Cell = body5Row.createCell(3);

        // 바디 타이틀 스타일
        XSSFCellStyle body19Style = (XSSFCellStyle) workbook.createCellStyle();
        body19Style.setBorderTop(BorderStyle.THICK);
        body19Style.setBorderBottom(BorderStyle.THICK);
        body19Style.setBorderRight(BorderStyle.THICK);
        body19Cell.setCellStyle(body19Style);

        // custom 배경색
        body19Style.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 255, 153)));	// 255 255 153
        body19Style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // 결재일자 ROW
        Row body6Row = outcomeHistorySheet.createRow(6 + rtn[1] + 4);
        outcomeHistorySheet.addMergedRegion(new CellRangeAddress(6 + rtn[1] + 4,6 + rtn[1] + 4,1,3));

        // 바디 타이틀 셀 - 결재일자
        Cell body20Cell = body6Row.createCell(0);
        body20Cell.setCellValue("결재일자");

        // 바디 타이틀 스타일 - 결재일자
        XSSFCellStyle body20Style = (XSSFCellStyle) workbook.createCellStyle();
        body20Style.setFillBackgroundColor((short) 3);
        //body1Style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        body20Style.setBorderTop(BorderStyle.THICK);
        body20Style.setBorderLeft(BorderStyle.THICK);
        body20Style.setBorderBottom(BorderStyle.THICK);
        body20Style.setBorderRight(BorderStyle.THIN);
        body20Style.setAlignment(HorizontalAlignment.CENTER);
        body20Style.setVerticalAlignment(VerticalAlignment.CENTER);

        // custom 배경색
        body20Style.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 255, 153)));	// 255 255 153
        body20Style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // 바디 타이틀 셀 폰트 - 결재일자
        Font body20Font = workbook.createFont();
        //headerTitleFont.setFontHeight((short) 14);
        body20Font.setBold(true);
        body20Font.setFontName("돋음");
        //headerTitleFont.setUnderline(Font.U_SINGLE);
        body20Style.setFont(body20Font);

        body20Cell.setCellStyle(body20Style);


        // 바디 타이틀 셀
        Cell body21Cell = body6Row.createCell(1);
        body21Cell.setCellValue(year + "년" + month + "월" + day + "일");

        // 바디 타이틀 스타일
        XSSFCellStyle body21Style = (XSSFCellStyle) workbook.createCellStyle();
        body21Style.setFillBackgroundColor((short) 3);
        //body1Style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        body21Style.setBorderTop(BorderStyle.THICK);
        body21Style.setBorderBottom(BorderStyle.THICK);
        body21Style.setBorderLeft(BorderStyle.THIN);
        body21Style.setAlignment(HorizontalAlignment.CENTER);
        body21Style.setVerticalAlignment(VerticalAlignment.CENTER);

        // custom 배경색
        body21Style.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 255, 153)));	// 255 255 153
        body21Style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        body21Cell.setCellStyle(body21Style);


        // 바디 타이틀 셀
        Cell body22Cell = body6Row.createCell(2);

        // 바디 타이틀 스타일
        XSSFCellStyle body22Style = (XSSFCellStyle) workbook.createCellStyle();
        body22Style.setFillBackgroundColor((short) 3);
        //body1Style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        body22Style.setBorderTop(BorderStyle.THICK);
        body22Style.setBorderBottom(BorderStyle.THICK);

        // custom 배경색
        body22Style.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 255, 153)));	// 255 255 153
        body22Style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        body22Cell.setCellStyle(body22Style);


        // 바디 타이틀 셀
        Cell body23Cell = body6Row.createCell(3);

        // 바디 타이틀 스타일
        XSSFCellStyle body23Style = (XSSFCellStyle) workbook.createCellStyle();
        body23Style.setFillBackgroundColor((short) 3);
        //body1Style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        body23Style.setBorderTop(BorderStyle.THICK);
        body23Style.setBorderRight(BorderStyle.THICK);
        body23Style.setBorderBottom(BorderStyle.THICK);
        body23Style.setBorderLeft(BorderStyle.THIN);

        // custom 배경색
        body23Style.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 255, 153)));	// 255 255 153
        body23Style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        body23Cell.setCellStyle(body23Style);


        // 근태관리 excel sheet 생성
        Sheet workManagementSheet = workbook.createSheet("근태관리");


        // 근태관리 excel sheet 생성
        Sheet taxiFeeReceiptSheet = workbook.createSheet("택시비 영수증");




        String yyyyMMdd = Integer.toString(year) + (month > 9 ? month : "0" + Integer.toString(month)) + (rtn[1] > 9 ? rtn[1] : "0" + Integer.toString(rtn[1]));

        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        String fileLocation = path.substring(0, path.length()-1) + name + "야근식대" + yyyyMMdd + ".xlsx";

        System.out.println("엑셀 생성 경로 >>>> " + fileLocation);
        FileOutputStream outputStream = new FileOutputStream(fileLocation);
        workbook.write(outputStream);
        workbook.close();


        return "성공";
    }
}
