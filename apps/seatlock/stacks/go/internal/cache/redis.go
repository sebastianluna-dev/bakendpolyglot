// Package cache is Redis behind domain.Cache, with singleflight so a cold key is loaded once per
// process, not once per concurrent request (Chapter 25). Redis errors fall back to loading.
package cache

import (
	"context"
	"time"

	"github.com/redis/go-redis/v9"
	"golang.org/x/sync/singleflight"
)

type Redis struct {
	rdb   *redis.Client
	group singleflight.Group
}

func New(url string) (*Redis, error) {
	opt, err := redis.ParseURL(url)
	if err != nil {
		return nil, err
	}
	return &Redis{rdb: redis.NewClient(opt)}, nil
}

func (c *Redis) GetOrLoad(ctx context.Context, key string, ttl time.Duration, load func() ([]byte, error)) ([]byte, error) {
	if b, err := c.rdb.Get(ctx, key).Bytes(); err == nil {
		return b, nil
	}
	v, err, _ := c.group.Do(key, func() (any, error) {
		b, err := load()
		if err == nil {
			c.rdb.Set(ctx, key, b, ttl) // best effort
		}
		return b, err
	})
	if err != nil {
		return nil, err
	}
	return v.([]byte), nil
}

func (c *Redis) Close() error { return c.rdb.Close() }
