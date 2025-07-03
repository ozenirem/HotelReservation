-- to be able to use daterange in the reservation table
create extension if not exists btree_gist;

-- to use the daterange type in PostgreSQL
ALTER TABLE reservation
    ADD EXCLUDE USING gist (
        room_id WITH =,
        daterange(check_in_date, check_out_date, '[]') WITH &&
        );