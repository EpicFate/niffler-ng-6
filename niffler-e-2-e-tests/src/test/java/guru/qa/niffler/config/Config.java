package guru.qa.niffler.config;

public interface Config {

  static Config getInstance() {
    return DockerConfig.INSTANCE;
  }

  String frontUrl();

  String spendUrl();

  String authUrl();

  String gatewayUrl();

  String userdataUrl();

  String ghUrl();
}
