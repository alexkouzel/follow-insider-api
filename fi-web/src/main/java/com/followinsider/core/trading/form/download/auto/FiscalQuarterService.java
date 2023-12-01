package com.followinsider.core.trading.form.download.auto;

import com.followinsider.core.trading.form.download.FormDownloaderService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FiscalQuarterService {

    private final FormDownloaderService formDownloaderService;

    private final FiscalQuarterRepository fiscalQuarterRepository;

    @PostConstruct
    public void init() {
        if (fiscalQuarterRepository.count() != 0) return;

        List<FiscalQuarter> quarters = new ArrayList<>();

        for (int[] quarterVals : generateQuarters(1993, 1, 2023, 4)) {
            quarters.add(new FiscalQuarter(quarterVals[0], quarterVals[1]));
        }
        fiscalQuarterRepository.saveAll(quarters);
    }

    @Scheduled(cron = "0 0 */2 * * ?")
    public void doEvery2Hours() {
        downloadLatestQuarter();
    }

    private void downloadLatestQuarter() {
        List<FiscalQuarter> quarters = fiscalQuarterRepository.findUnloadedAndOrder();

        if (!quarters.isEmpty()) {
            FiscalQuarter latestQuarter = quarters.get(0);
            formDownloaderService.downloadQuarter(latestQuarter);
        }
    }

    private static List<int[]> generateQuarters(int fromYear, int fromQuarter, int toYear, int toQuarter) {
        List<int[]> quarters = new ArrayList<>();

        for (int year = fromYear; year <= toYear; year++) {
            int fromIdx = year == fromYear ? fromQuarter : 1;
            int toIdx = year == toYear ? toQuarter : 4;

            for (int quarter = fromIdx; quarter <= toIdx; quarter++) {
                quarters.add(new int[]{year, quarter});
            }
        }
        return quarters;
    }

}
