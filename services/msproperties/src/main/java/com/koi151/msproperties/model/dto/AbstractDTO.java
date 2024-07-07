//package com.koi151.msproperties.model.dto;
//
//import lombok.Data;
//
//import java.io.Serial;
//import java.io.Serializable;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//@Data
//public class AbstractDTO<T> implements Serializable {
//
//    @Serial
//    private static final long serialVersionUID = -4013430427543366540L;
//
//    private LocalDateTime createdDate;
////    private String createdBy;
//    private LocalDateTime modifiedDate;
////    private String modifiedBy;
//    private int maxPageItems = 2;
//    private int page = 1;
//    private List<T> listResult = new ArrayList<>();
//    private int totalItems = 0;
//    private String tableId = "tableList";
//    private Integer limit;
//    private Integer totalPage;
//    private Integer totalItem;
//    private String searchValue;
//
//    public int getTotalPages() {
//        return (int) Math.ceil((double) this.getTotalItems() / this.getMaxPageItems());
//    }
//
//}