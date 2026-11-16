// 151 · Fast Power
package p151

func PowerMod(x, n, m int) int {
	if n == 0 {
		return 1 % m
	}
	half := PowerMod(x, n/2, m) // one recursive call, used twice
	square := half * half % m
	if n%2 == 0 {
		return square
	}
	return square * (x % m) % m
}
