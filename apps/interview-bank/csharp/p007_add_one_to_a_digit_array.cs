// 007 · Add One to a Digit Array
int[] input = [1, 2, 9];
Check(AddOne(input).SequenceEqual([1, 3, 0]));
Check(input.SequenceEqual([1, 2, 9]));
Check(AddOne([9, 9, 9]).SequenceEqual([1, 0, 0, 0]));
Check(AddOne([4, 5]).SequenceEqual([4, 6]));
Check(AddOne([0]).SequenceEqual([1]));
Check(AddOne(Enumerable.Repeat(9, 20).ToArray()).SequenceEqual([1, .. new int[20]]));
Console.WriteLine("007 ok");

static int[] AddOne(int[] digits)
{
    var result = (int[])digits.Clone();
    for (var i = result.Length - 1; i >= 0; i--)
    {
        if (result[i] < 9)
        {
            result[i]++;
            return result;
        }
        result[i] = 0;
    }
    return [1, .. result]; // every digit was 9
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
