package dev.shelfwise.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.shelfwise.data.LoanRepository;
import dev.shelfwise.data.MemberRepository;
import dev.shelfwise.domain.DomainException;
import dev.shelfwise.domain.LoanService;
import dev.shelfwise.domain.Models.Loan;
import dev.shelfwise.domain.Models.LoanCursor;
import dev.shelfwise.domain.Models.Member;
import dev.shelfwise.domain.Models.Page;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Members and loans. The rules live in LoanService; this class only translates HTTP. */
@RestController
class LendingController {
    private final MemberRepository members;
    private final LoanRepository loans;
    private final LoanService lending;
    private final ObjectMapper json;

    LendingController(MemberRepository members, LoanRepository loans, LoanService lending, ObjectMapper json) {
        this.members = members;
        this.loans = loans;
        this.lending = lending;
        this.json = json;
    }

    @PostMapping("/members")
    ResponseEntity<Member> createMember(@RequestBody(required = false) String raw) {
        var body = Body.read(json, raw, "email", "name");
        var email = body.email("email");
        var name = body.str("name", true, 1, 200);
        body.throwIfInvalid();

        var member = members.create(email, name);
        return ResponseEntity.created(URI.create("/members/" + member.id())).body(member);
    }

    @GetMapping("/members/{id}/loans")
    Page<Loan> memberLoans(@PathVariable String id,
                           @RequestParam(required = false) String status,
                           @RequestParam(required = false) String after,
                           @RequestParam(required = false) String limit) {
        long memberId = Http.parseId(id);
        int n = Http.parseLimit(limit);
        if (status != null && !status.isEmpty() && !status.equals("open") && !status.equals("all"))
            throw DomainException.validation("status", "must be open or all");
        if (!members.exists(memberId)) throw DomainException.notFound("Member", memberId);

        Long before = after == null || after.isEmpty() ? null
                : Cursors.decode(json, after, LoanCursor.class, c -> c.id() > 0).id();
        var rows = loans.forMember(memberId, "open".equals(status), before, n + 1);
        return Http.page(rows, n, l -> Cursors.encode(json, new LoanCursor(l.id())));
    }

    @PostMapping("/loans")
    ResponseEntity<Loan> lend(@RequestBody(required = false) String raw) {
        var body = Body.read(json, raw, "copyId", "memberId");
        var copyId = body.integer("copyId", true, false, 1, 1L << 53, "is too large");
        var memberId = body.integer("memberId", true, false, 1, 1L << 53, "is too large");
        body.throwIfInvalid();

        var loan = lending.lend(copyId, memberId);
        return ResponseEntity.created(URI.create("/loans/" + loan.id())).body(loan);
    }

    @PostMapping("/loans/{id}/return")
    Loan returnLoan(@PathVariable String id) {
        return lending.returnLoan(Http.parseId(id));
    }
}
