package com.example.msaccount.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "admin_account")
@Getter
@Setter
public class AdminAccount {

    @Id
    private int account_id;

    @OneToOne
    @MapsId
    private Account account;

//    @ManyToMany
//    @JoinTable(name = "admin_approved_posts",
//            joinColumns = @JoinColumn(name = "admin_account_id"),
//            inverseJoinColumns = @JoinColumn(name = "post_id"))
//    private List<Post> approvedPosts; // Change type to List<Post>
}
