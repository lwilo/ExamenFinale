package com.conference.conference.repository;

import com.conference.conference.entity.Conference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConferenceRepository extends JpaRepository<Conference, String> {
}
