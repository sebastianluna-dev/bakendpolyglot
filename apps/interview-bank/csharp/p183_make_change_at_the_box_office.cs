// 183 · Make Change at the Box Office
Check(CanMakeChange([5, 5, 10, 5, 20]));
Check(!CanMakeChange([5, 10, 10, 20]));
Check(CanMakeChange([]));
Check(!CanMakeChange([10]));
Check(!CanMakeChange([5, 5, 20]));
Check(CanMakeChange([5, 5, 5, 20]));
Check(CanMakeChange([5, 5, 5, 5, 10, 20, 10]));
Console.WriteLine("183 ok");

static bool CanMakeChange(int[] bills)
{
    int fives = 0, tens = 0;
    foreach (var bill in bills)
    {
        if (bill == 5)
        {
            fives++;
        }
        else if (bill == 10)
        {
            if (fives == 0) return false;
            fives--;
            tens++;
        }
        else if (tens > 0 && fives > 0)
        {
            // a 10 can only ever change a 20, so spend it before the fives
            tens--;
            fives--;
        }
        else if (fives >= 3)
        {
            fives -= 3;
        }
        else
        {
            return false;
        }
    }
    return true;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
