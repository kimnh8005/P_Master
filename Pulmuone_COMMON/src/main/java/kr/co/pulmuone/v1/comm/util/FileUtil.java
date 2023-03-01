package kr.co.pulmuone.v1.comm.util;


import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import kr.co.pulmuone.v1.comm.base.vo.FileVo;
import kr.co.pulmuone.v1.comm.exception.UserException;
import kr.co.pulmuone.v1.comm.framework.dto.AddFileUploadListResponseDto;


public final class FileUtil {

    //-- 파일 최대 크기 5M
    public static final int MAX_FILE_SIZE = 1024 * 1024 * 5 ;

    //-- 허용가능 공통 확장자
    public static String[] systemPermitExt = new String[] {
        "txt","hwp","pdf","doc","docx","ppt","pptx","xls","xlsx","odp","jpg","jpeg","gif","png","bmp"
    };



    /**
     * @MethodName  : uploadFiles
     * @description :파일을 저장한다.
     * @param   MultipartHttpServletRequest
     * @return  AddFileUploadListResponseDto
     * @throws Exception
     */
    public static AddFileUploadListResponseDto uploadMultiFiles (HttpServletRequest request, Map<String, Object> fileCheckInfo) throws Exception {

        //------------------------------------------------------------------------
        //---- Map<String, Object> fileCheckInfo 에 저장한 자료 목록
        //------------------------------------------------------------------------
    	//-- 파일을 저장하는 상위 경로      : String  rootPath
    	//-- 파일 확장자를 검사하는 지 유무 : Boolean fileExtCheck
    	//-- 파일 크기를 검사하는 지 유무   : Boolean fileSizeCheck
    	//-- 업무별, 화면별로 파일을 따로 저장하는 하위 경로 : String  domainSubPath
    	//-- 제한하는 파일 확장자 목록      : List<String> localPermitExt
        //------------------------------------------------------------------------

        //------------------------------------------------------------------------
        //---- Map<String, Object> fileCheckInfo 에 정보를 저장하는 예
        //------------------------------------------------------------------------
    	/*
        EnvConfig envConfig = EnvConfig.getInstance();
        String rootPath     = envConfig.getValue("FILE_UPLOAD_REPOSITORY");

        Map<String, Object> fileCheckInfo = new HashMap<String, Object> ();
        fileCheckInfo.put("rootPath"     , rootPath);
        fileCheckInfo.put("fileExtCheck" , true    );
        fileCheckInfo.put("fileSizeCheck", true    );
        fileCheckInfo.put("domainSubPath", ""      );

        List<String> localPermitExt = new ArrayList<String> ();
        localPermitExt.add("png");
        fileCheckInfo.put("localPermitExt", localPermitExt);
        */


    	MultipartHttpServletRequest mhsr = (MultipartHttpServletRequest)request;

        //------------------------------------------------------------------------
        //---- 서버에 저장할 수 있는 파일인 지 검사한다.
        //------------------------------------------------------------------------
        //---- a) 파일 확장자 검사
        //---- b) 파일 크기 검사
        //------------------------------------------------------------------------
        checkSaveFileExtension(mhsr, fileCheckInfo);
        checkSaveFileSize     (mhsr, fileCheckInfo);

        //------------------------------------------------------------------------
        //---- 저장할 파일 경로를 조사한다.
        //------------------------------------------------------------------------
        Map<String, String> pathInfo = getFileSavePath(fileCheckInfo);

        //------------------------------------------------------------------------
        //---- 반환용 객체
        //------------------------------------------------------------------------
        List <FileVo> listFileVO = new ArrayList<FileVo> ();

        //------------------------------------------------------------------------
        //---- 서버에 파일을 저장한다. - 시작
        //------------------------------------------------------------------------
        Iterator<String> iterator = mhsr.getFileNames();

        while (iterator.hasNext()) {

        	FileVo fileVO    = new FileVo();

            String fieldName = iterator.next();

            List<MultipartFile> fileList = mhsr.getFiles(fieldName);

            fileList.stream().filter(file -> file.isEmpty() == false)
                    .forEach(file -> {
                        try {
                        	String physicalFileName = getPhysicalFileName();
                            String originalFileName = file.getOriginalFilename();
                    		String fileExt          = originalFileName.substring(originalFileName.lastIndexOf(".") + 1, originalFileName.length());;

                            Files.write(Paths.get(pathInfo.get("saveFullPath") + physicalFileName), file.getBytes());

                            //------------------------------------------------------------------------
                            //---- 브라우저에 반환할 파일 정보를 작성한다. - 시작
                            //------------------------------------------------------------------------
                            fileVO.setFieldName       (fieldName            );
                            fileVO.setOriginalFileName(originalFileName     );
                            fileVO.setPhysicalFileName(physicalFileName     );
                            fileVO.setContentType     (file.getContentType());
                            fileVO.setFileSize        (file.getSize()       );
                            fileVO.setFileExt         (fileExt              );
                            fileVO.setServerSubPath   (pathInfo.get("serverSubPath"));
                            fileVO.setSaveResult      (BaseEnums.Default.SUCCESS.getCode());
                            listFileVO.add(fileVO);
                            //------------------------------------------------------------------------
                            //---- 브라우저에 반환할 파일 정보를 작성한다. - 끝
                            //------------------------------------------------------------------------
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        }
        //------------------------------------------------------------------------
        //---- 서버에 파일을 저장한다. - 끝
        //------------------------------------------------------------------------

        AddFileUploadListResponseDto responseDto = new AddFileUploadListResponseDto();
        responseDto.setRows (listFileVO       );
        responseDto.setTotal(listFileVO.size());
        responseDto.setRETURN_CODE(BaseEnums.Default.SUCCESS.getCode()   );
        responseDto.setRETURN_MSG (BaseEnums.Default.SUCCESS.getMessage());

        return responseDto;
    }


    public static ResponseEntity<Resource> downloadSingleFile (FileVo file) throws Exception {

        //------------------------------------------------------------------------------
        //---- 파일 정보를 추출한 후 Resource 생성 - 시작
        //------------------------------------------------------------------------------
    	//EnvConfig envConfig = EnvConfig.getInstance();

        //String rootPath         = envConfig.getValue("FILE_UPLOAD_REPOSITORY");
        String rootPath         = "";
        String serverSubPath    = file.getServerSubPath();
        String physicalFileName = file.getPhysicalFileName();
        String originalFileName = file.getOriginalFileName();
        String fileFullName     = rootPath + serverSubPath + physicalFileName;

        Resource fileResource = new FileSystemResource(fileFullName);
        //------------------------------------------------------------------------------
        //---- 파일 정보를 추출한 후 Resource 생성 - 끝
        //------------------------------------------------------------------------------

        //------------------------------------------------------------------------------
        //---- HttpHeader 작성 - 시작
        //------------------------------------------------------------------------------
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE        , file.getContentType());
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + new String(originalFileName.getBytes("UTF-8"), "ISO-8859-1") );
        headers.add(HttpHeaders.CONTENT_LENGTH      , String.valueOf(file.getFileSize() ));
        headers.add(HttpHeaders.CONTENT_ENCODING    , "binary");
        headers.add("Pragma" , "no-cache");
        headers.add("Expires", "0");
        //------------------------------------------------------------------------------
        //---- HttpHeader 작성 - 끝
        //------------------------------------------------------------------------------

        return new ResponseEntity<Resource> (fileResource, headers, HttpStatus.OK);
    }


    public static void downloadMultipleFiles (List<FileVo> files) throws Exception {
         /*
    	//dowloadFileMulti(HttpServletResponse resp, String where, String serverSubPath, List<File> listPhysicalFile, List<String> orginalFileNames) throws Exception{

        //UUID uuid = UUID.randomUUID();
        //create random UUID
//        String fileName = DateUtil.getCurrentDate().replace("-","_");

        // set content type is zip file
//        response.setContentType("Content-type: text/zip");

        // set header for download
//        response.setHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes(),"UTF-8") + ".zip\";");
//        response.setHeader("Content-Transfer-Encoding", "binary");
//        response.setHeader("Pragma", "no-cache");
//        response.setHeader("Expires", "0");

        ByteArrayOutputStream outputBuffer  = null;

        //if (listPhysicalFile.isEmpty()) {
        //    throw new UserException("5700", "파일이 없습니다.");
        //}

        try {
            // create new output buffer to compress files to zip file
            outputBuffer = new ByteArrayOutputStream();

            // compress to zip file
            //compressToZipFile(outputBuffer, listPhysicalFile, orginalFileNames);

            // write zip file in stream
            //response.getOutputStream().write(outputBuffer.toByteArray());
            //response.getOutputStream().flush();
        } finally {
            if (outputBuffer != null) {
                outputBuffer.close();
            }
        }

        //-- 압축한다.---

        */

    }


    /**
     * @MethodName  : compressToZipFile
     * @description :
     * @param OutputStream
     * @param List<File>
     * @param List<String>
     * @return void
     * @throws IOException
    */
    private static void compressToZipFile (final OutputStream out
                                              , List<File> files
                                              , List<String> listOriginalName) throws IOException {
        /*
        HashSet<String> fileNamesHashSet = new HashSet<String>();

        // Create zip output stream
        ZipOutputStream zipOutputStream = new ZipOutputStream(out);

        zipOutputStream.setLevel(ZipOutputStream.STORED);

        for (int i = 0; i < files.size(); i++) {
            FileInputStream fin = new FileInputStream(files.get(i));

            // set byte[] for one file
            byte[] oneFileContent = new byte[(int) files.get(i).length()];

            String ext = listOriginalName.get(i).toString().substring(listOriginalName.get(i).toString().lastIndexOf(".") + 1);
            String filename = listOriginalName.get(i).toString().substring(0, listOriginalName.get(i).toString().lastIndexOf("."));
            String entryName = getFileNames(fileNamesHashSet, filename, "", ext, 0);

            //add file to zip file
            addOneFileToZipArchive(fin, zipOutputStream, entryName,
            oneFileContent);
        }

        // close zip out put stream
        zipOutputStream.close();
        */
    }


    /**
     * @MethodName  : addOneFileToZipArchive
     * @description :
     * @param FileInputStream
     * @param ZipOutputStream
     * @param String
     * @param byte[]
     * @return void
     * @throws IOException
    */
    private static void addOneFileToZipArchive(   final FileInputStream fileInputStream
                                                , final ZipOutputStream zipStream
                                                , String fileName
                                                , byte[] content) throws IOException {

        //Create new zip entry by file name
        ZipEntry zipEntry = new ZipEntry(fileName);

        //put the file to zip file
        zipStream.putNextEntry(zipEntry);

        // write file content in zip outut stream
        int length;

        while((length = fileInputStream.read(content)) > 0) {
            zipStream.write(content, 0, length);
        }

        //zipStream.write(content);
        // close entry of zip ouput stream
        zipStream.closeEntry();
    }


    /**
     * @MethodName  : checkSystemPermitExt
     * @description : 업로드 가능한 확장자인지 검사한다.
     * @param String
     * @return boolean
     * @throws
    */
    private static boolean checkSystemPermitExt (String ext) {
        if (ext != null && ext.length() > 0) {
            for (int i = 0; i < systemPermitExt.length; i++) {
                if (ext.matches("(?i)" + systemPermitExt[i] + "$")) {
                    return true;
                }
            }
        }

        return false;
    }


    /**
     * @MethodName  : isAbleToSaveFile
     * @description : 서버에 파일을 저장하기 전에 파일 확장자를 검사한다.
     * @param MultipartHttpServletRequest
     * @param Map<String, Object>
     * @return void
     * @throws
    */
    private static void checkSaveFileExtension (MultipartHttpServletRequest mhsr, Map<String, Object> fileCheckInfo) throws Exception {

        boolean fileExtCheck = (boolean)fileCheckInfo.get("fileExtCheck");
        if (fileExtCheck == false) { return; }

    	List<String> localPermitExt = (List<String>)fileCheckInfo.get("localPermitExt");

    	Iterator<String> iterator = mhsr.getFileNames();

        while (iterator.hasNext()) {
            String fieldName = iterator.next();

            List<MultipartFile> fileList = mhsr.getFiles(fieldName);

            for (MultipartFile file : fileList) {
                String originalFileName = file.getOriginalFilename();

                //-- 확장자가 없을 경우
                if (originalFileName.lastIndexOf(".") < 0) { continue; }

                if (localPermitExt != null) {
                    if (localPermitExt.contains(file.getContentType().toLowerCase())) {
                    	throw new UserException("9999", "저장할 수 있는 파일 종류가 아닙니다.");
                    }
        		} else {
        			String fileExt = originalFileName.substring(originalFileName.lastIndexOf(".") + 1, originalFileName.length());

                    if (!checkSystemPermitExt(fileExt)) {
                    	throw new UserException("9999", "서버에 저장할 수 있는 파일 종류가 아닙니다.");
                    }
                }
            }
        }
    }


    /**
     * @MethodName  : checkSaveFileSize
     * @description : 서버에 파일을 저장하기 전에 파일크기를 검사한다.
     * @param MultipartHttpServletRequest
     * @param ap<String, Object>
     * @return void
     * @throws
    */
    private static void checkSaveFileSize (MultipartHttpServletRequest mhsr, Map<String, Object> fileCheckInfo) throws Exception {

        boolean fileSizeCheck = (boolean)fileCheckInfo.get("fileSizeCheck");
        if (fileSizeCheck == false) { return; }

        Iterator<String> iterator = mhsr.getFileNames();
        while (iterator.hasNext()) {
            String fieldName = iterator.next();

            List<MultipartFile> fileList = mhsr.getFiles(fieldName);

            for (MultipartFile file : fileList) {
                if ( file.getSize() > MAX_FILE_SIZE ) {
                    throw new UserException("9999", "파일 최대 크기를 초과했습니다.");
                }
            }
        }
    }


    /**
     * @MethodName  : getSubPathYearMonthDay
     * @description : YYYY/MM/DD 이름으로 하위 디렉토리를 만든다.
     * @return String (YYYY/MM/DD 로 생성한 하위 디렉토리 이름)
     * @throws
    */
    private static String getSubPathYearMonthDay() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date   today    = new Date();

        String strToday = dateFormat.format(today);

        return strToday.replace("-", File.separator);
    }


