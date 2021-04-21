package hu.lanoga.flatshares.service;

/**
 * Changelly integration
 * https://old.changelly.com/developer
 * <p>
 * Here is simple use case of our exchange API:
 * <p>
 * API — get available at the current moment list of currencies with getCurrencies or getCurrenciesFull method;
 * GUI — ask user for currency pair he wants to exchange. For example, it can be LTC (Litecoin) to ETH (Ethereum);
 * API — get minimum exchangeable amount for selected currency pair with getMinAmount method;
 * GUI — ask user for the amount to exchange;
 * API — call getExchangeAmount method to get estimated ETH amount after exchange;
 * GUI — show an estimated amount to user and ask for confirmation;
 * GUI — ask user for his wallet address to send coins after exchange;
 * API — call createTransaction method to get the LTC address to which user should send his funds;
 * GUI — ask user to send LTC coins to the address for exchange;
 * User sends LTC. We receive LTC and exchange it for ETH. We send ETH to the address that was submitted to createTransaction method;
 * Via socket.io API functions you can get the user's transaction status online;
 * Via getTransactions method you can get all the transactions history.
 */
public interface ChangellyService {

    /**
     * Get available at the current moment list of currencies with getCurrencies method
     */
    void getCurrencies();

    /**
     * Get available at the current moment list of currencies with getCurrenciesFull method
     */
    void getCurrenciesFull();

    /**
     * Get minimum exchangeable amount for selected currency pair
     */
    void getMinAmount();

    /**
     * Call getExchangeAmount method to get estimated ETH amount after exchange
     */
    void getExchangeAmount();

    /**
     * Call createTransaction method to get the LTC address to which user should send his funds;
     */
    void createTransaction();
}
