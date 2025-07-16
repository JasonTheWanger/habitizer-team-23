package edu.ucsd.cse110.habitizer.lib.domain;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record Person(
        @NotNull String name,
        int age,
        @Nullable Person bestFriend
) {
    public Person {
        if (name.isBlank()) throw new IllegalArgumentException("Name must not be blank");
        if (age < 0) throw new IllegalArgumentException("Age must be non-negative");
    }

    public Person(String name, int age) {
        this(name, age, null);
    }

    public Person withName(String name) {
        return new Person(name, this.age, this.bestFriend);
    }

    public Person withAge(int age) {
        return new Person(this.name, age, this.bestFriend);
    }

    public Person withBestFriend(Person bestFriend) {
        return new Person(this.name, this.age, bestFriend);
    }
}