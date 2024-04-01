
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

    create table app_user_role_seq (
        next_val bigint
    );

    insert into app_user_role_seq values ( 1 );

    create table card (
        difficulty float not null,
        display_order integer not null,
        elapsed_days integer not null,
        is_suspended boolean not null,
        lapses integer not null,
        reps integer not null,
        scheduled_days integer not null,
        stability float not null,
        state tinyint not null check (state between 0 and 3),
        actual_due timestamp not null,
        due timestamp not null,
        last_review timestamp not null,
        id blob not null,
        back clob not null,
        deck_id blob,
        explanation clob not null,
        front clob not null,
        primary key (id)
    );

    create table card_hint (
        display_order integer not null,
        id blob not null,
        card_id blob,
        text clob not null,
        primary key (id)
    );

    create table card_tag (
        id blob not null,
        name varchar(255) not null,
        primary key (id)
    );

    create table card_tag_mapping (
        id blob not null,
        card_id blob,
        card_tag_id blob,
        primary key (id)
    );

    create table deck (
        can_share_expired boolean not null,
        learning_count integer not null,
        new_count integer not null,
        review_count integer not null,
        total_count integer not null,
        last_review_time timestamp,
        share_expiration_time timestamp not null,
        id blob not null,
        deck_settings_id blob,
        description varchar(255) not null,
        name varchar(255) not null,
        share_code varchar(255) not null,
        primary key (id)
    );

    create table deck_image (
        id blob not null,
        deck_id blob,
        image_data blob,
        primary key (id)
    );

    create table deck_reviews (
        rating tinyint not null check (rating between 0 and 4),
        id blob not null,
        comment clob not null,
        deck_id blob,
        primary key (id)
    );

    create table deck_settings (
        desired_retention float not null,
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
        id blob not null,
        learning_steps varchar(255) not null,
        relearning_steps varchar(255) not null,
        primary key (id)
    );

    create table deck_tag (
        id blob not null,
        name varchar(255) not null,
        primary key (id)
    );

    create table deck_tag_mapping (
        id blob not null,
        deck_id blob,
        deck_tag_id blob,
        primary key (id)
    );

    create table review_log (
        elapsed_days integer not null,
        rating tinyint not null check (rating between 0 and 3),
        review_duration_in_ms integer not null,
        scheduled_days integer not null,
        state tinyint not null check (state between 0 and 3),
        last_review timestamp not null,
        review timestamp not null,
        id blob not null,
        card_id blob,
        primary key (id)
    );

    create table study_option (
        mode tinyint not null check (mode between 0 and 1),
        sort_option tinyint not null check (sort_option between 0 and 8),
        id blob not null,
        primary key (id)
    );

    create table study_option_deck (
        id blob not null,
        deck_id blob,
        study_option_id blob,
        primary key (id)
    );

    create table study_option_state (
        state tinyint not null check (state between 0 and 3),
        id blob not null,
        study_option_id blob,
        primary key (id)
    );

    create table study_option_tag (
        id blob not null,
        card_tag_id blob,
        study_option_id blob,
        primary key (id)
    );

    create table sync_entity (
        sort_order integer not null,
        created_at timestamp not null,
        deleted_at timestamp,
        last_modified timestamp not null,
        synced_at timestamp not null,
        version bigint not null,
        id blob not null,
        entity_type varchar(31) not null,
        modified_by_device_id blob not null,
        user_id varchar(255),
        primary key (id)
    );

    create index idx_created_at 
       on sync_entity (created_at);

    create index idx_user_id 
       on sync_entity (user_id);

    create index idx_sort_order 
       on sync_entity (sort_order);
