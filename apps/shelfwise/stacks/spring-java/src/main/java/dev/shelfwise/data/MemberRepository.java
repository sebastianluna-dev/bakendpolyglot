package dev.shelfwise.data;

import dev.shelfwise.domain.DomainException;
import dev.shelfwise.domain.Models.Member;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {
    private final JdbcClient db;

    public MemberRepository(JdbcClient db) {
        this.db = db;
    }

    public Member create(String email, String name) {
        try {
            long id = db.sql("INSERT INTO members (email, name) VALUES (:email, :name) RETURNING id")
                    .param("email", email).param("name", name).query(Long.class).single();
            return new Member(id, email, name);
        } catch (DataAccessException e) {
            if (Constraints.violates(e, "members_email_key")) throw DomainException.duplicateEmail(email);
            throw e;
        }
    }

    public boolean exists(long id) {
        return db.sql("SELECT EXISTS (SELECT 1 FROM members WHERE id = :id)").param("id", id).query(Boolean.class).single();
    }
}
