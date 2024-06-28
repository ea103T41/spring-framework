package com.euphy.learn.model;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MemberRepository extends MongoRepository<Member, Long> {

    Member findByUsername(String username);
}
