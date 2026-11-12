package dev.shelfwise.data;

import java.sql.SQLException;
import org.postgresql.util.PSQLException;
import org.springframework.dao.DataAccessException;

/** Reads the Postgres constraint name out of Spring's translated exception (Chapter 21). */
final class Constraints {
    private Constraints() {}

    static boolean violates(DataAccessException e, String constraint) {
        return e.getMostSpecificCause() instanceof PSQLException p
                && "23505".equals(p.getSQLState())
                && p.getServerErrorMessage() != null
                && constraint.equals(p.getServerErrorMessage().getConstraint());
    }

    static boolean foreignKey(DataAccessException e) {
        return e.getMostSpecificCause() instanceof SQLException s && "23503".equals(s.getSQLState());
    }
}
