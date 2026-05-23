package com.nonfollowers.finder.model;

public class User {

    private final String username;

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return username != null ? username.equalsIgnoreCase(user.username) : user.username == null;
    }

    @Override
    public int hashCode() {
        return username != null ? username.toLowerCase().hashCode() : 0;
    }
}
