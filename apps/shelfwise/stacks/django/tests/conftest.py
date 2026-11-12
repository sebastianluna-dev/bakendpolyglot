import os

# The domain tests need no database; settings only require the variable to be present.
os.environ.setdefault("DATABASE_URL", "postgres://unused:unused@localhost:5432/unused")
