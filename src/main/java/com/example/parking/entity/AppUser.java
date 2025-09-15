package com.example.parking.entity;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name="app_user")
public class AppUser {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String email;
  private String name;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name="user_roles", joinColumns = @JoinColumn(name="user_id"))
  @Column(name="role")
  private Set<String> roles;

  public AppUser() {}
  public AppUser(String email, String name, Set<String> roles) {
    this.email = email; this.name = name; this.roles = roles;
  }

  public Long getId() { return id; }
  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }
  public String getName() { return name; }
  public void setName(String name) { this.name = name; }
  public Set<String> getRoles() { return roles; }
  public void setRoles(Set<String> roles) { this.roles = roles; }
}
