// 137 · Change One Letter at a Time
package p137

func ShortestChain(start, target string, words []string) int {
	dictionary := map[string]bool{}
	for _, w := range words {
		dictionary[w] = true
	}
	length := map[string]int{start: 1} // words in the shortest chain that ends here
	queue := []string{start}
	for len(queue) > 0 {
		word := queue[0]
		queue = queue[1:]
		if word == target {
			return length[word]
		}
		chars := []byte(word)
		for i := range chars {
			original := chars[i]
			for c := byte('a'); c <= 'z'; c++ {
				chars[i] = c
				next := string(chars)
				if _, seen := length[next]; dictionary[next] && !seen {
					length[next] = length[word] + 1
					queue = append(queue, next)
				}
			}
			chars[i] = original
		}
	}
	return 0
}
