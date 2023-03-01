package kr.co.pulmuone.v1.comm.enums.generator;

import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.MessageCommEnum;
import kr.co.pulmuone.v1.comm.enums.UserEnums;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SwaggerMessageGenerator {

    public static void main(String[] args) {

        //--------------------------------------------------------------
        // Response DTO 내 Data Class 명
        // 예)
        //    있을경우 :
        //         String responseDataName = "LoginResponseDto";
        //    없을경우 :
        //         String responseDataName = "";    // 공백
        //--------------------------------------------------------------
        String responseDataName = "";

        //--------------------------------------------------------------
        // Resopnse code / message 의 Enum Class
        // 예)
        //    기본값 SUCCESS / FAIL 일경우 셋팅 하지 않아도됨.
        //      Class responseCodeList = BaseEnums.Default.class;
        //    값이 있을경우 해당 Enum Class 셋팅
        //      Class responseCodeList = UserEnums.Login.class;
        //--------------------------------------------------------------
        Class responseCodeList = BaseEnums.Default.class;

        // * 복사 이후 값이 원상태로 초기화를 해놓는다. Bitbucket 에 올리지 않는다.

        printSwaggerAnnotation(responseDataName, responseCodeList);
    }

    private static void printSwaggerAnnotation(String dataName, Class enumClass) {

        System.out.println("*******************************************************************************************************************");

        if(!"".equals(dataName.trim()) || !enumClass.equals(BaseEnums.Default.class)) {

            Object[] enums = enumClass.getEnumConstants();

            System.out.println("@ApiResponses(value = {");

            if("".equals(dataName.trim())) {
                if(enumClass.equals(BaseEnums.Default.class)) {
                    System.out.println("        @ApiResponse(code = 900, message = \"response data : null\")");
                } else {
                    System.out.println("        @ApiResponse(code = 900, message = \"response data : null\"),");
                }
            } else {
                if(enumClass.equals(BaseEnums.Default.class)) {
                    System.out.println("        @ApiResponse(code = 900, message = \"response data\", response = " + dataName + ".class)");
                } else {
                    System.out.println("        @ApiResponse(code = 900, message = \"response data\", response = " + dataName + ".class),");
                }
            }

            if(!enumClass.equals(BaseEnums.Default.class)) {
                System.out.println("        @ApiResponse(code = 901, message = \"\" + ");

                List<String> list = new ArrayList<>();
                for (Object obj : enums) {
                    MessageCommEnum mc = (MessageCommEnum) obj;
                    list.add(mc + "] " + mc.getCode() + " - " + mc.getMessage());
                }

                List<String> list2 = list.stream().sorted().collect(Collectors.toList());
                int n = 0;
                for(String str : list2) {
                    n++;
                    if(n != list2.size()) {
                        System.out.println("                \"[" + str + " \\n\" +");
                    } else {
                        System.out.println("                \"[" + str + "\"");
                    }
                }

                System.out.println("        )");
            }
            System.out.println("})");
        }

        System.out.println("*******************************************************************************************************************");
    }
}