    /**
     * @MethodName  : getFileSavePath
     * @description : 파일을 저장할 경로를 생성하고 그 정보를 반환한다.
     * @return Map<String, String>
     * @throws
    */
    private static Map<String, String> getFileSavePath(Map<String, Object> fileCheckInfo) throws Exception {

        Map<String, String> pathInfo = new HashMap<String, String> ();

        String rootPath         = (String)fileCheckInfo.get("rootPath"     );
        String domainSubPath    = (String)fileCheckInfo.get("domainSubPath");
        String pathYearMonthDay = getSubPathYearMonthDay();
        String serverSubPath    = File.separator + domainSubPath + File.separator + pathYearMonthDay + File.separator;
        String saveFullPath     = rootPath + serverSubPath;

        pathInfo.put("serverSubPath", serverSubPath);
        pathInfo.put("saveFullPath" , saveFullPath );

		File targetDir = new File(saveFullPath);
		if (!targetDir.exists()) {	//디렉토리가 없는 경우 생성
			targetDir.mkdirs();
		}

        if ( rootPath == null || "Not found data".equals(rootPath) || "".equals(rootPath) ) {
            throw new UserException("9999", "환경설정에 파일 저장 정보가 없습니다.");
        }

        return pathInfo;
    }


    /**
     * @MethodName  : getPhysicalFileName
     * @description : 물리적인 파일 이름을 생성한다.
     * @return String (물리적 파일명)
     * @throws IOException
    */
    private static String getPhysicalFileName() {
        return UidUtil.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }


    /**
     * @MethodName  : convert
     * @description : 물리적인 파일 이름을 utf-8 로 인코딩한다.
     * @param filename String
     * @return String (utf-8 로 인코딩한 파일명)
     * @throws Exception
    */
    public static String convert(String filename) throws Exception {
        //return java.net.URLEncoder.encode(filename, "utf-8");
        return filename;
    }


}
