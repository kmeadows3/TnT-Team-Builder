-- ********************************************************************************
-- This script creates the database users and grants them the necessary permissions
-- ********************************************************************************

CREATE USER tnt_owner
WITH PASSWORD 'thisisnotatest';

GRANT ALL
ON ALL TABLES IN SCHEMA public
TO tnt_owner;

GRANT ALL
ON ALL SEQUENCES IN SCHEMA public
TO tnt_owner;

CREATE USER tnt_appuser
WITH PASSWORD 'thisisnotatest';

GRANT SELECT, INSERT, UPDATE, DELETE
ON ALL TABLES IN SCHEMA public
TO tnt_appuser;

GRANT USAGE, SELECT
ON ALL SEQUENCES IN SCHEMA public
TO tnt_appuser;
