// 049 · Push the Zeros to the End
package p049

func PushZerosToEnd(slots []int) {
	write := 0 // slots[:write] holds the parcels seen so far, in order
	for read, id := range slots {
		if id != 0 {
			slots[write], slots[read] = slots[read], slots[write]
			write++
		}
	}
}
