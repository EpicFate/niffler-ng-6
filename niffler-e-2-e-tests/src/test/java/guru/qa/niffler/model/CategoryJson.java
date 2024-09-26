package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.data.entity.spend.CategoryEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record CategoryJson(
    @JsonProperty("id")
    UUID id,
    @JsonProperty("name")
    String name,
    @JsonProperty("username")
    String username,
    @JsonProperty("archived")
    boolean archived) {

  public static CategoryJson fromEntity(CategoryEntity entity) {
    return new CategoryJson(
        entity.getId(),
        entity.getName(),
        entity.getUsername(),
        entity.isArchived()
    );
  }

    public static List<CategoryJson> fromEntities(List<CategoryEntity> entities) {
        ArrayList<CategoryJson> list = new ArrayList<>();
        for (CategoryEntity entity : entities) {
            list.add(fromEntity(entity));
        }
        return list;
    }
}
