-- Chapter 27 gate. Every query must return zero rows.

-- 1. no seat belongs to two paid orders
SELECT 'seat in two paid orders' AS violation, seat_id::text AS id
FROM order_lines ol JOIN orders o ON o.id = ol.order_id
WHERE o.status = 'paid' GROUP BY seat_id HAVING count(*) > 1

UNION ALL
-- 2. every sold seat is in exactly one paid order
SELECT 'sold seat without exactly one paid order', s.id::text
FROM seats s
LEFT JOIN order_lines ol ON ol.seat_id = s.id
LEFT JOIN orders o ON o.id = ol.order_id AND o.status = 'paid'
WHERE s.status = 'sold' GROUP BY s.id HAVING count(o.id) <> 1

UNION ALL
-- 3. every seat of a paid order is sold
SELECT 'paid order with an unsold seat', o.id::text
FROM orders o JOIN order_lines ol ON ol.order_id = o.id JOIN seats s ON s.id = ol.seat_id
WHERE o.status = 'paid' AND s.status <> 'sold'

UNION ALL
-- 4. a held seat points at a hold that is still active or checked out
SELECT 'held seat on a dead hold', s.id::text
FROM seats s JOIN holds h ON h.id = s.hold_id
WHERE s.status = 'held' AND h.status NOT IN ('active', 'checked_out');
