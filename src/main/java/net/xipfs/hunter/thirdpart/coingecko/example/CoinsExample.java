package net.xipfs.hunter.thirdpart.coingecko.example;

import net.xipfs.hunter.thirdpart.coingecko.CoinGeckoApiClient;
import net.xipfs.hunter.thirdpart.coingecko.constant.Currency;
import net.xipfs.hunter.thirdpart.coingecko.domain.Coins.CoinData.DeveloperData;
import net.xipfs.hunter.thirdpart.coingecko.domain.Coins.CoinData.IcoData;
import net.xipfs.hunter.thirdpart.coingecko.domain.Coins.CoinFullData;
import net.xipfs.hunter.thirdpart.coingecko.domain.Coins.CoinList;
import net.xipfs.hunter.thirdpart.coingecko.domain.Coins.CoinMarkets;
import net.xipfs.hunter.thirdpart.coingecko.domain.Coins.CoinTickerById;
import net.xipfs.hunter.thirdpart.coingecko.impl.CoinGeckoApiClientImpl;

import java.util.List;

public class CoinsExample {
    public static void main(String[] args) {
        String OMGContract = "0xd26114cd6EE289AccF82350c8d8487fedB8A0C07";
        String platform = "ethereum";

        CoinGeckoApiClient client = new CoinGeckoApiClientImpl();

        List<CoinList> coinList = client.getCoinList();
        System.out.println(coinList);

        long totalCoins = coinList.size();
        System.out.println(totalCoins);

        List<CoinMarkets> coinMarkets = client.getCoinMarkets(Currency.USD);
        System.out.println(coinMarkets);

        CoinFullData bitcoinInfo = client.getCoinById("bitcoin");
        System.out.println(bitcoinInfo);

        String genesisDate = bitcoinInfo.getGenesisDate();
        System.out.println(genesisDate);


        DeveloperData bitcoinDevData = bitcoinInfo.getDeveloperData();
        System.out.println(bitcoinDevData);

        long bitcoinGithubStars = bitcoinDevData.getStars();
        System.out.println(bitcoinGithubStars);

        CoinTickerById bitcoinTicker = client.getCoinTickerById("bitcoin");
        System.out.println(bitcoinTicker);

        CoinFullData omiseGoInfo = client.getCoinInfoByContractAddress(platform, OMGContract);
        System.out.println(omiseGoInfo);

        IcoData omiseGoIcoInfo = omiseGoInfo.getIcoData();
        String icoStartDate = omiseGoIcoInfo.getIcoStartDate();
        System.out.println(icoStartDate);

    }
}
