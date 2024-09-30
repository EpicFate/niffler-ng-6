package guru.qa.niffler.data.entity.auth;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class AuthUserEntity implements Serializable {
  private UUID id;
  private String username;
  private String password;
  private Boolean enabled = true;
  private Boolean accountNonExpired = true;
  private Boolean accountNonLocked = true;
  private Boolean credentialsNonExpired = true;

//  @OneToMany(fetch = EAGER, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
//  private List<AuthorityEntity> authorities = new ArrayList<>();
//
//  public void addAuthorities(AuthorityEntity... authorities) {
//    for (AuthorityEntity authority : authorities) {
//      this.authorities.add(authority);
//      authority.setUser(this);
//    }
//  }
//
//  public void removeAuthority(AuthorityEntity authority) {
//    this.authorities.remove(authority);
//    authority.setUser(null);
//  }
}
