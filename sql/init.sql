-- user/
DROP TABLE IF EXISTS users CASCADE;
CREATE TABLE users (
    id SERIAL PRIMARY KEY NOT NULL,
    username CHAR(128) UNIQUE NOT NULL,
    email CHAR(256) UNIQUE NOT NULL,
    role CHAR(512) NOT NULL, -- For Website administrating.
    rating INTEGER NOT NULL,
    password CHAR(1024) NOT NULL,
    salt CHAR(128) NOT NULL, -- not used yet
    disable BOOLEAN NOT NULL
);
INSERT INTO users (username, email, role, password, salt, rating, disable) VALUES (
    'admin', 'no-reply@example.org', 'ADMIN', '123456', '0', 0, FALSE), ( -- TODO: replace default user password here
    '(deleted)', 'deleted@example.org', 'SYSTEM', 'x', 'x', 0, TRUE); -- used as placeholder

-- proj/proj
DROP TABLE IF EXISTS projects CASCADE;
CREATE TABLE projects (
    id SERIAL PRIMARY KEY NOT NULL,
    owner INTEGER NOT NULL REFERENCES users(id) ON UPDATE CASCADE,
    type INTEGER NOT NULL,
    name CHAR(256) UNIQUE NOT NULL,
    tags CHAR(512),
    ori_lang CHAR(16) NOT NULL,
    tar_lang CHAR(16) NOT NULL
);

-- proj/group
DROP TABLE IF EXISTS user_group_info;
CREATE TABLE user_group_info (
    id SERIAL PRIMARY KEY NOT NULL,
    user_id INTEGER REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL,
    proj_id INTEGER REFERENCES projects(id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL,
    role CHAR(256) -- For Project administrating.
);

-- proj/file
DROP TABLE IF EXISTS categories CASCADE;
CREATE TABLE categories (
    id SERIAL PRIMARY KEY NOT NULL,
    proj_id INTEGER REFERENCES projects(id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL,
    name CHAR(512) NOT NULL,
    "desc" CHAR(4096) NOT NULL
);

CREATE INDEX proj_idx ON categories(proj_id);

DROP TABLE IF EXISTS files CASCADE;
CREATE TABLE files(
    id SERIAL PRIMARY KEY NOT NULL,
    category_id INTEGER REFERENCES categories(id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL,
    name CHAR(128) NOT NULL,
    converter CHAR(128) NOT NULL,
    comment CHAR(4096) NOT NULL
);

DROP TABLE IF EXISTS texts;
CREATE TABLE texts (
    id SERIAL PRIMARY KEY NOT NULL,
    file_id INTEGER REFERENCES files(id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL,
    ori_text TEXT NOT NULL,
    trans TEXT -- translation
);

-- DROP TABLE IF EXISTS translations;
-- CREATE TABLE translations (
--     id SERIAL PRIMARY KEY NOT NULL,
--     ori_id INTEGER FOREIGN KEY texts(id) ON DELETE CASCADE ON UPDATE CASCADE,
--     trans TEXT,
-- );

DROP TABLE IF EXISTS terms;
CREATE TABLE terms (
    id SERIAL PRIMARY KEY NOT NULL,
    proj_id INTEGER REFERENCES projects(id) ON DELETE CASCADE NOT NULL,
    ori_word CHAR(1024) NOT NULL,
    tar_word CHAR(1024) NOT NULL,
    comment CHAR(2048),
    commiter INTEGER REFERENCES users(id) ON UPDATE CASCADE NOT NULL -- if a user wants to delete himself/herself, redirect this to a placeholder user, and this entry should NOT be deleted!
);