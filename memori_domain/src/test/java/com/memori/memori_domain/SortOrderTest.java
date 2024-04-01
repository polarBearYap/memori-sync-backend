package com.memori.memori_domain;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SortOrderTest {

    private static final Map<Class<? extends SyncEntity>, Integer> sortOrderMap = new HashMap<>();

    @BeforeAll
    void setup() {
        /*
         * CardTag=1
         * DeckSettings=1
         * DeckTag=1
         * StudyOption=1
         * 
         * Deck=2
         * StudyOptionState=2
         * StudyOptionTag=2
         * 
         * Card=3
         * DeckImage=3
         * DeckReview=3
         * DeckTagMapping=3
         * StudyOptionDeck=3
         * 
         * CardHint=4
         * CardTagMapping=4
         * ReviewLog=4
         */

        sortOrderMap.put(CardTag.class, 1);
        sortOrderMap.put(DeckSettings.class, 1);
        sortOrderMap.put(DeckTag.class, 1);
        sortOrderMap.put(StudyOption.class, 1);

        sortOrderMap.put(Deck.class, 2);
        sortOrderMap.put(StudyOptionState.class, 2);
        sortOrderMap.put(StudyOptionTag.class, 2);

        sortOrderMap.put(Card.class, 3);
        sortOrderMap.put(DeckImage.class, 3);
        sortOrderMap.put(DeckReviews.class, 3);
        sortOrderMap.put(DeckTagMapping.class, 3);
        sortOrderMap.put(StudyOptionDeck.class, 3);

        sortOrderMap.put(CardHint.class, 4);
        sortOrderMap.put(CardTagMapping.class, 4);
        sortOrderMap.put(ReviewLog.class, 4);
    }

    private void testSortOrder(Class<? extends SyncEntity> clazz, int expectedSortOrder) {
        // assertTrue(clazz.builder().build().getSortOrder().equals(expectedSortOrder));
        try {
            Object builder = clazz.getMethod("builder").invoke(null);
            SyncEntity entity = (SyncEntity) builder.getClass().getMethod("build").invoke(builder);
            assertTrue(entity.getSortOrderConstant().equals(expectedSortOrder));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to create instance using builder.");
        }
    }

    @Test
    void entitySortOrder_MustCorrect() {
        sortOrderMap.forEach((clazz, sortOrder) -> {
            testSortOrder(clazz, sortOrder);
        });
    }
}
