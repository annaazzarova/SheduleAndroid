package com.example.anna.shedule.application.settings;

import com.example.anna.shedule.application.settings.model.KeyValue;

import static com.example.anna.shedule.application.database.Database.getDbInstance;

public class SettingsService {

    public String getValue(String key) {
        KeyValue keyValue =getKeyValue(key);
        return (keyValue == null) ? null : keyValue.getValue();
    }

    public void setValue(String key, String value) {
        KeyValue keyValue = getKeyValue(key);
        if (keyValue == null) {
            keyValue = new KeyValue();
            keyValue.setKey(key);
        }

        keyValue.setValue(value);
        getDbInstance().save(keyValue);
    }

    public void setValue(String key, boolean value) {
        setValue(key, String.valueOf(value));
    }

    public boolean getValueAsBool(String key) {
        String value = getValue(key);
        return (value == null) ? false : Boolean.valueOf(value);
    }

    private KeyValue getKeyValue(String key) {
        String query = "SELECT * FROM " + KeyValue.TABLE_NAME + " WHERE key='" + key + "'";
        return getDbInstance().getOneByQuery(KeyValue.class, query);
    }

    public void setValueIfNotExists(String key, boolean value) {
        KeyValue keyValue = getKeyValue(key);
        if (keyValue == null) {
            keyValue = new KeyValue();
            keyValue.setKey(key);
            keyValue.setValue(String.valueOf(value));
            getDbInstance().save(keyValue);
        }
    }
}
