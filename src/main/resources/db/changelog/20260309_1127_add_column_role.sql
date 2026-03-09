ALTER TABLE final_user.user ADD COLUMN role INTEGER NULL;

UPDATE final_user.user SET role = 1;

ALTER TABLE final_user.user ALTER COLUMN role SET NOT NULL;

ALTER TABLE final_user.user ADD CONSTRAINT user_role_check CHECK (role BETWEEN 1 AND 2);
