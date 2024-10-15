package com.koi151.listing_services.repository.custom;

import java.util.List;

public interface PostServiceRepositoryCustom {

    List<Long> findMissingPostServiceIds(List<Long> ids);
}
