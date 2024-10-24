package GGUM_Team3.Server.domain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Gender {
    MALE("여자"),
    FEMALE("남자");
    private final String name;

    public String getName() {
        return name;
    }
}