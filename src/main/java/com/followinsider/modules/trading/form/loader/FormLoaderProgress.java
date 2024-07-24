package com.followinsider.modules.trading.form.loader;

public record FormLoaderProgress(int total, int filtered, int loaded, int failed) {

    public FormLoaderProgress(int total, int filtered, int loaded) {
        this(total, filtered, loaded, total - filtered - loaded);
    }

}
