package com.yash.kubesentry.repository;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class SmokeTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void contextLoads() {

    }
}