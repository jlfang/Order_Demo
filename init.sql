CREATE TABLE IF NOT EXISTS t_order
(
    id           BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT 'primary key',
    ori_lat      DECIMAL(10, 6) NOT NULL COMMENT 'origin latitude',
    ori_lng      DECIMAL(10, 6) NOT NULL COMMENT 'origin longitude',
    dest_lat     DECIMAL(10, 6) NOT NULL COMMENT 'destination latitude',
    dest_lng     DECIMAL(10, 6) NOT NULL COMMENT 'destination longitude',
    distance     BIGINT NOT NULL COMMENT 'distance',
    status       VARCHAR(50) NOT NULL COMMENT 'order status (UNASSIGNED,ASSIGNED)',
    creator      VARCHAR(50) NOT NULL DEFAULT 'SYSTEM' COMMENT 'creator',
    create_time  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create time',
    modifier     VARCHAR(50) NOT NULL DEFAULT 'SYSTEM' COMMENT 'modifier',
    modify_time  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'modify time',
    version      INT NOT NULL DEFAULT 1 COMMENT 'version number',
    deleted      TINYINT(1) NOT NULL DEFAULT 0 COMMENT 'order deleted (0-no 1-yes)'
    ) COMMENT 'order table';
