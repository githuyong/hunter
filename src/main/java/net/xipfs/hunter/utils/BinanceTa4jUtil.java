package net.xipfs.hunter.utils;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.binance.api.client.domain.market.CandlestickInterval;
import net.xipfs.hunter.rule.SarRule;
import org.ta4j.core.*;
import org.ta4j.core.indicators.EMAIndicator;
import org.ta4j.core.indicators.MACDIndicator;
import org.ta4j.core.indicators.ParabolicSarIndicator;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;


import com.binance.api.client.domain.market.Candlestick;
import org.ta4j.core.num.DecimalNum;
import org.ta4j.core.rules.CrossedDownIndicatorRule;
import org.ta4j.core.rules.CrossedUpIndicatorRule;
import org.ta4j.core.rules.OverIndicatorRule;
import org.ta4j.core.rules.UnderIndicatorRule;

/**
 * description: BinanceTa4jUtil <br>
 *
 * @author xie hui <br>
 * @version 1.0 <br>
 * @date 2021/7/9 17:53 <br>
 */
public class BinanceTa4jUtil {
    public static String MACD_STRATEGY = "MACD";
    public static String EMA_STRATEGY = "EMA";
    public static String SAR_STRATEGY = "SAR";

    public static BarSeries convertToBarSeries(
            List<Candlestick> candlesticks, String symbol, String period) {
        BarSeries series = new BaseBarSeries();
        for(Candlestick candlestick : candlesticks){
            series.addBar(convertToBaseBar(candlestick));
        }
        return series;
    }

    public static BaseBar convertToBaseBar(Candlestick candlestick) {
        ZonedDateTime closeTime = getZonedDateTime(candlestick.getCloseTime());
        Duration candleDuration = Duration.ofMillis(candlestick.getCloseTime()
                - candlestick.getOpenTime());
        DecimalNum openPrice = DecimalNum.valueOf(candlestick.getOpen());
        DecimalNum closePrice = DecimalNum.valueOf(candlestick.getClose());
        DecimalNum highPrice = DecimalNum.valueOf(candlestick.getHigh());
        DecimalNum lowPrice = DecimalNum.valueOf(candlestick.getLow());
        DecimalNum volume = DecimalNum.valueOf(candlestick.getVolume());

        return BaseBar.builder(DecimalNum::valueOf, Number.class).timePeriod(candleDuration).endTime(closeTime)
                .openPrice(openPrice).highPrice(highPrice).lowPrice(lowPrice).closePrice(closePrice).volume(volume)
                .build();
    }

    public static ZonedDateTime getZonedDateTime(Long timestamp) {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(timestamp),
                ZoneId.systemDefault());
    }

    public static boolean isSameBaseBar(Candlestick candlestick, BaseBar baseBar) {
        if (baseBar.getEndTime().equals(
                getZonedDateTime(candlestick.getCloseTime()))) {
            return true;
        }
        return false;
    }

    public static Strategy buildStrategy(BarSeries series, String strategyCode) {
        if (EMA_STRATEGY.equals(strategyCode)) {
            return buildEmaStrategy(series);
        }
        return null;
    }

    private static Strategy buildEmaStrategy(BarSeries series) {
        if (series == null) {
            throw new IllegalArgumentException("Series cannot be null");
        }
        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
        EMAIndicator shortEma = new EMAIndicator(closePrice, 15);
        EMAIndicator longEma = new EMAIndicator(closePrice, 30);
        Rule entryRule = new OverIndicatorRule(shortEma, longEma);
        Rule exitRule = new UnderIndicatorRule(shortEma, longEma);

        return new BaseStrategy(entryRule, exitRule);
    }

    public static Strategy buildSarStrategy(BarSeries series) {
        ParabolicSarIndicator sarIndicator = new ParabolicSarIndicator(series);
        SarRule               entryRule    = new SarRule(sarIndicator, true);
        SarRule               exitRule     = new SarRule(sarIndicator, false);
        return new BaseStrategy(entryRule, exitRule);
    }

    public static void main(String[] args) throws Exception {
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        BinanceUtil.init("LXyty1nDerKp0x9QRMXcW9YCsCbgv0h9HGxNb8C5Ysj7ov6rrSoBSGmjNrOs67Xo", "gZeHmJiRlsbZ8dMgkRIHxkgSGfQLpQOf0vQFRwmsLJ4YOlrqlK6Zrky7SnakvCvk");
        String symbol = "BTC".toUpperCase() + "USDT";
        List<Candlestick> candlesticks = BinanceUtil.getCandlestickBars(symbol, CandlestickInterval.DAILY);
        BarSeries barSeries = BinanceTa4jUtil.convertToBarSeries(candlesticks, symbol, CandlestickInterval.DAILY.getIntervalId());
        int barSize = barSeries.getBarCount();
        Strategy strategy = buildSarStrategy(barSeries);
        for (int i = 30; i > 0 ; i--) {
            int index = barSize - i;
            ZonedDateTime endTime = barSeries.getBar(index).getEndTime();
            if (strategy.shouldEnter(index)) {
                System.out.println(dtf2.format(endTime) + " ==>  IN");
            } else if (strategy.shouldExit(index)) {
                System.out.println(dtf2.format(endTime) + " ==>  OUT");
            }
        }

    }
}
