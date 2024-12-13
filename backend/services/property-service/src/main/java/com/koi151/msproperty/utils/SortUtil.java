package com.koi151.msproperty.utils;

import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class SortUtil {

    public static Sort createSortOrders(String[] sortParams) {
        List<Sort.Order> orders = new ArrayList<>();

        if (sortParams != null) {
            for (String sortParam : sortParams) {
                String[] parts = sortParam.split("-");
                if (parts.length > 0) {
                    String property = parts[0].trim();
                    Sort.Direction direction = parts.length > 1 && "desc".equalsIgnoreCase(parts[1])
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

                    orders.add(Sort.Order.by(property).with(direction));
                }
            }
        }

        // add default sort by createdDate if not present
        if (orders.stream().noneMatch(order -> order.getProperty().equals("createdDate"))) {
            orders.add(Sort.Order.desc("createdDate"));
        }

        return Sort.by(orders);
    }
}
