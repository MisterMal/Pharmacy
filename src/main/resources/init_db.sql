
CREATE VIEW public.glassfish_auth_view AS SELECT account.login, account.password, al.access_level_role FROM (public.access_level al JOIN public.account ON ((al.account_id = account.id))) WHERE ((account.confirmed = true) AND (account.active = true) AND (al.active = true));

REVOKE USAGE ON SCHEMA public FROM PUBLIC;
GRANT ALL ON SCHEMA public TO PUBLIC;

GRANT SELECT,INSERT,UPDATE ON TABLE public.access_level TO ssbd01mok;
GRANT SELECT ON TABLE public.access_level TO ssbd01moa;


GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.account TO ssbd01mok;
GRANT SELECT ON TABLE public.account TO ssbd01moa;


GRANT SELECT,INSERT,UPDATE ON TABLE public.admin_data TO ssbd01mok;
GRANT SELECT ON TABLE public.admin_data TO ssbd01moa;


GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.category TO ssbd01moa;


GRANT SELECT,INSERT,UPDATE ON TABLE public.chemist_data TO ssbd01mok;
GRANT SELECT ON TABLE public.chemist_data TO ssbd01moa;


GRANT SELECT ON TABLE public.glassfish_auth_view TO ssbd01glassfish;


GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.medication TO ssbd01moa;


GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.order_medication TO ssbd01moa;


GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.patient_data TO ssbd01mok;
GRANT SELECT ON TABLE public.patient_data TO ssbd01moa;


GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.patient_order TO ssbd01moa;


GRANT SELECT,INSERT,DELETE ON TABLE public.prescription TO ssbd01moa;


GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.shipment TO ssbd01moa;


GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.shipment_medication TO ssbd01moa;

GRANT SELECT, UPDATE ON TABLE public.account_id_seq TO ssbd01mok;

GRANT SELECT, UPDATE ON TABLE public.access_level_id_seq TO ssbd01mok;

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.token TO ssbd01mok;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.token_id_seq TO ssbd01mok;

GRANT SELECT,UPDATE ON TABLE public.category_id_seq TO ssbd01moa;
GRANT SELECT,UPDATE ON TABLE public.medication_id_seq TO ssbd01moa;

GRANT SELECT, UPDATE ON TABLE public.order_medication_id_seq TO ssbd01moa;
GRANT SELECT, UPDATE ON TABLE public.shipment_id_seq TO ssbd01moa;
GRANT SELECT, UPDATE ON TABLE public.shipment_medication_id_seq TO ssbd01moa;
GRANT SELECT, UPDATE ON TABLE public.patient_order_id_seq TO ssbd01moa;
GRANT SELECT, UPDATE ON TABLE public.prescription_id_seq TO ssbd01moa;

insert into account (active, confirmed, created_by, creation_date, email, language, last_negative_login, last_positive_login, logical_address, login,incorrect_login_attempts, modification_date, modified_by, password, version) values (true, true, null, now(), 'admin@o2.pl', 'en', null, null, null, 'admin123', 0, null, null,  'b03ddf3ca2e714a6548e7495e2a03f5e824eaac9837cd7f159c67b90fb4b7342', 0);
insert into account (active, confirmed, created_by, creation_date, email, language, last_negative_login, last_positive_login, logical_address, login,incorrect_login_attempts, modification_date, modified_by, password, version) values (true, true, null, now(), 'chemist@o2.pl', 'en', null, null, null, 'chemist123', 0, null, null,  '52d7a0431ddd469b9e0929a09ef67fefe99e3511893edb0c2fa0b09892df1e52', 0);
insert into account (active, confirmed, created_by, creation_date, email, language, last_negative_login, last_positive_login, logical_address, login, incorrect_login_attempts, modification_date, modified_by, password, version) values (true, true, null, now(), 'patient@o2.pl', 'en', null, null, null, 'patient123', 0, null, null,'b03ddf3ca2e714a6548e7495e2a03f5e824eaac9837cd7f159c67b90fb4b7342', 0);

