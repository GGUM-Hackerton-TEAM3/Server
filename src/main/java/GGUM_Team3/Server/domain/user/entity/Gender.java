package GGUM_Team3.Server.domain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Gender {
    MALE("남자"),
    FEMALE("여자");
    private final String name;

    public String getName() {
        return name;
    }
}