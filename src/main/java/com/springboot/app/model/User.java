package com.springboot.app.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@Entity
@Table(name = "users")
public class User {
    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    @Column(nullable = false, updatable = false)
    private Long id;
    private String name;
//    @Transient <- used to not save column / use column in class
    private Integer age;
    private String email;

    private Boolean active;
    private LocalDateTime createDate;

    public User() {
    }

    public User(Long id, String name, String email, Boolean active, LocalDateTime createDate) {
        this.active = active;
        this.id = id;
        this.name = name;
        this.email = email;
        this.createDate = createDate;
    }

    public User(String name, String email, Boolean active,LocalDateTime createDate) {
        this.active = active;
        this.name = name;
        this.email = email;
        this.createDate = createDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", createDate=" + createDate +
                '}';
    }
}
