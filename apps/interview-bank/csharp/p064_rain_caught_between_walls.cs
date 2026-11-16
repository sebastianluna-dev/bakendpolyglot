// 064 · Rain Caught Between Walls
Check(TrappedRain([3, 0, 2, 0, 4]) == 7);
Check(TrappedRain([5, 4, 3]) == 0);
Check(TrappedRain([]) == 0);
Check(TrappedRain([7]) == 0);
Check(TrappedRain([2, 0, 2]) == 2);
Check(TrappedRain([0, 2, 0, 1, 0, 3, 1, 0, 2]) == 8);
Check(TrappedRain([4, 1, 1, 4]) == 6);
Check(TrappedRain([3, 0, 1]) == 1);
Console.WriteLine("064 ok");

static int TrappedRain(int[] heights)
{
    int left = 0, right = heights.Length - 1, leftMax = 0, rightMax = 0, water = 0;
    while (left < right)
    {
        // The lower end decides: the far side has a wall at least this high.
        if (heights[left] < heights[right])
        {
            leftMax = Math.Max(leftMax, heights[left]);
            water += leftMax - heights[left];
            left++;
        }
        else
        {
            rightMax = Math.Max(rightMax, heights[right]);
            water += rightMax - heights[right];
            right--;
        }
    }
    return water;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
