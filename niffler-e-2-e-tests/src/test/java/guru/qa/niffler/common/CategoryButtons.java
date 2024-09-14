package guru.qa.niffler.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CategoryButtons {
    EDIT_CATEGORY_NAME("Edit category"),
    ARCHIVE_CATEGORY("Archive category"),
    UNARCHIVE_CATEGORY("Unarchive category");

    @Getter
    final String ariaLabel;
}
