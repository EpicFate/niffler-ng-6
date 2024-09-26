package guru.qa.niffler.model;

public enum CurrencyValues {
  RUB, USD, EUR, KZT;

  public static CurrencyValues findByName(String name) {
    return CurrencyValues.valueOf(name);
  }
}
