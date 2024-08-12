package com.koi151.listing_services.repository.custom;

import java.util.List;
import java.util.Set;

public interface PostServiceRepositoryCustom {

    Set<Long> findMissingPostServiceIds(Set<Long> ids);
}
