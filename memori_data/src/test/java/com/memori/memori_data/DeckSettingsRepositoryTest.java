package com.memori.memori_data;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

import com.memori.memori_data.configurations.SimpleJpaConfiguration;
import com.memori.memori_data.configurations.SyncJpaConfiguration;
import com.memori.memori_data.mappers.EntityMapper;
import com.memori.memori_data.models.PersistStatus;
import com.memori.memori_data.repositories.simple.UserRepository;
import com.memori.memori_data.repositories.sync.DeckSettingsRepository;
import com.memori.memori_domain.DeckSettings;
import com.memori.memori_domain.User;
import com.memori.memori_domain.DeckSettings.Priority;

import jakarta.persistence.EntityNotFoundException;

@DataJpaTest
@ComponentScan(basePackages = "com.memori.memori_data.mappers")
@EntityScan("com.memori.memori_domain")
@ContextConfiguration(classes = { DeckSettingsRepositoryTest.TestConfig.class, SimpleJpaConfiguration.class,
        SyncJpaConfiguration.class })
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DeckSettingsRepositoryTest {

    @Autowired
    private DeckSettingsRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityMapper<DeckSettings> mapper;

    private User dummyUser;

    @BeforeAll
    void setUpAll(@Autowired Flyway flyway) {
        flyway.migrate();
        UUID id = Objects.requireNonNull(UUID.randomUUID(), "UUID cannot be null");
        dummyUser = User.builder()
                .id(id.toString())
                .email("test@example.com")
                .username("teset user")
                .build();
        assertNotNull(dummyUser, "Dumm user cannot be nul");
        userRepository.save(dummyUser);
    }

    @Configuration
    static class TestConfig {
        @Bean
        Flyway flyway(DataSource dataSource) {
            Flyway flyway = Flyway.configure()
                    .dataSource(dataSource)
                    .locations("classpath:/db/migration/h2") // Adjust the path as necessary
                    .load();
            flyway.migrate();
            return flyway;
        }
    }

    private <T> void assertConflict(PersistStatus<T> persistStatus) {
        assertNotNull(persistStatus, "Persist status should not be null");
        assertFalse(persistStatus.getIsSuccessful());
        assertTrue(persistStatus.getHasSyncConflict());
    }

    private <T> void assertNoConflict(PersistStatus<T> persistStatus) {
        assertNotNull(persistStatus, "Persist status should not be null");
        assertTrue(persistStatus.getIsSuccessful());
        assertFalse(persistStatus.getHasSyncConflict());
    }

    private <T> void assertEqual(T a, T b) {
        // Cannot use assertEqual since hashcode is not implemented
        assertTrue(a.equals(b));
    }

    private <T> void assertNotEqual(T a, T b) {
        // Cannot use assertEqual since hashcode is not implemented
        assertFalse(a.equals(b));
    }

    private <T> void assertPropsNotEqual(T obj1, T obj2, Function<T, Object> propertyExtractor) {
        Object prop1 = propertyExtractor.apply(obj1);
        Object prop2 = propertyExtractor.apply(obj2);
        assertNotEqual(prop1, prop2);
    }

    private DeckSettings createDefaultDeckSettings() {
        Instant oNow = Instant.now();

        DeckSettings ds = DeckSettings.builder()
                .id(UUID.randomUUID())
                .createdAt(oNow)
                .lastModified(oNow)
                .deletedAt(oNow)
                .syncedAt(oNow)
                .version(1L)
                .modifiedByDeviceId(UUID.randomUUID())
                .isDefault(true)
                .learningSteps("1m 10m 1h")
                .relearningSteps("1m 10m")
                .maxNewCardsPerDay(10)
                .maxReviewPerDay(5)
                .maximumAnswerSeconds(60)
                .desiredRetention(0.95)
                .newPriority(Priority.FIRST)
                .interdayPriority(Priority.SECOND)
                .reviewPriority(Priority.THIRD)
                .userId(UUID.randomUUID().toString())
                .build();
        ds.setSortOrder(ds.getSortOrderConstant());

        return ds;
    }

    private DeckSettings createNullDeckSettings() {
        DeckSettings ds = DeckSettings.builder()
                .id(null)
                .createdAt(null)
                .lastModified(null)
                .deletedAt(null)
                .syncedAt(null)
                .version(null)
                .modifiedByDeviceId(null)
                .isDefault(null)
                .learningSteps(null)
                .relearningSteps(null)
                .maxNewCardsPerDay(null)
                .maxReviewPerDay(null)
                .maximumAnswerSeconds(null)
                .desiredRetention(null)
                .newPriority(null)
                .interdayPriority(null)
                .reviewPriority(null)
                .sortOrder(null)
                .userId(null)
                .build();
        return ds;
    }

    @Test
    void mapStructSetup_ShouldCorrect() {
        // The test below validates the entity mapper
        // Ensure that the columns are ignored as configured
        DeckSettings ds = createDefaultDeckSettings();
        DeckSettings ds2 = new DeckSettings();

        mapper.mapEntity(ds, ds2);
        assertTrue(ds.equals(ds2));

        ds = createDefaultDeckSettings();
        ds2 = createNullDeckSettings();
        mapper.updateEntity(ds, ds2);
        assertTrue(ds2.getId() == null);
        assertTrue(ds2.getCreatedAt() == null);
        assertTrue(ds2.getVersion() == null);
        assertTrue(ds2.getDeletedAt() == null);
        assertTrue(ds2.getSyncedAt() == null);
        assertTrue(ds2.getSortOrder() == null);

        ds = createDefaultDeckSettings();
        ds2 = createNullDeckSettings();
        mapper.updateEntity(ds, ds2);
        assertTrue(ds2.getId() == null);
        assertTrue(ds2.getCreatedAt() == null);
        assertTrue(ds2.getVersion() == null);
        assertTrue(ds2.getSyncedAt() == null);
        assertTrue(ds2.getSortOrder() == null);
    }

    @Test
    void entity_ShouldHaveNoConflict_ForValidEntity() throws InterruptedException {
        Instant now = Instant.now();
        UUID id = UUID.randomUUID();
        assertNotNull(id, "Id should not be null");
        UUID deviceId = UUID.randomUUID();
        DeckSettings deckSettings = DeckSettings
                .builder()
                .id(id)
                .createdAt(now)
                .lastModified(now)
                .modifiedByDeviceId(deviceId)
                .userId(dummyUser.getId())
                .user(dummyUser)
                .build();

        // Phase 1: Entity creation
        // Check if entity can be created without conflict
        assertNoConflict(repository.createEntity(deckSettings));
        // The entity from the database should match
        DeckSettings deckSettingsFromDb = repository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        assertEqual(deckSettings, deckSettingsFromDb);
        Thread.sleep(100);

        // Phase 2: Entity update
        now = now.plusSeconds(3600);
        // Check if entity can be updated without conflict
        deckSettings.setLastModified(now);
        assertNoConflict(repository.updateEntity(deckSettings, mapper));
        // The entity from the database should match
        deckSettingsFromDb = repository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        // The synced at will be different since record is updated/synced in server
        assertPropsNotEqual(deckSettings, deckSettingsFromDb, DeckSettings::getSyncedAt);
        deckSettings.setSyncedAt(deckSettingsFromDb.getSyncedAt());
        // The same goes with version
        assertPropsNotEqual(deckSettings, deckSettingsFromDb, DeckSettings::getVersion);
        deckSettings.setVersion(deckSettingsFromDb.getVersion());
        // The rest of the fields must match
        assertEqual(deckSettings, deckSettingsFromDb);
        Thread.sleep(100);

        // Phase 3: Entity delete
        now = now.plusSeconds(3600);
        // Check if entity can be (soft) updated without conflict
        deckSettings.setLastModified(now);
        deckSettings.setDeletedAt(now);
        assertNoConflict(repository.deleteEntity(deckSettings, mapper));
        // The entity from the database should match
        deckSettingsFromDb = repository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        // The synced at will be different since record is updated/synced in server
        assertPropsNotEqual(deckSettings, deckSettingsFromDb, DeckSettings::getSyncedAt);
        deckSettings.setSyncedAt(deckSettingsFromDb.getSyncedAt());
        // The same goes with version
        assertPropsNotEqual(deckSettings, deckSettingsFromDb, DeckSettings::getVersion);
        deckSettings.setVersion(deckSettingsFromDb.getVersion());
        // The rest of the fields must match
        assertEqual(deckSettings, deckSettingsFromDb);
    }

    @Test
    void entity_ShouldHaveConflict_ForInvalidEntity() throws InterruptedException {
        Instant now = Instant.now();
        UUID id = UUID.randomUUID();
        assertNotNull(id, "Id should not be null");
        UUID deviceId = UUID.randomUUID();
        UUID deviceId2 = UUID.randomUUID();
        DeckSettings deckSettings = DeckSettings
                .builder()
                .id(id)
                .createdAt(now)
                .lastModified(now)
                .modifiedByDeviceId(deviceId)
                .user(dummyUser)
                .build();
        DeckSettings deckSettings2 = DeckSettings
                .builder()
                .id(id)
                .createdAt(now)
                .lastModified(now)
                .modifiedByDeviceId(deviceId2)
                .user(dummyUser)
                .build();

        // Phase 1: Entity creation (Conflict detection)
        assertNoConflict(repository.createEntity(deckSettings));
        deckSettings = repository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        // Let's say someone wants to save the entity with the same id
        assertConflict(repository.createEntity(deckSettings2));
        // Assuming the conflict is resolved by dicarding deckSettings2 instead
        mapper.mapEntity(deckSettings, deckSettings2);
        Thread.sleep(100);

        // Phase 2: Entity update (Conflict detection)
        now = now.plusSeconds(3600);
        deckSettings.setMaxNewCardsPerDay(30);
        deckSettings.setLastModified(now);
        assertNoConflict(repository.updateEntity(deckSettings, mapper));
        // Let's say someone wants to update the entity with the same id
        deckSettings2.setMaxNewCardsPerDay(50);
        deckSettings2.setLastModified(now);
        assertConflict(repository.updateEntity(deckSettings2, mapper));
        // Resolve conflict by fetching latest deck settings
        deckSettings = repository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        mapper.mapEntity(deckSettings, deckSettings2);
        // Reapply previous updates
        deckSettings2.setMaxNewCardsPerDay(50);
        deckSettings2.setLastModified(now.plusSeconds(100));
        assertNoConflict(repository.updateEntity(deckSettings2, mapper));
        DeckSettings deckSettingsFromDb = repository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        mapper.mapEntity(deckSettingsFromDb, deckSettings2);
        mapper.mapEntity(deckSettingsFromDb, deckSettings);
        assertFalse(deckSettings == deckSettings2);
        Thread.sleep(100);

        // Phase 3: Entity delete (Conflict detection)
        now = now.plusSeconds(3600);
        deckSettings.setLastModified(now);
        deckSettings.setDeletedAt(now);
        assertNoConflict(repository.deleteEntity(deckSettings, mapper));
        // Let's say someone wants to delete the entity with the same id
        deckSettings2.setLastModified(now);
        deckSettings2.setDeletedAt(now);
        assertConflict(repository.deleteEntity(deckSettings2, mapper));
    }
}
