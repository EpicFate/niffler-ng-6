package guru.qa.niffler.config;

import javax.annotation.Nonnull;

enum DockerConfig implements Config {
    INSTANCE;

    @Nonnull
    @Override
    public String frontUrl() {
        return "http://frontend.niffler.dc";
    }

    @Nonnull
    @Override
    public String authUrl() {
        return "http://auth.niffler.dc:9000";
    }

    @Nonnull
    @Override
    public String authJdbcUrl() {
        return "jdbc:postgresql://127.0.0.1:5432/niffler-auth";
    }

    @Nonnull
    @Override
    public String gatewayUrl() {
        return "http://currency.niffler.dc:8091";
    }

    @Nonnull
    @Override
    public String userdataUrl() {
        return "http://userdata.niffler.dc:8089";
    }

    @Nonnull
    @Override
    public String userdataJdbcUrl() {
        return "jdbc:postgresql://127.0.0.1:5432/niffler-userdata";
    }

    @Nonnull
    @Override
    public String spendUrl() {
        return "http://frontend.niffler.dc:8093/";
    }

    @Nonnull
    @Override
    public String spendJdbcUrl() {
        return "jdbc:postgresql://127.0.0.1:5432/niffler-spend";
    }

    @Nonnull
    @Override
    public String currencyJdbcUrl() {
        return "jdbc:postgresql://127.0.0.1:5432/niffler-currency";
    }

    @Nonnull
    @Override
    public String ghUrl() {
        return "https://api.github.com/";
    }
}