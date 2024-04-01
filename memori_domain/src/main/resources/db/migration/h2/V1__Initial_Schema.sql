
    create sequence app_user_role_seq start with 1 increment by 50;

    create table app_user (
        daily_reset_time integer not null,
        is_email_verified boolean not null,
        tier tinyint not null check (tier between 0 and 1),
        storage_size_in_byte bigint not null,
        email varchar(255) not null unique,
        id varchar(255) not null,
        timezone varchar(255) not null,
        username varchar(255) not null unique,
        primary key (id)
    );

    create table app_user_role (
        role tinyint not null check (role between 0 and 0),
        id bigint not null,
        user_id varchar(255),
        primary key (id)
    );

    create table card (
        difficulty float(53) not null,
        display_order integer not null,
        elapsed_days integer not null,
        is_suspended boolean not null,
        lapses integer not null,
        reps integer not null,
        scheduled_days integer not null,
        stability float(53) not null,
        state tinyint not null check (state between 0 and 3),
        actual_due timestamp(6) with time zone not null,
        due timestamp(6) with time zone not null,
        last_review timestamp(6) with time zone not null,
        deck_id uuid,
        id uuid not null,
        back json not null,
        explanation json not null,
        front json not null,
        primary key (id)
    );

    create table card_hint (
        display_order integer not null,
        card_id uuid,
        id uuid not null,
        text json not null,
        primary key (id)
    );

    create table card_tag (
        id uuid not null,
        name varchar(255) not null,
        primary key (id)
    );

    create table card_tag_mapping (
        card_id uuid,
        card_tag_id uuid,
        id uuid not null,
        primary key (id)
    );

    create table deck (
        can_share_expired boolean not null,
        learning_count integer not null,
        new_count integer not null,
        review_count integer not null,
        total_count integer not null,
        last_review_time timestamp(6) with time zone,
        share_expiration_time timestamp(6) with time zone not null,
        deck_settings_id uuid,
        id uuid not null,
        description varchar(255) not null,
        name varchar(255) not null,
        share_code varchar(255) not null,
        primary key (id)
    );

    create table deck_image (
        deck_id uuid,
        id uuid not null,
        image_data blob,
        primary key (id)
    );

    create table deck_reviews (
        rating tinyint not null check (rating between 0 and 4),
        deck_id uuid,
        id uuid not null,
        comment json not null,
        primary key (id)
    );

    create table deck_settings (
        desired_retention float(53) not null,
        interday_priority tinyint not null check (interday_priority between 0 and 3),
        is_default boolean not null,
        max_new_cards_per_day integer not null,
        max_review_per_day integer not null,
        maximum_answer_seconds integer not null,
        new_priority tinyint not null check (new_priority between 0 and 3),
        review_priority tinyint not null check (review_priority between 0 and 3),
        skip_learning_card boolean not null,
        skip_new_card boolean not null,
        skip_review_card boolean not null,
        id uuid not null,
        learning_steps varchar(255) not null,
        relearning_steps varchar(255) not null,
        primary key (id)
    );

    create table deck_tag (
        id uuid not null,
        name varchar(255) not null,
        primary key (id)
    );

    create table deck_tag_mapping (
        deck_id uuid,
        deck_tag_id uuid,
        id uuid not null,
        primary key (id)
    );

    create table review_log (
        elapsed_days integer not null,
        rating tinyint not null check (rating between 0 and 3),
        review_duration_in_ms integer not null,
        scheduled_days integer not null,
        state tinyint not null check (state between 0 and 3),
        last_review timestamp(6) with time zone not null,
        review timestamp(6) with time zone not null,
        card_id uuid,
        id uuid not null,
        primary key (id)
    );

    create table study_option (
        mode tinyint not null check (mode between 0 and 1),
        sort_option tinyint not null check (sort_option between 0 and 8),
        id uuid not null,
        primary key (id)
    );

    create table study_option_deck (
        deck_id uuid,
        id uuid not null,
        study_option_id uuid,
        primary key (id)
    );

    create table study_option_state (
        state tinyint not null check (state between 0 and 3),
        id uuid not null,
        study_option_id uuid,
        primary key (id)
    );

    create table study_option_tag (
        card_tag_id uuid,
        id uuid not null,
        study_option_id uuid,
        primary key (id)
    );

    create table sync_entity (
        sort_order integer not null,
        created_at timestamp(6) with time zone not null,
        deleted_at timestamp(6) with time zone,
        last_modified timestamp(6) with time zone not null,
        synced_at timestamp(6) with time zone not null,
        version bigint not null,
        id uuid not null,
        modified_by_device_id uuid not null,
        entity_type varchar(31) not null,
        user_id varchar(255),
        primary key (id)
    );

    create index idx_created_at 
       on sync_entity (created_at);

    create index idx_user_id 
       on sync_entity (user_id);

    create index idx_sort_order 
       on sync_entity (sort_order);

    alter table if exists app_user_role 
       add constraint FKfnlxi1bmv5ao8u3nf30ymq7xa 
       foreign key (user_id) 
       references app_user;

    alter table if exists card 
       add constraint FK6k0or7dj9m5qhnshnk9fpg8r1 
       foreign key (deck_id) 
       references deck;

    alter table if exists card 
       add constraint FKe75sbma0d6bihcdw5cum44f8s 
       foreign key (id) 
       references sync_entity;

    alter table if exists card_hint 
       add constraint FK60ux1qla37guxbv1q3hv9c43h 
       foreign key (card_id) 
       references card;

    alter table if exists card_hint 
       add constraint FK3veqimfor5b012siud282bte5 
       foreign key (id) 
       references sync_entity;

    alter table if exists card_tag 
       add constraint FKh8awr2ofvhx93im0mqdbfbpq2 
       foreign key (id) 
       references sync_entity;

    alter table if exists card_tag_mapping 
       add constraint FKmraarrd44g8pgn46apvlb5jn5 
       foreign key (card_id) 
       references card;

    alter table if exists card_tag_mapping 
       add constraint FK5pmhc8wdunfb33y9tgegtaser 
       foreign key (card_tag_id) 
       references card_tag;

    alter table if exists card_tag_mapping 
       add constraint FKjnu5p95i5t5ulbrh3gblexvsl 
       foreign key (id) 
       references sync_entity;

    alter table if exists deck 
       add constraint FKgluc85eeh928ch4j1pg0ymx8n 
       foreign key (deck_settings_id) 
       references deck_settings;

    alter table if exists deck 
       add constraint FKr90oq5deg2snvgalkg4r7ujgk 
       foreign key (id) 
       references sync_entity;

    alter table if exists deck_image 
       add constraint FK8yn1jeh14coooi9ueyhdh9gow 
       foreign key (deck_id) 
       references deck;

    alter table if exists deck_image 
       add constraint FK4trvbkiyxwabvnsukln6uytkf 
       foreign key (id) 
       references sync_entity;

    alter table if exists deck_reviews 
       add constraint FKsgrw718n0lc8nks55e2lsy2n5 
       foreign key (deck_id) 
       references deck;

    alter table if exists deck_reviews 
       add constraint FKo9hs4ax9cclme7674b3qvjvcx 
       foreign key (id) 
       references sync_entity;

    alter table if exists deck_settings 
       add constraint FK2icqe7sqmiim1430ghccqub2k 
       foreign key (id) 
       references sync_entity;

    alter table if exists deck_tag 
       add constraint FKfu5fi7fjub846pxnnscn4h7lg 
       foreign key (id) 
       references sync_entity;

    alter table if exists deck_tag_mapping 
       add constraint FKsyouqp1yg80wxay1mexlgxjx7 
       foreign key (deck_id) 
       references deck;

    alter table if exists deck_tag_mapping 
       add constraint FKsurmq65cdccki37od598h7fso 
       foreign key (deck_tag_id) 
       references deck_tag;

    alter table if exists deck_tag_mapping 
       add constraint FKa6w65c7xwk8oap2ulil5k4pu2 
       foreign key (id) 
       references sync_entity;

    alter table if exists review_log 
       add constraint FKfbts3oxxa369row7v03tg2egg 
       foreign key (card_id) 
       references card;

    alter table if exists review_log 
       add constraint FKeagnpf2u3obtjgl5j6fpyn64d 
       foreign key (id) 
       references sync_entity;

    alter table if exists study_option 
       add constraint FKmidb42n7fy42dv758cwwtrc87 
       foreign key (id) 
       references sync_entity;

    alter table if exists study_option_deck 
       add constraint FK761fije2bd7ymlqvbwug234c7 
       foreign key (deck_id) 
       references deck;

    alter table if exists study_option_deck 
       add constraint FKh0w7siwjnxsrg32n6fgvk1848 
       foreign key (study_option_id) 
       references study_option;

    alter table if exists study_option_deck 
       add constraint FKax0ema4a4immwy5nb6h7p8mps 
       foreign key (id) 
       references sync_entity;

    alter table if exists study_option_state 
       add constraint FKlsioitqnx12vdud4mxpvok7hk 
       foreign key (study_option_id) 
       references study_option;

    alter table if exists study_option_state 
       add constraint FKg7etr2m0ntmydn3avu7ltso0 
       foreign key (id) 
       references sync_entity;

    alter table if exists study_option_tag 
       add constraint FKs92avv9dlv1vuey06nggojfge 
       foreign key (card_tag_id) 
       references card_tag;

    alter table if exists study_option_tag 
       add constraint FKs2ym5qvwewye0lylrtyockblo 
       foreign key (study_option_id) 
       references study_option;

    alter table if exists study_option_tag 
       add constraint FKnsiidoeaagnoe2c84ee1kddjo 
       foreign key (id) 
       references sync_entity;

    alter table if exists sync_entity 
       add constraint FK63ft98a5vlytj7fuqqifbwsk 
       foreign key (user_id) 
       references app_user;
