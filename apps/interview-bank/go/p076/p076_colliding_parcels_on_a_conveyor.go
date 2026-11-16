// 076 · Colliding Parcels on a Conveyor
package p076

func SurvivingParcels(parcels []int) []int {
	track := []int{}
	for _, p := range parcels {
		alive := true
		for alive && p < 0 && len(track) > 0 && track[len(track)-1] > 0 {
			top := track[len(track)-1]
			switch {
			case top < -p:
				track = track[:len(track)-1]
			case top == -p:
				track = track[:len(track)-1]
				alive = false
			default:
				alive = false
			}
		}
		if alive {
			track = append(track, p)
		}
	}
	return track
}
