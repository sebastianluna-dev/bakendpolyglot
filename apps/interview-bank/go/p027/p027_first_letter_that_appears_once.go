// 027 · First Letter That Appears Once
package p027

func FirstUniqueIndex(word string) int {
	var counts [26]int
	for i := 0; i < len(word); i++ {
		counts[word[i]-'a']++
	}
	for i := 0; i < len(word); i++ {
		if counts[word[i]-'a'] == 1 {
			return i
		}
	}
	return -1
}
