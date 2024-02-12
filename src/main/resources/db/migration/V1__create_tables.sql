create table users
(
    id serial primary key,
    address varchar(1024),
    birthday date,
    email varchar(255),
    first_name varchar(255),
    last_name varchar(255),
    login varchar(255) constraint uk_ow0gan20590jrb00upg3va2fn unique,
    password varchar(255),
    perm varchar(255),
    phone_number varchar(255),
    sex varchar(255) constraint users_sex_check
        check ((sex)::text = ANY ((ARRAY ['MAN'::character varying, 'WOMAN'::character varying])::text[]))
);

create table doctors
(
    experience double precision,
    id serial primary key,
    doctor_specialty varchar(255)
        constraint doctors_doctor_specialty_check check ((doctor_specialty)::text = ANY
                                                         ((ARRAY ['THERAPIST'::character varying, 'SURGEON'::character varying, 'CARDIOLOGIST'::character varying,
                                                             'OPHTHALMOLOGIST'::character varying, 'ENT_SPECIALIST'::character varying, 'ENDOCRINOLOGIST'::character varying,
                                                             'ORTHOPEDIST'::character varying, 'NEUROLOGIST'::character varying, 'GYNECOLOGIST'::character varying,
                                                             'UROLOGIST'::character varying, 'PEDIATRICIAN'::character varying, 'ONCOLOGIST'::character varying])::text[])),
    full_name varchar(255)
);

create table orders
(
    id serial primary key,
    appointment_time timestamp(6),
    doctor_id integer not null constraint fkricbgmp45ekbdqqgdqktabl14 references doctors,
    user_id integer not null constraint fk32ql8ubntj5uh44ph9659tiih references users
);
