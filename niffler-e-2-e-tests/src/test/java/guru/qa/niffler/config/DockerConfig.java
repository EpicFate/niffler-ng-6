package guru.qa.niffler.config;

enum DockerConfig implements Config {
    INSTANCE;

    @Override
    public String frontUrl() {
        return "http://frontend.niffler.dc";
    }

    @Override
    public String spendUrl() {
        return "http://frontend.niffler.dc:8093/";
    }

    @Override
    public String spendJdbcUrl() {
        return "jdbc:postgresql://127.0.0.1:5432/niffler-spend";
    }

    @Override
    public String currencyJdbcUrl() {
        return "";
    }

    @Override
    public String authUrl() {
        return "http://auth.niffler.dc:9000";
    }

    @Override
    public String authJdbcUrl() {
        return "";
    }

    @Override
    public String gatewayUrl() {
        return "http://currency.niffler.dc:8091";
    }

    @Override
    public String userdataUrl() {
        return "http://userdata.niffler.dc:8089";
    }

    @Override
    public String userdataJdbcUrl() {
        return "jdbc:postgresql://127.0.0.1:5432/niffler-userdata";
    }

    @Override
    public String ghUrl() {
        return "https://api.github.com/";
    }
}