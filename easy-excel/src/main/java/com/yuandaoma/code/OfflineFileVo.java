package com.yuandaoma.code;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@EqualsAndHashCode
public class OfflineFileVo extends FileResponseData implements Serializable {


    private List<SheetInfo> infos = new ArrayList<>();

    @Data
    @ToString
    @EqualsAndHashCode
    public static class SheetInfo {
        private Integer sheetIndex;
        private String sheetName;
        private List<Head> heads = new ArrayList<>();
    }


    @Data
    @ToString
    @EqualsAndHashCode
    public static class Head{
        private String headName;
        private String headType = "varchar";

        public Head(String headName) {
            this.headName = headName;
        }
    }
}


