package com.microservice.redpackage.service;

/**
 * @author 上官炳强
 * @description
 * @since 2018-10-15 / 09:08:18
 */
public interface RedPackageService {
    void saveUserRedPackageByRedis(Long id, Double unitAccount);
}
