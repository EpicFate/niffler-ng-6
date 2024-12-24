package guru.qa.niffler.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public record SpendJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("spendDate")
        Date spendDate,
        @JsonProperty("amount")
        Double amount,
        @JsonProperty("currency")
        CurrencyValues currency,
        @JsonProperty("category")
        CategoryJson category,
        @JsonProperty("description")
        String description,
        @JsonProperty("username")
        String username) {

    public SpendJson addUsername(String username) {
        return new SpendJson(
                id,
                spendDate,
                amount,
                currency,
                new CategoryJson(
                        category.id(),
                        category.name(),
                        username,
                        category.archived()
                ),
                description,
                username
        );
    }

    public static @Nonnull SpendJson fromEntity(@Nonnull SpendEntity entity) {
        final CategoryEntity category = entity.getCategory();
        final String username = entity.getUsername();

        return new SpendJson(
                entity.getId(),
                entity.getSpendDate(),
                entity.getAmount(),
                entity.getCurrency(),
                new CategoryJson(
                        category.getId(),
                        category.getName(),
                        username,
                        category.isArchived()
                ),
                entity.getDescription(),
                username
        );
    }

    public static List<SpendJson> fromEntities(List<SpendEntity> entities) {
        ArrayList<SpendJson> list = new ArrayList<>();
        for (SpendEntity entity : entities) {
            list.add(fromEntity(entity));
        }
        return list;
    }
}
