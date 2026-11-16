// 009 · Rotate the Carousel
Check(Rotated([1, 2, 3, 4, 5], 2).SequenceEqual([4, 5, 1, 2, 3]));
Check(Rotated([10, 20], 5).SequenceEqual([20, 10]));
Check(Rotated([1, 2, 3], 0).SequenceEqual([1, 2, 3]));
Check(Rotated([1, 2, 3], 3).SequenceEqual([1, 2, 3]));
Check(Rotated([7], 4).SequenceEqual([7]));
Check(Rotated([], 3).Length == 0);
Console.WriteLine("009 ok");

static void RotateCarousel(int[] slides, int k)
{
    var n = slides.Length;
    if (n == 0) return;
    k %= n;
    Array.Reverse(slides);
    Array.Reverse(slides, 0, k);     // (array, start, length)
    Array.Reverse(slides, k, n - k);
}

static int[] Rotated(int[] slides, int k)
{
    RotateCarousel(slides, k);
    return slides;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
