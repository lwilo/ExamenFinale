package com.conference.keynote.repository;

import com.conference.keynote.entity.KeynoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeynoteRepository extends JpaRepository<KeynoteEntity, String> {
}
