"""Models over the shared Shelfwise schema (db/migrations). managed = False: Django does not own these tables,
so `migrate` never touches them. In a Django-only project you would let Django generate the migrations."""

from django.db import models


class Book(models.Model):
    id = models.BigAutoField(primary_key=True)
    isbn = models.TextField(unique=True)
    title = models.TextField()
    author = models.TextField()
    year = models.IntegerField(null=True)
    version = models.IntegerField(default=1)

    class Meta:
        managed = False
        db_table = "books"


class Copy(models.Model):
    id = models.BigAutoField(primary_key=True)
    book = models.ForeignKey(Book, on_delete=models.DO_NOTHING, db_column="book_id")
    barcode = models.TextField(unique=True)
    status = models.TextField(default="available")

    class Meta:
        managed = False
        db_table = "copies"


class Member(models.Model):
    id = models.BigAutoField(primary_key=True)
    email = models.TextField(unique=True)
    name = models.TextField()

    class Meta:
        managed = False
        db_table = "members"


class Loan(models.Model):
    id = models.BigAutoField(primary_key=True)
    copy = models.ForeignKey(Copy, on_delete=models.DO_NOTHING, db_column="copy_id")
    member = models.ForeignKey(Member, on_delete=models.DO_NOTHING, db_column="member_id")
    loaned_at = models.DateTimeField()
    due_at = models.DateTimeField()
    returned_at = models.DateTimeField(null=True)

    class Meta:
        managed = False
        db_table = "loans"
