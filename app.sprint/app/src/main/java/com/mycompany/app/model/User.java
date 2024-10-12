package com.mycompany.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //ColumName no va
    @Column(name = "id")
    private long id;
    @OneToOne
    @JoinColumn(name = "personnid")
    private Person personId;
    @Column(name = " username ")
    private String username;
    @Column(name = "role")
    private String rol;
    @Column(name = "password")
    private String password;
}
   