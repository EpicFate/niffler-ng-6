package guru.qa.niffler.config;

import javax.annotation.Nonnull;

enum DockerConfig implements Config {
    INSTANCE;

    @Nonnull
    @Override
    public String frontUrl() {
        return "";
    }

    @Nonnull
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
        return "";
    }

    @Nonnull
    @Override
    public String authJdbcUrl() {
        return "";
    }

    @Nonnull
    @Override
    public String authJdbcUrl() {
        return "jdbc:postgresql://127.0.0.1:5432/niffler-auth";
    }

    @Override
    public String gatewayUrl() {
        return "";
    }

    @Nonnull
    @Override
    public String userdataUrl() {
        return "";
    }

    @Nonnull
    @Override
    public String userdataJdbcUrl() {
        return "";
    }

    @Nonnull
    @Override
    public String spendUrl() {
        return "";
    }

    @Nonnull
    @Override
    public String spendJdbcUrl() {
        return "";
    }

    @Nonnull
    @Override
    public String currencyJdbcUrl() {
        return "";
    }

    @Nonnull
    @Override
    public String userdataJdbcUrl() {
        return "jdbc:postgresql://127.0.0.1:5432/niffler-userdata";
    }

    @Override
    public String ghUrl() {
        return "";
    }
}
