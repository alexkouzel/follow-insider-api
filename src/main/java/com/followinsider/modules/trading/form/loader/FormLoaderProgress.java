package com.followinsider.modules.trading.form.loader;

public record FormLoaderProgress(int total, int old, int loaded, int failed) {

    public FormLoaderProgress(int total, int old, int loaded) {
        this(total, old, loaded, total - old - loaded);
    }

}
