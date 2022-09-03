package com.example.week6project.repository;


import com.example.week6project.domain.ImageMapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageMapperRepository extends JpaRepository<ImageMapper, Long> {
    Optional<ImageMapper> findByImageName(String name);
    Optional<ImageMapper> findByUrl(String url);
}
