import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.geo.GeoServiceImpl;
import ru.netology.i18n.LocalizationServiceImpl;
import ru.netology.sender.MessageSenderImpl;

import java.util.HashMap;

public class MessageSenderImplTest {
    @Test
    void russianText() {
        GeoServiceImpl geoService = Mockito.mock(GeoServiceImpl.class);

        String ip = "172.0.0.0";

        Mockito.when(geoService.byIp(ip))
                .thenReturn(new Location("Moscow", Country.RUSSIA, null, 0));

        LocalizationServiceImpl localizationService = Mockito.mock(LocalizationServiceImpl.class);
        Mockito.when(localizationService.locale(Country.RUSSIA))
                .thenReturn("Сообщение на русском языке");

        MessageSenderImpl messageSender = new MessageSenderImpl(geoService, localizationService);

        HashMap mapRus = new HashMap();
        mapRus.put(MessageSenderImpl.IP_ADDRESS_HEADER, ip);

        String preferences = messageSender.send(mapRus);
        String expected = "Сообщение на русском языке";

        Assertions.assertEquals(expected, preferences);

    }

    @Test
    void englishText() {
        GeoServiceImpl geoService = Mockito.mock(GeoServiceImpl.class);

        String ip = "96.0.0.0";

        Mockito.when(geoService.byIp(ip))
                .thenReturn(new Location("New York", Country.USA, null, 0));

        LocalizationServiceImpl localizationService = Mockito.mock(LocalizationServiceImpl.class);
        Mockito.when(localizationService.locale(Country.USA))
                .thenReturn("The message is in English");

        MessageSenderImpl messageSender = new MessageSenderImpl(geoService, localizationService);

        HashMap mapENG = new HashMap();
        mapENG.put(MessageSenderImpl.IP_ADDRESS_HEADER, ip);

        String preferences = messageSender.send(mapENG);
        String expected = "The message is in English";

        Assertions.assertEquals(expected, preferences);
    }

    @Test
    void byIpText() {
        String ip = "96.0.0.0";
        Location expected = new Location("New York", Country.USA, "Big", 0);
        GeoService geoService = new GeoServiceImpl();
        Location preferences = geoService.byIp(ip);

        Assertions.assertEquals(expected.getCountry(), preferences.getCountry());
    }

    @Test
    void checkingTheReturnedTextRussia() {
        String expected = "Добро пожаловать";
        String preferences = new LocalizationServiceImpl().locale(Country.RUSSIA);

        Assertions.assertEquals(expected, preferences);
    }

    @Test
    void checkingTheReturnedTextUsa() {
        String expected = "Welcome";
        String preferences = new LocalizationServiceImpl().locale(Country.USA);

        Assertions.assertEquals(expected, preferences);
    }
}
