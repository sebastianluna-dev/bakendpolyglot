using Microsoft.EntityFrameworkCore;

namespace SeatLock.Data;

// Entities over the shared schema (db/migrations owns the tables; no EF migrations here).
public sealed class Event
{
    public Guid Id { get; set; }
    public string Name { get; set; } = "";
    public string Venue { get; set; } = "";
    public DateTimeOffset StartsAt { get; set; }
    public string CreatedBy { get; set; } = "";
    public DateTimeOffset CreatedAt { get; set; }
    public List<Seat> Seats { get; set; } = [];
}

public sealed class Seat
{
    public Guid Id { get; set; }
    public Guid EventId { get; set; }
    public string Section { get; set; } = "";
    public string RowLabel { get; set; } = "";
    public int Number { get; set; }
    public int PriceCents { get; set; }
    public string Status { get; set; } = "available";
    public Guid? HoldId { get; set; }
}

public sealed class Hold
{
    public Guid Id { get; set; }
    public Guid EventId { get; set; }
    public string UserId { get; set; } = "";
    public Guid[] SeatIds { get; set; } = [];
    public string Status { get; set; } = "active";
    public DateTimeOffset CreatedAt { get; set; }
    public DateTimeOffset ExpiresAt { get; set; }
}

public sealed class Order
{
    public Guid Id { get; set; }
    public Guid HoldId { get; set; }
    public Hold Hold { get; set; } = null!;
    public string UserId { get; set; } = "";
    public int TotalCents { get; set; }
    public string Currency { get; set; } = "USD";
    public string Status { get; set; } = "pending";
    public string? PaymentIntentId { get; set; }
    public DateTimeOffset CreatedAt { get; set; }
    public DateTimeOffset UpdatedAt { get; set; }
    public List<OrderLine> Lines { get; set; } = [];
}

public sealed class OrderLine
{
    public Guid OrderId { get; set; }
    public Guid SeatId { get; set; }
    public int PriceCents { get; set; }
}

public sealed class IdempotencyKey
{
    public string Key { get; set; } = "";
    public string UserId { get; set; } = "";
    public string RequestHash { get; set; } = "";
    public int? Status { get; set; }
    public string? Response { get; set; }   // jsonb, kept as text
}

public sealed class SeatLockDb(DbContextOptions<SeatLockDb> options) : DbContext(options)
{
    public DbSet<Event> Events => Set<Event>();
    public DbSet<Seat> Seats => Set<Seat>();
    public DbSet<Hold> Holds => Set<Hold>();
    public DbSet<Order> Orders => Set<Order>();
    public DbSet<OrderLine> OrderLines => Set<OrderLine>();
    public DbSet<IdempotencyKey> IdempotencyKeys => Set<IdempotencyKey>();

    protected override void OnModelCreating(ModelBuilder b)
    {
        b.Entity<Event>(e =>
        {
            e.ToTable("events");
            e.Property(x => x.Id).HasColumnName("id");
            e.Property(x => x.Name).HasColumnName("name");
            e.Property(x => x.Venue).HasColumnName("venue");
            e.Property(x => x.StartsAt).HasColumnName("starts_at");
            e.Property(x => x.CreatedBy).HasColumnName("created_by");
            e.Property(x => x.CreatedAt).HasColumnName("created_at");
            e.HasMany(x => x.Seats).WithOne().HasForeignKey(s => s.EventId);
        });
        b.Entity<Seat>(e =>
        {
            e.ToTable("seats");
            e.Property(x => x.Id).HasColumnName("id");
            e.Property(x => x.EventId).HasColumnName("event_id");
            e.Property(x => x.Section).HasColumnName("section");
            e.Property(x => x.RowLabel).HasColumnName("row_label");
            e.Property(x => x.Number).HasColumnName("number");
            e.Property(x => x.PriceCents).HasColumnName("price_cents");
            e.Property(x => x.Status).HasColumnName("status");
            e.Property(x => x.HoldId).HasColumnName("hold_id");
        });
        b.Entity<Hold>(e =>
        {
            e.ToTable("holds");
            e.Property(x => x.Id).HasColumnName("id");
            e.Property(x => x.EventId).HasColumnName("event_id");
            e.Property(x => x.UserId).HasColumnName("user_id");
            e.Property(x => x.SeatIds).HasColumnName("seat_ids");
            e.Property(x => x.Status).HasColumnName("status");
            e.Property(x => x.CreatedAt).HasColumnName("created_at");
            e.Property(x => x.ExpiresAt).HasColumnName("expires_at");
        });
        b.Entity<Order>(e =>
        {
            e.ToTable("orders");
            e.Property(x => x.Id).HasColumnName("id");
            e.Property(x => x.HoldId).HasColumnName("hold_id");
            e.HasOne(x => x.Hold).WithMany().HasForeignKey(x => x.HoldId);
            e.Property(x => x.UserId).HasColumnName("user_id");
            e.Property(x => x.TotalCents).HasColumnName("total_cents");
            e.Property(x => x.Currency).HasColumnName("currency");
            e.Property(x => x.Status).HasColumnName("status");
            e.Property(x => x.PaymentIntentId).HasColumnName("payment_intent_id");
            e.Property(x => x.CreatedAt).HasColumnName("created_at");
            e.Property(x => x.UpdatedAt).HasColumnName("updated_at");
            e.HasMany(x => x.Lines).WithOne().HasForeignKey(l => l.OrderId);
        });
        b.Entity<OrderLine>(e =>
        {
            e.ToTable("order_lines");
            e.HasKey(x => new { x.OrderId, x.SeatId });
            e.Property(x => x.OrderId).HasColumnName("order_id");
            e.Property(x => x.SeatId).HasColumnName("seat_id");
            e.Property(x => x.PriceCents).HasColumnName("price_cents");
        });
        b.Entity<IdempotencyKey>(e =>
        {
            e.ToTable("idempotency_keys");
            e.HasKey(x => new { x.UserId, x.Key });
            e.Property(x => x.Key).HasColumnName("key");
            e.Property(x => x.UserId).HasColumnName("user_id");
            e.Property(x => x.RequestHash).HasColumnName("request_hash");
            e.Property(x => x.Status).HasColumnName("status");
            e.Property(x => x.Response).HasColumnName("response").HasColumnType("jsonb");
        });
    }
}
