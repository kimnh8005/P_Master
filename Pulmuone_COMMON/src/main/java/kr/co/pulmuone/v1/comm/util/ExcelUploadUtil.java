package kr.co.pulmuone.v1.comm.util;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExcelUploadUtil {

    public static Sheet excelParse(MultipartFile excelFile) {
    	return excelParse(excelFile, 0);
    }

    public static Sheet excelParse(MultipartFile excelFile, int index) {
        try {
            Workbook wbs = WorkbookFactory.create(excelFile.getInputStream());
            return wbs.getSheetAt(index);
        } catch (IOException e) {
            return null;    // Excel File 없을시 null 처리
        }
    }

    public static String cellValue(Cell cell) {
        String value = "";

        if (cell == null) {
            value = "";
        } else {
            CellType cellType = cell.getCellType();

            if (cellType.equals(CellType.FORMULA)) {
                value = cell.getCellFormula();
            } else if (cellType.equals(CellType.NUMERIC)) {
                if (DateUtil.isCellDateFormatted(cell)) {
                    //you should change this to your application date format
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDateTime localDateTime = cell.getLocalDateTimeCellValue();
                    value = "" + localDateTime.format(dateTimeFormatter);
                } else {
                    value = "" + String.format("%.0f", cell.getNumericCellValue());
                }
            } else if (cellType.equals(CellType.STRING)) {
                value = "" + cell.getStringCellValue();
            } else if (cellType.equals(CellType.BLANK)) {
                value = "" + cell.getStringCellValue();
            } else if (cellType.equals(CellType.ERROR)) {
                value = "" + cell.getErrorCellValue();
            }
        }

        return value.trim();
    }

    public static String cellValueForNumericNotFormatting(Cell cell) {
        String value = "";

        if (cell == null) {
            value = "";
        } else {
            CellType cellType = cell.getCellType();

            if (cellType.equals(CellType.FORMULA)) {
                value = cell.getCellFormula();
            } else if (cellType.equals(CellType.NUMERIC)) {
                if (DateUtil.isCellDateFormatted(cell)) {
                    //you should change this to your application date format
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDateTime localDateTime = cell.getLocalDateTimeCellValue();
                    value = "" + localDateTime.format(dateTimeFormatter);
                } else {
                    //value = "" + String.format("%.0f", cell.getNumericCellValue());
                    value = "" + cell.getNumericCellValue();
                }
            } else if (cellType.equals(CellType.STRING)) {
                value = "" + cell.getStringCellValue();
            } else if (cellType.equals(CellType.BLANK)) {
                value = "" + cell.getStringCellValue();
            } else if (cellType.equals(CellType.ERROR)) {
                value = "" + cell.getErrorCellValue();
            }
        }

        return value.trim();
    }

    public static boolean isFile(MultipartFile file) {
        boolean returnIsFile = true;
        if (file == null){ returnIsFile = false; }
        return returnIsFile;
    }
    /**
     * 주소 분리 공백 4번째 까지 앞자리
     * @param address
     * @param isHead 1: 앞자리 이외 뒷자리
     * @return
     */
    public static String splitAdress(String address, int isHead){
        String returnAddress = address;
        if (!StringUtils.isEmpty(address)) {

            int splitPosition = 4;
            String splitText = " ";
            int totalLength = address.length();
            int startNum = 0;
            if (totalLength > 0) {
                int i = 0;
                do {
                    i++;
                    startNum = address.indexOf(splitText, startNum + 1);

                    if (i >= splitPosition) {
                        break;
                    }
                } while (startNum + 1 < address.length() && startNum != -1);
            }

            if (startNum > 0){
                try {
                    if (isHead == 1) {
                        returnAddress = address.substring(0, startNum);
                    } else {
                        returnAddress = address.substring(startNum + 1);
                    }
                } catch (Exception e){
                    return address;
                }
            }

        }
        return returnAddress.trim();

    }

    /**
     * 주소1 공백 4개이상있는지 여부
     * @param address
     * @return
     */
    public static Boolean isSplitAdress(String address){
        String returnAddress = address;
        if (!StringUtils.isEmpty(address)) {

            int splitPosition = 4;
            String splitText = " ";
            int totalLength = address.length();
            int startNum = 0;
            if (totalLength > 0) {
                int i = 0;
                do {
                    i++;
                    startNum = address.indexOf(splitText, startNum + 1);

                    if (i >= splitPosition) {
                        break;
                    }
                } while (startNum + 1 < address.length() && startNum != -1);
            }

            if (startNum > 0){
                return true;
            }

        }
        return false;

    }

    /**
     * 주소 분리 공백 4번째 까지 앞자리
     * @param address
     * @return
     */
    public static String[] splitAdress2(String address){
        address = address.replaceAll("- ", "-").replaceAll(" -", "-");
        String addr1 = address;
        String addr2 = "";

        Pattern pattern = Pattern.compile("( |[가-힣.]|\\d{1,5})+(로|길)(( |-|\\d{1,5})+)?");

        Matcher matcher = pattern.matcher(address);

        while (matcher.find()) {
            if (matcher != null && matcher.group(0) != null) {
                addr1 = matcher.group(0).trim();
                addr2 = address.substring(addr1.length()).trim();
                break;
            }
        }

        if("".equals(addr2)) {
            addr1 = splitAdress(address, 1);
            addr2 = splitAdress(address, 2);
        }

        return new String[] {addr1, addr2};
    }


    /**
     * 연락처 기본값 맵핑
     * @param phoneNumbers
     * @return
     */
    public static String defaultPhoneNumber(String... phoneNumbers) {
        if (ArrayUtils.isEmpty(phoneNumbers)) {
            return null;
        }
        for (String phoneNumber : phoneNumbers) {
            if (PhoneUtil.isValid(phoneNumber)) {
                return phoneNumber;
            }
        }
        return "";
    }


    /**
     * 주문자명 기본값 맵핑
     * @param names
     * @return
     */
    public static String defaultName(String... names) {
        if (ArrayUtils.isEmpty(names)) {
            return null;
        }
        for (String name : names) {
            if (name.contains("??") || name.equals(".") || StringUtils.isEmpty(name)) {
                continue;
            }

            final String targetText = "(주문자";
            if (name.contains(targetText)) {
                name = name.substring(0, name.indexOf(targetText));
            }
            return substring(name, 20);
        }
        return null;
    }

    public static String substring(String str, int limit) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }

        if (str.length() > limit) {
            return str.substring(0, limit);
        }
        return str;
    }
}
