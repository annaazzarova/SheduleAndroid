package com.example.anna.shedule.server.cookies;

import com.example.anna.shedule.utils.ContextUtils;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CookieManager {

    public static CookieHelper getCookieHelperInstance() {
        return new CookieHelper(ContextUtils.getCacheDir().getAbsolutePath(), "cookies.private");
    }

    public static void saveCookiesToFile(CustomCookieStore cookiesStore) {
        if (cookiesStore != null){
            List<Cookie> cookies = cookiesStore.getChanges();
            saveCookiesToFile(cookies);
        }
    }

    public static CustomCookieStore getCookieFromFile() {
        CookieHelper helper = getCookieHelperInstance();
        CustomCookieStore cookieStore = new CustomCookieStore();
        helper.retrieveCookie(cookieStore);
        cookieStore.setChanged(false);
        return cookieStore;
    }

    public static void clearCookies(DefaultHttpClient httpClient) {
        CustomCookieStore cookieStore = (httpClient == null)
                ? new CustomCookieStore()
                : (CustomCookieStore) httpClient.getCookieStore();

        cookieStore.clear();
        saveCookiesToFile(cookieStore.getCookies());
    }

    private static void saveCookiesToFile(List<Cookie> cookies) {
        if (cookies != null) {
            CookieHelper helper = getCookieHelperInstance();
            helper.deleteFile();
            for (Cookie c : cookies) {
                helper.writeCookieTofile(c);
            }
        }
    }

}