insert into access_level (account_id, active, created_by, creation_date, modification_date, modified_by, version, access_level_role) values (1, true, null, now(), null, null, 0, 'ADMIN');
insert into access_level (account_id, active, created_by, creation_date, modification_date, modified_by, version, access_level_role) values (2, true, null, now(), null, null, 0, 'CHEMIST');
insert into access_level (account_id, active, created_by, creation_date, modification_date, modified_by, version, access_level_role) values (1, true, null, now(), null, null, 0, 'CHEMIST');
insert into access_level (account_id, active, created_by, creation_date, modification_date, modified_by, version, access_level_role) values (3, true, null, now(), null, null, 0, 'PATIENT');

insert into admin_data (id, work_phone_number) values (1, '123456789');
insert into chemist_data (id, license_number) values (2, '456456');
insert into chemist_data (id, license_number) values (3, '123123');
insert into patient_data (id, pesel, nip, phone_number, first_name, last_name) values (4, '22344678801', '2234557890', '721545784', 'Jan', 'Kowalski');

insert into category (name, created_by, creation_date, modification_date, modified_by, version, isOnPrescription) values ('antibiotics', null, now(), null, null, 0, true);
insert into category (name, created_by, creation_date, modification_date, modified_by, version, isOnPrescription) values ('painkillers', null, now(), null, null, 0, false);
insert into category (name, created_by, creation_date, modification_date, modified_by, version, isOnPrescription) values ('antidepressant', null, now(), null, null, 0, true);
insert into category (name, created_by, creation_date, modification_date, modified_by, version, isOnPrescription) values ('vitamins', null, now(), null, null, 0, false);

insert into medication (medication_name, current_price, category_id, created_by, creation_date, modification_date, modified_by, version, stock) values ('Penicylina', 10, 1, null, now(), null, null, 0, 10);
insert into medication (medication_name, current_price, category_id, created_by, creation_date, modification_date, modified_by, version, stock) values ('Ibuprom', 10, 2, null, now(), null, null, 0, 10);
insert into medication (medication_name, current_price, category_id, created_by, creation_date, modification_date, modified_by, version, stock) values ('Apap', 10, 2, null, now(), null, null, 0, 10);
insert into medication (medication_name, current_price, category_id, created_by, creation_date, modification_date, modified_by, version, stock) values ('Prozac', 10, 3, null, now(), null, null, 0, 10);
insert into medication (medication_name, current_price, category_id, created_by, creation_date, modification_date, modified_by, version, stock) values ('Zoloft', 20, 3, null, now(), null, null, 0, 10);
insert into medication (medication_name, current_price, category_id, created_by, creation_date, modification_date, modified_by, version, stock) values ('Marsjanki', 25, 4, null, now(), null, null, 0, 10);
insert into medication (medication_name, current_price, category_id, created_by, creation_date, modification_date, modified_by, version, stock) values ('Witamina D3', 25, 4, null, now(), null, null, 0, 15);
insert into medication (medication_name, current_price, category_id, created_by, creation_date, modification_date, modified_by, version, stock) values ('Amoksycylina', 25, 1, null, now(), null, null, 0, 15);
insert into medication (medication_name, current_price, category_id, created_by, creation_date, modification_date, modified_by, version, stock) values ('Xanax', 25, 3, null, now(), null, null, 0, 10);

insert into prescription (creation_date, modification_date, version, prescription_number, created_by, modified_by, patient_data_id) values (now(), null, 0, '123456789', null, null, 4);
insert into prescription (creation_date, modification_date, version, prescription_number, created_by, modified_by, patient_data_id) values (now(), null, 0, '123456788', null, null, 4);
insert into prescription (creation_date, modification_date, version, prescription_number, created_by, modified_by, patient_data_id) values (now(), null, 0, '123456787', null, null, 4);
insert into prescription (creation_date, modification_date, version, prescription_number, created_by, modified_by, patient_data_id) values (now(), null, 0, '123456786', null, null, 4);
insert into prescription (creation_date, modification_date, version, prescription_number, created_by, modified_by, patient_data_id) values (now(), null, 0, '123456733', null, null, 4);
insert into prescription (creation_date, modification_date, version, prescription_number, created_by, modified_by, patient_data_id) values (now(), null, 0, '123456734', null, null, 4);

