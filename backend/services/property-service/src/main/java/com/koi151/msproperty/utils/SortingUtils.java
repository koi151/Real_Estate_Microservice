package com.koi151.msproperty.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@UtilityClass
@Slf4j
public class SortingUtils {
    private static final String SORT_DELIMITER = "-";
    private static final String DEFAULT_SORT_FIELD = "createdDate";

    public static Sort buildSort(List<String> sortParams) {
        List<Sort.Order> orders = new ArrayList<>();

        if (!CollectionUtils.isEmpty(sortParams)) {
            orders.addAll(createSortOrders(sortParams));
        }

        ensureDefaultSorting(orders);
        return Sort.by(orders);
    }

    private static List<Sort.Order> createSortOrders(List<String> sortParams) {
        return sortParams.stream()
            .map(SortingUtils::parseSortParam)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    private static Sort.Order parseSortParam(String sortParam) {
        try {
            String[] parts = sortParam.split(SORT_DELIMITER);
            String property = validateAndGetProperty(parts);

            return new Sort.Order(
                determineDirection(parts),
                property
            );
        } catch (IllegalArgumentException e) {
            log.warn("Invalid sort parameter: {}", sortParam);
            return null;
        }
    }

    private static String validateAndGetProperty(String[] parts) {
        if (parts.length == 0 || !StringUtils.checkString(parts[0])) {
            throw new IllegalArgumentException("Sort property cannot be empty");
        }
        return parts[0].trim();
    }

    private static Sort.Direction determineDirection(String[] parts) {
        return parts.length > 1 && "desc".equalsIgnoreCase(parts[1].trim())
            ? Sort.Direction.DESC
            : Sort.Direction.ASC;
    }

    private static void ensureDefaultSorting(List<Sort.Order> orders) {
        boolean hasDefaultSort = orders.stream()
            .anyMatch(order -> order.getProperty().equals(DEFAULT_SORT_FIELD));

        if (!hasDefaultSort) {
            orders.add(Sort.Order.desc(DEFAULT_SORT_FIELD));
        }
    }
}