package p075

import "testing"

func TestSimplifyPath(t *testing.T) {
	cases := []struct {
		path string
		want string
	}{
		{"/shelfwise//covers/./2024/../thumbs/", "/shelfwise/covers/thumbs"},
		{"/../a/.../b/..", "/a/..."},
		{"/", "/"},
		{"/../..", "/"},
		{"/a//b////c/d//././/..", "/a/b/c"},
		{"/.hidden/..x/.", "/.hidden/..x"},
	}
	for _, c := range cases {
		if got := SimplifyPath(c.path); got != c.want {
			t.Errorf("SimplifyPath(%q) = %q, want %q", c.path, got, c.want)
		}
	}
}
