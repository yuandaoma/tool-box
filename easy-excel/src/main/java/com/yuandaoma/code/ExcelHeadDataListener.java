package com.yuandaoma.code;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.enums.RowTypeEnum;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.Cell;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.read.metadata.holder.ReadRowHolder;
import org.apache.poi.ss.usermodel.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.yuandaoma.code.OfflineFileVo.Head;

/**
 * 模板的读取类
 *
 * @author Jiaju Zhuang
 */
// 有个很重要的点 ExcelHeadDataListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
public class ExcelHeadDataListener extends AnalysisEventListener<HashMap<String, String>> {


    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelHeadDataListener.class);
    private static final String TIME = "time";
    public static final String DATE = "date";
    private static final String TIMESTAMP = "timestamp";
    private static final String FLOAT8 = "float8";
    private static final String INT8 = "int8";
    private static final String BOOL = "bool";
    private static final String VARCHAR = "varchar";

    private boolean parseFirstDataRow = false;

    private boolean readHead = false;


    private Map<String, List<Head>> headsMap = new LinkedHashMap<>();

    public Map<String, List<Head>> getHeadsMap() {
        return headsMap;
    }

    @Override
    public void invoke(HashMap<String, String> data, AnalysisContext analysisContext) {
        if (readHead) {
            final String sheetName = analysisContext.readSheetHolder().getSheetName();
            ReadRowHolder rowHolder = analysisContext.readRowHolder();
            final Integer rowIndex = rowHolder.getRowIndex();
            LOGGER.info("当前工作簿-{}，读取行数:{}",sheetName,rowIndex);
            RowTypeEnum rowType = rowHolder.getRowType();
            if (rowType.equals(RowTypeEnum.DATA)) {
                // 如果是数据
                Map<Integer, Cell> cellMap = rowHolder.getCellMap();
                Set<Integer> columnIndex = cellMap.keySet();
                for (Integer index : columnIndex) {
                    CellData cellData = (CellData) cellMap.get(index);
                    if (headsMap.containsKey(sheetName)) {
                        List<Head> heads = headsMap.get(sheetName);
                        heads.get(index).setHeadType(getColumnTypeByCellData(cellData));
                    } else {
                        LOGGER.error("无法解析的数据行");
                    }
                }
                if (rowIndex == 1){
                    parseFirstDataRow = true;
                    doAfterAllAnalysed(analysisContext);
                }
            } else {
                LOGGER.error("当前数据行为空行");
            }
            LOGGER.info("当前工作簿{}表头信息为{}", sheetName, headsMap);
        }

    }

    private String getColumnTypeByCellData(final CellData cellData) {
        if (cellData.getType().equals(CellDataTypeEnum.NUMBER)) {
            String dataFormatString = cellData.getDataFormatString();
            BigDecimal numberValue = cellData.getNumberValue();
            if (dataFormatString != null) {
                boolean aDateFormat = DateUtil.isADateFormat(0, dataFormatString);
                if (aDateFormat) {
                    Integer dataFormat = cellData.getDataFormat();
                    if (dataFormat != null) {
                        switch (dataFormat) {
                            case 14:
                                return DATE;
                            case 21:
                                return TIME;
                            case 22:
                            default:
                                return TIMESTAMP;
                        }
                    }

                } else {
                    if (numberValue.scale() != 0) {
                        return FLOAT8;
                    } else {
                        return INT8;
                    }
                }
            } else {
                if (numberValue.scale() != 0) {
                    return FLOAT8;
                } else {
                    return INT8;
                }
            }
        } else if (cellData.getType().equals(CellDataTypeEnum.BOOLEAN)) {
            return BOOL;
        }
        return VARCHAR;
    }

    /**
     * Returns the header as a map.Override the current method to receive header data.
     *
     * @param headMap
     * @param context
     */
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        final String sheetName = context.readSheetHolder().getSheetName();
        List<Head> heads = headMap.keySet().stream().map(e -> new Head(headMap.get(e))).collect(Collectors.toList());
        headsMap.put(sheetName, heads);
        readHead = true;
    }

    @Override
    public boolean hasNext(AnalysisContext context) {
//        context.
////        if (parseFirstDataRow) {
////            return false;
////        } else {
            return super.hasNext(context);
//        }
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        readHead = false;
        parseFirstDataRow = false;
        LOGGER.info("所有数据解析完成！");

    }


}