package guru.qa.niffler.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ModalButtons {

    UNARCHIVE("Unarchive"),
    ARCHIVE("Archive");

    @Getter
    final String buttonName;
}
