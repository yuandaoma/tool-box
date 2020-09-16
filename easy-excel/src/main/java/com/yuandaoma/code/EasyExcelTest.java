package com.yuandaoma.code;

import com.alibaba.excel.EasyExcel;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @createDate 2020/9/16-11:05
 */
public class EasyExcelTest {


    public static final String FILE_NAME = "src/main/resources/fastdfs.xlsx";
    public static final String FILE_NAME_2 = "src/main/resources/测试三个工作簿.xlsx";
    public static final String FILE_NAME_3 = "src/main/resources/测试两个工作簿.xlsx";
    public static final String FILE_NAME_4 = "src/main/resources/2007.xlsx";

    private static final Logger LOGGER = LoggerFactory.getLogger(EasyExcelTest.class);


    @Test
    public void testReadExcelHead() {
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        try {
            EasyExcel.read(FILE_NAME,new ExcelHeadDataListener()).sheet().doRead();
        }catch (Exception e){
            LOGGER.info(e.getMessage());
        }

    }

    @Test
    public void testReadExcelHead2() {
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        try {
            ExcelHeadDataListener listener = new ExcelHeadDataListener();
            EasyExcel.read(FILE_NAME_2,listener).doReadAllSync();
            final Map<String, List<OfflineFileVo.Head>> headsMap = listener.getHeadsMap();
            LOGGER.info("headsMap: {}",headsMap);

        }catch (Exception e){
            LOGGER.info(e.getMessage());
        }

    }

    @Test
    public void testReadExcelHead3() {
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        try {
            ExcelHeadDataListener listener = new ExcelHeadDataListener();
            EasyExcel.read(FILE_NAME_3,listener).doReadAll();
            final Map<String, List<OfflineFileVo.Head>> headsMap = listener.getHeadsMap();
            LOGGER.info("headsMap: {}",headsMap);

        }catch (Exception e){
            LOGGER.info(e.getMessage());
        }

    }

    @Test
    public void testReadExcelHead4() {
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        try {
            ExcelHeadDataListener listener = new ExcelHeadDataListener();
            EasyExcel.read(FILE_NAME_4,listener).doReadAll();
            final Map<String, List<OfflineFileVo.Head>> headsMap = listener.getHeadsMap();
            LOGGER.info("headsMap: {}",headsMap);

        }catch (Exception e){
            LOGGER.info(e.getMessage());
        }

    }
}
