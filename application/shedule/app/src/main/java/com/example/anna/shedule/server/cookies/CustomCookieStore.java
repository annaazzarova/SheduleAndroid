package com.example.anna.shedule.server.cookies;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;

import java.util.Date;
import java.util.List;

public class CustomCookieStore extends BasicCookieStore {

    private boolean changed = false;

    @Override
    public synchronized void addCookie(Cookie cookie) {
        changed |= true;
        super.addCookie(cookie);
    }

    @Override
    public synchronized void addCookies(Cookie[] cookies) {
        changed |= true;
        super.addCookies(cookies);
    }

    @Override
    public synchronized void clear() {
        changed |= true;
        super.clear();
    }

    @Override
    public synchronized boolean clearExpired(Date date) {
        boolean cleared = super.clearExpired(date);
        changed |= cleared;
        return cleared;
    }

    public synchronized boolean isChanged() {
        boolean oldState = changed;
        changed = false;
        return oldState;
    }

    public synchronized void setChanged(boolean isChanged){
        this.changed = isChanged;
    }

    public synchronized List<Cookie> getChanges() {
        List<Cookie> cookies = null;
        if (changed) {
            changed = false;
            cookies = getCookies();
        }
        return cookies;
    }
}