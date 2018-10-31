package com.microservice.redpackage.service;

/**
 * @author 上官炳强
 * @description
 * @since 2018-10-15 / 09:53:31
 */
public interface UserRedPackageService {
    Long grabRedPackageByRedis(Long redPackageId, Long userId);
}
