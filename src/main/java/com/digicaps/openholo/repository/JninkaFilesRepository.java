package com.digicaps.openholo.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.digicaps.openholo.api.JninkaFilesEntity;

public interface JninkaFilesRepository extends JpaRepository<JninkaFilesEntity, Long> {
    
}
