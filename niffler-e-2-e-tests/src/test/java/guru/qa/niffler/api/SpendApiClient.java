package guru.qa.niffler.api;

import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ParametersAreNonnullByDefault
public class SpendApiClient extends RestClient {

    private final SpendApi spendApi;

    public SpendApiClient() {
        super(CFG.spendUrl());
        this.spendApi = retrofit.create(SpendApi.class);
    }

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Config.getInstance().spendUrl())
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final SpendApi spendApi = retrofit.create(SpendApi.class);

    @Nonnull
    public SpendJson createSpend(SpendJson spend) {
        return executeSpend(spendApi -> spendApi.addSpend(spend));
    }

    @Nonnull
    public SpendJson editSpend(SpendJson spend) {
        return executeSpend(spendApi -> spendApi.editSpend(spend));
    }

    @Nonnull
    public SpendJson getSpend(String id, String username) {
        return executeSpend(spendApi -> spendApi.getSpend(id, username));
    }

    @Nonnull
    public List<SpendJson> allSpends(String username,
                                     @Nullable CurrencyValues currency,
                                     @Nullable String from,
                                     @Nullable String to) {
        final Response<List<SpendJson>> response;
        try {
            response = spendApi.allSpends(username, currency, from, to)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body() != null
                ? response.body()
                : Collections.emptyList();
    }

    @Nonnull
    public List<SpendJson> getSpends(String username, CurrencyValues filterCurrency, Date from, Date to) {
        return executeSpendAll(spendApi -> spendApi.getSpends(username, filterCurrency, from, to));
    }

    public void deleteSpends(String username, List<String> ids) {
        executeSpendRemove(spendApi -> spendApi.deleteSpends(username, ids));
    }

    @Nonnull
    public CategoryJson createCategory(CategoryJson category) {
        return executeCategory(spendApi -> spendApi.addCategory(category));
    }

    @Nonnull
    public CategoryJson updateCategory(CategoryJson category) {
        return executeCategory(spendApi -> spendApi.updateCategory(category));
    }

    @Nonnull
    public List<CategoryJson> getCategories(String username, boolean excludeArchived) {
        return executeGetCategories(spendApi -> spendApi.getCategories(username, excludeArchived));
    }

    @Nonnull
    public List<CategoryJson> allCategory(String username) {
        final Response<List<CategoryJson>> response;
        try {
            response = spendApi.allCategories(username)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body() != null
                ? response.body()
                : Collections.emptyList();
    }

    public List<CategoryJson> executeGetCategories(Function<SpendApi, Call<List<CategoryJson>>> function) {
        final Response<List<CategoryJson>> response;
        try {
            response = function.apply(spendApi)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    public CategoryJson executeCategory(Function<SpendApi, Call<CategoryJson>> function) {
        final Response<CategoryJson> response;
        try {
            response = function.apply(spendApi)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    public void executeSpendRemove(Function<SpendApi, Call<Void>> function) {
        final Response<Void> response;
        try {
            response = function.apply(spendApi)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
    }

    public List<SpendJson> executeSpendAll(Function<SpendApi, Call<List<SpendJson>>> function) {
        final Response<List<SpendJson>> response;
        try {
            response = function.apply(spendApi)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    private SpendJson executeSpend(Function<SpendApi, Call<SpendJson>> function) {
        final Response<SpendJson> response;
        try {
            response = function.apply(spendApi)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(201, response.code());
        return response.body();
    }
}
