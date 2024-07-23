package com.followinsider.modules.trading.form.loader;

import java.util.List;

public record FormLoaderProgress(int total, int filtered, int loaded, int failed) {

    public FormLoaderProgress(int total, int filtered, int loaded) {
        this(total, filtered, loaded, total - filtered - loaded);
    }

    public FormLoaderStatus getStatus() {
        return failed == 0
                ? FormLoaderStatus.FULL
                : FormLoaderStatus.PARTIAL;
    }

    public static FormLoaderProgress combine(List<FormLoaderProgress> progresses) {
        int total = 0;
        int filtered = 0;
        int loaded = 0;
        int failed = 0;

        for (FormLoaderProgress progress : progresses) {
            total += progress.total;
            filtered += progress.filtered;
            loaded += progress.loaded;
            failed += progress.failed;
        }

        return new FormLoaderProgress(total, filtered, loaded, failed);
    }

}
