// 062 · Stretches With a Small Product
Check(CountSmallProducts([4, 2, 6, 1, 3], 15) == 10);
Check(CountSmallProducts([1, 1, 1], 1) == 0);
Check(CountSmallProducts([1, 1, 1], 2) == 6);
Check(CountSmallProducts([], 10) == 0);
Check(CountSmallProducts([7], 0) == 0);
Check(CountSmallProducts([10, 5, 2], 100) == 5);
Check(CountSmallProducts([1000, 1000], 1000000) == 2);
Console.WriteLine("062 ok");

static int CountSmallProducts(int[] factors, int limit)
{
    if (limit <= 1) return 0; // every product is at least 1
    int left = 0, product = 1, count = 0; // product of factors[left..right]
    for (var right = 0; right < factors.Length; right++)
    {
        product *= factors[right];
        while (product >= limit)
        {
            product /= factors[left];
            left++;
        }
        count += right - left + 1; // every stretch that ends at right and starts at left or later
    }
    return count;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
