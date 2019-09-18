create table if not exists `oauth_client_token` (
  `token_id` VARCHAR(256),
  `token` BLOB,
  `authentication_id` VARCHAR(256) PRIMARY KEY,
  `user_name` VARCHAR(256),
  `client_id` VARCHAR(256)
);

create table if not exists `oauth_access_token` (
  `token_id` VARCHAR(256),
  `token` BLOB,
  `authentication_id` VARCHAR(256) PRIMARY KEY,
  `user_name` VARCHAR(256),
  `client_id` VARCHAR(256),
  `authentication` BLOB,
  `refresh_token` VARCHAR(256)
);

create table if not exists `oauth_refresh_token` (
  `token_id` VARCHAR(256),
  `token` BLOB,
  `authentication` BLOB
);

create table if not exists `oauth_code` (
  `code` VARCHAR(256),
  `authentication` BLOB
);

create table if not exists `oauth_approvals` (
	`userId` VARCHAR(256),
	`clientId` VARCHAR(256),
	`scope` VARCHAR(256),
	`status` VARCHAR(10),
	`expiresAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	`lastModifiedAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);