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
}
