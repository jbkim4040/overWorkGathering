package com.overWorkGathering.main.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.overWorkGathering.main.DTO.WorkCollectionDtlReqDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ExcelService {
    private final String[] alphabet = {
            "A", "B", "C", "D", "E", "F",
            "G", "H", "I", "J", "K", "L",
            "M", "N", "O", "P", "Q", "R",
            "S", "T", "U", "V", "W", "X",
            "Y", "Z"
    };


    public String createExcel(Map<String, Object> workCollectionDtl, HttpServletResponse response) throws IOException {
        String result = "success";
        String name = "";
        Set<String> keySet = workCollectionDtl.keySet();

        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance( );
        cal.add ( cal.MONTH, -1 ); //이전달

        int year = Integer.parseInt(df.format(cal.getTime()).substring(0,4));
        int month = Integer.parseInt(df.format(cal.getTime()).substring(4,6));
        int day = Integer.parseInt(df.format(cal.getTime()).substring(6,8));

        int[] rtn = calculateDate(year, month, day);

        Workbook workbook = new XSSFWorkbook();

        // 지출내역서 excel sheet 생성
        Sheet outcomeHistorySheet = workbook.createSheet("지출내역서");
        outcomeHistorySheet.setColumnWidth(0, 4553);
        outcomeHistorySheet.setColumnWidth(1, 4046);
        outcomeHistorySheet.setColumnWidth(2, 4046);
        outcomeHistorySheet.setColumnWidth(3, 4046);
        outcomeHistorySheet.setDisplayGridlines(false);

        //헤더//
        Row indexRow = outcomeHistorySheet.createRow(0);
        Row header = outcomeHistorySheet.createRow(1);
        Row header2 = outcomeHistorySheet.createRow(2);


        // 헤더 제목 셀 생성
        Cell headerTitleCell = header.createCell(0);
        outcomeHistorySheet.addMergedRegion(new CellRangeAddress(1,2,0,0));
        headerTitleCell.setCellValue("지출 내역서");

        // 헤더 제목 셀 스타일
        CellStyle headerTitleStyle = workbook.createCellStyle();
        headerTitleStyle.setAlignment(HorizontalAlignment.CENTER);
        headerTitleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // 헤더 제목 셀 폰트
        Font headerTitleFont = workbook.createFont();
        headerTitleFont.setBold(true);
        headerTitleFont.setFontName("돋음");
        headerTitleFont.setFontHeightInPoints((short) 14);
        headerTitleFont.setUnderline(Font.U_SINGLE);
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
        Row body = outcomeHistorySheet.createRow(4);
        Cell bodyCell = body.createCell(3);
        bodyCell.setCellValue("작성자 :");

        outcomeHistorySheet.addMergedRegion(new CellRangeAddress(6,7,0,0));
        outcomeHistorySheet.addMergedRegion(new CellRangeAddress(6,6,1,2));
        outcomeHistorySheet.addMergedRegion(new CellRangeAddress(6,7,3,3));

        Row body1Row = outcomeHistorySheet.createRow(6);

        // 바디 타이틀 셀 - 발의일자
        Cell body1Cell = body1Row.createCell(0);
        body1Cell.setCellValue("발의일자");

        // 바디 타이틀 스타일 - 발의일자
        CellStyle body1Style = workbook.createCellStyle();
        body1Style.setAlignment(HorizontalAlignment.CENTER);
        body1Style.setVerticalAlignment(VerticalAlignment.CENTER);
        body1Style.setBorderTop(BorderStyle.MEDIUM);
        body1Style.setBorderLeft(BorderStyle.MEDIUM);
        body1Style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
        body1Style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // 바디 타이틀 셀 폰트 - 발의일자
        Font body1Font = workbook.createFont();
        body1Font.setBold(true);
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
        body2Style.setBorderTop(BorderStyle.MEDIUM);
        body2Style.setBorderLeft(BorderStyle.THIN);
        body2Style.setBorderRight(BorderStyle.THIN);
        body2Style.setBorderBottom(BorderStyle.THIN);
        body2Style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
        body2Style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // 바디 타이틀 셀 폰트 - 금액
        Font body2Font = workbook.createFont();
        body2Font.setBold(true);
        body2Font.setFontName("돋음");
        body2Style.setFont(body2Font);

        body2Cell.setCellStyle(body2Style);

        CellStyle body2_1Style = workbook.createCellStyle();
        body2_1Style.setBorderTop(BorderStyle.MEDIUM);
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
        body3Style.setBorderTop(BorderStyle.MEDIUM);
        body3Style.setBorderRight(BorderStyle.MEDIUM);
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


        Row body2Row = outcomeHistorySheet.createRow(7);

        // 바디 타이틀 셀 - 발의일자
        Cell body4Cell = body2Row.createCell(0);

        // 바디 타이틀 스타일 - 발의일자
        CellStyle body4Style = workbook.createCellStyle();
        body4Style.setFillBackgroundColor((short) 3);
        //body1Style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        body4Style.setBorderLeft(BorderStyle.MEDIUM);
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
        body7Style.setBorderRight(BorderStyle.MEDIUM);
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
        monthLeftCellStyle.setBorderLeft(BorderStyle.MEDIUM);

        CellStyle monthRightCellStyle = workbook.createCellStyle();
        monthRightCellStyle.setBorderTop(BorderStyle.THIN);
        monthRightCellStyle.setBorderRight(BorderStyle.MEDIUM);
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

            Row body3Row = outcomeHistorySheet.createRow(7 + i);

            Cell body8Cell = body3Row.createCell(0);
            body8Cell.setCellStyle(monthLeftCellStyle);
            body8Cell.setCellValue(year + "년" + MM + "월" + dd + "일");

            Cell body9Cell = body3Row.createCell(1);
            body9Cell.setCellStyle(monthCellStyle);
            body9Cell.setCellFormula("근태관리!" + invertCellIndexToAlphabet(1 + 3 * keySet.size()) + (2 + 2 * i));// 야근식대 금액 세팅

            Cell body10Cell = body3Row.createCell(2);
            body10Cell.setCellStyle(monthCellStyle);
            body10Cell.setCellFormula("근태관리!" + invertCellIndexToAlphabet(1 + 3 * keySet.size()) + (2 + 2 * i + 1));


            Cell body11Cell = body3Row.createCell(3);
            body11Cell.setCellStyle(monthRightCellStyle);
        }

        // 총합계 ROW
        Row body4Row = outcomeHistorySheet.createRow(7 + rtn[1] + 1);


        // 바디 타이틀 셀 - 총합계
        Cell body12Cell = body4Row.createCell(0);

        // 바디 타이틀 스타일 - 총합계
        CellStyle body12Style = workbook.createCellStyle();
        body12Style.setBorderTop(BorderStyle.MEDIUM);
        body12Style.setBorderLeft(BorderStyle.MEDIUM);
        body12Style.setBorderBottom(BorderStyle.MEDIUM);
        body12Style.setBorderRight(BorderStyle.THIN);
        body12Cell.setCellStyle(body12Style);

        // 바디 타이틀 셀 - 총합계
        Cell body13Cell = body4Row.createCell(1);
        body13Cell.setCellFormula("SUM(B" + (8 + rtn[0]) + ":" + "B" + (8 + rtn[1]) + ")");

        // 바디 타이틀 스타일 - 총합계
        CellStyle body13Style = workbook.createCellStyle();
        body13Style.setBorderTop(BorderStyle.MEDIUM);
        body13Style.setBorderBottom(BorderStyle.MEDIUM);
        body13Style.setBorderRight(BorderStyle.THIN);
        body13Style.setBorderLeft(BorderStyle.THIN);
        body13Style.setAlignment(HorizontalAlignment.RIGHT);
        body13Style.setVerticalAlignment(VerticalAlignment.CENTER);
        body13Cell.setCellStyle(body13Style);


        // 바디 타이틀 셀 - 총합계
        Cell body14Cell = body4Row.createCell(2);
        body14Cell.setCellValue(transportAmt);
        body14Cell.setCellFormula("SUM(C" + (8 + rtn[0]) + ":" + "C" + (8 + rtn[1]) + ")");

        // 바디 타이틀 스타일 - 총합계
        CellStyle body14Style = workbook.createCellStyle();
        body14Style.setBorderTop(BorderStyle.MEDIUM);
        body14Style.setBorderBottom(BorderStyle.MEDIUM);
        body14Style.setBorderRight(BorderStyle.THIN);
        body14Style.setBorderLeft(BorderStyle.THIN);
        body14Style.setAlignment(HorizontalAlignment.RIGHT);
        body14Style.setVerticalAlignment(VerticalAlignment.CENTER);
        body14Cell.setCellStyle(body14Style);


        // 바디 타이틀 셀 - 총합계
        Cell body15Cell = body4Row.createCell(3);

        // 바디 타이틀 스타일 - 총합계
        CellStyle body15Style = workbook.createCellStyle();
        body15Style.setBorderTop(BorderStyle.MEDIUM);
        body15Style.setBorderBottom(BorderStyle.MEDIUM);
        body15Style.setBorderRight(BorderStyle.MEDIUM);
        body15Style.setBorderLeft(BorderStyle.THIN);
        body15Cell.setCellStyle(body15Style);


        // 총합계 ROW
        Row body5Row = outcomeHistorySheet.createRow(7 + rtn[1] + 2);
        outcomeHistorySheet.addMergedRegion(new CellRangeAddress(7 + rtn[1] + 2,7 + rtn[1] + 2,1,2));

        // 바디 타이틀 셀 - 총합계
        Cell body16Cell = body5Row.createCell(0);
        body16Cell.setCellValue("총합계 (원)");

        // 바디 타이틀 스타일 - 총합계
        XSSFCellStyle body16Style = (XSSFCellStyle) workbook.createCellStyle();
        body16Style.setBorderTop(BorderStyle.MEDIUM);
        body16Style.setBorderLeft(BorderStyle.MEDIUM);
        body16Style.setBorderBottom(BorderStyle.MEDIUM);
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
        body17Cell.setCellFormula("SUM(B" + (8 + rtn[1] + 1) + "," + "C" + (8 + rtn[1] + 1) + ")");

        // 바디 타이틀 스타일
        XSSFCellStyle body17Style = (XSSFCellStyle) workbook.createCellStyle();
        body17Style.setBorderTop(BorderStyle.MEDIUM);
        body17Style.setBorderBottom(BorderStyle.MEDIUM);
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
        body18Style.setBorderTop(BorderStyle.MEDIUM);
        body18Style.setBorderBottom(BorderStyle.MEDIUM);
        body18Style.setBorderRight(BorderStyle.THIN);
        body18Cell.setCellStyle(body18Style);

        // custom 배경색
        body18Style.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 255, 153)));	// 255 255 153
        body18Style.setFillPattern(FillPatternType.SOLID_FOREGROUND);


        // 바디 타이틀 셀
        Cell body19Cell = body5Row.createCell(3);

        // 바디 타이틀 스타일
        XSSFCellStyle body19Style = (XSSFCellStyle) workbook.createCellStyle();
        body19Style.setBorderTop(BorderStyle.MEDIUM);
        body19Style.setBorderBottom(BorderStyle.MEDIUM);
        body19Style.setBorderRight(BorderStyle.MEDIUM);
        body19Cell.setCellStyle(body19Style);

        // custom 배경색
        body19Style.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 255, 153)));	// 255 255 153
        body19Style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // 결재일자 ROW
        Row body6Row = outcomeHistorySheet.createRow(7 + rtn[1] + 4);
        outcomeHistorySheet.addMergedRegion(new CellRangeAddress(7 + rtn[1] + 4,7 + rtn[1] + 4,1,3));

        // 바디 타이틀 셀 - 결재일자
        Cell body20Cell = body6Row.createCell(0);
        body20Cell.setCellValue("결재일자");

        // 바디 타이틀 스타일 - 결재일자
        XSSFCellStyle body20Style = (XSSFCellStyle) workbook.createCellStyle();
        body20Style.setFillBackgroundColor((short) 3);
        //body1Style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        body20Style.setBorderTop(BorderStyle.MEDIUM);
        body20Style.setBorderLeft(BorderStyle.MEDIUM);
        body20Style.setBorderBottom(BorderStyle.MEDIUM);
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
        body21Cell.setCellValue(year + "년" + month + "월" + rtn[1] + "일");

        // 바디 타이틀 스타일
        XSSFCellStyle body21Style = (XSSFCellStyle) workbook.createCellStyle();
        body21Style.setFillBackgroundColor((short) 3);
        //body1Style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        body21Style.setBorderTop(BorderStyle.MEDIUM);
        body21Style.setBorderBottom(BorderStyle.MEDIUM);
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
        body22Style.setBorderTop(BorderStyle.MEDIUM);
        body22Style.setBorderBottom(BorderStyle.MEDIUM);

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
        body23Style.setBorderTop(BorderStyle.MEDIUM);
        body23Style.setBorderRight(BorderStyle.MEDIUM);
        body23Style.setBorderBottom(BorderStyle.MEDIUM);
        body23Style.setBorderLeft(BorderStyle.THIN);

        // custom 배경색
        body23Style.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 255, 153)));	// 255 255 153
        body23Style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        body23Cell.setCellStyle(body23Style);


        // 근태관리 excel sheet 생성
        Sheet workManagementSheet = workbook.createSheet("근태관리");
        workManagementSheet.createFreezePane(1, 3, 1, 3);
        workManagementSheet.setDisplayGridlines(false);

        CellStyle workManagementCell3Style1 = workbook.createCellStyle();
        workManagementCell3Style1.setAlignment(HorizontalAlignment.CENTER);
        workManagementCell3Style1.setBorderTop(BorderStyle.MEDIUM);
        workManagementCell3Style1.setBorderBottom(BorderStyle.THIN);
        workManagementCell3Style1.setBorderRight(BorderStyle.THIN);
        workManagementCell3Style1.setBorderLeft(BorderStyle.THIN);

        CellStyle workManagementCell3Style2 = workbook.createCellStyle();
        workManagementCell3Style2.setAlignment(HorizontalAlignment.CENTER);
        workManagementCell3Style2.setBorderTop(BorderStyle.THIN);
        workManagementCell3Style2.setBorderBottom(BorderStyle.MEDIUM);
        workManagementCell3Style2.setBorderRight(BorderStyle.THIN);
        workManagementCell3Style2.setBorderLeft(BorderStyle.THIN);


        Row workManagementRow1 = workManagementSheet.createRow(1);
        Cell workManagementCell1 = workManagementRow1.createCell(0);
        // 35px
        workManagementCell1.setCellValue(month + "월");
        workManagementCell1.setCellStyle(workManagementCell3Style1);

        Row workManagementRow1_1 = workManagementSheet.createRow(2);


        CellStyle employeeInfoHeaderStyle = workbook.createCellStyle();
        employeeInfoHeaderStyle.setAlignment(HorizontalAlignment.CENTER);
        employeeInfoHeaderStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        employeeInfoHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        employeeInfoHeaderStyle.setBorderTop(BorderStyle.THIN);
        employeeInfoHeaderStyle.setBorderBottom(BorderStyle.MEDIUM);
        employeeInfoHeaderStyle.setBorderRight(BorderStyle.THIN);
        employeeInfoHeaderStyle.setBorderLeft(BorderStyle.THIN);


        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, Row> dayRowMap = new HashMap<>();
        HashMap<String, String> amtSumMap = new HashMap<>();
        String[] dayOfWeekList = {"월", "화", "수", "목", "금", "토", "일"};
        int dayOfWeekCnt = rtn[2];

        CellStyle positionInfoCellStyle1 = workbook.createCellStyle();
        positionInfoCellStyle1.setAlignment(HorizontalAlignment.CENTER);
        positionInfoCellStyle1.setVerticalAlignment(VerticalAlignment.CENTER);
        positionInfoCellStyle1.setBorderTop(BorderStyle.MEDIUM);
        positionInfoCellStyle1.setBorderBottom(BorderStyle.THIN);
        positionInfoCellStyle1.setBorderRight(BorderStyle.THIN);
        positionInfoCellStyle1.setBorderLeft(BorderStyle.MEDIUM);

        CellStyle positionInfoCellStyle2 = workbook.createCellStyle();
        positionInfoCellStyle2.setAlignment(HorizontalAlignment.CENTER);
        positionInfoCellStyle2.setVerticalAlignment(VerticalAlignment.CENTER);
        positionInfoCellStyle2.setBorderTop(BorderStyle.THIN);
        positionInfoCellStyle2.setBorderBottom(BorderStyle.MEDIUM);
        positionInfoCellStyle2.setBorderRight(BorderStyle.THIN);
        positionInfoCellStyle2.setBorderLeft(BorderStyle.MEDIUM);

        CellStyle paymInfoCellStyle1 = workbook.createCellStyle();
        paymInfoCellStyle1.setAlignment(HorizontalAlignment.CENTER);
        paymInfoCellStyle1.setVerticalAlignment(VerticalAlignment.CENTER);
        paymInfoCellStyle1.setBorderTop(BorderStyle.MEDIUM);
        paymInfoCellStyle1.setBorderBottom(BorderStyle.THIN);
        paymInfoCellStyle1.setBorderRight(BorderStyle.THIN);
        paymInfoCellStyle1.setBorderLeft(BorderStyle.THIN);

        CellStyle paymInfoCellStyle2 = workbook.createCellStyle();
        paymInfoCellStyle2.setAlignment(HorizontalAlignment.CENTER);
        paymInfoCellStyle2.setVerticalAlignment(VerticalAlignment.CENTER);
        paymInfoCellStyle2.setBorderTop(BorderStyle.THIN);
        paymInfoCellStyle2.setBorderBottom(BorderStyle.MEDIUM);
        paymInfoCellStyle2.setBorderRight(BorderStyle.THIN);
        paymInfoCellStyle2.setBorderLeft(BorderStyle.THIN);

        CellStyle remarkInfoCellStyle1 = workbook.createCellStyle();
        remarkInfoCellStyle1.setAlignment(HorizontalAlignment.CENTER);
        remarkInfoCellStyle1.setVerticalAlignment(VerticalAlignment.CENTER);
        remarkInfoCellStyle1.setBorderTop(BorderStyle.MEDIUM);
        remarkInfoCellStyle1.setBorderBottom(BorderStyle.THIN);
        remarkInfoCellStyle1.setBorderRight(BorderStyle.MEDIUM);
        remarkInfoCellStyle1.setBorderLeft(BorderStyle.THIN);

        CellStyle remarkInfoCellStyle2 = workbook.createCellStyle();
        remarkInfoCellStyle2.setAlignment(HorizontalAlignment.CENTER);
        remarkInfoCellStyle2.setVerticalAlignment(VerticalAlignment.CENTER);
        remarkInfoCellStyle2.setBorderTop(BorderStyle.THIN);
        remarkInfoCellStyle2.setBorderBottom(BorderStyle.MEDIUM);
        remarkInfoCellStyle2.setBorderRight(BorderStyle.MEDIUM);
        remarkInfoCellStyle2.setBorderLeft(BorderStyle.THIN);

        for(int i = rtn[0]; i <= rtn[1]; i++){
            Row workManagementRow2 = workManagementSheet.createRow(1 + (i * 2));
            Cell workManagementCell3 = workManagementRow2.createCell(0);
            workManagementCell3.setCellValue(Integer.toString(i));
            workManagementCell3.setCellStyle(workManagementCell3Style1);


            Row workManagementRow3 = workManagementSheet.createRow(1 + (i * 2) + 1);
            Cell workManagementCell4 = workManagementRow3.createCell(0);
            workManagementCell4.setCellValue(dayOfWeekList[dayOfWeekCnt % 7]);
            workManagementCell4.setCellStyle(workManagementCell3Style2);


            for(int j = 1; j <= workCollectionDtl.size(); j++){
                // 굳이 0을 넣어야 하나?
                workManagementRow2.createCell((3 * j) - 1).setCellValue(0);
                workManagementRow3.createCell((3 * j) - 1).setCellValue(0);


                workManagementRow2.createCell((3 * j) - 2).setCellStyle(positionInfoCellStyle1);
                workManagementRow3.createCell((3 * j) - 2).setCellStyle(positionInfoCellStyle2);
                workManagementRow2.getCell((3 * j) - 1).setCellStyle(paymInfoCellStyle1);
                workManagementRow3.getCell((3 * j) - 1).setCellStyle(paymInfoCellStyle2);
                workManagementRow2.createCell(3 * j).setCellStyle(remarkInfoCellStyle1);
                workManagementRow3.createCell(3 * j).setCellStyle(remarkInfoCellStyle2);
            }

            dayRowMap.put(Integer.toString(i), workManagementRow2);
            dayRowMap.put(i + "_1", workManagementRow3);


            dayOfWeekCnt++;
        }

        System.out.println("dayRowMap >>>>> " + dayRowMap.toString());
        System.out.println("amtSumMap >>>>> " + amtSumMap.toString());

        int index = 1;

        CellStyle workManagementCell6Style = workbook.createCellStyle();
        workManagementCell6Style.setAlignment(HorizontalAlignment.CENTER);
        workManagementCell6Style.setVerticalAlignment(VerticalAlignment.CENTER);
        workManagementCell6Style.setBorderTop(BorderStyle.THIN);
        workManagementCell6Style.setBorderBottom(BorderStyle.MEDIUM);
        workManagementCell6Style.setBorderRight(BorderStyle.MEDIUM);
        workManagementCell6Style.setBorderLeft(BorderStyle.THIN);

        CellStyle blankCellStyle1 = workbook.createCellStyle();
        blankCellStyle1.setBorderTop(BorderStyle.MEDIUM);
        blankCellStyle1.setBorderBottom(BorderStyle.THIN);
        blankCellStyle1.setBorderLeft(BorderStyle.MEDIUM);

        CellStyle blankCellStyle2 = workbook.createCellStyle();
        blankCellStyle2.setBorderTop(BorderStyle.MEDIUM);
        blankCellStyle2.setBorderBottom(BorderStyle.THIN);

        CellStyle blankCellStyle3 = workbook.createCellStyle();
        blankCellStyle3.setBorderTop(BorderStyle.MEDIUM);
        blankCellStyle3.setBorderBottom(BorderStyle.THIN);
        blankCellStyle3.setBorderRight(BorderStyle.MEDIUM);

        CellStyle totalCommonCellStyle1 = workbook.createCellStyle();
        totalCommonCellStyle1.setAlignment(HorizontalAlignment.CENTER);
        totalCommonCellStyle1.setVerticalAlignment(VerticalAlignment.CENTER);
        totalCommonCellStyle1.setBorderTop(BorderStyle.MEDIUM);
        totalCommonCellStyle1.setBorderBottom(BorderStyle.THIN);
        totalCommonCellStyle1.setBorderRight(BorderStyle.THIN);
        totalCommonCellStyle1.setBorderLeft(BorderStyle.THIN);

        CellStyle totalCommonCellStyle2 = workbook.createCellStyle();
        totalCommonCellStyle2.setAlignment(HorizontalAlignment.CENTER);
        totalCommonCellStyle2.setVerticalAlignment(VerticalAlignment.CENTER);
        totalCommonCellStyle2.setBorderTop(BorderStyle.THIN);
        totalCommonCellStyle2.setBorderBottom(BorderStyle.THIN);
        totalCommonCellStyle2.setBorderRight(BorderStyle.THIN);
        totalCommonCellStyle2.setBorderLeft(BorderStyle.THIN);

        CellStyle totalCommonCellStyle3 = workbook.createCellStyle();
        totalCommonCellStyle3.setAlignment(HorizontalAlignment.CENTER);
        totalCommonCellStyle3.setVerticalAlignment(VerticalAlignment.CENTER);
        totalCommonCellStyle3.setBorderTop(BorderStyle.THIN);
        totalCommonCellStyle3.setBorderBottom(BorderStyle.MEDIUM);
        totalCommonCellStyle3.setBorderRight(BorderStyle.THIN);
        totalCommonCellStyle3.setBorderLeft(BorderStyle.THIN);

        CellStyle middlePositionInfoCellStyle = workbook.createCellStyle();
        middlePositionInfoCellStyle.setBorderTop(BorderStyle.THIN);
        middlePositionInfoCellStyle.setBorderBottom(BorderStyle.THIN);
        middlePositionInfoCellStyle.setBorderRight(BorderStyle.THIN);
        middlePositionInfoCellStyle.setBorderLeft(BorderStyle.MEDIUM);

        CellStyle middleRemarkCellStyle = workbook.createCellStyle();
        middleRemarkCellStyle.setBorderTop(BorderStyle.THIN);
        middleRemarkCellStyle.setBorderBottom(BorderStyle.THIN);
        middleRemarkCellStyle.setBorderRight(BorderStyle.MEDIUM);
        middleRemarkCellStyle.setBorderLeft(BorderStyle.THIN);

        CellStyle monthAmtSumCellStyle1 = workbook.createCellStyle();
        monthAmtSumCellStyle1.setBorderTop(BorderStyle.MEDIUM);
        monthAmtSumCellStyle1.setBorderBottom(BorderStyle.THIN);
        monthAmtSumCellStyle1.setBorderRight(BorderStyle.MEDIUM);
        monthAmtSumCellStyle1.setBorderLeft(BorderStyle.MEDIUM);

        CellStyle monthAmtSumCellStyle2 = workbook.createCellStyle();
        monthAmtSumCellStyle2.setBorderTop(BorderStyle.THIN);
        monthAmtSumCellStyle2.setBorderBottom(BorderStyle.THIN);
        monthAmtSumCellStyle2.setBorderRight(BorderStyle.MEDIUM);
        monthAmtSumCellStyle2.setBorderLeft(BorderStyle.MEDIUM);

        CellStyle monthAmtSumCellStyle3 = workbook.createCellStyle();
        monthAmtSumCellStyle3.setBorderTop(BorderStyle.THIN);
        monthAmtSumCellStyle3.setBorderBottom(BorderStyle.MEDIUM);
        monthAmtSumCellStyle3.setBorderRight(BorderStyle.THIN);
        monthAmtSumCellStyle3.setBorderLeft(BorderStyle.MEDIUM);

        for(String key : keySet){
            workManagementSheet.addMergedRegion(new CellRangeAddress(1,1, (3 * index) - 2,3 * index));

            List<WorkCollectionDtlReqDTO> reqDTOList = (List<WorkCollectionDtlReqDTO>)workCollectionDtl.get(key);

            workManagementRow1.createCell((3 * index) - 2).setCellStyle(blankCellStyle1);
            workManagementRow1.createCell((3 * index) - 1).setCellStyle(blankCellStyle2);
            workManagementRow1.createCell(3 * index).setCellStyle(blankCellStyle3);

            Cell workManagementCell4 = workManagementRow1_1.createCell((3 * index) - 2);
            workManagementCell4.setCellValue("직책");
            workManagementCell4.setCellStyle(employeeInfoHeaderStyle);

            Cell workManagementCell5 = workManagementRow1_1.createCell((3 * index) - 1);

            workManagementCell5.setCellValue(mapper.convertValue(reqDTOList.get(0), WorkCollectionDtlReqDTO.class).getName());
            workManagementCell5.setCellStyle(employeeInfoHeaderStyle);

            Cell workManagementCell6 = workManagementRow1_1.createCell(3 * index);
            workManagementCell6.setCellValue("비고");
            workManagementCell6.setCellStyle(workManagementCell6Style);


            for(int i = 0; i < reqDTOList.size(); i++){
                WorkCollectionDtlReqDTO DTO = mapper.convertValue(reqDTOList.get(i), WorkCollectionDtlReqDTO.class);
                System.out.println("시작 시간 >>>>> " + DTO.getStartTime());
                System.out.println("종료 시간 >>>>> " + DTO.getEndTime());

                int j = Integer.parseInt(DTO.getWorkDt().substring(8));

                dayRowMap.get(j + "")
                        .getCell((3 * index) - 2).setCellValue(DTO.getStartTime());
                dayRowMap.get(j + "_1")
                        .getCell((3 * index) - 2).setCellValue(DTO.getEndTime());

                dayRowMap.get(j + "")
                        .getCell((3 * index) - 1).setCellValue(DTO.getDinnerYn().equals("Y") ? 9000 : 0);
                dayRowMap.get(j + "_1")
                        .getCell((3 * index) - 1).setCellValue(DTO.getTaxiPay());

                dayRowMap.get(j + "")
                        .getCell(3 * index).setCellValue("비고1");
                dayRowMap.get(j + "_1")
                        .getCell(3 * index).setCellValue("비고2");



                dayRowMap.get(j + "")
                        .getCell((3 * index) - 2).setCellStyle(positionInfoCellStyle1);
                dayRowMap.get(j + "_1")
                        .getCell((3 * index) - 2).setCellStyle(positionInfoCellStyle2);

                dayRowMap.get(j + "")
                        .getCell((3 * index) - 1).setCellStyle(paymInfoCellStyle1);
                dayRowMap.get(j + "_1")
                        .getCell((3 * index) - 1).setCellStyle(paymInfoCellStyle2);

                dayRowMap.get(j + "")
                        .getCell(3 * index).setCellStyle(remarkInfoCellStyle1);
                dayRowMap.get(j + "_1")
                        .getCell(3 * index).setCellStyle(remarkInfoCellStyle2);
            }

            index++;
        }

        workManagementRow1.createCell(3 * keySet.size() + 1).setCellStyle(remarkInfoCellStyle1);
        workManagementRow1_1.createCell(3 * keySet.size() + 1).setCellValue("합계");
        workManagementRow1_1.getCell(3 * keySet.size() + 1).setCellStyle(remarkInfoCellStyle2);


        for(int i = 1; i <= 3 * keySet.size(); i++){
            workManagementSheet.setColumnWidth(i, 4320);
        }

        workManagementSheet.setColumnWidth(3 * keySet.size() + 1, 4320);


        // 일별 야근식대, 택시비 취합

        String totalDinnerAmt = "SUM(";
        String totalTaxiAmt = "SUM(";

        int darRowCount = dayRowMap.size()/2;

        for(int i = 1; i <= darRowCount; i++){
            Row dinnerRow = dayRowMap.get(Integer.toString(i));
            Cell dinnerTotalCell = dinnerRow.createCell(3 * keySet.size() + 1);

            Row taxiRow = dayRowMap.get(i + "_1");
            Cell taxiTotalCell = taxiRow.createCell(3 * keySet.size() + 1);

            for(int j = 1; j <= keySet.size(); j++){
                totalDinnerAmt += invertCellIndexToAlphabet(3 * j - 1) + (2 + 2 * i);
                totalTaxiAmt += invertCellIndexToAlphabet(3 * j - 1) + (2 + 2 * i + 1);

                if(j != keySet.size()){
                    totalDinnerAmt += ",";
                    totalTaxiAmt += ",";
                }else{
                    totalDinnerAmt += ")";
                    totalTaxiAmt += ")";
                }
            }

            dinnerTotalCell.setCellFormula(totalDinnerAmt);
            dinnerTotalCell.setCellStyle(remarkInfoCellStyle1);

            taxiTotalCell.setCellFormula(totalTaxiAmt);
            taxiTotalCell.setCellStyle(remarkInfoCellStyle2);

            totalDinnerAmt = "SUM(";
            totalTaxiAmt = "SUM(";
        }

        // 개인별 월별 야근식대, 택시비 취합
        Row personalMonthAmtSumRow1 = workManagementSheet.createRow(2 + dayRowMap.size() + 1);
        Row personalMonthAmtSumRow2 = workManagementSheet.createRow(2 + dayRowMap.size() + 2);
        Row personalMonthAmtSumRow3 = workManagementSheet.createRow(2 + dayRowMap.size() + 3);

        personalMonthAmtSumRow1.createCell(0).setCellValue("합계");
        personalMonthAmtSumRow1.getCell(0).setCellStyle(monthAmtSumCellStyle1);
        personalMonthAmtSumRow2.createCell(0).setCellStyle(monthAmtSumCellStyle2);
        personalMonthAmtSumRow3.createCell(0).setCellStyle(monthAmtSumCellStyle3);

        // 일별 개인 취합 로직
        for(int i = 1; i <= keySet.size(); i++){
            String oddCell = "SUM(";
            String evenCell = "SUM(";
            String totalCell = "SUM(";

            for(int j = 3; j <= dayRowMap.size() + 2; j++){

                if(j % 2 == 0){
                    oddCell += invertCellIndexToAlphabet(3 * i - 1) + j;
                }else {
                    evenCell += invertCellIndexToAlphabet(3 * i - 1) + j;
                }

                totalCell += invertCellIndexToAlphabet(3 * i - 1) + j;

                if(j != dayRowMap.size() + 2){
                    if(j % 2 == 0){
                        oddCell += ",";
                    }else {
                        evenCell += ",";
                    }

                    totalCell += ",";
                }
            }

            oddCell += ")";
            evenCell += ")";
            totalCell += ")";

            personalMonthAmtSumRow1.createCell(3 * i - 2).setCellStyle(positionInfoCellStyle1);
            personalMonthAmtSumRow1.createCell(3 * i - 1).setCellFormula(oddCell);
            personalMonthAmtSumRow1.getCell(3 * i - 1).setCellStyle(totalCommonCellStyle1);
            personalMonthAmtSumRow1.createCell(3 * i).setCellStyle(remarkInfoCellStyle1);

            personalMonthAmtSumRow2.createCell(3 * i - 2).setCellStyle(middlePositionInfoCellStyle);
            personalMonthAmtSumRow2.createCell(3 * i - 1).setCellFormula(evenCell);
            personalMonthAmtSumRow2.getCell(3 * i - 1).setCellStyle(totalCommonCellStyle2);
            personalMonthAmtSumRow2.createCell(3 * i).setCellStyle(middleRemarkCellStyle);

            personalMonthAmtSumRow3.createCell(3 * i - 2).setCellStyle(positionInfoCellStyle2);
            personalMonthAmtSumRow3.createCell(3 * i - 1).setCellFormula(totalCell);
            personalMonthAmtSumRow3.getCell(3 * i - 1).setCellStyle(totalCommonCellStyle3);
            personalMonthAmtSumRow3.createCell(3 * i).setCellStyle(remarkInfoCellStyle2);
        }


        String oddCell = "SUM(";
        String evenCell = "SUM(";
        String totalCell = "SUM(";

        String prefix = invertCellIndexToAlphabet(3 * (keySet.size() + 1) - 2);
        for(int i = 3; i <= dayRowMap.size() + 2; i++){
            if(i % 2 == 0){
                oddCell += prefix + i;
            }else {
                evenCell += prefix + i;
            }

            totalCell += prefix + i;

            if(i != dayRowMap.size() + 2){
                if(i % 2 == 0){
                    oddCell += ",";
                }else {
                    evenCell += ",";
                }

                totalCell += ",";
            }
        }

        oddCell += ")";
        evenCell += ")";
        totalCell += ")";


        CellStyle personalMonthAmtSumCellStyle1 = workbook.createCellStyle();
        personalMonthAmtSumCellStyle1.setAlignment(HorizontalAlignment.CENTER);
        personalMonthAmtSumCellStyle1.setVerticalAlignment(VerticalAlignment.CENTER);
        personalMonthAmtSumCellStyle1.setBorderTop(BorderStyle.MEDIUM);
        personalMonthAmtSumCellStyle1.setBorderBottom(BorderStyle.THIN);
        personalMonthAmtSumCellStyle1.setBorderRight(BorderStyle.MEDIUM);
        personalMonthAmtSumCellStyle1.setBorderLeft(BorderStyle.THIN);

        CellStyle personalMonthAmtSumCellStyle2 = workbook.createCellStyle();
        personalMonthAmtSumCellStyle2.setAlignment(HorizontalAlignment.CENTER);
        personalMonthAmtSumCellStyle2.setVerticalAlignment(VerticalAlignment.CENTER);
        personalMonthAmtSumCellStyle2.setBorderTop(BorderStyle.THIN);
        personalMonthAmtSumCellStyle2.setBorderBottom(BorderStyle.THIN);
        personalMonthAmtSumCellStyle2.setBorderRight(BorderStyle.MEDIUM);
        personalMonthAmtSumCellStyle2.setBorderLeft(BorderStyle.THIN);

        CellStyle personalMonthAmtSumCellStyle3 = workbook.createCellStyle();
        personalMonthAmtSumCellStyle3.setAlignment(HorizontalAlignment.CENTER);
        personalMonthAmtSumCellStyle3.setVerticalAlignment(VerticalAlignment.CENTER);
        personalMonthAmtSumCellStyle3.setBorderTop(BorderStyle.THIN);
        personalMonthAmtSumCellStyle3.setBorderBottom(BorderStyle.MEDIUM);
        personalMonthAmtSumCellStyle3.setBorderRight(BorderStyle.MEDIUM);
        personalMonthAmtSumCellStyle3.setBorderLeft(BorderStyle.THIN);


        //3 * (keySet.size() + 1) - 2
        personalMonthAmtSumRow1.createCell(3 * (keySet.size() + 1) - 2).setCellFormula(oddCell);
        personalMonthAmtSumRow1.getCell(3 * (keySet.size() + 1) - 2).setCellStyle(personalMonthAmtSumCellStyle1);

        personalMonthAmtSumRow2.createCell(3 * (keySet.size() + 1) - 2).setCellFormula(evenCell);
        personalMonthAmtSumRow2.getCell(3 * (keySet.size() + 1) - 2).setCellStyle(personalMonthAmtSumCellStyle2);

        personalMonthAmtSumRow3.createCell(3 * (keySet.size() + 1) - 2).setCellFormula(totalCell);
        personalMonthAmtSumRow3.getCell(3 * (keySet.size() + 1) - 2).setCellStyle(personalMonthAmtSumCellStyle3);


        // 총 야근식대, 택시비 취합



        workManagementSheet.setColumnWidth(0, 1350);
        //workManagementSheet.setDisplayGridlines(false);


        // 택시비 영수증 excel sheet 생성
        Sheet taxiFeeReceiptSheet = workbook.createSheet("택시비 영수증");




        String yyyyMMdd = Integer.toString(year) + (month > 9 ? month : "0" + Integer.toString(month)) + (rtn[1] > 9 ? rtn[1] : "0" + Integer.toString(rtn[1]));

        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        String fileLocation = path.substring(0, path.length()-1) + name + "야근식대" + yyyyMMdd + ".xlsx";

        System.out.println("엑셀 생성 경로 >>>> " + fileLocation);
        FileOutputStream outputStream = new FileOutputStream(fileLocation);

        response.setHeader("Content-Type", "application/octet-stream");
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"야근식대" + yyyyMMdd + ".xlsx\""));
        OutputStream os = response.getOutputStream();
        os.flush();

        workbook.write(outputStream);
        workbook.write(os);

        os.flush();
        workbook.close();

        os.close();

        return result;
    }


    // 날짜 계산 함수
    private int[] calculateDate (int year, int month, int day){
        Calendar cal = Calendar.getInstance();
        cal.set(year, month-1, day); //월은 -1해줘야 해당월로 인식

        int[] rtn = new int[3];
        rtn[0] = cal.getMinimum(Calendar.DAY_OF_MONTH);
        rtn[1] = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        rtn[2] = cal.getFirstDayOfWeek();
        return rtn;
    }


    private String invertCellIndexToAlphabet(int cellIndex){
        String cellAlphabet = "";
        int index = cellIndex + 1;

        // 26개
        if(index / 26 == 0){
            cellAlphabet += alphabet[cellIndex];
        }else{
            while(cellIndex / 26 >= 26){
                cellAlphabet += alphabet[(index % 26) - 1];

                index = index / 26;
            }

            cellAlphabet += alphabet[(index % 26) - 1];
        }

        return cellAlphabet;
    }
}
