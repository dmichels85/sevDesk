alter table CUSTOMERS
    add owner_id BIGINT NOT NULL;

alter table CUSTOMERS
    add FOREIGN KEY (owner_id) REFERENCES USERS(ID);