insert into patient_order (patient_data_id, order_date, order_state, created_by, creation_date, modification_date, modified_by, version, chemist_data_id, prescription_id) values (4, now(), 'IN_QUEUE', null, now(), null, null, 0, null, 1);
insert into patient_order (patient_data_id, order_date, order_state, created_by, creation_date, modification_date, modified_by, version, chemist_data_id) values (4, now(), 'IN_QUEUE', null, now(), null, null, 0, null);
insert into patient_order (patient_data_id, order_date, order_state, created_by, creation_date, modification_date, modified_by, version, chemist_data_id, prescription_id) values (4, now(), 'IN_QUEUE', null, now(), null, null, 0, null, 2);
insert into patient_order (patient_data_id, order_date, order_state, created_by, creation_date, modification_date, modified_by, version, chemist_data_id, prescription_id) values (4, now(), 'WAITING_FOR_CHEMIST_APPROVAL', null, now(), null, null, 0, null, 3);
insert into patient_order (patient_data_id, order_date, order_state, created_by, creation_date, modification_date, modified_by, version, chemist_data_id, prescription_id) values (4, now(), 'WAITING_FOR_CHEMIST_APPROVAL', null, now(), null, null, 0, null, 4);
insert into patient_order (patient_data_id, order_date, order_state, created_by, creation_date, modification_date, modified_by, version, chemist_data_id, prescription_id) values (4, now(), 'TO_BE_APPROVED_BY_PATIENT', null, now(), null, null, 0, null, 5);
insert into patient_order (patient_data_id, order_date, order_state, created_by, creation_date, modification_date, modified_by, version, chemist_data_id, prescription_id) values (4, now(), 'TO_BE_APPROVED_BY_PATIENT', null, now(), null, null, 0, null, 6);


insert into order_medication (order_id, medication_id, quantity, created_by, creation_date, modification_date, modified_by, version, purchase_price) values (1, 1, 2, null, now(), null, null, 0, 10);
insert into order_medication (order_id, medication_id, quantity, created_by, creation_date, modification_date, modified_by, version, purchase_price) values (1, 3, 4, null, now(), null, null, 0, 10);
insert into order_medication (order_id, medication_id, quantity, created_by, creation_date, modification_date, modified_by, version, purchase_price) values (1, 4, 2, null, now(), null, null, 0, 10);

insert into order_medication (order_id, medication_id, quantity, created_by, creation_date, modification_date, modified_by, version, purchase_price) values (2, 2, 5, null, now(), null, null, 0, 10);
insert into order_medication (order_id, medication_id, quantity, created_by, creation_date, modification_date, modified_by, version, purchase_price) values (2, 6, 1, null, now(), null, null, 0, 25);

insert into order_medication (order_id, medication_id, quantity, created_by, creation_date, modification_date, modified_by, version, purchase_price) values (3, 6, 5, null, now(), null, null, 0, 25);
insert into order_medication (order_id, medication_id, quantity, created_by, creation_date, modification_date, modified_by, version, purchase_price) values (3, 5, 4, null, now(), null, null, 0, 20);

insert into order_medication (order_id, medication_id, quantity, created_by, creation_date, modification_date, modified_by, version, purchase_price) values (4, 4, 4, null, now(), null, null, 0, 10);

insert into order_medication (order_id, medication_id, quantity, created_by, creation_date, modification_date, modified_by, version, purchase_price) values (5, 4, 4, null, now(), null, null, 0, 10);

insert into order_medication (order_id, medication_id, quantity, created_by, creation_date, modification_date, modified_by, version, purchase_price) values (6, 4, 4, null, now(), null, null, 0, 10);

insert into order_medication (order_id, medication_id, quantity, created_by, creation_date, modification_date, modified_by, version, purchase_price) values (7, 4, 4, null, now(), null, null, 0, 10);