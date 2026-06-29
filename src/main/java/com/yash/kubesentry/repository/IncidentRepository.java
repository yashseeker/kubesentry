package com.yash.kubesentry.repository;

import com.yash.kubesentry.model.entity.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository; // read what is this

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {

}